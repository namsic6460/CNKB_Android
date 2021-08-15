package lkd.namsic.game.creator;

import lkd.namsic.game.config.Config;
import lkd.namsic.game.enums.EquipType;
import lkd.namsic.game.enums.Id;
import lkd.namsic.game.enums.MonsterType;
import lkd.namsic.game.enums.StatType;
import lkd.namsic.game.enums.Variable;
import lkd.namsic.game.enums.object.EquipList;
import lkd.namsic.game.enums.object.EventList;
import lkd.namsic.game.enums.object.ItemList;
import lkd.namsic.game.enums.object.MonsterList;
import lkd.namsic.game.object.Monster;
import lkd.namsic.setting.Logger;

public class MonsterCreator implements Creatable {

    @Override
    public void start() {
        Monster monster;

        monster = new Monster(MonsterList.SHEEP);
        monster.getLv().set(2);
        monster.setLocation(null);
        monster.setType(MonsterType.MIDDLE);

        monster.setBasicStat(StatType.MAXHP, 20);
        monster.setBasicStat(StatType.HP, 20);
        monster.setBasicStat(StatType.ATK, 3);
        monster.setBasicStat(StatType.DEF, 5);

        monster.setItemDrop(ItemList.LAMB.getId(), 0.5D, 1, 1);
        monster.setItemDrop(ItemList.SHEEP_LEATHER.getId(), 0.2D, 1, 1);
        monster.setItemDrop(ItemList.WOOL.getId(), 0.5D, 1, 3);

        Config.unloadObject(monster);


        monster = new Monster(MonsterList.PIG);
        monster.getLv().set(8);
        monster.setLocation(null);
        monster.setType(MonsterType.MIDDLE);

        monster.setBasicStat(StatType.MAXHP, 60);
        monster.setBasicStat(StatType.HP, 60);
        monster.setBasicStat(StatType.ATK, 10);
        monster.setBasicStat(StatType.DEF, 5);
        monster.setBasicStat(StatType.ATS, 50);

        monster.setItemDrop(ItemList.PORK.getId(), 0.5D, 1, 1);
        monster.setItemDrop(ItemList.PIG_HEAD.getId(), 0.1D, 1, 1);

        Config.unloadObject(monster);


        monster = new Monster(MonsterList.COW);
        monster.getLv().set(20);
        monster.setLocation(null);
        monster.setType(MonsterType.MIDDLE);

        monster.setBasicStat(StatType.MAXHP, 100);
        monster.setBasicStat(StatType.HP, 100);
        monster.setBasicStat(StatType.ATK, 10);
        monster.setBasicStat(StatType.DEF, 15);
        monster.setBasicStat(StatType.ATS, 70);
        monster.setBasicStat(StatType.EVA, 20);

        monster.setItemDrop(ItemList.BEEF.getId(), 0.5D, 1, 1);
        monster.setItemDrop(ItemList.LEATHER.getId(), 0.5D, 1, 1);

        Config.unloadObject(monster);


        monster = new Monster(MonsterList.ZOMBIE);
        monster.getLv().set(30);
        monster.setLocation(null);

        monster.setBasicStat(StatType.MAXHP, 300);
        monster.setBasicStat(StatType.HP, 300);
        monster.setBasicStat(StatType.ATK, 20);
        monster.setBasicStat(StatType.ATS, 150);
        monster.setBasicStat(StatType.EVA, 40);

        monster.setItemDrop(ItemList.ZOMBIE_HEAD.getId(), 0.1D, 1, 1);
        monster.setItemDrop(ItemList.ZOMBIE_SOUL.getId(), 0.01D, 1, 1);
        monster.setItemDrop(ItemList.ZOMBIE_HEART.getId(), 0.1D, 1, 1);

        Config.unloadObject(monster);


        monster = new Monster(MonsterList.SLIME);
        monster.getLv().set(45);
        monster.setLocation(null);

        monster.setBasicStat(StatType.MAXHP, 400);
        monster.setBasicStat(StatType.HP, 400);
        monster.setBasicStat(StatType.ATK, 30);
        monster.setBasicStat(StatType.ATS, 50);
        monster.setBasicStat(StatType.ACC, 20);
        monster.setBasicStat(StatType.DRA, 10);
        monster.setBasicStat(StatType.BRE, 15);

        monster.setItemDrop(ItemList.PIECE_OF_SLIME.getId(), 1D, 1, 3);

        Config.unloadObject(monster);


        monster = new Monster(MonsterList.SPIDER);
        monster.getLv().set(50);
        monster.setLocation(null);

        monster.setBasicStat(StatType.MAXHP, 75);
        monster.setBasicStat(StatType.HP, 75);
        monster.setBasicStat(StatType.ATK, 30);
        monster.setBasicStat(StatType.DEF, 10);
        monster.setBasicStat(StatType.ATS, 200);
        monster.setBasicStat(StatType.ACC, 20);
        monster.setBasicStat(StatType.EVA, 40);
        monster.setBasicStat(StatType.AGI, 200);

        monster.setItemDrop(ItemList.SPIDER_LEG.getId(), 0.3D, 1, 1);
        monster.setItemDrop(ItemList.SPIDER_EYE.getId(), 0.2D, 1, 1);

        Config.unloadObject(monster);


        monster = new Monster(MonsterList.TROLL);
        monster.getLv().set(70);
        monster.setLocation(null);

        monster.setBasicStat(StatType.MAXHP, 500);
        monster.setBasicStat(StatType.HP, 500);
        monster.setBasicStat(StatType.ATK, 50);
        monster.setBasicStat(StatType.DRA, 50);

        monster.addBasicEquip(EquipList.TROLL_CLUB.getId());
        monster.setEquipDropPercent(EquipType.WEAPON, 0.01);

        monster.setItemDrop(ItemList.MAGIC_STONE.getId(), 0.1, 1, 1);

        Config.unloadObject(monster);


        monster = new Monster(MonsterList.ENT);
        monster.getLv().set(60);
        monster.setLocation(null);

        monster.setBasicStat(StatType.MAXHP, 100);
        monster.setBasicStat(StatType.HP, 100);
        monster.setBasicStat(StatType.DEF, 999);

        monster.setItemDrop(ItemList.BRANCH.getId(), 1, 3, 5);
        monster.setItemDrop(ItemList.LEAF.getId(), 1, 3, 5);
        monster.setItemDrop(ItemList.MAGIC_STONE.getId(), 1, 1, 1);

        monster.setVariable(Variable.ENT_WAIT_TURN, 3);
        monster.addEvent(EventList.ENT_DAMAGED);

        Config.unloadObject(monster);


        Config.ID_COUNT.put(Id.MONSTER, Math.max(Config.ID_COUNT.get(Id.MONSTER), 9L));
        Logger.i("ObjectMaker", "Monster making is done!");
    }

}
