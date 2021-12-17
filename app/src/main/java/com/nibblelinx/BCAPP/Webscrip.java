package com.nibblelinx.BCAPP;

//https://developer.android.com/guide/webapps/webview

//https://developer.android.com/guide/webapps/webview

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

public class Webscrip {
    Context mContext;
    private String wvstr;

    //private String dataToChain;
    private String[] dataToChain = new String[111];

    private String pieceMBDATA;
    private String dataAddress;
    private String mailData;
    private String amountToDataAddress;
    private String amountToAddress;
    private String lastTXID;

    //public String wvstr;
    //public String[] wvstr;
    private String buttonId;
    private String wvstr2;
    //private attributes
    private String id;
    //private String buttonId;
    private String buttonData;
    private String status;
    private String txid;
    private String normalizedTxid;
    private String amount;
    private String currency;
    private String satoshis;
    private String userId;
    private String rawtx;
    private String nftIndex;
    private String assetAdd;
    private int TXType = 0;
    //TXType 0 to net, 1 to local storage, 2 ....

    private String test = "";

    /** Instantiate the interface and set the context */
    Webscrip(Context c)
    {
        mContext = c;
    }

    /** Show a toast from the web page */
    @JavascriptInterface
    public void showToast(String toast)
    {
        Toast.makeText(mContext, toast, Toast.LENGTH_LONG).show();
    }

    @JavascriptInterface
    public String getwvstr()
    {
        return wvstr;
    }
    @JavascriptInterface
    public void setwvstr(String wvstr)
    {
        this.wvstr = wvstr;
    }

    @JavascriptInterface
    public String getNftIndex()
    {
        return nftIndex;
    }
    @JavascriptInterface
    public void setNftIndex(String nftIndex)
    {
        this.nftIndex = nftIndex;
    }


    @JavascriptInterface
    public String getAssetAdd()
    {
        return assetAdd;
    }
    @JavascriptInterface
    public void setAssetAdd(String assetAdd)
    {
        this.assetAdd = assetAdd;
    }

    @JavascriptInterface
    public String getTest()
    {
        return test;
    }
    @JavascriptInterface
    public void setTest(String test)
    {
        this.test = test;
    }


    @JavascriptInterface
    public int getTXType()
    {
        return TXType;
    }
    @JavascriptInterface
    public void setTXType(int TXType)
    {
        this.TXType = TXType;
    }


//////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////

    @JavascriptInterface
    public String getMailData()
    {
        return mailData;
    }
    @JavascriptInterface
    public void setMailData(String mailData)
    {
        this.mailData = mailData;
    }



    @JavascriptInterface
    public String getDataToChain(int i)
    {
        return dataToChain[i];
    }
    @JavascriptInterface
    public String [] getDataToChainTotal()
    {
        return dataToChain;
    }
    @JavascriptInterface
    public void setDataToChain(String dataToChain, int i)
    {
        this.dataToChain[i] = dataToChain;
    }



    @JavascriptInterface
    public String getPieceMBDATA()
    {
        return pieceMBDATA;
    }
    @JavascriptInterface
    public void setPieceMBDATA(String pieceMBDATA)
    {
        this.pieceMBDATA = pieceMBDATA;
    }



    @JavascriptInterface
    public String getDataAddress()
    {
        return dataAddress;
    }
    @JavascriptInterface
    public void setDataAddress(String dataAddress)
    {
        this.dataAddress = dataAddress;
        Variables.BSVWallet = dataAddress;
    }

    @JavascriptInterface
    public String getAmountToDataAddress()
    {
        return amountToDataAddress;
    }
    @JavascriptInterface
    public void setAmountToDataAddress(String amountToDataAddress)
    {
        this.amountToDataAddress = amountToDataAddress;
    }

    @JavascriptInterface
    public String getAmountToAddress()
    {
        return amountToAddress;
    }
    @JavascriptInterface
    public void setAmountToAddress(String amountToAddress)
    {
        this.amountToAddress = amountToAddress;
    }

    @JavascriptInterface
    public String getLastTXID()
    {
        return lastTXID;
    }
    @JavascriptInterface
    public void setLastTXID(String lastTXID)
    {
        TxidDB banco = new TxidDB(mContext);
        this.lastTXID = lastTXID;
        Variables.LastTXID = lastTXID;
        if (banco.isAgain(lastTXID)) {
            //Toast.makeText(TxidList.this, "TXID Collision", Toast.LENGTH_SHORT).show();
        } else {
            //banco.Update(Txid, Wallet);
            //banco.Update(lastTXID, Variables.BSVWallet);

            //Se não possuir endereço de dados,não atualizar no DB
            //Pois não é dado na rede BSV

            //if(this.dataAddress.compareTo("")!=0)
                //banco.Update(lastTXID, this.dataAddress);

                //this.dataAddress == "" deve ter corrompido o DB
        }
    }

    @JavascriptInterface
    public String getamount()
    {
        return amount;
    }
    @JavascriptInterface
    public void setamount(String amount)
    {
        this.amount = amount;
    }

//////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////

    @JavascriptInterface
    public String getwvstr2()
    {
        return wvstr2;
    }
    @JavascriptInterface
    public void setwvstr2(String wvstr)
    {
        this.wvstr2 = wvstr;
    }


    @JavascriptInterface
    public String getid()
    {
        return id;
    }
    @JavascriptInterface
    public void setid(String id)
    {
        this.id = id;
    }

    @JavascriptInterface
    public String getbuttonId()
    {
        return buttonId;
    }
    @JavascriptInterface
    public void setbuttonId(String buttonId)
    {
        this.buttonId = buttonId;
    }

    @JavascriptInterface
    public String getbuttonData()
    {
        return buttonData;
    }
    @JavascriptInterface
    public void setbuttonData(String buttonData)
    {
        this.buttonData = buttonData;
    }

    @JavascriptInterface
    public String getstatus()
    {
        return status;
    }
    @JavascriptInterface
    public void setstatus(String status)
    {
        this.status = status;
    }

    @JavascriptInterface
    public String gettxid()
    {
        return txid;
    }
    @JavascriptInterface
    public void settxid(String txid)
    {
        this.txid = txid;
    }

    @JavascriptInterface
    public String getnormalizedTxid()
    {
        return normalizedTxid;
    }
    @JavascriptInterface
    public void setnormalizedTxid(String normalizedTxid)
    {
        this.normalizedTxid = normalizedTxid;
    }

    @JavascriptInterface
    public String getcurrency()
    {
        return currency;
    }
    @JavascriptInterface
    public void setcurrency(String currency)
    {
        this.currency = currency;
    }

    @JavascriptInterface
    public String getsatoshis()
    {
        return satoshis;
    }
    @JavascriptInterface
    public void setsatoshis(String satoshis)
    {
        this.satoshis = satoshis;
    }

    @JavascriptInterface
    public String getuserId()
    {
        return userId;
    }
    @JavascriptInterface
    public void setuserId(String userId)
    {
        this.userId = userId;
    }

    @JavascriptInterface
    public String getrawtx()
    {
        return rawtx;
    }
    @JavascriptInterface
    public void setrawtx(String rawtx)
    {
        this.rawtx = rawtx;
    }
}

