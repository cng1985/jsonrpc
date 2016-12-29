/** 
 * CopyRright (c)1985-2012:宝亿电子 <br />                             
 * Project: jsonrpc<br />                                           
 * Module ID:    <br />   
 * Comments:            <br />                                  
 * JDK version used:<JDK1.6><br />                                 
 * Namespace:package org.json.rpc.cache;<br />                             
 * Author：陈联高 <br />                  
 * Create Date：  2012-9-9<br />   
 * Modified By：ada.young          <br />                                
 * Modified Date:2012-9-9      <br />                               
 * Why & What is modified <br />   
 * Version: 1.01         <br />       
 */


package com.quhaodian.cache;

import com.google.gson.JsonElement;

public interface RpcCache {

	public JsonElement get(String key);
	public void put(String key,JsonElement value);
	public void clearall();
}
