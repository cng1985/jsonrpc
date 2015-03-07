package org.json.rpc.servlet;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.rpc.annotation.RestFul;
import org.json.rpc.commons.AllowAllTypeChecker;
import org.json.rpc.server.JsonRpcExecutor;
import org.json.rpc.server.JsonRpcServletTransport;
import org.springframework.context.ApplicationContext;

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
			RestFul xx = o.getClass().getAnnotation(RestFul.class);
			String apiname = xx.value();
			executor.addHandler(apiname, o, xx.api());
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
