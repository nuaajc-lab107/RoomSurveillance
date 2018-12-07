package com.aye10032.roomsurveillance.util;

public class DataVoid {

    //0开1关
    private static int Swimming_Pool_Data = 0;
    private static int Living_Room_Data = 0;
    private static int Bed_Room_Data = 0;
    private static int Piano_Room_Data = 0;
    private static int Canopy_Data = 0;
    private static int Curtain_Data = 0;
    private static int Fire_Data = 0;
    private static int Secure_Data = 0;

    //public DataVoid(){}

    public static void setSwimming_Pool_Data(int swimming_Pool_Data) {
        Swimming_Pool_Data = swimming_Pool_Data;
    }

    public static void setPiano_Room_Data(int piano_Room_Data) {
        Piano_Room_Data = piano_Room_Data;
    }

    public static void setLiving_Room_Data(int living_Room_Data) {
        Living_Room_Data = living_Room_Data;
    }

    public static void setCurtain_Data(int curtain_Data) {
        Curtain_Data = curtain_Data;
    }

    public static void setCanopy_Data(int canopy_Data) {
        Canopy_Data = canopy_Data;
    }

    public static void setBed_Room_Data(int bed_Room_Data) {
        Bed_Room_Data = bed_Room_Data;
    }

    public static void setFire_Data(int fire_Data) {
        Fire_Data = fire_Data;
    }

    public static void setSecure_Data(int secure_Data) {
        Secure_Data = secure_Data;
    }

    public static int getSwimming_Pool_Data() {
        return Swimming_Pool_Data;
    }

    public static int getPiano_Room_Data() {
        return Piano_Room_Data;
    }

    public static int getLiving_Room_Data() {
        return Living_Room_Data;
    }

    public static int getCurtain_Data() {
        return Curtain_Data;
    }

    public static int getCanopy_Data() {
        return Canopy_Data;
    }

    public static int getBed_Room_Data() {
        return Bed_Room_Data;
    }

    public static int getFire_Data() {
        return Fire_Data;
    }

    public static int getSecure_Data() {
        return Secure_Data;
    }
}
