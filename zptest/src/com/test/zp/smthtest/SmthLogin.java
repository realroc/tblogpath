package com.test.zp.smthtest;

import java.net.URI;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class SmthLogin {

	public static void main(String[] args) throws Exception {
		new SmthLogin().jsoup();
	}
	
	
	public void jsoup() throws Exception{
		Document doc = Jsoup.parse(mLogin());
		System.out.println(doc.select(".title_11").text());
		
	}

	public String mLogin() throws Exception {

		BasicCookieStore cookieStore = new BasicCookieStore();
		CloseableHttpClient httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();

		try {

			HttpUriRequest login = RequestBuilder.post()
					.setUri(new URI("http://m.newsmth.net/user/login"))
					.addParameter("id", "realroc")
					.addParameter("passwd", "realroc")
					.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:41.0) Gecko/20100101 Firefox/41.0")
					.build();
			CloseableHttpResponse response2 = httpclient.execute(login);

			CloseableHttpResponse response3 = httpclient.execute(
					RequestBuilder.get("http://www.newsmth.net/nForum/board/Stock")
					.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:41.0) Gecko/20100101 Firefox/41.0")
					.build());
			
			String result = new String(EntityUtils.toString(response3.getEntity()));
			try {
				
				System.out.println(new String(EntityUtils.toString(response2.getEntity())));
				System.out.println(result);
			} finally {
				response2.close();
			}
			return result;
		} finally {
			httpclient.close();
		}
	}
	
	
	public void wwwLogin() throws Exception {

		BasicCookieStore cookieStore = new BasicCookieStore();
		CloseableHttpClient httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();

		try {

			 HttpUriRequest login = RequestBuilder.post()
			 .setUri(new URI("http://www.newsmth.net/bbslogin.php"))
			 .addParameter("id", "realroc")
			 .addParameter("passwd", "realroc")
			 .addParameter("mode", "0")
			 .addParameter("CookieDate", "0")
			 .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:41.0Gecko/20100101 Firefox/41.0")
			 .build();

//			HttpUriRequest login = RequestBuilder.post()
//					.setUri(new URI("http://www.newsmth.net/nForum/user/ajax_login.json"))
//					.addParameter("id", "realroc")
//					.addParameter("passwd", "realroc")
//					.addParameter("mode", "0")
//					.addParameter("CookieDate", "0")
//					.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:41.0) Gecko/20100101 Firefox/41.0")
//					.addHeader("Accept", "application/json, text/javascript, */*; q=0.01")
//					.addHeader("X-Requested-With", "XMLHttpRequest")
//					.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8").build();
			
			CloseableHttpResponse response2 = httpclient.execute(login);

			CloseableHttpResponse response3 = httpclient.execute(
					RequestBuilder.get("http://www.newsmth.net/nForum/board/Stock")
					.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:41.0) Gecko/20100101 Firefox/41.0")
					.build());

			try {
				System.out.println(EntityUtils.toString(response2.getEntity()));
//				System.out.println(new String(EntityUtils.toString(response2.getEntity()).getBytes("ISO-8859-1"), "GBK"));
				System.out.println(EntityUtils.toString(response3.getEntity()));
			} finally {
				response2.close();
			}
		} finally {
			httpclient.close();
		}
	}
}
