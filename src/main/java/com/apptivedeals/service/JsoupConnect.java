package com.apptivedeals.service;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service
public class JsoupConnect {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(JsoupConnect.class);
	
	private HashMap<String, Map<String, String>> cookiesByHost;
	
	private final static Map<String, String> DEFAULT_HEADERS = Collections
			.unmodifiableMap(new HashMap<String, String>() {
				private static final long serialVersionUID = -6196893314643020388L;
				{
					put("user-agent",
							"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36");
					put("accept-encoding", "gzip, deflate, br");
					put("accept",
							"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3");
					put("accept-language", "en-US,en;q=0.9,zh-TW;q=0.8,zh;q=0.7");
					
					put("pragma", "no-cache");
					put("cache-control", "no-cache");
					put("dnt", "1");
					put("upgrade-insecure-requests", "1");
					put("sec-fetch-site", "none");
					put("sec-fetch-mode", "navigate");
					
				}
			});
	
	@PostConstruct
	public void init() {
		cookiesByHost = new HashMap<String, Map<String, String>>();
	}
	
	public Response get(String url) throws IOException {
		Connection conn = Jsoup.connect(url).ignoreContentType(true).timeout(5000);
		for (String headerKey : DEFAULT_HEADERS.keySet()) {
			String headerValue = DEFAULT_HEADERS.get(headerKey);
			
			conn.header(headerKey, headerValue);
		}
		
		String host = getHost(url);
		Map<String, String> cookies = getCookiesByHost(host);
		if (cookies != null) {
			//conn.cookies(cookies);
		}
		Response response = null;
		int retryCount = 3;
		while (true) {
			try {
				response = conn.execute();
				saveCookiesByHost(host, response.cookies());
				break;
			} 
			catch (SocketTimeoutException ste) {
				LOGGER.error(ste.getMessage(), ste);
				retryCount--;
				if (retryCount > 0) {
					LOGGER.info("Retrying...");
				}
			}
		}
		
		return response;
	}
	
	private Map<String, String> getCookiesByHost(String host) {
		if (host == null || host.isEmpty()) {
			return null;
		}
		
		return cookiesByHost.get(host);
	}
	
	private void saveCookiesByHost(String host, Map<String, String> cookies) {
		if (host == null || host.isEmpty()) {
			return;
		}
		
		if (!cookiesByHost.containsKey(host)) {
			cookiesByHost.put(host, cookies);
		} else {
			for (String cookieName : cookies.keySet()) {
				cookiesByHost.get(host).put(cookieName, cookies.get(cookieName));
			}
		}
	}
	
	private String getHost(String url) {
		String host = null;
		try {
			URI uri = new URI(url);
			host = uri.getHost();
		} catch (URISyntaxException e) {
			LOGGER.error(e.getMessage(), e);
		}
		
		return host;
	}
}
