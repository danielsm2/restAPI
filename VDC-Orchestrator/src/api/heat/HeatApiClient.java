package api.heat;

import java.net.HttpURLConnection;
import java.net.URL;

public class HeatApiClient {

	public void deployVDC(String heatURL){
		HttpURLConnection http = null;
		
		try{
			URL url = new URL(heatURL + " ");
			http = (HttpURLConnection) url.openConnection();
			
		} catch(Exception e){
			e.printStackTrace();
		}
		
	}
}
