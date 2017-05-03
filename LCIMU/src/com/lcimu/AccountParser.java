package com.lcimu;

import org.dom4j.Document;               // the Document class is used to create a document
import org.dom4j.DocumentHelper;         // the DocumentHelper class is used to create a document from scratch
import org.dom4j.Element;                // the Element class is used to add elements to the document created
import org.dom4j.io.SAXReader;           // the SAXReader class is used to read documents in a file
import org.dom4j.io.XMLWriter;           // the XMLWriter class is used to write documents into a file

import java.io.File;                     // the File class is used to create a file in java
import java.io.FileWriter;               // the FileWriter class helps to write into a file

/**
 * The AccountParser class is used to
 * create, store and read account information from an xml format file
 */
public class AccountParser {
    private Document document;
    private XMLWriter writer;
    private String accountFileName;
    private Account currentAccount = null;
    private File profileDir;

    /**
     * This AccountParser's constructor is used to Create the User Account Validation xml file
     * This file will be used to hold a record of username and passwords
     * If the accounts.xml file doesn't exist,create it,or load the file
     */
    public AccountParser() {
        profileDir = new File(System.getProperty("user.dir") + File.separator + "profile");
        try {
            if (!profileDir.exists()) {
                profileDir.mkdir();
            }
            accountFileName = profileDir.getPath() + File.separator + "accounts.xml";

            File accountFile = new File(accountFileName);

            if (accountFile.exists()) {
                SAXReader saxReader = new SAXReader();
                document = saxReader.read(accountFile);
                System.out.println(document.asXML());

            } else {
                document = DocumentHelper.createDocument();
                document.addElement("accounts");
            }

            writer = new XMLWriter(new FileWriter(accountFileName));
            writer.write(document);
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * The main static method
     */
    public static void main(String[] args) {
        new AccountParser();

    }

    /*
     * A Get method that returns the document object
     */
    public Document getDocument() {
        return document;
    }

    /*
     *This boolean method is used to validate the Login account
     */
    public boolean signIn(String userName, String pwd) {
        Element ele = isUserExist(userName);
        if (ele != null) {
            String pwdVal = ele.attributeValue("password");
            if (pwd.equals(pwdVal)) {
                currentAccount = new Account(userName, pwd);
                return true;
            } else {  // if a username doesn't exist
                return false;
            }
        } else {
            return false;
        }
    }

    /*Check if the account exists.
     *If it doesn't,write the new account information to the accounts.xml file
     */
    public boolean signUp(String userName, String pwd) {
        Element ele = isUserExist(userName);
        if (ele == null) {//The Username does not exist
            Element root = document.getRootElement();
            Element newEle = root.addElement("account");
            newEle.addAttribute("name", userName);
            newEle.addAttribute("password", pwd);
            try {
                //Write the new account to a File
                writer = new XMLWriter(new FileWriter(accountFileName));
                writer.write(document);
                writer.flush();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return true;
        } else {
            //username exist
            return false;
        }
    }

     /*
      * This method is used to check if a username exist on file;
      */

    /**
     * Get the currently logged on account
     *
     * @return currentAccount
     */
    public Account getCurrentUser() {
        return currentAccount;
    }

    public Element isUserExist(String userName) {
        Element root = document.getRootElement();
        Element newEle = null;
        for (Element ele : root.elements()) {
            if (userName.equals(ele.attributeValue("name"))) {
                newEle = ele;
                return newEle;
            }
        }
        return null;
    }

    //close the xml writer
    public void destroy() {
        try {
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    /**
     * Get the user's unique profile directory
     *
     * @return curprofileDir the User's profile directory
     */
    public File getUserProfileDir() {
        String userName = getCurrentUser().getName();
        String CurrentProfileDiR = profileDir.getPath() + File.separator + userName;
        File curProfileDir = new File(CurrentProfileDiR);
        if (!curProfileDir.exists()) {
            curProfileDir.mkdir();
        }

        return curProfileDir;
    }
}
