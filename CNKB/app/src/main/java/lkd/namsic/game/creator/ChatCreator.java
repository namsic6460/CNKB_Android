package lkd.namsic.game.creator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Arrays;

import lkd.namsic.game.config.Config;
import lkd.namsic.game.config.Emoji;
import lkd.namsic.game.enums.Id;
import lkd.namsic.game.enums.WaitResponse;
import lkd.namsic.game.enums.object.QuestList;
import lkd.namsic.game.object.Chat;
import lkd.namsic.setting.Logger;

public class ChatCreator implements Creatable {

    @NonNull
    private Chat createChat(@Nullable String name, long chatId, @NonNull String...texts) {
        Chat chat = new Chat(name);
        chat.getId().setObjectId(chatId);
        chat.getText().addAll(Arrays.asList(texts));

        return chat;
    }

    @Override
    public void start() {
        Chat chat;

        chat = createChat(null, 1L,
                "드디어 일어났네 __nickname",
                "이 소리가 어디서 들려오는지는 아직은 몰라도 돼. 결국엔 알게 될테니까",
                "어찌됬든 넌 여기서 성장해야만 해. 그리고 니가 나한테 했던 약속을 지켜야겠지",
                "음 뭐가됬든 기본적인거부터 가르쳐줄게. " + Emoji.focus("n 도움말") +
                        " 을 입력해서 명령어를 살펴봐"
        );
        chat.setAnyResponseChat("__도움말", 2L);
        chat.setAnyResponseChat("__명령어", 2L);
        chat.setAnyResponseChat("__?", 2L);
        chat.setAnyResponseChat("__h", 2L);
        chat.setAnyResponseChat("__help", 2L);
        Config.unloadObject(chat);

        chat = createChat(null, 2L,
                "좋아, 잘 따라오고 있네. 네 정보도 살펴봐야겠지?",
                "명령어 목록을 보고 네 정보를 표시하는 명령어를 사용해봐",
                "아 물론 거기 적혀있기도 하지만 네가 명령어 창을 연 것 처럼, " +
                        "모든 명령어에는 " + Emoji.focus("n") + "이나 " +
                        Emoji.focus("ㅜ") + "라는 글자가 붙으니까 기억해");
        chat.setAnyResponseChat("__정보", 3L);
        chat.setAnyResponseChat("__info", 3L);
        chat.setAnyResponseChat("__i", 3L);
        chat.setAnyResponseChat("__정보 __nickname", 3L);
        chat.setAnyResponseChat("__info __nickname", 3L);
        chat.setAnyResponseChat("__i __nickname", 3L);
        Config.unloadObject(chat);

        chat = createChat(null, 3L,
                "기본 정보랑 상세 정보로 나뉘어서 표시되는게 보이지?",
                "일단 너는 0-0-1-1, 그러니까 서남쪽 끝에 있는거고, 거점도 여기로 잡혀있어",
                "거점? 아 거점은 죽으면 태어나는 장소야. 어짜피 넌 내 권능때문에 죽을 수 없거든...",
                "마지막으로 간단한거 하나만 소개하고 가봐야겠네. 마을에서는 광질을 할 수 있으니까" +
                        " 광질 명령어를 입력해봐"
        );
        chat.setAnyResponseChat("__광질", 4L);
        chat.setAnyResponseChat("__mine", 4L);
        Config.unloadObject(chat);

        chat = createChat(null, 4L,
                "그럼 난 가볼게. 어짜피 할 일도 많고 너도 이젠 혼자서 다 할 수 있을거 같으니까",
                "맵 정보를 확인하면 다른 주민들 이름도 보이니까 대화도 해보고 말이야",
                "뭐라도 주고 가라고? 골드를 조금 넣어놨으니까 그거라도 써",
                "아 그리고... ... ... (더 이상 들리지 않는다)"
        );
        chat.getDelayTime().set(1500L);
        chat.getMoney().set(1000L);
        Config.unloadObject(chat);

        chat = createChat(null, 5L,
                "음? 못보던 얼굴이군",
                "그래 이름이 __nickname 이라고?",
                "난 이 '시작의 마을' 의 이장이라네. 잘 지네봄세",
                "우리 마을은 다양한 인종이나 종족이 있지",
                "한번 마을을 살펴보고 오는것도 좋을게야"
        );
        Config.unloadObject(chat);

        chat = createChat(null, 6L, "그래 무슨 일인가 __nickname?");
        chat.setBaseMsg(true);
        Config.unloadObject(chat);

        chat = createChat("아무일도 아닙니다", 7L, "그래그래, 무슨 일 있으면 언제나 말 걸게나");
        Config.unloadObject(chat);

        chat = createChat("광질을 하면서 할만한 퀘스트가 있을까요?", 8L,
                "음, 아무래도 돌은 항상 부족해서 말이지",
                "돌 30개만 구해다 줄 수 있겠나?"
        );
        chat.setResponseChat(WaitResponse.YES, 10L);
        chat.setResponseChat(WaitResponse.NO, 11L);
        Config.unloadObject(chat);

        chat = createChat("낚시를 하면서 할만한 퀘스트가 있을까요?", 9L,
                "요즘 바다나 강이 너무 더러워저셔 말일세",
                "쓰레기가 낚이는게 있으면 3개만 구해다 줄 수 있겠나?"
        );
        chat.setResponseChat(WaitResponse.YES, 12L);
        chat.setResponseChat(WaitResponse.NO, 11L);
        Config.unloadObject(chat);

        chat = createChat(null, 10L, "고맙네! 돌을 다 구하고 다시 말을 걸어주게나");
        chat.getQuestId().set(1L);
        Config.unloadObject(chat);

        chat = createChat(null,11L, "그럼 어쩔 수 없지. 나중에 마음 바뀌면 다시 받아가게나");
        Config.unloadObject(chat);

        chat = createChat(null, 12L, "고맙네! 쓰레기를 다 수거하고 다시 말을 걸어주게나");
        chat.getQuestId().set(2L);
        Config.unloadObject(chat);

        chat = createChat(null, 13L, "빨리 구해와줬군! 여기 보상이네");
        Config.unloadObject(chat);

        chat = createChat(null, 14L, "좋은 아침이네 __nickname!\n그래 무슨 일인가?");
        chat.setBaseMsg(true);
        Config.unloadObject(chat);

        chat = createChat(null, 15L, "점심은 먹었나 __nickname?\n그래 무슨 일로 왔나?");
        chat.setBaseMsg(true);
        Config.unloadObject(chat);

        chat = createChat(null, 16L, "한적한 저녁이군\n무슨 일로 왔나 __nickname?");
        chat.setBaseMsg(true);
        Config.unloadObject(chat);

        chat = createChat("얼굴에 걱정이 많아보이시는데, 무슨 일 있나요?", 17L,
                "이런, 얼굴에 보였나? 사실 감기에 걸렸는데 방에 불을 아무리 떼도 추워서 말일세",
                "아무래도 불의 기운을 좀 쥐고 있으면 괜찮아질 것 같은데...",
                "붉은색 구체 하나만 구해줄 수 있겠나?"
        );
        chat.setResponseChat(WaitResponse.YES, 24L);
        chat.setResponseChat(WaitResponse.NO, 18L);
        Config.unloadObject(chat);

        chat = createChat(null, 18L, "그래 뭐... 바쁘다면 어쩔 수 없는 일이지");
        Config.unloadObject(chat);

        chat = createChat(null, 19L,
                "드디어 따뜻한 방에서 쉴 수 있겠구만",
                "여기 보상이네!"
        );
        Config.unloadObject(chat);

        chat = createChat("오히려 얼굴이 더 안좋아지신 것 같은데요..?", 20L,
                "끄윽... 마침 잘왔네",
                "자네가 준 붉은색 구체가 너무 강해서 오히려 역효과가 나는 모양이야..",
                "마지막으로 하급 마나 포션 3개만 만들어와줄 수 있겠나?"
        );
        chat.setResponseChat(WaitResponse.YES, 21L);
        chat.setResponseChat(WaitResponse.NO, 22L);
        Config.unloadObject(chat);

        chat = createChat(null, 21L, "자꾸 번거롭게 해서 미안하네\n최대한 빨리 구해와주게나");
        chat.getQuestId().set(4L);
        Config.unloadObject(chat);

        chat = createChat(null, 22L,
                "40년만 젊었어도 직접 만들었을텐데...",
                "있는 일만 빨리 끝내고 다시 와주게"
        );
        Config.unloadObject(chat);

        chat = createChat(null, 23L,
                "휴 이제야 살 것 같구만\n고맙네 __nickname",
                "이건 보상이니 받아가게나"
        );
        Config.unloadObject(chat);

        chat = createChat(null, 24L, "그래도 __nickname 자네가 구해준다니 마음이 놓이는구만");
        chat.getQuestId().set(3L);
        Config.unloadObject(chat);

        chat = createChat(null, 25L,
                "여행자인가? 하하",
                "반갑네, 난 이 마을 대장장이 형석이라고 한다",
                "마을을 둘러볼거면 내 아내 '엘' 에게도 가봤나 모르겠군"
        );
        Config.unloadObject(chat);

        chat = createChat(null, 26L, "무슨 일이지?");
        chat.setBaseMsg(true);
        Config.unloadObject(chat);

        chat = createChat(null, 27L,
                "이렇게 늦은 시간에 찾아오다니",
                "하하 역시 이 마을은 내가 없으면 안되는군",
                "그래서 무슨 일이지?"
        );
        chat.setBaseMsg(true);
        Config.unloadObject(chat);

        chat = createChat("아무일도 아닙니다", 28L, "흠... 싱겁긴");
        Config.unloadObject(chat);

        chat = createChat(null, 29L, "... 안녕하세요");
        Config.unloadObject(chat);

        chat = createChat(null, 30L, "엄마가 모르는 사람이랑 얘기하지 말랬어요...");
        chat.setBaseMsg(true);
        Config.unloadObject(chat);

        chat = createChat("어... 그래", 31L, "...");
        Config.unloadObject(chat);

        chat = createChat(null, 32L,
                "음? 처음 보는 분이네요",
                "흐응, 그런 눈으로 보는걸 보니 엘프는 처음 보시나 보네요",
                "아무래도 인간들이 엘프를 처음 보면 그렇게 신기한 눈빛을 보내더라고요",
                "어쨌든 마을에 있는 동안 잘 부탁드려요"
        );
        Config.unloadObject(chat);

        chat = createChat(null, 33L, "안녕하세요");
        chat.setBaseMsg(true);
        Config.unloadObject(chat);

        chat = createChat("아 그냥 지나가다 들렀습니다", 34L,
                "오늘도 부지런하시네요",
                "가끔은 여유를 가지는 것도 좋아요",
                "다음번에 오실 땐 꽃 사러 오세요~"
        );
        Config.unloadObject(chat);

        chat = createChat(null, 35L,
                "...............",
                "아 멍때리고 있었어 미안",
                "__nickname 이라고? 그래 잘 부탁해",
                "너도 시간나면 여기서 낚시나 해보라구"
        );
        Config.unloadObject(chat);

        chat = createChat(null, 36L, "음? 왜?");
        chat.setBaseMsg(true);
        Config.unloadObject(chat);

        chat = createChat("그냥 뭐하나 싶어서", 37L,
                "엄... 나야 뭐 항상 여기서 낚싯대나 드리우고 있지",
                "그럼 잘가"
        );
        Config.unloadObject(chat);

        chat = createChat(null, 38L, "......");
        chat.setBaseMsg(true);
        Config.unloadObject(chat);

        chat = createChat(null, 39L,
                "잠시 기다려라...",
                "지금! 크 월척이군",
                "낚시 방해하지말고 조용히 가라");
        Config.unloadObject(chat);

        chat = createChat(null, 40L,"무슨 일로 찾아왔지");
        chat.setBaseMsg(true);
        Config.unloadObject(chat);

        chat = createChat("어... 글쎼요?", 41L, "꺼져");
        Config.unloadObject(chat);

        chat = createChat(null, 42L,
                "처음 뵙는 분이네요",
                "저는 이 마을의 광부 중 한명인 페드로 라고 합니다",
                "잘부탁드립니다");
        Config.unloadObject(chat);

        chat = createChat(null, 43L, "볼일 있으신가요?");
        chat.setBaseMsg(true);
        Config.unloadObject(chat);

        chat = createChat("아무것도 아닙니다", 44L,"네네 전 또 광산으로 들어가봐야곘네요. 수고하세요");
        Config.unloadObject(chat);

        chat = createChat(null, 45L,
                "흠... 자네는?",
                "__nickname 이라. 이건...? 아... 아닐세",
                "좀 특이해보여서 말이지. 잘 부탁하네"
        );
        Config.unloadObject(chat);

        chat = createChat(null, 46L, "할 말 있는가?");
        chat.setBaseMsg(true);
        Config.unloadObject(chat);

        chat = createChat("아... (입이 움직이지 않는다)", 47L,
                "아직은 이 정도 위압감도 버티기 힘든가 보군",
                "나중에 다시 오게나"
        );
        Config.unloadObject(chat);

        chat = createChat(null, 48L,
                "음? 안녕 처음 보는 사람이네",
                "나는 무명님께 가르침을 받고 있는 셀리나라고 해",
                "앞으로 잘 부탁해"
        );
        Config.unloadObject(chat);

        chat = createChat(null, 49L, "할 말 있어?");
        chat.setBaseMsg(true);
        Config.unloadObject(chat);

        chat = createChat("딱히..?", 50L,
                "할 말 없으면 수련이 끝나고 나중에 찾아와 줘",
                "수련중에 말하기는 힘들거든",
                "후욱... 지금도 겨우 하는거야"
        );
        Config.unloadObject(chat);

        chat = createChat("무명은 어떤 분이셔?", 51L,
                "그건 나도 말해주기 힘든데...",
                "말해줄 수 있는건 정말 강하시단거 정도?"
        );
        Config.unloadObject(chat);

        chat = createChat("후우... 지금은 어떤가요", 52L,
                "하하하 이제 이정도는 버틴다는 건가?",
                "그래 좋군, 하지만 아직 부족하네",
                "자네의 경험을 증명해 보게나",
                "하급 모험의 증표 3개, 하급 낚시꾼의 증표 3개, 하급 광부의 증표 30개",
                "받아들이곘는가?"
        );
        chat.setResponseChat(WaitResponse.YES, 53L);
        chat.setResponseChat(WaitResponse.NO, 54L);
        Config.unloadObject(chat);

        chat = createChat(null, 53L,
                "좋아 좋아. 그정도 의지는 있어야지",
                "기다리고 있겠네"
        );
        chat.getQuestId().set(5L);
        Config.unloadObject(chat);

        chat = createChat(null, 54L,
                "흠... 그정도 의지도 없는겐가?",
                "약간 실망이군"
        );
        Config.unloadObject(chat);

        chat = createChat(null, 55L,
                "자네의 경험은 증명 됬네",
                "조금 더 성장해서 오는 그 때를 기대하고 있겠네"
        );
        Config.unloadObject(chat);

        chat = createChat("아무것도 아닙니다", 56L,
                "그래 빨리 성장해서 오게나"
        );
        Config.unloadObject(chat);

        chat = createChat("무명은 어떤 분이셔?", 57L,
                "내 스승님이자...",
                "과거 국가의 소드마스터이셨던 분",
                "딱 여기까지 알려줄 수 있겠네.."
        );
        Config.unloadObject(chat);

        chat = createChat("낚시는 할만 한가요?", 58L,
                "하, 내가 누군데 당연히 잘 낚이지",
                "가끔 낚싯대가 부서지는게 문제지만...",
                "어? 이건 내 문제가 아니야. 낚싯대 문제라고...",
                "그래.. 낚싯대라... 낚싯대 재료 좀 구해와 봐",
                "실은 있으니 가죽 20개에 나뭇가지 10개면 적당할 것 같다"
        );
        chat.setResponseChat(WaitResponse.YES, 59L);
        chat.setResponseChat(WaitResponse.NO, 60L);
        Config.unloadObject(chat);

        chat = createChat(null, 59L, "최대한 빨리 구해와라");
        chat.getQuestId().set(QuestList.NEED_FISHING_ROD_ITEM.getId());
        Config.unloadObject(chat);

        chat = createChat(null, 60L, "...... 꺼져");
        Config.unloadObject(chat);

        chat = createChat(null, 61L,"그렇게 굼뱅이는 아니었군. 여기 보수다");
        Config.unloadObject(chat);

        chat = createChat("근데 어떤 수련을 하고 있는거야?", 62L,
                "음... 나는 마검사쪽을 공부하고 있어",
                "검술은 무명님께 배우면 되지만 아무래도 마법쪽은 어렵긴 하네",
                "아 말 나온 김에 혹시 하급 사냥꾼의 증표 5개랑 중급 사냥꾼의 증표 5개만 구해줄 수 있을까?",
                "사냥꾼의 증표에 있는 기운을 좀 쓰고 싶거든"
        );
        chat.setResponseChat(WaitResponse.YES, 63L);
        chat.setResponseChat(WaitResponse.NO, 64L);
        Config.unloadObject(chat);

        chat = createChat(null, 63L, "고마워! 다 구하면 다시 말 걸어줘");
        chat.getQuestId().set(QuestList.POWER_OF_TOKEN.getId());
        Config.unloadObject(chat);

        chat = createChat(null, 64L, "그래? 아쉽네...");
        Config.unloadObject(chat);

        chat = createChat(null, 65L,
                "고마워! 이제.....",
                "하.. 다시 수련하러 가야겠네..."
        );
        Config.unloadObject(chat);

        chat = createChat("뭔가 급해 보이시는데...", 66L,
                "아 __nickname 마침 잘왔군",
                "아내를 위한 선물을 해주려고 하는데 마땅한 아이템이 없어서 말이지",
                "혹시 금 5개와 1만 골드를 구해줄 수 있겠나?"
        );
        chat.setResponseChat(WaitResponse.YES, 67L);
        chat.setResponseChat(WaitResponse.NO, 68L);
        Config.unloadObject(chat);

        chat = createChat(null, 67L, "오 고맙군! 덕분에 선물을 만들어 줄 수 있곘군");
        chat.getQuestId().set(QuestList.GOLD_RING_GIFT.getId());
        Config.unloadObject(chat);

        chat = createChat(null, 68L, "아쉽지만 별 수 없군! 다음에 또 들르게!");
        Config.unloadObject(chat);

        chat = createChat(null, 69L,
                "음... 보답을 해주고 싶은데 마땅한 광물이 없군",
                "아 이거라도 가져가게나! 꽤나 유용할거야"
        );
        Config.unloadObject(chat);

        chat = createChat("열심히 일하고 계시네요!", 70L,
                "음... 사실 형석씨한테 석탄을 납품하기로 했거든요",
                "근데 누가 석탄을 다 캐가고 있는건지 잘 안보여서 막막하네요",
                "혹시 석탄 남으시는거 있으시면 하급 강화석과 바꿀 수 있을까요?"
        );
        chat.setResponseChat(WaitResponse.YES, 71L);
        chat.setResponseChat(WaitResponse.NO, 72L);
        Config.unloadObject(chat);

        chat = createChat(null, 71L,
                "오 감사합니다!",
                "일단... 석탄 100개 정도면 적당할 것 같아요",
                "다 구하시면 다시 와주세요!"
        );
        chat.getQuestId().set(QuestList.NEED_COAL.getId());
        Config.unloadObject(chat);

        chat = createChat(null, 72L,
                "아... 별 수없죠",
                "저는 석탄이나 더 찾아봐야겠네요"
        );
        Config.unloadObject(chat);

        chat = createChat(null, 73L,
                "와 빨리 구해와주셨네요!",
                "덕분에 일이 훨씬 편해졌습니다",
                "여기 드리기로 한 하급 강화석이예요"
        );
        Config.unloadObject(chat);

        chat = createChat("기분 좋아 보이시는데요?", 74L,
                "아 오랜만이예요 __nickname 씨",
                "사실 그이가 선물을 줬거든요",
                "아 __nickname 씨는 모험가이시니까 다양한 재료를 구해올 수 있을 것 같은데...",
                "저도 보답으로 선물을 주고 싶어서요",
                "발광석이랑 물고기 몇마리정도면 될 것 같은데 구해와주실 수 있을까요?"
        );
        chat.setResponseChat(WaitResponse.YES, 75L);
        chat.setResponseChat(WaitResponse.NO, 76L);
        Config.unloadObject(chat);

        chat = createChat(null, 75L,
                "와아 고마워요",
                "그럼 최대한 빨리 부탁할게요?"
        );
        chat.getQuestId().set(QuestList.ANOTHER_PRESENT.getId());
        Config.unloadObject(chat);

        chat = createChat(null, 76L, "흐응.... 알겠어요");
        Config.unloadObject(chat);

        chat = createChat(null, 77L,
                "음~ 이정도면 될 것 같네요 고마워요",
                "음... 답례로 드릴 수 있는건 없지만 제 힘을 좀 써드릴게요",
                "다음에도 종종 부탁드릴게요?"
        );
        Config.unloadObject(chat);

        chat = createChat("혹시 다른 할 일이 있을까 싶어 와봤습니다", 78L,
                "허허 자네가 열심히 일을 해준 덕분에 지금은 딱히 없네",
                "아 그건 그렇고 마침 자네에게 할 말이 있었는데 잘 왔구만",
                "이번에 국가에서 모험가들을 장려한다고 일정 수준 이상의 모험가들에게 이 종이를 주라고 했네",
                "음.. 이름이 아마 스텟 머시깽이 였는데... 어쨌든...",
                "기준이 아마 50레벨 이었을테니 50레벨을 달성하면 와서 받아가게나"
        );
        chat.getQuestId().set(QuestList.LV50.getId());
        Config.unloadObject(chat);

        chat = createChat(null, 79L,
                "역시 자네라면 금방 받아갈 줄 알았지 허허",
                "자 여기 유용하게 쓰게나"
        );
        chat.setResponseChat(WaitResponse.NONE, 80L);
        Config.unloadObject(chat);

        chat = createChat(null, 80L,
                "아아 하나 까먹은게 있네",
                "이번 지원은 50레벨로 끝나는게 아니라 100레벨에서도 받을 수 있다고 하네",
                "100레벨을 달성하면 와서 그것도 받아가게나"
        );
        chat.getQuestId().set(QuestList.LV100.getId());
        Config.unloadObject(chat);

        chat = createChat("너답지 않게 고민이 있어 보이네?", 81L,
                "아아 마침 잘왔어 __nickname",
                "얼마 뒤에 제사가 있거든",
                "생선은 넉넉한데 평소에 돼지를 좋아하셔서 돼지 머리를 놓고 싶은데 어떻게 구해야할지 생각하고 있었어",
                "그래서 말인데 혹시 돼지머리 3개만 구해줄 수 있을까?"
        );
        chat.setResponseChat(WaitResponse.YES, 82L);
        chat.setResponseChat(WaitResponse.NO, 83L);
        Config.unloadObject(chat);

        chat = createChat(null, 82L, "고마워. 대신 구해오면 괜찮은거 알려줄게");
        chat.getQuestId().set(QuestList.MEMORIAL_CEREMONY.getId());
        Config.unloadObject(chat);

        chat = createChat(null, 83L,
                "그래? 바쁜가보네",
                "혹시 나중에라도 생각 바뀌면 다시 찾아와",
                "하하.. 나야 하루종일 여기 있는거 알잖아?"
        );
        Config.unloadObject(chat);

        chat = createChat(null, 84L,
                "오.. 구해왔구나?",
                "자 이 책 받아가"
        );
        Config.unloadObject(chat);

        chat = createChat("수련은 할만 해?", 85L,
                "좀 힘들긴 해도 재밌어. 그리고 힘들어도 이겨내야겠지 헤헤",
                "아 맞다, 수련하는데에 필요한 아이템이 있는데 좀 구해줄 수 있을까?",
                "거미의 눈에 마법의 힘이 담겨있다고 해서 일단 5개 정도만 구해주면 될 것 같은데"
        );
        chat.setResponseChat(WaitResponse.YES, 86L);
        chat.setResponseChat(WaitResponse.NO, 87L);
        Config.unloadObject(chat);

        chat = createChat(null, 86L,
                "아 다행이다",
                "그럼 다 구하고 다시 말걸어줘"
        );
        chat.getQuestId().set(QuestList.MAGIC_OF_SPIDER_EYE.getId());
        Config.unloadObject(chat);

        chat = createChat(null, 87L, "음 직접 구해야 할려나...?");
        Config.unloadObject(chat);

        chat = createChat(null, 88L,
                "와 고마워!",
                "앞으로도 종종 부탁해? 헤헤"
        );
        Config.unloadObject(chat);

        chat = createChat("으... 바닥에 이 끈적끈적한건 다 뭔가요?", 89L,
                "아.. 우리 봄이가 놀러 갔다가 실수로 슬라임 늪지 근처까지 갔지 뭐예요",
                "근데 요즘 슬라임이 많아져서 그런지 늪지 바깥까지 슬라임이 가끔 나오거든요",
                "그래서 겨우 도망쳐왔다고 하는데... 걱정이네요",
                "아 혹시 슬라임 20마리만 잡아주실 수 있을까요?",
                "슬라임 처리 증거로 슬라임 조각 20개면 충분할것 같은데..."
        );
        chat.setResponseChat(WaitResponse.YES, 90L);
        chat.setResponseChat(WaitResponse.NO, 91L);
        Config.unloadObject(chat);

        chat = createChat(null, 90L,
                "정말 고마워요!",
                "보수는 넉넉히 챙겨 드릴게요"
        );
        chat.getQuestId().set(QuestList.STICKY_SLIME.getId());
        Config.unloadObject(chat);

        chat = createChat(null, 91L,
                "그래도 아이의 안전을 위한건데...",
                "할 일만 다 끝내면 다시 와주세요..."
        );
        Config.unloadObject(chat);

        chat = createChat(null, 92L,
                "덕분에 마음이 좀 놓이네요",
                "고마워요. 여기 약속한 보수예요"
        );
        Config.unloadObject(chat);

        chat = createChat("꽃들이 좀 시들해진 것 같네요. 무슨 문제라도 있나요?", 93L,
                "흐응... 요즘 날씨때문에 엘프의 기운으로도 꽃이 잘 안자라네요",
                "이럴때는 엘릭서 허브만 있으면 될텐데...",
                "혹시 약초를 좀 캐와주실 수 있을까요?",
                "약초 10개정도를 구해와주시면 제가 엘릭서 허브를 만들어서 하나 나눠드릴게요"
        );
        chat.setResponseChat(WaitResponse.YES, 94L);
        chat.setResponseChat(WaitResponse.NO, 95L);
        Config.unloadObject(chat);

        chat = createChat(null, 94L,
                "그럼 빨리 구해와주세요~"
        );
        chat.getQuestId().set(QuestList.HERB_COLLECTING.getId());
        Config.unloadObject(chat);

        chat = createChat(null, 95L, "아쉽네요, 엘릭서 허브에 관심이 있으실 줄 알았는데...");
        Config.unloadObject(chat);

        chat = createChat(null, 96L,
                "잠시만 기다려주세요?",
                "(숲의 기운이 강하게 느껴진다)",
                "여기 주기로 했던 엘릭서 허브예요"
        );
        chat.getPauseTime().set(4000L);
        Config.unloadObject(chat);

        chat = createChat(null, 97L, "힝...");
        Config.unloadObject(chat);

        for(int i = 0; i < Config.GEMS.length; i++) {
            long chatId = 98L + (i * 2);
            String gemName = Config.GEMS[i].getDisplayName();

            chat = createChat("보석 수집 - " + gemName, chatId,
                    "모험가님... 혹시 " + gemName + " 하나 구해줄 수 있어요?",
                    "보석을 모으고 있는데 " + gemName + " (이/가) 필요해서요..."
            );
            chat.setResponseChat(WaitResponse.YES, chatId + 1);
            chat.setResponseChat(WaitResponse.NO, 97L);
            Config.unloadObject(chat);

            chat = createChat(null, chatId + 1, "감사합니다");
            chat.getQuestId().set(QuestList.GEM_COLLECTING_QUARTZ.getId() + i);
            Config.unloadObject(chat);
        }

        chat = createChat(null, 128L,
                "(엘프가 쓰러져있다)",
                "으으........",
                "(엘프의 치료법을 묻기 위해 " + Emoji.focus("엘") + " 에게 가보자)"
        );
        chat.getQuestId().set(QuestList.HEALING_ELF1.getId());
        Config.unloadObject(chat);

        chat = createChat(null, 129L,
                "숲에 엘프가 쓰러져있다고요?",
                "이런... 저는 지금 봄이때문에 가기가 좀 그런데...",
                "일단 엘릭서 허브를 그 엘프에게 주면 어느정도 회복할거예요",
                "제가 가지고 있는 엘릭서 허브를 하나 드릴테니 가서 회복시켜주세요"
        );
        chat.getQuestId().set(QuestList.HEALING_ELF2.getId());
        Config.unloadObject(chat);

        chat = createChat(null, 130L,
                "......",
                "감사합니다...",
                "도와주셔서 감사하지만 일단 어느정도 회복을 해야할 것 같아요",
                "나중에 와주세요"
        );
        Config.unloadObject(chat);

        chat = createChat("이 시간까지 낚시를 하고 있는거야?", 131L, "zzz......");
        Config.unloadObject(chat);

        chat = createChat(null, 132L,
                "역시 자네라면 이번에도 금방 받아갈 줄 알았지 허허",
                "자 여기 유용하게 쓰게나"
        );
        Config.unloadObject(chat);

        chat = createChat(null, 133L, "고마워요!");
        Config.unloadObject(chat);

        Logger.i("ObjectMaker", "Chat making is done!");
    }

}
