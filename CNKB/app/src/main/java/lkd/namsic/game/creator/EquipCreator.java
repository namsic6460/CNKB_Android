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

        equipment = new Equipment(EquipType.WEAPON, EquipList.WOODEN_SWORD, null, null);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.BRANCH.getId(), 5);
            put(ItemList.GRASS.getId(), 5);
        }});
        equipment.addBasicStat(StatType.ATK, 1);
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.WEAPON, EquipList.IRON_SWORD, null, null);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.STONE.getId(), 20);
            put(ItemList.LAPIS.getId(), 10);
            put(ItemList.IRON.getId(), 20);
        }});
        equipment.addBasicStat(StatType.ATK, 3);
        equipment.addBasicStat(StatType.ATS, 5);
        equipment.addBasicStat(StatType.ACC, 8);
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.WEAPON, EquipList.MIX_SWORD,
                "[10 마나]\n잃은 체력의 30%를 회복한다",
                "공격 시 33% 확률로 데미지 1.1배, 33% 확률로 +2 데미지를 준다");
        equipment.setHandleLv(2);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.LOW_ALLOY.getId(), 3);
            put(ItemList.QUARTZ.getId(), 4);
        }});
        equipment.addBasicStat(StatType.ATK, 7);
        equipment.addBasicStat(StatType.ATS, 7);
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.WEAPON, EquipList.HEART_BREAKER_1,
                null, "공격 시 체력이 10% 미만인 적을 처형시킨다");
        equipment.setHandleLv(4);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.IRON.getId(), 30);
            put(ItemList.ZOMBIE_HEART.getId(), 10);
        }});
        equipment.addBasicStat(StatType.ATK, 15);
        equipment.addBasicStat(StatType.ATS, 30);
        equipment.setLimitLv(40);
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.WEAPON, EquipList.HEAD_HUNTER_1,
                null, "치명타 미 발동 시 25% 확률로 2배의 치명타를 발동시킨다");
        equipment.setHandleLv(4);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.RED_STONE.getId(), 30);
            put(ItemList.PIG_HEAD.getId(), 10);
            put(ItemList.ZOMBIE_HEAD.getId(), 5);
        }});
        equipment.addBasicStat(StatType.ATK, 10);
        equipment.addBasicStat(StatType.ATS, 10);
        equipment.addBasicStat(StatType.ACC, 10);
        equipment.addBasicStat(StatType.AGI, 10);
        equipment.setLimitLv(40);
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.WEAPON, EquipList.GHOST_SWORD_1,
                "[최대 체력 6%]\n다음 공격의 데미지를 66% 증가시킨다",
                "공격 시 적 최대 체력의 3%에 해당하는 추가 데미지를 입힌다");
        equipment.setHandleLv(4);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.ZOMBIE_SOUL.getId(), 1);
            put(ItemList.SILVER.getId(), 20);
        }});
        equipment.addBasicStat(StatType.ATS, 60);
        equipment.setLimitLv(40);
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.AMULET, EquipList.HEALTH_AMULET, null, null);
        equipment.addBasicStat(StatType.MAXHP, 30);
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.AMULET, EquipList.BLOOD_AMULET, null, null);
        equipment.setHandleLv(3);
        equipment.addBasicStat(StatType.MAXHP, 100);
        equipment.addBasicStat(StatType.ATK, 10);
        equipment.setLimitLv(30);
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.AMULET, EquipList.DRAGON_AMULET, null, null);
        equipment.setHandleLv(6);
        equipment.addBasicStat(StatType.MAXHP, 200);
        equipment.addBasicStat(StatType.DEF, 30);
        equipment.addBasicStat(StatType.EVA, 20);
        equipment.setLimitLv(50);
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.HELMET, EquipList.LEATHER_HELMET,
                null, "치명타 데미지를 15% 감소시킨다");
        equipment.addBasicStat(StatType.MAXHP, 10);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.SHEEP_LEATHER.getId(), 10);
            put(ItemList.LEATHER.getId(), 5);
        }});
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.CHESTPLATE, EquipList.LEATHER_CHESTPLATE, null, null);
        equipment.addBasicStat(StatType.MAXHP, 10);
        equipment.addBasicStat(StatType.DEF, 2);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.SHEEP_LEATHER.getId(), 10);
            put(ItemList.LEATHER.getId(), 5);
        }});
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.LEGGINGS, EquipList.LEATHER_LEGGINGS, null, null);
        equipment.addBasicStat(StatType.MAXHP, 10);
        equipment.addBasicStat(StatType.MDEF, 2);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.SHEEP_LEATHER.getId(), 10);
            put(ItemList.LEATHER.getId(), 5);
        }});
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.CHESTPLATE, EquipList.LEATHER_SHOES, null, null);
        equipment.addBasicStat(StatType.MAXHP, 8);
        equipment.addBasicStat(StatType.AGI, 8);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.SHEEP_LEATHER.getId(), 10);
            put(ItemList.LEATHER.getId(), 5);
        }});
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.HELMET, EquipList.LOW_ALLOY_HELMET,
                null, "치명타 데미지를 40% 확률로 50% 감소시킨다");
        equipment.setHandleLv(2);
        equipment.addBasicStat(StatType.MAXHP, 30);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.LOW_ALLOY.getId(), 5);
            put(ItemList.GOLD.getId(), 5);
        }});
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.CHESTPLATE, EquipList.LOW_ALLOY_CHESTPLATE,
                "[4 마나]\n75% 확률로 방어를 한다", null);
        equipment.setHandleLv(2);
        equipment.addBasicStat(StatType.MAXHP, 30);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.LOW_ALLOY.getId(), 5);
            put(ItemList.GOLD.getId(), 5);
        }});
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.LEGGINGS, EquipList.LOW_ALLOY_LEGGINGS,
                "[5 마나]\n20초간 회피 15의 버프를 얻는다", null);
        equipment.setHandleLv(2);
        equipment.addBasicStat(StatType.MAXHP, 30);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.LOW_ALLOY.getId(), 5);
            put(ItemList.GOLD.getId(), 5);
        }});
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.SHOES, EquipList.LOW_ALLOY_SHOES,
                null, "공격 시 합검을 장착하고 있다면 10의 추가 데미지를 준다");
        equipment.setHandleLv(2);
        equipment.addBasicStat(StatType.MAXHP, 30);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.LOW_ALLOY.getId(), 5);
            put(ItemList.GOLD.getId(), 5);
        }});
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.WEAPON, EquipList.LOW_MANA_SWORD,
                null, "공격 시 마나 1을 회복한다");
        equipment.setHandleLv(2);
        equipment.addBasicStat(StatType.MAXMN, 5);
        equipment.addBasicStat(StatType.ATS, 10);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.PIECE_OF_MANA.getId(), 5);
            put(ItemList.LAPIS.getId(), 20);
            put(ItemList.RED_STONE.getId(), 20);
        }});
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.WEAPON, EquipList.QUARTZ_SWORD, null, null);
        equipment.setHandleLv(2);
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

        equipment = new Equipment(EquipType.HELMET, EquipList.SLIME_HELMET,
                null, "피격 시 데미지의 8% 를 회복한다\n해당 공격이 치명타일 경우 15% 를 회복한다");
        equipment.setHandleLv(3);
        equipment.addBasicStat(StatType.ATS, -10);
        equipment.addBasicStat(StatType.MAXHP, 20);
        equipment.addBasicStat(StatType.ATK, 3);
        equipment.addBasicStat(StatType.DEF, 5);
        equipment.addBasicStat(StatType.BRE, 5);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.PIECE_OF_SLIME.getId(), 30);
        }});
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.CHESTPLATE, EquipList.SLIME_CHESTPLATE,
                "[5 마나]\n다음 공격이 저장한 데미지를 사용하여 추가 데미지를 입힌다",
                "피격 시 데미지의 20% 를 저장한다\n해당 공격이 치명타일 경우 40% 를 저장한다");
        equipment.setHandleLv(3);
        equipment.addBasicStat(StatType.ATS, -10);
        equipment.addBasicStat(StatType.MAXHP, 20);
        equipment.addBasicStat(StatType.ATK, 3);
        equipment.addBasicStat(StatType.DEF, 5);
        equipment.addBasicStat(StatType.BRE, 5);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.PIECE_OF_SLIME.getId(), 30);
        }});
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.LEGGINGS, EquipList.SLIME_LEGGINGS,
                "[10 마나]\n30초간 모든 적의 공격 속도를 30 감소시킨다", null);
        equipment.setHandleLv(3);
        equipment.addBasicStat(StatType.ATS, -10);
        equipment.addBasicStat(StatType.MAXHP, 30);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.PIECE_OF_SLIME.getId(), 30);
        }});
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.SHOES, EquipList.SLIME_SHOES, null, null);
        equipment.setHandleLv(2);
        equipment.addBasicStat(StatType.ATS, -10);
        equipment.addBasicStat(StatType.MAXHP, 30);
        equipment.addBasicStat(StatType.BRE, 5);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.PIECE_OF_SLIME.getId(), 30);
        }});
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.HELMET, EquipList.WOOL_HELMET, null, null);
        equipment.addBasicStat(StatType.MAXHP, 15);
        equipment.addBasicStat(StatType.MDEF, 8);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.WOOL.getId(), 10);
        }});
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.CHESTPLATE, EquipList.HARD_IRON_CHESTPLATE, null, null);
        equipment.setLimitLv(30);
        equipment.setHandleLv(5);
        equipment.addBasicStat(StatType.MAXHP, 80);
        equipment.addBasicStat(StatType.DEF, 25);
        equipment.addBasicStat(StatType.ATS, -40);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.HARD_IRON.getId(), 5);
        }});
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.LEGGINGS, EquipList.WEIRD_LEGGINGS,
                null, "피격 시 10% 확률로 데미지를 50% 감소시키고 다음 자신의 공격 데미지를 1.5배로 준다");
        equipment.setHandleLv(3);
        equipment.addBasicStat(StatType.MAXHP, 30);
        equipment.addBasicStat(StatType.ATS, 30);
        equipment.addBasicStat(StatType.DEF, 5);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.PIECE_OF_SLIME.getId(), 10);
            put(ItemList.SPIDER_LEG.getId(),4 );
        }});
        equipment.setLimitLv(25);
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.SHOES, EquipList.MINER_SHOES,
                null, "광질 시 돌을 채광하면 40% 확률로 돌을 추가로 1개 더 채광한다");
        equipment.setHandleLv(1);
        equipment.setHandleLv(1);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.STONE.getId(), 1000);
            put(ItemList.COAL.getId(), 1000);
        }});
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.WEAPON, EquipList.TROLL_CLUB,
                "[마나 1]\n다음 공격이 자신보다 낮은 레벨의 몬스터를 공격하는 것이라면 데미지를 3배로 준다", null);
        equipment.setHandleLv(4);
        equipment.setLimitLv(30);
        equipment.addBasicStat(StatType.ATK, 30);
        equipment.addBasicStat(StatType.ATS, -20);
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.GEM, EquipList.BETA1_GEM, null, "베타에 참여해주셔서 감사합니다!");
        equipment.setHandleLv(5);
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

        equipment = new Equipment(EquipType.NECKLACE, EquipList.OAK_TOOTH_NECKLACE, null, null);
        equipment.setHandleLv(3);
        equipment.setLimitLv(20);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.BRANCH.getId(), 10);
            put(ItemList.GRASS.getId(), 20);
            put(ItemList.ELIXIR_HERB.getId(), 20);
            put(ItemList.OAK_TOOTH.getId(), 10);
        }});
        equipment.addBasicStat(StatType.ATS, 20);
        equipment.addBasicStat(StatType.EVA, 10);
        equipment.addBasicStat(StatType.ACC, 20);
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.WEAPON, EquipList.BONE_SWORD,
                null, "보호구가 모두 뼈 장비 또는 마족의 뼈 장비라면 피격 시 피해를 20% 줄여서 받는다");
        equipment.setHandleLv(2);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.PIECE_OF_BONE.getId(), 100);
            put(ItemList.COAL.getId(), 10);
        }});
        equipment.addBasicStat(StatType.ATK, 5);
        equipment.addBasicStat(StatType.AGI, 5);
        equipment.addBasicStat(StatType.ACC, 5);
        Config.unloadObject(equipment);
        
        equipment = new Equipment(EquipType.WEAPON, EquipList.BASIC_STAFF, null, null);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.BRANCH.getId(), 5);
            put(ItemList.WATER_GRASS.getId(), 5);
        }});
        equipment.addBasicStat(StatType.MATK, 3);
        equipment.addBasicStat(StatType.MAXMN, 1);
        Config.unloadObject(equipment);
        
        equipment = new Equipment(EquipType.WEAPON, EquipList.SEA_STAFF,
                null, "강/바다/오염된 강 에서 공격 시 데미지가 40% 증가한다");
        equipment.setHandleLv(2);
        equipment.setLimitLv(10);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.LITHIUM.getId(), 20);
            put(ItemList.LAPIS.getId(), 10);
            put(ItemList.RED_STONE.getId(), 10);
            put(ItemList.BLUE_SPHERE.getId(), 1);
        }});
        equipment.addBasicStat(StatType.MATK, 8);
        equipment.addBasicStat(StatType.MBRE, 3);
        equipment.addBasicStat(StatType.MDRA, 5);
        equipment.addBasicStat(StatType.MAXMN, 2);
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.WEAPON, EquipList.DEMON_STAFF,
                "[현재 체력 10%]\n60초간 마법 공격력 + 8, 마법 방어 관통력 + 8 을 획득하고 마나를 5 회복한다", null);
        equipment.setHandleLv(3);
        equipment.setLimitLv(25);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.BLACK_SPHERE.getId(), 3);
            put(ItemList.MAGIC_STONE.getId(), 20);
            put(ItemList.PIECE_OF_SLIME.getId(), 20);
            put(ItemList.ZOMBIE_HEAD.getId(), 5);
        }});
        equipment.addBasicStat(StatType.MATK, 10);
        equipment.addBasicStat(StatType.MBRE, 5);
        equipment.addBasicStat(StatType.ATS, 10);
        equipment.addBasicStat(StatType.MAXMN, 3);
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.HELMET, EquipList.BONE_HELMET, null, null);
        equipment.setHandleLv(3);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.PIECE_OF_BONE.getId(), 80);
            put(ItemList.COAL.getId(), 10);
        }});
        equipment.addBasicStat(StatType.MAXHP, 10);
        equipment.addBasicStat(StatType.DEF, 3);
        equipment.addBasicStat(StatType.MDEF, 3);
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.CHESTPLATE, EquipList.BONE_CHESTPLATE, null, null);
        equipment.setHandleLv(3);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.PIECE_OF_BONE.getId(), 100);
            put(ItemList.COAL.getId(), 10);
        }});
        equipment.addBasicStat(StatType.DEF, 6);
        equipment.addBasicStat(StatType.MDEF, 6);
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.LEGGINGS, EquipList.BONE_LEGGINGS, null, null);
        equipment.setHandleLv(3);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.PIECE_OF_BONE.getId(), 90);
            put(ItemList.COAL.getId(), 10);
        }});
        equipment.addBasicStat(StatType.MAXHP, 20);
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.SHOES, EquipList.BONE_SHOES, null, null);
        equipment.setHandleLv(3);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.PIECE_OF_BONE.getId(), 70);
            put(ItemList.COAL.getId(), 10);
        }});
        equipment.addBasicStat(StatType.MAXHP, 5);
        equipment.addBasicStat(StatType.DEF, 3);
        equipment.addBasicStat(StatType.MDEF, 3);
        equipment.addBasicStat(StatType.EVA, 5);
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.RINGS, EquipList.DEVIL_RING, null, null);
        equipment.setHandleLv(4);
        equipment.addBasicStat(StatType.MATK, 20);
        equipment.addBasicStat(StatType.MDRA, 10);
        equipment.addBasicStat(StatType.ATS, 10);
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.WEAPON, EquipList.HEART_BREAKER_2,
                null, "공격 시 체력이 15% 미만인 적을 처형시킨다");
        equipment.setHandleLv(6);
        equipment.setLimitLv(70);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.TITANIUM.getId(), 30);
            put(ItemList.HARD_COAL.getId(), 10);
            put(ItemList.IMP_HEART.getId(), 10);
        }});
        equipment.addBasicStat(StatType.ATK, 30);
        equipment.addBasicStat(StatType.ATS, 40);
        equipment.addBasicStat(StatType.ACC, 20);
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.WEAPON, EquipList.GHOST_SWORD_2,
                "[최대 체력 6%]\n다음 공격의 데미지를 100% 증가시킨다",
                "공격시 적 최대 체력의 6%에 해당하는 추가 데미지를 입힌다");
        equipment.setHandleLv(6);
        equipment.setLimitLv(70);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.LOW_DEVIL_SOUL.getId(), 1);
            put(ItemList.LIQUID_STONE.getId(), 30);
            put(ItemList.HARD_COAL.getId(), 10);
        }});
        equipment.addBasicStat(StatType.ATS, 100);
        equipment.addBasicStat(StatType.ATK, 15);
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.HELMET, EquipList.DEMON_BONE_HELMET, null, null);
        equipment.setHandleLv(5);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.HORN_OF_IMP.getId(), 5);
            put(ItemList.HORN_OF_LOW_DEVIL.getId(), 5);
            put(ItemList.COAL.getId(), 80);
        }});
        equipment.addBasicStat(StatType.MAXHP, 20);
        equipment.addBasicStat(StatType.DEF, 5);
        equipment.addBasicStat(StatType.MDEF, 5);
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.CHESTPLATE, EquipList.DEMON_BONE_CHESTPLATE, null, null);
        equipment.setHandleLv(5);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.HORN_OF_IMP.getId(), 5);
            put(ItemList.HORN_OF_LOW_DEVIL.getId(), 5);
            put(ItemList.COAL.getId(), 100);
        }});
        equipment.addBasicStat(StatType.DEF, 10);
        equipment.addBasicStat(StatType.MDEF, 10);
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.LEGGINGS, EquipList.DEMON_BONE_LEGGINGS, null, null);
        equipment.setHandleLv(5);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.HORN_OF_IMP.getId(), 5);
            put(ItemList.HORN_OF_LOW_DEVIL.getId(), 5);
            put(ItemList.COAL.getId(), 90);
        }});
        equipment.addBasicStat(StatType.MAXHP, 30);
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.SHOES, EquipList.DEMON_BONE_SHOES, null, null);
        equipment.setHandleLv(5);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.HORN_OF_IMP.getId(), 5);
            put(ItemList.HORN_OF_LOW_DEVIL.getId(), 5);
            put(ItemList.COAL.getId(), 70);
        }});
        equipment.addBasicStat(StatType.MAXHP, 10);
        equipment.addBasicStat(StatType.DEF, 5);
        equipment.addBasicStat(StatType.MDEF, 5);
        equipment.addBasicStat(StatType.EVA, 10);
        Config.unloadObject(equipment);

        Config.ID_COUNT.put(Id.EQUIPMENT, Math.max(Config.ID_COUNT.get(Id.EQUIPMENT), 90L));
        Logger.i("ObjectMaker", "Equipment making is done!");
    }

}
