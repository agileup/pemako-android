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
	
	public FlagItemizedOverlay(Drawable defaultMarker,Context tmpContext,MapView tmpMapView){
		super(boundCenterBottom(defaultMarker));
		drawable_blueflag=defaultMarker;
		mContext=tmpContext;
		mMapView=tmpMapView;
	}
	
	@Override
	protected boolean onTap(int index){
		final int mIndex=index;
		
		if(index==size()-1){
			
			AlertDialog.Builder alertDlg=new AlertDialog.Builder(mContext);
			alertDlg.setTitle("Remove a Flag");
			alertDlg.setMessage("Do you really want to remove this blue flag?");
			
			alertDlg.setPositiveButton("YES", new DialogInterface.OnClickListener() {		
				@Override
				public void onClick(DialogInterface dialog, int which) {
					mOverlays.remove(mIndex);
					Log.i("Chwang","FlagIO's mOverlays.size()="+Integer.toString(mIndex));
					if(size()!=0) {
						mOverlays.get(mIndex-1).setMarker(drawable_blueflag);
					}
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
			
			
		}//end if
		
		return true;
	}
	
	
}