package com.nibblelinx.BCAPP;

public class Variables {

    static String LastRawDecriptData = "";
    static Boolean Payment = false;
    static String MainPaymail = "";
    static Boolean MyNFTs = false;
    static int TotalList = 0;
    static String MBDATA = "";
    static int operation = 0;
    static String SatBalance = "";
    static int STREAMTT = 0;

    static Boolean threadM = false;
    static String LastTxHexData = "";


    static int ntries = 0;

    //Old Dust Limite = "0.00000546"
    static String BSVDustLimit = "0.00000135"; //it has changed to 136 on 10/25/2021
    //Old Dust Limite = 546
    static long BSVDustLimitLong = 135;
    static int progressBar = 0;
    static String BSVWallet = "";
    static String LastTXID = "";

    static Boolean activityPause = false;

    static int MAXPAUSETIME = 60;//60;
    static int MAXSPECIALPAUSETIME = 30;//30;
    static int timeCounter = MAXPAUSETIME;

    static int MAXNOINTERACTIONTIME = 30;//30;
    static int userInteractionAct = MAXNOINTERACTIONTIME;

    static int BarSize = 0;
    static Boolean BarType = false;
}
