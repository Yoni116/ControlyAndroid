package net.controly.controly.model;

import com.google.gson.annotations.SerializedName;

/**
 * This class represents a trigger for an event.
 */
public class Trigger implements BoxListItem {

    @SerializedName("TriggerID")
    private int triggerId;

    @SerializedName("TriggerType")
    private String triggerType;

    public Trigger(int triggerId, String triggerType) {
        this.triggerId = triggerId;
        this.triggerType = triggerType;
    }

    public int getTriggerId() {
        return triggerId;
    }

    public String getTriggerType() {
        return triggerType;
    }

    @Override
    public String getTitle() {
        return triggerType;
    }
}
