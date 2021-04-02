package lkd.namsic.Game.GameObject;

import androidx.annotation.NonNull;

import lkd.namsic.Game.Enum.Id;
import lombok.ToString;

@ToString
public class Npc extends AiEntity {

    public Npc(@NonNull String name) {
        super(name);

        this.id.setId(Id.NPC);
    }

}
