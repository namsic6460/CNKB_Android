package lkd.namsic.game.object.implement;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import lkd.namsic.game.base.IdClass;
import lkd.namsic.game.base.Location;
import lkd.namsic.game.base.SkillUse;
import lkd.namsic.game.config.Config;
import lkd.namsic.game.config.Emoji;
import lkd.namsic.game.enums.Doing;
import lkd.namsic.game.enums.EquipType;
import lkd.namsic.game.enums.Id;
import lkd.namsic.game.enums.StatType;
import lkd.namsic.game.enums.Variable;
import lkd.namsic.game.enums.object.EquipList;
import lkd.namsic.game.enums.object.EventList;
import lkd.namsic.game.enums.object.MonsterList;
import lkd.namsic.game.enums.object.SkillList;
import lkd.namsic.game.exception.NumberRangeException;
import lkd.namsic.game.exception.WeirdCommandException;
import lkd.namsic.game.manager.FightManager;
import lkd.namsic.game.manager.MoveManager;
import lkd.namsic.game.object.Entity;
import lkd.namsic.game.object.Equipment;
import lkd.namsic.game.object.GameMap;
import lkd.namsic.game.object.Monster;
import lkd.namsic.game.object.Player;

public class SkillUses {
    
    public final static Map<Long, SkillUse> MAP = new HashMap<Long, SkillUse>() {{
        put(SkillList.MAGIC_BALL.getId(), new SkillUse(0, 1) {
            @Override
            public void useSkill(@NonNull Entity self, @NonNull List<Entity> targets) {
                Entity target = Objects.requireNonNull(targets.get(0));
                self.damage(target, 0, self.getStat(StatType.MATK),
                    5, false, false, true);
            }
        });
        
        put(SkillList.SMITE.getId(), new SkillUse(0, 5) {
            @Override
            public void useSkill(@NonNull Entity self, @NonNull List<Entity> targets) {
                Entity target = Objects.requireNonNull(targets.get(0));
                
                int atk = self.getStat(StatType.ATK);
                int matk = self.getStat(StatType.MATK);
                self.damage(target, atk * 0.5, matk * 0.5, (atk + matk) * 0.1,
                    true, true, true);
            }
        });
        
        put(SkillList.LASER.getId(), new SkillUse(0, 10) {
            @Override
            public int getWaitTurn() {
                return 1;
            }
            
            @Override
            public void useSkill(@NonNull Entity self, @NonNull List<Entity> targets) {
                Entity target = Objects.requireNonNull(targets.get(0));
                
                self.damage(target, 0, self.getStat(StatType.MATK) * 2.25, 0,
                    true, true, true);
            }
        });
        
        put(SkillList.SCAR.getId(), new SkillUse(0, 5) {
            @Override
            public int getMaxTargetCount() {
                return 3;
            }
            
            @Override
            public void useSkill(@NonNull Entity self, @NonNull List<Entity> targets) {
                int atk = self.getStat(StatType.ATK);
                int physicDmg = atk / 10;
                
                Player player;
                int damage, drain, heal;
                boolean isCrit = false;
                
                int totalDmg = 0;
                int totalDra = 0;
                int critCount = 0;
                
                int attackCount = 3;
                
                long weaponId = self.getEquipped(EquipType.WEAPON);
                if(weaponId != EquipList.NONE.getId()) {
                    Equipment equipment = Config.getData(Id.EQUIPMENT, weaponId);
                    if(equipment.getOriginalId() == EquipList.HARPY_NAIL_GAUNTLETS.getId()) {
                        attackCount = 5;
                    }
                }
                
                for(Entity target : targets) {
                    player = target.getId().getId().equals(Id.PLAYER) ? (Player) target : null;
                    damage = 0;
                    drain = 0;
                    heal = 0;
                    
                    if(player != null) {
                        player.setPrintDeathMsg(false);
                    }
                    
                    for(int i = 0; i < attackCount; i++) {
                        self.damage(target, physicDmg, 0, 0, true, true, false);
                        
                        damage += self.getLastDamage();
                        drain += self.getLastDrain();
                        heal += target.getLastHeal();
                        
                        if(self.isLastCrit()) {
                            isCrit = true;
                            critCount++;
                        }
                        
                        if(target.getKiller() != null) {
                            break;
                        }
                    }
                    
                    totalDmg += damage;
                    totalDra += drain;
                    
                    if(player != null) {
                        player.printDamagedMsg(self, damage, drain, isCrit, heal);
                        
                        if(player.getKiller() != null) {
                            player.replyPlayer(Player.DEATH_MSG, player.getDeathMsg());
                        }
                    }
                    
                    if(isCrit) {
                        int bloodDamage = (int) Math.max(atk * 0.2, self.getVariable(Variable.SCAR_BLOOD_DAMAGE));
                        
                        long necklaceId = self.getEquipped(EquipType.NECKLACE);
                        if(necklaceId != EquipList.NONE.getId()) {
                            Equipment necklace = Config.getData(Id.EQUIPMENT, necklaceId);
                            if(necklace.getOriginalId() == EquipList.HARPY_NAIL_NECKLACE.getId()) {
                                bloodDamage *= 1.2;
                            }
                        }
                        
                        target.setVariable(Variable.SCAR_ENTITY, self);
                        target.setVariable(Variable.SCAR_BLOOD, 3);
                        target.setVariable(Variable.SCAR_BLOOD_DAMAGE, bloodDamage);
                        
                        target.addEvent(EventList.SCAR_TURN);
                        target.addEvent(EventList.SCAR_END);
                    }
                }
                
                if(self.getId().getId().equals(Id.PLAYER)) {
                    ((Player) self).printDamageMsg(targets, totalDmg, totalDra, critCount);
                }
            }
        });
        
        put(SkillList.CHARM.getId(), new SkillUse(0, 15) {
            @Override
            public void useSkill(@NonNull Entity self, @NonNull List<Entity> targets) {
                Entity target = Objects.requireNonNull(targets.get(0));
                
                if(Math.random() * 0.5 + (target.getStat(StatType.MDEF) - self.getStat(StatType.MATK)) < Math.random()) {
                    target.setVariable(Variable.CHARM_ENTITY, self);
                    
                    target.addEvent(EventList.CHARM_SELF_TURN);
                    target.addEvent(EventList.CHARM_END);
                } else {
                    self.setVariable(Variable.RESISTED_SKILL, true);
                    
                    if(target.getId().getId().equals(Id.PLAYER)) {
                        ((Player) target).replyPlayer("스킬 저항에 성공했습니다");
                    }
                }
            }
        });
        
        put(SkillList.RESIST.getId(), new SkillUse(0, 10) {
            @Override
            public int getMinTargetCount() {
                return 0;
            }
            
            @Override
            public int getMaxTargetCount() {
                return 0;
            }
            
            @Override
            public void checkUse(@NonNull Entity self, @Nullable String other) {
                super.checkUse(self, other);
                
                if(self.getVariable(Variable.RESIST) != 0) {
                    throw new WeirdCommandException(SkillList.RESIST.getDisplayName() + " 의 효과가 유지되는 동안 재사용이 불가능합니다");
                }
            }
            
            @Override
            public void useSkill(@NonNull Entity self, @NonNull List<Entity> targets) {
                self.setVariable(Variable.RESIST, 3);
                self.addEvent(EventList.RESIST_PRE_DAMAGED);
                self.addEvent(EventList.RESIST_TURN);
                self.addEvent(EventList.RESIST_END);
            }
        });
        
        put(SkillList.RUSH.getId(), new SkillUse(0, 5) {
            @Nullable
            @Override
            public String checkOther(@NonNull Entity self, @NonNull String... other) {
                String result = super.checkOther(self, other);
                if(result != null) {
                    return result;
                }
                
                int targetIndex = Integer.parseInt(other[0]);
                long fightId = FightManager.getInstance().getFightId(self.getId());
                Map<Integer, Entity> targetMap = FightManager.getInstance().getTargetMap(fightId);
                
                Entity target = Objects.requireNonNull(targetMap.get(targetIndex));
                if(self.getFieldDistance(target.getLocation()) < 2) {
                    return SkillList.RUSH.getDisplayName() + " 을 사용하기에 적과의 거리가 너무 가깝습니다";
                }
                
                return null;
            }
            
            @Override
            public void useSkill(@NonNull Entity self, @NonNull List<Entity> targets) {
                Entity target = Objects.requireNonNull(targets.get(0));
                Location location = target.getLocation();
                
                int distance = Math.min(self.getFieldDistance(location), 20);
                
                int physicDmg = (int) (0.08 * distance * self.getStat(StatType.ATK));
                self.damage(target, physicDmg, 0, 0, true, false, true);
                
                MoveManager.getInstance().setField(self, location.getFieldX(), location.getFieldY());
            }
        });
        
        put(SkillList.ROAR.getId(), new SkillUse(0, 2) {
            @Override
            public int getMinTargetCount() {
                return 0;
            }
            
            @Override
            public int getMaxTargetCount() {
                return 0;
            }
            
            @Override
            public void useSkill(@NonNull Entity self, @NonNull List<Entity> targets) {
                long fightId = FightManager.getInstance().getFightId(self.getId());
                Set<Entity> entitySet = FightManager.getInstance().getEntitySet(fightId);
                
                int levelGap;
                int stat;
                for(Entity entity : entitySet) {
                    if(entity.equals(self)) {
                        continue;
                    }
                    
                    levelGap = Math.min(self.getLv() - entity.getLv(), 30);
                    if(levelGap <= 0) {
                        continue;
                    }
                    
                    stat = (int) (levelGap / 150D * entity.getStat(StatType.ATS));
                    entity.addBuff(System.currentTimeMillis() + 30000, StatType.ATS, -1 * stat);
                    
                    if(entity.getId().getId().equals(Id.PLAYER)) {
                        ((Player) entity).replyPlayer(self.getFightName() + " (이/가) 사용한  스킬 " +
                            Emoji.focus(SkillList.ROAR.getDisplayName()) + " 로 인해 공격속도가 " + stat + " 만큼 낮아졌습니다");
                    }
                }
            }
        });
        
        put(SkillList.HOWLING_OF_WOLF.getId(), new SkillUse(0, 30) {
            @Override
            public int getWaitTurn() {
                return 1;
            }
            
            @Override
            public int getMinTargetCount() {
                return 0;
            }
            
            @Override
            public int getMaxTargetCount() {
                return 0;
            }
            
            @Override
            public void useSkill(@NonNull Entity self, @NonNull List<Entity> targets) {
                long fightId = FightManager.getInstance().getFightId(self.getId());
                
                Location location = self.getLocation();
                GameMap map = Config.loadMap(location);
                
                Location spawnLocation = new Location(location);
                
                try {
                    spawnLocation.setField(spawnLocation.getFieldX() - 1, spawnLocation.getFieldY());
                } catch(NumberRangeException ignore) {
                }
                
                Monster originalMonster = Config.getData(Id.MONSTER, MonsterList.LYCANTHROPE.getId());
                
                Monster monster = Config.newObject(originalMonster, true);
                monster.randomLevel();
                monster.setLocation(spawnLocation);
                map.addEntity(monster);
                Config.unloadObject(monster);
                
                monster = Config.loadObject(Id.MONSTER, monster.getId().getObjectId());
                monster.setDoing(Doing.FIGHT);
                
                FightManager.getInstance().getEntitySet(fightId).add(monster);
                FightManager.getInstance().fightId.put(monster.getId(), fightId);
                
                spawnLocation = new Location(location);
                
                try {
                    spawnLocation.setField(spawnLocation.getFieldX() + 1, spawnLocation.getFieldY());
                } catch(NumberRangeException ignore) {
                }
                
                monster = Config.newObject(originalMonster, true);
                monster.randomLevel();
                monster.setLocation(spawnLocation);
                map.addEntity(monster);
                Config.unloadObject(monster);
                
                monster = Config.loadObject(Id.MONSTER, monster.getId().getObjectId());
                monster.setDoing(Doing.FIGHT);
                
                FightManager.getInstance().getEntitySet(fightId).add(monster);
                FightManager.getInstance().fightId.put(monster.getId(), fightId);
                
                Set<Player> playerSet = FightManager.getInstance().getPlayerSet(fightId);
                Player.replyPlayers(playerSet, MonsterList.LYCANTHROPE.getDisplayName() + " 두마리가 전투에 난입했습니다!");
                
                Config.unloadMap(map);
            }
        });
        
        put(SkillList.CUTTING_MOONLIGHT.getId(), new SkillUse(0, 8) {
            @Override
            public int getMinTargetCount() {
                return 0;
            }
            
            @Override
            public int getMaxTargetCount() {
                return 0;
            }
            
            @Override
            public void useSkill(@NonNull Entity self, @NonNull List<Entity> targets) {
                boolean damageSame = self.getObjectVariable(Variable.CUTTING_MOONLIGHT_DAMAGE_SAME, false);
                Id id = self.getId().getId();
                
                int physicDmg = (int) (self.getStat(StatType.ATK) * 0.9);
                int magicDmg = (int) (self.getStat(StatType.MATK) * 0.9);
                
                if(physicDmg >= magicDmg) {
                    magicDmg = 0;
                } else {
                    physicDmg = 0;
                }
                
                long fightId = FightManager.getInstance().getFightId(self.getId());
                Set<Entity> entitySet = new HashSet<>(FightManager.getInstance().getEntitySet(fightId));
                
                for(Entity entity : entitySet) {
                    if(!damageSame && entity.getId().getId().equals(id)) {
                        continue;
                    }
                    
                    self.damage(entity, physicDmg, magicDmg, 0, true, true, true);
                }
            }
        });
        
        put(SkillList.ESSENCE_DRAIN.getId(), new SkillUse(0, 10) {
            @Override
            public void checkUse(@NonNull Entity self, @Nullable String other) {
                super.checkUse(self, other);
    
                if(self.getStat(StatType.HP) == self.getStat(StatType.MAXHP)) {
                    throw new WeirdCommandException("현재 체력이 최대치입니다");
                }
            }
    
            @Override
            public void useSkill(@NonNull Entity self, @NonNull List<Entity> targets) {
                Entity target = Objects.requireNonNull(targets.get(0));
                
                self.damage(target, 0, self.getStat(StatType.MATK), 0, false, false, true);
                self.addBasicStat(StatType.HP, self.getLastDamage());
            }
        });
    
        put(SkillList.MAGIC_DRAIN.getId(), new SkillUse(0, 0) {
            @Override
            public void checkUse(@NonNull Entity self, @Nullable String other) {
                super.checkUse(self, other);
            
                if(self.getStat(StatType.MN) == self.getStat(StatType.MAXMN)) {
                    throw new WeirdCommandException("현재 마나가 최대치입니다");
                }
            }
        
            @Override
            public void useSkill(@NonNull Entity self, @NonNull List<Entity> targets) {
                Entity target = Objects.requireNonNull(targets.get(0));
            
                int targetMn = target.getStat(StatType.MN);
                int removeMn = target.getStat(StatType.MAXMN) / 10;
                if(targetMn < removeMn) {
                    removeMn = targetMn;
                }
                
                target.addBasicStat(StatType.MN, removeMn);
                self.addBasicStat(StatType.MN, self.getStat(StatType.MAXMN) / 10);
            }
        });
        
        put(SkillList.MANA_EXPLOSION.getId(), new SkillUse(0, 50) {
            @Override
            public int getMinTargetCount() {
                return 0;
            }
    
            @Override
            public int getMaxTargetCount() {
                return 0;
            }
    
            @Override
            public void checkUse(@NonNull Entity self, @Nullable String other) {
                super.checkUse(self, other);
    
                if(self.getDistantEntity(16).isEmpty()) {
                    throw new WeirdCommandException("폭발 반경 내에 대상이 존재하지 않습니다");
                }
            }
    
            @Override
            public int getWaitTurn() {
                return 3;
            }
    
            @Override
            public void useSkill(@NonNull Entity self, @NonNull List<Entity> targets) {
                int magicDamage = self.getStat(StatType.MATK) * 5;
                
                int totalDmg = 0;
                int totalDra = 0;
                int critCount = 0;
                
                for(Set<IdClass> idSet : self.getDistantEntity(16).values()) {
                    for(IdClass id : idSet) {
                        Entity target = Config.getData(id.getId(), id.getObjectId());
                        Player player = id.getId().equals(Id.PLAYER) ? (Player) target : null;
    
                        if(player != null) {
                            player.setPrintDeathMsg(false);
                        }
                        
                        self.damage(target, 0, magicDamage, 0, false, true, false);
                        
                        int damage = self.getLastDamage();
                        int drain = self.getLastDrain();
                        boolean isCrit = self.isLastCrit();
                        
                        totalDmg += damage;
                        totalDra += drain;
                        if(isCrit) {
                            critCount++;
                        }
                        
                        if(player != null) {
                            player.printDamagedMsg(self, damage, drain, isCrit, target.getLastHeal());
        
                            if(player.getKiller() != null) {
                                player.replyPlayer(Player.DEATH_MSG, player.getDeathMsg());
                            }
                        }
                    }
                }
    
                if(self.getId().getId().equals(Id.PLAYER)) {
                    ((Player) self).printDamageMsg(targets, totalDmg, totalDra, critCount);
                }
            }
        });
    }};
    
}
