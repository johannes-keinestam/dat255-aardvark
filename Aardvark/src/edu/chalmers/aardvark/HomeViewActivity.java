package edu.chalmers.aardvark;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


public class HomeViewActivity extends Activity {
	 /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainview);
        
        final LayoutInflater inflater3 = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final LinearLayout ll3 = (LinearLayout) this.findViewById(R.id.searchLayout);
        
        final View searchView = inflater3.inflate(R.layout.searchuserview, null);
        ll3.addView(searchView,ViewGroup.LayoutParams.WRAP_CONTENT);
        searchView.setVisibility(View.GONE);
        
        final LayoutInflater inflaterResult = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final LinearLayout layoutResult = (LinearLayout) this.findViewById(R.id.resultLayout);
        
        View result = inflaterResult.inflate(R.layout.contactpanel, null);
        layoutResult.addView(result,ViewGroup.LayoutParams.WRAP_CONTENT);
        
        Button sb = (Button)  this.findViewById(R.id.searchuserviewbutton);
        sb.setOnClickListener(new OnClickListener() {
			boolean on = false;
			public void onClick(View v) {
				
				if(on){
					searchView.setVisibility(View.GONE);
					on = false;
				}
				else{
					searchView.setVisibility(View.VISIBLE);
			        on = true;
				}
				
			}
		});
        
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout ll = (LinearLayout) this.findViewById(R.id.linearLayout2);

        for(int i = 1; i< 5; i++) {        
            View item = inflater.inflate(R.layout.contactpanel, null);
            final TextView tx = (TextView) item.findViewById(R.id.contactName);
            tx.setText("Kalle"+i);
            final int j = i;
            tx.setOnClickListener(new OnClickListener() {
				
				public void onClick(View v) {
					tx.setText("pressed"+j);
					
				}
			});
           ll.addView(item,ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        LayoutInflater inflater2 = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout ll2 = (LinearLayout) this.findViewById(R.id.linearLayout3);

        for(int i = 1; i< 5; i++) {        
            View item = inflater2.inflate(R.layout.contactpanel, null);
            final TextView tx = (TextView) item.findViewById(R.id.contactName);
            tx.setText("Sven"+i);
            final int j = i;
            tx.setOnClickListener(new OnClickListener() {
				
				public void onClick(View v) {
					tx.setText("pressed"+j);
					
				}
			});
           ll2.addView(item,ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }
    

}
