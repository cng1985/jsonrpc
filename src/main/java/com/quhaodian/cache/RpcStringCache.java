/** 
 * CopyRright (c)1985-2012:宝亿电子 <br />                             
 * Project: jsonrpc<br />                                           
 * Module ID:    <br />   
 * Comments:            <br />                                  
 * JDK version used:<JDK1.6><br />                                 
 * Namespace:package org.json.rpc.cache;<br />                             
 * Author：陈联高 <br />                  
 * Create Date：  2012-11-26<br />   
 * Modified By：ada.young          <br />                                
 * Modified Date:2012-11-26      <br />                               
 * Why & What is modified <br />   
 * Version: 1.01         <br />       
 */


package com.quhaodian.cache;


public interface RpcStringCache {
	public String get(String key);
	public void put(String key,String value);
	public void clearall();
}
