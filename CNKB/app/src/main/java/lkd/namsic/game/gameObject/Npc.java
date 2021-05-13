package lkd.namsic.game.gameObject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import lkd.namsic.game.Config;
import lkd.namsic.game.base.ChatLimit;
import lkd.namsic.game.base.ConcurrentHashSet;
import lkd.namsic.game.enums.Id;
import lombok.Setter;
import lombok.ToString;

@ToString
public class Npc extends AiEntity {

    @Setter
    @Nullable
    Long firstChat = null;

    Map<ChatLimit, ConcurrentHashSet<Long>> chat = new ConcurrentHashMap<>();

    public Npc(@NonNull String name) {
        super(name);

        this.id.setId(Id.NPC);
    }

    public void setChat(@NonNull ChatLimit chatLimit, long chatId) {
        Config.checkId(Id.CHAT, chatId);

        ConcurrentHashSet<Long> chatSet = this.chat.get(chatLimit);
        if(chatSet == null) {
            chatSet = new ConcurrentHashSet<>();
            chatSet.add(chatId);
            this.chat.put(chatLimit, chatSet);
        } else {
            chatSet.add(chatId);
        }
    }

    @NonNull
    public Set<Long> getAvailableChat(@NonNull Player player) {
        Set<Long> availableSet = new HashSet<>();

        for(Map.Entry<ChatLimit, ConcurrentHashSet<Long>> entry : this.chat.entrySet()) {
            if(entry.getKey().isAvailable(player)) {
                for(long chatId : entry.getValue()) {
                    if(Config.SELECTABLE_CHAT_SET.contains(chatId)) {
                        availableSet.addAll(entry.getValue());
                    }
                }
            }
        }

        return availableSet;
    }

}
