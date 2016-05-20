package bensearle.mapper_3;

import bensearle.mapper_3.Structures.Fingerprint;

/**
 * Created by bensearle on 19/05/2016.
 */
public class Algorithms {

    /**
     * the distance between an unknown point and a point of reference
     * @return the distance
     */
    public double Euclidean_Distance(Fingerprint f1, Fingerprint f2){
        double d;

        double Sum = 0.0;

        for(int i=0;i<array1.length;i++) {
            Sum = Sum + Math.pow((array1[i]-array2[i]),2.0);
        }

        return Math.sqrt(Sum);

        return d;
    }



}
