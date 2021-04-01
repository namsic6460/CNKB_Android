package lkd.namsic.Game.Class;

import androidx.annotation.NonNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lkd.namsic.Game.Base.ConcurrentArrayList;
import lkd.namsic.Game.Base.ConcurrentHashSet;
import lkd.namsic.Game.Base.Location;
import lkd.namsic.Game.Enum.Doing;
import lkd.namsic.Game.Enum.EquipType;
import lkd.namsic.Game.Enum.Id;
import lkd.namsic.Game.Enum.StatType;
import lkd.namsic.Game.Event.Event;
import lkd.namsic.Game.Exception.NumberRangeException;
import lombok.ToString;

@ToString
public class Boss extends AiEntity {

    @NonNull
    ConcurrentHashMap<EquipType, Double> dropPercent = new ConcurrentHashMap<>();

    public Boss(@NonNull String name) {
        super(name);
        this.id.setId(Id.BOSS);
    }

    @Override
    public EquipType equip(long equipId) {
        return this.equip(equipId, 1);
    }

    public EquipType equip(long equipId, double dropPercent) {
        EquipType equipType = super.equip(equipId);
        this.setDropPercent(equipType, dropPercent);

        return equipType;
    }

    public void setDropPercent(Map<EquipType, Double> dropPercent) {
        for(Map.Entry<EquipType, Double> entry : dropPercent.entrySet()) {
            this.setDropPercent(entry.getKey(), entry.getValue());
        }
    }

    public void setDropPercent(EquipType equipType, double percent) {
        if(percent < 0 || percent > 1) {
            throw new NumberRangeException(percent, 0, 1);
        }

        this.dropPercent.put(equipType, percent);
    }

    public double getDropPercent(EquipType equipType) {
        Double value = this.dropPercent.get(equipType);

        if(value != null) {
            return value;
        } else {
            return 1;
        }
    }

}
