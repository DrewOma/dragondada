package com.lcimu;

/*
 * Import all the various classes needed to design the UI interface
 */

import com.lcimu.sensor.CanvasGraph;
import com.lcimu.sensor.CanvasGraphAccelGyro;
import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;
import org.jb2011.lnf.beautyeye.ch3_button.BEButtonUI;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;

import com.lcimu.sensor.DataItem;

/**
 * This IMUContainer class is used to design the User Interface
 */
public class IMUContainer extends JFrame {
    public static AbstractTableModel tm;
    public static Vector<DataItem> datas;
    private static JFrame frame;
    private JPanel outPanel;
    private JButton startBtn;
    private JPanel btnPanel;
    private JPanel canvasPanel;
    private JButton stopButton;
    private JLabel welcomeLabel;
    private JPanel labelPanel;
    private JTable dataTable;
    private JButton logOutButton;
    private IMUContainer container;

    /*
     * This Constructor would manage all the button functions on the frame
     * including starting and stopping the data display process and logging out the User
     */
    public IMUContainer() {
        container = this;
        setTitle("The LCIMU Application User Interface");
        setContentPane(outPanel);
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(new Dimension(600, 320));
        setLocationRelativeTo(null);

        startBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) { // When the start button is clicked listen for a response
                initCanvasGraph();
                super.mouseClicked(e); //to ensure that the mouseClicked event is not overridden
            }
        });

        setExtendedState(JFrame.MAXIMIZED_BOTH); //maximize the Jframe


        stopButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                destroyCanvasGraph();
                super.mouseClicked(e);
            }
        });

        logOutButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {// When the logout button is clicked listen for a respons
                super.mouseClicked(e);
                container.dispose();
                new MainWindow().main(new String[]{});
            }
        });
    }

    /*
     * The Main method
     */
    public static void main(String[] args) {
        try {
            BeautyEyeLNFHelper.frameBorderStyle = BeautyEyeLNFHelper.FrameBorderStyle.translucencyAppleLike;
            UIManager.put("RootPane.setupButtonVisible", false);
            BeautyEyeLNFHelper.launchBeautyEyeLNF();

        } catch (Exception e) {
            e.printStackTrace();
        }
        IMUContainer imuContainer = new IMUContainer();  //clear container
        imuContainer.setVisible(true);  //make the imuContainer Visible
    }

    /*
     * This method is used to create the components
     * of the User Interface
     */
    private void createUIComponents() {

        btnPanel = new JPanel();
        outPanel = new JPanel();
        Font buttonFont = new Font("Microsoft YaHei", Font.PLAIN, 14);
        BEButtonUI beButtonUI = new BEButtonUI();
        BEButtonUI startButtonUI = new BEButtonUI();
        beButtonUI.setNormalColor(BEButtonUI.NormalColor.red);
        startButtonUI.setNormalColor(BEButtonUI.NormalColor.blue);
        startBtn = new JButton("Start");
        startBtn.setFont(buttonFont);
        startBtn.setMargin(new Insets(10, 10, 10, 10));
        startBtn.setUI(startButtonUI);
        stopButton = new JButton("Stop");  //Label the stop button as 'stop'
        stopButton.setFont(buttonFont); //set the stop button font
        stopButton.setMargin(new Insets(10, 10, 10, 10));//set the stop button margin
        stopButton.setUI(beButtonUI); // set the color of the stop button
        outPanel.add(btnPanel);
        btnPanel.add(startBtn);
        canvasPanel = new JPanel();
        labelPanel = new JPanel();
        welcomeLabel = new JLabel();
        welcomeLabel.setSize(new Dimension(400, 48));
        welcomeLabel.setFont(new Font("Courier New", Font.BOLD | Font.ITALIC, 18));
        welcomeLabel.setBackground(Color.orange);

        logOutButton = new JButton("Log Out");
        logOutButton.setFont(buttonFont);
        logOutButton.setMargin(new Insets(10, 10, 10, 10));


        initDataTable();   //This will update the Data on the table

        SwingUtilities.invokeLater(new Runnable() { // wait until the call to run as being executed before updating the thread
            @Override
            public void run() {
                welcomeLabel.setText(" " + welcomeLabel.getText() + MainWindow.getAccountParser().getCurrentUser().getName());
                labelPanel.add(welcomeLabel);
                labelPanel.updateUI(); //update the UI
                labelPanel.setBackground(Color.white);
            }
        });
    }

    /*
     * This method allows the User to
     * click the 'start' button to start the application
     * and Display the Data captured
     */
    public void initCanvasGraph() {
        System.out.println("Display the Data");
        datas = new Vector<DataItem>();
        SwingUtilities.invokeLater(new Runnable() {// wait until the call to run as being executed before updating the thread
            @Override
            //Comment this section to switch canvas display
            public void run() {
                CanvasGraph.getInstance().initAndShowUI();
            }
            //Uncomment this section to switch canvas display
//            public void run() {
//                CanvasGraphAccelGyro.getInstance().initAndShowUI();
//            }
        });
    }

    /*
     * This method allows the User to
     * click the 'stop' button to
      * terminate the application
     */
    public void destroyCanvasGraph() {
        System.out.println("Stop The Data Display");
        CanvasGraph container = CanvasGraph.getInstance();  //comment to switch display
        // CanvasGraphAccelGyro container = CanvasGraphAccelGyro.getInstance();  //uncomment to switch display
        container.dispose();
        container.stop();

        container.loggerDev.stop();
        synchronized (container.loggerDev.syncObj) {
            if (container.loggerDev != null) {
                container.loggerDev.tag = 1;
            }
        }
        try {
            if (container.writer != null) {
            }
            container.writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        datas.removeAllElements();// initialize the content of JTable
        tm.fireTableStructureChanged();// Update the UI

        System.out.println("Now we outta here folks!");
    }

    /**
     * This method would allow the data captured to be displayed on a table
     * on the IMU container
     */
    private void initDataTable() {
        String[] header = {"Time", "Roll1", "Pitch1", "Yaw1", "Acc1x", "Acc1y", "Acc1z", "Gyro1x", "Gyro1y", "Gyro1z", "Q1w", "Q1x", "Q1y", "Q1z", "Roll2", "Pitch2", "Yaw2", "Acc2x", "Acc2y", "Acc2z", "Gyro2x", "Gyro2y", "Gyro2z", "Q2w", "Q2x", "Q2y", "Q2z", "Roll3", "Pitch3", "Yaw3", "Acc3x", "Acc3y", "Acc3z", "Gyro3x", "Gyro3y", "Gyro3z", "Q3w", "Q3x", "Q3y", "Q3z"};

        datas = new Vector<DataItem>();

        tm = new AbstractTableModel() {
            @Override
            public int getRowCount() {
                return datas.size();
            }

            @Override
            public int getColumnCount() {
                return header.length;
            }

            @Override
            public String getColumnName(int column) {
                return header[column];
            }

            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                if (!datas.isEmpty())
                    try {

                        if (datas.elementAt(rowIndex) == null) {
                            return "";
                        } else {
                            return datas.elementAt(rowIndex).getDataByColumnOrdinal(columnIndex);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        return "";
                    }
                else
                    return "";
            }
        };

        datas.removeAllElements();
        tm.fireTableStructureChanged();// update table content
        try {
            dataTable = new JTable(tm);
            dataTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
//            CsvParser csvParser = new CsvParser("bicprunfree01.csv");
//            csvParser.parseToTableData();
//
//            datas.addAll(csvParser.getDatas());
//            tm.fireTableStructureChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
