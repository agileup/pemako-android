package org.iptime.chwang.virtual_land;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.MotionEvent;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

public class FlagOverlay extends Overlay{

	private ArrayList<GeoPoint> myGeoPoints;
	Bitmap myBitmap;
	Drawable drawable_redflag;
	Drawable drawable_blueflag;
	MapView mMapView;
	Context mContext;
	
	
	public FlagOverlay(Drawable drawable_redflag,Drawable drawable_blueflag,MapView mapView,Context context){
		myGeoPoints=new ArrayList<GeoPoint>();
		this.drawable_redflag=drawable_redflag;
		this.drawable_blueflag=drawable_blueflag;
		this.mMapView=mapView;
		this.mContext=context;
		
	}
	
	public void addGeoPoint(GeoPoint tmpGeoPoint){
		myGeoPoints.add(tmpGeoPoint);
	}
	
	public void removeGeoPoint(int index){
		myGeoPoints.remove(index);
	}
	
	public void removeAll(){
		myGeoPoints.clear();
	}
	
	public GeoPoint getGeoPoint(int index){
		return myGeoPoints.get(index);
	}
	
	public boolean draw(Canvas canvas, MapView mapView, boolean shadow, long when){
		//super.draw(canvas, mapView, shadow);
		super.draw(canvas, mapView, true);
		
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
		
		Point screenPts=new Point();
		
		for(int i=0; i<myGeoPoints.size()-1; i++){
			
			//Draw Lines between flags (red line)
			projection.toPixels(myGeoPoints.get(i), p1);
			projection.toPixels(myGeoPoints.get(i+1), p2);
			path.moveTo(p2.x, p2.y);
			path.lineTo(p1.x, p1.y);
			canvas.drawPath(path,mPaint);
			
			
			//Draw red flags (index: 0 ~ size-2)
			mapView.getProjection().toPixels(myGeoPoints.get(i),screenPts);
			drawAt(canvas, drawable_redflag, screenPts.x, screenPts.y, shadow);
			
		}
		
		//Draw blue flag (index: size-1)
		if(myGeoPoints.size()>=1){
			GeoPoint tmp=myGeoPoints.get(myGeoPoints.size()-1);
			mapView.getProjection().toPixels(tmp,screenPts);
			drawAt(canvas, drawable_blueflag, screenPts.x, screenPts.y, shadow);
		}
		
		return true;
		
	}
	
	public boolean onTouchEvent(MotionEvent event, MapView mapView){
		if(event.getAction()==1 && size()>0){
			int touchedX=(int)event.getX();
			int touchedY=(int)event.getY();
			
			//GeoPoint p=mapView.getProjection().fromPixels(coorX, coorY);
			
			GeoPoint p=myGeoPoints.get(myGeoPoints.size()-1);
			Point target=new Point();
			mapView.getProjection().toPixels(p, target);
			
			if(touchedX<target.x+50 && touchedX>target.x-50 && touchedY>target.y-50 && touchedY<target.y+50){
				Log.i("Chwang","Removable Flag touched");
				
				
				AlertDialog.Builder alertDlg=new AlertDialog.Builder(mContext);
				alertDlg.setTitle("Remove a Flag");
				alertDlg.setMessage("Do you really want to remove this blue flag?");
				
				alertDlg.setPositiveButton("YES", new DialogInterface.OnClickListener() {		
					@Override
					public void onClick(DialogInterface dialog, int which) {
						removeGeoPoint(myGeoPoints.size()-1);
						showGeoPoints();
						
						mMapView.invalidate();
					}
				});
				
				alertDlg.setNegativeButton("NO", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
				
				alertDlg.show();
				
				
				
				
				
			}else{
				Log.i("Chwang","Removable Flag NOT touched");
			}
			
		}
		return false;
	}
	
	public int size(){
		return myGeoPoints.size();
	}
	
	public void showGeoPoints(){
		Log.i("Chwang","====================================");
		for(int i=0; i<size(); i++){
			Log.i("Chwang","Lat: "+ Integer.toString(myGeoPoints.get(i).getLatitudeE6()) 
					+ " / Lon: "+ Integer.toString(myGeoPoints.get(i).getLongitudeE6()));
		}
		Log.i("Chwang","====================================");
	}
}
