package javax.microedition.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.loment.cashewnut.util.Helper;

//import com.loment.cashewnut.utils.Helpers;



class HTTPConnectionImpl implements HttpConnection {
	
	InputStream is;
	StatusLine sl;
	String method = GET;
	HttpClient client;// = new DefaultHttpClient();
	String url="";
	HttpRequest httprequest = new HttpGet();
	ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	HttpResponse response;
	
	HTTPConnectionImpl(String url){
		
		this.url=url;
		client = HttpUtils.getNewHttpClient();
		
//		    // Create a new HttpClient and Post Header
//		HttpClient httpclient = new DefaultHttpClient();
//	    HttpPost httppost = new HttpPost("http://www.yoursite.com/script.php");
//
//	    try {
//	        // Add your data
//	        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
//	        nameValuePairs.add(new BasicNameValuePair("id", "12345"));
//	        nameValuePairs.add(new BasicNameValuePair("stringdata", "AndDev is Cool!"));
//	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
//
//	        // Execute HTTP Post Request
//	        HttpResponse response = httpclient.execute(httppost);
//	        
//	    } catch (ClientProtocolException e) {
//	        // TODO Auto-generated catch block
//	    } catch (IOException e) {
//	        // TODO Auto-generated catch block
//	    }
//		// see http://androidsnippets.com/executing-a-http-post-request-with-httpclient
	}

	@Override
	public void close() throws IOException {
		// TODO Auto-generated method stub
	}

	@Override
	public int getLength() throws IOException {
		// TODO Auto-generated method stub
		int ret = -1;
		ret = (int) response.getEntity().getContentLength();
		if(ret == 0)
			return -1;
		
		return ret;
	}

	@Override
	public int getResponseCode() throws IOException{
		// TODO Auto-generated method stub
		int responseCode = 404;
		if(method.equals(GET)){
			if(response == null){
//				HttpGet request = new HttpGet();
	            try {
	            	((HttpGet)httprequest).setURI(new URI(url));
				} catch (URISyntaxException e) {
					// TODO Auto-generated catch block
					throw new IOException("invalid url:"+url);
				}
	            response = client.execute(((HttpGet)httprequest));
	            sl = response.getStatusLine();
			}

		}else if(method.equals(POST)){
			try {
				if(response == null){
					//send request to server
					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
					if(outputStream.size()>0){
						String postData = new String(outputStream.toByteArray());
						String[] postPairs = Helper.splitUsingStringDelim(postData, "&");
						for(int i = 0; i < postPairs.length; i++){
							String name = postPairs[i].substring(0, postPairs[i].indexOf('='));
							String value = postPairs[i].substring(postPairs[i].indexOf('=')+1);
							nameValuePairs.add(new BasicNameValuePair(name, value));
						}
						((HttpPost)httprequest).setEntity(new UrlEncodedFormEntity(nameValuePairs));;
					}
					response = client.execute((HttpPost)httprequest);
					sl = response.getStatusLine();
					responseCode = sl.getStatusCode();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				throw e;
			}
		}
			if(sl != null){
				return sl.getStatusCode();
			}
		
		return responseCode;
	}

	@Override
	public InputStream openInputStream() throws IOException {
		if(method.equals(GET)){
			if(response == null){
//				HttpGet request = new HttpGet();
	            try {
	            	((HttpGet)httprequest).setURI(new URI(url));
				} catch (URISyntaxException e) {
					// TODO Auto-generated catch block
					throw new IOException("invalid url:"+url);
				}
	            response = client.execute(((HttpGet)httprequest));
	            sl = response.getStatusLine();
			}

		}else if(method.equals(POST)){
			if(response == null){
				//send request to server
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
				if(outputStream.size()>0){
					String postData = new String(outputStream.toByteArray());
					String[] postPairs = Helper.splitUsingStringDelim(postData, "&");
					for(int i = 0; i < postPairs.length; i++){
						nameValuePairs.add(new BasicNameValuePair(postPairs[i].substring(0, postPairs[i].indexOf('=')-1),postPairs[i].substring(postPairs[i].indexOf('=')) ));
					}
					((HttpPost)httprequest).setEntity(new UrlEncodedFormEntity(nameValuePairs));;
				}
				response = client.execute((HttpPost)httprequest);
				sl = response.getStatusLine();
			}
		}
		
        is = (response.getEntity().getContent());
		return is;
	}

	@Override
	public OutputStream openOutputStream() {
		// TODO Auto-generated method stub
		
		return outputStream;
	}

	@Override
	public void setRequestMethod(String requestMethod) {
		// TODO Auto-generated method stub
		method = requestMethod;
		if(method == POST){
			httprequest = new HttpPost(url);
		}
	}

	@Override
	public void setRequestProperty(String name, String value) {
		// TODO Auto-generated method stub
		if(!name.equals("Content-Length"))
			httprequest.addHeader(name, value);
	}

	@Override
	public String getFile() {
		// TODO Auto-generated method stub
		Header hdr = response.getFirstHeader("uri");
		if(hdr != null){
			return hdr.getValue();
		}
		return null;
	}

	@Override
	public String getHeaderField(String string) {
		// TODO Auto-generated method stub
		Header hdr = response.getFirstHeader(string);
		if(hdr != null){
			return hdr.getValue();
		}
		return null;
	}

	@Override
	public String getRequestMethod() {
		// TODO Auto-generated method stub
		return method;
	}

}
