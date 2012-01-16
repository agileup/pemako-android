package org.iptime.chwang.virtual_land;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomListAdapter extends ArrayAdapter<Custom_List_Data>{
	private ArrayList<Custom_List_Data> items;
	
	public CustomListAdapter(Context context, int textViewResourceId, ArrayList<Custom_List_Data> items){
		super(context,textViewResourceId, items);
		this.items=items;
	}
	
	
	public View getView(int position, View convertView, ViewGroup parent){
		View v=convertView;
		if(v==null){
			LayoutInflater vi=(LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v=vi.inflate(R.layout.custom_list,null);
		}
		Custom_List_Data custom_list_data=items.get(position);
		
		if(custom_list_data!=null){
			//�ϳ��� �̹������ 2���� �ؽ�Ʈ�� ������ �޾ƿ´�
			ImageView iv=(ImageView)v.findViewById(R.id.custom_list_image);
			TextView tv_Main=(TextView)v.findViewById(R.id.custom_list_title_main);
			TextView tv_Sub=(TextView)v.findViewById(R.id.custom_list_title_sub);
			
			//���� item�� position�� �´� �̹����� ���� �־��ش�
			iv.setBackgroundResource(custom_list_data.getImage_ID());
			tv_Main.setText(custom_list_data.getMain_Title());
			tv_Sub.setText(custom_list_data.getSub_Title());
		}
		
		return v;
	}
}

class Custom_List_Data {
	private int imageID;
	private String mainTitle;
	private String subTitle;
	
	public Custom_List_Data(int _imageID, String _mainTitle, String _subTitle){
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