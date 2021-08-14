package lkd.namsic.game.object;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import lkd.namsic.game.base.ChatLimit;
import lkd.namsic.game.base.LimitId;
import lkd.namsic.game.config.Config;
import lkd.namsic.game.enums.Id;
import lkd.namsic.game.enums.object.NpcList;
import lkd.namsic.game.exception.InvalidNumberException;
import lkd.namsic.game.manager.ChatManager;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Npc extends Entity {

    final long firstChat;

    Map<ChatLimit, Set<Long>> baseChat = new HashMap<>();
    Map<ChatLimit, Set<Long>> chat = new HashMap<>();

    public Npc(@NonNull NpcList npcData, long firstChat) {
        super(npcData.getDisplayName());

        this.id.setId(Id.NPC);
        this.id.setObjectId(npcData.getId());

        this.firstChat = firstChat;
    }

    public void setBaseChat(@NonNull ChatLimit chatLimit, long chatId) {
        Chat chat = Config.getData(Id.CHAT, chatId);
        if(chat.getQuestId().get() != 0) {
            throw new InvalidNumberException(0);
        } else if(!chat.isBaseMsg()) {
            throw new RuntimeException("common chat must be base msg");
        }

        Set<Long> chatSet = this.baseChat.get(chatLimit);
        if(chatSet == null) {
            chatSet = new HashSet<>();
            chatSet.add(chatId);
            this.baseChat.put(chatLimit, chatSet);
        } else {
            chatSet.add(chatId);
        }
    }

    public void setChat(@NonNull ChatLimit chatLimit, long chatId) {
        Set<Long> chatSet = this.chat.get(chatLimit);
        if(chatSet == null) {
            chatSet = new HashSet<>();
            chatSet.add(chatId);
            this.chat.put(chatLimit, chatSet);
        } else {
            chatSet.add(chatId);
        }
    }

    @NonNull
    private List<Long> getAvailableBaseChat(@NonNull Player player) {
        List<Long> list = new ArrayList<>();

        Log.i("namsic!", this.baseChat.toString());
        for(Map.Entry<ChatLimit, Set<Long>> entry : this.baseChat.entrySet()) {
            if(entry.getKey().isAvailable(player)) {
                list.addAll(entry.getValue());
            }
        }

        list.sort((o1, o2) -> o1 < o2 ? -1 : 1);
        return list;
    }

    @NonNull
    public List<Long> getAvailableChat(@NonNull Player player) {
        List<Long> list = new ArrayList<>();

        Chat chat;
        for(Map.Entry<ChatLimit, Set<Long>> entry : this.chat.entrySet()) {
            if(entry.getKey().isAvailable(player)) {
                for(long chatId : entry.getValue()) {
                    chat = Config.getData(Id.CHAT, chatId);
                    long questId = chat.getQuestId().get();

                    if(questId != 0) {
                        if(player.canAddQuest(questId)) {
                            list.add(chatId);
                        }
                    } else {
                        list.add(chatId);
                    }
                }
            }
        }

        list.sort((o1, o2) -> o1 < o2 ? -1 : 1);
        return list;
    }

    public void startChat(@NonNull Player player) {
        List<Long> availableBaseSet = this.getAvailableBaseChat(player);
        if(availableBaseSet.isEmpty()) {
            player.replyPlayer("해당 NPC 와 할 수 있는 대화가 없습니다");
            return;
        }
        
        long chatId = (long) availableBaseSet.toArray()[new Random().nextInt(availableBaseSet.size())];
        ChatManager.getInstance().startChat(player, chatId, this.getId().getObjectId());
    }

    @Deprecated
    @Override
    public void onKill(@NonNull Entity entity) {
        throw new RuntimeException("Deprecated");
    }

    @NonNull
    @Override
    public String getName() {
        return "[NPC] " + this.name;
    }

    @Deprecated
    @NonNull
    @Override
    public String getFightName() {
        throw new RuntimeException("Deprecated");
    }

}
