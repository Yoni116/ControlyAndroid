package net.controly.controly.model;

import com.google.gson.annotations.SerializedName;

import net.controly.controly.util.EventCreationUtils;

import java.io.Serializable;

/**
 * This class represents a trigger for an event.
 */
public class Trigger implements BoxListItem, Serializable {

    @SerializedName("TriggerID")
    private final int triggerId;

    @SerializedName("TriggerType")
    private final String triggerType;

    public Trigger(int triggerId, String triggerType) {
        this.triggerId = triggerId;
        this.triggerType = triggerType;
    }

    public int getTriggerId() {
        return triggerId;
    }

    public String getTriggerTitle() {
        return triggerType;
    }

    @Override
    public String getTitle() {
        return triggerType;
    }

    public EventCreationUtils.EventType getTriggerType() {
        if (triggerType.contains("location")) {
            return EventCreationUtils.EventType.LOCATION;
        }

        return EventCreationUtils.EventType.TIME;
    }
}
