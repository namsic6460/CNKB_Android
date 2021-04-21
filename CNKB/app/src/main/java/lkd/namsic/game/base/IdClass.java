package lkd.namsic.game.base;

import java.io.Serializable;

import lkd.namsic.game.enums.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IdClass implements Serializable {

    Id id = null;
    long objectId = 0;

    public IdClass() {}

}
