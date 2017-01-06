package policia.transito;

import java.util.HashMap;

import policia.transito.data.BaseADIM;
import policia.transito.data.BaseFiscalizacao;
import policia.transito.data.DBObject;
import policia.transito.data.DefaultDatas;
import policia.transito.model.Device;
import socket.listener.ClienteService;
import socket.listener.OnReciveTranfer;
import socket.modelo.Transfer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

@SuppressLint("NewApi") public class Login extends Activity implements DefaultDatas, DBObject, OnReciveTranfer
{
	private Button btLoginEnter;
	private  EditText edLoginUserName;
	private  EditText edLoginPWD;
	private BaseADIM data;
    private ClienteService service;
	private BaseFiscalizacao baseFiscalizacao;
	private String userState;
	private ProgressDialog dialog;
	private ProgressBar spinner;
	public static int COD_CALL_NEXT = 25;
	
	@Override
    protected void onCreate(Bundle savedInstanceState)
    {
		try
		{
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.layout_login);
	        overridePendingTransition(R.anim.translate_direita_to_esquerda, R.anim.translate_esquerda_to_direita);
	        init(); 
	  
        
		}catch(Exception ex)
		{
			Log.e("APP", "ERROR ON CREATE LOGIN - "+ex.getMessage(), ex);
		}
		
    }
	
	private void init() 
	{		
		this.dialog = new ProgressDialog(this);		
		dialog.setCancelable(false);
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

		this.service = ClienteService.findCurrentConnection();
		this.service.setOnTransitActivity(this, this);
		
		baseFiscalizacao = new BaseFiscalizacao(this);
		
		this.btLoginEnter = (Button) findViewById(R.id.btLoginEnter);
        this.edLoginUserName = (EditText) findViewById(R.id.incluLoginUserName).findViewById(R.id.edLoginText);
        this.edLoginPWD = (EditText) findViewById(R.id.incluLoginUserPwd).findViewById(R.id.edLoginText);
        this.edLoginPWD.setHint(R.string.userPwd);
        this.edLoginUserName.setHint(R.string.userName);
        PasswordTransformationMethod transformationPassWord =PasswordTransformationMethod.getInstance();
        this.edLoginPWD.setAllCaps(false);
        this.edLoginPWD.setTransformationMethod(transformationPassWord);
        
        findViewById(R.id.incluLoginUserPwd).findViewById(R.id.imageLoginItem).setBackgroundResource(R.drawable.image_lock);
        findViewById(R.id.incluLoginUserName).findViewById(R.id.imageLoginItem).setBackgroundResource(R.drawable.image_user);
        this.data = new BaseADIM(this);
        
        this.btLoginEnter.setOnClickListener(new OnClickListener() 
        {	
			@Override
			public void onClick(View arg0) {
			
				logar();
			}
		});
	}
	
	
	
    /**
     * Entrar no sistema que poder como administrador ou agente
     */
    private void logar()
    {
	  	String userName = edLoginUserName.getText().toString();
	  	String userPwd = edLoginPWD.getText().toString();
		if((!userName.equals("") && userName.length()>0) && (!userPwd.equals("") && userPwd.length()>0))
		{
			char init = userName.charAt(0);
			char end = userName.charAt(userName.length()-1);
			//Quando a inicial igual a inicial do administrador e a terminal igual aterminal do administrador entao logar como administrado
			if(init == INIT_ADM && end == END_ADM)
			{
				userName = userName.substring(1, userName.length()-1);
				loginADM(userName, userPwd);
			}else loginAgente(userName, userPwd);	
		} else Toast.makeText(getApplicationContext(),R.string.loginEmptyFields, Toast.LENGTH_LONG).show();	
 
    }
    
    
   
    /**
     * Logar no sistema como um agente para a utilização
     * @param userName
     * @param userPwd
     */
	private void loginAgente(String userName, String userPwd) 
	{
		if(BaseADIM.getIp(this) == null) 
			Toast.makeText(this, "O dispositivo ainda não está configurado.",Toast.LENGTH_LONG).show();
		else
		{
			if(BaseADIM.getDeviceName(this)==null)
				Toast.makeText(this, "Dispositivo disponivel", Toast.LENGTH_LONG).show();
			else
			{
				HashMap<String, String> map = new HashMap<String, String>();  
				Device device = new Device(this);
				Transfer transfer = new Transfer(device.getMac(), SERVER_APP_NAME, 1002,socket.modelo.Transfer.Intent.REG, "Petição para autenticar no sistema");
			
				if(this.service.hasConection())
				{	

			        map.put("ACCESSNAME", userName);
			        map.put("PWD", userPwd);
			        map.put("DEVICE NAME", BaseADIM.getDeviceName(this));
			        map.put("MAC", device.getMac());
			     
			        transfer.setEspera(Espera.LOGIN.name());
			        transfer.getListMaps().add(map);
			        service.transfer(transfer);
			        dialog.setMessage("A autenticar...");
			        dialog.show();
				}
				else
				{
					dialog.setMessage("A autenticar...");
					dialog.show();
					map.put("ACCESS NAME", userName);
					transfer.getListMaps().add(map);
					baseFiscalizacao.guardarDadosAgente(transfer,0);
					Intent intent = new Intent(this, ActivityInitial.class);
    				startActivity(intent);		
				}
			}
		}
	}
			
	
	private void validarAplicacao(String idUser )
	{
		Device device = new Device(this);
		Transfer transfer = new Transfer(device.getMac(), SERVER_APP_NAME, 5004,socket.modelo.Transfer.Intent.GET, "CARREGAR MENUS");
		
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("ID UTILIZADOR", idUser);
		transfer.setEspera(Espera.MENU.name());
		transfer.getListMaps().add(map);
		this.service.transfer(transfer);
	}
	
	
	/**
	 * Logar no sistema como um administrador de manutenção
	 * @param userName
	 * @param userPwd
	 */
	private void loginADM(String userName, String userPwd) 
	{
		if(data.login(userName, userPwd))
    	{
	    	Intent intet = new Intent(this, Administracao.class);
	    	startActivity(intet);
    	}
    	else Toast.makeText(this, R.string.access_deny, Toast.LENGTH_LONG).show();
	}

	@Override
	public void onProcessTranfer(Transfer transfer)
	{
		Espera espera = Espera.valueOf(transfer.getEspera());;
		
		if(espera == Espera.LOGIN)
		{			 
	        if(transfer.getMessage().equals("false"))
	        {
	        	dialog.dismiss();
	        	Toast.makeText(this, R.string.access_deny, Toast.LENGTH_LONG).show();
	        }
	        else
	        {  
	        	baseFiscalizacao.guardarDadosAgente(transfer,1);
	        	validarAplicacao(transfer.getListMaps().get(0).get("ID USER"));
	        	userState = transfer.getListMaps().get(0).get("STATE");
	        }
		}
		else if(espera==Espera.MENU)
		{
			if(transfer.getMessage().equals("false"))
    			Toast.makeText(this, "Sem acesso a aplicação de fiscalização", Toast.LENGTH_LONG).show();
    		else
    		{
    			if(userState.equals("2"))
    			{
        			Intent telaAtivarUtilizador = new Intent(this, ActiveUserActivity.class);
            		startActivity(telaAtivarUtilizador);
    			}
    			else
    			{
    				Intent intent = new Intent(this, ActivityInitial.class);
    				startActivity(intent);			
    			}		
    		}
		}
	}
	
	
	private enum Espera
	{
		LOGIN,
		MENU,
		ESTADO_CONDUTOR,
		CATEGORIA_VEICULO,
		INFRACOES
	}

	@Override
	public void onPosConnected()
	{
	}

	@Override
	public void onConnectLost() {

	
	}
	
	@Override
	protected void onStart()
	{
		super.onStart();
		this.service.setOnTransitActivity(this, this);
	}

	@Override
	public void onBackPressed() 
	{
	}
	
}
	
