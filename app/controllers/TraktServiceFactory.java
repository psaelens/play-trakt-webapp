package controllers;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import retrofit.RestAdapter;
import be.spitech.trakt.api.client.TraktService;

public class TraktServiceFactory {

	private final String endpoint;
	private final String apikey;

	public TraktServiceFactory(String endpoint, String apikey) {
		super();
		this.endpoint = endpoint;
		this.apikey = apikey;
	}

	public TraktService create() {
		RestAdapter restAdapter = new RestAdapter.Builder()
				.setRequestInterceptor(
						new ApiKeyRequestInterceptor(
								apikey))
				.setEndpoint(endpoint).build();

		return restAdapter.create(TraktService.class);
	}
	
	public static String hash(String s) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("SHA-1");
		byte[] key = s.getBytes();
        byte[] hashBytes = md.digest(key);
  
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < hashBytes.length; i++) {
          sb.append(Integer.toString((hashBytes[i] & 0xff) + 0x100, 16).substring(1));
        }
         
        return sb.toString();
	}

}
