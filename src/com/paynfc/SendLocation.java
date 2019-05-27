package com.paynfc;


import android.app.Activity;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.TableOperationCallback;
import com.microsoft.windowsazure.mobileservices.TableQueryCallback;

import java.net.MalformedURLException;
import java.util.List;

public class SendLocation extends Activity{
	
	Switch switch1;
	TextView tv1;
	
	protected LocationManager locationManager;
	
	private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 1; // in Meters
	private static final long MINIMUM_TIME_BETWEEN_UPDATES = 1000; // in Milliseconds
	
	private MobileServiceClient mClient;
	private MobileServiceTable<LocationTable> mLocation;
	double longi,lati;
	String boatname;
	
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.location);
	    switch1 = (Switch)findViewById(R.id.switch1);
	    tv1 = (TextView)findViewById(R.id.textView2);
	    
	    final Spinner spinner = (Spinner) findViewById(R.id.spinnerboat); 
	    final Button button1 = (Button) findViewById(R.id.buttonok1);
	        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
	                R.array.BOAT, android.R.layout.simple_spinner_item);
	     // Specify the layout to use when the list of choices appears
	        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	        // Apply the adapter to the spinner
	        spinner.setAdapter(adapter);
	        
	        
	        button1.setOnClickListener(new View.OnClickListener() {
	        	
	        	public void onClick(View v) 
	        	{Toast.makeText(SendLocation.this,
	        	
	        	"Your Selected : " + String.valueOf(spinner.getSelectedItem()),
	        	
	        	Toast.LENGTH_SHORT).show();
	        
	        	boatname = String.valueOf(spinner.getSelectedItem());
	        	tv1.setText(boatname);
	        	}
	        	
	        	});
	        tv1.setText(boatname);
	
	    locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
	    switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
	        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
	            if (isChecked) {
	                // The toggle is enabled
	          //  	Loadlocation loadlo = new Loadlocation();
	            	// new LoadMap().execute();
	            	 Criteria criteria = new Criteria();
	      	         String provider = locationManager.getBestProvider(criteria, true);
	            	 Location location = locationManager.getLastKnownLocation(provider);
	            	 locationManager.requestLocationUpdates(
	            			location.getProvider(),
	                 		MINIMUM_TIME_BETWEEN_UPDATES, 
	                 		MINIMUM_DISTANCE_CHANGE_FOR_UPDATES,
	                 		new MyLocationListener()
	                 );
	            } else {
	                // The toggle is disabled
	            	
	            	locationManager.removeUpdates(new MyLocationListener());
	            	
	            }
	        }
	    });
	

  	  try {
				mClient = new MobileServiceClient(
						"https://paynfcapp.azure-mobile.net/",
						"qfPJJhnOaXVIQdbHhOZbHrPSDdHpTu13", this);
		//		mCost = mClient.getTable(Cost.class);
		//		mHistory = mClient.getTable(History.class);
					} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
  	  mLocation = mClient.getTable(LocationTable.class);
  	  LocationTable item = new LocationTable();
  	  
  	  item.setmNameboat(boatname);
  	  mLocation.insert(item, new TableOperationCallback<LocationTable>() {

		public void onCompleted(LocationTable entity, Exception exception, ServiceFilterResponse response) {
			
			if (exception == null) {	                   
        		
			} else {
        		Toast.makeText(SendLocation.this,	
			        	"Cannot Insert Data.",
			        	Toast.LENGTH_SHORT).show();
	}            				
		}            			
	});
	}
	public class LoadMap extends AsyncTask<String, Integer, Location> {
		LocationManager lm;
	    LatLng p;

	    @Override
	    protected Location doInBackground(String... params) {
	     
	       /* LocationManager locationManager = (LocationManager)
	                getSystemService(Context.LOCATION_SERVICE);*/
	       Criteria criteria = new Criteria();
	       String provider = locationManager.getBestProvider(criteria, true);
	      //  Location location = locationManager.getLastKnownLocation(provider);
	        Location location = locationManager.getLastKnownLocation(provider);
	        
	        
	    //    p = new LatLng(location.getLatitude(), location.getLongitude());

	        return location;
	    } 

	    protected void onPostExecute(Location location) {

	        try {

	            if (location != null) {
	            	String message = String.format(
	    					"Current Location \n Longitude: %1$s \n Latitude: %2$s",
	    					location.getLongitude(), location.getLatitude()
	    			);
	            	
	            	
	    			Toast.makeText(SendLocation.this, message,
	    					Toast.LENGTH_LONG).show();
	    			tv1.setText(message);

	            } else {
	                Toast.makeText(SendLocation.this, "No Location",
	                        Toast.LENGTH_LONG).show();
	            }

	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	} 
	
	private class MyLocationListener implements LocationListener {

		public void onLocationChanged(Location location) {
			String message = String.format(
					"New Location \n Longitude: %1$s \n Latitude: %2$s",
					location.getLongitude(), location.getLatitude()
			);
			longi = location.getLongitude();
			lati =  location.getLatitude();
			 mLocation.where().field("nameboat").eq(boatname).execute(new TableQueryCallback<LocationTable>() {
	        		public void onCompleted(List<LocationTable> result, int count, Exception exception, 
							ServiceFilterResponse response) {
						
						if (exception == null) {
							if(result.size() > 0)
							{  LocationTable item = result.get(0);
							
								item.setmLatitude(lati);
								item.setmLongitude(longi);
								mLocation.update(item, new TableOperationCallback<LocationTable>() {

			            			public void onCompleted(LocationTable entity, Exception exception, ServiceFilterResponse response) {
			            				
			            				if (exception == null) {
			            					     
			            				} else {
			            					Toast.makeText(SendLocation.this,	
			            				        	"Cannot Update Data.",
			            				        	Toast.LENGTH_SHORT).show();	            				        
			            				}	            				
			            			}	            			
								});
								}}}});
							
			Toast.makeText(SendLocation.this, message, Toast.LENGTH_LONG).show();
		
			 }
		public void onStatusChanged(String s, int i, Bundle b) {
			Toast.makeText(SendLocation.this, "Provider status changed",
					Toast.LENGTH_LONG).show();
		}

		public void onProviderDisabled(String s) {
			Toast.makeText(SendLocation.this,
					"Provider disabled by the user. GPS turned off",
					Toast.LENGTH_LONG).show();
		}

		public void onProviderEnabled(String s) {
			Toast.makeText(SendLocation.this,
					"Provider enabled by the user. GPS turned on",
					Toast.LENGTH_LONG).show();
		}

	}

}





  