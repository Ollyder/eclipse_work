package com.example.wifi_suncase_copy;

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
	private TextView data_tv;
	private Data data;
	private Socket msocket;
	private SocketAddress socketaddress;
	byte[] read_buff;
	private Float sun;
	
	private boolean STATU = false;
	private boolean CIRCLE = false;
	
	private InputStream inputstream;
	private OutputStream outputstream;
	
	
	public ConnectTask(Context context,TextView error_tv,
			TextView data_tv,Data data) {
		this.context = context;
		this.error_tv = error_tv;
		this.data_tv = data_tv;
		this.data = data;
	}
	
	@Override
	protected void onPreExecute() {
		error_tv.setText("正在连接");
	}

	@Override
	protected Void doInBackground(Void... arg0) {
		// TODO Auto-generated method stub
		// 先创建套接字
		msocket = new Socket();
		socketaddress = new InetSocketAddress(Constant.IP, Constant.Port);
		try{
			msocket.connect(socketaddress,3000);
			if(msocket.isConnected()) {
				setSTATU(true);
				inputstream = msocket.getInputStream();
				outputstream = msocket.getOutputStream();
			} else {
				setSTATU(false);
			}
			
			while(CIRCLE) {
				StreamUtil.writeCommand(outputstream, Constant.SUN_CHK);
				Thread.sleep(200);
				read_buff = StreamUtil.readData(inputstream);
				sun = FROSun.getData(Constant.SUN_LEN, Constant.SUN_NUM, read_buff);
				if(sun != null) {
					data.setSun((int)(float)sun);
				}
				publishProgress();
				Thread.sleep(200);
			}
		} catch(IOException e) {
			setSTATU(false);
			publishProgress();
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	protected void onProgressUpdate(Void... values) { // 用于更新UI
		if(STATU) {
			error_tv.setTextColor(context.getResources().getColor(R.color.green));
			error_tv.setText("连接成功");
		} else {
			error_tv.setTextColor(context.getResources().getColor(R.color.red));
			error_tv.setText("连接失败");
		}
		data_tv.setText(String.valueOf(data.getSun()));
		
	}

	public void setSTATU(boolean STATU) {
		this.STATU = STATU;
	}
	
	public void setCIRCLE(boolean CIRCLE) {
		this.CIRCLE = CIRCLE;
	}
	
	public Socket getmsocket() {
		return msocket;
	}
}
