package com.openyelp.servlet;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;

import com.openyelp.annotation.RestFul;
import com.openyelp.commons.AllowAllTypeChecker;
import com.openyelp.server.JsonRpcExecutor;
import com.openyelp.server.JsonRpcServletTransport;

public abstract class RpcServlet extends HttpServlet {

	private final JsonRpcExecutor executor;

	public RpcServlet() {
		executor = bind();
	}

	public abstract ApplicationContext getApplicationContext();

	private JsonRpcExecutor bind() {
		JsonRpcExecutor executor = new JsonRpcExecutor(
				new AllowAllTypeChecker());
		ApplicationContext app = getApplicationContext();

		Map<String, Object> ms = app.getBeansWithAnnotation(RestFul.class);
		Set<String> sets = ms.keySet();
		for (String key : sets) {
			Object o = ms.get(key);
			Class[] classs = o.getClass().getInterfaces();
			for (Class class1 : classs) {
				Annotation xx =	class1.getAnnotation(RestFul.class);
				if(xx instanceof RestFul){
					RestFul xxx = (RestFul) xx;
					String apiname = xxx.value();
					executor.addHandler(apiname, o, xxx.api());
				}
		
			}
		}
		return executor;
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		executor.execute(new JsonRpcServletTransport(request, response));

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		executor.execute(new JsonRpcServletTransport(request, response));
	}
}
