package lkd.namsic.game.object.implement;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import lkd.namsic.game.base.Use;
import lkd.namsic.game.config.Config;
import lkd.namsic.game.config.Emoji;
import lkd.namsic.game.config.RandomList;
import lkd.namsic.game.enums.Id;
import lkd.namsic.game.enums.StatType;
import lkd.namsic.game.enums.object.BossList;
import lkd.namsic.game.enums.object.EquipList;
import lkd.namsic.game.enums.object.ItemList;
import lkd.namsic.game.enums.object.MapList;
import lkd.namsic.game.enums.object.SkillList;
import lkd.namsic.game.exception.WeirdCommandException;
import lkd.namsic.game.manager.FarmManager;
import lkd.namsic.game.manager.FightManager;
import lkd.namsic.game.object.Boss;
import lkd.namsic.game.object.Entity;
import lkd.namsic.game.object.Equipment;
import lkd.namsic.game.object.Farm;
import lkd.namsic.game.object.GameMap;
import lkd.namsic.game.object.Player;
import lkd.namsic.setting.Logger;

public class ItemUses {

    private final static String SKILL_BOOK = "스킬 북을 사용했습니다";
    private final static String SKILL_ALREADY_EXIST = "이미 해당 스킬을 보유하고 있습니다";

    public final static Map<Long, Use> MAP = new HashMap<Long, Use>() {{
        put(ItemList.SMALL_GOLD_BAG.getId(), new Use() {
            @NonNull
            @Override
            public String use(@NonNull Entity self, @Nullable String other) {
                int money = Math.min(1500, new Random().nextInt(10 * self.getLv()) + 500);

                self.addMoney(money);
                return "골드 주머니를 사용하여 " + money + "G 를 획득했습니다\n" + Emoji.GOLD + " 골드: " + self.getMoney();
            }
        });

        put(ItemList.GOLD_BAG.getId(), new Use() {
            @NonNull
            @Override
            public String use(@NonNull Entity self, @Nullable String other) {
                int money = Math.min(10000, new Random().nextInt(50 * self.getLv()) + 3000);

                self.addMoney(money);
                return "골드 보따리를 사용하여 " + money + "G 를 획득했습니다\n" + Emoji.GOLD + " 골드: " + self.getMoney();
            }
        });

        put(ItemList.LOW_HP_POTION.getId(), new Use() {
            @NonNull
            @Override
            public String use(@NonNull Entity self, @Nullable String other) {
                self.addBasicStat(StatType.HP, (int) (self.getStat(StatType.MAXHP) * 0.15));
                return "최대 체력의 15%를 회복했습니다\n현재 체력: " + self.getDisplayHp();
            }
        });

        put(ItemList.HP_POTION.getId(), new Use() {
            @NonNull
            @Override
            public String use(@NonNull Entity self, @Nullable String other) {
                self.addBasicStat(StatType.HP, self.getStat(StatType.MAXHP) / 2);
                return "최대 체력의 50%를 회복했습니다\n현재 체력: " + self.getDisplayHp();
            }
        });

        put(ItemList.HIGH_HP_POTION.getId(), new Use() {
            @NonNull
            @Override
            public String use(@NonNull Entity self, @Nullable String other) {
                self.setBasicStat(StatType.HP, self.getStat(StatType.MAXHP));
                return "체력을 100% 회복했습니다\n현재 체력: " + self.getDisplayHp();
            }
        });

        put(ItemList.LOW_MP_POTION.getId(), new Use() {
            @NonNull
            @Override
            public String use(@NonNull Entity self, @Nullable String other) {
                self.addBasicStat(StatType.MN, (int) (self.getStat(StatType.MAXMN) * 0.15));
                return "최대 마나의 15%를 회복했습니다\n현재 마나: " + self.getDisplayMn();
            }
        });

        put(ItemList.MP_POTION.getId(), new Use() {
            @NonNull
            @Override
            public String use(@NonNull Entity self, @Nullable String other) {
                self.setBasicStat(StatType.MN, self.getStat(StatType.MAXMN) / 2);
                return "최대 마나의 50%를 회복했습니다\n현재 마나: " + self.getDisplayMn();
            }
        });

        put(ItemList.HIGH_MP_POTION.getId(), new Use() {
            @NonNull
            @Override
            public String use(@NonNull Entity self, @Nullable String other) {
                self.setBasicStat(StatType.MN, self.getStat(StatType.MAXMN));
                return "마나를 100% 회복했습니다\n현재 마나: " + self.getDisplayMn();
            }
        });

        put(ItemList.STAT_POINT.getId(), new Use() {
            @NonNull
            @Override
            public String use(@NonNull Entity self, @Nullable String other) {
                if (Math.random() < 0.5) {
                    Player player = (Player) self;

                    player.setSp(player.getSp() + 1);
                    return "스텟 포인트를 1 획득하였습니다\n" + Emoji.SP + " 스텟 포인트: " + player.getSp();
                } else {
                    return "스텟 포인트를 획득하는데에 실패했습니다...";
                }
            }
        });

        put(ItemList.ADV_STAT.getId(), new Use() {
            @NonNull
            @Override
            public String use(@NonNull Entity self, @Nullable String other) {
                if (Math.random() < 0.5) {
                    Player player = (Player) self;

                    player.setAdv(player.getAdv() + 1);
                    return "모험 스텟을 1 획득하였습니다\n" + Emoji.ADV + " 모험: " + player.getAdv();
                } else {
                    return "모험 스텟을 획득하는데에 실패했습니다...";
                }
            }
        });

        put(ItemList.LOW_RECIPE.getId(), new Use() {
            @NonNull
            @Override
            public String use(@NonNull Entity self, @Nullable String other) {
                Random random = new Random();
                Player player = (Player) self;

                if (random.nextBoolean()) {
                    long itemId = RandomList.lowRecipeItems.get(random.nextInt(RandomList.lowRecipeItems.size()));

                    player.getItemRecipe().add(itemId);
                    return ItemList.findById(itemId) + " 의 제작법을 획득했습니다";
                } else {
                    long equipId = RandomList.lowRecipeEquips.get(random.nextInt(RandomList.lowRecipeEquips.size()));

                    player.getEquipRecipe().add(equipId);
                    return EquipList.findById(equipId) + " 의 제작법을 획득했습니다";
                }
            }
        });

        put(ItemList.RECIPE.getId(), new Use() {
            @NonNull
            @Override
            public String use(@NonNull Entity self, @Nullable String other) {
                Random random = new Random();
                Player player = (Player) self;

                if (random.nextBoolean()) {
                    long itemId = RandomList.middleRecipeItems.get(random.nextInt(RandomList.middleRecipeItems.size()));

                    player.getItemRecipe().add(itemId);
                    return ItemList.findById(itemId) + " 의 제작법을 획득했습니다";
                } else {
                    long equipId = RandomList.middleRecipeEquips.get(random.nextInt(RandomList.middleRecipeEquips.size()));

                    player.getEquipRecipe().add(equipId);
                    return EquipList.findById(equipId) + " 의 제작법을 획득했습니다";
                }
            }
        });

        put(ItemList.HIGH_RECIPE.getId(), new Use() {
            @NonNull
            @Override
            public String use(@NonNull Entity self, @Nullable String other) {
                Random random = new Random();
                Player player = (Player) self;

                if (random.nextBoolean()) {
                    long itemId = RandomList.highRecipeItems.get(random.nextInt(RandomList.highRecipeItems.size()));

                    player.getItemRecipe().add(itemId);
                    return ItemList.findById(itemId) + " 의 제작법을 획득했습니다";
                } else {
                    long equipId = RandomList.highRecipeEquips.get(random.nextInt(RandomList.highRecipeEquips.size()));

                    player.getEquipRecipe().add(equipId);
                    return EquipList.findById(equipId) + " 의 제작법을 획득했습니다";
                }
            }
        });

        put(ItemList.LOW_EXP_POTION.getId(), new Use() {
            @NonNull
            @Override
            public String use(@NonNull Entity self, @Nullable String other) {
                long exp = Math.min(50000, new Random().nextInt(500 * self.getLv()) + 20000);

                Player player = (Player) self;
                player.addExp(exp);
                return "하급 경험치 포션을 사용하여 " + exp + " 경험치를 획득했습니다\n" + Emoji.LV + " 레벨: " + player.getDisplayLv();
            }
        });

        put(ItemList.EXP_POTION.getId(), new Use() {
            @NonNull
            @Override
            public String use(@NonNull Entity self, @Nullable String other) {
                long exp = Math.min(1_000_000, new Random().nextInt(8000 * self.getLv()) + 150_000);

                Player player = (Player) self;
                player.addExp(exp);
                return "중급 경험치 포션을 사용하여 " + exp + " 경험치를 획득했습니다\n" + Emoji.LV + " 레벨: " + player.getDisplayLv();
            }
        });

        put(ItemList.HIGH_EXP_POTION.getId(), new Use() {
            @NonNull
            @Override
            public String use(@NonNull Entity self, @Nullable String other) {
                int lv = self.getLv();
                long exp = Math.min(30_000_000, new Random().nextInt(100_000 * lv) + 3_000_000);

                if (lv <= 200) {
                    exp *= 0.2;
                } else {
                    exp *= Math.min(0.2 - (lv - 200 / 100d), 1);
                }

                exp = Math.max(800_000, exp);

                Player player = (Player) self;
                player.addExp(exp);
                return "상급 경험치 포션을 사용하여 " + exp + " 경험치를 획득했습니다\n" + Emoji.LV + " 레벨: " + player.getDisplayLv();
            }
        });

        put(ItemList.EMPTY_SPHERE.getId(), new Use() {
            @NonNull
            @Override
            public String use(@NonNull Entity self, @Nullable String other) {
                long itemId = new Random().nextInt(11) + ItemList.RED_SPHERE.getId();
                self.addItem(itemId, 1, false);

                return ItemList.findById(itemId) + " " + 1 + "개를 획득헀습니다\n" +
                        "현재 개수: " + self.getItem(itemId);
            }
        });

        put(ItemList.LOW_AMULET.getId(), new Use() {
            @NonNull
            @Override
            public String use(@NonNull Entity self, @Nullable String other) {
                long equipId = RandomList.lowAmulets.get(new Random().nextInt(RandomList.lowAmulets.size()));
                self.addEquip(equipId);

                return EquipList.findById(equipId) + " (을/를) 획득하였습니다";
            }
        });

        put(ItemList.AMULET.getId(), new Use() {
            @NonNull
            @Override
            public String use(@NonNull Entity self, @Nullable String other) {
                long equipId = RandomList.middleAmulets.get(new Random().nextInt(RandomList.middleAmulets.size()));
                self.addEquip(equipId);

                return EquipList.findById(equipId) + " (을/를) 획득하였습니다";
            }
        });

        put(ItemList.HIGH_AMULET.getId(), new Use() {
            @NonNull
            @Override
            public String use(@NonNull Entity self, @Nullable String other) {
                long equipId = RandomList.highAmulets.get(new Random().nextInt(RandomList.highAmulets.size()));
                self.addEquip(equipId);

                return EquipList.findById(equipId) + " (을/를) 획득하였습니다";
            }
        });

        put(ItemList.LOW_ELIXIR.getId(), new Use() {
            @NonNull
            @Override
            public String use(@NonNull Entity self, @Nullable String other) {
                self.addBasicStat(StatType.HP, (int) (self.getStat(StatType.MAXHP) * 0.15));
                self.addBasicStat(StatType.MN, (int) (self.getStat(StatType.MAXMN) * 0.15));
                return "최대 체력과 마나의 15%를 회복했습니다\n현재 체력: " + self.getDisplayHp() + "\n현재 마나: " + self.getDisplayMn();
            }
        });

        put(ItemList.ELIXIR.getId(), new Use() {
            @NonNull
            @Override
            public String use(@NonNull Entity self, @Nullable String other) {
                self.addBasicStat(StatType.HP, self.getStat(StatType.MAXHP) / 2);
                self.addBasicStat(StatType.MN, self.getStat(StatType.MAXMN) / 2);
                return "최대 체력과 마나의 50%를 회복했습니다\n현재 체력: " + self.getDisplayHp() + "\n현재 마나: " + self.getDisplayMn();
            }
        });

        put(ItemList.HIGH_ELIXIR.getId(), new Use() {
            @NonNull
            @Override
            public String use(@NonNull Entity self, @Nullable String other) {
                self.addBasicStat(StatType.HP, self.getStat(StatType.MAXHP));
                self.addBasicStat(StatType.MN, self.getStat(StatType.MAXMN));
                return "최대 체력과 마나의 모두 회복했습니다\n현재 체력: " + self.getDisplayHp() + "\n현재 마나: " + self.getDisplayMn();
            }
        });

        put(ItemList.STONE_LUMP.getId(), new Use() {
            @NonNull
            @Override
            public String use(@NonNull Entity self, @Nullable String other) {
                self.addItem(ItemList.STONE.getId(), 10, false);
                return "돌 10개를 얻었습니다\n현재 개수: " + self.getItem(ItemList.STONE.getId()) + "개";
            }
        });

        put(ItemList.EQUIP_SAFENER.getId(), new Use() {
            @Override
            public int getMinTargetCount() {
                return 1;
            }

            @Override
            public int getMaxTargetCount() {
                return 1;
            }

            @Nullable
            @Override
            public String checkOther(@NonNull Entity self, @NonNull String... other) {
                String indexStr = Objects.requireNonNull(other[0]);

                try {
                    int index = Integer.parseInt(indexStr);
                    self.getEquipIdByIndex(index);
                } catch (NumberFormatException e) {
                    return "대상을 정확하게 입력해주세요(장비 번호)";
                } catch (WeirdCommandException e) {
                    return e.getMessage();
                }

                return null;
            }

            @NonNull
            @Override
            public String use(@NonNull Entity self, @Nullable String other) {
                if (other == null) {
                    throw new NullPointerException();
                }

                long equipId = self.getEquipIdByIndex(Integer.parseInt(other));
                Equipment equipment = Config.loadObject(Id.EQUIPMENT, equipId);

                equipment.addLvDown(5);
                Config.unloadObject(equipment);

                return equipment.getName() + " 의 장착 제한 레벨을 5 낮췄습니다\n장착 제한 레벨: " + equipment.getTotalLimitLv();
            }
        });

        put(ItemList.REINFORCE_MULTIPLIER.getId(), new Use() {
            @NonNull
            @Override
            public String use(@NonNull Entity self, @Nullable String other) {
                Player player = (Player) self;

                double reinforceMultiplier = player.getReinforceMultiplier();
                if (reinforceMultiplier > 2) {
                    throw new WeirdCommandException("이미 강화 확률이 " + reinforceMultiplier + "배로 증가되어 있습니다");
                }

                player.setReinforceMultiplier(2);
                return "다음 강화 확률이 2배로 증가되었습니다";
            }
        });

        put(ItemList.GLOW_REINFORCE_MULTIPLIER.getId(), new Use() {
            @NonNull
            @Override
            public String use(@NonNull Entity self, @Nullable String other) {
                Player player = (Player) self;

                double reinforceMultiplier = player.getReinforceMultiplier();
                if (reinforceMultiplier > 3) {
                    throw new WeirdCommandException("이미 강화 확률이 " + reinforceMultiplier + "배로 증가되어 있습니다");
                }

                player.setReinforceMultiplier(3);
                return "다음 강화 확률이 3배로 증가되었습니다";
            }
        });

        put(ItemList.SKILL_BOOK_MAGIC_BALL.getId(), new Use() {
            @Override
            public void checkUse(@NonNull Entity self, @Nullable String other) {
                super.checkUse(self, other);

                if (self.getSkill().contains(SkillList.MAGIC_BALL.getId())) {
                    throw new WeirdCommandException(SKILL_ALREADY_EXIST);
                }
            }

            @NonNull
            @Override
            public String use(@NonNull Entity self, @Nullable String other) {
                self.addSkill(SkillList.MAGIC_BALL.getId());
                return SKILL_BOOK;
            }
        });

        put(ItemList.SKILL_BOOK_SMITE.getId(), new Use() {
            @Override
            public void checkUse(@NonNull Entity self, @Nullable String other) {
                super.checkUse(self, other);

                if (self.getSkill().contains(SkillList.SMITE.getId())) {
                    throw new WeirdCommandException(SKILL_ALREADY_EXIST);
                }
            }

            @NonNull
            @Override
            public String use(@NonNull Entity self, @Nullable String other) {
                self.addSkill(SkillList.SMITE.getId());
                return SKILL_BOOK;
            }
        });

        put(ItemList.SKILL_BOOK_LASER.getId(), new Use() {
            @Override
            public void checkUse(@NonNull Entity self, @Nullable String other) {
                super.checkUse(self, other);

                if (self.getSkill().contains(SkillList.LASER.getId())) {
                    throw new WeirdCommandException(SKILL_ALREADY_EXIST);
                }
            }

            @NonNull
            @Override
            public String use(@NonNull Entity self, @Nullable String other) {
                self.addSkill(SkillList.LASER.getId());
                return SKILL_BOOK;
            }
        });

        put(ItemList.SKILL_BOOK_SCAR.getId(), new Use() {
            @Override
            public void checkUse(@NonNull Entity self, @Nullable String other) {
                super.checkUse(self, other);

                if (self.getSkill().contains(SkillList.SCAR.getId())) {
                    throw new WeirdCommandException(SKILL_ALREADY_EXIST);
                }
            }

            @NonNull
            @Override
            public String use(@NonNull Entity self, @Nullable String other) {
                self.addSkill(SkillList.SCAR.getId());
                return SKILL_BOOK;
            }
        });

        put(ItemList.SKILL_BOOK_CHARM.getId(), new Use() {
            @Override
            public void checkUse(@NonNull Entity self, @Nullable String other) {
                super.checkUse(self, other);

                if (self.getSkill().contains(SkillList.CHARM.getId())) {
                    throw new WeirdCommandException(SKILL_ALREADY_EXIST);
                }
            }

            @NonNull
            @Override
            public String use(@NonNull Entity self, @Nullable String other) {
                self.addSkill(SkillList.CHARM.getId());
                return SKILL_BOOK;
            }
        });

        put(ItemList.SKILL_BOOK_STRINGS_OF_LIFE.getId(), new Use() {
            @Override
            public void checkUse(@NonNull Entity self, @Nullable String other) {
                super.checkUse(self, other);

                if (self.getSkill().contains(SkillList.STRINGS_OF_LIFE.getId())) {
                    throw new WeirdCommandException(SKILL_ALREADY_EXIST);
                }
            }

            @NonNull
            @Override
            public String use(@NonNull Entity self, @Nullable String other) {
                self.addSkill(SkillList.STRINGS_OF_LIFE.getId());
                return SKILL_BOOK;
            }
        });

        put(ItemList.SKILL_BOOK_RESIST.getId(), new Use() {
            @Override
            public void checkUse(@NonNull Entity self, @Nullable String other) {
                super.checkUse(self, other);

                if (self.getSkill().contains(SkillList.RESIST.getId())) {
                    throw new WeirdCommandException(SKILL_ALREADY_EXIST);
                }
            }

            @NonNull
            @Override
            public String use(@NonNull Entity self, @Nullable String other) {
                self.addSkill(SkillList.RESIST.getId());
                return SKILL_BOOK;
            }
        });

        put(ItemList.CONFIRMED_LOW_RECIPE.getId(), new Use() {
            @Override
            public int getMinTargetCount() {
                return 1;
            }

            @Override
            public int getMaxTargetCount() {
                return 1;
            }

            @Nullable
            @Override
            public String checkOther(@NonNull Entity self, @NonNull String... other) {
                String objectName = other[0];

                boolean isItem = true;
                Long objectId = ItemList.findByName(objectName);

                if (objectId == null) {
                    objectId = EquipList.findByName(objectName);

                    if (objectId == null) {
                        return "알 수 없는 아이템 또는 장비입니다\n이름을 다시 한번 확인해주세요";
                    }

                    isItem = false;
                }

                Player player = (Player) self;

                if (isItem) {
                    if (player.getItemRecipe().contains(objectId)) {
                        return "해당 아이템의 제작법은 이미 알고 있습니다";
                    }

                    if (!RandomList.lowRecipeItems.contains(objectId)) {
                        return "해당 아이템은 하급 제작법에 포함되지 않습니다";
                    }
                } else {
                    if (player.getEquipRecipe().contains(objectId)) {
                        return "해당 장비의 제작법은 이미 알고 있습니다";
                    }

                    if (!RandomList.lowRecipeEquips.contains(objectId)) {
                        return "해당 장비는 하급 제작법에 포함되지 않습니다";
                    }
                }

                return null;
            }

            @NonNull
            @Override
            public String use(@NonNull Entity self, @Nullable String other) {
                boolean isItem = true;
                Long objectId = ItemList.findByName(Objects.requireNonNull(other));

                if (objectId == null) {
                    isItem = false;
                    objectId = Objects.requireNonNull(EquipList.findByName(other));
                }

                Player player = (Player) self;
                if (isItem) {
                    player.getItemRecipe().add(objectId);
                } else {
                    player.getEquipRecipe().add(objectId);
                }

                return other + " 의 제작법을 획득했습니다";
            }
        });

        put(ItemList.CONFIRMED_RECIPE.getId(), new Use() {
            @Override
            public int getMinTargetCount() {
                return 1;
            }

            @Override
            public int getMaxTargetCount() {
                return 1;
            }

            @Nullable
            @Override
            public String checkOther(@NonNull Entity self, @NonNull String... other) {
                String objectName = other[0];

                boolean isItem = true;
                Long objectId = ItemList.findByName(objectName);

                if (objectId == null) {
                    objectId = EquipList.findByName(objectName);

                    if (objectId == null) {
                        return "알 수 없는 아이템 또는 장비입니다\n이름을 다시 한번 확인해주세요";
                    }

                    isItem = false;
                }

                Player player = (Player) self;

                if (isItem) {
                    if (player.getItemRecipe().contains(objectId)) {
                        return "해당 아이템의 제작법은 이미 알고 있습니다";
                    }

                    if (!RandomList.middleRecipeItems.contains(objectId)) {
                        return "해당 아이템은 중급 제작법에 포함되지 않습니다";
                    }
                } else {
                    if (player.getEquipRecipe().contains(objectId)) {
                        return "해당 장비의 제작법은 이미 알고 있습니다";
                    }

                    if (!RandomList.middleRecipeEquips.contains(objectId)) {
                        return "해당 장비는 중급 제작법에 포함되지 않습니다";
                    }
                }

                return null;
            }

            @NonNull
            @Override
            public String use(@NonNull Entity self, @Nullable String other) {
                boolean isItem = true;
                Long objectId = ItemList.findByName(Objects.requireNonNull(other));

                if (objectId == null) {
                    isItem = false;
                    objectId = Objects.requireNonNull(EquipList.findByName(other));
                }

                Player player = (Player) self;
                if (isItem) {
                    player.getItemRecipe().add(objectId);
                } else {
                    player.getEquipRecipe().add(objectId);
                }

                return other + " 의 제작법을 획득했습니다";
            }
        });

        put(ItemList.CONFIRMED_HIGH_RECIPE.getId(), new Use() {
            @Override
            public int getMinTargetCount() {
                return 1;
            }

            @Override
            public int getMaxTargetCount() {
                return 1;
            }

            @Nullable
            @Override
            public String checkOther(@NonNull Entity self, @NonNull String... other) {
                String objectName = other[0];

                boolean isItem = true;
                Long objectId = ItemList.findByName(objectName);

                if (objectId == null) {
                    objectId = EquipList.findByName(objectName);

                    if (objectId == null) {
                        return "알 수 없는 아이템 또는 장비입니다\n이름을 다시 한번 확인해주세요";
                    }

                    isItem = false;
                }

                Player player = (Player) self;

                if (isItem) {
                    if (player.getItemRecipe().contains(objectId)) {
                        return "해당 아이템의 제작법은 이미 알고 있습니다";
                    }

                    if (!RandomList.highRecipeItems.contains(objectId)) {
                        return "해당 아이템은 상급 제작법에 포함되지 않습니다";
                    }
                } else {
                    if (player.getEquipRecipe().contains(objectId)) {
                        return "해당 장비의 제작법은 이미 알고 있습니다";
                    }

                    if (!RandomList.highRecipeEquips.contains(objectId)) {
                        return "해당 장비는 상급 제작법에 포함되지 않습니다";
                    }
                }

                return null;
            }

            @NonNull
            @Override
            public String use(@NonNull Entity self, @Nullable String other) {
                boolean isItem = true;
                Long objectId = ItemList.findByName(Objects.requireNonNull(other));

                if (objectId == null) {
                    isItem = false;
                    objectId = Objects.requireNonNull(EquipList.findByName(other));
                }

                Player player = (Player) self;
                if (isItem) {
                    player.getItemRecipe().add(objectId);
                } else {
                    player.getEquipRecipe().add(objectId);
                }

                return other + " 의 제작법을 획득했습니다";
            }
        });

        put(ItemList.SKILL_BOOK_RUSH.getId(), new Use() {
            @Override
            public void checkUse(@NonNull Entity self, @Nullable String other) {
                super.checkUse(self, other);

                if (self.getSkill().contains(SkillList.RUSH.getId())) {
                    throw new WeirdCommandException(SKILL_ALREADY_EXIST);
                }
            }

            @NonNull
            @Override
            public String use(@NonNull Entity self, @Nullable String other) {
                self.addSkill(SkillList.RUSH.getId());
                return SKILL_BOOK;
            }
        });

        put(ItemList.SKILL_BOOK_ROAR.getId(), new Use() {
            @Override
            public void checkUse(@NonNull Entity self, @Nullable String other) {
                super.checkUse(self, other);

                if (self.getSkill().contains(SkillList.ROAR.getId())) {
                    throw new WeirdCommandException(SKILL_ALREADY_EXIST);
                }
            }

            @NonNull
            @Override
            public String use(@NonNull Entity self, @Nullable String other) {
                self.addSkill(SkillList.ROAR.getId());
                return SKILL_BOOK;
            }
        });

        put(ItemList.FARM_EXPAND_DEED.getId(), new Use() {
            @Override
            public void checkUse(@NonNull Entity self, @Nullable String other) {
                super.checkUse(self, other);

                Player player = (Player) self;

                FarmManager.getInstance().checkFarm(player);
                Farm farm = Config.getData(Id.FARM, self.getId().getObjectId());

                if (farm.getMaxPlantCount() == Config.MAX_FARM_PLANT_COUNT) {
                    throw new WeirdCommandException("이미 농장이 최대 크기입니다(" + Config.MAX_FARM_PLANT_COUNT + "칸)");
                }
            }

            @NonNull
            @Override
            public String use(@NonNull Entity self, @Nullable String other) {
                Farm farm = Config.loadObject(Id.FARM, self.getId().getObjectId());

                int maxPlantCount = farm.getMaxPlantCount();
                farm.setMaxPlantCount(maxPlantCount + 1);

                Config.unloadObject(farm);

                return "농장이 1칸 확장되었습니다\n" + maxPlantCount + "칸 -> " + (maxPlantCount + 1) + "칸";
            }
        });

        put(ItemList.MOON_STONE.getId(), new Use() {
            @Override
            public void checkUse(@NonNull Entity self, @Nullable String other) {
                super.checkUse(self, other);

                if(self.getItem(ItemList.LYCANTHROPE_TOOTH.getId()) == 0 ||
                        self.getItem(ItemList.LYCANTHROPE_LEATHER.getId()) == 0 ||
                        self.getItem(ItemList.LYCANTHROPE_HEAD.getId()) == 0 ||
                        self.getItem(ItemList.LYCANTHROPE_HEART.getId()) == 0 ||
                        self.getItem(ItemList.LYCANTHROPE_SOUL.getId()) == 0) {
                    throw new WeirdCommandException("이 아이템을 사용하기 위한 준비물이 모두 모이지 않았습니다");
                }
                
                GameMap map = Config.getMapData(self.getLocation());
                if(!map.getLocation().equalsMap(MapList.ABANDONED_PLACE_OF_MOONLIGHT.getLocation())) {
                    throw new WeirdCommandException("달빛의 힘이 부족합니다");
                }
            }

            @NonNull
            @Override
            public String use(@NonNull Entity self, @Nullable String other) {
                self.addItem(ItemList.LYCANTHROPE_TOOTH.getId(), -1);
                self.addItem(ItemList.LYCANTHROPE_LEATHER.getId(), -1);
                self.addItem(ItemList.LYCANTHROPE_HEAD.getId(), -1);
                self.addItem(ItemList.LYCANTHROPE_HEART.getId(), -1);
                self.addItem(ItemList.LYCANTHROPE_SOUL.getId(), -1);

                GameMap map = Config.loadMap(self.getLocation());
                Boss boss = map.spawnBoss(BossList.WOLF_OF_MOON);

                new Thread(() -> {
                    Config.loadObject(Id.BOSS, boss.getId().getObjectId());

                    try {
                        Thread.sleep(500);
                        FightManager.getInstance().startFight((Player) self, boss, false);
                    } catch (Exception e) {
                        Logger.e("ItemUse.MOON_STONE", Config.errorString(e));
                    }

                    Config.unloadObject(boss);
                }).start();

                return BossList.WOLF_OF_MOON.getDisplayName() + "를 소환했습니다!";
            }
        });

        put(ItemList.SKILL_BOOK_CUTTING_MOONLIGHT.getId(), new Use() {
            @Override
            public void checkUse(@NonNull Entity self, @Nullable String other) {
                super.checkUse(self, other);

                if (self.getSkill().contains(SkillList.CUTTING_MOONLIGHT.getId())) {
                    throw new WeirdCommandException(SKILL_ALREADY_EXIST);
                }
            }

            @NonNull
            @Override
            public String use(@NonNull Entity self, @Nullable String other) {
                self.addSkill(SkillList.CUTTING_MOONLIGHT.getId());
                return SKILL_BOOK;
            }
        });
    }};

}
