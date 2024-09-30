package DTO;

public class INVENTORY {

    private int invenId;  // INVENID : NUMBER PRIMARY KEY,  인벤토리 ID (고유 ID PK)
    private int itemId;   // ITEMID  : NUMBER, FOREIGN KEY (ITEMID) REFERENCES ITEMS(ITEMID), 아이템 ID (외래키)
    private int charId;   // 캐릭터 ID (FK)

    public int getInvenId() {
        return invenId;
    }

    public void setInvenId(int invenId) {
        this.invenId = invenId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getCharId() {
        return charId;
    }

    public void setCharId(int charId) {
        this.charId = charId;
    }

    @Override
    public String toString() {
        return "Inventory{" +
                "invenId=" + invenId +
                ", charId=" + charId +
                ", itemId=" + itemId +
                '}';
    }
}
