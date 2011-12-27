package org.iptime.chwang.virtual_land;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;


public class MyIconItemizedOverlay extends ItemizedOverlay<OverlayItem> {

	protected ArrayList<OverlayItem> mOverlays=new ArrayList<OverlayItem>();
	//private Context mContext;
	
	public MyIconItemizedOverlay(Drawable defaultMarker){
		super(boundCenterBottom(defaultMarker));
		//super(defaultMarker);
	}
	
	
	@Override
	protected OverlayItem createItem(int arg0) {
		return mOverlays.get(arg0);
	}

	@Override
	public int size() {
		return mOverlays.size();
	}
		
	public void addOverlayItem(OverlayItem overlay){
		mOverlays.add(overlay);
		populate();
	}
	
	public void removeOverlayItem(OverlayItem overlay){
		mOverlays.remove(overlay);
		populate();
	}
	
	public void removeOverlayItem(int index){
		mOverlays.remove(index);
		populate();
	}

}
