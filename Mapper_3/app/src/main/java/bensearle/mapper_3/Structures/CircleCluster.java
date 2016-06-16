package bensearle.mapper_3.Structures;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import bensearle.mapper_3.UserVariables;

/**
 * Created by bensearle on 15/06/2016.
 */
public class CircleCluster {

    List<Circle3D> Circles = new ArrayList<Circle3D>();

    Circle3D[] circles;

    List<Point3D> IntersectionPoints = new ArrayList<Point3D>();


    public CircleCluster (){

    }

    public CircleCluster (Cursor c){

        int numberOfCircle = 1; // count of RPs *** implement

        circles = new Circle3D[numberOfCircle];

        for (Circle3D circle : circles){
            circle = new Circle3D(); // initialize the circle ** implement
        }
    }
    
    public Point3D Localise(){
        double percentOverlapping = getPercentOverlap();
        if (percentOverlapping > UserVariables.INCREASING_CIRCLES_PERCENT_OVERLAPPING){
            // get position
            Point3D estimatedLocation = getAverageIntersection();
            return estimatedLocation;
        } else {
            //
            IntersectionPoints.clear(); // remove intersection points
            increaseSize();
            return Localise();
        }
    }

    private double getPercentOverlap () {
        int numberOverlapping = 0;
        int numberOfCircles = circles.length;

        for (int a = 0 ; a < numberOfCircles; a++){ // circle a
            for (int b = 0 ; a < numberOfCircles; b++){ // circle b
                if (a<b){ // compare 2 circles if a<b to avoid duplicate comparisons
                    numberOverlapping += do2CirclesOverlap (circles[a],circles[b]);
                }
            }
        }

        double percent = (numberOverlapping/numberOfCircles)*100;
        return percent;
    }

    /**
     * find out whether the circles overlap, if yes, then add the intersection mid point to IntersectionPoints
     * @param circle1
     * @param circle2
     * @return  1:circels overlap, 0:do not overlap
     */
    private int do2CirclesOverlap (Circle3D circle1, Circle3D circle2) {
        int overlap = 1;

        // circle 1
        double x1 = circle1.Centre.X;
        double y1 = circle1.Centre.Y;
        double r1 = circle1.Radius;

        // circle 2
        double x2 = circle2.Centre.X;
        double y2 = circle2.Centre.Y;
        double r2 = circle1.Radius;

        // distance between circle centres
        double d = Math.sqrt ((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));

        if (d<(r1+r2)){
            // the circles overlap
            // distance between circle 1 centre and circle intersection line
            double d1 = (r1*r1 - r2*r2 + d*d) / d*d;

            // circle intersection mid point
            double x3 = x1 + (d1 * (x2 - x1)) / d;
            double y3 = y1 + (d1 * (y2 - y1)) / d;
            double z3 = (circle1.Centre.Z + circle2.Centre.Z) / 2; // z-coords should be the same
            // add point to the list of intersection points
            IntersectionPoints.add(new Point3D(x3,y3,z3));

            return 1;
        } else {
            // the circles do not overlap
            return 0;
        }
    }



    private void increaseSize(){
        for (Circle3D circle : circles){
            circle.Grow(); // increase the size of each circle
        }
    }

    private Point3D getAverageIntersection(){
        double totalX = 0;
        double totalY = 0;
        double totalZ = 0;

        int size = IntersectionPoints.size(); // size of list

        for (Iterator<Point3D> iter = IntersectionPoints.iterator(); iter.hasNext(); ) {
            Point3D item = iter.next();
            totalX += item.X;
            totalY += item.Y;
            totalZ += item.Z;
        }

        return new Point3D(totalX/size, totalY/size, totalZ/size);
    }
}
