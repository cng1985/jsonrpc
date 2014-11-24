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

package org.json.other.server;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.Base64Utils;
import org.json.rpc.server.JsonRpcServerTransport;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class Base64JsonRpcServletTransport2 implements JsonRpcServerTransport {

	private static final int BUFF_LENGTH = 1024;

	private final HttpServletRequest req;
	private final HttpServletResponse resp;

	public Base64JsonRpcServletTransport2(HttpServletRequest req,
			HttpServletResponse resp) {
		this.req = req;
		this.resp = resp;
	}

	public String readRequest() throws Exception {
			String body = req.getParameter("body");
			body = Base64Utils.decode(body);
			return body;
	}

	private static final int bufferSize = 0x20000; // ~130K.

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

	public void writeResponse(String responseData) throws Exception {
		// resp.setCharacterEncoding("UTF-8");
		resp.setContentType("text/html; charset=utf-8");      
		String result = Base64Utils.encode(responseData);
		PrintWriter out = null;
		try {
			out = resp.getWriter();
			out.print(result);
			out.flush();
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}
}
