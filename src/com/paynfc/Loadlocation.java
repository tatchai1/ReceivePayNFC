package com.paynfc;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceTable;

import java.net.MalformedURLException;

/*package com.paynfc;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;


public class Loadlocation extends FragmentActivity  {
	private static View view;
	GoogleMap gmap ;
	
	protected void onCreate(Bundle savedInstanceState) {
	  
 
       // rootView = inflater.inflate(R.layout.map_page, container, false);
        ViewGroup parent = (ViewGroup) rootView.getParent();
        parent.removeView(rootView);  
        
    
	}
	public class LoadMap extends AsyncTask<String, Integer, Location> {
		LocationManager lm;
	    LatLng p;

	    @Override
	    protected Location doInBackground(String... params) {
	   
	        LocationManager locationManager = (LocationManager) mContext.getActivity()
	                .getSystemService(Context.LOCATION_SERVICE);
	        Criteria criteria = new Criteria();
	        String provider = locationManager.getBestProvider(criteria, true);
	        Location location = locationManager.getLastKnownLocation(provider);
	       
	        p = new LatLng(location.getLatitude(), location.getLongitude());

	        return location;
	    } 

	    protected void onPostExecute(Location location) {

	        try {

	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	}   
	 
	}*/
public class Loadlocation extends Activity {

    Button searchBtn = null;
    Intent locatorService = null;
    AlertDialog alertDialog = null;
    
    private MobileServiceClient mClient;
	private MobileServiceTable<Location> mlocation;
    /** Called when the activity is first creamloted. */
    @Override
    public void onCreate(Bundle savedInstanceState) {/*
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        searchBtn = (Button) findViewById(R.id.searchBtn);

        searchBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                if (!startService()) {
                    CreateAlert("Error!", "Service Cannot be started");
                } else {
                    Toast.makeText(Loadlocation.this, "Service Started",
                            Toast.LENGTH_LONG).show();
                }

            }
        });

    */
    	
    	
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
	      
    
    
    
    }

    public boolean stopService() {
        if (this.locatorService != null) {
            this.locatorService = null;
        }
        return true;
    }

    public boolean startService() {
        try {
            // this.locatorService= new
            // Intent(FastMainActivity.this,LocatorService.class);
            // startService(this.locatorService);

            FetchCordinates fetchCordinates = new FetchCordinates();
            fetchCordinates.execute();
            return true;
        } catch (Exception error) {
            return false;
        }

    }

    public AlertDialog CreateAlert(String title, String message) {
        AlertDialog alert = new AlertDialog.Builder(this).create();

        alert.setTitle(title);

        alert.setMessage(message);

        return alert;

    }

    public class FetchCordinates extends AsyncTask<String, Integer, String> {
        ProgressDialog progDailog = null;

        public double lati = 0.0;
        public double longi = 0.0;

        public LocationManager mLocationManager;
        public VeggsterLocationListener mVeggsterLocationListener;

        @Override
        protected void onPreExecute() {
            mVeggsterLocationListener = new VeggsterLocationListener();
            mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, 0, 0,
                    mVeggsterLocationListener);

            progDailog = new ProgressDialog(Loadlocation.this);
          /*  progDailog.setOnCancelListener(new OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    FetchCordinates.this.cancel(true);
                }
            });*/
            progDailog.setMessage("Loading...");
            progDailog.setIndeterminate(true);
            progDailog.setCancelable(true);
            progDailog.show();

        }

        @Override
        protected void onCancelled(){
            System.out.println("Cancelled by user!");
    //        progDialog.dismiss();
            mLocationManager.removeUpdates(mVeggsterLocationListener);
        }

        @Override
        protected void onPostExecute(String result) {
            progDailog.dismiss();

            Toast.makeText(Loadlocation.this,
                    "LATITUDE :" + lati + " LONGITUDE :" + longi,
                    Toast.LENGTH_LONG).show();
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub

            while (this.lati == 0.0) {

            }
            return null;
        }

        public class VeggsterLocationListener implements LocationListener {

            @Override
            public void onLocationChanged(Location location) {

                int lat = (int) location.getLatitude(); // * 1E6);
                int log = (int) location.getLongitude(); // * 1E6);
                int acc = (int) (location.getAccuracy());

                String info = location.getProvider();
                try {

                    // LocatorService.myLatitude=location.getLatitude();

                    // LocatorService.myLongitude=location.getLongitude();

                    lati = location.getLatitude();
                    longi = location.getLongitude();

                } catch (Exception e) {
                    // progDailog.dismiss();
                    // Toast.makeText(getApplicationContext(),"Unable to get Location"
                    // , Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onProviderDisabled(String provider) {
                Log.i("OnProviderDisabled", "OnProviderDisabled");
            }

            @Override
            public void onProviderEnabled(String provider) {
                Log.i("onProviderEnabled", "onProviderEnabled");
            }

            @Override
            public void onStatusChanged(String provider, int status,
                    Bundle extras) {
                Log.i("onStatusChanged", "onStatusChanged");

            }

        }

    }

}