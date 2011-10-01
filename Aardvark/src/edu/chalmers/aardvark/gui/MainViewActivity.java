package edu.chalmers.aardvark.gui;

import edu.chalmers.aardvark.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainViewActivity extends Activity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mainview);

		Button sb = (Button) this.findViewById(R.id.startChatButton);
		sb.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

			}
		});

		drawOnlineContacts();
		drawOfflineContacts();

	}

	private void drawOnlineContacts() {
		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout ll = (LinearLayout) this.findViewById(R.id.online);
		final Intent intent = new Intent(this, ChatViewActivity.class);

		for (int i = 1; i < 5; i++) {
			View item = inflater.inflate(R.layout.contactpanel, null);
			final TextView tx = (TextView) item.findViewById(R.id.contactName);
			tx.setText("Kalle" + i);
			final int j = i;
			tx.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					tx.setText("pressed" + j);
					startActivity(intent);

				}
			});
			ll.addView(item, ViewGroup.LayoutParams.WRAP_CONTENT);
		}
	}

	private void drawOfflineContacts() {

		LinearLayout ll = (LinearLayout) this.findViewById(R.id.offline);
		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		for (int i = 1; i < 5; i++) {
			View item = inflater.inflate(R.layout.contactpanel, null);
			final TextView tx = (TextView) item.findViewById(R.id.contactName);
			tx.setText("Sven" + i);
			final int j = i;
			tx.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					tx.setText("pressed" + j);

				}
			});
			ll.addView(item, ViewGroup.LayoutParams.WRAP_CONTENT);
		}

	}
}
