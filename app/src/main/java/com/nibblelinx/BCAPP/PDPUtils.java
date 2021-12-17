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


    ///////////////////////////////////////////////////////////////////////
    //Emoji search:
    //https://unicode.org/Public/emoji/13.1/emoji-sequences.txt
    //Hebrew Language
    //https://unicode.org/charts/PDF/U0590.pdf
    ///////////////////////////////////////////////////////////////////////

    static String toUTF (String text)
    {

        char[] textChar = text.toCharArray();
        char[] newTextChar = new char[text.length()];

        int j = 0;
        //for (int i = 0, j = 0; i < text.length(); i++, j++)
        for (int i = 0; i < text.length(); i++, j++)
        {
            if (textChar[i] == 0xd8 && textChar[i + 1] >= 0x3d)
            {
                newTextChar[j] = (char) ((0x100 * (textChar[i] & 0xFF)) + (textChar[i+1] & 0xFF));
                newTextChar[j+1] = (char) ((0x100 * (textChar[i+2] & 0xFF)) + (textChar[i+3] & 0xFF));
                i = i + 3;
                j++;
            }
            //https://unicode.org/charts/PDF/U0590.pdf
            else if(textChar[i] == 0x05 && ((textChar[i + 1] >= 0x90) && (textChar[i + 1] <= 0xFF) ))
            {
                newTextChar[j] = (char) ((0x100 * (textChar[i] & 0xFF)) + (textChar[i+1] & 0xFF));
                i = i + 1;

            } else newTextChar[j] = textChar[i];

            //Chinese
            //Google: unicode for chinese characters
            //https://stackoverflow.com/questions/1366068/whats-the-complete-range-for-chinese-characters-in-unicode
            //
            // Novas Linguas:
            //      Chinês
            //      Japonês
            //      Arábe
            //      Russo
            //      Coreano
            //      ...
            /////////////////////////////////////////////
        }
        char[] newTextChar2 = new char[j];

        for (int i = 0; i < j; i++) newTextChar2 [i] = newTextChar [i];


        //return String.valueOf(newTextChar);
        return String.valueOf(newTextChar2);

        //Não Funciona
        /*
        byte[] textByte = new byte[text.length()];

        for (int i = 0; i<text.length(); i++)
            textByte[i] = (byte) (textChar[i] & 0xFF);
        //String text2 = new String(textByte, "UTF-8");
        text = new String(textByte, "UTF-8");
        return  text;
        */
    }
    ///////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////

    static String toSTR (String text)
    {
        char[] textChar = text.toCharArray();
        int numberOfNonChar = 0;

        for (int i = 0; i < text.length(); i++)
            if (textChar[i] > 0xFF) numberOfNonChar++;

        char[] newTextChar = new char[text.length() + numberOfNonChar];

        for (int i = 0, j = 0; i < text.length(); i++, j++)
        {
            if (textChar[i] > 0xFF) {
                newTextChar[j] = (char) ((textChar[i] / 0x100) & 0xFF);
                j++;
                newTextChar[j] = (char) (textChar[i] & 0xFF);
            } else {
                newTextChar[j] = (char) (textChar[i] & 0xFF);
            }
        }
        return String.valueOf(newTextChar);
    }

    /////////////////////////////////////////////////////////////////////////////
    //Verificação do tipo do Arquivo a ser salvo
    //https://en.wikipedia.org/wiki/List_of_file_signatures
    /////////////////////////////////////////////////////////////////////////////

    static String fileType (String text)
    {
        //return ".txt";
        //return (".txt");

        if(text.length() > 15) {
            if (text.substring(0, 3).compareTo("ÿØÿ") == 0) return(".jpg");
            if (text.substring(0, 2).compareTo("BM") == 0) return(".bmp");
            if (text.substring(0, 2).compareTo("PK") == 0) return(".zip");
            if (text.substring(0, 2).compareTo("ZM") == 0) return(".exe");

            if (text.substring(0, 2).compareTo("ÿû") == 0 ||
                    text.substring(0, 2).compareTo("ÿó") == 0 ||
                    text.substring(0, 2).compareTo("ÿò") == 0 ||
                    text.substring(0, 3).compareTo("ID3") == 0
            ) return(".mp3");

            //https://www.garykessler.net/library/file_sigs.html
            //https://stackoverflow.com/questions/13190005/detect-mp4-files
            if (text.substring(4, 12).compareTo("ftypisom") == 0 ||
                    text.substring(4, 11).compareTo("ftypmp4") == 0 ||
                    text.substring(0, 2).compareTo("0&") == 0 ||
                    text.substring(4, 7).compareTo("fty") == 0 ||
                    text.substring(4, 6).compareTo("ft") == 0 ||

                    text.substring(3, 7).compareTo("ftyp") == 0 ||
                    text.substring(2, 6).compareTo("ftyp") == 0 ||
                    text.substring(1, 5).compareTo("ftyp") == 0 ||
                    text.substring(0, 4).compareTo("ftyp") == 0 ||
                    text.substring(4, 8).compareTo("ftyp") == 0) return(".mp4"); //verificar este


            if (text.substring(0, 4).compareTo("Rar!") == 0) return(".rar");
            if (text.substring(0, 4).compareTo(".PNG") == 0  ||
                    text.substring(1, 4).compareTo("PNG") == 0) return(".png");
            if (text.substring(0, 4).compareTo("Êþº¾") == 0) return(".class");

            if (text.substring(0, 5).compareTo("%PDF-") == 0) return(".pdf");
            if (text.substring(0, 5).compareTo("ustar") == 0) return(".tar");

            if (text.substring(0, 4).compareTo("RIFF") == 0 &&
                    text.substring(8, 12).compareTo("WAVE") == 0
            ) return(".wav");
            if (text.substring(0, 4).compareTo("RIFF") == 0 &&
                    text.substring(8, 12).compareTo("AVI.") == 0
            ) return(".avi");

            if ((text.charAt(0) == 0x00 && text.charAt(1) == 0x00 && text.charAt(2) == 0x01 && text.charAt(3) == 0xba) ||
                    (text.charAt(0) == 0x00 && text.charAt(1) == 0x00 && text.charAt(2) == 0x01 && text.charAt(3) == 0xb3) ||
                    text.charAt(0) == 0x47
            ) return(".mpeg");

            //return (".txt");
        }
        return (".txt");
    }
    /////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////

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

    /////////////////////////////////////////////////////////////////////////////
    //coloca a string em formato adequado para o moneybutton
    /////////////////////////////////////////////////////////////////////////////
    static String dataSatoshiLocal(long DataLength){

        long satBSV[] = new long[8];
        //long amountSEC = (dataToChain.length()/1000)+ 1 + 546; //+ 100000000;
        //long amountSEC = (dataToChain.length()/1000)+ 1 + 546; //+ 100000000;

        //long amountSEC = (dataToChain.length()/2)+ 1 + 546; //+ 100000000;
        //long amountSEC = (dataToChain.length()/2)+ 1 + Variables.BSVDustLimitLong; //+ 100000000;
        long amountSEC = (DataLength/2000)+ 1 + Variables.BSVDustLimitLong; //+ 100000000;

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
