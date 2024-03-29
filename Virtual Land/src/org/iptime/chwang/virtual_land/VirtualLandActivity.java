package org.iptime.chwang.virtual_land;

import android.os.Bundle;


import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.MapController;
import com.google.android.maps.GeoPoint;

import android.location.Location;
import android.location.LocationManager;
import android.location.LocationListener;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.content.res.Resources;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.drawable.Drawable;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.google.android.maps.MapView.LayoutParams;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class VirtualLandActivity extends MapActivity {
	
	//서버주소
	final String SERVER_ADDR = "http://pemako.iptime.org";
	
	LocationListener mLocationListener;
	MapView mapView;
	MapController mc;
	List<Overlay> overlay;
	TextView status=null; //지도 화면 상단 위도경도 나타내는 텍스트뷰
	ImageButton bt_check;
	ImageButton bt_mission;
	Context mContext;
	
	//onLocationChangeListener에서 위치정보 받아서 저장해두는 변수
	GeoPoint buffer_geopoint=null;
	
	//처음 지도 띄울때만 영토정보 받아오기 위해 설정한 변수
	Boolean isFirst=true;
	
	
	//onBackPressed() 에서 두번 터치 시 종료를 위해 선언한 변수들
	private boolean checkBackKey=false;
    private long timeFirst;
    private long timeSecond;
    
    
    //Overlay 변수들 선언
	CurrentLocationOverlay currentLocationOverlay;
	FlagOverlay flagoverlay;
	
	//Overlay에 사용될 Drawable 변수들 선언
	Drawable drawable_redflag;
	Drawable drawable_blueflag;
	Drawable drawable_current;
	
	
    
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        mContext=this;
		
        //좌표 표시하는 임시 텍스트뷰
        status=new TextView(this);
        
        //맵뷰 참조 및 세팅
        mapView=(MapView)findViewById(R.id.mapview);
        mapView.setBuiltInZoomControls(true);
        mapView.setSatellite(false);
        //mapView.setBuiltInZoomControls(false);
             
        //맵을 다루기 위핸 맵뷰 컨트롤러
        mc=mapView.getController();
        
        //KAIST CS로 지도 이동
        GeoPoint kaistCS=new GeoPoint(36368455,127365285);
        mc.animateTo(kaistCS);
        mc.setZoom(18);
        
		
        //현재위치 오버레이 비트맵 설정 및 CurrentLocationItemizedOverlay 변수 currentIO 초기화
        Bitmap bitmap_current=BitmapFactory.decodeResource(getResources(), R.drawable.current_gp);
		bitmap_current=Bitmap.createScaledBitmap(bitmap_current, 15, 15, false);
		drawable_current=new BitmapDrawable(bitmap_current);
		drawable_current.setBounds(-drawable_current.getIntrinsicWidth()/2,-drawable_current.getIntrinsicHeight(),drawable_current.getIntrinsicWidth()/2,0);  
		currentLocationOverlay=new CurrentLocationOverlay(drawable_current);
		
		//플래그 오버레이(check시 표시하는 플래그) 비트맵 설정 및 FlagItemizedOverlay 변수 flagIO 초기화
		Bitmap bitmap_redflag=BitmapFactory.decodeResource(getResources(), R.drawable.check_redflag);
		bitmap_redflag=Bitmap.createScaledBitmap(bitmap_redflag, 50, 50, false);
		drawable_redflag=new BitmapDrawable(bitmap_redflag);
		drawable_redflag.setBounds(-drawable_redflag.getIntrinsicWidth()/2,-drawable_redflag.getIntrinsicHeight(),drawable_redflag.getIntrinsicWidth()/2,0);  
		
		
		Bitmap bitmap_blueflag=BitmapFactory.decodeResource(getResources(), R.drawable.check_blueflag);
		bitmap_blueflag=Bitmap.createScaledBitmap(bitmap_blueflag, 50, 50, false);
		drawable_blueflag=new BitmapDrawable(bitmap_blueflag);
		drawable_blueflag.setBounds(-drawable_blueflag.getIntrinsicWidth()/2,-drawable_blueflag.getIntrinsicHeight(),drawable_blueflag.getIntrinsicWidth()/2,0);
		
		
		flagoverlay=new FlagOverlay(drawable_redflag,drawable_blueflag,mapView,mContext);
		
		
        overlay=mapView.getOverlays();
        overlay.add(currentLocationOverlay);
        overlay.add(flagoverlay);
        
        //LocationManager 선언 및 초기화
        LocationManager lm=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
        
        //LocationListener 세팅
        mLocationListener=new LocationListener(){
        	public void onLocationChanged(Location location){
        		if(location!=null){
        			String provider=location.getProvider();
        			
        			Toast.makeText(getBaseContext(), 
        					"위도는 "+location.getLatitude()+", 경도는 "+location.getLongitude()+" 입니다.", 
        					Toast.LENGTH_SHORT).show();
        			
        			//buffer_geopoint 초기화 및 새로 할당
        			buffer_geopoint=new GeoPoint((int)(location.getLatitude()*1E6),(int)(location.getLongitude()*1E6));
        			
        			
        			
        			//GPS 상태 뿌려주는 상단 텍스트 메시지 세팅 및 애드
        			mapView.removeView(status);
        			status.setText("Lat: "+location.getLatitude()+"\nLong: "+location.getLongitude()+"\nProvider: "+provider);
        			status.setTextSize(15);
        			status.setTextColor(Color.BLACK);
        			MapView.LayoutParams lp=new MapView.LayoutParams(LayoutParams.WRAP_CONTENT, 
        															LayoutParams.WRAP_CONTENT, 10, 10, 
        															MapView.LayoutParams.LEFT | MapView.LayoutParams.TOP);
        			
        			mapView.addView(status,lp);
        			
        			
        			
        			mc.animateTo(buffer_geopoint);
        			mc.setZoom(18);
        			
        			currentLocationOverlay.setGeoPoint(buffer_geopoint);
        			
        			
        			
        			//서버 통신 테스트
        			if(isFirst==true){
        		        try{
        			        URL url=new URL(SERVER_ADDR+"/vland/tile_request.php");
        			    	HttpURLConnection http=(HttpURLConnection)url.openConnection();
        			    	
        			    	http.setDefaultUseCaches(false);
        			    	http.setDoInput(true);
        			    	http.setDoOutput(true);
        			    	http.setRequestMethod("POST");
        			    	
        			    	http.setRequestProperty("content-type","application/x-www-form-urlencoded");
        			    	
        			    	StringBuffer buffer=new StringBuffer();
        			    	
        			    	buffer.append("userid").append("=").append("chwang").append("&");
        			    	buffer.append("lat").append("=").append(buffer_geopoint.getLatitudeE6()).append("&");
        			    	buffer.append("lon").append("=").append(buffer_geopoint.getLongitudeE6());
        			    	
        			    	
        			    	OutputStreamWriter outStream=new OutputStreamWriter(http.getOutputStream(),"UTF-8");
        			    	PrintWriter writer=new PrintWriter(outStream);
        			    	writer.write(buffer.toString());
        			    	writer.flush();
        			    	
        			    	
        			    	InputStreamReader tmp=new InputStreamReader(http.getInputStream(),"UTF-8");
        			    	
        			    	 BufferedReader reader = new BufferedReader(tmp); 
        		             
        			    	 String starts=reader.readLine();
        		             String deltas=reader.readLine();
        		             String tiles=reader.readLine();
        		             
        		             String [] myStarts=starts.split("\\|");
        		             String [] myDeltas=deltas.split("\\|");
        		             String [] myTiles=tiles.split("\\|");
        		             
        		             
        		             int deltaLat=Integer.parseInt(myDeltas[0]);
        		             int deltaLon=Integer.parseInt(myDeltas[1]);
        		             
        		             int targetLat=Integer.parseInt(myStarts[0]);
        		             int targetLon=Integer.parseInt(myStarts[1]);
        		             
        		             
        		             Tile newTile;
        		             
        		             for(int i=0; i<48; i++){
        		            	 targetLon=Integer.parseInt(myStarts[1]);
        		            	 for(int j=0; j<30; j++){
        		            		 int tileInfo=Integer.parseInt(myTiles[i*30+j]);
        		            		 newTile=new Tile(targetLat,targetLon,deltaLat,deltaLon,tileInfo,mapView);
        		            		 overlay.add(newTile);
        		            		 
        		            		 targetLon+=deltaLon;
        		            	 }
        		            	 targetLat-=deltaLat;
        		             }
        		             
        		             isFirst=false;
        		             
        		        }catch(Exception e){
        		        	Toast.makeText(getBaseContext(), "서버 혹은 네트워크 환경이 원활하지 못합니다", Toast.LENGTH_SHORT).show();
        		        }
        			}
        		}
        	}//End of onLocationChanged
        	
        	public void onProviderDisabled(String arg0){
        		Log.i("Chwang","onProviderDisabled() called");
        		gpsDialog(mContext);
        	}//End of onProviderDisabled
        	
        	public void onProviderEnabled(String arg0){}//End of onProviderEnabled
        	public void onStatusChanged(String arg0,int arg1,Bundle arg2){}//end of onStatusChanged
        	
        }; //End-of-LocationListener
               
        
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 3, mLocationListener);
        
        
        
        //디바이스 화면 해상도 계산
        Display display = getWindowManager().getDefaultDisplay();  
        final int width = display.getWidth(); 
        final int height = display.getHeight(); 
        
        
        
        //지도위에 올릴 CHECK 버튼 만들기 
        bt_check=new ImageButton(this);
        Resources res=getResources();
        BitmapDrawable bitmapDrawable_check=(BitmapDrawable)res.getDrawable(R.drawable.button_check);
        Bitmap bitmap_check=bitmapDrawable_check.getBitmap();
        bitmap_check=Bitmap.createScaledBitmap(bitmap_check, 100, 100, false);
        bt_check.setImageBitmap(bitmap_check);
        bt_check.setBackgroundColor(Color.TRANSPARENT);
        
        
        //버튼 레이아웃 파라미터
        MapView.LayoutParams bt_check_layout=new MapView.LayoutParams(LayoutParams.WRAP_CONTENT, 
																	LayoutParams.WRAP_CONTENT, 
																	(int)(width*0.3), (int)(height*0.75), 
																	MapView.LayoutParams.CENTER);
        //레이아웃 파라미터와 함께 버튼을 맵뷰에 애드
        mapView.addView(bt_check,bt_check_layout);
    	
        
        
        bt_check.setOnClickListener(new OnClickListener(){
        	public void onClick(View v){
        		//Check 버튼 터치시 실행될 코드
        		
        		if(buffer_geopoint==null){
        			Log.i("Chwang","buffer_geopoint is null in setOnClickListener");
		        	Toast.makeText(getBaseContext(), "GPS 신호가 잡히지 않았습니다.", Toast.LENGTH_SHORT).show();
        			return;
        		}
        		
        		
        		mc.setZoom(18);
        		
        		flagoverlay.addGeoPoint(buffer_geopoint);
        		mapView.invalidate();
        		
        		
        		Log.i("Chwang","After check, flagoverlay.size()="+flagoverlay.size());
        		Log.i("Chwang","Total overlay size="+Integer.toString(overlay.size()));
    			
        		int numberOfFlags=flagoverlay.size();
        		
        		if(numberOfFlags>=4){
        			Location loc_first=new Location("First");
        			
            		loc_first.setLatitude((double)flagoverlay.getGeoPoint(0).getLatitudeE6()/1000000);
            		loc_first.setLongitude((double)flagoverlay.getGeoPoint(0).getLongitudeE6()/1000000);
            		
            		Location loc_last=new Location("Last");
            		loc_last.setLatitude((double)flagoverlay.getGeoPoint(numberOfFlags-1).getLatitudeE6()/1000000);
            		loc_last.setLongitude((double)flagoverlay.getGeoPoint(numberOfFlags-1).getLongitudeE6()/1000000);
            		
            		String dist=Float.toString(loc_first.distanceTo(loc_last));
            		
            		Log.i("chwang","Distance: "+dist);
            		
        			if(loc_first.distanceTo(loc_last)<=10){
            			//commit code
            			Log.i("chwang","Commit part");
            			commitDialog(mContext);
        			}
        		}
        		
        		
        	}//End of onClick
        }); //End of setOnClickListener
        
        
        //지도위에 올릴 Mission 버튼 임시로 만들기 
        bt_mission=new ImageButton(this);
        BitmapDrawable bitmapDrawable_mission=(BitmapDrawable)res.getDrawable(R.drawable.button_mission);
        Bitmap bitmap_mission=bitmapDrawable_mission.getBitmap();
        bitmap_mission=Bitmap.createScaledBitmap(bitmap_mission, 100, 100, false);
        bt_mission.setImageBitmap(bitmap_mission);
        bt_mission.setBackgroundColor(Color.TRANSPARENT);
        
        //버튼 레이아웃 파라미터
        MapView.LayoutParams bt_mission_layout=new MapView.LayoutParams(LayoutParams.WRAP_CONTENT, 
																	LayoutParams.WRAP_CONTENT, 
																	(int)(width*0.7), (int)(height*0.75), 
																	MapView.LayoutParams.CENTER);
        //레이아웃 파라미터와 함께 버튼을 맵뷰에 애드
        mapView.addView(bt_mission,bt_mission_layout);
        
        
        
        bt_mission.setOnClickListener(new OnClickListener(){
        	public void onClick(View v){
        		//Mission 버튼 터치시 실행될 코드
        		Log.i("Chwang","Mission Button clicked");
        		Intent i=new Intent(VirtualLandActivity.this,MissionActivity.class);
        		startActivity(i);
        		
        	}//End of onClick
        }); //End of setOnClickListener
        
    }//End-of-onCreate
    
    
    @Override
    public void onBackPressed(){
    	if(checkBackKey){
    		timeSecond=System.currentTimeMillis();
    		if(timeSecond-timeFirst<3000){
    			super.onBackPressed();
    		}else{
    			checkBackKey=false;
    		}
    	}
    	if(!checkBackKey){
    		timeFirst=System.currentTimeMillis();
    		Toast.makeText(getBaseContext(), "Press Back-key one more time to exit.", Toast.LENGTH_SHORT).show();
    		checkBackKey=true;
    	}
    }
    
    @Override
    protected boolean isRouteDisplayed(){
    	return false;
    }
    
    
    @Override
    public void onDestroy(){
    	super.onDestroy();
    	LocationManager lm=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
    	lm.removeUpdates(mLocationListener);
    }
    
    
    
    public void gpsDialog(Context mContext){
    	AlertDialog.Builder alertDlg=new AlertDialog.Builder(mContext);
		alertDlg.setTitle("GPS Status");
		alertDlg.setMessage("Without GPS, you cannot mark your exact position. Do you want to turn on the GPS?");
		
		alertDlg.setPositiveButton("YES", new DialogInterface.OnClickListener() {		
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent=new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				startActivity(intent);
			}
		});
		
		alertDlg.setNegativeButton("NO", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		
		alertDlg.show();
    }
    
    public void commitDialog(Context mContext){
    	AlertDialog.Builder alertDlg=new AlertDialog.Builder(mContext);
		alertDlg.setTitle("Commit");
		alertDlg.setMessage("Do you want to commit your polygon?");
		
		alertDlg.setPositiveButton("YES", new DialogInterface.OnClickListener() {		
			@Override
			public void onClick(DialogInterface dialog, int which) {
				sendVertices();
			}
		});
		
		alertDlg.setNegativeButton("NO", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}
		});
		
		alertDlg.show();
    }
    
    private boolean sendVertices(){
    	Log.i("Chwang","sendVertices() called");
    	
    	try{
        	URL url=new URL(SERVER_ADDR+"/vland/marking.php");
        	HttpURLConnection http=(HttpURLConnection)url.openConnection();
        	
        	http.setDefaultUseCaches(false);
        	http.setDoInput(true);
        	http.setDoOutput(true);
        	http.setRequestMethod("POST");
        	
        	http.setRequestProperty("content-type","application/x-www-form-urlencoded");
        	
        	StringBuffer buffer=new StringBuffer();
        	int vertices_size=flagoverlay.size();
        	GeoPoint tmpGeoPoint;
        	
        	for(int i=0; i<vertices_size; i++){
        		tmpGeoPoint=flagoverlay.getGeoPoint(i);
            	buffer.append("lat").append(i).append("=").append(tmpGeoPoint.getLatitudeE6()).append("&");
            	buffer.append("lon").append(i).append("=").append(tmpGeoPoint.getLongitudeE6()).append("&");
        	}
        	buffer.append("vertices_size").append("=").append(vertices_size);
        	
        	OutputStreamWriter outStream=new OutputStreamWriter(http.getOutputStream(),"UTF-8");
        	PrintWriter writer=new PrintWriter(outStream);
        	writer.write(buffer.toString());
        	writer.flush();
        	
        	InputStreamReader tmp=new InputStreamReader(http.getInputStream(),"UTF-8");
        	BufferedReader reader = new BufferedReader(tmp); 
            String response=reader.readLine();
            Log.i("Chwang","Server response: "+response);
        	
        	Toast.makeText(getBaseContext(), "영토 선포가 완료되었습니다!", Toast.LENGTH_SHORT).show();
        	
        	//flagoverlay 초기화
        	flagoverlay.removeAll();
        	
        	return true;
        	
        } catch(Exception e){
        	//Exception Occurred
        	Toast.makeText(getBaseContext(), "서버 혹은 네트워크 환경이 원활하지 못합니다", Toast.LENGTH_SHORT).show();
        	return false;
        }
    }
}//end of VirtualLandActivity Class