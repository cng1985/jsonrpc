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

package com.openyelp.commons;

/**
 * 服务器返回集合数据类
 * @author Administrator
 *
 * @param <T>
 */
public abstract class RpcPageSerializable<T> extends RpcListSerializable<T> {


	
	
	private int pageNo;
	private int pageSize;
	private int pageTotal;
	private int total;
	public int getPageNo() {
		return pageNo;
	}
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getPageTotal() {
		return pageTotal;
	}
	public void setPageTotal(int pageTotal) {
		this.pageTotal = pageTotal;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	
}
