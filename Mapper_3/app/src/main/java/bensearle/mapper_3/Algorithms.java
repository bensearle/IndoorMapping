package bensearle.mapper_3;

import bensearle.mapper_3.Structures.Circle3D;
import bensearle.mapper_3.Structures.Fingerprint;
import bensearle.mapper_3.Structures.Point3D;

/**
 * Created by bensearle on 19/05/2016.
 */
public class Algorithms {

    /**
     * the distance between an unknown point and a point of reference
     * @return the distance
     */
    public static double Euclidean_Distance(int[] values1, int[] values2){
        double d;

        double Sum = 0.0;

        for(int i=0; i<values1.length; i++) {
            Sum = Sum + Math.pow((values1[i]-values2[i]),2.0);
        }

        d = Math.sqrt(Sum);

        return d;
    }

    /**
     * the distance between an unknown point and a point of reference
     * @return the distance
     */
    public static double Euclidean_Distance(int[][] values){
        double d;

        double Sum = 0.0;

        for(int i=0; i<values.length/2; i++) {
            Sum = Sum + Math.pow((values[i][0]-values[i][1]),2.0);
        }

        d = Math.sqrt(Sum);

        return d;
    }

    public void CircleIntercetion(Circle3D circle1, Circle3D circle2){

    }

    public void TriangleCentre (Point3D p1, Point3D p2, Point3D p3){
        Point3D center12 = new Point3D((p1.X+p2.X)/2, (p1.Y+p2.Y)/2, (p1.Z+p2.Z)/2);
        Point3D center13 = new Point3D((p1.X+p3.X)/2, (p1.Y+p3.Y)/2, (p1.Z+p3.Z)/2);
        Point3D center23 = new Point3D((p2.X+p3.X)/2, (p2.Y+p3.Y)/2, (p2.Z+p3.Z)/2);


    }

    public double log2(double x)
    {
        return (int) (Math.log(x) / Math.log(2));
    }

}
