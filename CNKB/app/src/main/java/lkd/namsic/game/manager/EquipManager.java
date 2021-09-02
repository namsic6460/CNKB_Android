package lkd.namsic.game.manager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import lkd.namsic.game.base.ConcurrentHashSet;
import lkd.namsic.game.base.EquipUse;
import lkd.namsic.game.config.Config;
import lkd.namsic.game.config.Emoji;
import lkd.namsic.game.enums.EquipType;
import lkd.namsic.game.enums.Id;
import lkd.namsic.game.enums.LogData;
import lkd.namsic.game.enums.StatType;
import lkd.namsic.game.enums.object.EquipList;
import lkd.namsic.game.enums.object.ItemList;
import lkd.namsic.game.exception.UnhandledEnumException;
import lkd.namsic.game.exception.WeirdCommandException;
import lkd.namsic.game.object.Entity;
import lkd.namsic.game.object.Equipment;
import lkd.namsic.game.object.Player;

public class EquipManager {

    private static final EquipManager instance = new EquipManager();

    public static EquipManager getInstance() {
        return instance;
    }

    public boolean canEquip(@NonNull Player self, long equipId) {
        Equipment equipment = Config.getData(Id.EQUIPMENT, equipId);
        return equipment.getTotalLimitLv() <= self.getLv() && self.compareStat(equipment.getLimitStat());
    }

    public void equip(@NonNull Entity self, int equipIndex) {
        long equipId = self.getEquipIdByIndex(equipIndex);
        Equipment equipment = Config.getData(Id.EQUIPMENT, equipId);
        EquipType equipType = equipment.getEquipType();

        long equippedId = self.getEquipped(equipType);

        if (self.getId().getId().equals(Id.PLAYER)) {
            Player player = (Player) self;

            if (!this.canEquip(player, equipId)) {
                StringBuilder innerBuilder = new StringBuilder("장착 제한 레벨: ")
                        .append(equipment.getTotalLimitLv())
                        .append("\n\n")
                        .append("---장착 제한 스텟---");

                if (equipment.getLimitStat().isEmpty()) {
                    innerBuilder.append("\n장착 제한 스텟이 없습니다");
                } else {
                    for (Map.Entry<StatType, Integer> entry : equipment.getLimitStat().entrySet()) {
                        innerBuilder.append(Emoji.LIST)
                                .append(entry.getKey().getDisplayName())
                                .append(": ")
                                .append(entry.getValue());
                    }
                }

                player.replyPlayer("장비를 장착할 수 없습니다", innerBuilder.toString());
                return;
            }
        }

        if (equippedId == equipId) {
            this.unEquip(self, equipType);
        } else {
            this.equip(self, equipId);
        }
    }

    public void equip(@NonNull Entity self, long equipId) {
        Equipment equipment = Config.getData(Id.EQUIPMENT, equipId);
        EquipType equipType = equipment.getEquipType();
        long equippedId = self.getEquipped(equipType);

        if (equippedId != EquipList.NONE.getId()) {
            this.unEquip(self, equipType);
        }

        StringBuilder innerBuilder = new StringBuilder("---스텟 현황---");

        int stat;
        for (StatType statType : StatType.values()) {
            try {
                Config.checkStatType(statType);
            } catch (UnhandledEnumException e) {
                continue;
            }

            stat = equipment.getStat(statType);

            if (stat != 0) {
                self.addEquipStat(statType, stat);
                innerBuilder.append("\n")
                        .append(statType.getDisplayName())
                        .append(": ")
                        .append(Config.getIncrease(stat));
            }
        }

        self.getEquipped().put(equipType, equipId);
        self.getRemovedEquipEvent().put(equipType, new ConcurrentHashSet<>());

        if (self.getId().getId().equals(Id.PLAYER)) {
            ((Player) self).replyPlayer(equipment.getName() + " (을/를) 착용했습니다", innerBuilder.toString());
        }
    }

    public void unEquip(@NonNull Entity self, @NonNull EquipType equipType) {
        long equipId = self.getEquipped(equipType);
        if (equipId == EquipList.NONE.getId()) {
            throw new WeirdCommandException("해당 유형의 장비는 착용하고 있지 않습니다");
        }

        StringBuilder innerBuilder = new StringBuilder("---스텟 현황---");

        Equipment equipment = Config.getData(Id.EQUIPMENT, equipId);

        int stat;
        for (StatType statType : StatType.values()) {
            try {
                Config.checkStatType(statType);
            } catch (UnhandledEnumException e) {
                continue;
            }

            stat = equipment.getStat(statType) * -1;

            if (stat != 0) {
                self.addEquipStat(statType, stat);
                innerBuilder.append("\n")
                        .append(statType.getDisplayName())
                        .append(": ")
                        .append(Config.getIncrease(stat));
            }
        }

        self.getEquipped().remove(equipType);
        self.getRemovedEquipEvent().remove(equipType);

        if (self.getId().getId().equals(Id.PLAYER)) {
            ((Player) self).replyPlayer(equipment.getName() + "(을/를) 착용 해제했습니다", innerBuilder.toString());
        }
    }

    public long checkUse(@NonNull Player self, @NonNull EquipType equipType, @Nullable String other) {
        long equipId = self.getEquipped(equipType);

        if (equipId == EquipList.NONE.getId()) {
            throw new WeirdCommandException("해당 부위는 장비를 착용하고 있지 않습니다");
        }

        Equipment equipment = Config.getData(Id.EQUIPMENT, equipId);
        EquipUse equipUse = equipment.getEquipUse();
        if (equipUse == null) {
            throw new WeirdCommandException("해당 부위의 장비는 사용이 불가능합니다");
        }

        equipUse.checkUse(self, other);
        return equipment.getOriginalId();
    }

    public void tryUse(@NonNull Player self, @NonNull EquipType equipType, @Nullable String other) {
        long equipId = checkUse(self, equipType, other);
        this.use(self, equipId, other);
    }

    public void use(@NonNull Entity self, long equipId, @Nullable String other) {
        Equipment equipment = Config.getData(Id.EQUIPMENT, equipId);

        EquipUse use = Objects.requireNonNull(equipment.getEquipUse());
        String result = use.tryUse(self, other);

        if (self.getId().getId().equals(Id.PLAYER)) {
            Player player = (Player) self;

            player.addLog(LogData.TOTAL_EQUIP_USE, 1);

            String msg = Emoji.focus(equipment.getRealName()) + " 을 사용했습니다\n";
            player.replyPlayer(msg + result);
        }
    }

    public void decompose(@NonNull Player self, int equipIndex, @NonNull String equipName) {
        long equipId = self.getEquipIdByIndex(equipIndex);
        Equipment equipment = Config.getData(Id.EQUIPMENT, equipId);

        if(!equipment.getRealName().equals(equipName)) {
            throw new WeirdCommandException("정확한 장비 이름을 입력하세요(강화 제외)(실수 방지)\n" +
                    "(예시: " + Emoji.focus("ㅜ 장비 분해 2 목검"));
        }

        if(self.getEquipped(equipment.getEquipType()) != EquipList.NONE.getId()) {
            throw new WeirdCommandException("장착중인 장비는 분해할 수 없습니다");
        }
        
        if(equipment.getReinforceCount() < 5) {
            throw new WeirdCommandException("5강 미만의 장비는 분해할 수 없습니다");
        }

        StringBuilder innerBuilder = new StringBuilder("골드: ");

        long money = 0;
        for(int reinforceCount = 0; reinforceCount < equipment.getReinforceCount(); reinforceCount++) {
            money += equipment.getReinforceCost(reinforceCount);
        }

        money *= 1.2;

        self.addMoney(money);
        innerBuilder.append(Config.getIncrease(money))
                .append("\n\n---아이템 현황---");

        if(!equipment.getRecipe().isEmpty()) {
            long itemId;
            int itemCount;

            Optional<Map<Long, Integer>> first = equipment.getRecipe().stream().findFirst();
            if (!first.isPresent()) {
                throw new NullPointerException("Recipe is null");
            }

            Map<Long, Integer> recipeMap = first.get();
            for(Map.Entry<Long, Integer> entry : recipeMap.entrySet()) {
                itemId = entry.getKey();
                itemCount = Math.max(entry.getValue() / 2, 1);

                self.addItem(itemId, itemCount, false);
                innerBuilder.append("\n")
                        .append(ItemList.findById(itemId))
                        .append(": ")
                        .append(Config.getIncrease(itemCount));
            }
        } else {
            innerBuilder.append("\n획득한 아이템이 없습니다");
        }

        self.removeEquip(equipId);
        Config.deleteEquipment(equipment);

        self.replyPlayer(equipment.getName() + " 의 분해를 완료했습니다", innerBuilder.toString());
    }

}
