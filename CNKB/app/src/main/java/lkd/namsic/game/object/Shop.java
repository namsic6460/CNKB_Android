package lkd.namsic.game.object;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import lkd.namsic.game.enums.Id;
import lkd.namsic.game.enums.object.ItemList;
import lkd.namsic.game.enums.object.NpcList;
import lombok.Getter;
import lombok.Setter;

@Getter
public class Shop extends NamedObject {

    @Setter
    Map<String, Set<Long>> simpleMap = new LinkedHashMap<>();

    final Map<Long, Long> sellPrice = new HashMap<>();
    final Map<Long, Long> buyPrice = new HashMap<>();

    public Shop(@NonNull NpcList npcData) {
        super(npcData.getDisplayName());

        this.id.setId(Id.SHOP);
        this.id.setObjectId(npcData.getId());
    }

    public double getCloseRatePercent(@NonNull Player player, long npcId) {
        int closeRate = player.getCloseRate(npcId);
        return Math.min(0.2, closeRate / 500D);
    }

    public long getSellPrice(@NonNull Player player, long itemId) {
        return (long) (Objects.requireNonNull(this.sellPrice.get(itemId)) * (1 - this.getCloseRatePercent(player, this.id.getObjectId())));
    }

    public void addSellItem(@NonNull ItemList itemData, long price) {
        this.sellPrice.put(itemData.getId(), price);
    }

    public long getBuyPrice(@NonNull Player player, long itemId) {
        return (long) (this.buyPrice.getOrDefault(itemId, -1L) * (1 + this.getCloseRatePercent(player, this.id.getObjectId())));
    }

    public void addBuyItem(@NonNull ItemList itemData, long price) {
        this.buyPrice.put(itemData.getId(), price);
    }

    public void addSimpleMap(@NonNull Set<Long> idSet, @NonNull String...keys) {
        for(String key : keys) {
            this.simpleMap.put(key, idSet);
        }
    }

}
