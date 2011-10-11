package edu.chalmers.aardvark.gui;

import java.util.List;

import edu.chalmers.aardvark.R;
import edu.chalmers.aardvark.ctrl.ChatCtrl;
import edu.chalmers.aardvark.ctrl.ContactCtrl;
import edu.chalmers.aardvark.ctrl.ServerHandlerCtrl;
import edu.chalmers.aardvark.ctrl.SystemCtrl;
import edu.chalmers.aardvark.ctrl.UserCtrl;
import edu.chalmers.aardvark.model.Chat;
import edu.chalmers.aardvark.model.ChatMessage;
import edu.chalmers.aardvark.model.User;
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
import android.widget.ScrollView;
import android.widget.TextView;
import android.view.View.OnClickListener;

public class ChatViewActivity extends Activity implements edu.chalmers.aardvark.util.EventListener{
    /** Called when the activity is first created. */
	private String aardvarkID;
	private String alias;
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.chatview);
	
	aardvarkID = getIntent().getExtras().getString("aardvarkID");
	alias = ServerHandlerCtrl.getInstance().getAlias(aardvarkID);
	
	Log.i("CLASS", this.toString() + " STARTED");
	
	ComBus.subscribe(this);
	
	TextView aliasLable = (TextView) findViewById(R.id.userNameLable);
	aliasLable.setText(alias);
	
	drawMessages();
	Button sendButton = (Button) findViewById(R.id.sendButton);
	sendButton.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			EditText messageField = (EditText) findViewById(R.id.intputText);
			String message = messageField.getText().toString();
			if(message.length()>0){
				Chat chat = ChatCtrl.getInstance().getChat(aardvarkID);
				ChatCtrl.getInstance().sendMessage(chat, message);
				messageField.setText("");
			}
		}
	});
	
    }
    private void drawMessages(){
    	LayoutInflater inflater = (LayoutInflater) this
    			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    		LinearLayout ll = (LinearLayout) this.findViewById(R.id.messageLayout);
    		ll.removeAllViews();
    	
    	try {
			List<ChatMessage> messages = ChatCtrl.getInstance().getChatMessages(aardvarkID);
			Log.i("INFO", "chat");
			for (ChatMessage chatMessage : messages) {
				Log.i("INFO", "chatloop");
				View item;
				if(chatMessage.getUser().getAardvarkID().equals(aardvarkID)){
					 item = inflater.inflate(R.layout.bubblerightpanel, null);
				}
				else{
					 item = inflater.inflate(R.layout.bubbleleftpanel, null);
				}
				TextView tx = (TextView) item.findViewById(R.id.message);
				    tx.setText(chatMessage.getMessage());
				    ll.addView(item, ViewGroup.LayoutParams.WRAP_CONTENT);
			}
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			
		}
    	ScrollView sv = (ScrollView) this.findViewById(R.id.scrollViewChat);
    	sv.smoothScrollTo(0, sv.getMaxScrollAmount());
    	
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	MenuInflater inflater = getMenuInflater();
	inflater.inflate(R.menu.chatmenu, menu);
	if(ContactCtrl.getInstance().isContact(aardvarkID)){
		MenuItem item = menu.getItem(0);
		item.setVisible(false);
	}
	return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	switch (item.getItemId()) {
        	case R.id.addToContacts: ContactCtrl.getInstance().addContact(alias, aardvarkID); break;
        	case R.id.block: UserCtrl.getInstance().blockUser(aardvarkID); break;
	}
	return true;
    }

	@Override
	public void notifyEvent(String stateChange, Object object) {
		if(stateChange.equals(StateChanges.NEW_MESSAGE_IN_CHAT.toString())){
			drawMessages();
		} else if(stateChange.equals(StateChanges.CONTACT_ADDED.toString())){
			contactAdded();
		} else if (stateChange.equals(StateChanges.USER_BLOCKED.toString())) {
			User blockedUser = (User) object;
			if (blockedUser.getAardvarkID().equals(aardvarkID)) {
			    setBlockedEnabled(false);
			}
		} else if (stateChange.equals(StateChanges.USER_UNBLOCKED.toString())) {
			User blockedUser = (User) object;
			if (blockedUser.getAardvarkID().equals(aardvarkID)) {
			    setBlockedEnabled(false);
			}
		}
		
	}

	private void contactAdded() {
		// TODO Auto-generated method stub
		
	}

	private void update() {
		// TODO Auto-generated method stub
		
	}
	
	private void setBlockedEnabled(boolean enabled) {
	    	Button sendButton = (Button) findViewById(R.id.sendButton);
		sendButton.setEnabled(enabled);
	}
}
