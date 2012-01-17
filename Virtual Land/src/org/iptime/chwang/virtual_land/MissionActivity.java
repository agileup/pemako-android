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
		
		data=new CustomListData(R.drawable.icon,"첫번째 미션입니다","첫번째 미션 간략 설명");
		Array_Data.add(data);
		
		data=new CustomListData(R.drawable.check_redflag,"두번째 미션입니다","두번째 미션 간략 설명");
		Array_Data.add(data);
		
		data=new CustomListData(R.drawable.check_blueflag,"세번째 미션입니다","세번째 미션 간략 설명");
		Array_Data.add(data);
		
		data=new CustomListData(R.drawable.icon,"네번째 미션입니다","네번째 미션 간략 설명");
		Array_Data.add(data);
		
		data=new CustomListData(R.drawable.check_redflag,"다섯번째 미션입니다","다섯번째 미션 간략 설명");
		Array_Data.add(data);
		
		data=new CustomListData(R.drawable.check_blueflag,"여섯번째 미션입니다","여섯번째 미션 간략 설명");
		Array_Data.add(data);
		
		data=new CustomListData(R.drawable.icon,"일곱번째 미션입니다","일곱번째 미션 간략 설명");
		Array_Data.add(data);
		
		data=new CustomListData(R.drawable.check_redflag,"여덟번째 미션입니다","여덟번째 미션 간략 설명");
		Array_Data.add(data);
		
		data=new CustomListData(R.drawable.check_blueflag,"아홉번째 미션입니다","아홉번째 미션 간략 설명");
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


