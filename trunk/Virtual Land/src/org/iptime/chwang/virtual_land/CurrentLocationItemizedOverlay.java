package org.iptime.chwang.virtual_land;

import java.util.ArrayList;

import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

import android.graphics.drawable.Drawable;

public class CurrentLocationItemizedOverlay extends MyIconItemizedOverlay {

	public CurrentLocationItemizedOverlay(Drawable defaultMarker) {
		super(boundCenter(defaultMarker));
		mOverlays=new ArrayList<OverlayItem>();
	}
	
	public void draw(android.graphics.Canvas canvas, MapView mapView, boolean shadow){
		super.draw(canvas,mapView,false);
	}

	
}
