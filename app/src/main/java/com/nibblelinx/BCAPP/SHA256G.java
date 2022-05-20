package com.nibblelinx.BCAPP;

public class SHA256G {

    private static long[] Kzh = {

            0x428A2F98L, 0x71374491L, 0xB5C0FBCFL, 0xE9B5DBA5L,
            0x3956C25BL, 0x59F111F1L, 0x923F82A4L, 0xAB1C5ED5L,
            0xD807AA98L, 0x12835B01L, 0x243185BEL, 0x550C7DC3L,
            0x72BE5D74L, 0x80DEB1FEL, 0x9BDC06A7L, 0xC19BF174L,
            0xE49B69C1L, 0xEFBE4786L, 0x0FC19DC6L, 0x240CA1CCL,
            0x2DE92C6FL, 0x4A7484AAL, 0x5CB0A9DCL, 0x76F988DAL,
            0x983E5152L, 0xA831C66DL, 0xB00327C8L, 0xBF597FC7L,
            0xC6E00BF3L, 0xD5A79147L, 0x06CA6351L, 0x14292967L,
            0x27B70A85L, 0x2E1B2138L, 0x4D2C6DFCL, 0x53380D13L,
            0x650A7354L, 0x766A0ABBL, 0x81C2C92EL, 0x92722C85L,
            0xA2BFE8A1L, 0xA81A664BL, 0xC24B8B70L, 0xC76C51A3L,
            0xD192E819L, 0xD6990624L, 0xF40E3585L, 0x106AA070L,
            0x19A4C116L, 0x1E376C08L, 0x2748774CL, 0x34B0BCB5L,
            0x391C0CB3L, 0x4ED8AA4AL, 0x5B9CCA4FL, 0x682E6FF3L,
            0x748F82EEL, 0x78A5636FL, 0x84C87814L, 0x8CC70208L,
            0x90BEFFFAL, 0xA4506CEBL, 0xBEF9A3F7L, 0xC67178F2L

    };
    private static long[] H0 =   {
            0x6A09E667L, 0xBB67AE85L, 0x3C6Ef372L, 0xA54FF53AL,
            0x510E527FL, 0x9B05688CL, 0x1F83D9ABL, 0x5BE0CD19L
    };

    public static int sha_256( long[] W, long[] Hi0, long[] Hi )
    {
        long A, B, C, D, E, F, G, H, temp1, temp2;
        int t=0;

        for(t=16;t<64;t++)
        {
            W[t] = setRange(S1(W[t -	2]) + W[t -	7] + S0(W[t - 15]) + W[t - 16]);
        }
        temp1 = setRange(Hi0[7] + S3(Hi0[4]) + CH(Hi0[4],Hi0[5],Hi0[6]) + Kzh[0] + W[0]);
        temp2 = setRange(S2(Hi0[0]) + MAJ(Hi0[0],Hi0[1],Hi0[2]));
        H = Hi0[6];
        G = Hi0[5];
        F = Hi0[4];
        E = setRange(Hi0[3] + temp1);
        D = Hi0[2];
        C = Hi0[1];
        B = Hi0[0];
        A = setRange(temp1 + temp2);

        for(t=1;t<63;t++)
        {
            temp1 = setRange(H + S3(E) + CH(E,F,G) + Kzh[t] + W[t]);
            temp2 = setRange(S2(A) + MAJ(A,B,C));
            H = G;
            G = F;
            F = E;
            E = setRange(D + temp1);
            D = C;
            C = B;
            B = A;
            A = setRange(temp1 + temp2);
        }
        ///////////////////////////////////////////////////////////
        // when t = 63
        // In order to avoid a extra computing
        ///////////////////////////////////////////////////////////
        temp1 = setRange(H + S3(E) + CH(E,F,G) + Kzh[63] + W[63]);
        temp2 = setRange(S2(A) + MAJ(A,B,C));
        Hi[0] = setRange((temp1 + temp2) + Hi0[0]);
        Hi[1] = setRange(A + Hi0[1]);
        Hi[2] = setRange(B + Hi0[2]);
        Hi[3] = setRange(C + Hi0[3]);
        Hi[4] = setRange((D + temp1) + Hi0[4]);
        Hi[5] = setRange(E + Hi0[5]);
        Hi[6] = setRange(F + Hi0[6]);
        Hi[7] = setRange(G + Hi0[7]);

        return (0);
    }

    public static long setRange( long val)
    {
        //long tmp = ((x & 0xFFFFFFFF) >> n);
        return (val & 0xFFFFFFFFL);
    }
    // Shift right
    public static long SHR( long x, long n)
    {
        //long tmp = ((x & 0xFFFFFFFF) >> n);
        return ((x & 0xFFFFFFFFL) >> n);
    }
    // Rotate right
    public static long ROTR( long x, int n)
    {
        long tmp1 = setRange(x << (32 - n));
        return   (SHR(x,n) | tmp1 )  ;

    }
    // S0 and S1
    public static long S0( long x)
    {
        return (ROTR(x, 7) ^ ROTR(x,18) ^ SHR(x, 3));

    }
    public static long S1( long x)
    {
        return (ROTR(x,17) ^ ROTR(x,19) ^ SHR(x,10));

    }
    // Σ0 and Σ1
    public static long S2( long x)
    {
        return (ROTR(x, 2) ^ ROTR(x,13) ^ ROTR(x,22));

    }
    public static long S3( long x)
    {
        return (ROTR(x, 6) ^ ROTR(x,11) ^ ROTR(x,25));

    }
    // Maj and Ch
    public static long MAJ( long x, long y, long z)
    {
        return ((x & y) | (z & (x | y)));

    }
    public static long CH( long x, long y, long z)
    {

        return (z ^ (x & (y ^ z)));

    }

    //Metodo usado para transforma um HASH SHA256 de String para array de Bytes
    //HASH string to byte
    //Transforma cada posição em seu equivalente Hexadecimal;
    //OBS: Só usa o LSNibble de cada elemento do array de bytes;
    public static byte[] HashStrToByte(String hashKey)
    {
        byte[] result = new byte[hashKey.length()];
        char[] strChar = new char[hashKey.length()];
        strChar = hashKey.toCharArray();
        for (int i = 0; i < hashKey.length(); i++)
        {
            switch (strChar[i])
            {
                case '0':
                    result[i] = 0;
                    break;
                case '1':
                    result[i] = 1;
                    break;
                case '2':
                    result[i] = 2;
                    break;
                case '3':
                    result[i] = 3;
                    break;
                case '4':
                    result[i] = 4;
                    break;
                case '5':
                    result[i] = 5;
                    break;
                case '6':
                    result[i] = 6;
                    break;
                case '7':
                    result[i] = 7;
                    break;
                case '8':
                    result[i] = 8;
                    break;
                case '9':
                    result[i] = 9;
                    break;
                case 'a':
                    result[i] = 10;
                    break;
                case 'b':
                    result[i] = 11;
                    break;
                case 'c':
                    result[i] = 12;
                    break;
                case 'd':
                    result[i] = 13;
                    break;
                case 'e':
                    result[i] = 14;
                    break;
                case 'f':
                    result[i] = 15;
                    break;
            }
        }

        return  result;
    }

    //Metodo usado para transforma um HASH SHA256 de String para Bytes
    //HASH string to byte
    //REVISADO: Usa ambos os Nibbles de cada elemento do array de bytes;
    public static byte[] HashStrToByte2(String hashKey)
    {
        byte[] result = new byte[hashKey.length()];
        byte[] result2 = new byte[hashKey.length()/2];
        char[] strChar = new char[hashKey.length()];
        strChar = hashKey.toCharArray();
        for (int i = 0; i < hashKey.length(); i++)
        {
            switch (strChar[i])
            {
                case '0':
                    result[i] = 0;
                    break;
                case '1':
                    result[i] = 1;
                    break;
                case '2':
                    result[i] = 2;
                    break;
                case '3':
                    result[i] = 3;
                    break;
                case '4':
                    result[i] = 4;
                    break;
                case '5':
                    result[i] = 5;
                    break;
                case '6':
                    result[i] = 6;
                    break;
                case '7':
                    result[i] = 7;
                    break;
                case '8':
                    result[i] = 8;
                    break;
                case '9':
                    result[i] = 9;
                    break;
                case 'a':
                    result[i] = 10;
                    break;
                case 'b':
                    result[i] = 11;
                    break;
                case 'c':
                    result[i] = 12;
                    break;
                case 'd':
                    result[i] = 13;
                    break;
                case 'e':
                    result[i] = 14;
                    break;
                case 'f':
                    result[i] = 15;
                    break;
            }
        }
        for(int i = (hashKey.length()/2)-1; i>=0; i--)
        {
            //result2 [i] = (byte) (result[2*i + 1] + result[2*i] * 0x10); // Funciona, mas pode have propagação de sinal
            result2 [i] = (byte) ((result[2*i + 1] + result[2*i] * 0x10) & 0xFF);
        }
        return  result2;
    }


    //Metodo usado para transforma um HASH SHA256 de String para Bytes
    //String to byte
    public static byte[] StrToByte(String hashKey)
    {
        byte[] result = new byte[hashKey.length()];
        char[] strChar = new char[hashKey.length()];
        strChar = hashKey.toCharArray();
        for (int i = 0; i < hashKey.length(); i++)
        {
            result[i] = (byte) (strChar [i] & 0xFF);
        }

        return  result;
    }



    //Metodo usado para transforma um HASH SHA256 de String para Bytes
    //String to byte
    public static String LEformat (String hashKey)
    {
        char[] result = new char[hashKey.length()];
        char[] strChar = new char[hashKey.length()];
        strChar = hashKey.toCharArray();
        for (int i = 0; i < hashKey.length(); i = i+2)
        //for (int i = 0; i < hashKey.length(); i = i+1)
        {
            result[hashKey.length()-2 - i] = strChar [i];

            result[hashKey.length()-1 - i] = strChar [i+1];
            //result[hashKey.length() -1 - i] = strChar [i];
            //result[i] = strChar [hashKey.length() -1];
        }

        return  String.valueOf(result);
        //return  String.valueOf(strChar);
    }

    //public char base16[] = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
    //Metodo usado para transformar um HASH SHA256 de bytes para String
    //Não funciona se o HASH não estiver em HEXADECIMAL (Somente na primeira versão)
    //Funciona na segunda versão
    public static String HashByteToStr(byte[] hashKey)
    {
        //String result;
        char[] result = new char[hashKey.length];

        /*
        //strChar = hashKey.toCharArray();
        char base16[] = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
        for (int i = 0; i < hashKey.length; i++)   result[i] = base16[hashKey[i]];
        //não tem problema de propagação de sinal, já que somente o NIBBLE menos signifcativo é utilizado;
        */
        for (int i = 0; i < hashKey.length; i++)   result[i] = (char) (hashKey[i] & 0xFF);

        return String.valueOf(result);
    }

    //public char base16[] = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
    //Metodo usado para transformar um HASH SHA256 de bytes para String HexaDecimal
    //Atenção para metodos que produzem propagação de sinal
    public static String ByteToStrHex(byte[] hashKey)
    {
        //String result;
        char[] result = new char[2*hashKey.length];

        //strChar = hashKey.toCharArray();
        char base16[] = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
        for (int i = 0; i < hashKey.length; i++)
        {
            //result[2*i] = base16[(hashKey[i]/(0x10)) & 0x0F]; //não funciona
            result[2*i] = base16[(hashKey[i] & 0xF0)/ (0x10)]; //quebra a propagação de sinal
            //quebra de sinal é crítica na converão entre int e byte
            result[2*i + 1] = base16[hashKey[i] & 0x0F];
        }

        return String.valueOf(result);
    }



    //Metodo usado para construir um Hash SHA256 a partir de uma Stream de Bytes
    //A Stream de byte aqui é HEXADECIMAL de 64 posições
    //Só funciona se a ENTRADA chegar em forma NUMERICA
    public static String SHA256bytes(byte[] txtcomp)
    {

        long[] W= {

                0x00000000, 0x00000000, 0x00000000, 0x00000000,
                0x00000000, 0x00000000, 0x00000000, 0x00000000,
                0x00000000, 0x00000000, 0x00000000, 0x00000000,
                0x00000000, 0x00000000, 0x00000000, 0x00000008,
                0x00000000, 0x00000000, 0x00000000, 0x00000000,
                0x00000000, 0x00000000, 0x00000000, 0x00000000,
                0x00000000, 0x00000000, 0x00000000, 0x00000000,
                0x00000000, 0x00000000, 0x00000000, 0x00000000,
                0x00000000, 0x00000000, 0x00000000, 0x00000000,
                0x00000000, 0x00000000, 0x00000000, 0x00000000,
                0x00000000, 0x00000000, 0x00000000, 0x00000000,
                0x00000000, 0x00000000, 0x00000000, 0x00000000,
                0x00000000, 0x00000000, 0x00000000, 0x00000000,
                0x00000000, 0x00000000, 0x00000000, 0x00000000,
                0x00000000, 0x00000000, 0x00000000, 0x00000000,
                0x00000000, 0x00000000, 0x00000000, 0x00000000

        };
        long[] Hi=   {

                0x00000000, 0x00000000, 0x00000000, 0x00000000,
                0x00000000, 0x00000000, 0x00000000, 0x00000000
        };

        //byte [] result = new

        if(txtcomp.length>0) {

            //char[] txtcomp = new char[strIn.length];
            char[] result = new char[64];

            // Para String de 448 bits 56 letras A

            int i = 0;
            boolean firstCyle = true;

            //Toast.makeText(SHA256G.this,"Entrada Inválida",Toast.LENGTH_SHORT).show();

            for(; i < txtcomp.length - (txtcomp.length % 64); i = i + 64)
            {
                W[0] = (0x01000000L * (long) (txtcomp[i] & 0x000000FFL)) + (0x00010000L * (long) (txtcomp[i + 1] & 0x000000FFL))
                        + (0x00000100L * (long) (txtcomp[i + 2] & 0x000000FFL)) + (0x00000001L * (long) (txtcomp[i + 3] & 0x000000FFL));
                W[1] = (0x01000000L * (long) (txtcomp[i + 4] & 0x000000FFL)) + (0x00010000L * (long) (txtcomp[i + 5] & 0x000000FFL))
                        + (0x00000100L * (long) (txtcomp[i + 6] & 0x000000FFL)) + (0x00000001L * (long) (txtcomp[i + 7] & 0x000000FFL));
                W[2] = (0x01000000L * (long) (txtcomp[i + 8] & 0x000000FFL)) + (0x00010000L * (long) (txtcomp[i + 9] & 0x000000FFL))
                        + (0x00000100L * (long) (txtcomp[i + 10] & 0x000000FFL)) + (0x00000001L * (long) (txtcomp[i + 11] & 0x000000FFL));
                W[3] = (0x01000000L * (long) (txtcomp[i + 12] & 0x000000FFL)) + (0x00010000L * (long) (txtcomp[i + 13] & 0x000000FFL))
                        + (0x00000100L * (long) (txtcomp[i + 14] & 0x000000FFL)) + (0x00000001L * (long) (txtcomp[i + 15] & 0x000000FFL));
                W[4] = (0x01000000L * (long) (txtcomp[i + 16] & 0x000000FFL)) + (0x00010000L * (long) (txtcomp[i + 17] & 0x000000FFL))
                        + (0x00000100L * (long) (txtcomp[i + 18] & 0x000000FFL)) + (0x00000001L * (long) (txtcomp[i + 19] & 0x000000FFL));
                W[5] = (0x01000000L * (long) (txtcomp[i + 20] & 0x000000FFL)) + (0x00010000L * (long) (txtcomp[i + 21] & 0x000000FFL))
                        + (0x00000100L * (long) (txtcomp[i + 22] & 0x000000FFL)) + (0x00000001L * (long) (txtcomp[i + 23] & 0x000000FFL));
                W[6] = (0x01000000L * (long) (txtcomp[i + 24] & 0x000000FFL)) + (0x00010000L * (long) (txtcomp[i + 25] & 0x000000FFL))
                        + (0x00000100L * (long) (txtcomp[i + 26] & 0x000000FFL)) + (0x00000001L * (long) (txtcomp[i + 27] & 0x000000FFL));
                W[7] = (0x01000000L * (long) (txtcomp[i + 28] & 0x000000FFL)) + (0x00010000L * (long) (txtcomp[i + 29] & 0x000000FFL))
                        + (0x00000100L * (long) (txtcomp[i + 30] & 0x000000FFL)) + (0x00000001L * (long) (txtcomp[i + 31] & 0x000000FFL));
                W[8] = (0x01000000L * (long) (txtcomp[i + 32] & 0x000000FFL)) + (0x00010000L * (long) (txtcomp[i + 33] & 0x000000FFL))
                        + (0x00000100L * (long) (txtcomp[i + 34] & 0x000000FFL)) + (0x00000001L * (long) (txtcomp[i + 35] & 0x000000FFL));
                W[9] = (0x01000000L * (long) (txtcomp[i + 36] & 0x000000FFL)) + (0x00010000L * (long) (txtcomp[i + 37] & 0x000000FFL))
                        + (0x00000100L * (long) (txtcomp[i + 38] & 0x000000FFL)) + (0x00000001L * (long) (txtcomp[i + 39] & 0x000000FFL));
                W[10] = (0x01000000L * (long) (txtcomp[i + 40] & 0x000000FFL)) + (0x00010000L * (long) (txtcomp[i + 41] & 0x000000FFL))
                        + (0x00000100L * (long) (txtcomp[i + 42] & 0x000000FFL)) + (0x00000001L * (long) (txtcomp[i + 43] & 0x000000FFL));
                W[11] = (0x01000000L * (long) (txtcomp[i + 44] & 0x000000FFL)) + (0x00010000L * (long) (txtcomp[i + 45] & 0x000000FFL))
                        + (0x00000100L * (long) (txtcomp[i + 46] & 0x000000FFL)) + (0x00000001L * (long) (txtcomp[i + 47] & 0x000000FFL));
                W[12] = (0x01000000L * (long) (txtcomp[i + 48] & 0x000000FFL)) + (0x00010000L * (long) (txtcomp[i + 49] & 0x000000FFL))
                        + (0x00000100L * (long) (txtcomp[i + 50] & 0x000000FFL)) + (0x00000001L * (long) (txtcomp[i + 51] & 0x000000FFL));
                W[13] = (0x01000000L * (long) (txtcomp[i + 52] & 0x000000FFL)) + (0x00010000L * (long) (txtcomp[i + 53] & 0x000000FFL))
                        + (0x00000100L * (long) (txtcomp[i + 54] & 0x000000FFL)) + (0x00000001L * (long) (txtcomp[i + 55] & 0x000000FFL));
                W[14] = (0x01000000L * (long) (txtcomp[i + 56] & 0x000000FFL)) + (0x00010000L * (long) (txtcomp[i + 57] & 0x000000FFL))
                        + (0x00000100L * (long) (txtcomp[i + 58] & 0x000000FFL)) + (0x00000001L * (long) (txtcomp[i + 59] & 0x000000FFL));
                W[15] = (0x01000000L * (long) (txtcomp[i + 60] & 0x000000FFL)) + (0x00010000L * (long) (txtcomp[i + 61] & 0x000000FFL))
                        + (0x00000100L * (long) (txtcomp[i + 62] & 0x000000FFL)) + (0x00000001L * (long) (txtcomp[i + 63] & 0x000000FFL));

                if(firstCyle)
                {
                    sha_256(W, H0, Hi);
                    firstCyle = false;
                }
                else sha_256 (W, Hi, Hi);
            }

            W[0] = 0x00000000;  W[1] = 0x00000000; W[2] = 0x00000000; W[3] = 0x00000000;
            W[4] = 0x00000000; W[5] = 0x00000000; W[6] = 0x00000000; W[7] = 0x00000000;
            W[8] = 0x00000000; W[9] = 0x00000000; W[10] = 0x00000000; W[11] = 0x00000000;
            W[12] = 0x00000000; W[13] = 0x00000000; W[14] = 0x00000000; W[15] = 0x00000000;

            int j;
            int k = 0;
            for(j = i; j < txtcomp.length; j++)
            {
                switch(j%4) {
                    case 0:
                        W[k]  = (0x01000000L * (long) (txtcomp[j]  & 0x000000FFL));
                        break;
                    case 1:
                        W[k]  += (0x00010000L * (long) (txtcomp[j]  & 0x000000FFL));
                        break;
                    case 2:
                        W[k]  += (0x00000100L * (long) (txtcomp[j]  & 0x000000FFL));
                        break;
                    case 3:
                        W[k]  += (0x00000001L * (long) (txtcomp[j]  & 0x000000FFL));
                        //so para para o proximo k quando passar pelos 4
                        k++;
                        break;
                }
            }

            switch((j-1)%4) {
                case 0:
                    W[k]  += 0x00800000L;
                    break;
                case 1:
                    W[k]  += 0x00008000L;
                    break;
                case 2:
                    W[k]  += 0x00000080L;
                    break;
                case 3:
                    //neste caso será o k posterior
                    W[k]  = 0x80000000L;;
                    break;
            }

            if(txtcomp.length % 64 < 56)
            {
                W[14]  = 0x00000000L;
                W[15]  = 0x00000001L * (long) (8*txtcomp.length);

                if(firstCyle)
                {
                    sha_256(W, H0, Hi);
                    firstCyle = false;
                }
                else sha_256 (W, Hi, Hi);
            }
            else
            {

                //if(k % 16 == 14) // isso
                //    W[15]  = 0x00000000L;

                //W[15]  = 0x00000000L; //Esta linha pode ter causado uma serie de problemas, mas ainda não foi analisado direito
                if(firstCyle)
                {
                    sha_256(W, H0, Hi);
                    firstCyle = false;
                }
                else sha_256 (W, Hi, Hi);

                W[0] = 0x00000000; W[1] = 0x00000000; W[2] = 0x00000000; W[3] = 0x00000000;
                W[4] = 0x00000000; W[5] = 0x00000000; W[6] = 0x00000000; W[7] = 0x00000000;
                W[8] = 0x00000000; W[9] = 0x00000000; W[10] = 0x00000000; W[11] = 0x00000000;
                W[12] = 0x00000000; W[13] = 0x00000000; W[14] = 0x00000000; W[15] = 0x00000001L * (long) (8*txtcomp.length);

                sha_256 (W, Hi, Hi);
            }

            for(i = 0; i < 8; i++)
            {
                //Verificar o problema de quabra de sinal
                result[i*8 + 0] = (char)  ((Hi[i]/0x10000000L) & 0x0000000FL);
                result[i*8 + 1] = (char)  ((Hi[i]/0x01000000L) & 0x0000000FL);
                result[i*8 + 2] = (char)  ((Hi[i]/0x00100000L) & 0x0000000FL);
                result[i*8 + 3] = (char)  ((Hi[i]/0x00010000L) & 0x0000000FL);
                result[i*8 + 4] = (char)  ((Hi[i]/0x00001000L) & 0x0000000FL);
                result[i*8 + 5] = (char)  ((Hi[i]/0x00000100L) & 0x0000000FL);
                result[i*8 + 6] = (char)  ((Hi[i]/0x00000010L) & 0x0000000FL);
                result[i*8 + 7] = (char)  ((Hi[i]/0x00000001L) & 0x0000000FL);
            }

            char base16[] = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
            for(i = 0; i < 64; i++) result[i] = base16[(int)result[i]];

            return String.valueOf(result);
        }

        return null;
    }

    //Metodo usado para construir um Hash SHA256 a partir de uma String
    public static String SHA256STR(String strIn)
    {

        long[] W= {

                0x00000000, 0x00000000, 0x00000000, 0x00000000,
                0x00000000, 0x00000000, 0x00000000, 0x00000000,
                0x00000000, 0x00000000, 0x00000000, 0x00000000,
                0x00000000, 0x00000000, 0x00000000, 0x00000008,
                0x00000000, 0x00000000, 0x00000000, 0x00000000,
                0x00000000, 0x00000000, 0x00000000, 0x00000000,
                0x00000000, 0x00000000, 0x00000000, 0x00000000,
                0x00000000, 0x00000000, 0x00000000, 0x00000000,
                0x00000000, 0x00000000, 0x00000000, 0x00000000,
                0x00000000, 0x00000000, 0x00000000, 0x00000000,
                0x00000000, 0x00000000, 0x00000000, 0x00000000,
                0x00000000, 0x00000000, 0x00000000, 0x00000000,
                0x00000000, 0x00000000, 0x00000000, 0x00000000,
                0x00000000, 0x00000000, 0x00000000, 0x00000000,
                0x00000000, 0x00000000, 0x00000000, 0x00000000,
                0x00000000, 0x00000000, 0x00000000, 0x00000000

        };
        long[] Hi=   {

                0x00000000, 0x00000000, 0x00000000, 0x00000000,
                0x00000000, 0x00000000, 0x00000000, 0x00000000
        };

        if(strIn.length()>0) {

            char[] txtcomp = new char[strIn.length()];
            char[] result = new char[64];

            txtcomp = strIn.toCharArray();

            // Para String de 448 bits 56 letras A


            int i = 0;
            boolean firstCyle = true;

            //Toast.makeText(SHA256G.this,"Entrada Inválida",Toast.LENGTH_SHORT).show();

            //verificar se será necessário acressentar a metodologia de remoção de sinal
            //por ser string não teria problemas, pois é de um tipo que não causa problemas

            for(; i < strIn.length() - (strIn.length() % 64); i = i + 64)
            {
                W[0] = (0x01000000L * (long) (txtcomp[i] & 0x000000FFL)) + (0x00010000L * (long) (txtcomp[i + 1] & 0x000000FFL))
                        + (0x00000100L * (long) (txtcomp[i + 2] & 0x000000FFL)) + (0x00000001L * (long) (txtcomp[i + 3] & 0x000000FFL));
                W[1] = (0x01000000L * (long) (txtcomp[i + 4] & 0x000000FFL)) + (0x00010000L * (long) (txtcomp[i + 5] & 0x000000FFL))
                        + (0x00000100L * (long) (txtcomp[i + 6] & 0x000000FFL)) + (0x00000001L * (long) (txtcomp[i + 7] & 0x000000FFL));
                W[2] = (0x01000000L * (long) (txtcomp[i + 8] & 0x000000FFL)) + (0x00010000L * (long) (txtcomp[i + 9] & 0x000000FFL))
                        + (0x00000100L * (long) (txtcomp[i + 10] & 0x000000FFL)) + (0x00000001L * (long) (txtcomp[i + 11] & 0x000000FFL));
                W[3] = (0x01000000L * (long) (txtcomp[i + 12] & 0x000000FFL)) + (0x00010000L * (long) (txtcomp[i + 13] & 0x000000FFL))
                        + (0x00000100L * (long) (txtcomp[i + 14] & 0x000000FFL)) + (0x00000001L * (long) (txtcomp[i + 15] & 0x000000FFL));
                W[4] = (0x01000000L * (long) (txtcomp[i + 16] & 0x000000FFL)) + (0x00010000L * (long) (txtcomp[i + 17] & 0x000000FFL))
                        + (0x00000100L * (long) (txtcomp[i + 18] & 0x000000FFL)) + (0x00000001L * (long) (txtcomp[i + 19] & 0x000000FFL));
                W[5] = (0x01000000L * (long) (txtcomp[i + 20] & 0x000000FFL)) + (0x00010000L * (long) (txtcomp[i + 21] & 0x000000FFL))
                        + (0x00000100L * (long) (txtcomp[i + 22] & 0x000000FFL)) + (0x00000001L * (long) (txtcomp[i + 23] & 0x000000FFL));
                W[6] = (0x01000000L * (long) (txtcomp[i + 24] & 0x000000FFL)) + (0x00010000L * (long) (txtcomp[i + 25] & 0x000000FFL))
                        + (0x00000100L * (long) (txtcomp[i + 26] & 0x000000FFL)) + (0x00000001L * (long) (txtcomp[i + 27] & 0x000000FFL));
                W[7] = (0x01000000L * (long) (txtcomp[i + 28] & 0x000000FFL)) + (0x00010000L * (long) (txtcomp[i + 29] & 0x000000FFL))
                        + (0x00000100L * (long) (txtcomp[i + 30] & 0x000000FFL)) + (0x00000001L * (long) (txtcomp[i + 31] & 0x000000FFL));
                W[8] = (0x01000000L * (long) (txtcomp[i + 32] & 0x000000FFL)) + (0x00010000L * (long) (txtcomp[i + 33] & 0x000000FFL))
                        + (0x00000100L * (long) (txtcomp[i + 34] & 0x000000FFL)) + (0x00000001L * (long) (txtcomp[i + 35] & 0x000000FFL));
                W[9] = (0x01000000L * (long) (txtcomp[i + 36] & 0x000000FFL)) + (0x00010000L * (long) (txtcomp[i + 37] & 0x000000FFL))
                        + (0x00000100L * (long) (txtcomp[i + 38] & 0x000000FFL)) + (0x00000001L * (long) (txtcomp[i + 39] & 0x000000FFL));
                W[10] = (0x01000000L * (long) (txtcomp[i + 40] & 0x000000FFL)) + (0x00010000L * (long) (txtcomp[i + 41] & 0x000000FFL))
                        + (0x00000100L * (long) (txtcomp[i + 42] & 0x000000FFL)) + (0x00000001L * (long) (txtcomp[i + 43] & 0x000000FFL));
                W[11] = (0x01000000L * (long) (txtcomp[i + 44] & 0x000000FFL)) + (0x00010000L * (long) (txtcomp[i + 45] & 0x000000FFL))
                        + (0x00000100L * (long) (txtcomp[i + 46] & 0x000000FFL)) + (0x00000001L * (long) (txtcomp[i + 47] & 0x000000FFL));
                W[12] = (0x01000000L * (long) (txtcomp[i + 48] & 0x000000FFL)) + (0x00010000L * (long) (txtcomp[i + 49] & 0x000000FFL))
                        + (0x00000100L * (long) (txtcomp[i + 50] & 0x000000FFL)) + (0x00000001L * (long) (txtcomp[i + 51] & 0x000000FFL));
                W[13] = (0x01000000L * (long) (txtcomp[i + 52] & 0x000000FFL)) + (0x00010000L * (long) (txtcomp[i + 53] & 0x000000FFL))
                        + (0x00000100L * (long) (txtcomp[i + 54] & 0x000000FFL)) + (0x00000001L * (long) (txtcomp[i + 55] & 0x000000FFL));
                W[14] = (0x01000000L * (long) (txtcomp[i + 56] & 0x000000FFL)) + (0x00010000L * (long) (txtcomp[i + 57] & 0x000000FFL))
                        + (0x00000100L * (long) (txtcomp[i + 58] & 0x000000FFL)) + (0x00000001L * (long) (txtcomp[i + 59] & 0x000000FFL));
                W[15] = (0x01000000L * (long) (txtcomp[i + 60] & 0x000000FFL)) + (0x00010000L * (long) (txtcomp[i + 61] & 0x000000FFL))
                        + (0x00000100L * (long) (txtcomp[i + 62] & 0x000000FFL)) + (0x00000001L * (long) (txtcomp[i + 63] & 0x000000FFL));

                if(firstCyle)
                {
                    sha_256(W, H0, Hi);
                    firstCyle = false;
                }
                else sha_256 (W, Hi, Hi);
            }

            W[0] = 0x00000000;  W[1] = 0x00000000; W[2] = 0x00000000; W[3] = 0x00000000;
            W[4] = 0x00000000; W[5] = 0x00000000; W[6] = 0x00000000; W[7] = 0x00000000;
            W[8] = 0x00000000; W[9] = 0x00000000; W[10] = 0x00000000; W[11] = 0x00000000;
            W[12] = 0x00000000; W[13] = 0x00000000; W[14] = 0x00000000; W[15] = 0x00000000;

            int j;
            int k = 0;
            for(j = i; j < strIn.length(); j++)
            {
                switch(j%4) {
                    case 0:
                        W[k]  = (0x01000000L * (long) (txtcomp[j]  & 0x000000FFL));
                        break;
                    case 1:
                        W[k]  += (0x00010000L * (long) (txtcomp[j]  & 0x000000FFL));
                        break;
                    case 2:
                        W[k]  += (0x00000100L * (long) (txtcomp[j]  & 0x000000FFL));
                        break;
                    case 3:
                        W[k]  += (0x00000001L * (long) (txtcomp[j]  & 0x000000FFL));
                        //so para para o proximo k quando passar pelos 4
                        k++;
                        break;
                }
            }

            switch((j-1)%4) {
                case 0:
                    W[k]  += 0x00800000L;
                    break;
                case 1:
                    W[k]  += 0x00008000L;
                    break;
                case 2:
                    W[k]  += 0x00000080L;
                    break;
                case 3:
                    //neste caso será o k posterior
                    W[k]  = 0x80000000L;;
                    break;
            }

            if(strIn.length() % 64 < 56)
            {
                //W[k]  = 0x80000000L;
                //W[k+1]  = 0x00000000L;
                W[14]  = 0x00000000L;
                W[15]  = 0x00000001L * (long) (8*strIn.length());

                if(firstCyle)
                {
                    sha_256(W, H0, Hi);
                    firstCyle = false;
                }
                else sha_256 (W, Hi, Hi);
            }
            else
            {
                //W[k]  = 0x80000000L;
                //W[k+1]  = 0x00000000L;
                //W[15]  = 0x00000000L;

                //W[15]  = 0x00000000L; //Esta linha pode ter causado uma serie de problemas, mas ainda não foi analisado direito
                //W[k+2]  = 0x00000001L * (long) strIn.length();
                if(firstCyle)
                {
                    sha_256(W, H0, Hi);
                    firstCyle = false;
                }
                else sha_256 (W, Hi, Hi);

                W[0] = 0x00000000; W[1] = 0x00000000; W[2] = 0x00000000; W[3] = 0x00000000;
                W[4] = 0x00000000; W[5] = 0x00000000; W[6] = 0x00000000; W[7] = 0x00000000;
                W[8] = 0x00000000; W[9] = 0x00000000; W[10] = 0x00000000; W[11] = 0x00000000;
                W[12] = 0x00000000; W[13] = 0x00000000; W[14] = 0x00000000; W[15] = 0x00000001L * (long) (8*strIn.length());

                sha_256 (W, Hi, Hi);
            }

            for(i = 0; i < 8; i++)
            {
                result[i*8 + 0] = (char)  ((Hi[i]/0x10000000L) & 0x0000000FL);
                result[i*8 + 1] = (char)  ((Hi[i]/0x01000000L) & 0x0000000FL);
                result[i*8 + 2] = (char)  ((Hi[i]/0x00100000L) & 0x0000000FL);
                result[i*8 + 3] = (char)  ((Hi[i]/0x00010000L) & 0x0000000FL);
                result[i*8 + 4] = (char)  ((Hi[i]/0x00001000L) & 0x0000000FL);
                result[i*8 + 5] = (char)  ((Hi[i]/0x00000100L) & 0x0000000FL);
                result[i*8 + 6] = (char)  ((Hi[i]/0x00000010L) & 0x0000000FL);
                result[i*8 + 7] = (char)  ((Hi[i]/0x00000001L) & 0x0000000FL);
            }

            char base16[] = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
            for(i = 0; i < 64; i++) result[i] = base16[(int)result[i]];

            return String.valueOf(result);

        }

        return null;
    }

    //Metodo usado para construir um Hash com H0 diferente
    //Entradas podem ser mensagem e H0 diferentes
    //Hx precisa ter 32 bytes;
    public static String SHA256MsnHx(String strIn, String Hx)
    {

        long[] W= {

                0x00000000, 0x00000000, 0x00000000, 0x00000000,
                0x00000000, 0x00000000, 0x00000000, 0x00000000,
                0x00000000, 0x00000000, 0x00000000, 0x00000000,
                0x00000000, 0x00000000, 0x00000000, 0x00000008,
                0x00000000, 0x00000000, 0x00000000, 0x00000000,
                0x00000000, 0x00000000, 0x00000000, 0x00000000,
                0x00000000, 0x00000000, 0x00000000, 0x00000000,
                0x00000000, 0x00000000, 0x00000000, 0x00000000,
                0x00000000, 0x00000000, 0x00000000, 0x00000000,
                0x00000000, 0x00000000, 0x00000000, 0x00000000,
                0x00000000, 0x00000000, 0x00000000, 0x00000000,
                0x00000000, 0x00000000, 0x00000000, 0x00000000,
                0x00000000, 0x00000000, 0x00000000, 0x00000000,
                0x00000000, 0x00000000, 0x00000000, 0x00000000,
                0x00000000, 0x00000000, 0x00000000, 0x00000000,
                0x00000000, 0x00000000, 0x00000000, 0x00000000

        };
        long[] Hi=   {

                0x00000000, 0x00000000, 0x00000000, 0x00000000,
                0x00000000, 0x00000000, 0x00000000, 0x00000000
        };

        long[] H0x=   {

                0x00000000, 0x00000000, 0x00000000, 0x00000000,
                0x00000000, 0x00000000, 0x00000000, 0x00000000
        };

        if(strIn.length()>0) {

            char[] txtcomp = new char[strIn.length()];
            char[] result = new char[64];
            //char[] H0xStr0 = new char[64];
            char[] H0xStr;// = new char[32];
            /*
            txtcomp = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA".toCharArray();

            W[0] = 0x41414141L; W[1] = 0x41414141L; W[2] = 0x41414141L; W[3] = 0x41414141L;
            W[4] = 0x41414141L; W[5] = 0x41414141L; W[6] = 0x41414141L; W[7] = 0x41414141L;
            W[8] = 0x41414141L; W[9] = 0x41414141L; W[10] = 0x41414141L; W[11] = 0x41414141L;
            W[12] = 0x41414141L; W[13] = 0x41414141L; W[14] = 0x80000000L; W[15] = 0x00000000L;

            */
            txtcomp = strIn.toCharArray();
            H0xStr = Hx.toCharArray();

            for (int i = 0; i < Hx.length(); i++)
            {
                switch (H0xStr[i])
                {
                    case '0':
                        H0xStr[i] = 0;
                        break;
                    case '1':
                        H0xStr[i] = 1;
                        break;
                    case '2':
                        H0xStr[i] = 2;
                        break;
                    case '3':
                        H0xStr[i] = 3;
                        break;
                    case '4':
                        H0xStr[i] = 4;
                        break;
                    case '5':
                        H0xStr[i] = 5;
                        break;
                    case '6':
                        H0xStr[i] = 6;
                        break;
                    case '7':
                        H0xStr[i] = 7;
                        break;
                    case '8':
                        H0xStr[i] = 8;
                        break;
                    case '9':
                        H0xStr[i] = 9;
                        break;
                    case 'a':
                        H0xStr[i] = 10;
                        break;
                    case 'b':
                        H0xStr[i] = 11;
                        break;
                    case 'c':
                        H0xStr[i] = 12;
                        break;
                    case 'd':
                        H0xStr[i] = 13;
                        break;
                    case 'e':
                        H0xStr[i] = 14;
                        break;
                    case 'f':
                        H0xStr[i] = 15;
                        break;
                }
            }

            //for(int i = (Hx.length()/2)-1; i>=0; i--)
            for(int i = 0; i < (Hx.length()/2); i++)
            {
                H0xStr [i] = (char) (H0xStr[2*i + 1] + (H0xStr[2*i] * 0x10));
            }


            H0x[0] = (0x01000000L * (long) H0xStr[0]) + (0x00010000L * (long) H0xStr[1])
                    + (0x00000100L * (long) H0xStr[2]) + (0x00000001L * (long) H0xStr[3]);
            H0x[1] = (0x01000000L * (long) H0xStr[4]) + (0x00010000L * (long) H0xStr[5])
                    + (0x00000100L * (long) H0xStr[6]) + (0x00000001L * (long) H0xStr[7]);
            H0x[2] = (0x01000000L * (long) H0xStr[8]) + (0x00010000L * (long) H0xStr[9])
                    + (0x00000100L * (long) H0xStr[10]) + (0x00000001L * (long) H0xStr[11]);
            H0x[3] = (0x01000000L * (long) H0xStr[12]) + (0x00010000L * (long) H0xStr[13])
                    + (0x00000100L * (long) H0xStr[14]) + (0x00000001L * (long) H0xStr[15]);
            H0x[4] = (0x01000000L * (long) H0xStr[16]) + (0x00010000L * (long) H0xStr[17])
                    + (0x00000100L * (long) H0xStr[18]) + (0x00000001L * (long) H0xStr[19]);
            H0x[5] = (0x01000000L * (long) H0xStr[20]) + (0x00010000L * (long) H0xStr[21])
                    + (0x00000100L * (long) H0xStr[22]) + (0x00000001L * (long) H0xStr[23]);
            H0x[6] = (0x01000000L * (long) H0xStr[24]) + (0x00010000L * (long) H0xStr[25])
                    + (0x00000100L * (long) H0xStr[26]) + (0x00000001L * (long) H0xStr[27]);
            H0x[7] = (0x01000000L * (long) H0xStr[28]) + (0x00010000L * (long) H0xStr[29])
                    + (0x00000100L * (long) H0xStr[30]) + (0x00000001L * (long) H0xStr[31]);


            // Para String de 448 bits 56 letras A


            int i = 0;
            boolean firstCyle = true;

            //Toast.makeText(SHA256G.this,"Entrada Inválida",Toast.LENGTH_SHORT).show();

            for(; i < strIn.length() - (strIn.length() % 64); i = i + 64)
            {
                //O tipo char não precisa do cast para long
                W[0] = (0x01000000L * (long) txtcomp[i]) + (0x00010000L * (long) txtcomp[i + 1])
                        + (0x00000100L * (long) txtcomp[i + 2]) + (0x00000001L * (long) txtcomp[i + 3]);
                W[1] = (0x01000000L * (long) txtcomp[i + 4]) + (0x00010000L * (long) txtcomp[i + 5])
                        + (0x00000100L * (long) txtcomp[i + 6]) + (0x00000001L * (long) txtcomp[i + 7]);
                W[2] = (0x01000000L * (long) txtcomp[i + 8]) + (0x00010000L * (long) txtcomp[i + 9])
                        + (0x00000100L * (long) txtcomp[i + 10]) + (0x00000001L * (long) txtcomp[i + 11]);
                W[3] = (0x01000000L * (long) txtcomp[i + 12]) + (0x00010000L * (long) txtcomp[i + 13])
                        + (0x00000100L * (long) txtcomp[i + 14]) + (0x00000001L * (long) txtcomp[i + 15]);
                W[4] = (0x01000000L * (long) txtcomp[i + 16]) + (0x00010000L * (long) txtcomp[i + 17])
                        + (0x00000100L * (long) txtcomp[i + 18]) + (0x00000001L * (long) txtcomp[i + 19]);
                W[5] = (0x01000000L * (long) txtcomp[i + 20]) + (0x00010000L * (long) txtcomp[i + 21])
                        + (0x00000100L * (long) txtcomp[i + 22]) + (0x00000001L * (long) txtcomp[i + 23]);
                W[6] = (0x01000000L * (long) txtcomp[i + 24]) + (0x00010000L * (long) txtcomp[i + 25])
                        + (0x00000100L * (long) txtcomp[i + 26]) + (0x00000001L * (long) txtcomp[i + 27]);
                W[7] = (0x01000000L * (long) txtcomp[i + 28]) + (0x00010000L * (long) txtcomp[i + 29])
                        + (0x00000100L * (long) txtcomp[i + 30]) + (0x00000001L * (long) txtcomp[i + 31]);
                W[8] = (0x01000000L * (long) txtcomp[i + 32]) + (0x00010000L * (long) txtcomp[i + 33])
                        + (0x00000100L * (long) txtcomp[i + 34]) + (0x00000001L * (long) txtcomp[i + 35]);
                W[9] = (0x01000000L * (long) txtcomp[i + 36]) + (0x00010000L * (long) txtcomp[i + 37])
                        + (0x00000100L * (long) txtcomp[i + 38]) + (0x00000001L * (long) txtcomp[i + 39]);
                W[10] = (0x01000000L * (long) txtcomp[i + 40]) + (0x00010000L * (long) txtcomp[i + 41])
                        + (0x00000100L * (long) txtcomp[i + 42]) + (0x00000001L * (long) txtcomp[i + 43]);
                W[11] = (0x01000000L * (long) txtcomp[i + 44]) + (0x00010000L * (long) txtcomp[i + 45])
                        + (0x00000100L * (long) txtcomp[i + 46]) + (0x00000001L * (long) txtcomp[i + 47]);
                W[12] = (0x01000000L * (long) txtcomp[i + 48]) + (0x00010000L * (long) txtcomp[i + 49])
                        + (0x00000100L * (long) txtcomp[i + 50]) + (0x00000001L * (long) txtcomp[i + 51]);
                W[13] = (0x01000000L * (long) txtcomp[i + 52]) + (0x00010000L * (long) txtcomp[i + 53])
                        + (0x00000100L * (long) txtcomp[i + 54]) + (0x00000001L * (long) txtcomp[i + 55]);
                W[14] = (0x01000000L * (long) txtcomp[i + 56]) + (0x00010000L * (long) txtcomp[i + 57])
                        + (0x00000100L * (long) txtcomp[i + 58]) + (0x00000001L * (long) txtcomp[i + 59]);
                W[15] = (0x01000000L * (long) txtcomp[i + 60]) + (0x00010000L * (long) txtcomp[i + 61])
                        + (0x00000100L * (long) txtcomp[i + 62]) + (0x00000001L * (long) txtcomp[i + 63]);

                if(firstCyle)
                {
                    //sha_256(W, H0, Hi);
                    sha_256(W, H0x, Hi);
                    firstCyle = false;
                }
                else sha_256 (W, Hi, Hi);
            }

            W[0] = 0x00000000;  W[1] = 0x00000000; W[2] = 0x00000000; W[3] = 0x00000000;
            W[4] = 0x00000000; W[5] = 0x00000000; W[6] = 0x00000000; W[7] = 0x00000000;
            W[8] = 0x00000000; W[9] = 0x00000000; W[10] = 0x00000000; W[11] = 0x00000000;
            W[12] = 0x00000000; W[13] = 0x00000000; W[14] = 0x00000000; W[15] = 0x00000000;

            int j;
            int k = 0;
            for(j = i; j < strIn.length(); j++)
            {
                switch(j%4) {
                    case 0:
                        W[k]  = (0x01000000L * (long) txtcomp[j]);
                        break;
                    case 1:
                        W[k]  += (0x00010000L * (long) txtcomp[j]);
                        break;
                    case 2:
                        W[k]  += (0x00000100L * (long) txtcomp[j]);
                        break;
                    case 3:
                        W[k]  += (0x00000001L * (long) txtcomp[j]);
                        //so para para o proximo k quando passar pelos 4
                        k++;
                        break;
                }

            }

            switch((j-1)%4) {
                case 0:
                    W[k]  += 0x00800000L;
                    break;
                case 1:
                    W[k]  += 0x00008000L;
                    break;
                case 2:
                    W[k]  += 0x00000080L;
                    break;
                case 3:
                    //neste caso será o k posterior
                    W[k]  = 0x80000000L;;
                    break;
            }

            if(strIn.length() % 64 < 56)
            {
                //W[k]  = 0x80000000L;
                //W[k+1]  = 0x00000000L;
                W[14]  = 0x00000000L;
                W[15]  = 0x00000001L * (long) (8*strIn.length());

                if(firstCyle)
                {
                    //sha_256(W, H0, Hi);
                    sha_256(W, H0x, Hi);
                    firstCyle = false;
                }
                else sha_256 (W, Hi, Hi);
            }
            else
            {
                //W[k]  = 0x80000000L;
                //W[k+1]  = 0x00000000L;
                //W[15]  = 0x00000000L; //Esta linha pode ter causado uma serie de problemas, mas ainda não foi analisado direito
                //W[k+2]  = 0x00000001L * (long) strIn.length();
                if(firstCyle)
                {
                    //sha_256(W, H0, Hi);
                    sha_256(W, H0x, Hi);
                    firstCyle = false;
                }
                else sha_256 (W, Hi, Hi);

                W[0] = 0x00000000; W[1] = 0x00000000; W[2] = 0x00000000; W[3] = 0x00000000;
                W[4] = 0x00000000; W[5] = 0x00000000; W[6] = 0x00000000; W[7] = 0x00000000;
                W[8] = 0x00000000; W[9] = 0x00000000; W[10] = 0x00000000; W[11] = 0x00000000;
                W[12] = 0x00000000; W[13] = 0x00000000; W[14] = 0x00000000; W[15] = 0x00000001L * (long) (8*strIn.length());

                sha_256 (W, Hi, Hi);
            }

            for(i = 0; i < 8; i++)
            {
                result[i*8 + 0] = (char)  ((Hi[i]/0x10000000L) & 0x0000000FL);
                result[i*8 + 1] = (char)  ((Hi[i]/0x01000000L) & 0x0000000FL);
                result[i*8 + 2] = (char)  ((Hi[i]/0x00100000L) & 0x0000000FL);
                result[i*8 + 3] = (char)  ((Hi[i]/0x00010000L) & 0x0000000FL);
                result[i*8 + 4] = (char)  ((Hi[i]/0x00001000L) & 0x0000000FL);
                result[i*8 + 5] = (char)  ((Hi[i]/0x00000100L) & 0x0000000FL);
                result[i*8 + 6] = (char)  ((Hi[i]/0x00000010L) & 0x0000000FL);
                result[i*8 + 7] = (char)  ((Hi[i]/0x00000001L) & 0x0000000FL);
            }

            char base16[] = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
            for(i = 0; i < 64; i++) result[i] = base16[(int)result[i]];

            return String.valueOf(result);

        }

        return null;
    }

    //Para realizar testes de convergencia
    public static String H0String()
    {
        char[] result = new char[64];


        for(int i = 0; i < 8; i++)
        {
            result[i*8 + 0] = (char)  ((H0[i]/0x10000000L) & 0x0000000FL);
            result[i*8 + 1] = (char)  ((H0[i]/0x01000000L) & 0x0000000FL);
            result[i*8 + 2] = (char)  ((H0[i]/0x00100000L) & 0x0000000FL);
            result[i*8 + 3] = (char)  ((H0[i]/0x00010000L) & 0x0000000FL);
            result[i*8 + 4] = (char)  ((H0[i]/0x00001000L) & 0x0000000FL);
            result[i*8 + 5] = (char)  ((H0[i]/0x00000100L) & 0x0000000FL);
            result[i*8 + 6] = (char)  ((H0[i]/0x00000010L) & 0x0000000FL);
            result[i*8 + 7] = (char)  ((H0[i]/0x00000001L) & 0x0000000FL);
        }

        char base16[] = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
        for(int i = 0; i < 64; i++) result[i] = base16[(int)result[i]];

        return String.valueOf(result);
    }
    //Metodo usado para construir um Hash com H0 diferente
    //Entradas podem ser mensagem e H0 diferentes
    //Hx precisa ter 64 bytes;
    //Não Funciona
    public static String SHA256MsnHxHex(byte[] txtcomp, byte[] Hx)
    {

        long[] W= {

                0x00000000, 0x00000000, 0x00000000, 0x00000000,
                0x00000000, 0x00000000, 0x00000000, 0x00000000,
                0x00000000, 0x00000000, 0x00000000, 0x00000000,
                0x00000000, 0x00000000, 0x00000000, 0x00000008,
                0x00000000, 0x00000000, 0x00000000, 0x00000000,
                0x00000000, 0x00000000, 0x00000000, 0x00000000,
                0x00000000, 0x00000000, 0x00000000, 0x00000000,
                0x00000000, 0x00000000, 0x00000000, 0x00000000,
                0x00000000, 0x00000000, 0x00000000, 0x00000000,
                0x00000000, 0x00000000, 0x00000000, 0x00000000,
                0x00000000, 0x00000000, 0x00000000, 0x00000000,
                0x00000000, 0x00000000, 0x00000000, 0x00000000,
                0x00000000, 0x00000000, 0x00000000, 0x00000000,
                0x00000000, 0x00000000, 0x00000000, 0x00000000,
                0x00000000, 0x00000000, 0x00000000, 0x00000000,
                0x00000000, 0x00000000, 0x00000000, 0x00000000

        };
        long[] Hi=   {

                0x00000000, 0x00000000, 0x00000000, 0x00000000,
                0x00000000, 0x00000000, 0x00000000, 0x00000000
        };

        long[] H0x=   {

                0x00000000, 0x00000000, 0x00000000, 0x00000000,
                0x00000000, 0x00000000, 0x00000000, 0x00000000
        };

        //byte [] result = new

        if((txtcomp.length>0) && (Hx.length == 64)) {

            //char[] txtcomp = new char[strIn.length];
            char[] result = new char[64];
            //char[] H0xStr = new char[32];

            //Cada valor de Hx[i] usa somente 4 bits

            for(int i=0; i < 8; i++)
            {

                //Não precisa do cast para long, pois só usa 4 bytes de cada posição
                H0x[i] = (0x10000000L * (long) Hx[8*i + 0]) + (0x01000000L * (long) Hx[8*i + 1])
                        + (0x00100000L * (long) Hx[8*i + 2]) + (0x00010000L * (long) Hx[8*i + 3])
                        + (0x00001000L * (long) Hx[8*i + 4]) + (0x00000100L * (long) Hx[8*i + 5])
                        + (0x00000010L * (long) Hx[8*i + 6]) + (0x00000001L * (long) Hx[8*i + 7]);

            }

            // Para String de 448 bits 56 letras A

            int i = 0;
            boolean firstCyle = true;

            //Toast.makeText(SHA256G.this,"Entrada Inválida",Toast.LENGTH_SHORT).show();

            for(; i < txtcomp.length - (txtcomp.length % 64); i = i + 64)
            {

                W[0] = (0x01000000L * (long) (txtcomp[i] & 0x000000FFL)) + (0x00010000L * (long) (txtcomp[i + 1] & 0x000000FFL))
                        + (0x00000100L * (long) (txtcomp[i + 2] & 0x000000FFL)) + (0x00000001L * (long) (txtcomp[i + 3] & 0x000000FFL));
                W[1] = (0x01000000L * (long) (txtcomp[i + 4] & 0x000000FFL)) + (0x00010000L * (long) (txtcomp[i + 5] & 0x000000FFL))
                        + (0x00000100L * (long) (txtcomp[i + 6] & 0x000000FFL)) + (0x00000001L * (long) (txtcomp[i + 7] & 0x000000FFL));
                W[2] = (0x01000000L * (long) (txtcomp[i + 8] & 0x000000FFL)) + (0x00010000L * (long) (txtcomp[i + 9] & 0x000000FFL))
                        + (0x00000100L * (long) (txtcomp[i + 10] & 0x000000FFL)) + (0x00000001L * (long) (txtcomp[i + 11] & 0x000000FFL));
                W[3] = (0x01000000L * (long) (txtcomp[i + 12] & 0x000000FFL)) + (0x00010000L * (long) (txtcomp[i + 13] & 0x000000FFL))
                        + (0x00000100L * (long) (txtcomp[i + 14] & 0x000000FFL)) + (0x00000001L * (long) (txtcomp[i + 15] & 0x000000FFL));
                W[4] = (0x01000000L * (long) (txtcomp[i + 16] & 0x000000FFL)) + (0x00010000L * (long) (txtcomp[i + 17] & 0x000000FFL))
                        + (0x00000100L * (long) (txtcomp[i + 18] & 0x000000FFL)) + (0x00000001L * (long) (txtcomp[i + 19] & 0x000000FFL));
                W[5] = (0x01000000L * (long) (txtcomp[i + 20] & 0x000000FFL)) + (0x00010000L * (long) (txtcomp[i + 21] & 0x000000FFL))
                        + (0x00000100L * (long) (txtcomp[i + 22] & 0x000000FFL)) + (0x00000001L * (long) (txtcomp[i + 23] & 0x000000FFL));
                W[6] = (0x01000000L * (long) (txtcomp[i + 24] & 0x000000FFL)) + (0x00010000L * (long) (txtcomp[i + 25] & 0x000000FFL))
                        + (0x00000100L * (long) (txtcomp[i + 26] & 0x000000FFL)) + (0x00000001L * (long) (txtcomp[i + 27] & 0x000000FFL));
                W[7] = (0x01000000L * (long) (txtcomp[i + 28] & 0x000000FFL)) + (0x00010000L * (long) (txtcomp[i + 29] & 0x000000FFL))
                        + (0x00000100L * (long) (txtcomp[i + 30] & 0x000000FFL)) + (0x00000001L * (long) (txtcomp[i + 31] & 0x000000FFL));
                W[8] = (0x01000000L * (long) (txtcomp[i + 32] & 0x000000FFL)) + (0x00010000L * (long) (txtcomp[i + 33] & 0x000000FFL))
                        + (0x00000100L * (long) (txtcomp[i + 34] & 0x000000FFL)) + (0x00000001L * (long) (txtcomp[i + 35] & 0x000000FFL));
                W[9] = (0x01000000L * (long) (txtcomp[i + 36] & 0x000000FFL)) + (0x00010000L * (long) (txtcomp[i + 37] & 0x000000FFL))
                        + (0x00000100L * (long) (txtcomp[i + 38] & 0x000000FFL)) + (0x00000001L * (long) (txtcomp[i + 39] & 0x000000FFL));
                W[10] = (0x01000000L * (long) (txtcomp[i + 40] & 0x000000FFL)) + (0x00010000L * (long) (txtcomp[i + 41] & 0x000000FFL))
                        + (0x00000100L * (long) (txtcomp[i + 42] & 0x000000FFL)) + (0x00000001L * (long) (txtcomp[i + 43] & 0x000000FFL));
                W[11] = (0x01000000L * (long) (txtcomp[i + 44] & 0x000000FFL)) + (0x00010000L * (long) (txtcomp[i + 45] & 0x000000FFL))
                        + (0x00000100L * (long) (txtcomp[i + 46] & 0x000000FFL)) + (0x00000001L * (long) (txtcomp[i + 47] & 0x000000FFL));
                W[12] = (0x01000000L * (long) (txtcomp[i + 48] & 0x000000FFL)) + (0x00010000L * (long) (txtcomp[i + 49] & 0x000000FFL))
                        + (0x00000100L * (long) (txtcomp[i + 50] & 0x000000FFL)) + (0x00000001L * (long) (txtcomp[i + 51] & 0x000000FFL));
                W[13] = (0x01000000L * (long) (txtcomp[i + 52] & 0x000000FFL)) + (0x00010000L * (long) (txtcomp[i + 53] & 0x000000FFL))
                        + (0x00000100L * (long) (txtcomp[i + 54] & 0x000000FFL)) + (0x00000001L * (long) (txtcomp[i + 55] & 0x000000FFL));
                W[14] = (0x01000000L * (long) (txtcomp[i + 56] & 0x000000FFL)) + (0x00010000L * (long) (txtcomp[i + 57] & 0x000000FFL))
                        + (0x00000100L * (long) (txtcomp[i + 58] & 0x000000FFL)) + (0x00000001L * (long) (txtcomp[i + 59] & 0x000000FFL));
                W[15] = (0x01000000L * (long) (txtcomp[i + 60] & 0x000000FFL)) + (0x00010000L * (long) (txtcomp[i + 61] & 0x000000FFL))
                        + (0x00000100L * (long) (txtcomp[i + 62] & 0x000000FFL)) + (0x00000001L * (long) (txtcomp[i + 63] & 0x000000FFL));

                if(firstCyle)
                {
                    sha_256(W, H0x, Hi);
                    firstCyle = false;
                }
                else sha_256 (W, Hi, Hi);
            }

            W[0] = 0x00000000;  W[1] = 0x00000000; W[2] = 0x00000000; W[3] = 0x00000000;
            W[4] = 0x00000000; W[5] = 0x00000000; W[6] = 0x00000000; W[7] = 0x00000000;
            W[8] = 0x00000000; W[9] = 0x00000000; W[10] = 0x00000000; W[11] = 0x00000000;
            W[12] = 0x00000000; W[13] = 0x00000000; W[14] = 0x00000000; W[15] = 0x00000000;

            int j;
            int k = 0;
            for(j = i; j < txtcomp.length; j++)
            {
                switch(j%4) {
                    case 0:
                        W[k]  = (0x01000000L * (long) (txtcomp[j]  & 0x000000FFL));
                        break;
                    case 1:
                        W[k]  += (0x00010000L * (long) (txtcomp[j]  & 0x000000FFL));
                        break;
                    case 2:
                        W[k]  += (0x00000100L * (long) (txtcomp[j]  & 0x000000FFL));
                        break;
                    case 3:
                        W[k]  += (0x00000001L * (long) (txtcomp[j]  & 0x000000FFL));
                        //so para para o proximo k quando passar pelos 4
                        k++;
                        break;
                }
            }

            switch((j-1)%4) {
                case 0:
                    W[k]  += 0x00800000L;
                    break;
                case 1:
                    W[k]  += 0x00008000L;
                    break;
                case 2:
                    W[k]  += 0x00000080L;
                    break;
                case 3:
                    //neste caso será o k posterior
                    W[k]  = 0x80000000L;;
                    break;
            }

            if(txtcomp.length % 64 < 56)
            {
                //W[k]  = 0x80000000L;
                //W[k+1]  = 0x00000000L;
                W[14]  = 0x00000000L;
                W[15]  = 0x00000001L * (long) (8*txtcomp.length);

                if(firstCyle)
                {
                    sha_256(W, H0x, Hi);
                    firstCyle = false;
                }
                else sha_256 (W, Hi, Hi);
            }
            else
            {
                //W[k]  = 0x80000000L;
                //W[k+1]  = 0x00000000L;
                //W[15]  = 0x00000000L; //Esta linha pode ter causado uma serie de problemas, mas ainda não foi analisado direito
                //W[k+2]  = 0x00000001L * (long) strIn.length();
                if(firstCyle)
                {
                    sha_256(W, H0x, Hi);
                    firstCyle = false;
                }
                else sha_256 (W, Hi, Hi);

                W[0] = 0x00000000; W[1] = 0x00000000; W[2] = 0x00000000; W[3] = 0x00000000;
                W[4] = 0x00000000; W[5] = 0x00000000; W[6] = 0x00000000; W[7] = 0x00000000;
                W[8] = 0x00000000; W[9] = 0x00000000; W[10] = 0x00000000; W[11] = 0x00000000;
                W[12] = 0x00000000; W[13] = 0x00000000; W[14] = 0x00000000; W[15] = 0x00000001L * (long) (8*txtcomp.length);

                sha_256 (W, Hi, Hi);
            }

            for(i = 0; i < 8; i++)
            {
                result[i*8 + 0] = (char)  ((Hi[i]/0x10000000L) & 0x0000000FL);
                result[i*8 + 1] = (char)  ((Hi[i]/0x01000000L) & 0x0000000FL);
                result[i*8 + 2] = (char)  ((Hi[i]/0x00100000L) & 0x0000000FL);
                result[i*8 + 3] = (char)  ((Hi[i]/0x00010000L) & 0x0000000FL);
                result[i*8 + 4] = (char)  ((Hi[i]/0x00001000L) & 0x0000000FL);
                result[i*8 + 5] = (char)  ((Hi[i]/0x00000100L) & 0x0000000FL);
                result[i*8 + 6] = (char)  ((Hi[i]/0x00000010L) & 0x0000000FL);
                result[i*8 + 7] = (char)  ((Hi[i]/0x00000001L) & 0x0000000FL);
            }

            char base16[] = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
            for(i = 0; i < 64; i++) result[i] = base16[(int)result[i]];

            return String.valueOf(result);
        }
        return null;
    }

    //Metodo usado para construir um Hash com H0 diferente
    //Entradas podem ser mensagem e H0 diferentes
    //Hx precisa ter 32 bytes;
    public static String SHA256MsnHxHex2(byte[] txtcomp, byte[] Hx)
    {
        long[] W= {

                0x00000000, 0x00000000, 0x00000000, 0x00000000,
                0x00000000, 0x00000000, 0x00000000, 0x00000000,
                0x00000000, 0x00000000, 0x00000000, 0x00000000,
                0x00000000, 0x00000000, 0x00000000, 0x00000008,
                0x00000000, 0x00000000, 0x00000000, 0x00000000,
                0x00000000, 0x00000000, 0x00000000, 0x00000000,
                0x00000000, 0x00000000, 0x00000000, 0x00000000,
                0x00000000, 0x00000000, 0x00000000, 0x00000000,
                0x00000000, 0x00000000, 0x00000000, 0x00000000,
                0x00000000, 0x00000000, 0x00000000, 0x00000000,
                0x00000000, 0x00000000, 0x00000000, 0x00000000,
                0x00000000, 0x00000000, 0x00000000, 0x00000000,
                0x00000000, 0x00000000, 0x00000000, 0x00000000,
                0x00000000, 0x00000000, 0x00000000, 0x00000000,
                0x00000000, 0x00000000, 0x00000000, 0x00000000,
                0x00000000, 0x00000000, 0x00000000, 0x00000000

        };
        long[] Hi=   {

                0x00000000, 0x00000000, 0x00000000, 0x00000000,
                0x00000000, 0x00000000, 0x00000000, 0x00000000
        };

        long[] H0x=   {

                0x00000000, 0x00000000, 0x00000000, 0x00000000,
                0x00000000, 0x00000000, 0x00000000, 0x00000000
        };

        //byte [] result = new

        if((txtcomp.length>0) && (Hx.length == 32)) {

            //char[] txtcomp = new char[strIn.length];
            char[] result = new char[64];
            //char[] H0xStr = new char[32];

            for(int i=0; i < 8; i++)
            {

                H0x[i] = (0x01000000L * (long) (Hx[4*i + 0] & 0x00000000FFL))
                        + (0x00010000L * (long) (Hx[4*i + 1] & 0x00000000FFL))
                        + (0x00000100L * (long) (Hx[4*i + 2] & 0x00000000FFL))
                        + (0x00000001L * (long) (Hx[4*i + 3] & 0x00000000FFL));

            }

            // Para String de 448 bits 56 letras A

            int i = 0;
            boolean firstCyle = true;

            //Toast.makeText(SHA256G.this,"Entrada Inválida",Toast.LENGTH_SHORT).show();

            for(; i < txtcomp.length - (txtcomp.length % 64); i = i + 64)
            {
                W[0] = (0x01000000L * (long) (txtcomp[i] & 0x000000FFL)) + (0x00010000L * (long) (txtcomp[i + 1] & 0x000000FFL))
                        + (0x00000100L * (long) (txtcomp[i + 2] & 0x000000FFL)) + (0x00000001L * (long) (txtcomp[i + 3] & 0x000000FFL));
                W[1] = (0x01000000L * (long) (txtcomp[i + 4] & 0x000000FFL)) + (0x00010000L * (long) (txtcomp[i + 5] & 0x000000FFL))
                        + (0x00000100L * (long) (txtcomp[i + 6] & 0x000000FFL)) + (0x00000001L * (long) (txtcomp[i + 7] & 0x000000FFL));
                W[2] = (0x01000000L * (long) (txtcomp[i + 8] & 0x000000FFL)) + (0x00010000L * (long) (txtcomp[i + 9] & 0x000000FFL))
                        + (0x00000100L * (long) (txtcomp[i + 10] & 0x000000FFL)) + (0x00000001L * (long) (txtcomp[i + 11] & 0x000000FFL));
                W[3] = (0x01000000L * (long) (txtcomp[i + 12] & 0x000000FFL)) + (0x00010000L * (long) (txtcomp[i + 13] & 0x000000FFL))
                        + (0x00000100L * (long) (txtcomp[i + 14] & 0x000000FFL)) + (0x00000001L * (long) (txtcomp[i + 15] & 0x000000FFL));
                W[4] = (0x01000000L * (long) (txtcomp[i + 16] & 0x000000FFL)) + (0x00010000L * (long) (txtcomp[i + 17] & 0x000000FFL))
                        + (0x00000100L * (long) (txtcomp[i + 18] & 0x000000FFL)) + (0x00000001L * (long) (txtcomp[i + 19] & 0x000000FFL));
                W[5] = (0x01000000L * (long) (txtcomp[i + 20] & 0x000000FFL)) + (0x00010000L * (long) (txtcomp[i + 21] & 0x000000FFL))
                        + (0x00000100L * (long) (txtcomp[i + 22] & 0x000000FFL)) + (0x00000001L * (long) (txtcomp[i + 23] & 0x000000FFL));
                W[6] = (0x01000000L * (long) (txtcomp[i + 24] & 0x000000FFL)) + (0x00010000L * (long) (txtcomp[i + 25] & 0x000000FFL))
                        + (0x00000100L * (long) (txtcomp[i + 26] & 0x000000FFL)) + (0x00000001L * (long) (txtcomp[i + 27] & 0x000000FFL));
                W[7] = (0x01000000L * (long) (txtcomp[i + 28] & 0x000000FFL)) + (0x00010000L * (long) (txtcomp[i + 29] & 0x000000FFL))
                        + (0x00000100L * (long) (txtcomp[i + 30] & 0x000000FFL)) + (0x00000001L * (long) (txtcomp[i + 31] & 0x000000FFL));
                W[8] = (0x01000000L * (long) (txtcomp[i + 32] & 0x000000FFL)) + (0x00010000L * (long) (txtcomp[i + 33] & 0x000000FFL))
                        + (0x00000100L * (long) (txtcomp[i + 34] & 0x000000FFL)) + (0x00000001L * (long) (txtcomp[i + 35] & 0x000000FFL));
                W[9] = (0x01000000L * (long) (txtcomp[i + 36] & 0x000000FFL)) + (0x00010000L * (long) (txtcomp[i + 37] & 0x000000FFL))
                        + (0x00000100L * (long) (txtcomp[i + 38] & 0x000000FFL)) + (0x00000001L * (long) (txtcomp[i + 39] & 0x000000FFL));
                W[10] = (0x01000000L * (long) (txtcomp[i + 40] & 0x000000FFL)) + (0x00010000L * (long) (txtcomp[i + 41] & 0x000000FFL))
                        + (0x00000100L * (long) (txtcomp[i + 42] & 0x000000FFL)) + (0x00000001L * (long) (txtcomp[i + 43] & 0x000000FFL));
                W[11] = (0x01000000L * (long) (txtcomp[i + 44] & 0x000000FFL)) + (0x00010000L * (long) (txtcomp[i + 45] & 0x000000FFL))
                        + (0x00000100L * (long) (txtcomp[i + 46] & 0x000000FFL)) + (0x00000001L * (long) (txtcomp[i + 47] & 0x000000FFL));
                W[12] = (0x01000000L * (long) (txtcomp[i + 48] & 0x000000FFL)) + (0x00010000L * (long) (txtcomp[i + 49] & 0x000000FFL))
                        + (0x00000100L * (long) (txtcomp[i + 50] & 0x000000FFL)) + (0x00000001L * (long) (txtcomp[i + 51] & 0x000000FFL));
                W[13] = (0x01000000L * (long) (txtcomp[i + 52] & 0x000000FFL)) + (0x00010000L * (long) (txtcomp[i + 53] & 0x000000FFL))
                        + (0x00000100L * (long) (txtcomp[i + 54] & 0x000000FFL)) + (0x00000001L * (long) (txtcomp[i + 55] & 0x000000FFL));
                W[14] = (0x01000000L * (long) (txtcomp[i + 56] & 0x000000FFL)) + (0x00010000L * (long) (txtcomp[i + 57] & 0x000000FFL))
                        + (0x00000100L * (long) (txtcomp[i + 58] & 0x000000FFL)) + (0x00000001L * (long) (txtcomp[i + 59] & 0x000000FFL));
                W[15] = (0x01000000L * (long) (txtcomp[i + 60] & 0x000000FFL)) + (0x00010000L * (long) (txtcomp[i + 61] & 0x000000FFL))
                        + (0x00000100L * (long) (txtcomp[i + 62] & 0x000000FFL)) + (0x00000001L * (long) (txtcomp[i + 63] & 0x000000FFL));

                if(firstCyle)
                {
                    sha_256(W, H0x, Hi);
                    firstCyle = false;
                }
                else sha_256 (W, Hi, Hi);
            }

            W[0] = 0x00000000;  W[1] = 0x00000000; W[2] = 0x00000000; W[3] = 0x00000000;
            W[4] = 0x00000000; W[5] = 0x00000000; W[6] = 0x00000000; W[7] = 0x00000000;
            W[8] = 0x00000000; W[9] = 0x00000000; W[10] = 0x00000000; W[11] = 0x00000000;
            W[12] = 0x00000000; W[13] = 0x00000000; W[14] = 0x00000000; W[15] = 0x00000000;

            int j;
            int k = 0;
            for(j = i; j < txtcomp.length; j++)
            {
                switch(j%4) {
                    case 0:
                        W[k]  = (0x01000000L * (long) (txtcomp[j]  & 0x000000FFL));
                        break;
                    case 1:
                        W[k]  += (0x00010000L * (long) (txtcomp[j]  & 0x000000FFL));
                        break;
                    case 2:
                        W[k]  += (0x00000100L * (long) (txtcomp[j]  & 0x000000FFL));
                        break;
                    case 3:
                        W[k]  += (0x00000001L * (long) (txtcomp[j]  & 0x000000FFL));
                        //so para para o proximo k quando passar pelos 4
                        k++;
                        break;
                }

            }

            switch((j-1)%4) {
                case 0:
                    W[k]  += 0x00800000L;
                    break;
                case 1:
                    W[k]  += 0x00008000L;
                    break;
                case 2:
                    W[k]  += 0x00000080L;
                    break;
                case 3:
                    //neste caso será o k posterior
                    W[k]  = 0x80000000L;;
                    break;
            }

            if(txtcomp.length % 64 < 56)
            {
                //W[k]  = 0x80000000L;
                //W[k+1]  = 0x00000000L;
                W[14]  = 0x00000000L;
                W[15]  = 0x00000001L * (long) (8*txtcomp.length);

                if(firstCyle)
                {
                    sha_256(W, H0x, Hi);
                    firstCyle = false;
                }
                else sha_256 (W, Hi, Hi);
            }
            else
            {
                //W[k]  = 0x80000000L;
                //W[k+1]  = 0x00000000L;
                //W[15]  = 0x00000000L; //Esta linha pode ter causado uma serie de problemas, mas ainda não foi analisado direito
                //W[k+2]  = 0x00000001L * (long) strIn.length();
                if(firstCyle)
                {
                    sha_256(W, H0x, Hi);
                    firstCyle = false;
                }
                else sha_256 (W, Hi, Hi);

                W[0] = 0x00000000; W[1] = 0x00000000; W[2] = 0x00000000; W[3] = 0x00000000;
                W[4] = 0x00000000; W[5] = 0x00000000; W[6] = 0x00000000; W[7] = 0x00000000;
                W[8] = 0x00000000; W[9] = 0x00000000; W[10] = 0x00000000; W[11] = 0x00000000;
                W[12] = 0x00000000; W[13] = 0x00000000; W[14] = 0x00000000; W[15] = 0x00000001L * (long) (8*txtcomp.length);

                sha_256 (W, Hi, Hi);
            }

            for(i = 0; i < 8; i++)
            {
                result[i*8 + 0] = (char)  ((Hi[i]/0x10000000L) & 0x0000000FL);
                result[i*8 + 1] = (char)  ((Hi[i]/0x01000000L) & 0x0000000FL);
                result[i*8 + 2] = (char)  ((Hi[i]/0x00100000L) & 0x0000000FL);
                result[i*8 + 3] = (char)  ((Hi[i]/0x00010000L) & 0x0000000FL);
                result[i*8 + 4] = (char)  ((Hi[i]/0x00001000L) & 0x0000000FL);
                result[i*8 + 5] = (char)  ((Hi[i]/0x00000100L) & 0x0000000FL);
                result[i*8 + 6] = (char)  ((Hi[i]/0x00000010L) & 0x0000000FL);
                result[i*8 + 7] = (char)  ((Hi[i]/0x00000001L) & 0x0000000FL);
            }

            char base16[] = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
            for(i = 0; i < 64; i++) result[i] = base16[(int)result[i]];

            return String.valueOf(result);
        }
        return null;
    }

    //Para teste com H0
    public static String H0000000(byte[] txtcomp, byte[] Hx)
    {

        long[] W= {

                0x00000000, 0x00000000, 0x00000000, 0x00000000,
                0x00000000, 0x00000000, 0x00000000, 0x00000000,
                0x00000000, 0x00000000, 0x00000000, 0x00000000,
                0x00000000, 0x00000000, 0x00000000, 0x00000008,
                0x00000000, 0x00000000, 0x00000000, 0x00000000,
                0x00000000, 0x00000000, 0x00000000, 0x00000000,
                0x00000000, 0x00000000, 0x00000000, 0x00000000,
                0x00000000, 0x00000000, 0x00000000, 0x00000000,
                0x00000000, 0x00000000, 0x00000000, 0x00000000,
                0x00000000, 0x00000000, 0x00000000, 0x00000000,
                0x00000000, 0x00000000, 0x00000000, 0x00000000,
                0x00000000, 0x00000000, 0x00000000, 0x00000000,
                0x00000000, 0x00000000, 0x00000000, 0x00000000,
                0x00000000, 0x00000000, 0x00000000, 0x00000000,
                0x00000000, 0x00000000, 0x00000000, 0x00000000,
                0x00000000, 0x00000000, 0x00000000, 0x00000000

        };
        long[] Hi=   {

                0x00000000, 0x00000000, 0x00000000, 0x00000000,
                0x00000000, 0x00000000, 0x00000000, 0x00000000
        };

        long[] H0x=   {

                0x00000000, 0x00000000, 0x00000000, 0x00000000,
                0x00000000, 0x00000000, 0x00000000, 0x00000000
        };


        if((txtcomp.length>0) && (Hx.length == 32)) {

            //char[] txtcomp = new char[strIn.length];
            char[] result = new char[64];


            for(int i=0; i < 8; i++)
            {
                H0x[i] = (0x01000000L * (long) (Hx[4*i + 0] & 0x00000000FFL))
                        + (0x00010000L * (long) (Hx[4*i + 1] & 0x00000000FFL))
                        + (0x00000100L * (long) (Hx[4*i + 2] & 0x00000000FFL)) + (0x00000001L * (long) (Hx[4*i + 3] & 0x00000000FFL));
            }

            int i;

            for(i = 0; i < 8; i++)
            {
                result[i*8 + 0] = (char)  ((H0x[i]/0x10000000L) & 0x0000000FL);
                result[i*8 + 1] = (char)  ((H0x[i]/0x01000000L) & 0x0000000FL);
                result[i*8 + 2] = (char)  ((H0x[i]/0x00100000L) & 0x0000000FL);
                result[i*8 + 3] = (char)  ((H0x[i]/0x00010000L) & 0x0000000FL);
                result[i*8 + 4] = (char)  ((H0x[i]/0x00001000L) & 0x0000000FL);
                result[i*8 + 5] = (char)  ((H0x[i]/0x00000100L) & 0x0000000FL);
                result[i*8 + 6] = (char)  ((H0x[i]/0x00000010L) & 0x0000000FL);
                result[i*8 + 7] = (char)  ((H0x[i]/0x00000001L) & 0x0000000FL);
            }

            char base16[] = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
            for(i = 0; i < 64; i++) result[i] = base16[(int)result[i]];

            return String.valueOf(result);
        }
        return null;
    }



    public static byte[] SHA256MsnHxHex3(byte[] txtcomp, byte[] Hx)
    {
        long[] W= {

                0x00000000, 0x00000000, 0x00000000, 0x00000000,
                0x00000000, 0x00000000, 0x00000000, 0x00000000,
                0x00000000, 0x00000000, 0x00000000, 0x00000000,
                0x00000000, 0x00000000, 0x00000000, 0x00000008,
                0x00000000, 0x00000000, 0x00000000, 0x00000000,
                0x00000000, 0x00000000, 0x00000000, 0x00000000,
                0x00000000, 0x00000000, 0x00000000, 0x00000000,
                0x00000000, 0x00000000, 0x00000000, 0x00000000,
                0x00000000, 0x00000000, 0x00000000, 0x00000000,
                0x00000000, 0x00000000, 0x00000000, 0x00000000,
                0x00000000, 0x00000000, 0x00000000, 0x00000000,
                0x00000000, 0x00000000, 0x00000000, 0x00000000,
                0x00000000, 0x00000000, 0x00000000, 0x00000000,
                0x00000000, 0x00000000, 0x00000000, 0x00000000,
                0x00000000, 0x00000000, 0x00000000, 0x00000000,
                0x00000000, 0x00000000, 0x00000000, 0x00000000

        };
        long[] Hi=   {

                0x00000000, 0x00000000, 0x00000000, 0x00000000,
                0x00000000, 0x00000000, 0x00000000, 0x00000000
        };

        long[] H0x=   {

                0x00000000, 0x00000000, 0x00000000, 0x00000000,
                0x00000000, 0x00000000, 0x00000000, 0x00000000
        };

        //byte [] result = new

        if((txtcomp.length>0) && (Hx.length == 32)) {

            //char[] txtcomp = new char[strIn.length];
            char[] result = new char[64];
            //char[] H0xStr = new char[32];

            for(int i=0; i < 8; i++)
            {

                H0x[i] = (0x01000000L * (long) (Hx[4*i + 0] & 0x00000000FFL))
                        + (0x00010000L * (long) (Hx[4*i + 1] & 0x00000000FFL))
                        + (0x00000100L * (long) (Hx[4*i + 2] & 0x00000000FFL))
                        + (0x00000001L * (long) (Hx[4*i + 3] & 0x00000000FFL));

            }

            // Para String de 448 bits 56 letras A

            int i = 0;
            boolean firstCyle = true;

            //Toast.makeText(SHA256G.this,"Entrada Inválida",Toast.LENGTH_SHORT).show();

            for(; i < txtcomp.length - (txtcomp.length % 64); i = i + 64)
            {
                W[0] = (0x01000000L * (long) (txtcomp[i] & 0x000000FFL)) + (0x00010000L * (long) (txtcomp[i + 1] & 0x000000FFL))
                        + (0x00000100L * (long) (txtcomp[i + 2] & 0x000000FFL)) + (0x00000001L * (long) (txtcomp[i + 3] & 0x000000FFL));
                W[1] = (0x01000000L * (long) (txtcomp[i + 4] & 0x000000FFL)) + (0x00010000L * (long) (txtcomp[i + 5] & 0x000000FFL))
                        + (0x00000100L * (long) (txtcomp[i + 6] & 0x000000FFL)) + (0x00000001L * (long) (txtcomp[i + 7] & 0x000000FFL));
                W[2] = (0x01000000L * (long) (txtcomp[i + 8] & 0x000000FFL)) + (0x00010000L * (long) (txtcomp[i + 9] & 0x000000FFL))
                        + (0x00000100L * (long) (txtcomp[i + 10] & 0x000000FFL)) + (0x00000001L * (long) (txtcomp[i + 11] & 0x000000FFL));
                W[3] = (0x01000000L * (long) (txtcomp[i + 12] & 0x000000FFL)) + (0x00010000L * (long) (txtcomp[i + 13] & 0x000000FFL))
                        + (0x00000100L * (long) (txtcomp[i + 14] & 0x000000FFL)) + (0x00000001L * (long) (txtcomp[i + 15] & 0x000000FFL));
                W[4] = (0x01000000L * (long) (txtcomp[i + 16] & 0x000000FFL)) + (0x00010000L * (long) (txtcomp[i + 17] & 0x000000FFL))
                        + (0x00000100L * (long) (txtcomp[i + 18] & 0x000000FFL)) + (0x00000001L * (long) (txtcomp[i + 19] & 0x000000FFL));
                W[5] = (0x01000000L * (long) (txtcomp[i + 20] & 0x000000FFL)) + (0x00010000L * (long) (txtcomp[i + 21] & 0x000000FFL))
                        + (0x00000100L * (long) (txtcomp[i + 22] & 0x000000FFL)) + (0x00000001L * (long) (txtcomp[i + 23] & 0x000000FFL));
                W[6] = (0x01000000L * (long) (txtcomp[i + 24] & 0x000000FFL)) + (0x00010000L * (long) (txtcomp[i + 25] & 0x000000FFL))
                        + (0x00000100L * (long) (txtcomp[i + 26] & 0x000000FFL)) + (0x00000001L * (long) (txtcomp[i + 27] & 0x000000FFL));
                W[7] = (0x01000000L * (long) (txtcomp[i + 28] & 0x000000FFL)) + (0x00010000L * (long) (txtcomp[i + 29] & 0x000000FFL))
                        + (0x00000100L * (long) (txtcomp[i + 30] & 0x000000FFL)) + (0x00000001L * (long) (txtcomp[i + 31] & 0x000000FFL));
                W[8] = (0x01000000L * (long) (txtcomp[i + 32] & 0x000000FFL)) + (0x00010000L * (long) (txtcomp[i + 33] & 0x000000FFL))
                        + (0x00000100L * (long) (txtcomp[i + 34] & 0x000000FFL)) + (0x00000001L * (long) (txtcomp[i + 35] & 0x000000FFL));
                W[9] = (0x01000000L * (long) (txtcomp[i + 36] & 0x000000FFL)) + (0x00010000L * (long) (txtcomp[i + 37] & 0x000000FFL))
                        + (0x00000100L * (long) (txtcomp[i + 38] & 0x000000FFL)) + (0x00000001L * (long) (txtcomp[i + 39] & 0x000000FFL));
                W[10] = (0x01000000L * (long) (txtcomp[i + 40] & 0x000000FFL)) + (0x00010000L * (long) (txtcomp[i + 41] & 0x000000FFL))
                        + (0x00000100L * (long) (txtcomp[i + 42] & 0x000000FFL)) + (0x00000001L * (long) (txtcomp[i + 43] & 0x000000FFL));
                W[11] = (0x01000000L * (long) (txtcomp[i + 44] & 0x000000FFL)) + (0x00010000L * (long) (txtcomp[i + 45] & 0x000000FFL))
                        + (0x00000100L * (long) (txtcomp[i + 46] & 0x000000FFL)) + (0x00000001L * (long) (txtcomp[i + 47] & 0x000000FFL));
                W[12] = (0x01000000L * (long) (txtcomp[i + 48] & 0x000000FFL)) + (0x00010000L * (long) (txtcomp[i + 49] & 0x000000FFL))
                        + (0x00000100L * (long) (txtcomp[i + 50] & 0x000000FFL)) + (0x00000001L * (long) (txtcomp[i + 51] & 0x000000FFL));
                W[13] = (0x01000000L * (long) (txtcomp[i + 52] & 0x000000FFL)) + (0x00010000L * (long) (txtcomp[i + 53] & 0x000000FFL))
                        + (0x00000100L * (long) (txtcomp[i + 54] & 0x000000FFL)) + (0x00000001L * (long) (txtcomp[i + 55] & 0x000000FFL));
                W[14] = (0x01000000L * (long) (txtcomp[i + 56] & 0x000000FFL)) + (0x00010000L * (long) (txtcomp[i + 57] & 0x000000FFL))
                        + (0x00000100L * (long) (txtcomp[i + 58] & 0x000000FFL)) + (0x00000001L * (long) (txtcomp[i + 59] & 0x000000FFL));
                W[15] = (0x01000000L * (long) (txtcomp[i + 60] & 0x000000FFL)) + (0x00010000L * (long) (txtcomp[i + 61] & 0x000000FFL))
                        + (0x00000100L * (long) (txtcomp[i + 62] & 0x000000FFL)) + (0x00000001L * (long) (txtcomp[i + 63] & 0x000000FFL));

                if(firstCyle)
                {
                    sha_256(W, H0x, Hi);
                    firstCyle = false;
                }
                else sha_256 (W, Hi, Hi);
            }

            W[0] = 0x00000000;  W[1] = 0x00000000; W[2] = 0x00000000; W[3] = 0x00000000;
            W[4] = 0x00000000; W[5] = 0x00000000; W[6] = 0x00000000; W[7] = 0x00000000;
            W[8] = 0x00000000; W[9] = 0x00000000; W[10] = 0x00000000; W[11] = 0x00000000;
            W[12] = 0x00000000; W[13] = 0x00000000; W[14] = 0x00000000; W[15] = 0x00000000;

            int j;
            int k = 0;
            for(j = i; j < txtcomp.length; j++)
            {
                switch(j%4) {
                    case 0:
                        W[k]  = (0x01000000L * (long) (txtcomp[j]  & 0x000000FFL));
                        break;
                    case 1:
                        W[k]  += (0x00010000L * (long) (txtcomp[j]  & 0x000000FFL));
                        break;
                    case 2:
                        W[k]  += (0x00000100L * (long) (txtcomp[j]  & 0x000000FFL));
                        break;
                    case 3:
                        W[k]  += (0x00000001L * (long) (txtcomp[j]  & 0x000000FFL));
                        //so para para o proximo k quando passar pelos 4
                        k++;
                        break;
                }

            }

            switch((j-1)%4) {
                case 0:
                    W[k]  += 0x00800000L;
                    break;
                case 1:
                    W[k]  += 0x00008000L;
                    break;
                case 2:
                    W[k]  += 0x00000080L;
                    break;
                case 3:
                    //neste caso será o k posterior
                    W[k]  = 0x80000000L;;
                    break;
            }

            if(txtcomp.length % 64 < 56)
            {
                //W[k]  = 0x80000000L;
                //W[k+1]  = 0x00000000L;
                W[14]  = 0x00000000L;
                W[15]  = 0x00000001L * (long) (8*txtcomp.length);

                if(firstCyle)
                {
                    sha_256(W, H0x, Hi);
                    firstCyle = false;
                }
                else sha_256 (W, Hi, Hi);
            }
            else
            {
                //W[k]  = 0x80000000L;
                //W[k+1]  = 0x00000000L;
                //W[15]  = 0x00000000L; //Esta linha pode ter causado uma serie de problemas, mas ainda não foi analisado direito
                //W[k+2]  = 0x00000001L * (long) strIn.length();
                if(firstCyle)
                {
                    sha_256(W, H0x, Hi);
                    firstCyle = false;
                }
                else sha_256 (W, Hi, Hi);

                W[0] = 0x00000000; W[1] = 0x00000000; W[2] = 0x00000000; W[3] = 0x00000000;
                W[4] = 0x00000000; W[5] = 0x00000000; W[6] = 0x00000000; W[7] = 0x00000000;
                W[8] = 0x00000000; W[9] = 0x00000000; W[10] = 0x00000000; W[11] = 0x00000000;
                W[12] = 0x00000000; W[13] = 0x00000000; W[14] = 0x00000000; W[15] = 0x00000001L * (long) (8*txtcomp.length);

                sha_256 (W, Hi, Hi);
            }

/*
            for(i = 0; i < 8; i++)
            {
                result[i*8 + 0] = (char)  ((Hi[i]/0x10000000L) & 0x0000000FL);
                result[i*8 + 1] = (char)  ((Hi[i]/0x01000000L) & 0x0000000FL);
                result[i*8 + 2] = (char)  ((Hi[i]/0x00100000L) & 0x0000000FL);
                result[i*8 + 3] = (char)  ((Hi[i]/0x00010000L) & 0x0000000FL);
                result[i*8 + 4] = (char)  ((Hi[i]/0x00001000L) & 0x0000000FL);
                result[i*8 + 5] = (char)  ((Hi[i]/0x00000100L) & 0x0000000FL);
                result[i*8 + 6] = (char)  ((Hi[i]/0x00000010L) & 0x0000000FL);
                result[i*8 + 7] = (char)  ((Hi[i]/0x00000001L) & 0x0000000FL);
            }

            char base16[] = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
            for(i = 0; i < 64; i++) result[i] = base16[(int)result[i]];
*/

            byte result2[] = new byte[32];
            for(i = 0; i < 8; i++)
            {
                result2[i*4 + 0] = (byte)  ((Hi[i]/0x01000000L) & 0x000000FFL);
                result2[i*4 + 1] = (byte)  ((Hi[i]/0x00010000L) & 0x000000FFL);
                result2[i*4 + 2] = (byte)  ((Hi[i]/0x00000100L) & 0x000000FFL);
                result2[i*4 + 3] = (byte)  ((Hi[i]/0x00000001L) & 0x000000FFL);
            }
            //return String.valueOf(result);
            //return result;
            return result2;
        }
        return null;
    }





}
