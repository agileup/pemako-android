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
	
	final String SERVER_ADDR = "http://pemako.iptime.org";
	
	LocationListener mLocationListener;
	MapView mapView;
	MapController mc;
	List<Overlay> overlay;
	TextView status=null; //���� ȭ�� ��� �����浵 ��Ÿ���� �ؽ�Ʈ��
	//ArrayList<GeoPoint> vertices=new ArrayList<GeoPoint>(); //����ڰ� ������ �׸��� ���� üũ�ϴ� ��������
	Polygon myPolygon=null;
	ImageButton bt_check;
	ImageButton bt_mission;
	Context mContext;
	
	GeoPoint buffer_geopoint=null; //üũ�Ҷ� ��ġ�� ���������� �����̼� �����ʿ��� �̸� ������ �ϴµ�
	OverlayItem buffer_overlayitem=null;
	Boolean isFirst=false;
	Boolean isFirst_gpsDialog=true;
	
	//onBackPressed() ���� �ι� ��ġ �� ���Ḧ ���� ������ ������
	private boolean checkBackKey=false;
    private long timeFirst;
    private long timeSecond;
    
    
    //������ġ ǥ�� MyItemizedOverlay ���� �� �ʱ�ȭ
    
	CurrentLocationItemizedOverlay currentIO;
	ArrayList<Tile> tileOverlays=new ArrayList<Tile>();
	//FlagItemizedOverlay flagIO;
	FlagOverlay flagoverlay;
	
	Drawable drawable_redflag;
	Drawable drawable_blueflag;
	
	int checkCount=0;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        mContext=this;
		
        //��ǥ ǥ���ϴ� �ӽ� �ؽ�Ʈ��
        status=new TextView(this);
        
        //�ʺ� ���� �� ����
        mapView=(MapView)findViewById(R.id.mapview);
        mapView.setBuiltInZoomControls(true);
        mapView.setSatellite(false);
        //mapView.setBuiltInZoomControls(false);
             
        //���� �ٷ�� ���� �ʺ� ��Ʈ�ѷ�
        mc=mapView.getController();
        
        //KAIST CS�� ���� �̵�
        GeoPoint kaistCS=new GeoPoint(36368455,127365285);
        mc.animateTo(kaistCS);
        mc.setZoom(18);
        
		
        //������ġ �������� ��Ʈ�� ���� �� CurrentLocationItemizedOverlay ���� currentIO �ʱ�ȭ
        Bitmap bitmap_current=BitmapFactory.decodeResource(getResources(), R.drawable.current_gp);
		bitmap_current=Bitmap.createScaledBitmap(bitmap_current, 15, 15, false);
		Drawable drawable_current=new BitmapDrawable(bitmap_current);
		currentIO=new CurrentLocationItemizedOverlay(drawable_current);
		
		//�÷��� ��������(check�� ǥ���ϴ� �÷���) ��Ʈ�� ���� �� FlagItemizedOverlay ���� flagIO �ʱ�ȭ
		Bitmap bitmap_redflag=BitmapFactory.decodeResource(getResources(), R.drawable.check_redflag);
		bitmap_redflag=Bitmap.createScaledBitmap(bitmap_redflag, 50, 50, false);
		drawable_redflag=new BitmapDrawable(bitmap_redflag);
		drawable_redflag.setBounds(-drawable_redflag.getIntrinsicWidth()/2,-drawable_redflag.getIntrinsicHeight(),drawable_redflag.getIntrinsicWidth()/2,0);  
		
		
		Bitmap bitmap_blueflag=BitmapFactory.decodeResource(getResources(), R.drawable.check_blueflag);
		bitmap_blueflag=Bitmap.createScaledBitmap(bitmap_blueflag, 50, 50, false);
		drawable_blueflag=new BitmapDrawable(bitmap_blueflag);
		drawable_blueflag.setBounds(-drawable_blueflag.getIntrinsicWidth()/2,-drawable_blueflag.getIntrinsicHeight(),drawable_blueflag.getIntrinsicWidth()/2,0);
		
		//flagIO=new FlagItemizedOverlay(drawable_blueflag,mContext,mapView);
		flagoverlay=new FlagOverlay(drawable_redflag,drawable_blueflag,mapView,mContext);
		
        overlay=mapView.getOverlays();
        overlay.add(currentIO);
        //overlay.add(flagIO);
        overlay.add(flagoverlay);
        
        //LocationManager ���� �� �ʱ�ȭ
        LocationManager lm=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
        
        //LocationListener ����
        mLocationListener=new LocationListener(){
        	public void onLocationChanged(Location location){
        		if(location!=null){
        			String provider=location.getProvider();
        			
        			Toast.makeText(getBaseContext(), 
        					"������ "+location.getLatitude()+", �浵�� "+location.getLongitude()+" �Դϴ�.", 
        					Toast.LENGTH_SHORT).show();
        			//buffer_geopoint �ʱ�ȭ �� ���� �Ҵ�
        			buffer_geopoint=new GeoPoint((int)(location.getLatitude()*1E6),(int)(location.getLongitude()*1E6));
        			
        			
        			
        			//GPS ���� �ѷ��ִ� ��� �ؽ�Ʈ �޽��� ���� �� �ֵ�
        			mapView.removeView(status);
        			status.setText("Lat: "+location.getLatitude()+"\nLong: "+location.getLongitude()+"\nProvider: "+provider);
        			status.setTextSize(15);
        			status.setTextColor(Color.BLACK);
        			MapView.LayoutParams lp=new MapView.LayoutParams(LayoutParams.WRAP_CONTENT, 
        															LayoutParams.WRAP_CONTENT, 10, 10, 
        															MapView.LayoutParams.LEFT | MapView.LayoutParams.TOP);
        			
        			mapView.addView(status,lp);
        			
        			
        			
        			GeoPoint gp=new GeoPoint((int)(location.getLatitude()*1000000),(int)(location.getLongitude()*1000000));
        			
        			mc.animateTo(gp);
        			mc.setZoom(18);
        			
        			
        			OverlayItem overlayitem=new OverlayItem(gp,"","");
        			currentIO.addOverlayItem(overlayitem);
        			
        			if(buffer_overlayitem!=null) currentIO.removeOverlayItem(buffer_overlayitem);
        			
        			
        			//overlay=mapView.getOverlays();
        			//overlay.add(currentIO); //���ο� ��ġ ǥ�� �׸��� 
        			//���ο� ��ġ ǥ�� �׸��� OverlayItem ������Ʈ�� ���������� �����ص� - ���߿� ���� ����
        			buffer_overlayitem=overlayitem;
        			
        			//���� ��� �׽�Ʈ
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
        		             
        		             int [][] tileInfo = new int [48][30];
        		             
        		             Tile tmpTiles[] = new Tile [1440];
        		             
        		             for(int i=0; i<48; i++){
        		            	 targetLon=Integer.parseInt(myStarts[1]);
        		            	 for(int j=0; j<30; j++){
        		            		 tileInfo[i][j]=Integer.parseInt(myTiles[i*30+j]);
        		            		 GeoPoint center=new GeoPoint(targetLat,targetLon);
        		            		 
        		            		 tmpTiles[i*30+j]=new Tile(center,deltaLat,deltaLon,colorSelector(tileInfo[i][j]));
        		            		 
        		            		 tileOverlays.add(tmpTiles[i*30+j]);
        		            		 
        		            		 targetLon+=deltaLon;
        		            	 }
        		            	 targetLat-=deltaLat;
        		             }
        		             
        		             overlay.addAll(tileOverlays);
        		             
        		             isFirst=false;
        		        }catch(Exception e){
        		        	Toast.makeText(getBaseContext(), "���� Ȥ�� ��Ʈ��ũ ȯ���� ��Ȱ���� ���մϴ�", Toast.LENGTH_SHORT).show();
        		        	Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
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
        
        
        
        //����̽� ȭ�� �ػ� ���
        Display display = getWindowManager().getDefaultDisplay();  
        int width = display.getWidth(); 
        int height = display.getHeight(); 
        
        //�������� �ø� CHECK ��ư ����� 
        bt_check=new ImageButton(this);
        Resources res=getResources();
        BitmapDrawable bitmapDrawable_check=(BitmapDrawable)res.getDrawable(R.drawable.button_check);
        Bitmap bitmap_check=bitmapDrawable_check.getBitmap();
        bitmap_check=Bitmap.createScaledBitmap(bitmap_check, 100, 100, false);
        bt_check.setImageBitmap(bitmap_check);
        bt_check.setBackgroundColor(Color.TRANSPARENT);
        
        
        //��ư ���̾ƿ� �Ķ����
        MapView.LayoutParams bt_check_layout=new MapView.LayoutParams(LayoutParams.WRAP_CONTENT, 
																	LayoutParams.WRAP_CONTENT, 
																	(int)(width*0.3), (int)(height*0.75), 
																	MapView.LayoutParams.CENTER);
        //���̾ƿ� �Ķ���Ϳ� �Բ� ��ư�� �ʺ信 �ֵ�
        mapView.addView(bt_check,bt_check_layout);
    	
        
        
        bt_check.setOnClickListener(new OnClickListener(){
        	public void onClick(View v){
        		//Check ��ư ��ġ�� ����� �ڵ�
        		
        		
        		if(buffer_geopoint==null){
        			Log.i("Chwang","buffer_geopoint is null in setOnClickListener");
        		}
        		
        		
        		mc.setZoom(18);
        		
        		
        		//For Test
        		//flagIO.mOverlays.
        		
        		
        		
        		//overlay=mapView.getOverlays();
        		//overlay.remove(flagIO);
        		//Newly Added 20111216
        		/*
        		flagIO.addOverlayItem(overlayitem);
        		flagIO.mPolyLine.geoPoints.add(buffer_geopoint);
        		*/
        		
        		flagoverlay.addGeoPoint(buffer_geopoint);
        		
        		Log.i("Chwang","After check, flagoverlay.size()="+flagoverlay.size());
        		
        		
        		//overlay=mapView.getOverlays();
        		//overlay.add(flagIO);
        		
        		
        		mapView.invalidate();
    			
    			Log.i("Chwang",Integer.toString(overlay.size()));
    			
    			
    			
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
        
        
      //�������� �ø� Mission ��ư �ӽ÷� ����� 
        bt_mission=new ImageButton(this);
        BitmapDrawable bitmapDrawable_mission=(BitmapDrawable)res.getDrawable(R.drawable.button_mission);
        Bitmap bitmap_mission=bitmapDrawable_mission.getBitmap();
        bitmap_mission=Bitmap.createScaledBitmap(bitmap_mission, 100, 100, false);
        bt_mission.setImageBitmap(bitmap_mission);
        bt_mission.setBackgroundColor(Color.TRANSPARENT);
        
        //��ư ���̾ƿ� �Ķ����
        MapView.LayoutParams bt_mission_layout=new MapView.LayoutParams(LayoutParams.WRAP_CONTENT, 
																	LayoutParams.WRAP_CONTENT, 
																	(int)(width*0.7), (int)(height*0.75), 
																	MapView.LayoutParams.CENTER);
        //���̾ƿ� �Ķ���Ϳ� �Բ� ��ư�� �ʺ信 �ֵ�
        mapView.addView(bt_mission,bt_mission_layout);
        
        
        
        bt_mission.setOnClickListener(new OnClickListener(){
        	public void onClick(View v){
        		//Mission ��ư ��ġ�� ����� �ڵ�
        		Log.i("Chwang","Mission Button clicked");
        		Intent i=new Intent(VirtualLandActivity.this,MissionActivity.class);
        		startActivity(i);
        		
        	}//End of onClick
        }); //End of setOnClickListener
        
        
        
        
       //�������� �ø� COMMIT ��ư �ӽ÷� �����
        /*
        bt_commit=new Button(this);
        bt_commit.setText("COMMIT");
        bt_commit.setTextSize(15);
        bt_commit.setTextColor(Color.BLACK);
                
        //��ư ���̾ƿ� �Ķ����
        MapView.LayoutParams bt_commit_layout=new MapView.LayoutParams(150, 
																	LayoutParams.WRAP_CONTENT, (int)(width*0.7), (int)(height*0.8), 
																	MapView.LayoutParams.CENTER);
        //���̾ƿ� �Ķ���Ϳ� �Բ� ��ư�� �ʺ信 �ֵ�
        mapView.addView(bt_commit,bt_commit_layout);
    	
        bt_commit.setOnClickListener(new OnClickListener(){
        	public void onClick(View v){
        		//Commit ��ư ��ġ�� ����� �ڵ�
        		myPolygon=new Polygon(vertices);
        		overlay=mapView.getOverlays();
                overlay.add(myPolygon);
                mapView.invalidate();
                
                try{
                	URL url=new URL("http://chwang.iptime.org/vland/marking.php");
                	HttpURLConnection http=(HttpURLConnection)url.openConnection();
                	
                	http.setDefaultUseCaches(false);
                	http.setDoInput(true);
                	http.setDoOutput(true);
                	http.setRequestMethod("POST");
                	
                	http.setRequestProperty("content-type","application/x-www-form-urlencoded");
                	
                	StringBuffer buffer=new StringBuffer();
                	int vertices_size=vertices.size();
                	
                	for(int i=0; i<vertices_size; i++){
	                	buffer.append("lat").append(i).append("=").append(vertices.get(i).getLatitudeE6()).append("&");
	                	buffer.append("lon").append(i).append("=").append(vertices.get(i).getLongitudeE6()).append("&");
                	}
                	buffer.append("vertices_size").append("=").append(vertices_size);
                	
                	OutputStreamWriter outStream=new OutputStreamWriter(http.getOutputStream(),"UTF-8");
                	PrintWriter writer=new PrintWriter(outStream);
                	writer.write(buffer.toString());
                	writer.flush();
                	
                	//InputStreamReader tmp=new InputStreamReader(http.getInputStream(),"UTF-8");
                	
                	
                } catch(Exception e){
                	//Exception Occurred
                	Toast.makeText(getBaseContext(), "���� Ȥ�� ��Ʈ��ũ ȯ���� ��Ȱ���� ���մϴ�", Toast.LENGTH_SHORT).show();
                }
                
                
        	}
        }); //end of button_commit.setOnClickListener()
        */
        
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
    		Toast.makeText(getBaseContext(), "Baby One more time~��", Toast.LENGTH_SHORT).show();
    		checkBackKey=true;
    	}
    }
    
    @Override
    protected boolean isRouteDisplayed(){
    	return false;
    }
    
    
    
    @Override
    public void onStart(){
    	super.onStart();
    	
    }
    
    
    
    @Override
    public void onDestroy(){
    	super.onDestroy();
    	LocationManager lm=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
    	lm.removeUpdates(mLocationListener);
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
        	
        	//InputStreamReader tmp=new InputStreamReader(http.getInputStream(),"UTF-8");
        	
        	
        	
        	//flagoverlay �ʱ�ȭ
        	flagoverlay.removeAll();
        	
        	return true;
        	
        } catch(Exception e){
        	//Exception Occurred
        	Toast.makeText(getBaseContext(), "���� Ȥ�� ��Ʈ��ũ ȯ���� ��Ȱ���� ���մϴ�", Toast.LENGTH_SHORT).show();
        	return false;
        }
    }
}//end of VirtualLandActivity Class