package net.controly.controly.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * This class represents an event which triggers an automation.
 */
public class Event implements BoxListItem {

    @SerializedName("EventID")
    private long id;

    @SerializedName("UserID")
    private long userId;

    @SerializedName("Name")
    private String name;

    @SerializedName("CommandID")
    private String commandId;

    @SerializedName("TriggerInfo")
    private String triggerInfo;

    @SerializedName("TriggerID")
    private long triggerId;

    @SerializedName("CreatedAt")
    private Date creationDate;

    public Event() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCommandId() {
        return commandId;
    }

    public void setCommandId(String commandId) {
        this.commandId = commandId;
    }

    public String getTriggerInfo() {
        return triggerInfo;
    }

    public void setTriggerInfo(String triggerInfo) {
        this.triggerInfo = triggerInfo;
    }

    public long getTriggerId() {
        return triggerId;
    }

    public void setTriggerId(long triggerId) {
        this.triggerId = triggerId;
    }

    @Override
    public String getTitle() {
        return name;
    }
}
