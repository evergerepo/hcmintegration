import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import oracle.ucm.idcws.client.UploadTool;
import oracle.ucm.idcws.client.UploadTool.UploadResults;
import oracle.ucm.idcws.client.model.response.CheckinResponse;

public class TestUploadTool {
	
	private static String url = "XXX";
	private static String username = "XXX";
	private static String password = "XXX";
	private static String primaryFile = "/Users/abhisheksingh/ddrive/everge_ws/details/adp12ADP_dem602012017_v2.txt";
	private static String silent = "true";
	private static String dDocAccount = "hcm/dataloader/import";
	private static String policy = "oracle/wss_username_token_over_ssl_client_policy";
	

	public static void main(String[] args) throws Exception {

		List<String> coreArgs = new ArrayList<String>();
		coreArgs.add("url=" + url);
		coreArgs.add("policy=" + policy);
		coreArgs.add("username=" + username);
		coreArgs.add("password=" + password);
		coreArgs.add("silent=" + silent);
		coreArgs.add("primaryFile=" + primaryFile);
		coreArgs.add("dDocAccount=" + dDocAccount);

		String[] uploadArgs = coreArgs.toArray(new String[0]);
		
		UploadTool uploadTool = new UploadTool();
		uploadTool.setup(uploadArgs);
		
		UploadResults uploadResults = uploadTool.run();
		
		Map<Integer, CheckinResponse> successfulCheckins = uploadResults.getSuccessfulCheckinsKeyedByTaskNum();
		for (Map.Entry<Integer, CheckinResponse> entry : successfulCheckins.entrySet())
		{
			CheckinResponse response = entry.getValue();
			System.out.println(response.getDDocName());
		}
		
	}

}
