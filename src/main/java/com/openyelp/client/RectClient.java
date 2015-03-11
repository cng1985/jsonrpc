package com.openyelp.client;

import java.net.URL;

import com.openyelp.annotation.RestFul;

public class RectClient {

	public static <T> T getService(String url ,Class<T> classc) {
		T result = null;

		HttpJsonRpcClientTransport transport;
		try {
			RestFul ful = classc.getAnnotation(RestFul.class);
			transport = new HttpJsonRpcClientTransport(new URL(url));
			JsonRpcInvoker invoker = new JsonRpcInvoker();
			result = (T) invoker.get(transport, ful.value(),ful.api());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;

	} 
}
