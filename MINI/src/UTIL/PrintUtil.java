package UTIL;

import static UTIL.ColorUtil.*;

public class PrintUtil {
    // 슬로우 print
    public static void slowPrint(String message, long millisPerChar, String color) {
        message = ColorUtil.colorize(message, color); // 색상 적용
        for (char c : message.toCharArray()) {
            System.out.print(c);
            try {
                Thread.sleep(millisPerChar);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

    // 화면 클리어 시각적 효과
    public static void printClear() {
        for (int i = 0; i < 50; ++i) System.out.println();
    }

    // 라인
    public static void printLine() {
        slowPrint(GREEN + "════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════\n", 5, ColorUtil.RESET);
    }

    // 게임 로고(타이틀)
    public static void logo() {
        System.out.println(RED + " ______                ____                                               __                   __                     ");
        System.out.println(RED + "/\\  ___\\              /\\  _`\\                                            /\\ \\                 /\\ \\__                   ");
        System.out.println(RED + "\\ \\ \\__/              \\ \\ \\/\\ \\     __      __  __      ____             \\ \\ \\         __     \\ \\ ,_\\     __    _ __  ");
        System.out.println(RED + " \\ \\___``\\             \\ \\ \\ \\ \\  /'__`\\   /\\ \\/\\ \\    /',__\\             \\ \\ \\  __  /'__`\\    \\ \\ \\/   /'__`\\ /\\`'__\\");
        System.out.println(RED + "  \\/\\ \\L\\ \\             \\ \\ \\_\\ \\/\\ \\L\\._\\ \\ \\ \\_\\ \\  /\\__, `\\             \\ \\ \\L\\ \\/\\ \\L\\._\\   \\ \\ \\_ /\\  __/ \\ \\ \\/ ");
        System.out.println(RED + "   \\ \\____/              \\ \\____/\\ \\__/\\.\\_ \\/`____ \\ \\/\\____/              \\ \\____/\\ \\__/\\.\\_   \\ \\__\\\\ \\____\\ \\ \\_\\ ");
        System.out.println(RED + "    \\/___/                \\/___/  \\/__/\\/_/  `/___/> \\ \\/___/                \\/___/  \\/__/\\/_/    \\/__/ \\/____/  \\/_/ ");
        System.out.println(RED + "                                                /\\___/                                                                ");
        System.out.println(RED + "                                                \\/__/                                                                 ");
    }


    // 접속 환영
    public static void hello() {
        String[][] linesAndColors = {
                {"5 Days Later 접속을 환영합니다", RED},
                {"생존을 위한 게임을 시작하세요", RED},
                {"당신의 선택이 미래를 바꿀 수 있습니다.", RED}
        };

        for (String[] lineAndColor : linesAndColors) {
            String line = lineAndColor[0];
            String color = lineAndColor[1];
            slowPrint(line + "\n", 30, color);
        }
    }


    // 접속(시작)메뉴
    public static void printMainMenu() {
//        slowPrint(CYAN + "\n｀｀\n", 10, ColorUtil.CYAN);
        System.out.println(GREEN + "╔══════════════════════════ MENU ══════════════════════════════╗");
        System.out.println(GREEN + "║                   WELCOME TO 5 Days Later                    ║");
        System.out.println(GREEN + "     1. LOGIN                                                   ");
        System.out.println(GREEN + "     2. 회원가입                                                  ");
        System.out.println(GREEN + "     3. 설정                                                 ");
        System.out.println(GREEN + "     0. 종료                                                     ");
        System.out.println(GREEN + "╚══════════════════════════════════════════════════════════════╝" + BLACK);

    }

    // 소개
    public static void gameIntroduction() {
        slowPrint("\n[게임 소개]\n‘5 Days Later’는 ~~~~~~~~~ .\r즐겜!\n", 20, ColorUtil.BLUE);
    }

    // 게임오버 (사망)
    public static void gameOver() {
        // slowPrint("\n사망하였습니다. \n", 10, RED);
        // slowPrint("[ GAME OVER ]\n", 10, RED);
        System.out.println(RED + " ▄████  ▄▄▄      ███▄ ▄███ ▓▓█████   ▒█████  ██▒   █▓▓█████  ██▀███  " + RESET);
        System.out.println(RED + " ██▒ ▀█▒▒████▄   ▓██▒▀█▀██ ▒▓█   ▀   ▒██▒  ██▒▓██░  █▒▓█   ▀ ▓██ ▒ ██▒" + RESET);
        System.out.println(RED + " ▒██░▄▄▄░▒██  ▀█▄ ▓██    ▓██ ▒███     ▒██░  ██▒ ▓██  █▒░▒███   ▓██ ░▄█ ▒" + RESET);
        System.out.println(RED + " ░▓█  ██▓░██▄▄▄▄██▒██    ▒██ ▒▓█  ▄   ▒██   ██░  ▒██ █░░▒▓█  ▄ ▒██▀▀█▄   " + RESET);
        System.out.println(RED + " ░▒▓███▀▒ ▓█   ▓██░▒██    ░██▒░▒████▒   ████▓▒░   ▒▀█░  ░▒████▒░██▓ ▒██▒ " + RESET);
        System.out.println(RED + " ░▒   ▒  ▒▒   ▓▒█░░░ ▒░ ░░ ▒░   ░  ░   ░ ▒░▒░▒░    ░ ▐░  ░░ ▒░ ░░ ▒▓ ░▒▓░" + RESET);
        System.out.println(RED + " ░   ░   ▒   ▒▒ ░ ░ ░  ░░  ░      ░     ░ ▒ ▒░    ░ ░░   ░ ░  ░  ░▒ ░ ▒░" + RESET);
        System.out.println(RED + " ░ ░   ░   ░   ▒      ░   ░      ░      ░ ░ ░ ▒       ░░     ░     ░░   ░" + RESET);
        System.out.println(RED + " ░       ░  ░   ░  ░       ░          ░ ░        ░     ░  ░   ░");


    }

    // 게임 종료
    public static void goodbyeMessage() {
        slowPrint("\n5 Days Later를 이용해주셔서 감사합니다. 안녕히 가세요!\n", 20, RED);
    }

    // 잘못된 입력
    public static void printInvalidOptionMessage() {
        slowPrint("\n잘못된 옵션입니다. 다시 입력해주세요.\n", 50, ColorUtil.WHITE);
    }

    // 입력 오류
    public static void printInputErrorMessage() {
        slowPrint("\n오류: 유효한 입력을 해주세요.\n", 50, RED);
    }

    // 로그인 성공 -> 메뉴
    public static void printGameMainMenu() {
        System.out.println(GREEN + "╔══════════════════════════ MENU ══════════════════════════════╗");
        System.out.println(GREEN + "║                   WELCOME TO 5 Days Later                    ║");
        System.out.println(GREEN + "     1. 새로운 게임                                                   ");
        System.out.println(GREEN + "     2. 불러오기                                                  ");
        System.out.println(GREEN + "     3. 설정                                                 ");
        System.out.println(GREEN + "     0. 로그아웃                                                     ");
        System.out.println(GREEN + "╚══════════════════════════════════════════════════════════════╝" + BLACK);

    }

    // 데모 멘트
    public static void demo() {
        System.out.println("지금까지 [5일 후] DEMO_VERSION 이었습니다.");
        System.out.println("정식 출시 이전까지 기다려주세요");
    }

    public static void titleArt() {
        System.out.println();
        System.out.println(RED + "  █████▒██▓ ██▒   █▓▓█████    ▓█████▄  ▄▄▄     ▓██   ██▓  ██████     ██▓    ▄▄▄     ▄▄▄█████▓▓█████  ██▀███  " + RESET);
        System.out.println(RED + "▓██   ▒▓██▒▓██░   █▒▓█   ▀    ▒██▀ ██▌▒████▄    ▒██  ██▒▒██    ▒    ▓██▒   ▒████▄   ▓  ██▒ ▓▒▓█   ▀ ▓██ ▒ ██▒" + RESET);
        System.out.println(RED + "▒████ ░▒██▒ ▓██  █▒░▒███      ░██   █▌▒██  ▀█▄   ▒██ ██░░ ▓██▄      ▒██░   ▒██  ▀█▄ ▒ ▓██░ ▒░▒███   ▓██ ░▄█ ▒" + RESET);
        System.out.println(RED + "░▓█▒  ░░██░  ▒██ █░░▒▓█  ▄    ░▓█▄   ▌░██▄▄▄▄██  ░ ▐██▓░  ▒   ██▒   ▒██░   ░██▄▄▄▄██░ ▓██▓ ░ ▒▓█  ▄ ▒██▀▀█▄  " + RESET);
        System.out.println(RED + "░▒█░   ░██░   ▒▀█░  ░▒████▒   ░▒████▓  ▓█   ▓██▒ ░ ██▒▓░▒██████▒▒   ░██████▒▓█   ▓██▒ ▒██▒ ░ ░▒████▒░██▓ ▒██▒" + RESET);
        System.out.println(RED + " ▒ ░   ░▓     ░ ▐░  ░░ ▒░ ░    ▒▒▓  ▒  ▒▒   ▓▒█░  ██▒▒▒ ▒ ▒▓▒ ▒ ░   ░ ▒░▓  ░▒▒   ▓▒█░ ▒ ░░   ░░ ▒░ ░░ ▒▓ ░▒▓░" + RESET);
        System.out.println(RED + " ░      ▒ ░   ░ ░░   ░ ░  ░    ░ ▒  ▒   ▒   ▒▒ ░▓██ ░▒░ ░ ░▒  ░ ░   ░ ░ ▒  ░ ▒   ▒▒ ░   ░     ░ ░  ░  ░▒ ░ ▒░" + RESET);
        System.out.println(RED + " ░ ░    ▒ ░     ░░     ░       ░ ░  ░   ░   ▒   ▒ ▒ ░░  ░  ░  ░       ░ ░    ░   ▒    ░         ░     ░░   ░ " + RESET);
        System.out.println(RED + "        ░        ░     ░  ░      ░          ░  ░░ ░           ░         ░  ░     ░  ░           ░  ░   ░     " + RESET);
        System.out.println(RED + "                ░              ░                ░ ░                                                           " + RESET);
        System.out.println();
    }

    // 라인
    public static void redLine() {
        slowPrint(RED + "════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════\n", 5, ColorUtil.RESET);
    }

    public static void helloArt() {
        System.out.println(RED + "          ██░ ██ ▓█████  ██▓     ██▓     ▒█████ " + RESET);
        System.out.println(RED + "          ▓██░ ██▒▓█   ▀ ▓██▒    ▓██▒    ▒██▒  ██▒" + RESET);
        System.out.println(RED + "          ▒██▀▀██░▒███   ▒██░    ▒██░    ▒██░  ██▒" + RESET);
        System.out.println(RED + "          ░▓█ ░██ ▒▓█  ▄ ▒██░    ▒██░    ▒██   ██░" + RESET);
        System.out.println(RED + "          ░▓█▒░██▓░▒████▒░██████▒░██████▒░ ████▓▒░" + RESET);
        System.out.println(RED + "          ▒ ░░▒░▒░░ ▒░ ░░ ▒░▓  ░░ ▒░▓  ░░ ▒░▒░▒░" + RESET);
        System.out.println(RED + "          ▒ ░▒░ ░ ░ ░  ░░ ░ ▒  ░░ ░ ▒  ░  ░ ▒ ▒░" + RESET);
        System.out.println(RED + "          ░  ░░ ░   ░     ░ ░     ░ ░   ░ ░ ░ ▒" + RESET);
        System.out.println(RED + "          ░  ░  ░   ░  ░    ░  ░    ░  ░    ░ ░" + RESET);
        System.out.println();
    }
}


