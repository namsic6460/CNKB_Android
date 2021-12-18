package lkd.namsic.game.command.player.game;

import android.app.Notification;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import lkd.namsic.game.command.PlayerCommand;
import lkd.namsic.game.enums.Doing;
import lkd.namsic.game.enums.Variable;
import lkd.namsic.game.exception.DoingFilterException;
import lkd.namsic.game.exception.WeirdCommandException;
import lkd.namsic.game.object.Player;
import lkd.namsic.game.manager.FishManager;

public class FishCommand extends PlayerCommand {
    
    @Override
    public void executeCommand(@NonNull final Player player, @NonNull String command, @NonNull List<String> commands,
                               @Nullable String second, @Nullable String third, @Nullable String fourth,
                               @NonNull Notification.Action session) {
        if(player.getDoing().equals(Doing.NONE)) {
            if(second == null) {
                FishManager.getInstance().tryFish(player, player.getVariable(Variable.FISH));
            } else {
                int fishLv = player.getVariable(Variable.FISH);
                int inputLv = Integer.parseInt(second);
                
                if(inputLv < 0 || inputLv > fishLv) {
                    throw new WeirdCommandException("낚시 레벨은 0 이상, " + fishLv + "(현재 레벨) 이하여야 합니다");
                }
                
                FishManager.getInstance().tryFish(player, inputLv);
            }
        } else if(player.getDoing().equals(Doing.FISH)) {
            if(second == null) {
                throw new WeirdCommandException();
            }
            
            FishManager.getInstance().fishCommand(player, second);
        } else {
            throw new DoingFilterException(player.getDoing());
        }
    }
    
}
