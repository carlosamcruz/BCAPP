package com.nibblelinx.BCAPP;

import java.math.BigInteger;

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
    static int TxPhases = 0;
    static int TxPhasesNinp = 0;
    static int TxPhasesNinpTotal = 0;

    static Boolean threadM = false;
    static String LastTxHexData = "";
    static Boolean CompPKey = false;
    static int PoolID = 0;
    static int ErroPreImagem = 0; //0 sem erro;
    static int TokenType = 0;
    //static String ScriptADD= "";
    static String DEBUG = "";
    static String UTXOSET = "";
    static String UnspentUTXOs = "";
    static String shortS = "normal";

    //Didactic
    static Boolean ditactic = false;
    static BigInteger dPvtKey = BigInteger.valueOf(0);
    static BigInteger dPubKX = BigInteger.valueOf(0);
    static BigInteger dPubKY = BigInteger.valueOf(0);
    static String dPubKUnComp = "";
    static String dPubKComp = "";

    static String dSha256UnComp = "";
    static String dSha256Comp = "";

    static String dRMP160UnComp = "";
    static String dRMP160Comp = "";

    static String d00RMP160UnComp = "";
    static String d00RMP160Comp = "";

    static String dHashMD160UnComp = "";
    static String dHashMD160Comp = "";


    static BigInteger dCSRMD160UnCompBi = BigInteger.valueOf(0);
    static BigInteger dCSRMD160CompBi = BigInteger.valueOf(0);

    static String dAddUnCompPre = "";
    static String dAddCompPre = "";

    static String dAddUnCompLZ = "";
    static String dAddCompLZ = "";

    static String dAddUnComp = "";
    static String dAddComp = "";

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
