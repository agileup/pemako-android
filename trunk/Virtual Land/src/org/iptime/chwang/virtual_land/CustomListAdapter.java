package org.iptime.chwang.virtual_land;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomListAdapter extends ArrayAdapter<CustomListData>{
	private ArrayList<CustomListData> items;
	private Context mContext;
	
	public CustomListAdapter(Context context, int textViewResourceId, ArrayList<CustomListData> items){
		super(context,textViewResourceId, items);
		this.items=items;
		mContext=context;
	}
	
	
	public View getView(int position, View convertView, ViewGroup parent){
		View v=convertView;
		if(v==null){
			LayoutInflater vi=(LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v=vi.inflate(R.layout.custom_list,null);
		}
		CustomListData myCustomListData=items.get(position);
		
		if(myCustomListData!=null){
			//하나의 이미지뷰와 2개의 텍스트뷰 정보를 받아온다
			ImageView iv=(ImageView)v.findViewById(R.id.custom_list_image);
			TextView tv_Main=(TextView)v.findViewById(R.id.custom_list_title_main);
			TextView tv_Sub=(TextView)v.findViewById(R.id.custom_list_title_sub);
			
			//현재 item의 position에 맞는 이미지와 글을 넣어준다
			//iv.setBackgroundResource(myCustomListData.getImage_ID());
			
			Bitmap myBitmap=BitmapFactory.decodeResource(mContext.getResources(),myCustomListData.getImage_ID());
			myBitmap=Bitmap.createScaledBitmap(myBitmap,150,150,false);
			Drawable myDrawable=new BitmapDrawable(myBitmap);
			iv.setBackgroundDrawable(myDrawable);
			
			tv_Main.setText(myCustomListData.getMain_Title());
			tv_Sub.setText(myCustomListData.getSub_Title());
		}
		
		return v;
	}
}

class CustomListData {
	private int imageID;
	private String mainTitle;
	private String subTitle;
	 
	
	/*
	 * Bitmap bitmap_current=BitmapFactory.decodeResource(getResources(), R.drawable.current_gp);
		bitmap_current=Bitmap.createScaledBitmap(bitmap_current, 15, 15, false);
		Drawable drawable_current=new BitmapDrawable(bitmap_current);
		currentIO=new CurrentLocationItemizedOverlay(drawable_current);
		
	 */
	public CustomListData(int _imageID, String _mainTitle, String _subTitle){
		this.setImage_ID(_imageID);
		this.setMain_Title(_mainTitle);
		this.setSub_Title(_subTitle);
	}
	
	public int getImage_ID(){
		return imageID;
	}
	
	public void setImage_ID(int imageID){
		this.imageID=imageID;
	}
	
	public String getMain_Title(){
		return mainTitle;
	}
	
	public void setMain_Title(String mainTitle){
		this.mainTitle=mainTitle;
	}
	
	public String getSub_Title(){
		return subTitle;
	}
	
	public void setSub_Title(String subTitle){
		this.subTitle=subTitle;
	}
}