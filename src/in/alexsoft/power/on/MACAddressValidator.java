package in.alexsoft.power.on;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MACAddressValidator {
	 
		private Pattern pattern;
	    private Matcher matcher;
	        
	        private static final String MACADDRESS_PATTERN = 
	        		"^([0-9A-F]{2}[:-]){5}([0-9A-F]{2})$";
	     
	        public MACAddressValidator(){
	    	  pattern = Pattern.compile(MACADDRESS_PATTERN);
	        }	     
	       
	        public boolean validate(final String mac){		  
	    	  matcher = pattern.matcher(mac);
	    	  return matcher.matches();	    	    
	        }	        
	        
	    }