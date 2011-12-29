package org.iptime.chwang.virtual_land;

import android.graphics.drawable.Drawable;
import android.util.Log;

public class RemovableItemizedOverlay extends MyIconItemizedOverlay {

	Drawable drawable_blueflag;
	
	/*
	public RemovableItemizedOverlay(Drawable defaultMarker){
		super(boundCenterBottom(defaultMarker));
		//super(defaultMarker);
	}
	*/
	public RemovableItemizedOverlay(Drawable defaultMarker){
		super(boundCenterBottom(defaultMarker));
		drawable_blueflag=defaultMarker;
	}
	
	@Override
	protected boolean onTap(int index){
		if(index==size()-1){
			Log.i("Chwang","Here I will insert removing code");
			Log.i("Chwang",Integer.toString(index));
			
			mOverlays.remove(index);
			
			Log.i("Chwang",Integer.toString(index));
			
			if(size()!=0) mOverlays.get(index-1).setMarker(drawable_blueflag);
			
			Log.i("Chwang",Integer.toString(index));
			
			//populate();
			
			Log.i("Chwang","After populate()");
		}
		
		return true;
	}
}
