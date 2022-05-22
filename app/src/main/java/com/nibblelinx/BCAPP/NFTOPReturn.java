package com.nibblelinx.BCAPP;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Network;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

//////////////////////////////////////////////////////////////////
//OPERACOES COM ARQUIVOS
/////////////////////////////////////////////////////////////////
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
/////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////

public class NFTOPReturn extends AppCompatActivity {

    String TXID;
    private int NFTType;
    private Bitmap bmp;
    private VideoView videoView;
    private InputStream inputST;

    ImageView image;// = (ImageView) findViewById(R.id.imageView1);


    String jsonStrTXID = "";
    String OPRETURNDATA = "";
    String[] OPRETURNPKG = new String[111];
    String strTest;

    int nORTotal = 1;
    int nORCont = 0;
    Boolean firstOR = true;


    //String searchStrOPRETURN = "\"asm\":\"0 OP_RETURN ";
    String searchStrOPRETURN = "0 OP_RETURN ";
    //String searchStrOPRETURN = "OP_RETURN ";
    String urlBaseTXID = "https://api.whatsonchain.com/v1/bsv/main/tx/hash/";
    String urlBaseTXID2 = "";
    boolean url2 = false;

    Boolean DecOnly = false;
    Boolean isOnline;

    String NFTInfo = "";
    String NFToken = "";

    String NFTDescription;

    Timer timer = new Timer();
    Timer timerBAR;// = new Timer();
    int timerBarCont = 250;
    int BarCont = 0;
    int BarSize = 25;
    private Boolean dec = true;
    private Boolean decSuccess = false;
    private Boolean EncSuccess = false;
    private Boolean PDP = false;

    static boolean flagFirst;

    //https://stackoverflow.com/questions/10379134/finish-an-activity-from-another-activity
    public static Activity fa;

    private char base16[] = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};

    //private FileLoadDTO fld;
    //private FileUtils fileOP = new FileUtils();
    private String fileName;

    //https://pt.stackoverflow.com/questions/76476/como-utilizo-o-progress-bar
    ProgressBar mProgressBar;
    private Boolean activeProcess = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nftror);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarROR2);
        setSupportActionBar(toolbar);
        fa = this;
        image = (ImageView) findViewById(R.id.imageView1);

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar2);
        mProgressBar.setVisibility(View.INVISIBLE);
        //mProgressBar.setVisibility(View.GONE);

        isOnline = true;
        //mp4File = false;

        NFTType = 0;

        flagFirst = true;

        //O verficador de pausa deve ser modificado em OnResume e OnCreate de cada Activity
        Variables.activityPause = false;
        //O contador de pausa deve estar presente em OnResume e OnCreate de cada Activity
        //Atenção especial para o uso de MAXSPECIALPAUSETIME
        Variables.timeCounter = Variables.MAXPAUSETIME;
        //O contador de Interação com Usuário deve também estar presente em OnResume e em OnCreate de cada activity
        //O contador deve ser resetado em OnResume, OnCreate, onUserInteraction de cada Activity
        Variables.userInteractionAct = Variables.MAXNOINTERACTIONTIME;

        //banco = new Notes(this);

        TXID = getIntent().getExtras().getString("TXID");


        //https://stackoverflow.com/questions/15758856/android-how-to-download-a-file-from-a-webserver
        url2 = true;
        urlBaseTXID2 = "https://api.whatsonchain.com/v1/bsv/main/tx/" + TXID + "/out/0/hex";

        ((TextView)findViewById(R.id.TV_TEXTROR2)).setText("RETRIEVING DATA...");

        videoView = (VideoView) findViewById(R.id.videoView);
        videoView.setBackgroundColor(Color.parseColor("#FFFFFF"));

        ((EditText) findViewById(R.id.ET_TEXTOROR2)).setBackgroundColor(0);//  + NFTDescription);

        DecOnly = true;
        activeProcess = false;

        timer.schedule(new TimeCheckURL(), 0, 5000);
    }

    ////////////////////////////////////////////////////////////////
    //Execulta Decriptografia ou Criptografia
    ////////////////////////////////////////////////////////////////
    static boolean  UTFstr = false;

    private void exibirProgress(boolean exibir)
    {
        //Acionar a barra de progressão faz o APP quebrar depois de um retorno
        //da conexão de internet;
        mProgressBar.setVisibility(exibir ? View.VISIBLE : View.GONE);

        if(exibir)
        {
            timerBAR = new Timer();
            timerBAR.schedule(new TimeCheckBAR(), 0, 250);//mProgressBar.setProgress(0);
        }
        else{
            timerBAR.cancel();
            timerBAR.purge();
        }
    }

    ////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////
    //NESTED CLASS: nao precisa colocar fora
    //Inner Class: https://www.tutorialspoint.com/java/java_innerclasses.htm
    //https://stackoverflow.com/questions/12908412/print-hello-world-every-x-seconds
    ////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////
    class TimeCheckBAR extends TimerTask
    {
        public void run()
        {
            if(BarCont >= 100)
            {
                if(BarSize*nORTotal > 100 && (BarSize*nORTotal-100) <=100)
                    BarCont = 100 - (BarSize*nORTotal-100);
                mProgressBar.setProgress(100);
            }
            else
            {
                mProgressBar.setProgress(BarCont);
            }
        }
    }

    private String result;

    class TimeCheckURL extends TimerTask
    {
        public void run()
        {
            if (onNetwork()) {

                result = null;

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        //Looper.prepare();
                        result = new JsonTaskTXID().execute(urlBaseTXID2);;
                        //Looper.loop();

                        //Para a execução na thread pricipal
                        runOnUiThread(new Runnable() {
                            public void run() {
                                // do onPostExecute stuff
                                new JsonTaskTXID().onPostExecute(result);
                            }
                        });
                    }
                }).start();

            } else {

                result = null;

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        //Looper.prepare();
                        result = new NoConnection().execute(urlBaseTXID2);;
                        //Looper.loop();

                        //Para a execução na thread pricipal
                        runOnUiThread(new Runnable() {
                            public void run() {
                                // do onPostExecute stuff
                                new NoConnection().onPostExecute(result);
                            }
                        });
                    }
                }).start();
            }
        }
    }
    ////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////

    public boolean onNetwork()
    {
        ConnectivityManager connectivityManager = getSystemService(ConnectivityManager.class);
        Network currentNetwork = connectivityManager.getActiveNetwork();

        if(currentNetwork == null)
            return false;
        else
            //https://developer.android.com/reference/android/net/ConnectivityManager#isDefaultNetworkActive()
            //if(connectivityManager.isDefaultNetworkActive()) return true;
            //else return false;
            return true;
    }

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

        //@Override
        protected void onPostExecute(String result)
        {
            //super.onPostExecute(result);
            //exibirProgress(false);

            //CHAMA A FUNCAO DE PROCESSAMENTO
            //https://stackoverflow.com/questions/2920525/how-to-check-a-string-against-null-in-java
            //if(result.equals(null)) urlProccessTXID(jsonStrTXID, false);

            if(result == null) //urlProccessTXID(jsonStrTXID, false);
                urlProccessTXID(result, false);
            else
                urlProccessTXID(result, true);

        }
    }

    ////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////

    private class NoConnection //extends AsyncTask<String, String, String>
    {
        protected void onPreExecute()
        {
            //super.onPreExecute();
        }

        protected String execute(String... params)
        {
            try
            {
                return "try again";
            }
            finally { }
        }

        //@Override
        protected void onPostExecute(String result)
        {
            //super.onPostExecute(result);
            //CHAMA A FUNCAO DE PROCESSAMENTO
            urlProccessTXID(result, false);
        }
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
            Toast.makeText(NFTOPReturn.this, "Bad or no Conection", Toast.LENGTH_SHORT).show();
        }
    }

    class urlProccessTXIDBackGround //extends AsyncTask<String, String,String>
    {
        //@Override
        protected void onPreExecute() {
            //super.onPreExecute();
            exibirProgress(true);
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
            firstIndiceOf = jsonStrTXID.indexOf("006a4");

            //Finaliza o thread de tempo
            timer.cancel();
            timer.purge();

            if (firstIndiceOf == -1) {
                OPRETURNDATA = "X";
               // return OPRETURNDATA;
            }
            else if(url2)
            {
                //OPRETURNDATA = jsonStrTXID.substring(14, jsonStrTXID.length());

                if(firstOR) {
                    //NFT = 4e 46 54
                    for (int i = 0; i < jsonStrTXID.length(); i++)
                        //if (jsonStrTXID.substring(i, i + 6).compareTo("4e4654") == 0) {
                        //if (jsonStrTXID.substring(i, i + 6).compareTo("006a4c") == 0) {
                        if (jsonStrTXID.substring(i, i + 5).compareTo("006a4") == 0) {


                            //OPRETURNDATA = jsonStrTXID.substring(i+2, jsonStrTXID.length());


                            if(jsonStrTXID.substring(i+5, i+6).compareTo("c") == 0)
                               OPRETURNDATA = jsonStrTXID.substring(i+2, jsonStrTXID.length());


                            else if(jsonStrTXID.substring(i+5, i+6).compareTo("d") == 0)
                                OPRETURNDATA = jsonStrTXID.substring(i+4, jsonStrTXID.length());


                            else if(jsonStrTXID.substring(i+5, i+6).compareTo("e") == 0)
                                OPRETURNDATA = jsonStrTXID.substring(i+6, jsonStrTXID.length());


                            break;
                        }
                }
                else
                {
                    //NFT = 4e 46 54
                    for (int i = 0; i < jsonStrTXID.length(); i++)
                        //if (jsonStrTXID.substring(i, i + 6).compareTo("4e4654") == 0){
                        //if (jsonStrTXID.substring(i, i + 6).compareTo("006a4c") == 0){
                        if (jsonStrTXID.substring(i, i + 5).compareTo("006a4") == 0){


                            //Aqui deve ser feito a cosinderação de OP_RETURNs com tamanhos diferentes;

                            OPRETURNDATA = jsonStrTXID.substring(i + 6 + 2, jsonStrTXID.length());
                            //OPRETURNDATA = jsonStrTXID.substring(i + 6 + 2 + 1, jsonStrTXID.length());


                            if(jsonStrTXID.substring(i+5, i+6).compareTo("c") == 0)
                                OPRETURNDATA = jsonStrTXID.substring(i + 6 + 2, jsonStrTXID.length());

                            else if(jsonStrTXID.substring(i+5, i+6).compareTo("d") == 0)
                                OPRETURNDATA = jsonStrTXID.substring(i + 6 + 4, jsonStrTXID.length());

                            else if(jsonStrTXID.substring(i+5, i+6).compareTo("e") == 0)
                                OPRETURNDATA = jsonStrTXID.substring(i + 6 + 6, jsonStrTXID.length());

                            break;
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

    private void processOPRETURNresult()
    {
        if(firstOR)
        {
            if(OPRETURNDATA.compareTo("X")==0) {

                exibirProgress(false);

                ((TextView)findViewById(R.id.TV_TEXTROR2)).setText("NO GOOD DATA");
                ((EditText)findViewById(R.id.ET_TEXTOROR3)).setText(

                        "No Good Data to Display!!!"
                );

                return;
            }

            firstOR = false;
            nORTotal = 1;
        }

        if(nORCont + 1 == nORTotal)
        {
            //Toast.makeText(NFTOPReturn.this, "Até Aqui  X2!!", Toast.LENGTH_LONG).show();
            BarCont = BarCont + BarSize;

            OPRETURNPKG[nORTotal - 1] = OPRETURNDATA;
            OPRETURNDATA = OPRETURNPKG[0];

            for (int i=1; i < nORTotal; i++)
            {
                OPRETURNDATA = OPRETURNDATA.substring(0, OPRETURNDATA.length()-1) + OPRETURNPKG[i];
            }
            exibirProgress(false);

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
            BarCont = BarCont + BarSize;
            OPRETURNPKG[nORCont] = OPRETURNDATA;
            newSearch();
        }

        //new DecBackGround().execute(OPRETURNDATA);
        //exibirProgress(false);
    }

    private void newSearch()
    {
        //https://stackoverflow.com/questions/15758856/android-how-to-download-a-file-from-a-webserver
        url2 = true;

        nORCont ++;
        //urlBaseTXID2 = "https://api.whatsonchain.com/v1/bsv/main/tx/" + TXID + "/out/" + "0" + "/hex";
        urlBaseTXID2 = "https://api.whatsonchain.com/v1/bsv/main/tx/" + TXID + "/out/" + String.valueOf(nORCont) + "/hex";

        ((TextView)findViewById(R.id.TV_TEXTROR2)).setText("RETRIEVING DATA...");

        DecOnly = true;
        activeProcess = false;

        timer = new Timer();
        //timer.schedule(new TimeCheckURL(), 500, 5000);
        timer.schedule(new TimeCheckURL(), 0, 5000);
    }


    //class MinhaTask extends AsyncTask<Location,Void,Location> {
    class DecBackGround //extends AsyncTask<String, String,String>
    {

        //@Override
        protected void onPreExecute() {
            //super.onPreExecute();
            exibirProgress(true);
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
            exibirProgress(false);
        }
    }

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
            Toast.makeText(NFTOPReturn.this, "Invalid NFT Data", Toast.LENGTH_LONG).show();
            //for (int 1 = 1000000)
            //sleepOver();
            this.finish();

        }
    }

    ////////////////////////////////////////////////////////////////
    //Verificar as mudanças para evitar decriptação de textos invalidos
    ////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////

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
        ((TextView)findViewById(R.id.TV_TEXTROR2)).setText("BLOCKCHAIN DATA");
        ((EditText) findViewById(R.id.ET_TEXTOROR2)).setText(text);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if(Variables.MyNFTs)
            getMenuInflater().inflate(R.menu.menu_nftror, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        //https://stackoverflow.com/questions/10379134/finish-an-activity-from-another-activity
        timer.cancel();
        timer.purge();

        TxidList.TimerRun = true;
        this.finish();
    }

    //https://stackoverflow.com/questions/4783960/call-method-when-home-button-pressed
    @Override
    public void onPause(){
        super.onPause();
        Variables.activityPause = true;

    }
    //https://stuff.mit.edu/afs/sipb/project/android/docs/training/basics/activity-lifecycle/pausing.html
    @Override
    public void onResume(){
        super.onResume();
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