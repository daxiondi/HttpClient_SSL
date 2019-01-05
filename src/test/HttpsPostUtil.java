package test;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

public class HttpsPostUtil {
	
	public final static void main(String[] args) throws Exception {
		
    	String body = "";
    	
    	//采用绕过验证的方式处理https请求  
        SSLContext sslcontext = createIgnoreVerifySSL();  
        //设置协议http和https对应的处理socket链接工厂的对象  
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()  
            .register("http", PlainConnectionSocketFactory.INSTANCE)  
            .register("https", new SSLConnectionSocketFactory(sslcontext))  
            .build();  
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);  
        HttpClients.custom().setConnectionManager(connManager); 
    	
        //创建自定义的httpclient对象  
        CloseableHttpClient client = HttpClients.custom().setConnectionManager(connManager).build();  
        //CloseableHttpClient client = HttpClients.createDefault();
        
        try{
	        //创建post方式请求对象  
	        HttpPost httpPost = new HttpPost("https://api.douban.com/v2/book/1220562"); 
	        /*Map<String, String> map = new HashMap<String, String>();  
	      
	        map.put("city", "上海");  
	        map.put("charset", "utf-8");
	        
	      //装填参数  
	        List<NameValuePair> nvps = new ArrayList<NameValuePair>();  
	        if(map!=null){  
	            for (Entry<String, String> entry : map.entrySet()) {  
	                nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
	            }  
	        } 
	        System.out.println("请求参数："+nvps.toString());  
	        
	        //设置参数到请求对象中  
	        httpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));*/
	      
	        //指定报文头Content-type、User-Agent
	        httpPost.setHeader("Content-type", "application/x-www-form-urlencoded");  
	        
	        httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:6.0.2) Gecko/20100101 Firefox/6.0.2");
	     
	        
	        //执行请求操作，并拿到结果（同步阻塞）  
	        CloseableHttpResponse response = client.execute(httpPost);  
	        
	        //获取结果实体  
	        HttpEntity entity = response.getEntity(); 
	        if (entity != null) {  
	            //按指定编码转换结果实体为String类型  
	            body = EntityUtils.toString(entity, "UTF-8");  
	        }  
	        
	        EntityUtils.consume(entity);  
	        //释放链接  
	        response.close(); 
	        System.out.println("body:" + body);
        }finally{
        	client.close();
        }
	}
        
        
	/** 
     * 绕过验证 
     *   
     * @return 
     * @throws NoSuchAlgorithmException  
     * @throws KeyManagementException  
     */  
    public static SSLContext createIgnoreVerifySSL() throws NoSuchAlgorithmException, KeyManagementException {  
        SSLContext sc = SSLContext.getInstance("SSLv3");  
      
        // 实现一个X509TrustManager接口，用于绕过验证，不用修改里面的方法  
        X509TrustManager trustManager = new X509TrustManager() {  
            @Override  
            public void checkClientTrusted(  
                    java.security.cert.X509Certificate[] paramArrayOfX509Certificate,  
                    String paramString) throws CertificateException {  
            }  
      
            @Override  
            public void checkServerTrusted(  
                    java.security.cert.X509Certificate[] paramArrayOfX509Certificate,  
                    String paramString) throws CertificateException {  
            }  
      
            @Override  
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {  
                return null;  
            }  
        };  
      
        sc.init(null, new TrustManager[] { trustManager }, null);  
        return sc;  
    }
}
