package org.iptime.chwang.virtual_land;

import android.graphics.drawable.Drawable;
import android.util.Log;

public class RemovableItemizedOverlay extends MyIconItemizedOverlay {

	public RemovableItemizedOverlay(Drawable defaultMarker){
		super(boundCenterBottom(defaultMarker));
		//super(defaultMarker);
	}
	
	@Override
	protected boolean onTap(int index){
		if(index==size()-1){
			Log.i("Chwang","Here I will insert removing code");
			
		}
		
		return true;
	}
}
