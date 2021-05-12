package lkd.namsic.game.gameObject;

import androidx.annotation.NonNull;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import lkd.namsic.game.Config;
import lkd.namsic.game.base.ChatLimit;
import lkd.namsic.game.base.ConcurrentHashSet;
import lkd.namsic.game.base.RangeInteger;
import lkd.namsic.game.enums.Id;
import lombok.ToString;

@ToString
public class Npc extends AiEntity {

    Map<ChatLimit, Long> chat = new ConcurrentHashMap<>();

    public Npc(@NonNull String name) {
        super(name);

        this.id.setId(Id.NPC);
    }

    @NonNull
    public Set<Long> getAvailableChat(@NonNull Player player) {
        Set<Long> availableSet = new HashSet<>();
        for(Map.Entry<ChatLimit, Long> entry : this.chat.entrySet()) {
            if(entry.getKey().isAvailable(player)) {
                availableSet.add(entry.getValue());
            }
        }

        return availableSet;
    }

    public void startChat(@NonNull Player player, @NonNull List<Long> availableChat) {
        long chatId = (long) availableChat.toArray()[new Random().nextInt(availableChat.size())];
        player.startChat(chatId, this.getName());
    }

    public void addChat(ChatLimit chatLimit, long chatId) {
        Config.checkId(Id.CHAT, chatId);
        this.chat.put(chatLimit, chatId);
    }

}
