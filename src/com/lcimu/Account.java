package com.lcimu;

/**
 * Created by drewo on 2017/3/20.
 * This class represents the structure of the user account
 */
public class Account {
    private String name;   // a Variable to hold the name of the user(s)
    private String password; // a Variable to hold the password of the user(s)

    public Account(String name, String password) {
        this.name = name;  // The name variable is assigned in this constructor
        this.password = password; // The password variable is assigned in this constructor
    }

    public String getName() {
        return name; // this method returns the name variable
    }

    public void setName(String name) {
        this.name = name;// this method sets the name variable
    }

    public String getPassword() {
        return password;// this method returns the password variable
    }

    public void setPassword(String password) {
        this.password = password;// this method sets the password variable
    }
}
