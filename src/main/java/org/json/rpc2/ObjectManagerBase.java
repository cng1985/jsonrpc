/** 
 * CopyRright (c)1985-2012:宝亿电子 <br />                             
 * Project: jsonrpc<br />                                           
 * Module ID:    <br />   
 * Comments:            <br />                                  
 * JDK version used:<JDK1.6><br />                                 
 * Namespace:package org.json.rpc2;<br />                             
 * Author：陈联高 <br />                  
 * Create Date：  2012-10-12<br />   
 * Modified By：ada.young          <br />                                
 * Modified Date:2012-10-12      <br />                               
 * Why & What is modified <br />   
 * Version: 1.01         <br />       
 */

package org.json.rpc2;

import java.util.HashMap;
import java.util.Map;

import org.json.rpc.commons.JsonRpcException;
import org.json.rpc.commons.TypeChecker;
import org.json.rpc.server.HandleEntry;

public class ObjectManagerBase implements ObjectManager {
	
	private TypeChecker typeChecker;
	public ObjectManagerBase(TypeChecker typeChecker) {
		this.typeChecker = typeChecker;
		this.handlers = new HashMap<String, HandleEntry<?>>();
		//addHandler("system", this, RpcIntroSpection.class);
	}
    private final Map<String, HandleEntry<?>> handlers;
    private volatile boolean locked;
    public <T> void addHandler(String name, T handler, Class<T>... classes) {
        if (locked) {
            throw new JsonRpcException("executor has been locked, can't add more handlers");
        }

        synchronized (handlers) {
            HandleEntry<T> handleEntry = new HandleEntry<T>(typeChecker, handler, classes);
            if (this.handlers.containsKey(name)) {
                throw new IllegalArgumentException("handler already exists");
            }
			this.handlers.put(name, handleEntry);
        }
    }
	@Override
	public HandleEntry<?> get(String beanname) {
		// TODO Auto-generated method stub
		return handlers.get(beanname);
	}
	private HashMap<String, Object> datas = new HashMap<String, Object>();
}
