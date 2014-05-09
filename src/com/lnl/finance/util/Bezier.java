package com.lnl.finance.util;

import android.graphics.Point;

public class Bezier {
	private static final float AP = 0.5f;  
    private BezierPoint[] bPoints;  
    /** 
     * Creates a new Bezier curve. 
     *  
     * @param points 
     */  
    public Bezier(Point[] points) {  
        int n = points.length;  
        if (n < 3) {  
            // Cannot create bezier with less than 3 points  
            return;  
        }  
        bPoints = new BezierPoint[2 * (n - 2)];  
        double paX, paY;  
        double pbX = points[0].x;  
        double pbY = points[0].y;  
        double pcX = points[1].x;  
        double pcY = points[1].y;  
        for (int i = 0; i < n - 2; i++) {  
            paX = pbX;  
            paY = pbY;  
            pbX = pcX;  
            pbY = pcY;  
            pcX = points[i + 2].x;  
            pcY = points[i + 2].y;  
            double abX = pbX - paX;  
            double abY = pbY - paY;  
            double acX = pcX - paX;  
            double acY = pcY - paY;  
            double lac = Math.sqrt(acX * acX + acY * acY);  
            acX = acX / lac;  
            acY = acY / lac;  
            double proj = abX * acX + abY * acY;  
            proj = proj < 0 ? -proj : proj;  
            double apX = proj * acX;  
            double apY = proj * acY;  
            double p1X = pbX - AP * apX;  
            double p1Y = pbY - AP * apY;  
            bPoints[2 * i] = new BezierPoint((int) p1X, (int) p1Y);  
            acX = -acX;  
            acY = -acY;  
            double cbX = pbX - pcX;  
            double cbY = pbY - pcY;  
            proj = cbX * acX + cbY * acY;  
            proj = proj < 0 ? -proj : proj;  
            apX = proj * acX;  
            apY = proj * acY;  
            double p2X = pbX - AP * apX;  
            double p2Y = pbY - AP * apY;  
            bPoints[2 * i + 1] = new BezierPoint((int) p2X, (int) p2Y);  
        }  
    }  
    /** 
     * Returns the calculated bezier points. 
     *  
     * @return the calculated bezier points 
     */  
    public BezierPoint[] getPoints() {  
        return bPoints;  
    }  
    /** 
     * Returns the number of bezier points. 
     *  
     * @return number of bezier points 
     */  
    public int getPointCount() {  
        return bPoints.length;  
    }  
    /** 
     * Returns the bezier points at position i. 
     *  
     * @param i 
     * @return the bezier point at position i 
     */  
    public BezierPoint getPoint(int i) {  
        return bPoints[i];  
    }  
}
