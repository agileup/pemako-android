package org.iptime.chwang.virtual_land;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MissionActivity extends Activity {
	
	private String[] missions={"Taking Photos","Grapping flags", "String matching"};
	private ListView list;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mission);

		list=(ListView)findViewById(R.id.mission_list);
		list.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,missions));
		
	}
}
