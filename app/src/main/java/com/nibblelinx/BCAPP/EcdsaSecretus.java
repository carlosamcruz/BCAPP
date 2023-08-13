package com.nibblelinx.BCAPP;

import java.math.BigInteger;
//import java.security.PKCS12Attribute;

/////////////////////////////////////////////////
//JESUS is the LORD!!!
/////////////////////////////////////////////////
public class EcdsaSecretus {

    //BigInteger p, Gx, Gy, n_order;
    private BigInteger Ln = BigInteger.valueOf(0);
    private com.nibblelinx.BCAPP.Keygen keyGEN = new com.nibblelinx.BCAPP.Keygen();
    private Ecc myEcc = new Ecc();


    private BigInteger [] point = new BigInteger[2];
    private BigInteger [] pointNULL = new BigInteger[2];
    EcdsaSecretus()
    {
        BigInteger n_order = myEcc.n_order;
        while(n_order.compareTo(BigInteger.valueOf(0)) != 0)
        {
            Ln = Ln.multiply(BigInteger.valueOf(2));
            Ln = Ln.add(BigInteger.valueOf(1));
            //Ln++;
            n_order = n_order.divide(BigInteger.valueOf(2));
        }
    }
    /////////////////////////////////////////////////
    //JESUS is the LORD!!!
    /////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////
    //Geração do K a partir da Chave Privada do Emissor
    ///////////////////////////////////////////////////////////

    public BigInteger Knum(String HA4,String PVTKEY, String e)
    {
        //Verificar os NULLs
        String x = SHA256G.SHA256MsnHxHex2(SHA256G.HashStrToByte2(PVTKEY + e), SHA256G.HashStrToByte2(HA4));
        while (x == null)
        {
            x = x + "1";
            x = SHA256G.SHA256MsnHxHex2(SHA256G.HashStrToByte2(PVTKEY + e + x), SHA256G.HashStrToByte2(HA4));
        }
        //if (x == null) return null;

        String H1a = SHA256G.SHA256MsnHxHex2(SHA256G.HashStrToByte2(e), SHA256G.HashStrToByte2(x));

        while (H1a == null)
        {
            H1a = H1a + "1";
            H1a = SHA256G.SHA256MsnHxHex2(SHA256G.HashStrToByte2(e + H1a), SHA256G.HashStrToByte2(x));
        }
        //if (H1a == null) return null;

        BigInteger H1 = keyGEN.HashToBigInt(H1a);

        while((H1.compareTo(BigInteger.valueOf(1)) <= 0) || (H1.compareTo(myEcc.n_order.subtract(BigInteger.valueOf(1))) >= 0 ))
        {
            H1a = H1a + "1";
            H1a = SHA256G.SHA256MsnHxHex2(SHA256G.HashStrToByte2(H1a), SHA256G.HashStrToByte2(x));
            while (H1a == null)
            {
                H1a = H1a + "1";
                H1a = SHA256G.SHA256MsnHxHex2(SHA256G.HashStrToByte2(e + H1a), SHA256G.HashStrToByte2(x));
            }
            //if (H1a == null) return null;
            H1 = keyGEN.HashToBigInt(H1a);
        }
        //return BigInteger.valueOf(0);
        return H1;
    }

    /////////////////////////////////////////////////
    //JESUS is the LORD!!!
    /////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////
    //Criação da Assinatura Digital;
    //https://en.wikipedia.org/wiki/Elliptic_Curve_Digital_Signature_Algorithm
    ///////////////////////////////////////////////////////////
    //MSN - mensagem não encriptada em posse do emissor (De string para array de byte);
    //e = SHA256(SHA2560(MSN))
    //PVTKEY - Chave privada do usuário (String com 64 elementos 0-9 de a-f);
    //HA4 - HASH para formação de K (String com 64 elementos 0-9 de a-f);;
    ///////////////////////////////////////////////////////////
    //public BigInteger [] ECDSABSV (byte[] MSNx, String PVTKEY, String HA4)
    ///////////////////////////////////////////////////////////
    public BigInteger [] ECDSABSV (String e, String PVTKEY, String HA4)
    {
        BigInteger [] signECDSA = new BigInteger[3];
        signECDSA[0] = BigInteger.valueOf(0);
        signECDSA[1] = BigInteger.valueOf(0);
        signECDSA[2] = BigInteger.valueOf(0);
        //1 - Calcular uma HASH(MSN)
        //String e = SHA256G.SHA256bytes(MSN);

        //A MSN seria necessário apenas para formar o elemento e, entao e melhor trazer diretamento e

        /*
        String e = SHA256G.SHA256bytes(MSN);
        //e = SHA256G.SHA256bytes(SHA256G.HashStrToByte2(e));
        e = SHA256G.SHA256bytes(SHA256G.HashStrToByte2(e));
        */


        if (e == null) return signECDSA;
        //byte[] eByte = SHA256G.HashStrToByte2(e); //e pode ser null
        BigInteger z;

        Variables.BarSize = 5;
        /////////////////////////////////////////////////////
        //2 - Selecionar o os Ln bits de e
        /////////////////////////////////////////////////////
        z = keyGEN.HashToBigInt(e); //tranforma o HASH(MSN) de HEX para BigInt
        //Fica apenas com os Bit mais significativos de e
        while (z.compareTo(Ln) > 0)
            z = z.divide(BigInteger.valueOf(2));
        /////////////////////////////////////////////////////
        /////////////////////////////////////////////////////

        BigInteger [] point = new BigInteger[2];
        BigInteger K = BigInteger.valueOf(0);;
        BigInteger r = BigInteger.valueOf(0);
        BigInteger s = BigInteger.valueOf(0);
        boolean Flag = false;
        //STEP 3
        while (Flag == false)
        {
            Flag = true;

            /////////////////////////////////////////////////////
            //3 - Selecionando um Valor de K
            /////////////////////////////////////////////////////
            K = Knum(HA4, PVTKEY, e);

            //Não pode ser null, tem que tratar
            /////////////////////////////////////////////////////
            /////////////////////////////////////////////////////

            /////////////////////////////////////////////////////
            //4 - Calculando um ponto da Curva (x1, y1) = K * G
            /////////////////////////////////////////////////////
            point = myEcc.eccnP(K, myEcc.Gx, myEcc.Gy);
            /////////////////////////////////////////////////////
            /////////////////////////////////////////////////////

            /////////////////////////////////////////////////////
            //5 - Calculando r = x1 mod n
            /////////////////////////////////////////////////////
            r = myEcc.modp(point[0], myEcc.n_order);
            /////////////////////////////////////////////////////
            /////////////////////////////////////////////////////

            if( r.compareTo(BigInteger.valueOf(0)) == 0 )
            {
                Flag = false;
                //e = SHA256G.SHA256STR(e + "1");
                HA4 = SHA256G.SHA256STR(HA4 + "1");
            }
            else
            {
                /////////////////////////////////////////////////////
                //6 - Calculando s = ( (K^-1) * (z + r * PVTKEY) ) mod n
                /////////////////////////////////////////////////////
                // (K^-1) mod n
                BigInteger kinv = myEcc.inverse(K, myEcc.n_order);
                //BigInteger kinv = K.modInverse(myEcc.n_order);

                //BigInteger rdA = point[0].multiply(keyGEN.HashToBigInt(PVTKEY));
                // (r * PVTKEY) mod n
                BigInteger rdA = r.multiply(myEcc.modp(keyGEN.HashToBigInt(PVTKEY), myEcc.n_order));
                rdA = myEcc.modp(rdA, myEcc.n_order);

                // (z + (r * PVTKEY)) mod n
                s = z.add(rdA);
                s = myEcc.modp(s, myEcc.n_order);

                //( (K^-1) * (z + r * PVTKEY) ) mod n
                s = kinv.multiply(s);
                s = myEcc.modp(s, myEcc.n_order);
                /////////////////////////////////////////////////////
                /////////////////////////////////////////////////////
                if( s.compareTo(BigInteger.valueOf(0)) == 0 )
                {
                    Flag = false;
                    //e = SHA256G.SHA256STR(e + "1");
                    HA4 = SHA256G.SHA256STR(HA4 + "1");
                }
            }
            //if(Variables.BarSize < 20) Variables.BarSize++;
        }
        Variables.BarSize = 10;
        //signECDSA[0] = z;
        //signECDSA[0] = Knum(HA4, PVTKEY, e);

        signECDSA[0] = r;
        signECDSA[1] = s;
        signECDSA[2] = K;

        return signECDSA;
    }

    /////////////////////////////////////////////////
    //JESUS is the LORD!!!
    /////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////
    //Verificação do Estado a Assinatura Digital
    //Se o resultado for 1, então a assinatura é válida
    //https://en.wikipedia.org/wiki/Elliptic_Curve_Digital_Signature_Algorithm
    //Para testes
    ///////////////////////////////////////////////////////////
    //MSN - mensagem recuperada do processo de Encriptação (De string para array de byte);
    //e = SHA256(SHA2560(MSN))
    //pubKey - chave publica enviada pela emissor no inicio do processo de troca de mensagem;
    //sign -  Assinatura digital, enviada pelo emissor junto com a mesagem encriptada;
    ///////////////////////////////////////////////////////////
    public int ECDSAVerifyBSV2 (String e, BigInteger [] pubKey, BigInteger [] sign)
    //public int ECDSAVerifyBSV2 (byte[] MSN, BigInteger [] pubKey, BigInteger [] sign)
    {
        BigInteger [] signECDSA = new BigInteger[2];
        signECDSA[0] = BigInteger.valueOf(0);
        signECDSA[1] = BigInteger.valueOf(0);
        /////////////////////////////////////////////////////////////////////////////////////////////
        //Verificação da validade do Ponto da Curva Eliptica da Chave Publica do Contato
        //O App já faz esta verificação previamente, mas a verificação é ralizada novamente aqui
        //para deixar o algoritimo mais compreensivel.
        //Se houver necessidade de otimização, esta primeira etapa pode ser suprimida
        /////////////////////////////////////////////////////////////////////////////////////////////
        //1 Check that PUBKEY is not equal to the identity element (0,0), and its coordinates are otherwise valid
        if (pubKey[0].compareTo(BigInteger.valueOf(0))==0 && pubKey[1].compareTo(BigInteger.valueOf(0))==0)
            return -1;
        //2 Check that PUBKEY lies on the curva - Verificar se ponto faz parte da curva
        //3 Check that n * PUBKEY == (0,0)
        //signECDSA = myEcc.eccnP(myEcc.n_order, pubKey[0],pubKey[1]);

        ////////////////////////////////////////////////////////////////////
        //Este procedimento inviabiliza a pré-fatoração da curva eliptica
        //Inviabiliza também o retorno (0, 0) ao encontar n == n-order
        ////////////////////////////////////////////////////////////////////
        signECDSA = eccNPoint(myEcc.n_order, pubKey[0],pubKey[1]);
        ////////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////

        if (!(signECDSA[0].compareTo(BigInteger.valueOf(0))==0 && signECDSA[1].compareTo(BigInteger.valueOf(0))==0))
            return -2;

        ////////////////////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////////////////////////////

        /////////////////////////////////////////////////////////////////////////////////////////////
        //Verificação da validade da Assinatura após a verificação de PUBKEY
        /////////////////////////////////////////////////////////////////////////////////////////////
        //Compara para ver se r ou s estão entre 1 e n-1
        if((sign[0].compareTo(BigInteger.valueOf(1)) <= 0) || (sign[0].compareTo(myEcc.n_order.subtract(BigInteger.valueOf(1))) >= 0 ))
            return -3;
        if((sign[1].compareTo(BigInteger.valueOf(1)) <= 0) || (sign[1].compareTo(myEcc.n_order.subtract(BigInteger.valueOf(1))) >= 0 ))
            return -4;
        //Passos 2 e 3 de https://en.wikipedia.org/wiki/Elliptic_Curve_Digital_Signature_Algorithm


        //A MSN seria necessário apenas para formar o elemento e, entao e melhor trazer diretamento e

        /*
        String e = SHA256G.SHA256bytes(MSN);
        //e = SHA256G.SHA256bytes(SHA256G.HashStrToByte2(e));
        e = SHA256G.SHA256bytes(SHA256G.HashStrToByte2(e));
        */

        //String e = SHA256G.SHA256bytes(MSN);

        //String e = SHA256G.SHA256bytes(MSN);

        if (e == null)
            return -5;
        BigInteger z;
        z = keyGEN.HashToBigInt(e);
        //Variables.Z1 = z.toString();
        //Fica apenas com os Bit mais significativos de e
        while (z.compareTo(Ln) > 0)
            z = z.divide(BigInteger.valueOf(2));
        //Variables.Z2 = z.toString();

        //Passo 4
        BigInteger sInv = myEcc.inverse(sign[1], myEcc.n_order);

        BigInteger u1 = z.multiply(sInv);
        //signECDSA[0] = u1;
        BigInteger u2 = sign[0].multiply(sInv);
        u1 = myEcc.modp(u1, myEcc.n_order);
        u2 = myEcc.modp(u2, myEcc.n_order);

        //Passo 5
        BigInteger [] P1 = new BigInteger[2];
        BigInteger [] P2 = new BigInteger[2];
        BigInteger [] C = new BigInteger[2];

        P1 = eccNPoint(u1, myEcc.Gx, myEcc.Gy);

        ////////////////////////////////////////////////////////////////////
        //Este procedimento inviabiliza a pré-fatoração da curva eliptica
        ////////////////////////////////////////////////////////////////////
        P2 = eccNPoint(u2, pubKey[0], pubKey[1]);
        ////////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////

        C = myEcc.addp(P1[0], P1[1], P2[0], P2[1]);
        C[0] = myEcc.modp(C[0], myEcc.n_order);

        if (C[0].compareTo(BigInteger.valueOf(0))==0 && C[1].compareTo(BigInteger.valueOf(0))==0) {
            return -6;
        }

        //Passo 6
        //Interfere no resultado; A mesmo que use o eccNPoint para o signECDSA
        //Parace que o resultado fica travado dento de myECC.eccp() mesmo myECC sendo private
        signECDSA[0] = BigInteger.valueOf(100);


        if (C[0].compareTo(sign[0])==0) {
            return 1;
        }

        else return 0;
        ////////////////////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////////////////////////////
    }

    /////////////////////////////////////////////////
    //JESUS is the LORD!!!
    /////////////////////////////////////////////////

    private BigInteger[] eccNPoint(BigInteger n, BigInteger x, BigInteger y)
    {
        Ecc newECC = new Ecc();
        return newECC.eccnP(n, x, y);
    }

}