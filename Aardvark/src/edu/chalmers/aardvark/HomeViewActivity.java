package edu.chalmers.aardvark;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


public class HomeViewActivity extends Activity {
	 /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout ll = (LinearLayout) this.findViewById(R.id.linearLayout1);

        for(int i = 1; i< 15; i++) {        
            View item = inflater.inflate(R.layout.main, null);
            ll.addView(item,ViewGroup.LayoutParams.WRAP_CONTENT);
        } 
    }
    

}
