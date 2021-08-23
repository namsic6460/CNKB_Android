package lkd.namsic.game.object;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Map;

import lkd.namsic.game.base.SkillUse;
import lkd.namsic.game.enums.Id;
import lkd.namsic.game.enums.object.SkillList;
import lkd.namsic.game.event.Event;
import lkd.namsic.game.object.implement.EntityEvents;
import lkd.namsic.game.object.implement.SkillUses;
import lombok.Getter;

@Getter
public class Skill extends NamedObject {

    @Nullable
    final String activeDes;

    @Nullable
    final String passiveDes;

    public Skill(@NonNull SkillList skillData, @Nullable String activeDes, @Nullable String passiveDes) {
        super(skillData.getDisplayName());

        this.id.setId(Id.SKILL);
        this.id.setObjectId(skillData.getId());

        this.activeDes = activeDes;
        this.passiveDes = passiveDes;
    }

    @Nullable
    public SkillUse getSkillUse() {
        return SkillUses.MAP.get(this.id.getObjectId());
    }

    @Nullable
    public Map<String, Event> getEvent() {
        return EntityEvents.getEvent(this.id.getObjectId());
    }

}
