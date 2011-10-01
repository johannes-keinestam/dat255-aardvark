package edu.chalmers.aardvark.gui;

import edu.chalmers.aardvark.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class LoginViewActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Button loginButton = (Button) this.findViewById(R.id.loginButton);
        final Intent intent = new Intent(this, MainViewActivity.class);
        
        loginButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
			      startActivity(intent);	
			}
		});
       	
    }
}