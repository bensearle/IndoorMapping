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


    public void Localise(){

        double percentOverlapping = getPercentOverlap();
        if (percentOverlapping > UserVariables.INCREASING_CIRCLES_PERCENT_OVERLAPPING){
            // get position
            Point3D estimatedLocation = getAverageIntersection();
        } else {
            //
            IntersectionPoints.clear(); // remove intersection points
            increaseSize();
            Localise();
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

    private int do2CirclesOverlap (Circle3D a, Circle3D b) {
        int overlap = 1;

        Point3D d = new Point3D(
                a.Centre.X + b.Centre.X / 2,
                a.Centre.Y + b.Centre.Y / 2,
                a.Centre.Z + b.Centre.Z / 2
        );
        // difference between circle centre points














        // 1:overlap, 0:do not overlap
        if (overlap==1){
            IntersectionPoints.add(new Point3D()); // add the average intersection points ** TO DO
            return 1;
        } else {
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
