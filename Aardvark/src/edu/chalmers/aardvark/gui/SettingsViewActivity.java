package edu.chalmers.aardvark.gui;

import edu.chalmers.aardvark.R;
import android.app.ListActivity;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.view.*;

public class SettingsViewActivity extends ListActivity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	  super.onCreate(savedInstanceState);
	  
	  String[] settings = getResources().getStringArray(R.array.settings);

	  setListAdapter(new ArrayAdapter<String>(this, R.layout.settingsitem, settings));

	  ListView lv = getListView();
	  lv.setTextFilterEnabled(true);

	  lv.setOnItemClickListener(new OnItemClickListener() {
	    public void onItemClick(AdapterView<?> parent, View view,
	        int position, long id) {
	    }
	  });
	}
}
