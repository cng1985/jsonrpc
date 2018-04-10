jsonrpc
======
##一个简单android访问api组件##
部分代码参考：https://github.com/RitwikSaikia/jsonrpc
这部分代码版权归RitwikSaikia所有。服务端缓存、客服端缓存、注解支持，spring支持等版权归ada.young所有。


#maven使用#


        <dependency>
            <groupId>com.quhaodian</groupId>
            <artifactId>jsonrpc</artifactId>
            <version>1.06</version>
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

3. 通过与spring集成，编写servlet。
    
    	public class Rpc extends RpcServlet {
    
    	@Override
    	public ApplicationContext getApplicationContext() {
    		// TODO Auto-generated method stub
    		return ObjectFactory.get();
    	}
    
   	    }
    
4. 客服端访问

   
    	   
    	   url="远程服务端servlet地址"
    
    	   Calculator  s = RectClient.getService(url, Calculator .class);
    
           double result = calc.add(1.2, 7.5);
	   


  
