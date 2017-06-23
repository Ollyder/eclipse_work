package com.example.wifi_suncase_2;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import com.fro.util.FROSun;
import com.fro.util.StreamUtil;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;

public class ConnectTask extends AsyncTask<Void, Void, Void> {
	private Context context;
	private TextView error_tv;
	private TextView sun_tv;
	
	private Socket msocket;
	private SocketAddress msockerAddress;
	
	private InputStream inputStream;
	private OutputStream outputStream;
	
	private boolean STAUTS;
	private boolean CYCLE;
	
	private int data;
	
	private byte[] read_buff;
	public ConnectTask(Context context,TextView error_tv,TextView sun_tv) {
		this.context = context;
		this.error_tv = error_tv;
		this.sun_tv = sun_tv;
	}
	
	@Override
	protected void onPreExecute() {
		error_tv.setText("正在连接");
		
	}

	@Override
	protected Void doInBackground(Void... arg0) {
		// TODO Auto-generated method stub
		msocket = new Socket();
		msockerAddress = new InetSocketAddress(Constant.IP, 
				Integer.parseInt(Constant.Port));
		try {
			msocket.connect(msockerAddress,3000);
			if(msocket.isConnected()) {
				setSTAUTS(true);
				inputStream = msocket.getInputStream();
				outputStream = msocket.getOutputStream();
			} else {
				setSTAUTS(false);
			}
			
			while(CYCLE) {
				StreamUtil.writeCommand(outputStream, Constant.SUN_CHK);
				Thread.sleep(200);
				read_buff = StreamUtil.readData(inputStream);
				Float sun = FROSun.getData(Constant.SUN_LEN, Constant.SUN_NUM, read_buff);
				if(sun!=null) {
					data = (int)(float)sun;
				}
				publishProgress();
				Thread.sleep(200);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	@Override
	protected void onProgressUpdate(Void...voids) {
		// update UI
		if (STAUTS) {
			error_tv.setTextColor(context.getResources().getColor(R.color.green));
			error_tv.setText("连接正常！");
		} else {
			error_tv.setTextColor(context.getResources().getColor(R.color.red));
			error_tv.setText("连接失败！");
		}
		sun_tv.setText(String.valueOf(data));
	} 

	public boolean isSTAUTS() {
		return STAUTS;
	}

	public void setSTAUTS(boolean sTAUTS) {
		STAUTS = sTAUTS;
	}

	public boolean isCYCLE() {
		return CYCLE;
	}

	public void setCYCLE(boolean cYCLE) {
		CYCLE = cYCLE;
	}

	public Socket getmsocket() {
		return  msocket;
	}
	
}
