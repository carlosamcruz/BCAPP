
function myCallBack(payment)
{

    Android.setLastTXID(payment.txid);
}
//do not work
function errCall(error)
{
    //Informs: https://docs.moneybutton.com/docs/mb-javascript.html#onpayment
    setStr2(error)
}

//////////////////////////////////////////////////////////////////////////////////////////////
//Função para enviar apenas 1 OP_RETURN
//////////////////////////////////////////////////////////////////////////////////////////////
function mybuttonMainNet001(dataToChain, amountData, mailData, amountToDataAddress, nftIndex)
{
    let div = document.getElementById('my-button-here');
    moneyButton.render(div, {
        label: 'MB',
        outputs: [

            ///////////////////////////////////////////////////////////////////////////////////
            //Apenas 1 OP_RETURN
            ///////////////////////////////////////////////////////////////////////////////////
            {
                script: 'OP_FALSE OP_RETURN ' + dataToChain,
                amount: '0',
                currency: 'BSV'
            },

            ///////////////////////////////////////////////////////////////////////////////////
            ///////////////////////////////////////////////////////////////////////////////////

            {
                to: mailData,
                amount: amountData,
                currency: 'BSV'
            },

            {
                to: nftIndex,
                amount: amountToDataAddress,
                currency: 'BSV'
            }
        ],
        onPayment: myCallBack,
        onerror: errCall
    })
}



