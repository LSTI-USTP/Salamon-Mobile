package policia.transito.model;

import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationServices;

public class Localizacao implements ConnectionCallbacks, OnConnectionFailedListener
{
    
	private double latitude;
	private double longitude;
	private Context context;
	private GoogleApiClient mGoogleApiClient;
	private Location mLastLocation;
	
	private String address;
	private String city;
	private String country;
	private Address currentLocation;
	private String destrito;
	private boolean hasLoacation;
	
	public Localizacao(Context context)
	{
		this.context= context;
		findLocation();
		
	}
	
	public boolean hasLocation()
	{
		return this.hasLoacation;
	}

	public void findLocation() 
	{
		try
		{
			buildGoogleApiClient();
			Log.i("APP", "BUILDING LOCATION");
			
			if(mGoogleApiClient!= null)
			{
				Log.i("APP", "Connecting API CLIENT");
				   mGoogleApiClient.connect();
			}
		}
		catch(Exception ex)
		{
			Log.i("APP", "ERROR ON LOCATION");
		}
	}
	
	
	 protected synchronized void buildGoogleApiClient()
	 {
		 Log.i("APP", "Building Google Api Cliente...");
		 
		this.mGoogleApiClient = new GoogleApiClient.Builder(context)
         .addConnectionCallbacks(this)
         .addOnConnectionFailedListener(this)
         .addApi(LocationServices.API)
         .build();
     }
	 
	 
	 
	 

		@Override
		public void onConnectionFailed(ConnectionResult failed) 
		{
			Log.i("APP", "Connection Failed...");
		}


		@Override
		public void onConnected(Bundle arg0) 
		{
			try
			{
			   Log.i("APP", "TRYING GET LOCATION");
			   this.mLastLocation = LocationServices.FusedLocationApi.getLastLocation(this.mGoogleApiClient);
				
				Log.i("APP", "LOCATION GETD IS "+this.mLastLocation);
		        if (mLastLocation != null) 
		        {
		        	Log.i("APP", "GETING FIELD LOCATION...");
		        	this.latitude = mLastLocation.getLatitude();
		        	this.longitude = mLastLocation.getLongitude();
		        	this.hasLoacation  = true;
		        	
		        	
		        	Geocoder geocoder;
		        	List<Address> listAdress;
		        	geocoder = new Geocoder(context, Locale.getDefault());

		        	listAdress = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

		        	this.currentLocation = listAdress.get(0);
		        	Log.i("APP", "CURRENT LOCATION = "+currentLocation);
		        	
		        	this.address = currentLocation.getLocality(); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
		        	this.city = currentLocation.getLocality();
		        	this.country = currentLocation.getCountryName();
		        	this.destrito = currentLocation.getSubAdminArea();
		        	Log.i("APP", currentLocation.toString());
		        }
			}catch(Exception ex)
			{
			}
			
			
		}
		
		
		
		



	public double getLatitude() 
	{		
		return latitude;
	}


	public double getLongitude()
	{
		return longitude;
	}


	@Override
	public void onConnectionSuspended(int arg0)
	{
		
	}


	public String getAddress() {
		return address;
	}


	public String getCity() {
		return city;
	}


	public String getCountry() {
		return country;
	}


	public Address getCurrentLocation() {
		return currentLocation;
	}


	public String getDestrito() {
		return destrito;
	}

}
