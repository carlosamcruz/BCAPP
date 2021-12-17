package com.nibblelinx.BCAPP;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.ArrayAdapter;
import android.widget.Toast;

public class TxidDB extends SQLiteOpenHelper {

    private final static String Nome_DB = "TXIDLIST";
    private final static int Versao_DB = 2;
    private final static String Endereco_DB = "/data/user/0/com.nibblelinx.bcapp/databases/TXIDLIST";

    private Context context_DB;
    private SQLiteDatabase dbRead,dbWrite;

    @SuppressLint("WrongConstant")
    private void abri(){
        if (!dbWrite.isOpen()) dbWrite = context_DB.openOrCreateDatabase(Endereco_DB,SQLiteDatabase.OPEN_READWRITE,null);
        if (!dbRead.isOpen())  dbRead = context_DB.openOrCreateDatabase(Endereco_DB,SQLiteDatabase.OPEN_READWRITE,null);
    }

    private void fechar(){
        if (dbWrite.isOpen()) dbWrite.close();
        if (dbRead.isOpen())  dbRead.close();
    }


    public TxidDB(Context context) {
        super(context, Nome_DB, null, Versao_DB);
        this.context_DB = context;
        dbWrite = getWritableDatabase();
        dbRead = getReadableDatabase();
    }

    public void Updatex(String nome,String num){
        abri();
        try {
            ContentValues cv = new ContentValues();
            cv.put("NAME",nome); //TXID
            cv.put("NUM",num); //ADDRESS
            dbWrite.insert(Nome_DB,null,cv);

        }catch (Exception e){
            Toast.makeText(context_DB,"ERRO: BD.update",Toast.LENGTH_SHORT).show();
        }finally {
            fechar();
        }
    }

    public void ReplaceH3x(String oldname, String oldnum, String nome,String num){
        abri();
        try {
            ContentValues cv = new ContentValues();
            ContentValues initialValues = new ContentValues();
            cv.put("NAME",nome);
            cv.put("NUM",num);

            initialValues.put("NAME",oldname);
            initialValues.put("NUM",oldnum);

            dbWrite.update(Nome_DB, cv,"NAME=?", new String[] {oldname});


            //dbWrite.
            //dbWrite.replace(Nome_DB,null,cv);
            //if(oldname.compareTo(nome) == 0){
            //dbWrite.update(Nome_DB, initialValues,null, new String[] {nome, num});

            //if(oldname == nome){

            /*
            if(oldname.compareTo(nome) == 0){
                //dbWrite.update(Nome_DB, initialValues,"NUM", new String[] {num});
                dbWrite.update(Nome_DB, cv,"NUM=?", new String[] {oldnum});
            }else if(oldnum.compareTo(num) == 0)
                //}else if(oldnum == num)
                //dbWrite.update(Nome_DB, initialValues,"NAME", new String[] {nome});
                dbWrite.update(Nome_DB, cv,"NAME=?", new String[] {oldname});
            else {
                dbWrite.update(Nome_DB, cv,"NUM=?", new String[] {oldnum});
                dbWrite.update(Nome_DB, cv,"NAME=?", new String[] {oldname});
            }

            */


        }catch (Exception e){
            Toast.makeText(context_DB,"ERRO: BD.update",Toast.LENGTH_SHORT).show();
        }finally {
            fechar();
        }
    }

    public void Replace(String oldname, String oldnum, int position, String nome,String num){
        abri();
        try {
            ContentValues cv = new ContentValues();
            ContentValues initialValues = new ContentValues();
            cv.put("NAME",nome);
            cv.put("NUM",num);

            initialValues.put("NAME",oldname);
            initialValues.put("NUM",oldnum);

            //dbWrite.
            //dbWrite.replace(Nome_DB,null,cv);
            //if(oldname.compareTo(nome) == 0){
            //dbWrite.update(Nome_DB, initialValues,null, new String[] {nome, num});

            //if(oldname == nome){
            if(oldname.compareTo(nome) == 0){
                //dbWrite.update(Nome_DB, initialValues,"NUM", new String[] {num});
                dbWrite.update(Nome_DB, cv,"NUM=?", new String[] {oldnum});
            }else if(oldnum.compareTo(num) == 0)
                //}else if(oldnum == num)
                //dbWrite.update(Nome_DB, initialValues,"NAME", new String[] {nome});
                dbWrite.update(Nome_DB, cv,"NAME=?", new String[] {oldname});
            else {
                dbWrite.update(Nome_DB, cv,"NUM=?", new String[] {oldnum});
                dbWrite.update(Nome_DB, cv,"NAME=?", new String[] {oldname});
            }


        }catch (Exception e){
            Toast.makeText(context_DB,"ERRO: BD.update",Toast.LENGTH_SHORT).show();
        }finally {
            fechar();
        }
    }

    public void Delete(String nome){
        abri();
        try {
            //ContentValues cv = new ContentValues();
            //ContentValues initialValues = new ContentValues();
            //cv.put("NAME",nome);
            //cv.put("NUM",num);

            dbWrite.delete(Nome_DB,"NAME=?", new String[] {nome});

        }catch (Exception e){
            Toast.makeText(context_DB,"ERRO: BD.update",Toast.LENGTH_SHORT).show();
        }finally {
            fechar();
        }
    }


    public String UsersCode(String user){
        abri();
        String ret = "";
        try {
            Cursor c = dbRead.rawQuery("SELECT * FROM "+Nome_DB+" WHERE NAME='" + user + "'",null);
            if(c.moveToNext()) ret = c.getString(1);
        }catch (Exception e){
            Toast.makeText(context_DB,"ERRO: BD.read",Toast.LENGTH_SHORT).show();
        }finally {
            fechar();
            return ret;
        }
    }


    public String LastH3(String WALLET){
        abri();
        String ret = "";
        try {
            //Cursor c = dbRead.rawQuery("SELECT * FROM "+Nome_DB+" WHERE NAME='" + user + "'",null);

            Cursor c = dbRead.rawQuery("SELECT * FROM "+Nome_DB,null);
            //if(c.moveToNext()) ret = c.getString(1);


            if (c.moveToFirst()) {
                do {

                    if((WALLET.contains(c.getString(1))))
                    {
                        ret = c.getString(0);
                        break;
                    }

                }while (c.moveToNext());
            }

        }catch (Exception e){
            Toast.makeText(context_DB,"ERRO: BD.read",Toast.LENGTH_SHORT).show();
        }finally {
            fechar();
            return ret;
        }
    }

    public String FriendWallet(String TXID){
        abri();
        String ret = "";
        try {
            //Cursor c = dbRead.rawQuery("SELECT * FROM "+Nome_DB+" WHERE NAME='" + user + "'",null);

            Cursor c = dbRead.rawQuery("SELECT * FROM "+Nome_DB,null);
            //if(c.moveToNext()) ret = c.getString(1);


            if (c.moveToFirst()) {
                do {

                    if((TXID.contains(c.getString(0)))) ret = c.getString(1);

                }while (c.moveToNext());
            }



        }catch (Exception e){
            Toast.makeText(context_DB,"ERRO: BD.read",Toast.LENGTH_SHORT).show();
        }finally {
            fechar();
            return ret;
        }
    }


    public ArrayAdapter<String> ReadContacts(String AddSent, String AddReceived){
        abri();
        ArrayAdapter <String> total = new ArrayAdapter<String>(context_DB, android.R.layout.simple_list_item_1);

        String testBreak = "this";
        Variables.TotalList = 0;

        try {
            int iS = 0;
            int iR = 0;


            //Cursor c = dbRead.rawQuery("SELECT * FROM "+Nome_DB,null);
            Cursor c = dbRead.rawQuery("SELECT * FROM "+Nome_DB,null);
            if (c.moveToFirst()) {
                //do total.add(c.getString(0));
                do {

                    Variables.TotalList++;

                    if(AddSent.compareTo(c.getString(1))==0)
                        //total.add("TXID-S-" + String.valueOf(iS++) + ": " + c.getString(0));
                        //total.add("->" + String.valueOf(iS++) + ": " + c.getString(0));
                        total.add("[>>]" + ": " + c.getString(0));
                    else
                        if(AddReceived.compareTo(c.getString(1))==0)
                            //total.add("TXID-R-" + String.valueOf(iR++) + ": " + c.getString(0));
                            //total.add("<-" + String.valueOf(iR++) + ": " + c.getString(0));
                            total.add("[<<]"  + ": " + c.getString(0));

                    /*
                    ///////////////////////////////////
                    //Teste de quebra
                    ///////////////////////////////////
                    iS ++;
                    if(iS == 95) break; // em 95 o DB quebrou
                    ///////////////////////////////////
                    ///////////////////////////////////
                    testBreak = c.getString(0) + " " + c.getString(1);

                    */
                }
                while (c.moveToNext());

                //testBreak = c.getString(0) + " " + c.getString(1);
            }
        }catch (Exception e){
            Toast.makeText(context_DB,"ERRO: BD.read",Toast.LENGTH_SHORT).show();
            //Toast.makeText(context_DB,"ERRO: BD.read " + testBreak,Toast.LENGTH_SHORT).show();
        }finally {
            fechar();
        }
        fechar();
        return total;
    }
/*
    public void DeleteAll(){
        abri();
        //ArrayAdapter <String> total = new ArrayAdapter<String>(context_DB, android.R.layout.simple_list_item_1);

        String testBreak = "this";

        try {
            int iS = 0;
            int iR = 0;
            //Cursor c = dbRead.rawQuery("SELECT * FROM "+Nome_DB,null);
            Cursor c = dbRead.rawQuery("SELECT * FROM "+Nome_DB,null);
            if (c.moveToFirst()) {
                //do total.add(c.getString(0));
                do {

                    try {
                        //ContentValues cv = new ContentValues();
                        //ContentValues initialValues = new ContentValues();
                        //cv.put("NAME",nome);
                        //cv.put("NUM",num);

                        dbWrite.delete(Nome_DB,"NAME=?", new String[] {c.getString(0)});

                    }catch (Exception e){
                        Toast.makeText(context_DB,"ERRO: BD.update",Toast.LENGTH_SHORT).show();
                    }finally {
                        fechar();
                    }




                }
                while (c.moveToNext());

                //testBreak = c.getString(0) + " " + c.getString(1);
            }
        }catch (Exception e){
            Toast.makeText(context_DB,"ERRO: BD.read",Toast.LENGTH_SHORT).show();
            //Toast.makeText(context_DB,"ERRO: BD.read " + testBreak,Toast.LENGTH_SHORT).show();
        }finally {
            fechar();
            //return total;
        }
    }
*/



    public String LastTXID(){
        abri();
        String lastTXID = new String();
        lastTXID = null;
        ArrayAdapter <String> total = new ArrayAdapter<String>(context_DB, android.R.layout.simple_list_item_1);
        try {
            int i = 0;
            //Cursor c = dbRead.rawQuery("SELECT * FROM "+Nome_DB,null);
            Cursor c = dbRead.rawQuery("SELECT * FROM "+Nome_DB,null);
            if (c.moveToFirst()) {
                //do total.add(c.getString(0));
                do {
                    total.add("TXID-" + String.valueOf(i++) + ": " + c.getString(0));
                    lastTXID = (c.getString(0));
                    lastTXID = lastTXID.substring(lastTXID.indexOf(':') + 1);
                }
                while (c.moveToNext());
            }
        }catch (Exception e){
            //Toast.makeText(context_DB,"ERRO: BD.read",Toast.LENGTH_SHORT).show();
            return lastTXID;
        }finally {
            fechar();
            return lastTXID;
        }
    }


    public int ReadNumberOfElements(){
        abri();
        int number = 0;
        //ArrayAdapter <String> total = new ArrayAdapter<String>(context_DB, android.R.layout.simple_list_item_1);
        try {
            int i = 0;
            //Cursor c = dbRead.rawQuery("SELECT * FROM "+Nome_DB,null);
            Cursor c = dbRead.rawQuery("SELECT * FROM "+Nome_DB,null);
            if (c.moveToFirst()) {
                //do total.add(c.getString(0));
                do {
                    //total.add("TXID-" + String.valueOf(i++) + ": " + c.getString(0));
                    number++;
                }
                while (c.moveToNext());
            }
        }catch (Exception e){
            Toast.makeText(context_DB,"ERRO: BD.read",Toast.LENGTH_SHORT).show();
        }finally {
            fechar();

            return number;
        }
    }



    public int GetPsition(String TXID){
        abri();
        int position = 0;

        //Toast.makeText(context_DB,"TXID: "+TXID,Toast.LENGTH_SHORT).show();

        //Toast.makeText(context_DB,TXID,Toast.LENGTH_SHORT).show();

        //ArrayAdapter <String> total = new ArrayAdapter<String>(context_DB, android.R.layout.simple_list_item_1);
        try {
            Cursor c = dbRead.rawQuery("SELECT * FROM "+Nome_DB,null);
            //Cursor c = dbRead.rawQuery("SELECT * FROM "+Nome_DB,null);
            //Cursor c = dbRead.rawQuery("SELECT * FROM "+Nome_DB+" WHERE NAME='" + TXID + "'",null);
            //Cursor c = dbRead.rawQuery("SELECT * FROM "+Nome_DB+" WHERE NAME=" + TXID,null);
            //Cursor c = dbRead.rawQuery("SELECT * FROM "+ TXID,null);
            //if(c.moveToFirst()) position = c.getPosition();

            //Cursor c = dbRead.rawQuery("SELECT * FROM "+Nome_DB,null);
            if (c.moveToFirst()) {
                do {

                    if((TXID.contains(c.getString(0)))) position = c.getPosition();

                }while (c.moveToNext());
            }

            //return position;
        }catch (Exception e){
            Toast.makeText(context_DB,"ERRO: BD.read",Toast.LENGTH_SHORT).show();
        }finally {
            fechar();
            return (position);
        }
    }


    public String GetElement(int Position){
        abri();
        String result = "";

        Cursor c = dbRead.rawQuery("SELECT * FROM "+Nome_DB,null);
        try {
            //Cursor c = dbRead.rawQuery("SELECT * FROM "+Nome_DB,null);
            //Cursor c = dbRead.rawQuery("SELECT * FROM "+Nome_DB,null);
            //Cursor c = dbRead.rawQuery("SELECT * FROM "+Nome_DB+" WHERE NAME='" + TXID + "'",null);
            //Cursor c = dbRead.rawQuery("SELECT * FROM "+Nome_DB+" WHERE NAME=" + TXID,null);
            //Cursor c = dbRead.rawQuery("SELECT * FROM "+ TXID,null);
            //if(c.moveToFirst()) position = c.getPosition();

            //Cursor c = dbRead.rawQuery("SELECT * FROM "+Nome_DB,null);
            if (c.moveToFirst()) {
                int i = 0;

                do {
                    if(i == (Position -1))
                    {
                        result = c.getString(0) + "&" + c.getString(1);
                        break;
                    }
                    i++;

                    //if((TXID.contains(c.getString(0)))) position = c.getPosition();

                }while (c.moveToNext());
            }

            //return position;
        }catch (Exception e){
            Toast.makeText(context_DB,"ERRO: BD.read",Toast.LENGTH_SHORT).show();
        }finally {
            fechar();
            //return (position);
        }
        fechar();
        return (result);
    }



    public String MainUserPubKey(){
        abri();
        String text = null;
        try {
            Cursor c = dbRead.rawQuery("SELECT * FROM "+Nome_DB,null);
            c.moveToFirst();
            text = c.getString(1);
        }catch (Exception e) {Toast.makeText(context_DB,"ERRO: BD.read",Toast.LENGTH_SHORT).show();}
        fechar();
        return text;
    }

    public String MainUserName(){
        abri();
        String text = null;
        try {
            Cursor c = dbRead.rawQuery("SELECT * FROM "+Nome_DB,null);
            c.moveToFirst();
            text = c.getString(0);
        }catch (Exception e) {Toast.makeText(context_DB,"ERRO: BD.read",Toast.LENGTH_SHORT).show();}
        fechar();
        return text;
    }


    public Boolean isUserOK(){
        abri();
        Boolean test = true;
        try {
            Cursor c = dbRead.rawQuery("SELECT * FROM "+Nome_DB,null);
            test = c.moveToFirst();
        }catch (Exception e){
            Toast.makeText(context_DB,"ERRO: BD.read",Toast.LENGTH_SHORT).show();
        }finally {
            fechar();
            return test;
        }
    }

    public Boolean isAgain(String input){
        abri();
        Boolean test = false;
        try {
            Cursor c = dbRead.rawQuery("SELECT * FROM "+Nome_DB,null);
            if (c.moveToFirst()) {
                do {

                    if(input.compareTo(c.getString(0))==0) {
                        test = true;
                        break;
                    }

                    //test = test || ((input.contains(c.getString(0))) && (input.length() == (c.getString(0)).length()));
                }while (c.moveToNext());
            }
        }catch (Exception e){
            Toast.makeText(context_DB,"ERRO: BD.read",Toast.LENGTH_SHORT).show();
            fechar();
        }finally {
            fechar();
            //return test;
        }
        fechar();
        return test;
    }

    public int isAgainInt(String input){
        abri();
        int test = 0;
        boolean collision = false;
        try {
            Cursor c = dbRead.rawQuery("SELECT * FROM "+Nome_DB,null);
            if (c.moveToFirst()) {
                do {
                    test++;

                    if(input.compareTo(c.getString(0))==0) {
                        collision = true;
                        break;
                    }

                    //test = test || ((input.contains(c.getString(0))) && (input.length() == (c.getString(0)).length()));
                }while (c.moveToNext());

                if(!collision)
                {
                    test = -1;
                }

            }
        }catch (Exception e){
            Toast.makeText(context_DB,"ERRO: BD.read",Toast.LENGTH_SHORT).show();
            fechar();
        }finally {
            fechar();
        }
        fechar();
        return test;

    }

    public int [] isAgainInt2(String input, String Wallet){
        abri();
        int test [] = {0, 0};
        boolean collision = false;
        try {
            Cursor c = dbRead.rawQuery("SELECT * FROM "+Nome_DB,null);
            if (c.moveToFirst()) {
                do {
                    test[1]++;

                    if(input.compareTo(c.getString(0))==0) {
                        collision = true;
                        break;
                    }

                    if(Wallet.compareTo(c.getString(1))!=0)
                    {
                        test[0] = test[1];
                        break;
                    }

                    //test = test || ((input.contains(c.getString(0))) && (input.length() == (c.getString(0)).length()));
                }while (c.moveToNext());

                if(!collision)
                {
                    test[1] = -1;
                }
            }
        }catch (Exception e){
            Toast.makeText(context_DB,"ERRO: BD.read",Toast.LENGTH_SHORT).show();
            fechar();
        }finally {
            fechar();
        }
        fechar();
        return test;
    }


    public Boolean isAgain02(String input){
        abri();
        Boolean test = false;
        try {
            Cursor c = dbRead.rawQuery("SELECT * FROM "+Nome_DB,null);
            if (c.moveToFirst()) {
                do {
                    test = test || ((input.contains(c.getString(1))) && (input.length() == (c.getString(1)).length()));
                }while (c.moveToNext());
            }
        }catch (Exception e){
            Toast.makeText(context_DB,"ERRO: BD.read",Toast.LENGTH_SHORT).show();
        }finally {
            fechar();
            return test;
        }
    }



    public Boolean isKeyAgain(String input){
        abri();
        Boolean test = false;
        try {
            Cursor c = dbRead.rawQuery("SELECT * FROM "+Nome_DB,null);
            if (c.moveToFirst()) {
                do {
                    test = test || ((input.contains(c.getString(1))) && (input.length() == (c.getString(1)).length()));
                }while (c.moveToNext());
            }
        }catch (Exception e){
            Toast.makeText(context_DB,"ERRO: BD.read",Toast.LENGTH_SHORT).show();
        }finally {
            fechar();
            return test;
        }
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS "+Nome_DB+" (NAME TEXT, NUM TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+Nome_DB);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+Nome_DB);
        onCreate(db);
    }
}
