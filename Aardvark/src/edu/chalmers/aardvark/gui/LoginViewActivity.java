package edu.chalmers.aardvark.gui;

import edu.chalmers.aardvark.HomeViewActivity;
import edu.chalmers.aardvark.R;
import edu.chalmers.aardvark.R.layout;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class LoginViewActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Intent intent = new Intent(this, HomeViewActivity.class);
        startActivity(intent);	
    }
}