package com.example.have_it;

public class RequestedUser extends GeneralUser{
    private boolean allowed;
    private boolean replied;

    public RequestedUser(String UID, String name, boolean allowed, boolean replied) {
        super(UID, name);
        this.allowed = allowed;
        this.replied = replied;
    }

    public boolean isAllowed() {
        return allowed;
    }

    public void setAllowed(boolean allowed) {
        this.allowed = allowed;
    }

    public boolean isReplied() {
        return replied;
    }

    public void setReplied(boolean replied) {
        this.replied = replied;
    }
}
