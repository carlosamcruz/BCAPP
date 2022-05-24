package com.nibblelinx.BCAPP;


import java.io.*;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
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

    Boolean threadEndReadHexBsvTx = false;
    public void readHexBsvTx(String TXID){

        //timer.cancel();
        //timer.purge();
        timer = new Timer();
        urlBaseTXID = "https://api.whatsonchain.com/v1/bsv/main/tx/" + TXID +  "/hex";
        //timer.schedule(new TimeCheckURL(), 0, 5000);
        threadEndReadHexBsvTx = false;
        timer.schedule(new TimeCheckURL(), dalyWhatsOnChain, 5000);
    }

    //private String result = "";

    class TimeCheckURL extends TimerTask
    {
        @RequiresApi(api = Build.VERSION_CODES.M)
        public void run()
        {
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

    //////////////////////////////////////////////////////////////////////////////////
    // Adress Unspent Inputs
    //////////////////////////////////////////////////////////////////////////////////
    String unsPentInputs;

    public void readBsvAddsUnspent(String BSVADD){

        //timer.cancel();
        //timer.purge();
        timer = new Timer();
        urlBaseTXID = "https://api.whatsonchain.com/v1/bsv/main/address/" + BSVADD +  "/unspent";

        /*
        if(Variables.STREAMTT %2 ==0)
            urlBaseTXID = "https://api.whatsonchain.com/v1/bsv/main/address/" + BSVADD +  "/unspent";
        else
            urlBaseTXID = "https://api.whatsonchain.com/v1/bsv/main/address/" + BSVADD +  "/unspent";

        */

        threadEndReadBsvAddsUnspent = false;


        //timer.schedule(new TimeCheckURL(), 0, 5000);
        timer.schedule(new TimeCheckURL2(), dalyWhatsOnChain, 5000);
    }

    //private String result = "";

    Boolean threadEndReadBsvAddsUnspent = false;

    class TimeCheckURL2 extends TimerTask
    {
        @RequiresApi(api = Build.VERSION_CODES.M)
        public void run()
        {
            new Thread(new Runnable() {
                @Override
                public void run() {

                    //Looper.prepare();

                    ////////////////////////////////////////////////////////////
                    //Resultado Armazenado em uma variável Global
                    ////////////////////////////////////////////////////////////
                    unsPentInputs = null;
                    //Variables.TxHexData = new JsonTaskTXID().execute(urlBaseTXID2);

                    unsPentInputs = new JsonTaskTXID().execute(urlBaseTXID);
                    //Variables.threadM = true;
                    threadEndReadBsvAddsUnspent = true;

                    /*
                    if(Variables.STREAMTT %2 ==0)
                        unsPentInputs = new JsonTaskTXID2().execute(urlBaseTXID);
                    else
                        unsPentInputs = new JsonTaskTXID().execute(urlBaseTXID);
                    */

                    //unsPentInputs = "[{\"height\":740197,\"tx_pos\":1,\"tx_hash\":\"f31e32ccda4780dd863c79fb671b5560b417ff100e6de988ec178fc78acd2b65\",\"value\":50602}]";
                    //result = new JsonTaskTXID().execute(urlBaseTXID2);;
                    //Looper.loop();
                    //    timer.cancel();
                    //    timer.purge();

                }
            }).start();

        }
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

        String inputSeq = "ffffffff";

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

    public String inputPosString (int nInputs, String SECsizeOut, String PUBKEYSEC, String[] derSecPAD)
    {
        String Version = "01000000";
        //String nOfInputs = "01";
        String nOfInputs = Integer.toHexString(nInputs);

        while (nOfInputs.length() < 2)
            nOfInputs = "0" + nOfInputs;

        String inputSeq = "ffffffff";

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

                if((Out1Script.length() / 2) >= 0x01 && (Out1Script.length() / 2) <= 0xff) {
                    //out1size = out1size;
                    byteSizeout1size = "";

                    out1size = byteSizeout1size + SHA256G.LEformat(out1size);
                }
                else if ((Out1Script.length() / 2) >= 0x0100 && (Out1Script.length() / 2) <= 0xffff) {

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
                    ivalue = ivalue - totalSpent -
                            ((
                                    inputPreString.length() +
                                            totalOutString.length() +
                                            ("0000000000000000"+"1976a914" + PayWallet160 + "88ac" + "00000000").length()
                            )/2) / 2; //0.5 Satoshis por byte
                }

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


        }
        String lockTime = "00000000";


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
    Boolean threadEndBroadcastHexBsvTx = false;

    //public void sendPost(final String TXIDin) {
    public void broadcastHexBsvTx(final String TxHexBsv) {

        TxHexDataSent = null;
        threadEndBroadcastHexBsvTx = false;

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(urlAdress);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    //conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                    conn.setRequestProperty("Content-Type", "text/plain;charset=UTF-8");
                    conn.setRequestProperty("Accept","application/json");
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
                    os.writeBytes("{\"txhex\": \"" +
                            //"hex..." +
                            TxHexBsv +
                            //HEXTX +
                            "\" }");

                    ////////////////////////////////////////////////////////////
                    //Resultado Verificqdo em uma variável Global
                    ////////////////////////////////////////////////////////////

                    //Variables.TxHexDataSent = conn.getResponseMessage();
                    TxHexDataSent = conn.getResponseMessage();
                    threadEndBroadcastHexBsvTx = true;//necessário
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

        thread.start();
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


            //https://wiki.bitcoinsv.io/index.php/Opcodes_used_in_Bitcoin_Script
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

                signDER[j] = txHexParts[txPartIndex - 1];

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
        //Extração de Inputs
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
            if(     txHexParts[txPartIndex-1].compareTo("fd") == 0 ||
                    txHexParts[txPartIndex-1].compareTo("fe") == 0 ||
                    txHexParts[txPartIndex-1].compareTo("fe") == 0
            )
            {
                //Tamanho do SCRIPT
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
    {
        //Vamos precisar das partes da TX atual
        String [] txPartsCurrent = txParts(txHexDataIn);

        int totalElementsCurrent = this.totalElements;

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


        for(int i=0; i<nOfInputsCurrent; i++)
            //prvOutHASH = prvOutHASH + this.prvOutHASH[i] + this.pvOutIndex[i];
            prvOutHASH = prvOutHASH + prvOutHASHCurrent[i] + pvOutIndexCurrent[i];;

//        prvOutHASH = PREVTXID + pvOutIndex;

//        preImage41parts = preImage41parts + prvOutHASH + "\n";


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

        for(int i = 0; i < nOfInputsCurrent; i++) {

            //txPartsCurrent
            String TxidTosearch =  SHA256G.LEformat(prvOutHASHCurrent[i]);

//            preImage41parts = preImage41parts + TxidTosearch + "\n"  + pvOutIndexCurrent[i] + "\n";

            //Busca da TX

            Boolean searchFail = true;

            String TXToSeach = "";

            while (searchFail) {

                TxHexData = null;
                readHexBsvTx(TxidTosearch);

                //Aguarda até que o dado seja lido da WhatsOnChain
                //Aqui temos que respeitar o
                //while (TxHexData == null)
                while(!threadEndReadHexBsvTx)
                    TXToSeach = "";

                TXToSeach = TxHexData;

                if(TXToSeach.length() < 100 || TXToSeach == null)
                    searchFail = true;
                else
                    searchFail = false;

                timer.cancel();
                timer.purge();
            }

            //preImage41parts = preImage41parts + "\"\\n\"Aqui\"\\n\"" + TXToSeach + "\n";
//            preImage41parts = preImage41parts + TXToSeach + "\n";

            //String [] TxPartsHere = txParts2(TXToSeach);
            String [] TxPartsHere = txParts(TXToSeach);
            //txPartsCurrent = txParts2(TXToSeach);


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

        }

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

        for(int i = 0; i < nOfInputsCurrent; i++ )
            TxPreimageOut[i] =
                    TXVersion + prvOutHASH + inSeqDHash
                            + prevOutID[i] + lockingScript[i] + prevOutSatValue[i] + inSeqNumber[i]
                            + TxOutputsDHASH
                            + nLockTime + sighashType;
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