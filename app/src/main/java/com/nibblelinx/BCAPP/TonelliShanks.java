package com.nibblelinx.BCAPP;

import java.math.BigInteger;

public class TonelliShanks {

    Ecc eccobj = new Ecc();
    public TonelliShanks() {}

    public  BigInteger pow_mod(BigInteger base, BigInteger expoent, BigInteger modulus)
    //cpp_int pow_mod(cpp_int base, cpp_int exponent, cpp_int modulus)
    {
        BigInteger result = BigInteger.valueOf(1);
        //cpp_int result = 1;
        base = eccobj.modp(base, modulus);
        //base = base % modulus;
        while (expoent.compareTo(BigInteger.valueOf(0)) > 0)
        //while (exponent > 0)
        {
            if((eccobj.modp(expoent, BigInteger.valueOf(2))).compareTo(BigInteger.valueOf(1)) == 0)
                //if (exponent % 2 == 1)
                result = eccobj.modp(result.multiply(base), modulus);
            //result = (result * base)% modulus;
            expoent = expoent.divide(BigInteger.valueOf(2));
            //exponent = exponent >> 1;
            base = eccobj.modp(base.multiply(base), modulus);
            //base = (base * base) % modulus;
        }
        return result;
    }

    public  BigInteger sqrtCF (BigInteger n, BigInteger p){
        BigInteger s = BigInteger.valueOf(0);
        BigInteger q = p.subtract(BigInteger.valueOf(1));
        while ((q.and(BigInteger.valueOf(1))).intValue() == 0){
            q = q.divide(BigInteger.valueOf(2));
            s = s.add(BigInteger.valueOf(1));
        }
        if (s.compareTo(BigInteger.valueOf(1)) == 0){
            BigInteger r = pow_mod(n, (p.add(BigInteger.valueOf(1))).divide(BigInteger.valueOf(4)),p);
            //if ((r * r) % p == n) return r;
            if ((eccobj.modp(r.multiply(r), p)).compareTo(n) == 0) return r;
            return BigInteger.valueOf(0);
        }

        // Find the first quadratic non-residue z by brute-force search
        BigInteger z = BigInteger.valueOf(1);

        while (true)
            if ((pow_mod(z.add(BigInteger.valueOf(1)), (p.subtract(BigInteger.valueOf(1))).divide(BigInteger.valueOf(2)), p)).compareTo(p.subtract(BigInteger.valueOf(1))) == 0)
                break;

        //while (pow_mod(++z, (p-1)/2, p) != p - 1); // ORIGINAL
        //while (1) if (pow_mod(++z, (p-1)/2, p) == p - 1) break;  //ALTERNATIVA



        BigInteger c = pow_mod(z, q, p);
        BigInteger r = pow_mod(n, (q.add(BigInteger.valueOf(1))).divide(BigInteger.valueOf(2)), p);
        BigInteger t = pow_mod(n, q, p);
        BigInteger m = s;

        while(t.compareTo(BigInteger.valueOf(1)) != 0)
        //while (t != 1)
        {
            BigInteger tt = t;
            BigInteger i = BigInteger.valueOf(0);
            while (tt.compareTo(BigInteger.valueOf(1)) != 0)
            {
                tt = eccobj.modp( tt.multiply(tt), p);
                i = i.add(BigInteger.valueOf(1));
                //++i;
                //if (i == m) return 0;
                if (i.compareTo(m) == 0) return BigInteger.valueOf(0);
            }
            //cpp_int b = pow_mod(c, pow_mod(2, m-i-1, p-1), p);
            BigInteger b = pow_mod(c, pow_mod(BigInteger.valueOf(2), m.subtract(i.subtract(BigInteger.valueOf(1))), p.subtract(BigInteger.valueOf(1))), p);
            //cpp_int b2 = (b * b) % p;
            BigInteger b2 = eccobj.modp(b.multiply(b), p);
            r = eccobj.modp(r.multiply(b), p);
            //t = (t * b2) % p;
            t = eccobj.modp (t.multiply(b2), p);
            c = b2;
            m = i;
        }
        //if ((r * r) % p == n) return r;
        if ((eccobj.modp(r.multiply(r), p)).compareTo(n) == 0) return r;
        return BigInteger.valueOf(0);
    }
}