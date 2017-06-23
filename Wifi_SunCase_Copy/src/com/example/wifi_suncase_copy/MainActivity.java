package com.example.wifi_suncase_copy;



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
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends Activity {

	private EditText IP_et;
	private EditText PORT_et;
	private ToggleButton BT_tb;
	private TextView wrong_im;
	private TextView sun_tv;
	private Context context;
	private Data data;
	
	private ConnectTask connect;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		bindview();
		initdata();
		workingit();
	}
	
	private void bindview() {
		IP_et = (EditText)findViewById(R.id.IP_et);
		PORT_et = (EditText)findViewById(R.id.PORT_et);
		BT_tb = (ToggleButton)findViewById(R.id.BT_tb);
		wrong_im = (TextView)findViewById(R.id.wrong_im);
		sun_tv = (TextView)findViewById(R.id.sun_tv);
		data = new Data();
		context = this;
	}
	
	private void initdata() {
		IP_et.setText(Constant.IP);
		PORT_et.setText(String.valueOf(Constant.Port));
	}
	
	private void workingit() {
		BT_tb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked) {
					String IP = IP_et.getText().toString().trim();
					String Port = PORT_et.getText().toString().trim();
					if(checked(IP,Port)) {
						Constant.IP = IP;
						Constant.Port = Integer.parseInt(Port);
					} else {
						Toast.makeText(context, "输入正确的IP和PORT", Toast.LENGTH_SHORT).show();
						return;
					}
					connect = new ConnectTask(context, wrong_im, sun_tv, data);
					connect.setCIRCLE(true);
					connect.execute();
				} else {
					if(connect != null && connect.getStatus() == AsyncTask.Status.RUNNING) {
						connect.setCIRCLE(false);
						connect.setSTATU(false);
						connect.cancel(true);
						
						try {
							connect.getmsocket().close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					wrong_im.setTextColor(context.getResources().getColor(R.color.gray));
					wrong_im.setText("点击连接");
				}
			}
		});
	}
	
	private boolean checked(String IP, String port) {
		boolean isIP = false;
		boolean isPort = false;
		
		if(IP==null || IP.length() < 7 || IP.length() > 15 || "".equals(IP) 
				|| port==null || port.length() < 4 || port.length() > 5)
	      {
	        return false;
	      }
		
	      String rexp = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";
	      
	      Pattern pat = Pattern.compile(rexp);
	      
	      Matcher mat = pat.matcher(IP);
	      
	      isIP = mat.find();
	      
	      int portInt = Integer.parseInt(port);
	      if(portInt>1024&&portInt<65536) {
	    	  isPort = true;
	      }
		
		return isIP && isPort;
	}
	
	@Override
	public void finish() {
		
		if(connect != null && connect.getStatus() == AsyncTask.Status.RUNNING) {
			connect.setCIRCLE(false);
			connect.setSTATU(false);
			connect.cancel(true);
			
			try {
				connect.getmsocket().close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		super.finish();
	}
}
