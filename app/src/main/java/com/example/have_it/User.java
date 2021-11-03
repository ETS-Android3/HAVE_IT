package com.example.have_it;

/**
 * User represent the UID information for each users.
 * @author Jianbang Chen,Yuling Shen
 * @see User_RegisterActivity
 * @version 1.2
 */
public class User {
    /**
     *
     */
    private String UID;
    /**
     *
     */
    private static final User instance = new User();

    /**
     *
     */
    private User(){
        UID = "";
    }

    /**
     *
     * @return
     */
    public static User getInstance(){
        return instance;
    }

    /**
     *
     * @return
     */
    public String getUID() {
        return UID;
    }

    /**
     *
     * @param UID
     */
    public void setUID(String UID) {
        this.UID = UID;
    }
}
