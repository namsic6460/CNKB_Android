package lkd.namsic.game.command;

import androidx.annotation.NonNull;

public interface CommandListener {

    void execute(@NonNull Object...params);

}
