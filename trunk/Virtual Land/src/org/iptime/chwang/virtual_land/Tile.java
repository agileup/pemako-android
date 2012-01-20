package org.iptime.chwang.virtual_land;


import java.util.ArrayList;

import android.graphics.Paint;
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
	
	
	public Tile(int targetLat,int targetLon,int deltaLat, int deltaLon, int tileInfo, MapView mapView){
		
		this.mapView=mapView;
		
		int centerLat=targetLat;
		int centerLon=targetLon;
		
		GeoPoint left_top=new GeoPoint(centerLat + deltaLat/2, centerLon - deltaLon/2);
		GeoPoint left_bottom=new GeoPoint(centerLat - deltaLat/2, centerLon - deltaLon/2);
		GeoPoint right_top=new GeoPoint(centerLat + deltaLat/2, centerLon + deltaLon/2);
		GeoPoint right_bottom=new GeoPoint(centerLat - deltaLat/2, centerLon + deltaLon/2);
		
		geoPoints.add(left_top);
		geoPoints.add(left_bottom);
		geoPoints.add(right_bottom);
		geoPoints.add(right_top);
		
		tileColor=colorSelector(tileInfo);
	}
	
	@Override
	public void draw (Canvas canvas, MapView mapView, boolean shadow){
		
		
		Point screenPoint1=new Point();
		Point screenPoint2=new Point();
		mapView.getProjection().toPixels(geoPoints.get(0),screenPoint1);
		mapView.getProjection().toPixels(geoPoints.get(2),screenPoint2);
		Rect myRect=new Rect(screenPoint1.x,screenPoint2.y,screenPoint2.x,screenPoint1.y);
		
		
	    //Set the color and style for Paint object
	    Paint paint = new Paint();
	    paint.setColor(tileColor);
	    paint.setStyle(Paint.Style.FILL);
	    paint.setAntiAlias(true);
	    paint.setAlpha(30);
	    
	    //draw Rect
	    canvas.drawRect(myRect, paint);
	    
	    super.draw(canvas, mapView, shadow);
	}
	
	//Temporary Function
    private int colorSelector(int input){
    	int result=0xff000000;
    	int tmp;
    	int remain=input%7;
    	
    	switch(remain){
	    	case 0:
	    		tmp=0xe61a0b; //RED
	    		break;
	    	case 1:
	    		tmp=0x09c016; //GREEN
	    		break;
	    	case 2:
	    		tmp=0x1a0be6; //BLUE
	    		break;
	    	case 3:
	    		tmp=0xf0ff32; //YELLOW
	    		break;
	    	case 4:
	    		tmp=0xff32f0; //PLUM
	    		break;
	    	case 5:
	    		tmp=0xa2988c; //Dark BEIGE 
	    		break;
	    	case 6:
	    		tmp=0xc86e02; //Dark ORANGE
	    		break;
	    	default:
	    		tmp=0x8fa9c8; //Cobalt
	    		break;
    	}
    	
    	result+=tmp;
    	return result;
    }//end of colorSelector()
}