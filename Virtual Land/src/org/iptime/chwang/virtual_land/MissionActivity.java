package org.iptime.chwang.virtual_land;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MissionActivity extends Activity{
	
	private ArrayList<CustomListData> Array_Data;
	private CustomListData data;
	private CustomListAdapter adapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mission);
	
		Array_Data=new ArrayList<CustomListData>();
		
		data=new CustomListData(R.drawable.icon,"ù��° �̼��Դϴ�","ù��° �̼� ���� ����");
		Array_Data.add(data);
		
		data=new CustomListData(R.drawable.check_redflag,"�ι�° �̼��Դϴ�","�ι�° �̼� ���� ����");
		Array_Data.add(data);
		
		data=new CustomListData(R.drawable.check_blueflag,"����° �̼��Դϴ�","����° �̼� ���� ����");
		Array_Data.add(data);
		
		data=new CustomListData(R.drawable.icon,"�׹�° �̼��Դϴ�","�׹�° �̼� ���� ����");
		Array_Data.add(data);
		
		data=new CustomListData(R.drawable.check_redflag,"�ټ���° �̼��Դϴ�","�ټ���° �̼� ���� ����");
		Array_Data.add(data);
		
		data=new CustomListData(R.drawable.check_blueflag,"������° �̼��Դϴ�","������° �̼� ���� ����");
		Array_Data.add(data);
		
		data=new CustomListData(R.drawable.icon,"�ϰ���° �̼��Դϴ�","�ϰ���° �̼� ���� ����");
		Array_Data.add(data);
		
		data=new CustomListData(R.drawable.check_redflag,"������° �̼��Դϴ�","������° �̼� ���� ����");
		Array_Data.add(data);
		
		data=new CustomListData(R.drawable.check_blueflag,"��ȩ��° �̼��Դϴ�","��ȩ��° �̼� ���� ����");
		Array_Data.add(data);
		
		ListView custom_list=(ListView)findViewById(R.id.Custom_List);
		adapter=new CustomListAdapter(this,android.R.layout.simple_list_item_1,Array_Data);
		custom_list.setAdapter(adapter);
		
		custom_list.setOnItemClickListener(new infoListSelection(this));
	}//end of onCreate()
	
}

class infoListSelection implements OnItemClickListener{
	Context mContext;
	
	public infoListSelection(Context mContext){
		this.mContext=mContext;
	}
	
    public void onItemClick(AdapterView<?> parent, View view, int position,    long id) {
        Toast.makeText(mContext, Integer.toString(position), 1).show();
    }
}


