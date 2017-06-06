package com.lcimu.sensor;
/*
 * This sketch was modified from that
 * developed my Mitchell Welch, 2015
 */
import com.lcimu.IMUContainer;
import gnu.io.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.TooManyListenersException;
import java.util.Vector;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * This SerialLogger class is used to log in the serial data
 * from the serial port on the local device
 */
public class SerialLogger implements Runnable, SerialPortEventListener {

    private static CommPortIdentifier portId;
    private static Enumeration portList;
    public volatile int tag = 0;
    public volatile Object syncObj = new Object();
    private SerialPort serialPort;
    private InputStream inputStream;
    private ConcurrentLinkedQueue<DataItem> dataQ;
    private Vector<DataItem> tableDataQ = new Vector<>();

    /*
     * This constructor is used to check for any available serial port.
     * If a serial port exist, it will configure the port and read data in from the port
     *
     */
    public SerialLogger(ConcurrentLinkedQueue<DataItem> dataQ, String serial) {

        this.dataQ = dataQ;  //instantiate the concurrent array variable as dataQ

        serialPort = null; //initialize the serialPort variable as null
        portList = CommPortIdentifier.getPortIdentifiers(); // retrieve the list of ports

        while (portList.hasMoreElements()) { //while there are more elements available
            portId = (CommPortIdentifier) portList.nextElement(); //assign an id to the element
            if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) { // assign a type to the port Id
                if (portId.getName().equals(serial)) {  //if the portId is equal to the string variable passed
                    try {
                        serialPort = (SerialPort) portId.open("LCIMU Application", 2000); //open the port
                    } catch (PortInUseException e) {
                        System.out.println(e);
                    }
                }
            }
        }

        if (serialPort == null) {  // if there are no serial ports found

            System.out.println("Serial Port not found...Exiting."); // send out a notification
            System.exit(1); //exit after one milli seconds

        } else {


            try {
                inputStream = serialPort.getInputStream(); // get the input stream data
            } catch (IOException e) {
                System.out.println(e);
                System.out.println("....Exiting."); //send out a notification
                System.exit(1); //exit after 1 milli seconds
            }
            try {
                serialPort.addEventListener(this); // listen for a port
            } catch (TooManyListenersException e) { //if there are more than one listeners on this port throw an exception
                System.out.println(e);  //print out the exception
                System.out.println("....Exiting.");  // exit statement
                System.exit(1); // exit the system after 1 milli seconds
            }
            serialPort.notifyOnDataAvailable(true); // the user will be notified if there is data on the serial port
            try {
                serialPort.setSerialPortParams(115200, //sets the baud rate to 115200
                        SerialPort.DATABITS_8,   //sets the data bits format to 8
                        SerialPort.STOPBITS_1,   //sets the stop bits to 1
                        SerialPort.PARITY_NONE); //sets the parity to null
            } catch (UnsupportedCommOperationException e) {  //Throws an exception
                System.out.println(e);                       //prints out the exception
                System.out.println("....Exiting.");        //sends exit notification
                System.exit(1);  //exits the system
            }
        }


        new Thread(new Runnable() {//Sync data to the table and notify the TabelModel to change the display every 5 seconds
            @Override
            public void run() {
                try {
                    while (tag == 0) {
                        Thread.sleep(5000);
                        if (tableDataQ != null && !tableDataQ.isEmpty()) {
                            IMUContainer.datas.addAll(tableDataQ);
                            IMUContainer.tm.fireTableStructureChanged();
                            tableDataQ = new Vector<>();
//                            }
                        }
                    }
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        }).start(); //starts the thread
    }

    /*
     * The Main Method
     */
    public static void main(String[] args) {

        ExecutorService executor;
        executor = Executors.newCachedThreadPool(); //new threads will be created if required
        SerialLogger logger = new SerialLogger(new ConcurrentLinkedQueue<DataItem>(), null);
        executor.execute(logger); //execute the Seriallogger class

    }

    @Override
    public void serialEvent(SerialPortEvent serialPortEvent) {
        switch (serialPortEvent.getEventType()) {

            case SerialPortEvent.BI:  //Break interrupt.
            case SerialPortEvent.OE:  //Overrun Error
            case SerialPortEvent.FE:// Framing error.
            case SerialPortEvent.PE://Parity error.
            case SerialPortEvent.CD://Carrier detect
            case SerialPortEvent.CTS://Clear to send.
            case SerialPortEvent.DSR://Data set ready.
            case SerialPortEvent.RI: //Ring indicator.
            case SerialPortEvent.OUTPUT_BUFFER_EMPTY: //Output buffer is empty.
                break;
            case SerialPortEvent.DATA_AVAILABLE: //Data available at the serial port.
                //byte[] buff = new byte[111];
                try {
                    byte the_byte = 0;   //initialize a byte to null
                    while (inputStream.available() > 0) { //if there is data available
                        synchronized (syncObj) {
                            if (tag != 0) {
                                return;
                            }
                        }
                        the_byte = (byte) inputStream.read();  //read in the first byte and cast it as a byte
                        if (the_byte == '@') {  //if the byte variable is equal to '@' proceed
                            the_byte = (byte) inputStream.read();  //read in the second byte and cast it as a byte
                            if (the_byte == '_') {  //if the byte variable is equal to '_' proceed
                                the_byte = (byte) inputStream.read();//read in the third byte and cast it as a byte
                                if (the_byte == '@') {//if the byte variable is equal to '@' proceed
                                    int dataB_1 = Byte.toUnsignedInt((byte) inputStream.read()); // the next byte would be casted and converted to an unsigned integer
                                    int dataB_2 = Byte.toUnsignedInt((byte) inputStream.read());//the same will be done for this and the next
                                    int dataB_3 = Byte.toUnsignedInt((byte) inputStream.read());
                                    int dataB_4 = Byte.toUnsignedInt((byte) inputStream.read());
                                    /*In other to get the variable in the correct order,
                                     *the  msb has to be moved to its appropriate position as when shifted on the
                                     *Arduino sketch before been sent
                                     */
                                    int time_val = ((dataB_4) | (dataB_3 << 8) | (dataB_2 << 16) | (dataB_1 << 24));

                                    System.out.print(time_val + ",");  //display the variable

                                    dataB_1 = ((byte) inputStream.read());
                                    dataB_2 = ((byte) inputStream.read());
                                    float rollone_val = ((dataB_2 & 0xFF) | dataB_1 << 8);
                                    //System.out.println(rollone_val);

                                    dataB_1 = (byte) inputStream.read();
                                    dataB_2 = (byte) inputStream.read();
                                    float pitchone_val = ((dataB_2 & 0xFF) | dataB_1 << 8);
                                    //System.out.print(pitchone_val+",");

                                    dataB_1 = (byte) inputStream.read();
                                    dataB_2 = (byte) inputStream.read();
                                    float yawone_val = ((dataB_2 & 0xFF) | dataB_1 << 8);
                                    //System.out.print(yawone_val+",");

                                    dataB_1 = (byte) inputStream.read();
                                    dataB_2 = (byte) inputStream.read();
                                    float psione_val = ((dataB_2 & 0xFF) | dataB_1 << 8);
                                    //System.out.print(psione_val+",");

                                    dataB_1 = (byte) inputStream.read();
                                    dataB_2 = (byte) inputStream.read();
                                    float thetaone_val = ((dataB_2 & 0xFF) | dataB_1 << 8);
                                    //System.out.print(thetaone_val+",");

                                    dataB_1 = (byte) inputStream.read();
                                    dataB_2 = (byte) inputStream.read();
                                    float phione_val = ((dataB_2 & 0xFF) | dataB_1 << 8);
                                    //System.out.print(phione_val);

                                    dataB_1 = (byte) inputStream.read();
                                    dataB_2 = (byte) inputStream.read();
                                    float axone_val = ((dataB_2 & 0xFF) | dataB_1 << 8);
                                    System.out.print(axone_val + ",");

                                    dataB_1 = (byte) inputStream.read();
                                    dataB_2 = (byte) inputStream.read();
                                    float ayone_val = ((dataB_2 & 0xFF) | dataB_1 << 8);
                                    System.out.print(ayone_val + ",");

                                    dataB_1 = (byte) inputStream.read();
                                    dataB_2 = (byte) inputStream.read();
                                    float azone_val = ((dataB_2 & 0xFF) | dataB_1 << 8);
                                    System.out.print(azone_val + ",");

                                    dataB_1 = (byte) inputStream.read();
                                    dataB_2 = (byte) inputStream.read();
                                    int gxone_val = ((dataB_2 & 0xFF) | dataB_1 << 8);
                                    //System.out.print(gxone_val+",");

                                    dataB_1 = (byte) inputStream.read();
                                    dataB_2 = (byte) inputStream.read();
                                    int gyone_val = ((dataB_2 & 0xFF) | dataB_1 << 8);
                                    // System.out.print(gyone_val+",");

                                    dataB_1 = (byte) inputStream.read();
                                    dataB_2 = (byte) inputStream.read();
                                    int gzone_val = ((dataB_2 & 0xFF) | dataB_1 << 8);
                                    //System.out.println(gzone_val);

                                    dataB_1 = (byte) inputStream.read();
                                    dataB_2 = (byte) inputStream.read();
                                    int qwone_val = ((dataB_2 & 0xFF) | dataB_1 << 8);
                                    //System.out.print(qwone_val+",");

                                    dataB_1 = (byte) inputStream.read();
                                    dataB_2 = (byte) inputStream.read();
                                    int qxone_val = ((dataB_2 & 0xFF) | dataB_1 << 8);
                                    // System.out.print(gxone_val+",");

                                    dataB_1 = (byte) inputStream.read();
                                    dataB_2 = (byte) inputStream.read();
                                    int qyone_val = ((dataB_2 & 0xFF) | dataB_1 << 8);
                                    //System.out.println(qyone_val + ",");

                                    dataB_1 = (byte) inputStream.read();
                                    dataB_2 = (byte) inputStream.read();
                                    int qzone_val = ((dataB_2 & 0xFF) | dataB_1 << 8);
                                    System.out.println(qzone_val);


                                    dataB_1 = (byte) inputStream.read();
                                    dataB_2 = (byte) inputStream.read();
                                    int rolltwo_val = ((dataB_2 & 0xFF) | dataB_1 << 8);
                                    //System.out.prfloat(rolltwo_val+",");

                                    dataB_1 = (byte) inputStream.read();
                                    dataB_2 = (byte) inputStream.read();
                                    int pitchtwo_val = ((dataB_2 & 0xFF) | dataB_1 << 8);
                                    //System.out.prfloat(pitchstwo_val+",");

                                    dataB_1 = (byte) inputStream.read();
                                    dataB_2 = (byte) inputStream.read();
                                    int yawtwo_val = ((dataB_2 & 0xFF) | dataB_1 << 8);
                                    //System.out.prfloat(yawtwo_val+",");

                                    dataB_1 = (byte) inputStream.read();
                                    dataB_2 = (byte) inputStream.read();
                                    float psitwo_val = ((dataB_2 & 0xFF) | dataB_1 << 8);
                                    //System.out.print(psitwo_val+",");

                                    dataB_1 = (byte) inputStream.read();
                                    dataB_2 = (byte) inputStream.read();
                                    float thetatwo_val = ((dataB_2 & 0xFF) | dataB_1 << 8);
                                    //System.out.print(thetatwo_val+",");

                                    dataB_1 = (byte) inputStream.read();
                                    dataB_2 = (byte) inputStream.read();
                                    float phitwo_val = ((dataB_2 & 0xFF) | dataB_1 << 8);
                                    //System.out.print(phitwo_val);

                                    dataB_1 = ((byte) inputStream.read());
                                    dataB_2 = ((byte) inputStream.read());
                                    float axtwo_val = ((dataB_2 & 0xFF) | dataB_1 << 8);
                                    //System.out.print(axtwo_val+",");

                                    dataB_1 = ((byte) inputStream.read());
                                    dataB_2 = ((byte) inputStream.read());
                                    float aytwo_val = ((dataB_2 & 0xFF) | dataB_1 << 8);
                                    //System.out.print(aytwo_val+",");

                                    dataB_1 = ((byte) inputStream.read());
                                    dataB_2 = ((byte) inputStream.read());
                                    float aztwo_val = ((dataB_2 & 0xFF) | dataB_1 << 8);
                                    //System.out.print(aztwo_val + ",");

                                    dataB_1 = (byte) inputStream.read();
                                    dataB_2 = (byte) inputStream.read();
                                    int gxtwo_val = ((dataB_2 & 0xFF) | dataB_1 << 8);
                                    //System.out.print(gxtwo_val+",");

                                    dataB_1 = (byte) inputStream.read();
                                    dataB_2 = (byte) inputStream.read();
                                    int gytwo_val = ((dataB_2 & 0xFF) | dataB_1 << 8);
                                    //System.out.print(gytwo_val+",");

                                    dataB_1 = (byte) inputStream.read();
                                    dataB_2 = (byte) inputStream.read();
                                    int gztwo_val = ((dataB_2 & 0xFF) | dataB_1 << 8);
                                    //System.out.print(gztwo_val+",");

                                    dataB_1 = (byte) inputStream.read();
                                    dataB_2 = (byte) inputStream.read();
                                    int qwtwo_val = ((dataB_2 & 0xFF) | dataB_1 << 8);
                                    //System.out.println(qwtwo_val);

                                    dataB_1 = (byte) inputStream.read();
                                    dataB_2 = (byte) inputStream.read();
                                    int qxtwo_val = ((dataB_2 & 0xFF) | dataB_1 << 8);
                                    //System.out.println(qxtwo_val);

                                    dataB_1 = (byte) inputStream.read();
                                    dataB_2 = (byte) inputStream.read();
                                    int qytwo_val = ((dataB_2 & 0xFF) | dataB_1 << 8);
                                    //System.out.println(qytwo_val);

                                    dataB_1 = (byte) inputStream.read();
                                    dataB_2 = (byte) inputStream.read();
                                    int qztwo_val = ((dataB_2 & 0xFF) | dataB_1 << 8);
                                    //System.out.println(qztwo_val);


                                    dataB_1 = (byte) inputStream.read();
                                    dataB_2 = (byte) inputStream.read();
                                    float rollthree_val = ((dataB_2 & 0xFF) | dataB_1 << 8);
                                    // System.out.print(rollthree_val + ",");

                                    dataB_1 = (byte) inputStream.read();
                                    dataB_2 = (byte) inputStream.read();
                                    float pitchthree_val = ((dataB_2 & 0xFF) | dataB_1 << 8);
                                    //System.out.print(pitchthree_val+",");

                                    dataB_1 = (byte) inputStream.read();
                                    dataB_2 = (byte) inputStream.read();
                                    float yawthree_val = ((dataB_2 & 0xFF) | dataB_1 << 8);
                                    //System.out.print(yawthree_val+",");

                                    dataB_1 = (byte) inputStream.read();
                                    dataB_2 = (byte) inputStream.read();
                                    float psithree_val = ((dataB_2 & 0xFF) | dataB_1 << 8);
                                    //System.out.print(psithree_val);

                                    dataB_1 = ((byte) inputStream.read());
                                    dataB_2 = (byte) inputStream.read();
                                    float thetathree_val = ((dataB_2 & 0xFF) | dataB_1 << 8);
                                    //System.out.print(thetathree_val+",");

                                    dataB_1 = ((byte) inputStream.read());
                                    dataB_2 = (byte) inputStream.read();
                                    float phithree_val = ((dataB_2 & 0xFF) | dataB_1 << 8);
                                    //System.out.print(phithree_val);

                                    dataB_1 = ((byte) inputStream.read());
                                    dataB_2 = ((byte) inputStream.read());
                                    float axthree_val = ((dataB_2 & 0xFF) | dataB_1 << 8);
                                    //System.out.print(axthree_val + ",");

                                    dataB_1 = (byte) inputStream.read();
                                    dataB_2 = (byte) inputStream.read();
                                    float aythree_val = ((dataB_2 & 0xFF) | dataB_1 << 8);
                                    //System.out.print(aythree_val+",");

                                    dataB_1 = (byte) inputStream.read();
                                    dataB_2 = (byte) inputStream.read();
                                    float azthree_val = ((dataB_2 & 0xFF) | dataB_1 << 8);
                                    //System.out.println(azthree_val );

                                    dataB_1 = (byte) inputStream.read();
                                    dataB_2 = (byte) inputStream.read();
                                    int gxthree_val = ((dataB_2 & 0xFF) | dataB_1 << 8);
                                    //System.out.print(gxthree_val+",");

                                    dataB_1 = (byte) inputStream.read();
                                    dataB_2 = (byte) inputStream.read();
                                    int gythree_val = ((dataB_2 & 0xFF) | dataB_1 << 8);
                                    // System.out.print(gythree_val+",");

                                    dataB_1 = (byte) inputStream.read();
                                    dataB_2 = (byte) inputStream.read();
                                    int gzthree_val = ((dataB_2 & 0xFF) | dataB_1 << 8);
                                    // System.out.println(gzthree_val);

                                    dataB_1 = (byte) inputStream.read();
                                    dataB_2 = (byte) inputStream.read();
                                    int qwthree_val = ((dataB_2 & 0xFF) | dataB_1 << 8);
                                    //System.out.println(qwthree_val);

                                    dataB_1 = (byte) inputStream.read();
                                    dataB_2 = (byte) inputStream.read();
                                    int qxthree_val = ((dataB_2 & 0xFF) | dataB_1 << 8);
                                    //System.out.print(qxthree_val + ",");

                                    dataB_1 = (byte) inputStream.read();
                                    dataB_2 = (byte) inputStream.read();
                                    int qythree_val = ((dataB_2 & 0xFF) | dataB_1 << 8);
                                    //System.out.print(qythree_val + ",");

                                    dataB_1 = (byte) inputStream.read();
                                    dataB_2 = (byte) inputStream.read();
                                    int qzthree_val = ((dataB_2 & 0xFF) | dataB_1 << 8);
                                    //System.out.println(qzthree_val);

                                    this.dataQ.add(new DataItem(time_val, rollone_val, pitchone_val, yawone_val, psione_val, thetaone_val, phione_val, axone_val, ayone_val, azone_val, gxone_val, gyone_val, gzone_val, qwone_val, qxone_val, qyone_val, qzone_val, rolltwo_val, pitchtwo_val, yawtwo_val, psitwo_val, thetatwo_val, phitwo_val, axtwo_val, aytwo_val, aztwo_val, gxtwo_val, gytwo_val, gztwo_val, qwtwo_val, qxtwo_val, qytwo_val, qztwo_val, rollthree_val, pitchthree_val, yawthree_val, psithree_val, thetathree_val, phithree_val, axthree_val, aythree_val, azthree_val, gxthree_val, gythree_val, gzthree_val, qwthree_val, qxthree_val, qythree_val, qzthree_val));

                                    DataItem item = new DataItem(time_val, rollone_val, pitchone_val, yawone_val, psione_val, thetaone_val, phione_val, axone_val, ayone_val, azone_val, gxone_val, gyone_val, gzone_val, qwone_val, qxone_val, qyone_val, qzone_val, rolltwo_val, pitchtwo_val, yawtwo_val, psitwo_val, thetatwo_val, phitwo_val, axtwo_val, aytwo_val, aztwo_val, gxtwo_val, gytwo_val, gztwo_val, qwtwo_val, qxtwo_val, qytwo_val, qztwo_val, rollthree_val, pitchthree_val, yawthree_val, psithree_val, thetathree_val, phithree_val, axthree_val, aythree_val, azthree_val, gxthree_val, gythree_val, gzthree_val, qwthree_val, qxthree_val, qythree_val, qzthree_val);
                                    //this.dataQ.add(item);

                                    synchronized (syncObj) {
                                        tableDataQ.add(item);  //sync data to the table and notify the TableModdel to update in real time
                                    }
                                }
                            }

                        }


                    }


                } catch (IOException e) {
                    System.out.println(e);
                }
                //System.out.println("Done reading data!");
                break;
        }

    }

    /*
     * This method will close the serial port and input streams and
     * will throw an exception if there is an error
     */
    public void stop() {
        try {
            inputStream.close();  //close the input stream
        } catch (Exception e1) {
            System.out.println("close inputsteam error:");
            e1.printStackTrace();
        }
        try {
            serialPort.close();
        } catch (Exception e2) {
            System.out.println("close  serial port error:");
            e2.printStackTrace();
        }
    }

    /*
     * This method must be called up in Runnable threads
     * to begin the thread
     */
    @Override
    public void run() {
    }

}
