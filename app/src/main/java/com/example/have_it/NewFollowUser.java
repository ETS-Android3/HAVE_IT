package com.example.have_it;

public class NewFollowUser extends GeneralUser{
    private boolean requested;
    private boolean following;

    public NewFollowUser(String UID, String name, boolean requested, boolean following) {
        super(UID, name);
        this.following = following;
        this.requested = requested;
    }

    public boolean isRequested() {
        return requested;
    }

    public void setRequested(boolean requested) {
        this.requested = requested;
    }

    public boolean isFollowing() {
        return following;
    }

    public void setFollowing(boolean following) {
        this.following = following;
    }
}
