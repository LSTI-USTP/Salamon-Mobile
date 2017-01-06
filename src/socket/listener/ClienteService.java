/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socket.listener;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Timer;

import policia.transito.data.BaseADIM;
import policia.transito.model.Device;
import socket.modelo.Transfer;
import socket.modelo.Transfer.Intent;
import socket.modelo.TransferConverter;
import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.thoughtworks.xstream.XStream;

/**
 *
 * @author AhmedJorge
 */
public class ClienteService extends AsyncTask<String, String, Object>
{
	private static ClienteService LOCAL_CONNECTION = new ClienteService();
    private static final int PORTA = 12345;
    
    private String host;
    private Activity activity;
    private OnReciveTranfer process;
    
	private Socket server;
	private Client serviceListiner;

	Thread controleThread;
	private boolean run;
	private boolean nextThred;
	private boolean abort;
	protected boolean find;
	
	public static ClienteService findCurrentConnection()
	{
		return LOCAL_CONNECTION;
	}
	
	
	
    public void setOnTransitActivity(Activity activity, OnReciveTranfer process) 
    {
    	this.activity = activity;
    	this.process = process;
    	Log.i("APP", "Não entrou reiniciar servidor");
    	if(!this.hasConection() || this.host == null)
    	{
    	 	Log.i("APP", "Entrou reiniciar servidor");
    		reInitServer();
    	}
    		
    }

	
	public void initService()
	{
		Log.i("APP", "STARTING SERVICE");
		if(this.findServer())
    	{
    		if(connect())
    			this.runService();
    		Log.i("APP", "SERVICE STARTED");
    	}
		Log.i("APP", "NAO CONSEGUE INICIAR O SERVICO");
	}
	
	public void reInitServer()
	{
		this.host = BaseADIM.getIp(activity);
		this.initService();
	}
	
	
	/**
	 * PARAR O SERCICO DE CONNEXAO COM O SERVIDOR
	 */
	public void stopService() 
    {
		Log.i("APP", "STOPING SERVICE");
		try 
		{
			this.stopRun();
			this.serviceListiner.stop();
			this.server.close();
			Log.i("APP", "SERVICE STOPPED");
		} catch (Exception e) 
		{
			Log.e("APP", "STOP SERVICE FAILED");
		}
	}
    
    /**
	 * Proucura pelo servidor da aplicacao
	 * @return
	 */
	public boolean findServer() 
	{
		Log.i("APP", "PROCDURANDO PELO SERVIDOR DA APLICACAO "+host);
		this.find = false;
		Thread finder = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				try 
				{
					server = new Socket(host, PORTA);
					serviceListiner = new Client(server);
					ClienteService.this.find = true;
				} catch (Exception e) 
				{
					Log.e("APP", "ERRO AO PROCURAR O SERVER | INIT SOCKT", e);
				} 
			}
		});
		finder.start();
		try 
		{
			finder.join(10000);
			if(this.find)
				Log.i("APP", "SERVIDOR DA APLICACAO ENCONTRADO");
			return this.find;
		} catch (Exception e) 
		{
			Log.e("APP", "ERRO AO PROCURAR O SERVER | JOIN SOCKT", e);
		}
		return false;
	}
    
	public boolean hasConnectionServer()
	{	
		try 
		{
			server = new Socket(host, PORTA);
			serviceListiner = new Client(server);
			Log.i("APP", "CONECTADO COM O SERVIDOR");
		   this.find = true;
		} catch (Exception e) 
		{
			Log.i("APP", "IMPOSSIVEL CONECTAr COM O SERVIDOR");
			this.find = false;
		} 
			
		return this.find;
	}
	/**
	 * CONNECTA COM O SERVIDOR
	 */
    private boolean connect() 
	{
		Log.i("APP", "CONNECTING TO SERVER...");
		if(this.hasConection())
		{
			Device de = new Device(this.activity);
			Transfer transferConnect = new Transfer(de.getMac(), "SERVIDOR", 9001, Intent.CONNECT, "Estabelecendo o mapamento de coexao");
			transferConnect.setEspera(Espera.WAITING_CONNECTION_ACCEPT.name());
			this.transfer(transferConnect);
			Log.i("APP", "CONNECTION REQUEST SENDED");
			return true;
		}
		else Log.i("APP", "CONNECTION FAILED");
		return false;
		
	}


	public boolean isRunming() 
	{
		return this.run;
	}
	
	public void stopRun()
	{
		if(this.serviceListiner != null)
			this.serviceListiner.stop();
	}

	public void setHost(String host)
    {
		this.host = host;
		if(!hasConection())//AhmedKadafi is so bad
		{
			this.initService();
		}
    }
    
    public void setActivity(Activity activity)
    {
    	this.activity = activity;
    }
    
    public void setOnReciveData(OnReciveTranfer onReciveData)
    {
    	this.process = onReciveData;
    }
    
    /**
     * ENVIARA OS DADOS PARA O SERVIDOR
     * @param data
     * @return
     */
    public boolean transfer (Transfer data)
    {
    	if(data.getListMaps().size() > 0)
    	{
    		data.getListMaps().get(0).put("TRANSFER_TIMESTAMP", getTimeStampOut());
    	}
    	else
    	{
    		data.getListMaps().add(new HashMap<String, String>());
    		data.getListMaps().get(0).put("TRANSFER_TIMESTAMP", getTimeStampOut());
    	}
    	return this.hasConection()
    			&& this.serviceListiner.sendXMLData(data);
    	
    }

	/**
     * Verrificar se ha connexao com o servidor
     * @return
     */
	public boolean hasConection()
    {
		return 
				this.serviceListiner != null
				&& this.server != null
				&& this.server.isConnected()
				&& !this.server.isClosed();
		
	}
	
	 /**
     * COLOCAR O SERVICO EM EXECUSAO
     */
	private void runService()
	{
		if(!this.isRunming())
		{
			this.execute();
			this.controleThread = new Thread(serviceListiner);
			this.run = true;
			this.nextThred = true;
		}
		else
		{
			if(this.controleThread != null)
				this.controleThread.interrupt();
			this.controleThread = new Thread(serviceListiner);
			this.nextThred = true;
		}
	}	


	/**
	 * COLOCAR O SERVIDO NOS FUNDOS
	 */
	@Override
	protected Object doInBackground(String... params) 
	{
		while (true) 
		{
			if(controleThread != null && this.nextThred)
			{
				controleThread.start();
				this.nextThred = false;
			}
			if(this.abort) 
				return null;
		}	
	}
	
	
	

	private class Client implements Runnable
    {
        private ObjectInputStream in;
        private ObjectOutputStream out;
        
        public Client(Socket socket) 
        {
        	try 
        	{
				this.in = new ObjectInputStream(socket.getInputStream());
				this.out = new ObjectOutputStream(socket.getOutputStream());
			} catch (Exception e) 
			{
				Log.e("APP", "ERROR AO CRIAR O CLIENTE LISTENER: "+e.getMessage(), e);
			}
        	
		}

		public void stop() 
		{
			Log.i("APP", "STOPING LISTINER...");
			try 
			{
				this.in.close();
				this.out.close();
			} catch (Exception e)
			{
				Log.i("APP", "LISTINER STOPING FAILED");
			}
			Log.i("APP", "LISTINER STOPED");
		}

		
		@Override
		public void run() 
		{
			Log.i("APP", "RUMING...");
			try 
			{
				String xml;
				while((xml = (String) this.in.readObject()) != null)
				{
					Log.i("APP", "NEW XML...");
					Log.i("APP", xml);
					
					XStream stream =  TransferConverter.createStrem();
					final Transfer transfer = (Transfer) stream.fromXML(xml);
					
					Thread thread = new Thread()
					{
				        @Override
				        public void run(){
				            try 
				            {
				                synchronized (this) 
				                {
			                    	activity.runOnUiThread(new Runnable() 
			                    	{
				                        @Override
				                        public void run()
				                        {
				                        	if(transfer.getIntent() ==  Intent.DISCONNECT)
				                        	{
				                        		Client.this.disconnect();
				                        	}
				                        	else if(transfer.getIntent() == Intent.CONNECT 
				        							&& Espera.isConnectEspera(transfer.getEspera())
				        							&& transfer.getMessage().equals("true"))
				                        	{
				                        		Log.i("APP", "CONNECTION ACCEPT");
				                        		Log.i("APP", "PROCESSING DATA");
				                        		process.onPosConnected();
				                        	}
				                        	else 
				                        	{
				                        		Log.i("APP", "PROCESSING DATA");
				                        		process.onProcessTranfer(transfer);
				                        	}
				                        }
			                    	});
				                }
				            } catch (Exception e) 
				            {
				            	Log.e("APP", "ERRO AO ATUALIZAR AS COMPONETES DA ACTIVITE: "+e.getMessage(), e);
				            }
				        };
				    };  
				    thread.start();
				    Log.i("APP", "END DATA PROCESS");
				}
			} catch (Exception e) 
			{
				Log.e("APP", "ERRO DE CONNEXAO: "+e.getMessage(), e);
				this.disconnect();
			}
			Log.i("APP", "END RUMING...");
		}
		
		public void disconnect() 
		{
			try
			{
				this.connectionLosted();
				this.in.close();
				this.out.close();
				server.shutdownInput();
				server.shutdownInput();
				server.close();
			}catch(Exception ex)
			{
				Log.e("APP", "ERROR IN DISCONNECTION");
			}
		}

		private void connectionLosted() 
		{
			Thread thread = new Thread()
			{
		        @Override
		        public void run(){
		            try 
		            {
		                synchronized (this) 
		                {
	                    	activity.runOnUiThread(new Runnable() 
	                    	{
		                        @Override
		                        public void run()
		                        {
		                        	process.onConnectLost();
		                        }
	                    	});
		                }
		            } catch (Exception e) 
		            {
		            	Log.e("APP", "ERRO AO ATUALIZAR AS COMPONETES DA ACTIVITE: "+e.getMessage(), e);
		            }
		        };
		    };  
		    thread.start();
		}

		/**
		 * Servico de envio de xml para o servidor
		 * @param data
		 * @return 
		 */
		private boolean sendXMLData(Transfer data) 
		{
			HashMap<String, String> map = new HashMap<String, String>();
			try 
			{
				if(data.getListMaps().size()>0) 
					data.getListMaps().get(0).put("CLIENTE.TIME.SEND", getTimeStampOut());
				else
				{
					map.put("CLIENTE.TIME.SEND", getTimeStampOut());
					data.getListMaps().add(map);
				}
					
				String xml = TransferConverter.createStrem().toXML(data);
				Log.i("APP", "SENDING XML...");
				Log.i("APP", xml);
				this.out.writeObject(xml);
				Log.i("APP", "XML SENDED");
				return true;
			}
			catch (IOException e) 
			{
				Log.e("APP", "ERROR AO ENVIAR OS DADOS: "+e.getMessage(), e);
				return false;
			}
		}
    }


	public boolean isConnected() 
	{
		return this.server != null
				&& this.server.isConnected();
	}
	
	public  enum Espera
	{
		WAITING_CONNECTION_ACCEPT;
		
		private static boolean isConnectEspera(String name)
		{
			return name != null
					&& name.equals(WAITING_CONNECTION_ACCEPT.name());
		}
	}
	public static String getTimeStampOut()
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentDateandTime = sdf.format(new Date());
		return currentDateandTime;
	}

}
