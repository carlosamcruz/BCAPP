package com.nibblelinx.BCAPP;


import java.io.*;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

//////////////////////////////////////////////////////////////////
//OPERACOES COM ARQUIVOS
/////////////////////////////////////////////////////////////////
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
/////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////

public class BsvTxOperations {


    //String urlBaseTXID = "https://api.whatsonchain.com/v1/bsv/main/tx/" + TXID +  "/hex";
    String urlBaseTXID = "";

    Ecc eccVar = new Ecc();

    String lockTime = "00000000"; // standard

    //String lockTime = "94d50b00";  // test 1

    //String lockTime = "74d50b00"; // test 2

    //String lockTime = "a0d60b00";  // test 3
    //String lockTime = "a1d60b00";  // test 4
    //String lockTime = "ffffffff";  // test 5



    String inputSeq = "ffffffff"; //standard

    //String inputSeq = "feffffff"; //teste 1
    //String inputSeq = "00000000"; //teste 2

    //Para recuperar o Script
    //String urlBaseTXID2 = "https://api.whatsonchain.com/v1/bsv/main/tx/" + TXID + "/out/0/hex";

    //Boolean isOnline;

    //Timer timer = new Timer();
    Timer timer;


//////////////////////////////////////////////////////////////////////////////////
//  Faz a leitura do conteúdo em HEXADECINAL de uma TX pela API WhatsOnChain
//  Conteudo: TXID
//  A operação é feita em BACKGROUND, o resultado será colocado em TxHexData
//  Equanto TxHexData = null a operação não foi concluida ou falhou
///////////////////////////////////////////////////////////////////////////////////
    int dalyWhatsOnChain = 0;

    String TxHexData;
    //Boolean threadEndReadHexBsvTx = false;

    Thread threadReadHexBsvTx = new Thread(new Runnable() {
        @Override
        public void run() {
            try {

                TxHexData = JsonTaskTXIDNew(urlBaseTXID);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    });

    private void renewThread()
    {
        threadReadHexBsvTx = new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    TxHexData = JsonTaskTXIDNew(urlBaseTXID);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }




    Thread[] threadArray = new Thread[256];
    int threadArrayIndex = 0;

    private void setThreadArray(int i)
    {
        for(int j=0; j<i; j++)
        {
            threadArray[j] = new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    TxHexData = JsonTaskTXIDNew(urlBaseTXID);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        }
    }


    TimeCheckURL timerWoc01;// = new TimeCheckURL();

    public void readHexBsvTx(String TXID){

        //timer.cancel();
        //timer.purge();
        //timer = new Timer();
        urlBaseTXID = "https://api.whatsonchain.com/v1/bsv/main/tx/" + TXID +  "/hex";
        //timer.schedule(new TimeCheckURL(), 0, 5000);
        //threadEndReadHexBsvTx = false;


        //timer.schedule(new TimeCheckURL(), dalyWhatsOnChain, 5000);
        //timer.schedule(timerWoc01 = new TimeCheckURL(), dalyWhatsOnChain, 5000);
//        timer.schedule(timerWoc01, dalyWhatsOnChain, 5000);


        threadReadHexBsvTx.start();

        //threadArray[threadArrayIndex].start();

    }

    public void readHexBsvTxSCRIP(String TXID){

        //timer.cancel();
        //timer.purge();
        //timer = new Timer();

        //urlBaseTXID = "https://api.whatsonchain.com/v1/bsv/main/tx/" + TXID +  "/hex";

        urlBaseTXID = "https://api.whatsonchain.com/v1/bsv/main/tx/" + TXID + "/out/0/hex";


        //timer.schedule(new TimeCheckURL(), 0, 5000);
        //threadEndReadHexBsvTx = false;


        //timer.schedule(new TimeCheckURL(), dalyWhatsOnChain, 5000);
        //timer.schedule(timerWoc01 = new TimeCheckURL(), dalyWhatsOnChain, 5000);
//        timer.schedule(timerWoc01, dalyWhatsOnChain, 5000);


        threadReadHexBsvTx.start();

        //threadArray[threadArrayIndex].start();

    }

    public void readHexBsvTx2(String TXID){

        //timer.cancel();
        //timer.purge();
        //timer = new Timer();
        urlBaseTXID = "https://api.whatsonchain.com/v1/bsv/main/tx/" + TXID +  "/hex";
        //timer.schedule(new TimeCheckURL(), 0, 5000);
        //threadEndReadHexBsvTx = false;


        //timer.schedule(new TimeCheckURL(), dalyWhatsOnChain, 5000);
        //timer.schedule(timerWoc01 = new TimeCheckURL(), dalyWhatsOnChain, 5000);
//        timer.schedule(timerWoc01, dalyWhatsOnChain, 5000);


        //threadReadHexBsvTx.start();
        threadArray[threadArrayIndex].start();

    }

    public void readHexBsvTxWOC(String TXID){

        //timer.cancel();
        //timer.purge();
        timer = new Timer();
        urlBaseTXID = "https://api.whatsonchain.com/v1/bsv/main/tx/" + TXID +  "/hex";
        //timer.schedule(new TimeCheckURL(), 0, 5000);
        threadEndReadHexBsvTx = false;


        timer.schedule(new TimeCheckURLWOC(), dalyWhatsOnChain, 5000);

        //threadReadHexBsvTx.start();

    }


    class TimeCheckURLWOC extends TimerTask
    {
        @RequiresApi(api = Build.VERSION_CODES.M)
        public void run()
        {
            TxHexData = null;
            //Variables.TxHexData = new JsonTaskTXID().execute(urlBaseTXID2);
            threadReadHexBsvTx.start();
            threadEndReadHexBsvTx = true;


        }
    }


    public void timerCallWOC(){

        //timer.cancel();
        //timer.purge();
        //timer = new Timer();
        //urlBaseTXID = "https://api.whatsonchain.com/v1/bsv/main/tx/" + TXID +  "/hex";
        //timer.schedule(new TimeCheckURL(), 0, 5000);
        //threadEndReadHexBsvTx = false;


        wocTimer = 0;
        timer.schedule(new TimerWOC(), dalyWhatsOnChain, 5000);

        //threadReadHexBsvTx.start();

    }

    int wocTimer = 0;
    class TimerWOC extends TimerTask
    {
        @RequiresApi(api = Build.VERSION_CODES.M)
        public void run()
        {
            //threadReadHexBsvTx.start();
            wocTimer ++;
        }
    }


    //private String result = "";

    Boolean threadEndReadHexBsvTx = false;

    class TimeCheckURL extends TimerTask
    {

        Thread thread00 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    //Looper.prepare();

                    ////////////////////////////////////////////////////////////
                    //Resultado Armazenado em uma variável Global
                    ////////////////////////////////////////////////////////////
                    TxHexData = null;
                    //Variables.TxHexData = new JsonTaskTXID().execute(urlBaseTXID2);
                    TxHexData = new JsonTaskTXID().execute(urlBaseTXID);
                    threadEndReadHexBsvTx = true;

                    //  timer.cancel();
                    //  timer.purge();
                    //result = new JsonTaskTXID().execute(urlBaseTXID2);;
                    //Looper.loop();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });



        @RequiresApi(api = Build.VERSION_CODES.M)
        public void run()
        {
            /*
            new Thread(new Runnable() {
                @Override
                public void run() {

                    //Looper.prepare();

                    ////////////////////////////////////////////////////////////
                    //Resultado Armazenado em uma variável Global
                    ////////////////////////////////////////////////////////////
                    TxHexData = null;
                    //Variables.TxHexData = new JsonTaskTXID().execute(urlBaseTXID2);
                    TxHexData = new JsonTaskTXID().execute(urlBaseTXID);
                    threadEndReadHexBsvTx = true;

                    //  timer.cancel();
                    //  timer.purge();
                    //result = new JsonTaskTXID().execute(urlBaseTXID2);;
                    //Looper.loop();

                }
            }).start();
            */
            thread00.start();

        }
    }
    ////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////


    ////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////
    //NESTED CLASS: nao precisa colocar fora
    //Inner Class: https://www.tutorialspoint.com/java/java_innerclasses.htm
    // https://stackoverflow.com/questions/33229869/get-json-data-from-url-using-android
    ////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////
    private class JsonTaskTXID //extends AsyncTask<String, String, String>
    {
        protected void onPreExecute()
        {
            //super.onPreExecute();
            //Exibir a barra de progressão nesta operação faz o App quebrar
            //isso ocorre pois por algum motivo acionar a barra depois de um retorno
            //da conexão faz o App quebrar;
            //exibirProgress(true);
        }

        protected String execute(String... params)
        {
            //return "xxx";

            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try
            {
                URL url = new URL(params[0]);

                connection = (HttpURLConnection) url.openConnection();
                connection.connect();


                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null)
                {
                    buffer.append(line+"\n");
                    //Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)
                }

                return buffer.toString();

            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();
                return null;
            }
            catch (IOException e)
            {
                e.printStackTrace();
                return null;
            }
            finally
            {
                if (connection != null)
                {
                    connection.disconnect();
                }
                try
                {
                    if (reader != null)
                    {
                        reader.close();
                    }
                }
                catch (IOException e)
                {
                    e.printStackTrace();

                }
                //return null;
            }

            //return null;
        }

    }


    //private static String JsonTaskTXIDNew(String theUrl) {
    private String JsonTaskTXIDNew(String theUrl) {
        StringBuilder content = new StringBuilder();
        try {
            URL url = new URL(theUrl);
            URLConnection urlConnection = url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line + "\n");
            }
            bufferedReader.close();
        } catch(Exception e) {
            //e.printStackTrace();
            return null;
        }
        return content.toString();
    }

    //////////////////////////////////////////////////////////////////////////////////
    // Adress Unspent Inputs
    //////////////////////////////////////////////////////////////////////////////////
    String unsPentInputs;
    //Boolean threadEndReadBsvAddsUnspent = false;
    //int threadCounterReadBsvAddsUnspent = 0;

    public void readBsvAddsUnspent(String BSVADD){

         urlBaseTXID = "https://api.whatsonchain.com/v1/bsv/main/address/" + BSVADD +  "/unspent";

        //O Processo a seguir foi testado e não pode ser executado fora de uma thread
        //timer.schedule(new TimeCheckURL2(), dalyWhatsOnChain, 5000);
        threadreadBsvAddsUnspent.start();
    }

    public void readBsvSCRIPTUnspent(String SCRIPTHASH){

        //urlBaseTXID = "https://api.whatsonchain.com/v1/bsv/main/address/" + BSVADD +  "/unspent";

        urlBaseTXID = "https://api.whatsonchain.com/v1/bsv/main/script/" + SCRIPTHASH + "/unspent";

        //O Processo a seguir foi testado e não pode ser executado fora de uma thread
        //timer.schedule(new TimeCheckURL2(), dalyWhatsOnChain, 5000);
        threadreadBsvAddsUnspent.start();
    }

    Thread threadreadBsvAddsUnspent = new Thread(new Runnable() {
        @Override
        public void run() {
            try {

                unsPentInputs = JsonTaskTXIDNew(urlBaseTXID);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    });

    public void renewThreadUnspent()
    {

        threadreadBsvAddsUnspent = new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    unsPentInputs = JsonTaskTXIDNew(urlBaseTXID);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    String[] unspentIndex = new String[1000];
    String[] unspentValue = new String[1000];
    String[] unspentTXID = new String[1000];

    public int unspentUTXO (String unspentUTXOstring)
    {
        String[] parts = new String[1000];

        String search1 = "\"tx_pos\":";
        String search2 = "\"tx_hash\":\"";
        String search3 = "\"value\":";

        //jsonStrTXID = params[0];
        int firstIndiceOf;
        int nextIndex;

        //https://www.geeksforgeeks.org/searching-for-character-and-substring-in-a-string/

        firstIndiceOf = unspentUTXOstring.indexOf(search1);

        int i = 0;

        while (firstIndiceOf != -1) {

            //nextIndex = jsonStrTXID.indexOf("\"", firstIndiceOf + searchStrOPRETURN.length());
            nextIndex = unspentUTXOstring.indexOf(",", firstIndiceOf + search1.length());
            unspentIndex[i] = unspentUTXOstring.substring(firstIndiceOf + search1.length(), nextIndex);

            firstIndiceOf = unspentUTXOstring.indexOf(search2, nextIndex);
            nextIndex = unspentUTXOstring.indexOf("\"", firstIndiceOf + search2.length());
            unspentTXID[i] = unspentUTXOstring.substring(firstIndiceOf + search2.length(), nextIndex);

            firstIndiceOf = unspentUTXOstring.indexOf(search3, nextIndex);
            nextIndex = unspentUTXOstring.indexOf("}", firstIndiceOf + search3.length());
            unspentValue[i] = unspentUTXOstring.substring(firstIndiceOf + search3.length(), nextIndex);

            firstIndiceOf = unspentUTXOstring.indexOf(search1, nextIndex);
            i++;
        }

        return i;
    }

    public String inputPreString (int nInputs, String SECsizeOut, String PUBKEYSEC)
    {
        String Version = "01000000";
        //String nOfInputs = "01";
        String nOfInputs = Integer.toHexString(nInputs);

        while (nOfInputs.length() < 2)
            nOfInputs = "0" + nOfInputs;

        //String inputSeq = "ffffffff";

        String totalString = Version + nOfInputs;

        // String inTxID = SHA256G.LEformat(toSpendTXID);
        // String prvTxIndex = "01000000";

        String derSecPAD =
                "8a4730" +
                        "44" +
                        "0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000" +
                        "41" +
                        SECsizeOut + PUBKEYSEC;


        for(int i = 0 ; i < nInputs; i ++)
        {

            int indexI = Integer.valueOf(unspentIndex[i]);

            String inputIndex = Integer.toHexString(indexI);

            while (inputIndex.length() < 8)
                inputIndex = "0" + inputIndex;

            totalString = totalString + SHA256G.LEformat(unspentTXID[i]) + SHA256G.LEformat(inputIndex) + derSecPAD + inputSeq;
        }


        return totalString;
    }

    public String inputPreStringP2PK (int nInputs, String SECsizeOut, String PUBKEYSEC)
    {
        String Version = "01000000";
        //String nOfInputs = "01";
        String nOfInputs = Integer.toHexString(nInputs);

        while (nOfInputs.length() < 2)
            nOfInputs = "0" + nOfInputs;

        //String inputSeq = "ffffffff";

        String totalString = Version + nOfInputs;

        // String inTxID = SHA256G.LEformat(toSpendTXID);
        // String prvTxIndex = "01000000";

        String derSecPAD =
                //"8a4730" +
                "484730" +
                        "44" +
                        "0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000" +
                        "41";// +
                        //SECsizeOut + PUBKEYSEC;


        for(int i = 0 ; i < nInputs; i ++)
        {

            int indexI = Integer.valueOf(unspentIndex[i]);

            String inputIndex = Integer.toHexString(indexI);

            while (inputIndex.length() < 8)
                inputIndex = "0" + inputIndex;

            totalString = totalString + SHA256G.LEformat(unspentTXID[i]) + SHA256G.LEformat(inputIndex) + derSecPAD + inputSeq;
        }


        return totalString;
    }

    /*
    //private String ScriptTXID = "cafbd9718e9b7ee324014015789576b7eee10d3b63de70a3061a3c41f0d4ecda";
    //private String ScriptTXID = "9dda331ceec2c8e259f3d599b7703b4a49aaffeec076f7a423cb17bd173b5e27";
    private String ScriptTXID = "8a08ae7c4a48dd9b36440cc435389eb1e9b5a4e724b20100220d81ee40f8df02";
    public String inputPreStringV2 (int nInputs, String SECsizeOut, String PUBKEYSEC)
    {
        String Version = "01000000";
        //String nOfInputs = "01";

        nInputs = 1;//Somente o output do Script
        String nOfInputs = Integer.toHexString(nInputs);



        while (nOfInputs.length() < 2)
            nOfInputs = "0" + nOfInputs;

        //String inputSeq = "ffffffff";

        String totalString = Version + nOfInputs;

        // String inTxID = SHA256G.LEformat(toSpendTXID);
        // String prvTxIndex = "01000000";

        String derSecPAD =
                "8a4730" +
                        "44" +
                        "0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000" +
                        "41" +
                        SECsizeOut + PUBKEYSEC;



        for(int i = 0 ; i < nInputs; i ++)
        {

            int indexI = Integer.valueOf(unspentIndex[i]);

            //String inputIndex = Integer.toHexString(indexI);
            //String inputIndex = Integer.toHexString(1);
            String inputIndex = Integer.toHexString(0);

            while (inputIndex.length() < 8)
                inputIndex = "0" + inputIndex;

            //String ScriptTXID = "28759c36041943cbc4b4cbf98b12978cb821433e9a7a9f0fb21cfe6f83105b0c";
            //String ScriptTXID = "cafbd9718e9b7ee324014015789576b7eee10d3b63de70a3061a3c41f0d4ecda";
            totalString = totalString + SHA256G.LEformat(ScriptTXID)
                    + SHA256G.LEformat(inputIndex) + derSecPAD + inputSeq;
        }


        return totalString;
    }
    */

    public String inputPosString (int nInputs, String SECsizeOut, String PUBKEYSEC, String[] derSecPAD)
    {
        String Version = "01000000";
        //String nOfInputs = "01";
        String nOfInputs = Integer.toHexString(nInputs);

        while (nOfInputs.length() < 2)
            nOfInputs = "0" + nOfInputs;

        //String inputSeq = "ffffffff";

        String totalString = Version + nOfInputs;

        // String inTxID = SHA256G.LEformat(toSpendTXID);
        // String prvTxIndex = "01000000";

        /*String derSecPAD =
                "8a4730" +
                        "44" +
                        "0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000" +
                        "41" +
                        SECsizeOut + PUBKEYSEC;
        */


        for(int i = 0 ; i < nInputs; i ++)
        {

            int indexI = Integer.valueOf(unspentIndex[i]);

            String inputIndex = Integer.toHexString(indexI);

            while (inputIndex.length() < 8)
                inputIndex = "0" + inputIndex;

            totalString = totalString + SHA256G.LEformat(unspentTXID[i]) + SHA256G.LEformat(inputIndex) + derSecPAD[i] + inputSeq;
        }


        return totalString;
    }

    /*
    public String inputPosStringV2 (int nInputs, String SECsizeOut, String PUBKEYSEC, String[] derSecPAD)
    {
        String Version = "01000000";
        //String nOfInputs = "01";

        nInputs = 1;

        String nOfInputs = Integer.toHexString(nInputs);

        while (nOfInputs.length() < 2)
            nOfInputs = "0" + nOfInputs;

        //String inputSeq = "ffffffff";

        String totalString = Version + nOfInputs;

        // String inTxID = SHA256G.LEformat(toSpendTXID);
        // String prvTxIndex = "01000000";


        //String ScriptTXID = "28759c36041943cbc4b4cbf98b12978cb821433e9a7a9f0fb21cfe6f83105b0c";
        //String ScriptTXID = "cafbd9718e9b7ee324014015789576b7eee10d3b63de70a3061a3c41f0d4ecda";
        //nInputs = 1;

//        unspentValue[0] = "2000";
//        unspentValue[1] = "0000";
//        unspentValue[2] = "0000";
//        unspentValue[3] = "0000";


        for(int i = 0 ; i < nInputs; i ++)
        {

            int indexI = Integer.valueOf(unspentIndex[i]);

            //String inputIndex = Integer.toHexString(indexI);
            //String inputIndex = Integer.toHexString(1);
            String inputIndex = Integer.toHexString(0);

            while (inputIndex.length() < 8)
                inputIndex = "0" + inputIndex;

            //totalString = totalString + SHA256G.LEformat(unspentTXID[i]) + SHA256G.LEformat(inputIndex) + derSecPAD[i] + inputSeq;
            totalString = totalString + SHA256G.LEformat(ScriptTXID)
                    + SHA256G.LEformat(inputIndex) + derSecPAD[i] + inputSeq;
        }


        return totalString;
    }

     */



    public String OutputString(int nOutPuts, String[] PayWallets, String[] PayValues, int nORs, String[] OP_RETURNs, String inputPreString)
    {
        String totalOutString = "";


        String nOuts = Integer.toHexString(nOutPuts);

        while (nOuts.length() < 2)
            nOuts = "0" + nOuts;

        totalOutString = nOuts;

        String out2Sat = ""; //Total - TXSize - fee
        long totalSpent = 0;

        //O ultimo output será sempre o retorno

        for(int i = 0; i < nOutPuts; i ++)
        {

            if( i < nORs)
            {
                String outOpReturn = "0000000000000000";
                String out1size = "00";
                String out1ScriptType = "00" + "6a"; // OP_Return
                String out1DataSizeType = "4c";//only one byte
                String byteSizeout1size = "";
                //String out1DataSizeType;
                String out1DataSize = "00";//only one byte

                //String out1Data = "2e2e2e617420746865206e616d65206f66204a65737573206576657279206b6e65652073686f756c6420626f772c206f66207468696e677320696e2068656176656e2c20616e64207468696e677320696e2065617274682c20616e64207468696e677320756e646572207468652065617274683b";
                String out1Data = OP_RETURNs[i];

                out1DataSize = Integer.toHexString(
                        out1Data.length() / 2
                );

                if(out1DataSize.length() % 2 == 1)
                    out1DataSize = "0" + out1DataSize;

                ////////////////////////////////////////////
                //OP_RETURN size
                ////////////////////////////////////////////

                //Verificar se OP_RETURN aceita a otimização para dado com menos de 0x4c bytes
                //Aparentemente funcionou

                if((out1Data.length() / 2) >= 0x01 && (out1Data.length() / 2) <= 0x4b) {
                    out1DataSizeType = "";
                    //byteSizeout1size = "";
                }

                //if((out1Data.length() / 2)>=1 && (out1Data.length() / 2)<=256)

                //if((out1Data.length() / 2) >= 0x01 && (out1Data.length() / 2) <= 0xff) {
                else if((out1Data.length() / 2) >= 0x4c && (out1Data.length() / 2) <= 0xff) {
                    out1DataSizeType = "4c";
                    //byteSizeout1size = "";
                }
                else if ((out1Data.length() / 2) >= 0x0100 && (out1Data.length() / 2) <= 0xffff) {
                    out1DataSizeType = "4d";
                    //byteSizeout1size = "fd";

                    while (out1DataSize.length() < 4)
                        out1DataSize = "0" + out1DataSize;

                    out1DataSize = SHA256G.LEformat(out1DataSize);


                }
                //else if ((out1Data.length() / 2) >= 0x10000 && (out1Data.length() / 2) <= 0xffffffff)
                //Limit 99 kbytes por OP_RETURN
                else if ((out1Data.length() / 2) >= 0x00010000 && (out1Data.length() / 2) <= 0x000182b8) {
                    out1DataSizeType = "4e";
                    //byteSizeout1size = "fe";

                    while (out1DataSize.length() < 8)
                        out1DataSize = "0" + out1DataSize;

                    out1DataSize = SHA256G.LEformat(out1DataSize);
                }

                ////////////////////////////////////////////
                ////////////////////////////////////////////

                //String Out1Script = "1976a914" + PayWallet160 + "88ac";
                String Out1Script = out1ScriptType + out1DataSizeType + out1DataSize + out1Data;

                out1size = Integer.toHexString(
                        Out1Script.length() / 2
                );

                if(out1size.length() % 2 == 1)
                    out1size = "0" + out1size;

                //out1size = SHA256G.LEformat(out1size);


                ////////////////////////////////////////////
                //Ajuste do tamanho do Script e indicação do byte de tamanho em bytes
                // Só pode ser indicado a partir desta posição para não dar conflito
                // se o script ultrapassar os bytes indicados para os dados
                ////////////////////////////////////////////

                //if((Out1Script.length() / 2) >= 0x01 && (Out1Script.length() / 2) <= 0xff) {
                if((Out1Script.length() / 2) >= 0x01 && (Out1Script.length() / 2) <= 0xfc) {
                    //out1size = out1size;
                    byteSizeout1size = "";

                    out1size = byteSizeout1size + SHA256G.LEformat(out1size);
                }
                //else if ((Out1Script.length() / 2) >= 0x0100 && (Out1Script.length() / 2) <= 0xffff) {
                else if ((Out1Script.length() / 2) >= 0x00fd && (Out1Script.length() / 2) <= 0xffff) {

                    byteSizeout1size = "fd";

                    while (out1size.length() < 4)
                        out1size = "0" + out1size;

                    out1size = byteSizeout1size + SHA256G.LEformat(out1size);
                }
                //else if ((out1Data.length() / 2) >= 0x10000 && (out1Data.length() / 2) <= 0xffffffff)
                //Limit 99 kbytes por OP_RETURN
                //Verificar se OP_RETURN a expansão até o limite máximo de 0xffffffff
                else if ((Out1Script.length() / 2) >= 0x00010000 && (Out1Script.length() / 2) <= 0x000182b8) {

                    byteSizeout1size = "fe";

                    while (out1size.length() < 8)
                        out1size = "0" + out1size;

                    out1size = byteSizeout1size + SHA256G.LEformat(out1size);
                }

                ////////////////////////////////////////////
                ////////////////////////////////////////////

                Out1Script = outOpReturn + out1size + Out1Script;

                totalOutString = totalOutString + Out1Script;

            }
            else
            {
                Keygen pubKey = new Keygen();

                int j = i - nORs;
                //String out2Sat = "00c8000000000000"; //Total - TXSize - fee
                //String out2Sat = "7bc7000000000000"; //Total - TXSize - fee
                //String out2Sat = ""; //Total - TXSize - fee

                String PayWallet160 = pubKey.addRMD(PayWallets[j]);

                long ivalue = Integer.valueOf(PayValues[j]);

                if( i < nOutPuts -1 )
                    totalSpent = totalSpent + ivalue;
                else
                {
                    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                    //Neste ponto eh calculado o valor de retorno para a carteira original,
                    // menos a taxa oferecida aos mineradores
                    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

                    /////////////////////////////////////////////////////////////////////////////////////
                    //ivalue = ivalue - totalSpent - 1;
                    //A taxa Minima para ser testada na Gorilla Pool, a WhatsOnChain também aceitou em 04/07/2022;
                    // Transações enviadas entre 19:50 e 20:00 de 04/07/2022
                    //2d046b7bc16cb5863482d293b6a5fd9738945bace98f93abfcb46ca40ab364ab
                    //b564a1212d0d5b0a4c1333de640e8dec387a05b1ed5d1580efbd0d4cd4338d05
                    //Gorilla Pool Minerou 2 Blocos as 20:30 3 20:35 de 04/07/2022 mas não levou as TXs
                    //As transações foram removidas da MemPool depois de cerca de 30h
                    /////////////////////////////////////////////////////////////////////////////////////
                    // Esta taxa pode ser interessantes para comunicar dados que não queremos que permaneçam na rede
                    // Usar a MemPool como canal de comunicação
                    /////////////////////////////////////////////////////////////////////////////////////


                    ivalue = ivalue - totalSpent -
                            ((
                                    inputPreString.length() +
                                            totalOutString.length() +
                                            ("0000000000000000"+"1976a914" + PayWallet160 + "88ac" + "00000000").length()
                            )/2)
                                    // / 2; //0.5 Satoshis por byte
                                    / 20; //0.05 Satoshis por byte

                                    // (03/07/2022) 0.05 sat/byte ainda é limite mais segura para menor taxa
                                    // Mais baixo que isso, ou fica congelado, ou semente a Gorilla Pool aceita

                                    // / 30; //0.033 Satoshis por byte
                                    // 2 TX enviadas por volta de 19:35 de 03/07/2022
                                    // Gorilla Pool has mined the 2 TXx on 04/07/2022
                                    // https://whatsonchain.com/tx/03840ceecf29f079879b4b49ea96e879d9ff5ae69b5315944da95363ab746136
                                    // https://whatsonchain.com/tx/b0dc1711dda5994eb27eb3cff9de75fc9026d16ebad9077ba4b96386437f9bc0

                                    // / 40; //0.025 Satoshis por byte
                                    // TAAL não permite entrada de TX com taxa < 0.032 sat/byte
                                    // TX recusada pela TAAL em 03/07/2022
                                    //There was an issue with the broadcast:unexpected response code 500: 66: mempool min fee not met


                                    // / 200; //0.005 Satoshis por byte - funciona, mas ainda demora demais.
                                    // 0.005 sat/b demora cerca de 6 hora para ser minerado em 30/06/2022
                                    // Somente Gorilla Pool aceita TXs com estas taxas por enquanto (03/07/2022);

                    //Tem que haver pelo menos 1 sat de taxa;
                    //if(ivalue<0)
                    //    ivalue = ivalue - totalSpent;


                }


                if(ivalue>0) { //se nao houver troco, nao executa no final

                    Variables.SatBalance = Long.toString(ivalue);

                    out2Sat = Long.toHexString(ivalue);

                    while (out2Sat.length() < 16)
                        out2Sat = "0" + out2Sat;

                    out2Sat = SHA256G.LEformat(out2Sat);


                    //String PayWallet160 = pubKey.addRMD(PayWallets[j]);

                    //String Out1Script = "1976a914" + "61079f5031a5b7e312d9fc5051fd7ce018fabc92" + "88ac";
                    //String Out2Script = "1976a914" + BSV160 + "88ac";
                    String Out2Script = "1976a914" + PayWallet160 + "88ac";

                    totalOutString = totalOutString + out2Sat + Out2Script;
                }
                else{
                    nOuts = Integer.toHexString(nOutPuts - 1);

                    while (nOuts.length() < 2)
                        nOuts = "0" + nOuts;

                    totalOutString = nOuts + totalOutString.substring(2);
                }

            }


        }

        //Se o LockTime for referente a um bloco futuro, a TX só será minerada quando este bloco acontecer
        //String lockTime = "00000000";
        //lockTime = "00000000";


        //long txFee = Long.valueOf(SHA256G.LEformat(out2Sat));



        totalOutString = totalOutString + lockTime;// + out2Sat;//  + Long.toHexString(txFee);

        //return  inputPreString + totalOutString;
        return  totalOutString;
    }

    //Cria um Output OP_RETURN com OP_TRUE e OP_DROP
    public String OutputStringV2(int nOutPuts, String[] PayWallets, String[] PayValues, int nORs, String[] OP_RETURNs, String inputPreString, int tokenType)
    {
        String totalOutString = "";


        //String nOuts = Integer.toHexString(nOutPuts);
        String nOuts = Integer.toHexString(nOutPuts - 1);

        while (nOuts.length() < 2)
            nOuts = "0" + nOuts;

        totalOutString = nOuts;

        String out2Sat = ""; //Total - TXSize - fee
        long totalSpent = 0;

        //O ultimo output será sempre o retorno

        String returnDataNew = ""; //somente 1 OP_RETURN
        //String Out2Script = "";

        for(int i = 0; i < nOutPuts; i ++)
        {

            if( i < nORs)
            {
                String outOpReturn = "0000000000000000";
                String out1size = "00";
                String out1ScriptType = "00" + "6a"; // OP_Return
                String out1DataSizeType = "4c";//only one byte
                String byteSizeout1size = "";
                //String out1DataSizeType;
                String out1DataSize = "00";//only one byte

                //String out1Data = "2e2e2e617420746865206e616d65206f66204a65737573206576657279206b6e65652073686f756c6420626f772c206f66207468696e677320696e2068656176656e2c20616e64207468696e677320696e2065617274682c20616e64207468696e677320756e646572207468652065617274683b";
                String out1Data = OP_RETURNs[i];

                out1DataSize = Integer.toHexString(
                        out1Data.length() / 2
                );

                if(out1DataSize.length() % 2 == 1)
                    out1DataSize = "0" + out1DataSize;

                ////////////////////////////////////////////
                //OP_RETURN size
                ////////////////////////////////////////////

                //Verificar se OP_RETURN aceita a otimização para dado com menos de 0x4c bytes

                //Verificar se OP_RETURN aceita a otimização para dado com menos de 0x4c bytes
                //Aparentemente funcionou

                if((out1Data.length() / 2) >= 0x01 && (out1Data.length() / 2) <= 0x4b) {
                    out1DataSizeType = "";
                    //byteSizeout1size = "";
                }

                //if((out1Data.length() / 2)>=1 && (out1Data.length() / 2)<=256)

                //if((out1Data.length() / 2) >= 0x01 && (out1Data.length() / 2) <= 0xff) {
                else if((out1Data.length() / 2) >= 0x4c && (out1Data.length() / 2) <= 0xff) {
                    out1DataSizeType = "4c";
                    //byteSizeout1size = "";
                }

                else if ((out1Data.length() / 2) >= 0x0100 && (out1Data.length() / 2) <= 0xffff) {
                    out1DataSizeType = "4d";
                    //byteSizeout1size = "fd";

                    while (out1DataSize.length() < 4)
                        out1DataSize = "0" + out1DataSize;

                    out1DataSize = SHA256G.LEformat(out1DataSize);


                }
                //else if ((out1Data.length() / 2) >= 0x10000 && (out1Data.length() / 2) <= 0xffffffff)
                //Limit 99 kbytes por OP_RETURN
                else if ((out1Data.length() / 2) >= 0x00010000 && (out1Data.length() / 2) <= 0x000182b8) {
                    out1DataSizeType = "4e";
                    //byteSizeout1size = "fe";

                    while (out1DataSize.length() < 8)
                        out1DataSize = "0" + out1DataSize;

                    out1DataSize = SHA256G.LEformat(out1DataSize);
                }

                ////////////////////////////////////////////
                ////////////////////////////////////////////

                //String Out1Script = "1976a914" + PayWallet160 + "88ac";
                String Out1Script = out1ScriptType + out1DataSizeType + out1DataSize + out1Data;

/*
                out1size = Integer.toHexString(
                        Out1Script.length() / 2
                );

                if(out1size.length() % 2 == 1)
                    out1size = "0" + out1size;

                //out1size = SHA256G.LEformat(out1size);


                ////////////////////////////////////////////
                //Ajuste do tamanho do Script e indicação do byte de tamanho em bytes
                // Só pode ser indicado a partir desta posição para não dar conflito
                // se o script ultrapassar os bytes indicados para os dados
                ////////////////////////////////////////////

                //if((Out1Script.length() / 2) >= 0x01 && (Out1Script.length() / 2) <= 0xff) {
                if((Out1Script.length() / 2) >= 0x01 && (Out1Script.length() / 2) <= 0xfc) {
                    //out1size = out1size;
                    byteSizeout1size = "";

                    out1size = byteSizeout1size + SHA256G.LEformat(out1size);
                }
                //else if ((Out1Script.length() / 2) >= 0x0100 && (Out1Script.length() / 2) <= 0xffff) {
                else if ((Out1Script.length() / 2) >= 0x00fd && (Out1Script.length() / 2) <= 0xffff) {

                    byteSizeout1size = "fd";

                    while (out1size.length() < 4)
                        out1size = "0" + out1size;

                    out1size = byteSizeout1size + SHA256G.LEformat(out1size);
                }
                //else if ((out1Data.length() / 2) >= 0x10000 && (out1Data.length() / 2) <= 0xffffffff)
                //Limit 99 kbytes por OP_RETURN
                else if ((Out1Script.length() / 2) >= 0x00010000 && (Out1Script.length() / 2) <= 0x000182b8) {

                    byteSizeout1size = "fe";

                    while (out1size.length() < 8)
                        out1size = "0" + out1size;

                    out1size = byteSizeout1size + SHA256G.LEformat(out1size);
                }
*/
                ////////////////////////////////////////////
                ////////////////////////////////////////////

                ////////////////////////////////////////////////////////////////////////////
                //Testes para dados com OP_TRUE OP_RETURN <DATA> e <DATA> OP_DROP
                //  Para dados com até 256 caracteres
                ////////////////////////////////////////////////////////////////////////////

                //returnDataNew = Out1Script.substring(2); // uso do OP_RETURN com OP_TRUE

                //returnDataNew = Out1Script.substring(4) + "75"; // com esta metodologia deu erro
                // sem a otimzação prévia do tamanho do script de dado;

                //Com o código otimizado para o menor formato de dado possível
                // Remover o OP_FALSE OP_RETURN 0x00 0x6a
                // Reconstruir o tamanho do Script no fim do processo
//                returnDataNew = Out1Script.substring(4) + "75"; // Funcionou com a otimização

                if(tokenType == 1)
                    returnDataNew = Out1Script.substring(2); // uso do OP_RETURN com OP_TRUE
                if (tokenType == 2)
                    returnDataNew = Out1Script.substring(4) + "75"; // Funcionou com a otimização

                //Funcionou - fazer os testes com cuidado para Dados maiores
                //returnDataNew = Out1Script.substring(6) + "75";// for data with less than 4b bytes;

                //returnDataNew = "75" + Out1Script.substring(4); // com esta metodologia deu erro;
                //Problema Relacionado a https://github.com/lbryio/lbrycrd/issues/242


                Out1Script = outOpReturn + out1size + Out1Script;

                //returnDataNew = Out1Script;

                //totalOutString = totalOutString + Out1Script;

            }
            else
            {
                Keygen pubKey = new Keygen();

                int j = i - nORs;
                //String out2Sat = "00c8000000000000"; //Total - TXSize - fee
                //String out2Sat = "7bc7000000000000"; //Total - TXSize - fee
                //String out2Sat = ""; //Total - TXSize - fee

                String PayWallet160 = pubKey.addRMD(PayWallets[j]);

                long ivalue = Integer.valueOf(PayValues[j]);

                String paySTRFinal = "";
                paySTRFinal = "1976a914" + PayWallet160 + "88ac";

                if( i < nOutPuts -1 )
                    totalSpent = totalSpent + ivalue;
                    //totalSpent = 2000 + ivalue; // não utilizar
                else
                {
                    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                    //Neste ponto eh calculado o valor de retorno para a carteira original,
                    // menos a taxa oferecida aos mineradores
                    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

                    /////////////////////////////////////////////////////////////////////////////////////
                    //ivalue = ivalue - totalSpent - 1;
                    //A taxa Minima para ser testada na Gorilla Pool, a WhatsOnChain também aceitou em 04/07/2022;
                    // Transações enviadas entre 19:50 e 20:00 de 04/07/2022
                    //2d046b7bc16cb5863482d293b6a5fd9738945bace98f93abfcb46ca40ab364ab
                    //b564a1212d0d5b0a4c1333de640e8dec387a05b1ed5d1580efbd0d4cd4338d05
                    //Gorilla Pool Minerou 2 Blocos as 20:30 3 20:35 de 04/07/2022 mas não levou as TXs
                    //As transações foram removidas da MemPool depois de cerca de 30h
                    /////////////////////////////////////////////////////////////////////////////////////
                    // Esta taxa pode ser interessantes para comunicar dados que não queremos que permaneçam na rede
                    // Usar a MemPool como canal de comunicação
                    /////////////////////////////////////////////////////////////////////////////////////

                    String paySTR = "0000000000000000"+"1976a914" + PayWallet160 + "88ac" + "00000000";
                    //paySTRFinal = "1976a914" + PayWallet160 + "88ac";
                    //Out2Script = "1976a914" + PayWallet160 + "88ac";

                    /*
                    if(returnDataNew.length()>0)
                    {
                        String outputSize = Integer.toHexString(
                                ("76a914" + PayWallet160 + "88ac" + returnDataNew).length() / 2
                        );
                        while (outputSize.length() < 2)
                            outputSize = "0" + outputSize;

                        paySTR = "0000000000000000" + outputSize + "76a914" + PayWallet160 + "88ac" + returnDataNew + "00000000";
                        paySTRFinal = outputSize + "76a914" + PayWallet160 + "88ac" + returnDataNew;


                        returnDataNew = "";
                    }
                    */





                    ivalue = ivalue - totalSpent -
                    //ivalue = 2000 - totalSpent -
                    //ivalue = 1000 - totalSpent -
                    //ivalue = 3000 - totalSpent -
                            ((
                                    inputPreString.length() +
                                            totalOutString.length() +
                                            //("0000000000000000"+"1976a914" + PayWallet160 + "88ac" + "00000000").length()
                                            paySTR.length()
                            )/2)
                                    // / 2; //0.5 Satoshis por byte
                                    / 20; //0.05 Satoshis por byte

                    // (03/07/2022) 0.05 sat/byte ainda é limite mais segura para menor taxa
                    // Mais baixo que isso, ou fica congelado, ou semente a Gorilla Pool aceita

                    // / 30; //0.033 Satoshis por byte
                    // 2 TX enviadas por volta de 19:35 de 03/07/2022
                    // Gorilla Pool has mined the 2 TXx on 04/07/2022
                    // https://whatsonchain.com/tx/03840ceecf29f079879b4b49ea96e879d9ff5ae69b5315944da95363ab746136
                    // https://whatsonchain.com/tx/b0dc1711dda5994eb27eb3cff9de75fc9026d16ebad9077ba4b96386437f9bc0

                    // / 40; //0.025 Satoshis por byte
                    // TAAL não permite entrada de TX com taxa < 0.032 sat/byte
                    // TX recusada pela TAAL em 03/07/2022
                    //There was an issue with the broadcast:unexpected response code 500: 66: mempool min fee not met


                    // / 200; //0.005 Satoshis por byte - funciona, mas ainda demora demais.
                    // 0.005 sat/b demora cerca de 6 hora para ser minerado em 30/06/2022
                    // Somente Gorilla Pool aceita TXs com estas taxas por enquanto (03/07/2022);
                }

                Variables.SatBalance = Long.toString(ivalue);
                out2Sat = Long.toHexString(ivalue);

                while (out2Sat.length() < 16)
                    out2Sat = "0" + out2Sat;

                out2Sat = SHA256G.LEformat(out2Sat);

                //String PayWallet160 = pubKey.addRMD(PayWallets[j]);

                //String Out1Script = "1976a914" + "61079f5031a5b7e312d9fc5051fd7ce018fabc92" + "88ac";
                //String Out2Script = "1976a914" + BSV160 + "88ac";


                //String Out2Script = "1976a914" + PayWallet160 + "88ac";
                //Out2Script = "1976a914" + PayWallet160 + "88ac";
                //Variables.ScriptADD = paySTRFinal;

                /*

                //Teste de envio de SATOSHIS para token do MoneyButton

                //String testeScript = "610773667040302e33226535346638666639306535302e6173736574406d6f6e6579627574746f6e2e636f6d14036d480462d6bc7b69b303cd6688e4bfb9e13a1314f05528b5ffe7e4bbcc12a6a5ec39afb9e889b99414ed7af4b6b613d5b38ca4b1f6ebd98ef2f5a6354a463044022015da9754c1cb6a6d3f0c663e7e56097905b6a2d4f87cbb3314703c3d177f780e0220115466bc532693fa83cba774b40e10d61dfd5a18690f877f08f3c575bfda3baa241d804c4bf719543f8153bf42c68e3163c4b0ab5908340b4423fd06d61459eb6500000000000000000000005d79577a75567a567a567a567a567a567a5c79567a75557a557a557a557a557a5b79557a75547a547a547a547a5a79547a75537a537a537a5979537a75527a527a5779527a75517a5879517a75615f7901008791635e79a9537987695f795f79ac696851790087916900790087916956795e798769011479a954798769011579011579ac69011279a955798769011379011379ac777777777777777777777777777777777777777777776a0b0100000000000000080000";
                String testeScript = "fd9501610773667040302e33226535346638666639306535302e6173736574406d6f6e6579627574746f6e2e636f6d14036d480462d6bc7b69b303cd6688e4bfb9e13a1314f05528b5ffe7e4bbcc12a6a5ec39afb9e889b99414ed7af4b6b613d5b38ca4b1f6ebd98ef2f5a6354a463044022015da9754c1cb6a6d3f0c663e7e56097905b6a2d4f87cbb3314703c3d177f780e0220115466bc532693fa83cba774b40e10d61dfd5a18690f877f08f3c575bfda3baa241d804c4bf719543f8153bf42c68e3163c4b0ab5908340b4423fd06d61459eb6500000000000000000000005d79577a75567a567a567a567a567a567a5c79567a75557a557a557a557a557a5b79557a75547a547a547a547a5a79547a75537a537a537a5979537a75527a527a5779527a75517a5879517a75615f7901008791635e79a9537987695f795f79ac696851790087916900790087916956795e798769011479a954798769011579011579ac69011279a955798769011379011379ac777777777777777777777777777777777777777777776a0b0100000000000000080000";

                if(returnDataNew.length()>0)
                {
                    paySTRFinal = testeScript;

                    returnDataNew = "";
                }
                */



                if(returnDataNew.length()>0)
                {

                    /*
                    String outputSize = Integer.toHexString(
                            ("76a914" + PayWallet160 + "88ac" + returnDataNew).length() / 2
                    );
                    while (outputSize.length() < 2)
                        outputSize = "0" + outputSize;

                    //paySTR = "0000000000000000" + outputSize + "76a914" + PayWallet160 + "88ac" + returnDataNew + "00000000";
                    paySTRFinal = outputSize + "76a914" + PayWallet160 + "88ac" + returnDataNew;

                    */

                    ////////////////////////////////////////////
                    ////////////////////////////////////////////

                    String out1size = "00";
                    String byteSizeout1size = "";
                    //String Out1Script = "1976a914" + PayWallet160 + "88ac";
                    String Out1Script = "76a914" + PayWallet160 + "88ac" + returnDataNew;

                    out1size = Integer.toHexString(
                            Out1Script.length() / 2
                    );

                    if(out1size.length() % 2 == 1)
                        out1size = "0" + out1size;

                    //out1size = SHA256G.LEformat(out1size);


                    ////////////////////////////////////////////
                    //Ajuste do tamanho do Script e indicação do byte de tamanho em bytes
                    // Só pode ser indicado a partir desta posição para não dar conflito
                    // se o script ultrapassar os bytes indicados para os dados
                    ////////////////////////////////////////////

                    //if((Out1Script.length() / 2) >= 0x01 && (Out1Script.length() / 2) <= 0xff) {
                    if((Out1Script.length() / 2) >= 0x01 && (Out1Script.length() / 2) <= 0xfc) {
                        //out1size = out1size;
                        byteSizeout1size = "";

                        out1size = byteSizeout1size + SHA256G.LEformat(out1size);
                    }
                    //else if ((Out1Script.length() / 2) >= 0x0100 && (Out1Script.length() / 2) <= 0xffff) {
                    else if ((Out1Script.length() / 2) >= 0x00fd && (Out1Script.length() / 2) <= 0xffff) {

                        byteSizeout1size = "fd";

                        while (out1size.length() < 4)
                            out1size = "0" + out1size;

                        out1size = byteSizeout1size + SHA256G.LEformat(out1size);
                    }
                    //else if ((out1Data.length() / 2) >= 0x10000 && (out1Data.length() / 2) <= 0xffffffff)
                    //Limit 99 kbytes por OP_RETURN
                    else if ((Out1Script.length() / 2) >= 0x00010000 && (Out1Script.length() / 2) <= 0x000182b8) {

                        byteSizeout1size = "fe";

                        while (out1size.length() < 8)
                            out1size = "0" + out1size;

                        out1size = byteSizeout1size + SHA256G.LEformat(out1size);
                    }

                    ////////////////////////////////////////////
                    ////////////////////////////////////////////


                    //paySTRFinal = outputSize + "76a914" + PayWallet160 + "88ac" + returnDataNew;

                    //Out1Script = outOpReturn + out1size + Out1Script;

                    paySTRFinal = out1size + Out1Script;



                    returnDataNew = "";
                }



                String Out2Script = paySTRFinal;

                totalOutString = totalOutString + out2Sat + Out2Script;

            }
        }

        //Se o LockTime for referente a um bloco futuro, a TX só será minerada quando este bloco acontecer
        //String
        //lockTime = "00000000";

        //long txFee = Long.valueOf(SHA256G.LEformat(out2Sat));

        totalOutString = totalOutString + lockTime;// + out2Sat;//  + Long.toHexString(txFee);

        //return  inputPreString + totalOutString;
        return  totalOutString;
    }


    public String OutputStringV3(int nOutPuts, String[] PayWallets, String[] PayValues, int nORs, String[] OP_RETURNs,
                                 String inputPreString, int TXType)
    {

        //Just for melting tokens
        String totalOutString = "";

        PayValues[0] = PayValues[1];

        nOutPuts = 1;

        String nOuts = Integer.toHexString(nOutPuts);

        while (nOuts.length() < 2)
            nOuts = "0" + nOuts;

        totalOutString = nOuts;

        String out2Sat = ""; //Total - TXSize - fee
        long totalSpent = 0;

        //O ultimo output será sempre o retorno

        for(int i = 0; i < nOutPuts; i ++)
        {

            if( i < nORs)
            {
                String outOpReturn = "0000000000000000";
                String out1size = "00";
                String out1ScriptType = "00" + "6a"; // OP_Return
                String out1DataSizeType = "4c";//only one byte
                String byteSizeout1size = "";
                //String out1DataSizeType;
                String out1DataSize = "00";//only one byte

                //String out1Data = "2e2e2e617420746865206e616d65206f66204a65737573206576657279206b6e65652073686f756c6420626f772c206f66207468696e677320696e2068656176656e2c20616e64207468696e677320696e2065617274682c20616e64207468696e677320756e646572207468652065617274683b";
                String out1Data = OP_RETURNs[i];

                out1DataSize = Integer.toHexString(
                        out1Data.length() / 2
                );

                if(out1DataSize.length() % 2 == 1)
                    out1DataSize = "0" + out1DataSize;

                ////////////////////////////////////////////
                //OP_RETURN size
                ////////////////////////////////////////////

                //Verificar se OP_RETURN aceita a otimização para dado com menos de 0x4c bytes
                //Aparentemente funcionou

                if((out1Data.length() / 2) >= 0x01 && (out1Data.length() / 2) <= 0x4b) {
                    out1DataSizeType = "";
                    //byteSizeout1size = "";
                }

                //if((out1Data.length() / 2)>=1 && (out1Data.length() / 2)<=256)

                //if((out1Data.length() / 2) >= 0x01 && (out1Data.length() / 2) <= 0xff) {
                else if((out1Data.length() / 2) >= 0x4c && (out1Data.length() / 2) <= 0xff) {
                    out1DataSizeType = "4c";
                    //byteSizeout1size = "";
                }
                else if ((out1Data.length() / 2) >= 0x0100 && (out1Data.length() / 2) <= 0xffff) {
                    out1DataSizeType = "4d";
                    //byteSizeout1size = "fd";

                    while (out1DataSize.length() < 4)
                        out1DataSize = "0" + out1DataSize;

                    out1DataSize = SHA256G.LEformat(out1DataSize);


                }
                //else if ((out1Data.length() / 2) >= 0x10000 && (out1Data.length() / 2) <= 0xffffffff)
                //Limit 99 kbytes por OP_RETURN
                else if ((out1Data.length() / 2) >= 0x00010000 && (out1Data.length() / 2) <= 0x000182b8) {
                    out1DataSizeType = "4e";
                    //byteSizeout1size = "fe";

                    while (out1DataSize.length() < 8)
                        out1DataSize = "0" + out1DataSize;

                    out1DataSize = SHA256G.LEformat(out1DataSize);
                }

                ////////////////////////////////////////////
                ////////////////////////////////////////////

                //String Out1Script = "1976a914" + PayWallet160 + "88ac";
                String Out1Script = out1ScriptType + out1DataSizeType + out1DataSize + out1Data;

                out1size = Integer.toHexString(
                        Out1Script.length() / 2
                );

                if(out1size.length() % 2 == 1)
                    out1size = "0" + out1size;

                //out1size = SHA256G.LEformat(out1size);


                ////////////////////////////////////////////
                //Ajuste do tamanho do Script e indicação do byte de tamanho em bytes
                // Só pode ser indicado a partir desta posição para não dar conflito
                // se o script ultrapassar os bytes indicados para os dados
                ////////////////////////////////////////////

                //if((Out1Script.length() / 2) >= 0x01 && (Out1Script.length() / 2) <= 0xff) {
                if((Out1Script.length() / 2) >= 0x01 && (Out1Script.length() / 2) <= 0xfc) {
                    //out1size = out1size;
                    byteSizeout1size = "";

                    out1size = byteSizeout1size + SHA256G.LEformat(out1size);
                }
                //else if ((Out1Script.length() / 2) >= 0x0100 && (Out1Script.length() / 2) <= 0xffff) {
                else if ((Out1Script.length() / 2) >= 0x00fd && (Out1Script.length() / 2) <= 0xffff) {

                    byteSizeout1size = "fd";

                    while (out1size.length() < 4)
                        out1size = "0" + out1size;

                    out1size = byteSizeout1size + SHA256G.LEformat(out1size);
                }
                //else if ((out1Data.length() / 2) >= 0x10000 && (out1Data.length() / 2) <= 0xffffffff)
                //Limit 99 kbytes por OP_RETURN
                //Verificar se OP_RETURN a expansão até o limite máximo de 0xffffffff
                else if ((Out1Script.length() / 2) >= 0x00010000 && (Out1Script.length() / 2) <= 0x000182b8) {

                    byteSizeout1size = "fe";

                    while (out1size.length() < 8)
                        out1size = "0" + out1size;

                    out1size = byteSizeout1size + SHA256G.LEformat(out1size);
                }

                ////////////////////////////////////////////
                ////////////////////////////////////////////

                Out1Script = outOpReturn + out1size + Out1Script;

                totalOutString = totalOutString + Out1Script;

            }
            else
            {
                Keygen pubKey = new Keygen();

                int j = i - nORs;

                //String out2Sat = "00c8000000000000"; //Total - TXSize - fee
                //String out2Sat = "7bc7000000000000"; //Total - TXSize - fee
                //String out2Sat = ""; //Total - TXSize - fee

                //String PayWallet160 = pubKey.addRMD(PayWallets[j]);

                String PayWallet160 = "";

                if(TXType == 8 && j == 0)
                {
                    PayWallet160 = PDPUtils.strToHEXStr(PayWallets[j]);
                }
                else
                {
                    PayWallet160 = pubKey.addRMD(PayWallets[j]);
                }

                long ivalue = Integer.valueOf(PayValues[j]);

                if( i < nOutPuts -1 )
                    totalSpent = totalSpent + ivalue;
                else
                {
                    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                    //Neste ponto eh calculado o valor de retorno para a carteira original,
                    // menos a taxa oferecida aos mineradores
                    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

                    /////////////////////////////////////////////////////////////////////////////////////
                    //ivalue = ivalue - totalSpent - 1;
                    //A taxa Minima para ser testada na Gorilla Pool, a WhatsOnChain também aceitou em 04/07/2022;
                    // Transações enviadas entre 19:50 e 20:00 de 04/07/2022
                    //2d046b7bc16cb5863482d293b6a5fd9738945bace98f93abfcb46ca40ab364ab
                    //b564a1212d0d5b0a4c1333de640e8dec387a05b1ed5d1580efbd0d4cd4338d05
                    //Gorilla Pool Minerou 2 Blocos as 20:30 3 20:35 de 04/07/2022 mas não levou as TXs
                    //As transações foram removidas da MemPool depois de cerca de 30h
                    /////////////////////////////////////////////////////////////////////////////////////
                    // Esta taxa pode ser interessantes para comunicar dados que não queremos que permaneçam na rede
                    // Usar a MemPool como canal de comunicação
                    /////////////////////////////////////////////////////////////////////////////////////


                    ivalue = ivalue - totalSpent -
                            ((
                                    inputPreString.length() +
                                            totalOutString.length() +
                                            ("0000000000000000"+"1976a914" + PayWallet160 + "88ac" + "00000000").length()
                            )/2)
                                    // / 2; //0.5 Satoshis por byte
                                    / 20; //0.05 Satoshis por byte

                    // (03/07/2022) 0.05 sat/byte ainda é limite mais segura para menor taxa
                    // Mais baixo que isso, ou fica congelado, ou semente a Gorilla Pool aceita

                    // / 30; //0.033 Satoshis por byte
                    // 2 TX enviadas por volta de 19:35 de 03/07/2022
                    // Gorilla Pool has mined the 2 TXx on 04/07/2022
                    // https://whatsonchain.com/tx/03840ceecf29f079879b4b49ea96e879d9ff5ae69b5315944da95363ab746136
                    // https://whatsonchain.com/tx/b0dc1711dda5994eb27eb3cff9de75fc9026d16ebad9077ba4b96386437f9bc0

                    // / 40; //0.025 Satoshis por byte
                    // TAAL não permite entrada de TX com taxa < 0.032 sat/byte
                    // TX recusada pela TAAL em 03/07/2022
                    //There was an issue with the broadcast:unexpected response code 500: 66: mempool min fee not met


                    // / 200; //0.005 Satoshis por byte - funciona, mas ainda demora demais.
                    // 0.005 sat/b demora cerca de 6 hora para ser minerado em 30/06/2022
                    // Somente Gorilla Pool aceita TXs com estas taxas por enquanto (03/07/2022);
                }

                Variables.SatBalance = Long.toString(ivalue);
                out2Sat = Long.toHexString(ivalue);

                while (out2Sat.length() < 16)
                    out2Sat = "0" + out2Sat;

                out2Sat = SHA256G.LEformat(out2Sat);

                //String PayWallet160 = pubKey.addRMD(PayWallets[j]);

                //String Out1Script = "1976a914" + "61079f5031a5b7e312d9fc5051fd7ce018fabc92" + "88ac";
                //String Out2Script = "1976a914" + BSV160 + "88ac";
                String Out2Script = "";

                if(TXType == 8)//Only 1 Byte Size Total a string precisa ser < 253
                {
                    String sizeSCR = Integer.toHexString(PayWallet160.length()/2);

                    while (sizeSCR.length() < 2)
                        sizeSCR = "0" + sizeSCR;

                    Out2Script = "51" + sizeSCR + PayWallet160 + "75"; //OP_DROP

                    sizeSCR = Integer.toHexString(Out2Script.length()/2);

                    while (sizeSCR.length() < 2)
                        sizeSCR = "0" + sizeSCR;

                    Out2Script = sizeSCR + Out2Script;

                }
                else
                    Out2Script = "1976a914" + PayWallet160 + "88ac";

                totalOutString = totalOutString + out2Sat + Out2Script;

            }


        }

        //Se o LockTime for referente a um bloco futuro, a TX só será minerada quando este bloco acontecer
        //String
        //lockTime = "00000000";


        //long txFee = Long.valueOf(SHA256G.LEformat(out2Sat));



        totalOutString = totalOutString + lockTime;// + out2Sat;//  + Long.toHexString(txFee);

        //return  inputPreString + totalOutString;
        return  totalOutString;
    }


    public String OutputStringV4(int nOutPuts, String[] PayWallets, String[] PayValues, int nORs, String ScriptHEX,
                                 String inputPreString, int tokenType)
    {
        String totalOutString = "";

        //String nOuts = Integer.toHexString(nOutPuts);
        String nOuts = Integer.toHexString(nOutPuts - 1);

        while (nOuts.length() < 2)
            nOuts = "0" + nOuts;

        totalOutString = nOuts;

        String out2Sat = ""; //Total - TXSize - fee
        long totalSpent = 0;

        //O ultimo output será sempre o retorno

        String returnDataNew = ""; //somente 1 OP_RETURN
        //String Out2Script = "";




        for(int i = 0; i < nOutPuts; i ++)
        {

            if( i < nORs)
            {
                //Keygen pubKey = new Keygen();

                //returnDataNew = ScriptHEX;
                //returnDataNew = ScriptHEX.substring(0,6) + pubKey.addRMD(PayWallets[0]) +
                //        ScriptHEX.substring(6+20, ScriptHEX.length());
                returnDataNew = ScriptHEX.substring(6+40+4, ScriptHEX.length()-1);

                //return  returnDataNew;

            }
            else
            {
                Keygen pubKey = new Keygen();

                int j = i - nORs;
                //String out2Sat = "00c8000000000000"; //Total - TXSize - fee
                //String out2Sat = "7bc7000000000000"; //Total - TXSize - fee
                //String out2Sat = ""; //Total - TXSize - fee

                String PayWallet160 = pubKey.addRMD(PayWallets[j]);

                long ivalue = Integer.valueOf(PayValues[j]);

                String paySTRFinal = "";
                paySTRFinal = "1976a914" + PayWallet160 + "88ac";

                if( i < nOutPuts -1 )
                    totalSpent = totalSpent + ivalue;
                    //totalSpent = 2000 + ivalue; // não utilizar
                else
                {
                    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                    //Neste ponto eh calculado o valor de retorno para a carteira original,
                    // menos a taxa oferecida aos mineradores
                    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

                    /////////////////////////////////////////////////////////////////////////////////////
                    //ivalue = ivalue - totalSpent - 1;
                    //A taxa Minima para ser testada na Gorilla Pool, a WhatsOnChain também aceitou em 04/07/2022;
                    // Transações enviadas entre 19:50 e 20:00 de 04/07/2022
                    //2d046b7bc16cb5863482d293b6a5fd9738945bace98f93abfcb46ca40ab364ab
                    //b564a1212d0d5b0a4c1333de640e8dec387a05b1ed5d1580efbd0d4cd4338d05
                    //Gorilla Pool Minerou 2 Blocos as 20:30 3 20:35 de 04/07/2022 mas não levou as TXs
                    //As transações foram removidas da MemPool depois de cerca de 30h
                    /////////////////////////////////////////////////////////////////////////////////////
                    // Esta taxa pode ser interessantes para comunicar dados que não queremos que permaneçam na rede
                    // Usar a MemPool como canal de comunicação
                    /////////////////////////////////////////////////////////////////////////////////////

                    String paySTR = "0000000000000000"+"1976a914" + PayWallet160 + "88ac" + "00000000";
                    //paySTRFinal = "1976a914" + PayWallet160 + "88ac";
                    //Out2Script = "1976a914" + PayWallet160 + "88ac";


                    ivalue = ivalue - totalSpent -
                            //ivalue = 2000 - totalSpent -
                            //ivalue = 1000 - totalSpent -
                            //ivalue = 3000 - totalSpent -
                            ((
                                    inputPreString.length() +
                                            totalOutString.length() +
                                            //("0000000000000000"+"1976a914" + PayWallet160 + "88ac" + "00000000").length()
                                            paySTR.length()
                            )/2)
                                    // / 2; //0.5 Satoshis por byte
                                    / 20; //0.05 Satoshis por byte

                    // (03/07/2022) 0.05 sat/byte ainda é limite mais segura para menor taxa
                    // Mais baixo que isso, ou fica congelado, ou semente a Gorilla Pool aceita

                    // / 30; //0.033 Satoshis por byte
                    // 2 TX enviadas por volta de 19:35 de 03/07/2022
                    // Gorilla Pool has mined the 2 TXx on 04/07/2022
                    // https://whatsonchain.com/tx/03840ceecf29f079879b4b49ea96e879d9ff5ae69b5315944da95363ab746136
                    // https://whatsonchain.com/tx/b0dc1711dda5994eb27eb3cff9de75fc9026d16ebad9077ba4b96386437f9bc0

                    // / 40; //0.025 Satoshis por byte
                    // TAAL não permite entrada de TX com taxa < 0.032 sat/byte
                    // TX recusada pela TAAL em 03/07/2022
                    //There was an issue with the broadcast:unexpected response code 500: 66: mempool min fee not met


                    // / 200; //0.005 Satoshis por byte - funciona, mas ainda demora demais.
                    // 0.005 sat/b demora cerca de 6 hora para ser minerado em 30/06/2022
                    // Somente Gorilla Pool aceita TXs com estas taxas por enquanto (03/07/2022);

                   // if(Variables.trocoECDSA.length() == 16 && Variables.kTEST.compareTo(BigInteger.valueOf(0)) != 0) {
                   //     out2Sat = Variables.trocoECDSA;
                   //     Variables.flagECDSA = true;
                   // }

                }

                Variables.SatBalance = Long.toString(ivalue);

                //if(!Variables.flagECDSA) {

                    out2Sat = Long.toHexString(ivalue);

                    while (out2Sat.length() < 16)
                        out2Sat = "0" + out2Sat;

                    out2Sat = SHA256G.LEformat(out2Sat);
                //}
                //Variables.flagECDSA = false;



                //String PayWallet160 = pubKey.addRMD(PayWallets[j]);

                //String Out1Script = "1976a914" + "61079f5031a5b7e312d9fc5051fd7ce018fabc92" + "88ac";
                //String Out2Script = "1976a914" + BSV160 + "88ac";


                //String Out2Script = "1976a914" + PayWallet160 + "88ac";
                //Out2Script = "1976a914" + PayWallet160 + "88ac";
                //Variables.ScriptADD = paySTRFinal;



                if(returnDataNew.length()>0)
                {


                    ////////////////////////////////////////////
                    ////////////////////////////////////////////

                    String out1size = "00";
                    String byteSizeout1size = "";
                    //String Out1Script = "1976a914" + PayWallet160 + "88ac";
                    String Out1Script = "76a914" + PayWallet160 + "88ac" + returnDataNew;

                    out1size = Integer.toHexString(
                            Out1Script.length() / 2
                    );

                    if(out1size.length() % 2 == 1)
                        out1size = "0" + out1size;

                    //out1size = SHA256G.LEformat(out1size);


                    ////////////////////////////////////////////
                    //Ajuste do tamanho do Script e indicação do byte de tamanho em bytes
                    // Só pode ser indicado a partir desta posição para não dar conflito
                    // se o script ultrapassar os bytes indicados para os dados
                    ////////////////////////////////////////////

                    //if((Out1Script.length() / 2) >= 0x01 && (Out1Script.length() / 2) <= 0xff) {
                    if((Out1Script.length() / 2) >= 0x01 && (Out1Script.length() / 2) <= 0xfc) {
                        //out1size = out1size;
                        byteSizeout1size = "";

                        out1size = byteSizeout1size + SHA256G.LEformat(out1size);
                    }
                    //else if ((Out1Script.length() / 2) >= 0x0100 && (Out1Script.length() / 2) <= 0xffff) {
                    else if ((Out1Script.length() / 2) >= 0x00fd && (Out1Script.length() / 2) <= 0xffff) {

                        byteSizeout1size = "fd";

                        while (out1size.length() < 4)
                            out1size = "0" + out1size;

                        out1size = byteSizeout1size + SHA256G.LEformat(out1size);
                    }
                    //else if ((out1Data.length() / 2) >= 0x10000 && (out1Data.length() / 2) <= 0xffffffff)
                    //Limit 99 kbytes por OP_RETURN
                    else if ((Out1Script.length() / 2) >= 0x00010000 && (Out1Script.length() / 2) <= 0x000182b8) {

                        byteSizeout1size = "fe";

                        while (out1size.length() < 8)
                            out1size = "0" + out1size;

                        out1size = byteSizeout1size + SHA256G.LEformat(out1size);
                    }

                    ////////////////////////////////////////////
                    ////////////////////////////////////////////


                    //paySTRFinal = outputSize + "76a914" + PayWallet160 + "88ac" + returnDataNew;

                    //Out1Script = outOpReturn + out1size + Out1Script;

                    paySTRFinal = out1size + Out1Script;



                    returnDataNew = "";
                }



                String Out2Script = paySTRFinal;

                totalOutString = totalOutString + out2Sat + Out2Script;

            }


        }

        //Se o LockTime for referente a um bloco futuro, a TX só será minerada quando este bloco acontecer
        //String
        //        lockTime = "00000000";

        //long txFee = Long.valueOf(SHA256G.LEformat(out2Sat));

        totalOutString = totalOutString + lockTime;// + out2Sat;//  + Long.toHexString(txFee);

        //return  inputPreString + totalOutString;
        return  totalOutString;
    }

    //Cria um Output OP_DROP
    public String OutputStringV5(int nOutPuts, String[] PayWallets, String[] PayValues, int nORs, String[] OP_RETURNs, String inputPreString)
    {
        String totalOutString = "";


        //String nOuts = Integer.toHexString(nOutPuts);
        String nOuts = Integer.toHexString(nOutPuts - 1);

        while (nOuts.length() < 2)
            nOuts = "0" + nOuts;

        totalOutString = nOuts;

        String out2Sat = ""; //Total - TXSize - fee
        long totalSpent = 0;

        //O ultimo output será sempre o retorno

        String returnDataNew = ""; //somente 1 OP_RETURN
        //String Out2Script = "";

        for(int i = 0; i < nOutPuts; i ++)
        {

            if( i < nORs)
            {
                String outOpReturn = "0000000000000000";
                String out1size = "00";
                String out1ScriptType = "00" + "6a"; // OP_Return
                String out1DataSizeType = "4c";//only one byte
                String byteSizeout1size = "";
                //String out1DataSizeType;
                String out1DataSize = "00";//only one byte

                //String out1Data = "2e2e2e617420746865206e616d65206f66204a65737573206576657279206b6e65652073686f756c6420626f772c206f66207468696e677320696e2068656176656e2c20616e64207468696e677320696e2065617274682c20616e64207468696e677320756e646572207468652065617274683b";
                String out1Data = OP_RETURNs[i];

                out1DataSize = Integer.toHexString(
                        out1Data.length() / 2
                );

                if(out1DataSize.length() % 2 == 1)
                    out1DataSize = "0" + out1DataSize;

                ////////////////////////////////////////////
                //OP_RETURN size
                ////////////////////////////////////////////

                //if((out1Data.length() / 2)>=1 && (out1Data.length() / 2)<=256)
                if((out1Data.length() / 2) >= 0x01 && (out1Data.length() / 2) <= 0xff) {
                    out1DataSizeType = "4c";
                    //byteSizeout1size = "";
                }
                else if ((out1Data.length() / 2) >= 0x0100 && (out1Data.length() / 2) <= 0xffff) {
                    out1DataSizeType = "4d";
                    //byteSizeout1size = "fd";

                    while (out1DataSize.length() < 4)
                        out1DataSize = "0" + out1DataSize;

                    out1DataSize = SHA256G.LEformat(out1DataSize);


                }
                //else if ((out1Data.length() / 2) >= 0x10000 && (out1Data.length() / 2) <= 0xffffffff)
                //Limit 99 kbytes por OP_RETURN
                else if ((out1Data.length() / 2) >= 0x00010000 && (out1Data.length() / 2) <= 0x000182b8) {
                    out1DataSizeType = "4e";
                    //byteSizeout1size = "fe";

                    while (out1DataSize.length() < 8)
                        out1DataSize = "0" + out1DataSize;

                    out1DataSize = SHA256G.LEformat(out1DataSize);
                }

                ////////////////////////////////////////////
                ////////////////////////////////////////////

                //String Out1Script = "1976a914" + PayWallet160 + "88ac";
                String Out1Script = out1ScriptType + out1DataSizeType + out1DataSize + out1Data;

                out1size = Integer.toHexString(
                        Out1Script.length() / 2
                );

                if(out1size.length() % 2 == 1)
                    out1size = "0" + out1size;

                //out1size = SHA256G.LEformat(out1size);


                ////////////////////////////////////////////
                //Ajuste do tamanho do Script e indicação do byte de tamanho em bytes
                // Só pode ser indicado a partir desta posição para não dar conflito
                // se o script ultrapassar os bytes indicados para os dados
                ////////////////////////////////////////////

                //if((Out1Script.length() / 2) >= 0x01 && (Out1Script.length() / 2) <= 0xff) {
                if((Out1Script.length() / 2) >= 0x01 && (Out1Script.length() / 2) <= 0xfc) {
                    //out1size = out1size;
                    byteSizeout1size = "";

                    out1size = byteSizeout1size + SHA256G.LEformat(out1size);
                }
                //else if ((Out1Script.length() / 2) >= 0x0100 && (Out1Script.length() / 2) <= 0xffff) {
                else if ((Out1Script.length() / 2) >= 0x00fd && (Out1Script.length() / 2) <= 0xffff) {

                    byteSizeout1size = "fd";

                    while (out1size.length() < 4)
                        out1size = "0" + out1size;

                    out1size = byteSizeout1size + SHA256G.LEformat(out1size);
                }
                //else if ((out1Data.length() / 2) >= 0x10000 && (out1Data.length() / 2) <= 0xffffffff)
                //Limit 99 kbytes por OP_RETURN
                else if ((Out1Script.length() / 2) >= 0x00010000 && (Out1Script.length() / 2) <= 0x000182b8) {

                    byteSizeout1size = "fe";

                    while (out1size.length() < 8)
                        out1size = "0" + out1size;

                    out1size = byteSizeout1size + SHA256G.LEformat(out1size);
                }

                ////////////////////////////////////////////
                ////////////////////////////////////////////

                returnDataNew = Out1Script.substring(4)+"75";
                Out1Script = outOpReturn + out1size + Out1Script;

                //returnDataNew = Out1Script;

                //totalOutString = totalOutString + Out1Script;

            }
            else
            {
                Keygen pubKey = new Keygen();

                int j = i - nORs;
                //String out2Sat = "00c8000000000000"; //Total - TXSize - fee
                //String out2Sat = "7bc7000000000000"; //Total - TXSize - fee
                //String out2Sat = ""; //Total - TXSize - fee

                String PayWallet160 = pubKey.addRMD(PayWallets[j]);

                long ivalue = Integer.valueOf(PayValues[j]);

                String paySTRFinal = "";
                paySTRFinal = "1976a914" + PayWallet160 + "88ac";

                if( i < nOutPuts -1 )
                    totalSpent = totalSpent + ivalue;
                    //totalSpent = 2000 + ivalue; // não utilizar
                else
                {
                    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                    //Neste ponto eh calculado o valor de retorno para a carteira original,
                    // menos a taxa oferecida aos mineradores
                    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

                    /////////////////////////////////////////////////////////////////////////////////////
                    //ivalue = ivalue - totalSpent - 1;
                    //A taxa Minima para ser testada na Gorilla Pool, a WhatsOnChain também aceitou em 04/07/2022;
                    // Transações enviadas entre 19:50 e 20:00 de 04/07/2022
                    //2d046b7bc16cb5863482d293b6a5fd9738945bace98f93abfcb46ca40ab364ab
                    //b564a1212d0d5b0a4c1333de640e8dec387a05b1ed5d1580efbd0d4cd4338d05
                    //Gorilla Pool Minerou 2 Blocos as 20:30 3 20:35 de 04/07/2022 mas não levou as TXs
                    //As transações foram removidas da MemPool depois de cerca de 30h
                    /////////////////////////////////////////////////////////////////////////////////////
                    // Esta taxa pode ser interessantes para comunicar dados que não queremos que permaneçam na rede
                    // Usar a MemPool como canal de comunicação
                    /////////////////////////////////////////////////////////////////////////////////////

                    String paySTR = "0000000000000000"+"1976a914" + PayWallet160 + "88ac" + "00000000";
                    //paySTRFinal = "1976a914" + PayWallet160 + "88ac";
                    //Out2Script = "1976a914" + PayWallet160 + "88ac";

                    /*
                    if(returnDataNew.length()>0)
                    {
                        String outputSize = Integer.toHexString(
                                ("76a914" + PayWallet160 + "88ac" + returnDataNew).length() / 2
                        );
                        while (outputSize.length() < 2)
                            outputSize = "0" + outputSize;

                        paySTR = "0000000000000000" + outputSize + "76a914" + PayWallet160 + "88ac" + returnDataNew + "00000000";
                        paySTRFinal = outputSize + "76a914" + PayWallet160 + "88ac" + returnDataNew;


                        returnDataNew = "";
                    }
                    */





                    ivalue = ivalue - totalSpent -
                            //ivalue = 2000 - totalSpent -
                            //ivalue = 1000 - totalSpent -
                            //ivalue = 3000 - totalSpent -
                            ((
                                    inputPreString.length() +
                                            totalOutString.length() +
                                            //("0000000000000000"+"1976a914" + PayWallet160 + "88ac" + "00000000").length()
                                            paySTR.length()
                            )/2)
                                    // / 2; //0.5 Satoshis por byte
                                    / 20; //0.05 Satoshis por byte

                    // (03/07/2022) 0.05 sat/byte ainda é limite mais segura para menor taxa
                    // Mais baixo que isso, ou fica congelado, ou semente a Gorilla Pool aceita

                    // / 30; //0.033 Satoshis por byte
                    // 2 TX enviadas por volta de 19:35 de 03/07/2022
                    // Gorilla Pool has mined the 2 TXx on 04/07/2022
                    // https://whatsonchain.com/tx/03840ceecf29f079879b4b49ea96e879d9ff5ae69b5315944da95363ab746136
                    // https://whatsonchain.com/tx/b0dc1711dda5994eb27eb3cff9de75fc9026d16ebad9077ba4b96386437f9bc0

                    // / 40; //0.025 Satoshis por byte
                    // TAAL não permite entrada de TX com taxa < 0.032 sat/byte
                    // TX recusada pela TAAL em 03/07/2022
                    //There was an issue with the broadcast:unexpected response code 500: 66: mempool min fee not met


                    // / 200; //0.005 Satoshis por byte - funciona, mas ainda demora demais.
                    // 0.005 sat/b demora cerca de 6 hora para ser minerado em 30/06/2022
                    // Somente Gorilla Pool aceita TXs com estas taxas por enquanto (03/07/2022);
                }

                Variables.SatBalance = Long.toString(ivalue);
                out2Sat = Long.toHexString(ivalue);

                while (out2Sat.length() < 16)
                    out2Sat = "0" + out2Sat;

                out2Sat = SHA256G.LEformat(out2Sat);

                //String PayWallet160 = pubKey.addRMD(PayWallets[j]);

                //String Out1Script = "1976a914" + "61079f5031a5b7e312d9fc5051fd7ce018fabc92" + "88ac";
                //String Out2Script = "1976a914" + BSV160 + "88ac";


                //String Out2Script = "1976a914" + PayWallet160 + "88ac";
                //Out2Script = "1976a914" + PayWallet160 + "88ac";
                //Variables.ScriptADD = paySTRFinal;

                if(returnDataNew.length()>0)
                {
                    String outputSize = Integer.toHexString(
                            ("76a914" + PayWallet160 + "88ac" + returnDataNew).length() / 2
                    );
                    while (outputSize.length() < 2)
                        outputSize = "0" + outputSize;

                    //paySTR = "0000000000000000" + outputSize + "76a914" + PayWallet160 + "88ac" + returnDataNew + "00000000";
                    paySTRFinal = outputSize + "76a914" + PayWallet160 + "88ac" + returnDataNew;


                    returnDataNew = "";
                }

                String Out2Script = paySTRFinal;

                totalOutString = totalOutString + out2Sat + Out2Script;

            }


        }

        //Se o LockTime for referente a um bloco futuro, a TX só será minerada quando este bloco acontecer
        //String
        //        lockTime = "00000000";

        //long txFee = Long.valueOf(SHA256G.LEformat(out2Sat));

        totalOutString = totalOutString + lockTime;// + out2Sat;//  + Long.toHexString(txFee);

        //return  inputPreString + totalOutString;
        return  totalOutString;
    }

    public String OutputStringV6(int nOutPuts, String[] PayWallets, String[] PayValues, int nORs, String[] OP_RETURNs, String inputPreString)
    {
        String totalOutString = "";


        String nOuts = Integer.toHexString(nOutPuts);

        while (nOuts.length() < 2)
            nOuts = "0" + nOuts;

        totalOutString = nOuts;

        String out2Sat = ""; //Total - TXSize - fee
        long totalSpent = 0;

        //O ultimo output será sempre o retorno

        for(int i = 0; i < nOutPuts; i ++)
        {

            if( i < nORs)
            {
                String outOpReturn = "0000000000000000";
                String out1size = "00";
                String out1ScriptType = "00" + "6a"; // OP_Return
                String out1DataSizeType = "4c";//only one byte
                String byteSizeout1size = "";
                //String out1DataSizeType;
                String out1DataSize = "00";//only one byte

                //String out1Data = "2e2e2e617420746865206e616d65206f66204a65737573206576657279206b6e65652073686f756c6420626f772c206f66207468696e677320696e2068656176656e2c20616e64207468696e677320696e2065617274682c20616e64207468696e677320756e646572207468652065617274683b";
                String out1Data = OP_RETURNs[i];

                out1DataSize = Integer.toHexString(
                        out1Data.length() / 2
                );

                if(out1DataSize.length() % 2 == 1)
                    out1DataSize = "0" + out1DataSize;

                ////////////////////////////////////////////
                //OP_RETURN size
                ////////////////////////////////////////////

                //Verificar se OP_RETURN aceita a otimização para dado com menos de 0x4c bytes
                //Aparentemente funcionou

                if((out1Data.length() / 2) >= 0x01 && (out1Data.length() / 2) <= 0x4b) {
                    out1DataSizeType = "";
                    //byteSizeout1size = "";
                }

                //if((out1Data.length() / 2)>=1 && (out1Data.length() / 2)<=256)

                //if((out1Data.length() / 2) >= 0x01 && (out1Data.length() / 2) <= 0xff) {
                else if((out1Data.length() / 2) >= 0x4c && (out1Data.length() / 2) <= 0xff) {
                    out1DataSizeType = "4c";
                    //byteSizeout1size = "";
                }
                else if ((out1Data.length() / 2) >= 0x0100 && (out1Data.length() / 2) <= 0xffff) {
                    out1DataSizeType = "4d";
                    //byteSizeout1size = "fd";

                    while (out1DataSize.length() < 4)
                        out1DataSize = "0" + out1DataSize;

                    out1DataSize = SHA256G.LEformat(out1DataSize);


                }
                //else if ((out1Data.length() / 2) >= 0x10000 && (out1Data.length() / 2) <= 0xffffffff)
                //Limit 99 kbytes por OP_RETURN
                else if ((out1Data.length() / 2) >= 0x00010000 && (out1Data.length() / 2) <= 0x000182b8) {
                    out1DataSizeType = "4e";
                    //byteSizeout1size = "fe";

                    while (out1DataSize.length() < 8)
                        out1DataSize = "0" + out1DataSize;

                    out1DataSize = SHA256G.LEformat(out1DataSize);
                }

                ////////////////////////////////////////////
                ////////////////////////////////////////////

                //String Out1Script = "1976a914" + PayWallet160 + "88ac";
                String Out1Script = out1ScriptType + out1DataSizeType + out1DataSize + out1Data;

                out1size = Integer.toHexString(
                        Out1Script.length() / 2
                );

                if(out1size.length() % 2 == 1)
                    out1size = "0" + out1size;

                //out1size = SHA256G.LEformat(out1size);


                ////////////////////////////////////////////
                //Ajuste do tamanho do Script e indicação do byte de tamanho em bytes
                // Só pode ser indicado a partir desta posição para não dar conflito
                // se o script ultrapassar os bytes indicados para os dados
                ////////////////////////////////////////////

                //if((Out1Script.length() / 2) >= 0x01 && (Out1Script.length() / 2) <= 0xff) {
                if((Out1Script.length() / 2) >= 0x01 && (Out1Script.length() / 2) <= 0xfc) {
                    //out1size = out1size;
                    byteSizeout1size = "";

                    out1size = byteSizeout1size + SHA256G.LEformat(out1size);
                }
                //else if ((Out1Script.length() / 2) >= 0x0100 && (Out1Script.length() / 2) <= 0xffff) {
                else if ((Out1Script.length() / 2) >= 0x00fd && (Out1Script.length() / 2) <= 0xffff) {

                    byteSizeout1size = "fd";

                    while (out1size.length() < 4)
                        out1size = "0" + out1size;

                    out1size = byteSizeout1size + SHA256G.LEformat(out1size);
                }
                //else if ((out1Data.length() / 2) >= 0x10000 && (out1Data.length() / 2) <= 0xffffffff)
                //Limit 99 kbytes por OP_RETURN
                //Verificar se OP_RETURN a expansão até o limite máximo de 0xffffffff
                else if ((Out1Script.length() / 2) >= 0x00010000 && (Out1Script.length() / 2) <= 0x000182b8) {

                    byteSizeout1size = "fe";

                    while (out1size.length() < 8)
                        out1size = "0" + out1size;

                    out1size = byteSizeout1size + SHA256G.LEformat(out1size);
                }

                ////////////////////////////////////////////
                ////////////////////////////////////////////

                Out1Script = outOpReturn + out1size + Out1Script;

                totalOutString = totalOutString + Out1Script;

                //return totalOutString;

            }
            else
            {
                Keygen pubKey = new Keygen();

                int j = i - nORs;
                //String out2Sat = "00c8000000000000"; //Total - TXSize - fee
                //String out2Sat = "7bc7000000000000"; //Total - TXSize - fee
                //String out2Sat = ""; //Total - TXSize - fee

                String PayWallet160;

                //if(i == 0)
                if(j == 0)
                    PayWallet160 = PayWallets[j];
                else
                    PayWallet160 = pubKey.addRMD(PayWallets[j]);

                //if(i == 1)
                //    return totalOutString + " ASDFG: "+ nORs;


                long ivalue = Integer.valueOf(PayValues[j]);




                if( i < nOutPuts -1 )
                    totalSpent = totalSpent + ivalue;
                else
                {
                    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                    //Neste ponto eh calculado o valor de retorno para a carteira original,
                    // menos a taxa oferecida aos mineradores
                    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

                    /////////////////////////////////////////////////////////////////////////////////////
                    //ivalue = ivalue - totalSpent - 1;
                    //A taxa Minima para ser testada na Gorilla Pool, a WhatsOnChain também aceitou em 04/07/2022;
                    // Transações enviadas entre 19:50 e 20:00 de 04/07/2022
                    //2d046b7bc16cb5863482d293b6a5fd9738945bace98f93abfcb46ca40ab364ab
                    //b564a1212d0d5b0a4c1333de640e8dec387a05b1ed5d1580efbd0d4cd4338d05
                    //Gorilla Pool Minerou 2 Blocos as 20:30 3 20:35 de 04/07/2022 mas não levou as TXs
                    //As transações foram removidas da MemPool depois de cerca de 30h
                    /////////////////////////////////////////////////////////////////////////////////////
                    // Esta taxa pode ser interessantes para comunicar dados que não queremos que permaneçam na rede
                    // Usar a MemPool como canal de comunicação
                    /////////////////////////////////////////////////////////////////////////////////////


                    ivalue = ivalue - totalSpent -
                            ((
                                    inputPreString.length() +
                                            totalOutString.length() +
                                            ("0000000000000000"+"1976a914" + PayWallet160 + "88ac" + "00000000").length()
                            )/2)
                                    // / 2; //0.5 Satoshis por byte
                                    / 20; //0.05 Satoshis por byte

                    // (03/07/2022) 0.05 sat/byte ainda é limite mais segura para menor taxa
                    // Mais baixo que isso, ou fica congelado, ou semente a Gorilla Pool aceita

                    // / 30; //0.033 Satoshis por byte
                    // 2 TX enviadas por volta de 19:35 de 03/07/2022
                    // Gorilla Pool has mined the 2 TXx on 04/07/2022
                    // https://whatsonchain.com/tx/03840ceecf29f079879b4b49ea96e879d9ff5ae69b5315944da95363ab746136
                    // https://whatsonchain.com/tx/b0dc1711dda5994eb27eb3cff9de75fc9026d16ebad9077ba4b96386437f9bc0

                    // / 40; //0.025 Satoshis por byte
                    // TAAL não permite entrada de TX com taxa < 0.032 sat/byte
                    // TX recusada pela TAAL em 03/07/2022
                    //There was an issue with the broadcast:unexpected response code 500: 66: mempool min fee not met


                    // / 200; //0.005 Satoshis por byte - funciona, mas ainda demora demais.
                    // 0.005 sat/b demora cerca de 6 hora para ser minerado em 30/06/2022
                    // Somente Gorilla Pool aceita TXs com estas taxas por enquanto (03/07/2022);

                    //Tem que haver pelo menos 1 sat de taxa;
                    //if(ivalue<0)
                    //    ivalue = ivalue - totalSpent;
                }

                if(ivalue>0) { //se nao houver troco, nao executa no final

                    Variables.SatBalance = Long.toString(ivalue);
                    out2Sat = Long.toHexString(ivalue);

                    while (out2Sat.length() < 16)
                        out2Sat = "0" + out2Sat;

                    out2Sat = SHA256G.LEformat(out2Sat);

                    //String PayWallet160 = pubKey.addRMD(PayWallets[j]);

                    //String Out1Script = "1976a914" + "61079f5031a5b7e312d9fc5051fd7ce018fabc92" + "88ac";
                    //String Out2Script = "1976a914" + BSV160 + "88ac";
                    String Out2Script;
                    //if(i == 0) {
                    if(j == 0) {

                        if(PayWallet160.length()/2 == 0x41)
                            Out2Script = "4341" + PayWallet160 + "ac";
                        else
                            Out2Script = "2321" + PayWallet160 + "ac";
                    }
                    else
                        Out2Script = "1976a914" + PayWallet160 + "88ac";

                    totalOutString = totalOutString + out2Sat + Out2Script;
                }
                else{
                    nOuts = Integer.toHexString(nOutPuts - 1);

                    while (nOuts.length() < 2)
                        nOuts = "0" + nOuts;

                    totalOutString = nOuts + totalOutString.substring(2);
                }

                //return totalOutString;

            }


        }

        //Se o LockTime for referente a um bloco futuro, a TX só será minerada quando este bloco acontecer
        //String
        //        lockTime = "00000000";


        //long txFee = Long.valueOf(SHA256G.LEformat(out2Sat));



        totalOutString = totalOutString + lockTime;// + out2Sat;//  + Long.toHexString(txFee);

        //return  inputPreString + totalOutString;
        return  totalOutString;
    }


    ////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////


///////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////

    /*
    String HEXTX = "0100000001a772376502aef2738dca0089811044c3689896ac37053f24a9784a13399f764a010000008b4830450221" +
            "00b8a72a38b9630139a597f271c7016501656d717012d14f81ceee47ad7bce8bf702203bc16d1098ed3b81d04e836b4ea6ed5" +
            "791a6b0863c9165529215b7be3616ec1a4141044ff33350fbb662de8f22d488319792b563dbd120b016a1bc55645465ee27edcb7" +
            "e58fbaa4bb7ff1cd2620882a18132a3e51e0b0da6455d0ff08703db5b6234b2ffffffff02e8030000000000001976a91461079f5031" +
            "a5b7e312d9fc5051fd7ce018fabc9288acb01d0000000000001976a9141f02307e6139effb4ec53283bcf6072e4796e10688ace9390b00";
*/

    //TXTest 1
    //String HEXTX = "0100000002284efca1b2e3d9ad7f097a7dc41ae9713c56a066662ada8c29b4d17575eae40a000000008a47304402200cd2a110cc4832f9eff2244c99485e1cee2ebd020aafb80ee92f55afeffb902302200abedcad2f6a9f90b77c332d99f819c7c910ae6aca235eb1c29ae0d9341e2cf54141044ff33350fbb662de8f22d488319792b563dbd120b016a1bc55645465ee27edcb7e58fbaa4bb7ff1cd2620882a18132a3e51e0b0da6455d0ff08703db5b6234b2ffffffffa13e1c1edcc19bde42863fc509c11d24e11c1c9e44d7ed84ee4e7e0e5ce2132c010000008a473044022004bac11b601886c4225560a13448e269366f934d2958820c8c28ada2001545ed02202fdca0613d4f7902f3f7ecbc4b00dd912605b2203fa29498116f14e7240da7a84141044ff33350fbb662de8f22d488319792b563dbd120b016a1bc55645465ee27edcb7e58fbaa4bb7ff1cd2620882a18132a3e51e0b0da6455d0ff08703db5b6234b2ffffffff02e8030000000000001976a914da7b6f4109019577dd70411d4ced6c841c6d21a388ac74d60000000000001976a9141f02307e6139effb4ec53283bcf6072e4796e10688ac5d420b00";
    //TXTest 2
    private String HEXTX = "010000000104310925db4b67e6b4e113730f26b81715a2cd0a783a8b401e75fc9fcd0078c0010000008b483045022100d6dbdad1e46e8e3f6a83451a1c3efb1df106043ebbc4e3dfd76d4d7ac651b2c50220218e2f2413a58495809a3849e71991c7c73e7a353034ac697b4838553c4b8f694141044ff33350fbb662de8f22d488319792b563dbd120b016a1bc55645465ee27edcb7e58fbaa4bb7ff1cd2620882a18132a3e51e0b0da6455d0ff08703db5b6234b2ffffffff02d0070000000000001976a914da7b6f4109019577dd70411d4ced6c841c6d21a388acdccd0000000000001976a9141f02307e6139effb4ec53283bcf6072e4796e10688ac60420b00";

    // String TxBcRes = "";

//////////////////////////////////////////////////////////////////////////////////
//  Faz o BroadCast de uma TX pela API WhatsOnChain
//  Conteudo: Transação em Hexadecimal
//  A operação é feita em BACKGROUND, o resultado será colocado em TxHexDataSent
//  Equanto TxHexDataSent = null a operação não foi concluida ou falhou
///////////////////////////////////////////////////////////////////////////////////

    String TxHexDataSent;
    private String urlAdress = "https://api.whatsonchain.com/v1/bsv/main/tx/raw";
    private String urlAdress02 = "https://mapi.gorillapool.io/mapi/tx";

    //Boolean threadEndBroadcastHexBsvTx = false;

    Thread threadBroadCast = new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                URL url;

                //poolID == 0 WhatsOnChain
                //poolID == 1 Gorilla Pool

                    /*switch (poolID){
                        case 0: url = new URL(urlAdress);
                        break;
                        default: url = new URL(urlAdress02);
                    } */


                if(poolID == 0)
                    url = new URL(urlAdress);
                else
                    url = new URL(urlAdress02);



                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                //conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");

                //Whats On Chain

                //conn.setRequestProperty("Content-Type", "text/plain;charset=UTF-8");
                //    conn.setRequestProperty("Content-Type", "text/plain");
                //    conn.setRequestProperty("Accept","application/json");

                //Content-Type: application/octet-stream



                //Gorilla Pool
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("accept","text/plain");


                conn.setDoOutput(true);
                conn.setDoInput(true);

                    /*
                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("timestamp", 1488873360);
                    jsonParam.put("uname", message.getUser());
                    jsonParam.put("message", message.getMessage());
                    jsonParam.put("latitude", 0D);
                    jsonParam.put("longitude", 0D);

                    Log.i("JSON", jsonParam.toString());
                    */

                DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                //os.writeBytes(URLEncoder.encode(jsonParam.toString(), "UTF-8"));
                //os.writeBytes(jsonParam.toString());

                    /*
                    os.writeBytes("{\"txhex\": \"" +
                            //"hex..." +
                            TxHexBsv +
                            //HEXTX +
                            "\" }");

                    */


                String TXJson;

                //poolID == 0 WhatsOnChain
                //poolID == 1 Gorilla Pool

                    /*
                    switch (poolID){
                        case 0: TXJson = "{\"txhex\": \"" +
                                //"hex..." +
                                TxHexBsv +
                                //HEXTX +
                                "\" }";
                            break;
                        default: TXJson = "{\"rawTx\": \"" +
                                TxHexBsv +
                                "\" }";
                    }
                    */
                if(poolID == 0) {
                    TXJson = "{\"txhex\": \"" +
                            //"hex..." +
                            TxHexBsv +
                            //HEXTX +
                            "\" }";
                }
                else{
                    TXJson = "{\"rawTx\": \"" +
                            TxHexBsv +
                            "\" }";
                }

                    /*

                    String TXJson01 = "{\"txhex\": \"" +
                            //"hex..." +
                            TxHexBsv +
                            //HEXTX +
                            "\" }";


                    String TXJson02 = "{\"rawTx\": \"" +
                            TxHexBsv +
                            "\" }";

                    */


                //os.writeBytes(TXJson01);
                //os.writeBytes(TXJson02);
                os.writeBytes(TXJson);


                ////////////////////////////////////////////////////////////
                //Resultado Verificqdo em uma variável Global
                ////////////////////////////////////////////////////////////

                //Variables.TxHexDataSent = conn.getResponseMessage();
                TxHexDataSent = conn.getResponseMessage();
//                threadEndBroadcastHexBsvTx = true;//necessário
                //Get Message "OK" if successfull
                //Get Message "Bad Request" Oterwise


                //Variables.TxidDataSend = conn.getHeaderField("Content-Type");


                os.flush();
                os.close();

                //TxRec(TxidHex);

                //Log.i("STATUS", String.valueOf(conn.getResponseCode()));
                //Log.i("MSG" , conn.getResponseMessage());

                conn.disconnect();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    });

    String TxHexBsv = "";
    int poolID;

    //public void sendPost(final String TXIDin) {
    public void broadcastHexBsvTx(final String TxHexBsv, int poolID) {

        TxHexDataSent = null;
//        threadEndBroadcastHexBsvTx = false;
        this.TxHexBsv = TxHexBsv;
        this.poolID = poolID;

        threadBroadCast.start();
    }
///////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////


    String TXID = "";

    public void txID (String txHexData)
    {
        String txID = SHA256G.SHA256bytes(
                SHA256G.HashStrToByte2(txHexData)
        );

        //Double HASH of PreImage in BE format
        //String e = SHA256G.SHA256bytes(SHA256G.HashStrToByte2(TxPreimage));
        //e = SHA256G.SHA256bytes(SHA256G.HashStrToByte2(e));

        txID = SHA256G.SHA256bytes(
                SHA256G.HashStrToByte2(txID)
        );

        TXID = SHA256G.LEformat(txID);
    }

    ///////////////////////////////////////////////////////////////////////////////////
//  Dissecação de uma TX em suas diversas partes
//  Cada elemento da TX será colocado em uma celula da string de retorno
///////////////////////////////////////////////////////////////////////////////////
    int version = 0;
    int nOfInputs = 0;
    int nOfOutputs = 0;
    int totalElements = 0;
    String TXVersion = "";
    String[] txHexOutputs = new String[3*1000];
    String[] prvOutHASH = new String[3*1000];
    String[] pvOutIndex = new String[3*1000];
    String[] inSeq = new String[3*1000];
    String[] pubKeySEC = new String[3*1000];
    String[] signDER = new String[3*1000];


    //public String[] txParts2 (String txHexData)
    public String[] txParts (String txHexData)
    {
        String[] txHexParts = new String[1000];
        int txPartIndex = 0;

        txID(txHexData);

        //Version 4 bytes LE
        txHexParts[txPartIndex] = txHexData.substring(0, 8);
        txHexData = txHexData.substring(8);
        txPartIndex = txPartIndex + 1;

        TXVersion = txHexParts[txPartIndex - 1];
        version = Integer.decode("0x"+ SHA256G.LEformat(txHexParts[txPartIndex-1]));

        ////////////////////////////////////////////////////////////////
        //Numero de TX usado como input
        //Usaremos apenas 256 neste momento
        //Revisar no futuro
        ////////////////////////////////////////////////////////////////

        txHexParts[txPartIndex] = txHexData.substring(0, 2);
        txHexData = txHexData.substring(2);
        txPartIndex = txPartIndex + 1;

        nOfInputs = Integer.decode("0x"+ txHexParts[txPartIndex-1]);

        //Tratamento de até 2 tx como input

        // int numberOfTxIn = 0;

        ////////////////////////////////////////////////////////////
        //Extração de Inputs
        ////////////////////////////////////////////////////////////

        for (int i=0, j=0; i < nOfInputs; i++)
        //for (int i=0; i < 1; i++)
        {
            //TXID da transacao anterior em LE
            txHexParts[txPartIndex] = txHexData.substring(0, 64);
            txHexData = txHexData.substring(64);
            txPartIndex = txPartIndex + 1;

            prvOutHASH[j] =  txHexParts[txPartIndex-1];
            //j++;

            //Index do Output da Tx anterior usado nesta operacao
            txHexParts[txPartIndex] = txHexData.substring(0, 8);
            txHexData = txHexData.substring(8);
            txPartIndex = txPartIndex + 1;

            pvOutIndex[j] =  txHexParts[txPartIndex-1];
            //j++;

            //Tamanho da assinatura + pub key da operacao
            txHexParts[txPartIndex] = txHexData.substring(0, 2);
            txHexData = txHexData.substring(2);
            txPartIndex = txPartIndex + 1;


            //True Token
            if(txHexParts[txPartIndex-1].compareTo("00") == 0)
            {
                signDER[j] = "";
                pubKeySEC[j] = "";

            }
            else

            //https://wiki.bitcoinsv.io/index.php/Opcodes_used_in_Bitcoin_Script
            //"fc" não entra aqui, pois não é necessário para OP_RETURN < 256 bytes
            if(     txHexParts[txPartIndex-1].compareTo("fd") == 0 ||
                    txHexParts[txPartIndex-1].compareTo("fe") == 0 ||
                    txHexParts[txPartIndex-1].compareTo("fe") == 0
            )
            {
                //Tamanho da assinatrua da operacao SCRIPT
                txHexParts[txPartIndex] = txHexData.substring(0, 4);
                txHexData = txHexData.substring(4);
                txPartIndex = txPartIndex + 1;

                int sigNBytes = Integer.decode("0x" +
                        SHA256G.LEformat(txHexParts[txPartIndex - 1])) * 2;

                //Assinatrua DER + 41 da operacao
                txHexParts[txPartIndex] = txHexData.substring(0, sigNBytes);
                txHexData = txHexData.substring(sigNBytes);
                txPartIndex = txPartIndex + 1;

                signDER[j] = "";
                pubKeySEC[j] = "";

            }
            else {

                //Tamanho da assinatrua DER + 41 da operacao
                txHexParts[txPartIndex] = txHexData.substring(0, 2);
                txHexData = txHexData.substring(2);
                txPartIndex = txPartIndex + 1;

                //signDER[j] = txHexParts[txPartIndex-1];

                int sigNBytes = Integer.decode("0x" + txHexParts[txPartIndex - 1]) * 2;

                //Assinatrua DER + 41 da operacao
                txHexParts[txPartIndex] = txHexData.substring(0, sigNBytes);
                txHexData = txHexData.substring(sigNBytes);
                txPartIndex = txPartIndex + 1;

                //signDER[j] = signDER[j] + txHexParts[txPartIndex-1];

                //Scrip < 50 nao tem Chave Publica

                signDER[j] = txHexParts[txPartIndex - 1];

                /*
                //DEBUG para P2PK

                Variables.DEBUG = Integer.decode("0x" + txHexParts[txPartIndex - 3]).toString() +
                " = " + txHexParts[txPartIndex - 2].length()/2 + " + " + txHexParts[txPartIndex - 1].length()/2;

                if(Variables.DEBUG != null)
                    return txHexParts;

                */


                //Neste caso sera P2PK
                //if(Integer.decode("0x" + txHexParts[txPartIndex - 3]) < 0x50)
                if(Integer.decode("0x" + txHexParts[txPartIndex - 3]) ==
                        //Integer.decode("0x" + txHexParts[txPartIndex - 2] + Integer.decode("0x" + txHexParts[txPartIndex - 1])))
                        txHexParts[txPartIndex - 2].length()/2 + txHexParts[txPartIndex - 1].length()/2)
                {
                    pubKeySEC[j] = "";
                }
                else

                {

                    //Tamanho da chave publica SEC da operacao
                    txHexParts[txPartIndex] = txHexData.substring(0, 2);
                    txHexData = txHexData.substring(2);
                    txPartIndex = txPartIndex + 1;

                    pubKeySEC[j] = txHexParts[txPartIndex - 1];

                    int pkNBytes = Integer.decode("0x" + txHexParts[txPartIndex - 1]) * 2;

                    //Chave Publica SEC da operacao
                    txHexParts[txPartIndex] = txHexData.substring(0, pkNBytes);
                    txHexData = txHexData.substring(pkNBytes);
                    txPartIndex = txPartIndex + 1;

                    pubKeySEC[j] = pubKeySEC[j] + txHexParts[txPartIndex - 1];
                }
            }

            //Sequencia do Input da Operacao
            txHexParts[txPartIndex] = txHexData.substring(0, 8);
            txHexData = txHexData.substring(8);
            txPartIndex = txPartIndex + 1;

            inSeq[j] =  txHexParts[txPartIndex-1];
            j++;
        }

        ////////////////////////////////////////////////////////////////
        //Numero Outputs
        //Usaremos apenas 256 neste momento
        //Revisar no futuro
        ////////////////////////////////////////////////////////////////

        txHexParts[txPartIndex] = txHexData.substring(0, 2);
        txHexData = txHexData.substring(2);
        txPartIndex = txPartIndex + 1;

        nOfOutputs = Integer.decode("0x"+ txHexParts[txPartIndex-1]);

        ////////////////////////////////////////////////////////////
        //Extração de Outputs
        ////////////////////////////////////////////////////////////

        for (int i=0, j=0; i < nOfOutputs; i++)
        //for (int i=0; i < 1; i++)
        {
            //Valor do Pagamento
            txHexParts[txPartIndex] = txHexData.substring(0, 16);
            txHexData = txHexData.substring(16);
            txPartIndex = txPartIndex + 1;

            txHexOutputs[j] =  txHexParts[txPartIndex-1];
            j++;

            //Tamanho do Script
            txHexParts[txPartIndex] = txHexData.substring(0, 2);
            txHexData = txHexData.substring(2);
            txPartIndex = txPartIndex + 1;

            txHexOutputs[j] =  txHexParts[txPartIndex-1];
            j++;

            //https://wiki.bitcoinsv.io/index.php/Opcodes_used_in_Bitcoin_Script

            //"fc" não entra aqui, pois não é necessário para OP_RETURN < 256 bytes
            if(     txHexParts[txPartIndex-1].compareTo("fd") == 0 ||
                    txHexParts[txPartIndex-1].compareTo("fe") == 0 ||
                    txHexParts[txPartIndex-1].compareTo("fe") == 0
            )
            {
                //Tamanho do SCRIPT

                /////////////////////////////////////////////////////////////
                // Precisa ser corrigido para OP_RETURNS de "fd" e "fe"
                //////////////////////////////////////////////////////////////
                txHexParts[txPartIndex] = txHexData.substring(0, 4);
                txHexData = txHexData.substring(4);
                txPartIndex = txPartIndex + 1;

                txHexOutputs[j] = txHexParts[txPartIndex - 1];
                j++;

                int sigNBytes = Integer.decode("0x" +
                        SHA256G.LEformat(txHexParts[txPartIndex - 1])) * 2;

                //Script
                //txHexParts[txPartIndex] = txHexData.substring(0, sigNBytes);
                txHexParts[txPartIndex - 1] = txHexParts[txPartIndex - 1] + txHexData.substring(0, sigNBytes);
                txHexData = txHexData.substring(sigNBytes);
                //txPartIndex = txPartIndex + 1;

                //txHexOutputs[j] = txHexParts[txPartIndex - 1];
                txHexOutputs[j-1] = txHexParts[txPartIndex - 1];
                //j++;

            }
            else {


                int sigNBytes = Integer.decode("0x" + txHexParts[txPartIndex - 1]) * 2;

                //Script
                txHexParts[txPartIndex] = txHexData.substring(0, sigNBytes);
                txHexData = txHexData.substring(sigNBytes);
                txPartIndex = txPartIndex + 1;

                txHexOutputs[j] = txHexParts[txPartIndex - 1];
                j++;
            }
        }

        //LockTime
        txHexParts[txPartIndex] = txHexData.substring(0, 8);
        //txHexData = txHexData.substring(2);

        totalElements = txPartIndex + 1;
        return txHexParts;
    }


///////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////





///////////////////////////////////////////////////////////////////////////////////
//  Constroi a PreImagem de uma TX
//      Input: TxHexData
//      Apenas ForkID type 41
//      para fazer a PREIMAGEM de uma nova TX Indicar elementos padrões de uma TX
//
//      Single TXID as input
//
//  Produz um Array de Preimages
//      Uma para cada Output sendo utilizado
///////////////////////////////////////////////////////////////////////////////////

    int nOfPreImages41 = 0;

//    String preImage41parts = "";

    //String[] OutDEP = new String[2];

    public String [] txPreImager41 (String txHexDataIn)
    //public String [] txPreImager41x (String txHexDataIn)
    {

        /*
        //DEBUG
        if(txHexDataIn != null) {
            String [] TxPreimageOutFail = new String[1];
            TxPreimageOutFail[0] = "Error 1";
            return TxPreimageOutFail;
        }
        */

        //Vamos precisar das partes da TX atual
        //Ajustar TX para PK2K
        String [] txPartsCurrent = txParts(txHexDataIn);

        int totalElementsCurrent = this.totalElements;


        /*
        //DEBUG
        if(txHexDataIn != null) {
            String [] TxPreimageOutFail = new String[1];
            TxPreimageOutFail[0] = "Error 1";
            return TxPreimageOutFail;
        }
        */



        //Vamos precisar dos outputs da TX atual
        //O elemento muda quando uma nova busca é realizada.
        //String [] txOutPutsCurrent = this.txHexOutputs;


        String [] txOutPutsCurrent = new String[this.nOfOutputs * 3];;
        for(int i = 0; i < this.nOfOutputs * 3; i++ )
            txOutPutsCurrent[i] = this.txHexOutputs[i];


        int nOfInputsCurrent = this.nOfInputs;
        nOfPreImages41 = nOfInputsCurrent;
        int nOfOutputsCurrent = this.nOfOutputs;

        //O elemento muda quando uma nova busca é realizada.
        //String [] prvOutHASHCurrent = this.prvOutHASH;
        //String [] pvOutIndexCurrent = this.pvOutIndex;


        String [] prvOutHASHCurrent = new String[this.nOfInputs];
        for(int i = 0; i<this.nOfInputs; i++ )
            prvOutHASHCurrent[i] = this.prvOutHASH[i];


        String [] pvOutIndexCurrent = new String[this.nOfInputs];
        for(int i = 0; i<this.nOfInputs; i++ )
            pvOutIndexCurrent[i] = this.pvOutIndex[i];


        //String [] inSeqCurrent = this.inSeq;
        String [] inSeqCurrent = new String[this.nOfInputs];
        for(int i = 0; i<this.nOfInputs; i++ )
            inSeqCurrent[i] = this.inSeq[i];


        /*
        //DEBUG
        if(txHexDataIn != null) {
            String [] TxPreimageOutFail = new String[1];
            TxPreimageOutFail[0] = "Error 1";
            return TxPreimageOutFail;
        }

        */
        //https://www.reference.cash/protocol/blockchain/transaction/transaction-signing

        ////////////////////////////////////////////////////////////////////////////////
        //Step 1: Transaction version (4-byte little endian)
        ////////////////////////////////////////////////////////////////////////////////

        //String TXVersion = "01000000";
        String TXVersion = txPartsCurrent[0];


        //       preImage41parts = preImage41parts + TXVersion + "\n";

        ////////////////////////////////////////////////////////////////////////////////
        //Step 2: Previous transaction outputs identifiers (32-byte hash)
        ////////////////////////////////////////////////////////////////////////////////

        //previous outputs hash	32 bytes	hash(BE)
        //A double SHA-256 hash of the set of previous outputs spent by the inputs of the transaction.
        //See Previous Outputs for the hash preimage format.
        //If hash type is “ANYONECANPAY” then this is all 0x00 bytes.

        String prvOutHASH = "";


        //DEBUG
        //if(txHexDataIn != null) {
        //    String [] TxPreimageOutFail = new String[1];
        //    TxPreimageOutFail[0] = "Error 1";
        //    return TxPreimageOutFail;
        //}



        for(int i=0; i<nOfInputsCurrent; i++)
            //prvOutHASH = prvOutHASH + this.prvOutHASH[i] + this.pvOutIndex[i];
            prvOutHASH = prvOutHASH + prvOutHASHCurrent[i] + pvOutIndexCurrent[i];;

//        prvOutHASH = PREVTXID + pvOutIndex;

//        preImage41parts = preImage41parts + prvOutHASH + "\n";


        //DEBUG
        /*
        if(txHexDataIn != null) {
            String [] TxPreimageOutFail = new String[1];
            TxPreimageOutFail[0] = "Error 1";
            return TxPreimageOutFail;
        }
        */

        prvOutHASH = SHA256G.SHA256bytes(SHA256G.HashStrToByte2(prvOutHASH));
        //prvOut Double HASH
        prvOutHASH = SHA256G.SHA256bytes(SHA256G.HashStrToByte2(prvOutHASH));

//        preImage41parts = preImage41parts + prvOutHASH + "\n";

        ////////////////////////////////////////////////////////////////////////////////
        //Step 3: Transaction input sequence numbers (32-byte hash)
        ////////////////////////////////////////////////////////////////////////////////

        //sequence numbers hash	32 bytes	hash(BE)
        // A double SHA-256 hash of the set of sequence numbers of the inputs of the transaction.
        // See Sequence Numbers for the hash preimage format.
        //
        //If hash type is “ANYONECANPAY”, “SINGLE”, or “NONE” then this is all 0x00 bytes.

        //String inSeq = "ffffffff";
        String inSeq = "";

        for(int i=0; i<nOfInputsCurrent; i++)
            //inSeq = inSeq + this.prvOutHASH[i] + this.pvOutIndex[i];
            inSeq = inSeq + inSeqCurrent[i];

//        preImage41parts = preImage41parts + inSeq + "\n";


        String inSeqDHash;
        inSeqDHash = SHA256G.SHA256bytes(SHA256G.HashStrToByte2(inSeq));
        inSeqDHash = SHA256G.SHA256bytes(SHA256G.HashStrToByte2(inSeqDHash));



        ////////////////////////////////////////////////////////////////////////////////
        //Step 4: The identifier of the output being spent (32-byte hash + 4-byte little endian)
        ////////////////////////////////////////////////////////////////////////////////

        //previous output hash	32 bytes	hash(LE)	The transaction ID of the previous output being spent.
        //previous output index	4 bytes	unsigned integer(LE)	The index of the output to be spent.


        //Uma PrevOutID para cada Output being spent.

        //String prevOutID = PREVTXID + pvOutIndex;
        String[] prevOutID = new String[nOfInputsCurrent];

        for(int i=0; i<nOfInputsCurrent; i++)
        {
            //prevOutID[i] =  this.prvOutHASH[i] + this.pvOutIndex[i];
            prevOutID[i] = prvOutHASHCurrent[i] + pvOutIndexCurrent[i];

//            preImage41parts = preImage41parts + prevOutID[i] + "\n";
        }


        /*
        //DEBUG
        if(txHexDataIn != null) {
            String [] TxPreimageOutFail = new String[1];
            TxPreimageOutFail[0] = "Error 1";
            return TxPreimageOutFail;
        }
        */

/////////////////////////////////////////////////////////////
/// Inicio da busca por cada TX anterior
/////////////////////////////////////////////////////////////

        String[] lockingScript = new String[nOfInputsCurrent];
        String[] prevOutSatValue = new String[nOfInputsCurrent];
        String[] inSeqNumber = new String[nOfInputsCurrent];

        //https://developers.whatsonchain.com/#introduction
        //Up to 3 requests/sec. Need more? Please let us know in the WoC devs telegram channel
        // or send us an email at support@taal.com
        dalyWhatsOnChain = 350;

        //dalyWhatsOnChain = 500;


        //DEBUG
        //if(txHexDataIn != null) {
        //    String [] TxPreimageOutFail = new String[1];
        //    TxPreimageOutFail[0] = "Error 1";
        //    return TxPreimageOutFail;
        //}

        /////////////////////////////////////////////////
        //Array de threads para este procedimento
        // Funcionou bem para mostrar que as threads precisam ser renovadas
        // a cada uso
        /////////////////////////////////////////////////
        //setThreadArray(nOfInputsCurrent);


        /*
        //DEBUG
        if(txHexDataIn != null) {
            String [] TxPreimageOutFail = new String[1];
            TxPreimageOutFail[0] = "Error 1";
            return TxPreimageOutFail;
        }
        */

        for(int i = 0; i < nOfInputsCurrent; i++) {
        //for(int i = 1; i < nOfInputsCurrent; i++) {

            //txPartsCurrent
            String TxidTosearch =  SHA256G.LEformat(prvOutHASHCurrent[i]);

//            preImage41parts = preImage41parts + TxidTosearch + "\n"  + pvOutIndexCurrent[i] + "\n";

            //Busca da TX

            Boolean searchFail = true;

            String TXToSeach = "";

            //Revisar e tratar este While

            //while (searchFail) {
            //{

            //DEBUG
            //if(txHexDataIn != null) {
            //    String [] TxPreimageOutFail = new String[1];
            //    TxPreimageOutFail[0] = "Error 1";
            //    return TxPreimageOutFail;
            //}

            //DEBUG
            /*
            if(i == 1) {
                String [] TxPreimageOutFail = new String[1];
                TxPreimageOutFail[0] = "Error 1";
                return TxPreimageOutFail;
            }
            /*

            TxHexData = null;
//            threadEndReadHexBsvTx = true;
//            timerWoc01 = new TimeCheckURL();

            //threadArrayIndex = i;

            ////////////////////////////////////////////////////////////////////////
            //O timer apresenta sempre um problema entre uma chamada e outra
            // Não encontrei a maneira de lidar com o timer entre uma chamada e outra
            // sem usar a força bruta e quebrar o while
            // ele não permite consulta ao estado da thread
            // e quebra ao tentar realizar
            ////////////////////////////////////////////////////////////////////////
            /*
            timer = new Timer();
            wocTimer = 0;
            timer.schedule(new TimerWOC(), dalyWhatsOnChain, 5000);
            while(wocTimer < 1)
            {
                TxHexData = null;
            }
            timer.cancel();
            timer.purge();

            */





            renewThread();
            readHexBsvTx(TxidTosearch);
            //readHexBsvTxWOC(TxidTosearch);

            //Aguarda até que o dado seja lido da WhatsOnChain
            //Aqui temos que respeitar o
            //while (TxHexData == null)
            //int inparPar = 0;

            //DEBUG
            /*
            if(i == 1) {
                String [] TxPreimageOutFail = new String[1];
                TxPreimageOutFail[0] = "Error 1";
                return TxPreimageOutFail;
            }
            */

//            String waitHashing = "";
//            waitHashing = SHA256G.SHA256STR("ABC");
            /*
                if(Variables.LastTXID != null)
                    waitHashing = SHA256G.SHA256STR(Variables.LastTXID);
                else
                    waitHashing = SHA256G.SHA256STR("ABC");
                */

//            long count = 0;

//            int flagFail = 0;

            //Tentativa 1: Array de Threads
            //Tentativa 2: Time elapse Android


            //Funcionou uma vez
            while(threadReadHexBsvTx.isAlive()){
            //while(threadArray[i].isAlive()){
            //while(timerWoc01.thread00.isAlive()){

            //while(!threadEndReadHexBsvTx) {
                TXToSeach = "";

                /*
                if(waitHashing != null)
                    waitHashing = SHA256G.SHA256STR(waitHashing);
                else
                    waitHashing = SHA256G.SHA256STR("ABC");

                if(waitHashing != null)
                {
                    count ++;
                }

                if(count == 40000) {

                    flagFail = 1;
                    break;
                }

                */
                //  break;
            }

            /*
            //DEBUG
            if(i == 1) {
                String [] TxPreimageOutFail = new String[1];
                TxPreimageOutFail[0] = "Error 1";
                return TxPreimageOutFail;
            }
            */

            //DEBUG


//            timer.cancel();
//            timer.purge();

            //DEBUG
            //if(txHexDataIn != null) {
            //    String [] TxPreimageOutFail = new String[1];
            //    TxPreimageOutFail[0] = "Error 1";
            //    return TxPreimageOutFail;
            //}

            //monitorar esta variável entre 2 transações;
            //escrever o estado no inicio e no final da execução

            //if(TxHexData != null && i==1) {
            //    String[] TxPreimageOutFail = new String[1];
            //    TxPreimageOutFail[0] = "Error: " + TxHexData;
            //    return TxPreimageOutFail;
            //}

            if(TxHexData == null) {
                String[] TxPreimageOutFail = new String[1];
                TxPreimageOutFail[0] = "Error: Time out reading TX HEX DATA";
                return TxPreimageOutFail;
            }
            /*

            if(flagFail == 1) {

                if(TxHexData == null) {
                    String[] TxPreimageOutFail = new String[1];
                    TxPreimageOutFail[0] = "Error: Time out reading TX HEX DATA";
                    return TxPreimageOutFail;
                }
            }
            */

            TXToSeach = TxHexData;

            /*
            //DEBUG
            if(i == 1) {
                String [] TxPreimageOutFail = new String[1];
                TxPreimageOutFail[0] = "Error 1";
                return TxPreimageOutFail;
            }
            */

            //Debug Error:
            //TXToSeach = TxHexData.substring(0,50);

            if(TXToSeach.length() < 100) {
                searchFail = true;
                //Variables.ErroPreImagem = 1; //não conseguiu ler o input
                String [] TxPreimageOutFail = new String[1];
                //Não conseguiu ler toda a string
                TxPreimageOutFail[0] = "Error: TX HEX DATA incomplete";
                return TxPreimageOutFail;
            }
            else {
                searchFail = false;
                //Variables.ErroPreImagem = 0; //não houve erro
            }

            /*
            //DEBUG
            if(i == 1) {
                String [] TxPreimageOutFail = new String[1];
                TxPreimageOutFail[0] = "Error 1";
                return TxPreimageOutFail;
            }
            */


            //preImage41parts = preImage41parts + "\"\\n\"Aqui\"\\n\"" + TXToSeach + "\n";
//            preImage41parts = preImage41parts + TXToSeach + "\n";



            //String [] TxPartsHere = txParts2(TXToSeach);

            //Esta funcao teve que ser corrigida para P2PK




            String [] TxPartsHere = txParts(TXToSeach); //esta tendo problemas com P2PK

            /*
            if(txHexDataIn != null  && i == 2) {
                String [] TxPreimageOutFail = new String[1];
                TxPreimageOutFail[0] = "Error 1";
                return TxPreimageOutFail;
            }
            */




            //txPartsCurrent = txParts2(TXToSeach);


            /*
            //DEBUG P2PK
            if(i == 1) {
                String [] TxPreimageOutFail = new String[1];
                TxPreimageOutFail[0] = "Error 1";
                return TxPreimageOutFail;
            }
            */

            ///////////////////////////////////////////////
            //Output to Search
            ///////////////////////////////////////////////

            int indexOut = Integer.decode( "0x" + SHA256G.LEformat(pvOutIndexCurrent[i]));

//            preImage41parts = preImage41parts + indexOut + "\n" + pvOutIndexCurrent[i] + "\n";


            ////////////////////////////////////////////////////////////////////////////////
            //Step 5: The locking script of the output being spent (serialized as scripts inside CTxOuts)
            ////////////////////////////////////////////////////////////////////////////////

            //modified locking script length	variable	variable length integer	The number of bytes for modified_locking_script.
            //modified locking script	modified_locking_script_length bytes	bytes(BE)
            // The subset of the locking script used for signing. See Modified Locking Script


            //String lockingScript = "19" + "76a914" + "1f02307e6139effb4ec53283bcf6072e4796e106" + "88ac";

            //Tamanho do Script + Script = "19" + "76a9141f02307e6139effb4ec53283bcf6072e4796e10688ac"

            //this.txHexOutputs[indexOut + 1] + this.txHexOutputs[indexOut + 2]
            indexOut = indexOut * 3;


            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            //Existe neste ponto um prblema de delay que precisa ser investigado
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////


            lockingScript[i] = this.txHexOutputs[indexOut + 1] + this.txHexOutputs[indexOut + 2];



            ////////////////////////////////////////////////////////////////////////////////
            //Step 6:value of the output spent by this input (8-byte little endian)
            ////////////////////////////////////////////////////////////////////////////////

            //previous output value	8 bytes	unsigned integer(LE)	The value of the transaction output being spent.

            //String prevOutSatValue = "1027000000000000"; //Total in the previous output

            prevOutSatValue[i] = this.txHexOutputs[indexOut];

            ////////////////////////////////////////////////////////////////////////////////
            //Step 7: The sequence number of the transaction input (8-byte little endian)
            ////////////////////////////////////////////////////////////////////////////////

            //input sequence number	8 bytes	unsigned integer(LE)	The sequence number of the input this signature is for.

            //String inSeqNumber = "ffffffff";

            inSeqNumber[i] = inSeqCurrent[i];

            //DEBUG

            /*
            if(txHexDataIn != null  && i == 2) {
                String [] TxPreimageOutFail = new String[1];
                TxPreimageOutFail[0] = "Error 1";
                return TxPreimageOutFail;
            }

            */




        }

        /*
        //DEBUG

        if(txHexDataIn != null) {
            String [] TxPreimageOutFail = new String[1];
            TxPreimageOutFail[0] = "Error 1";
            return TxPreimageOutFail;
        }

        */


/////////////////////////////////////////////////////////////
/// Fim da busca por cada TX anterior
/////////////////////////////////////////////////////////////

        ////////////////////////////////////////////////////////////////////////////////
        //Step 8: The created transaction outputs - hashOutputs (32-byte hash)
        ////////////////////////////////////////////////////////////////////////////////

        //transaction outputs hash	32 bytes	hash(BE)	A double SHA-256 hash of the outputs of the transaction.
        // See Transaction Outputs for the hash preimage format.

        //value	8 bytes	unsigned integer(LE)	The number of satoshis to be transferred.
        //locking script length	variable	variable length integer	The size of the locking script in bytes.
        //locking script	variable	bytes(BE)	The contents of the locking script.

        //String TxOutputs = "e803000000000000" + "19" + "76a914" + "61079f5031a5b7e312d9fc5051fd7ce018fabc92" + "88ac"
        //        + "b01d000000000000" + "19" + "76a914" + "1f02307e6139effb4ec53283bcf6072e4796e106" + "88ac";


        //String TxOutputs = "e8030000000000001976a9141ba498cf3f715f733d49c096caa58e6406fb04de88ac60220000000000001976a9141f02307e6139effb4ec53283bcf6072e4796e10688ac";

        String TxOutputs = "";

        for(int i = 0; i < nOfOutputsCurrent * 3; i++)
            TxOutputs = TxOutputs + txOutPutsCurrent[i];


        //String TxOutputs = "88000000000000001976a914462ffd2e35d27f6a0fea913aa68bb3dc0769584488acb9000000000000001976a91460a67e2d905de43df35ebe57b1e28b260be9325888ac88000000000000001976a914a7c03c57edffb6971aac9e0dcc71bc0c978fbb8488ac88000000000000001976a914b42422a29f5312292b238f41108400b107856bf588acf0ac0000000000001976a914125c5a63d962967117ad5924c8355a1dccb10f1d88ac";

        String TxOutputsDHASH = SHA256G.SHA256bytes(SHA256G.HashStrToByte2(TxOutputs));
        TxOutputsDHASH = SHA256G.SHA256bytes(SHA256G.HashStrToByte2(TxOutputsDHASH));

        ////////////////////////////////////////////////////////////////////////////////
        //Step 9: nLocktime of the transaction (4-byte little endian)
        ////////////////////////////////////////////////////////////////////////////////

        //transaction lock time	4 bytes	unsigned integer(LE)	The lock time of the transaction.

        //String nLockTime = "dd390b00";
        String nLockTime = "";

        nLockTime = txPartsCurrent[totalElementsCurrent - 1];

        ////////////////////////////////////////////////////////////////////////////////
        //Step 10: sighash type of the signature (4-byte little endian)
        ////////////////////////////////////////////////////////////////////////////////

        //hash type	4 bytes	Hash Type(LE)	Flags indicating the rules for how this signature was generated.

        String  sighashType = "41000000";

        ////////////////////////////////////////////////////////////////////////////////
        //Transaction preimage
        ////////////////////////////////////////////////////////////////////////////////

        //String  TxPreimage = TXVersion + prvOutHASH + inSeqDHash + prevOutID + lockingScript
        //        + prevOutSatValue + inSeqNumber + TxOutputsDHASH + nLockTime + sighashType;

        txID(txHexDataIn);

        String [] TxPreimageOut = new String[nOfInputsCurrent];


        for(int i = 0; i < nOfInputsCurrent; i++ ) {
            TxPreimageOut[i] =
                    TXVersion + prvOutHASH + inSeqDHash
                            + prevOutID[i] + lockingScript[i] + prevOutSatValue[i] + inSeqNumber[i]
                            + TxOutputsDHASH
                            + nLockTime + sighashType;

        }
        return TxPreimageOut;


        //return txPartsCurrent;
    }

///////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////

///////////////////////////////////////////////////////////////////////////////////
//  Verificação de Assinatura da TX com o Conjuto
//      PreImagem
//      Public Key
//      Assinatura
///////////////////////////////////////////////////////////////////////////////////

    public String txSigVerify (String preImage, String pubKeySEC, String sigDER41)
    {
        Keygen pubKey = new Keygen();
        BigInteger [] sigDERrev = pubKey.sigDERrev(sigDER41.substring(0,sigDER41.length()-2));
        BigInteger [] pubKeyREV;

        if(pubKeySEC.substring(2,4).compareTo("04")==0)
            //Chave não comprimida
            pubKeyREV = pubKey.pubKeyUncompSECRev(pubKeySEC.substring(2));
        else
            //Chave comprimida
            pubKeyREV = pubKey.pubKeyCompSECRev(pubKeySEC.substring(2));

        //ECSA signature verifier

        EcdsaSecretus ECDSA = new EcdsaSecretus();


        //ECDSA VERIFY
        //e = SHA256G.SHA256bytes(SHA256G.HashStrToByte2(e));
        //The Double Hash is a very importart component that can be considered to be kept outside of the ECDSA ALGO
        //BigInteger [] sigTST2 = ECDSA.ECDSAVerifyBSV(SHA256G.HashStrToByte2(TxPreimage), pubKeySECUncREV, sigDERrev);

        //Double HASH of PreImage in BE format
        String e = SHA256G.SHA256bytes(SHA256G.HashStrToByte2(preImage));
        e = SHA256G.SHA256bytes(SHA256G.HashStrToByte2(e));

        //Nova função ECDSAVerify with e as input
        //BigInteger [] sigTST2 = ECDSA.ECDSAVerifyBSV(e, pubKeyREV, sigDERrev);

        //O Resultado do BigInteger é contaminado se houverem outras operações:
        //String sigTSTStr = sigTST2[0] +"\n" + sigTST2[1];

        //Nova função ECDSAVerify with e as input
        int sigTST3 = ECDSA.ECDSAVerifyBSV2(e, pubKeyREV, sigDERrev);

        if(sigTST3 == 1)
            return "OK";
        else
            return "Invalid";
    }

///////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////

///////////////////////////////////////////////////////////////////////////////////
//  Assinatura Digital ECDSA da TX com o Conjuto
//      PreImagem
//      Private Key
//      H4
///////////////////////////////////////////////////////////////////////////////////

    public String signEcdsaDER41 (String preImage, String pvtKey, String H4)
    {
        EcdsaSecretus ECDSA = new EcdsaSecretus();


        //ECDSA VERIFY
        //e = SHA256G.SHA256bytes(SHA256G.HashStrToByte2(e));
        //The Double Hash is a very importart component that can be considered to be kept outside of the ECDSA ALGO
        //BigInteger [] sigTST2 = ECDSA.ECDSAVerifyBSV(SHA256G.HashStrToByte2(TxPreimage), pubKeySECUncREV, sigDERrev);

        //Double HASH of PreImage in BE format
        String e = SHA256G.SHA256bytes(SHA256G.HashStrToByte2(preImage));
        e = SHA256G.SHA256bytes(SHA256G.HashStrToByte2(e));

        //Nova função ECDSAVerify with e as input

        boolean repeatFlag = true;
        String signDER = "";
        Variables.ntries = 0;

        while (repeatFlag) {

            BigInteger[] signature = ECDSA.ECDSABSV(e, pvtKey, H4);
            repeatFlag = false;
            Variables.ntries ++;

            BigInteger[] sigECD = new BigInteger[2];
            sigECD[0] = signature[0];
            sigECD[1] = signature[1];

            //////////////////////////////////////////////////////////
            // Escolhe o S mais curto
            // Para evitar a maleabilidade e fazer o algo ser um pouco mais rápido
            // https://www.derpturkey.com/inherent-malleability-of-ecdsa-signatures/
            //////////////////////////////////////////////////////////

            Variables.shortS = "normal s";

            BigInteger sInv = eccVar.n_order.subtract(sigECD[1]); // sInv = n - s

            if(sInv.compareTo(sigECD[1]) == -1) {
                sigECD[1] = sInv;
                Variables.shortS = "inverted s";
            }

            //////////////////////////////////////////////////////////
            //////////////////////////////////////////////////////////



            Keygen pubKey = new Keygen();
            //Assinatura no formato DER + FORKID
            //String signDER = pubKey.sigDER(sigECD) + "41";
            signDER = pubKey.sigDER(sigECD) + "41";

            /////////////////////////////////////////////////////////////////////////////////////
            // S size treatment, R also
            /////////////////////////////////////////////////////////////////////////////////////
            //DER starts with 30
            String rSize = signDER.substring(6, 8);
            int rSizeInt = 2 * Integer.decode("0x" + rSize);
            String sSize = signDER.substring(8 + rSizeInt + 2, 8 + rSizeInt + 2 + 2);
            //Variables.sSize = rSize + " " + sSize;
            int sSizeInt = 2 * Integer.decode("0x" + sSize);

            if (sSizeInt > 64 || rSizeInt > 64) {
                H4 = SHA256G.SHA256bytes(SHA256G.HashStrToByte2(H4));
                repeatFlag = true;
            }
        }

        /////////////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////////////////////

        return signDER;
    }

}