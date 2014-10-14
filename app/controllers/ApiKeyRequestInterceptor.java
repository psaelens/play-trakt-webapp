package controllers;

import retrofit.RequestInterceptor;

public class ApiKeyRequestInterceptor implements RequestInterceptor {
	private final String apikey;
	
	
	public ApiKeyRequestInterceptor(String apikey) {
		super();
		this.apikey = apikey;
	}


	@Override
	public void intercept(RequestFacade request) {
		request.addPathParam("apikey", apikey);
	}
}
