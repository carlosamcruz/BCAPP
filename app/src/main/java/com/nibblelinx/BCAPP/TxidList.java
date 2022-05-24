package com.nibblelinx.BCAPP;

import android.app.Activity;
import android.net.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.os.AsyncTask;
import android.view.View;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

//https://stackoverflow.com/questions/33229869/get-json-data-from-url-using-android

public class TxidList extends AppCompatActivity {

    ListView lista;
    TxidDB banco;

    //Notes dbcont;
    Boolean AddSend = true;

    /////////////////////////////////////
    // BUSCA NA Whatsonchain
    /////////////////////////////////////
    String jsonStr = "";
    String myURL = "";
    String searchStr = "\"tx_hash\":\"";
    String urlBaseAddress = "https://api.whatsonchain.com/v1/bsv/main/address/";

    private String myBSVaddress;
    private String BSVAdddressSend;
    private String BSVAdddressReceive;

    int lastLength = 0;
    int lastLengthSend = 0;
    int lastLengthReceive = 0;
    int numberOfTxid = 0;

    Timer timer = new Timer();

    static Boolean TimerRun = true;

    Boolean netRunFirst = true;

    /////////////////////////////////////
    /////////////////////////////////////

    //https://stackoverflow.com/questions/10379134/finish-an-activity-from-another-activity
    public static Activity fa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_txidlist);
        banco = new TxidDB(TxidList.this);
        //dbcont = new Notes(TxidList.this);


        //O verficador de pausa deve ser modificado em OnResume e OnCreate de cada Activity
        Variables.activityPause = false;
        //O contador de pausa deve estar presente em OnResume e OnCreate de cada Activity
        //Atenção especial para o uso de MAXSPECIALPAUSETIME
        Variables.timeCounter = Variables.MAXPAUSETIME;
        //O contador de Interação com Usuário deve também estar presente em OnResume e em OnCreate de cada activity
        //O contador deve ser resetado em OnResume, OnCreate, onUserInteraction de cada Activity
        Variables.userInteractionAct = Variables.MAXNOINTERACTIONTIME;

        //myBSVaddress = dbcont.ContactWallet(dbcont.MainUserName());
        //Carteira de envio de dados

        //Keygen BSVWallet = new Keygen();
        //BSVAdddressSend = dbcont.ContactWallet(contName);

        BSVAdddressSend = getIntent().getExtras().getString("NFTIndex");
        BSVAdddressReceive = BSVAdddressSend;
        myBSVaddress = BSVAdddressSend;

        lista = (ListView)findViewById(R.id.LV_TXID_List);
        //  lista2 = (ListView)findViewById(R.id.LV_List_Cont);

        //Rolar a lista para o fim;
        //https://stackoverflow.com/questions/3606530/listview-scroll-to-the-end-of-the-list-after-updating-the-list
        lista.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        lista.setStackFromBottom(true);

        lista.setAdapter(banco.ReadContacts(BSVAdddressSend, BSVAdddressReceive));

        lista.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                //https://stackoverflow.com/questions/13988761/how-to-get-string-from-listview
                String list =  lista.getItemAtPosition(position).toString();

                //Função de Envio
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, list.substring(list.indexOf(':') + 2));
                sendIntent.setType("text/plain");

                /////////////////////////////////////////////////
                //MAXSPECIALPAUSETIME é para uso quando o Usuário sair do contexto da aplicação
                //para executar uma atividade relacionada com a aplicação
                //como ele pode não retornar, é bom fechar tudo
                //contador de retorno para o contexto da aplicação
                //Variables.timeCounter = 10;

                //O contador de pausa deve estar presente em OnResume e OnCreate de cada Activity
                //Atenção especial para o uso de MAXSPECIALPAUSETIME
                Variables.timeCounter = Variables.MAXSPECIALPAUSETIME;

                //////////////////////////////////////////////////

                startActivity(Intent.createChooser(sendIntent, "Enviar TXID:"));
                return true;
            }
        });


        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                String list =  lista.getItemAtPosition(position).toString();
                Intent it = new Intent(TxidList.this,NFTOPReturn.class);

                it.putExtra("TXID", list.substring(list.indexOf(':') + 2));

                Variables.LastTXID =  list.substring(list.indexOf(':') + 2);
                Variables.LastTxHexData = "";

                TimerRun = false;

                startActivity(it);
            }
        });

        myURL = urlBaseAddress + myBSVaddress + "/history";
        timer.schedule(new TimeCheckURL(), 0, 5000);

    }

    //Executa depois que o JSON eh extraido
    //public String urlProccess(String urlContent, Boolean Flag)
    //private String urlProccess(String urlContent, Boolean Flag)
    private void urlProccess(String urlContent, Boolean Flag)
    {
        jsonStr = urlContent;
        int firstIndiceOf;
        int lastIndiceOf;
/*
        int contI = 0;
        int contIant = 0;
        int contIantAnt = 0;
        int contFirst = 0;
        int [] contCheck = {0, 0};
*/
        if(myBSVaddress.compareTo(BSVAdddressSend) == 0) lastLength = lastLengthSend;
        else lastLength = lastLengthReceive;

        String lastTXIDSEQ = "";

        //Registrar no DB este tamanho;
        if(Flag)
        {
            //Toast.makeText(TxidList.this, "Length: " + lastLength + "\n" + jsonStr, Toast.LENGTH_SHORT).show();
            if (lastLength != jsonStr.length())
            //if (lastJsonStr.compareTo(jsonStr) != 0)
            {
                //jsonStrLast = jsonStr;
                String Txid;

                //https://www.geeksforgeeks.org/searching-for-character-and-substring-in-a-string/
                firstIndiceOf = jsonStr.indexOf(searchStr);
                lastIndiceOf = jsonStr.lastIndexOf(searchStr);

                if((firstIndiceOf == -1) || (lastIndiceOf == -1))
                {
                    //Toast.makeText(TxidList.this, "NO DATA IN: " + myBSVaddress, Toast.LENGTH_SHORT).show();
                    if(AddSend) {
                        myBSVaddress = BSVAdddressReceive;
                        AddSend = false;
                    }
                    else
                    {
                        myBSVaddress = BSVAdddressSend;
                        AddSend = true;
                    }
                    myURL = urlBaseAddress + myBSVaddress + "/history";
                    return;
                    //return jsonStr;
                }

                int i = 1;
                //position = "Posicao " + i + ": " + firstIndiceOf + "\n";

                //https://beginnersbook.com/2013/12/java-string-substring-method-example/
                //TXID tem sempre 64 characteres 256 bits
                Txid = jsonStr.substring(firstIndiceOf + searchStr.length(), firstIndiceOf + searchStr.length() + 64);


                if (!banco.isAgain(Txid)) {
                    //banco.Update(Txid, Wallet);
                    //banco.Update(Txid, myBSVaddress);
                    banco.Updatex(Txid, myBSVaddress);
                }


                while (firstIndiceOf < lastIndiceOf) {
                    i++;
                    firstIndiceOf = jsonStr.indexOf(searchStr, firstIndiceOf + 1);
                    //position += "Posicao " + i + ": " + firstIndiceOf + "\n";

                    Txid = jsonStr.substring(firstIndiceOf + searchStr.length(), firstIndiceOf + searchStr.length() + 64);

                    if (!banco.isAgain(Txid)) {
                         banco.Updatex(Txid, myBSVaddress);
                    }


                    lastTXIDSEQ = Txid;
                }
                numberOfTxid = i;


                lista.setAdapter(banco.ReadContacts(BSVAdddressSend, BSVAdddressReceive));

            }

            if(myBSVaddress.compareTo(BSVAdddressSend) == 0) lastLengthSend = jsonStr.length();
            else lastLengthReceive = jsonStr.length();


            netRunFirst = false;

            Toast.makeText(TxidList.this, "Connected", Toast.LENGTH_SHORT).show();
        }
        else
        {
            if(netRunFirst) {
                lista.setAdapter(banco.ReadContacts(BSVAdddressSend, BSVAdddressReceive));
                netRunFirst = false;
            }
            //Toast.makeText(TxidList.this, myNet, Toast.LENGTH_SHORT).show();
            Toast.makeText(TxidList.this, "Bad or no Conection", Toast.LENGTH_SHORT).show();
        }


        if(AddSend) {
            myBSVaddress = BSVAdddressReceive;
            AddSend = false;
        }
        else
        {
            myBSVaddress = BSVAdddressSend;
            AddSend = true;
        }
        myURL = urlBaseAddress + myBSVaddress + "/history";

        //return jsonStr;
        return;
    }


    //NESTED CLASS: nao precisa colocar fora
    //Inner Class: https://www.tutorialspoint.com/java/java_innerclasses.htm
    //https://stackoverflow.com/questions/12908412/print-hello-world-every-x-seconds
    private String resultSTR;

    class TimeCheckURL extends TimerTask
    {
        public void run()
        {
            if(TimerRun) {
                //if(netOn) {
                if (onNetwork()) {
                    //if(false) {
                    //Manda para execucao e so pode ser verficiado quando esta task finaliar

                    //new JsonTask().execute(myURL);

                    resultSTR = null;
                    new JsonTask().onPreExecute();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            resultSTR = new JsonTask().execute(myURL);

                            //Para a execução na thread pricipal
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    // do onPostExecute stuff
                                    new JsonTask().onPostExecute(resultSTR);
                                }
                            });
                        }
                    }).start();


                } else {
                    //new NoConnection().execute(myURL);

                    resultSTR = null;
                    new NoConnection().onPreExecute();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            resultSTR = new NoConnection().execute(myURL);

                            //Para a execução na thread pricipal
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    // do onPostExecute stuff
                                    new NoConnection().onPostExecute(resultSTR);
                                }
                            });
                        }
                    }).start();


                }
            }

        }
    }

    //https://stackoverflow.com/questions/4238921/detect-whether-there-is-an-internet-connection-available-on-android
    //https://stackoverflow.com/questions/37232927/app-crashes-when-no-internet-connection-is-available
    //https://stackoverflow.com/questions/32547006/connectivitymanager-getnetworkinfoint-deprecated

    //CheckNetwork network = new CheckNetwork(getApplicationContext());

    // Network Check
    //https://gist.github.com/PasanBhanu/730a32a9eeb180ec2950c172d54bb06a
    //https://gist.github.com/Abhinav1217/0ff6b39e70fa38379d61e85e09b49fe7

    //https://developer.android.com/reference/android/net/ConnectivityManager
    //https://developer.android.com/reference/android/net/ConnectivityManager#getActiveNetwork()
    //public void onNetwork()
    //public boolean onNetwork()
    private boolean onNetwork()
    {
        ConnectivityManager connectivityManager = getSystemService(ConnectivityManager.class);
        Network currentNetwork = connectivityManager.getActiveNetwork();


        if(currentNetwork == null)
            return false;
        else
            //netOn = true;
            //https://developer.android.com/reference/android/net/ConnectivityManager#isDefaultNetworkActive()
            //if(connectivityManager.isDefaultNetworkActive()) return true;
            //else return false;
            return true;
    }


    //NESTED CLASS: nao precisa colocar fora
    //Inner Class: https://www.tutorialspoint.com/java/java_innerclasses.htm
    // https://stackoverflow.com/questions/33229869/get-json-data-from-url-using-android
    private class JsonTask //extends AsyncTask<String, String, String>
    {
        protected void onPreExecute()
        {

            //super.onPreExecute();
        }

        //protected String doInBackground(String... params)
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

                    ///////////////////////////////////////////////////////////////////
                    //NAO FUNCIONA COLOCAR O CONTEXTO DA CLASSE EXTERNA
                    ///////////////////////////////////////////////////////////////////
                    //Toast.makeText(mContext, "> "+ line, Toast.LENGTH_LONG).show();
                    //Toast.makeText(MActJSON, "> "+ line, Toast.LENGTH_LONG).show();
                }

                return buffer.toString();
            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();

                //return null;
                //urlProccess("ABC");
            }
            catch (IOException e)
            {
                e.printStackTrace();
                //return null;
                //urlProccess("ABC");
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
                    //return null;
                }
                //urlProccess("ABC");
                //return null;
            }
            return null;
        }

        //@Override
        protected void onPostExecute(String result)
        {
            //super.onPostExecute(result);

            ///////////////////////////////////////////////////////////////////
            //VERIFICAR
            ///////////////////////////////////////////////////////////////////

            //AQUI ESTE METODO FUNCIONA
            //((EditText)findViewById(R.id.etvJsonItem)).setText(result);

            //Resposta precisa ser uma variavel global

            //jsonStr = result;

            //CHAMA A FUNCAO DE PROCESSAMENTO
            //https://stackoverflow.com/questions/2920525/how-to-check-a-string-against-null-in-java
            //if(result.equals(null)) urlProccess(jsonStr, false);
            if(result == null) urlProccess(jsonStr, false);
            else urlProccess(result, true);

            //lastLength = jsonStr.length();
        }
    }

    private class NoConnection //extends AsyncTask<String, String, String>
    {
        protected void onPreExecute()
        {

            //super.onPreExecute();
        }

        //protected String doInBackground(String... params)
        protected String execute(String... params)
        {
            try
            {
                if(netRunFirst) return "ABC";
                else return jsonStr;
            }
            finally { }
        }

        //@Override
        protected void onPostExecute(String result)
        {
            //super.onPostExecute(result);
            //CHAMA A FUNCAO DE PROCESSAMENTO
            urlProccess(result, false);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        timer.cancel();
        timer.purge();
        this.finish();
    }

    //https://stackoverflow.com/questions/4783960/call-method-when-home-button-pressed
    @Override
    public void onPause(){
        super.onPause();
        //Para o leitor de internet
        TimerRun = false;
        Variables.activityPause = true;
    }
    //https://stuff.mit.edu/afs/sipb/project/android/docs/training/basics/activity-lifecycle/pausing.html
    @Override
    public void onResume(){
        super.onResume();
        //Variables.activityPause = false;
        //Retorna o leitor de internet
        TimerRun = true;

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