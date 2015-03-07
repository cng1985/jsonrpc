/** 
 * CopyRright (c)1985-2012:宝亿电子 <br />                             
 * Project: jsonrpc<br />                                           
 * Module ID:    <br />   
 * Comments:            <br />                                  
 * JDK version used:<JDK1.6><br />                                 
 * Namespace:package org.json.rpc.commons;<br />                             
 * Author：陈联高 <br />                  
 * Create Date：  2012-12-25<br />   
 * Modified By：ada.young          <br />                                
 * Modified Date:2012-12-25      <br />                               
 * Why & What is modified <br />   
 * Version: 1.01         <br />       
 */

package com.ada.commons;

import java.io.Serializable;
/**
 * 服务器返回类
 * @author Administrator
 *
 */
public class RpcSerializable implements Serializable {

	/**
	 * 服务器返回信息.可以用改标记服务器的运行情况,比如为0表示服务器处理成功,为-1表示处理失败
	 */
	private int code;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
}
