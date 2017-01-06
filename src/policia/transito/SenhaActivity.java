package policia.transito;

import policia.transito.data.DefaultDatas;
import socket.listener.ClienteService;
import socket.listener.OnReciveTranfer;
import socket.modelo.Transfer;
import android.app.Activity;
import android.os.Bundle;

public class SenhaActivity extends Activity implements DefaultDatas, OnReciveTranfer
{
	private ClienteService service;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.layout_user_alter_pwd);
		
		this.service = ClienteService.findCurrentConnection();
		this.service.setOnTransitActivity(this, this);
	}
	
	private void init()
	{
		
		
	}

	@Override
	public void onProcessTranfer(Transfer transfer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPosConnected() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConnectLost() {
		// TODO Auto-generated method stub
		
	}
}
