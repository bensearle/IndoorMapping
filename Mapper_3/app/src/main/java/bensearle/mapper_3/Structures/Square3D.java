package bensearle.mapper_3.Structures;

import java.util.List;
import java.util.Map;

import bensearle.mapper_3.UserVariables;

/**
 * Created by bensearle on 19/06/2016.
 */
public class Square3D {

    // corners of the square, top/bottom left/right
    private Point3D tl;
    private Point3D tr;
    private Point3D bl;
    private Point3D br;

    private double distance1;
    private double distance2;
    private double distance3;
    private double distance4;

    public Square3D(){

    }


    public Square3D(List<Float> distances, Map<Float, Fingerprint> fingerprintAndDistance) {
        int corners = 0;
        String construction = ""; // are the first to on the same X, Y, or XY axis

        double zcoord = 0;
        Point3D corner1 = new Point3D();
        Point3D corner2 = new Point3D();
        Point3D corner3 = new Point3D();
        Point3D corner4 = new Point3D();

        for (Float distance: distances){
            Fingerprint fp = fingerprintAndDistance.get(distance);

            if (corners==0){
                corner1 = fp.GetPosition(); // add the corner point
                zcoord = corner1.Z;
                distance1 = distance; // add the distance of the first corner
                corners++;
            } else if (corners==1){
                corner2 = fp.GetPosition(); // add the corner point
                distance2 = distance; // add the distance of the first corner
                if (corner2.Z == zcoord) {
                    if (corner1.X == corner2.X && (Math.abs(corner1.Y - corner2.Y)) <= UserVariables.GRID_THICKNESS) {
                        // same x, y are next to eachother
                        construction = "X";
                        corners++;
                    } else if (corner1.Y == corner2.Y && (Math.abs(corner1.X - corner2.X)) <= UserVariables.GRID_THICKNESS) {
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
                distance3 = distance; // add the distance of the first corner

                if (corner3.Z == zcoord) {
                    if (construction == "X") {
                        // x-line, need other
                        if ((corner3.Y == corner1.Y) && (Math.abs(corner3.X - corner1.X)) <= UserVariables.GRID_THICKNESS) {
                            corner4 = new Point3D(corner3.X, corner2.Y, zcoord);
                            corners++;
                        } else if ((corner3.Y == corner2.Y) && (Math.abs(corner3.X - corner1.X)) <= UserVariables.GRID_THICKNESS) {
                            corner4 = new Point3D(corner3.X, corner1.Y, zcoord);
                            corners++;
                        }
                    } else if (construction == "Y"){
                        // have y-line, need other
                        if ((corner3.X == corner1.X) && (Math.abs(corner3.Y - corner1.Y)) <= UserVariables.GRID_THICKNESS){
                            corner4 = new Point3D(corner2.X, corner3.Y, zcoord);
                            corners++;
                        } else if ((corner3.X == corner2.X) && (Math.abs(corner3.Y - corner1.Y)) <= UserVariables.GRID_THICKNESS){
                            corner4 = new Point3D(corner1.X, corner3.Y, zcoord);
                            corners++;
                        }
                    } else if (construction == "XY"){
                        // have opposite corners
                        if (corner3.X == corner1.X && corner3.Y == corner2.Y){
                            corner4 = new Point3D(corner2.X, corner1.Y, zcoord);
                            corners++;
                        } else if (corner3.X == corner2.X && corner3.Y == corner1.Y){
                            corner4 = new Point3D(corner1.X, corner2.Y, zcoord);
                            corners++;
                        }
                    }
                }

            } else if (corners==3){
                if (corner4 == fp.GetPosition()){
                    distance4 = distance; // add the distance of the first corner
                    corners++;
                }
            }

        }
        // TODO put corners to top/bottom left/right

    }
}
