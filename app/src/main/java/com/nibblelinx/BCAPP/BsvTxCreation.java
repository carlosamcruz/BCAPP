package com.nibblelinx.BCAPP;

import java.util.Timer;

public class BsvTxCreation {

    public BsvTxOperations bsvTX = new BsvTxOperations();

    public String NewTxHexData = "";

///////////////////////////////////////////////////////////////////////////////
// Verifica a quantidade de satoshis que uma determinada carteira
// possui para gastar;
///////////////////////////////////////////////////////////////////////////////

    public  String totalUnspent (String BSVADD)
    {
        /////////////////////////////////////////////////////////////////////
        //Unspent TX treatment do Endereço da Chave privada Indicada pelo Usruário
        /////////////////////////////////////////////////////////////////////

        bsvTX.unsPentInputs = null;
        //bsvTX.timer = new Timer();
        bsvTX.readBsvAddsUnspent(BSVADD);
        String unspentTX = "";

        //Primeiro

        while (bsvTX.threadreadBsvAddsUnspent.isAlive())
        {
            unspentTX = "";
        }

        //if(bsvTX.unsPentInputs == null)
        //    return ("erro");

        unspentTX = bsvTX.unsPentInputs;
        //bsvTX.timer.cancel();
        //bsvTX.timer.purge();

        if(bsvTX.unsPentInputs == null)
            //return  "Error: Time out reading Unspent TX inputs" + " " + BSVADD;
            return  "Error: reading Unspent TX inputs fail";


        /////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////

        int nInp = bsvTX.unspentUTXO(unspentTX);
        long totalValue = 0;
        String totalValueHex = "";

        //String unspentParts = "";

        /////////////////////////////////////////////////////////////////////
        //Verificação de quanto tem para gastar
        /////////////////////////////////////////////////////////////////////
        for (int i =0; i < nInp; i++)
        {
            //unspentParts += bsvTX.unspentIndex[i] + "\n" + bsvTX.unspentTXID[i] + "\n" + bsvTX.unspentValue[i] + "\n";
            //totalValue +=  Integer.decode(bsvTX.unspentValue[i]);
            totalValue +=  Long.valueOf(bsvTX.unspentValue[i]);

        }

        //totalValueHex = Long.toHexString(totalValue);
        return Long.toString(totalValue);
    }
///////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////

///////////////////////////////////////////////////////////////////////////////
// Cria uma transação e envia para a rede BSV tipo ForkID 41
///////////////////////////////////////////////////////////////////////////////

    String TXID = "";

    //public String buildAndBroadCast (String PVTKEY, int nOfOutputs, String [] PayWallets, String [] PayValues,
    public String txBuilder (String PVTKEY, Boolean CompPKey, int nOfOutputs, String [] PayWallets, String [] PayValues,
                                     String [] OP_RETURNs, int numberOfOPRETURNS)
    {
        NewTxHexData = "";

        //DEBUG
        //if(PVTKEY != null)
        //    return "Error 1";

        //////////////////////////////////////////////////////////////////////////////////////////////////
        //Preparação das Chaves
        //////////////////////////////////////////////////////////////////////////////////////////////////
        Keygen pubKey = new Keygen();
        EcdsaSecretus ECDSA = new EcdsaSecretus();
        //PUB KEY from PVT Key
        //PVT key
        //String PUBKEY = pubKey.publicKey(PVTKEY);

        //Boolean CompPKey = false;

        String PUBKEY = pubKey.publicKeyHEX(PVTKEY);
        String BSV160 = pubKey.bsvWalletRMD160(PUBKEY, CompPKey);
        String BSVADD = pubKey.bsvWalletFull(PUBKEY, CompPKey);
        String PUBKEYSEC;

        if(CompPKey)
            PUBKEYSEC =  pubKey.publicKeyCompSEC(PVTKEY);
        else
            PUBKEYSEC =  pubKey.publicKeyUncompSEC(PVTKEY);


        /////////////////////////////////////////////////////////////////////
        //Unspent TX treatment do Endereço da Chave privada Indicada pelo Usruário
        //////1///////////////////////////////////////////////////////////////

        String unspentTX = "";

        bsvTX.unsPentInputs = null;
        bsvTX.readBsvAddsUnspent(BSVADD);

        /*
        //Tratamento da quebra
        String waitHashing = "";
        waitHashing = SHA256G.SHA256STR("ABC");
        long count = 0;
        int flagFail = 0;

        */

        while (bsvTX.threadreadBsvAddsUnspent.isAlive())
        {
            unspentTX = "";
        }

        unspentTX = bsvTX.unsPentInputs;
        //bsvTX.timer.cancel();
        //bsvTX.timer.purge();

        if(bsvTX.unsPentInputs == null)
            return  "Error: Time out reading Unspent TX inputs";


        //if(bsvTX.unsPentInputs != null)
        //    return  "Error: " + unspentTX;


//////////Debug
        //unspentTX = "[{\"height\":748082,\"tx_pos\":1,\"tx_hash\":\"285ba37b6d402bac560929eaaa935b8e88aafa04206db5de1ed2731c24eaf66d\",\"value\":13698},{\"height\":748103,\"tx_pos\":0,\"tx_hash\":\"59a8155eb097e04715bd2f6920766b7a32fd0e2aa6d9b8bdf38f581c4cfd9fe4\",\"value\":1000},{\"height\":748132,\"tx_pos\":0,\"tx_hash\":\"6c4d93bd473fefc84bce4132e6294723913c564fd0a1c7d85ab9cce8a42bffc1\",\"value\":1000}]";

        /////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////

        int nInp = bsvTX.unspentUTXO(unspentTX);
        long totalValue = 0;
        String totalValueHex = "";

        //String unspentParts = "";

        /////////////////////////////////////////////////////////////////////
        //Verificação de quanto tem para gastar
        /////////////////////////////////////////////////////////////////////
        for (int i =0; i < nInp; i++)
        {
            //unspentParts += bsvTX.unspentIndex[i] + "\n" + bsvTX.unspentTXID[i] + "\n" + bsvTX.unspentValue[i] + "\n";
            //totalValue +=  Integer.decode(bsvTX.unspentValue[i]);
            totalValue +=  Long.valueOf(bsvTX.unspentValue[i]);

        }

        totalValueHex = Long.toHexString(totalValue);

        //Preparação do retorno da sobra para a carteira de origem
        //PayValues[1] = Long.toString(totalValue);
        //PayValues[(3-1)-1] = Long.toString(totalValue);
        PayValues[(nOfOutputs-numberOfOPRETURNS)-1] = Long.toString(totalValue);

        /////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////

        int SECsize = PUBKEYSEC.length() / 2;
        String SECsizeOut = Integer.toHexString(SECsize);

        /////////////////////////////////////////////////////////////////////
        //Confecção da String de Input
        /////////////////////////////////////////////////////////////////////
        String inputString = "";
        inputString =  bsvTX.inputPreString(nInp, SECsizeOut, PUBKEYSEC);
        /////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////

        String OutputString = "";
        String preTX = "";

        /////////////////////////////////////////////////////////////////////
        //Confecção da String de Output
        /////////////////////////////////////////////////////////////////////
        OutputString =  bsvTX.OutputString(nOfOutputs, PayWallets, PayValues, numberOfOPRETURNS, OP_RETURNs, inputString);

        //Verifica o que acontece quando o RETORNO for igual a zero, remover o ultimo output

        preTX = inputString + OutputString;
        /////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////


        //DEBUG
        //if(PVTKEY != null)
        //    return "Error 1";

        //if(OutputString != null)
        //    return  "Error: " + OutputString;
        /////////////////////////////////////////////////////////////////////
        //Confecção das preImagenS dos inputs
        /////////////////////////////////////////////////////////////////////

        //Delay da WhatsOnChain;
        //bsvTX.timer = new Timer();
        //bsvTX.timerCallWOC();

        String[] preimage = bsvTX.txPreImager41(preTX);

        //bsvTX.timer.cancel();
        //bsvTX.timer.purge();

        //if(preimage [0] != null)
          //     return  "Error: " + preimage [0] + preimage [1];

        //DEBUG
        //if(PVTKEY != null)
        //    return "Error 1 " + nInp;
        //nInp = 1;

        //Esta correção é necessária para o trabalho com SCRIPTs
        nInp = bsvTX.nOfPreImages41;

        //if(preimage[0].compareTo("Error 1") == 0  || preimage[0].compareTo("Error 2") == 0)
        if(preimage[0].length()>5)
            if(preimage[0].substring(0,5).compareTo("Error") == 0)
                return preimage[0];

        /////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////


        ////////////////////////////////////////////////////////////
        //Assinatura
        ////////////////////////////////////////////////////////////

        /////////////////////////////////////////////////////////
        //Chave de Encriptação após a Primeira
        //Neste Conjunto de testes somente este conjunto de Chaves é testada
        /////////////////////////////////////////////////////////

        String HA1, HA2, HA3, HA4;

        String[] signDER41 = new String[nInp];
        String[] DERsizeOut = new String[nInp];
        String[] inputScript = new String[nInp];
        String newTX = "";
        //String TXID = "";
        TXID = "";
        int[] DERsize = new int[nInp];
        String[] inputScriptSize = new String[nInp];

        /////////////////////////////////////////////////////////////////////
        //Confecção das Assinatura DER para cada input
        /////////////////////////////////////////////////////////////////////
        for(int i = 0; i < nInp; i++)
        {

            String lastTXID = bsvTX.unspentTXID[i];

            HA1 = SHA256G.SHA256bytes(SHA256G.HashStrToByte2(PVTKEY));
            HA2 = SHA256G.SHA256MsnHxHex2(SHA256G.HashStrToByte2(lastTXID), SHA256G.HashStrToByte2(HA1));
            HA3 = SHA256G.SHA256bytes(SHA256G.HashStrToByte2(HA2));

            //HA4 != H4 Elemento "Secreto" utilizado na Geração de H5 e também de K em ECDSA;
            HA4 = SHA256G.SHA256MsnHxHex2(SHA256G.HashStrToByte2(HA3), SHA256G.HashStrToByte2(HA2));

            //Tratar o caso de assinaturas grandes
            signDER41[i] = bsvTX.signEcdsaDER41(preimage[i], PVTKEY, HA4);


            DERsize[i] = signDER41[i].length() / 2;
            //int DERsizeHEX = DERsize;
            DERsizeOut[i] = Integer.toHexString(DERsize[i]);


            inputScriptSize[i] = Integer.toHexString(
                    (DERsizeOut[i] + signDER41[i] + SECsizeOut + PUBKEYSEC).length() / 2);

            inputScript[i] = inputScriptSize[i] +
                    DERsizeOut[i] + signDER41[i] +
                    SECsizeOut + PUBKEYSEC;
        }

        /////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////

        /////////////////////////////////////////////////////////////////////
        //Confecção string final de input com as assinaturas DER
        /////////////////////////////////////////////////////////////////////
        inputString =  bsvTX.inputPosString(nInp, SECsizeOut, PUBKEYSEC, inputScript);

        newTX = inputString + OutputString;

        NewTxHexData = newTX;

        return newTX;
        /////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////

    }

    //Nesta nova versão tratamos a criação de TX não padrão
    //  com OP_RETURN OP_TRUE e também o uso destes OP_RETUNRs
    //public String buildAndBroadCast (String PVTKEY, int nOfOutputs, String [] PayWallets, String [] PayValues,
    public String txBuilderV2 (String PVTKEY, Boolean CompPKey, int nOfOutputs, String [] PayWallets, String [] PayValues,
                             String [] OP_RETURNs, int numberOfOPRETURNS, int TXtype, String SCRIPTHASH)
    {
        //TXtype
        // 0 - TX normal
        // 1 - TX com OP_TRUE
        // 2 - TX com OP_DROP
        // 3 - TX uso de TX com OP_TRUE - preciso do TX_ID da transação com OP_TRUE ou do Script

        if(TXtype == 1 && numberOfOPRETURNS < 1)
            return "Error: Sem Dado";
        if(TXtype == 2 && numberOfOPRETURNS < 1)
            return "Error: Sem Dado";


        NewTxHexData = "";

        //DEBUG
        //if(PVTKEY != null)
        //    return "Error 1";

        //////////////////////////////////////////////////////////////////////////////////////////////////
        //Preparação das Chaves
        //////////////////////////////////////////////////////////////////////////////////////////////////
        Keygen pubKey = new Keygen();
        EcdsaSecretus ECDSA = new EcdsaSecretus();
        //PUB KEY from PVT Key
        //PVT key
        //String PUBKEY = pubKey.publicKey(PVTKEY);

        //Boolean CompPKey = false;

        String PUBKEY = pubKey.publicKeyHEX(PVTKEY);
        String BSV160 = pubKey.bsvWalletRMD160(PUBKEY, CompPKey);
        String BSVADD = pubKey.bsvWalletFull(PUBKEY, CompPKey);
        String PUBKEYSEC;

        if(CompPKey)
            PUBKEYSEC =  pubKey.publicKeyCompSEC(PVTKEY);
        else
            PUBKEYSEC =  pubKey.publicKeyUncompSEC(PVTKEY);


        /////////////////////////////////////////////////////////////////////
        //Unspent TX treatment do Endereço da Chave privada Indicada pelo Usruário
        //////1///////////////////////////////////////////////////////////////

        String unspentTX = "";



        bsvTX.unsPentInputs = null;

        if(TXtype < 3)
            bsvTX.readBsvAddsUnspent(BSVADD);
        else
            bsvTX.readBsvSCRIPTUnspent(SCRIPTHASH);

        /*
        //Tratamento da quebra
        String waitHashing = "";
        waitHashing = SHA256G.SHA256STR("ABC");
        long count = 0;
        int flagFail = 0;

        */

        while (bsvTX.threadreadBsvAddsUnspent.isAlive()) {
            unspentTX = "";
        }

        unspentTX = bsvTX.unsPentInputs;
        //bsvTX.timer.cancel();
        //bsvTX.timer.purge();

        if (bsvTX.unsPentInputs == null)
            return "Error: Time out reading Unspent TX inputs";


        //if(bsvTX.unsPentInputs != null)
         //   return  "Error: " + unspentTX;

        //Para Enviar Token
        if(TXtype == 4)
        {
            //return "Error: Time out reading Unspent TX inputs";

            bsvTX.renewThreadUnspent();

            bsvTX.unsPentInputs = null;
            bsvTX.readBsvAddsUnspent(BSVADD);

            while (bsvTX.threadreadBsvAddsUnspent.isAlive()) {
                //unspentTX = "";
            }



            unspentTX = unspentTX + bsvTX.unsPentInputs;
            //bsvTX.timer.cancel();
            //bsvTX.timer.purge();

            if (bsvTX.unsPentInputs == null)
                return "Error: Time out reading Unspent TX inputs";


            //return "Error: Time out reading Unspent TX inputs";

        }


//////////Debug
        //unspentTX = "[{\"height\":748082,\"tx_pos\":1,\"tx_hash\":\"285ba37b6d402bac560929eaaa935b8e88aafa04206db5de1ed2731c24eaf66d\",\"value\":13698},{\"height\":748103,\"tx_pos\":0,\"tx_hash\":\"59a8155eb097e04715bd2f6920766b7a32fd0e2aa6d9b8bdf38f581c4cfd9fe4\",\"value\":1000},{\"height\":748132,\"tx_pos\":0,\"tx_hash\":\"6c4d93bd473fefc84bce4132e6294723913c564fd0a1c7d85ab9cce8a42bffc1\",\"value\":1000}]";

        /////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////

        int nInp = bsvTX.unspentUTXO(unspentTX);
        long totalValue = 0;
        String totalValueHex = "";

        //String unspentParts = "";

        /////////////////////////////////////////////////////////////////////
        //Verificação de quanto tem para gastar
        /////////////////////////////////////////////////////////////////////
        for (int i =0; i < nInp; i++)
        {
            //unspentParts += bsvTX.unspentIndex[i] + "\n" + bsvTX.unspentTXID[i] + "\n" + bsvTX.unspentValue[i] + "\n";
            //totalValue +=  Integer.decode(bsvTX.unspentValue[i]);
            totalValue +=  Long.valueOf(bsvTX.unspentValue[i]);

        }

        totalValueHex = Long.toHexString(totalValue);

        //Preparação do retorno da sobra para a carteira de origem
        //PayValues[1] = Long.toString(totalValue);
        //PayValues[(3-1)-1] = Long.toString(totalValue);
        PayValues[(nOfOutputs-numberOfOPRETURNS)-1] = Long.toString(totalValue);

        /////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////

        int SECsize = PUBKEYSEC.length() / 2;
        String SECsizeOut = Integer.toHexString(SECsize);

        /////////////////////////////////////////////////////////////////////
        //Confecção da String de Input
        /////////////////////////////////////////////////////////////////////
        String inputString = "";
        if(TXtype < 3) {
            inputString = bsvTX.inputPreString(nInp, SECsizeOut, PUBKEYSEC);
        }else
            inputString = bsvTX.inputPreString(nInp, SECsizeOut, PUBKEYSEC);
        /////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////

        String OutputString = "";
        String preTX = "";

        /////////////////////////////////////////////////////////////////////
        //Confecção da String de Output
        /////////////////////////////////////////////////////////////////////
        if(TXtype == 0) {
            OutputString = bsvTX.OutputString(nOfOutputs, PayWallets, PayValues, numberOfOPRETURNS, OP_RETURNs, inputString);
        }
        if(TXtype == 1) {
            OutputString = bsvTX.OutputStringV2(nOfOutputs, PayWallets, PayValues, numberOfOPRETURNS, OP_RETURNs, inputString, TXtype);
        }

        if(TXtype == 2) {
            OutputString = bsvTX.OutputStringV2(nOfOutputs, PayWallets, PayValues, numberOfOPRETURNS, OP_RETURNs, inputString, TXtype);
        }

        if(TXtype == 3) {
            OutputString = bsvTX.OutputStringV3(nOfOutputs, PayWallets, PayValues, numberOfOPRETURNS, OP_RETURNs, inputString);
        }



        //Verificar o OP_RETURN
        if(TXtype == 4) {

            //Buscar o Script

            String txScript = txHexReadSCRIPT(bsvTX.unspentTXID[0]);

            if(txScript.substring(0,5).compareTo("Error") == 0)
                return txScript;

            PayValues[0] = bsvTX.unspentValue[0];


            //return "Error: Time out reading Unspent TX inputs";

            OutputString = bsvTX.OutputStringV4(nOfOutputs, PayWallets, PayValues, numberOfOPRETURNS, txScript, inputString, TXtype);

            //return inputString + OutputString;
        }

        //Verifica o que acontece quando o RETORNO for igual a zero, remover o ultimo output

        preTX = inputString + OutputString;


        //return "Error: Time out reading Unspent TX inputs";

        //return preTX;
        /////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////


        //DEBUG
        //if(PVTKEY != null)
        //    return "Error 1";

        //if(OutputString != null)
        //    return  "Error: " + OutputString;
        /////////////////////////////////////////////////////////////////////
        //Confecção das preImagenS dos inputs
        /////////////////////////////////////////////////////////////////////

        //Delay da WhatsOnChain;
        //bsvTX.timer = new Timer();
        //bsvTX.timerCallWOC();

        String[] preimage = bsvTX.txPreImager41(preTX);

        //bsvTX.timer.cancel();
        //bsvTX.timer.purge();

        //if(preimage [0] != null)
        //     return  "Error: " + preimage [0] + preimage [1];

        //DEBUG
        //if(PVTKEY != null)
        //    return "Error 1 " + nInp;
        //nInp = 1;

        //Esta correção é necessária para o trabalho com SCRIPTs
        nInp = bsvTX.nOfPreImages41;

        //if(preimage[0].compareTo("Error 1") == 0  || preimage[0].compareTo("Error 2") == 0)
        if(preimage[0].length()>5)
            if(preimage[0].substring(0,5).compareTo("Error") == 0)
                return preimage[0];

        /////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////


        ////////////////////////////////////////////////////////////
        //Assinatura
        ////////////////////////////////////////////////////////////

        /////////////////////////////////////////////////////////
        //Chave de Encriptação após a Primeira
        //Neste Conjunto de testes somente este conjunto de Chaves é testada
        /////////////////////////////////////////////////////////

        String HA1, HA2, HA3, HA4;

        String[] signDER41 = new String[nInp];
        String[] DERsizeOut = new String[nInp];
        String[] inputScript = new String[nInp];
        String newTX = "";
        //String TXID = "";
        TXID = "";
        int[] DERsize = new int[nInp];
        String[] inputScriptSize = new String[nInp];

        /////////////////////////////////////////////////////////////////////
        //Confecção das Assinatura DER para cada input
        /////////////////////////////////////////////////////////////////////
        for(int i = 0; i < nInp; i++)
        {

            String lastTXID = bsvTX.unspentTXID[i];

            HA1 = SHA256G.SHA256bytes(SHA256G.HashStrToByte2(PVTKEY));
            HA2 = SHA256G.SHA256MsnHxHex2(SHA256G.HashStrToByte2(lastTXID), SHA256G.HashStrToByte2(HA1));
            HA3 = SHA256G.SHA256bytes(SHA256G.HashStrToByte2(HA2));

            //HA4 != H4 Elemento "Secreto" utilizado na Geração de H5 e também de K em ECDSA;
            HA4 = SHA256G.SHA256MsnHxHex2(SHA256G.HashStrToByte2(HA3), SHA256G.HashStrToByte2(HA2));

            //Tratar o caso de assinaturas grandes
            signDER41[i] = bsvTX.signEcdsaDER41(preimage[i], PVTKEY, HA4);


            DERsize[i] = signDER41[i].length() / 2;
            //int DERsizeHEX = DERsize;
            DERsizeOut[i] = Integer.toHexString(DERsize[i]);


            inputScriptSize[i] = Integer.toHexString(
                    (DERsizeOut[i] + signDER41[i] + SECsizeOut + PUBKEYSEC).length() / 2);

            inputScript[i] = inputScriptSize[i] +
                    DERsizeOut[i] + signDER41[i] +
                    SECsizeOut + PUBKEYSEC;
        }

        /////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////

        /////////////////////////////////////////////////////////////////////
        //Confecção string final de input com as assinaturas DER
        /////////////////////////////////////////////////////////////////////
        inputString =  bsvTX.inputPosString(nInp, SECsizeOut, PUBKEYSEC, inputScript);

        newTX = inputString + OutputString;

        NewTxHexData = newTX;

        return newTX;
        /////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////

    }

///////////////////////////////////////////////////////////////////////////////
// Envia uma transação para a rede BSV via API WhatsOnChain
///////////////////////////////////////////////////////////////////////////////
    public String txBroadCast(String newTX)
    {
        TXID = "";
        /////////////////////////////////////////////////////////////////////
        //TXID da Transação
        /////////////////////////////////////////////////////////////////////
        TXID = SHA256G.SHA256bytes(SHA256G.HashStrToByte2(newTX));
        TXID = SHA256G.SHA256bytes(SHA256G.HashStrToByte2(TXID));
        TXID = SHA256G.LEformat(TXID);


        /////////////////////////////////////////////////////
        //ENVIO da Transação
        /////////////////////////////////////////////////////

        String txSent = null;
        bsvTX.TxHexDataSent = null;

        //Adaptar a função para o caso de falta de sinal de internet


        bsvTX.broadcastHexBsvTx(newTX, Variables.PoolID);

        /////////////////////////////////////////
        //Change the pool for each TX sent
        // WhatsOnChain = 0
        // Gorilla Pool = 1
        // More pool can be added in the future
        /////////////////////////////////////////

        //Variables.PoolID = 0;     //Testado para Taxa 0.05 sat/byte - ok
        //Whats On Chain aceitou uma TX com 1 sat de Taxa em 04/07/2022;
        //Variables.PoolID = 1;     //Testado para Taxa 0.05 sat/byte - ok
        //Whats On Chain aceitou uma TX com 1 sat de Taxa em 04/07/2022;

        if(Variables.PoolID == 0)
            Variables.PoolID = 1;
        else
            Variables.PoolID = 0;

        //No Previous TX found - na Gorilla Pool, quando a primeira TX de uma dupla enviada para WOC
        // TX consecutivas, antes da execução - devem ser enviadas para a mesma POOL
        // Isso parece ser um problema intermitente de comunicação entre POOLs, pois não se repete com frequência
        // Também o envio entre pools afeta a leitura das TXs ainda não Mineradas para o saldo da carteira;



        /////////////////////////////////////////
        /////////////////////////////////////////

        /*
        String waitHashing = "";
        waitHashing = SHA256G.SHA256STR("ABC");


        long count = 0;
        int flagFail = 0;
        */


        //Loop necessário para esperar a consulta a rede realizado pelo metodo acima
        while (bsvTX.threadBroadCast.isAlive()) {
            txSent = bsvTX.TxHexDataSent;

        }
        //Esta tread não é chamada com timer
        //timer.cancel();
        //timer.purge();

        //monitorar esta variável entre 2 transações;
        //escrever o estado no inicio e no final da execução

        if(bsvTX.TxHexDataSent == null)
            return "Error: Time out Broacasting";


        /////////////////////////////////////////////////////
        /////////////////////////////////////////////////////

        //return bsvTX.TxHexDataSent;


        if(bsvTX.TxHexDataSent.compareTo("OK")==0)
            return TXID;
            //return TXID + " " + Variables.PoolID;
        else
            return bsvTX.TxHexDataSent;



        //return newTX;
    }
///////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////

///////////////////////////////////////////////////////////////////////////////
// Cria uma transação e envia para a rede BSV
///////////////////////////////////////////////////////////////////////////////
    public Boolean txVerify(String txToVerify)
    {
        String [] TXPartes41 = bsvTX.txParts(txToVerify);

        String [] signDER41 = new String[bsvTX.nOfInputs];
        String [] pubKeySEC41 = new String[bsvTX.nOfInputs];
        for (int i =0 ; i < bsvTX.nOfInputs; i ++) {

            signDER41[i] = bsvTX.signDER[i];
            pubKeySEC41[i] = bsvTX.pubKeySEC[i];
        }
        int nInputs = bsvTX.nOfInputs;

        String [] preImage41 = bsvTX.txPreImager41(txToVerify);

        if(preImage41[0].compareTo("Error 1") == 0)
            return false;

        int nPI = bsvTX.nOfPreImages41;

        String [] signVerify = new String[nInputs];

        for (int i =0 ; i < nInputs; i ++) {

            signVerify[i] = bsvTX.txSigVerify(preImage41[i], pubKeySEC41[i], signDER41[i]);
            if(signVerify[i].compareTo("OK") != 0)
                return false;
        }
        return true;
    }

    public String txHexRead (String TXID)
    {
        Boolean searchFail = true;

        String TXToSeach = "";

        bsvTX.TxHexData = null;
        bsvTX.readHexBsvTx(TXID);

        /*

        String waitHashing = "";
        waitHashing = SHA256G.SHA256STR("ABC");

        long count = 0;
        int flagFail = 0;

        */

            //Aguarda até que o dado seja lido da WhatsOnChain
            //Aqui temos que respeitar o


        //Ultimo
        while(bsvTX.threadReadHexBsvTx.isAlive()) {
            TXToSeach = "";

        }

        //bsvTX.timer.cancel();
        //bsvTX.timer.purge();

        TXToSeach = bsvTX.TxHexData;

        if(bsvTX.TxHexData == null) {
            //String[] TxPreimageOutFail = new String[1];
            return  "Error: Time out reading TX HEX DATA";
            //return TxPreimageOutFail;
        }

        if(TXToSeach.length() < 100) {
            searchFail = true;
            return "Error: TX HEX DATA incomplete";
        }
        else
            searchFail = false;


        return TXToSeach;
    }
    public String txHexReadSCRIPT (String TXID)
    {
        Boolean searchFail = true;

        String TXToSeach = "";

        bsvTX.TxHexData = null;
        bsvTX.readHexBsvTxSCRIP(TXID);

        /*

        String waitHashing = "";
        waitHashing = SHA256G.SHA256STR("ABC");

        long count = 0;
        int flagFail = 0;

        */

        //Aguarda até que o dado seja lido da WhatsOnChain
        //Aqui temos que respeitar o


        //Ultimo
        while(bsvTX.threadReadHexBsvTx.isAlive()) {
            TXToSeach = "";

        }

        //bsvTX.timer.cancel();
        //bsvTX.timer.purge();

        TXToSeach = bsvTX.TxHexData;

        if(bsvTX.TxHexData == null) {
            //String[] TxPreimageOutFail = new String[1];
            return  "Error: Time out reading TX HEX DATA";
            //return TxPreimageOutFail;
        }

        if(TXToSeach.length() < 40) {
            searchFail = true;
            return "Error: TX HEX DATA incomplete - " + TXToSeach;
        }
        else
            searchFail = false;


        return TXToSeach;
    }
}

