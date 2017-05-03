package com.lcimu;

import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;
import org.jb2011.lnf.beautyeye.ch3_button.BEButtonUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * The LCIMU application will be ran from the MainWindow class
 */
public class MainWindow {
    private static JFrame frame;
    private static int singTag = 0;//signin=0,signUp=1
    private static AccountParser accountParser;
    private JPanel outPanel;
    private JPanel framePanel;
    private JTextField userNameField;
    private JPasswordField passwordField;
    private JButton signInButton;
    private JButton cancelButton;
    private JLabel ecgIMUUserSignLabel;
    private JPanel labelPanel;
    private JLabel userNameLabel;
    private JLabel passwordLabel;
    private JPanel registerPanel;
    private JButton signUpButton;
    private Font labelFont, buttonFont;
    private int userNameConst = 0;//is the textfield's text content empty?
    private int pwdConst = 0;

    /*
     * This constructor would manage, the user  sign in and sign up actions
     */
    public MainWindow() {
        signInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userName = userNameField.getText();
                char[] pwd = passwordField.getPassword();
                System.out.println("userName:" + userName + "   password:" + new String(pwd));
                //Decide if the username or password equals null or empty
                if (userName == null || "".equals(userName.trim())) { //if the user fails to input a username
                    userNameField.setText("Input a username");
                    userNameField.setFont(new Font("Courier New", Font.BOLD | Font.ITALIC, 12));
                    userNameField.setForeground(Color.blue);
                    userNameConst = 1;
                    return;
                } else if (pwd == null || pwd.length == 0) {  //if the user fails to input a password
                    passwordField.setEchoChar((char) 0);
                    passwordField.setFont(new Font("Courier New", Font.BOLD | Font.ITALIC, 12));
                    passwordField.setForeground(Color.blue);
                    passwordField.setText("Input a password");
                    pwdConst = 1;
                    return;
                }
                //process sign in action
                if ("Sign In".equals(e.getActionCommand())) {
                    if (accountParser.signIn(userName, new String(pwd))) {
                        JOptionPane.showConfirmDialog(frame, "Welcome " + userName, "You are now in the Sunken Place!", JOptionPane.CLOSED_OPTION);
                        frame.setVisible(false);
                        IMUContainer container = new IMUContainer();
                        container.setVisible(true);

                    } else {
                        JOptionPane.showConfirmDialog(frame, "Username and Password do not match, try again", "Error", JOptionPane.CLOSED_OPTION);
                    }
                } else if ("Sign Up".equals(e.getActionCommand())) {
                    //process sign up
                    if (accountParser.signUp(userName, new String(pwd))) {
                        int res = JOptionPane.showConfirmDialog(frame, " Your account was sucessfully created ,You may login ", "You are now in the Sunken Place!", JOptionPane.CLOSED_OPTION);

                        //back to login window
                        ecgIMUUserSignLabel.setText("LCIMU User Sign In");
                        registerPanel.setVisible(true);
                        signInButton.setText("Sign In");
                        singTag = 0;
                    } else {
                        JOptionPane.showConfirmDialog(frame, "Username '" + userName + "' exists,please choose another.", "Error", JOptionPane.CLOSED_OPTION);
                    }


                }
            }
        });


        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //sign in cancel
                if (singTag == 0) {
                    frame.dispose();
                }
                //sign up cancel,back to signin window
                if (singTag == 1) {
                    ecgIMUUserSignLabel.setText("LCIMU User Sign In");
                    registerPanel.setVisible(true);
                    signInButton.setText("Sign In");
                    singTag = 0;
                }
            }
        });

        //when foccussed,clear the text
        userNameField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (userNameConst == 1) {
                    userNameField.setText("");
                    userNameField.setForeground(Color.black);
                }
                super.mouseClicked(e);
            }
        });

        //
        passwordField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (pwdConst == 1) {
                    passwordField.setEchoChar('*');
                    passwordField.setText("");
                    passwordField.setForeground(Color.black);
                }
                super.mouseClicked(e);
            }
        });

        signUpButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                singTag = 1;
                ecgIMUUserSignLabel.setText("LCIMU User Sign Up");
                registerPanel.setVisible(false);
                signInButton.setText("Sign Up");
            }
        });
    }

    /*
     * This method will retrieve an instance of the AccountParser class
     */
    public static AccountParser getAccountParser() {
        return accountParser;
    }

    /*
     * The main class
     */
    public static void main(String[] args) {
        try {
            BeautyEyeLNFHelper.frameBorderStyle = BeautyEyeLNFHelper.FrameBorderStyle.translucencyAppleLike;
            UIManager.put("RootPane.setupButtonVisible", false);
            BeautyEyeLNFHelper.launchBeautyEyeLNF();

        } catch (Exception e) {
            e.printStackTrace();
        }

        accountParser = new AccountParser();
        System.out.println(accountParser.getDocument().asXML());
        //Set the layout of the frame panel
        frame = new JFrame("LCIMU App");
        frame.setContentPane(new MainWindow().outPanel);
        //frame.setBackground(Color.CYAN);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(new Dimension(600, 320));
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);


    }

    /**
     * initialize the UI components
     */
    private void createUIComponents() {

        outPanel = new JPanel(new BorderLayout(60, 60));

        framePanel = new JPanel(new GridLayout(3, 2));
        framePanel.setSize(new Dimension(360, 180));

        outPanel.add(framePanel, BorderLayout.CENTER);

        userNameField = new JTextField();
        userNameField.setPreferredSize(new Dimension(240, 32));
        passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(240, 32));

        labelFont = new Font("Consolas", Font.PLAIN | Font.ITALIC, 14);
        buttonFont = new Font("Microsoft YaHei", Font.PLAIN, 14);

        userNameLabel = new JLabel();
        userNameLabel.setFont(labelFont);
        passwordLabel = new JLabel();
        passwordLabel.setFont(labelFont);


        labelPanel = new JPanel(new BorderLayout(10, 10));
        ecgIMUUserSignLabel = new JLabel("LCIMU User Sign In", SwingConstants.CENTER);
        ecgIMUUserSignLabel.setFont(new Font("Courier New", Font.PLAIN | Font.BOLD, 18));
        labelPanel.add(ecgIMUUserSignLabel, BorderLayout.CENTER);

//        BEButtonUI conButtonUI = new BEButtonUI();
//        conButtonUI.setNormalColor(BEButtonUI.NormalColor.lightBlue);
        signInButton = new JButton();
        signInButton.setFont(buttonFont);
//        signInButton.setUI(conButtonUI);
        cancelButton = new JButton();
        cancelButton.setFont(buttonFont);
        BEButtonUI beButtonUI = new BEButtonUI();
        beButtonUI.setNormalColor(BEButtonUI.NormalColor.red);
        cancelButton.setUI(beButtonUI);

        registerPanel = new JPanel(new BorderLayout());
        signUpButton = new JButton("Sign Up");
        Font signUpFont = buttonFont;
        BEButtonUI sonButtonUI = new BEButtonUI();
        signUpButton.setFont(signUpFont);
        sonButtonUI.setNormalColor(BEButtonUI.NormalColor.lightBlue);
        signUpButton.setUI(sonButtonUI);
        registerPanel.setBorder(new EmptyBorder(5, 5, 5, 20));
    }
}
