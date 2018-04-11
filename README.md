# jsonrpc
[![maven](https://img.shields.io/maven-central/v/com.quhaodian.jsonrpc/core.svg)](http://mvnrepository.com/artifact/com.quhaodian.jsonrpc/core)
[![QQ](https://img.shields.io/badge/chat-on%20QQ-ff69b4.svg?style=flat-square)](//shang.qq.com/wpa/qunwpa?idkey=d1a308945e4b2ff8aeb1711c2c7914342dae15e9ce7041e94756ab355430dc78)
[![Apache-2.0](https://img.shields.io/hexpm/l/plug.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)
[![使用IntelliJ IDEA开发维护](https://img.shields.io/badge/IntelliJ%20IDEA-提供支持-blue.svg)](https://www.jetbrains.com/idea/)
[![GitHub forks](https://img.shields.io/github/stars/cng1985/jsonrpc.svg?style=social&logo=github&label=Stars)](https://github.com/cng1985/jsonrpc)
======
##一个简单android访问api组件##
部分代码参考：https://github.com/RitwikSaikia/jsonrpc
这部分代码版权归RitwikSaikia所有。服务端缓存、客服端缓存、注解支持，spring支持等版权归ada.young所有。


##maven使用##


           <dependency>
               <groupId>com.quhaodian.jsonrpc</groupId>
               <artifactId>core</artifactId>
               <version>1.01</version>
           </dependency>

## 使用方法 ##
1. 定义接口

            @RestFul(api=Calculator.class,value="calculator")
            public interface Calculator {
                double add(double x, double y);
                double multiply(double x, double y);
            }
        


2. 实现接口

	    public class SimpleCalculatorImpl implements Calculator {
	    
            public double add(double x, double y) {
              return x + y;
            }
            
            public double multiply(double x, double y) {
              return x * y;
            }
	    
	    }

3. 通过与spring集成，配置servlet。
    
        <context-param>
            <param-name>contextConfigLocation</param-name>
            <param-value>classpath:context.xml</param-value>
        </context-param>
        
        <listener>
            <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
        </listener>
        
        <servlet>
            <servlet-name>dispatcher</servlet-name>
            <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
            <init-param>
                <param-name>contextConfigLocation</param-name>
                <param-value></param-value>
            </init-param>
            <load-on-startup>1</load-on-startup>
        </servlet> 
          
        <servlet>
            <servlet-name>rpc</servlet-name>
            <servlet-class>com.quhaodian.servlet.RpcServlet</servlet-class>
            <load-on-startup>2</load-on-startup>
        </servlet>
        
        <servlet-mapping>
             <servlet-name>rpc</servlet-name>
             <url-pattern>/rpc</url-pattern>
        </servlet-mapping>
    
4. 客服端访问

   
    	   
    	   String url="远程服务端servlet地址"
    
    	   Calculator  s = RestFulClient.getService(url, Calculator .class);
    
           double result = calc.add(1.2, 7.5);
           
           
	   


  
