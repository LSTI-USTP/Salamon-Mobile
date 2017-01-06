package policia.transito.model;

import android.telephony.gsm.SmsManager;
import android.util.Log;

public class SendMessage 
{
	public static void sendSmsMessage(String phoneNumber, String message)
	{
		  try 
		  {
			  SmsManager smsManager = SmsManager.getDefault();
			  smsManager.sendTextMessage(phoneNumber, null, message, null, null);
		  } 
		  catch (Exception e)
		  {
			 Log.e("APP","erro a enviar mensagem "+e.getMessage(), e);
		  }

	}
}
