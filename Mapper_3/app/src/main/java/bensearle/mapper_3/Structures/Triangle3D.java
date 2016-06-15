package bensearle.mapper_3.Structures;

import bensearle.mapper_3.MainActivity;
import bensearle.mapper_3.UserVariables;

/**
 * Created by bensearle on 20/05/2016.
 */
public class Triangle3D {
    // coordinates of vertices (RPs)
    public Point3D Point1;
    public Point3D Point2;
    public Point3D Point3;

    // Euclidean distance between RPs and current fingerprint
    public double Distance1;
    public double Distance2;
    public double Distance3;

    private Point3D centroid; // centre of the triangle

    public Triangle3D (){}

    public Triangle3D (Point3D p1, Point3D p2, Point3D p3){
        Point1 = p1;
        Point2 = p2;
        Point3 = p3;
    }

    public Triangle3D (Point3D p1, Point3D p2, Point3D p3, double d1, double d2, double d3){
        Point1 = p1;
        Point2 = p2;
        Point3 = p3;
        Distance1 = d1;
        Distance2 = d2;
        Distance3 = d3;
        centroid = GetCentroid();
    }

    /**
     * localization through use of decreasing triangle size
     * @return the centre point of the smallest circle
     */
    public Point3D Localise(){
        boolean canBeSmaller = true;
        while (canBeSmaller){
            canBeSmaller = DecreaseSize();
            this.GetCentroid(); // calculate new centre point
        }
        return this.GetCentroid();
    }

    public boolean DecreaseSize() {
        Point3D p1 = moveAlongLine(Point1, centroid, UserVariables.DECREASING_TRIANGLES_FACTOR*Distance1);
        Point3D p2 = moveAlongLine(Point2, centroid, UserVariables.DECREASING_TRIANGLES_FACTOR*Distance2);
        Point3D p3 = moveAlongLine(Point3, centroid, UserVariables.DECREASING_TRIANGLES_FACTOR*Distance3);

        if (Point1==p1 || Point2==p2 || Point3==p3) {
            // at least one line cannot move closer to the centre
            // triangle is as small as possible
            return false;
        } else {
            // triangle decreased in size
            Point1 = p1;
            Point2 = p2;
            Point3 = p3;

            return true;
        }
    }

    private Point3D moveAlongLine (Point3D point, Point3D centre, double distance){
        Point3D vector = new Point3D(centre.X-point.X, centre.Y-point.Y, centre.Z-point.Z);
        double length = Math.sqrt(vector.X*vector.X + vector.Y*vector.Y + vector.Z*vector.Z);

        if ( distance < length ) {
            // can move closer to the centroid
            Point3D vectorNormalized = new Point3D(vector.X / length, vector.Y / length, vector.Z / length);

            Point3D newPoint = new Point3D(centre.X + distance * vectorNormalized.X,
                    centre.Y + distance * vectorNormalized.Y, centre.Z + distance * vectorNormalized.Z);

            return newPoint;
        } else {
            // cannot move any closer, without going over
            return point;
        }
    }

    public Point3D GetCentroid (){
        //Point3D center12 = new Point3D((Point1.X+Point2.X)/2, (Point1.Y+Point2.Y)/2, (Point1.Z+Point2.Z)/2);
        //Point3D center13 = new Point3D((Point1.X+Point3.X)/2, (Point1.Y+Point3.Y)/2, (Point1.Z+Point3.Z)/2);
        //Point3D center23 = new Point3D((Point2.X+Point3.X)/2, (Point2.Y+Point3.Y)/2, (Point2.Z+Point3.Z)/2);

        double x = (Point1.X + Point2.X + Point3.X)/3;
        double y = (Point1.Y + Point2.Y + Point3.Y)/3;
        double z = (Point1.Z + Point2.Z + Point3.Z)/3;

        centroid.X = x;
        centroid.Y = y;
        centroid.Z = z;

        return new Point3D(x,y,z);
    }

    public void AddPoint(Point3D point){
        if (!Point1.isComplete()){
            Point1 = point;
        } else if (!Point2.isComplete()){
            Point2 = point;
        } else if (!Point3.isComplete()){
            Point3 = point;
        } else {
            // all points have been set
        }
    }
}
