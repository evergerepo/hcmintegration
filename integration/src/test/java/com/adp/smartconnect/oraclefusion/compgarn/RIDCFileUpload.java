package com.adp.smartconnect.oraclefusion.compgarn;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import oracle.stellent.ridc.IdcClient;
import oracle.stellent.ridc.IdcClientManager;
import oracle.stellent.ridc.IdcContext;
import oracle.stellent.ridc.model.DataBinder;
import oracle.stellent.ridc.protocol.ServiceResponse;

public class RIDCFileUpload  {
	
	public static String FILE_NAME = "/mule/smartconnect/data/arff/compgarn/lien/adp1.06062017_v1.lien.grn";
	public static String URL = "https://ecbfdev4-test.fs.us8.oraclecloud.com/cs/idcplg";
	
	public static void fileUpload(String filaName, String url){
		
		IdcClient idcClient = null;
		IdcContext userContext = null;
		try{
			
			IdcClientManager m_clientManager = new IdcClientManager();
			
			
			//eVerge
			/* URL = "https://hcwh-test.fs.ap1.oraclecloud.com/cs/idcplg";
			 idcClient = m_clientManager.createClient(url);
			 userContext = new IdcContext("HCMUSER", "Cloud2World!"); 
			*/

			//Oracale Fusion
			URL = "https://ecbfdev4-test.fs.us8.oraclecloud.com/cs/idcplg";
			idcClient = m_clientManager.createClient(url);
			 userContext = new IdcContext("ADP_Connect", "ah2lB}A8wj05"); 
			 
			 SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
			 String formattedDate = sdf.format(Calendar.getInstance().getTime());
			 String contentId = "UCM" + formattedDate;	
			System.out.println("contentId==>:"+contentId);
			
			// create request
			DataBinder binder = idcClient.createBinder(); 
			binder.putLocal ("IdcService", "CHECKIN_UNIVERSAL");
			binder.putLocal ("dDocTitle", "Test File"); 
			binder.putLocal ("dDocName", contentId); 
			binder.putLocal ("dDocType", "ADACCT"); 
			binder.putLocal ("dSecurityGroup", "Public");
			
			
			// add a file
			binder.addFile ("primaryFile", new File (filaName));
			
			
			// checkin the file
			ServiceResponse response= idcClient.sendRequest (userContext, binder);
			
			
			String responseString = response.getResponseAsString ();
			System.out.println("File Upload RESPONSE:"+responseString);
			
			
		}catch(Exception e){
			System.out.println("File Upload Error:"+e.getLocalizedMessage());
			e.printStackTrace();
		}

	}
	
	
	
	public static void main(String[] args){
		
		String _fileName=FILE_NAME;
		String _url = URL;
		
		try{
			
		//	_url="https://hcwh-test.login.ap1.oraclecloud.com/oam/server/obrareq.cgi?encquery%3DFmEFj3nOx8A%2BLfqyJkUFLVCv6wivN%2FpRpNfCxJzWCIWgjjK8DOxKBb77DC9LXfrscEGOWl5kPjtuaxPvmHWEYSepMy%2FAEn5DIeHjKRYKoR0Pm0oeaGEy6KXsxjLNps6YkppF1BWa6zAgl95cI%2F7hKYtrzrJ%2FwLsVCeVvpSgjMx7f%2F6wl3lLoJIArQEE59fRYMRMoz5XC%2BskdGazG0RFjlLVddjA6eQXxkwm%2FjQ9D7vbnrwuGJHqAfGRMbiLNiG5at63aXrS%2F%2Ff1i4ojS8ooO1%2B3dBtms65UyrFeXOZH8FjYqU5D0IbQjpwlt66jvC%2FmTjy6KwnnMUkJeqr3ReanUCtIaf1QFoWh%2BgSeXq3WjDj2UQyY12bRDaMdMqBbDJ8IP%20agentid%3DOraFusionApp_11AG%20ver%3D1%20crmethod%3D2&ECID-Context=1.005Kkm8KPLL3z005RzH7id0001Ui000455%3BkXhgv0ZCLILI9V9O%5EMPGpKSQ_UOTdJPOoPRRiG";
			
			if(args.length>1){
				System.out.println("Setup Parameters:"+args.length);
				_fileName=args[0];
				_url=args[1];
			}
			
			fileUpload(_fileName, _url);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	

}
