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

public abstract class ItemEatEvent extends Event {

    @NonNull
    public static String getName() {
        return "ItemEatEvent";
    }

    public static boolean handleEvent(@NonNull Entity self, @Nullable List<Event> events, long itemId, int count) {
        boolean isCancelled = false;

        Item item = Config.getData(Id.ITEM, itemId);

        if (events != null) {
            List<Event> removeList = new ArrayList<>();

            for (Event itemUseEvent : events) {
                try {
                    isCancelled = ((ItemEatEvent) itemUseEvent).onEat(self, item, count);

                    if (itemUseEvent.activeCount != -1) {
                        if (--itemUseEvent.activeCount == 0) {
                            removeList.add(itemUseEvent);
                        }
                    }
                } catch (EventSkipException ignore) {}
            }

            events.removeAll(removeList);
        }

        return isCancelled;
    }

    public ItemEatEvent(int activeCount) {
        this(activeCount, null);
    }

    public ItemEatEvent(int activeCount, @Nullable Map<String, Object> variable) {
        super(activeCount, variable);
    }

    public abstract boolean onEat(@NonNull Entity self, Item item, int count);

    @NonNull
    @Override
    public String getClassName() {
        return getName();
    }

}
