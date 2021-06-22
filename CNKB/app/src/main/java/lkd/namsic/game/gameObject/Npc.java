package lkd.namsic.game.gameObject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import lkd.namsic.game.Config;
import lkd.namsic.game.base.ChatLimit;
import lkd.namsic.game.base.ConcurrentHashSet;
import lkd.namsic.game.enums.Id;
import lkd.namsic.game.exception.InvalidNumberException;
import lkd.namsic.game.manager.ChatManager;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Npc extends Entity {

    private static final ChatManager chatManager = ChatManager.getInstance();

    @Nullable
    Long firstChat = null;

    Map<ChatLimit, ConcurrentHashSet<Long>> commonChat = new ConcurrentHashMap<>();
    Map<ChatLimit, ConcurrentHashSet<Long>> chat = new ConcurrentHashMap<>();

    public Npc(@NonNull String name) {
        super(name);
        this.id.setId(Id.NPC);
    }

    public void setCommonChat(@NonNull ChatLimit chatLimit, long chatId) {
        Config.checkId(Id.CHAT, chatId);

        Chat chat = Config.getData(Id.CHAT, chatId);
        if(chat.getQuestId().get() != 0) {
            throw new InvalidNumberException(0);
        }

        ConcurrentHashSet<Long> chatSet = this.commonChat.get(chatLimit);
        if(chatSet == null) {
            chatSet = new ConcurrentHashSet<>();
            chatSet.add(chatId);
            this.commonChat.put(chatLimit, chatSet);
        } else {
            chatSet.add(chatId);
        }
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
    private Set<Long> getAvailableCommonChat(@NonNull Player player) {
        Set<Long> availableSet = new HashSet<>();

        for(Map.Entry<ChatLimit, ConcurrentHashSet<Long>> entry : this.commonChat.entrySet()) {
            if(entry.getKey().isAvailable(player)) {
                availableSet.addAll(entry.getValue());
            }
        }

        return availableSet;
    }

    @NonNull
    public Set<Long> getAvailableChat(@NonNull Player player) {
        Set<Long> availableSet = new HashSet<>();

        Chat chat;
        for(Map.Entry<ChatLimit, ConcurrentHashSet<Long>> entry : this.chat.entrySet()) {
            if(entry.getKey().isAvailable(player)) {
                for(long chatId : entry.getValue()) {
                    chat = Config.getData(Id.CHAT, chatId);
                    long questId = chat.getQuestId().get();

                    if(questId != 0) {
                        if(player.canAddQuest(questId)) {
                            availableSet.add(chatId);
                        }
                    } else {
                        availableSet.add(chatId);
                    }
                }
            }
        }

        return availableSet;
    }

    public void startChat(@NonNull Player player) {
        Set<Long> availableCommonSet = this.getAvailableCommonChat(player);
        if(availableCommonSet.isEmpty()) {
            player.replyPlayer("해당 NPC 와 할 수 있는 대화가 없습니다");
            return;
        }
        
        long chatId = (long) availableCommonSet.toArray()[new Random().nextInt(availableCommonSet.size())];
        chatManager.startChat(player, chatId, this.getId().getObjectId());
    }

    @Override
    public void onDeath() {
        throw new RuntimeException("Why onDeath in NPC?");
    }

    @Override
    public void onKill(@NonNull Entity entity) {
        throw new RuntimeException("Why onKill in NPC?");
    }

}
