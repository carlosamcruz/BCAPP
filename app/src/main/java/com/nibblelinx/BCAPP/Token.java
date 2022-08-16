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

        if(Variables.TokenType == 1)
        {
            ((TextView) findViewById(R.id.ET_LobbyAct_SentTo)).setHint("Owner Address:");
            ((TextView) findViewById(R.id.ET_LobbyAct_Value)).setHint("TOR Token Value (Satoshis):");
            ((TextView) findViewById(R.id.ET_LobbyAct_Data)).setHint("Token Content:");
            ((TextView) findViewById(R.id.buttonSEND)).setText("CREATE");
        }
        if(Variables.TokenType == 2)
        {
            ((TextView) findViewById(R.id.ET_LobbyAct_SentTo)).setHint("Owner Address:");
            ((TextView) findViewById(R.id.ET_LobbyAct_Value)).setHint("ODR Token Value (Satoshis):");
            ((TextView) findViewById(R.id.ET_LobbyAct_Data)).setHint("Token Content:");
            ((TextView) findViewById(R.id.buttonSEND)).setText("CREATE");
        }

        if(Variables.TokenType == 3)
        {
            ((TextView) findViewById(R.id.ET_LobbyAct_SentTo)).setHint("Receiver Address:");
            ((TextView) findViewById(R.id.ET_LobbyAct_Value)).setText("Total Value");
            ((TextView) findViewById(R.id.ET_LobbyAct_Data)).setHint("Script Hash:");
            ((TextView) findViewById(R.id.buttonSEND)).setText("MELT");
        }

        if(Variables.TokenType == 4)
        {
            ((TextView) findViewById(R.id.ET_LobbyAct_SentTo)).setHint("Send to:");
            //((TextView) findViewById(R.id.ET_LobbyAct_Value)).setHint("Number of Tokens:");
            ((TextView) findViewById(R.id.ET_LobbyAct_Value)).setText("Single Token (NFT)");
            ((TextView) findViewById(R.id.ET_LobbyAct_Data)).setHint("Script Hash:");
            ((TextView) findViewById(R.id.buttonSEND)).setText("SEND");
        }


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
        //Boolean CompPKey = false;
        //Variables.CompPKey = false;

        String PUBKEY = pubKey.publicKeyHEX(pvtkey);

        String BSV160 = pubKey.bsvWalletRMD160(PUBKEY, Variables.CompPKey);
        String BSVADD = pubKey.bsvWalletFull(PUBKEY, Variables.CompPKey);


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

        //String newTX = txCreate.txBuilder(pvtkey, Variables.CompPKey,2 + nOR, PayWallets,PayValues,OP_RETURNs, nOR);

        //String SCRIPTHASH = "5c88edcd8ebaa34f16e97676cc3a11c0cfd2c1d9ad125e9e2023a923e4d12d65";
        //String SCRIPTHASH = "262e2fd813023f6050e7e2753c06fd61edf3eda228573c1dda687e2dc6f28d39";
        //String SCRIPTHASH = "53750ee980cc8f87ebf2888300a9b17367a85166eadbd1fdb81f9ad5445a7380";
        //String SCRIPTHASH = "0fccc2cf5757bcf3b300aee7b16a2df250d49c5660785b54d768a83b8988d1ee";

        // Problema em redimir outputs com OP_DROP;
        //There was an issue with the broadcast:unexpected response code 500: 64: non-mandatory-script-verify-flag (Data push larger than necessary)
        //Problema Relacionado a https://github.com/lbryio/lbrycrd/issues/242

        //String SCRIPTHASH = "626fd899c047310f3843be5d72e22084234618c401ab83b951cb1e21194d2395";
        // Problema em redimir outputs com OP_DROP;
        // There was an issue with the broadcast:unexpected response code 500: 64: non-mandatory-script-verify-flag (Data push larger than necessary)
        //Problema Relacionado a https://github.com/lbryio/lbrycrd/issues/242

        //String SCRIPTHASH = "1baecdac8f41f4692a1aa94199e3dbb3032f4abe07a3608a21beb27a3d1dad2a";//funcionou

        //String SCRIPTHASH = "9b9e01af809b9064bbbc9e55978a8bdb76589cf057a963c33f39ef1047de99b1"; // funcionou

        String SCRIPTHASH = "9b7eaedbd83c0ec707820824750afa620e98c4a861298cf06980486c68e6fdce";

        if(Variables.TokenType == 3)
        {
            SCRIPTHASH = data;
            data = "";
            nOR = 0;
            OP_RETURNs[0] = null;
            PayWallets[1] = PayWallets[0];

            PayValues[0] = "0000000000000000";

            if(SCRIPTHASH.length() != 64)
            {
                Toast.makeText(Token.this, "Wrong Script Hash!!!"
                        , Toast.LENGTH_LONG).show();
                return;
            }
        }

        if(Variables.TokenType == 4)
        {
            SCRIPTHASH = data;
            data = "";
            nOR = 1; //script
            OP_RETURNs[0] = null;
            //PayWallets[1] = PayWallets[0];

            PayValues[0] = "0000000000000000";

            if(SCRIPTHASH.length() != 64)
            {
                Toast.makeText(Token.this, "Wrong Script Hash!!!"
                        , Toast.LENGTH_LONG).show();
                return;
            }
        }

        int TXType = Variables.TokenType;

        String newTX = txCreate.txBuilderV2(pvtkey, Variables.CompPKey,2 + nOR,
                PayWallets,PayValues,OP_RETURNs, nOR, TXType, SCRIPTHASH);
        String result = "";

        if(newTX.length()>5)
            if(newTX.substring(0,5).compareTo("Error")==0) {
                result = newTX;
                Toast.makeText(Token.this, "Result: " + result
                        , Toast.LENGTH_LONG).show();
                return;
            }


        Variables.LastTxHexData = newTX;

        BsvTxOperations bsvTxOp = new BsvTxOperations();
        bsvTxOp.txID(newTX);
        Variables.LastTXID = bsvTxOp.TXID;

        result = txCreate.txBroadCast(newTX);

        //result = newTX;


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