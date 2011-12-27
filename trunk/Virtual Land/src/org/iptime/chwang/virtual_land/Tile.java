package org.iptime.chwang.virtual_land;


import java.util.ArrayList;

import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Canvas;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import android.util.Log;

public class Tile extends Overlay {
	
	ArrayList<GeoPoint> geoPoints = new ArrayList<GeoPoint>();
	int tileColor;
	
	public Tile(GeoPoint center,int deltaLat, int deltaLon, int myColor){
		
		int centerLat=center.getLatitudeE6();
		int centerLon=center.getLongitudeE6();
		
		
		
		GeoPoint left_top=new GeoPoint(centerLat + deltaLat/2, centerLon - deltaLon/2);
		GeoPoint left_bottom=new GeoPoint(centerLat + deltaLat/2, centerLon + deltaLon/2);
		GeoPoint right_top=new GeoPoint(centerLat - deltaLat/2, centerLon - deltaLon/2);
		GeoPoint right_bottom=new GeoPoint(centerLat - deltaLat/2, centerLon + deltaLon/2);
		
		//Log.i("Chwang", "Right After Vertices Initializing");
		
		geoPoints.add(left_top);
		geoPoints.add(left_bottom);
		geoPoints.add(right_bottom);
		geoPoints.add(right_top);
		
		//Log.i("Chwang", "Right After Vertices are added to ArrayList");
		
		tileColor=myColor;
		//Log.i("Chwang", "Right Before Tile Constructor Ends");
	}
	
	@Override
	public void draw (Canvas canvas, MapView mapView, boolean shadow){
	    //Set the color and style
	    Paint paint = new Paint();
	    paint.setColor(tileColor);
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