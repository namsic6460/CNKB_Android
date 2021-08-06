package lkd.namsic.game.enums;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lkd.namsic.game.exception.WeirdCommandException;

public enum ShopWaitType {

    END("종료", "end", "e"),
    CHANGE("변경", "change", "c"),
    NEXT("다음", "next", "n"),
    PREV("이전", "prev", "p"),
    BUY("구매", "buy", "b"),
    SELL("판매", "sell", "s"),
    PAGE;

    private final List<String> texts;

    ShopWaitType() {
        this.texts = new ArrayList<>(0);
    }

    ShopWaitType(@NonNull String...texts) {
        this.texts = Arrays.asList(texts);
    }

    @NonNull
    public static ShopWaitType parseWaitType(@NonNull String command) throws WeirdCommandException {
        try {
            Integer.parseInt(command);
            return PAGE;
        } catch (NumberFormatException e) {
            for(ShopWaitType waitType : ShopWaitType.values()) {
                if(waitType.equals(PAGE)) {
                    continue;
                }

                if(waitType.texts.contains(command)) {
                    return waitType;
                }
            }
        }

        throw new WeirdCommandException();
    }

}
