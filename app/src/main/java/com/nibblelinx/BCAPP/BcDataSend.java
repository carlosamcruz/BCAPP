package com.nibblelinx.BCAPP;

public class BcDataSend {

    public String dataSend(String newData) {

        String text = newData;


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

        return sendTX(newTextChar);
        //return "test 1";
    }



    public String sendTX(byte[] newTextChar)
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

            //Toast.makeText(DataSendRead.this, "No Data!!!"
            //        , Toast.LENGTH_LONG).show();
            return "No Data!!!";
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

        return "Result: " + result;

        //((EditText) findViewById(R.id.ET_TEXTOST)).setText(result);
    }
}
