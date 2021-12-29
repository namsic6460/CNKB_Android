package lkd.namsic.game.object.implement;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import lkd.namsic.game.base.Bool;
import lkd.namsic.game.base.Int;
import lkd.namsic.game.base.LoNg;
import lkd.namsic.game.base.WrappedObject;
import lkd.namsic.game.config.Config;
import lkd.namsic.game.enums.Doing;
import lkd.namsic.game.enums.EquipType;
import lkd.namsic.game.enums.FightWaitType;
import lkd.namsic.game.enums.Id;
import lkd.namsic.game.enums.MapType;
import lkd.namsic.game.enums.StatType;
import lkd.namsic.game.enums.Variable;
import lkd.namsic.game.enums.object.EquipList;
import lkd.namsic.game.enums.object.EventList;
import lkd.namsic.game.enums.object.ItemList;
import lkd.namsic.game.event.AddExpEvent;
import lkd.namsic.game.event.DamageEvent;
import lkd.namsic.game.event.DamagedEvent;
import lkd.namsic.game.event.EndFightEvent;
import lkd.namsic.game.event.Event;
import lkd.namsic.game.event.FishEvent;
import lkd.namsic.game.event.HarvestEvent;
import lkd.namsic.game.event.InjectFightEvent;
import lkd.namsic.game.event.MineEvent;
import lkd.namsic.game.event.PreDamageEvent;
import lkd.namsic.game.event.PreDamagedEvent;
import lkd.namsic.game.event.PreEvadeEvent;
import lkd.namsic.game.event.StartFightEvent;
import lkd.namsic.game.event.TurnEvent;
import lkd.namsic.game.manager.FightManager;
import lkd.namsic.game.object.Entity;
import lkd.namsic.game.object.Equipment;
import lkd.namsic.game.object.GameMap;
import lkd.namsic.game.object.Item;
import lkd.namsic.game.object.Player;

public class EquipEvents {
    
    public final static Map<Long, Map<String, Event>> MAP = new HashMap<Long, Map<String, Event>>() {{
        put(EquipList.MIX_SWORD_1.getId(), new HashMap<String, Event>() {{
            put(DamageEvent.getName(), new DamageEvent() {
                @Override
                public void onDamage(@NonNull Entity self, @NonNull Entity victim, @NonNull Int totalDmg,
                                     @NonNull Int totalDra, @NonNull Bool isCrit, boolean canCrit) {
                    int randomValue = new Random().nextInt(3);
                    
                    switch(randomValue) {
                        case 0:
                            break;
                        
                        case 1:
                            totalDmg.multiple(1.1);
                            break;
                        
                        case 2:
                            totalDmg.add(5);
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
                        totalDmg.multiple(0.85);
                    }
                }
            });
        }});
        
        put(EquipList.LOW_ALLOY_HELMET.getId(), new HashMap<String, Event>() {{
            put(DamagedEvent.getName(), new DamagedEvent() {
                @Override
                public void onDamaged(@NonNull Entity self, @NonNull Entity attacker,
                                      @NonNull Int totalDmg, @NonNull Int totalDra, @NonNull Bool isCrit) {
                    if(isCrit.get() && Math.random() < 0.3) {
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
                    if(equipped.getOriginalId() == EquipList.MIX_SWORD_1.getId()) {
                        totalDmg.add(10);
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
            
            put(EndFightEvent.getName(), new EndFightEvent() {
                @Override
                public void onEndFight(@NonNull Entity self) {
                    self.removeVariable(Variable.SLIME_CHESTPLATE);
                    self.removeVariable(Variable.SLIME_CHESTPLATE_USE);
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
                public void onMine(@NonNull Entity self, @NonNull Item item, @NonNull Int itemCount) {
                    if(item.getId().getObjectId() == ItemList.STONE.getId()) {
                        itemCount.add(1);
                    }
                }
            });
        }});
        
        put(EquipList.TROLL_CLUB.getId(), new HashMap<String, Event>() {{
            put(DamageEvent.getName(), new DamageEvent() {
                @Override
                public void onDamage(@NonNull Entity self, @NonNull Entity victim, @NonNull Int totalDmg,
                                     @NonNull Int totalDra, @NonNull Bool isCrit, boolean canCrit) {
                    boolean used = self.getObjectVariable(Variable.TROLL_CLUB_USE, false);
                    
                    if(used) {
                        if(victim.getId().getId().equals(Id.MONSTER) && self.getLv() > victim.getLv()) {
                            totalDmg.multiple(3);
                        }
                        
                        self.removeVariable(Variable.TROLL_CLUB_USE);
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
                            magicDmg.multiple(1.4);
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
        
        put(EquipList.HEAD_HUNTER_2.getId(), new HashMap<String, Event>() {{
            put(DamageEvent.getName(), new DamageEvent() {
                @Override
                public void onDamage(@NonNull Entity self, @NonNull Entity victim, @NonNull Int totalDmg,
                                     @NonNull Int totalDra, @NonNull Bool isCrit, boolean canCrit) {
                    if(canCrit && !isCrit.get()) {
                        if(Math.random() < 0.5) {
                            isCrit.set(true);
                            totalDmg.multiple(3);
                        }
                    }
                }
            });
        }});
        
        put(EquipList.YIN_YANG_SWORD_1.getId(), new HashMap<String, Event>() {{
            put(StartFightEvent.getName(), new StartFightEvent() {
                @Override
                public void onStartFight(@NonNull Entity self, @NonNull Entity enemy, boolean isOwner) {
                    self.setVariable(Variable.YIN_YANG_SWORD_1, true);
                }
            });
            
            put(InjectFightEvent.getName(), new InjectFightEvent() {
                @Override
                public void onInjectFight(@NonNull Entity self, @NonNull Entity enemy) {
                    self.setVariable(Variable.YIN_YANG_SWORD_1, true);
                }
            });
            
            put(DamageEvent.getName(), new DamageEvent() {
                @Override
                public void onDamage(@NonNull Entity self, @NonNull Entity victim, @NonNull Int totalDmg,
                                     @NonNull Int totalDra, @NonNull Bool isCrit, boolean canCrit) {
                    boolean isFirst = self.getObjectVariable(Variable.YIN_YANG_SWORD_1, true);
                    self.setVariable(Variable.YIN_YANG_SWORD_1, !isFirst);
                    
                    FightWaitType response;
                    
                    Object object = self.getObjectVariable(Variable.FIGHT_WAIT_TYPE);
                    if(object == null) {
                        object = FightWaitType.NONE;
                    }
                    
                    if(object instanceof FightWaitType) {
                        response = (FightWaitType) object;
                    } else {
                        response = FightWaitType.parseWaitType(((String) object).toLowerCase());
                    }
                    
                    if(isFirst) {
                        if(response.equals(FightWaitType.SKILL)) {
                            totalDmg.add(victim.getStat(StatType.MAXHP) * 0.05);
                        }
                    } else {
                        if(response.equals(FightWaitType.ATTACK)) {
                            totalDra.add(self.getStat(StatType.MAXHP) * 0.05);
                        }
                    }
                }
            });
        }});
        
        put(EquipList.SILVER_SWORD.getId(), new HashMap<String, Event>() {{
            put(PreDamagedEvent.getName(), new PreDamagedEvent() {
                @Override
                public void onPreDamaged(@NonNull Entity self, @NonNull Entity attacker, @NonNull Int physicDmg,
                                         @NonNull Int magicDmg, @NonNull Int staticDmg, boolean canCrit) {
                    long helmetId = self.getEquipped(EquipType.HELMET);
                    long chestplateId = self.getEquipped(EquipType.CHESTPLATE);
                    long leggingsId = self.getEquipped(EquipType.LEGGINGS);
                    long shoesId = self.getEquipped(EquipType.SHOES);
                    long ringsId = self.getEquipped(EquipType.RINGS);
                    
                    long noneId = EquipList.NONE.getId();
                    if(helmetId == noneId || chestplateId == noneId || leggingsId == noneId ||
                        shoesId == noneId || ringsId == noneId) {
                        return;
                    }
                    
                    Equipment helmet = Config.getData(Id.EQUIPMENT, helmetId);
                    Equipment chestplate = Config.getData(Id.EQUIPMENT, chestplateId);
                    Equipment leggings = Config.getData(Id.EQUIPMENT, leggingsId);
                    Equipment shoes = Config.getData(Id.EQUIPMENT, shoesId);
                    Equipment rings = Config.getData(Id.EQUIPMENT, ringsId);
                    
                    if(helmet.getOriginalId() == EquipList.SILVER_HELMET.getId() &&
                        chestplate.getOriginalId() == EquipList.SILVER_CHESTPLATE.getId() &&
                        leggings.getOriginalId() == EquipList.SILVER_LEGGINGS.getId() &&
                        shoes.getOriginalId() == EquipList.SILVER_SHOES.getId() &&
                        rings.getOriginalId() == EquipList.SILVER_RING.getId()) {
                        attacker.addEvent(EventList.SILVER_SET_DAMAGE);
                        attacker.addEvent(EventList.SILVER_SET_END);
                    }
                }
            });
        }});
        
        put(EquipList.SILVER_HELMET.getId(), new HashMap<String, Event>() {{
            put(PreDamagedEvent.getName(), new PreDamagedEvent() {
                @Override
                public void onPreDamaged(@NonNull Entity self, @NonNull Entity attacker, @NonNull Int physicDmg,
                                         @NonNull Int magicDmg, @NonNull Int staticDmg, boolean canCrit) {
                    int mdefIncreased = self.getVariable(Variable.SILVER_HELMET);
                    if(mdefIncreased < 50) {
                        self.setVariable(Variable.SILVER_HELMET, mdefIncreased + 10);
                        self.addBasicStat(StatType.MDEF, 10);
                    }
                }
            });
            
            put(EndFightEvent.getName(), new EndFightEvent() {
                @Override
                public void onEndFight(@NonNull Entity self) {
                    int mdefIncreased = self.getVariable(Variable.SILVER_HELMET);
                    if(mdefIncreased > 0) {
                        self.removeVariable(Variable.SILVER_HELMET);
                        self.addBasicStat(StatType.MDEF, -1 * mdefIncreased);
                    }
                }
            });
        }});
        
        put(EquipList.SILVER_SHOES.getId(), new HashMap<String, Event>() {{
            put(PreDamagedEvent.getName(), new PreDamagedEvent() {
                @Override
                public void onPreDamaged(@NonNull Entity self, @NonNull Entity attacker, @NonNull Int physicDmg,
                                         @NonNull Int magicDmg, @NonNull Int staticDmg, boolean canCrit) {
                    int agiIncreased = self.getVariable(Variable.SILVER_SHOES);
                    if(agiIncreased < 50) {
                        self.setVariable(Variable.SILVER_SHOES, agiIncreased + 10);
                        self.addBasicStat(StatType.AGI, 10);
                    }
                }
            });
            
            put(EndFightEvent.getName(), new EndFightEvent() {
                @Override
                public void onEndFight(@NonNull Entity self) {
                    int agiIncreased = self.getVariable(Variable.SILVER_SHOES);
                    if(agiIncreased > 0) {
                        self.removeVariable(Variable.SILVER_SHOES);
                        self.addBasicStat(StatType.AGI, -1 * agiIncreased);
                    }
                }
            });
        }});
        
        put(EquipList.MIX_SWORD_2.getId(), new HashMap<String, Event>() {{
            put(DamageEvent.getName(), new DamageEvent() {
                @Override
                public void onDamage(@NonNull Entity self, @NonNull Entity victim, @NonNull Int totalDmg,
                                     @NonNull Int totalDra, @NonNull Bool isCrit, boolean canCrit) {
                    int randomValue = new Random().nextInt(3);
                    
                    switch(randomValue) {
                        case 0:
                            break;
                        
                        case 1:
                            totalDmg.multiple(1.5);
                            break;
                        
                        case 2:
                            totalDmg.add(33);
                            break;
                        
                        default:
                            throw new RuntimeException();
                    }
                }
            });
        }});
        
        put(EquipList.ALLOY_HELMET.getId(), new HashMap<String, Event>() {{
            put(DamagedEvent.getName(), new DamagedEvent() {
                @Override
                public void onDamaged(@NonNull Entity self, @NonNull Entity attacker,
                                      @NonNull Int totalDmg, @NonNull Int totalDra, @NonNull Bool isCrit) {
                    if(isCrit.get() && Math.random() < 0.5) {
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
                    if(equipped.getOriginalId() == EquipList.MIX_SWORD_1.getId()) {
                        totalDmg.add(40);
                    }
                }
            });
        }});
        
        put(EquipList.OWLBEAR_LEATHER_SHOES.getId(), new HashMap<String, Event>() {{
            put(DamageEvent.getName(), new DamageEvent() {
                @Override
                public void onDamage(@NonNull Entity self, @NonNull Entity victim, @NonNull Int totalDmg,
                                     @NonNull Int totalDra, @NonNull Bool isCrit, boolean canCrit) {
                    long helmetId = self.getEquipped(EquipType.HELMET);
                    long chestplateId = self.getEquipped(EquipType.CHESTPLATE);
                    long leggingsId = self.getEquipped(EquipType.LEGGINGS);
                    
                    long noneId = EquipList.NONE.getId();
                    if(helmetId == noneId || chestplateId == noneId || leggingsId == noneId) {
                        return;
                    }
                    
                    Equipment helmet = Config.getData(Id.EQUIPMENT, helmetId);
                    Equipment chestplate = Config.getData(Id.EQUIPMENT, chestplateId);
                    Equipment leggings = Config.getData(Id.EQUIPMENT, leggingsId);
                    
                    if(helmet.getOriginalId() == EquipList.OWLBEAR_LEATHER_HELMET.getId() &&
                        chestplate.getOriginalId() == EquipList.OWLBEAR_LEATHER_CHESTPLATE.getId() &&
                        leggings.getOriginalId() == EquipList.OWLBEAR_LEATHER_LEGGINGS.getId()) {
                        totalDmg.multiple(2);
                    }
                }
            });
            
            put(DamagedEvent.getName(), new DamagedEvent() {
                @Override
                public void onDamaged(@NonNull Entity self, @NonNull Entity attacker, @NonNull Int totalDmg,
                                      @NonNull Int totalDra, @NonNull Bool isCrit) {
                    long helmetId = self.getEquipped(EquipType.HELMET);
                    long chestplateId = self.getEquipped(EquipType.CHESTPLATE);
                    long leggingsId = self.getEquipped(EquipType.LEGGINGS);
                    
                    long noneId = EquipList.NONE.getId();
                    if(helmetId == noneId || chestplateId == noneId || leggingsId == noneId) {
                        return;
                    }
                    
                    Equipment helmet = Config.getData(Id.EQUIPMENT, helmetId);
                    Equipment chestplate = Config.getData(Id.EQUIPMENT, chestplateId);
                    Equipment leggings = Config.getData(Id.EQUIPMENT, leggingsId);
                    
                    if(helmet.getOriginalId() == EquipList.OWLBEAR_LEATHER_HELMET.getId() &&
                        chestplate.getOriginalId() == EquipList.OWLBEAR_LEATHER_CHESTPLATE.getId() &&
                        leggings.getOriginalId() == EquipList.OWLBEAR_LEATHER_LEGGINGS.getId()) {
                        totalDmg.divide(2);
                    }
                }
            });
        }});
        
        put(EquipList.HARDENED_SLIME_HELMET.getId(), new HashMap<String, Event>() {{
            put(DamagedEvent.getName(), new DamagedEvent() {
                @Override
                public void onDamaged(@NonNull Entity self, @NonNull Entity attacker,
                                      @NonNull Int totalDmg, @NonNull Int totalDra, @NonNull Bool isCrit) {
                    double coe;
                    if(isCrit.get()) {
                        coe = 0.25;
                    } else {
                        coe = 0.15;
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
                        coe = 0.55;
                    } else {
                        coe = 0.3;
                    }
                    
                    int saved = (int) (totalDmg.get() * coe);
                    self.setVariable(Variable.HARDENED_SLIME_CHESTPLATE, saved);
                }
            });
            
            put(DamageEvent.getName(), new DamageEvent() {
                @Override
                public void onDamage(@NonNull Entity self, @NonNull Entity victim, @NonNull Int totalDmg,
                                     @NonNull Int totalDra, @NonNull Bool isCrit, boolean canCrit) {
                    boolean used = self.getObjectVariable(Variable.HARDENED_SLIME_CHESTPLATE_USE, false);
                    
                    if(used) {
                        int saved = self.getVariable(Variable.HARDENED_SLIME_CHESTPLATE);
                        totalDmg.add(saved);
                        
                        self.removeVariable(Variable.HARDENED_SLIME_CHESTPLATE);
                        self.removeVariable(Variable.HARDENED_SLIME_CHESTPLATE_USE);
                    }
                }
            });
            
            put(EndFightEvent.getName(), new EndFightEvent() {
                @Override
                public void onEndFight(@NonNull Entity self) {
                    self.removeVariable(Variable.HARDENED_SLIME_CHESTPLATE);
                    self.removeVariable(Variable.HARDENED_SLIME_CHESTPLATE_USE);
                }
            });
        }});
        
        put(EquipList.ELEMENT_HEART_GEM.getId(), new HashMap<String, Event>() {{
            put(DamagedEvent.getName(), new DamagedEvent() {
                @Override
                public void onDamaged(@NonNull Entity self, @NonNull Entity attacker, @NonNull Int totalDmg,
                                      @NonNull Int totalDra, @NonNull Bool isCrit) {
                    Random random = new Random();
                    double randomValue = random.nextDouble();
                    
                    if(randomValue < 0.25) {
                        if(random.nextDouble() < 0.3) {
                            totalDmg.set(0);
                        }
                    } else if(randomValue < 0.5) {
                        totalDmg.multiple(0.3);
                    } else if(randomValue < 0.75) {
                        int ats = (int) (attacker.getStat(StatType.ATS) * 0.3);
                        attacker.addBasicStat(StatType.ATS, -1 * ats);
                        
                        attacker.setVariable(Variable.ELEMENT_HEART_GEM_ATS, ats);
                        attacker.addEvent(EventList.ELEMENT_HEART_GEM_ATS_TURN);
                        attacker.addEvent(EventList.ELEMENT_HEART_GEM_ATS_END);
                    } else {
                        int damage = (int) (attacker.getStat(StatType.MATK) * 0.15);
                        
                        attacker.setVariable(Variable.ELEMENT_HEART_GEM_FIRE, 3);
                        attacker.setVariable(Variable.ELEMENT_HEART_GEM_FIRE_DAMAGE, damage);
                        attacker.setVariable(Variable.ELEMENT_HEART_GEM_FIRE_ENTITY, self);
                        
                        attacker.addEvent(EventList.ELEMENT_HEART_GEM_FIRE_TURN);
                        attacker.addEvent(EventList.ELEMENT_HEART_GEM_FIRE_END);
                    }
                }
            });
        }});
        
        put(EquipList.GOLEM_HEART_GEM.getId(), new HashMap<String, Event>() {{
            put(StartFightEvent.getName(), new StartFightEvent() {
                @Override
                public void onStartFight(@NonNull Entity self, @NonNull Entity enemy, boolean isOwner) {
                    int shield = (int) (self.getStat(StatType.MAXHP) * 0.66);
                    
                    self.setVariable(Variable.GOLEM_HEART_GEM_PHYSIC_SHIELD, shield);
                    self.setVariable(Variable.GOLEM_HEART_GEM_MAGIC_SHIELD, shield);
                }
            });
            
            put(InjectFightEvent.getName(), new InjectFightEvent() {
                @Override
                public void onInjectFight(@NonNull Entity self, @NonNull Entity enemy) {
                    int shield = (int) (self.getStat(StatType.MAXHP) * 0.8);
                    
                    self.setVariable(Variable.GOLEM_HEART_GEM_PHYSIC_SHIELD, shield);
                    self.setVariable(Variable.GOLEM_HEART_GEM_MAGIC_SHIELD, shield);
                }
            });
            
            put(PreDamagedEvent.getName(), new PreDamagedEvent() {
                @Override
                public void onPreDamaged(@NonNull Entity self, @NonNull Entity attacker, @NonNull Int physicDmg,
                                         @NonNull Int magicDmg, @NonNull Int staticDmg, boolean canCrit) {
                    int physicShield = self.getVariable(Variable.GOLEM_HEART_GEM_PHYSIC_SHIELD);
                    int magicShield = self.getVariable(Variable.GOLEM_HEART_GEM_MAGIC_SHIELD);
                    
                    if(physicShield >= physicDmg.get()) {
                        physicShield -= physicDmg.get() - 1;
                        physicDmg.set(1);
                        
                        self.setVariable(Variable.GOLEM_HEART_GEM_PHYSIC_SHIELD, physicShield);
                    } else {
                        physicDmg.add(-1 * physicShield);
                        self.removeVariable(Variable.GOLEM_HEART_GEM_PHYSIC_SHIELD);
                    }
                    
                    if(magicShield >= magicDmg.get()) {
                        magicShield -= magicDmg.get() - 1;
                        magicDmg.set(1);
                        
                        self.setVariable(Variable.GOLEM_HEART_GEM_MAGIC_SHIELD, magicShield);
                    } else {
                        magicDmg.add(-1 * physicShield);
                        self.removeVariable(Variable.GOLEM_HEART_GEM_MAGIC_SHIELD);
                    }
                }
            });
        }});
        
        put(EquipList.REGENERATION_AMULET.getId(), new HashMap<String, Event>() {{
            put(TurnEvent.getName(), new TurnEvent() {
                @Override
                public void onTurn(@NonNull Entity self, @NonNull WrappedObject<Entity> attacker) {
                    self.addBasicStat(StatType.HP, (int) (self.getStat(StatType.MAXHP) * 0.03));
                }
            });
        }});
        
        put(EquipList.NATURE_AMULET.getId(), new HashMap<String, Event>() {{
            put(TurnEvent.getName(), new TurnEvent() {
                @Override
                public void onTurn(@NonNull Entity self, @NonNull WrappedObject<Entity> attacker) {
                    self.addBasicStat(StatType.HP, (int) (self.getStat(StatType.MAXHP) * 0.055));
                }
            });
        }});
        
        put(EquipList.SILVER_RING.getId(), new HashMap<String, Event>() {{
            put(PreDamagedEvent.getName(), new PreDamagedEvent() {
                @Override
                public void onPreDamaged(@NonNull Entity self, @NonNull Entity attacker, @NonNull Int physicDmg,
                                         @NonNull Int magicDmg, @NonNull Int staticDmg, boolean canCrit) {
                    if(Math.random() < 0.2) {
                        int reflect = (int) (magicDmg.get() * 0.2);
                        
                        magicDmg.add(-1 * reflect);
                        self.damage(attacker, 0, reflect, 0, true, false, false);
                        
                        if(reflect > 0 && self.getId().getId().equals(Id.PLAYER)) {
                            ((Player) self).replyPlayer("마법 피해의 20% 인 " + reflect + " 데미지를 반사했습니다");
                        }
                    }
                }
            });
        }});
        
        put(EquipList.GARNET_EARRING.getId(), new HashMap<String, Event>() {{
            put(PreDamageEvent.getName(), new PreDamageEvent() {
                @Override
                public void onPreDamage(@NonNull Entity self, @NonNull Entity victim, @NonNull Int physicDmg,
                                        @NonNull Int magicDmg, @NonNull Int staticDmg, boolean canCrit) {
                    int changeDamage = (int) (physicDmg.get() * 0.07);
                    physicDmg.add(-1 * changeDamage);
                    staticDmg.add(changeDamage);
                    
                    changeDamage = (int) (magicDmg.get() * 0.07);
                    magicDmg.add(-1 * changeDamage);
                    staticDmg.add(changeDamage);
                }
            });
        }});
        
        put(EquipList.AMETHYST_EARRING.getId(), new HashMap<String, Event>() {{
            put(HarvestEvent.getName(), new HarvestEvent() {
                @Override
                public void onHarvest(@NonNull Entity self, @NonNull LoNg growTime) {
                    growTime.multiple(0.9);
                }
            });
        }});
        
        put(EquipList.DIAMOND_EARRING.getId(), new HashMap<String, Event>() {{
            put(DamageEvent.getName(), new DamageEvent() {
                @Override
                public void onDamage(@NonNull Entity self, @NonNull Entity victim, @NonNull Int totalDmg,
                                     @NonNull Int totalDra, @NonNull Bool isCrit, boolean canCrit) {
                    totalDra.multiple(1.1);
                }
            });
        }});
        
        put(EquipList.PEARL_EARRING.getId(), new HashMap<String, Event>() {{
            put(StartFightEvent.getName(), new StartFightEvent() {
                @Override
                public void onStartFight(@NonNull Entity self, @NonNull Entity enemy, boolean isOwner) {
                    if(self.getStat(StatType.HP) == self.getStat(StatType.MAXHP)) {
                        int ats = (int) (self.getStat(StatType.ATS) * 0.08);
                        
                        self.addBasicStat(StatType.ATS, ats);
                        self.setVariable(Variable.PEARL_EARRING_ATS, ats);
                    }
                }
            });
            
            put(InjectFightEvent.getName(), new InjectFightEvent() {
                @Override
                public void onInjectFight(@NonNull Entity self, @NonNull Entity enemy) {
                    if(self.getStat(StatType.HP) == self.getStat(StatType.MAXHP)) {
                        int ats = (int) (self.getStat(StatType.ATS) * 0.08);
                        
                        self.addBasicStat(StatType.ATS, ats);
                        self.setVariable(Variable.PEARL_EARRING_ATS, ats);
                    }
                }
            });
            
            put(EndFightEvent.getName(), new EndFightEvent() {
                @Override
                public void onEndFight(@NonNull Entity self) {
                    int ats = self.getVariable(Variable.PEARL_EARRING_ATS);
                    self.addBasicStat(StatType.ATS, -1 * ats);
                }
            });
        }});
        
        put(EquipList.PERIDOT_EARRING.getId(), new HashMap<String, Event>() {{
            put(DamagedEvent.getName(), new DamagedEvent() {
                @Override
                public void onDamaged(@NonNull Entity self, @NonNull Entity attacker, @NonNull Int totalDmg,
                                      @NonNull Int totalDra, @NonNull Bool isCrit) {
                    long fightId = FightManager.getInstance().getFightId(self.getId());
                    Map<Integer, Entity> targetMap = FightManager.getInstance().getTargetMap(fightId);
                    
                    if(targetMap.size() == 1) {
                        totalDmg.multiple(0.08);
                    }
                }
            });
        }});
        
        put(EquipList.SAPPHIRE_EARRING.getId(), new HashMap<String, Event>() {{
            put(MineEvent.getName(), new MineEvent() {
                @Override
                public void onMine(@NonNull Entity self, @NonNull Item item, @NonNull Int itemCount) {
                    if(Math.random() < 0.1) {
                        itemCount.add(1);
                    }
                }
            });
            
            put(FishEvent.getName(), new FishEvent() {
                @Override
                public void onFish(@NonNull Entity self, @NonNull Item item, @NonNull Int itemCount) {
                    if(Math.random() < 0.1) {
                        itemCount.add(1);
                    }
                }
            });
        }});
        
        put(EquipList.OPAL_EARRING.getId(), new HashMap<String, Event>() {{
            put(DamageEvent.getName(), new DamageEvent() {
                @Override
                public void onDamage(@NonNull Entity self, @NonNull Entity victim, @NonNull Int totalDmg,
                                     @NonNull Int totalDra, @NonNull Bool isCrit, boolean canCrit) {
                    int selfHp = self.getStat(StatType.HP);
                    int victimHp = victim.getStat(StatType.HP);
                    
                    if(selfHp < victimHp) {
                        double multiplier = Math.min(0.75, (victimHp / (double) selfHp - 1) * 1.5) + 1;
                        totalDmg.multiple(multiplier);
                    }
                }
            });
        }});
        
        put(EquipList.TOPAZ_EARRING.getId(), new HashMap<String, Event>() {{
            put(TurnEvent.getName(), new TurnEvent() {
                @Override
                public void onTurn(@NonNull Entity self, @NonNull WrappedObject<Entity> attacker) {
                    int maxHp = self.getStat(StatType.MAXHP);
                    int hp = self.getStat(StatType.HP);
                    
                    double lostHpPercent = (maxHp - hp) / (double) maxHp;
                    int heal = (int) ((lostHpPercent / 0.02) * (maxHp * 0.0018));
                    
                    self.addBasicStat(StatType.HP, heal);
                }
            });
        }});
        
        put(EquipList.TURQUOISE_EARRING.getId(), new HashMap<String, Event>() {{
            put(AddExpEvent.getName(), new AddExpEvent() {
                @Override
                public void onAddExp(@NonNull Entity self, @NonNull LoNg exp) {
                    if(Doing.fightList().contains(self.getDoing())) {
                        exp.multiple(1.08);
                    }
                }
            });
        }});
        
        put(EquipList.MOON_SWORD.getId(), new HashMap<String, Event>() {{
            put(DamageEvent.getName(), new DamageEvent() {
                @Override
                public void onDamage(@NonNull Entity self, @NonNull Entity victim, @NonNull Int totalDmg,
                                     @NonNull Int totalDra, @NonNull Bool isCrit, boolean canCrit) {
                    double percent = ((double) self.getStat(StatType.HP)) / self.getStat(StatType.MAXHP);
                    double increasePercent = Math.min(2 - percent, 1.35);
                    
                    long gemId = self.getEquipped(EquipType.GEM);
                    
                    if(gemId != EquipList.NONE.getId()) {
                        Equipment gem = Config.getData(Id.EQUIPMENT, gemId);
                        
                        if(gem.getOriginalId() == EquipList.MOON_GEM.getId()) {
                            increasePercent *= 2;
                        }
                    }
                    
                    totalDmg.multiple(increasePercent);
                }
            });
        }});
        
        put(EquipList.LYCANTHROPE_HELMET.getId(), new HashMap<String, Event>() {{
            put(DamagedEvent.getName(), new DamagedEvent() {
                @Override
                public void onDamaged(@NonNull Entity self, @NonNull Entity attacker,
                                      @NonNull Int totalDmg, @NonNull Int totalDra, @NonNull Bool isCrit) {
                    if(isCrit.get()) {
                        double percent = ((double) self.getStat(StatType.HP)) / self.getStat(StatType.MAXHP) - 0.5;
                        totalDmg.divide(percent);
                    }
                }
            });
        }});
        
        put(EquipList.LYCANTHROPE_LEGGINGS.getId(), new HashMap<String, Event>() {{
            put(DamageEvent.getName(), new DamageEvent() {
                @Override
                public void onDamage(@NonNull Entity self, @NonNull Entity victim, @NonNull Int totalDmg,
                                     @NonNull Int totalDra, @NonNull Bool isCrit, boolean canCrit) {
                    totalDmg.multiple(1.1);
                }
            });
        }});
        
        put(EquipList.LYCANTHROPE_SHOES.getId(), new HashMap<String, Event>() {{
            put(PreEvadeEvent.getName(), new PreEvadeEvent() {
                
                @Override
                public void onPreEvade(@NonNull Entity self, @NonNull Entity attacker, @NonNull Int physicDmg,
                                       @NonNull Int magicDmg, @NonNull Int staticDmg,
                                       @NonNull Bool evade, @NonNull Bool canCrit) {
                    if(evade.get()) {
                        self.addBasicStat(StatType.HP, self.getStat(StatType.MAXHP) / 10);
                    }
                }
            });
        }});
        
        put(EquipList.LYCANTHROPE_TOOTH_NECKLACE.getId(), new HashMap<String, Event>() {{
            put(EndFightEvent.getName(), new EndFightEvent() {
                @Override
                public void onEndFight(@NonNull Entity self) {
                    if(self.getKiller() != null) {
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
                        
                        if(helmet.getOriginalId() == EquipList.LYCANTHROPE_HELMET.getId() &&
                            chestplate.getOriginalId() == EquipList.LYCANTHROPE_CHESTPLATE.getId() &&
                            leggings.getOriginalId() == EquipList.LYCANTHROPE_LEGGINGS.getId() &&
                            shoes.getOriginalId() == EquipList.LYCANTHROPE_SHOES.getId()) {
                            double increased = self.getObjectVariable(Variable.LYCANTHROPE_TOOTH_NECKLACE_INCREASE, 0);
                            
                            if(increased < 30) {
                                increased += 0.1;
                                
                                if(increased % 1 == 0) {
                                    self.addBasicStat(StatType.ATK, 1);
                                    self.addBasicStat(StatType.MATK, 1);
                                    
                                    self.setVariable(Variable.LYCANTHROPE_TOOTH_NECKLACE_INCREASE, increased);
                                }
                            }
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
