package lkd.namsic.game.creator;

import lkd.namsic.game.config.Config;
import lkd.namsic.game.enums.EquipType;
import lkd.namsic.game.enums.Id;
import lkd.namsic.game.enums.StatType;
import lkd.namsic.game.enums.object.BossList;
import lkd.namsic.game.enums.object.EquipList;
import lkd.namsic.game.enums.object.EventList;
import lkd.namsic.game.enums.object.ItemList;
import lkd.namsic.game.enums.object.SkillList;
import lkd.namsic.game.object.Boss;
import lkd.namsic.setting.Logger;

public class BossCreator implements Creatable {

    @Override
    public void start() {
        Boss boss;

        boss = new Boss(BossList.WOLF_OF_MOON,
                "인간들의 욕망에 의해 소멸하고",
                "인간들의 욕망에 의해 다시 부활하니...",
                "내가 직접 너희의 욕망을 끊을것이다"
        );
        boss.setLv(200);

        boss.setBasicStat(StatType.MAXHP, 5000);
        boss.setBasicStat(StatType.HP, 5000);
        boss.setBasicStat(StatType.MAXMN, 500);
        boss.setBasicStat(StatType.MN, 500);
        boss.setBasicStat(StatType.ATK, 300);
        boss.setBasicStat(StatType.MATK, 300);
        boss.setBasicStat(StatType.AGI, 100);
        boss.setBasicStat(StatType.ATS, 350);
        boss.setBasicStat(StatType.DEF, 200);
        boss.setBasicStat(StatType.BRE, 150);
        boss.setBasicStat(StatType.DRA, 50);
        boss.setBasicStat(StatType.EVA, 150);
        boss.setBasicStat(StatType.ACC, 100);

        boss.setItemDrop(ItemList.GOLD_BAG.getId(), 1, 10, 20);
        boss.setItemDrop(ItemList.MAGIC_STONE.getId(), 1, 30, 50);
        boss.setItemDrop(ItemList.LYCANTHROPE_SOUL.getId(), 1, 3, 3);
        boss.setItemDrop(ItemList.SKILL_BOOK_CUTTING_MOONLIGHT.getId(), 0.1, 1, 1);

        boss.addBasicEquip(EquipList.MOON_SWORD.getId());
        boss.setEquipDropPercent(EquipType.WEAPON, 0.1);
        boss.addBasicEquip(EquipList.MOON_GEM.getId());
        boss.setEquipDropPercent(EquipType.GEM, 0.1);

        boss.addSkill(SkillList.SCAR.getId());
        boss.setSkillPercent(SkillList.SCAR.getId(), 0.25);
        boss.addSkill(SkillList.STRINGS_OF_LIFE.getId());

        boss.addEvent(EventList.WOLF_OF_MOON_PAGE_2);

        Config.unloadObject(boss);


        Config.ID_COUNT.put(Id.BOSS, Math.max(Config.ID_COUNT.get(Id.BOSS), 2L));
        Logger.i("ObjectMaker", "Boss making is done!");
    }

}
