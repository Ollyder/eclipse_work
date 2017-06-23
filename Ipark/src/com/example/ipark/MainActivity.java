package com.example.ipark;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

public class MainActivity extends Activity {
	private TextView login_name;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		login_name=(TextView)findViewById(R.id.login_name);
		Intent i = getIntent();
		String user = i.getStringExtra("user");
		if(user==null) user="";
		login_name.setText((CharSequence)user);
	}

}
