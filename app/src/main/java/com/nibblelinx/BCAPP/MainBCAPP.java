package com.nibblelinx.BCAPP;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;


public class MainBCAPP extends AppCompatActivity {

    public static Activity fa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainnft);
        Button buttonMS1 = (Button) findViewById(R.id.buttonMS1);
        Button buttonMS2 = (Button) findViewById(R.id.buttonMS2);
        Button buttonMS4 = (Button) findViewById(R.id.buttonMS4);
        Button buttonMS6 = (Button) findViewById(R.id.buttonMS6);

        fa = this;

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

        buttonMS1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(Variables.MainPaymail.compareTo("")==0)
                {
                    Toast.makeText(MainBCAPP.this, "Inform Your Main Paymail!!!", Toast.LENGTH_LONG).show();
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

                Intent it = new Intent(MainBCAPP.this, TxidList.class);
                Variables.MyNFTs = false;
                //Este é o endereço para os dados serão enviados
                //Deste endereço os dados também serão lidos
                //É importante você ter o controle sobre este endereço
                //Ou seja, que faça parte de uma das carteiras das quais você tem acesso
                it.putExtra("NFTIndex", "1McQFu2vM5LnnanWA3su4LmokHZDchqM7u");
                startActivity(it);
            }
        });

        buttonMS4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String senha = MyPass

                Intent it = new Intent(MainBCAPP.this, MoneyButton.class);
                startActivity(it);
            }
        });

        buttonMS6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String senha = MyPass

                AlertDialog.Builder dialog = new AlertDialog.Builder(MainBCAPP.this);
                //dialog.setTitle("BANCO DE DADOS PESSOAL");
                dialog.setTitle("MAIN PAYMAIL");
                dialog.setMessage("Insert your paymail:");

                /////////////////////////////////////////////////////
                //PIN
                /////////////////////////////////////////////////////
                final EditText ETdialog_PIN;
                ETdialog_PIN = new EditText(MainBCAPP.this);
                LinearLayout layout = new LinearLayout(MainBCAPP.this);
                layout.setOrientation(LinearLayout.VERTICAL);
                ETdialog_PIN.setHint("paymail@moneybutton.com:");
                //ETdialog_PIN.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD); // phone 3
                ETdialog_PIN.setInputType(InputType.TYPE_CLASS_TEXT); // phone 3
                layout.addView(ETdialog_PIN);
                dialog.setView(layout);
                /////////////////////////////////////////////////////
                /////////////////////////////////////////////////////

                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Variables.MainPaymail = ETdialog_PIN.getText().toString();
                    }
                });
                dialog.create();
                dialog.show();
            }
        });
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