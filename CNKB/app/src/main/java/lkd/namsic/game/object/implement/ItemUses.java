package lkd.namsic.game.object.implement;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import lkd.namsic.game.base.Use;
import lkd.namsic.game.config.Emoji;
import lkd.namsic.game.config.RandomList;
import lkd.namsic.game.enums.StatType;
import lkd.namsic.game.enums.object.EquipList;
import lkd.namsic.game.enums.object.ItemList;
import lkd.namsic.game.object.Player;

public class ItemUses {

    public final static Map<Long, Use> USE_MAP = new HashMap<Long, Use>() {{
        put(ItemList.SMALL_GOLD_BAG.getId(), (self, other) -> {
            int money = Math.min(1000, new Random().nextInt(10 * self.getLv().get()) + 500);

            self.addMoney(money);
            return "골드 주머니를 사용하여 " + money + "G 를 획득했습니다\n" + Emoji.GOLD + " 골드: " + self.getMoney() + "\n";
        });

        put(ItemList.GOLD_BAG.getId(), (self, other) -> {
            int money = Math.min(10000, new Random().nextInt(50 * self.getLv().get()) + 3000);

            self.addMoney(money);
            return "골드 보따리를 사용하여 " + money + "G 를 획득했습니다\n" + Emoji.GOLD + " 골드: " + self.getMoney() + "\n";
        });

        put(ItemList.LOW_HP_POTION.getId(), (self, other) -> {
            self.addBasicStat(StatType.HP, (int) (self.getStat(StatType.MAXHP) * 0.15));
            return "최대 체력의 15%를 회복했습니다\n현재 체력: " + self.getDisplayHp() + "\n";
        });

        put(ItemList.HP_POTION.getId(), (self, other) -> {
            self.addBasicStat(StatType.HP, self.getStat(StatType.MAXHP) / 2);
            return "최대 체력의 50%를 회복했습니다\n현재 체력: " + self.getDisplayHp() + "\n";
        });

        put(ItemList.HIGH_HP_POTION.getId(), (self, other) -> {
            self.setBasicStat(StatType.HP, self.getStat(StatType.MAXHP));
            return "체력을 100% 회복했습니다\n현재 체력: " + self.getDisplayHp() + "\n";
        });

        put(ItemList.LOW_MP_POTION.getId(), (self, other) -> {
            self.addBasicStat(StatType.MN, (int) (self.getStat(StatType.MAXMN) * 0.15));
            return "최대 마나의 15%를 회복했습니다\n현재 마나: " + self.getDisplayMn() + "\n";
        });

        put(ItemList.MP_POTION.getId(), (self, other) -> {
            self.setBasicStat(StatType.MN, self.getStat(StatType.MAXMN) / 2);
            return "최대 마나의 50%를 회복했습니다\n현재 마나: " + self.getDisplayMn() + "\n";
        });

        put(ItemList.HIGH_MP_POTION.getId(), (self, other) -> {
            self.setBasicStat(StatType.MN, self.getStat(StatType.MAXMN));
            return "마나를 100% 회복했습니다\n현재 마나: " + self.getDisplayMn() + "\n";
        });

        put(ItemList.STAT_POINT.getId(), (self, other) -> {
            if (Math.random() < 0.5) {
                Player player = (Player) self;

                player.getSp().add(1);
                return "스텟 포인트를 1 획득하였습니다\n" + Emoji.SP + " 스텟 포인트: " + player.getSp().get() + "\n";
            } else {
                return "스텟 포인트를 획득하는데에 실패했습니다...";
            }
        });

        put(ItemList.ADV_STAT.getId(), (self, other) -> {
            if (Math.random() < 0.5) {
                Player player = (Player) self;

                player.getAdv().add(1);
                return "모험 스텟을 1 획득하였습니다\n" + Emoji.ADV + " 모험: " + player.getAdv().get() + "\n";
            } else {
                return "모험 스텟을 획득하는데에 실패했습니다...";
            }
        });

        put(ItemList.LOW_RECIPE.getId(), (self, other) -> {
            Random random = new Random();
            Player player = (Player) self;

            if(random.nextBoolean()) {
                long itemId = RandomList.lowRecipeItems.get(random.nextInt(RandomList.lowRecipeItems.size()));

                player.getItemRecipe().add(itemId);
                return ItemList.findById(itemId) + " 의 제작법을 획득했습니다";
            } else {
                long equipId = RandomList.lowRecipeEquips.get(random.nextInt(RandomList.lowRecipeEquips.size()));

                player.getEquipRecipe().add(equipId);
                return EquipList.findById(equipId) + " 의 제작법을 획득했습니다";
            }
        });

        put(ItemList.RECIPE.getId(), (self, other) -> {
            Random random = new Random();
            Player player = (Player) self;

            if(random.nextBoolean()) {
                long itemId = RandomList.middleRecipeItems.get(random.nextInt(RandomList.middleRecipeItems.size()));

                player.getItemRecipe().add(itemId);
                return ItemList.findById(itemId) + " 의 제작법을 획득했습니다";
            } else {
                long equipId = RandomList.middleRecipeEquips.get(random.nextInt(RandomList.middleRecipeEquips.size()));

                player.getEquipRecipe().add(equipId);
                return EquipList.findById(equipId) + " 의 제작법을 획득했습니다";
            }
        });

        put(ItemList.HIGH_RECIPE.getId(), (self, other) -> {
            Random random = new Random();
            Player player = (Player) self;

            //TODO : 고급 장비 생성 시 주석 해제
//            if(random.nextBoolean()) {
            long itemId = RandomList.highRecipeItems.get(random.nextInt(RandomList.highRecipeItems.size()));

            player.getItemRecipe().add(itemId);
            return ItemList.findById(itemId) + " 의 제작법을 획득했습니다";
//            } else {
//                long equipId = RandomList.highRecipeEquips.get(random.nextInt(RandomList.highRecipeEquips.size()));
//
//                player.getEquipRecipe().add(equipId);
//                return ItemList.findById(equipId) + " 의 제작법을 획득했습니다";
//            }
        });

        put(ItemList.LOW_EXP_POTION.getId(), (self, other) -> {
            long exp = new Random().nextInt(500 * self.getLv().get()) + 20000;

            Player player = (Player) self;
            player.addExp(exp);
            return "하급 경험치 포션을 사용하여 " + exp + " 경험치를 획득했습니다\n" + Emoji.LV + " 레벨: " + player.getDisplayLv() + "\n";
        });

        put(ItemList.EXP_POTION.getId(), (self, other) -> {
            long exp = new Random().nextInt(8000 * self.getLv().get()) + 150_000;

            Player player = (Player) self;
            player.addExp(exp);
            return "중급 경험치 포션을 사용하여 " + exp + " 경험치를 획득했습니다\n" + Emoji.LV + " 레벨: " + player.getDisplayLv() + "\n";
        });

        put(ItemList.HIGH_EXP_POTION.getId(), (self, other) -> {
            long exp = new Random().nextInt(100_000 * self.getLv().get()) + 3_000_000;

            Player player = (Player) self;
            player.addExp(exp);
            return "상급 경험치 포션을 사용하여 " + exp + " 경험치를 획득했습니다\n" + Emoji.LV + " 레벨: " + player.getDisplayLv() + "\n";
        });

        put(ItemList.EMPTY_SPHERE.getId(), (self, other) -> {
            long itemId = new Random().nextInt(11) + ItemList.RED_SPHERE.getId();
            self.addItem(itemId, 1, false);

            return ItemList.findById(itemId) + " " + 1 + "개를 획득헀습니다\n" +
                    "현재 개수: " + self.getItem(itemId) + "\n";
        });

        put(ItemList.LOW_AMULET.getId(), (self, other) -> {
            long equipId = RandomList.lowAmulets.get(new Random().nextInt(RandomList.lowAmulets.size()));
            self.addEquip(equipId);

            return EquipList.findById(equipId) + " (을/를) 획득하였습니다";
        });

        put(ItemList.AMULET.getId(), (self, other) -> {
            long equipId = RandomList.middleAmulets.get(new Random().nextInt(RandomList.middleAmulets.size()));
            self.addEquip(equipId);

            return EquipList.findById(equipId) + " (을/를) 획득하였습니다";
        });

        put(ItemList.HIGH_AMULET.getId(), (self, other) -> {
            long equipId = RandomList.highAmulets.get(new Random().nextInt(RandomList.highAmulets.size()));
            self.addEquip(equipId);

            return EquipList.findById(equipId) + " (을/를) 획득하였습니다";
        });

        put(ItemList.LOW_ELIXIR.getId(), (self, other) -> {
            self.setBasicStat(StatType.HP, (int) (self.getStat(StatType.MAXHP) * 0.15));
            self.setBasicStat(StatType.MN, (int) (self.getStat(StatType.MAXMN) * 0.15));
            return "최대 체력과 마나의 15%를 회복했습니다\n현재 체력: " + self.getDisplayHp() + "\n현재 마나: " + self.getDisplayMn();
        });

        put(ItemList.ELIXIR.getId(), (self, other) -> {
            self.setBasicStat(StatType.HP, self.getStat(StatType.MAXHP) / 2);
            self.setBasicStat(StatType.MN, self.getStat(StatType.MAXMN) / 2);
            return "최대 체력과 마나의 50%를 회복했습니다\n현재 체력: " + self.getDisplayHp() + "\n현재 마나: " + self.getDisplayMn();
        });

        put(ItemList.HIGH_ELIXIR.getId(), (self, other) -> {
            self.setBasicStat(StatType.HP, self.getStat(StatType.MAXHP));
            self.setBasicStat(StatType.MN, self.getStat(StatType.MAXMN));
            return "최대 체력과 마나의 모두 회복했습니다\n현재 체력: " + self.getDisplayHp() + "\n현재 마나: " + self.getDisplayMn();
        });

        put(ItemList.STONE_LUMP.getId(), (self, other) -> {
            self.addItem(ItemList.STONE.getId(), 10, false);
            return "돌 10개를 얻었습니다\n현재 개수: " + self.getItem(ItemList.STONE.getId()) + "개";
        });
    }};

}
