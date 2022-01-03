package lkd.namsic.game.creator;

import androidx.annotation.NonNull;

import java.util.LinkedHashMap;

import lkd.namsic.game.config.Config;
import lkd.namsic.game.config.Emoji;
import lkd.namsic.game.enums.StatType;
import lkd.namsic.game.enums.object.ItemList;
import lkd.namsic.game.enums.object.SkillList;
import lkd.namsic.game.object.Item;
import lkd.namsic.setting.Logger;

public class ItemCreator implements Creatable {

    private void createItem(@NonNull ItemList itemData, @NonNull String description) {
        Item item = new Item(itemData, description);
        Config.unloadObject(item);
    }

    @Override
    public void start() {
        Item item;

        createItem(ItemList.COBBLE_STONE, "평범한 돌멩이다");
        createItem(ItemList.BRANCH, "평범한 나뭇가지다");
        createItem(ItemList.LEAF, "평범한 나뭇잎이다");
        createItem(ItemList.GRASS, "평범한 잡초다");

        item = new Item(ItemList.SMALL_GOLD_BAG, "골드가 소량 들어간 주머니다");
        item.addRecipe(new LinkedHashMap<Long, Integer>() {{
            put(ItemList.GOLD_FRUIT.getId(), 3);
        }});
        Config.unloadObject(item);

        createItem(ItemList.GOLD_BAG, "골드가 들어간 주머니다");

        createItem(ItemList.HERB, "약의 기운이 있는 풀이다");

        item = new Item(ItemList.PIECE_OF_MANA, "마나의 기운이 있는 고체 마나 파편이다");
        for(long itemId = ItemList.RED_SPHERE.getId(); itemId < ItemList.WHITE_SPHERE.getId(); itemId++) {
            long itemId_ = itemId;

            item.addRecipe(new LinkedHashMap<Long, Integer>() {{
                put(ItemList.NONE.getId(), 3);

                put(itemId_, 1);
            }});
        }
        Config.unloadObject(item);

        createItem(ItemList.DIRT, "땅에서 흔히 볼 수 있는 평번한 흙이다");
        createItem(ItemList.SAND, "땅에서 흔히 볼 수 있는 평번한 모래다");

        item = new Item(ItemList.GLASS, "투명한 고체 물질이다");
        item.addRecipe(new LinkedHashMap<Long, Integer>() {{
            put(ItemList.SAND.getId(), 3);
            put(ItemList.COAL.getId(), 1);
        }});
        Config.unloadObject(item);

        item = new Item(ItemList.GLASS_BOTTLE, "유리를 가공하여 만든 병이다");
        item.addRecipe(new LinkedHashMap<Long, Integer>() {{
            put(ItemList.GLASS.getId(), 2);
            put(ItemList.COAL.getId(), 1);
        }});
        Config.unloadObject(item);

        item = new Item(ItemList.LOW_HP_POTION, "최대 체력의 15%를 회복시켜주는 포션이다");
        item.addRecipe(new LinkedHashMap<Long, Integer>() {{
            put(ItemList.LEAF.getId(), 3);
            put(ItemList.HERB.getId(), 3);
            put(ItemList.GLASS_BOTTLE.getId(), 1);
            put(ItemList.COAL.getId(), 1);
        }});
        item.addRecipe(new LinkedHashMap<Long, Integer>() {{
            put(ItemList.NONE.getId(), 3);

            put(ItemList.HP_POTION.getId(), 1);
        }});
        Config.unloadObject(item);

        item = new Item(ItemList.HP_POTION, "최대 체력의 50%를 회복시켜주는 포션이다");
        item.addRecipe(new LinkedHashMap<Long, Integer>() {{
            put(ItemList.HERB.getId(), 5);
            put(ItemList.LOW_HP_POTION.getId(), 1);
            put(ItemList.RED_STONE.getId(), 3);
            put(ItemList.GLOW_STONE.getId(), 1);
            put(ItemList.FIRE_QUARTZ.getId(), 3);
        }});
        item.addRecipe(new LinkedHashMap<Long, Integer>() {{
            put(ItemList.NONE.getId(), 2);

            put(ItemList.HIGH_HP_POTION.getId(), 1);
        }});
        Config.unloadObject(item);

        item = new Item(ItemList.HIGH_HP_POTION, "체력을 모두 회복시켜주는 포션이다");
        item.addRecipe(new LinkedHashMap<Long, Integer>() {{
            put(ItemList.HERB.getId(), 30);
            put(ItemList.HP_POTION.getId(), 1);
            put(ItemList.RED_STONE.getId(), 10);
            put(ItemList.GLOW_STONE.getId(), 10);
            put(ItemList.HARD_COAL.getId(), 1);
            put(ItemList.LIQUID_STONE.getId(), 3);
        }});
        item.addRecipe(new LinkedHashMap<Long, Integer>() {{
            put(ItemList.HIGH_MP_POTION.getId(), 1);
            put(ItemList.LAPIS_RED_STONE.getId(), 1);
        }});
        Config.unloadObject(item);

        item = new Item(ItemList.LOW_MP_POTION, "최대 마나의 15%를 회복시켜주는 포션이다");
        item.addRecipe(new LinkedHashMap<Long, Integer>() {{
            put(ItemList.LEAF.getId(), 3);
            put(ItemList.PIECE_OF_MANA.getId(), 3);
            put(ItemList.GLASS_BOTTLE.getId(), 1);
            put(ItemList.COAL.getId(), 1);
        }});
        item.addRecipe(new LinkedHashMap<Long, Integer>() {{
            put(ItemList.NONE.getId(), 3);

            put(ItemList.MP_POTION.getId(), 1);
        }});
        Config.unloadObject(item);

        item = new Item(ItemList.MP_POTION, "최대 마나의 50%를 회복시켜주는 포션이다");
        item.addRecipe(new LinkedHashMap<Long, Integer>() {{
            put(ItemList.PIECE_OF_MANA.getId(), 5);
            put(ItemList.LOW_MP_POTION.getId(), 1);
            put(ItemList.LAPIS.getId(), 3);
            put(ItemList.GLOW_STONE.getId(), 1);
            put(ItemList.FIRE_QUARTZ.getId(), 3);
        }});
        item.addRecipe(new LinkedHashMap<Long, Integer>() {{
            put(ItemList.NONE.getId(), 2);

            put(ItemList.HIGH_MP_POTION.getId(), 1);
        }});
        Config.unloadObject(item);

        item = new Item(ItemList.HIGH_MP_POTION, "마나를 모두 회복시켜주는 포션이다");
        item.addRecipe(new LinkedHashMap<Long, Integer>() {{
            put(ItemList.PIECE_OF_MANA.getId(), 30);
            put(ItemList.MP_POTION.getId(), 1);
            put(ItemList.LAPIS.getId(), 10);
            put(ItemList.GLOW_STONE.getId(), 10);
            put(ItemList.HARD_COAL.getId(), 1);
            put(ItemList.LIQUID_STONE.getId(), 3);
        }});
        item.addRecipe(new LinkedHashMap<Long, Integer>() {{
            put(ItemList.HIGH_HP_POTION.getId(), 1);
            put(ItemList.LAPIS_RED_STONE.getId(), 1);
        }});
        Config.unloadObject(item);

        item = new Item(ItemList.STONE, "가장 기본적인 광석이다");
        item.addRecipe(new LinkedHashMap<Long, Integer>() {{
            put(ItemList.COBBLE_STONE.getId(), 10);
            put(ItemList.COAL.getId(), 5);
        }});
        Config.unloadObject(item);

        createItem(ItemList.COAL, "불이 잘 붙는 흔한 광물이다");

        createItem(ItemList.QUARTZ, "약간 투명한 보석이다");
        createItem(ItemList.COPPER, "붉으스름한 광물이다");
        createItem(ItemList.LEAD, "다양한 곳에서 사용되는 광물이다");
        createItem(ItemList.TIN, "다양한 곳에서 사용되는 광물이다");
        createItem(ItemList.NICKEL, "다양한 곳에서 사용되는 광물이다");

        createItem(ItemList.IRON, "매우 많이 사용되는 일반적인 광물이다");
        createItem(ItemList.LITHIUM, "다양한 곳에서 사용되는 광물이다");

        createItem(ItemList.LAPIS, "푸른색을 띄는 광물이다");
        createItem(ItemList.RED_STONE, "붉은색을 띄는 광물이다");

        createItem(ItemList.SILVER, "회백색을 띄며 신비한 기운을 내뿜는 광물이다");

        item = new Item(ItemList.GOLD, "밝은 노란색을 띄는 보석이다");
        item.addRecipe(new LinkedHashMap<Long, Integer>() {{
            put(ItemList.NONE.getId(), 3);

            put(ItemList.DARK_QUARTZ.getId(), 3);
            put(ItemList.WHITE_GOLD.getId(), 1);
        }});
        Config.unloadObject(item);

        createItem(ItemList.GLOW_STONE, "스스로 빛을 내는 광물이다");

        createItem(ItemList.FIRE_QUARTZ, "주변의 열기를 흡수하는 석영이다");
        createItem(ItemList.DARK_QUARTZ, "주변의 빛을 흡수하는 석영이다");

        item = new Item(ItemList.GLOW_LAPIS, "스스로 빛을 내는 마나의 기운이 담긴 푸른 광물이다");
        item.addRecipe(new LinkedHashMap<Long, Integer>() {{
            put(ItemList.LAPIS.getId(), 3);
            put(ItemList.GLOW_STONE.getId(), 5);
        }});
        item.addRecipe(new LinkedHashMap<Long, Integer>() {{
            put(ItemList.NONE.getId(), 10);

            put(ItemList.GLOW_STONE.getId(), 10);
            put(ItemList.LAPIS_RED_STONE.getId(), 1);
        }});
        Config.unloadObject(item);

        item = new Item(ItemList.GLOW_RED_STONE, "스스로 빛을 내는 힘의 기운이 담긴 붉은 광물이다");
        item.addRecipe(new LinkedHashMap<Long, Integer>() {{
            put(ItemList.RED_STONE.getId(), 3);
            put(ItemList.GLOW_STONE.getId(), 5);
        }});
        item.addRecipe(new LinkedHashMap<Long, Integer>() {{
            put(ItemList.NONE.getId(), 10);

            put(ItemList.GLOW_STONE.getId(), 10);
            put(ItemList.LAPIS_RED_STONE.getId(), 1);
        }});
        Config.unloadObject(item);

        item = new Item(ItemList.WHITE_GOLD, "주변의 어둠을 흡수하는 보석이다");
        item.addRecipe(new LinkedHashMap<Long, Integer>() {{
            put(ItemList.GOLD.getId(), 5);
            put(ItemList.LIQUID_STONE.getId(), 1);
        }});
        item.addRecipe(new LinkedHashMap<Long, Integer>() {{
            put(ItemList.NONE.getId(), 5);

            put(ItemList.AITUME.getId(), 1);
        }});
        Config.unloadObject(item);

        createItem(ItemList.HARD_COAL, "일반 석탄보다 훨씬 질이 좋은 광물이다");
        createItem(ItemList.TITANIUM, "꽤나 단단한 광물이다");

        item = new Item(ItemList.LIQUID_STONE, "거의 보이지 않을 정도로 투명한 보석이다");
        item.addRecipe(new LinkedHashMap<Long, Integer>() {{
            put(ItemList.QUARTZ.getId(), 10);
            put(ItemList.GLOW_STONE.getId(), 1);
            put(ItemList.FIRE_QUARTZ.getId(), 10);
            put(ItemList.DARK_QUARTZ.getId(), 1);
            put(ItemList.WHITE_GOLD.getId(), 5);
        }});
        Config.unloadObject(item);

        item = new Item(ItemList.DIAMOND, "매우 단단하고 아름다운 보석이다");
        item.addRecipe(new LinkedHashMap<Long, Integer>() {{
            put(ItemList.TITANIUM.getId(), 10);
            put(ItemList.LIQUID_STONE.getId(), 3);
        }});
        Config.unloadObject(item);

        createItem(ItemList.ORICHALCON, "일반적으로 가장 단단하다고 여겨지는 광물이다");

        item = new Item(ItemList.LAPIS_RED_STONE, "힘과 마나, 두개의 상반된 기운을 한번에 가진 광물이다");
        item.addRecipe(new LinkedHashMap<Long, Integer>() {{
            put(ItemList.GLOW_LAPIS.getId(), 50);
            put(ItemList.GLOW_RED_STONE.getId(), 50);
            put(ItemList.WHITE_GOLD.getId(), 20);
            put(ItemList.DIAMOND.getId(), 10);
        }});
        Config.unloadObject(item);

        item = new Item(ItemList.LANDIUM, "부서질수록 단단해자고, 스스로 복구되는 광물이다");
        item.addRecipe(new LinkedHashMap<Long, Integer>() {{
            put(ItemList.WHITE_GOLD.getId(), 10);
            put(ItemList.AITUME.getId(), 1);
        }});
        Config.unloadObject(item);

        item = new Item(ItemList.AITUME, "공기처럼 가벼우나 오리하르콘만큼 단단한 광물이다");
        item.addRecipe(new LinkedHashMap<Long, Integer>() {{
            put(ItemList.WHITE_GOLD.getId(), 10);
            put(ItemList.LANDIUM.getId(), 1);
        }});
        Config.unloadObject(item);

        createItem(ItemList.GARNET, "검붉은색을 띄는 보석이다");
        createItem(ItemList.AMETHYST, "보라색을 띄는 보석이다");
        createItem(ItemList.AQUAMARINE, "파란색을 띄는 보석이다");
        createItem(ItemList.EMERALD, "녹색을 띄는 보석이다");
        createItem(ItemList.PEARL, "흰색을 띄는 보석이다");
        createItem(ItemList.RUBY, "붉은색을 띄는 보석이다");
        createItem(ItemList.PERIDOT, "연두색을 띄는 보석이다");
        createItem(ItemList.SAPPHIRE, "남색을 띄는 보석이다");
        createItem(ItemList.OPAL, "다양한 색을 띄는 보석이다");
        createItem(ItemList.TOPAZ, "주황색을 띄는 보석이다");
        createItem(ItemList.TURQUOISE, "청록색을 띄는 보석이다");

        createItem(ItemList.TRASH, "누가 이런걸 물에 버렸어?");
        createItem(ItemList.WATER_GRASS, "누가 이런걸 물에 버렸어?");

        item = new Item(ItemList.COMMON_FISH1, "일반 등급의 물고기다");
        item.setCanEat(true);
        item.setEatBuff(-1, StatType.HP, 10);
        Config.unloadObject(item);

        item = new Item(ItemList.COMMON_FISH2, "일반 등급의 물고기다");
        item.setCanEat(true);
        item.setEatBuff(-1, StatType.HP, 10);
        Config.unloadObject(item);

        item = new Item(ItemList.COMMON_FISH3, "일반 등급의 물고기다");
        item.setCanEat(true);
        item.setEatBuff(-1, StatType.HP, 10);
        Config.unloadObject(item);

        item = new Item(ItemList.COMMON_FISH4, "일반 등급의 물고기다");
        item.setCanEat(true);
        item.setEatBuff(-1, StatType.HP, 10);
        Config.unloadObject(item);

        item = new Item(ItemList.COMMON_FISH5, "일반 등급의 물고기다");
        item.setCanEat(true);
        item.setEatBuff(-1, StatType.HP, 10);
        Config.unloadObject(item);

        item = new Item(ItemList.RARE_FISH1, "희귀 등급의 물고기다");
        item.setCanEat(true);
        item.setEatBuff(-1, StatType.HP, 20);
        Config.unloadObject(item);

        item = new Item(ItemList.RARE_FISH2, "희귀 등급의 물고기다");
        item.setCanEat(true);
        item.setEatBuff(-1, StatType.HP, 20);
        Config.unloadObject(item);

        item = new Item(ItemList.RARE_FISH3, "희귀 등급의 물고기다");
        item.setCanEat(true);
        item.setEatBuff(-1, StatType.HP, 20);
        Config.unloadObject(item);

        item = new Item(ItemList.RARE_FISH4, "희귀 등급의 물고기다");
        item.setCanEat(true);
        item.setEatBuff(-1, StatType.HP, 20);
        Config.unloadObject(item);

        item = new Item(ItemList.RARE_FISH5, "희귀 등급의 물고기다");
        item.setCanEat(true);
        item.setEatBuff(-1, StatType.HP, 20);
        Config.unloadObject(item);

        item = new Item(ItemList.RARE_FISH6, "희귀 등급의 물고기다");
        item.setCanEat(true);
        item.setEatBuff(-1, StatType.HP, 20);
        Config.unloadObject(item);

        item = new Item(ItemList.SPECIAL_FISH1, "특별 등급의 물고기다");
        item.setCanEat(true);
        item.setEatBuff(-1, StatType.HP, 45);
        Config.unloadObject(item);

        item = new Item(ItemList.SPECIAL_FISH2, "특별 등급의 물고기다");
        item.setCanEat(true);
        item.setEatBuff(-1, StatType.HP, 45);
        Config.unloadObject(item);

        item = new Item(ItemList.SPECIAL_FISH3, "특별 등급의 물고기다");
        item.setCanEat(true);
        item.setEatBuff(-1, StatType.HP, 45);
        Config.unloadObject(item);

        item = new Item(ItemList.SPECIAL_FISH4, "특별 등급의 물고기다");
        item.setCanEat(true);
        item.setEatBuff(-1, StatType.HP, 45);
        Config.unloadObject(item);

        item = new Item(ItemList.SPECIAL_FISH5, "특별 등급의 물고기다");
        item.setCanEat(true);
        item.setEatBuff(-1, StatType.HP, 45);
        Config.unloadObject(item);

        item = new Item(ItemList.SPECIAL_FISH6, "특별 등급의 물고기다");
        item.setCanEat(true);
        item.setEatBuff(-1, StatType.HP, 45);
        Config.unloadObject(item);

        item = new Item(ItemList.SPECIAL_FISH7, "특별 등급의 물고기다");
        item.setCanEat(true);
        item.setEatBuff(-1, StatType.HP, 45);
        Config.unloadObject(item);

        item = new Item(ItemList.SPECIAL_FISH8, "특별 등급의 물고기다");
        item.setCanEat(true);
        item.setEatBuff(-1, StatType.HP, 45);
        Config.unloadObject(item);

        item = new Item(ItemList.SPECIAL_FISH9, "특별 등급의 물고기다");
        item.setCanEat(true);
        item.setEatBuff(-1, StatType.HP, 45);
        Config.unloadObject(item);

        item = new Item(ItemList.SPECIAL_FISH10, "특별 등급의 물고기다");
        item.setCanEat(true);
        item.setEatBuff(-1, StatType.HP, 45);
        Config.unloadObject(item);

        item = new Item(ItemList.UNIQUE_FISH1, "유일 등급의 물고기다");
        item.setCanEat(true);
        item.setEatBuff(-1, StatType.HP, 80);
        Config.unloadObject(item);

        item = new Item(ItemList.UNIQUE_FISH2, "유일 등급의 물고기다");
        item.setCanEat(true);
        item.setEatBuff(-1, StatType.HP, 80);
        Config.unloadObject(item);

        item = new Item(ItemList.UNIQUE_FISH3, "유일 등급의 물고기다");
        item.setCanEat(true);
        item.setEatBuff(-1, StatType.HP, 80);
        Config.unloadObject(item);

        item = new Item(ItemList.UNIQUE_FISH4, "유일 등급의 물고기다");
        item.setCanEat(true);
        item.setEatBuff(-1, StatType.HP, 80);
        Config.unloadObject(item);

        item = new Item(ItemList.UNIQUE_FISH5, "유일 등급의 물고기다");
        item.setCanEat(true);
        item.setEatBuff(-1, StatType.HP, 80);
        Config.unloadObject(item);

        item = new Item(ItemList.UNIQUE_FISH6, "유일 등급의 물고기다");
        item.setCanEat(true);
        item.setEatBuff(-1, StatType.HP, 80);
        Config.unloadObject(item);

        item = new Item(ItemList.UNIQUE_FISH7, "유일 등급의 물고기다");
        item.setCanEat(true);
        item.setEatBuff(-1, StatType.HP, 80);
        Config.unloadObject(item);

        item = new Item(ItemList.LEGENDARY_FISH1, "전설 등급의 물고기다");
        item.setCanEat(true);
        item.setEatBuff(-1, StatType.HP, 200);
        Config.unloadObject(item);

        item = new Item(ItemList.LEGENDARY_FISH2, "전설 등급의 물고기다");
        item.setCanEat(true);
        item.setEatBuff(-1, StatType.HP, 200);
        Config.unloadObject(item);

        item = new Item(ItemList.LEGENDARY_FISH3, "전설 등급의 물고기다");
        item.setCanEat(true);
        item.setEatBuff(-1, StatType.HP, 200);
        Config.unloadObject(item);

        item = new Item(ItemList.LEGENDARY_FISH4, "전설 등급의 물고기다");
        item.setCanEat(true);
        item.setEatBuff(-1, StatType.HP, 200);
        Config.unloadObject(item);

        item = new Item(ItemList.MYSTIC_FISH1, "신화 등급의 물고기다");
        item.setCanEat(true);
        item.setEatBuff(-1, StatType.HP, 500);
        Config.unloadObject(item);

        item = new Item(ItemList.MYSTIC_FISH2, "신화 등급의 물고기다");
        item.setCanEat(true);
        item.setEatBuff(-1, StatType.HP, 500);
        Config.unloadObject(item);


        createItem(ItemList.PVP_DISABLE_1, "PvP를 1일간 비활성화 하기 위해 필요한 아이템이다(중첩 불가)");
        createItem(ItemList.PVP_DISABLE_7, "PvP를 7일간 비활성화 하기 위해 필요한 아이템이다(중첩 불가)");

        item = new Item(ItemList.STAT_POINT, "스텟 포인트(SP)를 50% 확률로 1 얻을 수 있게 해주는 아이템이다");
        Config.unloadObject(item);

        item = new Item(ItemList.ADV_STAT, "모험 스텟(ADV)를 50% 확률로 1 얻을 수 있게 해주는 아이템이다");
        Config.unloadObject(item);

        createItem(ItemList.LOW_RECIPE, "하급 아이템 또는 하급 장비의 제작법을 무작위로 1개 획득할 수 있다(중복 가능)");

        item = new Item(ItemList.RECIPE, "중급 아이템 또는 중급 장비의 제작법을 무작위로 1개 획득할 수 있다(중복 가능)");
        item.addRecipe(new LinkedHashMap<Long, Integer>() {{
            put(ItemList.LOW_RECIPE.getId(), 6);
        }});
        item.addRecipe(new LinkedHashMap<Long, Integer>() {{
            put(ItemList.CONFIRMED_LOW_RECIPE.getId(), 2);
        }});
        Config.unloadObject(item);

        item = new Item(ItemList.HIGH_RECIPE, "상급 아이템 또는 상급 장비의 제작법을 무작위로 1개 획득할 수 있다(중복 가능)");
        item.addRecipe(new LinkedHashMap<Long, Integer>() {{
            put(ItemList.RECIPE.getId(), 10);
        }});
        item.addRecipe(new LinkedHashMap<Long, Integer>() {{
            put(ItemList.CONFIRMED_RECIPE.getId(), 2);
        }});
        Config.unloadObject(item);

        createItem(ItemList.RED_SPHERE, "불의 기운을 담고 있는 구체다");
        createItem(ItemList.GREEN_SPHERE, " 독의 기운을 담고 있는 구체다");
        createItem(ItemList.LIGHT_GREEN_SPHERE, "풀의 기운을 담고 있는 구체다");
        createItem(ItemList.BLUE_SPHERE, "물의 기운을 담고 있는 구체다");
        createItem(ItemList.BROWN_SPHERE, "얼음의 기운을 담고 있는 구체다");
        createItem(ItemList.GRAY_SPHERE, "흙의 기운을 담고 있는 구체다");
        createItem(ItemList.SILVER_SPHERE, "돌의 기운을 담고 있는 구체다");
        createItem(ItemList.LIGHT_GRAY_SPHERE, "반사의 기운을 담고 있는 구체다");
        createItem(ItemList.YELLOW_SPHERE, "쇠의 기운을 담고 있는 구체다");
        createItem(ItemList.BLACK_SPHERE, " 마법의 기운을 담고 있는 구체다");
        createItem(ItemList.RED_SPHERE, "어둠의 기운을 담고 있는 구체다");
        createItem(ItemList.WHITE_SPHERE, "빛의 기운을 담고 있는 구체다");

        item = new Item(ItemList.LOW_EXP_POTION, "경험치를 소량 제공해주는 포션이다");
        item.addRecipe(new LinkedHashMap<Long, Integer>() {{
            put(ItemList.EXP_FRUIT.getId(), 5);
        }});
        Config.unloadObject(item);

        createItem(ItemList.EXP_POTION, "경험치를 제공해주는 포션이다");
        createItem(ItemList.HIGH_EXP_POTION, "경험치를 대량 제공해주는 포션이다");

        createItem(ItemList.LOW_ADV_TOKEN, "간단한 모험을 완수했다는 증표이다");
        createItem(ItemList.ADV_TOKEN, "모험을 완수했다는 증표이다");
        createItem(ItemList.HIGH_ADV_TOKEN, "어려운 모험을 완수했다는 증표이다");

        createItem(ItemList.EMPTY_SPHERE, "무슨 색이든 될 수 있는 구체이다");

        createItem(ItemList.LOW_REINFORCE_STONE, "0~4강 무기를 강화할 수 있는 아이템이다");

        item = new Item(ItemList.REINFORCE_STONE, "5~9강 무기를 강화할 수 있는 아이템이다");
        item.addRecipe(new LinkedHashMap<Long, Integer>() {{
            put(ItemList.LOW_REINFORCE_STONE.getId(), 10);
        }});
        Config.unloadObject(item);

        item = new Item(ItemList.HIGH_REINFORCE_STONE, "10~14강 무기를 강화할 수 있는 아이템이다");
        item.addRecipe(new LinkedHashMap<Long, Integer>() {{
            put(ItemList.REINFORCE_STONE.getId(), 5);
        }});
        Config.unloadObject(item);

        createItem(ItemList.PIECE_OF_LOW_AMULET, "장착은 불가하지만 약간의 부적의 기운을 띄고 있는 파편이다");
        createItem(ItemList.PIECE_OF_AMULET, "장착은 불가하지만 부적의 기운을 띄고 있는 파편이다");
        createItem(ItemList.PIECE_OF_HIGH_AMULET, "장착은 불가하지만 강한 부적의 기운을 띄고 있는 파편이다");

        createItem(ItemList.PIECE_OF_GEM, "장착은 불가하지만 조각을 모으면 보석을 만들 수 있을 것 같다");
        createItem(ItemList.GEM_ABRASIVE_MATERIAL, "보석을 연마할 수 있는 아이템이다");
        createItem(ItemList.GLOW_GEM_ABRASIVE_MATERIAL, "중심 보석을 연마할 수 있는 아이템이다");

        createItem(ItemList.EQUIP_SAFENER, "무기의 제한 레벨을 5 낮출 수 있는 아이템이다\n명령어 대상 : 장비 번호");

        createItem(ItemList.LOW_MINER_TOKEN, "간단한 광질을 완수했다는 증표이다");
        createItem(ItemList.MINER_TOKEN, "광질을 완수했다는 증표이다");
        createItem(ItemList.HIGH_MINER_TOKEN, "어려운 광질을 완수했다는 증표이다");
        createItem(ItemList.LOW_FISH_TOKEN, "간단한 낚시를 완수했다는 증표이다");
        createItem(ItemList.FISH_TOKEN, "낚시를 완수했다는 증표이다");
        createItem(ItemList.HIGH_FISH_TOKEN, "어려운 낚시를 완수했다는 증표이다");
        createItem(ItemList.LOW_HUNTER_TOKEN, "간단한 사냥을 완수했다는 증표이다");
        createItem(ItemList.HUNTER_TOKEN, "사냥을 완수했다는 증표이다");
        createItem(ItemList.HIGH_HUNTER_TOKEN, "어려운 사냥을 완수했다는 증표이다");

        createItem(ItemList.LOW_TOKEN, "간단한 노동을 완수했다는 증표이다");
        createItem(ItemList.TOKEN, "노동을 완수했다는 증표이다");
        createItem(ItemList.HIGH_TOKEN, "어려운 노동을 완수했다는 증표이다");

        item = new Item(ItemList.LOW_AMULET, "하급 부적 1개를 뽑을 수 있는 아이템이다");
        item.addRecipe(new LinkedHashMap<Long, Integer>() {{
            put(ItemList.PIECE_OF_LOW_AMULET.getId(), 10);
        }});
        Config.unloadObject(item);

        item = new Item(ItemList.AMULET, "중급 부적 1개를 뽑을 수 있는 아이템이다");
        item.addRecipe(new LinkedHashMap<Long, Integer>() {{
            put(ItemList.PIECE_OF_AMULET.getId(), 10);
        }});
        Config.unloadObject(item);

        item = new Item(ItemList.HIGH_AMULET, "상급 부적 1개를 뽑을 수 있는 아이템이다");
        item.addRecipe(new LinkedHashMap<Long, Integer>() {{
            put(ItemList.PIECE_OF_HIGH_AMULET.getId(), 10);
        }});
        Config.unloadObject(item);

        item = new Item(ItemList.LAMB, "양에서 나온 고기다");
        item.setCanEat(true);
        item.setEatBuff(50000, StatType.ATS, 5);
        item.setEatBuff(50000, StatType.ATK, 1);
        Config.unloadObject(item);

        createItem(ItemList.SHEEP_LEATHER, "양에서 나온 가죽이다");
        createItem(ItemList.WOOL, "양에서 나온 털이다");

        item = new Item(ItemList.PORK, "돼지에서 나온 고기다");
        item.setCanEat(true);
        item.setEatBuff(50000, StatType.ACC, 8);
        item.setEatBuff(50000, StatType.ATS, 8);
        Config.unloadObject(item);

        createItem(ItemList.PIG_HEAD, "행운의 상징으로 자주 쓰이는 아이템이다");

        item = new Item(ItemList.BEEF, "소에게서 나온 고기다");
        item.setCanEat(true);
        item.setEatBuff(50000, StatType.DEF, 5);
        item.setEatBuff(50000, StatType.AGI, 10);
        Config.unloadObject(item);

        createItem(ItemList.LEATHER, "소에서 나온 가죽이다");

        createItem(ItemList.ZOMBIE_HEAD, "보기엔 좀 역겨운 좀비 머리다");
        createItem(ItemList.ZOMBIE_SOUL, "좀비의 영혼을 강제로 해제하여 떨어진 파편이다");
        createItem(ItemList.ZOMBIE_HEART, "심장이지만 좀비의 것이라 그런지 피는 나지 않는다");

        item = new Item(ItemList.LOW_ALLOY, "다양한 광물을 합쳐 만든 합금이다");
        item.addRecipe(new LinkedHashMap<Long, Integer>() {{
            put(ItemList.COAL.getId(), 30);
            put(ItemList.COPPER.getId(), 20);
            put(ItemList.LEAD.getId(), 15);
            put(ItemList.TIN.getId(), 15);
            put(ItemList.NICKEL.getId(), 15);
        }});
        Config.unloadObject(item);

        item = new Item(ItemList.ALLOY, "다양한 광물을 합쳐 만든 괜찮은 합금이다");
        item.addRecipe(new LinkedHashMap<Long, Integer>() {{
            put(ItemList.COAL.getId(), 100);
            put(ItemList.LITHIUM.getId(), 50);
            put(ItemList.SILVER.getId(), 20);
            put(ItemList.GOLD.getId(), 5);
        }});
        Config.unloadObject(item);

        item = new Item(ItemList.HIGH_ALLOY, "다양한 광물을 합쳐 만든 질 좋은 합금이다");
        item.addRecipe(new LinkedHashMap<Long, Integer>() {{
            put(ItemList.WHITE_GOLD.getId(), 10);
            put(ItemList.TITANIUM.getId(), 30);
            put(ItemList.LIQUID_STONE.getId(), 30);
            put(ItemList.DIAMOND.getId(), 10);
            put(ItemList.ORICHALCON.getId(), 50);
            put(ItemList.LANDIUM.getId(), 2);
            put(ItemList.AITUME.getId(), 2);
            put(ItemList.EMPTY_SPHERE.getId(), 50);
        }});
        Config.unloadObject(item);

        createItem(ItemList.PIECE_OF_SLIME, "슬라임에게서 떨어져나온 파편이다");
        createItem(ItemList.SPIDER_LEG, "찢어진 거미 다리다. 별로 좋은 형태는 아니다");
        createItem(ItemList.SPIDER_EYE, "거미 눈이다. 반짝거려서 꽤나 예쁘다");

        item = new Item(ItemList.HARD_IRON, "기존의 철을 개선한 단단한 철이다");
        item.addRecipe(new LinkedHashMap<Long, Integer>() {{
            put(ItemList.IRON.getId(), 100);
            put(ItemList.LOW_ALLOY.getId(), 3);
            put(ItemList.COAL.getId(), 100);
        }});
        Config.unloadObject(item);

        createItem(ItemList.ELIXIR_HERB, "엘프의 기운이 들어간 약초다");

        item = new Item(ItemList.LOW_ELIXIR, "최대 체력과 마나의 15%를 회복시켜주는 포션이다");
        item.addRecipe(new LinkedHashMap<Long, Integer>() {{
            put(ItemList.ELIXIR_HERB.getId(), 3);
            put(ItemList.GLASS_BOTTLE.getId(), 1);
            put(ItemList.COAL.getId(), 1);
        }});
        item.addRecipe(new LinkedHashMap<Long, Integer>() {{
            put(ItemList.NONE.getId(), 2);

            put(ItemList.ELIXIR.getId(), 1);
        }});
        Config.unloadObject(item);

        item = new Item(ItemList.ELIXIR, "최대 체력과 마나의 50%를 회복시켜주는 포션이다");
        item.addRecipe(new LinkedHashMap<Long, Integer>() {{
            put(ItemList.LOW_ELIXIR.getId(), 3);
            put(ItemList.HP_POTION.getId(), 1);
            put(ItemList.MP_POTION.getId(), 1);
            put(ItemList.RED_SPHERE.getId(), 3);
        }});
        item.addRecipe(new LinkedHashMap<Long, Integer>() {{
            put(ItemList.NONE.getId(), 2);

            put(ItemList.HIGH_ELIXIR.getId(), 1);
        }});
        Config.unloadObject(item);

        item = new Item(ItemList.HIGH_ELIXIR, "최대 체력과 마나를 모두 회복시켜주는 포션이다");
        item.addRecipe(new LinkedHashMap<Long, Integer>() {{
            put(ItemList.ELIXIR.getId(), 5);
            put(ItemList.HIGH_HP_POTION.getId(), 1);
            put(ItemList.HIGH_MP_POTION.getId(), 1);
            put(ItemList.HARD_COAL.getId(), 1);
        }});
        Config.unloadObject(item);

        item = new Item(ItemList.COMMON_GRILLED_FISH, "일반 등급의 생선을 구운 음식이다");
        for(long itemId = ItemList.COMMON_FISH1.getId(); itemId <= ItemList.COMMON_FISH5.getId(); itemId++) {
            long itemId_ = itemId;

            item.addRecipe(new LinkedHashMap<Long, Integer>() {{
                put(itemId_, 1);
                put(ItemList.COAL.getId(), 5);
            }});
        }
        item.setCanEat(true);
        item.setEatBuff(-1, StatType.HP, 15);
        Config.unloadObject(item);

        item = new Item(ItemList.RARE_GRILLED_FISH, "희귀 등급의 생선을 구운 음식이다");
        for(long itemId = ItemList.RARE_FISH1.getId(); itemId <= ItemList.RARE_FISH6.getId(); itemId++) {
            long itemId_ = itemId;

            item.addRecipe(new LinkedHashMap<Long, Integer>() {{
                put(itemId_, 1);
                put(ItemList.COAL.getId(), 5);
            }});
        }
        item.setCanEat(true);
        item.setEatBuff(-1, StatType.HP, 30);
        Config.unloadObject(item);

        item = new Item(ItemList.SPECIAL_GRILLED_FISH, "특별 등급의 생선을 구운 음식이다");
        for(long itemId = ItemList.SPECIAL_FISH1.getId(); itemId <= ItemList.SPECIAL_FISH10.getId(); itemId++) {
            long itemId_ = itemId;

            item.addRecipe(new LinkedHashMap<Long, Integer>() {{
                put(itemId_, 1);
                put(ItemList.COAL.getId(), 5);
            }});
        }
        item.setCanEat(true);
        item.setEatBuff(-1, StatType.HP, 67);
        Config.unloadObject(item);

        item = new Item(ItemList.UNIQUE_GRILLED_FISH, "유일 등급의 생선을 구운 음식이다");
        for(long itemId = ItemList.UNIQUE_FISH1.getId(); itemId <= ItemList.UNIQUE_FISH7.getId(); itemId++) {
            long itemId_ = itemId;

            item.addRecipe(new LinkedHashMap<Long, Integer>() {{
                put(itemId_, 1);
                put(ItemList.COAL.getId(), 5);
            }});
        }
        item.setCanEat(true);
        item.setEatBuff(-1, StatType.HP, 120);
        Config.unloadObject(item);

        item = new Item(ItemList.LEGENDARY_GRILLED_FISH, "전설 등급의 생선을 구운 음식이다");
        for(long itemId = ItemList.LEGENDARY_FISH1.getId(); itemId <= ItemList.LEGENDARY_FISH4.getId(); itemId++) {
            long itemId_ = itemId;

            item.addRecipe(new LinkedHashMap<Long, Integer>() {{
                put(itemId_, 1);
                put(ItemList.COAL.getId(), 5);
            }});
        }
        item.setCanEat(true);
        item.setEatBuff(-1, StatType.HP, 300);
        Config.unloadObject(item);

        item = new Item(ItemList.MYSTIC_GRILLED_FISH, "신화 등급의 생선을 구운 음식이다");
        for(long itemId = ItemList.MYSTIC_FISH1.getId(); itemId <= ItemList.MYSTIC_FISH2.getId(); itemId++) {
            long itemId_ = itemId;

            item.addRecipe(new LinkedHashMap<Long, Integer>() {{
                put(itemId_, 1);
                put(ItemList.COAL.getId(), 5);
            }});
        }
        item.setCanEat(true);
        item.setEatBuff(-1, StatType.HP, 750);
        Config.unloadObject(item);

        item = new Item(ItemList.COOKED_LAMB, "양고기를 노릇하게 익힌 음식이다");
        item.addRecipe(new LinkedHashMap<Long, Integer>() {{
            put(ItemList.LAMB.getId(), 1);
            put(ItemList.COAL.getId(), 5);
        }});
        item.setEatBuff(50000, StatType.ATS, 10);
        item.setEatBuff(50000, StatType.ATK, 2);
        item.setCanEat(true);
        Config.unloadObject(item);

        item = new Item(ItemList.COOKED_PORT, "돼지고기를 노릇하게 익힌 음식이다");
        item.addRecipe(new LinkedHashMap<Long, Integer>() {{
            put(ItemList.PORK.getId(), 1);
            put(ItemList.COAL.getId(), 5);
        }});
        item.setEatBuff(50000, StatType.ACC, 15);
        item.setEatBuff(50000, StatType.ATS, 15);
        item.setCanEat(true);
        Config.unloadObject(item);

        item = new Item(ItemList.COOKED_BEEF, "소고기를 살짝 익힌 음식이다");
        item.addRecipe(new LinkedHashMap<Long, Integer>() {{
            put(ItemList.BEEF.getId(), 1);
            put(ItemList.COAL.getId(), 5);
        }});
        item.setEatBuff(50000, StatType.DEF, 10);
        item.setEatBuff(50000, StatType.AGI, 20);
        item.setCanEat(true);
        Config.unloadObject(item);

        item = new Item(ItemList.MAGIC_STONE, "어둠의 힘이 담긴 돌이다");
        item.addRecipe(new LinkedHashMap<Long, Integer>() {{
            put(ItemList.NONE.getId(), 5);

            put(ItemList.CORRUPTED_MAGIC_STONE.getId(), 1);
            put(ItemList.PURIFYING_FRUIT.getId(), 1);
        }});
        Config.unloadObject(item);

        createItem(ItemList.FLOWER, "아름다운 꽃이다");
        
        item = new Item(ItemList.STONE_LUMP, "돌 10개다");
        item.addRecipe(new LinkedHashMap<Long, Integer>() {{
            put(ItemList.STONE.getId(), 10);
        }});
        Config.unloadObject(item);
        
        createItem(ItemList.REINFORCE_MULTIPLIER, "다음 강화 확률을 2배로 증가시켜주는 아이템이다 (중첩 불가)");
        createItem(ItemList.GLOW_REINFORCE_MULTIPLIER, "다음 강화 확률을 3배로 증가시켜주는 아이템이다 (중첩 불가)");
        
        createItem(ItemList.OAK_TOOTH, "생각보단 깨끗한 오크의 이빨이다");
        createItem(ItemList.OAK_LEATHER, "상당히 질긴 고품질의 가죽이다");
        
        createItem(ItemList.SKILL_BOOK_MAGIC_BALL, "스킬 " + Emoji.focus(SkillList.MAGIC_BALL.getDisplayName()) +
                " 을 배울 수 있게 해주는 스킬 북이다");
        createItem(ItemList.SKILL_BOOK_SMITE, "스킬 " + Emoji.focus(SkillList.SMITE.getDisplayName()) +
                " 를 배울 수 있게 해주는 스킬 북이다");
        createItem(ItemList.SKILL_BOOK_LASER, "스킬 " + Emoji.focus(SkillList.LASER.getDisplayName()) +
                " 를 배울 수 있게 해주는 스킬 북이다");

        createItem(ItemList.PIECE_OF_BONE, "많이 부서진 뼛조각이다");
        createItem(ItemList.HORN_OF_IMP, "임프의 뿔이다. 상당히 단단하다");
        createItem(ItemList.IMP_HEART, "임프의 심장이다. 마족이라 그런지 색깔이 어둡다");
        createItem(ItemList.HORN_OF_LOW_DEVIL, "하급 악마의 뿔이지만 내부에 마력이 상당하다");
        createItem(ItemList.LOW_DEVIL_SOUL, "하급 악마여도 희귀한 악마의 영혼이다");

        createItem(ItemList.GOLD_FRUIT, "골드로 바꿀 수 있는 열매다");
        createItem(ItemList.EXP_FRUIT, "경험치로 바꿀 수 있는 열매다");
        createItem(ItemList.SMALL_GOLD_SEED, "골드 씨앗이다\n[수확]: 1 골드 열매/120분");
        createItem(ItemList.GOLD_SEED, "골드 씨앗이다 (씨앗 Lv.2)\n[수확]: 2 골드 열매/90분");
        createItem(ItemList.BIG_GOLD_SEED, "골드 씨앗이다 (씨앗 Lv.3)\n[수확]: 3 골드 열매/60분");
        createItem(ItemList.SMALL_EXP_SEED, "경험치 씨앗이다\n[수확]: 1 경험치 열매/30분");
        createItem(ItemList.EXP_SEED, "경험치 씨앗이다 (씨앗 Lv.2)\n[수확]: 2 경험치 열매/15분");
        createItem(ItemList.BIG_EXP_SEED, "경험치 씨앗이다 (씨앗 Lv.3)\n[수확]: 4 경험치 열매/10분");
        
        createItem(ItemList.HARPY_WING, "하피의 날개다. 날개치곤 상당히 단단하다");
        createItem(ItemList.HARPY_NAIL, "부서졌음에도 여전히 날카롭다");
        
        createItem(ItemList.GOLEM_CORE, "여전히 빛나고 있다. 언젠간 쓰일 것 같다");

        createItem(ItemList.SKILL_BOOK_SCAR, "스킬 " + Emoji.focus(SkillList.SCAR.getDisplayName()) +
                " 를 배울 수 있게 해주는 스킬 북이다");
        createItem(ItemList.SKILL_BOOK_CHARM, "스킬 " + Emoji.focus(SkillList.CHARM.getDisplayName()) +
                " 를 배울 수 있게 해주는 스킬 북이다");
        createItem(ItemList.SKILL_BOOK_STRINGS_OF_LIFE, "스킬 " + Emoji.focus(SkillList.STRINGS_OF_LIFE.getDisplayName()) +
                " 를 배울 수 있게 해주는 스킬 북이다");
        createItem(ItemList.SKILL_BOOK_RESIST, "스킬 " + Emoji.focus(SkillList.RESIST.getDisplayName()) +
                " 를 배울 수 있게 해주는 스킬 북이다");
        
        createItem(ItemList.PIECE_OF_MAGIC, "마법 그 자체를 담은 특이한 조각이다");
        
        item = new Item(ItemList.CONFIRMED_LOW_RECIPE, "하급 아이템 또는 하급 장비의 제작법을 1개 지정하여 확정적으로 획득할 수 있다");
        item.addRecipe(new LinkedHashMap<Long, Integer>() {{
            put(ItemList.LOW_RECIPE.getId(), 5);
        }});
        Config.unloadObject(item);

        item = new Item(ItemList.CONFIRMED_RECIPE, "중급 아이템 또는 중급 장비의 제작법을 1개 지정하여 확정적으로 획득할 수 있다");
        item.addRecipe(new LinkedHashMap<Long, Integer>() {{
            put(ItemList.RECIPE.getId(), 6);
        }});
        item.addRecipe(new LinkedHashMap<Long, Integer>() {{
            put(ItemList.CONFIRMED_LOW_RECIPE.getId(), 10);
        }});
        Config.unloadObject(item);

        item = new Item(ItemList.CONFIRMED_HIGH_RECIPE,
                "상급 아이템 또는 상급 장비의 제작법을 1개 지정하여 확정적으로 획득할 수 있다");
        item.addRecipe(new LinkedHashMap<Long, Integer>() {{
            put(ItemList.HIGH_RECIPE.getId(), 8);
        }});
        item.addRecipe(new LinkedHashMap<Long, Integer>() {{
            put(ItemList.CONFIRMED_RECIPE.getId(), 10);
        }});
        Config.unloadObject(item);

        createItem(ItemList.OWLBEAR_LEATHER, "곰인지 부엉인지 모를 아울베어의 가죽이다");

        createItem(ItemList.SKILL_BOOK_RUSH, "스킬 " + Emoji.focus(SkillList.RUSH.getDisplayName()) +
                " 를 배울 수 있게 해주는 스킬 북이다");
        createItem(ItemList.SKILL_BOOK_ROAR, "스킬 " + Emoji.focus(SkillList.ROAR.getDisplayName()) +
                " 를 배울 수 있게 해주는 스킬 북이다");

        createItem(ItemList.CORRUPTED_MAGIC_STONE, "심각하게 오염된 마정석이다\n일반인들은 접촉도 할 수 없을 것 같다");

        createItem(ItemList.PURIFYING_FRUIT, "정화의 힘이 담긴 열매다");
        createItem(ItemList.SMALL_PURIFYING_SEED, "정화의 씨앗이다\n[수확]: 1 정화의 열매/20분");
        createItem(ItemList.PURIFYING_SEED, "정화의 씨앗이다(씨앗 Lv.2)\n[수확]: 1 정화의 열매/10분");
        createItem(ItemList.BIG_PURIFYING_SEED, "정화의 씨앗이다(씨앗 Lv.4)\n[수확]: 2 정화의 열매/8분");
        
        item = new Item(ItemList.HARDENED_SLIME, "적당히 탱탱해진 슬라임이다");
        item.addRecipe(new LinkedHashMap<Long, Integer>() {{
            put(ItemList.PIECE_OF_SLIME.getId(), 10);
            put(ItemList.PURIFYING_FRUIT.getId(), 1);
        }});
        Config.unloadObject(item);
        
        createItem(ItemList.OWLBEAR_HEAD, "부엉이의 형태를 지닌 아울베어의 머리다");

        createItem(ItemList.FARM_EXPAND_DEED, "농장을 1칸 확장시킬 수 있게 하는 문서다(최대 "
                + Config.MAX_FARM_PLANT_COUNT + "칸)");

        item = new Item(ItemList.TITANIUM_STEEL, "티타늄에 합금을 더해 강도를 끌어올린 강철이다");
        item.addRecipe(new LinkedHashMap<Long, Integer>() {{
            put(ItemList.TITANIUM.getId(), 10);
            put(ItemList.ALLOY.getId(), 10);
        }});
        Config.unloadObject(item);

        item = new Item(ItemList.TRACE_OF_SKY, "하늘, 봄, 동쪽, 인(仁) 을 뜻한다");
        item.addRecipe(new LinkedHashMap<Long, Integer>() {{
            put(ItemList.HARPY_WING.getId(), 10);
        }});
        Config.unloadObject(item);

        item = new Item(ItemList.TRACE_OF_EARTH, "땅, 여름, 서쪽, 의(義) 를 뜻한다");
        item.addRecipe(new LinkedHashMap<Long, Integer>() {{
            put(ItemList.HARD_IRON.getId(), 3);
            put(ItemList.ALLOY.getId(), 3);
        }});
        Config.unloadObject(item);

        item = new Item(ItemList.TRACE_OF_WATER, "달, 겨울, 북쪽, 지(智) 를 뜻한다");
        item.addRecipe(new LinkedHashMap<Long, Integer>() {{
            put(ItemList.LAPIS.getId(), 30);
            put(ItemList.BLUE_SPHERE.getId(), 5);
        }});
        Config.unloadObject(item);

        item = new Item(ItemList.TRACE_OF_FIRE, "해, 가을, 남쪽, 예(禮) 를 뜻한다");
        item.addRecipe(new LinkedHashMap<Long, Integer>() {{
            put(ItemList.RED_STONE.getId(), 30);
            put(ItemList.RED_SPHERE.getId(), 5);
        }});
        Config.unloadObject(item);
        
        createItem(ItemList.LYCANTHROPE_TOOTH, "알 수 없는 살기와 강인함이 느껴지는 이빨이다");
        createItem(ItemList.LYCANTHROPE_LEATHER, "알 수 없는 살기와 강인함이 느껴지는 가죽이다");
        createItem(ItemList.LYCANTHROPE_HEAD, "죽었지만 그 눈은 죽지 않은 것처럼 보인다");
        createItem(ItemList.LYCANTHROPE_HEART, "온기와 냉기가 같이 느껴지는 특이한 심장이다");
        createItem(ItemList.LYCANTHROPE_SOUL, "늑대의 강력한 힘이 느껴지는 영혼이다");

        createItem(ItemList.MOON_STONE, "라이칸스로프의 전리품이 한개씩 모인 후 사용한다면 무슨 일이 일어날까?");

        createItem(ItemList.SKILL_BOOK_CUTTING_MOONLIGHT, "스킬 " +
                Emoji.focus(SkillList.CUTTING_MOONLIGHT.getDisplayName()) + " 를 배울 수 있게 해주는 스킬 북이다");
        
        createItem(ItemList.SKILL_BOOK_ESSENCE_DRAIN, "스킬 " +
            Emoji.focus(SkillList.ESSENCE_DRAIN.getDisplayName()) + " 를 배울 수 있게 해주는 스킬 북이다");
        createItem(ItemList.SKILL_BOOK_MAGIC_DRAIN, "스킬 " +
            Emoji.focus(SkillList.MAGIC_DRAIN.getDisplayName()) + " 를 배울 수 있게 해주는 스킬 북이다");
        createItem(ItemList.SKILL_BOOK_MANA_EXPLOSION, "스킬 " +
            Emoji.focus(SkillList.MANA_EXPLOSION.getDisplayName()) + " 를 배울 수 있게 해주는 스킬 북이다");

        Logger.i("ObjectMaker", "Item making is done!");
    }

}
