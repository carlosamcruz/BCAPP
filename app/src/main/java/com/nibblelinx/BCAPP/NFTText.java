package com.nibblelinx.BCAPP;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.BidiClassifier;
import android.net.ConnectivityManager;
import android.net.Network;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import androidx.annotation.RequiresApi;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.textfield.TextInputEditText;

//////////////////////////////////////////////////////////////////
//OPERACOES COM ARQUIVOS
/////////////////////////////////////////////////////////////////
import java.math.BigInteger;
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

                if(Variables.ditactic == true)
                {
                    //didacticPrint();

                    //BigInteger bNumber;

                    final String text = ((EditText) findViewById(R.id.ET_TEXTOST)).getText().toString();
                    //int numberOfZeros = Integer.valueOf(text);
                    //didacticPoW(numberOfZeros);

                    //bNumber = new BigInteger(text);

                    //didacticInvGalois(text);

                    chooseOperation(text);


                }else

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
                            result2 = null;
                            new DecBackGround2().onPreExecute();
                            new Thread(new Runnable() {
                                @Override
                                public void run() {

                                    result2 = new DecBackGround2().execute(SHA256G.HashStrToByte2(text));

                                    //Para a execução na thread pricipal
                                    runOnUiThread(new Runnable() {
                                        public void run() {
                                            // do onPostExecute stuff
                                            new DecBackGround2().onPostExecute(result2);
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
                        //sendTX(newTextChar);
                        sendTXNEW(newTextChar);
                    }
                }
            }
        });

        Toast.makeText(this, "TEXT DATA!!!", Toast.LENGTH_SHORT).show();
    }

    public void chooseOperation(String n)
    {
        int firstIndiceOf;
        int nextIndex;
        firstIndiceOf = n.indexOf(";");

        nextIndex = n.indexOf(";", firstIndiceOf+1);

        //n = n.substring(firstIndiceOf+1, nextIndex);

        if(n.substring(0,1).compareTo("0") == 0)
            didacticInvGalois(n.substring(firstIndiceOf+1, nextIndex));
        if(n.substring(0,1).compareTo("1") == 0) {

            String x, y, no;
            x = n.substring(firstIndiceOf+1, nextIndex);
            firstIndiceOf = n.indexOf(";", nextIndex+1);
            nextIndex = n.indexOf(";", firstIndiceOf+1);
            y = n.substring(firstIndiceOf+1, nextIndex);
            firstIndiceOf = n.indexOf(";", nextIndex+1);
            nextIndex = n.indexOf(";", firstIndiceOf+1);
            no = n.substring(firstIndiceOf+1, nextIndex);

            nG(x, y, no);
        }
        if(n.substring(0,1).compareTo("2") == 0) {

            String x, y, x2, y2;
            x = n.substring(firstIndiceOf+1, nextIndex);
            firstIndiceOf = n.indexOf(";", nextIndex+1);
            nextIndex = n.indexOf(";", firstIndiceOf+1);
            y = n.substring(firstIndiceOf+1, nextIndex);
            firstIndiceOf = n.indexOf(";", nextIndex+1);
            nextIndex = n.indexOf(";", firstIndiceOf+1);
            x2 = n.substring(firstIndiceOf+1, nextIndex);
            firstIndiceOf = n.indexOf(";", nextIndex+1);
            nextIndex = n.indexOf(";", firstIndiceOf+1);
            y2 = n.substring(firstIndiceOf+1, nextIndex);


            somaG(x, y, x2, y2);
        }

        //Determinar os Valores de Y de um Ponto X
        if(n.substring(0,1).compareTo("3") == 0) {

            yFromx(n.substring(firstIndiceOf+1, nextIndex));
        }

        //Determina se o Ponto Pertence a Curva
        if(n.substring(0,1).compareTo("4") == 0) {

            String x, y, no;
            x = n.substring(firstIndiceOf+1, nextIndex);
            firstIndiceOf = n.indexOf(";", nextIndex+1);
            nextIndex = n.indexOf(";", firstIndiceOf+1);
            y = n.substring(firstIndiceOf+1, nextIndex);
           
            pointInECC(x, y);
        }

        //((EditText) findViewById(R.id.ET_TEXTOST)).setText(n.substring(0,1) +"\n" + n.substring(firstIndiceOf+1, nextIndex));
    }

    Ecc keys = new Ecc();
    TonelliShanks sqrtCF = new TonelliShanks();


    public void pointInECC (String x, String y)
    {
        BigInteger [] point = new BigInteger[2];
        point [0] = new BigInteger(x);
        point [1] = new BigInteger(y);;

        int i = 0;

        if(keys.modp(
                ((point[0].multiply(keys.modp(point[0].multiply(point[0]),keys.p))).add((keys.A).multiply(point[0]))).add(keys.B)
                ,keys.p).compareTo(keys.modp(point[1].multiply(point[1]),keys.p)) == 0) {
            i = 1;
            ((EditText) findViewById(R.id.ET_TEXTOST)).setText(
                    "Result: Ponto Pertence a Curva"
            );
        }
        else
            ((EditText) findViewById(R.id.ET_TEXTOST)).setText(
                    "Result: Ponto NÃO Pertence a Curva"
            );
    }

    public void yFromx (String x)
    {
        BigInteger [] point = new BigInteger[2];
        point [0] = new BigInteger(x);
        point [1] = BigInteger.valueOf(0);

        // Entcontra o valor de GBy atraves do algoritmo de raiz quadrada em campos finitos
        //   gBchIN64[1] = tonelli_shanks(modp((gBchIN64[0]*modp(gBchIN64[0]*gBchIN64[0], p) + A*gBchIN64[0] + B), p), p);

        //Operacao xor para decidir se escolhe y ou (p - y)
        //    if( ( !(gBchIN64[1] & 1) && (gBch64CAP[0] & 1) ) || ( (gBchIN64[1] & 1) && !(gBch64CAP[0] & 1) ) )
        //        gBchIN64[1] = p - gBchIN64[1];

        point[1] = sqrtCF.sqrtCF(keys.modp(
                ((point[0].multiply(keys.modp(point[0].multiply(point[0]),keys.p))).add((keys.A).multiply(point[0]))).add(keys.B)
                ,keys.p),keys.p);

            //point[1] = (keys.p).subtract(point[1]);

        ((EditText) findViewById(R.id.ET_TEXTOST)).setText(
                "Result: \n" +
                        "\n\nx: " + point[0] +
                        "\n\ny1: " + point[1]+
                "\n\ny2: " + (keys.p).subtract(point[1])
                //        "\n\nNZeros: " + numZeros +
                //        "\n\nTZeros: " + 4*numZeros
                // "\n\nCont: " + cont
                // "\n\nCont2: " + cont2
                //"\n\nChar 63: " + result.charAt(63)
        );
    }

    public void somaG(String x, String y, String x2, String y2) {


        Ecc bInt;
        bInt = new Ecc();

        BigInteger[] result;
        BigInteger bx = new BigInteger(x);
        BigInteger by = new BigInteger(y);
        BigInteger bx2 = new BigInteger(x2);
        BigInteger by2 = new BigInteger(y2);

        result = bInt.addp(bx, by, bx2, by2);

        //((EditText) findViewById(R.id.ET_TEXTOST)).setText("PVT Key: " + Variables.MainPaymail);



        ((EditText) findViewById(R.id.ET_TEXTOST)).setText(
                "Result: \n" +
                        "\n\nx: " + result[0] +
                        "\n\ny: " + result[1]
                //        "\n\nNZeros: " + numZeros +
                //        "\n\nTZeros: " + 4*numZeros
                // "\n\nCont: " + cont
                // "\n\nCont2: " + cont2
                //"\n\nChar 63: " + result.charAt(63)
        );

        //((TextView) findViewById(R.id.TV_TEXT2bsv)).setText("Balance (Satoshis): " + Variables.SatBalance + " sats");
    }

    public void nG(String x, String y, String n_order) {


        Ecc bInt;
        bInt = new Ecc();

        BigInteger[] result;
        BigInteger bx = new BigInteger(x);
        BigInteger by = new BigInteger(y);
        BigInteger n_o = new BigInteger(n_order);

        result = bInt.eccnP(n_o, bx, by);

        //((EditText) findViewById(R.id.ET_TEXTOST)).setText("PVT Key: " + Variables.MainPaymail);



        ((EditText) findViewById(R.id.ET_TEXTOST)).setText(
                "Result: \n" +
                        "\n\nx: " + result[0] +
                        "\n\ny: " + result[1]
                //        "\n\nNZeros: " + numZeros +
                //        "\n\nTZeros: " + 4*numZeros
                // "\n\nCont: " + cont
                // "\n\nCont2: " + cont2
                //"\n\nChar 63: " + result.charAt(63)
        );

        //((TextView) findViewById(R.id.TV_TEXT2bsv)).setText("Balance (Satoshis): " + Variables.SatBalance + " sats");
    }

    public void didacticInvGalois(String n) {


        Ecc bInt;
        bInt = new Ecc();

        BigInteger result = new BigInteger(n);

        result = bInt.inverse(result, bInt.n_order);

        //((EditText) findViewById(R.id.ET_TEXTOST)).setText("PVT Key: " + Variables.MainPaymail);



        ((EditText) findViewById(R.id.ET_TEXTOST)).setText(
                "Result: \n" + n +
                        "\n\nn_order: " + bInt.n_order +
                        "\n\nInv: " + result
                //        "\n\nNZeros: " + numZeros +
                //        "\n\nTZeros: " + 4*numZeros
                // "\n\nCont: " + cont
                // "\n\nCont2: " + cont2
                //"\n\nChar 63: " + result.charAt(63)
        );

        //((TextView) findViewById(R.id.TV_TEXT2bsv)).setText("Balance (Satoshis): " + Variables.SatBalance + " sats");
    }

    public void didacticPoW(int numZeros) {

        //((EditText) findViewById(R.id.ET_TEXTOST)).setText("PVT Key: " + Variables.MainPaymail);

        long nonce = 0;
        //String base = "Texto qualquer1";

        String base = "00000895e2c2a19ec3906a6e87f63bdd90554ea83a7ae078bb0495c23aa6f832 INFO BLOCK; Tx1 abcd; Tx3 cef5463; Tx2 123456; ;... Txn efabc456";
        //String result = SHA256G.SHA256STR(base);
        String result = "";

        int cont = 0;
        int cont2 = 0;

        do {
            cont2 ++;
            cont = 0;
            result = SHA256G.SHA256STR(base + nonce);
            result = SHA256G.SHA256bytes(SHA256G.HashStrToByte2(result));

            for (int i = 0; i < numZeros; i++) {
                if (result.charAt(i) == '0')
                    cont++;
            }
            nonce++;

        } while (cont < numZeros && nonce < 0x7fffffffffffffffL);
        //} while (cont2 <= 100);


        ((EditText) findViewById(R.id.ET_TEXTOST)).setText(
                "Result: \n" + result +
                "\n\nNonce: " + (nonce - 1) +
                "\n\nBase: " + base +
                "\n\nNZeros: " + numZeros +
                "\n\nTZeros: " + 4*numZeros
               // "\n\nCont: " + cont
               // "\n\nCont2: " + cont2
                //"\n\nChar 63: " + result.charAt(63)
        );

        //((TextView) findViewById(R.id.TV_TEXT2bsv)).setText("Balance (Satoshis): " + Variables.SatBalance + " sats");
    }

    public void didacticPrint() {

        //((EditText) findViewById(R.id.ET_TEXTOST)).setText("PVT Key: " + Variables.MainPaymail);
        Keygen pubKey = new Keygen();

        String PUBKEY = pubKey.publicKeyHEX(Variables.MainPaymail); //PVTKEY - string Hexadecimal de 64 elementos.

        String BSV160 = pubKey.bsvWalletRMD160(PUBKEY, Variables.CompPKey);
        String BSVADD = pubKey.bsvWalletFull(PUBKEY, Variables.CompPKey);

        ((EditText) findViewById(R.id.ET_TEXTOST)).setText(
                "PVT Key: \n" + Variables.MainPaymail +
                "\n\nPVT Key BI: \n" + Variables.dPvtKey +
                "\n\nPub Key X BI: \n" + Variables.dPubKX +
                "\n\nPub Key Y BI: \n" + Variables.dPubKY +
                "\n\nPub Key Uncomp: \n" + Variables.dPubKUnComp +
                "\n\nPub Key Comp: \n" + Variables.dPubKComp +
                "\n\nSHA256(Pub Key Uncomp): \n" + Variables.dSha256UnComp +
                "\n\nSHA256(Pub Key Comp): \n" + Variables.dSha256Comp +
                "\n\nRMD160(SHA256(Pub Key Uncomp)): \n" + Variables.dRMP160UnComp +
                "\n\nRMD160(SHA256(Pub Key Comp)): \n" + Variables.dRMP160Comp +

                "\n\nBVRMD160(SHA256(Pub Key Uncomp)): \n" + Variables.d00RMP160UnComp +
                "\n\nBVRMD160(SHA256(Pub Key Comp)): \n" + Variables.d00RMP160Comp +

                "\n\n2SHA256(BVRMD160(SHA256(Pub Key Uncomp))): \n" + Variables.dHashMD160UnComp +
                "\n\n2SHA256(BVRMD160(SHA256(Pub Key Comp))): \n" + Variables.dHashMD160Comp +

                "\n\nCHECKSUM UnComp: \n" + Variables.dHashMD160UnComp.substring(0, 8) +
                "\n\nCHECKSUM Comp: \n" + Variables.dHashMD160Comp.substring(0, 8) +
                "\n\n00 + RMD160 + CHECKSUM UnComp: \n" +"00"+ Variables.dRMP160UnComp + Variables.dHashMD160UnComp.substring(0, 8)  +
                "\n\n00 + RMD160 + CHECKSUM Comp: \n" +"00"+ Variables.dRMP160Comp + Variables.dHashMD160Comp.substring(0, 8) +

                "\n\nBI(00 + RMD160 + CHECKSUM UnComp): \n" + Variables.dCSRMD160UnCompBi +
                "\n\nBI(00 + RMD160 + CHECKSUM Comp): \n" + Variables.dCSRMD160CompBi +

                "\n\nBase58(BI(00 + RMD160 + CHECKSUM UnComp))Pre: \n" + Variables.dAddUnCompPre +
                "\n\nBas58(BI(00 + RMD160 + CHECKSUM Comp))Pre: \n" + Variables.dAddCompPre +

                "\n\nBase58(BI(00 + RMD160 + CHECKSUM UnComp))LZ: \n" + Variables.dAddUnCompLZ +
                "\n\nBas58(BI(00 + RMD160 + CHECKSUM Comp))LZ: \n" + Variables.dAddCompLZ +

                "\n\nBase58(BI(00 + RMD160 + CHECKSUM UnComp)): \n" + Variables.dAddUnComp +
                "\n\nBas58(BI(00 + RMD160 + CHECKSUM Comp)): \n" + Variables.dAddComp

        );

        //((TextView) findViewById(R.id.TV_TEXT2bsv)).setText("Balance (Satoshis): " + Variables.SatBalance + " sats");
    }



////////////////////////////////////////////////////////////////////////////////////////
// Thread Work
////////////////////////////////////////////////////////////////////////////////////////

    //final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Token.this, R.style.ThemeOverlay_App_MaterialAlertDialog);
    private AlertDialog.Builder alertDialogBuilder;// = new AlertDialog.Builder(Token.this, R.style.ThemeOverlay_App_MaterialAlertDialog);
    public void createAlertDialog(int id, String res) {
        LayoutInflater inflater = getLayoutInflater();
        View view = null;

        if (id == 1) {
            view = inflater.inflate(R.layout.alertdialog_error, null);
        } else if (id == 2) {
            view = inflater.inflate(R.layout.alertdialog_success, null);
            //view = inflater.inflate(R.layout.alertdialog_wait, null);
            //((TextView) findViewById(R.id.DisplayTXID)).setText("ABC");
        }

        if (view == null) {
            return;
        }

        if(alertDialogBuilder != null)
            alertDialogBuilder = null;

        //final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Token.this, R.style.ThemeOverlay_App_MaterialAlertDialog);
        alertDialogBuilder = new AlertDialog.Builder(NFTText.this);
        alertDialogBuilder.setView(view);

        final AlertDialog alertDialog = alertDialogBuilder.create();
        //((TextView) findViewById(R.id.DisplayTXID)).setText("TXID: "+res);

        alertDialog.show();

        switch (id) {
            case 1:
                Button buttonTryAgain = alertDialog.findViewById(R.id.ButtonTryAgain);
                buttonTryAgain.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                        //ScannerQrCode();
                    }
                });
                break;

            case 2:

                //setContentView(R.layout.alertdialog_success);
                Button buttonConfirm = alertDialog.findViewById(R.id.ButtonConfirm);

                //((TextView) findViewById(R.id.DisplayTXID2)).setText("ABC");
                //TextInputEditText inputTXID2 = alertDialog.findViewById(R.id.DisplayTXID);
                TextInputEditText inputTXID = alertDialog.findViewById(R.id.InputTXID);
                inputTXID.setText(res);
                //inputTXID2.setText("abcd");

                buttonConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

                break;

            default:
                break;
        }
    }


    private AlertDialog alertDialogWait;
    public void DialogWait(int STATE) {
        LayoutInflater inflater = getLayoutInflater();
        View view = null;

        view = inflater.inflate(R.layout.alertdialog_wait, null);

        if (view == null) {
            return;
        }

        if(STATE == 0) {
            //final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Token.this, R.style.ThemeOverlay_App_MaterialAlertDialog);
            alertDialogBuilder = new AlertDialog.Builder(NFTText.this);
            alertDialogBuilder.setView(view);

            alertDialogWait = alertDialogBuilder.create();
            alertDialogWait.show();

            TextInputEditText inputTXID = alertDialogWait.findViewById(R.id.InputPhase);

            //inputTXID.setText("Phase " + Variables.TxPhases);


            switch (Variables.TxPhases) {
                case 1:
                    inputTXID.setText("Iniciando a Criação da Transação ... ");
                    break;

                case 2:
                    inputTXID.setText("Verificando Entradas da Transação ... ");
                    break;

                case 3:
                    inputTXID.setText("Criando as Saídas da Transação ... ");
                    break;

                case 4:
                    inputTXID.setText("Criando Pre-Imagens da Transação ... ");
                    break;

                case 5:
                    inputTXID.setText("Assinando Input " + Variables.TxPhasesNinp
                            + " de " + Variables.TxPhasesNinpTotal + " (Aguarde!!!)");
                    break;

                case 6:
                    inputTXID.setText("Enviando a Transação ..." );
                    break;

                default:
                    break;
            }
        }

        if(STATE == 1) {
            alertDialogWait.dismiss();
            alertDialogBuilder = null;
        }

    }

    Thread threadBuild;
    private void renewThreadBuild()
    {
        threadBuild = new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    buildTXTH();

                } catch (Exception e) {
                    e.printStackTrace();
                }

                //Para a execução na thread pricipal
                runOnUiThread(new Runnable() {
                    public void run() {
                        // do onPostExecute stuff
                        //new NoConnection().onPostExecute(result);
                        sendTXPhase2();
                    }
                });
            }
        });
    }


    String newTX = "";

    private byte[] newTextChar;

    public void buildTXTH()
    {

        String pvtkey = Variables.MainPaymail;
        //String sendTo = SendTo.getText().toString();
        //String sats = Satoshis.getText().toString();
        String data = SHA256G.ByteToStrHex(newTextChar);


        //////////////////////////////////////////////////////////////////////////////////////////////////
        //Preparação das Chaves
        //////////////////////////////////////////////////////////////////////////////////////////////////
        Keygen pubKey = new Keygen();
        //Boolean CompPKey = false;
        //Variables.CompPKey = false;

        String PUBKEY = pubKey.publicKeyHEX(pvtkey); //PVTKEY - string Hexadecimal de 64 elementos.
        String BSV160 = pubKey.bsvWalletRMD160(PUBKEY, Variables.CompPKey);
        String BSVADD = pubKey.bsvWalletFull(PUBKEY, Variables.CompPKey);


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

        //Problema Aqui;
        newTX = txCreate.txBuilder(pvtkey, Variables.CompPKey,1 + nOR, PayWallets,PayValues,OP_RETURNs, nOR);
    }

    Thread threadSend;
    private void renewThreadSend()
    {
        threadSend = new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    sendTXTH();

                } catch (Exception e) {
                    e.printStackTrace();
                }

                //Para a execução na thread pricipal
                runOnUiThread(new Runnable() {
                    public void run() {
                        // do onPostExecute stuff
                        //new NoConnection().onPostExecute(result);
                        sendTXPhase3();
                    }
                });
            }
        });
    }


    String result = "";
    //Boolean dialogX = false;
    public void sendTXTH() {

        BsvTxCreation txCreate = new BsvTxCreation();


        Variables.LastTxHexData = newTX;

        BsvTxOperations bsvTxOp = new BsvTxOperations();
        bsvTxOp.txID(newTX);
        Variables.LastTXID = bsvTxOp.TXID;

        result = txCreate.txBroadCast(newTX);
        //result = newTX;
        //dialogX =true;
    }

    int phaseTx = 0;
    //public void sendTXNEW() {
    public void sendTXNEW(byte[] newTextCharThis){

        newTextChar = newTextCharThis;

        //dialogX = false;
        phaseTx = 0;
        Variables.TxPhases = 1;
        if (timer != null) {
            timer.cancel();
            timer.purge();
        }

        timer = new Timer();
        timer.schedule(new TimeCheck(), 0, 4000);

        //if(Variables.SatBalance!=null) {
        //    Toast.makeText(Token.this, "Construindo TX...", Toast.LENGTH_LONG).show();
        //    return;
        //}
        //Toast.makeText(Token.this, "Construindo TX...", Toast.LENGTH_LONG).show();
        renewThreadBuild();
        threadBuild.start();

        phaseTx = 1;
        DialogWait(0);

    }


    public void sendTXPhase2() {

        if (newTX.length() > 5) {
            if (newTX.substring(0, 5).compareTo("Error") == 0) {
                Toast.makeText(NFTText.this, newTX
                        , Toast.LENGTH_LONG).show();
                return;
            }
        }

        //Toast.makeText(NFTText.this, "Enviando Transação ...", Toast.LENGTH_LONG).show();
        renewThreadSend();
        threadSend.start();
    }


    public void sendTXPhase3()
    {
        if (timer != null) {
            timer.cancel();
            timer.purge();

            if(alertDialogBuilder != null)
                //if(dialogX)
                DialogWait(1);
        }

        //Toast.makeText(NFTText.this, "Result: " + result, Toast.LENGTH_LONG).show();

        //DialogWait(1);

        //createAlertDialog(2, result);

        if(result.length() != 64)
            createAlertDialog(1, result);
        else
            createAlertDialog(2, result);

        //Valor final da carteira
        //Variables.SatBalance = Long.toString(Long.valueOf(Variables.SatBalance));

        //((TextView) findViewById(R.id.DisplayCurrentBalance)).setText("Saldo (Miritis): " + Variables.SatBalance + " mrts");
        //((TextView) findViewById(R.id.DisplayUserWallet)).setText(Variables.BSVWallet);

    }

    Timer timer;

    //BsvTxCreation txCreate = new BsvTxCreation();
    class TimeCheck extends TimerTask
    {
        @RequiresApi(api = Build.VERSION_CODES.M)
        public void run()
        {
            new Thread(new Runnable() {
                @Override
                public void run() {

                    //Para a execução na thread pricipal
                    runOnUiThread(new Runnable() {
                        public void run() {

                            if(alertDialogBuilder != null)
                                //if(dialogX)
                                DialogWait(1);
                            phaseTx ++;
                            DialogWait(0);

                        }
                    });

                }
            }).start();
        }
    }


////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////



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
        //Boolean CompPKey = false;
        //Variables.CompPKey = false;

        String PUBKEY = pubKey.publicKeyHEX(pvtkey); //PVTKEY - string Hexadecimal de 64 elementos.
        String BSV160 = pubKey.bsvWalletRMD160(PUBKEY, Variables.CompPKey);
        String BSVADD = pubKey.bsvWalletFull(PUBKEY, Variables.CompPKey);


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

        //Problema Aqui;
        String newTX = txCreate.txBuilder(pvtkey, Variables.CompPKey,1 + nOR, PayWallets,PayValues,OP_RETURNs, nOR);
        String result = "";
        //if(newTX.compareTo("Error 1")==0 || newTX.compareTo("Error 2")==0)

        if(newTX.length()>5)
            if(newTX.substring(0,5).compareTo("Error")==0)
                result = newTX;


        //if(newTX.substring(0,5).compareTo("Error")==0)
        //    result = newTX;
        //else
        {

            Variables.LastTxHexData = newTX;

            BsvTxOperations bsvTxOp = new BsvTxOperations();
            bsvTxOp.txID(newTX);
            Variables.LastTXID = bsvTxOp.TXID;


            result = txCreate.txBroadCast(newTX);
            //result = "Debug";
        }

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

    byte[] result2;

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
