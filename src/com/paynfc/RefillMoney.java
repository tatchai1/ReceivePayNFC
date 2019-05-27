package com.paynfc;

import android.app.Activity;
import android.app.AlertDialog;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.NextServiceFilterCallback;
import com.microsoft.windowsazure.mobileservices.ServiceFilter;
import com.microsoft.windowsazure.mobileservices.ServiceFilterRequest;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponseCallback;
import com.microsoft.windowsazure.mobileservices.TableOperationCallback;
import com.microsoft.windowsazure.mobileservices.TableQueryCallback;

import java.net.MalformedURLException;
import java.util.List;

public class RefillMoney extends Activity {
	
	EditText refill,phone;
	TextView money;
	private MobileServiceClient mClient;
	private MobileServiceTable<Userinfo> mUserinfo;
	private String key = "xcab43sx";
	public String NFCText,decrytuser,encrytuser;
	NfcAdapter mNfcAdapter; 
	Encryption enc;
	private AlertDialog.Builder adb; 
	private static final int MESSAGE_SENT = 1;
	Button bt1,bt2;
	String sphone;
	private ProgressBar mProgressBar;
	int balancemoney,refillmoney;
	
	protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    
    adb = new AlertDialog.Builder(this);
    setContentView(R.layout.refill_page);
    
    refill = (EditText)findViewById(R.id.erefill);
    phone = (EditText)findViewById(R.id.editTextphone);
    
    money = (TextView)findViewById(R.id.showmoney);
    
    bt1 = (Button) findViewById(R.id.buttonsearch);
    bt2 = (Button) findViewById(R.id.buttonok);
    
    mProgressBar = (ProgressBar) findViewById(R.id.progressBar7);
	mProgressBar.setVisibility(ProgressBar.GONE);
//    sphone = phone.getText();
    try {
		mClient = new MobileServiceClient(
				"https://paynfcapp.azure-mobile.net/",
				"qfPJJhnOaXVIQdbHhOZbHrPSDdHpTu13", this).withFilter(new ProgressFilter());
			} catch (MalformedURLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
  
    	mUserinfo = mClient.getTable(Userinfo.class);
    	
    	 bt1.setOnClickListener(new View.OnClickListener() {
	        	
	        	public void onClick(View v) 
	        	{

	    			if (mClient == null) {
	    				AlertDialog ad = adb.create();
	    				ad.setMessage("Cannot connect to Windows Azure Mobile Service!");
	    				ad.show();
	    			} else {
	    				
	    				mUserinfo.where().field("phone").eq(phone.getText().toString()).execute(new TableQueryCallback<Userinfo>() {
	    					public void onCompleted(List<Userinfo> result, int count, Exception exception, 
	    							ServiceFilterResponse response) {
	    						
	    						if (exception == null) {
	    							if(result.size() > 0)
	    							{	Userinfo item = result.get(0);
	    								
	    								//int money;
	    								//money = item.getBalance();
	    							money.setText(String.valueOf(item.getBalance()));
	    						
	    							}
	    							else if(result.size() <= 0)
	    							{	AlertDialog ad = adb.create();
	    							ad.setMessage("Wrong phone number!");
	    							ad.show();
	    						
	    							}
	    						} else {
	    							AlertDialog ad = adb.create();
	    							ad.setMessage("Error : " + exception.getCause().getMessage());
	    							ad.show();
	    						}
	    					}
	    				});

	    			}
	    		
	        		
	        	}
	        	

    	 });
    	 bt2.setOnClickListener(new View.OnClickListener() {
	        	
	        	public void onClick(View v) 
	        	{
	        		if (mClient == null) {
	    				AlertDialog ad = adb.create();
	    				ad.setMessage("Cannot connect to Windows Azure Mobile Service!");
	    				ad.show();
	    			} else {
	    				
	    				mUserinfo.where().field("phone").eq(phone.getText().toString()).execute(new TableQueryCallback<Userinfo>() {
	    					public void onCompleted(List<Userinfo> result, int count, Exception exception, 
	    							ServiceFilterResponse response) {
	    						
	    						if (exception == null) {
	    							if(result.size() > 0)
	    							{	Userinfo item = result.get(0);
	    						//	item.getBalance();
	    							 balancemoney  = item.getBalance();
	    							 refillmoney = Integer.parseInt(refill.getText().toString());
	    							balancemoney = refillmoney + balancemoney;
	    						//	money.setText(String.valueOf(item.getBalance()));
	    							item.setBalance(balancemoney);
	    							mUserinfo.update(item, new TableOperationCallback<Userinfo>() {

	    		            			public void onCompleted(Userinfo entity, Exception exception, ServiceFilterResponse response) {
	    		            				
	    		            				if (exception == null) {
	    		            				//	money.setText(balancemoney);
	    		            					Toast.makeText(RefillMoney.this,	
	    		            				        	"Update Data Successfully.",
	    		            				        	Toast.LENGTH_SHORT).show();	        
	    		            				} else {
	    		            					Toast.makeText(RefillMoney.this,	
	    		            				        	"Cannot Update Data Successfully.",
	    		            				        	Toast.LENGTH_SHORT).show();	            				        
	    		            				}	            				
	    		            			}	            			
	    		            		});
	    							}
	    							else if(result.size() <= 0)
	    							{	AlertDialog ad = adb.create();
	    							ad.setMessage("Wrong phone!");
	    							ad.show();
	    						
	    							}
	    						} else {
	    							AlertDialog ad = adb.create();
	    							ad.setMessage("Error : " + exception.getCause().getMessage());
	    							ad.show();
	    						}
	    					}
	    				});

	    			}
	    		
	        		
	        	}
	        	

 	 });
   
	}
	 private class ProgressFilter implements ServiceFilter {
			
			@Override
			public void handleRequest(ServiceFilterRequest request, NextServiceFilterCallback nextServiceFilterCallback,
					final ServiceFilterResponseCallback responseCallback) {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						if (mProgressBar != null) mProgressBar.setVisibility(ProgressBar.VISIBLE);
					}
				});
				
				nextServiceFilterCallback.onNext(request, new ServiceFilterResponseCallback() {
					
					@Override
					public void onResponse(ServiceFilterResponse response, Exception exception) {
						runOnUiThread(new Runnable() {

							@Override
							public void run() {
								if (mProgressBar != null) mProgressBar.setVisibility(ProgressBar.GONE);
							}
						});
						
						if (responseCallback != null)  responseCallback.onResponse(response, exception);
					}
				});
			}
		}
}