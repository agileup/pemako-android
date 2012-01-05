package org.iptime.chwang.virtual_land;

import com.google.android.maps.MapView;

import android.graphics.drawable.Drawable;

public class CurrentLocationItemizedOverlay extends MyIconItemizedOverlay {

	public CurrentLocationItemizedOverlay(Drawable defaultMarker) {
		super(boundCenter(defaultMarker));
	}
	
	public void draw(android.graphics.Canvas canvas, MapView mapView, boolean shadow){
		super.draw(canvas,mapView,false);
	}

}
