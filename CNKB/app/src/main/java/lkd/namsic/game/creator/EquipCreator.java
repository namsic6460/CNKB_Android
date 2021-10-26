package lkd.namsic.game.creator;

import java.util.HashMap;

import lkd.namsic.game.config.Config;
import lkd.namsic.game.config.Emoji;
import lkd.namsic.game.enums.EquipType;
import lkd.namsic.game.enums.Id;
import lkd.namsic.game.enums.StatType;
import lkd.namsic.game.enums.object.EquipList;
import lkd.namsic.game.enums.object.ItemList;
import lkd.namsic.game.enums.object.SkillList;
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
        equipment.setBasicStat(StatType.ATK, 1);
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.WEAPON, EquipList.IRON_SWORD, null, null);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.STONE.getId(), 20);
            put(ItemList.LAPIS.getId(), 10);
            put(ItemList.IRON.getId(), 20);
        }});
        equipment.setBasicStat(StatType.ATK, 3);
        equipment.setBasicStat(StatType.ATS, 5);
        equipment.setBasicStat(StatType.ACC, 8);
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.WEAPON, EquipList.MIX_SWORD_1,
                "[10 마나]\n잃은 체력의 30%를 회복한다",
                "공격 시 33% 확률로 데미지 1.1배, 33% 확률로 5 데미지를 준다");
        equipment.setHandleLv(2);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.LOW_ALLOY.getId(), 3);
            put(ItemList.QUARTZ.getId(), 4);
        }});
        equipment.setBasicStat(StatType.ATK, 7);
        equipment.setBasicStat(StatType.ATS, 7);
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.WEAPON, EquipList.HEART_BREAKER_1,
                null, "공격 시 체력이 10% 미만인 적을 처형시킨다");
        equipment.setHandleLv(4);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.IRON.getId(), 30);
            put(ItemList.ZOMBIE_HEART.getId(), 10);
        }});
        equipment.setBasicStat(StatType.ATK, 15);
        equipment.setBasicStat(StatType.ATS, 30);
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
        equipment.setBasicStat(StatType.ATK, 10);
        equipment.setBasicStat(StatType.ATS, 10);
        equipment.setBasicStat(StatType.ACC, 10);
        equipment.setBasicStat(StatType.AGI, 10);
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
        equipment.setBasicStat(StatType.ATS, 60);
        equipment.setLimitLv(40);
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.AMULET, EquipList.HEALTH_AMULET, null, null);
        equipment.setBasicStat(StatType.MAXHP, 30);
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.AMULET, EquipList.BLOOD_AMULET, null, null);
        equipment.setHandleLv(3);
        equipment.setBasicStat(StatType.MAXHP, 100);
        equipment.setBasicStat(StatType.ATK, 10);
        equipment.setLimitLv(30);
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.AMULET, EquipList.DRAGON_AMULET, null, null);
        equipment.setHandleLv(6);
        equipment.setBasicStat(StatType.MAXHP, 200);
        equipment.setBasicStat(StatType.DEF, 30);
        equipment.setBasicStat(StatType.EVA, 20);
        equipment.setLimitLv(50);
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.HELMET, EquipList.LEATHER_HELMET,
                null, "치명타 데미지를 15% 감소시킨다");
        equipment.setBasicStat(StatType.MAXHP, 10);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.SHEEP_LEATHER.getId(), 10);
            put(ItemList.LEATHER.getId(), 5);
        }});
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.CHESTPLATE, EquipList.LEATHER_CHESTPLATE, null, null);
        equipment.setBasicStat(StatType.MAXHP, 10);
        equipment.setBasicStat(StatType.DEF, 2);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.SHEEP_LEATHER.getId(), 10);
            put(ItemList.LEATHER.getId(), 5);
        }});
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.LEGGINGS, EquipList.LEATHER_LEGGINGS, null, null);
        equipment.setBasicStat(StatType.MAXHP, 10);
        equipment.setBasicStat(StatType.MDEF, 2);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.SHEEP_LEATHER.getId(), 10);
            put(ItemList.LEATHER.getId(), 5);
        }});
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.SHOES, EquipList.LEATHER_SHOES, null, null);
        equipment.setBasicStat(StatType.MAXHP, 8);
        equipment.setBasicStat(StatType.AGI, 8);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.SHEEP_LEATHER.getId(), 10);
            put(ItemList.LEATHER.getId(), 5);
        }});
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.HELMET, EquipList.LOW_ALLOY_HELMET,
                null, "치명타 데미지를 30% 확률로 50% 감소시킨다");
        equipment.setHandleLv(2);
        equipment.setBasicStat(StatType.MAXHP, 30);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.LOW_ALLOY.getId(), 5);
            put(ItemList.GOLD.getId(), 5);
        }});
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.CHESTPLATE, EquipList.LOW_ALLOY_CHESTPLATE,
                "[4 마나]\n75% 확률로 방어를 한다", null);
        equipment.setHandleLv(2);
        equipment.setBasicStat(StatType.MAXHP, 30);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.LOW_ALLOY.getId(), 5);
            put(ItemList.GOLD.getId(), 5);
        }});
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.LEGGINGS, EquipList.LOW_ALLOY_LEGGINGS,
                "[5 마나]\n20초간 회피 15의 버프를 얻는다", null);
        equipment.setHandleLv(2);
        equipment.setBasicStat(StatType.MAXHP, 30);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.LOW_ALLOY.getId(), 5);
            put(ItemList.GOLD.getId(), 5);
        }});
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.SHOES, EquipList.LOW_ALLOY_SHOES,
                null, "공격 시 합검1 을 장착하고 있다면 10의 추가 데미지를 준다");
        equipment.setHandleLv(2);
        equipment.setBasicStat(StatType.MAXHP, 30);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.LOW_ALLOY.getId(), 5);
            put(ItemList.GOLD.getId(), 5);
        }});
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.WEAPON, EquipList.LOW_MANA_SWORD,
                null, "공격 시 마나 1을 회복한다");
        equipment.setHandleLv(2);
        equipment.setBasicStat(StatType.MAXMN, 5);
        equipment.setBasicStat(StatType.ATS, 10);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.PIECE_OF_MANA.getId(), 5);
            put(ItemList.LAPIS.getId(), 20);
            put(ItemList.RED_STONE.getId(), 20);
        }});
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.WEAPON, EquipList.QUARTZ_SWORD, null, null);
        equipment.setHandleLv(2);
        equipment.setBasicStat(StatType.ATK, 5);
        equipment.setBasicStat(StatType.MAXMN, 5);
        equipment.setBasicStat(StatType.ATS, 10);
        equipment.setBasicStat(StatType.ACC, 10);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.QUARTZ.getId(), 50);
            put(ItemList.DARK_QUARTZ.getId(), 10);
            put(ItemList.FIRE_QUARTZ.getId(), 10);
        }});
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.HELMET, EquipList.SLIME_HELMET,
                null, "피격 시 데미지의 8% 를 회복한다\n해당 공격이 치명타일 경우 15% 를 회복한다");
        equipment.setHandleLv(3);
        equipment.setBasicStat(StatType.ATS, -10);
        equipment.setBasicStat(StatType.MAXHP, 20);
        equipment.setBasicStat(StatType.ATK, 3);
        equipment.setBasicStat(StatType.DEF, 5);
        equipment.setBasicStat(StatType.BRE, 5);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.PIECE_OF_SLIME.getId(), 30);
        }});
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.CHESTPLATE, EquipList.SLIME_CHESTPLATE,
                "[5 마나]\n다음 공격이 저장한 데미지를 사용하여 추가 데미지를 입힌다",
                "피격 시 데미지의 20% 를 저장한다\n해당 공격이 치명타일 경우 40% 를 저장한다");
        equipment.setHandleLv(3);
        equipment.setBasicStat(StatType.ATS, -10);
        equipment.setBasicStat(StatType.MAXHP, 20);
        equipment.setBasicStat(StatType.ATK, 3);
        equipment.setBasicStat(StatType.DEF, 5);
        equipment.setBasicStat(StatType.BRE, 5);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.PIECE_OF_SLIME.getId(), 30);
        }});
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.LEGGINGS, EquipList.SLIME_LEGGINGS,
                "[10 마나]\n30초간 모든 적의 공격 속도를 30 감소시킨다", null);
        equipment.setHandleLv(3);
        equipment.setBasicStat(StatType.ATS, -10);
        equipment.setBasicStat(StatType.MAXHP, 30);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.PIECE_OF_SLIME.getId(), 30);
        }});
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.SHOES, EquipList.SLIME_SHOES, null, null);
        equipment.setHandleLv(2);
        equipment.setBasicStat(StatType.ATS, -10);
        equipment.setBasicStat(StatType.MAXHP, 30);
        equipment.setBasicStat(StatType.BRE, 5);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.PIECE_OF_SLIME.getId(), 30);
        }});
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.HELMET, EquipList.WOOL_HELMET, null, null);
        equipment.setBasicStat(StatType.MAXHP, 15);
        equipment.setBasicStat(StatType.MDEF, 8);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.WOOL.getId(), 10);
        }});
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.CHESTPLATE, EquipList.HARD_IRON_CHESTPLATE, null, null);
        equipment.setLimitLv(30);
        equipment.setHandleLv(5);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.HARD_IRON.getId(), 5);
        }});
        equipment.setBasicStat(StatType.MAXHP, 80);
        equipment.setBasicStat(StatType.DEF, 25);
        equipment.setBasicStat(StatType.ATS, -40);
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.LEGGINGS, EquipList.WEIRD_LEGGINGS,
                null, "피격 시 10% 확률로 데미지를 50% 감소시키고 다음 자신의 공격 데미지를 1.5배로 준다");
        equipment.setHandleLv(3);
        equipment.setBasicStat(StatType.MAXHP, 30);
        equipment.setBasicStat(StatType.ATS, 30);
        equipment.setBasicStat(StatType.DEF, 5);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.PIECE_OF_SLIME.getId(), 10);
            put(ItemList.SPIDER_LEG.getId(), 4);
        }});
        equipment.setLimitLv(25);
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.SHOES, EquipList.MINER_SHOES,
                null, "광질 시 돌을 2개씩 채광한다");
        equipment.setHandleLv(1);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.STONE.getId(), 1000);
            put(ItemList.COAL.getId(), 1000);
        }});
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.WEAPON, EquipList.TROLL_CLUB,
                "[2 마나]\n다음 공격이 자신보다 낮은 레벨의 몬스터를 공격하는 것이라면 데미지를 3배로 준다", null);
        equipment.setHandleLv(4);
        equipment.setLimitLv(30);
        equipment.setBasicStat(StatType.ATK, 30);
        equipment.setBasicStat(StatType.ATS, -20);
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.GEM, EquipList.BETA1_GEM, null, "베타에 참여해주셔서 감사합니다!");
        equipment.setHandleLv(5);
        equipment.setBasicStat(StatType.MAXHP, 5);
        equipment.setBasicStat(StatType.ATK, 5);
        equipment.setBasicStat(StatType.MATK, 5);
        equipment.setBasicStat(StatType.DEF, 5);
        equipment.setBasicStat(StatType.MDEF, 5);
        equipment.setBasicStat(StatType.BRE, 5);
        equipment.setBasicStat(StatType.MBRE, 5);
        equipment.setBasicStat(StatType.ACC, 5);
        equipment.setBasicStat(StatType.EVA, 5);
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
        equipment.setBasicStat(StatType.ATS, 20);
        equipment.setBasicStat(StatType.EVA, 10);
        equipment.setBasicStat(StatType.ACC, 20);
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.WEAPON, EquipList.BONE_SWORD,
                null, "보호구가 모두 뼈 장비 또는 마족의 뼈 장비라면 피격 시 피해를 20% 줄여서 받는다");
        equipment.setHandleLv(2);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.PIECE_OF_BONE.getId(), 100);
            put(ItemList.COAL.getId(), 10);
        }});
        equipment.setBasicStat(StatType.ATK, 5);
        equipment.setBasicStat(StatType.AGI, 5);
        equipment.setBasicStat(StatType.ACC, 5);
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.WEAPON, EquipList.BASIC_STAFF, null, null);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.BRANCH.getId(), 5);
            put(ItemList.WATER_GRASS.getId(), 5);
        }});
        equipment.setBasicStat(StatType.MATK, 3);
        equipment.setBasicStat(StatType.MAXMN, 1);
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
        equipment.setBasicStat(StatType.MATK, 8);
        equipment.setBasicStat(StatType.MBRE, 3);
        equipment.setBasicStat(StatType.MDRA, 5);
        equipment.setBasicStat(StatType.MAXMN, 2);
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
        equipment.setBasicStat(StatType.MATK, 10);
        equipment.setBasicStat(StatType.ATS, 5);
        equipment.setBasicStat(StatType.MBRE, 5);
        equipment.setBasicStat(StatType.MDRA, 5);
        equipment.setBasicStat(StatType.MAXMN, 3);
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.HELMET, EquipList.BONE_HELMET, null, null);
        equipment.setHandleLv(3);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.PIECE_OF_BONE.getId(), 80);
            put(ItemList.COAL.getId(), 10);
        }});
        equipment.setBasicStat(StatType.MAXHP, 10);
        equipment.setBasicStat(StatType.DEF, 3);
        equipment.setBasicStat(StatType.MDEF, 3);
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.CHESTPLATE, EquipList.BONE_CHESTPLATE, null, null);
        equipment.setHandleLv(3);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.PIECE_OF_BONE.getId(), 100);
            put(ItemList.COAL.getId(), 10);
        }});
        equipment.setBasicStat(StatType.DEF, 6);
        equipment.setBasicStat(StatType.MDEF, 6);
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.LEGGINGS, EquipList.BONE_LEGGINGS, null, null);
        equipment.setHandleLv(3);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.PIECE_OF_BONE.getId(), 90);
            put(ItemList.COAL.getId(), 10);
        }});
        equipment.setBasicStat(StatType.MAXHP, 20);
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.SHOES, EquipList.BONE_SHOES, null, null);
        equipment.setHandleLv(3);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.PIECE_OF_BONE.getId(), 70);
            put(ItemList.COAL.getId(), 10);
        }});
        equipment.setBasicStat(StatType.MAXHP, 5);
        equipment.setBasicStat(StatType.DEF, 3);
        equipment.setBasicStat(StatType.MDEF, 3);
        equipment.setBasicStat(StatType.EVA, 5);
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.RINGS, EquipList.DEVIL_RING, null, null);
        equipment.setHandleLv(4);
        equipment.setBasicStat(StatType.MATK, 20);
        equipment.setBasicStat(StatType.MDRA, 10);
        equipment.setBasicStat(StatType.ATS, 10);
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.WEAPON, EquipList.HEART_BREAKER_2,
                null, "공격 시 체력이 15% 미만인 적을 처형시킨다");
        equipment.setHandleLv(6);
        equipment.setLimitLv(70);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.TITANIUM.getId(), 20);
            put(ItemList.HARD_COAL.getId(), 10);
            put(ItemList.IMP_HEART.getId(), 10);
        }});
        equipment.setBasicStat(StatType.ATK, 30);
        equipment.setBasicStat(StatType.ATS, 40);
        equipment.setBasicStat(StatType.ACC, 20);
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.WEAPON, EquipList.GHOST_SWORD_2,
                "[최대 체력 6%]\n다음 공격의 데미지를 100% 증가시킨다",
                "공격시 적 최대 체력의 6%에 해당하는 추가 데미지를 입힌다");
        equipment.setHandleLv(6);
        equipment.setLimitLv(70);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.LOW_DEVIL_SOUL.getId(), 1);
            put(ItemList.LIQUID_STONE.getId(), 20);
            put(ItemList.HARD_COAL.getId(), 10);
        }});
        equipment.setBasicStat(StatType.ATS, 100);
        equipment.setBasicStat(StatType.ATK, 15);
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.HELMET, EquipList.DEMON_BONE_HELMET, null, null);
        equipment.setHandleLv(5);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.HORN_OF_IMP.getId(), 5);
            put(ItemList.HORN_OF_LOW_DEVIL.getId(), 5);
            put(ItemList.COAL.getId(), 80);
        }});
        equipment.setBasicStat(StatType.MAXHP, 20);
        equipment.setBasicStat(StatType.DEF, 5);
        equipment.setBasicStat(StatType.MDEF, 5);
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.CHESTPLATE, EquipList.DEMON_BONE_CHESTPLATE, null, null);
        equipment.setHandleLv(5);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.HORN_OF_IMP.getId(), 5);
            put(ItemList.HORN_OF_LOW_DEVIL.getId(), 5);
            put(ItemList.COAL.getId(), 100);
        }});
        equipment.setBasicStat(StatType.DEF, 10);
        equipment.setBasicStat(StatType.MDEF, 10);
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.LEGGINGS, EquipList.DEMON_BONE_LEGGINGS, null, null);
        equipment.setHandleLv(5);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.HORN_OF_IMP.getId(), 5);
            put(ItemList.HORN_OF_LOW_DEVIL.getId(), 5);
            put(ItemList.COAL.getId(), 90);
        }});
        equipment.setBasicStat(StatType.MAXHP, 30);
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.SHOES, EquipList.DEMON_BONE_SHOES, null, null);
        equipment.setHandleLv(5);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.HORN_OF_IMP.getId(), 5);
            put(ItemList.HORN_OF_LOW_DEVIL.getId(), 5);
            put(ItemList.COAL.getId(), 70);
        }});
        equipment.setBasicStat(StatType.MAXHP, 10);
        equipment.setBasicStat(StatType.DEF, 5);
        equipment.setBasicStat(StatType.MDEF, 5);
        equipment.setBasicStat(StatType.EVA, 10);
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.WEAPON, EquipList.HEAD_HUNTER_2,
                null, "치명타 미 발동 시 50% 확률로 치명타를 발동시킨다");
        equipment.setHandleLv(6);
        equipment.setLimitLv(70);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.HARD_COAL.getId(), 10);
            put(ItemList.OWLBEAR_HEAD.getId(), 10);
        }});
        equipment.setBasicStat(StatType.ATK, 25);
        equipment.setBasicStat(StatType.ATS, 25);
        equipment.setBasicStat(StatType.ACC, 25);
        equipment.setBasicStat(StatType.AGI, 25);
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.WEAPON, EquipList.YIN_YANG_SWORD_1, null,
                "매 공격마다 음/양 효과가 변경된다(전투 시작 시 " + Emoji.focus("음") + " 으로 초기화)\n" +
                        "음: 스킬 사용 시 적 최대 체력의 5% 고정데미지\n" +
                        "양: 일반 공격 시 최대 체력의 5% 회복");
        equipment.setHandleLv(3);
        equipment.setLimitLv(20);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.LAPIS.getId(), 30);
            put(ItemList.RED_STONE.getId(), 30);
            put(ItemList.EMPTY_SPHERE.getId(), 5);
        }});
        equipment.setBasicStat(StatType.ATK, 10);
        equipment.setBasicStat(StatType.MATK, 10);
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.WEAPON, EquipList.SILVER_SWORD,
                "[5 마나]\n대상에게 마법 공격력만큼의 마법 데미지를 가하고, 다음 대상의 공격으로 인한 회복량을 80% 감소시킨다",
                "은 보호구 및 은 반지를 모두 착용할 시 상대방의 공격으로 인한 회복량을 50% 감소시킨다");
        equipment.setHandleLv(4);
        equipment.setLimitLv(35);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.SILVER.getId(), 50);
        }});
        equipment.setBasicStat(StatType.MATK, 15);
        equipment.setBasicStat(StatType.ACC, 30);
        equipment.setBasicStat(StatType.MBRE, 20);
        equipment.setBasicStat(StatType.MDRA, 20);
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.HELMET, EquipList.SILVER_HELMET,
                null, "피격 전 해당 전투에서의 마법 저항력을 10씩 증가시킨다(최대 50)");
        equipment.setHandleLv(4);
        equipment.setLimitLv(35);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.SILVER.getId(), 30);
        }});
        equipment.setBasicStat(StatType.MAXHP, 20);
        equipment.setBasicStat(StatType.MAXMN, 5);
        equipment.setBasicStat(StatType.DEF, 10);
        equipment.setBasicStat(StatType.MDEF, 20);
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.CHESTPLATE, EquipList.SILVER_CHESTPLATE,
                "[3 마나] [치명타 불가]\n다음 피격 시 마법 데미지가 포함되어 있다면 물리 데미지와 마법 데미지의 30%를 반사하고 " +
                        "마법 데미지를 50% 감소시킨다", null);
        equipment.setHandleLv(4);
        equipment.setLimitLv(35);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.SILVER.getId(), 50);
        }});
        equipment.setBasicStat(StatType.MAXHP, 50);
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.LEGGINGS, EquipList.SILVER_LEGGINGS, null, null);
        equipment.setHandleLv(4);
        equipment.setLimitLv(35);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.SILVER.getId(), 40);
        }});
        equipment.setBasicStat(StatType.MAXHP, 30);
        equipment.setBasicStat(StatType.MDEF, 10);
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.SHOES, EquipList.SILVER_SHOES,
                null, "피격 전 해당 전투에서의 민첩을 10씩 증가시킨다(최대 50)");
        equipment.setHandleLv(4);
        equipment.setLimitLv(35);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.SILVER.getId(), 30);
        }});
        equipment.setBasicStat(StatType.MAXHP, 30);
        equipment.setBasicStat(StatType.MDEF, 5);
        equipment.setBasicStat(StatType.AGI, 10);
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.WEAPON, EquipList.YIN_YANG_SWORD_2, null,
                "매 공격마다 음/양 효과가 변경된다(전투 시작 시 " + Emoji.focus("음") + " 으로 초기화)\n" +
                        "음: 스킬 사용 시 적 최대 체력의 10% 고정데미지\n" +
                        "양: 일반 공격 시 최대 체력의 10% 회복");
        equipment.setHandleLv(6);
        equipment.setLimitLv(50);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.GLOW_LAPIS.getId(), 30);
            put(ItemList.GLOW_RED_STONE.getId(), 30);
            put(ItemList.EMPTY_SPHERE.getId(), 20);
            put(ItemList.HARD_COAL.getId(), 10);
        }});
        equipment.setBasicStat(StatType.ATK, 30);
        equipment.setBasicStat(StatType.MATK, 30);
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.WEAPON, EquipList.MIX_SWORD_2,
                "[10 마나]\n잃은 체력의 50%를 회복한다",
                "공격 시 33% 확률로 데미지 1.5배, 33% 확률로 33 데미지를 준다");
        equipment.setHandleLv(5);
        equipment.setLimitLv(40);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.ALLOY.getId(), 5);
        }});
        equipment.setBasicStat(StatType.ATK, 20);
        equipment.setBasicStat(StatType.ATS, 20);
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.HELMET, EquipList.ALLOY_HELMET,
                null, "치명타 데미지를 50% 확률로 50% 감소시킨다");
        equipment.setHandleLv(5);
        equipment.setLimitLv(40);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.ALLOY.getId(), 5);
            put(ItemList.WHITE_GOLD.getId(), 5);
        }});
        equipment.setBasicStat(StatType.MAXHP, 60);
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.CHESTPLATE, EquipList.ALLOY_CHESTPLATE,
                "[5 마나]\n85% 확률로 방어를 한다", null);
        equipment.setHandleLv(5);
        equipment.setLimitLv(40);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.ALLOY.getId(), 5);
            put(ItemList.WHITE_GOLD.getId(), 5);
        }});
        equipment.setBasicStat(StatType.MAXHP, 60);
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.LEGGINGS, EquipList.ALLOY_LEGGINGS,
                "[5 마나]\n30초간 회피 60의 버프를 얻는다", null);
        equipment.setHandleLv(5);
        equipment.setLimitLv(40);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.ALLOY.getId(), 5);
            put(ItemList.WHITE_GOLD.getId(), 5);
        }});
        equipment.setBasicStat(StatType.MAXHP, 60);
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.SHOES, EquipList.ALLOY_SHOES,
                null, "공격 시 합검2 를 장착하고 있다면 40의 추가 데미지를 준다");
        equipment.setHandleLv(5);
        equipment.setLimitLv(40);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.ALLOY.getId(), 5);
            put(ItemList.WHITE_GOLD.getId(), 5);
        }});
        equipment.setBasicStat(StatType.MAXHP, 60);
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.HELMET, EquipList.TITANIUM_HELMET, null, null);
        equipment.setHandleLv(5);
        equipment.setLimitLv(50);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.TITANIUM.getId(), 20);
            put(ItemList.HARD_COAL.getId(), 7);
        }});
        equipment.setBasicStat(StatType.MAXHP, 60);
        equipment.setBasicStat(StatType.DEF, 35);
        equipment.setBasicStat(StatType.MDEF, 10);
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.CHESTPLATE, EquipList.TITANIUM_CHESTPLATE, null, null);
        equipment.setHandleLv(5);
        equipment.setLimitLv(50);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.TITANIUM.getId(), 20);
            put(ItemList.HARD_COAL.getId(), 7);
        }});
        equipment.setBasicStat(StatType.MAXHP, 60);
        equipment.setBasicStat(StatType.DEF, 35);
        equipment.setBasicStat(StatType.MDEF, 10);
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.LEGGINGS, EquipList.TITANIUM_LEGGINGS, null, null);
        equipment.setHandleLv(5);
        equipment.setLimitLv(50);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.TITANIUM.getId(), 20);
            put(ItemList.HARD_COAL.getId(), 7);
        }});
        equipment.setBasicStat(StatType.MAXHP, 60);
        equipment.setBasicStat(StatType.DEF, 35);
        equipment.setBasicStat(StatType.MDEF, 10);
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.SHOES, EquipList.TITANIUM_SHOES, null, null);
        equipment.setHandleLv(5);
        equipment.setLimitLv(50);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.TITANIUM.getId(), 20);
            put(ItemList.HARD_COAL.getId(), 7);
        }});
        equipment.setBasicStat(StatType.MAXHP, 60);
        equipment.setBasicStat(StatType.DEF, 35);
        equipment.setBasicStat(StatType.MDEF, 10);
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.WEAPON, EquipList.HARPY_NAIL_GAUNTLETS,
                null, "스킬 " + SkillList.SCAR.getDisplayName() + " 를 사용할 때 3회가 아닌 5회 타격한다");
        equipment.setHandleLv(5);
        equipment.setLimitLv(40);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.HARPY_NAIL.getId(), 20);
            put(ItemList.COAL.getId(), 100);
        }});
        equipment.setBasicStat(StatType.ATK, 30);
        equipment.setBasicStat(StatType.ATS, 30);
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.HELMET, EquipList.OWLBEAR_LEATHER_HELMET,
                null, "세트 효과 : 아울베어 신발 참고");
        equipment.setHandleLv(6);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.OWLBEAR_LEATHER.getId(), 20);
            put(ItemList.PIECE_OF_SLIME.getId(), 20);
        }});
        equipment.setBasicStat(StatType.MAXHP, 50);
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.CHESTPLATE, EquipList.OWLBEAR_LEATHER_CHESTPLATE,
                null, "세트 효과 : 아울베어 신발 참고");
        equipment.setHandleLv(6);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.OWLBEAR_LEATHER.getId(), 20);
            put(ItemList.PIECE_OF_SLIME.getId(), 20);
        }});
        equipment.setBasicStat(StatType.DEF, 15);
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.LEGGINGS, EquipList.OWLBEAR_LEATHER_LEGGINGS,
                null, "세트 효과 : 아울베어 신발 참고");
        equipment.setHandleLv(6);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.OWLBEAR_LEATHER.getId(), 20);
            put(ItemList.PIECE_OF_SLIME.getId(), 20);
        }});
        equipment.setBasicStat(StatType.MDEF, 15);
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.SHOES, EquipList.OWLBEAR_LEATHER_SHOES,
                null, "모든 방어구가 아울베어 방어구일 때 받는 모든 데미지를 50% 감소시켜 받고, " +
                "가하는 모든 데미지를 2배로 가한다");
        equipment.setHandleLv(6);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.OWLBEAR_LEATHER.getId(), 20);
            put(ItemList.PIECE_OF_SLIME.getId(), 20);
        }});
        equipment.setBasicStat(StatType.EVA, 30);
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.HELMET, EquipList.HARDENED_SLIME_HELMET,
                null, "피격 시 데미지의 15% 를 회복한다\n해당 공격이 치명타일 경우 25% 를 회복한다");
        equipment.setHandleLv(5);
        equipment.setLimitLv(30);
        equipment.setBasicStat(StatType.ATS, -20);
        equipment.setBasicStat(StatType.MAXHP, 40);
        equipment.setBasicStat(StatType.ATK, 10);
        equipment.setBasicStat(StatType.DEF, 15);
        equipment.setBasicStat(StatType.BRE, 15);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.HARDENED_SLIME.getId(), 30);
        }});
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.CHESTPLATE, EquipList.HARDENED_SLIME_CHESTPLATE,
                "[5 마나]\n다음 공격이 저장한 데미지를 사용하여 추가 데미지를 입힌다",
                "피격 시 데미지의 30% 를 저장한다\n해당 공격이 치명타일 경우 55% 를 저장한다");
        equipment.setHandleLv(5);
        equipment.setLimitLv(30);
        equipment.setBasicStat(StatType.ATS, -20);
        equipment.setBasicStat(StatType.MAXHP, 40);
        equipment.setBasicStat(StatType.ATK, 10);
        equipment.setBasicStat(StatType.DEF, 15);
        equipment.setBasicStat(StatType.BRE, 15);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.HARDENED_SLIME.getId(), 30);
        }});
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.LEGGINGS, EquipList.HARDENED_SLIME_LEGGINGS,
                "[10 마나]\n30초간 모든 적의 공격 속도를 60 감소시킨다", null);
        equipment.setHandleLv(5);
        equipment.setLimitLv(30);
        equipment.setBasicStat(StatType.ATS, -20);
        equipment.setBasicStat(StatType.MAXHP, 60);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.HARDENED_SLIME.getId(), 30);
        }});
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.SHOES, EquipList.HARDENED_SLIME_SHOES, null, null);
        equipment.setHandleLv(5);
        equipment.setLimitLv(30);
        equipment.setBasicStat(StatType.ATS, -20);
        equipment.setBasicStat(StatType.MAXHP, 60);
        equipment.setBasicStat(StatType.BRE, 20);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.HARDENED_SLIME.getId(), 30);
        }});
        Config.unloadObject(equipment);
        
        equipment = new Equipment(EquipType.HEART_GEM, EquipList.ELEMENT_HEART_GEM,
                null, "스킬에 의해 피격당할 시 공기/땅/물/불 중 1가지의 랜덤한 효과가 발동한다\n" +
                "공기: 30% 의 확률로 데미지를 0으로 만든다\n" +
                "땅: 데미지를 30% 감소시킨다\n" +
                "물: 공격자의 공격 속도를 1턴간 30% 감소시킨다\n" +
                "블: 자신의 마법 공격력의 15%에 해당하는 마법 데미지를 3턴간 가하는 화염을 공격자에게 부여한다");
        equipment.setHandleLv(6);
        equipment.setLimitLv(50);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.TRACE_OF_SKY.getId(), 1);
            put(ItemList.TRACE_OF_EARTH.getId(), 1);
            put(ItemList.TRACE_OF_WATER.getId(), 1);
            put(ItemList.TRACE_OF_FIRE.getId(), 1);
        }});
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.HEART_GEM, EquipList.GOLEM_HEART_GEM,
                null, "전투 시작 시 최대 체력의 80% 에 해당하는 방어막과 마법 방어막을 각각 생성합니다\n" +
                "(최소 데미지 1은 계속 적용) (방어력 및 마법 방어력 무시)");
        equipment.setHandleLv(6);
        equipment.setLimitLv(50);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.GOLEM_CORE.getId(), 50);
        }});
        Config.unloadObject(equipment);
        
        equipment = new Equipment(EquipType.AMULET, EquipList.REGENERATION_AMULET, 
                null, "매 턴 마다 최대 체력의 3% 를 회복한다");
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.AMULET, EquipList.STRENGTH_AMULET, null, null);
        equipment.setBasicStat(StatType.ATK, 7);
        equipment.setBasicStat(StatType.MATK, 7);
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.AMULET, EquipList.PROTECTION_AMULET, null, null);
        equipment.setBasicStat(StatType.DEF, 12);
        equipment.setBasicStat(StatType.MDEF, 12);
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.AMULET, EquipList.NATURE_AMULET,
                null, "매 턴 마다 최대 체력의 5.5% 를 회복한다");
        equipment.setHandleLv(4);
        equipment.setLimitLv(30);
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.AMULET, EquipList.DEVIL_AMULET, null, null);
        equipment.setHandleLv(6);
        equipment.setLimitLv(50);
        equipment.setBasicStat(StatType.MATK, 30);
        equipment.setBasicStat(StatType.MDRA, 25);
        equipment.setBasicStat(StatType.MBRE, 25);
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.RINGS, EquipList.SILVER_RING,
                null, "피격 시 20% 확률로 마법 데미지의 20% 를 반사한다");
        equipment.setHandleLv(4);
        equipment.setLimitLv(30);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.SILVER.getId(), 20);
        }});
        equipment.setBasicStat(StatType.DEF, 25);
        equipment.setBasicStat(StatType.MDEF, 25);
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.NECKLACE, EquipList.HARPY_NAIL_NECKLACE,
                null, "스킬 " + SkillList.SCAR.getDisplayName() + " 를 사용할 때 출혈의 데미지가 20% 증가한다");
        equipment.setHandleLv(5);
        equipment.setLimitLv(40);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.HARPY_NAIL.getId(), 20);
            put(ItemList.COAL.getId(), 100);
        }});
        equipment.setBasicStat(StatType.MAXMN, 15);
        equipment.setBasicStat(StatType.ATS, 30);
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.EARRINGS, EquipList.GARNET_EARRING,
                null, "공격 시 물리 데미지와 마법 데미지의 7%가 고정 데미지로 변환된다");
        equipment.setHandleLv(3);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.GARNET.getId(), 5);
        }});
        equipment.setBasicStat(StatType.MAXMN, 5);
        equipment.setBasicStat(StatType.ATK, 5);
        equipment.setBasicStat(StatType.MATK, 5);
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.EARRINGS, EquipList.AMETHYST_EARRING,
                null, "농사 수확에 필요한 작물 성장 시간이 10% 감소한다");
        equipment.setHandleLv(3);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.AMETHYST.getId(), 5);
        }});
        equipment.setBasicStat(StatType.MAXMN, 5);
        equipment.setBasicStat(StatType.DEF, 8);
        equipment.setBasicStat(StatType.MDEF, 8);
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.EARRINGS, EquipList.AQUAMARINE_EARRING,
                null, "휴식의 시간이 5분으로 변경된다");
        equipment.setHandleLv(3);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.AQUAMARINE.getId(), 5);
        }});
        equipment.setBasicStat(StatType.MAXMN, 5);
        equipment.setBasicStat(StatType.MAXHP, 40);
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.EARRINGS, EquipList.DIAMOND_EARRING,
                null, "공격으로 인한 회복량이 10% 증가한다");
        equipment.setHandleLv(3);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.DIAMOND.getId(), 5);
        }});
        equipment.setBasicStat(StatType.MAXMN, 5);
        equipment.setBasicStat(StatType.DRA, 5);
        equipment.setBasicStat(StatType.MDRA, 5);
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.EARRINGS, EquipList.EMERALD_EARRING,
                null, "몬스터 및 보스 전리품 확률이 15% 증가한다\n(예시: 10% -> 11.5%)");
        equipment.setHandleLv(3);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.EMERALD.getId(), 5);
        }});
        equipment.setBasicStat(StatType.MAXMN, 5);
        equipment.setBasicStat(StatType.BRE, 10);
        equipment.setBasicStat(StatType.MBRE, 10);
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.EARRINGS, EquipList.PEARL_EARRING,
                null, "전투 시작 시 현재 체력이 최대 체력이라면 해당 전투에서 공격속도가 8% 증가한다");
        equipment.setHandleLv(3);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.PEARL.getId(), 5);
        }});
        equipment.setBasicStat(StatType.MAXMN, 5);
        equipment.setBasicStat(StatType.ATS, 40);
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.EARRINGS, EquipList.RUBY_EARRING,
                "[10 마나]\n다음 공격으로 인한 회복량이 3배가 된다", null);
        equipment.setHandleLv(3);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.RUBY.getId(), 5);
        }});
        equipment.setBasicStat(StatType.MAXMN, 5);
        equipment.setBasicStat(StatType.DRA, 5);
        equipment.setBasicStat(StatType.MDRA, 5);
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.EARRINGS, EquipList.PERIDOT_EARRING,
                null, "피격 시 전투 중인 인원이 본인 포함 2개체라면 데미지를 8% 줄여 받는다");
        equipment.setHandleLv(3);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.PERIDOT.getId(), 5);
        }});
        equipment.setBasicStat(StatType.MAXMN, 5);
        equipment.setBasicStat(StatType.DEF, 8);
        equipment.setBasicStat(StatType.MDEF, 8);
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.EARRINGS, EquipList.SAPPHIRE_EARRING,
                null, "광질 및 낚시로 얻는 아이템의 양이 10% 확률로 2배가 된다");
        equipment.setHandleLv(3);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.SAPPHIRE.getId(), 5);
        }});
        equipment.setBasicStat(StatType.MAXMN, 5);
        equipment.setBasicStat(StatType.ACC, 20);
        equipment.setBasicStat(StatType.EVA, 20);
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.EARRINGS, EquipList.OPAL_EARRING,
                null, "공격 시 상대와 자신의 체력차가 많이 날 수록(자신의 체력이 적을수록) 가하는 데미지가 증가한다" +
                "(체력차 1% 당 1.5% 증가) (최대 75%)");
        equipment.setHandleLv(3);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.OPAL.getId(), 5);
        }});
        equipment.setBasicStat(StatType.MAXMN, 5);
        equipment.setBasicStat(StatType.ATK, 5);
        equipment.setBasicStat(StatType.MATK, 5);
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.EARRINGS, EquipList.TOPAZ_EARRING,
                null, "매 턴 마다 잃은 체력에 비례하여 체력을 회복한다(잃은 체력 2% 당 최대 체력의 0.18%)");
        equipment.setHandleLv(3);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.TOPAZ.getId(), 5);
        }});
        equipment.setBasicStat(StatType.MAXMN, 5);
        equipment.setBasicStat(StatType.MAXHP, 15);
        equipment.setBasicStat(StatType.DEF, 5);
        equipment.setBasicStat(StatType.MDEF, 5);
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.EARRINGS, EquipList.TURQUOISE_EARRING,
                null, "전투로 획득하는 경험치가 8% 증가한다");
        equipment.setHandleLv(3);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.TURQUOISE.getId(), 5);
        }});
        equipment.setBasicStat(StatType.MAXMN, 5);
        equipment.setBasicStat(StatType.ATK, 4);
        equipment.setBasicStat(StatType.MATK, 4);
        equipment.setBasicStat(StatType.ATS, 10);
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.WEAPON, EquipList.MOON_SWORD,
                null, "잃은 체력에 비례하여 가하는 모든 데미지가 증가한다(1% 당 1% 증가) (최대 35%)");
        equipment.setHandleLv(8);
        equipment.setBasicStat(StatType.ATK, 20);
        equipment.setBasicStat(StatType.MATK, 20);
        equipment.setBasicStat(StatType.ATS, 30);
        equipment.setBasicStat(StatType.AGI, 20);
        equipment.setBasicStat(StatType.DRA, 10);
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.GEM, EquipList.MOON_GEM,
                null, "월검을 장착하고 있을 시 월검의 패시브가 2배로 강화된다");
        equipment.setHandleLv(8);
        equipment.setBasicStat(StatType.MAXHP, 50);
        equipment.setBasicStat(StatType.MAXMN, 20);
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.HELMET, EquipList.LYCANTHROPE_HELMET, null,
                "치명타 피격 시 체력이 높을수록 데미지를 감소하여 받는다(50% 부터 1% 당 1% 감소)");
        equipment.setHandleLv(7);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.LYCANTHROPE_LEATHER.getId(), 50);
            put(ItemList.MOON_STONE.getId(), 1);
        }});
        equipment.setBasicStat(StatType.MAXHP, 50);
        equipment.setBasicStat(StatType.MAXMN, 5);
        equipment.setBasicStat(StatType.DEF, 30);
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.CHESTPLATE, EquipList.LYCANTHROPE_CHESTPLATE,
                "[3 마나]\n잃은 체력에 비례하여 1분간 공격력과 마법 공격력을 증가시킨다(잃은 체력 1% 당 각각 1)",null);
        equipment.setHandleLv(7);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.LYCANTHROPE_LEATHER.getId(), 50);
            put(ItemList.MOON_STONE.getId(), 1);
        }});
        equipment.setBasicStat(StatType.MAXHP, 60);
        equipment.setBasicStat(StatType.ATK, 10);
        equipment.setBasicStat(StatType.MATK, 10);
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.LEGGINGS, EquipList.LYCANTHROPE_LEGGINGS,
                null, "치명타 데미지가 10% 증가한다");
        equipment.setHandleLv(7);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.LYCANTHROPE_LEATHER.getId(), 50);
            put(ItemList.MOON_STONE.getId(), 1);
        }});
        equipment.setBasicStat(StatType.MAXHP, 45);
        equipment.setBasicStat(StatType.MAXMN, 5);
        equipment.setBasicStat(StatType.AGI, 20);
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.SHOES, EquipList.LYCANTHROPE_SHOES, null,
                "회피 시 최대 체력의 10% 를 회복한다");
        equipment.setHandleLv(7);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.LYCANTHROPE_LEATHER.getId(), 50);
            put(ItemList.MOON_STONE.getId(), 1);
        }});
        equipment.setBasicStat(StatType.MAXHP, 70);
        equipment.setBasicStat(StatType.EVA, 20);
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.NECKLACE, EquipList.LYCANTHROPE_TOOTH_NECKLACE, null,
                "모든 방어구가 라이칸스로프 방어구일 때 전투 승리 시 공격력과 마법 공격력을 영구적으로 0.1 상승시킨다(최대 30)");
        equipment.setHandleLv(7);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.LYCANTHROPE_TOOTH.getId(), 30);
            put(ItemList.BRANCH.getId(), 30);
            put(ItemList.WATER_GRASS.getId(), 30);
            put(ItemList.HARDENED_SLIME.getId(), 50);
        }});
        equipment.setBasicStat(StatType.MAXHP, 30);
        equipment.setBasicStat(StatType.ATK, 10);
        equipment.setBasicStat(StatType.MATK, 10);
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.HELMET, EquipList.BASIC_HELMET, null, null);
        equipment.setBasicStat(StatType.MAXHP, 5);
        equipment.setBasicStat(StatType.DEF, 1);
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.CHESTPLATE, EquipList.BASIC_CHESTPLATE, null, null);
        equipment.setBasicStat(StatType.MAXHP, 5);
        equipment.setBasicStat(StatType.DEF, 1);
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.LEGGINGS, EquipList.BASIC_LEGGINGS, null, null);
        equipment.setBasicStat(StatType.MAXHP, 5);
        equipment.setBasicStat(StatType.DEF, 1);
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.SHOES, EquipList.BASIC_SHOES, null, null);
        equipment.setBasicStat(StatType.MAXHP, 5);
        equipment.setBasicStat(StatType.DEF, 1);
        Config.unloadObject(equipment);

        Config.ID_COUNT.put(Id.EQUIPMENT, Math.max(Config.ID_COUNT.get(Id.EQUIPMENT), 279L));
        Logger.i("ObjectMaker", "Equipment making is done!");
    }

}
