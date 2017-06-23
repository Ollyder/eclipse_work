package com.example.wifi_suncase_2;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

public class MainActivity extends Activity {
	
	private EditText ip_et;
	private EditText port_et;
	
	private ToggleButton connect_tb;
	
	private TextView error_tv;
	private TextView sun_tv;
	
	private Context context;
	
	private ConnectTask connect;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO do a little change
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		bindView();
		
		initdata();
		
		startwork();
	}
	
	private void bindView() {
		ip_et = (EditText)findViewById(R.id.ip_et);
		port_et = (EditText)findViewById(R.id.port_et);
		connect_tb = (ToggleButton)findViewById(R.id.connect_tb);
		error_tv = (TextView)findViewById(R.id.error_tv);
		sun_tv = (TextView)findViewById(R.id.sun_tv);
		context = this;
	}
	
	private void initdata() {
		error_tv.setText("请点击连接");
		ip_et.setText(Constant.IP);
		port_et.setText(Constant.Port);
	}
	
	private void startwork() {
		connect_tb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked) {
					String IP = ip_et.getText().toString().trim();
					String Port = port_et.getText().toString().trim();
					if(isok(IP,Port)) {
						Constant.IP = IP;
						Constant.Port = Port;
					}
					connect = new ConnectTask(context,error_tv,sun_tv);
					connect.setCYCLE(true);
					connect.execute();
					
				} else {
					cancelConnect();
					error_tv.setTextColor(context.getResources().getColor(R.color.gray));
					error_tv.setText("请点击连接");
				}
			}
		});
	}
	
	private boolean isok(String ip, String port) {

		boolean isIP = false;
		boolean isPort = false;
		
		if(ip == null || ip.length() <7 || ip.length()>15 || port == null ||
				port.length()<4 || port.length()>5) {
			return false;
		}
		int portInt = Integer.parseInt(port);
		if(portInt>1024 || portInt < 65536) {
			isPort = true;
		}
		
		String rex = "([1-9]|1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d)"
				+ "(\\.([1-9]|1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d)){3}";
		Pattern pat = Pattern.compile(rex);
		
		Matcher mat = pat.matcher(ip);
		isIP = mat.find();
		
		return isIP && isPort;
	}
	
	@Override
	public void finish() {
		super.finish();
		cancelConnect();
	}
	
	private void cancelConnect() {
		
		if (connect != null && connect.getStatus() == AsyncTask.Status.RUNNING) {
			connect.setCYCLE(false);
			connect.setSTAUTS(false);
			// 如果Task还在运行，则先取消它
			connect.cancel(true);
			try {
				connect.getmsocket().close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
}
}
