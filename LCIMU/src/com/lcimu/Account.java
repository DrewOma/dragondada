package com.lcimu;

/**
 * The User Account Class
 */
public class Account {
    private String name;   //  Declare a String variable to represent the User's Name
    private String password; //  Declare a variable to represent the User's Password

    public Account(String name, String password) { //Create the Account constructor and pass two parameters
        this.name = name;                          // Initialize the variable name
        this.password = password;                  // Initialize the variable password
    }

    /*
       A Get method that returns the variable name
     */
    public String getName() {
        return name;
    }

    /*
       A Set method that sets the Username
     */
    public void setName(String name) {
        this.name = name;
    }

    /*
       A Get method that returns the variable password
     */
    public String getPassword() {
        return password;
    }

    /*
       A Set method that sets the User's passwords
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
