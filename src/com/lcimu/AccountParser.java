package com.lcimu;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.File;
import java.io.FileWriter;

/**
 * Created by drewo on 2017/3/20.
 * In the absence of DataBase verification, this class helps parse the user account information to an xml file
 * It stores and reads account information from an xml format file
 */
public class AccountParser {
    private Document document;   // create a Document object variable
    private XMLWriter writer;    // create an XMLwriter object variable
    private String filePath;     // declare a String variable to represent the file path

    public AccountParser() {
        //if accounts.xml dosen't exist,create it,or load the file
        filePath = getClass().getResource(".").getPath() + File.separator + "accounts.xml";
        File accountFile = new File(filePath);  // accountFile represents a File object variable
        try {
            if (accountFile.exists()) {  // an If statement to check if the File object inputted exists
                SAXReader saxReader = new SAXReader();
                    document = saxReader.read(accountFile);
                System.out.println(document.asXML());// The Document object variable is parsed as an XML document

            } else {
                document = DocumentHelper.createDocument(); // If the account does not exist create it
                document.addElement("accounts");  // Add the newly created account to the Document object
            }

            //Write the newly created account to the file
            writer = new XMLWriter(new FileWriter(filePath));
            writer.write(document);
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Document getDocument() {  // This method retrieves the Document object variable
        return document;
    }

    //Validate the login account information
    public boolean signIn(String userName, String pwd) {
        Element ele = isUserExist(userName);
        if (ele != null) {
            String pwdVal = ele.attributeValue("password");
            //if the password matches
            if (pwd.equals(pwdVal)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    //Validate if there exist a same account of that name ,if not,write the account information to the accounts.xml file
    public boolean signUp(String userName, String pwd) {
        Element ele = isUserExist(userName);
        if (ele == null) {
            Element root = document.getRootElement();
            Element newEle = root.addElement("account");
            newEle.addAttribute("name", userName);
            newEle.addAttribute("password", pwd);
            try {
                writer=new XMLWriter(new FileWriter(filePath));
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

    //Validate if there is a user that has the same username as that inputted.
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

    public static void main(String[] args) {  // a static initialized instance of the AccountParser class
        new AccountParser();

    }
}
