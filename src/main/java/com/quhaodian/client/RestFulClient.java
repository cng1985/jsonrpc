package com.quhaodian.client;

import java.net.URL;

import com.quhaodian.annotation.RestFul;

public class RestFulClient {

	public static <T> T getService(String url ,Class<T> classc) {
		T result = null;

		HttpJsonRpcClientTransport transport;
		try {
			RestFul ful = classc.getAnnotation(RestFul.class);
			
			transport = new HttpJsonRpcClientTransport(new URL(url));
			JsonRpcInvoker invoker = new JsonRpcInvoker();
			String apiname = ful.value();
            if(apiname==null||apiname.equals("")){
				apiname=classc.getSimpleName();
    			result = (T) invoker.get(transport, apiname,classc);
            }else{
            	if (ful.api() == null || ful.api() == void.class) {
					apiname = classc.getSimpleName();
	    			result = (T) invoker.get(transport, apiname,classc);
				} else {
	    			result = (T) invoker.get(transport, ful.value(),ful.api());
				}

            }
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;

	} 
	@SuppressWarnings("unchecked")
	public static <T> T getService(String url ,Class<T> classc,JsonRpcInvoker invoker,JsonRpcClientTransport jsonRpcClientTransport ) {
		T result = null;

		try {
			RestFul ful = classc.getAnnotation(RestFul.class);
			
			String apiname = ful.value();
            if(apiname==null){
				apiname=classc.getSimpleName();
    			result = (T) invoker.get(jsonRpcClientTransport, apiname,classc);
            }else{
    			result = (T) invoker.get(jsonRpcClientTransport, ful.value(),ful.api());

            }
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;

	} 
	
	public static <T> T getService(String url ,Class<T> classc,JsonRpcInvoker invoker ) {
		T result = null;

		HttpJsonRpcClientTransport transport;
		try {
			RestFul ful = classc.getAnnotation(RestFul.class);
			
			transport = new HttpJsonRpcClientTransport(new URL(url));
			String apiname = ful.value();
            if(apiname==null){
				apiname=classc.getSimpleName();
    			result = (T) invoker.get(transport, apiname,classc);
            }else{
    			result = (T) invoker.get(transport, ful.value(),ful.api());

            }
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;

	} 
}
