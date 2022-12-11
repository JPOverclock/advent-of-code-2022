package aoc2022.util;

import java.math.BigInteger;
import java.util.List;

public class Math {

    /**
     * Use the Chinese Remainder Theorem to derive a number - X - that satisfies a
     * series of remainders - a - modulo m.
     * @param a List of results a = X mod m, for every m
     * @param m List of coprime factors
     * @return A number satisfying X mod m = a for every pair (a,m)
     */
    public static BigInteger ChineseRemainderTheorem(List<BigInteger> a, List<BigInteger> m) {
        BigInteger x = BigInteger.ZERO;
        BigInteger p = m.stream().reduce(BigInteger::multiply).orElseThrow();

        for(int i = 0; i < a.size(); i++){

            BigInteger M = p.divide(m.get(i));
            BigInteger y = BigInteger.ZERO; // M1 = p/m1, M2 = p/m2 ....., Mn = p/mn

            for(BigInteger j = BigInteger.ZERO; j.compareTo(m.get(i)) < 0; j = j.add(BigInteger.ONE)){
                var l = M.multiply(j).remainder(m.get(i));
                if(l.equals(BigInteger.ONE)){
                    y = j; break; // Finding the values for y1, y2,..., yn
                }
            }

            x = x.add(a.get(i).multiply(M).multiply(y)); // x = a1*M1*y1 + a2*M2*y2 + ... + an*Mn*yn
        }

        return x.mod(p);
    }
}
