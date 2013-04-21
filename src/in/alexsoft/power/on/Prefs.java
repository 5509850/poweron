package in.alexsoft.power.on;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;


public class Prefs extends PreferenceActivity 
{
	
	
	private static final String OPT_FTP = "serverftp" ;
	private static final String OPT_FTP_DEF = "192.168.1.0";
	
	private static final String OPT_PORTFTP = "portftp" ;
	private static final String OPT_PORTFTP_DEF = "22";
	
	private static final String OPT_NAMEFTP = "nameftp" ;
	private static final String OPT_NAMEFTP_DEF = "qwe#sdf6AsdfgzTs";
	
	private static final String OPT_PASSFTP = "passftp" ;
	private static final String OPT_PASSFTP_DEF = "FgskTgv617ssdf";
	
	
	
	private static final String OPT_MAC = "mac" ;
	private static final String OPT_MAC_DEF = "XX-XX-XX-XX-XX-XX";
	
	private static final String OPT_OFF= "off" ;
	private static final String OPT_OFF_DEF = "1";
	
	@Override
   protected void onCreate(Bundle savedInstanceState) 
	{
      super.onCreate(savedInstanceState);
      addPreferencesFromResource(R.xml.settings);
    }
	
	public static String getVideoPlayerDefault(Context context) 
	{
	   return PreferenceManager.getDefaultSharedPreferences(context)
	         .getString(OPT_OFF, OPT_OFF_DEF);
	}
		
	public static void setNewPass(Context context, String Newvalue) 
	{
	   // PreferenceManager.getDefaultSharedPreferences(context)
	     //    .getBoolean(OPT_REC, OPT_REC_DEF);
	    
		Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();		
		editor.putString(OPT_PASSFTP, SplitIpClear(Newvalue));
		editor.commit();
	}
	
	public static void setNewIpAdress(Context context, String Newvalue) 
	{
	   // PreferenceManager.getDefaultSharedPreferences(context)
	     //    .getBoolean(OPT_REC, OPT_REC_DEF);
	    
		Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();		
		editor.putString(OPT_FTP, SplitIpClear(Newvalue));
		editor.commit();
	}	
	
	public static void setNewMacAdress(Context context, String Newvalue) 
	{
	   // PreferenceManager.getDefaultSharedPreferences(context)
	     //    .getBoolean(OPT_REC, OPT_REC_DEF);
	    
		Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();		
		editor.putString(OPT_MAC, SplitMacClear(Newvalue));
		editor.commit();
	}
	
	//------------------------------------------------------------------------
	
	
	public static String getFtpServer(Context context) 
	{
	   return SplitIpClear(PreferenceManager.getDefaultSharedPreferences(context)
	         .getString(OPT_FTP, OPT_FTP_DEF));
	}
	
	public static String getFtpPort(Context context) 
	{
	   return PreferenceManager.getDefaultSharedPreferences(context)
	         .getString(OPT_PORTFTP, OPT_PORTFTP_DEF);
	}
	
	public static String getFtpLogin(Context context) 
	{
	   return PreferenceManager.getDefaultSharedPreferences(context)
	         .getString(OPT_NAMEFTP, OPT_NAMEFTP_DEF);
	}
	
	public static String getFtpPass(Context context) 
	{
	   return SplitIpClear(PreferenceManager.getDefaultSharedPreferences(context)
	         .getString(OPT_PASSFTP, OPT_PASSFTP_DEF));
	}
	
	
	public static String getMac(Context context) 
	{
	   return SplitIpClear(PreferenceManager.getDefaultSharedPreferences(context)
	         .getString(OPT_MAC, OPT_MAC_DEF));
	}
	
	//очищаем ip адрес от пробелов
	private static String SplitIpClear(String ip)
    {
    	
    	if (ip == null || ip.equals(""))
    		return "192.168.1.0";
    	try {    	          
    	          return  ip.replaceAll("\\s", "");
    	}
    	catch (Exception ex)
    	{
    		return "192.168.1.0";
    	}  	
    }
	
	//очищаем mac адрес от пробелов
		private static String SplitMacClear(String ip)
	    {
	    	
	    	if (ip == null || ip.equals(""))
	    		return "XX-XX-XX-XX-XX-XX";
	    	try {    	          
	    	          return  ip.replaceAll("\\s", "").replaceAll("o","0").replaceAll("O","0").replaceAll("a","A").replaceAll("b","B").replaceAll("c","C").replaceAll("d","D").replaceAll("e","E").replaceAll("f","F");
	    	}
	    	catch (Exception ex)
	    	{
	    		return "XX-XX-XX-XX-XX-XX";
	    	}  	
	    }
	
}