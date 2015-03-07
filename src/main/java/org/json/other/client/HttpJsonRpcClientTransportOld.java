package org.json.other.client;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import org.json.rpc.client.JsonRpcClientTransport;
import org.json.rpc.commons.JsonRpcClientException;

public class HttpJsonRpcClientTransportOld   implements JsonRpcClientTransport{
	   private URL url;
	    private final Map<String, String> headers;

	    public HttpJsonRpcClientTransportOld(URL url) {
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

	    private String post(URL url, Map<String, String> headers, String data)
	            throws IOException {

	        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

	        if (headers != null) {
	            for (Map.Entry<String, String> entry : headers.entrySet()) {
	                connection.addRequestProperty(entry.getKey(), entry.getValue());
	            }
	        }

	        connection.addRequestProperty("Accept-Encoding", "gzip");

	        connection.setRequestMethod("POST");
	        connection.setDoOutput(true);
	        connection.connect();

	        OutputStream out = null;

	        try {
	            out = connection.getOutputStream();

	            out.write(data.getBytes());
	            out.flush();
	            out.close();

	            int statusCode = connection.getResponseCode();
	            if (statusCode != HttpURLConnection.HTTP_OK) {
	                throw new JsonRpcClientException("unexpected status code returned : " + statusCode);
	            }
	        } finally {
	            if (out != null) {
	                out.close();
	            }
	        }

	        String responseEncoding = connection.getHeaderField("Content-Encoding");
	        responseEncoding = (responseEncoding == null ? "" : responseEncoding.trim());

	        ByteArrayOutputStream bos = new ByteArrayOutputStream();

	        InputStream in = connection.getInputStream();
	        try {
	            in = connection.getInputStream();
	            if ("gzip".equalsIgnoreCase(responseEncoding)) {
	                in = new GZIPInputStream(in);
	            }
	            in = new BufferedInputStream(in);
	            
	            byte[] buff = new byte[1024];
	            int n;
	            while ((n = in.read(buff)) > 0) {
	                bos.write(buff, 0, n);
	            }
	            bos.flush();
	            bos.close();
	        } finally {
	            if (in != null) {
	                in.close();
	            }
	        }

	        return bos.toString();
	    }
}
