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

package org.json.rpc.client;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import org.apache.commons.codec.Base64Utils;
import org.json.rpc.client.JsonRpcClientTransport;

public class RequestTransport implements JsonRpcClientTransport {

	private URL url;
	private final Map<String, String> headers;

	public RequestTransport(URL url) {
		this.url = url;
		this.headers = new HashMap<String, String>();
		this.readTimeout = 9000;
		this.connectTimeout = 6000;
	}

	public RequestTransport(URL url, int connectTimeout, int readTimeout) {
		super();
		this.url = url;
		this.connectTimeout = connectTimeout;
		this.readTimeout = readTimeout;
		this.headers = new HashMap<String, String>();
	}

	private int connectTimeout;
	private int readTimeout;

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
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		if (headers != null) {
			for (Map.Entry<String, String> entry : headers.entrySet()) {
				connection.addRequestProperty(entry.getKey(), entry.getValue());
			}
		}

		connection.addRequestProperty("Accept-Encoding", "gzip");
		connection.setConnectTimeout(connectTimeout);
		connection.setRequestMethod("POST");
		connection.setReadTimeout(readTimeout);
		connection.setDoOutput(true);
		connection.connect();
		/**
		 * 向服务器写数据
		 */
		OutputStream out = null;
		try {
			out = connection.getOutputStream();
			OutputStreamWriter w = new OutputStreamWriter(out);
			w.write("body=" + data);
			w.close();
			int statusCode = connection.getResponseCode();
			if (statusCode != HttpURLConnection.HTTP_OK) {
				// throw new JsonRpcClientException(
				// "unexpected status code returned : " + statusCode);
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
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
		body = Base64Utils.decode(body);
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
