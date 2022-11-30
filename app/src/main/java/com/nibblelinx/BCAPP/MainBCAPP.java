package com.nibblelinx.BCAPP;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentCallbacks2;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.View;
import android.widget.*;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;


public class MainBCAPP extends AppCompatActivity {

    //public static Activity fa;

    int inicio = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainnft);
        Button buttonMS1 = (Button) findViewById(R.id.buttonMS1);
        Button buttonMS2 = (Button) findViewById(R.id.buttonMS2);
       // Button buttonMS4 = (Button) findViewById(R.id.buttonMS4);
        Button buttonMS6 = (Button) findViewById(R.id.buttonMS6); //PVT KEY
        Button buttonMS7 = (Button) findViewById(R.id.buttonMS7);
        Button buttonMS8 = (Button) findViewById(R.id.buttonMS8);

        Button buttonMS9 = (Button) findViewById(R.id.buttonMS9);
        Button buttonMS10 = (Button) findViewById(R.id.buttonMS10); //Criar ODR Token
        Button buttonMS11 = (Button) findViewById(R.id.buttonMS11);
        Button buttonMS12 = (Button) findViewById(R.id.buttonMS12);
        Button buttonMS13 = (Button) findViewById(R.id.buttonMS13);
        Button buttonMS14 = (Button) findViewById(R.id.buttonMS14);
        Button buttonMS15 = (Button) findViewById(R.id.buttonMS15);
        Button buttonMS16 = (Button) findViewById(R.id.buttonMS16);
        Button buttonMS17 = (Button) findViewById(R.id.buttonMS17); //Specific UTXOs
        Button buttonMS18 = (Button) findViewById(R.id.buttonMS18); //Criacao de TRUE Tokens para ensino
        Button buttonMS19 = (Button) findViewById(R.id.buttonMS19); //PassWord

        //fa = this;



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarMS);
        setSupportActionBar(toolbar);

        //O verficador de pausa deve ser modificado em OnResume e OnCreate de cada Activity
        Variables.activityPause = false;
        //O contador de pausa deve estar presente em OnResume e OnCreate de cada Activity
        //Atenção especial para o uso de MAXSPECIALPAUSETIME
        Variables.timeCounter = Variables.MAXPAUSETIME;
        //O contador de Interação com Usuário deve também estar presente em OnResume e em OnCreate de cada activity
        //O contador deve ser resetado em OnResume, OnCreate, onUserInteraction de cada Activity
        Variables.userInteractionAct = Variables.MAXNOINTERACTIONTIME;

        Variables.SatBalance = "";
        Variables.MainPaymail = "";

        buttonMS1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Variables.ditactic = false;

                //if(Variables.MainPaymail.compareTo("")==0)
                if(Variables.MainPaymail.length() != 64)
                {
                    Toast.makeText(MainBCAPP.this, "Informe uma Chave Válida!!!", Toast.LENGTH_LONG).show();
                }
                else if(Variables.SatBalance.compareTo("0")==0)
                {
                    Toast.makeText(MainBCAPP.this, "Informe uma Carteira com Fundos!!!", Toast.LENGTH_LONG).show();
                }
                else {

                    Intent it = new Intent(MainBCAPP.this, NFTText.class);
                    startActivity(it);

                }
            }
        });

        buttonMS13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Variables.ditactic = true;

                //if(Variables.MainPaymail.compareTo("")==0)
                if(Variables.MainPaymail.length() != 64)
                {
                    Toast.makeText(MainBCAPP.this, "Informe uma Chave Válida!!!", Toast.LENGTH_LONG).show();
                }
                else {

                    Intent it = new Intent(MainBCAPP.this, NFTText.class);
                    startActivity(it);

                }
            }
        });


        buttonMS2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(Variables.MainPaymail.length() != 64)
                {
                    Toast.makeText(MainBCAPP.this, "Informe uma Chave Válida!!!", Toast.LENGTH_LONG).show();
                }

                else {

                    Keygen pubKey = new Keygen();
                    //Boolean CompPKey = false;
                    //Variables.CompPKey = false;

                    String PUBKEY = pubKey.publicKeyHEX(Variables.MainPaymail);//Variables.MainPaymail hex 64 elementos
                    String BSV160 = pubKey.bsvWalletRMD160(PUBKEY, Variables.CompPKey);
                    String BSVADD = pubKey.bsvWalletFull(PUBKEY, Variables.CompPKey);


                    Intent it = new Intent(MainBCAPP.this, TxidList.class);
                    Variables.MyNFTs = false;
                    //Este é o endereço para os dados serão enviados
                    //Deste endereço os dados também serão lidos
                    //É importante você ter o controle sobre este endereço
                    //Ou seja, que faça parte de uma das carteiras das quais você tem acesso
                    it.putExtra("NFTIndex", BSVADD);
                    startActivity(it);
                }
            }
        });

        buttonMS14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(Variables.MainPaymail.length() != 64)
                {
                    Toast.makeText(MainBCAPP.this, "Informe uma Chave Válida!!!", Toast.LENGTH_LONG).show();
                }

                else {

                    Keygen pubKey = new Keygen();
                    //Boolean CompPKey = false;
                    //Variables.CompPKey = false;

                    String PUBKEY = pubKey.publicKeyHEX(Variables.MainPaymail);//Variables.MainPaymail hex 64 elementos
                    //String BSV160 = pubKey.bsvWalletRMD160(PUBKEY, Variables.CompPKey);
                    String BSVADD = pubKey.bsvWalletFull(PUBKEY, Variables.CompPKey);


                    Intent it = new Intent(MainBCAPP.this, BcListen.class);
                    Variables.MyNFTs = false;
                    //Este é o endereço para os dados serão enviados
                    //Deste endereço os dados também serão lidos
                    //É importante você ter o controle sobre este endereço
                    //Ou seja, que faça parte de uma das carteiras das quais você tem acesso
                    it.putExtra("NFTIndex", BSVADD);
                    startActivity(it);
                }
            }
        });


        buttonMS7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String senha = MyPass

                if(Variables.MainPaymail.length() != 64)
                {
                    Toast.makeText(MainBCAPP.this, "Informe uma Chave Válida!!!", Toast.LENGTH_LONG).show();
                }
                else if(Variables.SatBalance.compareTo("0")==0)
                {
                    Toast.makeText(MainBCAPP.this, "Informe uma Carteira com Fundos!!!", Toast.LENGTH_LONG).show();
                }
                else {

                    Variables.TokenType = 0;
                    Intent it = new Intent(MainBCAPP.this, Token.class);
                    startActivity(it);
                }



            }
        });

        //SEND P2PK
        buttonMS15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String senha = MyPass

                if(Variables.MainPaymail.length() != 64)
                {
                    Toast.makeText(MainBCAPP.this, "Informe uma Chave Válida!!!", Toast.LENGTH_LONG).show();
                }
                else if(Variables.SatBalance.compareTo("0")==0)
                {
                    Toast.makeText(MainBCAPP.this, "Informe uma Carteira com Fundos!!!", Toast.LENGTH_LONG).show();
                }
                else {

                    Variables.TokenType = 5; //P2PK
                    Intent it = new Intent(MainBCAPP.this, Token.class);
                    startActivity(it);
                }



            }
        });

        //SPEND P2PK
        buttonMS16.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String senha = MyPass

                if(Variables.MainPaymail.length() != 64)
                {
                    Toast.makeText(MainBCAPP.this, "Informe uma Chave Válida!!!", Toast.LENGTH_LONG).show();
                }
                /*
                else if(Variables.SatBalance.compareTo("0")==0)
                {
                    Toast.makeText(MainBCAPP.this, "Informe uma Carteira com Fundos!!!", Toast.LENGTH_LONG).show();
                }
                */
                else {

                    Variables.TokenType = 6; //SPEND P2PK
                    Intent it = new Intent(MainBCAPP.this, Token.class);
                    startActivity(it);
                }



            }
        });

        //Specific UTXOs
        buttonMS17.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String senha = MyPass

                if(Variables.MainPaymail.length() != 64)
                {
                    Toast.makeText(MainBCAPP.this, "Informe uma Chave Válida!!!", Toast.LENGTH_LONG).show();
                }
                /*
                else if(Variables.SatBalance.compareTo("0")==0)
                {
                    Toast.makeText(MainBCAPP.this, "Informe uma Carteira com Fundos!!!", Toast.LENGTH_LONG).show();
                }
                */
                else {

                    Variables.TokenType = 7; //UTXO
                    Intent it = new Intent(MainBCAPP.this, Token.class);
                    startActivity(it);
                }



            }
        });

        //Criar TOR Token
        buttonMS9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String senha = MyPass

                if(Variables.MainPaymail.length() != 64)
                {
                    Toast.makeText(MainBCAPP.this, "Informe uma Chave Válida!!!", Toast.LENGTH_LONG).show();
                }
                else if(Variables.SatBalance.compareTo("0")==0)
                {
                    Toast.makeText(MainBCAPP.this, "Informe uma Carteira com Fundos!!!", Toast.LENGTH_LONG).show();
                }
                else {

                    Variables.TokenType = 1;
                    Intent it = new Intent(MainBCAPP.this, Token.class);
                    startActivity(it);
                }



            }
        });

        //Criar ODR Token
        buttonMS10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String senha = MyPass

                if(Variables.MainPaymail.length() != 64)
                {
                    Toast.makeText(MainBCAPP.this, "Informe uma Chave Válida!!!", Toast.LENGTH_LONG).show();
                }
                else if(Variables.SatBalance.compareTo("0")==0)
                {
                    Toast.makeText(MainBCAPP.this, "Informe uma Carteira com Fundos!!!", Toast.LENGTH_LONG).show();
                }
                else {

                    Variables.TokenType = 2;
                    Intent it = new Intent(MainBCAPP.this, Token.class);
                    startActivity(it);
                }



            }
        });


        buttonMS11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String senha = MyPass

                if(Variables.MainPaymail.length() != 64)
                {
                    Toast.makeText(MainBCAPP.this, "Informe uma Chave Válida!!!", Toast.LENGTH_LONG).show();
                }
                /*else if(Variables.SatBalance.compareTo("0")==0)
                {
                    Toast.makeText(MainBCAPP.this, "Informe uma Carteira com Fundos!!!", Toast.LENGTH_LONG).show();
                }*/
                else {

                    Variables.TokenType = 4;
                    Intent it = new Intent(MainBCAPP.this, Token.class);
                    startActivity(it);
                }



            }
        });

        //Melt Tokens
        buttonMS12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String senha = MyPass

                if(Variables.MainPaymail.length() != 64)
                {
                    Toast.makeText(MainBCAPP.this, "Informe uma Chave Válida!!!", Toast.LENGTH_LONG).show();
                }
                /*else if(Variables.SatBalance.compareTo("0")==0)
                {
                    Toast.makeText(MainBCAPP.this, "Informe uma Carteira com Fundos!!!", Toast.LENGTH_LONG).show();
                }*/
                else {

                    Variables.TokenType = 3; //Melt Tokens
                    Intent it = new Intent(MainBCAPP.this, Token.class);
                    startActivity(it);
                }



            }
        });

        //Create True Tokens
        buttonMS18.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String senha = MyPass

                if(Variables.MainPaymail.length() != 64)
                {
                    Toast.makeText(MainBCAPP.this, "Informe uma Chave Válida!!!", Toast.LENGTH_LONG).show();
                }
                /*else if(Variables.SatBalance.compareTo("0")==0)
                {
                    Toast.makeText(MainBCAPP.this, "Informe uma Carteira com Fundos!!!", Toast.LENGTH_LONG).show();
                }*/
                else {

                    Variables.TokenType = 8; //Create Tokens
                    Intent it = new Intent(MainBCAPP.this, Token.class);
                    startActivity(it);
                }



            }
        });


        buttonMS8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String senha = MyPass

                if(Variables.LastTXID.length() != 64)
                {
                    Toast.makeText(MainBCAPP.this, "TXID inválido!!!", Toast.LENGTH_LONG).show();
                }
                else {

                    Intent it = new Intent(MainBCAPP.this, TxVerify.class);
                    startActivity(it);

                }



            }
        });

        //PVT KEY
        buttonMS6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String senha = MyPass

                AlertDialog.Builder dialog = new AlertDialog.Builder(MainBCAPP.this);
                //dialog.setTitle("BANCO DE DADOS PESSOAL");
                dialog.setTitle("Chave Privada");
                dialog.setMessage("Insert your Hex Pvt Key:");


                /////////////////////////////////////////////////////
                //PIN
                /////////////////////////////////////////////////////
                final EditText ETdialog_PIN;
                ETdialog_PIN = new EditText(MainBCAPP.this);
                LinearLayout layout = new LinearLayout(MainBCAPP.this);
                layout.setOrientation(LinearLayout.VERTICAL);
                ETdialog_PIN.setHint("Hexadecimal PVT Key");
                ETdialog_PIN.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD); // phone 3
                //ETdialog_PIN.setInputType(InputType.TYPE_CLASS_TEXT); // phone 3
                layout.addView(ETdialog_PIN);
                dialog.setView(layout);
                /////////////////////////////////////////////////////
                /////////////////////////////////////////////////////


                dialog.setPositiveButton("UnCompressed", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Variables.MainPaymail = ETdialog_PIN.getText().toString();

                        if(Variables.MainPaymail.length() == 64) {

                            Variables.CompPKey = false;

                            setAddValue();

                            dialog.dismiss();
                        }

                    }
                });
                dialog.setNegativeButton("Compressed", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Variables.MainPaymail = ETdialog_PIN.getText().toString();

                        if(Variables.MainPaymail.length() == 64) {
                            //Keygen pubKey = new Keygen();
                            Variables.CompPKey = true;
                            setAddValue();

                        }

                    }
                });

                dialog.create();
                dialog.show();
            }
        });


        //Senha
        buttonMS19.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String senha = MyPass

                AlertDialog.Builder dialog = new AlertDialog.Builder(MainBCAPP.this);
                //dialog.setTitle("BANCO DE DADOS PESSOAL");
                dialog.setTitle("PassWord");
                dialog.setMessage("Insert your PassWord:");


                /////////////////////////////////////////////////////
                //PIN
                /////////////////////////////////////////////////////
                final EditText ETdialog_PIN;
                ETdialog_PIN = new EditText(MainBCAPP.this);
                LinearLayout layout = new LinearLayout(MainBCAPP.this);
                layout.setOrientation(LinearLayout.VERTICAL);
                ETdialog_PIN.setHint("Min 8 char");
                ETdialog_PIN.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD); // phone 3
                //ETdialog_PIN.setInputType(InputType.TYPE_CLASS_TEXT); // phone 3
                layout.addView(ETdialog_PIN);
                dialog.setView(layout);
                /////////////////////////////////////////////////////
                /////////////////////////////////////////////////////


                dialog.setPositiveButton("Uncomp", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Variables.MainPaymail = ETdialog_PIN.getText().toString();

                        //if(Variables.MainPaymail.length() == 64) {
                        if(Variables.MainPaymail.length() >= 8) {

                            Variables.CompPKey = false;

                            ChaveDaSenha();
                            setAddValue();

                            dialog.dismiss();
                        }

                    }
                });
                dialog.setNegativeButton("Comp", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Variables.MainPaymail = ETdialog_PIN.getText().toString();

                        //if(Variables.MainPaymail.length() == 64) {
                        if(Variables.MainPaymail.length() >= 8) {
                            //Keygen pubKey = new Keygen();

                            Variables.CompPKey = true;

                            ChaveDaSenha();
                            setAddValue();

                        }

                    }
                });

                dialog.setNeutralButton("SHOW PVT KEY", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Variables.MainPaymail = ETdialog_PIN.getText().toString();

                        if(Variables.MainPaymail.length() >= 8) {

                            ChaveDaSenha();
                            showPVTKEY();
                        }

                    }
                });


                dialog.create();
                dialog.show();
            }
        });



    }

    private void ChaveDaSenha()
    {
        Variables.MainPaymail = SHA256G.SHA256STR(Variables.MainPaymail);

        if(Variables.MainPaymail == null)
        {
            Toast.makeText(MainBCAPP.this, "Senha Invalida!!", Toast.LENGTH_LONG).show();
            return;
        }

        Variables.MainPaymail = SHA256G.LEformat(Variables.MainPaymail);
        Variables.MainPaymail = SHA256G.SHA256bytes( SHA256G.HashStrToByte2(Variables.MainPaymail));

        //Toast.makeText(MainBCAPP.this, Variables.MainPaymail, Toast.LENGTH_LONG).show();

        //QRCODE = false;
        //setAddValue();
    }

    private void showPVTKEY()
    {
        AlertDialog.Builder dialog = new AlertDialog.Builder(MainBCAPP.this);
        //dialog.setTitle("BANCO DE DADOS PESSOAL");
        dialog.setTitle("Private Key");
        dialog.setMessage("Private Key from PassWord:");


        /////////////////////////////////////////////////////
        //PIN
        /////////////////////////////////////////////////////
        final EditText ETdialog_PIN;
        ETdialog_PIN = new EditText(MainBCAPP.this);
        LinearLayout layout = new LinearLayout(MainBCAPP.this);
        layout.setOrientation(LinearLayout.VERTICAL);
        ETdialog_PIN.setText(Variables.MainPaymail);
        //ETdialog_PIN.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD); // phone 3
        //ETdialog_PIN.setInputType(InputType.TYPE_CLASS_TEXT); // phone 3
        layout.addView(ETdialog_PIN);
        dialog.setView(layout);
        /////////////////////////////////////////////////////
        /////////////////////////////////////////////////////


        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {



            }
        });

        dialog.create();
        dialog.show();
    }


    //Boolean CompPKey = true;

    public void setAddValue()
    {
        Keygen pubKey = new Keygen();
        //BsvTxCreation txCreate = new BsvTxCreation();
        String PUBKEY = pubKey.publicKeyHEX(Variables.MainPaymail);
        String BSVADD = pubKey.bsvWalletFull(PUBKEY, Variables.CompPKey);
        Variables.BSVWallet = BSVADD;

        //String SatBalance = "";

        BsvTxCreation txCreate = new BsvTxCreation();

        //BsvAddUnspent txCreate = new BsvAddUnspent();

        //txCreate = null;
        //txCreate = new BsvTxCreation();
        //Variables.STREAMTT ++;

        //SatBalance = txCreate.totalUnspent(BSVADD);

        //Para solucionar problema da Espera é bom colocar o comando dentro de uma thread
        Variables.SatBalance = txCreate.totalUnspent(BSVADD);

        //txCreate = null;

        /*
        if(SatBalance.substring(0,5).compareTo("Error") == 0) {
            return;
        }

         */


        if(Variables.SatBalance == null) {

            Toast.makeText(MainBCAPP.this, "Falha ao Ler o Valor", Toast.LENGTH_LONG).show();
            //Toast.makeText(MainBCAPP.this, Variables.SatBalance, Toast.LENGTH_LONG).show();
            return;
        }



        //SatBalance = "Balance (Satoshis): " + SatBalance + " sats";



        //txCreate.totalUnspent(BSVADD);
        ((TextView) findViewById(R.id.TV_TEXT2)).setText("Balance (Satoshis): " + Variables.SatBalance + " sats");
        //((TextView) findViewById(R.id.TV_TEXT2)).setText(SatBalance);
        ((TextView) findViewById(R.id.TV_TEXT3)).setText(Variables.BSVWallet);
        //((TextView) findViewById(R.id.TV_TEXT4)).setText("Balance (Miritis): " + Long.valueOf(Variables.SatBalance)/1000 + " Miritis");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_txidlist, menu);
        return true;
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
        //Variables.activityPause = false;

        //O verficador de pausa deve ser modificado em OnResume e OnCreate de cada Activity
        Variables.activityPause = false;
        //O contador de pausa deve estar presente em OnResume e OnCreate de cada Activity
        //Atenção especial para o uso de MAXSPECIALPAUSETIME
        Variables.timeCounter = Variables.MAXPAUSETIME;
        //O contador de Interação com Usuário deve também estar presente em OnResume e em OnCreate de cada activity
        //O contador deve ser resetado em OnResume, OnCreate, onUserInteraction de cada Activity
        Variables.userInteractionAct = Variables.MAXNOINTERACTIONTIME;


        if(inicio == 0)
            inicio = 1;
        else
        {
           if(Variables.SatBalance.length() > 0) {

               ((TextView) findViewById(R.id.TV_TEXT2)).setText("Balance (Satoshis): " + Variables.SatBalance + " sats");
               ((TextView) findViewById(R.id.TV_TEXT3)).setText(Variables.BSVWallet);
               //((TextView) findViewById(R.id.TV_TEXT4)).setText("Balance (Miritis): " + Long.valueOf(Variables.SatBalance) / 1000 + " Miritis");
           }
        }


        //((TextView) findViewById(R.id.TV_TEXT2)).setText("Balance: " + Variables.SatBalance + " sats");
        //((TextView) findViewById(R.id.TV_TEXT3)).setText(Variables.BSVWallet);

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