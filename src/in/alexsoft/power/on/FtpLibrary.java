package in.alexsoft.power.on;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;


import android.content.Context;
import android.widget.Toast;

public final class FtpLibrary {
	
	private static FTPClient mFtp; 
	
	public FtpLibrary()
	{
		mFtp = new FTPClient();
	}
	
	public static boolean isActive()
	{
		return (mFtp != null);
		
	}
	
	public static boolean connect(Context context) throws SocketException, IOException
	{
		boolean result = false;
		String userid = Prefs.getFtpLogin(context);
		String pwd = Prefs.getFtpPass(context);;
		InetAddress server = null;
		try {
			//server = InetAddress.getLocalHost();
			server = InetAddress.getByName(Prefs.getFtpServer(context));
		} catch (UnknownHostException e) {
			Toast.makeText(context, "UnknownHostException = " + e.getMessage(), Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
		catch (Exception e) {
			Toast.makeText(context, "0  Exception = " + e.getMessage(), Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
		int port = Integer.valueOf(Prefs.getFtpPort(context));			
		try{
			if (!isActive())
				mFtp = new FTPClient();
			mFtp.connect(server, port); // Using port no=21	
		}
		catch (Exception e) {
			Toast.makeText(context, "-7 Exception = " + e.getMessage(), Toast.LENGTH_LONG).show();
			return false;
		}
		
		result = mFtp.login(userid, pwd);
		return result;
	}
	
	public static int CheckConnect(Context context) throws SocketException, IOException
	{
		//-7 не найден хост (не верный ip)
		//-1 не верный пароль (код)
		//0 = ОК		
		String userid = Prefs.getFtpLogin(context);
		String pwd = Prefs.getFtpPass(context);;
		InetAddress server = null;
		try {
			//server = InetAddress.getLocalHost();
			server = InetAddress.getByName(Prefs.getFtpServer(context));
		} catch (UnknownHostException e) {
			Toast.makeText(context, "UnknownHostException = " + e.getMessage(), Toast.LENGTH_LONG).show();			
		}
		catch (Exception e) {			
			Toast.makeText(context, "Exception = " + e.getMessage(), Toast.LENGTH_LONG).show();
		}
		int port = Integer.valueOf(Prefs.getFtpPort(context));			
		try{
			
				
			mFtp.connect(server, port); // Using port no=22	
		}
		catch (Exception e) {			
			return -7;
		}
		
		if (mFtp.login(userid, pwd))
		{
			mFtp.disconnect();
			return 0;
		}
		else
		{
			mFtp.disconnect();
			return -1;
		}
	}
	
	public static String CheckServerConnect(Context context) throws SocketException, IOException
	{
		boolean result = false;
		String userid = Prefs.getFtpLogin(context);
		String pwd = Prefs.getFtpPass(context);;
		InetAddress server = null;
		try {
			//server = InetAddress.getLocalHost();
			server = InetAddress.getByName(Prefs.getFtpServer(context));
		} catch (UnknownHostException e) {
			Toast.makeText(context, "UnknownHostException = " + e.getMessage(), Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
		int port = Integer.valueOf(Prefs.getFtpPort(context));			
		
		mFtp.connect(server, port); // Using port no=21
		//result = mFtp.login(userid, pwd);
		
		return mFtp.getStatus();
	}
	
	//upload one file
	public static boolean upload(String remoteFileName, InputStream aInputStream) throws Exception
	{
		mFtp.setFileType(FTP.BINARY_FILE_TYPE);
		mFtp.enterLocalPassiveMode();		
		boolean aRtn= mFtp.storeFile(remoteFileName, aInputStream);
		aInputStream.close();
		return aRtn;
	}
	
	//upload all files from folder in SD card! //upload ONLY file *.in
	public static void upload(String SourceFolderName, boolean deleteAfterCopy, Context context, int code) throws Exception
	{
		File dir;
		if (SourceFolderName != null && context == null)
		{
			dir = new File(android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + SourceFolderName + "/");
		}
		else
		{	  
			dir = new File(context.getResources().getString(R.string.searchurl) + "/" + code + ".in");
		}
		   	            
         if (dir != null && !dir.exists() && !dir.mkdirs()) {
        	 throw new Exception("local folder not found" + SourceFolderName + " not found");
			}
         if (code == 0) //old methode from SD
         {
        	 File[] filesource = dir.listFiles();
             
    		 for(int i = 0; i < filesource.length; i++) 
             {
                 File from = filesource[i];
                 if (from.isFile())//пропускаем папки
                 {
                	String fileName = from.getName();
                	
                	
                	int dotPos = fileName.lastIndexOf(".");
                	String ext = fileName.substring(dotPos);  
                	
                
                	//пропускаем файлы с отличным расширением от *.in
    	            	if (ext != null && ext.equals(".in"))		            	
    			            	{
    			            		File source = new File(android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + SourceFolderName + "/" + fileName);		
    			             		InputStream aInputStream = null;
    			             		try {
    			             			aInputStream = new FileInputStream(source);
    			             		} catch (FileNotFoundException e) {
    			             			e.printStackTrace();
    			             			throw new Exception("1 - aInputStream = new FileInputStream(source) ");			
    			             		}		
    			             		mFtp.setFileType(FTP.BINARY_FILE_TYPE);
    			             		mFtp.enterLocalPassiveMode();		
    			             		boolean aRtn= mFtp.storeFile(fileName, aInputStream);
    			             		aInputStream.close();
    			             		if (!aRtn)
    			             			throw new Exception("2 - boolean aRtn= mFtp.storeFile(fileName, aInputStream)");
    			             		
    			             		if (deleteAfterCopy)
    			                  	{
    			    	                		try {
    			    		                		if(!from.delete()){
    			    		                			//файл не удален!
    			    		                		}
    			    	                	}
    			    	                	catch (Exception e) {
    			    	                		throw new Exception("3 - error deleting file");
    			    	    				}
    			                  	}          
    			                }
                		
                	}
             
             }//end cycl		
         }
         else //(code != 0) - new methode copy to ftp from Assets
         {
        	 
             if (dir.isFile())//пропускаем папки
             {
            	String fileName = dir.getName();
            	
            	
            	int dotPos = fileName.lastIndexOf(".");
            	String ext = fileName.substring(dotPos);  
            	
            
            	//пропускаем файлы с отличным расширением от *.in
	            	if (ext != null && ext.equals(".in"))		            	
			            	{
			            		File source = dir;		
			             		InputStream aInputStream = null;
			             		try {
			             			aInputStream = new FileInputStream(source);
			             		} catch (FileNotFoundException e) {
			             			e.printStackTrace();
			             			throw new Exception("101 - aInputStream = new FileInputStream(source) ");			
			             		}		
			             		mFtp.setFileType(FTP.BINARY_FILE_TYPE);
			             		mFtp.enterLocalPassiveMode();		
			             		boolean aRtn= mFtp.storeFile(fileName, aInputStream);			             		
			             		aInputStream.close();
			             		if (!aRtn)
			             			throw new Exception("202 - boolean aRtn= mFtp.storeFile(fileName, aInputStream)");      
			                }            		
            	} 	
         }
	}
	
	public static int sendCommand(int code) throws Exception
	{
		try{
			mFtp.setFileType(FTP.BINARY_FILE_TYPE);
     		mFtp.enterLocalPassiveMode();
     		return mFtp.sendCommand("#" + code + "#");
     		             		}
		catch(Exception ex)
		{
			return -666;
		}
			             		
	}
	
	public static void uploadfile(File source) throws Exception
	{              		
			             		InputStream aInputStream = null;
			             		try {
			             			aInputStream = new FileInputStream(source);
			             		} catch (FileNotFoundException e) {
			             			e.printStackTrace();
			             			throw new Exception("1 - aInputStream = new FileInputStream(source) ");			
			             		}		
			             		mFtp.setFileType(FTP.BINARY_FILE_TYPE);
			             		mFtp.enterLocalPassiveMode();
			             		
			             		boolean aRtn= mFtp.storeFile(source.getName(), aInputStream);
			             		aInputStream.close();
			             		if (!aRtn)
			             			throw new Exception("2 - boolean aRtn= mFtp.storeFile(fileName, aInputStream)");
			             		
			             		    		try {
			    		                		if(!source.delete()){
			    		                			//файл не удален!
			    		                		}
			    	                	}
			    	                	catch (Exception e) {
			    	                		throw new Exception("3 - ошибка при удалении файла");
			    	    				}
	}
	
	
	public static void disconnect() throws Exception
	{
		mFtp.disconnect();
	}
	
	public static void download(String SourceFileName, String SourceFolderName, boolean needDelete)	        throws Exception
	    {
			
		File dir = new File(android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + SourceFolderName + "/");  	            
        if (dir != null && !dir.exists() && !dir.mkdirs()) {
       	 throw new Exception("локальная папка не найдена" + SourceFolderName + " не найдена");
			}     
        
        		File file = new File(android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + SourceFolderName + "/" + SourceFileName);
        		
        		OutputStream aOuputStream = null;
        		
        		try {
        			aOuputStream = new FileOutputStream(file);
        		} catch (FileNotFoundException e) {
        			e.printStackTrace();
        			throw new Exception("aInputStream = new FileInputStream(source) ");			
        		}	
        		
        		mFtp.setFileType(FTP.BINARY_FILE_TYPE);
        		mFtp.enterLocalPassiveMode();		
        		boolean aRtn= mFtp.retrieveFile(SourceFileName, aOuputStream);        		
        		aOuputStream.close();        		
        		if (!aRtn)
        		{
        			if (file.exists())
        			{
        				try{
        					file.delete();
        				}
        				catch(Exception ex)
        				{
        					throw new Exception("скачать файл! - не могу удалить пустой файл");
        				}
        				
        			}
        			throw new Exception("не могу скачать файл!");
        		}
        			
        		else
        			if(needDelete)
        			{
        				if (!mFtp.deleteFile(SourceFileName))
        					throw new Exception("не могу удалить файл!");
        			}
	     
	    }

}
