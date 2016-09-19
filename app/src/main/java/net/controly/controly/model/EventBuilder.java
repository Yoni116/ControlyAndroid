package net.controly.controly.model;

import java.io.Serializable;

/**
 * Created by Itai on 16-Sep-16.
 */
public final class EventBuilder implements Serializable {
    private long id;
    private long userId;
    private long triggerId;
    private String name;
    private String commandId;
    private String triggerInfo;

    private EventBuilder() {
    }

    public static EventBuilder getInstance() {
        return new EventBuilder();
    }

    public EventBuilder id(long id) {
        this.id = id;
        return this;
    }

    public EventBuilder userId(long userId) {
        this.userId = userId;
        return this;
    }

    public EventBuilder name(String name) {
        this.name = name;
        return this;
    }

    public EventBuilder commandId(String commandId) {
        this.commandId = commandId;
        return this;
    }

    public EventBuilder triggerInfo(String triggerInfo) {
        this.triggerInfo = triggerInfo;
        return this;
    }

    public EventBuilder triggerId(long triggerId) {
        this.triggerId = triggerId;
        return this;
    }

    public Event build() {
        Event event = new Event();
        event.setId(id);
        event.setUserId(userId);
        event.setName(name);
        event.setCommandId(commandId);
        event.setTriggerInfo(triggerInfo);
        event.setTriggerId(triggerId);
        return event;
    }
}
