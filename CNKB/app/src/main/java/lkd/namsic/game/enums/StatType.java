package lkd.namsic.game.enums;

import lombok.Getter;

public enum StatType {

    MAXHP("최대 체력"),
    HP("현재 체력"),
    MAXMN("최대 마나"),
    MN("현재 마나"),
    ATK("공격력"),
    MATK("마법 공격력"),
    AGI("민첩"),
    ATS("공격속도"),
    ATR("공격 사거리"),
    DEF("방어력"),
    MDEF("마법 방어력"),
    BRE("방어 관통력"),
    MBRE("마법 방어 관통력"),
    DRA("흡수력"),
    MDRA("마법 흡수력"),
    EVA("회피"),
    ACC("정확도");

    @Getter
    String displayName;

    StatType(String displayName) {
        this.displayName = displayName;
    }

}
