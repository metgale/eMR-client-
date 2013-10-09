package hr.foi.air.http;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * Http helper
 * 
 * @author FRM
 * 
 */
public class HttpHelper {

	/**
	 * Executes get request
	 * 
	 * @param url - request url
	 *           
	 * @return - returns String result from url
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public String get(String url) throws ClientProtocolException, IOException {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		HttpGet request = new HttpGet(url);
		ResponseHandler<String> handler = new BasicResponseHandler();
		String result = null;
		try {
			result = httpclient.execute(request, handler);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		httpclient.getConnectionManager().shutdown();
		return result;
	}

}
