package bensearle.mapper_3.Structures;

/**
 * Created by bensearle on 19/05/2016.
 */
public class Point3D {
    public Double X;
    public Double Y;
    public Double Z;

    public Point3D (){
        // initialize empty Point3D
    }

    public Point3D (double x, double y, double z){
        X=x;
        Y=y;
        Z=z;
    }

    public boolean isComplete(){
        if (X.isNaN() || Y.isNaN() || Z.isNaN()){
            return false;
        }
        return true;
    }

    public boolean isEmpty(){
        if (X.isNaN() && Y.isNaN() && Z.isNaN()){
            return true;
        }
        return false;
    }
}
