package com.example.ipark;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends Activity {
	
	private EditText username;
	private EditText passwd;
	private Button signin;	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);
		username = (EditText)findViewById(R.id.user);
		passwd = (EditText)findViewById(R.id.passwd);
		signin = (Button)findViewById(R.id.signin);
		signin.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String user = username.getText().toString().trim();
				String pass = passwd.getText().toString().trim();
				// TODO 验证账户名与密码
				
				Intent i = new Intent(LoginActivity.this,MainActivity.class);
				i.putExtra("user",user);
				startActivity(i);
				finish();
			}
			
		});
	}
}
