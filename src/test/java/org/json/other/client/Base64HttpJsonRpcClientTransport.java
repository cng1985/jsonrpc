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

package org.json.other.client;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import org.apache.commons.codec.Base64Utils;
import org.json.rpc.client.JsonRpcClientTransport;
import org.json.rpc.commons.JsonRpcClientException;

public class Base64HttpJsonRpcClientTransport implements JsonRpcClientTransport {

	private URL url;
	private final Map<String, String> headers;

	public Base64HttpJsonRpcClientTransport(URL url) {
		this.url = url;
		this.headers = new HashMap<String, String>();
	}

	public final void setHeader(String key, String value) {
		this.headers.put(key, value);
	}

	public final String call(String requestData) throws Exception {
		String responseData = post(url, headers, requestData);
		return responseData;
	}
	private static final int BUFF_LENGTH = 1024;

	private String post(URL url, Map<String, String> headers, String data)
			throws IOException {
		System.out.println("客服端返回数据解码前" + data);
		String tdata = Base64Utils.encode(data);
		System.out.println("客服端返回数据解码后" + tdata);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		if (headers != null) {
			for (Map.Entry<String, String> entry : headers.entrySet()) {
				connection.addRequestProperty(entry.getKey(), entry.getValue());
			}
		}
		connection.addRequestProperty("Accept-Encoding", "gzip");
		connection.setConnectTimeout(6000);
		connection.setRequestMethod("POST");
		connection.setReadTimeout(9000);
		connection.setDoOutput(true);
		connection.connect();
		/**
		 * 向服务器写数据
		 */
		OutputStream out = null;
		try {
			out = connection.getOutputStream();
			out.write(tdata.getBytes());
			out.flush();
			out.close();
			int statusCode = connection.getResponseCode();
			if (statusCode != HttpURLConnection.HTTP_OK) {
				// throw new JsonRpcClientException(
				// "unexpected status code returned : " + statusCode);
				return null;
			}
		} catch (Exception e) {
			return null;
		} finally {
			if (out != null) {
				out.close();
			}
		}

		String responseEncoding = connection.getHeaderField("Content-Encoding");
		responseEncoding = (responseEncoding == null ? "" : responseEncoding
				.trim());

		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		InputStream in = connection.getInputStream();
		try {
			in = connection.getInputStream();
			if ("gzip".equalsIgnoreCase(responseEncoding)) {
				in = new GZIPInputStream(in);
			}
			byte[] buff = new byte[BUFF_LENGTH];
			int n;
			while ((n = in.read(buff)) > 0) {
				bos.write(buff, 0, n);
			}
		} finally {
			if (in != null) {
				in.close();
			}
		}
		String body = bos.toString();
		System.out.println("服务器返回数据解码前" + body);
		body = Base64Utils.decode(body);
		System.out.println("服务器返回数据解码后" + body);
		return body;
	}

	public static ByteBuffer readToByteBuffer(InputStream inStream)
			throws IOException {
		byte[] buffer = new byte[bufferSize];
		ByteArrayOutputStream outStream = new ByteArrayOutputStream(bufferSize);
		int read;
		while (true) {
			read = inStream.read(buffer);
			if (read == -1)
				break;
			outStream.write(buffer, 0, read);
		}
		ByteBuffer byteData = ByteBuffer.wrap(outStream.toByteArray());
		return byteData;
	}

	private static final int bufferSize = 0x20000; // ~130K.

}
