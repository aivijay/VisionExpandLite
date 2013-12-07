package com.bcsoft.estx.visionexpand;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ArrayAdapter;

public class DrillBActivity extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_drill_b);

		// storing string resources into Array
		String[] drillBData = getResources().getStringArray(R.array.drillBData);

		// Binding resources Array to ListAdapter
		this.setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item,
				R.id.list_item, drillBData));
		this.getListView().setDivider(null);

		// Remove notification bar
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}
}
