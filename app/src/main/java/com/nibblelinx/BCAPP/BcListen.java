package com.nibblelinx.BCAPP;

import android.app.Activity;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;


//https://stackoverflow.com/questions/33229869/get-json-data-from-url-using-android

public class BcListen extends AppCompatActivity {

    ListView lista;
    Boolean AddSend = true;

    String dataOR = "";
    String lastDataOR = "";

    /////////////////////////////////////
    // BUSCA NA Whatsonchain
    /////////////////////////////////////
    String jsonStr = "";
    String myURL = "";
    String searchStr = "\"tx_hash\":\"";
    String urlBaseAddress = "https://api.whatsonchain.com/v1/bsv/main/address/";

    private String myBSVaddress;
    private String BSVAdddressSend;
    private String BSVAdddressReceive;

    int lastLength = 0;
    int lastLengthSend = 0;
    int lastLengthReceive = 0;
    int numberOfTxid = 0;

    Timer timer = new Timer();

    static Boolean TimerRun = true;

    Boolean netRunFirst = true;

    String[] txIDVector = new String[1000];

    /////////////////////////////////////
    /////////////////////////////////////

    //https://stackoverflow.com/questions/10379134/finish-an-activity-from-another-activity
    public static Activity fa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_txidlist);

        //O verficador de pausa deve ser modificado em OnResume e OnCreate de cada Activity
        Variables.activityPause = false;
        //O contador de pausa deve estar presente em OnResume e OnCreate de cada Activity
        //Atenção especial para o uso de MAXSPECIALPAUSETIME
        Variables.timeCounter = Variables.MAXPAUSETIME;
        //O contador de Interação com Usuário deve também estar presente em OnResume e em OnCreate de cada activity
        //O contador deve ser resetado em OnResume, OnCreate, onUserInteraction de cada Activity
        Variables.userInteractionAct = Variables.MAXNOINTERACTIONTIME;

        //Coloque aqui o endereco Bitcoin que deseja monitorar:
        BSVAdddressSend = getIntent().getExtras().getString("NFTIndex");

        BSVAdddressReceive = BSVAdddressSend;
        myBSVaddress = BSVAdddressSend;
        lastDataOR = dataOR;

        myURL = urlBaseAddress + myBSVaddress + "/history";

        for(int i = 0; i < 1000; i++)
            txIDVector[i] = "";

        // A cada 5 segundos o sistema checa se tem algo novo no endereco informado
        timer.schedule(new TimeCheckURL(), 0, 5000);


    }

    //public BcDataSend sendData;

    private void sendDataToBc (String dataToSend)
    {
        BcDataSend sendData = new BcDataSend();
        String result = sendData.dataSend(dataToSend);

        //String result = dataSend(dataToSend);

        //String result = Variables.MainPaymail;
        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
    }

    int sendCont = 0;

    //int runTurn = 0;

    private void addNewData (String newData)
    {
        dataOR = newData;
        //Toast.makeText(this, newData, Toast.LENGTH_LONG).show();

        if(lastDataOR.compareTo(dataOR) != 0) {
            Toast.makeText(this, newData, Toast.LENGTH_LONG).show();
            lastDataOR = dataOR;

            /////////////////////////////////////////
            /////////////////////////////////////////
            if(sendCont == 0)
            {
                //sendDataToBc("Teste 1 2 + SendCont: " + sendCont + 1);
                sendCont++;
            }
            /////////////////////////////////////////
            /////////////////////////////////////////
        }

    }

//////////////////////////////////////////////////////////////////////////////////////////////////
// Monitoramento de chegada de dados
//////////////////////////////////////////////////////////////////////////////////////////////////

    //NESTED CLASS: nao precisa colocar fora
    //Inner Class: https://www.tutorialspoint.com/java/java_innerclasses.htm
    //https://stackoverflow.com/questions/12908412/print-hello-world-every-x-seconds
    private String resultSTR;

    class TimeCheckURL extends TimerTask
    {
        public void run()
        {
            if(TimerRun) {
                //if(netOn) {

                resultSTR = null;
                new JsonTask().onPreExecute();
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        resultSTR = new JsonTask().execute(myURL);

                        //Para a execução na thread pricipal
                        runOnUiThread(new Runnable() {
                            public void run() {
                                // do onPostExecute stuff
                                new JsonTask().onPostExecute(resultSTR);
                            }
                        });
                    }
                }).start();
            }
        }
    }

    //https://stackoverflow.com/questions/4238921/detect-whether-there-is-an-internet-connection-available-on-android
    //https://stackoverflow.com/questions/37232927/app-crashes-when-no-internet-connection-is-available
    //https://stackoverflow.com/questions/32547006/connectivitymanager-getnetworkinfoint-deprecated

    //CheckNetwork network = new CheckNetwork(getApplicationContext());

    // Network Check
    //https://gist.github.com/PasanBhanu/730a32a9eeb180ec2950c172d54bb06a
    //https://gist.github.com/Abhinav1217/0ff6b39e70fa38379d61e85e09b49fe7

    //https://developer.android.com/reference/android/net/ConnectivityManager
    //https://developer.android.com/reference/android/net/ConnectivityManager#getActiveNetwork()
    //public void onNetwork()
    //public boolean onNetwork()

    //NESTED CLASS: nao precisa colocar fora
    //Inner Class: https://www.tutorialspoint.com/java/java_innerclasses.htm
    // https://stackoverflow.com/questions/33229869/get-json-data-from-url-using-android
    private class JsonTask //extends AsyncTask<String, String, String>
    {
        protected void onPreExecute()
        {

            //super.onPreExecute();
        }

        //protected String doInBackground(String... params)
        protected String execute(String... params)
        {
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

                    ///////////////////////////////////////////////////////////////////
                    //NAO FUNCIONA COLOCAR O CONTEXTO DA CLASSE EXTERNA
                    ///////////////////////////////////////////////////////////////////
                    //Toast.makeText(mContext, "> "+ line, Toast.LENGTH_LONG).show();
                    //Toast.makeText(MActJSON, "> "+ line, Toast.LENGTH_LONG).show();
                }

                return buffer.toString();
            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();

                //return null;
                //urlProccess("ABC");
            }
            catch (IOException e)
            {
                e.printStackTrace();
                //return null;
                //urlProccess("ABC");
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
                    //return null;
                }
                //urlProccess("ABC");
                //return null;
            }
            return null;
        }

        //@Override
        protected void onPostExecute(String result)
        {
            //super.onPostExecute(result);

            ///////////////////////////////////////////////////////////////////
            //VERIFICAR
            ///////////////////////////////////////////////////////////////////

            //AQUI ESTE METODO FUNCIONA
            //((EditText)findViewById(R.id.etvJsonItem)).setText(result);

            //Resposta precisa ser uma variavel global

            //jsonStr = result;

            //CHAMA A FUNCAO DE PROCESSAMENTO
            //https://stackoverflow.com/questions/2920525/how-to-check-a-string-against-null-in-java
            //if(result.equals(null)) urlProccess(jsonStr, false);
            if(result == null) urlProccess(jsonStr, false);
            else urlProccess(result, true);

            //lastLength = jsonStr.length();
        }
    }

    private Boolean firstRun = true;

    //Executa depois que o JSON eh extraido
    //public String urlProccess(String urlContent, Boolean Flag)
    //private String urlProccess(String urlContent, Boolean Flag)
    private void urlProccess(String urlContent, Boolean Flag)
    {
        jsonStr = urlContent;
        int firstIndiceOf;
        int lastIndiceOf;

        if(myBSVaddress.compareTo(BSVAdddressSend) == 0) lastLength = lastLengthSend;
        else lastLength = lastLengthReceive;

        String lastTXIDSEQ = "";

        //Registrar no DB este tamanho;
        if(Flag)
        {
            //Toast.makeText(TxidList.this, "Length: " + lastLength + "\n" + jsonStr, Toast.LENGTH_SHORT).show();
            if (lastLength != jsonStr.length())
            //if (lastJsonStr.compareTo(jsonStr) != 0)
            {
                //jsonStrLast = jsonStr;
                String Txid;

                //https://www.geeksforgeeks.org/searching-for-character-and-substring-in-a-string/
                firstIndiceOf = jsonStr.indexOf(searchStr);
                lastIndiceOf = jsonStr.lastIndexOf(searchStr);

                if((firstIndiceOf == -1) || (lastIndiceOf == -1))
                {
                    //Toast.makeText(TxidList.this, "NO DATA IN: " + myBSVaddress, Toast.LENGTH_SHORT).show();
                    if(AddSend) {
                        myBSVaddress = BSVAdddressReceive;
                        AddSend = false;
                    }
                    else
                    {
                        myBSVaddress = BSVAdddressSend;
                        AddSend = true;
                    }
                    myURL = urlBaseAddress + myBSVaddress + "/history";
                    return;
                    //return jsonStr;
                }

                int i = 1;
                //position = "Posicao " + i + ": " + firstIndiceOf + "\n";

                //https://beginnersbook.com/2013/12/java-string-substring-method-example/
                //TXID tem sempre 64 characteres 256 bits
                Txid = jsonStr.substring(firstIndiceOf + searchStr.length(), firstIndiceOf + searchStr.length() + 64);

                /*
                if(firstRun)
                {
                    txIDVector[i-1] = Txid;
                }
                else {

                    for (int j = 0; j < 1000; j++) {

                        if(txIDVector[j].compareTo(Txid) == 0)
                            break;
                        else if (txIDVector[j].compareTo("") == 0){
                            txIDVector[j] = Txid;
                            break;
                        }
                    }
                }
                */

                //Registro e Organizacao de TXIDs
                for (int j = 0; j < 1000; j++) {

                    if(txIDVector[j].compareTo(Txid) == 0)
                        break;
                    else if (txIDVector[j].compareTo("") == 0){
                        txIDVector[j] = Txid;
                        break;
                    }
                }


                while (firstIndiceOf < lastIndiceOf) {
                    i++;
                    firstIndiceOf = jsonStr.indexOf(searchStr, firstIndiceOf + 1);
                    //position += "Posicao " + i + ": " + firstIndiceOf + "\n";

                    Txid = jsonStr.substring(firstIndiceOf + searchStr.length(), firstIndiceOf + searchStr.length() + 64);

                    //txIDVector[i-1] = Txid;

                    for (int j = 0; j < 1000; j++) {

                        if(txIDVector[j].compareTo(Txid) == 0)
                            break;
                        else if (txIDVector[j].compareTo("") == 0){
                            txIDVector[j] = Txid;
                            break;
                        }
                    }

                    //Iniciar, processar o ultimo TXID;
                    //Depois disso registrar todos
                    //Comparar o ultimo processado;
                    //registar todos os TXIDs
                    //registrar os ultimos TXIDs processados;

                    lastTXIDSEQ = Txid;
                }
                numberOfTxid = i;

                //Toast.makeText(BcListen.this, "Last TXID: " + Txid, Toast.LENGTH_SHORT).show();

                firstRun = false;
                //lastDataOR = dataOR;
                //dataRead(Txid);

                Toast.makeText(BcListen.this, "Last TXID: " + txIDVector[numberOfTxid-1], Toast.LENGTH_SHORT).show();

                dataRead(txIDVector[numberOfTxid-1]);

                //Toast.makeText(BcListen.this, "Data OR: " + dataOR, Toast.LENGTH_SHORT).show();



                //  lista.setAdapter(banco.ReadContacts(BSVAdddressSend, BSVAdddressReceive));

            }

            if(myBSVaddress.compareTo(BSVAdddressSend) == 0) lastLengthSend = jsonStr.length();
            else lastLengthReceive = jsonStr.length();


            netRunFirst = false;

            Toast.makeText(BcListen.this, "Connected", Toast.LENGTH_SHORT).show();
        }
        else
        {
            if(netRunFirst) {
                //   lista.setAdapter(banco.ReadContacts(BSVAdddressSend, BSVAdddressReceive));
                netRunFirst = false;
            }
            //Toast.makeText(TxidList.this, myNet, Toast.LENGTH_SHORT).show();
            Toast.makeText(BcListen.this, "Bad or no Conection", Toast.LENGTH_SHORT).show();
        }


        if(AddSend) {
            myBSVaddress = BSVAdddressReceive;
            AddSend = false;
        }
        else
        {
            myBSVaddress = BSVAdddressSend;
            AddSend = true;
        }
        myURL = urlBaseAddress + myBSVaddress + "/history";

        //return jsonStr;
        return;
    }



    String TXID;
    String urlBaseTXID2 = "";
    boolean url2 = false;
    private String result;
    String jsonStrTXID = "";


    private void dataRead(String TXID)
    {
        url2 = true;
        urlBaseTXID2 = "https://api.whatsonchain.com/v1/bsv/main/tx/" + TXID + "/out/0/hex";

        result = null;

        new Thread(new Runnable() {
            @Override
            public void run() {

                //Looper.prepare();
                result = new JsonTask().execute(urlBaseTXID2);;
                //Looper.loop();

                //Para a execução na thread pricipal
                runOnUiThread(new Runnable() {
                    public void run() {
                        // do onPostExecute stuff
                        //new JsonTask().onPostExecute(result);


                        if(result == null) //urlProccessTXID(jsonStrTXID, false);
                            urlProccessTXID(result, false);
                        else
                            urlProccessTXID(result, true);
                    }
                });
            }
        }).start();
    }


    public void urlProccessTXID(final String urlContent, Boolean Flag) //content
    {
        jsonStrTXID = urlContent;

        if (Flag) {

            //https://www.geeksforgeeks.org/searching-for-character-and-substring-in-a-string/
            //new urlProccessTXIDBackGround().execute(urlContent);

            //Esquecer de chamar o preExecute causou um monte de problemas
            //pois ao tentar finalizar o progressBar o sistema quebrava
            //pois esta não havia sido inicializada
            new urlProccessTXIDBackGround().onPreExecute();

            new Thread(new Runnable() {
                @Override
                public void run() {

                    final String resultIn;

                    //Looper.prepare();
                    resultIn = new urlProccessTXIDBackGround().execute(urlContent);;
                    //Looper.loop();

                    //Para a execução na thread pricipal
                    runOnUiThread(new Runnable() {
                        public void run() {
                            // do onPostExecute stuff
                            new urlProccessTXIDBackGround().onPostExecute(resultIn);
                            //new urlProccessTXIDBackGround().onPostExecute(OPRETURNDATA);
                        }
                    });

                }
            }).start();
        }
        else{
            Toast.makeText(this, "Bad or no Conection", Toast.LENGTH_SHORT).show();
        }
    }

    String OPRETURNDATA = "";
    Boolean firstOR = true;

    class urlProccessTXIDBackGround //extends AsyncTask<String, String,String>
    {
        //@Override
        protected void onPreExecute() {
            //super.onPreExecute();
            //exibirProgress(true);
        }

        //@Override
        //protected String doInBackground(String... params) {
        protected String execute(String... params) {

            String jsonStrTXID = params[0];

            int firstIndiceOf;
            int nextIndex;

            //https://www.geeksforgeeks.org/searching-for-character-and-substring-in-a-string/

            //firstIndiceOf = jsonStrTXID.indexOf(searchStrOPRETURN);
            //firstIndiceOf = jsonStrTXID.indexOf("006a4c");

            //firstIndiceOf = jsonStrTXID.indexOf("006a4");
            firstIndiceOf = jsonStrTXID.indexOf("006a");

            //Finaliza o thread de tempo
            //timer.cancel();
            //timer.purge();

            if (firstIndiceOf == -1) {
                OPRETURNDATA = "X";
                // return OPRETURNDATA;
            }
            else if(url2)
            {
                //OPRETURNDATA = jsonStrTXID.substring(14, jsonStrTXID.length());

                if(firstOR) {
                    //NFT = 4e 46 54
                    for (int i = 0; i < jsonStrTXID.length(); i++) {
                        //if (jsonStrTXID.substring(i, i + 6).compareTo("4e4654") == 0) {
                        //if (jsonStrTXID.substring(i, i + 6).compareTo("006a4c") == 0) {


                        if (jsonStrTXID.substring(i, i + 5).compareTo("006a4") == 0) {


                            //OPRETURNDATA = jsonStrTXID.substring(i+2, jsonStrTXID.length());


                            if (jsonStrTXID.substring(i + 5, i + 6).compareTo("c") == 0)
                                OPRETURNDATA = jsonStrTXID.substring(i + 2, jsonStrTXID.length());


                            else if (jsonStrTXID.substring(i + 5, i + 6).compareTo("d") == 0)
                                OPRETURNDATA = jsonStrTXID.substring(i + 4, jsonStrTXID.length());


                            else if (jsonStrTXID.substring(i + 5, i + 6).compareTo("e") == 0)
                                OPRETURNDATA = jsonStrTXID.substring(i + 6, jsonStrTXID.length());
                            else if (jsonStrTXID.substring(i, i + 4).compareTo("006a") == 0)
                                OPRETURNDATA = jsonStrTXID.substring(i + 0, jsonStrTXID.length());


                            break;
                        }
                        else if (jsonStrTXID.substring(i, i + 4).compareTo("006a") == 0) {
                            OPRETURNDATA = jsonStrTXID.substring(i + 0, jsonStrTXID.length());
                            break;

                        }
                    }

                }
                else
                {
                    //NFT = 4e 46 54
                    for (int i = 0; i < jsonStrTXID.length(); i++) {
                        //if (jsonStrTXID.substring(i, i + 6).compareTo("4e4654") == 0){
                        //if (jsonStrTXID.substring(i, i + 6).compareTo("006a4c") == 0){
                        if (jsonStrTXID.substring(i, i + 5).compareTo("006a4") == 0) {


                            //Aqui deve ser feito a cosinderação de OP_RETURNs com tamanhos diferentes;

                            OPRETURNDATA = jsonStrTXID.substring(i + 6 + 2, jsonStrTXID.length());
                            //OPRETURNDATA = jsonStrTXID.substring(i + 6 + 2 + 1, jsonStrTXID.length());


                            if (jsonStrTXID.substring(i + 5, i + 6).compareTo("c") == 0)
                                OPRETURNDATA = jsonStrTXID.substring(i + 6 + 2, jsonStrTXID.length());

                            else if (jsonStrTXID.substring(i + 5, i + 6).compareTo("d") == 0)
                                OPRETURNDATA = jsonStrTXID.substring(i + 6 + 4, jsonStrTXID.length());

                            else if (jsonStrTXID.substring(i + 5, i + 6).compareTo("e") == 0)
                                OPRETURNDATA = jsonStrTXID.substring(i + 6 + 6, jsonStrTXID.length());

                            else if (jsonStrTXID.substring(i, i + 4).compareTo("006a") == 0)
                                OPRETURNDATA = jsonStrTXID.substring(i + 6 + 0, jsonStrTXID.length());

                            break;
                        }
                        else if (jsonStrTXID.substring(i, i + 4).compareTo("006a") == 0) {
                            OPRETURNDATA = jsonStrTXID.substring(i + 6 + 0, jsonStrTXID.length());
                            break;

                        }
                    }
                }
            }
            return OPRETURNDATA;
        }

        //@Override
        protected void onPostExecute(String params) {
            //super.onPostExecute(params);
            //((EditText)findViewById(R.id.ET_TEXTOROR2)).setText(OPRETURNDATA);
            processOPRETURNresult();
        }
    }

    int nORTotal = 1;
    int nORCont = 0;
    String[] OPRETURNPKG = new String[111];

    private void processOPRETURNresult()
    {
        if(firstOR)
        {
            if(OPRETURNDATA.compareTo("X")==0) {

                Toast.makeText(this, "No Good Data to Display!!!", Toast.LENGTH_LONG).show();

                //exibirProgress(false);

                /*
                ((TextView)findViewById(R.id.TV_TEXTROR2)).setText("NO GOOD DATA");
                ((EditText)findViewById(R.id.ET_TEXTOROR3)).setText(

                        "No Good Data to Display!!!"
                );

                */

                return;
            }

            firstOR = false;
            nORTotal = 1;
        }

        if(nORCont + 1 == nORTotal)
        {
            //Toast.makeText(NFTOPReturn.this, "Até Aqui  X2!!", Toast.LENGTH_LONG).show();
            //BarCont = BarCont + BarSize;

            OPRETURNPKG[nORTotal - 1] = OPRETURNDATA;
            OPRETURNDATA = OPRETURNPKG[0];

            for (int i=1; i < nORTotal; i++)
            {
                OPRETURNDATA = OPRETURNDATA.substring(0, OPRETURNDATA.length()-1) + OPRETURNPKG[i];
            }
            //exibirProgress(false);

            new Thread(new Runnable() {
                @Override
                public void run() {

                    final String resultIn;
                    //Looper.prepare();
                    resultIn = new DecBackGround().execute(OPRETURNDATA);
                    //result = new urlProccessTXIDBackGround().execute(jsonStrTXID);;
                    //Looper.loop();

                    //Para a execução na thread pricipal
                    runOnUiThread(new Runnable() {
                        public void run() {
                            // do onPostExecute stuff
                            new DecBackGround().onPostExecute(resultIn);
                        }
                    });
                }
            }).start();
        }
        else
        {
            //BarCont = BarCont + BarSize;
            //OPRETURNPKG[nORCont] = OPRETURNDATA;
            //newSearch();
        }

        //new DecBackGround().execute(OPRETURNDATA);
        //exibirProgress(false);
    }

    //class MinhaTask extends AsyncTask<Location,Void,Location> {
    class DecBackGround //extends AsyncTask<String, String,String>
    {

        //@Override
        protected void onPreExecute() {
            //super.onPreExecute();
            //exibirProgress(true);
        }

        //@Override
        //protected String doInBackground(String... params) {
        protected String execute(String... params) {

            Variables.progressBar = 0;
            //decriptografia(params[0]);

            //PDPUtils.byteToString(Arrays.copyOfRange(text, 65, (text.length - 32)));

            return PDPUtils.byteToString(decriptografia(SHA256G.HashStrToByte2(params[0])));
            //return PDPUtils.byteToString(null);
            //return null;

            //return Variables.LastRawDecriptData;
        }

        //@Override
        protected void onPostExecute(String params) {
            //super.onPostExecute(params);

            if(dec)
            {
               DecResult(params);
            }
            //exibirProgress(false);
        }
    }
    private Boolean dec = true;
    private Boolean decSuccess = false;
    private void DecResult(String result)
    {
        //Toast.makeText(NFTOPReturn.this, Variables.Test1, Toast.LENGTH_LONG).show();
        if(decSuccess) {

            decSuccess = false;
            chooseDataType(result);

        }
        else {
            //Snackbar.make(view, "Dados inválidos" + " NULL", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            //Toast.makeText(NFTOPReturn.this, "Dados inválidos", Toast.LENGTH_LONG).show();
            Toast.makeText(this, "Invalid NFT Data", Toast.LENGTH_LONG).show();
            //for (int 1 = 1000000)
            //sleepOver();
            this.finish();

        }
    }

    String NFTInfo = "";
    String NFTDescription;
    private int NFTType;



    private byte[] decriptografia(byte[] text)
    {
        NFTInfo = "";
        NFTDescription = "";
        decSuccess = true;
        //DecOnly = false;

        NFTType = 0;
        //Retirar NFT do DADO
        return Arrays.copyOfRange(text, 3, text.length);
    }


    private void chooseDataType (String text)
    {
        putText(text);
    }
    private void putText (String text)
    {
        Variables.LastRawDecriptData = text;
        //((TextView)findViewById(R.id.TV_TEXTROR2)).setText("BLOCKCHAIN DATA");
        //((EditText) findViewById(R.id.ET_TEXTOROR2)).setText(text);
        //Toast.makeText(this, text, Toast.LENGTH_LONG).show();
        addNewData (text);

    }

//////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        timer.cancel();
        timer.purge();
        this.finish();
    }

    //https://stackoverflow.com/questions/4783960/call-method-when-home-button-pressed
    @Override
    public void onPause(){
        super.onPause();
        //Para o leitor de internet
        TimerRun = false;
        Variables.activityPause = true;
    }
    //https://stuff.mit.edu/afs/sipb/project/android/docs/training/basics/activity-lifecycle/pausing.html
    @Override
    public void onResume(){
        super.onResume();
        //Variables.activityPause = false;
        //Retorna o leitor de internet
        TimerRun = true;

        //O verficador de pausa deve ser modificado em OnResume e OnCreate de cada Activity
        Variables.activityPause = false;
        //O contador de pausa deve estar presente em OnResume e OnCreate de cada Activity
        //Atenção especial para o uso de MAXSPECIALPAUSETIME
        Variables.timeCounter = Variables.MAXPAUSETIME;
        //O contador de Interação com Usuário deve também estar presente em OnResume e em OnCreate de cada activity
        //O contador deve ser resetado em OnResume, OnCreate, onUserInteraction de cada Activity
        Variables.userInteractionAct = Variables.MAXNOINTERACTIONTIME;
    }

    //Monitora intecação do usuário com a aplicação
    //O contador deve também estar presente em OnResume e em OnCreate de cada activity
    //Se o contador atingir 0 a aplicação encerra
    //https://stackoverflow.com/questions/4208730/how-to-detect-user-inactivity-in-android
    @Override
    public void onUserInteraction(){
        super.onUserInteraction();
        //O contador de Interação com Usuário deve também estar presente em OnResume e em OnCreate de cada activity
        //O contador deve ser resetado em OnResume, OnCreate, onUserInteraction de cada Activity
        Variables.userInteractionAct = Variables.MAXNOINTERACTIONTIME;
    }


}
