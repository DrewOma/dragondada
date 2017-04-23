package com.lcimu.sensor;

/**
 * Created by mitchell on 12/11/15.
 */

import gnu.io.CommPortIdentifier;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
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

import java.io.BufferedWriter;
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


public class CanvasGraph extends Application {
    private static final int MAX_DATA_POINTS = 1000;

    //private Series series;
    //private Series ax_series;
    //private Series ay_series;
    //private Series az_series;
    private int xSeriesData = 0;
    private ConcurrentLinkedQueue<DataItem> dataQ = new ConcurrentLinkedQueue<>();
    private ExecutorService executor;
    // private AddToQueue addToQueue;
    //private Timeline timeline2;
    //private NumberAxis xAxis;
    private int displayCount;
    private GraphicsContext gc;
    private int previousTimeVal;
    private int previousRolloneVal;
    private int previousCompxVal;
    private int previousGyroxVal;
    private int previousPitchoneVal;
    private int previousCompyVal;
    private int previousGyroyVal;
    private int previousRolltwoVal;
    private int previousCompxxVal;
    private int previousGyroxxVal;
    private int previousPitchtwoVal;
    private int previousCompyyVal;
    private int previousGyroyyVal;
    private int previousRollthreeVal;
    private int previousCompxxxVal;
    private int previousGyroxxxVal;
    private int previousPitchthreeVal;
    private int previousCompyyyVal;
    private int previousGyroyyyVal;
    private int xPosition;

    FileWriter out = null;
    BufferedWriter writer;
    int plotCount;
    String serial;
    Scene mainScene;
    Scene popupScene;
    Stage popupStage;
    ComboBox serialComboBox;
    TextField outFileName;
    String fileName;

    private void init(Stage primaryStage) {

        //Output file setup
        Date dNow = new Date( );
        SimpleDateFormat ft =
                new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss");

        fileName = "out_file_"+ft.format(dNow) + ".csv";

        Canvas canvas = new Canvas(1000, 700);
        gc = canvas.getGraphicsContext2D();

        plotCount = 0;
        this.xPosition = 0;

        //Draw the boundary lines
        this.drawBoundaries(gc);

        VBox vbox = new VBox();
        //vbox.setPadding(new Insets(10));
        //vbox.setSpacing(5);

        //Text title = new Text("Data Output");
        //vbox.getChildren().add(title);
        vbox.getChildren().add(canvas);

        mainScene = new Scene(vbox, 1000,700);
        mainScene.getStylesheets().add("graphStyles.css");


        /*sc.setAnimated(true);
        ax_sc.setAnimated(true);
        ay_sc.setAnimated(true);
        az_sc.setAnimated(true);*/

        primaryStage.setTitle("Accelerometer Data Acquisition");
        primaryStage.setScene(mainScene);

        //Popup widow to select the serial port
        Label lblPopup = new Label("Select Serial Port");
        VBox popupPane = new VBox();
        popupPane.setPadding(new Insets(10));
        popupPane.setSpacing(8);
        //popupPane.setVgap(10);
        Button popupOk = new Button("Continue");
        popupOk.setOnAction(e->ButtonClicked(e));

        String [] serials = listSerialPorts();

        serialComboBox = new ComboBox();
        serialComboBox.getItems().addAll(serials);
        serialComboBox.setValue(serials[0]);

        Label lblFile = new Label("Output File Name:");
        outFileName = new TextField ();
        outFileName.setText(fileName);

        popupPane.getChildren().addAll(lblPopup,serialComboBox,lblFile,outFileName,popupOk);

        popupScene = new Scene(popupPane, 300, 200);
        popupStage= new Stage();
        popupStage.setScene(popupScene);

        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Serial Port");


        popupStage.showAndWait();

    }

    public void ButtonClicked(ActionEvent e)
    {
        if (e.getSource()==mainScene)
            popupStage.showAndWait();
        else
            serial = serialComboBox.getValue().toString();
        fileName = outFileName.getText();
        popupStage.close();
    }

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

    @Override public void start(Stage primaryStage) throws Exception {



        displayCount=0;
        init(primaryStage);
        primaryStage.show();


        System.out.println(fileName);
        out = new FileWriter(fileName);
        writer = new BufferedWriter(out);

        //String header = ",RollOne, CompAngle1, Gyroangle1,Pitch1,CompAngle11, Gyroangle11,Roll2, CompAngle2, Gyroangle2,Pitch2,CompAngle22, Gyroangle22,Roll3, CompAngle3, Gyroangle3,Pitch3,CompAngle33, Gyroangle33\n";
        //String header = "Time(Chest), Roll1,Pitch1,Acc1x,Acc1y ,Acc1z,Gyro1x,Gyro1y,Gyro1z,Gyro1X,Gyro1Y,Comp1X,Comp1Y,Time(LeftHand),Roll2,Pitch2,Acc2x,Acc2y ,Acc2z,Gyro2x,Gyro2y,Gyro2z,Gyro2X,Gyro2Y,Comp2X,Comp2Y,Time(RightHand),Roll3,Pitch3,Acc3x,Acc3y, Acc3z,Gyro3x,Gyro3y,Gyro3z,Gyro3X,Gyro3Y,Comp3X,Comp3Y\n";
        //String header = "Time1, Time2, Time3\n";
        //String header = "Axone, Axtwo, Axthree\n";
        // String header = "Axone\n";
        //String header = "Time(Chest), Roll1,Pitch1,Yaw1,Psi1,Theta1,Phi1,Acc1x,Acc1y,Acc1z,Gyro1x,Gyro1y,Gyro1z,Qw1w,Q1x,Q1y,Q1z,Time(Lefthand),Roll2,Pitch2,Yaw2,Psi2,Theta2,Phi2,Acc2x,Acc2y,Acc2z,Gyro2x,Gyro2y,Gyro2z,Qw2w,Q2x,Q2y,Q2z,Time(RightHand), Roll3,Pitch3,Yaw3,Psi3,Theta3,Phi3,Acc3x,Acc3y,Acc3z,Gyro3x,Gyro3y,Gyro3z,Qw3w,Q3x,Q3y,Q3z";
        //String header = "Time(Chest), Roll1,Pitch1,Yaw1,Acc1x,Acc1y,Acc1z,Gyro1x,Gyro1y,Gyro1z,Q1w,Q1x,Q1y,Q1z,Time(LeftHand), Roll2,Pitch2,Yaw2,Acc2x,Acc2y,Acc2z,Gyro2x,Gyro2y,Gyro2z,Q2w,Q2x,Q2y,Q2z,Time(RightHand), Roll3,Pitch3,Yaw3,Acc3x,Acc3y,Acc3z,Gyro3x,Gyro3y,Gyro3z,Q3w,Q3x,Q3y,Q3z\n";
        String header = "Time, Roll1,Pitch1,Yaw1,Acc1x,Acc1y,Acc1z,Gyro1x,Gyro1y,Gyro1z,Q1w,Q1x,Q1y,Q1z, Roll2,Pitch2,Yaw2,Acc2x,Acc2y,Acc2z,Gyro2x,Gyro2y,Gyro2z,Q2w,Q2x,Q2y,Q2z, Roll3,Pitch3,Yaw3,Acc3x,Acc3y,Acc3z,Gyro3x,Gyro3y,Gyro3z,Q3w,Q3x,Q3y,Q3z\n";

        try {
            writer.write(header);
        }catch (IOException ioe){

            System.out.println(ioe);
            System.exit(1);

        }

        //-- Prepare Executor Services
        executor = Executors.newCachedThreadPool();
        //addToQueue = new AddToQueue();
        SerialLogger loggerDev = new SerialLogger(dataQ,serial);
        executor.execute(loggerDev);
        //-- Prepare Timeline
        prepareTimeline();
    }

    @Override public void stop(){

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
        // gc.strokeLine(0, 700, 1000, 700);
    }

    void shutdownAndAwaitTermination(ExecutorService pool) {
        try
        {
            if ( writer != null)
                writer.close( );
        }
        catch ( IOException e)
        {
        }
        pool.shutdown(); // Disable new tasks from being submitted
        try {
            // Wait a while for existing tasks to terminate
            if (!pool.awaitTermination(1, TimeUnit.SECONDS)) {
                pool.shutdownNow(); // Cancel currently executing tasks
                // Wait a while for tasks to respond to being cancelled
                if (!pool.awaitTermination(1, TimeUnit.SECONDS))
                    System.err.println("Pool did not terminate");
            }
        } catch (InterruptedException ie) {
            // (Re-)Cancel if current thread also interrupted
            pool.shutdownNow();
            // Preserve interrupt status
            Thread.currentThread().interrupt();
        }
        System.exit(0);
    }

    public static void main(String[] args) {
        launch(args);
    }

//    private class AddToQueue implements Runnable {
//        public void run(){
//
//            try {
//                Thread.sleep(1);
//                double val = Math.random()*149;
//                // add a item of random data to queue
//                if(displayCount ==10) {
//                    dataQ.add(new DataItem((int)(Math.random() * 148),(int)(Math.random() * 148),(int)(Math.random() * 148),(int)(Math.random() * 148),1,1));
//                    displayCount = 0;
//                }else{
//                    displayCount++;
//                }
//
//                executor.execute(this);
//            } catch (InterruptedException ex) {
//                Logger.getLogger(AreaChartSample.class.getName()).log(Level.SEVERE, null, ex);
//            }catch(RejectedExecutionException e){
//                System.out.println("Serial Reader Shutting down ...");
//            }
//        }
//    }

    //-- Timeline gets called in the JavaFX MainApp thread
    private void prepareTimeline() {
        // Every frame to take any data from queue and add to chart
        new AnimationTimer() {
            @Override public void handle(long now) {
                addDataToSeries();
            }
        }.start();
    }

    private void addDataToSeries() {
        for (int i = 0; i < 40; i++) { //-- add 40 numbers to the plot+
            if (dataQ.isEmpty()) break;
            DataItem temp = dataQ.remove();
            //Write Data to file

           // String row = temp.getTime_val()+","+ temp.getTime_vall() +","+ temp.getTime_valll() + '\n';
            //String row = temp.getRollone_val()+","+ temp.getRolltwo_val()+","+ temp.getRollthree_val() + '\n';
            //String row = temp.getTime_val()+","+ temp.getRollone_val()+","+ temp.getPitchone_val()+","+ temp.getAxone_val()+ "," + temp.getAyone_val()+ "," + temp.getAzone_val()+ "," + temp.getGxone_val()+ "," + temp.getGyone_val()+ "," + temp.getGzone_val()+ ","+temp.getGyrox_val() + ","+temp.getGyroy_val()+ ","  + temp.getCompx_val()+","+ temp.getCompy_val()+ "," + temp.getTime_vall()+"," + temp.getRolltwo_val()+","+ temp.getPitchtwo_val()+","+ temp.getAxtwo_val()+ "," + temp.getAytwo_val()+ "," + temp.getAztwo_val()+ "," + temp.getGxtwo_val()+ "," + temp.getGytwo_val()+ "," + temp.getGztwo_val() + "," +temp.getGyroxx_val() + ","+temp.getGyroyy_val()+ ","  + temp.getCompxx_val()+","+ temp.getCompyy_val()+ ","+ temp.getTime_valll()+ "," + temp.getRollthree_val()+"," + temp.getPitchthree_val()+","+ temp.getAxthree_val()+ "," + temp.getAythree_val()+ "," + temp.getAzthree_val()+ "," + temp.getGxthree_val()+ "," + temp.getGythree_val()+ "," + temp.getGzthree_val() + "," +temp.getGyroxxx_val() + ","+temp.getGyroyyy_val()+ ","  + temp.getCompxxx_val()+","+ temp.getCompyyy_val() + '\n';
            // String row =  temp.getAxone_val()+'\n';
            //String row = temp.getTime_val()+","+ temp.getRollone_val()+","+ temp.getPitchone_val()+","+ temp.getYawone_val()+","+ temp.getPsione_val()+","+temp.getThetaone_val()+ ","+temp.getPhione_val()+ "," +temp.getAxone_val()+ "," + temp.getAyone_val()+ "," + temp.getAzone_val()+ "," + temp.getGxone_val()+ "," + temp.getGyone_val()+ "," + temp.getGzone_val()+ ","+temp.getQwone_val() + ","+temp.getQxone_val()+ ","  + temp.getQyone_val()+","+ temp.getQzone_val()+ "," + temp.getTime_vall()+"," + temp.getRolltwo_val()+","+ temp.getPitchtwo_val()+","+ temp.getYawtwo_val() + "," + temp.getPsitwo_val() + "," + temp.getThetatwo_val()+","+ temp.getPhitwo_val() + "," + temp.getAxtwo_val()+ "," + temp.getAytwo_val()+ "," + temp.getAztwo_val()+ "," + temp.getGxtwo_val()+ "," + temp.getGytwo_val()+ "," + temp.getGztwo_val() + "," +temp.getQwtwo_val() + ","+temp.getQxtwo_val()+ ","  + temp.getQytwo_val()+","+ temp.getQztwo_val()+ ","+ temp.getTime_valll()+ "," + temp.getRollthree_val()+"," + temp.getPitchthree_val()+","+ temp.getYawthree_val() + "," + temp.getPsithree_val() + "," + temp.getThetathree_val()+ "," + temp.getPhithree_val() + "," + temp.getAxthree_val()+ "," + temp.getAythree_val()+ "," + temp.getAzthree_val()+ "," + temp.getGxthree_val()+ "," + temp.getGythree_val()+ "," + temp.getGzthree_val() + "," +temp.getQwthree_val() + ","+temp.getQxthree_val()+ ","  + temp.getQythree_val()+","+ temp.getQzthree_val() + '\n';
            //String row = temp.getTime_val()+","+ temp.getRollone_val()+","+ temp.getPitchone_val()+","+ temp.getYawone_val()+"," +temp.getAxone_val()+ "," + temp.getAyone_val()+ "," + temp.getAzone_val()+ "," + temp.getGxone_val()+ "," + temp.getGyone_val()+ "," + temp.getGzone_val()+ ","+temp.getQwone_val() + ","+temp.getQxone_val()+ ","  + temp.getQyone_val()+","+ temp.getQzone_val()+ "," + temp.getTime_vall()+"," + temp.getRolltwo_val()+","+ temp.getPitchtwo_val()+","+ temp.getYawtwo_val() +  "," + temp.getAxtwo_val()+ "," + temp.getAytwo_val()+ "," + temp.getAztwo_val()+ "," + temp.getGxtwo_val()+ "," + temp.getGytwo_val()+ "," + temp.getGztwo_val() + "," +temp.getQwtwo_val() + ","+temp.getQxtwo_val()+ ","  + temp.getQytwo_val()+","+ temp.getQztwo_val()+ ","+ temp.getTime_valll()+ "," + temp.getRollthree_val()+"," + temp.getPitchthree_val()+","+ temp.getYawthree_val() + "," + temp.getAxthree_val()+ "," + temp.getAythree_val()+ "," + temp.getAzthree_val()+ "," + temp.getGxthree_val()+ "," + temp.getGythree_val()+ "," + temp.getGzthree_val() + "," +temp.getQwthree_val() + ","+temp.getQxthree_val()+ ","  + temp.getQythree_val()+","+ temp.getQzthree_val() + '\n';
            String row = temp.getTime_val()+","+ temp.getRollone_val()+","+ temp.getPitchone_val()+","+ temp.getYawone_val()+"," +temp.getAxone_val()+ "," + temp.getAyone_val()+ "," + temp.getAzone_val()+ "," + temp.getGxone_val()+ "," + temp.getGyone_val()+ "," + temp.getGzone_val()+ ","+temp.getQwone_val() + ","+temp.getQxone_val()+ ","  + temp.getQyone_val()+","+ temp.getQzone_val()+ "," + temp.getRolltwo_val()+","+ temp.getPitchtwo_val()+","+ temp.getYawtwo_val() +  "," + temp.getAxtwo_val()+ "," + temp.getAytwo_val()+ "," + temp.getAztwo_val()+ "," + temp.getGxtwo_val()+ "," + temp.getGytwo_val()+ "," + temp.getGztwo_val() + "," +temp.getQwtwo_val() + ","+temp.getQxtwo_val()+ ","  + temp.getQytwo_val()+","+ temp.getQztwo_val()+ "," + temp.getRollthree_val()+"," + temp.getPitchthree_val()+","+ temp.getYawthree_val() + "," + temp.getAxthree_val()+ "," + temp.getAythree_val()+ "," + temp.getAzthree_val()+ "," + temp.getGxthree_val()+ "," + temp.getGythree_val()+ "," + temp.getGzthree_val() + "," +temp.getQwthree_val() + ","+temp.getQxthree_val()+ ","  + temp.getQythree_val()+","+ temp.getQzthree_val() + '\n';


            try {
                writer.write(row);
            }catch (IOException ioe){

                System.out.println(ioe);
                System.exit(1);

            }

            if(plotCount==1) {

                //We will need to normalise the real data correctly here
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
                }catch(Exception e){
                    System.out.println(e);
                }

                previousRolloneVal = currentRolloneVal;
                previousRolltwoVal = currentRolltwoVal;
                previousRollthreeVal = currentRollthreeVal;
                previousPitchoneVal = currentPitchoneVal;
                previousPitchtwoVal = currentPitchtwoVal;
                previousPitchthreeVal = currentPitchthreeVal;
                xPosition = xPosition + 1;
                plotCount=0;

            }
            plotCount = plotCount+1;

            if(xPosition==1000){
                xPosition = 0;
                gc.clearRect(0,0, 1000,100);
                gc.clearRect(0,50, 1000,100);
                gc.clearRect(0,150, 1000,100);
                gc.clearRect(0,200, 1000,200);
                gc.clearRect(0,400, 1000,100);
                gc.clearRect(0,450, 1000,100);
                gc.clearRect(0,500, 1000,100);
                gc.clearRect(0,600, 1000,100);

            }

        }

    }

}