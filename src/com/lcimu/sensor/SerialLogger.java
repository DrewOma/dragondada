package com.lcimu.sensor;

import gnu.io.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.TooManyListenersException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * Created by mwelch8 on 18/11/2015.
 */
public class SerialLogger implements Runnable, SerialPortEventListener{

    private SerialPort serialPort;
    private InputStream inputStream;
    private static CommPortIdentifier portId;
    private static Enumeration portList;
    private ConcurrentLinkedQueue<DataItem> dataQ;


    public SerialLogger(ConcurrentLinkedQueue<DataItem> dataQ, String serial){

        this.dataQ = dataQ;

        //System.out.println(java.library.path);

        serialPort=null;
        portList = CommPortIdentifier.getPortIdentifiers();

        while (portList.hasMoreElements()) {
            portId = (CommPortIdentifier) portList.nextElement();
            if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                if (portId.getName().equals(serial)) {
                    try {
                        serialPort = (SerialPort) portId.open("SimpleReadApp", 2000);
                    } catch (PortInUseException e) {
                        System.out.println(e);
                    }
                }
            }
        }

        if(serialPort==null){

            System.out.println("Serial Port not found...Exiting.");
            System.exit(1);

        }else {


            try {
                inputStream = serialPort.getInputStream();
            } catch (IOException e) {
                System.out.println(e);
                System.out.println("....Exiting.");
                System.exit(1);
            }
            try {
                serialPort.addEventListener(this);
            } catch (TooManyListenersException e) {
                System.out.println(e);
                System.out.println("....Exiting.");
                System.exit(1);
            }
            serialPort.notifyOnDataAvailable(true);
            try {
                serialPort.setSerialPortParams(115200,
                        SerialPort.DATABITS_8,
                        SerialPort.STOPBITS_1,
                        SerialPort.PARITY_NONE);
            } catch (UnsupportedCommOperationException e) {
                System.out.println(e);
                System.out.println("....Exiting.");
                System.exit(1);
            }
        }



    }

    @Override
    public void serialEvent(SerialPortEvent serialPortEvent) {


        switch(serialPortEvent.getEventType()) {

            case SerialPortEvent.BI:
            case SerialPortEvent.OE:
            case SerialPortEvent.FE:
            case SerialPortEvent.PE:
            case SerialPortEvent.CD:
            case SerialPortEvent.CTS:
            case SerialPortEvent.DSR:
            case SerialPortEvent.RI:
            case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
                break;
            case SerialPortEvent.DATA_AVAILABLE:
                byte[] buff = new byte[111];
                // inputStream.read(buff);
                // int dataB_1 = (byte)buff[3];
                // int dataB_2 = (byte)buff[4];
                // int dataB_3 = (byte)buff[5];
                // int dataB_4 = (byte)buff[6];
                try {
                    byte the_byte=0;
                    while (inputStream.available() > 0) {
                        the_byte = (byte)inputStream.read();
                        if(the_byte == '@'){
                            the_byte = (byte)inputStream.read();
                            if(the_byte == '_') {
                                the_byte = (byte) inputStream.read();
                                if (the_byte == '@') {
                                    int  dataB_1 =  Byte.toUnsignedInt((byte) inputStream.read());
                                    int dataB_2 =  Byte.toUnsignedInt((byte) inputStream.read());
                                    int dataB_3 = Byte.toUnsignedInt((byte) inputStream.read());
                                    int dataB_4 = Byte.toUnsignedInt((byte) inputStream.read());
                                    int time_val = ((dataB_4 ) | (dataB_3 << 8)  | (dataB_2  << 16) | (dataB_1 << 24));

                                    System.out.print(time_val+",");

                                    dataB_1 =  ((byte) inputStream.read());
                                    dataB_2 =  ((byte) inputStream.read());
                                    float rollone_val = ((dataB_2  & 0xFF) | dataB_1  << 8);
                                    //System.out.println(rollone_val);

                                    dataB_1 = (byte) inputStream.read();
                                    dataB_2 = (byte) inputStream.read();
                                    float pitchone_val = ((dataB_2  & 0xFF) | dataB_1  << 8);
                                    //System.out.print(pitchone_val+",");

                                    dataB_1 = (byte) inputStream.read();
                                    dataB_2 = (byte) inputStream.read();
                                    float yawone_val = ((dataB_2  & 0xFF) | dataB_1  << 8);
                                    //System.out.print(yawone_val+",");

                                    dataB_1 = (byte) inputStream.read();
                                    dataB_2 = (byte) inputStream.read();
                                    float psione_val = ((dataB_2  & 0xFF) | dataB_1  << 8);
                                    //System.out.print(psione_val+",");

                                    dataB_1 = (byte) inputStream.read();
                                    dataB_2 = (byte) inputStream.read();
                                    float thetaone_val = ((dataB_2  & 0xFF) | dataB_1  << 8);
                                    //System.out.print(thetaone_val+",");

                                    dataB_1 = (byte) inputStream.read();
                                    dataB_2 = (byte) inputStream.read();
                                    float phione_val = ((dataB_2  & 0xFF) | dataB_1  << 8);
                                    //System.out.print(phione_val);

                                    dataB_1 = (byte) inputStream.read();
                                    dataB_2 = (byte) inputStream.read();
                                    float axone_val = ((dataB_2  & 0xFF) | dataB_1  << 8);
                                    System.out.print(axone_val+",");

                                    dataB_1 = (byte) inputStream.read();
                                    dataB_2 = (byte) inputStream.read();
                                    float ayone_val = ((dataB_2  & 0xFF) | dataB_1  << 8);
                                    System.out.print(ayone_val+",");

                                    dataB_1 = (byte) inputStream.read();
                                    dataB_2 = (byte) inputStream.read();
                                    float azone_val = ((dataB_2  & 0xFF) | dataB_1  << 8);
                                    System.out.print(azone_val + ",");

                                    dataB_1 = (byte) inputStream.read();
                                    dataB_2 = (byte) inputStream.read();
                                    int gxone_val = ((dataB_2  & 0xFF) | dataB_1  << 8);
                                    //System.out.print(gxone_val+",");

                                    dataB_1 = (byte) inputStream.read();
                                    dataB_2 = (byte) inputStream.read();
                                    int gyone_val = ((dataB_2  & 0xFF) | dataB_1  << 8);
                                    // System.out.print(gyone_val+",");

                                    dataB_1 = (byte) inputStream.read();
                                    dataB_2 = (byte) inputStream.read();
                                    int gzone_val = ((dataB_2  & 0xFF) | dataB_1  << 8);
                                    //System.out.println(gzone_val);

                                    dataB_1 = (byte) inputStream.read();
                                    dataB_2 = (byte) inputStream.read();
                                    int qwone_val = ((dataB_2  & 0xFF) | dataB_1  << 8);
                                    //System.out.print(qwone_val+",");

                                    dataB_1 = (byte) inputStream.read();
                                    dataB_2 = (byte) inputStream.read();
                                    int qxone_val = ((dataB_2  & 0xFF) | dataB_1  << 8);
                                    // System.out.print(gxone_val+",");

                                    dataB_1 = (byte) inputStream.read();
                                    dataB_2 = (byte) inputStream.read();
                                    int qyone_val = ((dataB_2  & 0xFF) | dataB_1  << 8);
                                    //System.out.println(qyone_val + ",");

                                    dataB_1 = (byte) inputStream.read();
                                    dataB_2 = (byte) inputStream.read();
                                    int qzone_val = ((dataB_2  & 0xFF) | dataB_1  << 8);
                                    System.out.println(qzone_val);


//                                    dataB_1 =  Byte.toUnsignedInt((byte) inputStream.read());
//                                    dataB_2 =  Byte.toUnsignedInt((byte) inputStream.read());
//                                    dataB_3 =  Byte.toUnsignedInt((byte) inputStream.read());
//                                    dataB_4 = Byte.toUnsignedInt((byte) inputStream.read());
//                                    int time_vall = ((dataB_4 ) | ((dataB_3) << 8)  | (dataB_2  << 16) | (dataB_1 << 24));
                                    //System.out.prfloatln(time_vall+",");

                                    dataB_1 = (byte) inputStream.read();
                                    dataB_2 = (byte) inputStream.read();
                                    int rolltwo_val = ((dataB_2  & 0xFF) | dataB_1  << 8);
                                    //System.out.prfloat(rolltwo_val+",");

                                    dataB_1 = (byte) inputStream.read();
                                    dataB_2 = (byte) inputStream.read();
                                    int pitchtwo_val = ((dataB_2  & 0xFF) | dataB_1  << 8);
                                    //System.out.prfloat(pitchstwo_val+",");

                                    dataB_1 =  (byte) inputStream.read();
                                    dataB_2 = (byte) inputStream.read();
                                    int yawtwo_val = ((dataB_2  & 0xFF) | dataB_1  << 8);
                                    //System.out.prfloat(yawtwo_val+",");

                                    dataB_1 = (byte) inputStream.read();
                                    dataB_2 = (byte) inputStream.read();
                                    float psitwo_val = ((dataB_2  & 0xFF) | dataB_1  << 8);
                                    //System.out.print(psitwo_val+",");

                                    dataB_1 = (byte) inputStream.read();
                                    dataB_2 = (byte) inputStream.read();
                                    float thetatwo_val = ((dataB_2  & 0xFF) | dataB_1  << 8);
                                    //System.out.print(thetatwo_val+",");

                                    dataB_1 = (byte) inputStream.read();
                                    dataB_2 = (byte) inputStream.read();
                                    float phitwo_val = ((dataB_2  & 0xFF) | dataB_1  << 8);
                                    //System.out.print(phitwo_val);

                                    dataB_1 = ((byte) inputStream.read());
                                    dataB_2 = ((byte) inputStream.read());
                                    float axtwo_val = ((dataB_2  & 0xFF) | dataB_1  << 8);
                                    //System.out.print(axtwo_val+",");

                                    dataB_1 =   ((byte) inputStream.read());
                                    dataB_2 =   ((byte) inputStream.read());
                                    float aytwo_val = ((dataB_2  & 0xFF) | dataB_1  << 8);
                                    //System.out.print(aytwo_val+",");

                                    dataB_1 =   ((byte) inputStream.read());
                                    dataB_2 =   ((byte) inputStream.read());
                                    float aztwo_val = ((dataB_2  & 0xFF) | dataB_1  << 8);
                                    //System.out.print(aztwo_val + ",");

                                    dataB_1 = (byte) inputStream.read();
                                    dataB_2 = (byte) inputStream.read();
                                    int gxtwo_val = ((dataB_2 & 0xFF) | dataB_1  << 8);
                                    //System.out.print(gxtwo_val+",");

                                    dataB_1 = (byte) inputStream.read();
                                    dataB_2 = (byte) inputStream.read();
                                    int gytwo_val = ((dataB_2 & 0xFF) | dataB_1  << 8);
                                    //System.out.print(gytwo_val+",");

                                    dataB_1 = (byte) inputStream.read();
                                    dataB_2 = (byte) inputStream.read();
                                    int gztwo_val = ((dataB_2 & 0xFF) | dataB_1  << 8);
                                    //System.out.print(gztwo_val+",");

                                    dataB_1 = (byte) inputStream.read();
                                    dataB_2 = (byte) inputStream.read();
                                    int qwtwo_val = ((dataB_2 & 0xFF) | dataB_1  << 8);
                                    //System.out.println(qwtwo_val);

                                    dataB_1 = (byte) inputStream.read();
                                    dataB_2 = (byte) inputStream.read();
                                    int qxtwo_val = ((dataB_2 & 0xFF) | dataB_1  << 8);
                                    //System.out.println(qxtwo_val);

                                    dataB_1 = (byte) inputStream.read();
                                    dataB_2 = (byte) inputStream.read();
                                    int qytwo_val = ((dataB_2 & 0xFF) | dataB_1  << 8);
                                    //System.out.println(qytwo_val);

                                    dataB_1 = (byte) inputStream.read();
                                    dataB_2 = (byte) inputStream.read();
                                    int qztwo_val = ((dataB_2 & 0xFF) | dataB_1  << 8);
                                    //System.out.println(qztwo_val);

//                                    dataB_1 =  Byte.toUnsignedInt((byte) inputStream.read());
//                                    dataB_2 =  Byte.toUnsignedInt((byte) inputStream.read());
//                                    dataB_3 =  Byte.toUnsignedInt((byte) inputStream.read());
//                                    dataB_4 = Byte.toUnsignedInt((byte) inputStream.read());
//                                    int time_valll = ((dataB_4 ) | ((dataB_3) << 8)  | (dataB_2  << 16) | (dataB_1 << 24));
//                                    System.out.println(time_valll + ",");

                                    dataB_1 = (byte) inputStream.read();
                                    dataB_2 = (byte) inputStream.read();
                                    float rollthree_val = ((dataB_2  & 0xFF) | dataB_1  << 8);
                                    // System.out.print(rollthree_val + ",");

                                    dataB_1 = (byte) inputStream.read();
                                    dataB_2 = (byte) inputStream.read();
                                    float pitchthree_val = ((dataB_2  & 0xFF) | dataB_1  << 8);
                                    //System.out.print(pitchthree_val+",");

                                    dataB_1 = (byte) inputStream.read();
                                    dataB_2 = (byte) inputStream.read();
                                    float yawthree_val = ((dataB_2  & 0xFF) | dataB_1  << 8);
                                    //System.out.print(yawthree_val+",");

                                    dataB_1 = (byte) inputStream.read();
                                    dataB_2 = (byte) inputStream.read();
                                    float psithree_val = ((dataB_2  & 0xFF) | dataB_1  << 8);
                                    //System.out.print(psithree_val);

                                    dataB_1 = ((byte) inputStream.read());
                                    dataB_2 = (byte) inputStream.read();
                                    float thetathree_val = ((dataB_2  & 0xFF) | dataB_1  << 8);
                                    //System.out.print(thetathree_val+",");

                                    dataB_1 = ((byte) inputStream.read());
                                    dataB_2 = (byte) inputStream.read();
                                    float phithree_val = ((dataB_2  & 0xFF) | dataB_1  << 8);
                                    //System.out.print(phithree_val);

                                    dataB_1 =   ((byte) inputStream.read());
                                    dataB_2 =   ((byte) inputStream.read());
                                    float axthree_val = ((dataB_2  & 0xFF) | dataB_1  << 8);
                                    //System.out.print(axthree_val + ",");

                                    dataB_1 =   (byte) inputStream.read();
                                    dataB_2 =   (byte) inputStream.read();
                                    float aythree_val = ((dataB_2  & 0xFF) | dataB_1  << 8);
                                    //System.out.print(aythree_val+",");

                                    dataB_1 =   (byte) inputStream.read();
                                    dataB_2 =   (byte) inputStream.read();
                                    float azthree_val = ((dataB_2  & 0xFF) | dataB_1  << 8);
                                    //System.out.println(azthree_val );

                                    dataB_1 = (byte) inputStream.read();
                                    dataB_2 = (byte) inputStream.read();
                                    int gxthree_val = ((dataB_2  & 0xFF) | dataB_1  << 8);
                                    //System.out.print(gxthree_val+",");

                                    dataB_1 = (byte) inputStream.read();
                                    dataB_2 = (byte) inputStream.read();
                                    int gythree_val = ((dataB_2  & 0xFF) | dataB_1  << 8);
                                    // System.out.print(gythree_val+",");

                                    dataB_1 = (byte) inputStream.read();
                                    dataB_2 = (byte) inputStream.read();
                                    int gzthree_val = ((dataB_2  & 0xFF) | dataB_1  << 8);
                                    // System.out.println(gzthree_val);

                                    dataB_1 = (byte) inputStream.read();
                                    dataB_2 = (byte) inputStream.read();
                                    int qwthree_val = ((dataB_2  & 0xFF) | dataB_1  << 8);
                                    //System.out.println(qwthree_val);

                                    dataB_1 = (byte) inputStream.read();
                                    dataB_2 = (byte) inputStream.read();
                                    int qxthree_val = ((dataB_2  & 0xFF) | dataB_1  << 8);
                                    //System.out.print(qxthree_val + ",");

                                    dataB_1 = (byte) inputStream.read();
                                    dataB_2 = (byte) inputStream.read();
                                    int qythree_val = ((dataB_2  & 0xFF) | dataB_1  << 8);
                                    //System.out.print(qythree_val + ",");

                                    dataB_1 = (byte) inputStream.read();
                                    dataB_2 = (byte) inputStream.read();
                                    int qzthree_val = ((dataB_2  & 0xFF) | dataB_1  << 8);
                                    //System.out.println(qzthree_val);

                                    //this.dataQ.add(new DataItem(time_val, rollone_val, pitchone_val, yawone_val, psione_val, thetaone_val, phione_val, axone_val, ayone_val, azone_val, gxone_val, gyone_val, gzone_val,qwone_val, qxone_val,qyone_val, qzone_val, time_vall, rolltwo_val, pitchtwo_val, yawtwo_val, psitwo_val, thetatwo_val, phitwo_val,axtwo_val, aytwo_val, aztwo_val, gxtwo_val, gytwo_val, gztwo_val, qwtwo_val, qxtwo_val, qytwo_val, qztwo_val, time_valll, rollthree_val, pitchthree_val, yawthree_val, psithree_val, thetathree_val, phithree_val,axthree_val, aythree_val, azthree_val, gxthree_val, gythree_val, gzthree_val, qwthree_val, qxthree_val, qythree_val, qzthree_val));
                                    this.dataQ.add(new DataItem(time_val, rollone_val, pitchone_val, yawone_val, psione_val, thetaone_val, phione_val, axone_val, ayone_val, azone_val, gxone_val, gyone_val, gzone_val,qwone_val, qxone_val,qyone_val, qzone_val, rolltwo_val, pitchtwo_val, yawtwo_val, psitwo_val, thetatwo_val, phitwo_val,axtwo_val, aytwo_val, aztwo_val, gxtwo_val, gytwo_val, gztwo_val, qwtwo_val, qxtwo_val, qytwo_val, qztwo_val, rollthree_val, pitchthree_val, yawthree_val, psithree_val, thetathree_val, phithree_val,axthree_val, aythree_val, azthree_val, gxthree_val, gythree_val, gzthree_val, qwthree_val, qxthree_val, qythree_val, qzthree_val));

                                    //System.out.println();

                                }
                            }

                        }

                        //System.out.print((byte)the_byte+",");

                    }
                    //System.out.println();
                    //System.out.print(numBytes+": "+readBuffer);

                } catch (IOException e) {System.out.println(e);}
                //System.out.println("Done reading data!");
                break;
        }

    }

    @Override
    public void run() {
        /*try {
            //Thread.sleep(20000);
        } catch (InterruptedException e) {System.out.println(e); }*/
    }

    public static void main(String[] args) {

        ExecutorService executor;
        executor = Executors.newCachedThreadPool();
        SerialLogger logger = new SerialLogger(new ConcurrentLinkedQueue<DataItem>(),null);
        executor.execute(logger);

    }

}
