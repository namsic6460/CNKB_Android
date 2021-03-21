package lkd.namsic.Game.Exception;

import androidx.annotation.NonNull;

import java.util.List;

import lkd.namsic.Game.Config;

public class ListAdderException extends RuntimeException {

    public ListAdderException(@NonNull List<?> originalList, @NonNull List<?> settingList, @NonNull Throwable throwable) {
        super(Config.listsToString(originalList, settingList) + "\n" + Config.errorString(throwable));
    }

}
