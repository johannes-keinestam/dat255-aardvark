package edu.chalmers.aardvark.gui;

import java.util.List;

import edu.chalmers.aardvark.R;
import edu.chalmers.aardvark.ctrl.ChatCtrl;
import edu.chalmers.aardvark.model.Chat;
import edu.chalmers.aardvark.model.ChatMessage;
import edu.chalmers.aardvark.util.ComBus;
import edu.chalmers.aardvark.util.StateChanges;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.View.OnClickListener;

public class ChatViewActivity extends Activity implements edu.chalmers.aardvark.util.EventListener{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.chatview);
	
	Log.i("INFO", this.toString() + " STARTED");
	
	ComBus.subscribe(this);
	
	TextView aliasLable = (TextView) findViewById(R.id.userNameLable);
	aliasLable.setText(getIntent().getExtras().getString("aardwarkId"));
	drawMessages();
	Button sendButton = (Button) findViewById(R.id.sendButton);
	sendButton.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			EditText messageField = (EditText) findViewById(R.id.intputText);
			String message = messageField.getText().toString();
			if(message.length()>0){
				Chat chat = ChatCtrl.getInstance().getChat(getIntent().getExtras().getString("aardwarkId"));
				ChatCtrl.getInstance().sendMessage(chat, message);
			}
		}
	});
	
    }
    private void drawMessages(){
    	LayoutInflater inflater = (LayoutInflater) this
    			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    		LinearLayout ll = (LinearLayout) this.findViewById(R.id.messageLayout);
    		ll.removeAllViews();
    	
//    	List<ChatMessage> messages = ChatCtrl.getInstance().getChatMessages((getIntent().getExtras().getString("aardwarkId")));
//    	for (ChatMessage chatMessage : messages) {
//			
//			 View item = inflater.inflate(R.layout.bubbleleftpanel, null);
//			 TextView tx = (TextView) item.findViewById(R.id.message);
//			    tx.setText(chatMessage.getMessage());
//			    ll.addView(item, ViewGroup.LayoutParams.WRAP_CONTENT);
//		}
    	
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	MenuInflater inflater = getMenuInflater();
	inflater.inflate(R.menu.chatmenu, menu);
	return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	switch (item.getItemId()) {
	case R.id.addToContacts:
	case R.id.block:

	}
	return true;
    }

	@Override
	public void notifyEvent(String stateChange, Object object) {
		if(stateChange.equals(StateChanges.NEW_MESSAGE_IN_CHAT.toString())){
			drawMessages();
		}
		else if(stateChange.equals(StateChanges.CONTACT_ADDED.toString())){
			contactAdded();
		}
		
	}

	private void contactAdded() {
		// TODO Auto-generated method stub
		
	}

	private void update() {
		// TODO Auto-generated method stub
		
	}
}
