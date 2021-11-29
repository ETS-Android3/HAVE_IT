package com.example.have_it;

/**
 * The class representing another user than the current logged user
 */
public class GeneralUser {
    /**
     * The UID, of class {@link String}
     */
    private String UID;
    /**
     * The name, of class {@link String}
     */
    private String name;

    /**
     * The constructor of GeneralUser
     * @param UID The UID, of class {@link String}
     * @param name The name, of class {@link String}
     */
    public GeneralUser(String UID, String name) {
        this.UID = UID;
        this.name = name;
    }

    /**
     * Getter for UID
     * @return {@link String}, UID to get
     */
    public String getUID() {
        return UID;
    }

    /**
     * Setter for UID
     * @param UID {@link String}, UID to set
     */
    public void setUID(String UID) {
        this.UID = UID;
    }

    /**
     * Getter for name
     * @return {@link String}, name to get
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for name
     * @param name {@link String}, name to set
     */
    public void setName(String name) {
        this.name = name;
    }
}
