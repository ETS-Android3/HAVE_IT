package com.example.have_it;

/**
 * A class extending on {@link GeneralUser}, with extra requested and following state
 */
public class NewFollowUser extends GeneralUser{
    /**
     * The requested state, true for having sent the request, of class {@link boolean}
     */
    private boolean requested;
    /**
     * The following state, true for now following, of class {@link boolean}
     */
    private boolean following;

    /**
     * The constructor of NewFollowUser
     * @param UID The UID, of class {@link String}, used by super constructor
     * @param name The name, of class {@link String}, used by super constructor
     * @param requested The requested state, true for having sent the request, of class {@link boolean}
     * @param following The following state, true for now following, of class {@link boolean}
     */
    public NewFollowUser(String UID, String name, boolean requested, boolean following) {
        super(UID, name);
        this.following = following;
        this.requested = requested;
    }

    /**
     * Getter for requested state
     * @return {@link boolean}, requested state to get
     */
    public boolean isRequested() {
        return requested;
    }

    /**
     * Setter for requested state
     * @param requested {@link boolean}, requested state to set
     */
    public void setRequested(boolean requested) {
        this.requested = requested;
    }

    /**
     * Getter for following state
     * @return {@link boolean}, following state to get
     */
    public boolean isFollowing() {
        return following;
    }

    /**
     * Setter for following state
     * @param following {@link boolean}, following state to set
     */
    public void setFollowing(boolean following) {
        this.following = following;
    }
}
