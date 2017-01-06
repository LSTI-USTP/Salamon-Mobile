package policia.transito.model;

import java.io.Serializable;
import java.text.DecimalFormat;

import android.app.Activity;
import android.content.ContentProvider;
import android.content.Context;
import android.content.pm.ProviderInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class Device implements Serializable
{
	private String marca;
	private String modelo;
	private String versao;
	private String imei;
	private String polegada;
	private String mac;
	
	public Device(Activity activity)
	{
		TelephonyManager telephonyManager = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
		
		DisplayMetrics dm = new DisplayMetrics();
		DecimalFormat decimalFormat = new DecimalFormat("#.#");
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		
		
		WifiManager wifiManager = (WifiManager) activity.getSystemService(Context.WIFI_SERVICE);
		wifiManager.setWifiEnabled(true);
		WifiInfo wInfo = wifiManager.getConnectionInfo();
		if(wInfo.getMacAddress() != null)
		{
			this.mac = wInfo.getMacAddress();
		}
		
        int width=dm.widthPixels;
        int height=dm.heightPixels;
        int dens=dm.densityDpi;
        double wi=(double)width/(double)dens;
        double hi=(double)height/(double)dens;
        double x = Math.pow(wi,2);
        double y = Math.pow(hi,2);
        double polegada = Math.sqrt(x+y);
        polegada = Math.round(polegada);
		
		this.marca = android.os.Build.MANUFACTURER+" "+android.os.Build.DEVICE;
		this.modelo = android.os.Build.MODEL;
		this.versao = android.os.Build.VERSION.RELEASE;
		this.imei = telephonyManager.getDeviceId();
		this.polegada = polegada+"";
	}
	
	
	public String getMarca()
	{
		return marca;
	}
	
	public String getModelo() {
		return modelo;
	}
	
	public void setModelo(String modelo) {
		this.modelo = modelo;
	}
	public String getVersao() {
		return versao;
	}
	

	public String getMac() {
		return mac;
	}
	
	public String getPolegada() {
		return polegada;
	}


	@Override
	public String toString() {
		return "Device [marca=" + marca + ", modelo=" + modelo + ", versao="
				+ versao + ", imei=" + imei + ", polegada=" + polegada
				+ ", mac=" + mac + "]";
	}
	
	
}
