package escapeShip;

import java.awt.*;
import java.awt.geom.Line2D;

/**
 * Created by Tyler on 3/22/2014.
 */
public class GamePolygon extends Polygon {

    public GamePolygon(int[] xPoints, int[] yPoints, int nPoints){
        super(xPoints,yPoints,nPoints);
    }

    public static boolean intersects(GameObjects firstGO, GameObjects secondGO){
        GamePolygon firstGP = firstGO.gamePolygon;
        GamePolygon secondGP = secondGO.gamePolygon;

        if(targetsClose(firstGO, secondGO)) {
            for (int i = 0; i < firstGP.npoints; i++) {
                int fLineFX = firstGP.xpoints[i];
                int fLineFY = firstGP.ypoints[i];
                int fLineSX;
                int fLineSY;
                if (i >= firstGP.npoints - 2) {//it is on the last one, use the first set of points in the list for second point in the line
                    fLineSX = firstGP.xpoints[0];
                    fLineSY = firstGP.ypoints[0];
                } else {//use the next set of points
                    fLineSX = firstGP.xpoints[i + 1];
                    fLineSY = firstGP.ypoints[i + 1];
                }
                for (int k = 0; k < secondGP.npoints; k++) {
                    int sLineFX = secondGP.xpoints[k];
                    int sLineFY = secondGP.ypoints[k];
                    int sLineSX;
                    int sLineSY;
                    if (k >= secondGP.npoints - 2) {//it is on the last one, use the first set of points in the list for second point in the line
                        sLineSX = secondGP.xpoints[0];
                        sLineSY = secondGP.ypoints[0];
                    } else {//use the next set of points
                        sLineSX = secondGP.xpoints[k + 1];
                        sLineSY = secondGP.ypoints[k + 1];
                    }

                    Line2D line1 = new Line2D.Float(fLineFX, fLineFY, fLineSX, fLineSY);
                    Line2D line2 = new Line2D.Float(sLineFX, sLineFY, sLineSX, sLineSY);

                    if (line2.intersectsLine(line1))
                        return true;
                }
            }
        }

        return false;
    }

    private static boolean targetsClose(GameObjects firstGO, GameObjects secondGO){
        boolean xClose = false;
        boolean yClose = false;

        int diffOfX = (int)(firstGO.xCoordinate - secondGO.xCoordinate);
        int diffOfY = (int)(firstGO.yCoordinate - secondGO.yCoordinate);

        if(diffOfX < 20 || diffOfX > -20)
            xClose = true;

        if(diffOfY < 20 || diffOfX > -20)
            yClose = true;

        if(xClose && yClose)
            return true;
        else
            return false;
    }

}


