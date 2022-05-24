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

public class TxVerify extends AppCompatActivity {

    Boolean state;
    private boolean UTFstr = false;

    //https://pt.stackoverflow.com/questions/76476/como-utilizo-o-progress-bar
    //private ProgressBar mProgressBar;
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

        //Variables.Payment = false;

        //mProgressBar = (ProgressBar) findViewById(R.id.progressBarST);
        //mProgressBar.setVisibility(View.INVISIBLE);

        state = false;

        ((TextView)findViewById(R.id.TV_TEXTST)).setText("TEXT DATA:");

        descInput = true;

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabST);

        //timerMB = new Timer();

        if(Variables.LastTxHexData.compareTo("")==0)
        {
            BsvTxCreation bsvTx = new BsvTxCreation();
            Variables.LastTxHexData = bsvTx.txHexRead(Variables.LastTXID);
        }

        ((EditText) findViewById(R.id.ET_TEXTOST)).setText(
                Variables.LastTXID + "\n\n" + Variables.LastTxHexData
        );


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                txVerify(Variables.LastTxHexData);
            }
        });

        if(Variables.LastTxHexData != null) {

            BsvTxOperations bsvTxOp = new BsvTxOperations();
            bsvTxOp.txID(Variables.LastTxHexData);

            if (bsvTxOp.TXID.compareTo(Variables.LastTXID) == 0)
                Toast.makeText(this, "TXID is OK", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, "TXID are different", Toast.LENGTH_SHORT).show();
        }
    }

    private void txVerify(String txToVerify)
    {
        BsvTxCreation bsvTx = new BsvTxCreation();

        if(bsvTx.txVerify(txToVerify))
            Toast.makeText(this, "Valid TX", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, "Invalid TX", Toast.LENGTH_SHORT).show();

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
