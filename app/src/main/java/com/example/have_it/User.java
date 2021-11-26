package com.example.have_it;

/**
 * User represent the UID information for each users.
 * This is the class using Singleton to share the current logged in user information
 * @author Jianbang Chen,Yuling Shen
 * @see UserRegisterActivity
 * @version 1.2
 */
public class User {
    /**
     *The User ID, generated when registered, of class {@link String}
     */
    private String UID;
    /**
     *An instance to finalize this to be the only user among this app, of class {@link User}
     */
    private static final User instance = new User();

    /**
     *This is the constructor of {@link User}, private as others should not invoke this
     */
    private User(){
        UID = "";
    }

    /**
     *This is for getting the instance of user
     * @return {@link User} the current user
     */
    public static User getInstance(){
        return instance;
    }

    /**
     *This is getter for UID
     * @return {@link String}, get {@link User#UID}
     */
    public String getUID() {
        return UID;
    }

    /**
     *This is setter for UID
     * @param UID {@link String}, set {@link User#UID}
     */
    public void setUID(String UID) {
        this.UID = UID;
    }
}
