package com.nibblelinx.BCAPP;

import java.math.BigInteger;

public class Keygen {

    Ecc keys = new Ecc();
    TonelliShanks sqrtCF = new TonelliShanks();
    private BigInteger [] point = new BigInteger[2];
    private char base64[] = {'*','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F','G','H','#','J','K','L','M','N','%','P','Q','R','S','T',
            'U','V','W','X','Y','Z','a','b','c','d','e','f','g','h','i','j','k','?','m','n','o','p','q','r','s','t','u','v','w','x','y','z','$','&'};
    private char base16[] = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};

    public Keygen() {}

    //Formação da chave publica a partir de uma string
    //Outra função será publicKeyHEX - formação da chave publica a partir de uma chave privada em HEXADECIMAL
    public String publicKey (String text)
    {
        int sizeStr256 = text.length()/8;
        int keySize;
        BigInteger SecretKey = new BigInteger("0");
        BigInteger cont = new BigInteger("1");
        char[] secKey;
        char[] pubKey64 = new char[44];
        secKey = text.toCharArray();

        if(text.length()<=31) keySize = text.length() % 32;
        else  keySize = 32;

        for(int i = keySize-1; i>=0; i--)
        {
            SecretKey = SecretKey.add(cont.multiply(BigInteger.valueOf(secKey[i])));
            cont = cont.multiply(BigInteger.valueOf(0x100));
        }

        // Chave MOD P
        SecretKey = keys.modp(SecretKey, keys.p);

        BigInteger [] point = new BigInteger[2];
        point = keys.eccnP(SecretKey, keys.Gx, keys.Gy);

        for(int i = 43 ; i>0; i--)
        {
            pubKey64[i] = base64[(point[0].and(BigInteger.valueOf(63))).intValue()];
            point[0] = point[0].divide(BigInteger.valueOf(64));
        }

        //PRODUZINDO VALOR ERRADO???
        if((point[1].and(BigInteger.valueOf(1)).intValue()) == 1)
            pubKey64[0] = base64[3];
        else
            pubKey64[0] = base64[2];

        text = String.valueOf(pubKey64);//necessario

        return text;
    }

    public String stringToHEXString (String text)
    {

        //int sizeStr256 = text.length()/8;
        int keySize;
        //BigInteger SecretKey = new BigInteger("0");
        //BigInteger cont = new BigInteger("1");
        char[] secKey;
        char[] hexString64 = new char[64];
        //char[] pubKey64 = new char[44];
        secKey = text.toCharArray();
        for (int i=0; i<64; i++) hexString64[i] = '0';

        if(text.length()<=31) keySize = text.length() % 32;
        else  keySize = 32;

        for(int i = keySize-1, j = 32-1; i>=0; i--, j--)
        {

            hexString64[j*2+1] =  base16[(secKey[i] & 0x0F)];
            hexString64[j*2] = base16[((secKey[i]/0x10) & 0x0F)];
        }
        text = String.valueOf(hexString64);//necessario

        return text;
    }

    //Formação da chave publica a partir de uma string com elemetos em HEXADECIMAL
    //Outra função será publicKey - formação da chave publica a partir de uma chave privada em string
    //A string tem 64 posições
    public String publicKeyHEX (String text)
    {
        //int sizeStr256 = text.length()/8;
        //int keySize;
        BigInteger SecretKey = new BigInteger("0");
        BigInteger cont = new BigInteger("1");
        char[] secKey;
        char[] pubKey64 = new char[44];
        secKey = text.toCharArray();

        //if(text.length()<=31) keySize = text.length() % 32;
        //else  keySize = 32;
        //keySize = 32;

        //for(int i = keySize-1; i>=0; i--)
        //A string tem 64 posições
        for(int i = 64-1; i>=0; i--)
        {
            int value;
            for (value =0 ; value < 16; value++)
                if (secKey[i]==base16[value]) break;

            //SecretKey = SecretKey.add(cont.multiply(BigInteger.valueOf(secKey[i])));
            SecretKey = SecretKey.add(cont.multiply(BigInteger.valueOf(value)));
            //cont = cont.multiply(BigInteger.valueOf(0x100));
            cont = cont.multiply(BigInteger.valueOf(0x10));
        }

        // Chave MOD P
        SecretKey = keys.modp(SecretKey, keys.p);

        BigInteger [] point = new BigInteger[2];
        point = keys.eccnP(SecretKey, keys.Gx, keys.Gy);

        for(int i = 43 ; i>0; i--)
        {
            pubKey64[i] = base64[(point[0].and(BigInteger.valueOf(63))).intValue()];
            point[0] = point[0].divide(BigInteger.valueOf(64));
        }

        if((point[1].and(BigInteger.valueOf(1)).intValue()) == 1)
            pubKey64[0] = base64[3];
        else
            pubKey64[0] = base64[2];

        text = String.valueOf(pubKey64);//necessario

        return text;
    }


    //Formação da chave publica a partir de uma string com elemetos em HEXADECIMAL
    //Outra função será publicKey - formação da chave publica a partir de uma chave privada em string
    //A string tem 64 posições
    //FORMATO SEC Comprimido
    public String publicKeyCompSEC (String text)
    {
        //int sizeStr256 = text.length()/8;
        //int keySize;
        BigInteger SecretKey = new BigInteger("0");
        BigInteger cont = new BigInteger("1");
        char[] secKey;
        char[] pubKeyHEX = new char[65];
        secKey = text.toCharArray();

        //if(text.length()<=31) keySize = text.length() % 32;
        //else  keySize = 32;
        //keySize = 32;

        //for(int i = keySize-1; i>=0; i--)
        //A string tem 64 posições
        for(int i = 64-1; i>=0; i--)
        {
            int value;
            for (value =0 ; value < 16; value++)
                if (secKey[i]==base16[value]) break;

            //SecretKey = SecretKey.add(cont.multiply(BigInteger.valueOf(secKey[i])));
            SecretKey = SecretKey.add(cont.multiply(BigInteger.valueOf(value)));
            //cont = cont.multiply(BigInteger.valueOf(0x100));
            cont = cont.multiply(BigInteger.valueOf(0x10));
        }

        // Chave MOD P
        SecretKey = keys.modp(SecretKey, keys.p);

        BigInteger [] point = new BigInteger[2];
        point = keys.eccnP(SecretKey, keys.Gx, keys.Gy);

        for(int i = 64 ; i>0; i--)
        {
            pubKeyHEX[i] = base16[(point[0].and(BigInteger.valueOf(15))).intValue()];
            point[0] = point[0].divide(BigInteger.valueOf(16));
        }

        if((point[1].and(BigInteger.valueOf(1)).intValue()) == 1)
            pubKeyHEX[0] = base16[3];
        else
            pubKeyHEX[0] = base16[2];

        text = String.valueOf(pubKeyHEX);//necessario

        return "0" +text;
    }

    //Formação da chave publica a partir de uma string com elemetos em HEXADECIMAL
    //Outra função será publicKey - formação da chave publica a partir de uma chave privada em string
    //A string tem 64 posições
    //FORMATO SEC UNCOMP
    public String publicKeyUncompSEC (String text)
    {
        //int sizeStr256 = text.length()/8;
        //int keySize;
        BigInteger SecretKey = new BigInteger("0");
        BigInteger cont = new BigInteger("1");
        char[] secKey;
        char[] pubKeyHEX = new char[65];
        char[] pubKeyHEXY = new char[64];
        secKey = text.toCharArray();

        //if(text.length()<=31) keySize = text.length() % 32;
        //else  keySize = 32;
        //keySize = 32;

        //for(int i = keySize-1; i>=0; i--)
        //A string tem 64 posições
        //for(int i = (64 + 64)-1; i>=0; i--)
        for(int i = (text.length())-1; i>=0; i--)
        {
            int value;
            for (value =0 ; value < 16; value++)
                if (secKey[i]==base16[value]) break;

            //SecretKey = SecretKey.add(cont.multiply(BigInteger.valueOf(secKey[i])));
            SecretKey = SecretKey.add(cont.multiply(BigInteger.valueOf(value)));
            //cont = cont.multiply(BigInteger.valueOf(0x100));
            cont = cont.multiply(BigInteger.valueOf(0x10));
        }

        // Chave MOD P
        SecretKey = keys.modp(SecretKey, keys.p);

        BigInteger [] point = new BigInteger[2];
        point = keys.eccnP(SecretKey, keys.Gx, keys.Gy);

        for(int i = 64; i>0; i--)
        {
            pubKeyHEX[i] = base16[(point[0].and(BigInteger.valueOf(15))).intValue()];
            point[0] = point[0].divide(BigInteger.valueOf(16));


            pubKeyHEXY[i-1] = base16[(point[1].and(BigInteger.valueOf(15))).intValue()];
            point[1] = point[1].divide(BigInteger.valueOf(16));

        }

        pubKeyHEX[0] = base16[4];

        text = "0" + String.valueOf(pubKeyHEX) + String.valueOf(pubKeyHEXY);//necessario

        return text;
    }


    //Produz uma chave privada em hexadecimal a partir de uma string qualquer
    public String pvtKey2 (String text)
    {

        int keySize;
        BigInteger SecretKey = new BigInteger("0");
        BigInteger cont = new BigInteger("1");
        char[] secKey;
        char[] pvtKeyHex = new char[64];

        if(text == null) return null;

        secKey = text.toCharArray();

        if(text.length()<=31) keySize = text.length() % 32;
        else  keySize = 32;

        for(int i = keySize-1; i>=0; i--)
        {
            SecretKey = SecretKey.add(cont.multiply(BigInteger.valueOf(secKey[i])));
            cont = cont.multiply(BigInteger.valueOf(0x100));
        }

        // Chave MOD P
        SecretKey = keys.modp(SecretKey, keys.p);

        for(int i = 63 ; i>0; i--)
        {
            pvtKeyHex[i] = base16[(SecretKey.and(BigInteger.valueOf(15))).intValue()];
            SecretKey = SecretKey.divide(BigInteger.valueOf(16));
        }

        //text = String.valueOf(pvtKey64);//necessario
        //text = String.valueOf(pvtKeyHex);//necessario

        //return text;

        return String.valueOf(pvtKeyHex);//necessario
    }

    //gera uma chave privada atraves de uma string qualquer em formato BigInteger
    public BigInteger secKey (String text)
    {
        //int sizeStr256 = text.length()/8;
        int keySize;
        BigInteger SecretKey = new BigInteger("0");
        BigInteger cont = new BigInteger("1");
        char[] secKey;

        secKey = text.toCharArray();

        if(text.length()<=31) keySize = text.length() % 32;
        else  keySize = 32;

        for(int i = keySize-1; i>=0; i--)
        {
            SecretKey = SecretKey.add(cont.multiply(BigInteger.valueOf(secKey[i])));
            cont = cont.multiply(BigInteger.valueOf(0x100));
        }

        // Chave MOD P
        SecretKey = keys.modp(SecretKey, keys.p);

        return SecretKey;
    }

    //Gera uma chave privada a partir de uma string Hexadecimal resultante de uma HASH SHA256
    public BigInteger secKeyHashToBigInt (String text)
    {
        //int sizeStr256 = text.length()/8;
        int keySize;
        BigInteger SecretKey = new BigInteger("0");
        BigInteger cont = new BigInteger("1");
        char[] secKey;

        secKey = text.toCharArray();

        for (int i = 0; i < text.length(); i++)
        {
            switch (secKey[i])
            {
                case '0':
                    secKey[i] = 0;
                    break;
                case '1':
                    secKey[i] = 1;
                    break;
                case '2':
                    secKey[i] = 2;
                    break;
                case '3':
                    secKey[i] = 3;
                    break;
                case '4':
                    secKey[i] = 4;
                    break;
                case '5':
                    secKey[i] = 5;
                    break;
                case '6':
                    secKey[i] = 6;
                    break;
                case '7':
                    secKey[i] = 7;
                    break;
                case '8':
                    secKey[i] = 8;
                    break;
                case '9':
                    secKey[i] = 9;
                    break;
                case 'a':
                    secKey[i] = 10;
                    break;
                case 'b':
                    secKey[i] = 11;
                    break;
                case 'c':
                    secKey[i] = 12;
                    break;
                case 'd':
                    secKey[i] = 13;
                    break;
                case 'e':
                    secKey[i] = 14;
                    break;
                case 'f':
                    secKey[i] = 15;
                    break;
            }
        }
        //for(int i = (Hx.length()/2)-1; i>=0; i--)
        for(int i = 0; i < (text.length()/2); i++)
        {
            secKey [i] = (char) (secKey[2*i + 1] + (secKey[2*i] * 0x10));
        }

        if(text.length()/2<=31) keySize = text.length()/2 % 32;
        else  keySize = 32;

        for(int i = keySize-1; i>=0; i--)
        {
            SecretKey = SecretKey.add(cont.multiply(BigInteger.valueOf(secKey[i])));
            cont = cont.multiply(BigInteger.valueOf(0x100));
        }

        // Chave MOD P
        SecretKey = keys.modp(SecretKey, keys.p);
        return SecretKey;
    }

    //Gera uma chave privada a partir de uma string Hexadecimal resultante de uma HASH SHA256
    //Não devolve MOD P
    public BigInteger HashToBigInt (String text)
    {
        //int sizeStr256 = text.length()/8;
        int keySize;
        BigInteger SecretKey = new BigInteger("0");
        BigInteger cont = new BigInteger("1");
        char[] secKey;

        secKey = text.toCharArray();

        for (int i = 0; i < text.length(); i++)
        {
            switch (secKey[i])
            {
                case '0':
                    secKey[i] = 0;
                    break;
                case '1':
                    secKey[i] = 1;
                    break;
                case '2':
                    secKey[i] = 2;
                    break;
                case '3':
                    secKey[i] = 3;
                    break;
                case '4':
                    secKey[i] = 4;
                    break;
                case '5':
                    secKey[i] = 5;
                    break;
                case '6':
                    secKey[i] = 6;
                    break;
                case '7':
                    secKey[i] = 7;
                    break;
                case '8':
                    secKey[i] = 8;
                    break;
                case '9':
                    secKey[i] = 9;
                    break;
                case 'a':
                    secKey[i] = 10;
                    break;
                case 'b':
                    secKey[i] = 11;
                    break;
                case 'c':
                    secKey[i] = 12;
                    break;
                case 'd':
                    secKey[i] = 13;
                    break;
                case 'e':
                    secKey[i] = 14;
                    break;
                case 'f':
                    secKey[i] = 15;
                    break;
            }
        }
        //for(int i = (Hx.length()/2)-1; i>=0; i--)
        for(int i = 0; i < (text.length()/2); i++)
        {
            secKey [i] = (char) (secKey[2*i + 1] + (secKey[2*i] * 0x10));
        }

        if(text.length()/2<=31) keySize = text.length()/2 % 32;
        else  keySize = 32;

        for(int i = keySize-1; i>=0; i--)
        {
            SecretKey = SecretKey.add(cont.multiply(BigInteger.valueOf(secKey[i])));
            cont = cont.multiply(BigInteger.valueOf(0x100));
        }

        // Chave MOD P
        //SecretKey = keys.modp(SecretKey, keys.p);
        return SecretKey;
    }


    //Gera um BigInteger em Formato WIF
    public BigInteger HashToBigIntWIF (String text)
    {
        //int sizeStr256 = text.length()/8;
        int keySize;
        BigInteger SecretKey = new BigInteger("0");
        BigInteger cont = new BigInteger("1");
        char[] secKey;

        secKey = text.toCharArray();

        for (int i = 0; i < text.length(); i++)
        {
            switch (secKey[i])
            {
                case '0':
                    secKey[i] = 0;
                    break;
                case '1':
                    secKey[i] = 1;
                    break;
                case '2':
                    secKey[i] = 2;
                    break;
                case '3':
                    secKey[i] = 3;
                    break;
                case '4':
                    secKey[i] = 4;
                    break;
                case '5':
                    secKey[i] = 5;
                    break;
                case '6':
                    secKey[i] = 6;
                    break;
                case '7':
                    secKey[i] = 7;
                    break;
                case '8':
                    secKey[i] = 8;
                    break;
                case '9':
                    secKey[i] = 9;
                    break;
                case 'a':
                    secKey[i] = 10;
                    break;
                case 'b':
                    secKey[i] = 11;
                    break;
                case 'c':
                    secKey[i] = 12;
                    break;
                case 'd':
                    secKey[i] = 13;
                    break;
                case 'e':
                    secKey[i] = 14;
                    break;
                case 'f':
                    secKey[i] = 15;
                    break;
            }
        }
        //for(int i = (Hx.length()/2)-1; i>=0; i--)
        for(int i = 0; i < (text.length()/2); i++)
        {
            secKey [i] = (char) (secKey[2*i + 1] + (secKey[2*i] * 0x10));
        }

        //if(text.length()/2<=31) keySize = text.length()/2 % 32;
        //else  keySize = 32;

        for(int i = (text.length()/2)-1; i>=0; i--)
        {
            SecretKey = SecretKey.add(cont.multiply(BigInteger.valueOf(secKey[i])));
            cont = cont.multiply(BigInteger.valueOf(0x100));
        }

        // Chave MOD P
        //SecretKey = keys.modp(SecretKey, keys.p);
        return SecretKey;
    }




    //Produz uma chave privada em hexadecimal a partir de uma string qualquer
    public String encKeyToHexSTR (BigInteger [] point)
    {
        BigInteger[] pointx = new BigInteger[2];
        pointx[0] = point[0];
        pointx[1] = point[1];
        char[] pvtKeyHex = new char[64];
        char[] pvtKeyHex2 = new char[64];

        for(int i = 63 ; i>=0; i--)
        {
            //pvtKeyHex[i] = base16[(SecretKey.and(BigInteger.valueOf(15))).intValue()];
            pvtKeyHex[i] = base16[(pointx[0].and(BigInteger.valueOf(15))).intValue()];
            pvtKeyHex2[i] = base16[(pointx[1].and(BigInteger.valueOf(15))).intValue()];
            pointx[0] = pointx[0].divide(BigInteger.valueOf(16));
            pointx[1] = pointx[1].divide(BigInteger.valueOf(16));
        }

        //text = String.valueOf(pvtKey64);//necessario
        //text = String.valueOf(pvtKeyHex);//necessario

        //return text;

        return String.valueOf(pvtKeyHex) + String.valueOf(pvtKeyHex2);//necessario
    }

    //Converte um BigInteger em uma String Hexadecimal
    public String encKeyHalfToHexSTR (BigInteger point)
    {
        BigInteger pointx =  point;
        //pointx[1] = point[1];
        char[] pvtKeyHex = new char[64];
        char[] pvtKeyHex2 = new char[64];

        for(int i = 63 ; i>=0; i--)
        {
            //pvtKeyHex[i] = base16[(SecretKey.and(BigInteger.valueOf(15))).intValue()];
            pvtKeyHex[i] = base16[(pointx.and(BigInteger.valueOf(15))).intValue()];
            //pvtKeyHex2[i] = base16[(pointx[1].and(BigInteger.valueOf(15))).intValue()];
            pointx = pointx.divide(BigInteger.valueOf(16));
            //pointx[1] = pointx[1].divide(BigInteger.valueOf(16));
        }

        //text = String.valueOf(pvtKey64);//necessario
        //text = String.valueOf(pvtKeyHex);//necessario

        //return text;

        return String.valueOf(pvtKeyHex);//necessario
    }

    //Converte um BigInteger em uma String Hexadecimal
    public byte[] encKeyHalfToByteSTR (BigInteger point)
    {
        BigInteger pointx =  point;
        byte[] pvtKeyHex = new byte[32];
        //char[] pvtKeyHex = new char[64];

        //for(int i = 63 ; i>=0; i--)
        for(int i = 31 ; i>=0; i--)
        {
            //pvtKeyHex[i] = base16[(pointx.and(BigInteger.valueOf(15))).intValue()];
            pvtKeyHex[i] = (byte) (((pointx.and(BigInteger.valueOf(255))).intValue()) & 0xFF);
            //pointx = pointx.divide(BigInteger.valueOf(16));
            pointx = pointx.divide(BigInteger.valueOf(256));
        }
        //text = String.valueOf(pvtKey64);//necessario
        //text = String.valueOf(pvtKeyHex);//necessario
        //return text;

        //return String.valueOf(pvtKeyHex);//necessario
        return pvtKeyHex;//necessario
    }

    public BigInteger [] pubKeyRev (String pubkey)
    {
        BigInteger [] point = new BigInteger[2];
        point [0] = BigInteger.valueOf(0);
        point [1] = BigInteger.valueOf(0);
        BigInteger tA = BigInteger.valueOf(1);
        char [] pubKey1 = pubkey.toCharArray();

        if(pubkey.length() != 44 ) return point;

        //for(int i = 43; i>0; i--)
        for(int i = (pubkey.length()-1); i>((pubkey.length()-1)-43); i--)
        {
            int x;
            for(x=0; x<64; x++) if(base64[x] == pubKey1[i]) break;

            point[0] = point[0].add(tA.multiply(BigInteger.valueOf(x)));
            tA = tA.multiply(BigInteger.valueOf(64));
        }



        // Entcontra o valor de GBy atraves do algoritmo de raiz quadrada em campos finitos
        //   gBchIN64[1] = tonelli_shanks(modp((gBchIN64[0]*modp(gBchIN64[0]*gBchIN64[0], p) + A*gBchIN64[0] + B), p), p);

        //Operacao xor para decidir se escolhe y ou (p - y)
        //    if( ( !(gBchIN64[1] & 1) && (gBch64CAP[0] & 1) ) || ( (gBchIN64[1] & 1) && !(gBch64CAP[0] & 1) ) )
        //        gBchIN64[1] = p - gBchIN64[1];

        point[1] = sqrtCF.sqrtCF(keys.modp(
                ((point[0].multiply(keys.modp(point[0].multiply(point[0]),keys.p))).add((keys.A).multiply(point[0]))).add(keys.B)
                ,keys.p),keys.p);

        //if( ((point[1].and(BigInteger.valueOf(1))).compareTo(BigInteger.valueOf(0)) == 0 &&
        if( ((point[1].and(BigInteger.valueOf(1))).compareTo(BigInteger.valueOf(0)) == 0
                &&
                //(point[0].and(BigInteger.valueOf(1))).compareTo(BigInteger.valueOf(1)) == 0)
                ((pubKey1[0] & 0x01) == 1))   //point[0].and(BigInteger.valueOf(1))).compareTo(BigInteger.valueOf(1)) == 0)
                ||
                ((point[1].and(BigInteger.valueOf(1))).compareTo(BigInteger.valueOf(1)) == 0
                        &&
                        //(point[0].and(BigInteger.valueOf(1))).compareTo(BigInteger.valueOf(0)) == 0)  )
                        ((pubKey1[0] & 0x01) != 1))  // (point[0].and(BigInteger.valueOf(1))).compareTo(BigInteger.valueOf(0)) == 0)
        )

            point[1] = (keys.p).subtract(point[1]);

        //gBchIN64[1] = tonelli_shanks(modp((gBchIN64[0]*modp(gBchIN64[0]*gBchIN64[0], p) + A*gBchIN64[0] + B), p), p);
        /*
        cpp_int gBchIN64[2];
        // Converte de Base 64 para inteiro
        gBchIN64[0] = 0;
        tA = 1;

        for (int i = 43; i>0; i--)
        {
            gBchIN64[0] = gBchIN64[0] + tA*toint64(gBch64CAP[i]);;
            tA = tA*64;
        }
        // Entcontra o valor de GBy atraves do algoritmo de raiz quadrada em campos finitos
        gBchIN64[1] = tonelli_shanks(modp((gBchIN64[0]*modp(gBchIN64[0]*gBchIN64[0], p) + A*gBchIN64[0] + B), p), p);

        //Operacao xor para decidir se escolhe y ou (p - y)
        if( ( !(gBchIN64[1] & 1) && (gBch64CAP[0] & 1) ) || ( (gBchIN64[1] & 1) && !(gBch64CAP[0] & 1) ) )
            gBchIN64[1] = p - gBchIN64[1];
            */
        return point;
    }

    public BigInteger [] pubKeyCompSECRev (String pubkey)
    {
        BigInteger [] point = new BigInteger[2];
        point [0] = BigInteger.valueOf(0);
        point [1] = BigInteger.valueOf(0);
        BigInteger tA = BigInteger.valueOf(1);

        pubkey = pubkey.substring(1);

        char [] pubKey1 = pubkey.toCharArray();

        //if(pubkey.length() != 44 ) return point;
        if(pubkey.length() != 65 ) return point;

        //for(int i = 43; i>0; i--)
        //for(int i = (pubkey.length()-1); i>((pubkey.length()-1)-43); i--)
        for(int i = (pubkey.length()-1); i>((pubkey.length()-1)-64); i--)
        {
            int x;
            for(x=0; x<16; x++) if(base16[x] == pubKey1[i]) break;

            point[0] = point[0].add(tA.multiply(BigInteger.valueOf(x)));
            tA = tA.multiply(BigInteger.valueOf(16));
        }



        // Entcontra o valor de GBy atraves do algoritmo de raiz quadrada em campos finitos
        //   gBchIN64[1] = tonelli_shanks(modp((gBchIN64[0]*modp(gBchIN64[0]*gBchIN64[0], p) + A*gBchIN64[0] + B), p), p);

        //Operacao xor para decidir se escolhe y ou (p - y)
        //    if( ( !(gBchIN64[1] & 1) && (gBch64CAP[0] & 1) ) || ( (gBchIN64[1] & 1) && !(gBch64CAP[0] & 1) ) )
        //        gBchIN64[1] = p - gBchIN64[1];

        point[1] = sqrtCF.sqrtCF(keys.modp(
                ((point[0].multiply(keys.modp(point[0].multiply(point[0]),keys.p))).add((keys.A).multiply(point[0]))).add(keys.B)
                ,keys.p),keys.p);

        //if( ((point[1].and(BigInteger.valueOf(1))).compareTo(BigInteger.valueOf(0)) == 0 &&
        if( ((point[1].and(BigInteger.valueOf(1))).compareTo(BigInteger.valueOf(0)) == 0
                &&
                //(point[0].and(BigInteger.valueOf(1))).compareTo(BigInteger.valueOf(1)) == 0)
                ((pubKey1[0] & 0x01) == 1))   //point[0].and(BigInteger.valueOf(1))).compareTo(BigInteger.valueOf(1)) == 0)
                ||
                ((point[1].and(BigInteger.valueOf(1))).compareTo(BigInteger.valueOf(1)) == 0
                        &&
                        //(point[0].and(BigInteger.valueOf(1))).compareTo(BigInteger.valueOf(0)) == 0)  )
                        ((pubKey1[0] & 0x01) != 1))  // (point[0].and(BigInteger.valueOf(1))).compareTo(BigInteger.valueOf(0)) == 0)
        )

            point[1] = (keys.p).subtract(point[1]);

        //gBchIN64[1] = tonelli_shanks(modp((gBchIN64[0]*modp(gBchIN64[0]*gBchIN64[0], p) + A*gBchIN64[0] + B), p), p);
        /*
        cpp_int gBchIN64[2];
        // Converte de Base 64 para inteiro
        gBchIN64[0] = 0;
        tA = 1;

        for (int i = 43; i>0; i--)
        {
            gBchIN64[0] = gBchIN64[0] + tA*toint64(gBch64CAP[i]);;
            tA = tA*64;
        }
        // Entcontra o valor de GBy atraves do algoritmo de raiz quadrada em campos finitos
        gBchIN64[1] = tonelli_shanks(modp((gBchIN64[0]*modp(gBchIN64[0]*gBchIN64[0], p) + A*gBchIN64[0] + B), p), p);

        //Operacao xor para decidir se escolhe y ou (p - y)
        if( ( !(gBchIN64[1] & 1) && (gBch64CAP[0] & 1) ) || ( (gBchIN64[1] & 1) && !(gBch64CAP[0] & 1) ) )
            gBchIN64[1] = p - gBchIN64[1];
            */
        return point;
    }

    public BigInteger [] pubKeyUncompSECRev (String pubkey)
    {

        BigInteger [] point = new BigInteger[2];
        point [0] = BigInteger.valueOf(0);
        point [1] = BigInteger.valueOf(0);
        BigInteger tA = BigInteger.valueOf(1);

        pubkey = pubkey.substring(1);
        char [] pubKey1 = pubkey.toCharArray();

        //if(pubkey.length() != 44 ) return point;
        if(pubkey.length() != 65 + 64) return point;

        //for(int i = 43; i>0; i--)
        //for(int i = (pubkey.length()-1); i>((pubkey.length()-1)-43); i--)
        //for(int i = (pubkey.length()-1); i>((pubkey.length()-1)-64); i--)
        /*

        for(int i = (pubkey.length()-1); i>((pubkey.length()-1)-64); i--)
        {
            int x;
            for(x=0; x<16; x++) if(base16[x] == pubKey1[i]) break;

            point[0] = point[0].add(tA.multiply(BigInteger.valueOf(x)));
            tA = tA.multiply(BigInteger.valueOf(16));
        }

        */
        for(int i = (pubkey.length()-1); i>((pubkey.length()-1)-64); i--)
        {
            int x;
            for(x=0; x<16; x++) if(base16[x] == pubKey1[i]) break;

            point[1] = point[1].add(tA.multiply(BigInteger.valueOf(x)));
            tA = tA.multiply(BigInteger.valueOf(16));
        }

        tA = BigInteger.valueOf(1);

        for(int i = (pubkey.length()-1)-64; i>((pubkey.length()-1)-128); i--)
        {
            int x;
            for(x=0; x<16; x++) if(base16[x] == pubKey1[i]) break;

            point[0] = point[0].add(tA.multiply(BigInteger.valueOf(x)));
            tA = tA.multiply(BigInteger.valueOf(16));
        }


        return point;
    }

    public String bsvWallet (String pubkeyCOD)
    {
        BigInteger[] pubKey = pubKeyRev(pubkeyCOD);
        byte[] chA = new byte[64];
        byte[] cSHA = new byte[33]; // chave comprimida
        byte[] cSHA2 = new byte[65]; // chave não comprimida

        BigInteger tA, ichA, ichA2;
        //pubkeyCOD = pubKey[0].toString() + "\n" + pubKey[1].toString();

////////////////////////////////////////////////////////////////////
//CONSTROI AS CHAVES PUBLICAS COMPRIMIDAS E NAO COMPRIMIDAS
////////////////////////////////////////////////////////////////////

        ichA = pubKey [0];
        ichA2 = pubKey [1];
//        pubkeyCOD = ichA.toString() + "\n"+ ichA2.toString();

        for (int i = 32; i>= 1; i--)
        {
            // este cast pode dar problemas em UBUNTU
            //cSHA2[i] = cSHA[i] = (byte) (ichA & 0xFF);
            cSHA2[i] = cSHA[i] = (byte) (((ichA.and(BigInteger.valueOf(0xFF))).intValue()) & 0xFF);
            //cSHA2[i+32] = (unsigned char)(ichA2 & 255);
            cSHA2[i+32] = (byte) (((ichA2.and(BigInteger.valueOf(0xFF))).intValue()) & 0xFF);

            //ichA = ichA/256;
            ichA = ichA.divide(BigInteger.valueOf(0x100));
            //ichA2 = ichA2/256;
            ichA2 = ichA2.divide(BigInteger.valueOf(0x100));
        }

        //if (g[1] & 1) cSHA [0] = 3;
        if ((pubKey[1].and(BigInteger.valueOf(1))).intValue() == 1) cSHA [0] = 3;
        else cSHA [0] = 2;
        cSHA2 [0] = 4;

////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////
//HASH SHA256 DA CHAVE PUBLICA COMPRIMIDA
////////////////////////////////////////////////////////////////////

        //unsigned char SHA256out[32], SHA256out2[32];

        //byte[] SHA256out = new byte[32];
        //byte[] SHA256out2 = new byte[32];
        String SHA256out, SHA256out2;

        //size_t in_len = sizeof(cSHA);
        //size_t in_len2 = sizeof(cSHA2);

        //Hash SHA256
        //mbedtls_sha256(cSHA, in_len, SHA256out, 0);

        SHA256out = SHA256G.SHA256bytes(cSHA);
        //mbedtls_sha256(cSHA2, in_len2, SHA256out2, 0);
        SHA256out2 = SHA256G.SHA256bytes(cSHA2);

        //pubkeyCOD = SHA256out2;

////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////
//HASH RIPEMD160 DA SHA256 DA CHAVE PUBLICA
////////////////////////////////////////////////////////////////////

        //unsigned char RIPEMDout[20], RIPEMDout2[20];

        String RIPEMDout, RIPEMDout2;
        Ripemd160 rmd160 = new Ripemd160();

        //in_len = sizeof(SHA256out);
        //in_len2 = sizeof(SHA256out2);

        //mbedtls_ripemd160(SHA256out, in_len, RIPEMDout);

        byte[] SHA256outb;// = new char[SHA256out.length()/2];
        byte[] SHA256out2b;// = new char[SHA256out2.length()/2];
        SHA256outb = SHA256G.HashStrToByte2(SHA256out);
        SHA256out2b = SHA256G.HashStrToByte2(SHA256out2);
        char[] SHA256outc = new char[SHA256out.length()/2];
        char[] SHA256out2c = new char[SHA256out2.length()/2];
        for (int i=0;i<SHA256out.length()/2 ; i++) SHA256outc [i] = (char) (SHA256outb[i] & 0xFF);
        for (int i=0;i<SHA256out2.length()/2 ; i++) SHA256out2c [i] = (char) (SHA256out2b[i] & 0xFF);


        //RIPEMDout = Ripemd160.HashCharToStr(rmd160.ripemd160(SHA256out.toCharArray(), SHA256out.length()));
        RIPEMDout = Ripemd160.HashCharToStr(rmd160.ripemd160(SHA256outc, SHA256out.length()/2));

        //mbedtls_ripemd160(SHA256out2, in_len2, RIPEMDout2);

        //RIPEMDout2 = Ripemd160.HashCharToStr(rmd160.ripemd160(SHA256out2.toCharArray(), SHA256out2.length()));
        RIPEMDout2 = Ripemd160.HashCharToStr(rmd160.ripemd160(SHA256out2c, SHA256out2.length()/2));
//        pubkeyCOD = RIPEMDout2;

////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////
//PREFIX PLUS CHECKSUM
////////////////////////////////////////////////////////////////////

        //unsigned char pRIPEMD160[21], pRIPEMD1602[21];
        byte[] pRIPEMD160 = new byte[21];
        byte[] pRIPEMD1602 = new byte[21];

        byte[] RIPEMDoutj = new byte[RIPEMDout.length()];
        byte[] RIPEMDout2j = new byte[RIPEMDout2.length()];

        RIPEMDoutj = SHA256G.HashStrToByte2(RIPEMDout);
        RIPEMDout2j = SHA256G.HashStrToByte2(RIPEMDout2);

        //in_len = sizeof(pRIPEMD160);
        //in_len2 = sizeof(pRIPEMD1602);

        //Acrescenta o byte de versao do BTC, neste caso 0x00
        pRIPEMD1602 [0] = pRIPEMD160 [0] = 0;

        for (int i = 1; i<21; i++)
        {
            //pRIPEMD160 [i] = RIPEMDout[i-1];
            pRIPEMD160 [i] = RIPEMDoutj[i-1];

            //pRIPEMD1602 [i] = RIPEMDout2[i-1];
            pRIPEMD1602 [i] = RIPEMDout2j[i-1];
        }

        //SHAS256 (PREFIX + RIPEMD(SHA256(COMPRESSED PUB KEY)))
        //mbedtls_sha256(pRIPEMD160, in_len, SHA256out, 0);
        SHA256out = SHA256G.SHA256bytes(pRIPEMD160);

        //SHAS256 (PREFIX + RIPEMD(SHA256(UNCOMPRESSED PUB KEY)))
        //mbedtls_sha256(pRIPEMD1602, in_len2, SHA256out2, 0);
        SHA256out2 = SHA256G.SHA256bytes(pRIPEMD1602);

        //in_len = sizeof(SHA256out);
        //in_len2 = sizeof(SHA256out2);

        //SHA256 (SHAS256 (PREFIX + RIPEMD(SHA256(COMPRESSED PUB KEY))))
        //mbedtls_sha256(SHA256out, in_len, SHA256out, 0);
        SHA256out = SHA256G.SHA256bytes(SHA256G.HashStrToByte2(SHA256out));
        //SHA256 (SHAS256 (PREFIX + RIPEMD(SHA256(UNCOMPRESSED PUB KEY))))
        //mbedtls_sha256(SHA256out2, in_len2, SHA256out2, 0);
        SHA256out2 = SHA256G.SHA256bytes(SHA256G.HashStrToByte2(SHA256out2));

        //pubkeyCOD = SHA256out2;

////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////
//PREPARA O BIGINT PARA CONVERSAO A BASE 58
////////////////////////////////////////////////////////////////////

        //Transformar o Payload em BIGNUMBER
        //cpp_int VPC = 0, VPCcont = 1, VPC2 = 0, VPCcont2 = 1;
        //cpp_int VPC = 0, VPCcont = 1, VPC2 = 0, VPCcont2 = 1;
        BigInteger VPC, VPCcont, VPC2, VPCcont2;
        VPC = BigInteger.valueOf(0);
        VPCcont = BigInteger.valueOf(1);
        VPC2 = BigInteger.valueOf(0);
        VPCcont2 = BigInteger.valueOf(1);

        byte[] SHA256outj = new byte[SHA256out.length()/2];
        byte[] SHA256out2j = new byte[SHA256out2.length()/2];

        SHA256outj = SHA256G.HashStrToByte2(SHA256out);
        SHA256out2j = SHA256G.HashStrToByte2(SHA256out2);


        //Primeiros 4 bites do SHA256 (SHAS256 (PREFIX + RIPEMD(SHA256(PUB KEY))))
        //Adiciona o CHEKSUM ao BIGINT
        for(int i = 3; i >= 0; i--)
        {
            //VPC = VPC + VPCcont * SHA256out[i];
            //VPC = VPC + VPCcont * SHA256out[i];
            VPC = VPC.add(VPCcont.multiply(BigInteger.valueOf(SHA256outj[i] & 0xFF)));
            //VPCcont = VPCcont * 256;
            VPCcont = VPCcont.multiply(BigInteger.valueOf(256));


            //VPC2 = VPC2 + VPCcont2 * SHA256out2[i];
            VPC2 = VPC2.add(VPCcont2.multiply(BigInteger.valueOf(SHA256out2j[i] & 0xFF)));
            //VPCcont2 = VPCcont2 * 256;
            VPCcont2 = VPCcont2.multiply(BigInteger.valueOf(256));
        }

        //Adiciona o PREFIXO + RIPEMD160 ao BIGINT
        for(int i = 20; i >= 0; i--)
        {
            //VPC = VPC + VPCcont * pRIPEMD160[i];
            VPC = VPC.add(VPCcont.multiply(BigInteger.valueOf(pRIPEMD160[i] & 0xFF)));
            //VPCcont = VPCcont * 256;
            VPCcont = VPCcont.multiply(BigInteger.valueOf(256));

            //VPC2 = VPC2 + VPCcont2 * pRIPEMD1602[i];
            VPC2 = VPC2.add(VPCcont2.multiply(BigInteger.valueOf(pRIPEMD1602[i] & 0xFF)));
            //VPCcont2 = VPCcont2 * 256;
            VPCcont2 = VPCcont2.multiply(BigInteger.valueOf(256));
        }

        //pubkeyCOD = VPC2.toString();

////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////
//CONVERTE O BIGINT PARA BASE 58
////////////////////////////////////////////////////////////////////

        //unsigned char base58[] = "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz";
        String base58s = "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz";
        char[] base58 = base58s.toCharArray();
        //unsigned char BTCaddr[100], BTCaddr2[100];
        char[] BTCaddr = new char[100];
        char[] BTCaddr2 = new char[100];

        int li = 0, li2 = 0;

        //while (VPC > 0)
        while (VPC.compareTo(BigInteger.valueOf(0)) == 1)
        {
            //BTCaddr [li] = base58[(int)(VPC % 58)];
            BTCaddr [li] = base58[(VPC.mod(BigInteger.valueOf(58))).intValue()];
            //VPC = VPC / 58;
            VPC = VPC.divide(BigInteger.valueOf(58));
            li ++;
        }

        //while (VPC2 > 0)
        while (VPC2.compareTo(BigInteger.valueOf(0)) == 1)
        {
            //BTCaddr2 [li2] = base58[(int)(VPC2 % 58)];
            BTCaddr2 [li2] = base58[(VPC2.mod(BigInteger.valueOf(58))).intValue()];
            //VPC2 = VPC2 / 58;
            VPC2 = VPC2.divide(BigInteger.valueOf(58));
            li2 ++;
        }

        //pubkeyCOD = String.valueOf(BTCaddr2);
////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////


////////////////////////////////////////////////////////////////////
//ADICIONA OS LEADING ZEROES DO PREFIX + RIPEMED160
////////////////////////////////////////////////////////////////////

        int lzeros = 0, lzeros2 = 0;

        while (pRIPEMD160[lzeros] == 0)
        {
            BTCaddr [li] = base58[0];
            lzeros++;
            li++;
        }
        while (pRIPEMD1602[lzeros2] == 0)
        {
            BTCaddr2 [li2] = base58[0];
            lzeros2++;
            li2++;
        }

        //pubkeyCOD = String.valueOf(BTCaddr2);

////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////
//INVERTE A ORDEM DA MATRIZ DE ENDERECO E ADICIONA O FINALIZADOR
////////////////////////////////////////////////////////////////////

        char[] BTCaddrUNC = new char[li2];
        char[] BTCaddrCOMP = new char[li];

        // ENDERECO NAO COMPRIMIDO
        int iout = 0;
        li2--;
        while (li2>=0)
        {
            BTCaddrUNC[iout] = BTCaddr2 [li2];
            iout++;
            li2--;
        }
        //FINALIZADOR PARA FUTURA COMPARACAO
        //BTCaddrUNC[iout] = 0;

        // ENDERECO COMPRIMIDO
        iout = 0;
        li--;
        while (li>=0)
        {
            BTCaddrCOMP[iout] = BTCaddr [li];
            iout++;
            li--;
        }
        //FINALIZADOR PARA FUTURA COMPARACAO
        //BTCaddrCOMP[iout] = 0;

////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////
        /*
         */
//        return pubkeyCOD;
        //return (String.valueOf(BTCaddrUNC) + "\n" + String.valueOf(BTCaddrCOMP));
        return String.valueOf(BTCaddrUNC);
    }


    public String bsvWalletFull (String pubkeyCOD, Boolean Compressed)
    {
        BigInteger[] pubKey = pubKeyRev(pubkeyCOD);
        byte[] chA = new byte[64];
        byte[] cSHA = new byte[33]; // chave comprimida
        byte[] cSHA2 = new byte[65]; // chave não comprimida

        BigInteger tA, ichA, ichA2;
        //pubkeyCOD = pubKey[0].toString() + "\n" + pubKey[1].toString();

////////////////////////////////////////////////////////////////////
//CONSTROI AS CHAVES PUBLICAS COMPRIMIDAS E NAO COMPRIMIDAS
////////////////////////////////////////////////////////////////////

        ichA = pubKey [0];
        ichA2 = pubKey [1];
//        pubkeyCOD = ichA.toString() + "\n"+ ichA2.toString();

        for (int i = 32; i>= 1; i--)
        {
            // este cast pode dar problemas em UBUNTU
            //cSHA2[i] = cSHA[i] = (byte) (ichA & 0xFF);
            cSHA2[i] = cSHA[i] = (byte) (((ichA.and(BigInteger.valueOf(0xFF))).intValue()) & 0xFF);
            //cSHA2[i+32] = (unsigned char)(ichA2 & 255);
            cSHA2[i+32] = (byte) (((ichA2.and(BigInteger.valueOf(0xFF))).intValue()) & 0xFF);

            //ichA = ichA/256;
            ichA = ichA.divide(BigInteger.valueOf(0x100));
            //ichA2 = ichA2/256;
            ichA2 = ichA2.divide(BigInteger.valueOf(0x100));
        }

        //if (g[1] & 1) cSHA [0] = 3;
        if ((pubKey[1].and(BigInteger.valueOf(1))).intValue() == 1) cSHA [0] = 3;
        else cSHA [0] = 2;
        cSHA2 [0] = 4;

////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////
//HASH SHA256 DA CHAVE PUBLICA COMPRIMIDA
////////////////////////////////////////////////////////////////////

        //unsigned char SHA256out[32], SHA256out2[32];

        //byte[] SHA256out = new byte[32];
        //byte[] SHA256out2 = new byte[32];
        String SHA256out, SHA256out2;

        //size_t in_len = sizeof(cSHA);
        //size_t in_len2 = sizeof(cSHA2);

        //Hash SHA256
        //mbedtls_sha256(cSHA, in_len, SHA256out, 0);

        SHA256out = SHA256G.SHA256bytes(cSHA);
        //mbedtls_sha256(cSHA2, in_len2, SHA256out2, 0);
        SHA256out2 = SHA256G.SHA256bytes(cSHA2);

        //pubkeyCOD = SHA256out2;

////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////
//HASH RIPEMD160 DA SHA256 DA CHAVE PUBLICA
////////////////////////////////////////////////////////////////////

        //unsigned char RIPEMDout[20], RIPEMDout2[20];

        String RIPEMDout, RIPEMDout2;
        Ripemd160 rmd160 = new Ripemd160();

        //in_len = sizeof(SHA256out);
        //in_len2 = sizeof(SHA256out2);

        //mbedtls_ripemd160(SHA256out, in_len, RIPEMDout);

        byte[] SHA256outb;// = new char[SHA256out.length()/2];
        byte[] SHA256out2b;// = new char[SHA256out2.length()/2];
        SHA256outb = SHA256G.HashStrToByte2(SHA256out);
        SHA256out2b = SHA256G.HashStrToByte2(SHA256out2);
        char[] SHA256outc = new char[SHA256out.length()/2];
        char[] SHA256out2c = new char[SHA256out2.length()/2];
        for (int i=0;i<SHA256out.length()/2 ; i++) SHA256outc [i] = (char) (SHA256outb[i] & 0xFF);
        for (int i=0;i<SHA256out2.length()/2 ; i++) SHA256out2c [i] = (char) (SHA256out2b[i] & 0xFF);


        //RIPEMDout = Ripemd160.HashCharToStr(rmd160.ripemd160(SHA256out.toCharArray(), SHA256out.length()));
        RIPEMDout = Ripemd160.HashCharToStr(rmd160.ripemd160(SHA256outc, SHA256out.length()/2));

        //mbedtls_ripemd160(SHA256out2, in_len2, RIPEMDout2);

        //RIPEMDout2 = Ripemd160.HashCharToStr(rmd160.ripemd160(SHA256out2.toCharArray(), SHA256out2.length()));
        RIPEMDout2 = Ripemd160.HashCharToStr(rmd160.ripemd160(SHA256out2c, SHA256out2.length()/2));
//        pubkeyCOD = RIPEMDout2;

////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////
//PREFIX PLUS CHECKSUM
////////////////////////////////////////////////////////////////////

        //unsigned char pRIPEMD160[21], pRIPEMD1602[21];
        byte[] pRIPEMD160 = new byte[21];
        byte[] pRIPEMD1602 = new byte[21];

        byte[] RIPEMDoutj = new byte[RIPEMDout.length()];
        byte[] RIPEMDout2j = new byte[RIPEMDout2.length()];

        RIPEMDoutj = SHA256G.HashStrToByte2(RIPEMDout);
        RIPEMDout2j = SHA256G.HashStrToByte2(RIPEMDout2);

        //in_len = sizeof(pRIPEMD160);
        //in_len2 = sizeof(pRIPEMD1602);

        //Acrescenta o byte de versao do BTC, neste caso 0x00
        pRIPEMD1602 [0] = pRIPEMD160 [0] = 0;

        for (int i = 1; i<21; i++)
        {
            //pRIPEMD160 [i] = RIPEMDout[i-1];
            pRIPEMD160 [i] = RIPEMDoutj[i-1];

            //pRIPEMD1602 [i] = RIPEMDout2[i-1];
            pRIPEMD1602 [i] = RIPEMDout2j[i-1];
        }

        //SHAS256 (PREFIX + RIPEMD(SHA256(COMPRESSED PUB KEY)))
        //mbedtls_sha256(pRIPEMD160, in_len, SHA256out, 0);
        SHA256out = SHA256G.SHA256bytes(pRIPEMD160);

        //SHAS256 (PREFIX + RIPEMD(SHA256(UNCOMPRESSED PUB KEY)))
        //mbedtls_sha256(pRIPEMD1602, in_len2, SHA256out2, 0);
        SHA256out2 = SHA256G.SHA256bytes(pRIPEMD1602);

        //in_len = sizeof(SHA256out);
        //in_len2 = sizeof(SHA256out2);

        //SHA256 (SHAS256 (PREFIX + RIPEMD(SHA256(COMPRESSED PUB KEY))))
        //mbedtls_sha256(SHA256out, in_len, SHA256out, 0);
        SHA256out = SHA256G.SHA256bytes(SHA256G.HashStrToByte2(SHA256out));
        //SHA256 (SHAS256 (PREFIX + RIPEMD(SHA256(UNCOMPRESSED PUB KEY))))
        //mbedtls_sha256(SHA256out2, in_len2, SHA256out2, 0);
        SHA256out2 = SHA256G.SHA256bytes(SHA256G.HashStrToByte2(SHA256out2));

        //pubkeyCOD = SHA256out2;

////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////
//PREPARA O BIGINT PARA CONVERSAO A BASE 58
////////////////////////////////////////////////////////////////////

        //Transformar o Payload em BIGNUMBER
        //cpp_int VPC = 0, VPCcont = 1, VPC2 = 0, VPCcont2 = 1;
        //cpp_int VPC = 0, VPCcont = 1, VPC2 = 0, VPCcont2 = 1;
        BigInteger VPC, VPCcont, VPC2, VPCcont2;
        VPC = BigInteger.valueOf(0);
        VPCcont = BigInteger.valueOf(1);
        VPC2 = BigInteger.valueOf(0);
        VPCcont2 = BigInteger.valueOf(1);

        byte[] SHA256outj = new byte[SHA256out.length()/2];
        byte[] SHA256out2j = new byte[SHA256out2.length()/2];

        SHA256outj = SHA256G.HashStrToByte2(SHA256out);
        SHA256out2j = SHA256G.HashStrToByte2(SHA256out2);


        //Primeiros 4 bites do SHA256 (SHAS256 (PREFIX + RIPEMD(SHA256(PUB KEY))))
        //Adiciona o CHEKSUM ao BIGINT
        for(int i = 3; i >= 0; i--)
        {
            //VPC = VPC + VPCcont * SHA256out[i];
            //VPC = VPC + VPCcont * SHA256out[i];
            VPC = VPC.add(VPCcont.multiply(BigInteger.valueOf(SHA256outj[i] & 0xFF)));
            //VPCcont = VPCcont * 256;
            VPCcont = VPCcont.multiply(BigInteger.valueOf(256));


            //VPC2 = VPC2 + VPCcont2 * SHA256out2[i];
            VPC2 = VPC2.add(VPCcont2.multiply(BigInteger.valueOf(SHA256out2j[i] & 0xFF)));
            //VPCcont2 = VPCcont2 * 256;
            VPCcont2 = VPCcont2.multiply(BigInteger.valueOf(256));
        }

        //Adiciona o PREFIXO + RIPEMD160 ao BIGINT
        for(int i = 20; i >= 0; i--)
        {
            //VPC = VPC + VPCcont * pRIPEMD160[i];
            VPC = VPC.add(VPCcont.multiply(BigInteger.valueOf(pRIPEMD160[i] & 0xFF)));
            //VPCcont = VPCcont * 256;
            VPCcont = VPCcont.multiply(BigInteger.valueOf(256));

            //VPC2 = VPC2 + VPCcont2 * pRIPEMD1602[i];
            VPC2 = VPC2.add(VPCcont2.multiply(BigInteger.valueOf(pRIPEMD1602[i] & 0xFF)));
            //VPCcont2 = VPCcont2 * 256;
            VPCcont2 = VPCcont2.multiply(BigInteger.valueOf(256));
        }

        //pubkeyCOD = VPC2.toString();

////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////
//CONVERTE O BIGINT PARA BASE 58
////////////////////////////////////////////////////////////////////

        //unsigned char base58[] = "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz";
        String base58s = "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz";
        char[] base58 = base58s.toCharArray();
        //unsigned char BTCaddr[100], BTCaddr2[100];
        char[] BTCaddr = new char[100];
        char[] BTCaddr2 = new char[100];

        int li = 0, li2 = 0;

        //while (VPC > 0)
        while (VPC.compareTo(BigInteger.valueOf(0)) == 1)
        {
            //BTCaddr [li] = base58[(int)(VPC % 58)];
            BTCaddr [li] = base58[(VPC.mod(BigInteger.valueOf(58))).intValue()];
            //VPC = VPC / 58;
            VPC = VPC.divide(BigInteger.valueOf(58));
            li ++;
        }

        //while (VPC2 > 0)
        while (VPC2.compareTo(BigInteger.valueOf(0)) == 1)
        {
            //BTCaddr2 [li2] = base58[(int)(VPC2 % 58)];
            BTCaddr2 [li2] = base58[(VPC2.mod(BigInteger.valueOf(58))).intValue()];
            //VPC2 = VPC2 / 58;
            VPC2 = VPC2.divide(BigInteger.valueOf(58));
            li2 ++;
        }

        //pubkeyCOD = String.valueOf(BTCaddr2);
////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////


////////////////////////////////////////////////////////////////////
//ADICIONA OS LEADING ZEROES DO PREFIX + RIPEMED160
////////////////////////////////////////////////////////////////////

        int lzeros = 0, lzeros2 = 0;

        while (pRIPEMD160[lzeros] == 0)
        {
            BTCaddr [li] = base58[0];
            lzeros++;
            li++;
        }
        while (pRIPEMD1602[lzeros2] == 0)
        {
            BTCaddr2 [li2] = base58[0];
            lzeros2++;
            li2++;
        }

        //pubkeyCOD = String.valueOf(BTCaddr2);

////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////
//INVERTE A ORDEM DA MATRIZ DE ENDERECO E ADICIONA O FINALIZADOR
////////////////////////////////////////////////////////////////////

        char[] BTCaddrUNC = new char[li2];
        char[] BTCaddrCOMP = new char[li];

        // ENDERECO NAO COMPRIMIDO
        int iout = 0;
        li2--;
        while (li2>=0)
        {
            BTCaddrUNC[iout] = BTCaddr2 [li2];
            iout++;
            li2--;
        }
        //FINALIZADOR PARA FUTURA COMPARACAO
        //BTCaddrUNC[iout] = 0;

        // ENDERECO COMPRIMIDO
        iout = 0;
        li--;
        while (li>=0)
        {
            BTCaddrCOMP[iout] = BTCaddr [li];
            iout++;
            li--;
        }
        //FINALIZADOR PARA FUTURA COMPARACAO
        //BTCaddrCOMP[iout] = 0;

////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////
        /*
         */
//        return pubkeyCOD;

        if(Compressed)
            return String.valueOf(BTCaddrCOMP);
        else
            return String.valueOf(BTCaddrUNC);

        //return (String.valueOf(BTCaddrUNC) + "\n" + String.valueOf(BTCaddrCOMP));
        //return String.valueOf(BTCaddrUNC);
    }

    public String addRMD (String hashKey)
    {
        byte[] result = new byte[20];
        char[] strChar = new char[hashKey.length()];
        strChar = hashKey.toCharArray();

        BigInteger addrINT;
        addrINT = BigInteger.valueOf(0);
        int x;
        char[] base58begin = new char["123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz".length()];
        base58begin = "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz".toCharArray();

        for(int j = 1; j < hashKey.length(); j++)
        {
            for(x=0;base58begin[x]!= strChar[j];x++);

            addrINT = addrINT.multiply(BigInteger.valueOf(58));
            addrINT = addrINT.add(BigInteger.valueOf(x));
        }

        addrINT = addrINT.divide(BigInteger.valueOf(0x100000000L));
        for(int j = 19; j >=0; j--)
        {
            //int bint = addrINT.and(BigInteger.valueOf(0xFF)).intValue();

            result[j] = (byte) (addrINT.and(BigInteger.valueOf(0xFF)).intValue() & 0xFF );
            //rmd160[i][j] = (unsigned char)(addrINT & 0xFF);
            addrINT = addrINT.divide(BigInteger.valueOf(0x100));
        }

        //return  String.valueOf(result);
        return  SHA256G.ByteToStrHex(result);
        //return  String.valueOf(strChar);
    }

    public String bsvWalletRMD160 (String pubkeyCOD, Boolean Compressed)
    {
        BigInteger[] pubKey = pubKeyRev(pubkeyCOD);
        byte[] chA = new byte[64];
        byte[] cSHA = new byte[33]; // chave comprimida
        byte[] cSHA2 = new byte[65]; // chave não comprimida

        BigInteger tA, ichA, ichA2;
        //pubkeyCOD = pubKey[0].toString() + "\n" + pubKey[1].toString();

////////////////////////////////////////////////////////////////////
//CONSTROI AS CHAVES PUBLICAS COMPRIMIDAS E NAO COMPRIMIDAS
////////////////////////////////////////////////////////////////////

        ichA = pubKey [0];
        ichA2 = pubKey [1];
//        pubkeyCOD = ichA.toString() + "\n"+ ichA2.toString();

        for (int i = 32; i>= 1; i--)
        {
            // este cast pode dar problemas em UBUNTU
            //cSHA2[i] = cSHA[i] = (byte) (ichA & 0xFF);
            cSHA2[i] = cSHA[i] = (byte) (((ichA.and(BigInteger.valueOf(0xFF))).intValue()) & 0xFF);
            //cSHA2[i+32] = (unsigned char)(ichA2 & 255);
            cSHA2[i+32] = (byte) (((ichA2.and(BigInteger.valueOf(0xFF))).intValue()) & 0xFF);

            //ichA = ichA/256;
            ichA = ichA.divide(BigInteger.valueOf(0x100));
            //ichA2 = ichA2/256;
            ichA2 = ichA2.divide(BigInteger.valueOf(0x100));
        }

        //if (g[1] & 1) cSHA [0] = 3;
        if ((pubKey[1].and(BigInteger.valueOf(1))).intValue() == 1) cSHA [0] = 3;
        else cSHA [0] = 2;
        cSHA2 [0] = 4;

////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////
//HASH SHA256 DA CHAVE PUBLICA COMPRIMIDA
////////////////////////////////////////////////////////////////////

        //unsigned char SHA256out[32], SHA256out2[32];

        //byte[] SHA256out = new byte[32];
        //byte[] SHA256out2 = new byte[32];
        String SHA256out, SHA256out2;

        //size_t in_len = sizeof(cSHA);
        //size_t in_len2 = sizeof(cSHA2);

        //Hash SHA256
        //mbedtls_sha256(cSHA, in_len, SHA256out, 0);

        SHA256out = SHA256G.SHA256bytes(cSHA);
        //mbedtls_sha256(cSHA2, in_len2, SHA256out2, 0);
        SHA256out2 = SHA256G.SHA256bytes(cSHA2);

        //pubkeyCOD = SHA256out2;

////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////
//HASH RIPEMD160 DA SHA256 DA CHAVE PUBLICA
////////////////////////////////////////////////////////////////////

        //unsigned char RIPEMDout[20], RIPEMDout2[20];

        String RIPEMDout, RIPEMDout2;
        Ripemd160 rmd160 = new Ripemd160();

        //in_len = sizeof(SHA256out);
        //in_len2 = sizeof(SHA256out2);

        //mbedtls_ripemd160(SHA256out, in_len, RIPEMDout);

        byte[] SHA256outb;// = new char[SHA256out.length()/2];
        byte[] SHA256out2b;// = new char[SHA256out2.length()/2];
        SHA256outb = SHA256G.HashStrToByte2(SHA256out);
        SHA256out2b = SHA256G.HashStrToByte2(SHA256out2);
        char[] SHA256outc = new char[SHA256out.length()/2];
        char[] SHA256out2c = new char[SHA256out2.length()/2];
        for (int i=0;i<SHA256out.length()/2 ; i++) SHA256outc [i] = (char) (SHA256outb[i] & 0xFF);
        for (int i=0;i<SHA256out2.length()/2 ; i++) SHA256out2c [i] = (char) (SHA256out2b[i] & 0xFF);


        //RIPEMDout = Ripemd160.HashCharToStr(rmd160.ripemd160(SHA256out.toCharArray(), SHA256out.length()));
        RIPEMDout = Ripemd160.HashCharToStr(rmd160.ripemd160(SHA256outc, SHA256out.length()/2));

        //mbedtls_ripemd160(SHA256out2, in_len2, RIPEMDout2);

        //RIPEMDout2 = Ripemd160.HashCharToStr(rmd160.ripemd160(SHA256out2.toCharArray(), SHA256out2.length()));
        RIPEMDout2 = Ripemd160.HashCharToStr(rmd160.ripemd160(SHA256out2c, SHA256out2.length()/2));
//        pubkeyCOD = RIPEMDout2;


        if(Compressed)
            //Compressed Address
            return RIPEMDout;
        else
            //UnCompressed Address
            return RIPEMDout2;

        //return (RIPEMDout + "\n" + RIPEMDout2);


        /*
         */
//        return pubkeyCOD;
        //return (String.valueOf(BTCaddrUNC) + "\n" + String.valueOf(BTCaddrCOMP));
        //return String.valueOf(BTCaddrUNC);
    }

    public String bsvPKvsWALLET (String pubkeyCOD, String WalletRMD)
    {
        BigInteger[] pubKey = pubKeyRev(pubkeyCOD);
        byte[] chA = new byte[64];
        byte[] cSHA = new byte[33]; // chave comprimida
        byte[] cSHA2 = new byte[65]; // chave não comprimida

        BigInteger tA, ichA, ichA2;
        //pubkeyCOD = pubKey[0].toString() + "\n" + pubKey[1].toString();

////////////////////////////////////////////////////////////////////
//CONSTROI AS CHAVES PUBLICAS COMPRIMIDAS E NAO COMPRIMIDAS
////////////////////////////////////////////////////////////////////

        ichA = pubKey [0];
        ichA2 = pubKey [1];
//        pubkeyCOD = ichA.toString() + "\n"+ ichA2.toString();

        for (int i = 32; i>= 1; i--)
        {
            // este cast pode dar problemas em UBUNTU
            //cSHA2[i] = cSHA[i] = (byte) (ichA & 0xFF);
            cSHA2[i] = cSHA[i] = (byte) (((ichA.and(BigInteger.valueOf(0xFF))).intValue()) & 0xFF);
            //cSHA2[i+32] = (unsigned char)(ichA2 & 255);
            cSHA2[i+32] = (byte) (((ichA2.and(BigInteger.valueOf(0xFF))).intValue()) & 0xFF);

            //ichA = ichA/256;
            ichA = ichA.divide(BigInteger.valueOf(0x100));
            //ichA2 = ichA2/256;
            ichA2 = ichA2.divide(BigInteger.valueOf(0x100));
        }

        //if (g[1] & 1) cSHA [0] = 3;
        if ((pubKey[1].and(BigInteger.valueOf(1))).intValue() == 1) cSHA [0] = 3;
        else cSHA [0] = 2;
        cSHA2 [0] = 4;

////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////
//HASH SHA256 DA CHAVE PUBLICA COMPRIMIDA
////////////////////////////////////////////////////////////////////

        //unsigned char SHA256out[32], SHA256out2[32];

        //byte[] SHA256out = new byte[32];
        //byte[] SHA256out2 = new byte[32];
        String SHA256out, SHA256out2;

        //size_t in_len = sizeof(cSHA);
        //size_t in_len2 = sizeof(cSHA2);

        //Hash SHA256
        //mbedtls_sha256(cSHA, in_len, SHA256out, 0);

        SHA256out = SHA256G.SHA256bytes(cSHA);
        //mbedtls_sha256(cSHA2, in_len2, SHA256out2, 0);
        SHA256out2 = SHA256G.SHA256bytes(cSHA2);

        //pubkeyCOD = SHA256out2;

////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////
//HASH RIPEMD160 DA SHA256 DA CHAVE PUBLICA
////////////////////////////////////////////////////////////////////

        //unsigned char RIPEMDout[20], RIPEMDout2[20];

        String RIPEMDout, RIPEMDout2;
        Ripemd160 rmd160 = new Ripemd160();

        //in_len = sizeof(SHA256out);
        //in_len2 = sizeof(SHA256out2);

        //mbedtls_ripemd160(SHA256out, in_len, RIPEMDout);

        byte[] SHA256outb;// = new char[SHA256out.length()/2];
        byte[] SHA256out2b;// = new char[SHA256out2.length()/2];
        SHA256outb = SHA256G.HashStrToByte2(SHA256out);
        SHA256out2b = SHA256G.HashStrToByte2(SHA256out2);
        char[] SHA256outc = new char[SHA256out.length()/2];
        char[] SHA256out2c = new char[SHA256out2.length()/2];
        for (int i=0;i<SHA256out.length()/2 ; i++) SHA256outc [i] = (char) (SHA256outb[i] & 0xFF);
        for (int i=0;i<SHA256out2.length()/2 ; i++) SHA256out2c [i] = (char) (SHA256out2b[i] & 0xFF);


        //RIPEMDout = Ripemd160.HashCharToStr(rmd160.ripemd160(SHA256out.toCharArray(), SHA256out.length()));
        RIPEMDout = Ripemd160.HashCharToStr(rmd160.ripemd160(SHA256outc, SHA256out.length()/2));

        //mbedtls_ripemd160(SHA256out2, in_len2, RIPEMDout2);

        //RIPEMDout2 = Ripemd160.HashCharToStr(rmd160.ripemd160(SHA256out2.toCharArray(), SHA256out2.length()));
        RIPEMDout2 = Ripemd160.HashCharToStr(rmd160.ripemd160(SHA256out2c, SHA256out2.length()/2));
//        pubkeyCOD = RIPEMDout2;



        if(WalletRMD.compareTo(RIPEMDout) == 0)
        {
            //return (RIPEMDout + "\n" + SHA256G.ByteToStrHex(cSHA));
            return (SHA256G.ByteToStrHex(cSHA));

            //return  SHA256G.ByteToStrHex(cSHA);

        }
        else if(WalletRMD.compareTo(RIPEMDout2) == 0)
        {
            //return (RIPEMDout2 + "\n" + SHA256G.ByteToStrHex(cSHA2));
            return (SHA256G.ByteToStrHex(cSHA2));
        }
        else
        {
            return ("NONE");
            //return (RIPEMDout2 + "\n" + SHA256G.ByteToStrHex(cSHA2));
        }



        /*
         */
//        return pubkeyCOD;
        //return (String.valueOf(BTCaddrUNC) + "\n" + String.valueOf(BTCaddrCOMP));
        //return String.valueOf(BTCaddrUNC);
    }


    //public String sigDERrev (BigInteger[] signECDSA)

    public BigInteger[] sigDERrev (String signECDSA)
    //public String sigDERrev (String signECDSA)
    {
        //BigInteger[] pubKey = pubKeyRev(pubkeyCOD);
        //byte[] chA = new byte[64];

        signECDSA = signECDSA.substring(6);

        byte[] x = new byte[1]; // chave comprimida
        x = SHA256G.HashStrToByte2( signECDSA.substring(0,2) );
        String r = signECDSA.substring(2, (2 * x[0]) + 2);
        signECDSA = signECDSA.substring((2 * x[0]) + 2);
        //x = SHA256G.HashStrToByte2( signECDSA.substring(0,2) );
        String s = signECDSA.substring(4);

        BigInteger[] bISign = new BigInteger[2];
        bISign[0] = BigInteger.valueOf(0);
        bISign[1] = BigInteger.valueOf(0);

        byte[] cR;// = new byte[r.length()]; // chave comprimida
        byte[] cS;// = new byte[s.length()]; // chave não comprimida

        cR = SHA256G.HashStrToByte2(r);
        cS = SHA256G.HashStrToByte2(s);


        //for(int i = 0; i < r.length(); i++)
        for(int i = 0; i < cR.length; i++)
        {
            bISign[0] = bISign[0].multiply(BigInteger.valueOf(0x100));
            bISign[0] = bISign[0].add(BigInteger.valueOf(((int)cR[i]) & 0xFF));
        }
        //for(int i = 0; i < 32 - cR.length; i++)
        //  bISign[0] = bISign[0].multiply(BigInteger.valueOf(0x100));


        for(int i = 0; i < cS.length; i++)
        {
            bISign[1] = bISign[1].multiply(BigInteger.valueOf(0x100));
            bISign[1] = bISign[1].add(BigInteger.valueOf(((int)cS[i]) & 0xFF));
        }
        //for(int i = 0; i < 32 - cS.length; i++)
        //  bISign[1] = bISign[1].multiply(BigInteger.valueOf(0x100));



        //return r + "\n" + s;
        return bISign;

        //O método está correto
        //return  sigDER(bISign);
    }
    public String sigDER (BigInteger[] signECDSA)
    {
        //BigInteger[] pubKey = pubKeyRev(pubkeyCOD);
        //byte[] chA = new byte[64];
        byte[] cSHA = new byte[100]; // chave comprimida
        byte[] cSHA2 = new byte[100]; // chave não comprimida

        BigInteger tA, ichA, ichA2;
        //pubkeyCOD = pubKey[0].toString() + "\n" + pubKey[1].toString();

////////////////////////////////////////////////////////////////////
//CONSTROI AS CHAVES PUBLICAS COMPRIMIDAS E NAO COMPRIMIDAS
////////////////////////////////////////////////////////////////////

        ichA = signECDSA [0];
        ichA2 = signECDSA [1];
//        pubkeyCOD = ichA.toString() + "\n"+ ichA2.toString();

        //for (int i = 31; i>0 ; i--)
        for (int i = 99; i>0 ; i--)
        {
            // este cast pode dar problemas em UBUNTU
            //cSHA2[i] = cSHA[i] = (byte) (ichA & 0xFF);
            cSHA[i] = (byte) (((ichA.and(BigInteger.valueOf(0xFF))).intValue()) & 0xFF);
            //cSHA2[i+32] = (unsigned char)(ichA2 & 255);
            cSHA2[i] = (byte) (((ichA2.and(BigInteger.valueOf(0xFF))).intValue()) & 0xFF);

            //ichA = ichA/256;
            ichA = ichA.divide(BigInteger.valueOf(0x100));
            //ichA2 = ichA2/256;
            ichA2 = ichA2.divide(BigInteger.valueOf(0x100));
        }

        String r = SHA256G.ByteToStrHex(cSHA);
        int ir = 0;
        while ("00".compareTo(r.substring(0,2))==0)
        {
            r = r.substring(2);
            ir ++;
        }

        String s = SHA256G.ByteToStrHex(cSHA2);
        int is = 0;
        while ("00".compareTo(s.substring(0,2))==0) {
            s = s.substring(2);
            is ++;
        }

        //if (g[1] & 1) cSHA [0] = 3;
        int jr = cSHA[ir] & 0xFF;
        int js = cSHA2[is] & 0xFF;


        //if (cSHA[ir] > 0x7f)
        if (jr > 0x7f)
            //if (0x8A > 0x7f)
            //cSHA [0] = 0x00;
            r = "00" + r;

        //if (cSHA2[is] > 0x7f)
        if (js > 0x7f)
            //cSHA2 [0] = 0x00;
            s = "00" + s;

        byte[] sizeR = new byte[1]; // chave comprimida
        byte[] sizeS = new byte[1]; // chave não comprimida
        byte[] sizeSig = new byte[1]; // chave não comprimida


        sizeR[0] = (byte) ((r.length()/2) & 0xff);
        sizeS[0] = (byte) ((s.length()/2) & 0xff);

        r = "02" + SHA256G.ByteToStrHex(sizeR) + r;
        s = "02" + SHA256G.ByteToStrHex(sizeS) + s;

        sizeSig[0] = (byte) (((r + s).length()/2) & 0xff);

        return ("30" + SHA256G.ByteToStrHex(sizeSig) + r + s);
        //return (RIPEMDout + "\n" + RIPEMDout2);
    }


}
