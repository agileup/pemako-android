package org.iptime.chwang.virtual_land;

import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.Log;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

public class PolyLine extends Overlay {

	ArrayList<GeoPoint> geoPoints;
	
	//Constructor1
	public PolyLine(){
		geoPoints=new ArrayList<GeoPoint>();
	}
	
	//Constructor2
	public PolyLine(ArrayList<GeoPoint> points){
		geoPoints=points;
	}
	
	//function to add geopoint
	public void addGeoPoint(GeoPoint tmpGeoPoint){
		geoPoints.add(tmpGeoPoint);
	}
	
	//function to remove the last geopoint
	public void removeLastGeoPoint(){
		geoPoints.remove(geoPoints.size()-1);
	}
	
	//draw function (override)
	public void draw (Canvas canvas, MapView mapView, boolean shadow){
		//super.draw(canvas,mapView,shadow);
		Log.i("Chwang","PolyLine.draw() called");
		
		Paint mPaint = new Paint();
		mPaint.setDither(true);
		mPaint.setColor(Color.RED);
		mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
		mPaint.setStrokeJoin(Paint.Join.ROUND);
		mPaint.setStrokeCap(Paint.Cap.ROUND);
		mPaint.setStrokeWidth(2);
		
		Path path=new Path();
		Point p1=new Point();
		Point p2=new Point();
		Projection projection=mapView.getProjection();
		
		int length=geoPoints.size();
		
		for(int i=0; i<length-1; i++){
			projection.toPixels(geoPoints.get(i), p1);
			projection.toPixels(geoPoints.get(i+1), p2);
			path.moveTo(p2.x, p2.y);
			path.lineTo(p1.x, p1.y);
			canvas.drawPath(path,mPaint);
		}
		
	}
	
}
