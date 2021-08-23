package lkd.namsic.game.manager;

import android.app.Notification;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.Map;
import java.util.Set;

import lkd.namsic.game.config.Config;
import lkd.namsic.game.KakaoTalk;
import lkd.namsic.game.enums.Variable;
import lkd.namsic.game.base.Location;
import lkd.namsic.game.enums.Doing;
import lkd.namsic.game.enums.Id;
import lkd.namsic.game.enums.LogData;
import lkd.namsic.game.enums.WaitResponse;
import lkd.namsic.game.enums.object.NpcList;
import lkd.namsic.game.enums.object.QuestList;
import lkd.namsic.game.exception.InvalidNumberException;
import lkd.namsic.game.object.Chat;
import lkd.namsic.game.object.Npc;
import lkd.namsic.game.object.Player;
import lkd.namsic.setting.Logger;

public class ChatManager {

    private static final ChatManager instance = new ChatManager();

    private static final QuestManager questManager = QuestManager.getInstance();
    private static final MoveManager moveManager = MoveManager.getInstance();

    public static ChatManager getInstance() {
        return instance;
    }

    public void checkResponseChat(@NonNull Player player, @NonNull String msg) {
        String response;

        if(msg.startsWith("__")) {
            return;
        } else if(msg.startsWith("N") || msg.startsWith("n ") || msg.startsWith("ㅜ ")) {
            response = msg.replaceFirst("n ", "__")
                    .replaceFirst("ㅜ ", "__")
                    .replaceFirst("N ", "__");
        } else {
            response = msg;
        }

        WaitResponse waitResponse = WaitResponse.parseResponse(response);
        long chatId = player.getResponseChat(waitResponse);
        if(chatId != 0L) {
            player.getResponseChat().clear();
            this.startChat(player, chatId, player.getWaitNpcId());
            return;
        }

        chatId = player.getAnyResponseChat(response);
        if(chatId != 0L) {
            player.getAnyResponseChat().clear();
            this.startChat(player, chatId, player.getWaitNpcId());
        }
    }

    public void startChat(@NonNull Player self, @NonNull String npcName) {
        long npcId = NpcList.checkByName(self, npcName);

        Set<Long> questSet = self.getQuestNpc().get(npcId);
        if(questSet != null) {
            for (long questId : questSet) {
                if (questManager.canClearQuest(self, questId)) {
                    this.startChat(self, self.getQuest().get(questId), npcId);
                    questManager.clearQuest(self, questId, npcId);
                    return;
                }
            }
        }

        Npc npc = Config.getData(Id.NPC, npcId);

        if(self.getChatCount(npcId) == 0) {
            this.startChat(self, npc.getFirstChat(), npcId);
        } else {
            npc.startChat(self);
        }
    }

    public void startChat(@NonNull Player self, long chatId, long npcId) {
        self.setDoing(Doing.CHAT);

        Chat chat = Config.getData(Id.CHAT, chatId);
        Npc npc = Config.getData(Id.NPC, npcId);
        Notification.Action session = self.getSession();

        self.addLog(LogData.CHAT, 1);
        self.addChatCount(npcId, 1);

        try {
            Thread.sleep(chat.getDelayTime());
        } catch (InterruptedException e) {
            Logger.e("ChatManager", e);
            return;
        }

        long pauseTime = chat.getPauseTime();
        String preString = npc.getName() + " -> " + self.getNickName() + "\n";

        List<String> texts = chat.getText();
        int size = texts.size() - 1;
        for (int i = 0; i <= size; i++) {
            KakaoTalk.reply(session, preString + texts.get(i)
                    .replaceAll("__nickname", self.getNickName())
                    .replaceAll("__lv", self.getLv() + ""));

            try {
                if (i != size) {
                    Thread.sleep(pauseTime);
                }
            } catch (InterruptedException e) {
                Logger.e("ChatManager", e);
                return;
            }
        }

        self.addMoney(chat.getMoney());

        for (Map.Entry<Long, Integer> entry : chat.getItem().entrySet()) {
            self.addItem(entry.getKey(), entry.getValue(), false);
        }

        for (long equipId : chat.getEquipment()) {
            self.addEquip(equipId);
        }

        for (Map.Entry<Variable, Integer> entry : chat.getVariable().entrySet()) {
            self.addVariable(entry.getKey(), entry.getValue());
        }

        long questId = chat.getQuestId();
        if (questId != 0) {
            self.addQuest(chat.getQuestId());
            self.replyPlayer("퀘스트 \"" + QuestList.findById(questId) + "\" (을/를) 수락하였습니다");
        }

        Location tpLocation = chat.getTpLocation();
        if (tpLocation != null && !self.getLocation().equals(tpLocation)) {
            moveManager.setMap(self, chat.getTpLocation());
        }

        if (chat.isBaseMsg()) {
            self.getResponseChat().clear();
            self.getAnyResponseChat().clear();

            List<Long> availableChat = npc.getAvailableChat(self);

            if (availableChat.isEmpty()) {
                self.replyPlayer("가능한 대화가 없습니다");
                self.setDoing(Doing.NONE);
                return;
            }

            int index = 1;
            String indexStr;
            Chat chatData;
            StringBuilder builder = new StringBuilder("대화를 선택해주세요\n");
            for (long availableChatId : availableChat) {
                chatData = Config.getData(Id.CHAT, availableChatId);
                indexStr = Integer.toString(index++);

                builder.append("\n")
                        .append(indexStr)
                        .append(": ")
                        .append(chatData.getName());

                self.setAnyResponseChat(indexStr, availableChatId);
            }

            self.setWaitNpcId(npcId);
            self.setDoing(Doing.WAIT_RESPONSE);

            self.replyPlayer(builder.toString().trim());
        } else {
            if (chat.getResponseChat().isEmpty() && chat.getAnyResponseChat().isEmpty()) {
                self.setDoing(Doing.NONE);
            } else {
                long noneChatId = chat.getResponseChat(WaitResponse.NONE);
                if (noneChatId != 0) {
                    if (chatId == noneChatId) {
                        throw new InvalidNumberException(noneChatId);
                    }

                    long noneNpcId = chat.getNoneNpcId();
                    if(noneNpcId == NpcList.NONE.getId()) {
                        noneNpcId = npcId;
                    }

                    this.startChat(self, noneChatId, noneNpcId);
                    return;
                }

                self.setWaitNpcId(npcId);

                self.getResponseChat().clear();
                self.getResponseChat().putAll(chat.getResponseChat());

                self.getAnyResponseChat().clear();
                for(Map.Entry<String, Long> entry : chat.getAnyResponseChat().entrySet()) {
                    self.getAnyResponseChat().put(entry.getKey()
                            .replace("__nickname", self.getNickName()), entry.getValue());
                }

                boolean isTutorial = self.getObjectVariable(Variable.IS_TUTORIAL, false);
                if (!isTutorial) {
                    StringBuilder builder = new StringBuilder("대답을 입력해주세요\n");

                    if (!chat.getResponseChat().isEmpty()) {
                        builder.append("\n");

                        for (WaitResponse waitResponse : chat.getResponseChat().keySet()) {
                            builder.append(waitResponse.getDisplay())
                                    .append("\n");
                        }
                    }

                    if (!chat.getAnyResponseChat().isEmpty()) {
                        builder.append("\n(다른 메세지 목록)");

                        String waitResponse;
                        for (String response : chat.getAnyResponseChat().keySet()) {
                            if (response.startsWith("__")) {
                                waitResponse = response.replace("__", "(ㅜ/n) ");
                            } else {
                                waitResponse = response;
                            }

                            builder.append("\n")
                                    .append(waitResponse);
                        }
                    }

                    self.replyPlayer(builder.toString().trim());
                }

                self.setDoing(Doing.WAIT_RESPONSE);
            }
        }
    }

}
