package com.nibblelinx.BCAPP;

import java.math.BigInteger;

public class Ecc
{
    BigInteger p, Gx, Gy, n_order;
    BigInteger A, B;
    private BigInteger [] point = new BigInteger[2];
    private BigInteger [] pointNULL = new BigInteger[2];

    //public ecc (BigInteger p, BigInteger A, BigInteger B, BigInteger n_order, BigInteger Gx, BigInteger Gy)
    public Ecc ()
    {
      /*this.p = p;
        this.A = A;
        this.B = B;
        this.n_order = n_order;
        this.Gx = Gx;
        this.Gy = Gy; */

        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Parametros da Curva Eliptica: secp256k1
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        p = new BigInteger("115792089237316195423570985008687907853269984665640564039457584007908834671663");       // "FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEFFFFFC2F"
        //n_order is a prime number https://www.numberempire.com/primenumbers.php?number=115792089237316195423570985008687907852837564279074904382605163141518161494337&action=check&_p1=2435
        n_order = new BigInteger("115792089237316195423570985008687907852837564279074904382605163141518161494337"); // "FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEBAAEDCE6AF48A03BBFD25E8CD0364141"
        Gx = new BigInteger("55066263022277343669578718895168534326250603453777594175500187360389116729240");       // "79BE667EF9DCBBAC55A06295CE870B07029BFCDB2DCE28D959F2815B16F81798"
        Gy = new BigInteger("32670510020758816978083085130507043184471273380659243275938904335757337482424");       // "483ADA7726A3C4655DA4FBFC0E1108A8FD17B448A68554199C47D08FFB10D4B8"
        A = new BigInteger("0");
        B = new BigInteger("7");

        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Parametros da Curva Eliptica: Exercicio 1
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //p = new BigInteger("47");
        //n_order = new BigInteger("43");
        //Gx = new BigInteger("3");
        //Gy = new BigInteger("22");
        //A = new BigInteger("22");
        //B = new BigInteger("15");

        pointNULL[0] = BigInteger.valueOf(0);
        pointNULL[1] = BigInteger.valueOf(0);
    }

    public  BigInteger modp(BigInteger n, BigInteger p1)
    {
        n = n.remainder(p1);
        if(n.signum() < 0) return p1.add(n);
        return n;
        //return n.mod(p1);
    }
    public BigInteger inverse(BigInteger r, BigInteger p)
    //se nao declarar como static o programa compila e execulta, mas quebra na execucao
    {
        //BigInteger t = new BigInteger("1");
        BigInteger t = BigInteger.valueOf(1);
        BigInteger aux = p;
        //BigInteger newr = aux.remainder(r);
        BigInteger newr = r;

        BigInteger newt = BigInteger.valueOf(0).subtract(aux.divide(r));
        //newt = newt.subtract(aux.divide(r));

        //while (newr.signum() !=0 )
        while ((newr = aux.remainder(r = newr)).signum() !=0 )
        {
            aux = t;
            //t = newt;
            //newt = t.multiply(r.divide(newr));
            newt = aux.subtract((t = newt).multiply(r.divide(newr)));

            //newt = aux.subtract(newt);
            aux = r;
            //newr = aux.remainder(r = newr);
        }
        if (t.signum() < 0) return (t.add(p));
        return (t);
    }
    public BigInteger [] doublep (BigInteger x, BigInteger y)
    {
        BigInteger m;
        //m = modp (((3*(x*x) + A)*inverse ((2*y), p)), p);

        m = modp(((inverse(y.multiply(BigInteger.valueOf(2)), p)).multiply((x.multiply(x.multiply(BigInteger.valueOf(3)))).add(A))), p);

        //point[0] = modp ((((m*m)) - (2*x)), p);
        point[0] = modp(((m.multiply(m)).subtract(x.multiply(BigInteger.valueOf(2)))), p);

        //point[1] = modp ((-( (m * modp ((m*m), p)) - (m*(2*x)) + (y - (m*x)))), p);
        point[1] = modp(((BigInteger.valueOf(0)).subtract((((modp(m.multiply(m), p)).multiply(m)).subtract(m.multiply(x.multiply(BigInteger.valueOf(2))))).add(y.subtract(m.multiply(x))))), p);

        return point;
    }
    public BigInteger [] addp (BigInteger x1, BigInteger y1, BigInteger x2, BigInteger y2)
    {
        BigInteger m, d;

        //if (x1 == x2)
        if (x1.compareTo(x2) == 0)
        {
            // Verificar a condicao de que y1 = y2 = p/2
            // equivalente a y1 = y2 = 0 nao acontecerah
            //if (y1 == y2) return (doublep (x1, y1));
            if (y1.compareTo(y2) == 0) return (doublep (x1, y1));

            else return (pointNULL);
        }
        //m = modp (((y2 - y1)*inverse (modp ((x2 - x1), p), p)), p)
        m = modp ((y2.subtract(y1)).multiply(inverse (modp (x2.subtract(x1), p), p)), p);

        point[0] = modp((m.multiply(m)).subtract(x1.add(x2)), p);
        point[1] = modp((BigInteger.valueOf(0)).subtract(((m.multiply(modp((m.multiply(m)), p))).subtract(m.multiply(x1.add(x2)))).add(y1.subtract(m.multiply(x1)))), p);

        return point;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //A pré-fatoração da curva elíptica seria uma possibilidade para agilizar a execução da operação eccnP
    //Entretanto, tal procedimento somente é possível quando se trata de (n * (Gx, Gy))
    //Em caso de (n * (Px, Py)), onde (Px, Py) != (Gx, Gy) a quantidade de possibilidade deixa o procedimento inviável
    //
    //Procedimento que inviabilizam a pré-fatoração existem:
    //      Na geração de chave comum de comunicação entre dois Links;
    //      Na verificação da ECDSA;
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public BigInteger [] eccnP (BigInteger n, BigInteger x, BigInteger y)
    {
        //static cpp_int point[2], pointNULL[2] = {0, 0};
        //cpp_int point[2], pointNULL[2] = {0, 0};
        BigInteger x3, y3, n3, bittest;
        BigInteger [] pointR = new BigInteger[2];

        if (n.signum() < 0)
        {
            n = BigInteger.valueOf(0).subtract(n);
            // como y estah em (mod p) -(yp) mod p = p - yp
            y = p.subtract(y);
        }

        //////////////////////////////////////////////////////////////////////////////////
        //Este procedimento não deve ser usado se (Px, Py) != (Gx, Gy)
        //Senão a assinatura digital EDCSA não poderá ser realizada de forma adequada
        //////////////////////////////////////////////////////////////////////////////////
        /*

        //if (modp(n, n_order) == BigInteger.valueOf(0)) return (pointNULL);
        //Verificar necessidade???
        if ((modp(n, n_order)).compareTo(BigInteger.valueOf(0)) == 0)
            return (pointNULL);
        //////////////////////////////////////////////////////////////////////////////////
        //////////////////////////////////////////////////////////////////////////////////

        //Este procedimento é necessário para usar o procedimento abaixo
        if(n.compareTo(n_order) > 0)
            n = modp(n, n_order);

        // XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
        // OTIMIZACAO: Divide o Ciclo de Hash em duas partes;
        // se n for da segunda metade da curva, aplicamos esta modificacao para economizar processamento
        // XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

        // Remover esta otimizacao caso utilize n > n_order sem tirar o mod n_order  antes da chamada da funcao

        if ((n.subtract((n_order.subtract(BigInteger.valueOf(1))).divide(BigInteger.valueOf(2)))).signum() > 0)
        {
            n = n_order.subtract(n);
            y = p.subtract(y);
        }
        // XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

        */

        //////////////////////////////////////////////////////////////////////////////////
        //////////////////////////////////////////////////////////////////////////////////


        n3 = n.multiply(BigInteger.valueOf(3));
        bittest = BigInteger.valueOf(1);

        //while ((bittest) <= n3)
        while ((bittest.subtract(n3)).signum() <= 0)
        {
            bittest = bittest.multiply(BigInteger.valueOf(2));
        }

        // deslocamento de dois bits para a direita
        bittest = bittest.divide(BigInteger.valueOf(4));

        x3 = x;
        y3 = y;

        // Executa a operacao n*P
        while ((bittest.subtract(BigInteger.valueOf(1))).signum() > 0)
        {
            // Primeiramente dobramos o valor atual do ponto P = (x3, y3) atraves da operacao P + P = 2*P
            pointR = doublep (x3, y3);
            x3 = pointR[0];
            y3 = pointR[1];

            // Verificar se devemos acrescentar o valor de G = (x, y) ao valor atual de P = (x3, y3)
            //if ( ((n3 & bittest) != BigInteger.valueOf(0)) && ((n & bittest) == BigInteger.valueOf(0)) )
            //if ( ((n3.and(bittest)) != BigInteger.valueOf(0)) && ((n.and(bittest)) == BigInteger.valueOf(0)) )
            if ( ((n3.and(bittest)).compareTo(BigInteger.valueOf(0)) != 0 ) && ((n.and(bittest)).compareTo(BigInteger.valueOf(0)) == 0) )
            {
                pointR = addp (x3, y3, x, y);
                x3 = pointR[0];
                y3 = pointR[1];
            }

            // Verificar se demvemos subtrair o valor de G = (x, y) ao valor atual de P = (x3, y3)
            // Neste caso a subtracao de G = (x, y) eh equivalente a somar -G = (x, -y) = (x, p - y)
            //if ( ((n3 & bittest) == 0) && ((n & bittest) != 0) )
            //if ( ((n3.and(bittest)) == BigInteger.valueOf(0)) && ((n.and(bittest)) != BigInteger.valueOf(0)) )
            if ( ((n3.and(bittest)).compareTo(BigInteger.valueOf(0)) == 0) && ((n.and(bittest)).compareTo(BigInteger.valueOf(0)) != 0) )
            {
                pointR = addp (x3, y3, x, (p.subtract(y)));
                x3 = pointR[0];
                y3 = pointR[1];
            }

            bittest = bittest.divide(BigInteger.valueOf(2));
        }

        point[0] = x3;
        point[1] = y3;

        return point;
    }
}