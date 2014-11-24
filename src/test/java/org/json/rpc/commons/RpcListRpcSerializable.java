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

package org.json.rpc.commons;

import java.util.List;
/**
 * 服务器返回集合数据类
 * @author Administrator
 *
 * @param <T>
 */
public class RpcListRpcSerializable<T> extends RpcSerializable {

	private List<T> datas;

	public List<T> getDatas() {
		return datas;
	}

	public void setDatas(List<T> datas) {
		this.datas = datas;
	}
}
