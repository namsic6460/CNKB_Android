package lkd.namsic.Game.Event;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import lkd.namsic.Game.GameObject.Item;

public abstract class ItemUseEvent extends Event {

    public abstract boolean onUse(boolean isCancelled, Item item);

    public static boolean handleEvent(@Nullable List<Event> events, @NonNull Object[] args) {
        boolean isCancelled = false;

        Item item = (Item) args[0];

        if (events != null) {
            List<Event> removeList = new ArrayList<>();
            for (Event itemUseEvent : events) {
                isCancelled = ((ItemUseEvent) itemUseEvent).onUse(isCancelled, item);

                if (itemUseEvent.activeCount != -1) {
                    if (--itemUseEvent.activeCount == 0) {
                        removeList.add(itemUseEvent);
                    }
                }
            }

            events.removeAll(removeList);
        }

        return isCancelled;
    }

    @NonNull
    public static String getName() {
        return "ItemUseEvent";
    }

}
