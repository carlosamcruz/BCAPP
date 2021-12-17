package com.nibblelinx.BCAPP;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

//////////////////////////////////////////////////////////////////
//OPERACOES COM ARQUIVOS
/////////////////////////////////////////////////////////////////
import java.util.Timer;
import java.util.TimerTask;
/////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////

public class NFTText extends AppCompatActivity {

    Boolean state;
    private boolean UTFstr = false;

    //https://pt.stackoverflow.com/questions/76476/como-utilizo-o-progress-bar
    private ProgressBar mProgressBar;
    private Timer timerMB;
    private Boolean descInput = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_senddata);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarST);
        setSupportActionBar(toolbar);

        //O verficador de pausa deve ser modificado em OnResume e OnCreate de cada Activity
        Variables.activityPause = false;
        //O contador de pausa deve estar presente em OnResume e OnCreate de cada Activity
        //Atenção especial para o uso de MAXSPECIALPAUSETIME
        Variables.timeCounter = Variables.MAXPAUSETIME;
        //O contador de Interação com Usuário deve também estar presente em OnResume e em OnCreate de cada activity
        //O contador deve ser resetado em OnResume, OnCreate, onUserInteraction de cada Activity
        Variables.userInteractionAct = Variables.MAXNOINTERACTIONTIME;

        Variables.Payment = false;

        mProgressBar = (ProgressBar) findViewById(R.id.progressBarST);
        mProgressBar.setVisibility(View.INVISIBLE);

        state = false;

        ((TextView)findViewById(R.id.TV_TEXTST)).setText("TEXT DATA:");

        descInput = true;

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabST);

        timerMB = new Timer();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                if(descInput) {

                    if (timerMB != null) {
                        timerMB.cancel();
                        timerMB.purge();

                        //Tem um problema de quebra na falta de internet
                        //Mas quando foi colocado a linha abaixo o app não parou mais
                        //Toast.makeText(getApplicationContext(), "Connection Out!!!", Toast.LENGTH_SHORT).show();
                        timerMB = new Timer();
                    }

                    if (state) {

                        //Verifica se a ultima operação foi realizada com a internet boa ou ruím;
                        if (MBCon) {
                            MBFLag = true;

                            MBCon = false;
                        } else MBFLag = false;

                        //processo de decriptografia
                        //new DecBackGround2().execute(((EditText) findViewById(R.id.ET_TEXTO)).getText().toString());
                        char base16[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
                        final String text = ((EditText) findViewById(R.id.ET_TEXTOST)).getText().toString();

                        int x = 0;

                        for (int i = 0; i < text.length(); i++) {
                            x = 0;

                            for (int j = 0; j < 16; j++)
                                //if (txtcomp[i] == base16[j]) {
                                if (((char) text.charAt(i)) == base16[j]) {
                                    x = 1;
                                    break;
                                }
                            if (x == 0) break;
                        }

                        //text = "NFT Data: \n" + NFTasset + "\n" + TXIDStart+ "\n" + NFTWallet + "&" + text;

                        if (x == 1) {

                            //new DecBackGround2().execute(SHA256G.HashStrToByte2(text));
                            result = null;
                            new DecBackGround2().onPreExecute();
                            new Thread(new Runnable() {
                                @Override
                                public void run() {

                                    result = new DecBackGround2().execute(SHA256G.HashStrToByte2(text));

                                    //Para a execução na thread pricipal
                                    runOnUiThread(new Runnable() {
                                        public void run() {
                                            // do onPostExecute stuff
                                            new DecBackGround2().onPostExecute(result);
                                        }
                                    });
                                }
                            }).start();
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Dados Inválidos!!!", Toast.LENGTH_SHORT).show();
                        }

                    } else {

                        String text = ((EditText) findViewById(R.id.ET_TEXTOST)).getText().toString();

                        int numberOfNonChar = 0;

                        for (int i = 0; i < text.length(); i++)
                            if (text.charAt(i) > 0xFF) numberOfNonChar++;

                        byte[] newTextChar = new byte[text.length() + numberOfNonChar];

                        for (int i = 0, j = 0; i < text.length(); i++, j++) {
                            if (text.charAt(i) > 0xFF) {
                                newTextChar[j] = (byte) ((text.charAt(i) / 0x100) & 0xFF);
                                j++;
                                newTextChar[j] = (byte) (text.charAt(i) & 0xFF);
                            } else {
                                newTextChar[j] = (byte) (text.charAt(i) & 0xFF);
                            }
                        }
                        assinatura2(newTextChar);
                    }
                }
            }
        });

        Toast.makeText(this, "TEXT DATA!!!", Toast.LENGTH_SHORT).show();
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

    String MBDATA = "";
    String lastTXID = "";

    class TimeCheckMB extends TimerTask
    {
        public void run()
        {
            if (onNetwork()) {
                //new MBOn().execute(MBDATA);

                Thread b = new Thread(new Runnable() {
                    @Override
                    public void run() {

                        MBDATA = new MBOn().execute(MBDATA);

                        //Para a execução na thread pricipal
                        runOnUiThread(new Runnable() {
                            public void run() {
                                // do onPostExecute stuff
                                new MBOn().onPostExecute(MBDATA);
                            }
                        });
                    }
                });
                b.start();

            } else {

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        MBDATA = new MBOff().execute(MBDATA);;

                        //Para a execução na thread pricipal
                        runOnUiThread(new Runnable() {
                            public void run() {
                                // do onPostExecute stuff
                                new MBOff().onPostExecute(MBDATA);
                            }
                        });
                    }
                }).start();

            }
        }
    }

    private class MBOn //extends AsyncTask<String, String, String>
    {
        protected void onPreExecute()
        {
            //super.onPreExecute();
        }

        //protected String doInBackground(String... params)
        protected String execute(String... params)
        {
            return MBDATA;
        }

        //@Override
        protected void onPostExecute(String result)
        {
            //super.onPostExecute(result);
            //CHAMA A FUNCAO DE PROCESSAMENTO
            MBProccess(result, true);
        }
    }

    private class MBOff //extends AsyncTask<String, String, String>
    {
        protected void onPreExecute()
        {
            //super.onPreExecute();
        }

        //protected String doInBackground(String... params)
        protected String execute(String... params)
        {
            return MBDATA;
        }

        //@Override
        protected void onPostExecute(String result)
        {
            //super.onPostExecute(result);
            //CHAMA A FUNCAO DE PROCESSAMENTO
            MBProccess(result, false);
        }
    }

    boolean MBFLag = true;
    boolean MBCon = false;

    public void MBProccess(String urlContent, Boolean Flag) //content
    {
        if(lastTXID.compareTo(Variables.LastTXID) != 0){
            timerMB.cancel();
            timerMB.purge();
            timerMB = new Timer();
            MBCon = false;
        }
        else {
            if (Flag) {

                if(!MBFLag) {

                    MBFLag = true;
                }
                MBCon = true;

                Intent it = new Intent(NFTText.this, MBPayment.class);

                it.putExtra("MBDATA", MBDATA);
                Variables.MBDATA = MBDATA;
                it.putExtra("MailData", "15164");

                //Em AmountToAddress é determinado a taxa do serviço de
                it.putExtra("AmountToAddress", PDPUtils.dataSatoshi(MBDATA));
                it.putExtra("AmountToDataAddress", Variables.BSVDustLimit);

                startActivity(it);

            } else {

                if(MBCon) {
                    MBFLag = false;
                }

                MBCon = false;
                //Toast.makeText(this, "Bad or no Conection: " + myWebInterface.getTest(), Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "Bad or no Conection!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    Timer timerBAR;
    int BarCont = 0;
    int BarSize = 25;

    private void exibirProgress(boolean exibir)
    {
        mProgressBar.setVisibility(exibir ? View.VISIBLE : View.GONE);
        if(exibir)
        {
            BarSize = 25;
            timerBAR = new Timer();
            timerBAR.schedule(new TimeCheckBAR(), 0, 250);//mProgressBar.setProgress(0);
        }
        else{
            //Finaliza o thread de tempo
            timerBAR.cancel();
            timerBAR.purge();
        }
    }

    class TimeCheckBAR extends TimerTask
    {
        public void run()
        {
            if(Variables.BarType)
                mProgressBar.setProgress(Variables.BarSize);
            else{

                if (BarCont >= 100) {
                    BarCont = 0;
                    BarSize--;
                    mProgressBar.setProgress(100);
                } else {
                    mProgressBar.setProgress(BarCont);
                    BarCont = BarCont + BarSize;
                }
                if (BarSize == 1) BarSize = 25;
            }
        }
    }

    byte[] result;

    class DecBackGround2 //extends AsyncTask<byte[], byte[],byte[]>
    {

        //@Override
        protected void onPreExecute() {
            //super.onPreExecute();
            Variables.BarType = true;
            exibirProgress(true);
        }

        //@Override
        //protected byte[] doInBackground(byte[]... params) {
        protected byte[] execute(byte[]... params) {

            return decriptografia2(params[0]);
        }

        //@Override
        protected void onPostExecute(byte[] params) {
          //  super.onPostExecute(params);

            DecResult2(params);
            exibirProgress(false);
        }
    }
    private void DecResult2(byte[] result)
    {
        if(result != null) {

            Variables.BarType = false;
            chooseDataType2(result);
        }else {
            Toast.makeText(this, "Dados inválidos", Toast.LENGTH_LONG).show();
        }
    }


    private byte[] decriptografia2(byte[] text)
    {
        //PROCESSO DE DECRIPITOGRAFIA
        if(text.length > 0) {

            return text;
        }
        else return null;
    }

    private void chooseDataType2 (final byte[] bStr)
    {
        String bStringPiece;
        byte[] b;

        char[] bChar;

        b = bStr;

        bChar = new char[b.length];
        for (int i = 0; i < b.length; i++)
            bChar[i] = (char) (b[i] & 0xFF);
        bStringPiece = String.valueOf(bChar);

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("DADOS DE TEXTO");
        dialog.setMessage("ASCII/UTF16:");

        final String text2 = bStringPiece;

        dialog.setPositiveButton("UTF16", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                UTFstr = true;
                putTextAndSaveFile(bStr, text2);

                state = !state;
            }
        });
        dialog.setNegativeButton("ASCII", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                UTFstr = false;
                //putTextAndSave(text2);
                putTextAndSaveFile(bStr, text2);

                state = !state;
            }
        });
        dialog.create();
        dialog.show();
    }

    private void putTextAndSaveFile(byte[] DATA,  String text)
    {
        ((EditText)findViewById(R.id.ET_TEXTOST)).setText(text);

        ((TextView)findViewById(R.id.TV_TEXTST)).setText("CONTEÚDO:");
    }

    private void assinatura2(final byte[] text) {

        if(text.length>0) {

            //new AssinaturaBackGround2().execute(text);

            result = null;
            new AssinaturaBackGround2().onPreExecute();
            new Thread(new Runnable() {
                @Override
                public void run() {

                    result = new AssinaturaBackGround2().execute(text);

                    //Para a execução na thread pricipal
                    runOnUiThread(new Runnable() {
                        public void run() {
                            // do onPostExecute stuff
                            new AssinaturaBackGround2().onPostExecute(result);
                        }
                    });
                }
            }).start();

        }else
            Toast.makeText(this, "Dados inválidos", Toast.LENGTH_LONG).show();
    }

    class AssinaturaBackGround2 //extends AsyncTask<byte[], byte[],byte[]> {
    {

        //@Override
        protected void onPreExecute() {
            //super.onPreExecute();
            exibirProgress(true);
        }

        //@Override
        //protected byte[] doInBackground(byte[]... params) {
        protected byte[] execute(byte[]... params) {

            return criptografia2(params[0]);
        }

        //@Override
        protected void onPostExecute(byte[] params) {
            //super.onPostExecute(params);

            AssinaturaResult2(params);
            exibirProgress(false);
        }
    }

    void AssinaturaResult2(byte[] text)
    {
        if(text == null)
        {
            Toast.makeText(this, "Dados inválidos", Toast.LENGTH_LONG).show();
        }
        else //Toast.makeText(SendText.this, "Dados ?????", Toast.LENGTH_LONG).show();
        {

            String bStringPiece;
            byte[] b;
            char[] bChar;
            //if(text.length > 250 && (!TextOrFile))
            if(text.length > 250)
            {
                b = new byte[250];
                for(int i = 0; i < 250 ; i++)
                    b[i] = text[i];
            }
            else b = text;

            bChar = new char[b.length];
            for (int i = 0; i < b.length; i++)
                bChar[i] = (char) (b[i] & 0xFF);

            bStringPiece = PDPUtils.strToHEXStr(String.valueOf(bChar));

            ((EditText) findViewById(R.id.ET_TEXTOST)).setText(bStringPiece);
            ((TextView) findViewById(R.id.TV_TEXTST)).setText("HEXADECIMAL DATA:");

        }
        state = !state;
    }

    private byte[] criptografia2(byte[] text){


        int textIni = text.length;

        //PROCESSO DE CRIPTOGRAFIA - Não existe criptorafia nesta aplicação
        if(textIni>0)
        {
            char[] pkg16New = new char[2*text.length];
            char base16[] = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
            for(int j = (text.length-1); j >= 0; j--)
            {
                pkg16New[j*2] = base16[((text[j] & 0xFF)/0x10) & 0x0F];
                pkg16New[(j*2) + 1] = base16[ text[j] & 0x0F ];
            }

            MBDATA = String.valueOf(pkg16New);
            lastTXID = Variables.LastTXID;

            timerMB.schedule(new TimeCheckMB(), 0, 5000);

            ///////////////////////////////////////////////////////
            ///////////////////////////////////////////////////////
            return text;
        }
        else
            return null;
    }

    private void checkForPayment()
    {
        if (Variables.Payment) {
            //Toast.makeText(this, "NFT data recorded @ BSV Chain!!!", Toast.LENGTH_LONG).show();
            Variables.Payment = false;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_senddata, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //int pvtKeyShow = item.getItemId();
        //Keygen keyGen = new Keygen();

        //noinspection SimplifiableIfStatement
        //if (id == R.id.action_settingsST) {
        return super.onOptionsItemSelected(item);
    }
    //https://stackoverflow.com/questions/4783960/call-method-when-home-button-pressed
    @Override
    public void onPause(){
        super.onPause();
        Variables.activityPause = true;

        timerMB.cancel();
        timerMB.purge();
    }
    //https://stuff.mit.edu/afs/sipb/project/android/docs/training/basics/activity-lifecycle/pausing.html
    @Override
    public void onResume(){
        super.onResume();

        checkForPayment();

        //O verficador de pausa deve ser modificado em OnResume e OnCreate de cada Activity
        Variables.activityPause = false;
        //O contador de pausa deve estar presente em OnResume e OnCreate de cada Activity
        //Atenção especial para o uso de MAXSPECIALPAUSETIME
        Variables.timeCounter = Variables.MAXPAUSETIME;
        //O contador de Interação com Usuário deve também estar presente em OnResume e em OnCreate de cada activity
        //O contador deve ser resetado em OnResume, OnCreate, onUserInteraction de cada Activity
        Variables.userInteractionAct = Variables.MAXNOINTERACTIONTIME;

        timerMB = new Timer();
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
