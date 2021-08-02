package lkd.namsic.game.object.interfaces;

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
import lkd.namsic.game.enums.StatType;
import lkd.namsic.game.enums.Variable;
import lkd.namsic.game.enums.object.EquipList;
import lkd.namsic.game.enums.object.ItemList;
import lkd.namsic.game.event.DamageEvent;
import lkd.namsic.game.event.DamagedEvent;
import lkd.namsic.game.event.Event;
import lkd.namsic.game.event.MineEvent;
import lkd.namsic.game.object.Entity;
import lkd.namsic.game.object.Equipment;
import lkd.namsic.game.object.Item;
import lkd.namsic.game.object.Player;

public class EquipEvents {

    public final static Map<Long, Map<String, Event>> EVENT_MAP = new HashMap<Long, Map<String, Event>>() {{
        put(EquipList.MIX_SWORD.getId(), new HashMap<String, Event>() {{
            put(DamageEvent.getName(), new DamageEvent() {
                @Override
                public void onDamage(@NonNull Entity self, @NonNull Entity victim,
                                     @NonNull Int totalDmg, @NonNull Int totalDra, @NonNull Bool isCrit) {
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
                    }
                }
            });
        }});

        put(EquipList.HEART_BREAKER_1.getId(), new HashMap<String, Event>() {{
            put(DamageEvent.getName(), new DamageEvent() {
                @Override
                public void onDamage(@NonNull Entity self, @NonNull Entity victim,
                                     @NonNull Int totalDmg, @NonNull Int totalDra, @NonNull Bool isCrit) {
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
                public void onDamage(@NonNull Entity self, @NonNull Entity victim,
                                     @NonNull Int totalDmg, @NonNull Int totalDra, @NonNull Bool isCrit) {
                    if(!isCrit.get()) {
                        if(Math.random() < 0.2) {
                            isCrit.set(true);
                            totalDmg.multiple(1.8);
                        }
                    }
                }
            });
        }});

        put(EquipList.GHOST_SWORD_1.getId(), new HashMap<String, Event>() {{
            put(DamageEvent.getName(), new DamageEvent() {
                @Override
                public void onDamage(@NonNull Entity self, @NonNull Entity victim,
                                     @NonNull Int totalDmg, @NonNull Int totalDra, @NonNull Bool isCrit) {
                    int maxHp = victim.getStat(StatType.MAXHP);
                    totalDmg.add(maxHp * 0.06);

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
                        totalDmg.multiple(0.95);
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
                public void onDamage(@NonNull Entity self, @NonNull Entity victim,
                                     @NonNull Int totalDmg, @NonNull Int totalDra, @NonNull Bool isCrit) {
                    long equippedId = self.getEquipped(EquipType.WEAPON);
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
                public void onDamage(@NonNull Entity self, @NonNull Entity victim,
                                     @NonNull Int totalDmg, @NonNull Int totalDra, @NonNull Bool isCrit) {
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
                public void onDamage(@NonNull Entity self, @NonNull Entity victim,
                                     @NonNull Int totalDmg, @NonNull Int totalDra, @NonNull Bool isCrit) {
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
                        totalDmg.set(0);
                        self.setVariable(Variable.WEIRD_LEGGINGS, true);

                        if(attacker.getId().getId().equals(Id.PLAYER)) {
                            ((Player) attacker).replyPlayer("상대방의 기괴한 바지의 효과로 공격이 상쇄되었습니다");
                        }

                        if(self.getId().getId().equals(Id.PLAYER)) {
                            ((Player) self).replyPlayer("기괴한 바지의 효과로 공격이 상쇄되며, 다음 공격이 치명타가 됩니다");
                        }
                    }
                }
            });
            put(DamageEvent.getName(), new DamageEvent() {
                @Override
                public void onDamage(@NonNull Entity self, @NonNull Entity victim,
                                     @NonNull Int totalDmg, @NonNull Int totalDra, @NonNull Bool isCrit) {
                    boolean weirdLeggings = self.getObjectVariable(Variable.WEIRD_LEGGINGS, false);

                    if(weirdLeggings) {
                        isCrit.set(true);
                        totalDmg.multiple(2);

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
                public void onDamage(@NonNull Entity self, @NonNull Entity victim,
                                     @NonNull Int totalDmg, @NonNull Int totalDra, @NonNull Bool isCrit) {
                    boolean trollClub = self.getObjectVariable(Variable.TROLL_CLUB, false);

                    if(trollClub) {
                        if(victim.getId().getId().equals(Id.MONSTER)) {
                            totalDmg.multiple(3);
                        }

                        self.removeVariable(Variable.TROLL_CLUB);
                    }
                }
            });
        }});
    }};

    @SuppressWarnings("unchecked")
    @NonNull
    public static <T extends Event> T getEvent(long equipId, @NonNull String eventName) {
        return (T) Objects.requireNonNull(Objects.requireNonNull(EVENT_MAP.get(equipId)).get(eventName));
    }

}
