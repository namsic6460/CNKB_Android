package lkd.namsic.game.creator;

import java.util.HashMap;

import lkd.namsic.game.config.Config;
import lkd.namsic.game.enums.EquipType;
import lkd.namsic.game.enums.Id;
import lkd.namsic.game.enums.StatType;
import lkd.namsic.game.enums.object.EquipList;
import lkd.namsic.game.enums.object.ItemList;
import lkd.namsic.game.object.Equipment;
import lkd.namsic.setting.Logger;

public class EquipCreator implements Creatable {

    @Override
    public void start() {
        Equipment equipment;

        equipment = new Equipment(EquipType.WEAPON, EquipList.WOODEN_SWORD, "나무로 만든 기본적인 검이다");
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.BRANCH.getId(), 5);
            put(ItemList.GRASS.getId(), 5);
        }});
        equipment.addBasicStat(StatType.ATK, 1);
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.WEAPON, EquipList.IRON_SWORD, "철로 만든 날카로운 검이다");
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.STONE.getId(), 20);
            put(ItemList.LAPIS.getId(), 10);
            put(ItemList.IRON.getId(), 20);
        }});
        equipment.addBasicStat(StatType.ATK, 3);
        equipment.addBasicStat(StatType.ATS, 5);
        equipment.addBasicStat(StatType.ACC, 10);
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.WEAPON, EquipList.MIX_SWORD,
                "공격 시 33% 확률로 데미지 1.1배, 33% 확률로 +2 데미지를 준다\n" +
                        "사용 시 5 마나를 소모하여 잃은 체력의 10%를 회복한다");
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.LOW_ALLOY.getId(), 3);
            put(ItemList.QUARTZ.getId(), 4);
        }});
        equipment.addBasicStat(StatType.ATK, 5);
        equipment.addBasicStat(StatType.ATS, 5);
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.WEAPON, EquipList.HEART_BREAKER_1, "공격 시 체력이 10% 미만인 적을 처형시킨다");
        equipment.getHandleLv().set(2);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.IRON.getId(), 30);
            put(ItemList.ZOMBIE_HEART.getId(), 10);
        }});
        equipment.addBasicStat(StatType.ATK, 15);
        equipment.addBasicStat(StatType.ATS, 50);
        equipment.getHandleLv().set(4);
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.WEAPON, EquipList.HEAD_HUNTER_1,
                "치명타 미 발동시 20% 확률로 1.8배의 치명타를 발동시킨다");
        equipment.getHandleLv().set(2);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.RED_STONE.getId(), 30);
            put(ItemList.PIG_HEAD.getId(), 5);
            put(ItemList.ZOMBIE_HEAD.getId(), 5);
        }});
        equipment.addBasicStat(StatType.ATK, 20);
        equipment.addBasicStat(StatType.ATS, 10);
        equipment.addBasicStat(StatType.ACC, 10);
        equipment.addBasicStat(StatType.AGI, 10);
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.WEAPON, EquipList.GHOST_SWORD_1,
                "공격시 적 최대 체력의 6%에 해당하는 추가 데미지를 입힌다\n" +
                        "사용 시 최대 체력의 6%를 소모하여 다음 공격의 데미지를 66% 증가시킨다");
        equipment.getHandleLv().set(2);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.ZOMBIE_SOUL.getId(), 3);
            put(ItemList.SILVER.getId(), 20);
        }});
        equipment.addBasicStat(StatType.ATS, 150);
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.AMULET, EquipList.HEALTH_AMULET, "착용하고 있으면 건강해지는 기분이 드는 부적이다");
        equipment.addBasicStat(StatType.MAXHP, 30);
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.AMULET, EquipList.BLOOD_AMULET, "누군가의 피가 들어간 섬뜩한 부적이다");
        equipment.getHandleLv().set(3);
        equipment.addBasicStat(StatType.MAXHP, 100);
        equipment.addBasicStat(StatType.ATK, 10);
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.AMULET, EquipList.DRAGON_AMULET, "용이 지니고 다녔다는 부적이다");
        equipment.getHandleLv().set(6);
        equipment.addBasicStat(StatType.MAXHP, 200);
        equipment.addBasicStat(StatType.DEF, 30);
        equipment.addBasicStat(StatType.EVA, 20);
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.HELMET, EquipList.LEATHER_HELMET, "가죽으로 만든 간단한 보호구다\n" +
                "치명타 데미지를 20% 줄여준다");
        equipment.addBasicStat(StatType.MAXHP, 10);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.SHEEP_LEATHER.getId(), 10);
            put(ItemList.LEATHER.getId(), 5);
        }});
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.CHESTPLATE, EquipList.LEATHER_CHESTPLATE, "가죽으로 만든 간단한 보호구다");
        equipment.addBasicStat(StatType.MAXHP, 10);
        equipment.addBasicStat(StatType.DEF, 2);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.SHEEP_LEATHER.getId(), 10);
            put(ItemList.LEATHER.getId(), 5);
        }});
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.LEGGINGS, EquipList.LEATHER_LEGGINGS, "가죽으로 만든 간단한 보호구다");
        equipment.addBasicStat(StatType.MAXHP, 10);
        equipment.addBasicStat(StatType.MDEF, 2);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.SHEEP_LEATHER.getId(), 10);
            put(ItemList.LEATHER.getId(), 5);
        }});
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.CHESTPLATE, EquipList.LEATHER_SHOES, "가죽으로 만든 간단한 보호구다");
        equipment.addBasicStat(StatType.MAXHP, 8);
        equipment.addBasicStat(StatType.AGI, 8);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.SHEEP_LEATHER.getId(), 10);
            put(ItemList.LEATHER.getId(), 5);
        }});
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.HELMET, EquipList.LOW_ALLOY_HELMET, "하급 합금으로 만든 간단한 보호구다\n" +
                "치명타 데미지를 20% 확률로 50% 방어한다");
        equipment.getHandleLv().set(2);
        equipment.addBasicStat(StatType.MAXHP, 25);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.LOW_ALLOY.getId(), 5);
            put(ItemList.GOLD.getId(), 5);
        }});
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.CHESTPLATE, EquipList.LOW_ALLOY_CHESTPLATE, "하급 합금으로 만든 간단한 보호구다\n" +
                "사용 시 4 마나를 사용하여 75% 확률로 방어를 한다");
        equipment.getHandleLv().set(2);
        equipment.addBasicStat(StatType.MAXHP, 25);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.LOW_ALLOY.getId(), 5);
            put(ItemList.GOLD.getId(), 5);
        }});
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.LEGGINGS, EquipList.LOW_ALLOY_LEGGINGS, "하급 합금으로 만든 간단한 보호구다\n" +
                "사용 시 5 마나를 사용하여 20초간 회피 15의 버프를 얻는다");
        equipment.getHandleLv().set(2);
        equipment.addBasicStat(StatType.MAXHP, 25);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.LOW_ALLOY.getId(), 5);
            put(ItemList.GOLD.getId(), 5);
        }});
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.SHOES, EquipList.LOW_ALLOY_SHOES, "하급 합금으로 만든 간단한 보호구다\n" +
                "타격 시 합검을 장착하고 있다면 5의 추가 데미지를 준다");
        equipment.getHandleLv().set(2);
        equipment.addBasicStat(StatType.MAXHP, 25);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.LOW_ALLOY.getId(), 5);
            put(ItemList.GOLD.getId(), 5);
        }});
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.WEAPON, EquipList.LOW_MANA_SWORD, "마나의 기운을 담고 있는 검이다\n" +
                "공격 시 마나 1을 회복한다");
        equipment.getHandleLv().set(2);
        equipment.addBasicStat(StatType.ATS, 15);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.PIECE_OF_MANA.getId(), 5);
            put(ItemList.LAPIS.getId(), 20);
            put(ItemList.RED_STONE.getId(), 20);
        }});
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.WEAPON, EquipList.QUARTZ_SWORD, "다양한 석영으로 만들어진 빛나는 검이다");
        equipment.getHandleLv().set(2);
        equipment.addBasicStat(StatType.ATK, 5);
        equipment.addBasicStat(StatType.MAXMN, 5);
        equipment.addBasicStat(StatType.ATS, 10);
        equipment.addBasicStat(StatType.ACC, 10);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.QUARTZ.getId(), 50);
            put(ItemList.DARK_QUARTZ.getId(), 10);
            put(ItemList.FIRE_QUARTZ.getId(), 10);
        }});
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.HELMET, EquipList.SLIME_HELMET, "끈적끈적한 투구다\n" +
                "피격 시 데미지의 8% 를 회복한다\n해당 공격이 치명타일 경우 15% 를 회복한다");
        equipment.getHandleLv().set(3);
        equipment.addBasicStat(StatType.ATS, -10);
        equipment.addBasicStat(StatType.MAXHP, 20);
        equipment.addBasicStat(StatType.ATK, 3);
        equipment.addBasicStat(StatType.DEF, 5);
        equipment.addBasicStat(StatType.BRE, 5);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.PIECE_OF_SLIME.getId(), 30);
        }});
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.CHESTPLATE, EquipList.SLIME_CHESTPLATE, "끈적끈적한 갑옷이다\n" +
                "피격 시 데미지의 20% 를 저장한다\n해당 공격이 치명타일 경우 40% 를 저장한다\n" +
                "사용 시 마나 5를 소모하여 다음 공격이 저장한 데미지를 사용하여 추가 데미지를 입힌다");
        equipment.getHandleLv().set(3);
        equipment.addBasicStat(StatType.ATS, -10);
        equipment.addBasicStat(StatType.MAXHP, 20);
        equipment.addBasicStat(StatType.ATK, 3);
        equipment.addBasicStat(StatType.DEF, 5);
        equipment.addBasicStat(StatType.BRE, 5);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.PIECE_OF_SLIME.getId(), 30);
        }});
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.LEGGINGS, EquipList.SLIME_LEGGINGS, "끈적끈적한 바지다\n" +
                "사용 시 마나를 10 사용하여 30초간 모든 적의 공격 속도를 25 감소시킨다");
        equipment.getHandleLv().set(3);
        equipment.addBasicStat(StatType.ATS, -10);
        equipment.addBasicStat(StatType.MAXHP, 20);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.PIECE_OF_SLIME.getId(), 30);
        }});
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.SHOES, EquipList.SLIME_SHOES, "끈적끈적한 신발이다");
        equipment.getHandleLv().set(2);
        equipment.addBasicStat(StatType.ATS, -10);
        equipment.addBasicStat(StatType.MAXHP, 30);
        equipment.addBasicStat(StatType.BRE, 5);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.PIECE_OF_SLIME.getId(), 30);
        }});
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.HELMET, EquipList.WOOL_HELMET, "양털로 만든 따뜻한 모자다");
        equipment.addBasicStat(StatType.MAXHP, 15);
        equipment.addBasicStat(StatType.MDEF, 5);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.WOOL.getId(), 10);
        }});
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.CHESTPLATE, EquipList.HARD_IRON_CHESTPLATE, "강철로 만든 단단한 갑옷이다");
        equipment.getHandleLv().set(3);
        equipment.addBasicStat(StatType.MAXHP, 75);
        equipment.addBasicStat(StatType.DEF, 10);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.HARD_IRON.getId(), 5);
            put(ItemList.COAL.getId(), 100);
        }});
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.LEGGINGS, EquipList.WEIRD_LEGGINGS, "거미 다리로 만든 기괴하게 생긴 바지다\n" +
                "피격 시 10% 확률로 데미지를 50% 감소시키고 다음 자신의 공격의 데미지를 1.5배로 준다");
        equipment.getHandleLv().set(3);
        equipment.addBasicStat(StatType.MAXHP, 30);
        equipment.addBasicStat(StatType.ATS, 50);
        equipment.addBasicStat(StatType.DEF, 5);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.PIECE_OF_SLIME.getId(), 10);
            put(ItemList.SPIDER_LEG.getId(), 4);
        }});
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.SHOES, EquipList.MINER_SHOES,
                "광부들을 위해 맞춤 제작된 가볍고 튼튼한 신발이다\n" +
                        "광질 시 돌을 채광하면 25% 확률로 돌을 추가로 1개 더 채광한다");
        equipment.getHandleLv().set(1);
        equipment.getHandleLv().set(1);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.STONE.getId(), 1000);
            put(ItemList.COAL.getId(), 1000);
        }});
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.WEAPON, EquipList.TROLL_CLUB, "트롤이 가지고 다니는 무식하게 생긴 몽둥이다\n" +
                "사용 시 마나 1을 소모하여 다음 공격이 자신보다 낮은 레벨의 몬스터를 공격하는 것이라면 데미지를 3배로 준다");
        equipment.getHandleLv().set(4);
        equipment.addBasicStat(StatType.ATK, 35);
        equipment.addBasicStat(StatType.ATS, -20);
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.GEM, EquipList.BETA1_GEM, "베타에 참여해주셔서 감사합니다!");
        equipment.getHandleLv().set(5);
        equipment.addBasicStat(StatType.MAXHP, 5);
        equipment.addBasicStat(StatType.ATK, 5);
        equipment.addBasicStat(StatType.MATK, 5);
        equipment.addBasicStat(StatType.DEF, 5);
        equipment.addBasicStat(StatType.MDEF, 5);
        equipment.addBasicStat(StatType.BRE, 5);
        equipment.addBasicStat(StatType.MBRE, 5);
        equipment.addBasicStat(StatType.ACC, 5);
        equipment.addBasicStat(StatType.EVA, 5);
        Config.unloadObject(equipment);

        Config.ID_COUNT.put(Id.EQUIPMENT, Math.max(Config.ID_COUNT.get(Id.EQUIPMENT), 39L));
        Logger.i("ObjectMaker", "Equipment making is done!");
    }

}
