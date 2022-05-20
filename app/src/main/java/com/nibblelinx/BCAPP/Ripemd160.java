package com.nibblelinx.BCAPP;

public class Ripemd160
{
    public class ripemd160_context {
        long total[] = new long[2];
        long state[] = new long[5];
        char buffer[] = new char[64];
    }

    ripemd160_context ctx = new ripemd160_context();

    public long uint32_t (long valor) {
        return valor & 0xFFFFFFFFl;
    }

    public class Define {
        public long S (long x, long n) {
            return ((x << n) | (x >> (32l - n)));
        }

        public long[] P (long M[], int a, int b, int c, int d, int e,
                         long X, long s, int F, long K) {
            long f = 0l;

            if (F == 1) f =  M[b] ^ M[c] ^ M[d];
            else if (F == 2) f = (M[b] & M[c]) | (~M[b] & M[d]);
            else if (F == 3) f = (M[b] | ~M[c]) ^ M[d];
            else if (F == 4) f = (M[b] & M[d]) | (M[c] & ~M[d]);
            else if (F == 5) f = M[b] ^ (M[c] | ~M[d]);

            f = uint32_t(f);

            M[a] = uint32_t(M[a] + f + X + K);
            M[a] = uint32_t(uint32_t(S(M[a], s)) + M[e]);
            M[c] = uint32_t(S(M[c], 10l));

            return M;
        }
    }

    public long GET_UINT32_LE (char b[], int i) {
        long n =  b[i]
                | b[i + 1] << 8
                | b[i + 2] << 16
                | b[i + 3] << 24;
        return n;
    }

    public char[] PUT_UINT32_LE (long n, char b[], int i) {
        b[i    ] = (char) (n & 0xFF);
        b[i + 1] = (char) (n >>  8 & 0xFF);
        b[i + 2] = (char) (n >> 16 & 0xFF);
        b[i + 3] = (char) (n >> 24 & 0xFF);

        return b;
    }

    public void ripemd160_starts () {
        ctx.total[0] = 0l;
        ctx.total[1] = 0l;

        ctx.state[0] = uint32_t(0x67452301);
        ctx.state[1] = uint32_t(0xEFCDAB89);
        ctx.state[2] = uint32_t(0x98BADCFE);
        ctx.state[3] = uint32_t(0x10325476);
        ctx.state[4] = uint32_t(0xC3D2E1F0);
    }

    public void ripemd160_process (char data[]) {
        final int A = 0, B = 1, C = 2, D = 3, E = 4;
        long M[] = new long[5];
        long Mp[] = new long[5];
        long X[] = new long[16];
        int F, Fp; long K, Kp;

        X[ 0] = uint32_t(GET_UINT32_LE(data,  0));
        X[ 1] = uint32_t(GET_UINT32_LE(data,  4));
        X[ 2] = uint32_t(GET_UINT32_LE(data,  8));
        X[ 3] = uint32_t(GET_UINT32_LE(data, 12));
        X[ 4] = uint32_t(GET_UINT32_LE(data, 16));
        X[ 5] = uint32_t(GET_UINT32_LE(data, 20));
        X[ 6] = uint32_t(GET_UINT32_LE(data, 24));
        X[ 7] = uint32_t(GET_UINT32_LE(data, 28));
        X[ 8] = uint32_t(GET_UINT32_LE(data, 32));
        X[ 9] = uint32_t(GET_UINT32_LE(data, 36));
        X[10] = uint32_t(GET_UINT32_LE(data, 40));
        X[11] = uint32_t(GET_UINT32_LE(data, 44));
        X[12] = uint32_t(GET_UINT32_LE(data, 48));
        X[13] = uint32_t(GET_UINT32_LE(data, 52));
        X[14] = uint32_t(GET_UINT32_LE(data, 56));
        X[15] = uint32_t(GET_UINT32_LE(data, 60));

        M[A] = Mp[A] = ctx.state[0];
        M[B] = Mp[B] = ctx.state[1];
        M[C] = Mp[C] = ctx.state[2];
        M[D] = Mp[D] = ctx.state[3];
        M[E] = Mp[E] = ctx.state[4];

        Define def = new Define();

        K = 0x00000000; Kp = 0x50A28BE6; F = 1; Fp = 5;
        M = def.P(M, A, B, C, D, E,  X[0], 11, F, K); Mp = def.P(Mp, A, B, C, D, E,  X[5],  8, Fp, Kp); // OK
        M = def.P(M, E, A, B, C, D,  X[1], 14, F, K); Mp = def.P(Mp, E, A, B, C, D, X[14],  9, Fp, Kp); // OK
        M = def.P(M, D, E, A, B, C,  X[2], 15, F, K); Mp = def.P(Mp, D, E, A, B, C,  X[7],  9, Fp, Kp); // OK
        M = def.P(M, C, D, E, A, B,  X[3], 12, F, K); Mp = def.P(Mp, C, D, E, A, B,  X[0], 11, Fp, Kp); // OK
        M = def.P(M, B, C, D, E, A,  X[4],  5, F, K); Mp = def.P(Mp, B, C, D, E, A,  X[9], 13, Fp, Kp); // OK
        M = def.P(M, A, B, C, D, E,  X[5],  8, F, K); Mp = def.P(Mp, A, B, C, D, E,  X[2], 15, Fp, Kp); // OK
        M = def.P(M, E, A, B, C, D,  X[6],  7, F, K); Mp = def.P(Mp, E, A, B, C, D, X[11], 15, Fp, Kp); // OK
        M = def.P(M, D, E, A, B, C,  X[7],  9, F, K); Mp = def.P(Mp, D, E, A, B, C,  X[4],  5, Fp, Kp); // OK
        M = def.P(M, C, D, E, A, B,  X[8], 11, F, K); Mp = def.P(Mp, C, D, E, A, B, X[13],  7, Fp, Kp); // OK
        M = def.P(M, B, C, D, E, A,  X[9], 13, F, K); Mp = def.P(Mp, B, C, D, E, A,  X[6],  7, Fp, Kp); // OK
        M = def.P(M, A, B, C, D, E, X[10], 14, F, K); Mp = def.P(Mp, A, B, C, D, E, X[15],  8, Fp, Kp); // OK
        M = def.P(M, E, A, B, C, D, X[11], 15, F, K); Mp = def.P(Mp, E, A, B, C, D,  X[8], 11, Fp, Kp); // OK
        M = def.P(M, D, E, A, B, C, X[12],  6, F, K); Mp = def.P(Mp, D, E, A, B, C,  X[1], 14, Fp, Kp); // OK
        M = def.P(M, C, D, E, A, B, X[13],  7, F, K); Mp = def.P(Mp, C, D, E, A, B, X[10], 14, Fp, Kp); // OK
        M = def.P(M, B, C, D, E, A, X[14],  9, F, K); Mp = def.P(Mp, B, C, D, E, A,  X[3], 12, Fp, Kp); // OK
        M = def.P(M, A, B, C, D, E, X[15],  8, F, K); Mp = def.P(Mp, A, B, C, D, E, X[12],  6, Fp, Kp); // OK

        K = 0x5A827999; Kp = 0x5C4DD124; F = 2; Fp = 4;
        M = def.P(M, E, A, B, C, D,  X[7],  7, F, K); Mp = def.P(Mp, E, A, B, C, D,  X[6],  9, Fp, Kp); // Ok
        M = def.P(M, D, E, A, B, C,  X[4],  6, F, K); Mp = def.P(Mp, D, E, A, B, C, X[11], 13, Fp, Kp); // Ok
        M = def.P(M, C, D, E, A, B, X[13],  8, F, K); Mp = def.P(Mp, C, D, E, A, B,  X[3], 15, Fp, Kp); // Ok
        M = def.P(M, B, C, D, E, A,  X[1], 13, F, K); Mp = def.P(Mp, B, C, D, E, A,  X[7],  7, Fp, Kp); // Ok
        M = def.P(M, A, B, C, D, E, X[10], 11, F, K); Mp = def.P(Mp, A, B, C, D, E,  X[0], 12, Fp, Kp); // Ok
        M = def.P(M, E, A, B, C, D,  X[6],  9, F, K); Mp = def.P(Mp, E, A, B, C, D, X[13],  8, Fp, Kp); // Ok
        M = def.P(M, D, E, A, B, C,  X[15], 7, F, K); Mp = def.P(Mp, D, E, A, B, C,  X[5],  9, Fp, Kp); // Ok
        M = def.P(M, C, D, E, A, B,  X[3], 15, F, K); Mp = def.P(Mp, C, D, E, A, B, X[10], 11, Fp, Kp); // Ok
        M = def.P(M, B, C, D, E, A, X[12],  7, F, K); Mp = def.P(Mp, B, C, D, E, A, X[14],  7, Fp, Kp); // Ok
        M = def.P(M, A, B, C, D, E,  X[0], 12, F, K); Mp = def.P(Mp, A, B, C, D, E, X[15],  7, Fp, Kp); // Ok
        M = def.P(M, E, A, B, C, D,  X[9], 15, F, K); Mp = def.P(Mp, E, A, B, C, D,  X[8], 12, Fp, Kp); // Ok
        M = def.P(M, D, E, A, B, C,  X[5],  9, F, K); Mp = def.P(Mp, D, E, A, B, C, X[12],  7, Fp, Kp); // Ok
        M = def.P(M, C, D, E, A, B,  X[2], 11, F, K); Mp = def.P(Mp, C, D, E, A, B,  X[4],  6, Fp, Kp); // Ok
        M = def.P(M, B, C, D, E, A, X[14],  7, F, K); Mp = def.P(Mp, B, C, D, E, A,  X[9], 15, Fp, Kp); // Ok
        M = def.P(M, A, B, C, D, E, X[11], 13, F, K); Mp = def.P(Mp, A, B, C, D, E,  X[1], 13, Fp, Kp); // Ok
        M = def.P(M, E, A, B, C, D,  X[8], 12, F, K); Mp = def.P(Mp, E, A, B, C, D,  X[2], 11, Fp, Kp); // Ok

        K = 0x6ED9EBA1; Kp = 0x6D703EF3; F = 3; Fp = 3;
        M = def.P(M, D, E, A, B, C,  X[3], 11, F, K); Mp = def.P(Mp, D, E, A, B, C, X[15],  9, Fp, Kp); // Ok
        M = def.P(M, C, D, E, A, B, X[10], 13, F, K); Mp = def.P(Mp, C, D, E, A, B,  X[5],  7, Fp, Kp); // Ok
        M = def.P(M, B, C, D, E, A, X[14],  6, F, K); Mp = def.P(Mp, B, C, D, E, A,  X[1], 15, Fp, Kp); // Ok
        M = def.P(M, A, B, C, D, E,  X[4],  7, F, K); Mp = def.P(Mp, A, B, C, D, E,  X[3], 11, Fp, Kp); // Ok
        M = def.P(M, E, A, B, C, D,  X[9], 14, F, K); Mp = def.P(Mp, E, A, B, C, D,  X[7],  8, Fp, Kp); // Ok
        M = def.P(M, D, E, A, B, C, X[15],  9, F, K); Mp = def.P(Mp, D, E, A, B, C, X[14],  6, Fp, Kp); // Ok
        M = def.P(M, C, D, E, A, B,  X[8], 13, F, K); Mp = def.P(Mp, C, D, E, A, B,  X[6],  6, Fp, Kp); // Ok
        M = def.P(M, B, C, D, E, A,  X[1], 15, F, K); Mp = def.P(Mp, B, C, D, E, A,  X[9], 14, Fp, Kp); // Ok
        M = def.P(M, A, B, C, D, E,  X[2], 14, F, K); Mp = def.P(Mp, A, B, C, D, E, X[11], 12, Fp, Kp); // Ok
        M = def.P(M, E, A, B, C, D,  X[7],  8, F, K); Mp = def.P(Mp, E, A, B, C, D,  X[8], 13, Fp, Kp); // Ok
        M = def.P(M, D, E, A, B, C,  X[0], 13, F, K); Mp = def.P(Mp, D, E, A, B, C, X[12],  5, Fp, Kp); // Ok
        M = def.P(M, C, D, E, A, B,  X[6],  6, F, K); Mp = def.P(Mp, C, D, E, A, B,  X[2], 14, Fp, Kp); // Ok
        M = def.P(M, B, C, D, E, A, X[13],  5, F, K); Mp = def.P(Mp, B, C, D, E, A, X[10], 13, Fp, Kp); // Ok
        M = def.P(M, A, B, C, D, E, X[11], 12, F, K); Mp = def.P(Mp, A, B, C, D, E,  X[0], 13, Fp, Kp); // Ok
        M = def.P(M, E, A, B, C, D,  X[5],  7, F, K); Mp = def.P(Mp, E, A, B, C, D,  X[4],  7, Fp, Kp); // Ok
        M = def.P(M, D, E, A, B, C, X[12],  5, F, K); Mp = def.P(Mp, D, E, A, B, C, X[13],  5, Fp, Kp); // Ok

        K = 0x8F1BBCDC; Kp = 0x7A6D76E9; F = 4; Fp = 2;
        M = def.P(M, C, D, E, A, B,  X[1], 11, F, K); Mp = def.P(Mp, C, D, E, A, B,  X[8], 15, Fp, Kp); // Ok
        M = def.P(M, B, C, D, E, A,  X[9], 12, F, K); Mp = def.P(Mp, B, C, D, E, A,  X[6],  5, Fp, Kp); // Ok
        M = def.P(M, A, B, C, D, E, X[11], 14, F, K); Mp = def.P(Mp, A, B, C, D, E,  X[4],  8, Fp, Kp); // Ok
        M = def.P(M, E, A, B, C, D, X[10], 15, F, K); Mp = def.P(Mp, E, A, B, C, D,  X[1], 11, Fp, Kp); // Ok
        M = def.P(M, D, E, A, B, C,  X[0], 14, F, K); Mp = def.P(Mp, D, E, A, B, C,  X[3], 14, Fp, Kp); // Ok
        M = def.P(M, C, D, E, A, B,  X[8], 15, F, K); Mp = def.P(Mp, C, D, E, A, B, X[11], 14, Fp, Kp); // Ok
        M = def.P(M, B, C, D, E, A, X[12],  9, F, K); Mp = def.P(Mp, B, C, D, E, A, X[15],  6, Fp, Kp); // Ok
        M = def.P(M, A, B, C, D, E,  X[4],  8, F, K); Mp = def.P(Mp, A, B, C, D, E,  X[0], 14, Fp, Kp); // Ok
        M = def.P(M, E, A, B, C, D, X[13],  9, F, K); Mp = def.P(Mp, E, A, B, C, D,  X[5],  6, Fp, Kp); // Ok
        M = def.P(M, D, E, A, B, C,  X[3], 14, F, K); Mp = def.P(Mp, D, E, A, B, C, X[12],  9, Fp, Kp); // Ok
        M = def.P(M, C, D, E, A, B,  X[7],  5, F, K); Mp = def.P(Mp, C, D, E, A, B,  X[2], 12, Fp, Kp); // Ok
        M = def.P(M, B, C, D, E, A, X[15],  6, F, K); Mp = def.P(Mp, B, C, D, E, A, X[13],  9, Fp, Kp); // Ok
        M = def.P(M, A, B, C, D, E, X[14],  8, F, K); Mp = def.P(Mp, A, B, C, D, E,  X[9], 12, Fp, Kp); // Ok
        M = def.P(M, E, A, B, C, D,  X[5],  6, F, K); Mp = def.P(Mp, E, A, B, C, D,  X[7],  5, Fp, Kp); // Ok
        M = def.P(M, D, E, A, B, C,  X[6],  5, F, K); Mp = def.P(Mp, D, E, A, B, C, X[10], 15, Fp, Kp); // Ok
        M = def.P(M, C, D, E, A, B,  X[2], 12, F, K); Mp = def.P(Mp, C, D, E, A, B, X[14],  8, Fp, Kp); // Ok

        K = 0xA953FD4E; Kp = 0x00000000; F = 5; Fp = 1;
        M = def.P(M, B, C, D, E, A,  X[4],  9, F, K); Mp = def.P(Mp, B, C, D, E, A, X[12],  8, Fp, Kp); // Ok
        M = def.P(M, A, B, C, D, E,  X[0], 15, F, K); Mp = def.P(Mp, A, B, C, D, E, X[15],  5, Fp, Kp); // Ok
        M = def.P(M, E, A, B, C, D,  X[5],  5, F, K); Mp = def.P(Mp, E, A, B, C, D, X[10], 12, Fp, Kp); // Ok
        M = def.P(M, D, E, A, B, C,  X[9], 11, F, K); Mp = def.P(Mp, D, E, A, B, C,  X[4],  9, Fp, Kp); // Ok
        M = def.P(M, C, D, E, A, B,  X[7],  6, F, K); Mp = def.P(Mp, C, D, E, A, B,  X[1], 12, Fp, Kp); // Ok
        M = def.P(M, B, C, D, E, A, X[12],  8, F, K); Mp = def.P(Mp, B, C, D, E, A,  X[5],  5, Fp, Kp); // Ok
        M = def.P(M, A, B, C, D, E,  X[2], 13, F, K); Mp = def.P(Mp, A, B, C, D, E,  X[8], 14, Fp, Kp); // Ok
        M = def.P(M, E, A, B, C, D, X[10], 12, F, K); Mp = def.P(Mp, E, A, B, C, D,  X[7],  6, Fp, Kp); // Ok
        M = def.P(M, D, E, A, B, C, X[14],  5, F, K); Mp = def.P(Mp, D, E, A, B, C,  X[6],  8, Fp, Kp); // Ok
        M = def.P(M, C, D, E, A, B,  X[1], 12, F, K); Mp = def.P(Mp, C, D, E, A, B,  X[2], 13, Fp, Kp); // Ok
        M = def.P(M, B, C, D, E, A,  X[3], 13, F, K); Mp = def.P(Mp, B, C, D, E, A, X[13],  6, Fp, Kp); // Ok
        M = def.P(M, A, B, C, D, E,  X[8], 14, F, K); Mp = def.P(Mp, A, B, C, D, E, X[14],  5, Fp, Kp); // Ok
        M = def.P(M, E, A, B, C, D, X[11], 11, F, K); Mp = def.P(Mp, E, A, B, C, D,  X[0], 15, Fp, Kp); // Ok
        M = def.P(M, D, E, A, B, C,  X[6],  8, F, K); Mp = def.P(Mp, D, E, A, B, C,  X[3], 13, Fp, Kp); // Ok
        M = def.P(M, C, D, E, A, B, X[15],  5, F, K); Mp = def.P(Mp, C, D, E, A, B,  X[9], 11, Fp, Kp); // Ok
        M = def.P(M, B, C, D, E, A, X[13],  6, F, K); Mp = def.P(Mp, B, C, D, E, A, X[11], 11, Fp, Kp); // Ok

        M[C]         = uint32_t(ctx.state[1] + M[C] + Mp[D]);
        ctx.state[1] = uint32_t(ctx.state[2] + M[D] + Mp[E]);
        ctx.state[2] = uint32_t(ctx.state[3] + M[E] + Mp[A]);
        ctx.state[3] = uint32_t(ctx.state[4] + M[A] + Mp[B]);
        ctx.state[4] = uint32_t(ctx.state[0] + M[B] + Mp[C]);
        ctx.state[0] = uint32_t(M[C]);
    }

    public char[] cpy (char a[], char b[], int start, int end) {
        for (int idx = start; idx < end; idx++)
            a[idx] = b[idx - start];
        return a;
    }

    public char[] pop (char str[], int ilen, int n) {
        char ans[] = new char[ilen - n];
        for (int idx = n; idx < ilen; idx++)
            ans[idx - n] = str[idx];
        return ans;
    }

    public void ripemd160_update (char input[], int ilen) {
        long left = ctx.total[0] & 0x3F;
        long fill = 64 - left;

        ctx.total[0] += ilen;
        ctx.total[0] &= 0xFFFFFFFFl;

        if (ctx.total[0] < ilen)
            ctx.total[1]++;

        if (left != 0 && ilen >= fill) {
            ctx.buffer = cpy(ctx.buffer, input, (int) left, (int) (left + fill));
            ripemd160_process(ctx.buffer);

            input = pop(input, ilen, (int) fill);
            ilen -= fill;
            left = 0;
        }

        while (ilen >= 64) {
            System.out.println(input);
            ripemd160_process(input);
            input = pop(input, ilen, 64);
            ilen -= 64;
        }

        if (ilen > 0) ctx.buffer = cpy(ctx.buffer, input, (int) left, (int) (left + ilen));
    }

    public char[] ripemd160_finish () {

        char ripemd160_padding[] =
                {
                        0x80, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
                };

        long last, padn;
        long high, low;
        char msglen[] = new char[8];
        char output[] = new char[20];

        high = (ctx.total[0] >> 29)
                | (ctx.total[1] <<  3);
        low = (ctx.total[0] <<  3);

        msglen = PUT_UINT32_LE(low, msglen, 0);
        msglen = PUT_UINT32_LE(high, msglen, 4);

        last = ctx.total[0] & 0x3F;
        padn = (last < 56) ?(56 - last) :(120 - last);


        ripemd160_update(ripemd160_padding,(int) padn);
        ripemd160_update(msglen, 8);

        output = PUT_UINT32_LE(ctx.state[0], output,  0);
        output = PUT_UINT32_LE(ctx.state[1], output,  4);
        output = PUT_UINT32_LE(ctx.state[2], output,  8);
        output = PUT_UINT32_LE(ctx.state[3], output, 12);
        output = PUT_UINT32_LE(ctx.state[4], output, 16);

        return output;
    }

    public char[] ripemd160 (char input[], int ilen) {
        ripemd160_starts();
        ripemd160_update(input, ilen);
        return ripemd160_finish();
    }

    //Metodo usado para transforma um HASH SHA256 de bytes para String
    public static String HashCharToStr(char[] hashKey)
    {
        //String result;
        //Cada posição do vetor tem 2 nibbles
        char[] result = new char[2*hashKey.length];

        //strChar = hashKey.toCharArray();
        for (int i = 0; i < 2*hashKey.length; i++)
        {
            if ((i % 2) == 0) result[i] = (char) ((hashKey[i/2] / 0x10) & 0x0F);
            else result[i] = (char) (hashKey[i/2] & 0x0F);

            switch (result[i])
            {
                case 0:
                    result[i] = '0';
                    break;
                case 1:
                    result[i] = '1';
                    break;
                case 2:
                    result[i] = '2';
                    break;
                case 3:
                    result[i] = '3';
                    break;
                case 4:
                    result[i] = '4';
                    break;
                case 5:
                    result[i] = '5';
                    break;
                case 6:
                    result[i] = '6';
                    break;
                case 7:
                    result[i] = '7';
                    break;
                case 8:
                    result[i] = '8';
                    break;
                case 9:
                    result[i] = '9';
                    break;
                case 10:
                    result[i] = 'a';
                    break;
                case 11:
                    result[i] = 'b';
                    break;
                case 12:
                    result[i] = 'c';
                    break;
                case 13:
                    result[i] = 'd';
                    break;
                case 14:
                    result[i] = 'e';
                    break;
                case 15:
                    result[i] = 'f';
                    break;
            }
        }

        return String.valueOf(result);
    }


}

