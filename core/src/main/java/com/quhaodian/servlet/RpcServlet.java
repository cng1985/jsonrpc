package com.quhaodian.servlet;

import com.quhaodian.commons.AllowAllTypeChecker;
import com.quhaodian.jsonrpc.annotation.RestFul;
import com.quhaodian.server.JsonRpcExecutor;
import com.quhaodian.server.JsonRpcServletTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Set;

public  class RpcServlet extends HttpServlet {


    private Logger logger= LoggerFactory.getLogger(RpcServlet.class);

    private JsonRpcExecutor executor;

    public RpcServlet() {

    }


    private ApplicationContext context;

    @Override
    public void init() throws ServletException {
        super.init();
        logger.info("init context");
        context = WebApplicationContextUtils
                .getWebApplicationContext(getServletContext());
        executor = bind();
    }


    private JsonRpcExecutor bind() {
        JsonRpcExecutor executor = new JsonRpcExecutor(
                new AllowAllTypeChecker());

        Map<String, Object> ms = context.getBeansWithAnnotation(RestFul.class);
        Set<String> sets = ms.keySet();
        for (String key : sets) {
            Object o = ms.get(key);
            Class[] classs = o.getClass().getInterfaces();
            for (Class apiclass : classs) {
                Annotation xx = apiclass.getAnnotation(RestFul.class);
                if (xx instanceof RestFul) {
                    RestFul xxx = (RestFul) xx;
                    String apiname = xxx.value();
                    if (apiname == null || apiname.equals("")) {
                        apiname = apiclass.getSimpleName();
                        executor.addHandler(apiname, o, apiclass);
                    } else {
                        if (xxx.api() == null || xxx.api() == void.class) {
                            apiname = apiclass.getSimpleName();
                            executor.addHandler(apiname, o, apiclass);
                        } else {
                            executor.addHandler(apiname, o, xxx.api());
                        }
                    }
                }

            }
        }
        return executor;
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     * response)
     */
    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {
        executor.execute(new JsonRpcServletTransport(request, response));

    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     * response)
     */
    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {
        executor.execute(new JsonRpcServletTransport(request, response));
    }
}
