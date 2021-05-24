package com.gaadi.neon.events;

/**
 * Created by lakshaygirdhar on 4/12/15.
 */
public class GeneralEvent {

    protected String eventType;
    protected boolean success;

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
