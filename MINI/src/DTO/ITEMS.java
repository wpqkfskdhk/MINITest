package DTO;

public class ITEMS {
    private int itemId;     // ITEMID  : NUMBER PRIMARY KEY, 아이템 ID (고유 ID PK)
    private String iName;   // INAME   : NVARCHAR2(50) NOT NULL, 아이템 이름
    private String iType;   // ITYPE   : NVARCHAR2(20), 아이템 종류 (무기, 방어, 회복 등)
    private String iEffect; // IEFFECT : NVARCHAR2(100), 아이템 효과 (HP 회복, 데미지 증가 등)

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getiName() {
        return iName;
    }

    public void setiName(String iName) {
        this.iName = iName;
    }

    public String getiType() {
        return iType;
    }

    public void setiType(String iType) {
        this.iType = iType;
    }

    public String getiEffect() {
        return iEffect;
    }

    public void setiEffect(String iEffect) {
        this.iEffect = iEffect;
    }

    @Override
    public String toString() {
        return "Items{" +
                "itemId=" + itemId +
                ", iName='" + iName + '\'' +
                ", iType='" + iType + '\'' +
                ", iEffect='" + iEffect + '\'' +
                '}';
    }
}
