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


        monster = new Monster(MonsterList.SKELETON);
        monster.getLv().set(25);
        monster.setLocation(null);
        monster.setType(MonsterType.BAD);

        monster.setBasicStat(StatType.MAXHP, 150);
        monster.setBasicStat(StatType.HP, 150);
        monster.setBasicStat(StatType.ATK, 15);
        monster.setBasicStat(StatType.BRE, 10);
        monster.setBasicStat(StatType.AGI, 40);

        monster.setItemDrop(ItemList.PIECE_OF_BONE.getId(), 1D, 1, 5);

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
        monster.setBasicStat(StatType.ATS, 150);
        monster.setBasicStat(StatType.ACC, 20);
        monster.setBasicStat(StatType.EVA, 40);
        monster.setBasicStat(StatType.AGI, 200);

        monster.setItemDrop(ItemList.SPIDER_LEG.getId(), 0.3D, 1, 1);
        monster.setItemDrop(ItemList.SPIDER_EYE.getId(), 0.2D, 1, 1);

        Config.unloadObject(monster);


        monster = new Monster(MonsterList.TROLL);
        monster.getLv().set(70);
        monster.setLocation(null);

        monster.setBasicStat(StatType.MAXHP, 550);
        monster.setBasicStat(StatType.HP, 550);
        monster.setBasicStat(StatType.ATK, 50);
        monster.setBasicStat(StatType.DRA, 50);
        monster.setBasicStat(StatType.ACC, 40);

        monster.addBasicEquip(EquipList.TROLL_CLUB.getId());
        monster.setEquipDropPercent(EquipType.WEAPON, 0.01);

        monster.setItemDrop(ItemList.MAGIC_STONE.getId(), 0.1, 1, 1);

        Config.unloadObject(monster);


        monster = new Monster(MonsterList.ENT);
        monster.getLv().set(60);
        monster.setLocation(null);

        monster.setBasicStat(StatType.MAXHP, 150);
        monster.setBasicStat(StatType.HP, 150);
        monster.setBasicStat(StatType.DEF, 999);

        monster.setItemDrop(ItemList.BRANCH.getId(), 1, 3, 5);
        monster.setItemDrop(ItemList.LEAF.getId(), 1, 3, 5);
        monster.setItemDrop(ItemList.MAGIC_STONE.getId(), 1, 1, 1);

        monster.setVariable(Variable.ENT_WAIT_TURN, 3);
        monster.addEvent(EventList.ENT_DAMAGED);

        Config.unloadObject(monster);


        monster = new Monster(MonsterList.IMP);
        monster.getLv().set(70);
        monster.setLocation(null);

        monster.setBasicStat(StatType.MAXHP, 250);
        monster.setBasicStat(StatType.HP, 250);
        monster.setBasicStat(StatType.ATK, 30);
        monster.setBasicStat(StatType.ATS, 180);
        monster.setBasicStat(StatType.DEF, 20);
        monster.setBasicStat(StatType.MDEF, 100);
        monster.setBasicStat(StatType.AGI, 20);
        monster.setBasicStat(StatType.ACC, 100);
        monster.setBasicStat(StatType.EVA, 100);

        monster.setItemDrop(ItemList.HORN_OF_IMP.getId(), 0.25, 1, 1);
        monster.setItemDrop(ItemList.IMP_HEART.getId(), 0.1, 1, 1);
        monster.setItemDrop(ItemList.MAGIC_STONE.getId(), 1, 1, 2);

        monster.addEvent(EventList.IMP_ATTACK);

        Config.unloadObject(monster);


        monster = new Monster(MonsterList.OAK);
        monster.getLv().set(90);
        monster.setLocation(null);

        monster.setBasicStat(StatType.MAXHP, 400);
        monster.setBasicStat(StatType.HP, 400);
        monster.setBasicStat(StatType.ATK, 40);
        monster.setBasicStat(StatType.ATS, 150);
        monster.setBasicStat(StatType.DEF, 30);
        monster.setBasicStat(StatType.MDEF, 15);
        monster.setBasicStat(StatType.BRE, 50);
        monster.setBasicStat(StatType.ACC, 60);
        monster.setBasicStat(StatType.EVA, 30);

        monster.setItemDrop(ItemList.OAK_TOOTH.getId(), 0.3, 1, 1);
        monster.setItemDrop(ItemList.OAK_LEATHER.getId(), 0.5, 1, 1);

        monster.addEvent(EventList.OAK_START_FIGHT);

        Config.unloadObject(monster);


        monster = new Monster(MonsterList.LOW_DEVIL);
        monster.getLv().set(110);
        monster.setLocation(null);
        monster.setType(MonsterType.MIDDLE);

        monster.setBasicStat(StatType.MAXHP, 400);
        monster.setBasicStat(StatType.HP, 400);
        monster.setBasicStat(StatType.ATK, 50);
        monster.setBasicStat(StatType.ATS, 140);
        monster.setBasicStat(StatType.BRE, 15);
        monster.setBasicStat(StatType.DEF, 20);
        monster.setBasicStat(StatType.MDEF, 20);
        monster.setBasicStat(StatType.DRA, 40);
        monster.setBasicStat(StatType.ACC, 50);
        monster.setBasicStat(StatType.EVA, 15);

        monster.setItemDrop(ItemList.HORN_OF_LOW_DEVIL.getId(), 0.1, 1, 1);
        monster.setItemDrop(ItemList.LOW_DEVIL_SOUL.getId(), 0.01, 1, 1);
        monster.setItemDrop(ItemList.MAGIC_STONE.getId(), 1, 2, 2);

        monster.addBasicEquip(EquipList.DEVIL_RING.getId());
        monster.setEquipDropPercent(EquipType.RINGS, 0.01);

        Config.unloadObject(monster);


        Config.ID_COUNT.put(Id.MONSTER, Math.max(Config.ID_COUNT.get(Id.MONSTER), 13L));
        Logger.i("ObjectMaker", "Monster making is done!");
    }

}
