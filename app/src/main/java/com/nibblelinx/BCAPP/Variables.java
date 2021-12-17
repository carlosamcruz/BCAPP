package com.nibblelinx.BCAPP;

public class Variables {
    // Global variable used to store network state
    public boolean isNetworkConnected = false;
    //public boolean newTXID = false;
    public int lastLength;

    static String LastRawDecriptData = "";
    static String LastRawEncriptData = "";
    static Boolean Payment = false;
    static Boolean ValidToken = false;
    static String LastNFTAdd = "";
    static String MainPaymail = "";
    static String TXIDStart = "";
    static Boolean MyNFTs = false;
    static Boolean TransferSame = false;
    static int TotalList = 0;
    static String AssetBSVAdd = "";
    static String AssetTXID = "";
    static Boolean AssetAttention = false;
    static String MBDATA = "";

    static int operation = 0;

    //Old Dust Limite = "0.00000546"
    static String BSVDustLimit = "0.00000135"; //it has changed to 136 on 10/25/2021
    //Old Dust Limite = 546
    static long BSVDustLimitLong = 135;


    //static boolean Processing = false;
    static boolean RORprosc = false;
    static int a = 0;
    static int b = 0;
    static int progressBar = 0;
    static Boolean Flag = false;

    static String DATAFILE = "";

    static String BSVWallet = "";
    static String LastTXID = "";

    static String Test1 = "";
    static String Test2 = "";
    static String Test3 = "";
    static String Test4 = "";
    static String Test5 = "";
    static String Test6 = "";

    static String Test1x = "";
    static String Test2x = "";
    static String Test3x = "";
    static String Test4x = "";

    static String Test1x2 = "";
    static String Test2x2 = "";
    static String Test3x2 = "";
    static String Test4x2 = "";



    //static String ECDSA0STR = "";
    //static String ECDSA0sSTR = "";
    //static String ECDSATTSTR = "";
    //static String ECDSATTsSTR = "";
    //static String ECDSA0 = "";
    //static String ECDSA0s = "";
    //static String ECDSATT = "";
    //static String ECDSATTs = "";


    static String text = "";
    static String H4 = "";
    static String PVTKEY = "";
    static String PVTKEY2 = "";

    static Boolean activityPause = false;
    static Boolean EncDecLargeFile = false;
    //static String SizeText1 = "";
    //static String SizeText2 = "";

    //static Boolean EncDecLargeFile2 = false;
//    static long TimeCouterLarge = 0;

    static int MAXPAUSETIME = 60;//60;
    static int MAXSPECIALPAUSETIME = 30;//30;
    static int timeCounter = MAXPAUSETIME;
    static int TimeFactor = 1;

    static int MAXNOINTERACTIONTIME = 30;//30;
    static int userInteractionAct = MAXNOINTERACTIONTIME;

    //static int TotalPackages = 0;
    //static int NumberPackages = 0;
    static int avgPckInt;
    static float avgPck;
    static float NPck;
    static float TPck;
    static int BarSize = 0;
    static Boolean BarType = false;
}
