//http://findevelop.blogspot.com/2011/07/android_12.html
//http://ontabs.com/programming/1409-sozdaem-svoy-vidzhet-dlya-android.html
//http://habrahabr.ru/post/114515/
//http://startandroid.ru/ru/uroki/vse-uroki-spiskom/195-urok-117-vidzhety-sozdanie-lifecycle.html

package in.alexsoft.power.on;

//utils - https://github.com/rorist/android-network-discovery/tree/master/src/info/lamatricexiste/network

//String in value.xml
//http://habrahabr.ru/post/133503/


//сканирование сети - 
//https://github.com/rorist/android-network-discovery/blob/master/src/info/lamatricexiste/network/DnsDiscovery.java
//https://github.com/rorist/android-network-discovery/blob/master/src/info/lamatricexiste/network/Network/HardwareAddress.java#L60
//http://stackoverflow.com/questions/10404734/get-an-information-about-all-connected-devices-in-the-wifi-connection
//http://stackoverflow.com/questions/15454623/network-scan-using-android-takes-much-more-time-how-can-i-shorten-the-scan-time
//http://stackoverflow.com/questions/9504721/getting-ip-addresses-of-the-pcs-available-on-wifi-network-in-android
//http://stackoverflow.com/questions/15720558/device-discovery-in-local-network
//http://stackoverflow.com/questions/12386948/how-to-detect-all-the-devices-connected-in-a-wifi-network-from-android-app


import in.alexsoft.power.on.net.ActivityMain;

import java.io.IOException;
import java.net.SocketException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.Settings;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class main extends Activity implements OnClickListener
{

	 private IPAddressValidator ipAddressValidator;
	 private MACAddressValidator macAddressValidator;
	 private KeyguardManager mKeyGuardManager;
	 private KeyguardLock mLock;
	 
	
	  private Context context = this;
	  Button poweron, chup, poweroff, voldown,fullscreen, volup, mute, chdown, settings, netsettings;
	  private Vibrator mVibrator;
	  private static final int VIBRATE_MILLIS = 50;
	  
	  private static final int no_network = 0; 
   	  private static final int wifi = 1;
   	  private static final int GGG = 2;
   	  
   	  String defaultIP = "192.168.1.0";
   	 
     int _poweroff = 1;
     int _chup = 2;
     int _voldown = 3;
     int _fullscreen = 4;
     int _volup = 5;
     int _chdown = 6;
     int _mute = 7;
     
     int _shutdown = 8; //good
     int _reboot = 9;
     int _hibernate = 10;
     int _standby = 11;
     
     String DEFpoweroff = "1";
     String DEFshutdown = "8";
     String DEFreboot = "9";
     String DEFhibernate = "10";
     String DEFstandby = "11";
     
     FtpLibrary ftp = new FtpLibrary();
	  
	  @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.mymain);
	        
	        poweron = (Button)findViewById(R.id.poweron);
	        chup = (Button)findViewById(R.id.chup);
	        poweroff = (Button)findViewById(R.id.poweroff);
	        voldown = (Button)findViewById(R.id.voldown);
	        fullscreen = (Button)findViewById(R.id.fullscreen);
	        volup = (Button)findViewById(R.id.volup);
	        mute = (Button)findViewById(R.id.mute);
	        chdown = (Button)findViewById(R.id.chdown);
	        settings = (Button)findViewById(R.id.settings);
	        
	        netsettings = (Button)findViewById(R.id.netsettings);
	        
	        poweron.setOnClickListener(this);
	        chup.setOnClickListener(this);
	        poweroff.setOnClickListener(this);
	        voldown.setOnClickListener(this);
	        fullscreen.setOnClickListener(this);
	        volup.setOnClickListener(this);
	        mute.setOnClickListener(this);
	        chdown.setOnClickListener(this);
	        settings.setOnClickListener(this);
	        netsettings.setOnClickListener(this);
	        
	        mVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
	        
	        //off blocking screen!!!
	        mKeyGuardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
	        mLock = mKeyGuardManager.newKeyguardLock("VidViewerActivity");
	        mLock.disableKeyguard();
	        
	  }
	  
	  //http://www.goodroid.ru/articles/programmirovanie/activity-lifecycle.html
	    
	    @Override
	    protected void onResume() 
	    {
	    	//Toast.makeText(context, "ONResume" , Toast.LENGTH_LONG).show();
	    	 try {
	    		 mLock.disableKeyguard();
	   		} catch (Exception e) {
	   			Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
	   		}
	     	  super.onResume();
	    	
	    }
	    
	    @Override
	    protected void onRestart()
	    {
	    	//Toast.makeText(context, "onRestart" , Toast.LENGTH_LONG).show();
	    	try {
	    		mLock.disableKeyguard();
	   		} catch (Exception e) {
	   			Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
	   		}
	    	super.onRestart();
	    }
	    
	    @Override
	    public void onPause()
	    {   	    	
	    	mLock.reenableKeyguard();
	    	//Toast.makeText(context, "onPause" , Toast.LENGTH_LONG).show();
	        super.onPause();
	       
	    }
	    
	    @Override
	    public void onStop()
	    {   
	    	
	    	//Toast.makeText(context, "onStop" , Toast.LENGTH_LONG).show();
//	    	if (!Prefs.getReady(context))
//	        {
//	    		stopService(new Intent(this, ServiceExample.class));            
//	    	    stopNotificationsIcon();
//	        }        
	    	mLock.reenableKeyguard();
	        super.onStop();  
	    }
	    
	    @Override
	    public void onDestroy()
	    {       
	    	//Toast.makeText(context, "onDestroy" , Toast.LENGTH_LONG).show();
	    	mLock.reenableKeyguard();
	        super.onDestroy();	       
	    }
	    
	  
		public void onClick(View v) 
		{
			if (v.getId() == R.id.settings)//отдельно для кнопки настроек
			{
				startActivity(new Intent(this, Prefs.class));				
				return;
			}	
			
			if (v.getId() == R.id.netsettings)//отдельно для кнопки настроек
			{
				startActivity(new Intent(this, ActivityMain.class));   				
				return;
			}
			
			
			 		
			
			//check wifi connection
			if(!CheckWiFi())
			{
			 Toast.makeText(context, getResources().getString(R.string.needwifi) , Toast.LENGTH_LONG).show();
				
			 AlertDialog.Builder builder = new AlertDialog.Builder(this);		 
			 builder.setMessage(getResources().getString(R.string.needwifi))
			        .setCancelable(false)
			        .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
			            public void onClick(DialogInterface dialog, int id) 
			            {
			            	//YESSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS
			            	context.startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
			            }
			        })
			        .setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
			            public void onClick(DialogInterface dialog, int id) 
			            {
			                 dialog.cancel();
			                 return;
			            }
			        });
			 AlertDialog alert = builder.create();
			 alert.show();
			 return;
			}
			
			//power on PC button pressed
			if (v.getId() == R.id.poweron)		
			{	
				if (Prefs.getMac(context).equals("XX-XX-XX-XX-XX-XX"))
				{
					Toast.makeText(context, getResources().getString(R.string.entermac), Toast.LENGTH_LONG).show();
					ChangeServerMAC(true);
					return;
				}
				if (!CheckValidMac())
				{
					Toast.makeText(context, Prefs.getMac(context) + " - " + getResources().getString(R.string.notvalidmac) , Toast.LENGTH_LONG).show();
					ChangeServerMAC(false);
				}
				WakeOnLan.WakeUp("255.255.255.255", Prefs.getMac(context));
				return;
			}
			
			if (!CheckIpServerSet())
			{
				ChangeServerIp(true);		    						
				return;
			}
			
			if (!CheckValidIP())
			{				
				Toast.makeText(context, getResources().getString(R.string.ipnotcorrect) + Prefs.getFtpServer(context), Toast.LENGTH_LONG).show();
				Prefs.setNewIpAdress(context, defaultIP);
				ChangeServerIp(true);
				return;
				  
			}
			
			Vibrate();
						
			switch(v.getId())
	    	{				
				case R.id.chup:
				{	
					operate(_chup);
					break;
				}				
				
				case R.id.poweroff:
				{	
					if (Prefs.getVideoPlayerDefault(context).equals(DEFpoweroff))						
						operate(_poweroff);
					if (Prefs.getVideoPlayerDefault(context).equals(DEFshutdown))						
						operate(_shutdown);
					if (Prefs.getVideoPlayerDefault(context).equals(DEFreboot))						
						operate(_reboot);
					if (Prefs.getVideoPlayerDefault(context).equals(DEFhibernate))						
						operate(_hibernate);
					if (Prefs.getVideoPlayerDefault(context).equals(DEFstandby))						
						operate(_standby);
					
					break;
				}
				
				case R.id.voldown:
				{	
					operate(_voldown);
					break;
				}
				
				case R.id.fullscreen:
				{	
					operate(_fullscreen);
					break;
				}
				
				case R.id.volup:
				{	
					operate(_volup);
					break;
				}
				
				case R.id.mute:
				{	
					operate(_mute);
					break;
				}
				
				case R.id.chdown:
				{	
					operate(_chdown);
					break;
				}					
			}				
		}
		
		private String GetTemplateIp()
		{			
			if (!CheckIpServerSet())//ip not set - default ip 192.168.1.0 
			{
				//get local ip
				if (myUtils.getIPAddress(true) != null && !myUtils.getIPAddress(true).equals(""))
					return RemoveLastDigitIp(myUtils.getIPAddress(true));
			}
			else
				return Prefs.getFtpServer(context);
				
			return RemoveLastDigitIp("192.168.1.0");
		}
		
		private boolean CheckValidMac() //TODO
		{
			//	String MAC_PATTERN = "^([0-9A-F]{2}[:-]){5}([0-9A-F]{2})$";
			macAddressValidator = new MACAddressValidator();
			return macAddressValidator.validate(Prefs.getMac(context));
		}
		
		private void ChangeServerMAC(boolean isNew)
		   {   
			   AlertDialog.Builder alert = new AlertDialog.Builder(this);                 
			   alert.setTitle(getResources().getString(R.string.macadressserver));
			   if (isNew)
			   {
				   alert.setMessage(getResources().getString(R.string.macadresst));  
			   }
			   else
			   {
				   alert.setMessage(getResources().getString(R.string.macadresst) + " \n" + getResources().getString(R.string.macnotcorrect) + " \n" + getResources().getString(R.string.macnow) + " "  + Prefs.getMac(context));   
			   }
			   // Set an EditText view to get user input
			   final EditText input = new EditText(this);  
			   input.setText(Prefs.getMac(context)); //TODO - определить mac по ip!!!!! 
			   alert.setView(input);

			       alert.setPositiveButton(getResources().getString(R.string.mac_title), new DialogInterface.OnClickListener() {  
			       public void onClick(DialogInterface dialog, int whichButton) {  
			           //String value = input.getText().toString();
			           Prefs.setNewMacAdress(context, input.getText().toString());
			           //Toast.makeText(context, "Pin Value : " + value, Toast.LENGTH_LONG).show();
			           return;                  
			          }  
			        });  
			       
			       alert.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {

			           public void onClick(DialogInterface dialog, int which) {
			               
			               return;   
			           }
			       });
			               alert.show();			               
		   }
		
		private boolean CheckValidIP()
		{
			ipAddressValidator = new IPAddressValidator();
			return ipAddressValidator.validate(Prefs.getFtpServer(context));			
		}
		
		private String RemoveLastDigitIp(String ip)
		{//remove last digit from IP + .?
			if (ip == null)
				return "";
			
			String[] result = ip.split("[.]");
			if (result.length == 4)
				return result[0] + "." + result[1] + "." + result[2] + ".?";	
			
			return ip;			
		}
		

		private void ChangeServerIp(boolean isNew)
		   {   
			   AlertDialog.Builder alert = new AlertDialog.Builder(this);                 
			   alert.setTitle(getResources().getString(R.string.ipadressserver));
			   if (isNew)
			   {
				   alert.setMessage(getResources().getString(R.string.ipadresstip));  
			   }
			   else
			   {
				   alert.setMessage(getResources().getString(R.string.ipadresstip) + " \n" + getResources().getString(R.string.ipnotcorrect) + " \n" + getResources().getString(R.string.ipnow) + " "  + Prefs.getFtpServer(context));   
			   }
			                   

			    // Set an EditText view to get user input   
			    final EditText input = new EditText(this);
			    input.setText(GetTemplateIp());
			    alert.setView(input);

			       alert.setPositiveButton(getResources().getString(R.string.ipadress), new DialogInterface.OnClickListener() {  
			       public void onClick(DialogInterface dialog, int whichButton) {  
			           //String value = input.getText().toString();
			           Prefs.setNewIpAdress(context, input.getText().toString());
			           //Toast.makeText(context, "Pin Value : " + value, Toast.LENGTH_LONG).show();
			           return;                  
			          }  
			        });  
			       
			       alert.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {

			           public void onClick(DialogInterface dialog, int which) {
			               
			               return;   
			           }
			       });
			               alert.show();			               
		   }
		
		 private boolean CheckIpServerSet()
		    {
		    	return (!Prefs.getFtpServer(context).equals(defaultIP));
		    }	
		

	private  int operate(int code)
	{
		int result = 0;
		try {
			if (!FtpLibrary.connect(context))
			{					
				return -3;
			}							
			
			result = FtpLibrary.sendCommand(code);
			
			FtpLibrary.disconnect();
			

		} catch (SocketException e) {
			Toast.makeText(context, "1 SocketException = " + e.getMessage(), Toast.LENGTH_LONG).show();			
			e.printStackTrace();
		} catch (IOException e) {			
			Toast.makeText(context, "2 IOException = " + e.getMessage(), Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
		catch (Exception e) {			
			Toast.makeText(context, "3 Exception = " + e.getMessage(), Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
		//Toast.makeText(context, "result = " + result, Toast.LENGTH_SHORT).show();
		//Toast.makeText(context, "*5*", Toast.LENGTH_LONG).show();		
		return result;
		
	}
		
		 private boolean CheckWiFi()
		    {
			 	return  (haveNetworkConnectionType(context) == wifi); //TODO test TRUE
		    }
		
		 private void Vibrate()
			{
				  {
		             mVibrator.vibrate(
		                 new long[]{0l, VIBRATE_MILLIS},
		                     -1);
		         }
			}
		 
		 private int  haveNetworkConnectionType(Context context)
		    {
				//return int
				//0 - no network
				//1 - only wifi
				//2 - only 3G
				int WIFI = 1;
				int GGG = 2;
				int type = 0;
				
		        boolean haveConnectedWifi = false;
		        boolean haveConnectedMobile = false;

		        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
		        for (NetworkInfo ni : netInfo)
		        {
		            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
		            {
		                if (ni.isConnected())
		                {
		                    haveConnectedWifi = true;
		                    //Log.v("WIFI CONNECTION ", "AVAILABLE");
		                    //Toast.makeText(this,"WIFI CONNECTION is Available", Toast.LENGTH_LONG).show();
		                    type = WIFI; 
		                } else
		                {
		                   // Log.v("WIFI CONNECTION ", "NOT AVAILABLE");
		                    //Toast.makeText(this,"WIFI CONNECTION is NOT AVAILABLE", Toast.LENGTH_LONG).show();
		                }
		            }
		            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
		            {
		                if (ni.isConnected())
		                {
		                    haveConnectedMobile = true;
		                   // Log.v("MOBILE INTERNET CONNECTION ", "AVAILABLE");
		                    //Toast.makeText(this,"MOBILE INTERNET CONNECTION - AVAILABLE", Toast.LENGTH_LONG).show();
		                    type = GGG;
		                } else
		                {
		                  // Log.v("MOBILE INTERNET CONNECTION ", "NOT AVAILABLE");
		                    //Toast.makeText(this,"MOBILE INTERNET CONNECTION - NOT AVAILABLE", Toast.LENGTH_LONG).show();
		                }
		            }
		        }
		        if (!haveConnectedWifi && !haveConnectedMobile)
		        	return 0;
		        
		        return type;
		    }

	
	}