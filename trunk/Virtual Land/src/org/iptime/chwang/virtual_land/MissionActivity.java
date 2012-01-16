package org.iptime.chwang.virtual_land;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MissionActivity extends Activity {
	
	private ArrayList<Custom_List_Data> Array_Data;
	private Custom_List_Data data;
	private CustomListAdapter adapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mission);

		Log.i("Chwang","Before Array_Data initialized");
		
		Array_Data=new ArrayList<Custom_List_Data>();
		
		Log.i("Chwang","After Array_Data initialized");
		
		data=new Custom_List_Data(R.drawable.icon,"첫번째 미션입니다","첫번째 미션 간략 설명");
		Array_Data.add(data);
		
		data=new Custom_List_Data(R.drawable.check_redflag,"두번째 미션입니다","두번째 미션 간략 설명");
		Array_Data.add(data);
		
		Log.i("Chwang","After add datas");
		
		ListView custom_list=(ListView)findViewById(R.id.Custom_List);
		Log.i("Chwang","After custom_list set");
		adapter=new CustomListAdapter(this,android.R.layout.simple_list_item_1,Array_Data);
		Log.i("Chwang","After adapter initialized");
		custom_list.setAdapter(adapter);
		Log.i("Chwang","After setAdapter() called");
		
	}
}
