package lkd.namsic.game.object;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import lkd.namsic.game.config.Config;
import lkd.namsic.game.enums.Id;
import lkd.namsic.game.enums.object.ItemList;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
public class Farm extends GameObject {

    @Getter
    public static class Plant extends GameObject {

        int lv;

        long growTime;

        final Map<Long, Integer> rewardItem = new HashMap<>();

        @Setter
        long plantedTime = 0;

        @Setter
        long lastHarvestTime = 0;

        public Plant(@NonNull ItemList itemData, int lv, long growMinute, @NonNull Map<Long, Integer> rewardItem) {
            this.id.setId(Id.PLANT);
            this.id.setObjectId(itemData.getId());

            this.lv = lv;
            this.growTime = growMinute * 60000;
            this.rewardItem.putAll(rewardItem);
        }

        public Plant(@NonNull ItemList itemData) {
            this.id.setId(Id.PLANT);
            this.id.setObjectId(itemData.getId());

            this.plantedTime = System.currentTimeMillis();
            this.lastHarvestTime = this.plantedTime;
        }

        public int getLv() {
            Plant plant = Objects.requireNonNull(Config.getData(Id.PLANT, this.id.getObjectId()));
            return plant.lv;
        }

        public long getGrowTime() {
            Plant plant = Objects.requireNonNull(Config.getData(Id.PLANT, this.id.getObjectId()));
            return plant.growTime;
        }

        public Map<Long, Integer> getRewardItem() {
            Plant plant = Objects.requireNonNull(Config.getData(Id.PLANT, this.id.getObjectId()));
            return plant.rewardItem;
        }
    }

    final List<Plant> planted = new LinkedList<>();

    @Setter
    int maxPlantCount = 10;

    @Setter
    int lv = 1;

    public Farm(long playerId) {
        this.id.setId(Id.FARM);
        this.id.setObjectId(playerId);
    }

}
