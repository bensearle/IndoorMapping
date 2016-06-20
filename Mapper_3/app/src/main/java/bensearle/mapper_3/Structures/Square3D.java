package bensearle.mapper_3.Structures;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import bensearle.mapper_3.Algorithms;
import bensearle.mapper_3.UserVariables;

/**
 * Created by bensearle on 19/06/2016.
 */
public class Square3D {

    // corners of the square, top/bottom left/right
    private Point3D pTL;
    private Point3D pTR;
    private Point3D pBL;
    private Point3D pBR;

    // distance of each corner
    private double dTL;
    private double dTR;
    private double dBL;
    private double dBR;
    private double dTotal;

    private double zcoord = 0;
    private double minX;
    private double maxX;
    private double minY;
    private double maxY;

    public Square3D(){

    }


    public Square3D(List<Float> distances, Map<Float, Fingerprint> fingerprintAndDistance) {
        int corners = 0;
        String construction = ""; // are the first to on the same X, Y, or XY axis


        Point3D corner1 = new Point3D();
        Point3D corner2 = new Point3D();
        Point3D corner3 = new Point3D();
        Point3D corner4 = new Point3D();

        double d1 = 0;
        double d2 = 0;
        double d3 = 0;
        double d4 = 0;

        for (Float distance: distances){
            Fingerprint fp = fingerprintAndDistance.get(distance);

            if (corners==0){
                corner1 = fp.GetPosition(); // add the corner point
                zcoord = corner1.Z;
                d1 = distance; // add the distance of the first corner
                corners++;
            } else if (corners==1){
                corner2 = fp.GetPosition(); // add the corner point
                d2 = distance; // add the distance of the first corner

                // test variables
                boolean cX = corner1.X.equals(corner2.X);
                boolean cY = corner1.Y.equals(corner2.Y);
                boolean cXY = corner1.X.equals(corner2.Y);
                double dX = Math.abs(corner1.X - corner2.X);
                double dY = Math.abs(corner1.Y - corner2.Y);
                double uV = UserVariables.GRID_THICKNESS;
                boolean bX = dX<uV;
                boolean bY = dY<uV;

                if (corner2.Z == zcoord) {
                    if (corner1.X.equals(corner2.X) && (Math.abs(corner1.Y - corner2.Y)) <= UserVariables.GRID_THICKNESS) {
                        // same x, y are next to eachother
                        construction = "X";
                        corners++;
                    } else if (corner1.Y.equals(corner2.Y) && (Math.abs(corner1.X - corner2.X)) <= UserVariables.GRID_THICKNESS) {
                        // same y, x are next to eachother
                        construction = "X";
                        corners++;
                    } else if (Math.abs(corner1.X - corner2.X) <= UserVariables.GRID_THICKNESS && (Math.abs(corner1.Y - corner2.Y)) <= UserVariables.GRID_THICKNESS) {
                        // x and y are next to eachother
                        construction = "XY";
                        corners++;
                    }
                }
                // else ignore because this RP is further that (1,1) away from closest
            } else if (corners==2){
                corner3 = fp.GetPosition(); // add the corner point
                d3 = distance; // add the distance of the first corner

                if (corner3.Z.equals(zcoord)) {
                    if (construction == "X") {
                        // x-line, need other
                        if (corner3.Y.equals(corner1.Y) && (Math.abs(corner3.X - corner1.X)) <= UserVariables.GRID_THICKNESS) {
                            corner4 = new Point3D(corner3.X, corner2.Y, zcoord);
                            corners++;
                        } else if (corner3.Y.equals(corner2.Y) && (Math.abs(corner3.X - corner1.X)) <= UserVariables.GRID_THICKNESS) {
                            corner4 = new Point3D(corner3.X, corner1.Y, zcoord);
                            corners++;
                        }
                    } else if (construction == "Y"){
                        // have y-line, need other
                        if (corner3.X.equals(corner1.X) && (Math.abs(corner3.Y - corner1.Y)) <= UserVariables.GRID_THICKNESS){
                            corner4 = new Point3D(corner2.X, corner3.Y, zcoord);
                            corners++;
                        } else if (corner3.X.equals(corner2.X) && (Math.abs(corner3.Y - corner1.Y)) <= UserVariables.GRID_THICKNESS){
                            corner4 = new Point3D(corner1.X, corner3.Y, zcoord);
                            corners++;
                        }
                    } else if (construction == "XY"){
                        // have opposite corners
                        if (corner3.X.equals(corner1.X) && corner3.Y.equals(corner2.Y)){
                            corner4 = new Point3D(corner2.X, corner1.Y, zcoord);
                            corners++;
                        } else if (corner3.X.equals(corner2.X) && corner3.Y.equals(corner1.Y)){
                            corner4 = new Point3D(corner1.X, corner2.Y, zcoord);
                            corners++;
                        }
                    }
                }

            } else if (corners==3){
                if (corner4.equals(fp.GetPosition())){
                    d4 = distance; // add the distance of the first corner
                    corners++;
                }
            }

        }
        // TODO put corners to top/bottom left/right
        minX = Algorithms.min(corner1.X, corner2.X, corner3.X, corner4.X);
        maxX = Algorithms.max(corner1.X, corner2.X, corner3.X, corner4.X);
        minY = Algorithms.min(corner1.Y, corner2.Y, corner3.Y, corner4.Y);
        maxY = Algorithms.max(corner1.Y, corner2.Y, corner3.Y, corner4.Y);

        assignCorner(corner1, d1);
        assignCorner(corner2, d2);
        assignCorner(corner3, d3);
        assignCorner(corner4, d4);

        dTotal = d1 + d2 + d3 + d4;
    }

    /**
     * assign the corner to a position
     * TopLeft = (minX, minY)
     * BottomRight = (maxX, maxY)
     * @param p the position of the corner/RP
     * @param d the Euclidean distance for the corner/RP
     */
    private void assignCorner (Point3D p, double d){
        double x = p.X;
        double y = p.Y;
        if (x == minX && y == minY) {
            pTL = p;
            dTL = d;
        } else if (x == maxX && y == minY) {
            pTR = p;
            dTR = d;
        } else if (x == minX && y == maxY) {
            pBL = p;
            dBL = d;
        } else if (x == maxX && y == maxY) {
            pBR = p;
            dBR = d;
        }
    }

    public Point3D Localise (){

        // weight the coordinates by their distance and move closer to the centre
        pTL = new Point3D(pTL.X + dTL/dTotal * UserVariables.GRID_THICKNESS,
                pTL.Y + dTL/dTotal * UserVariables.GRID_THICKNESS, zcoord);
        pTR = new Point3D(pTR.X - dTR/dTotal * UserVariables.GRID_THICKNESS,
                pTR.Y + dTR/dTotal * UserVariables.GRID_THICKNESS, zcoord);
        pBL = new Point3D(pBL.X + dBL/dTotal * UserVariables.GRID_THICKNESS,
                pBL.Y - dBL/dTotal * UserVariables.GRID_THICKNESS, zcoord);
        pBR = new Point3D(pBR.X - dBR/dTotal * UserVariables.GRID_THICKNESS,
                pBR.Y - dBR/dTotal * UserVariables.GRID_THICKNESS, zcoord);


        // find whether the new coordinates have passed eachother on the x or y axis
        List<Double> underlapX = new ArrayList<Double>();
        List<Double> underlapY = new ArrayList<Double>();
        List<Double> overlapX = new ArrayList<Double>();
        List<Double> overlapY = new ArrayList<Double>();

        if (pTL.X < pTR.X){
            underlapX.add(pTL.X);
            underlapX.add(pTR.X);
        } else {
            overlapX.add(pTL.X);
            overlapX.add(pTR.X);
        }

        if (pBL.X < pBR.X){
            underlapX.add(pBL.X);
            underlapX.add(pBR.X);
        } else {
            overlapX.add(pBL.X);
            overlapX.add(pBR.X);
        }

        if (pTL.Y < pBL.Y){
            underlapX.add(pTL.Y);
            underlapX.add(pBL.Y);
        } else {
            overlapX.add(pTL.Y);
            overlapX.add(pBL.Y);
        }

        if (pTR.Y < pBR.Y){
            underlapX.add(pTR.Y);
            underlapX.add(pBR.Y);
        } else {
            overlapX.add(pTR.Y);
            overlapX.add(pBR.Y);
        }

        // estimate point
        double estX = 0;
        double estY = 0;

        if (underlapX.size() > 0){
            double total = 0;
            double count = 0;
            for(Iterator<Double> i = underlapX.iterator(); i.hasNext(); ) {
                double item = i.next();
                total += item;
                count++;
            }
            estX = total/count;
        } else if (overlapX.size() > 0){
            double total = 0;
            double count = 0;
            for(Iterator<Double> i = overlapX.iterator(); i.hasNext(); ) {
                double item = i.next();
                total += item;
                count++;
            }
            estX = total/count;
        }

        if (underlapY.size() > 0){
            double total = 0;
            double count = 0;
            for(Iterator<Double> i = underlapY.iterator(); i.hasNext(); ) {
                double item = i.next();
                total += item;
                count++;
            }
            estY = total/count;
        } else if (overlapY.size() > 0){
            double total = 0;
            double count = 0;
            for(Iterator<Double> i = overlapY.iterator(); i.hasNext(); ) {
                double item = i.next();
                total += item;
                count++;
            }
            estY = total/count;
        }

        //TODO test whether considering overlap improves accuracy, if not then use next 2 lines instead all the above
        estX = (pTL.X + pTR.X + pBL.X + pBR.X) / 4;
        estY = (pTL.Y + pTR.Y + pBL.Y + pBR.Y) / 4;

        return new Point3D(estX, estY, zcoord);
    }
}
