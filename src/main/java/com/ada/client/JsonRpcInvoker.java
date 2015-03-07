/*
 * Copyright (C) 2011 ritwik.net
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ada.client;

import java.io.StringReader;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Random;
import java.util.UUID;

import com.ada.cache.RpcStringCache;
import com.ada.commons.GsonTypeChecker;
import com.ada.commons.TypeChecker;
import com.ada.commons.Utils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public final class JsonRpcInvoker {

	private final Random rand = new Random();

	private final TypeChecker typeChecker;

	public JsonRpcInvoker() {
		this(new GsonTypeChecker());
	}

	public JsonRpcInvoker(TypeChecker typeChecker) {
		this.typeChecker = typeChecker;
	}

	/**
	 * 
	 * @param typeChecker
	 *            类型检查
	 * @param maxWorkTime
	 *            最大超时时间
	 */
	public JsonRpcInvoker(TypeChecker typeChecker, int maxWorkTime) {
		super();
		this.typeChecker = typeChecker;
		this.maxWorkTime = maxWorkTime;
	}

	@SuppressWarnings("unchecked")
	public <T> T get(final JsonRpcClientTransport transport,
			final String handle, final Class<T>... classes) {
		for (Class<T> clazz : classes) {
			typeChecker.isValidInterface(clazz);
		}
		return (T) Proxy.newProxyInstance(
				JsonRpcInvoker.class.getClassLoader(), classes,
				new InvocationHandler() {

					public Object invoke(final Object proxy,
							final Method method, final Object[] args)
							throws Throwable {
						return JsonRpcInvoker.this.invoke(handle, transport,
								method, args);
					}
				});
	}

	public int maxWorkTime = 3;

	private Object invoke(String handleName, JsonRpcClientTransport transport,
			Method method, Object[] args) throws Throwable {

		String key = UUID.randomUUID().toString().toLowerCase();
		int time = 0;
		Object object = work(handleName, transport, method, args, key);
		if (object == null) {
			while (time < maxWorkTime) {
				object = work(handleName, transport, method, args, key);
				System.out.println("重新连接服务器" + time);
				if (object != null) {
					break;
				}
				time++;
			}
		}
		return object;
	}

	public String getKey(Object[] args) {
		StringBuffer buffer = new StringBuffer();
		if (args != null) {
			for (int i = 0; i < args.length; i++) {
				buffer.append(args[i].toString() + "/");
			}
		}
		return buffer.toString();

	}

	private RpcStringCache cache;
	private RpcStringCache diskCache;

	public RpcStringCache getDiskCache() {
		return diskCache;
	}

	public void setDiskCache(RpcStringCache diskCache) {
		this.diskCache = diskCache;
	}

	public JsonRpcInvoker(TypeChecker typeChecker, int maxWorkTime,
			RpcStringCache cache) {
		super();
		this.typeChecker = typeChecker;
		this.maxWorkTime = maxWorkTime;
		this.cache = cache;
	}

	public RpcStringCache getCache() {
		return cache;
	}

	public void setCache(RpcStringCache cache) {
		this.cache = cache;
	}

	private Object work(String handleName, JsonRpcClientTransport transport,
			Method method, Object[] args, String key) {
		Gson gson = new Gson();
		String cachekey = handleName + method.getName() + getKey(args);
		String keyy = Utils.getMD5Str(cachekey);
		String responseData = null;
		if (cache != null) {
			responseData = cache.get(keyy);
			if (responseData == null) {
				responseData = c(handleName, transport, method, args, key, gson);
				cache.put(keyy, responseData);
			}
		} else {
			responseData = c(handleName, transport, method, args, key, gson);
		}
		if (responseData == null) {
			if(diskCache!=null){
				responseData=diskCache.get(keyy);
			}
		}
		if (responseData == null) {
			return null;
		}
		
		JsonParser parser = new JsonParser();
		JsonObject resp = (JsonObject) parser.parse(new StringReader(
				responseData));
		// int sid = resp.get("id").getAsInt();
		// if (id == sid) {
		// } else {
		// return null;
		// }
		JsonElement result = resp.get("result");
		JsonElement error = resp.get("error");
		/**
		 * 出错以后返回null
		 */
		if (error != null && !error.isJsonNull()) {
			if (error.isJsonPrimitive()) {
				System.out.println("<<>>>>>" + error.getAsString());
			} else if (error.isJsonObject()) {
				JsonObject o = error.getAsJsonObject();
				Integer code = (o.has("code") ? o.get("code").getAsInt() : null);
				String message = (o.has("message") ? o.get("message")
						.getAsString() : null);
				String data = (o.has("data") ? (o.get("data") instanceof JsonObject ? o
						.get("data").toString() : o.get("data").getAsString())
						: null);
				System.out.println(message + "<<>>>>>" + data);
			} else {
				System.out.println("<<>>>>>" + error.toString());
			}
			return null;
		}
		if(diskCache!=null){
			System.out.println("缓存数据");
			diskCache.put(keyy,responseData);
		}
		if (method.getReturnType() == void.class) {
			return null;
		}
		return gson.fromJson(result.toString(), method.getReturnType());
	}

	private String c(String handleName, JsonRpcClientTransport transport,
			Method method, Object[] args, String key, Gson gson) {
		String responseData = null;
		int id = rand.nextInt(Integer.MAX_VALUE);
		String methodName = handleName + "." + method.getName();
		JsonObject req = new JsonObject();
		req.addProperty("id", id);
		req.addProperty("key", key);
		req.addProperty("method", methodName);
		JsonArray params = new JsonArray();
		if (args != null) {
			for (Object o : args) {
				params.add(gson.toJsonTree(o));
			}
		}
		req.add("params", params);
		String requestData = req.toString();
		try {
			responseData = transport.call(requestData);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return responseData;
	}
}
