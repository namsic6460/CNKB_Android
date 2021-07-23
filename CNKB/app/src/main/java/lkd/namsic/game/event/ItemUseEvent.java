package lkd.namsic.game.event;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lkd.namsic.game.config.Config;
import lkd.namsic.game.enums.Id;
import lkd.namsic.game.exception.EventSkipException;
import lkd.namsic.game.gameObject.Entity;
import lkd.namsic.game.gameObject.Item;

public abstract class ItemUseEvent extends Event {

    private static final long serialVersionUID = 1L;

    @NonNull
    public static String getName() {
        return "ItemUseEvent";
    }

    public static void handleEvent(@NonNull Entity self, @Nullable List<Event> events, long itemId, int count) {
        Item item = Config.getData(Id.ITEM, itemId);

        if (events != null) {
            List<Event> removeList = new ArrayList<>();

            for (Event itemUseEvent : events) {
                try {
                    ((ItemUseEvent) itemUseEvent).onUse(self, item, count);

                    if (itemUseEvent.activeCount != -1) {
                        if (--itemUseEvent.activeCount == 0) {
                            removeList.add(itemUseEvent);
                        }
                    }
                } catch (EventSkipException ignore) {}
            }

            events.removeAll(removeList);
        }
    }

    public ItemUseEvent(int activeCount) {
        this(activeCount, null);
    }

    public ItemUseEvent(int activeCount, @Nullable Map<String, Object> variable) {
        super(activeCount, variable);
    }

    public abstract void onUse(@NonNull Entity self, Item item, int count);

    @NonNull
    @Override
    public String getClassName() {
        return getName();
    }

}
