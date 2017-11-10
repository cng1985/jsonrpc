package com.quhaodian.other.server;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JsonRpcServletTransportOld {

    private static final int BUFF_LENGTH = 1024;

    private final HttpServletRequest req;
    private final HttpServletResponse resp;


    public JsonRpcServletTransportOld(HttpServletRequest req, HttpServletResponse resp) {
        this.req = req;
        this.resp = resp;
    }

    public String readRequest() throws Exception {
        InputStream in = null;
        try {
            in = req.getInputStream();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            byte[] buff = new byte[BUFF_LENGTH];
            int n;
            while ((n = in.read(buff)) > 0) {
                bos.write(buff, 0, n);
            }

            return bos.toString();
        } finally {
            if (in != null) {
                in.close();
            }
        }
    }

    public void writeResponse(String responseData) throws Exception {
        byte[] data = responseData.getBytes(resp.getCharacterEncoding());
        resp.addHeader("Content-Type", "application/json");
        resp.setHeader("Content-Length", Integer.toString(data.length));

        PrintWriter out = null;
        try {
            out = resp.getWriter();
            out.write(responseData);
            out.flush();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }
}
