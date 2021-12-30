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

        boss = new Boss(
            BossList.WOLF_OF_MOON,
            "인간들의 욕망에 의해 소멸하고",
            "인간들의 욕망에 의해 다시 부활하니...",
            "내가 직접 너희의 욕망을 끊을것이다"
        );
        boss.setLv(200);

        boss.setBasicStat(StatType.MAXHP, 20000);
        boss.setBasicStat(StatType.HP, 20000);
        boss.setBasicStat(StatType.MAXMN, 1000);
        boss.setBasicStat(StatType.MN, 1000);
        boss.setBasicStat(StatType.ATK, 400);
        boss.setBasicStat(StatType.MATK, 300);
        boss.setBasicStat(StatType.AGI, 120);
        boss.setBasicStat(StatType.ATS, 550);
        boss.setBasicStat(StatType.DEF, 500);
        boss.setBasicStat(StatType.BRE, 150);
        boss.setBasicStat(StatType.DRA, 60);
        boss.setBasicStat(StatType.EVA, 100);
        boss.setBasicStat(StatType.ACC, 700);

        boss.setItemDrop(ItemList.GOLD_BAG.getId(), 1, 10, 20);
        boss.setItemDrop(ItemList.MAGIC_STONE.getId(), 1, 30, 50);
        boss.setItemDrop(ItemList.LYCANTHROPE_SOUL.getId(), 1, 3, 3);
        boss.setItemDrop(ItemList.SKILL_BOOK_CUTTING_MOONLIGHT.getId(), 0.1, 1, 1);

        boss.addBasicEquip(EquipList.MOON_SWORD.getId());
        boss.setEquipDropPercent(EquipType.WEAPON, 0.1);
        boss.addBasicEquip(EquipList.MOON_GEM.getId());
        boss.setEquipDropPercent(EquipType.GEM, 0.1);

        boss.addSkill(SkillList.SCAR.getId());
        boss.setSkillPercent(SkillList.SCAR.getId(), 0.2);
        boss.addSkill(SkillList.STRINGS_OF_LIFE.getId());

        boss.addEvent(EventList.WOLF_OF_MOON_PAGE_2);

        Config.unloadObject(boss);
        
        
        boss = new Boss(
            BossList.SUCCUBUS,
            "내 귀여운 아이들을 누가 이렇게 죽이고 있는걸까...",
            "역시 네놈들이겠지?",
            "쉽게 죽을 생각은 하지 마렴 후훗"
        );
        boss.setLv(100);
        
        boss.setBasicStat(StatType.MAXHP, 7000);
        boss.setBasicStat(StatType.HP, 7000);
        boss.setBasicStat(StatType.MAXMN, 400);
        boss.setBasicStat(StatType.MN, 400);
        boss.setBasicStat(StatType.ATK, 150);
        boss.setBasicStat(StatType.MATK, 300);
        boss.setBasicStat(StatType.ATS, 300);
        boss.setBasicStat(StatType.AGI, 100);
        boss.setBasicStat(StatType.DEF, 75);
        boss.setBasicStat(StatType.MDEF, 150);
        boss.setBasicStat(StatType.MBRE, 50);
        boss.setBasicStat(StatType.MDRA, 40);
        boss.setBasicStat(StatType.EVA, 200);
        boss.setBasicStat(StatType.ACC, 300);
    
        boss.setItemDrop(ItemList.SMALL_GOLD_BAG.getId(), 1, 20, 40);
        boss.setItemDrop(ItemList.MAGIC_STONE.getId(), 1, 10, 30);
        boss.setItemDrop(ItemList.SKILL_BOOK_CHARM.getId(), 0.1, 1, 1);
        boss.setItemDrop(ItemList.SKILL_BOOK_ESSENCE_DRAIN.getId(), 0.1, 1, 1);
        boss.setItemDrop(ItemList.SKILL_BOOK_MAGIC_DRAIN.getId(), 0.1, 1, 1);
        boss.setItemDrop(ItemList.SKILL_BOOK_MANA_EXPLOSION.getId(), 0.1, 1, 1);
    
        boss.addEquip(EquipList.SUCCUBUS_GEM.getId());
        boss.setEquipDropPercent(EquipType.GEM, 0.1);
        boss.addEquip(EquipList.CHARM_WHIP.getId());
        boss.setEquipDropPercent(EquipType.WEAPON, 0.1);
        
        boss.addSkill(SkillList.CHARM.getId());
        boss.setSkillPercent(SkillList.CHARM.getId(), 0.1);
        boss.addSkill(SkillList.SPAWN_IMP.getId());
        boss.addSkill(SkillList.ESSENCE_DRAIN.getId());
        boss.setSkillPercent(SkillList.ESSENCE_DRAIN.getId(), 0.3);
        boss.addSkill(SkillList.MAGIC_DRAIN.getId());
        boss.setSkillPercent(SkillList.MAGIC_DRAIN.getId(), 0.3);
        boss.addSkill(SkillList.MANA_EXPLOSION.getId());
        boss.setSkillPercent(SkillList.MANA_EXPLOSION.getId(), 0.1);
        
        boss.addEvent(EventList.SUCCUBUS_START);
        boss.addEvent(EventList.SUCCUBUS_PAGE2);
        
        Config.unloadObject(boss);


        Config.ID_COUNT.put(Id.BOSS, Math.max(Config.ID_COUNT.get(Id.BOSS), 1000000L));
        Logger.i("ObjectMaker", "Boss making is done!");
    }

}
