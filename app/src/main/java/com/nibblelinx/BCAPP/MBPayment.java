package com.nibblelinx.BCAPP;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


//////////////////////////////////////////////////////////////////
//OPERACOES COM ARQUIVOS
/////////////////////////////////////////////////////////////////

import java.util.Timer;
import java.util.TimerTask;
/////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////

public class MBPayment extends AppCompatActivity {

    private Timer timerMB;
    String lastTXID = "";
    private int TXType = 0;
///////////////////////////////////////////////////////
//WEB INTERFACE
///////////////////////////////////////////////////////
    Webscrip myWebInterface = new Webscrip(this);
    WebView myWebView;
///////////////////////////////////////////////////////
///////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mbpayment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarMBPay);
        setSupportActionBar(toolbar);

        //O verficador de pausa deve ser modificado em OnResume e OnCreate de cada Activity
        Variables.activityPause = false;
        //O contador de pausa deve estar presente em OnResume e OnCreate de cada Activity
        //Atenção especial para o uso de MAXSPECIALPAUSETIME
        Variables.timeCounter = Variables.MAXPAUSETIME;
        //O contador de Interação com Usuário deve também estar presente em OnResume e em OnCreate de cada activity
        //O contador deve ser resetado em OnResume, OnCreate, onUserInteraction de cada Activity
        Variables.userInteractionAct = Variables.MAXNOINTERACTIONTIME;

///////////////////////////////////////////////////////
//WEB INTERFACE
///////////////////////////////////////////////////////

        myWebView = (WebView) findViewById(R.id.webviewMBPay);

        //Ativando o JAVA SCRIPT

        //WebView myWebView = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        //FROM: https://stackoverflow.com/questions/5899087/android-webview-localstorage
        WebSettings settings = myWebView.getSettings();
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);

        //https://www.w3schools.com/html/html5_webstorage.asp

        myWebView.addJavascriptInterface(myWebInterface, "Android");
        //A WebView definida por ultimo fica na frente
        // independente de quem é chamado primeiro
        //myWebView2.loadUrl("https://www.moneybutton.com");

        //https://stackoverflow.com/questions/7746409/android-webview-launches-browser-when-calling-loadurl
        myWebView.setWebViewClient(new WebViewClient());

        timerMB = new Timer();
        timerMB.schedule(new TimeCheckMB(), 0, 1000);
        lastTXID = Variables.LastTXID;

        //myWebInterface.setTXType(1);

        String DataToChain = getIntent().getExtras().getString("MBDATA");
        DataToChain = Variables.MBDATA;

        //Necessário para saber onde começa nosso dado.
        //Marcador de Inicio de bloco "4e4654" NFT
        myWebInterface.setDataToChain("4e4654" + DataToChain,0);

        /////////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////////////////

        myWebInterface.setPieceMBDATA(getIntent().getExtras().getString("MBDATA"));
        myWebInterface.setMailData(getIntent().getExtras().getString("MailData"));
        myWebInterface.setAmountToAddress(getIntent().getExtras().getString("AmountToAddress"));
        //it.putExtra("AmountToDataAddress", Variables.BSVDustLimit);
        myWebInterface.setAmountToDataAddress(getIntent().getExtras().getString("AmountToDataAddress"));
        //Principal
        //Este é o endereço para os dados serão enviados
        //Deste endereço os dados também serão lidos
        //É importante você ter o controle sobre este endereço
        //Ou seja, que faça parte de uma das carteiras das quais você tem acesso
        myWebInterface.setNftIndex("1McQFu2vM5LnnanWA3su4LmokHZDchqM7u");// Novo Endereço

        ////////////////////////////////////////////////////////////////////////////////////////////////
        //Avoid reloading WEBVIEW upon rotation
        //https://stackoverflow.com/questions/12131025/android-preventing-webview-reload-on-rotate
        ////////////////////////////////////////////////////////////////////////////////////////////////
        if (savedInstanceState == null)
        {
            myWebView.loadUrl("file:///android_asset/www/MBPayment.html");
        }

///////////////////////////////////////////////////////
///////////////////////////////////////////////////////

    }
    class TimeCheckMB extends TimerTask
    {
        public void run()
        {
            MBProccess();
        }
    }

    public void MBProccess() //content
    {
        if(lastTXID.compareTo(Variables.LastTXID) != 0){
            timerMB.cancel();
            timerMB.purge();
            Variables.Payment = true;

            switch (TXType){

                case 2: Variables.operation = 1;
                    break;
                case 3: Variables.operation = 2;
                    break;
                default: Variables.operation = 0;
            }

            this.finish();
        }
    }

    ////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////


    @Override
    public void onBackPressed() {
        super.onBackPressed();
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

    @Override
    protected void onSaveInstanceState(Bundle outState )
    {
        super.onSaveInstanceState(outState);
        myWebView.saveState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
       myWebView.restoreState(savedInstanceState);
    }

}