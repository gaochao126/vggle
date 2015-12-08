package com.jiuyi.vggle.common.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

public class URLInvoke
{
    private final static Logger logger = Logger.getLogger(URLInvoke.class);

    public static String post(String url, String msg)
	{
		String content = null;
		BasicHttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 10000);
		HttpConnectionParams.setSoTimeout(httpParams, 15000);
		DefaultHttpClient httpClient = new DefaultHttpClient(httpParams);
		HttpPost postMethod = new HttpPost(url);
		try
		{
			// 从接过过来的代码转换为UTF-8的编码
            logger.info("URLInvoke.post#request message : " + msg);
			HttpEntity stringEntity = new StringEntity(msg, "text/plain", "UTF-8");
			postMethod.setEntity(stringEntity);
			HttpResponse response = httpClient.execute(postMethod);
			HttpEntity entity = response.getEntity();
			if (entity != null)
			{
				// 使用EntityUtils的toString方法，传递默认编码，在EntityUtils中的默认编码是ISO-8859-1
				content = EntityUtils.toString(entity, "UTF-8");
			}
		}
		catch (Exception e)
		{
            logger.error("URLInvoke.post#request error", e);
		}
		finally
		{
			postMethod.abort();
			httpClient.getConnectionManager().shutdown();
		}
        logger.info("URLInvoke.post#response message : " + content);
		return content;
	}
}