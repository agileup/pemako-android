package org.iptime.chwang.virtual_land;

import java.util.ArrayList;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Canvas;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class Polygon extends Overlay {
	
	ArrayList<GeoPoint> geoPoints;
	public Polygon(ArrayList<GeoPoint> points){
		geoPoints = points;
	}
	
	@Override
	public void draw (Canvas canvas, MapView mapView, boolean shadow){
	    //Set the color and style
	    Paint paint = new Paint();
	    paint.setColor(Color.RED);
	    paint.setStyle(Paint.Style.FILL_AND_STROKE);    
	    paint.setAntiAlias(true);
	    paint.setAlpha(30);
	    
	    //Create path and add points
	    Path path = new Path();
	    Point firstPoint = new Point();
	    mapView.getProjection().toPixels(geoPoints.get(0), firstPoint);
	    path.moveTo(firstPoint.x, firstPoint.y);    
	    
	    for(int i = 1; i < geoPoints.size(); ++i){
	        Point nextPoint = new Point();
	        mapView.getProjection().toPixels(geoPoints.get(i), nextPoint);
	        path.lineTo(nextPoint.x, nextPoint.y);
	    }    //Close polygon
	    
	    path.lineTo(firstPoint.x, firstPoint.y);
	    path.setLastPoint(firstPoint.x, firstPoint.y);
	    canvas.drawPath(path, paint);
	    super.draw(canvas, mapView, shadow);
	}
}