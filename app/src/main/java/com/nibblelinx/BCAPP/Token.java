package com.nibblelinx.BCAPP;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
//import android.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;

//import com.google.android.material.button.MaterialButton;


public class Token extends AppCompatActivity {

    //Notes banco;
    String myPassword;
    String myUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_token);
        Button buttonSEND = (Button) findViewById(R.id.buttonSEND);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarTK);
        setSupportActionBar(toolbar);

        //O verficador de pausa deve ser modificado em OnResume e OnCreate de cada Activity
        Variables.activityPause = false;
        //O contador de pausa deve estar presente em OnResume e OnCreate de cada Activity
        //Atenção especial para o uso de MAXSPECIALPAUSETIME
        Variables.timeCounter = Variables.MAXPAUSETIME;
        //O contador de Interação com Usuário deve também estar presente em OnResume e em OnCreate de cada activity
        //O contador deve ser resetado em OnResume, OnCreate, onUserInteraction de cada Activity
        Variables.userInteractionAct = Variables.MAXNOINTERACTIONTIME;

        //EditText PVTKEY = (EditText) findViewById(R.id.ET_LobbyAct_PVTKEY);
        EditText SendTo = (EditText) findViewById(R.id.ET_LobbyAct_SentTo);
        EditText Satoshis = (EditText) findViewById(R.id.ET_LobbyAct_Value);
        EditText Data = (EditText) findViewById(R.id.ET_LobbyAct_Data);


        //String pvtkey = PVTKEY.getText().toString();
        String sendTo = SendTo.getText().toString();
        String sats = Satoshis.getText().toString();
        String data = Data.getText().toString();

        ((TextView) findViewById(R.id.TV_TEXT2bsv)).setText("Balance (Satoshis): " + Variables.SatBalance + " sats");
        ((TextView) findViewById(R.id.TV_TEXT3bsv)).setText(Variables.BSVWallet);


        buttonSEND.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String senha = MyPass

                //sendTX(pvtkey,sendTo, sats, data);
                sendTX();
                ((TextView) findViewById(R.id.TV_TEXT2bsv)).setText("Balance (Satoshis): " + Variables.SatBalance + " sats");
                ((TextView) findViewById(R.id.TV_TEXT3bsv)).setText(Variables.BSVWallet);


            }
        });


    }

    //public void sendTX(String PVTKEY, String Wallet, String Value, String DATA)
    public void sendTX()
    {

        //EditText PVTKEY = (EditText) findViewById(R.id.ET_LobbyAct_PVTKEY);
        EditText SendTo = (EditText) findViewById(R.id.ET_LobbyAct_SentTo);
        EditText Satoshis = (EditText) findViewById(R.id.ET_LobbyAct_Value);
        EditText Data = (EditText) findViewById(R.id.ET_LobbyAct_Data);

        //String pvtkey = PVTKEY.getText().toString();

        String pvtkey = Variables.MainPaymail;

        String sendTo = SendTo.getText().toString();
        //String sendTo = "1B69q3ZY6VsuKwCinvbB5tkKWLjHWfGz1J";
        String sats = Satoshis.getText().toString();
        //String sats = "1000";
        String data = Data.getText().toString();
        //String data = "Teste de mensagem mais longa.";
        //String data = "Teste N";
        //String data = "Teste N TTT t";//bad request



        //////////////////////////////////////////////////////////////////////////////////////////////////
        //Preparação das Chaves
        //////////////////////////////////////////////////////////////////////////////////////////////////
        Keygen pubKey = new Keygen();
        Boolean CompPKey = false;

        String PUBKEY = pubKey.publicKeyHEX(pvtkey);

        String BSV160 = pubKey.bsvWalletRMD160(PUBKEY, CompPKey);
        String BSVADD = pubKey.bsvWalletFull(PUBKEY, CompPKey);


        /////////////////////////////////////////////////////////////////////
        //User Data Input
        /////////////////////////////////////////////////////////////////////

        String [] PayWallets = new String[10];
        String [] PayValues = new String[10];
        String [] OP_RETURNs = new String[10];

        //PayWallets[0] = "1B69q3ZY6VsuKwCinvbB5tkKWLjHWfGz1J"; //MoneyButton
        PayWallets[0] = sendTo; //Carteira para onde esta sendo enviado
        PayWallets[1] = BSVADD;
        //PayValues[0] = "1000";
        PayValues[0] = sats;
        //...at the name of Jesus every knee should bow, of things in heaven, and things in earth, and things under the earth;
        //OP_RETURNs[0] = "2e2e2e617420746865206e616d65206f66204a65737573206576657279206b6e65652073686f756c6420626f772c206f66207468696e677320696e2068656176656e2c20616e64207468696e677320696e2065617274682c20616e64207468696e677320756e646572207468652065617274683b";

        int nOR = 0;
        if(data.length() > 0) {
            OP_RETURNs[0] = StrToHex(data);
            nOR = 1;
        }

        /////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////

        BsvTxCreation txCreate = new BsvTxCreation();

        String newTX = txCreate.txBuilder(pvtkey,2 + nOR, PayWallets,PayValues,OP_RETURNs, nOR);
        String result = "";
        Variables.LastTxHexData = newTX;

        BsvTxOperations bsvTxOp = new BsvTxOperations();
        bsvTxOp.txID(newTX);
        Variables.LastTXID = bsvTxOp.TXID;

        result = txCreate.txBroadCast(newTX);

        //result = txCreate.totalUnspent(BSVADD);

        Toast.makeText(Token.this, "Result: " + result
                , Toast.LENGTH_LONG).show();
    }

    static public String StrToHex(String text)
    {
        char base16[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
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

        return  SHA256G.ByteToStrHex(newTextChar);
    }

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

}