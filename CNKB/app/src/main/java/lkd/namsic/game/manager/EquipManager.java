package lkd.namsic.game.manager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Map;
import java.util.Objects;

import lkd.namsic.game.base.ConcurrentHashSet;
import lkd.namsic.game.base.EquipUse;
import lkd.namsic.game.config.Config;
import lkd.namsic.game.config.Emoji;
import lkd.namsic.game.enums.EquipType;
import lkd.namsic.game.enums.Id;
import lkd.namsic.game.enums.LogData;
import lkd.namsic.game.enums.StatType;
import lkd.namsic.game.enums.object.EquipList;
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
        return equipment.getTotalLimitLv() <= self.getLv().get() && self.compareStat(equipment.getLimitStat());
    }

    public void equip(@NonNull Entity self, int index) {
        long equipId = self.getEquipIdByIndex(index);

        Equipment equipment = Config.getData(Id.EQUIPMENT, equipId);
        EquipType equipType = equipment.getEquipType();
        long equippedId = self.getEquipped(equipType);

        if(self.getId().getId().equals(Id.PLAYER)) {
            Player player = (Player) self;

            if(!this.canEquip(player, equipId)) {
                StringBuilder innerBuilder = new StringBuilder("장착 제한 레벨: ")
                        .append(equipment.getTotalLimitLv())
                        .append("\n\n")
                        .append("---장착 제한 스텟---");

                if(equipment.getLimitStat().isEmpty()) {
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

        if(equippedId == equipId) {
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
        for(StatType statType : StatType.values()) {
            try {
                Config.checkStatType(statType);
            } catch (UnhandledEnumException e) {
                continue;
            }

            stat = equipment.getStat(statType);

            if(stat != 0) {
                self.addEquipStat(statType, stat);
                innerBuilder.append("\n")
                        .append(statType.getDisplayName())
                        .append(": ")
                        .append(Config.getIncrease(stat));
            }
        }

        self.getEquipped().put(equipType, equipId);
        self.getRemovedEquipEvent().put(equipType, new ConcurrentHashSet<>());

        if(self.getId().getId().equals(Id.PLAYER)) {
            ((Player) self).replyPlayer(equipment.getName() + " (을/를) 착용했습니다", innerBuilder.toString());
        }
    }

    public void unEquip(@NonNull Entity self, @NonNull EquipType equipType) {
        long equipId = self.getEquipped(equipType);
        if(equipId == EquipList.NONE.getId()) {
            throw new WeirdCommandException("해당 유형의 장비는 착용하고 있지 않습니다");
        }

        StringBuilder innerBuilder = new StringBuilder("---스텟 현황---");

        Equipment equipment = Config.getData(Id.EQUIPMENT, equipId);

        int stat;
        for(StatType statType : StatType.values()) {
            try {
                Config.checkStatType(statType);
            } catch (UnhandledEnumException e) {
                continue;
            }

            stat = equipment.getStat(statType) * -1;

            if(stat != 0) {
                self.addEquipStat(statType, stat);
                innerBuilder.append("\n")
                        .append(statType.getDisplayName())
                        .append(": ")
                        .append(Config.getIncrease(stat));
            }
        }

        self.getEquipped().remove(equipType);
        self.getRemovedEquipEvent().remove(equipType);

        if(self.getId().getId().equals(Id.PLAYER)) {
            ((Player) self).replyPlayer(equipment.getName() + "(을/를) 착용 해제했습니다", innerBuilder.toString());
        }
    }

    public long checkUse(@NonNull Player self, @NonNull EquipType equipType) {
        long equipId = self.getEquipped(equipType);

        if(equipId == EquipList.NONE.getId()) {
            throw new WeirdCommandException("해당 부위는 장비를 착용하고 있지 않습니다");
        }

        Equipment equipment = Config.getData(Id.EQUIPMENT, equipId);
        if(equipment.getEquipUse() == null) {
            throw new WeirdCommandException("해당 부위의 장비는 사용이 불가능합니다");
        }

        return equipment.getOriginalId();
    }

    public void tryUse(@NonNull Player self, @NonNull EquipType equipType, @Nullable String other) {
        long equipId = checkUse(self, equipType);
        this.use(self, equipId, other);
    }

    public void use(@NonNull Entity self, long equipId, @Nullable String other) {
        Equipment equipment = Config.getData(Id.EQUIPMENT, equipId);

        String msg = equipment.getName() + " 을 사용했습니다\n";

        EquipUse use = Objects.requireNonNull(equipment.getEquipUse());
        String result = use.tryUse(self, other);

        if(self.getId().getId().equals(Id.PLAYER)) {
            Player player = (Player) self;

            player.addLog(LogData.TOTAL_EQUIP_USE, 1);
            player.replyPlayer(msg + result);
        }
    }

}
