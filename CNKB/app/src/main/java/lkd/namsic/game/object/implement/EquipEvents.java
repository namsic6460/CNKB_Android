package lkd.namsic.game.object.implement;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import lkd.namsic.game.base.Bool;
import lkd.namsic.game.base.Int;
import lkd.namsic.game.config.Config;
import lkd.namsic.game.enums.EquipType;
import lkd.namsic.game.enums.Id;
import lkd.namsic.game.enums.MapType;
import lkd.namsic.game.enums.StatType;
import lkd.namsic.game.enums.Variable;
import lkd.namsic.game.enums.object.EquipList;
import lkd.namsic.game.enums.object.ItemList;
import lkd.namsic.game.event.DamageEvent;
import lkd.namsic.game.event.DamagedEvent;
import lkd.namsic.game.event.Event;
import lkd.namsic.game.event.MineEvent;
import lkd.namsic.game.event.PreDamageEvent;
import lkd.namsic.game.object.Entity;
import lkd.namsic.game.object.Equipment;
import lkd.namsic.game.object.GameMap;
import lkd.namsic.game.object.Item;
import lkd.namsic.game.object.Player;

public class EquipEvents {

    public final static Map<Long, Map<String, Event>> MAP = new HashMap<Long, Map<String, Event>>() {{
        put(EquipList.MIX_SWORD.getId(), new HashMap<String, Event>() {{
            put(DamageEvent.getName(), new DamageEvent() {
                @Override
                public void onDamage(@NonNull Entity self, @NonNull Entity victim, @NonNull Int totalDmg, 
                                     @NonNull Int totalDra, @NonNull Bool isCrit, boolean canCrit) {
                    int randomValue = new Random().nextInt(3);

                    switch (randomValue) {
                        case 0:
                            break;

                        case 1:
                            totalDmg.multiple(1.1);
                            break;

                        case 2:
                            totalDmg.add(2);
                            break;

                        default:
                            throw new RuntimeException();
                    }
                }
            });
        }});

        put(EquipList.HEART_BREAKER_1.getId(), new HashMap<String, Event>() {{
            put(DamageEvent.getName(), new DamageEvent() {
                @Override
                public void onDamage(@NonNull Entity self, @NonNull Entity victim, @NonNull Int totalDmg, 
                                     @NonNull Int totalDra, @NonNull Bool isCrit, boolean canCrit) {
                    int hp = victim.getStat(StatType.HP);
                    double remainPercent = (double) hp / victim.getStat(StatType.MAXHP);
                    if(remainPercent < 0.1) {
                        totalDmg.set(Math.max(hp, totalDmg.get()));
                    }
                }
            });
        }});

        put(EquipList.HEAD_HUNTER_1.getId(), new HashMap<String, Event>() {{
            put(DamageEvent.getName(), new DamageEvent() {
                @Override
                public void onDamage(@NonNull Entity self, @NonNull Entity victim, @NonNull Int totalDmg, 
                                     @NonNull Int totalDra, @NonNull Bool isCrit, boolean canCrit) {
                    if(canCrit && !isCrit.get()) {
                        if(Math.random() < 0.25) {
                            isCrit.set(true);
                            totalDmg.multiple(2);
                        }
                    }
                }
            });
        }});

        put(EquipList.GHOST_SWORD_1.getId(), new HashMap<String, Event>() {{
            put(DamageEvent.getName(), new DamageEvent() {
                @Override
                public void onDamage(@NonNull Entity self, @NonNull Entity victim, @NonNull Int totalDmg, 
                                     @NonNull Int totalDra, @NonNull Bool isCrit, boolean canCrit) {
                    int maxHp = victim.getStat(StatType.MAXHP);
                    totalDmg.add(maxHp * 0.03);

                    if(self.getId().getId().equals(Id.PLAYER)) {
                        Player player = (Player) self;

                        if(player.getObjectVariable(Variable.GHOST_SWORD_1, false)) {
                            totalDmg.multiple(1.66);
                            player.removeVariable(Variable.GHOST_SWORD_1);
                        }
                    }
                }
            });
        }});

        put(EquipList.LEATHER_HELMET.getId(), new HashMap<String, Event>() {{
            put(DamagedEvent.getName(), new DamagedEvent() {
                @Override
                public void onDamaged(@NonNull Entity self, @NonNull Entity attacker,
                                      @NonNull Int totalDmg, @NonNull Int totalDra, @NonNull Bool isCrit) {
                    if(isCrit.get()) {
                        totalDmg.multiple(0.8);
                    }
                }
            });
        }});

        put(EquipList.LOW_ALLOY_HELMET.getId(), new HashMap<String, Event>() {{
            put(DamagedEvent.getName(), new DamagedEvent() {
                @Override
                public void onDamaged(@NonNull Entity self, @NonNull Entity attacker,
                                      @NonNull Int totalDmg, @NonNull Int totalDra, @NonNull Bool isCrit) {
                    if(isCrit.get() && Math.random() < 0.2) {
                        totalDmg.divide(2);
                    }
                }
            });
        }});

        put(EquipList.LOW_ALLOY_SHOES.getId(), new HashMap<String, Event>() {{
            put(DamageEvent.getName(), new DamageEvent() {
                @Override
                public void onDamage(@NonNull Entity self, @NonNull Entity victim, @NonNull Int totalDmg, 
                                     @NonNull Int totalDra, @NonNull Bool isCrit, boolean canCrit) {
                    long equippedId = self.getEquipped(EquipType.WEAPON);
                    if(equippedId == EquipList.NONE.getId()) {
                        return;
                    }

                    Equipment equipped = Config.getData(Id.EQUIPMENT, equippedId);
                    if(equipped.getOriginalId() == EquipList.MIX_SWORD.getId()) {
                        totalDmg.add(5);
                    }
                }
            });
        }});

        put(EquipList.LOW_MANA_SWORD.getId(), new HashMap<String, Event>() {{
            put(DamageEvent.getName(), new DamageEvent() {
                @Override
                public void onDamage(@NonNull Entity self, @NonNull Entity victim, @NonNull Int totalDmg, 
                                     @NonNull Int totalDra, @NonNull Bool isCrit, boolean canCrit) {
                    self.addBasicStat(StatType.MN, 1);
                }
            });
        }});

        put(EquipList.SLIME_HELMET.getId(), new HashMap<String, Event>() {{
            put(DamagedEvent.getName(), new DamagedEvent() {
                @Override
                public void onDamaged(@NonNull Entity self, @NonNull Entity attacker,
                                      @NonNull Int totalDmg, @NonNull Int totalDra, @NonNull Bool isCrit) {
                    double coe;
                    if(isCrit.get()) {
                        coe = 0.15;
                    } else {
                        coe = 0.08;
                    }

                    int heal = (int) (totalDmg.get() * coe);
                    self.addBasicStat(StatType.HP, heal);
                }
            });
        }});

        put(EquipList.SLIME_CHESTPLATE.getId(), new HashMap<String, Event>() {{
            put(DamagedEvent.getName(), new DamagedEvent() {
                @Override
                public void onDamaged(@NonNull Entity self, @NonNull Entity attacker,
                                      @NonNull Int totalDmg, @NonNull Int totalDra, @NonNull Bool isCrit) {
                    double coe;
                    if(isCrit.get()) {
                        coe = 0.4;
                    } else {
                        coe = 0.2;
                    }

                    int saved = (int) (totalDmg.get() * coe);
                    self.setVariable(Variable.SLIME_CHESTPLATE, saved);
                }
            });
            put(DamageEvent.getName(), new DamageEvent() {
                @Override
                public void onDamage(@NonNull Entity self, @NonNull Entity victim, @NonNull Int totalDmg, 
                                     @NonNull Int totalDra, @NonNull Bool isCrit, boolean canCrit) {
                    boolean used = self.getObjectVariable(Variable.SLIME_CHESTPLATE_USE, false);

                    if(used) {
                        int saved = self.getVariable(Variable.SLIME_CHESTPLATE);
                        totalDmg.add(saved);

                        self.removeVariable(Variable.SLIME_CHESTPLATE);
                        self.removeVariable(Variable.SLIME_CHESTPLATE_USE);
                    }
                }
            });
        }});

        put(EquipList.WEIRD_LEGGINGS.getId(), new HashMap<String, Event>() {{
            put(DamagedEvent.getName(), new DamagedEvent() {
                @Override
                public void onDamaged(@NonNull Entity self, @NonNull Entity attacker,
                                      @NonNull Int totalDmg, @NonNull Int totalDra, @NonNull Bool isCrit) {
                    if(Math.random() < 0.1) {
                        totalDmg.divide(2);
                        self.setVariable(Variable.WEIRD_LEGGINGS, true);

                        if(self.getId().getId().equals(Id.PLAYER)) {
                            ((Player) self).replyPlayer("기괴한 바지의 효과로 데미지가 줄어들며, 다음 공격이 강화됩니다");
                        }
                    }
                }
            });
            put(DamageEvent.getName(), new DamageEvent() {
                @Override
                public void onDamage(@NonNull Entity self, @NonNull Entity victim, @NonNull Int totalDmg, 
                                     @NonNull Int totalDra, @NonNull Bool isCrit, boolean canCrit) {
                    boolean weirdLeggings = self.getObjectVariable(Variable.WEIRD_LEGGINGS, false);

                    if(weirdLeggings) {
                        if(canCrit) {
                            isCrit.set(true);
                            totalDmg.multiple(1.5);
                        }

                        self.removeVariable(Variable.WEIRD_LEGGINGS);
                    }
                }
            });
        }});

        put(EquipList.MINER_SHOES.getId(), new HashMap<String, Event>() {{
            put(MineEvent.getName(), new MineEvent() {
                @Override
                public void onMine(@NonNull Entity self, @NonNull Item item, @NonNull Int mineCount) {
                    if(item.getId().getObjectId() == ItemList.STONE.getId() && Math.random() < 0.25) {
                        mineCount.add(1);
                    }
                }
            });
        }});

        put(EquipList.TROLL_CLUB.getId(), new HashMap<String, Event>() {{
            put(DamageEvent.getName(), new DamageEvent() {
                @Override
                public void onDamage(@NonNull Entity self, @NonNull Entity victim, @NonNull Int totalDmg, 
                                     @NonNull Int totalDra, @NonNull Bool isCrit, boolean canCrit) {
                    boolean trollClub = self.getObjectVariable(Variable.TROLL_CLUB, false);

                    if(trollClub) {
                        if(victim.getId().getId().equals(Id.MONSTER) && self.getLv().get() > victim.getLv().get()) {
                            totalDmg.multiple(3);
                        }

                        self.removeVariable(Variable.TROLL_CLUB);
                    }
                }
            });
        }});

        put(EquipList.BONE_SWORD.getId(), new HashMap<String, Event>() {{
            put(DamagedEvent.getName(), new DamagedEvent() {
                @Override
                public void onDamaged(@NonNull Entity self, @NonNull Entity attacker, @NonNull Int totalDmg,
                                      @NonNull Int totalDra, @NonNull Bool isCrit) {
                    long helmetId = self.getEquipped(EquipType.HELMET);
                    long chestplateId = self.getEquipped(EquipType.CHESTPLATE);
                    long leggingsId = self.getEquipped(EquipType.LEGGINGS);
                    long shoesId = self.getEquipped(EquipType.SHOES);

                    if(helmetId == EquipList.NONE.getId() || chestplateId == EquipList.NONE.getId() ||
                            leggingsId == EquipList.NONE.getId() || shoesId == EquipList.NONE.getId()) {
                        return;
                    }

                    Equipment helmet = Config.getData(Id.EQUIPMENT, helmetId);
                    Equipment chestplate = Config.getData(Id.EQUIPMENT, chestplateId);
                    Equipment leggings = Config.getData(Id.EQUIPMENT, leggingsId);
                    Equipment shoes = Config.getData(Id.EQUIPMENT, shoesId);

                    helmetId = helmet.getOriginalId();
                    chestplateId = chestplate.getOriginalId();
                    leggingsId = leggings.getOriginalId();
                    shoesId = shoes.getOriginalId();

                    if((helmetId == EquipList.BONE_HELMET.getId() || helmetId == EquipList.DEMON_BONE_HELMET.getId()) &&
                            (chestplateId == EquipList.BONE_CHESTPLATE.getId() || chestplateId == EquipList.DEMON_BONE_CHESTPLATE.getId()) &&
                            (leggingsId == EquipList.BONE_LEGGINGS.getId() || leggingsId == EquipList.DEMON_BONE_LEGGINGS.getId()) &&
                            (shoesId == EquipList.BONE_SHOES.getId() || shoesId == EquipList.DEMON_BONE_SHOES.getId())) {
                        totalDmg.multiple(0.8);
                    }
                }
            });
        }});

        put(EquipList.SEA_STAFF.getId(), new HashMap<String, Event>() {{
            put(PreDamageEvent.getName(), new PreDamageEvent() {
                @Override
                public void onPreDamage(@NonNull Entity self, @NonNull Entity victim, @NonNull Int physicDmg,
                                        @NonNull Int magicDmg, @NonNull Int staticDmg, boolean canCrit) {
                    GameMap map = Config.getMapData(self.getLocation());
                    MapType mapType = map.getMapType();

                    if(MapType.waterList().contains(mapType) || mapType.equals(MapType.CORRUPTED_RIVER)) {
                        if(canCrit && Math.random() < 0.5) {
                            magicDmg.multiple(1.25);
                        }
                    }
                }
            });
        }});

        put(EquipList.HEART_BREAKER_2.getId(), new HashMap<String, Event>() {{
            put(DamageEvent.getName(), new DamageEvent() {
                @Override
                public void onDamage(@NonNull Entity self, @NonNull Entity victim, @NonNull Int totalDmg,
                                     @NonNull Int totalDra, @NonNull Bool isCrit, boolean canCrit) {
                    int hp = victim.getStat(StatType.HP);
                    double remainPercent = (double) hp / victim.getStat(StatType.MAXHP);
                    if(remainPercent < 0.15) {
                        totalDmg.set(Math.max(hp, totalDmg.get()));
                    }
                }
            });
        }});

        put(EquipList.GHOST_SWORD_2.getId(), new HashMap<String, Event>() {{
            put(DamageEvent.getName(), new DamageEvent() {
                @Override
                public void onDamage(@NonNull Entity self, @NonNull Entity victim, @NonNull Int totalDmg,
                                     @NonNull Int totalDra, @NonNull Bool isCrit, boolean canCrit) {
                    int maxHp = victim.getStat(StatType.MAXHP);
                    totalDmg.add(maxHp * 0.06);

                    if(self.getId().getId().equals(Id.PLAYER)) {
                        Player player = (Player) self;

                        if(player.getObjectVariable(Variable.GHOST_SWORD_2, false)) {
                            totalDmg.multiple(2);
                            player.removeVariable(Variable.GHOST_SWORD_2);
                        }
                    }
                }
            });
        }});
    }};

    @SuppressWarnings("unchecked")
    @NonNull
    public static <T extends Event> T getEvent(long equipId, @NonNull String eventName) {
        return (T) Objects.requireNonNull(Objects.requireNonNull(MAP.get(equipId)).get(eventName));
    }

}
