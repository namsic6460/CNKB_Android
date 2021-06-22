package lkd.namsic.game.manager;

import android.app.Notification;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.Map;
import java.util.Set;

import lkd.namsic.game.config.Config;
import lkd.namsic.game.KakaoTalk;
import lkd.namsic.game.config.ObjectList;
import lkd.namsic.game.enums.Variable;
import lkd.namsic.game.base.Location;
import lkd.namsic.game.enums.Doing;
import lkd.namsic.game.enums.Id;
import lkd.namsic.game.enums.LogData;
import lkd.namsic.game.enums.WaitResponse;
import lkd.namsic.game.exception.InvalidNumberException;
import lkd.namsic.game.exception.WeirdCommandException;
import lkd.namsic.game.gameObject.Chat;
import lkd.namsic.game.gameObject.MapClass;
import lkd.namsic.game.gameObject.Npc;
import lkd.namsic.game.gameObject.Player;
import lkd.namsic.setting.Logger;

public class ChatManager {

    private static final ChatManager instance = new ChatManager();
    private static final QuestManager questManager = QuestManager.getInstance();
    private static final MoveManager moveManager = new MoveManager();

    public static ChatManager getInstance() {
        return instance;
    }

    public void checkResponseChat(@NonNull Player player, @NonNull String msg) {
        String response;

        if(msg.startsWith("__")) {
            return;
        } else if(msg.startsWith("n ") || msg.startsWith("ㅜ ")) {
            response = msg.replaceAll("n ", "__").replaceAll("ㅜ ", "__");
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

    public void startChat(@NonNull Player self,  @NonNull String npcName) {
        Long npcId = ObjectList.npcList.get(npcName);
        MapClass map = Config.getMapData(self.getLocation());

        if(npcId == null || !map.getEntity(Id.NPC).contains(npcId)) {
            throw new WeirdCommandException("해당 NPC 를 찾을 수 없습니다\n" +
                    "존재하지 않거나 현재 맵에 없는 NPC 일 수 있습니다\n" +
                    "현재 위치 정보를 확인해보세요");
        }

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
        long chatCount = self.getChatCount(npcId);

        if(chatCount == 0 && npc.getFirstChat() != null) {
            this.startChat(self, npc.getFirstChat(), npcId);
        } else {
            npc.startChat(self);
        }
    }

    public void startChat(@NonNull Player self, long chatId, long npcId) {
        Config.checkId(Id.CHAT, chatId);
        self.setDoing(Doing.CHAT);

        Chat chat = Config.getData(Id.CHAT, chatId);
        Npc npc = Config.getData(Id.NPC, npcId);
        Notification.Action session = self.getSession();

        Thread thread = new Thread(() -> {
            try {
                try {
                    Thread.sleep(chat.getDelayTime().get());
                } catch (InterruptedException e) {
                    Log.e("Player.startChat - chat Thread", Config.errorString(e));
                    return;
                }

                long pauseTime = chat.getPauseTime().get();
                String preString = "[" + npc.getName() + " -> " + self.getNickName() + "]\n";

                List<String> texts = chat.getText();
                int size = texts.size() - 1;
                for (int i = 0; i <= size; i++) {
                    KakaoTalk.reply(session, preString + texts.get(i).replaceAll("__nickname", self.getNickName())
                            .replaceAll("__lv", self.getLv().get().toString()));

                    try {
                        if (i != size) {
                            Thread.sleep(pauseTime);
                        }
                    } catch (InterruptedException e) {
                        Log.e("Player.startChat - chat Thread", Config.errorString(e));
                        return;
                    }
                }

                self.addMoney(chat.getMoney().get());

                for (Map.Entry<Long, Integer> entry : chat.getItem().entrySet()) {
                    self.addItem(entry.getKey(), entry.getValue());
                }

                for (long equipId : chat.getEquipment()) {
                    self.addEquip(equipId);
                }

                for (Map.Entry<Variable, Integer> entry : chat.getVariable().entrySet()) {
                    self.addVariable(entry.getKey(), entry.getValue());
                }

                long questId = chat.getQuestId().get();
                if (questId != 0) {
                    self.addQuest(chat.getQuestId().get());
                }

                Location tpLocation = chat.getTpLocation();
                if (!self.getLocation().equals(tpLocation)) {
                    moveManager.setMap(self, chat.getTpLocation());
                }

                if (chat.isBaseMsg()) {
                    self.getResponseChat().clear();
                    self.getAnyResponseChat().clear();

                    Set<Long> availableChat = npc.getAvailableChat(self);

                    if (availableChat.isEmpty()) {
                        self.replyPlayer("가능한 대화가 없습니다");
                        self.setDoing(Doing.NONE);
                        return;
                    }

                    int index = 0;
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

                            this.startChat(self, noneChatId, npcId);
                            return;
                        }

                        self.setWaitNpcId(npcId);

                        self.getResponseChat().clear();
                        self.getResponseChat().putAll(chat.getResponseChat());
                        self.getAnyResponseChat().clear();
                        self.getAnyResponseChat().putAll(chat.getAnyResponseChat());

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
            } catch (Exception e) {
                Log.e("ChatManager", Config.errorString(e));
                Logger.e("ChatManager", e);
            }
        });

        self.addLog(LogData.CHAT, 1);
        self.addChatCount(npcId, 1);
        thread.start();

        try {
            thread.join();
        } catch (InterruptedException e) {
            Logger.e("player.chatThread", e);
            throw new RuntimeException(e);
        }
    }

}
