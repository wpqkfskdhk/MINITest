package UTIL;

import DAO.SQL;
import DTO.Characters;
import MAIN.GameMain;

import java.util.Random;
import java.util.Scanner;

import static UTIL.ColorUtil.RESET;
import static UTIL.ColorUtil.YELLOW;

public class GameMainUtil {
    private final Scanner sc = new Scanner(System.in);
    private final SQL sql = new SQL();
    private String loggedInPlayerId; // 로그인한 플레이어 ID 저장
    private String currentSavePoint; // 저장된 시점
    private Characters character; // 현재 플레이 중인 캐릭터 객체
    private boolean gameRunning = true;
    private GameMain gameMain;  // GameMain 객체 참조 추가
    private boolean hasFood = false; // 식량 여부를 저장하는 플래그 (전역 변수로 선언)

    public GameMainUtil(GameMain gameMain) {
        this.gameMain = gameMain;  // 생성자를 통해 GameMain 객체 전달받기
    }

    // 캐릭터 설정 메서드 (불러온 캐릭터를 설정)
    public void setCharacter(Characters character) {
        this.character = character;
    }

    // GameMainUtil 클래스에서 체력 변화 후 DB 업데이트
    private void updateCharacterHP(int hpChange) {
        int newHP = character.getHP() + hpChange;  // 체력 변화 적용
        if (newHP > 10) {
            newHP = 10;  // 체력이 10을 넘지 않도록 처리
        } else if (newHP < 0) {
            newHP = 0;  // 체력이 0 미만이 되지 않도록 처리
        }

        character.setHP(newHP);  // 캐릭터 객체의 HP 업데이트

        // 경고 출력 로직 추가
        if (newHP >= 8 && newHP < 10) {
            System.out.println("경고: 피로도가 위험 수준에 도달했습니다! 현재 피로도: " + newHP);
        } else if (newHP == 10) {
            System.out.println("경고: 피로도가 10에 도달했습니다! 당신은 사망했습니다.");
            gameOver();  // 사망 처리
        }
    }

    // 선택지 처리 함수 통합
    private int handleChoices(String message, String... options) {
        return GameUtil.getChoice(sc, message, options);
    }

    // 스토리 순서 ( Intro 스토리 스킵 기능 추가)
    public void storySequence() {
        // Intro 1
        StoryUtil.intro();
        int choice = handleChoices("\n====== 스토리 계속 보기 =======", "계속", "SKIP");
        if (choice == 2) {
            weaponChoice();  // SKIP 선택 시 무기 선택으로 스토리로이동
            return;
        }

        // Intro 2
        StoryUtil.intro2();
        choice = handleChoices("\n====== 스토리 계속 보기 =======", "계속", "SKIP");
        if (choice == 2) {
            weaponChoice();  // SKIP 선택 시 무기 선택으로 스토리로이동
            return;
        }

        // Intro 3
        StoryUtil.intro3();
        choice = handleChoices("\n====== 스토리 계속 보기 =======", "계속", "SKIP");
        if (choice == 2) {
            weaponChoice();  // SKIP 선택 시 무기 선택으로 스토리로이동
        } else {
            weaponChoice();  // 마지막으로 무기 선택스토리로 이동
        }
    }

    // 플레이어 ID 설정 메서드 (로그인 후 ID를 설정할 수 있게 함)
    public void setLoggedInPlayerId(String loggedInPlayerId) {
        this.loggedInPlayerId = loggedInPlayerId;
    }

    public String getLoggedInPlayerId() {
        return loggedInPlayerId;
    }

    // 게임 시작
    public void startGame() {
        if (loggedInPlayerId == null) {
            System.out.println("로그인한 플레이어가 없습니다. 로그인을 먼저 해주세요.");
            return;
        }
        PrintUtil.titleArt();

        PrintUtil.hello();                 // 게임 인사말
        PrintUtil.redLine();

        System.out.println("캐릭터 이름을 설정하세요: ");
        String charName = sc.nextLine(); // 캐릭터 이름 설정
        createCharacter(charName);       // 캐릭터 생성 및 DB 저장

        System.out.println("이제 게임을 시작합니다!");
        PrintUtil.printLine();

        // 본격 스토리 시작
        storySequence();                    // 인트로 스토리 스킵 기능 추가
    }

    private void createCharacter(String charName) {
        // 이미 생성된 캐릭터 수를 확인
        int characterCount = sql.getCharacterCountForPlayer(loggedInPlayerId);
        if (characterCount >= 3) {
            System.out.println("한 ID당 최대 3개의 캐릭터만 생성할 수 있습니다.");
            // 캐릭터가 3개 이상일 경우, 메뉴로 돌아가기
            gameMain.gameMenu(); // 로그인 후 메뉴로 다시 돌아가도록 호출
            return;
        }

        int charId = sql.characterNumber();  // 캐릭터 ID 생성
        character = new Characters();        // 생성한 캐릭터를 this.character에 저장
        character.setCharId(charId);
        character.setCharName(charName);
        character.setHP(4); // 초기 피로도(HP) 4로 지정
        character.setpId(loggedInPlayerId);

        // 캐릭터를 DB에 저장
        sql.createCharacter(character);  // 캐릭터를 먼저 DB에 저장

        // 인벤토리를 생성하여 캐릭터와 연동
        int invenId = sql.createInventory(charId);  // 캐릭터의 인벤토리 생성
        character.setInvenId(invenId);  // 생성된 인벤토리 ID를 캐릭터에 설정

        System.out.println("캐릭터 이름이 " + charName + "으로 설정되었습니다.");
    }

    // 저장된 시점에서 게임을 이어서 진행
    public void resumeFromSavePoint(String saveData) {
        System.out.println("저장된 시점에서 게임을 이어서 진행합니다.");

        // 저장된 데이터를 ','로 구분하여 분리
        String[] saveDataParts = saveData.split(",");
        currentSavePoint = saveDataParts[0]; // 저장된 시점

        // 기본적으로 hasFood는 false로 설정
        hasFood = false;

        // 저장된 데이터에 hasFood 정보가 있으면 처리
        if (saveDataParts.length > 1 && saveDataParts[1].equals("hasFood=true")) {
            hasFood = true;
        }

        // 시점에 맞는 로직을 처리
        switch (currentSavePoint) {
            case "weaponChoice":
                weaponChoice();
                break;
            case "hallway":
                hallwayStory();
                break;
            case "nextEscape":
                nextEscape();
                break;
            case "chooseTransport":
                chooseTransport();
                break;
            case "handleBikeSituation":
                handleBikeSituation();
                break;
            case "convenienceStore":
                convenienceStore();
                break;
            case "nextSleepOrMove":
                // 편의점에서 아이템 초기화 후 nextSleepOrMove 시작
                sql.resetInventory2(character.getCharId()); // 아이템 초기화 ( 첫번째 선택지에서 고른 무기빼고)

                character.increaseHP(-1);                           // 피로도 다시 돌려놓기
                convenienceStore(); // 편의점으로 이동하여 다시 진행
                break;
            case "finalDirection":
                finalDirection();
                break;
            case "shelter":
                shelter();
                break;
            default:
                System.out.println("알 수 없는 저장 포인트입니다.");
        }
    }


    // 게임 저장 (원본)

    private void saveGame() {
        System.out.println();
        System.out.println("게임을 저장하시겠습니까?");
        System.out.println("[1] 저장 | [2] 취소");
        int choice = sc.nextInt();
        sc.nextLine();  // 버퍼 비우기
        if (choice == 1) {
            sql.updateCharacterHP(character);
            sql.saveGame(loggedInPlayerId, character.getCharId(), currentSavePoint); // 저장 지점과 함께 저장
            System.out.println("게임이 저장되었습니다.");
        } else {
            System.out.println("저장을 취소하였습니다.");
        }
    }

    // 게임 불러오기
    public void loadGame(String pId, int charId) {
        // SQL에서 저장된 게임 데이터를 불러옴
        String saveData = sql.loadGame(pId, charId);

        if (saveData != null) {
            System.out.println("저장된 게임을 불러옵니다...");

            // 저장된 시점과 상태를 기반으로 게임을 이어서 진행
            resumeFromSavePoint(saveData);
        } else {
            // 저장된 게임이 없을 때 새로운 게임 시작
            System.out.println("저장된 게임이 없습니다.");
            System.out.println("새 게임을 시작합니다.");

            // 캐릭터를 초기 상태로 초기화
            resetCharacterToStart(charId);
            // HP를 초기 상태로 설정 (4로 초기화)
            character.setHP(4);
            // 새로운 게임 스토리 진행 (다시 이름 설정은 하지 않음)
            storySequence();
        }
    }


    // 캐릭터를 처음 상태로 초기화하는 메서드
    public void resetCharacterToStart(int charId) {
        // 캐릭터에 이미 인벤토리가 연결되어 있는지 확인
        int existingInvenId = sql.getInventoryIdForCharacter(charId);
        if (existingInvenId != -1) {
            character.setInvenId(existingInvenId);  // 기존 인벤토리 설정
        } else {
            // 인벤토리가 없을 경우 새로 생성
            int invenId = sql.createInventory(charId);  // 인벤토리 생성
            character.setInvenId(invenId);  // 생성된 인벤토리 ID를 캐릭터에 설정
        }
        character.setHP(4);  // 기본 HP를 4로 설정 (처음 시작 상태)
        sql.updateCharacterHP(character);  // DB에 체력 정보 초기화
        System.out.println("캐릭터가 초기화되었습니다. 처음부터 시작합니다.");
    }


    // 무기 선택 (저장 가능)
    public void weaponChoice() {
        currentSavePoint = "weaponChoice"; // 현재 시점 저장
        StoryUtil.weaponChoice(); // 무기 선택 대화 출력

        // 시작무기 선택
        int weaponChoice = handleChoices("\n 어느 것을 가져갈까?", "식칼", "알루미늄 배트", "저장");

        // 캐릭터 고유번호 불러오기 추가
        int charId = character.getCharId();

        // 아이템 고유번호 변수 추가
        int itemId = 0;

        switch (weaponChoice) {
            case 1:
                itemId = 1;
                sql.addItemToInventory(itemId, charId);         // 아이템 획득, '식칼' 인벤토리에 저장 추가
                StoryUtil.knifeChoice(character.getCharName()); // 캐릭터 이름과 함께 출력
                gameOver();
                break;
            case 2:
                itemId = 2;
                sql.addItemToInventory(itemId, charId);         // 아이템 획득, '알루미늄 배트' 인벤토리에 저장 추가

                StoryUtil.batChoice(character.getCharName());   // 캐릭터 이름과 함께 출력
                hallwayStory();          // 복도 스토리 진행

                break;
            case 3:
                saveGame(); // 저장 기능
                break;
            default:
                System.out.println("잘못된 입력입니다. 다시 선택하세요.");
        }
    }


    // 복도 스토리 진행
    private void hallwayStory() {
        currentSavePoint = "hallway";  // 복도 저장 포인트


        int choice = handleChoices("\n복도로 나왔습니다. 좀비 1마리를 마주쳤습니다.", "공격한다", "도망간다", "저장");

        switch (choice) {
            case 1:
                StoryUtil.hallwayAttack();         // 복도에서 좀비 공격 선택
                character.increaseHP(2);            // 피로도 증가
                nextEscape();  // 탈출 시나리오로 이동
                break;
            case 2:
                StoryUtil.hallwayRun();            // 복도에서 도망 선택
                gameOver();
                break;
            case 3:
                saveGame();                         // 복도에서 저장 가능
                break;
            default:
                System.out.println("잘못된 입력입니다. 다시 선택하세요.");
        }
    }

    // 탈출 시나리오
    private void nextEscape() {
        currentSavePoint = "nextEscape";  // 탈출 저장 포인트


        int choice = handleChoices("\n 다음 선택지 입니다.\n계단에서 어디로 갈 것입니까?", "옥상으로 간다", "지상으로 내려간다", "저장");

        switch (choice) {
            case 1:
                StoryUtil.roofChoice(character.getCharName()); // 옥상 선택 (사망 루트)
                gameOver();
                break;
            case 2:
                StoryUtil.groundChoice(character.getCharName()); // 지상 선택 (생존 루트)
                character.increaseHP(1);             // 피로도 1 증가
                chooseTransport();                   // 이동수단 선택
                break;
            case 3:
                saveGame(); // 저장
                break;
            default:
                System.out.println("잘못된 입력입니다. 다시 선택하세요.");
        }
    }

    // 이동 수단 선택
    private void chooseTransport() {
        currentSavePoint = "chooseTransport";                  // 이동 수단 저장 포인트
        StoryUtil.chooseTransport();                           // 이동수단 선택 대화 출력
        int choice = handleChoices("\n대피소로 가기 위한 이동수단을 선택하세요:", "걷기", "자전거", "자동차", "저장");

        switch (choice) {
            case 1:
                StoryUtil.walkChoice(character.getCharName());  // 걷기 선택


                character.increaseHP(1);                 // 피로도 1 증가
                convenienceStore();                             // 편의점 스토리 진행
                break;
            case 2:
                StoryUtil.bikeChoice(character.getCharName());          // 자전거 선택

                character.increaseHP(1);                                // 피로도 1 증가
                handleBikeSituation();                                   // 자전거 상황 처리
                break;
            case 3:
                StoryUtil.carChoice(character.getCharName());   // 자동차 선택 (사망 루트)
                gameOver();
                break;
            case 4:
                saveGame();// 저장
                break;
            default:
                System.out.println("잘못된 입력입니다. 다시 선택하세요.");
        }
    }


    // 자전거 상황 처리
    private void handleBikeSituation() {
        currentSavePoint = "handleBikeSituation"; // 자전거 상황 저장 포인트
        StoryUtil.handleBikeSituation();          // 자전거 상황 선택 대화 출력
        int choice = handleChoices("\n자전거를 계속 타고 갈 것입니까, 도망갈 것입니까?", "자전거를 계속 탄다", "자전거를 버리고 도망간다", "저장");

        switch (choice) {
            case 1:
                StoryUtil.bicycle_choice1(character.getCharName()); // 자전거 계속 타기 (사망 루트)
                gameOver();
                break;
            case 2:
                StoryUtil.bicycle_choice2(character.getCharName()); // 자전거 버리기 (사망 루트)
                gameOver();
                break;
            case 3:
                saveGame(); // 저장
                break;
            default:
                System.out.println("잘못된 입력입니다. 다시 선택하세요.");
        }
    }


    // 편의점 상황 처리
    private void convenienceStore() {
        currentSavePoint = "convenienceStore"; // 편의점 저장 포인트
        StoryUtil.convenienceStore(); // 편의점 상황 선택 대화 출력
        int choice = handleChoices("\n편의점에 들어가시겠습니까, 지나치시겠습니까?", "들어간다", "지나친다", "저장");

        switch (choice) {
            case 1:
                int charId = character.getCharId();                // 인벤토리 위한 캐릭터 고유번호 불러오기 추가

                StoryUtil.store_choice1(character.getCharName());  // 편의점 들어간다

                int itemIdWater = 3;
                sql.addItemToInventory(itemIdWater, charId);       // 아이템 획득, 인벤토리에 '생수' 저장 추가

                int itemIdFood = 4;
                sql.addItemToInventory(itemIdFood, charId);        // 아이템 획득, 인벤토리에 '식량' 저장 추가

                character.increaseHP(1);                           // 피로도 1 증가

                nextSleepOrMove(true); // 식량을 획득했으므로 true 전달

                break;

            case 2:
                StoryUtil.store_choice2(character.getCharName()); // 편의점 지나친다
                character.increaseHP(1);                          // 피로도 1 증가
                nextSleepOrMove(false); // 식량을 얻지 못했으므로 false 전달
                break;
            case 3:
                saveGame();                 // 저장
                break;
            default:
                System.out.println("잘못된 입력입니다. 다시 선택하세요.");
        }
    }



    // 휴식 또는 이동 선택 수정중
    private void nextSleepOrMove(boolean hasFood) {
        currentSavePoint = "nextSleepOrMove"; // 휴식/이동 저장 포인트

        int charId = character.getCharId(); // 캐릭터 ID를 불러오기

        if (hasFood) {
            // 식량이 있는 경우 선택지에 "식량을 먹고 잔다" 포함
            int choice = handleChoices("\n다음 선택을 하십시오:", "식량을 먹고 잔다", "그냥 잔다", "계속 이동", "저장");

            switch (choice) {
                case 1:
                    StoryUtil.sleepOrContinue(true); // 식량을 먹고 잔다
                    sql.useItemInInventory(3, charId); // 물 사용
                    sql.useItemInInventory(4, charId); // 식량 사용
                    updateCharacterHP(-2); // 피로도 2 감소
                    finalDirection(); // 다음 스토리로 이동
                    break;

                case 2:
                    StoryUtil.sleepOrContinue(false); // 그냥 잔다
                    updateCharacterHP(-1); // 피로도 1 감소
                    finalDirection(); // 다음 스토리로 이동
                    break;

                case 3:
                    StoryUtil.subwayChoice(character.getCharName()); // 계속 이동 (사망 루트)
                    gameOver();
                    break;

                case 4:
                    saveGame(); // 저장만 수행, 상태 변화 없음
                    break;

                default:
                    System.out.println("잘못된 입력입니다. 다시 선택하세요.");
            }
        } else {
            // 식량이 없는 경우 "식량을 먹고 잔다"를 제외한 선택지
            int choice = handleChoices("\n다음 선택을 하십시오:", "그냥 잔다", "계속 이동", "저장");

            switch (choice) {
                case 1:
                    StoryUtil.sleepOrContinue(false); // 그냥 잔다
                    updateCharacterHP(-1); // 피로도 1 감소
                    finalDirection(); // 다음 스토리로 이동
                    break;

                case 2:
                    StoryUtil.subwayChoice(character.getCharName()); // 계속 이동 (사망 루트)
                    gameOver();
                    break;

                case 3:
                    saveGame(); // 저장만 수행, 상태 변화 없음
                    break;

                default:
                    System.out.println("잘못된 입력입니다. 다시 선택하세요.");
            }
        }
    }


    // 이벤트가 연속으로 발생하는 상황을 막기위해서 넣었어요
    private boolean eventSave = false; // 이벤트 발생 여부를 저장

    // 랜덤 이벤트 발생
    public void chanceEncounter() {

        // 이벤트가 이미 발생했으면 더 이상 발생하지 않도록 처리
        if (eventSave) {
            return;
        }


        boolean chance = Math.random() < 0.5; //30%

        if (chance) {

            System.out.println(YELLOW + "!!이벤트 발생!!" + RESET);      // 확인을 위한 이벤트 발생 메세지, 불필요시 삭제

            Random rand = new Random();

            int encounter = 1 + rand.nextInt(3);  // 1부터 3 사이의 무작위 숫자 선택


            switch (encounter) {
                case 1:
                    // 식량얻기 이벤트 (선택)
                    foodChance();
                    break;
                case 2:
                    // 피로회복 이벤트 (선택)
                    restChance();
                    break;
                case 3:
                    // 무기얻기 이벤트; 선택지 없음
                    int charId = character.getCharId();
                    int itemId = 5;

                    StoryUtil.randomWeaponChance();
                    sql.addItemToInventory(itemId, charId);
                default:
                    break;
            }

            // 이벤트가 발생했으므로 플래그 설정
            eventSave = true;
            // 이벤트 발생하지않았다는 메세지는 생략
        }

    }

    // 이벤트 발생 가능 상태로 재설정
    public void resetEvent() {
        eventSave = false;
    }

    // 식량얻기 이벤트
    public void foodChance() {

        int charId = character.getCharId();

        boolean event = true;
        while (event) {

            int choice = handleChoices("\n이동 중에 바닥에 반짝거리는 무언가가 눈에 들어온다.\n확인해 볼까?", "확인한다", "무시한다");

            if (choice == 1) {
                StoryUtil.randomFoodChance(true);
                int itemId = 4;
                sql.addItemToInventory(itemId, charId);
                break;
            } else if (choice == 2) {
                StoryUtil.randomFoodChance(false);
                break;
            } else {
                System.out.println("잘못 입력하였습니다.");
            }
        }


    }

    // 피로회복 이벤트
    public void restChance() {
        boolean event = true;

        while (event) {
            int choice = handleChoices("\n쉼 없이 걷다 보니 피로가 좀 쌓였다.\n계속 가도 문제는 없을 것 같지만, 잠깐 숨 좀 고르는 것도 나쁘지 않겠지\n주위에 좀비가 안 보이기도 하고. \n 어떻게 할까?", "쉰다", "계속 간다");
            if (choice == 1) {
                StoryUtil.randomRestChance(true);
                updateCharacterHP(-2);
                break;
            } else if (choice == 2) {
                StoryUtil.randomFoodChance(false);
                break;
            } else {
                System.out.println("잘못 입력하였습니다.");
            }
        }

    }

    // 마지막 이동 방향 선택 (도로 vs 지하철)
    private void finalDirection() {
        currentSavePoint = "finalDirection"; // 최종 이동 저장 포인트

        int choice = handleChoices("\n다음날 아침입니다. 어디로 이동하시겠습니까?", "도로로 간다", "지하철로 간다", "저장");

        switch (choice) {
            case 1:
                StoryUtil.roadChoice();            // 도로 선택
                character.increaseHP(1);    // 피로도 1 증가

                chanceEncounter();                 // 랜덤 이벤트 발생

                shelter();                         // 대피소로 이동
                break;
            case 2:
                StoryUtil.subwayChoice(character.getCharName()); // 지하철 선택 (사망 루트)
                gameOver();
                break;
            case 3:
                saveGame();  // 저장
                break;
            default:
                System.out.println("잘못된 입력입니다. 다시 선택하세요.");
        }
    }

    // 대피소 상황 처리
    private void shelter() {
        currentSavePoint = "shelter"; // 대피소 저장 포인트

        int choice = handleChoices("\n대피소에 접근합니다. 어떻게 이동하시겠습니까?", "뛰어간다", "걸어간다", "저장");

        switch (choice) {
            case 1:
                StoryUtil.shelterApproach(true);  // 뛰어간다 (사망 루트)
                gameOver();
                break;
            case 2:
                StoryUtil.shelterApproach(false); // 걸어간다 (생존 루트)
                PrintUtil.demo();                 // 데모 종료 메시지
                break;
            case 3:
                saveGame();                       // 저장
                break;
            default:
                System.out.println("잘못된 입력입니다. 다시 선택하세요.");
        }
    }

    // 게임 종료 처리
    private void gameOver() {
        PrintUtil.gameOver();
        gameRunning = false;
    }
}
