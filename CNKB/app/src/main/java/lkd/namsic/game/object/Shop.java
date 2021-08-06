package lkd.namsic.game.object;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import lkd.namsic.game.enums.Id;
import lkd.namsic.game.enums.object.ItemList;
import lkd.namsic.game.enums.object.ShopList;
import lombok.Getter;

@Getter
public class Shop extends NamedObject {

    final Map<Long, Long> sellPrice = new HashMap<>();
    final Map<Long, Long> buyPrice = new HashMap<>();

    public Shop(@NonNull ShopList shopData) {
        super(shopData.getDisplayName());

        this.id.setId(Id.SHOP);
        this.id.setObjectId(shopData.getId());
    }

    public double getCloseRatePercent(@NonNull Player player, long npcId) {
        int closeRate = player.getCloseRate(npcId);
        return Math.min(0.2, closeRate / 500D);
    }

    public long getSellPrice(@NonNull Player player, long itemId, long npcId) {
        return (long) (Objects.requireNonNull(this.sellPrice.get(itemId)) * (1 - this.getCloseRatePercent(player, npcId)));
    }

    public void addSellItem(@NonNull ItemList itemData, long price) {
        this.sellPrice.put(itemData.getId(), price);
    }

    public long getBuyPrice(@NonNull Player player, long itemId, long npcId) {
        return (long) (this.buyPrice.getOrDefault(itemId, -1L) * (1 + this.getCloseRatePercent(player, npcId)));
    }

    public void addBuyItem(@NonNull ItemList itemData, long price) {
        this.buyPrice.put(itemData.getId(), price);
    }

}
