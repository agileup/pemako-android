package org.iptime.chwang.virtual_land;


import java.util.ArrayList;

import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import android.util.Log;

public class Tile extends Overlay {
	
	ArrayList<GeoPoint> geoPoints = new ArrayList<GeoPoint>();
	int tileColor;
	MapView mapView;
	
	
	public Tile(GeoPoint center,int deltaLat, int deltaLon, int myColor, MapView mapView){
		
		this.mapView=mapView;
		
		int centerLat=center.getLatitudeE6();
		int centerLon=center.getLongitudeE6();
		
		
		/*
		GeoPoint left_top=new GeoPoint(centerLat + deltaLat/2, centerLon - deltaLon/2);
		GeoPoint left_bottom=new GeoPoint(centerLat + deltaLat/2, centerLon + deltaLon/2);
		GeoPoint right_top=new GeoPoint(centerLat - deltaLat/2, centerLon - deltaLon/2);
		GeoPoint right_bottom=new GeoPoint(centerLat - deltaLat/2, centerLon + deltaLon/2);
		*/
		
		GeoPoint left_top=new GeoPoint(centerLat + deltaLat/2, centerLon - deltaLon/2);
		GeoPoint left_bottom=new GeoPoint(centerLat - deltaLat/2, centerLon - deltaLon/2);
		GeoPoint right_top=new GeoPoint(centerLat + deltaLat/2, centerLon + deltaLon/2);
		GeoPoint right_bottom=new GeoPoint(centerLat - deltaLat/2, centerLon + deltaLon/2);
		
		//Log.i("Chwang", "Right After Vertices Initializing");
		
		geoPoints.add(left_top);
		geoPoints.add(left_bottom);
		geoPoints.add(right_bottom);
		geoPoints.add(right_top);
		
		//Log.i("Chwang", "Right After Vertices are added to ArrayList");
		
		Point screenPoint1=new Point();
		Point screenPoint2=new Point();
		mapView.getProjection().toPixels(left_top,screenPoint1);
		mapView.getProjection().toPixels(right_bottom,screenPoint2);
		
		/*
		myRect=new Rect(screenPoint1.x,screenPoint2.y,screenPoint2.x,screenPoint1.y);
		Log.i("Chwang","==================================");
		Log.i("Chwang","Left: "+Integer.toString(screenPoint1.x));
		Log.i("Chwang","Top: "+Integer.toString(screenPoint1.y));
		Log.i("Chwang","Right: "+Integer.toString(screenPoint2.x));
		Log.i("Chwang","Bottom: "+Integer.toString(screenPoint2.y));
		*/
		
		tileColor=myColor;
		//Log.i("Chwang", "Right Before Tile Constructor Ends");
	}
	
	@Override
	public void draw (Canvas canvas, MapView mapView, boolean shadow){
		
		
	    //Set the color and style
	    Paint paint = new Paint();
	    paint.setColor(tileColor);
	    //paint.setStyle(Paint.Style.FILL_AND_STROKE);
	    paint.setStyle(Paint.Style.FILL);
	    paint.setAntiAlias(true);
	    paint.setAlpha(30);
	    
	    //Create path and add points
	    /*
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
	    */
	    
	    //canvas.drawRect(myRect, paint);
	    //Log.i("Chwang","Drawing myRect");
	    super.draw(canvas, mapView, shadow);
	}
}