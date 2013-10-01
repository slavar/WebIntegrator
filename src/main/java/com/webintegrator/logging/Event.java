/*
 *Java Bean , holds log events properties
 */

package com.webintegrator.logging;

/**
 *
 * @author slava
 */
public class Event {

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getReplicationName() {
        return replicationName;
    }

    public void setReplicationName(String replicationName) {
        this.replicationName = replicationName;
    }
    private String priority = null;
    private String replicationName = null;
    private String message = null;
}
