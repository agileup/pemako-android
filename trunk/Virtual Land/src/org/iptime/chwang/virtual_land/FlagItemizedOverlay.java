package org.iptime.chwang.virtual_land;

import com.google.android.maps.MapView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;

public class FlagItemizedOverlay extends MyIconItemizedOverlay {

	Drawable drawable_blueflag;
	Context mContext;
	MapView mMapView;
	PolyLine mPolyLine;
	int mIndex;
	
	public FlagItemizedOverlay(Drawable defaultMarker,Context tmpContext,MapView tmpMapView){
		super(boundCenterBottom(defaultMarker));
		drawable_blueflag=defaultMarker;
		mContext=tmpContext;
		mMapView=tmpMapView;
		mPolyLine=new PolyLine();
		
		mMapView.getOverlays().add(mPolyLine);
	}
	
	private void overlaysIterator(){
		Log.i("Chwang","============================================");
		for(int i=0; i<size(); i++){
			Log.i("Chwang","Index "+Integer.toString(i)+": "+mOverlays.get(i).getPoint().toString());
		}
		Log.i("Chwang","============================================");
	}
	
	@Override
	protected boolean onTap(int index){
		mIndex=index;
		
		if(index==size()-1){
			
			overlaysIterator();
			Log.i("Chwang","tapped index : "+Integer.toString(index));
			Log.i("Chwang","Before removing mOverlay.size()="+Integer.toString(mOverlays.size()));			
			mOverlays.remove(index);
			mPolyLine.removeLastGeoPoint();
			Log.i("Chwang","After removing mOverlay.size()="+Integer.toString(mOverlays.size()));
			if(size()!=0) mOverlays.get(index-1).setMarker(drawable_blueflag);
			mMapView.invalidate();
			
			
			/*
			AlertDialog.Builder alertDlg=new AlertDialog.Builder(mContext);
			alertDlg.setTitle("Remove a Flag");
			alertDlg.setMessage("Do you really want to remove this blue flag?");
			
			alertDlg.setPositiveButton("YES", new DialogInterface.OnClickListener() {		
				@Override
				public void onClick(DialogInterface dialog, int which) {
					overlaysIterator();
					mPolyLine.removeLastGeoPoint();
					mOverlays.remove(mIndex);
					
					Log.i("Chwang","FlagIO's mOverlays.size()="+Integer.toString(mIndex));
					if(size()!=0) {
						mOverlays.get(mIndex-1).setMarker(drawable_blueflag);
					}
					
					mMapView.invalidate();
					overlaysIterator();
				}
			});
			
			alertDlg.setNegativeButton("NO", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			});
			
			alertDlg.show();
			*/
			
		}//end if
		
		return true;
	}
	
	
}
