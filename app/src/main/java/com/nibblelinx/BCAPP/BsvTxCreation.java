package com.nibblelinx.BCAPP;

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
        //bsvTX.timer.cancel();
        //bsvTX.timer.purge();

        bsvTX.unsPentInputs = null;
        bsvTX.readBsvAddsUnspent(BSVADD);
        String unspentTX = "";

        //int j = 0;

        //Variables.threadM = false;
        //while(bsvTX.unsPentInputs == null)
        //while (!Variables.threadM)
        while (!bsvTX.threadEndReadBsvAddsUnspent)
        {
            unspentTX = "";
            //j++;
        }

        if(bsvTX.unsPentInputs == null)
            return ("erro");

        unspentTX = bsvTX.unsPentInputs;
        bsvTX.timer.cancel();
        bsvTX.timer.purge();

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
    public String txBuilder (String PVTKEY, int nOfOutputs, String [] PayWallets, String [] PayValues,
                                     String [] OP_RETURNs, int numberOfOPRETURNS)
    {
        NewTxHexData = "";
        //////////////////////////////////////////////////////////////////////////////////////////////////
        //Preparação das Chaves
        //////////////////////////////////////////////////////////////////////////////////////////////////
        Keygen pubKey = new Keygen();
        EcdsaSecretus ECDSA = new EcdsaSecretus();
        //PUB KEY from PVT Key
        //PVT key
        //String PUBKEY = pubKey.publicKey(PVTKEY);

        Boolean CompPKey = false;

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
        bsvTX.unsPentInputs = null;
        bsvTX.readBsvAddsUnspent(BSVADD);
        String unspentTX = "";

        //while(bsvTX.unsPentInputs == null)
        while(!bsvTX.threadEndReadBsvAddsUnspent)
        {
            unspentTX = "";
        }
        unspentTX = bsvTX.unsPentInputs;
        bsvTX.timer.cancel();
        bsvTX.timer.purge();

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


        /////////////////////////////////////////////////////////////////////
        //Confecção das preImagenS dos inputs
        /////////////////////////////////////////////////////////////////////
        String[] preimage = bsvTX.txPreImager41(preTX);

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
/*
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
        bsvTX.broadcastHexBsvTx(newTX);

        //Loop necessário para esperar a consulta a rede realizado pelo metodo acima
        //while (bsvTX.TxHexDataSent == null)
        while (!bsvTX.threadEndBroadcastHexBsvTx)
            txSent = bsvTX.TxHexDataSent;

        /////////////////////////////////////////////////////
        /////////////////////////////////////////////////////

        if(bsvTX.TxHexDataSent.compareTo("OK")==0)
            return TXID;
        else
            return bsvTX.TxHexDataSent;
            //return newTX;

 */
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
        bsvTX.broadcastHexBsvTx(newTX);

        //Loop necessário para esperar a consulta a rede realizado pelo metodo acima
        //while (bsvTX.TxHexDataSent == null)
        while (!bsvTX.threadEndBroadcastHexBsvTx)
            txSent = bsvTX.TxHexDataSent;

        /////////////////////////////////////////////////////
        /////////////////////////////////////////////////////

        if(bsvTX.TxHexDataSent.compareTo("OK")==0)
            return TXID;
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

        //while (searchFail) {

            bsvTX.TxHexData = null;
            bsvTX.readHexBsvTx(TXID);

            //Aguarda até que o dado seja lido da WhatsOnChain
            //Aqui temos que respeitar o
            //while (TxHexData == null)
            while(!bsvTX.threadEndReadHexBsvTx)
                TXToSeach = "";

            TXToSeach = bsvTX.TxHexData;

            if(TXToSeach.length() < 100 || TXToSeach == null)
                searchFail = true;
            else
                searchFail = false;

            bsvTX.timer.cancel();
            bsvTX.timer.purge();
        //}

        return TXToSeach;
    }
}

