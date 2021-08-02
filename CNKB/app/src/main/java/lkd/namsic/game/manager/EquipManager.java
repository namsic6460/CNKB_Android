package lkd.namsic.game.manager;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import lkd.namsic.game.base.ConcurrentHashSet;
import lkd.namsic.game.base.EquipUse;
import lkd.namsic.game.config.Config;
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

    public void equip(@NonNull Entity self, int index) {
        List<Long> list = new ArrayList<>(self.getEquipInventory());

        int size = list.size();
        if(index <= 0 || index > size) {
            throw new WeirdCommandException("1 ~ " + size + " 의 숫자를 입력해주세요");
        }

        Collections.sort(list);

        long equipId = list.get(index - 1);

        Equipment equipment = Config.getData(Id.EQUIPMENT, equipId);
        EquipType equipType = equipment.getEquipType();
        long equippedId = self.getEquipped(equipType);

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
            ((Player) self).replyPlayer(equipment.getName() + "(을/를) 착용했습니다", innerBuilder.toString());
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

        return equipId;
    }

    public void tryUse(@NonNull Player self, @NonNull EquipType equipType) {
        long equipId = checkUse(self, equipType);
        this.use(self, equipId);
    }

    public void use(@NonNull Player self, long equipId) {
        Config.checkId(Id.EQUIPMENT, equipId);

        self.addLog(LogData.TOTAL_EQUIP_USE, 1);

        Equipment equipment = Config.getData(Id.EQUIPMENT, equipId);

        String msg = equipment.getName() + " 을 사용했습니다\n";

        EquipUse use = Objects.requireNonNull(equipment.getEquipUse());
        String result = use.tryUse(self, new ArrayList<>());

        self.replyPlayer(msg + result);
    }

}
