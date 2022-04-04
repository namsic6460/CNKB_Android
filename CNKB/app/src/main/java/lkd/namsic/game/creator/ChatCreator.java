package lkd.namsic.game.creator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Arrays;

import lkd.namsic.game.config.Config;
import lkd.namsic.game.config.Emoji;
import lkd.namsic.game.enums.WaitResponse;
import lkd.namsic.game.enums.object.ItemList;
import lkd.namsic.game.enums.object.NpcList;
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
    
    private void createBaseChat(long chatId, @NonNull String...texts) {
        Chat chat = createChat(null, chatId, texts);
        chat.setBaseMsg(true);
        Config.unloadObject(chat);
    }
    
    private void createAskingChat(@Nullable String name, long chatId, long yesChatId, long noChatId, @NonNull String...texts) {
        Chat chat = createChat(name, chatId, texts);
        chat.setResponseChat(WaitResponse.YES, yesChatId);
        chat.setResponseChat(WaitResponse.NO, noChatId);
        Config.unloadObject(chat);
    }
    
    private void createQuestChat(long chatId, @NonNull QuestList questData, @NonNull String...texts) {
        createQuestChat(null, chatId, questData.getId(), texts);
    }
    
    private void createQuestChat(long chatId, long questId, @NonNull String...texts) {
        createQuestChat(null, chatId, questId, texts);
    }
    
    private void createQuestChat(@Nullable String name, long chatId, long questId, @NonNull String...texts) {
        Chat chat = createChat(name, chatId, texts);
        chat.setQuestId(questId);
        Config.unloadObject(chat);
    }
    
    private void createCommonChat(@Nullable String name, long chatId, @NonNull String...texts) {
        Chat chat = createChat(name, chatId, texts);
        Config.unloadObject(chat);
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
        chat.setDelayTime(1500L);
        chat.setMoney(1000L);
        Config.unloadObject(chat);

        chat = createChat(null, 5L,
                "음? 못보던 얼굴이군",
                "그래 이름이 __nickname 이라고?",
                "난 이 '시작의 마을' 의 이장이라네. 잘 지네봄세",
                "우리 마을은 다양한 인종이나 종족이 있지",
                "한번 마을을 살펴보고 오는것도 좋을게야"
        );
        Config.unloadObject(chat);

        createBaseChat(6L, "그래 무슨 일인가 __nickname?");
        createCommonChat("아무일도 아닙니다", 7L, "그래그래, 무슨 일 있으면 언제나 말 걸게나");

        createAskingChat("광질을 하면서 할만한 퀘스트가 있을까요?", 8L, 10L, 11L,
            "음, 아무래도 돌은 항상 부족해서 말이지",
            "돌 30개만 구해다 줄 수 있겠나?"
        );
        createAskingChat("낚시를 하면서 할만한 퀘스트가 있을까요?", 9L, 12L, 11L,
            "요즘 바다나 강이 너무 더러워저셔 말일세",
            "쓰레기가 낚이는게 있으면 1개만 구해다 줄 수 있겠나?"
        );

        createQuestChat(10L, QuestList.WORK_OF_MINER, "고맙네! 돌을 다 구하고 다시 말을 걸어주게나");
    
        createCommonChat(null, 11L, "그럼 어쩔 수 없지. 나중에 마음 바뀌면 다시 받아가게나");
        
        createQuestChat(12L, QuestList.TRASH_COLLECTING, "고맙네! 쓰레기를 다 수거하고 다시 말을 걸어주게나");
    
        createCommonChat(null, 13L, "빨리 구해와줬군! 여기 보상이네");

        createBaseChat(14L, "좋은 아침이네 __nickname!\n그래 무슨 일인가?");
        createBaseChat(15L, "점심은 먹었나 __nickname?\n그래 무슨 일로 왔나?");
        createBaseChat(16L, "한적한 저녁이군\n무슨 일로 왔나 __nickname?");

        createAskingChat("얼굴에 걱정이 많아보이시는데, 무슨 일 있나요?", 17L, 24L, 18L,
            "이런, 얼굴에 보였나? 사실 감기에 걸렸는데 방에 불을 아무리 떼도 추워서 말일세",
            "아무래도 불의 기운을 좀 쥐고 있으면 괜찮아질 것 같은데...",
            "붉은색 구체 하나만 구해줄 수 있겠나?"
        );
    
        createCommonChat(null, 18L, "그래 뭐... 바쁘다면 어쩔 수 없는 일이지");
    
        createCommonChat(null, 19L,
                "드디어 따뜻한 방에서 쉴 수 있겠구만",
                "여기 보상이네!"
        );

        createAskingChat("오히려 얼굴이 더 안좋아지신 것 같은데요..?", 20L, 21L, 22L,
            "끄윽... 마침 잘왔네",
            "자네가 준 붉은색 구체가 너무 강해서 오히려 역효과가 나는 모양이야..",
            "마지막으로 하급 마나 포션 3개만 만들어와줄 수 있겠나?"
        );

        createQuestChat(21L, QuestList.TOO_STRONG_FIRE, "자꾸 번거롭게 해서 미안하네\n최대한 빨리 구해와주게나");
    
        createCommonChat(null, 22L,
                "40년만 젊었어도 직접 만들었을텐데...",
                "있는 일만 빨리 끝내고 다시 와주게"
        );
    
        createCommonChat(null, 23L,
                "휴 이제야 살 것 같구만\n고맙네 __nickname",
                "이건 보상이니 받아가게나"
        );

        createQuestChat(24L, QuestList.NEED_FIRE, "그래도 __nickname 자네가 구해준다니 마음이 놓이는구만");
    
        createCommonChat(null, 25L,
                "여행자인가? 하하",
                "반갑네, 난 이 마을 대장장이 형석이라고 한다",
                "마을을 둘러볼거면 내 아내 '엘' 에게도 가봤나 모르겠군"
        );

        createBaseChat(26L, "무슨 일이지?");
        createBaseChat(27L,
            "이렇게 늦은 시간에 찾아오다니",
            "하하 역시 이 마을은 내가 없으면 안되는군",
            "그래서 무슨 일이지?"
        );
    
        createCommonChat("아무일도 아닙니다", 28L, "흠... 싱겁긴");
        createCommonChat(null, 29L, "... 안녕하세요");

        createBaseChat(30L, "엄마가 모르는 사람이랑 얘기하지 말랬어요...");
        createCommonChat("어... 그래", 31L, "...");
    
        createCommonChat(null, 32L,
                "음? 처음 보는 분이네요",
                "흐응, 그런 눈으로 보는걸 보니 엘프는 처음 보시나 보네요",
                "아무래도 인간들이 엘프를 처음 보면 그렇게 신기한 눈빛을 보내더라고요",
                "어쨌든 마을에 있는 동안 잘 부탁드려요"
        );

        createBaseChat(33L, "안녕하세요");
    
        createCommonChat("아 그냥 지나가다 들렀습니다", 34L,
                "오늘도 부지런하시네요",
                "가끔은 여유를 가지는 것도 좋아요",
                "다음번에 오실 땐 꽃 사러 오세요~"
        );
    
        createCommonChat(null, 35L,
                "...............",
                "아 멍때리고 있었어 미안",
                "__nickname 이라고? 그래 잘 부탁해",
                "너도 시간나면 여기서 낚시나 해보라구"
        );

        createBaseChat(36L, "음? 왜?");
    
        createCommonChat("그냥 뭐하나 싶어서", 37L,
                "엄... 나야 뭐 항상 여기서 낚싯대나 드리우고 있지",
                "그럼 잘가"
        );

        createBaseChat(38L, "......");
    
        createCommonChat(null, 39L,
                "잠시 기다려라...",
                "지금! 크 월척이군",
                "낚시 방해하지말고 조용히 가라"
        );

        createBaseChat(40L, "무슨 일로 찾아왔지");
    
        createCommonChat("어... 글쎼요?", 41L, "꺼져");
    
        createCommonChat(null, 42L,
                "처음 뵙는 분이네요",
                "저는 이 마을의 광부 중 한명인 페드로 라고 합니다",
                "잘부탁드립니다"
        );

        createBaseChat(43L, "볼일 있으신가요?");
    
        createCommonChat("아무것도 아닙니다", 44L,"네네 전 또 광산으로 들어가봐야곘네요. 수고하세요");
    
        createCommonChat(null, 45L,
                "흠... 자네는?",
                "__nickname 이라. 이건...? 아... 아닐세",
                "좀 특이해보여서 말이지. 잘 부탁하네"
        );

        createBaseChat(46L, "할 말 있는가?");
    
        createCommonChat("아... (입이 움직이지 않는다)", 47L,
                "아직은 이 정도 위압감도 버티기 힘든가 보군",
                "나중에 다시 오게나"
        );
    
        createCommonChat(null, 48L,
                "음? 안녕 처음 보는 사람이네",
                "나는 무명님께 가르침을 받고 있는 셀리나라고 해",
                "앞으로 잘 부탁해"
        );

        createBaseChat(49L, "할 말 있어?");
    
        createCommonChat("딱히..?", 50L,
                "할 말 없으면 수련이 끝나고 나중에 찾아와 줘",
                "수련중에 말하기는 힘들거든",
                "후욱... 지금도 겨우 하는거야"
        );
    
        createCommonChat("무명은 어떤 분이셔?", 51L,
                "그건 나도 말해주기 힘든데...",
                "말해줄 수 있는건 정말 강하시단거 정도?"
        );

        createAskingChat("윽... 후우...", 52L, 53L, 54L,
            "하하하 이 정도 기세는 버틴다는 건가?",
            "그래 좋군, 하지만 아직 부족하네",
            "자네의 경험을 증명해 보게나",
            "하급 모험의 증표 1개, 하급 낚시꾼의 증표 3개, 하급 광부의 증표 30개",
            "받아들이곘는가?"
        );

        createQuestChat(53L, QuestList.PROVE_EXPERIENCE,
            "좋아 좋아. 그정도 의지는 있어야지",
            "기다리고 있겠네"
        );
    
        createCommonChat(null, 54L,
                "흠... 그정도 의지도 없는겐가?",
                "약간 실망이군"
        );
    
        createCommonChat(null, 55L,
                "자네의 경험은 증명 됬네",
                "조금 더 성장해서 오는 그 때를 기대하고 있겠네"
        );
    
        createCommonChat("아무것도 아닙니다", 56L,
                "그래 빨리 성장해서 오게나"
        );
    
        createCommonChat("무명은 어떤 분이셔?", 57L,
                "내 스승님이자...",
                "과거 국가의 소드마스터이셨던 분",
                "딱 여기까지 알려줄 수 있겠네.."
        );

        createAskingChat("낚시는 할만 한가요?", 58L, 59L, 60L,
            "하, 내가 누군데 당연히 잘 낚이지",
            "가끔 낚싯대가 부서지는게 문제지만...",
            "어? 이건 내 문제가 아니야. 낚싯대 문제라고...",
            "그래.. 낚싯대라... 낚싯대 재료 좀 구해와 봐",
            "실은 있으니 가죽 20개에 나뭇가지 10개면 적당할 것 같다"
        );

        createQuestChat(59L, QuestList.NEED_FISHING_ROD_ITEM, "최대한 빨리 구해와라");
        
        createCommonChat(null, 60L, "...... 꺼져");
        
        createCommonChat(null, 61L,"그렇게 굼뱅이는 아니었군. 여기 보수다");

        createAskingChat("근데 어떤 수련을 하고 있는거야?", 62L, 63L, 64L,
            "음... 나는 마검사쪽을 공부하고 있어",
            "검술은 무명님께 배우면 되지만 아무래도 마법쪽은 어렵긴 하네",
            "아 말 나온 김에 혹시 하급 사냥꾼의 증표 5개랑 중급 사냥꾼의 증표 5개만 구해줄 수 있을까?",
            "사냥꾼의 증표에 있는 기운을 좀 쓰고 싶거든"
        );

        createQuestChat(63L, QuestList.POWER_OF_TOKEN, "고마워! 다 구하면 다시 말 걸어줘");
    
        createCommonChat(null, 64L, "그래? 아쉽네...");
    
        createCommonChat(null, 65L,
                "고마워! 이제.....",
                "하.. 다시 수련하러 가야겠네..."
        );

        createAskingChat("뭔가 급해 보이시는데...", 66L, 67L, 68L,
            "아 __nickname 마침 잘왔군",
            "아내를 위한 선물을 해주려고 하는데 마땅한 아이템이 없어서 말이지",
            "혹시 금 5개와 1만 골드를 구해줄 수 있겠나?"
        );

        createQuestChat(67L, QuestList.GOLD_RING_GIFT, "오 고맙군! 덕분에 선물을 만들어 줄 수 있곘군");
    
        createCommonChat(null, 68L, "아쉽지만 별 수 없군! 다음에 또 들르게!");
    
        createCommonChat(null, 69L,
                "음... 보답을 해주고 싶은데 마땅한 광물이 없군",
                "아 이거라도 가져가게나! 꽤나 유용할거야"
        );

        createAskingChat("열심히 일하고 계시네요!", 70L, 71L, 72L,
            "음... 사실 형석씨한테 석탄을 납품하기로 했거든요",
            "근데 누가 석탄을 다 캐가고 있는건지 잘 안보여서 막막하네요",
            "혹시 석탄 남으시는거 있으시면 하급 강화석과 바꿀 수 있을까요?"
        );

        createQuestChat(71L, QuestList.NEED_COAL,
            "오 감사합니다!",
            "일단... 석탄 100개 정도면 적당할 것 같아요",
            "다 구하시면 다시 와주세요!"
        );
    
        createCommonChat(null, 72L,
                "아... 별 수없죠",
                "저는 석탄이나 더 찾아봐야겠네요"
        );
    
        createCommonChat(null, 73L,
                "와 빨리 구해와주셨네요!",
                "덕분에 일이 훨씬 편해졌습니다",
                "여기 드리기로 한 하급 강화석이예요"
        );

        createAskingChat("기분 좋아 보이시는데요?", 74L, 75L, 76L,
            "아 오랜만이예요 __nickname 씨",
            "사실 그이가 선물을 줬거든요",
            "아 __nickname 씨는 모험가이시니까 다양한 재료를 구해올 수 있을 것 같은데...",
            "저도 보답으로 선물을 주고 싶어서요",
            "발광석이랑 물고기 몇마리정도면 될 것 같은데 구해와주실 수 있을까요?"
        );

        createQuestChat(75L, QuestList.ANOTHER_PRESENT,
            "와아 고마워요",
            "그럼 최대한 빨리 부탁할게요?"
        );
    
        createCommonChat(null, 76L, "흐응.... 알겠어요");
    
        createCommonChat(null, 77L,
                "음~ 이정도면 될 것 같네요 고마워요",
                "음... 답례로 드릴 수 있는건 없지만 제 힘을 좀 써드릴게요",
                "다음에도 종종 부탁드릴게요?"
        );

        createQuestChat("혹시 다른 할 일이 있을까 싶어 와봤습니다", 78L, QuestList.LV50.getId(),
            "허허 자네가 열심히 일을 해준 덕분에 지금은 딱히 없네",
            "아 그건 그렇고 마침 자네에게 할 말이 있었는데 잘 왔구만",
            "이번에 국가에서 모험가들을 장려한다고 일정 수준 이상의 모험가들에게 이 종이를 주라고 했네",
            "음.. 이름이 아마 스텟 머시깽이 였는데... 어쨌든...",
            "기준이 아마 50레벨 이었을테니 50레벨을 달성하면 와서 받아가게나"
        );

        chat = createChat(null, 79L,
                "역시 자네라면 금방 받아갈 줄 알았지 허허",
                "자 여기 유용하게 쓰게나"
        );
        chat.setResponseChat(WaitResponse.NONE, 80L);
        Config.unloadObject(chat);

        createQuestChat(80L, QuestList.LV100,
            "아아 하나 까먹은게 있네",
            "이번 지원은 50레벨로 끝나는게 아니라 100레벨에서도 받을 수 있다고 하네",
            "100레벨을 달성하면 와서 그것도 받아가게나"
        );

        createAskingChat("너답지 않게 고민이 있어 보이네?", 81L, 82L, 83L,
            "아아 마침 잘왔어 __nickname",
            "얼마 뒤에 제사가 있거든",
            "생선은 넉넉한데 평소에 돼지를 좋아하셔서 돼지 머리를 놓고 싶은데 어떻게 구해야할지 생각하고 있었어",
            "그래서 말인데 혹시 돼지머리 3개만 구해줄 수 있을까?"
        );

        createQuestChat(82L, QuestList.MEMORIAL_CEREMONY, "고마워. 대신 구해오면 괜찮은거 알려줄게");
    
        createCommonChat(null, 83L,
                "그래? 바쁜가보네",
                "혹시 나중에라도 생각 바뀌면 다시 찾아와",
                "하하.. 나야 하루종일 여기 있는거 알잖아?"
        );
    
        createCommonChat(null, 84L,
                "오.. 구해왔구나?",
                "자 이 책 받아가"
        );

        createAskingChat("수련은 할만 해?", 85L, 86L, 87L,
            "좀 힘들긴 해도 재밌어. 그리고 힘들어도 이겨내야겠지 헤헤",
            "아 맞다, 수련하는데에 필요한 아이템이 있는데 좀 구해줄 수 있을까?",
            "거미의 눈에 마법의 힘이 담겨있다고 해서 일단 5개 정도만 구해주면 될 것 같은데"
        );

        createQuestChat(86L, QuestList.MAGIC_OF_SPIDER_EYE,
            "아 다행이다",
            "그럼 다 구하고 다시 말걸어줘"
        );
    
        createCommonChat(null, 87L, "음 직접 구해야 할려나...?");
    
        createCommonChat(null, 88L,
                "와 고마워!",
                "앞으로도 종종 부탁해? 헤헤"
        );

        createAskingChat("으... 바닥에 이 끈적끈적한건 다 뭔가요?", 89L, 90L, 91L,
            "아.. 우리 봄이가 놀러 갔다가 실수로 슬라임 늪지 근처까지 갔지 뭐예요",
            "근데 요즘 슬라임이 많아져서 그런지 늪지 바깥까지 슬라임이 가끔 나오거든요",
            "그래서 겨우 도망쳐왔다고 하는데... 걱정이네요",
            "아 혹시 슬라임 20마리만 잡아주실 수 있을까요?",
            "슬라임 처리 증거로 슬라임 조각 20개면 충분할것 같은데..."
        );
        
        createQuestChat(90L, QuestList.STICKY_SLIME,
            "정말 고마워요!",
            "보수는 넉넉히 챙겨 드릴게요"
        );
    
        createCommonChat(null, 91L,
                "그래도 아이의 안전을 위한건데...",
                "할 일만 다 끝내면 다시 와주세요..."
        );
    
        createCommonChat(null, 92L,
                "덕분에 마음이 좀 놓이네요",
                "고마워요. 여기 약속한 보수예요"
        );

        createAskingChat("꽃들이 좀 시들해진 것 같네요. 무슨 문제라도 있나요?", 93L, 94L, 95L,
            "흐응... 요즘 날씨때문에 엘프의 기운으로도 꽃이 잘 안자라네요",
            "이럴때는 엘릭서 허브만 있으면 될텐데...",
            "혹시 약초를 좀 캐와주실 수 있을까요?",
            "약초 10개정도를 구해와주시면 제가 엘릭서 허브를 만들어서 하나 나눠드릴게요"
        );

        createQuestChat(94L, QuestList.HERB_COLLECTING, "그럼 빨리 구해와주세요~");
    
        createCommonChat(null, 95L, "아쉽네요, 엘릭서 허브에 관심이 있으실 줄 알았는데...");

        chat = createChat(null, 96L,
                "잠시만 기다려주세요?",
                "(숲의 기운이 강하게 느껴진다)",
                "여기 주기로 했던 엘릭서 허브예요"
        );
        chat.setPauseTime(4000L);
        Config.unloadObject(chat);
    
        createCommonChat(null, 97L, "힝...");

        for(int i = 0; i < Config.GEMS.length; i++) {
            long chatId = 98L + (i * 2);
            String gemName = Config.GEMS[i].getDisplayName();

            createAskingChat("보석 수집 - " + gemName, chatId, chatId + 1, 97L,
                "모험가님... 혹시 " + gemName + " 하나 구해줄 수 있어요?",
                "보석을 모으고 있는데 " + gemName + " (이/가) 필요해서요..."
            );
            createQuestChat(chatId + 1, QuestList.GEM_COLLECTING_QUARTZ.getId() + i, "감사합니다");
        }

        createQuestChat(128L, QuestList.HEALING_ELF1,
            "(엘프가 쓰러져있다)",
            "으으........",
            "(엘프의 치료법을 묻기 위해 " + Emoji.focus("엘") + " 에게 가보자)"
        );
        createQuestChat(129L, QuestList.HEALING_ELF2,
            "숲에 엘프가 쓰러져있다고요?",
            "이런... 저는 지금 봄이때문에 가기가 좀 그런데...",
            "일단 엘릭서 허브를 그 엘프에게 주면 어느정도 회복할거예요",
            "제가 가지고 있는 엘릭서 허브를 하나 드릴테니 가서 회복시켜주세요"
        );
    
        createCommonChat(null, 130L,
                "......",
                "감사합니다...",
                "도와주셔서 감사하지만 일단 어느정도 회복을 해야할 것 같아요",
                "나중에 와주세요"
        );
    
        createCommonChat("이 시간까지 낚시를 하고 있는거야?", 131L, "zzz......");
    
        createCommonChat(null, 132L,
                "역시 자네라면 이번에도 금방 받아갈 줄 알았지 허허",
                "자 여기 유용하게 쓰게나"
        );
    
        createCommonChat(null, 133L, "고마워요!");

        createAskingChat("혹시 일하시는데 필요한 재료가 있나요?", 134L, 135L, 136L,
            "재료라... 아 그래",
            "장비를 만들 때 가죽이 필요한데 앞으로 좀 구해주겠나?",
            "양 가죽 10개 정도면 적당할 것 같군"
        );
        
        createQuestChat(135L, QuestList.LEATHER_COLLECTING1,
            "하하 역시 시원시원할 줄 알았어",
            "그럼 기다릴테니 빨리 구해와라"
        );
    
        createCommonChat(null, 136L, "이런 아쉽구만");

        createAskingChat("이번에도 양가죽을 구해오면 될까요?", 137L, 138L, 136L,
            "하하 괜찮네. 덕분에 양가죽은 충분해졌어",
            "이번엔 소 가죽을 구해오는건 어떤가"
        );

        createQuestChat(138L, QuestList.LEATHER_COLLECTING2,
            "좋아 좋아. 덕분에 일이 편해지는군",
            "이번에도 10개 정도면 될거다"
        );

        createAskingChat("이제 가죽은 한동안 충분하겠네요", 139L, 140L, 136L,
            "일반적인 장비를 만들 가죽은 충분한 것 같군",
            "하지만 이번에는 도전을 해보고싶단 말이지",
            "혹시 오크 가죽도 구해다줄 수 있겠나?"
        );

        createQuestChat(140L, QuestList.LEATHER_COLLECTING3,
            "크으 오랜만에 몸 쓸 준비를 좀 해야겠구만",
            "기다리고 있을테니 5개 정도만 가져와"
        );
    
        createCommonChat(null, 141L,
                "이야 빨리 왔구만",
                "여기 보상이다"
        );

        createAskingChat("왜 이렇게 다치셨어요..?", 142L, 143L, 144L,
            "요즘 좀비가 너무 불어나서...",
            "지하 어딘가에 통로가 있는지 계속 튀어나오네요",
            "혹시 좀비를 좀 처리해주실 수 있을까요?"
        );
        
        createQuestChat(143L, QuestList.INCREASING_ZOMBIE, "감사합니다..! 꼭 보상해드릴게요");
    
        createCommonChat(null, 144L, "생각보다 훨씬 차가운 분이셨네요...");
    
        createCommonChat(null, 145L,
                "확실히 좀비 숫자가 줄어든 것 같네요!",
                "감사합니다. 여기 보상이예요"
        );
        
        createAskingChat("왜... 이렇게 화가 나셨나요?", 146L, 147L, 148L,
            "또 너로군",
            "이 망할 스켈레톤들이 계속 바닷속에서 죽는다",
            "덕분에 계속 죽은 물고기들만 낚이고 있지",
            "모험가라면 이 정도는 해결해 줄 수 있곘지?"
        );
        
        createQuestChat(147L, QuestList.BONE_IN_THE_SEA, "그래, 빨리가서 저 망할 뼈다귀들을 없애버리라고");
    
        createCommonChat(null, 148L,
                "....",
                "꺼져라"
        );
    
        createCommonChat(null, 149L, "이제 좀 제대로 된 물고기들이 낚이곘군");
        
        createAskingChat("불안해 보이시네요", 150L, 151L, 152L,
            "자네가 오기 며칠 전, 마을 주변에 싱크홀이 생겼었다네",
            "그런데 요즘들어 싱크홀에서 이상한 소리가 들린다는 신고가 많아져서 말이지...",
            "그래서 말인데 혹시 확인좀 해줄 수 있겠나?"
        );
        
        createQuestChat(151L, QuestList.SOUND_IN_THE_SINKHOLE,
            "아이고 고맙네",
            "싱크홀이 생각보다 깊으니 조심하게나"
        );

        createAskingChat(null, 152L, 151L, 152L,
            "아이고 이번 한번만 좀 부탁하겠네...",
            "어떻게 안되겠나?"
        );
        
        chat = createChat(null, 153L,
                "아니... 그런 곳에서 마물이 튀어나오다니...",
                "아무래도 이건 내 선에서 해결되지 않을 것 같구만",
                "일단 확인해줘서 고맙네"
        );
        chat.setNoneNpcId(NpcList.ABEL.getId());
        chat.setResponseChat(WaitResponse.NONE, 154L);
        Config.unloadObject(chat);

        chat = createChat(null, 154L,
                "드디어 여기까지 왔네",
                "이번에도 시간은 적으니까 잘 들어",
                "첫번째, 왕국에 소속된 인간들을 믿지 마",
                "두번째, 절대 왕국의 주요 인사들을 만나지 마",
                "세번째, 넌 그 때 죽지 않았어",
                "....."
        );
        chat.setDelayTime(1000L);
        Config.unloadObject(chat);
    
        createCommonChat(null, 155L,
                "흠.. 오랜만에 보는 방문자로군",
                "이곳의 엘프의 마을일세. 나는 이곳의 장로를 맡고 있는 엘우드라고 하네",
                "원래 외지인은 잘 들이지 않지만.. 셀리나를 도와줬으니 보답이라면 보답으로 치지",
                "그래 뭐... 혹시나 필요한 게 있으면 말하게"
        );
        
        createBaseChat(156L, "무슨 일이신가요?");
    
        createCommonChat("몸은 좀 회복 되셨나요?", 157L,
                "네 덕분에 완전히 회복된 것 같아요",
                "음... 제가 딱히 드릴건 없어서 일단 마을에 연락을 해 뒀는데...",
                "모험가라고 하시니 저희 마을에 한번쯤 방문해보세요"
        );
    
        createCommonChat(null, 158L,
                "장로님께 말씀은 들었다",
                "우리 동족을 도와준 것도 있으니 해를 가하지는 않겠지만...",
                "혹시라도 허튼 수작이 보이면 이 은색 검이 네 목을 벨것이다"
        );
    
        createCommonChat(null, 159L,
                "음... 장로님이 말하신 그 여행자분이신가 보네요",
                "뭐... 그렇게 나쁜분 같지는 않네요",
                "잘 부탁드려요"
        );
    
        createCommonChat(null, 160L,
                "와! 모험가는 처음보네요",
                "뭐... 마을에선 크게 환영받지 못해도 저랑은 친하게 지내자구요"
        );
    
        createCommonChat(null, 161L,
                ".....",
                "네가 여기서 하는 모든 행동이 네 목숨을 앗아갈 수도 있다는 것을 명심해라",
                "언제나 그림자속에서 도를 겨누고 있을테니"
        );
        
        createBaseChat(162L, "용건만 빠르게 전해라");
    
        createCommonChat("왜 당신들은 저를 경계하는 건가요?", 163L,
                "흠... 풋내기 모험가였군",
                "인간들은 우리를 내쫓았다",
                "그런 너희 인간들을 우리가 기꺼워 할거라 생각하나?"
        );
        
        createBaseChat(164L, "그래 무슨 일이지 모험가");
    
        createCommonChat("당신 이름의 뜻을 알려줄 수 있나요?", 165L,
                "우리가 마지막으로 싸웠던 적의 종이 흡혈귀였다",
                "그 때 나는 지휘관이었고, 흡혈귀와 싸우기로 맹세한 순간 내 이름을 버렸다",
                "그리고 그들을 죽이기 위한 이 이름을 정헀다"
        );

        createBaseChat(166L, "무슨 일로 오셨나요?");
    
        createCommonChat("아 그냥 지나가다 들렀습니다", 167L, "네네 안녕히 가세요");
        
        createBaseChat(168L, "음 무슨 일이세요?");
    
        createCommonChat("엘프들은 과일만 먹고 사는건가요?", 169L,
                "음 꼭 그런건 아니예요",
                "하지만 자연이 저희의 일부인 만큼 최대한 검소하게 사는거라고 보시면 될 것 같네요"
        );

        createBaseChat(170L, "안녕하세요! 뭐 궁금한거 있으세요?");
    
        createCommonChat("혹시 나이가 어떻게 되니?", 171L,
                "음... 올해로 딱 100살이예요!",
                "아아 그렇다고 갑자기 존댓말 쓰지는 말구요...",
                "엘프 기준으로는 어린거라구요"
        );
    
        createCommonChat("남자 엘프들은 다 어디갔어?", 172L,
                "네?? 아.. 인간 기준으로는 그럴 수도 있곘네요",
                "순수 엘프는 여자밖에 없어요",
                "새로운 생명은 자연에서 태어나구요"
        );
    
        createCommonChat("혹시 엘 이라는 엘프 아니?", 173L,
                "음... 잘 모르겠긴 한데...",
                "엘 이라는 이름은 은퇴한 장로들이 쓰는 이름이예요"
        );
    
        createCommonChat("엘프 장로를 뽑는 기준이 뭐야?", 174L,
                "장로님들은 2000년 마다 바뀌어요",
                "그리고 바뀔 때 가장 나이가 많은 다른 엘프가 장로직을 맡게 되죠"
        );
    
        createCommonChat("엘프들은 자연속에서만 생활하는거야?", 175L,
                "기본적으로는 그렇긴 해요",
                "근데 장로직을 완수하고 나면 자유가 주어져요",
                "대신 숲 밖으로 나가면 수명이 50년만 남게 되지만요"
        );

        createBaseChat(176L, "찾아온 이유가 뭐지");
    
        createCommonChat("아하하... 그냥 지나가다 들른건데... 이 칼좀 치워주실래요", 177L,
                "시덥잖은 농담할 시간 없다",
                "빨리 이 마을을 나가라"
        );
        
        createBaseChat(178L, "왜 왔지");
    
        createCommonChat("엘프에 암살자라..?", 179L, "호기심이 고양이를 죽이기도 한다더군");

        createAskingChat("또 뭐가 필요하셔서 똥 싸다 만 얼굴을 하고 계신가요", 180L, 181L, 182L,
            "똥 싸다 만 얼굴이라니... 에잉...",
            "그.. 들려오는 소문에 악마의 뿔을 달여 먹으면 그렇게 좋다던데...",
            "거 한 10개 정도만 좀 구해줄 수 있겠나?"
        );
        createQuestChat(181L, QuestList.HEALTHY_DEVIL_HORN, "크흠크흠... 고맙네");
        createCommonChat(null, 182L, "에잉 자네도 이 나이가 되면 이해할걸세...");
        createCommonChat(null, 183L,
            "이것만 다려먹으면..!!",
            "커험.. 고맙네 거 이만 가게나"
        );
        
        createAskingChat("혹시 도울만한 일이 있을까요?", 184L, 185L, 186L,
            "흠... 네놈이 못미덥긴 하지만 일단 지금은 일이 급하니 맡겨보지",
            "최근 임프가 우거진 숲까지도 나타난다는 소문이 있다",
            "주 서식지와는 엄청나게 떨어져있는데도 말이지",
            "일단 임프의 뿔을 세 개 구해와라"
        );
        createQuestChat(185L, QuestList.WORK_TO_KILL_DEVIL1, "빨리 구해오도록");
        createCommonChat(null, 186L, "역시 네놈한테 기대를 한 내가 바보로군");
        createCommonChat(null, 187L, "일단 이 뿔을 좀 분석해야겠군...");
        
        createAskingChat("뿔 분석은 다 됬나요?", 188L, 189L, 190L,
            "그래 마침 다 됬군",
            "아무래도 임프를 소환하는 숙주가 있는 것 같다",
            "일단 개체수 조절부터 해보지",
            "임프의 심장 두 개를 구해오도록"
        );
        createQuestChat(189L, QuestList.WORK_TO_KILL_DEVIL2, "빨리 구해오도록");
        createCommonChat(null, 190L, "하... 이래서 인간이란... " + Emoji.CARROT);
        createCommonChat(null, 191L, "흠... 역시 그년인가...");
        
        createAskingChat("뭐가 원인인건가요?", 192L, 193L, 194L,
            "아무래도 서큐버스가 그 원인인 것 같다",
            "서큐버스정도는 잡을 수 있겠지?",
            "임프의 심장 10개를 구하고 임프의 뿔을 사용하면 서큐버스가 나타날거다",
            "서큐버스의 영혼 한 개를 구해와라"
        );
        createQuestChat(193L, QuestList.WORK_TO_KILL_DEVIL3, "빨리 구해오도록");
        createCommonChat(null, 194L, "역시 네놈 실력에 서큐버스까지 바란건 무리였나... 어쩔 수 없군");
        createCommonChat(null, 195L, "다시 봤군");
    
        chat = createChat("저기 찌가 흔들리는데..!", 196L,
            "잠깐만... 낚시보다 중요한게 있어서...",
            "아! 마침 잘 왔어",
            "몇가지 부탁을 좀 들어줄 수 있을까?"
        );
        chat.setAnyResponseChat("부탁?", 197L);
        chat.setResponseChat(WaitResponse.YES, 198L);
        chat.setResponseChat(WaitResponse.NO, 199L);
        Config.unloadObject(chat);
        
        createAskingChat(null, 197L, 198L, 199L,
            "아니 뭐... 급하게 구할게 있는데 나한텐 좀 버거워서",
            "그래서 안될까?"
        );
        createQuestChat(198L, QuestList.SECRET_REQUEST1,
            "고마워 역시 믿을만하네",
            "일단, 마정석 30개 정도면 될 것 같아"
        );
        createCommonChat(null, 199L, "음... 나중에 혹여나 마음이 바뀌면 다시 찾아와줘");
        
        createQuestChat(200L, QuestList.SECRET_REQUEST2,
            "빨리 구해왔네",
            "보자... 이번엔 하피의 날개랑 손톱 각각 5개씩을 구해와줘"
        );
    
        createQuestChat(201L, QuestList.SECRET_REQUEST3,
            "음... 이번엔 골렘 코어가 3개정도 필요할 것 같아"
        );
        
        createQuestChat(202L, QuestList.SECRET_REQUEST4,
            "드디어 마지막이네",
            "마지막으로 마법 파편을 15개 구해와줘"
        );
        
        createQuestChat(203L, QuestList.SECRET_REQUEST5,
            "고마워! 이제 전부 다 구해졌네",
            "응? 어디에 쓸거냐고?",
            "어... 나중에 다 완성하면 알려줄게",
            "구해와줘서 고마워",
            "아 맞다, 내가 이걸 구해달라고 한건 아무한테도 말하지 말아줘"
        );
        
        chat = createChat(null, 204L,
            "마침 궁금한 것이 있었는데 잘됬군",
            "네가 계속 이상한 장소를 뛰어다닌다는 소문이 돌아서 말이지",
            "흠... 네놈이 원인인 것 같지는 않은데...",
            "혹시 최근 이상한 의뢰를 받은 적이 있나?"
        );
        chat.setAnyResponseChat("왜 묻는거죠?", 205L);
        chat.setResponseChat(WaitResponse.YES, 207L);
        chat.setResponseChat(WaitResponse.NO, 206L);
        Config.unloadObject(chat);
        
        createAskingChat(null, 205L, 207L, 206L,
            "이 마을에 악마가 날뛰는 광경을 보기 싫다면 말해야할거다",
            "마지막으로 다시 한번 묻지",
            "최근 이상한 의뢰를 받은 적이 있나?",
            "신중히 답하는게 좋을거야"
        );
        
        chat = createChat(null, 206L,
            "흠... 넌 큰 실수를 하고 있는거야"
        );
        chat.setItem(ItemList.TRUST_OF_JOON_SIK.getId(), 1);
        Config.unloadObject(chat);
        
        chat = createChat(null, 207L,
            "의뢰라...",
            "그 의뢰를 맡긴 사람이 누구지?"
        );
        chat.setAnyResponseChat("...", 208L);
        chat.setAnyResponseChat("준식", 209L);
        Config.unloadObject(chat);
        
        chat = createChat(null, 208L,
            "이미 말한거 쉽게쉽게 가자고",
            "그 의뢰를 맡긴 사람이 누구지?"
        );
        chat.setAnyResponseChat("준식", 209L);
        Config.unloadObject(chat);
        
        chat = createChat(null, 209L,
            "역시 그런건가...",
            "많은 도움이 됬군. 고맙다"
        );
        chat.setItem(ItemList.TRUST_OF_TAE_GONG.getId(), 1);
        Config.unloadObject(chat);

        Logger.i("ObjectMaker", "Chat making is done!");
    }

}
