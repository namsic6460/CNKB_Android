package lkd.namsic.Game.Class;

import java.io.Serializable;

import lkd.namsic.Game.Base.IdClass;

public interface GameObject extends Serializable, Cloneable {

    IdClass id = new IdClass();

}