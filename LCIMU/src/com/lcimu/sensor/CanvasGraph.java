package com.lcimu.sensor;
/*
 * This sketch was modified from that
 * developed my Mitchell Welch, 2015
 */
import com.lcimu.MainWindow;
import gnu.io.CommPortIdentifier;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/*
 * This class is responsible for displaying the real time data
 */

public class CanvasGraph extends JFrame {
    private static final int MAX_DATA_POINTS = 1000;
    private static JFXPanel jfxPanel;   //  a static variable to hold the JFXPanel object to allow integration with the Java Swing class
    private static volatile CanvasGraph instance;  // declaring an instance of CanvasGraph that can be updated
    public BufferedWriter writer;  //Create a buffered character-output stream that uses a default-sized output buffer
    public SerialLogger loggerDev;          //a variable to hold the SerialLogger object
    FileWriter out = null;          //initialize an output file
    int plotCount;                  // an integer variable to hold the plot count
    String serial;                  // declare a String variable named serial
    Scene mainScene;                // instantiate a Scene object variable named mainScene to hold contents on a screen graph
    Scene popupScene;                // instantiate a Scene object variable named popupScene to hold contents on a mini screen
    Stage popupStage;                 // this Stage object variable would hold values at the top of the javaFx container
    ComboBox serialComboBox;          // This ComboBox variable would hold the list of serial ports
    TextField outFileName;             // a TextField object to hold the editable output File name
    String fileName;                   // a string variable to hold the  file name
    private int xSeriesData = 0;
    private ConcurrentLinkedQueue<DataItem> dataQ = new ConcurrentLinkedQueue<>();
    private ExecutorService executor;
    private int displayCount;         // a variable to hold the display count
    private GraphicsContext gc;      // an instance of the GraphicsContext class for calls to the Canvas display
    private int previousRolloneVal;  // a variable to hold the roll value from the first sensor
    private int previousPitchoneVal; // a variable to hold the pitch value from the first sensor
    private int previousRolltwoVal; // a variable to hold the roll value from the second sensor
    private int previousPitchtwoVal; // a variable to hold the pitch value from the second sensor
    private int previousRollthreeVal; // a variable to hold the roll value from the third sensor
    private int previousPitchthreeVal; // a variable to hold the pitch value from the third sensor
    private int xPosition;          // a variable used to hold the value of the line on the x-axis

    /*
     * This constructor is used to set the
     * basic configuration settings of the display frame
     */
    public CanvasGraph() {
        setTitle("LCIMU Data Acquisition");
        setResizable(true);
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);//u can't close the window by clicking the 'x' icon in the upper right corner
        jfxPanel = new JFXPanel();
        add(jfxPanel);
    }

    /*
     * Get a single instance of the CanvasGraph
     */
    public static CanvasGraph getInstance() {
        if (instance == null) {   //if the instance of the CanvasGraph is equal to null
            synchronized (CanvasGraph.class) {  //Is there only one thread on the CanvasGraph class block
                if (instance == null) {
                    instance = new CanvasGraph();
                }
            }
        }
        return instance;
    }

    /*This method would initialize and display the UI
     * when called by default
     */

    /*
     * This method would search for the available serial ports on the local computer
     */
    public static String[] listSerialPorts() {
        Enumeration ports = CommPortIdentifier.getPortIdentifiers();
        ArrayList portList = new ArrayList();
        String portArray[] = null;
        while (ports.hasMoreElements()) {
            CommPortIdentifier port = (CommPortIdentifier) ports.nextElement();
            if (port.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                portList.add(port.getName());
            }
        }
        portArray = (String[]) portList.toArray(new String[0]);
        return portArray;
    }

    /*
     * The Main method
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                getInstance().initAndShowUI();
            }
        });
    }

    public void initAndShowUI() {
        setVisible(true); //Make the UI visible

        Platform.runLater(new Runnable() { //Update this GUI component from an outside thread
            @Override
            public void run() {
                try {
                    initFX(jfxPanel); //This method will update the thread from outside
                    start();
                } catch (Exception e) {//Throw Exception
                    e.printStackTrace();
                }
            }
        });
    }

    /*
     *  This method will control the storage of the captured data on a csv file
     *  and configure the display pane
     */
    private void initFX(JFXPanel jfxPanel) {
        Platform.setImplicitExit(false);  //set the attribute to false to prevent the application from automatically shutting down

        Date dNow = new Date();           // create a Date object variable
        SimpleDateFormat ft = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss"); // create a Date format

        /*
         *Create a csv file name with the Date format
         */
        fileName = MainWindow.getAccountParser().getUserProfileDir().getPath() + File.separator + ft.format(dNow) + ".csv";

        // Create a canvas node and set its dimensions
        Canvas canvas = new Canvas(1000, 700);

        gc = canvas.getGraphicsContext2D(); //returns a GraphicContext instance associated with the Canvas and stores it in the variable gc

        plotCount = 0;      // set the plotCount variable to 0
        this.xPosition = 0;  // set the xposition variable to 0

        //Draw the boundary lines
        this.drawBoundaries(gc);

        VBox vbox = new VBox();  //create a Vbox object layout with spacing equal to null or zero
        vbox.getChildren().add(canvas); // gets the canvas node object as a child of the parent vbox object

        // set the dimensions of the main scene
        mainScene = new Scene(vbox, 1000, 700);
        //mainScene.getStylesheets().add("graphStyles.css");
        //Display the mainScene object on the JavaFax panel.
        jfxPanel.setScene(mainScene);
        //Popup window to select the serial port
        Label lblPopup = new Label("Select Serial Port");
        VBox popupPane = new VBox();
        popupPane.setPadding(new Insets(10));
        popupPane.setSpacing(8);
        Button popupOk = new Button("Continue");
        popupOk.setOnAction(e -> ButtonClicked(e));

        // instantiate a String variable array to hold the list of serial ports
        String[] serials = listSerialPorts();
        serialComboBox = new ComboBox();
        serialComboBox.getItems().addAll(serials);  //retrieve the number of serial ports
        serialComboBox.setValue(serials[0]);        // set the values of the serial ports

        Label lblFile = new Label("Output File Name:");
        outFileName = new TextField();
        outFileName.setText(fileName);

        popupPane.getChildren().addAll(lblPopup, serialComboBox, lblFile, outFileName, popupOk);

        popupScene = new Scene(popupPane, 300, 200);
        popupStage = new Stage();
        popupStage.setScene(popupScene);

        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Serial Port");

        popupStage.showAndWait();
    }

    /*
     * This method controls the pop up display
     */
    public void ButtonClicked(ActionEvent e) {
        if (e.getSource() == mainScene)
            popupStage.showAndWait();
        else
            serial = serialComboBox.getValue().toString();
        fileName = outFileName.getText();
        popupStage.close();
    }

    /*
     * This method will start the thread
     *initialize the csv file and prepare executor services
     */
    private void start() throws Exception {

        displayCount = 0;

        System.out.println(fileName);
        out = new FileWriter(fileName);
        writer = new BufferedWriter(out);

        String header = "Time, Roll1,Pitch1,Yaw1,Acc1x,Acc1y,Acc1z,Gyro1x,Gyro1y,Gyro1z,Q1w,Q1x,Q1y,Q1z, Roll2,Pitch2,Yaw2,Acc2x,Acc2y,Acc2z,Gyro2x,Gyro2y,Gyro2z,Q2w,Q2x,Q2y,Q2z, Roll3,Pitch3,Yaw3,Acc3x,Acc3y,Acc3z,Gyro3x,Gyro3y,Gyro3z,Q3w,Q3x,Q3y,Q3z\n";

        try {
            writer.write(header);
        } catch (IOException ioe) {
            //ioe.printStackTrace();
            System.out.println(ioe);
            //System.exit(1);

        }

        //-- Prepare Executor Services
        executor = Executors.newCachedThreadPool();
        //addToQueue = new AddToQueue();

        loggerDev = new SerialLogger(dataQ, serial);
        executor.execute(loggerDev);

        //-- Prepare Timeline
        prepareTimeline();
    }

    /*
      * This method will terminate sthe display thread
     */
    //@Override
    public void stop() {
        synchronized (CanvasGraph.class) {
            instance = null;
        }
        shutdownAndAwaitTermination(executor);

    }

    private void drawBoundaries(GraphicsContext gc) {
        gc.setStroke(Color.LIGHTSLATEGREY);
        gc.setLineWidth(0.5);
        gc.strokeLine(0, 100, 1000, 100);
        gc.strokeLine(0, 150, 1000, 150);
        gc.strokeLine(0, 200, 1000, 200);
        gc.strokeLine(0, 400, 1000, 400);
        gc.strokeLine(0, 450, 1000, 450);
        gc.strokeLine(0, 500, 1000, 500);
        gc.strokeLine(0, 700, 1000, 700);

    }

    void shutdownAndAwaitTermination(ExecutorService pool) {
        try {
            if (writer != null) {
            }
            //writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            pool.shutdown(); // Disable new tasks from being submitted
            // Wait a while for existing tasks to terminate
            if (!pool.awaitTermination(1, TimeUnit.SECONDS)) {
                pool.shutdownNow(); // Cancel currently executing tasks
                // Wait a while for tasks to respond to being cancelled
                if (!pool.awaitTermination(1, TimeUnit.SECONDS))
                    System.err.println("Pool did not terminate");
            }
        } catch (InterruptedException ie) {
            // (Re-)Cancel if current thread also interrupted
            ie.printStackTrace();
            pool.shutdownNow();
            // Preserve interrupt status
            Thread.currentThread().interrupt();
        }
//        System.exit(0);
    }

    /*
      This method controls the Timeline  that gets called in the JavaFX Main Window thread
     */
    private void prepareTimeline() {

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                addDataToSeries();
            }
        }.start();

    }

    /*
     * This method controls the amount of data being
     * added to the graph display window
     */
    private void addDataToSeries() {
        for (int i = 0; i < 40; i++) { //-- add 40 numbers to the plot+
            if (dataQ.isEmpty()) break;
            DataItem temp = dataQ.remove();
            //Write Data to file
            String row = temp.getTime_val() + "," + temp.getRollone_val() + "," + temp.getPitchone_val() + "," + temp.getYawone_val() + "," + temp.getAxone_val() + "," + temp.getAyone_val() + "," + temp.getAzone_val() + "," + temp.getGxone_val() + "," + temp.getGyone_val() + "," + temp.getGzone_val() + "," + temp.getQwone_val() + "," + temp.getQxone_val() + "," + temp.getQyone_val() + "," + temp.getQzone_val() + "," + temp.getRolltwo_val() + "," + temp.getPitchtwo_val() + "," + temp.getYawtwo_val() + "," + temp.getAxtwo_val() + "," + temp.getAytwo_val() + "," + temp.getAztwo_val() + "," + temp.getGxtwo_val() + "," + temp.getGytwo_val() + "," + temp.getGztwo_val() + "," + temp.getQwtwo_val() + "," + temp.getQxtwo_val() + "," + temp.getQytwo_val() + "," + temp.getQztwo_val() + "," + temp.getRollthree_val() + "," + temp.getPitchthree_val() + "," + temp.getYawthree_val() + "," + temp.getAxthree_val() + "," + temp.getAythree_val() + "," + temp.getAzthree_val() + "," + temp.getGxthree_val() + "," + temp.getGythree_val() + "," + temp.getGzthree_val() + "," + temp.getQwthree_val() + "," + temp.getQxthree_val() + "," + temp.getQythree_val() + "," + temp.getQzthree_val() + '\n';

            try {
                writer.write(row);
            } catch (IOException ioe) {
                ioe.printStackTrace();
                System.out.println(ioe);
            }

            if (plotCount == 1) {

                int currentRolloneVal = (int) (100.0 - (((temp.getRollone_val() / 100) * 50) + 50));
                int currentRolltwoVal = (int) (100.0 - (((temp.getRolltwo_val() / 100) * 50) + 50));
                int currentRollthreeVal = (int) (100.0 - (((temp.getRollthree_val() / 100) * 50) + 50));
                int currentPitchoneVal = (int) (100.0 - (((temp.getPitchone_val() / 100) * 50) + 50));
                int currentPitchtwoVal = (int) (100.0 - (((temp.getPitchtwo_val() / 100) * 50) + 50));
                int currentPitchthreeVal = (int) (100.0 - (((temp.getPitchthree_val() / 100) * 50) + 50));
                this.gc.setLineWidth(1);
                this.gc.setStroke(Color.BLUE);
                this.gc.strokeLine(xPosition, previousRolloneVal + 50, xPosition + 1, currentRolloneVal + 50);
                this.gc.setStroke(Color.GREEN);
                this.gc.strokeLine(xPosition, previousRolltwoVal + 150, xPosition + 1, currentRolltwoVal + 150);
                this.gc.setStroke(Color.BLACK);
                this.gc.strokeLine(xPosition, previousRollthreeVal + 200, xPosition + 1, currentRollthreeVal + 200);
                this.gc.setStroke(Color.RED);
                this.gc.strokeLine(xPosition, previousPitchoneVal + 400, xPosition + 1, currentPitchoneVal + 400);
                this.gc.setStroke(Color.ORANGE);
                this.gc.strokeLine(xPosition, previousPitchtwoVal + 450, xPosition + 1, currentPitchtwoVal + 450);
                this.gc.setStroke(Color.YELLOW);
                this.gc.strokeLine(xPosition, previousPitchthreeVal + 500, xPosition + 1, currentPitchthreeVal + 500);


                try {
                    Thread.sleep(1);
                } catch (Exception e) {
                    System.out.println(e);
                }

                previousRolloneVal = currentRolloneVal;
                previousRolltwoVal = currentRolltwoVal;
                previousRollthreeVal = currentRollthreeVal;
                previousPitchoneVal = currentPitchoneVal;
                previousPitchtwoVal = currentPitchtwoVal;
                previousPitchthreeVal = currentPitchthreeVal;
                xPosition = xPosition + 1;
                plotCount = 0;

            }
            plotCount = plotCount + 1;

            if (xPosition == 1000) {
                xPosition = 0;
                gc.clearRect(0, 0, 1000, 100);
                gc.clearRect(0, 50, 1000, 100);
                gc.clearRect(0, 150, 1000, 100);
                gc.clearRect(0, 200, 1000, 200);
                gc.clearRect(0, 400, 1000, 100);
                gc.clearRect(0, 450, 1000, 100);
                gc.clearRect(0, 500, 1000, 100);
                gc.clearRect(0, 600, 1000, 100);

            }

        }

    }
}