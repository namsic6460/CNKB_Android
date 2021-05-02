package lkd.namsic.game.gameObject;

import androidx.annotation.NonNull;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import lkd.namsic.game.Config;
import lkd.namsic.game.base.ConcurrentHashSet;
import lkd.namsic.game.base.RangeInteger;
import lkd.namsic.game.enums.Id;
import lombok.ToString;

@ToString
public class Npc extends AiEntity {

    Map<RangeInteger, ConcurrentHashSet<Long>> chat = new ConcurrentHashMap<>();

    public Npc(@NonNull String name) {
        super(name);

        this.id.setId(Id.NPC);
    }

    @NonNull
    public Set<Long> getAvailableChat(@NonNull Player player) {
        int closeRate = player.getCloseRate(this.id.getObjectId());

        Set<Long> availableSet = new HashSet<>();
        for(Map.Entry<RangeInteger, ConcurrentHashSet<Long>> entry : this.chat.entrySet()) {
            if(entry.getKey().isInRange(closeRate)) {
                availableSet.addAll(entry.getValue());
            }
        }

        return availableSet;
    }

    public void startChat(@NonNull Player player, @NonNull List<Long> availableChat) {
        long chatId = (long) availableChat.toArray()[new Random().nextInt(availableChat.size())];
        player.startChat(chatId, this.getName());
    }

    public void addChat(int minCloseRate, int maxCloseRate, long chatId) {
        Config.checkId(Id.CHAT, chatId);

        RangeInteger rangeInteger = new RangeInteger(minCloseRate, maxCloseRate);

        ConcurrentHashSet<Long> chatSet = this.chat.get(rangeInteger);
        if(chatSet == null) {
            chatSet = new ConcurrentHashSet<>();
            chatSet.add(chatId);
            this.chat.put(rangeInteger, chatSet);
        } else {
            chatSet.add(chatId);
        }
    }

}
