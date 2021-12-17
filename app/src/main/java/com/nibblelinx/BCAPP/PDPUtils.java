package com.nibblelinx.BCAPP;

public class PDPUtils {

    public PDPUtils ()
    {}
    //Função para teste de comparação de resultados
    static String strToHEXStr (String text)
    {
        char base16[] = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
        char[] hexString64 = new char[(2*text.length())];

        for(int i = 0; i < text.length(); i++)
        {
            hexString64[i*2+1] =  base16[(text.charAt(i) & 0x0F)];
            hexString64[i*2] = base16[((text.charAt(i)/0x10) & 0x0F)];
        }
        text = String.valueOf(hexString64);//necessario

        return text;
    }


    static String byteToString (byte[] text)
    {
        char[] text2 = new char[text.length];

        //return "abc";

        if((text.length == 1) && (text[0] == 0))
            return null;
            //return "abc";

        for(int i = 0; i < text.length; i++)
            text2[i] = (char) (text[i] & 0xFF);

        return String.valueOf(text2);//necessario
    }

    /////////////////////////////////////////////////////////////////////////////
    //coloca a string em formato adequado para o moneybutton
    /////////////////////////////////////////////////////////////////////////////
    static String dataSatoshi(String dataToChain){

        long satBSV[] = new long[8];
        //long amountSEC = (dataToChain.length()/1000)+ 1 + 546; //+ 100000000;
        //long amountSEC = (dataToChain.length()/1000)+ 1 + 546; //+ 100000000;

        //long amountSEC = (dataToChain.length()/2)+ 1 + 546; //+ 100000000;

        //Aqui esta sendo cobrado 1 SAT/BYTE + DustLimit
        //long amountSEC = (dataToChain.length()/2)+ 1 + Variables.BSVDustLimitLong; //+ 100000000;

        //Aqui esta sendo cobrado 0.5 SAT/BYTE + DustLimit
        //long amountSEC = (dataToChain.length()/4)+ 1 + Variables.BSVDustLimitLong; //+ 100000000;

        //Aqui esta sendo cobrado 0.25 SAT/BYTE  + DustLimit
        long amountSEC = (dataToChain.length()/8)+ 1 + Variables.BSVDustLimitLong; //+ 100000000;

        long amountSEC2 = ( amountSEC/10 )*10;

        satBSV [7] = amountSEC - amountSEC2;
        amountSEC = amountSEC/10; // 10000000
        amountSEC2 = ( amountSEC/10 )*10;
        satBSV [6] = amountSEC - amountSEC2;
        amountSEC = amountSEC/10; // 1000000
        amountSEC2 = ( amountSEC/10 )*10;
        satBSV [5] = amountSEC - amountSEC2;
        amountSEC = amountSEC/10; // 100000
        amountSEC2 = ( amountSEC/10 )*10;
        satBSV [4] = amountSEC - amountSEC2;
        amountSEC = amountSEC/10; // 10000
        amountSEC2 = ( amountSEC/10 )*10;
        satBSV [3] = amountSEC - amountSEC2;
        amountSEC = amountSEC/10; // 1000
        amountSEC2 = ( amountSEC/10 )*10;
        satBSV [2] = amountSEC - amountSEC2;
        amountSEC = amountSEC/10; // 100
        amountSEC2 = ( amountSEC/10 )*10;
        satBSV [1] = amountSEC - amountSEC2;
        amountSEC = amountSEC/10; // 10
        amountSEC2 = ( amountSEC/10 )*10;
        satBSV [0] = amountSEC - amountSEC2;
        amountSEC = amountSEC/10; // 1
        //amountSEC2 = ( amountSEC/10 )*10;
        String[] amountX = {"0", ".", "0","0","0","0","0","0","0","0"};
        amountX[0] = String.valueOf(amountSEC);
        amountX[2] = String.valueOf(satBSV[0]);
        amountX[3] = String.valueOf(satBSV[1]);
        amountX[4] = String.valueOf(satBSV[2]);
        amountX[5] = String.valueOf(satBSV[3]);
        amountX[6] = String.valueOf(satBSV[4]);
        amountX[7] = String.valueOf(satBSV[5]);
        amountX[8] = String.valueOf(satBSV[6]);
        amountX[9] = String.valueOf(satBSV[7]);

        return (amountX[0]+amountX[1]+amountX[2]+amountX[3]+amountX[4]+amountX[5]+amountX[6]+amountX[7]+amountX[8]+amountX[9]);
    }
    /////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////

}
