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

import org.json.rpc.server.HandleEntry;

public interface ObjectManager {

	public HandleEntry<?> get(String beanname);

	public <T> void addHandler(String name, T handler, Class<T>... classes);
}
