package com.example.have_it;

/**
 * A class extending on {@link GeneralUser}, with extra allowed and replied state
 */
public class RequestedUser extends GeneralUser{
    /**
     * The allowed state, true for having been allowed to follow, of class {@link boolean}
     */
    private boolean allowed;
    /**
     * The replied state, true for having replied, of class {@link boolean}
     */
    private boolean replied;

    /**
     * The constructor of NewFollowUser
     * @param UID The UID, of class {@link String}, used by super constructor
     * @param name The name, of class {@link String}, used by super constructor
     * @param allowed The allowed state, true for having been allowed to follow, of class {@link boolean}
     * @param replied The replied state, true for having replied, of class {@link boolean}
     */
    public RequestedUser(String UID, String name, boolean allowed, boolean replied) {
        super(UID, name);
        this.allowed = allowed;
        this.replied = replied;
    }

    /**
     * Getter for allowed state
     * @return {@link boolean}, allowed state to get
     */
    public boolean isAllowed() {
        return allowed;
    }

    /**
     * Setter for allowed state
     * @param allowed {@link boolean}, allowed state to set
     */
    public void setAllowed(boolean allowed) {
        this.allowed = allowed;
    }

    /**
     * Getter for replied state
     * @return {@link boolean}, replied state to get
     */
    public boolean isReplied() {
        return replied;
    }

    /**
     * Setter for replied state
     * @param replied {@link boolean}, replied state to set
     */
    public void setReplied(boolean replied) {
        this.replied = replied;
    }
}
