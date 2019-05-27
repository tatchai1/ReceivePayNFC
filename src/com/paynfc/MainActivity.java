package com.paynfc;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcAdapter.CreateNdefMessageCallback;
import android.nfc.NfcAdapter.OnNdefPushCompleteCallback;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.text.format.Time;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.microsoft.windowsazure.mobileservices.*;

import java.net.MalformedURLException;
import java.util.List;


public class MainActivity extends Activity implements CreateNdefMessageCallback,
OnNdefPushCompleteCallback{

	 NfcAdapter mNfcAdapter;
	 TextView mInfoText,mResult,mTextCost,mTextMoney,mTextUserName;
	 private static final int MESSAGE_SENT = 1;
	 private AlertDialog.Builder adb;
	 
	 private MobileServiceClient mClient;
	 private MobileServiceTable<Cost> mCost;
	 private MobileServiceTable<History> mHistory;
	 private MobileServiceTable<Userinfo> mUserinfo;
	 static String pier,Uinfo,frpier,topier;
	 private String key = "xcab43sx";
	 public String NFCText,decrytuser;
	 private ProgressBar mProgressBar;
	 Encryption enc;
	 int cost,usermoney,usermoney2;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		adb = new AlertDialog.Builder(this);
		 mInfoText = (TextView) findViewById(R.id.textView1);
		 mResult = (TextView) findViewById(R.id.result1);
		 mTextUserName = (TextView) findViewById(R.id.textView4);
		 mTextCost = (TextView) findViewById(R.id.textView2);
		 mTextMoney = (TextView) findViewById(R.id.textView3);
		 mProgressBar = (ProgressBar) findViewById(R.id.progressBar2);
		 mProgressBar.setVisibility(ProgressBar.GONE);
		 // Check for available NFC Adapter
	        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
	        if (mNfcAdapter == null) {
	         //   mInfoText = (TextView) findViewById(R.id.sx);
	            mInfoText.setText("NFC is not available on this device.");
	        } else {
	            // Register callback to set NDEF message
	            mNfcAdapter.setNdefPushMessageCallback(this, this);
	            // Register callback to listen for message-sent success
	            mNfcAdapter.setOnNdefPushCompleteCallback(this, this);
	        }
	        final Spinner spinner = (Spinner) findViewById(R.id.spinner1); 
	        final Button button1 = (Button) findViewById(R.id.button1);
	        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
	                R.array.PIER, android.R.layout.simple_spinner_item);
	     // Specify the layout to use when the list of choices appears
	        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	        // Apply the adapter to the spinner
	        spinner.setAdapter(adapter);
	        
	        
	        button1.setOnClickListener(new View.OnClickListener() {
	        	
	        	public void onClick(View v) 
	        	{Toast.makeText(MainActivity.this,
	        	
	        	"Your Selected : " + String.valueOf(spinner.getSelectedItem()),
	        	
	        	Toast.LENGTH_SHORT).show();
	        
	        	pier = String.valueOf(spinner.getSelectedItem());
	        	mResult.setText(pier);
	        	}
	        	
	        	});
	        mResult.setText(pier);
	    
	        
	     //   pier = String.valueOf(spinner.getSelectedItem());
	    //	mResult.setText(pier);
	        try {
				mClient = new MobileServiceClient(
						"https://paynfcapp.azure-mobile.net/",
						"qfPJJhnOaXVIQdbHhOZbHrPSDdHpTu13", this).withFilter(new ProgressFilter());
		//		mCost = mClient.getTable(Cost.class);
		//		mHistory = mClient.getTable(History.class);
					} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	      
	        	mCost = mClient.getTable(Cost.class);
	        	mHistory = mClient.getTable(History.class);
	        	mUserinfo = mClient.getTable(Userinfo.class);
	}
  
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	
		if (item.getItemId() == R.id.location) {
			Intent newActivity = new Intent(this,SendLocation.class);  
            startActivity(newActivity);	
		}
		else if (item.getItemId() == R.id.refill) {	
			Intent newActivity = new Intent(this,RefillMoney.class);  
			startActivity(newActivity);	
		}
		
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	
	public NdefMessage createNdefMessage(NfcEvent event) {
	        Time time = new Time();
	        time.setToNow();
	        String text = ("Beam Time: " + time.format("%H:%M:%S"));
	        NdefMessage msg = new NdefMessage(NdefRecord.createMime(
	                "application/com.example.android.beam", text.getBytes())
	         /**
	          * The Android Application Record (AAR) is commented out. When a device
	          * receives a push with an AAR in it, the application specified in the AAR
	          * is guaranteed to run. The AAR overrides the tag dispatch system.
	          * You can add it back in to guarantee that this
	          * activity starts when receiving a beamed message. For now, this code
	          * uses the tag dispatch system.
	          */
	          //,NdefRecord.createApplicationRecord("com.example.android.beam")
	        );
	        return msg;
	    }

	    /**
	     * Implementation for the OnNdefPushCompleteCallback interface
	     */
	    @Override
	    public void onNdefPushComplete(NfcEvent arg0) {
	        // A handler is needed to send messages to the activity when this
	        // callback occurs, because it happens from a binder thread
	       // mHandler.obtainMessage(MESSAGE_SENT).sendToTarget();
	    }
	    /** This handler receives a message from onNdefPushComplete */
	    private final Handler mHandler = new Handler() {
	        @Override
	        public void handleMessage(Message msg) {
	            switch (msg.what) {
	            case MESSAGE_SENT:
	                Toast.makeText(getApplicationContext(), "Message sent!", Toast.LENGTH_LONG).show();
	                break;
	            }
	        }
	    };

	    @Override
	    public void onResume() {
	        super.onResume();
	        // Check to see that the Activity started due to an Android Beam
	        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
	            processIntent(getIntent());
	        }
	    }

	    @Override
	    public void onNewIntent(Intent intent) {
	        // onResume gets called after this to handle the intent
	        setIntent(intent);
	    }

	    /**
	     * Parses the NDEF Message from the intent and prints to the TextView
	     */
	    void processIntent(Intent intent) {
	        Parcelable[] rawMsgs = intent.getParcelableArrayExtra(
	                NfcAdapter.EXTRA_NDEF_MESSAGES);
	        // only one message sent during the beam
	        NdefMessage msg = (NdefMessage) rawMsgs[0];
	        // record 0 contains the MIME type, record 1 is the AAR, if present
	        NFCText =  new String(msg.getRecords()[0].getPayload());
	        try {
				enc = new Encryption(key);
				decrytuser = enc.decrypt(NFCText);
	        } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		//	Log.i(tag, "masage");
	        }
	    //    mInfoText.setText(decrytuser);
	       
	    	mResult.setText(pier);
	
	    	if (mClient == null) {
        		AlertDialog ad = adb.create();
        		ad.setMessage("Cannot connect to Windows Azure Mobile Service!");
        		ad.show();
        	}
        	else 
        		
        		
        // user in check from to get cost
        		
        	{ mHistory.where().field("UserID").eq(decrytuser).and().field("Status").eq("in").execute(new TableQueryCallback<History>() {
        		public void onCompleted(List<History> result, int count, Exception exception, 
						ServiceFilterResponse response) {
					
					if (exception == null) {
						if(result.size() > 0)
						{  History item = result.get(0);
							item.setmTo(pier);
							frpier = item.getmFrom();
							topier = pier;
							item.setmStatus("out");
							mInfoText.setText(frpier+" to "+topier);
							mHistory.update(item, new TableOperationCallback<History>() {

		            			public void onCompleted(History entity, Exception exception, ServiceFilterResponse response) {
		            				
		            				if (exception == null) {
		            					Toast.makeText(MainActivity.this,	
		            				        	"Update Data Successfully.",
		            				        	Toast.LENGTH_SHORT).show();	        
		            				} else {
		            					Toast.makeText(MainActivity.this,	
		            				        	"Cannot Update Data.",
		            				        	Toast.LENGTH_SHORT).show();	            				        
		            				}	            				
		            			}	            			
		            		});
					
							mUserinfo.where().field("id").eq(decrytuser).and().field("Status").eq("in").execute(new TableQueryCallback<Userinfo>() {
				        		public void onCompleted(List<Userinfo> result, int count, Exception exception, 
										ServiceFilterResponse response) {
									
									if (exception == null) {
										if(result.size() > 0)

					    		{ 			Userinfo item = result.get(0);
					    					item.setStatus("out");
					    					
					    					mTextUserName.setText("User : "+item.getUsername());
					    					usermoney = item.getBalance();
					    					
					    					usermoney = usermoney - cost;
					    					item.setBalance(usermoney);
										//	mTextMoney.setText(""+usermoney);
											
											mUserinfo.update(item, new TableOperationCallback<Userinfo>() {

								            			public void onCompleted(Userinfo entity, Exception exception, ServiceFilterResponse response) {
								            				
								            				if (exception == null) {
								            					Toast.makeText(MainActivity.this,	
								            				        	"Update Data Successfully.",
								            				        	Toast.LENGTH_SHORT).show();		            				        
								            				} else {
								            					Toast.makeText(MainActivity.this,	
								            				        	"Cannot Update Data.",
								            				        	Toast.LENGTH_SHORT).show();			            				        
								            				}			            				
								            			}			            			
								            		});
									} 
								}     		
				        	}});
							
							mCost.where().field("name").eq(frpier.toString())						
							.execute(new TableQueryCallback<Cost>(){
								public void onCompleted(List<Cost> result, int count, Exception exception, 
										ServiceFilterResponse response) {
									
									if (exception == null) {
										
										if(result.size() > 0)
										{  Cost item = result.get(0);
										
										
											
											
									//		mTextCost.setText("test" +item.getmPANFA_LEELARD_PIER()); 
									//		xxx = item.getmPANFA_LEELARD_PIER();
											
											if (topier.equals("PANFA LEELARD PIER")) {
												cost = item.getmPANFA_LEELARD_PIER();
										//		mTextCost.setText(String.valueOf("Cost "+cost));
												}
											
											else if(topier.equals("TALAD BOBAE PIER")) {
												cost =	item.getmTALAD_BOBAE_PIER();
										//		mTextCost.setText(String.valueOf("Cost "+cost));
												}
											
											else if(topier.equals("SAPAN CHAROENPOL PIER")) {
												cost =	item.getmSAPAN_CHAROENPOL_PIER();
										//		mTextCost.setText(String.valueOf("Cost "+cost));
													}
											
											else if(topier.equals("SAPAN HUA CHANG PIER")) {
												cost =	item.getmSAPAN_HUA_CHANG_PIER();
											//	mTextCost.setText(String.valueOf("Cost "+cost));
												}
											
											else if(topier.equals("PRATUNAM PIER")) {
												cost =	item.getmPRATUNAM_PIER();
											//	mTextCost.setText(String.valueOf("Cost = "+cost));
												}
											
											else if(topier.equals("CHIDLOM PIER")) {
												cost =	item.getmCHIDLOM_PIER();
											//	mTextCost.setText(String.valueOf("Cost = "+cost));
												}
											
											else if(topier.equals("WIRELESS PIER")) {
												cost =	item.getmWIRELESS_PIER();
											//	mTextCost.setText(String.valueOf("Cost = "+cost));
												}
											
											else if(topier.equals("NANA NUA PIER")) {
												cost =	item.getmNANA_NUA_PIER();
											//	mTextCost.setText(String.valueOf("Cost "+cost));
												}
											
											else if(topier.equals("NANACHARD PIER")) {
												cost =	item.getmNANACHARD_PIER();
										//		mTextCost.setText(String.valueOf("Cost "+cost));
												}
											
											else if(topier.equals("ASOKE PIER")) {
												cost =	item.getmASOKE_PIER();
										//		mTextCost.setText(String.valueOf("Cost "+cost));
												}
											
											else if(topier.equals("PRASANMIT PIER")) {
												cost =	item.getmPRASANMIT_PIER();
											//	mTextCost.setText(String.valueOf("Cost "+cost));
												}
											
											else if(topier.equals("ITAL THAI PIER")) {
												cost =	item.getmITAL_THAI_PIER();
											//	mTextCost.setText(String.valueOf("Cost "+cost));
												}
											
											else if(topier.equals("WAT MAI CHONGLOM PIER")) {
												cost =	item.getmWAT_MAI_CHONGLOM_PIER();
										//		mTextCost.setText(String.valueOf("Cost "+cost));
												}
											
											else if(topier.equals("BAANDON MOSQUE PIER")) {
												cost =	item.getmBAANDON_MOSQUE_PIER();
											//	mTextCost.setText(String.valueOf("Cost "+cost));
												}
											
											else if(topier.equals("SOI THONGLOR PIER")) {
												cost =	item.getmSOI_THONGLOR_PIER();
											//	mTextCost.setText(String.valueOf("Cost "+cost));
												}
											
											else if(topier.equals("CHARN ISSARA PIER")) {
												cost =	item.getmCHARN_ISSARA_PIER();
											//	mTextCost.setText(String.valueOf("Cost "+cost));
												}
											
											else if(topier.equals("VITJITTRA SCHOOL PIER")) {
												cost =	item.getmVITJITTRA_SCHOOL_PIER();
										//		mTextCost.setText(String.valueOf("Cost "+cost));
												}
											
											else if(topier.equals("SAPAN KLONGTUN PIER")) {
												cost =	item.getmSAPAN_KLONGTUN_PIER();
											//	mTextCost.setText(String.valueOf("Cost "+cost));
												}
											
											else if(topier.equals("THE MALL RAM 3 PIER")) {
												cost =	item.getmTHE_MALL_RAM_3_PIER();
											//	mTextCost.setText(String.valueOf("Cost "+cost));
												}
											
											else if(topier.equals("RAMKHAMHAENG 29 PIER")) {
												cost =	item.getmRAMKHAMHAENG_29_PIER();
											//	mTextCost.setText(String.valueOf("Cost "+cost));
												}
											
											else if(topier.equals("WAT THEPLEELA PIER")) {
												cost =	item.getmWAT_THEPLEELA_PIER();
											//	mTextCost.setText(String.valueOf("Cost "+cost));
												}
											
											else if(topier.equals("RAMKHAMHAENG 53 PIER")) {
												cost =	item.getmRAMKHAMHAENG_53_PIER();
											//	mTextCost.setText(String.valueOf("Cost "+cost));
												}
											
											else if(topier.equals("SAPAN MIT MAHADTHAI PIER")) {
												cost =	item.getmSAPAN_MIT_MAHADTHAI_PIER();
											//	mTextCost.setText(String.valueOf("Cost "+cost));
												}
											
											else if(topier.equals("WAT KLANG PIER")) {
												cost =	item.getmWAT_KLANG_PIER();
											//	mTextCost.setText(String.valueOf("Cost "+cost));
												}
											
											else if(topier.equals("THE MALL BANGKAPI PIER")) {
												cost =	item.getmTHE_MALL_BANGKAPI_PIER();
											//	mTextCost.setText(String.valueOf("Cost "+cost));
												}
											
											else if(topier.equals("BANGKAPI PIER")) {
												cost =	item.getmBANGKAPI_PIER();
											//	mTextCost.setText(String.valueOf("Cost "+cost));
												}
											
											else if(topier.equals("WAT SRIBOONREUNG PIER")) {
												cost =	item.getmWAT_SRIBOONREUNG_PIER();
											//	mTextCost.setText(String.valueOf("Cost = "+cost));
												}
											
											else if(topier == null) {
												
												mTextCost.setText(String.valueOf("No to Pier"));
												}
											else {
												mTextCost.setText(String.valueOf("Error : Cannot connect to azure"));
												
											}
											mTextCost.setText(String.valueOf("Cost = "+cost));
											//usermoney2 = usermoney - cost;
											//mTextMoney.setText(usermoney2);							
											}
										}
						                    
								}});
						
						
							
							
							
						} 
				}       		
        	}});

        	};
        	
//        	before user in
        	
        	{mUserinfo.where().field("id").eq(decrytuser).and().field("Status").eq("out").execute(new TableQueryCallback<Userinfo>() {
        		public void onCompleted(List<Userinfo> result, int count, Exception exception, 
						ServiceFilterResponse response) {
					
					if (exception == null) {
						if(result.size() > 0)

	    		{   		History item = new History();
	    					Userinfo item2 = result.get(0);
	    					
	    					mTextUserName.setText("User : "+item2.getUsername());
	    					
	    					item2.setStatus("in");
							item.setmFrom(pier);
						//	frpier = pier;
							item.setmStatus("in");
							item.setmUserID(decrytuser);
							item.setmBalance(item2.getBalance());
							mUserinfo.update(item2, new TableOperationCallback<Userinfo>() {

				            			public void onCompleted(Userinfo entity, Exception exception, ServiceFilterResponse response) {
				            				
				            				if (exception == null) {
				            					Toast.makeText(MainActivity.this,	
				            				        	"Update Data Successfully.",
				            				        	Toast.LENGTH_SHORT).show();
				            				        
				            				} else {
				            					Toast.makeText(MainActivity.this,	
				            				        	"Cannot Update Data.",
				            				        	Toast.LENGTH_SHORT).show();			            				        
				            				}			            				
				            			}			            			
				            		});
							mHistory.insert(item, new TableOperationCallback<History>() {

		            			public void onCompleted(History entity, Exception exception, ServiceFilterResponse response) {
		            				
		            				if (exception == null) {	                   
		                        		Toast.makeText(MainActivity.this,	
		                        				"Insert History Data Successfully.",
		            				        	Toast.LENGTH_SHORT).show();
		            				} else {
		                        		Toast.makeText(MainActivity.this,	
		            				        	"Cannot Insert Data.",
		            				        	Toast.LENGTH_SHORT).show();
            				}            				
		            			}            			
		            		});

					} 
				}
        		
        	}});
        	} ;
	 	
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
	    
	    public void onBackPressed() {
			   Intent intent = new Intent(Intent.ACTION_MAIN);
			   intent.addCategory(Intent.CATEGORY_HOME);
			   intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			   startActivity(intent);
			 }
	    
}

