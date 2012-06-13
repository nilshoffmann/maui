package de.unibielefeld.cebitec.lstutz.pca.data;

import java.io.InputStream;
import java.net.URL;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class ImportUtilities {
	
	public static InputStream get_url_input_stream(String file_url){
		
		HostnameVerifier hv = new HostnameVerifier(){
			public boolean verify(String urlHostname, SSLSession s) {
				System.out.println("WARNING: Hostname is not matched for cert.");
				return true;
				}			
		};
		HttpsURLConnection.setDefaultHostnameVerifier(hv);
		
	    TrustManager[] trustAllCerts = new TrustManager[]{
	        new X509TrustManager() {
	            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
	                return null;
	            }
	            public void checkClientTrusted(
	                java.security.cert.X509Certificate[] certs, String authType) {
	            }
	            public void checkServerTrusted(
	                java.security.cert.X509Certificate[] certs, String authType) {
	            }
	        }
	    };
	    
	    try {
	        SSLContext sc = SSLContext.getInstance("SSL");
	        sc.init(null, trustAllCerts, new java.security.SecureRandom());
	        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
	    } catch (Exception e) {
	    }
	    
		URL url;
		InputStream is = null;
		String d = null;
		try {
			url = new URL(file_url);
			d = url.toString();
			is = url.openStream();
		} catch (Exception e1) {
			e1.printStackTrace();
			JOptionPane.showMessageDialog(new JFrame(), d+"\n"+e1.getMessage());
		}
		if(is==null) System.exit(1);
		return is;
	}

}
