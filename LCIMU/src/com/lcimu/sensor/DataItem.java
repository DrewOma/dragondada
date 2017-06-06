package com.lcimu.sensor;

/**
 * This sketch was modified from that
 * developed my Mitchell Welch, 2015

 * The DataItem class will be used to instantiate
 * all the incoming serial sensor data variables
 */
public class DataItem {

    private int time_val = 0;  // Initialize the sensor time variable
    private float rollone_val = 0; // Initialize the roll variable of the first sensor
    private float pitchone_val = 0;// Initialize the pitch variable of the first sensor
    private float yawone_val = 0;// Initialize the yaw variable of the first sensor
    private float psione_val = 0;// Initialize the psi variable of the first sensor
    private float thetaone_val = 0;// Initialize the theta variable of the first sensor
    private float phione_val = 0;// Initialize the phi variable of the first sensor
    private float axone_val = 0;// Initialize the x-axis acceleration variable of the first sensor
    private float ayone_val = 0;// Initialize the y-axis acceleration variable of the first sensor
    private float azone_val = 0;// Initialize the z-axis acceleration variable of the first sensor
    private int gxone_val = 0;// Initialize the x-axis angular acceleration variable of the first sensor
    private int gyone_val = 0;// Initialize the y-axis angular acceleration variable of the first sensor
    private int gzone_val = 0;// Initialize the z-axis angular acceleration variable of the first sensor
    private int qwone_val = 0;// Initialize the w-axis quaternion variable of the first sensor
    private int qxone_val = 0;// Initialize the x-axis quaternion variable of the first sensor
    private int qyone_val = 0;// Initialize the y-axis quaternion variable of the first sensor
    private int qzone_val = 0;// Initialize the z-axis quaternion variable of the first sensor
    //private int time_vall;
    private float rolltwo_val = 0;// Initialize the roll variable of the second sensor
    private float pitchtwo_val = 0;//Initialize the pitch variable of the second sensor
    private float yawtwo_val = 0; // Initialize the yaw variable of the second sensor
    private float psitwo_val = 0; // Initialize the psi variable of the second sensor
    private float thetatwo_val = 0; //Initialize the theta variable of the second sensor
    private float phitwo_val = 0;// Initialize the phi variable of the second sensor
    private float axtwo_val = 0; // Initialize the x-axis acceleration variable of the second sensor
    private float aytwo_val = 0; // Initialize the y-axis acceleration variable of the second sensor
    private float aztwo_val = 0; // Initialize the z-axis acceleration variable of the second sensor
    private int gxtwo_val = 0;// Initialize the x-axis angular acceleration variable of the second sensor
    private int gytwo_val = 0;// Initialize the y-axis angular acceleration variable of the second sensor
    private int gztwo_val = 0;// Initialize the z-axis angular acceleration variable of the second sensor
    private int qwtwo_val = 0;// Initialize the w-axis quaternion variable of the second sensor
    private int qxtwo_val = 0;// Initialize the x-axis quaternion variable of the second sensor
    private int qytwo_val = 0;// Initialize the y-axis quaternion variable of the second sensor
    private int qztwo_val = 0;// Initialize the z-axis quaternion variable of the second sensor
    private float rollthree_val = 0; // Initialize the roll variable of the third sensor
    private float pitchthree_val = 0;// Initialize the pitch variable of the third sensor
    private float yawthree_val = 0;// Initialize the yaw variable of the third sensor
    private float psithree_val = 0;// Initialize the psi variable of the third sensor
    private float thetathree_val = 0;// Initialize the theta variable of the third sensor
    private float phithree_val = 0;// Initialize the phi variable of the third sensor
    private float axthree_val = 0;// Initialize the x-axis acceleration variable of the third sensor
    private float aythree_val = 0;// Initialize the y-axis acceleration variable of the third sensor
    private float azthree_val = 0;// Initialize the z-axis acceleration variable of the third sensor
    private int gxthree_val = 0;// Initialize the x-axis angular acceleration variable of the third sensor
    private int gythree_val = 0;// Initialize the y-axis angular acceleration variable of the third sensor
    private int gzthree_val = 0;// Initialize the z-axis angular acceleration variable of the third sensor
    private int qwthree_val = 0;// Initialize the w-axis quaternion variable of the third sensor
    private int qxthree_val = 0;// Initialize the x-axis quaternion variable of the third sensor
    private int qythree_val = 0;// Initialize the y-axis quaternion variable of the third sensor
    private int qzthree_val = 0;// Initialize the z-axis quaternion variable of the third sensor

    //An empty constructor
    public DataItem() {
    }

    /*
     * This constructor receives multiple variable parameters as arguments and assigns them to
     * the initialized variables.
     */
    public DataItem(int tyme, float rollx, float comx, float gyrx, float pit, float comy, float gyry, float axxx, float ayxx, float azonex, int gxxx, int gyoney, int gzone, int qwone, int qxone, int qyone, int qzone, float rollxx, float comxx, float gyrxx, float pity, float comyy, float gyryy, float coyxx, float cozxx, float gyyxx, int pactry, int cactus, int gocto, int qwtwo, int qxtwo, int qytwo, int qztwo, float rollxxx, float comxxx, float gyrxxx, float pityy, float comyyy, float gyryyy, float axthee, float aythee, float azthee, int gxthee, int gythee, int gzthee, int qwthree, int qxthree, int qythree, int qzthree) {

        time_val = tyme;
        rollone_val = rollx;
        pitchone_val = comx;
        yawone_val = gyrx;
        psione_val = pit;
        thetaone_val = comy;
        phione_val = gyry;
        axone_val = axxx;
        ayone_val = ayxx;
        azone_val = azonex;
        gxone_val = gxxx;
        gyone_val = gyoney;
        gzone_val = gzone;
        qwone_val = qwone;
        qxone_val = qxone;
        qyone_val = qyone;
        qzone_val = qzone;
        //time_vall = tymee;
        rolltwo_val = rollxx;
        pitchtwo_val = comxx;
        yawtwo_val = gyrxx;
        psitwo_val = pity;
        thetatwo_val = comyy;
        phitwo_val = gyryy;
        axtwo_val = coyxx;
        aytwo_val = cozxx;
        aztwo_val = gyyxx;
        gxtwo_val = pactry;
        gytwo_val = cactus;
        gztwo_val = gocto;
        qwtwo_val = qwtwo;
        qxtwo_val = qxtwo;
        qytwo_val = qytwo;
        qztwo_val = qztwo;
        //time_valll = tymeee;
        rollthree_val = rollxxx;
        pitchthree_val = comxxx;
        yawthree_val = gyrxxx;
        psithree_val = pityy;
        thetathree_val = comyyy;
        phithree_val = gyryyy;
        axthree_val = axthee;
        aythree_val = aythee;
        azthree_val = azthee;
        gxthree_val = gxthee;
        gythree_val = gythee;
        gzthree_val = gzthee;
        qwthree_val = qwthree;
        qxthree_val = qxthree;
        qythree_val = qythree;
        qzthree_val = qzthree;

    }

    /*
     * This whole section would contain a list of Get methods that returns the initialized sensor variables
     */
    public int getTime_val() {
        return time_val;
    }

    /*
     * This whole section would contain a list of set methods that sets all initialized variables
     */
    public void setTime_val(int time_val) {
        this.time_val = time_val;
    }

    public float getRollone_val() {
        return rollone_val;
    }

    public void setRollone_val(float rollone_val) {
        this.rollone_val = rollone_val;
    }

    public float getPitchone_val() {
        return (pitchone_val);
    }

    public void setPitchone_val(float compx_val) {
        this.pitchone_val = compx_val;
    }

    public float getYawone_val() {
        return (yawone_val);
    }

    public void setYawone_val(float gyrox_val) {
        this.yawone_val = gyrox_val;
    }

    public float getPsione_val() {
        return psione_val;
    }

    public void setPsione_val(float pitchone_val) {
        this.psione_val = pitchone_val;
    }

    public float getThetaone_val() {
        return thetaone_val;
    }

    public void setThetaone_val(float compy_val) {
        this.thetaone_val = compy_val;
    }

    public float getPhione_val() {
        return phione_val;
    }

    public void setPhione_val(float gyroy_val) {
        this.phione_val = gyroy_val;
    }

    public float getAxone_val() {
        return axone_val;
    }

    public void setAxone_val(float axtone_val) {
        this.axone_val = axtone_val;
    }

    public float getAyone_val() {
        return (ayone_val);
    }

    public void setAyone_val(float aytone_val) {
        this.ayone_val = aytone_val;
    }

    public float getAzone_val() {
        return (azone_val);
    }

    public void setAzone_val(float aztone_val) {
        this.azone_val = aztone_val;
    }

    public int getGxone_val() {
        return gxone_val;
    }

    public void setGxone_val(int gxtone_val) {
        this.gxone_val = gxtone_val;
    }

    public int getGyone_val() {
        return (gyone_val);
    }

    public void setGyone_val(int gytone_val) {
        this.gyone_val = gytone_val;
    }

    public int getGzone_val() {
        return gzone_val;
    }

    public void setGzone_val(int gztone_val) {
        this.gzone_val = gztone_val;
    }

    public int getQwone_val() {
        return qwone_val;
    }

    public void setQwone_val(int qwtone_val) {
        this.qwone_val = qwtone_val;
    }

    public int getQxone_val() {
        return qxone_val;
    }

    public void setQxone_val(int qxtone_val) {
        this.qxone_val = qxtone_val;
    }

    public int getQyone_val() {
        return qyone_val;
    }

    public void setQyone_val(int qytone_val) {
        this.qyone_val = qytone_val;
    }

    public int getQzone_val() {
        return qzone_val;
    }

    public void setQzone_val(int qztone_val) {
        this.qzone_val = qztone_val;
    }

    public float getRolltwo_val() {
        return rolltwo_val;
    }

    public void setRolltwo_val(float rolltwo_val) {
        this.rolltwo_val = rolltwo_val;
    }

    public float getPitchtwo_val() {
        return pitchtwo_val;
    }

    public float getYawtwo_val() {
        return yawtwo_val;
    }

    public void setYawtwo_val(float yawtwo_val) {
        this.yawtwo_val = yawtwo_val;
    }

    public float getPsitwo_val() {
        return psitwo_val;
    }

    public void setPsitwo_val(float pitchtwo_val) {
        this.psitwo_val = pitchtwo_val;
    }

    public float getThetatwo_val() {
        return thetatwo_val;
    }

    public void setThetatwo_val(float compyy_val) {
        this.thetatwo_val = compyy_val;
    }

    public float getPhitwo_val() {
        return phitwo_val;
    }

    public void setPhitwo_val(float gyroyy_val) {
        this.phitwo_val = gyroyy_val;
    }

    public float getAxtwo_val() {
        return (axtwo_val);
    }

    public void setAxtwo_val(float axtwone_val) {
        this.axtwo_val = axtwone_val;
    }

    public float getAytwo_val() {
        return (aytwo_val);
    }

    public float getAztwo_val() {
        return (aztwo_val);
    }

    public void setAztwo_val(float aztwone_val) {
        this.aztwo_val = aztwone_val;
    }

    public int getGxtwo_val() {
        return (gxtwo_val);
    }

    public void setGxtwo_val(int gxtwone_val) {
        this.gxtwo_val = gxtwone_val;
    }

    public int getGytwo_val() {
        return (gytwo_val);
    }

    public void setGytwo_val(int gytwone_val) {
        this.gytwo_val = gytwone_val;
    }

    public int getGztwo_val() {
        return (gztwo_val);
    }

    public void setGztwo_val(int gztwone_val) {
        this.gztwo_val = gztwone_val;
    }

    public int getQwtwo_val() {
        return qwtwo_val;
    }

    public void setQwtwo_val(int qwtwo_val) {
        this.qwtwo_val = qwtwo_val;
    }

    public int getQxtwo_val() {
        return qxtwo_val;
    }

    public void setQxtwo_val(int gxtwone_val) {
        this.qxtwo_val = gxtwone_val;
    }

    public int getQytwo_val() {
        return qytwo_val;
    }

    public void setQytwo_val(int gytwone_val) {
        this.qytwo_val = gytwone_val;
    }

    public int getQztwo_val() {
        return qztwo_val;
    }

    public void setQztwo_val(int gztwone_val) {
        this.qztwo_val = gztwone_val;
    }

    public float getRollthree_val() {
        return rollthree_val;
    }

    public void setRollthree_val(float rollthree_val) {
        this.rollthree_val = rollthree_val;
    }

    public float getPitchthree_val() {
        return pitchthree_val;
    }

    public float getYawthree_val() {
        return yawthree_val;
    }

    public void setYawthree_val(float gyroxxx_val) {
        this.yawthree_val = gyroxxx_val;
    }

    public float getPsithree_val() {
        return psithree_val;
    }

    public void setPsithree_val(float pitchthree_val) {
        this.psithree_val = pitchthree_val;
    }

    public float getThetathree_val() {
        return thetathree_val;
    }

    public void setThetathree_val(float compyyy_val) {
        this.thetathree_val = compyyy_val;
    }

    public float getPhithree_val() {
        return phithree_val;
    }

    public void setPhithree_val(float gyroyyy_val) {
        this.phithree_val = gyroyyy_val;
    }

    public float getAxthree_val() {
        return (axthree_val);
    }

    public void setAxthree_val(float axthrone_val) {
        this.axthree_val = axthrone_val;
    }

    public float getAythree_val() {
        return (aythree_val);
    }

    public float getAzthree_val() {
        return (azthree_val);
    }

    public void setAzthree_val(float azthrone_val) {
        this.azthree_val = azthrone_val;
    }

    public int getGxthree_val() {
        return (gxthree_val);
    }

    public void setGxthree_val(int gxthrone_val) {
        this.gxthree_val = gxthrone_val;
    }

    public int getGythree_val() {
        return (gythree_val);
    }

    public void setGythree_val(int gythrone_val) {
        this.gythree_val = gythrone_val;
    }

    public int getGzthree_val() {
        return (gzthree_val);
    }

    public void setGzthree_val(int gzthrone_val) {
        this.gzthree_val = gzthrone_val;
    }

    public int getQwthree_val() {
        return qwthree_val;
    }

    public void setQwthree_val(int gxthrone_val) {
        this.qwthree_val = gxthrone_val;
    }

    public int getQxthree_val() {
        return qxthree_val;
    }

    public void setQxthree_val(int gythrone_val) {
        this.qxthree_val = gythrone_val;
    }

    public int getQythree_val() {
        return qythree_val;
    }

    public void setQythree_val(int gzthrone_val) {
        this.qythree_val = gzthrone_val;
    }

    public int getQzthree_val() {
        return qzthree_val;
    }

    public void setQzthree_val(int gzthrone_val) {
        this.qzthree_val = gzthrone_val;
    }

    public void setPitchtwo_val_val(float pitchtwo_val) {
        this.pitchtwo_val = pitchtwo_val;
    }

    public void setAytwo_val_val(float aytwone_val) {
        this.aytwo_val = aytwone_val;
    }

    public void setPitchthree_val_val(float compxxx_val) {
        this.pitchthree_val = compxxx_val;
    }

    public void setAythree_val_val(float aythrone_val) {
        this.aythree_val = aythrone_val;
    }

    /**
     * This method is used to get data by the column ordinal
     * which will be used to display data on the Jtable
     *
     * @param columnOrdinal
     * @return
     */
    public String getDataByColumnOrdinal(int columnOrdinal) {
        switch (columnOrdinal) {
            case 0:
                return String.valueOf(getTime_val());
            case 1:
                return String.valueOf(getRollone_val());
            case 2:
                return String.valueOf(getPitchone_val());
            case 3:
                return String.valueOf(getYawone_val());
            case 4:
                return String.valueOf(getAxone_val());
            case 5:
                return String.valueOf(getAyone_val());
            case 6:
                return String.valueOf(getAzone_val());
            case 7:
                return String.valueOf(getGxone_val());
            case 8:
                return String.valueOf(getGyone_val());
            case 9:
                return String.valueOf(getGzone_val());
            case 10:
                return String.valueOf(getQwone_val());
            case 11:
                return String.valueOf(getQxone_val());
            case 12:
                return String.valueOf(getQyone_val());
            case 13:
                return String.valueOf(getQzone_val());
            case 14:
                return String.valueOf(getRolltwo_val());
            case 15:
                return String.valueOf(getPitchtwo_val());
            case 16:
                return String.valueOf(getYawtwo_val());
            case 17:
                return String.valueOf(getAxtwo_val());
            case 18:
                return String.valueOf(getAytwo_val());
            case 19:
                return String.valueOf(getAztwo_val());
            case 20:
                return String.valueOf(getGxtwo_val());
            case 21:
                return String.valueOf(getGytwo_val());
            case 22:
                return String.valueOf(getGztwo_val());
            case 23:
                return String.valueOf(getQwtwo_val());
            case 24:
                return String.valueOf(getQxtwo_val());
            case 25:
                return String.valueOf(getQytwo_val());
            case 26:
                return String.valueOf(getQztwo_val());
            case 27:
                return String.valueOf(getRollthree_val());
            case 28:
                return String.valueOf(getPitchthree_val());
            case 29:
                return String.valueOf(getYawthree_val());
            case 30:
                return String.valueOf(getAxthree_val());
            case 31:
                return String.valueOf(getAythree_val());
            case 32:
                return String.valueOf(getAzthree_val());
            case 33:
                return String.valueOf(getGxthree_val());
            case 34:
                return String.valueOf(getGythree_val());
            case 35:
                return String.valueOf(getGzthree_val());
            case 36:
                return String.valueOf(getQwthree_val());
            case 37:
                return String.valueOf(getQxthree_val());
            case 38:
                return String.valueOf(getQythree_val());
            case 39:
                return String.valueOf(getQzthree_val());
        }
        return "";
    }
}


