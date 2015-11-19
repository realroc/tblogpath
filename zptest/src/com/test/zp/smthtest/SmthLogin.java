package com.test.zp.smthtest;

import java.net.URI;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class SmthLogin {

	public static void main(String[] args) throws Exception {
		new SmthLogin().jsoup(new SmthLogin().wwwLogin());
	}
	
	
	public void jsoup(String html) throws Exception{
		Document doc = Jsoup.parse(html);
//		System.out.println(doc.select(".title_9").html());
//		System.out.println(doc.select(".title_9").text());
		Iterator<Element> it =doc.select(".title_9").select("a").iterator();
		while(it.hasNext()){
			Element e = it.next();

//			Pattern p = Pattern.compile("^\\d+$") ;
			Pattern p = Pattern.compile("^\\d+$") ;
			Matcher m = p.matcher(e.text());
			if(m.find()) continue ;
//			System.out.println(e + "------" + e.attr("href"));
			System.out.println(e.text());
		}
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
					RequestBuilder.get("http://m.newsmth.net")
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
	
	
	public String wwwLogin() throws Exception {

		BasicCookieStore cookieStore = new BasicCookieStore();
		CloseableHttpClient httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();

		try {

//			 HttpUriRequest login = RequestBuilder.post()
//			 .setUri(new URI("http://www.newsmth.net/bbslogin.php"))
//			 .addParameter("id", "realroc")
//			 .addParameter("passwd", "realroc")
//			 .addParameter("mode", "0")
//			 .addParameter("CookieDate", "0")
//			 .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:41.0Gecko/20100101 Firefox/41.0")
//			 .build();

			HttpUriRequest login = RequestBuilder.post()
					.setUri(new URI("http://www.newsmth.net/nForum/user/ajax_login.json"))
					.addParameter("id", "realroc")
					.addParameter("passwd", "realroc")
					.addParameter("mode", "0")
					.addParameter("CookieDate", "0")
					.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:41.0) Gecko/20100101 Firefox/41.0")
					.addHeader("Accept", "application/json, text/javascript, */*; q=0.01")
					.addHeader("X-Requested-With", "XMLHttpRequest")
					.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8").build();
			
			CloseableHttpResponse response2 = httpclient.execute(login);

			CloseableHttpResponse response3 = httpclient.execute(
					RequestBuilder.get("http://www.newsmth.net/nForum/board/Stock")
//					RequestBuilder.get("http://www.newsmth.net/nForum/board/Hubei")
					.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:41.0) Gecko/20100101 Firefox/41.0")
					.build());
			
			String result = new String(EntityUtils.toString(response3.getEntity()));
			try {
				System.out.println(EntityUtils.toString(response2.getEntity()));
//				System.out.println(new String(EntityUtils.toString(response2.getEntity()).getBytes("ISO-8859-1"), "GBK"));
				System.out.println(result);
			} finally {
				response2.close();
			}
			return result;
		} finally {
			httpclient.close();
		}
	}
}
