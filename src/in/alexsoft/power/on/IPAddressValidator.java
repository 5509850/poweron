package in.alexsoft.power.on;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IPAddressValidator {
 
    private Pattern pattern;
    private Matcher matcher;
 
    private static final String IPADDRESS_PATTERN = 
		"^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
		"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
		"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
		"([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
 
    public IPAddressValidator(){
	  pattern = Pattern.compile(IPADDRESS_PATTERN);
    }
 
   /**
    * Validate ip address with regular expression
    * @param ip ip address for validation
    * @return true valid ip address, false invalid ip address
    */
    public boolean validate(final String ip){		  
	  matcher = pattern.matcher(ip);
	  return matcher.matches();	    	    
    }
    
    /*TESTING
     * public class IPAddressValidatorTest  {
	 
		private IPAddressValidator ipAddressValidator;
	 
		@BeforeClass
	        public void initData(){
			ipAddressValidator = new IPAddressValidator();
	        }
	 
		@DataProvider
		public Object[][] ValidIPAddressProvider() {
			return new Object[][]{
			   new Object[] {"1.1.1.1"},new Object[] {"255.255.255.255"},
	                   new Object[] {"192.168.1.1"},new Object[] {"10.10.1.1"},
	                   new Object[] {"132.254.111.10"},new Object[] {"26.10.2.10"},
			   new Object[] {"127.0.0.1"}
			};
		}
	 
		@DataProvider
		public Object[][] InvalidIPAddressProvider() {
			return new Object[][]{
			   new Object[] {"10.10.10"},new Object[] {"10.10"},
	                   new Object[] {"10"},new Object[] {"a.a.a.a"},
	                   new Object[] {"10.0.0.a"},new Object[] {"10.10.10.256"},
			   new Object[] {"222.222.2.999"},new Object[] {"999.10.10.20"},
	                   new Object[] {"2222.22.22.22"},new Object[] {"22.2222.22.2"},
	                   new Object[] {"10.10.10"},new Object[] {"10.10.10"},	
			};
		}
	 
		@Test(dataProvider = "ValidIPAddressProvider")
		public void ValidIPAddressTest(String ip) {
			   boolean valid = ipAddressValidator.validate(ip);
			   System.out.println("IPAddress is valid : " + ip + " , " + valid);
			   Assert.assertEquals(true, valid);
		}
	 
		@Test(dataProvider = "InvalidIPAddressProvider",
	                 dependsOnMethods="ValidIPAddressTest")
		public void InValidIPAddressTest(String ip) {
			   boolean valid = ipAddressValidator.validate(ip);
			   System.out.println("IPAddress is valid : " + ip + " , " + valid);
			   Assert.assertEquals(false, valid); 
		}	
	}
     * */
}