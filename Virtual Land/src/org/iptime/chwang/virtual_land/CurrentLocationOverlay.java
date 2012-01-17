package org.iptime.chwang.virtual_land;

import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;


public class CurrentLocationOverlay extends Overlay{
	private GeoPoint currentGeoPoint;
	Drawable myDrawable;
	
	public CurrentLocationOverlay(Drawable myDrawable){
		currentGeoPoint=new GeoPoint(36368455,127365285);
		this.myDrawable=myDrawable;
	}
	
	public void setGeoPoint(GeoPoint newGeoPoint){
		currentGeoPoint=newGeoPoint;
	}
	
	public boolean draw(Canvas canvas, MapView mapView, boolean shadow, long when){
		super.draw(canvas, mapView, false);
		
		Point screenPoint=new Point();
		mapView.getProjection().toPixels(currentGeoPoint,screenPoint);
		drawAt(canvas, myDrawable, screenPoint.x, screenPoint.y+myDrawable.getIntrinsicHeight()/2, false);
		
		return true;
		
	}
	
}
