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
    //private Timer timerMB;
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

        //timerMB = new Timer();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                if(descInput) {

                    if (state) {

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
                        sendTX(newTextChar);
                    }
                }
            }
        });

        Toast.makeText(this, "TEXT DATA!!!", Toast.LENGTH_SHORT).show();
    }


    public void sendTX(byte[] newTextChar)
    {

        String pvtkey = Variables.MainPaymail;
        //String sendTo = SendTo.getText().toString();
        //String sats = Satoshis.getText().toString();
        String data = SHA256G.ByteToStrHex(newTextChar);


        //////////////////////////////////////////////////////////////////////////////////////////////////
        //Preparação das Chaves
        //////////////////////////////////////////////////////////////////////////////////////////////////
        Keygen pubKey = new Keygen();
        Boolean CompPKey = false;

        String PUBKEY = pubKey.publicKeyHEX(pvtkey); //PVTKEY - string Hexadecimal de 64 elementos.
        String BSV160 = pubKey.bsvWalletRMD160(PUBKEY, CompPKey);
        String BSVADD = pubKey.bsvWalletFull(PUBKEY, CompPKey);


        /////////////////////////////////////////////////////////////////////
        //User Data Input
        /////////////////////////////////////////////////////////////////////

        String [] PayWallets = new String[10];
        String [] PayValues = new String[10];
        String [] OP_RETURNs = new String[10];

        //PayWallets[0] = "1B69q3ZY6VsuKwCinvbB5tkKWLjHWfGz1J"; //MoneyButton
        //PayWallets[0] = sendTo; //Carteira para onde esta sendo enviado
        //PayWallets[1] = BSVADD;
        PayWallets[0] = BSVADD;
        PayValues[0] = "0";
        //PayValues[0] = "1000";
        //PayValues[0] = sats;
        //...at the name of Jesus every knee should bow, of things in heaven, and things in earth, and things under the earth;
        //OP_RETURNs[0] = "2e2e2e617420746865206e616d65206f66204a65737573206576657279206b6e65652073686f756c6420626f772c206f66207468696e677320696e2068656176656e2c20616e64207468696e677320696e2065617274682c20616e64207468696e677320756e646572207468652065617274683b";

        int nOR = 0;
        if(data.length() > 0) {
            //OP_RETURNs[0] = StrToHex(data);
            OP_RETURNs[0] = SHA256G.ByteToStrHex(newTextChar);
            //OP_RETURNs[0] = "5465737465204e205454542074" + "5465737465204e205454542074";
            nOR = 1;
        }

        if(nOR == 0) {

            Toast.makeText(NFTText.this, "No Data!!!"
                    , Toast.LENGTH_LONG).show();
            return;
        }

        /////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////

        BsvTxCreation txCreate = new BsvTxCreation();

        String newTX = txCreate.txBuilder(pvtkey,1 + nOR, PayWallets,PayValues,OP_RETURNs, nOR);
        String result = "";
        Variables.LastTxHexData = newTX;

        BsvTxOperations bsvTxOp = new BsvTxOperations();
        bsvTxOp.txID(newTX);
        Variables.LastTXID = bsvTxOp.TXID;

        result = txCreate.txBroadCast(newTX);





        //result = txCreate.totalUnspent(BSVADD);

        //Toast.makeText(NFTText.this, "Result: " + OP_RETURNs[0]
        //Toast.makeText(NFTText.this, "Result: " + result + 1 + nOR
        Toast.makeText(NFTText.this, "Result: " + result
                , Toast.LENGTH_LONG).show();

        //((EditText) findViewById(R.id.ET_TEXTOST)).setText(result);
    }


    ////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////

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

        //timerMB.cancel();
        //timerMB.purge();
    }
    //https://stuff.mit.edu/afs/sipb/project/android/docs/training/basics/activity-lifecycle/pausing.html
    @Override
    public void onResume(){
        super.onResume();

        //checkForPayment();

        //O verficador de pausa deve ser modificado em OnResume e OnCreate de cada Activity
        Variables.activityPause = false;
        //O contador de pausa deve estar presente em OnResume e OnCreate de cada Activity
        //Atenção especial para o uso de MAXSPECIALPAUSETIME
        Variables.timeCounter = Variables.MAXPAUSETIME;
        //O contador de Interação com Usuário deve também estar presente em OnResume e em OnCreate de cada activity
        //O contador deve ser resetado em OnResume, OnCreate, onUserInteraction de cada Activity
        Variables.userInteractionAct = Variables.MAXNOINTERACTIONTIME;

       // timerMB = new Timer();
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
