package com.zgmao.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;

/**
 * 网络请求工具
 * 
 * @author mzg
 *
 */
public class RequestUtil {
	/**
	 * 基本的post请求
	 * 
	 * @param url
	 *            url
	 * @param action
	 *            action
	 * @param param
	 *            参数
	 * @param listener
	 *            回调监听器
	 */
	public static String basePost(String url, String action,
			Map<String, String> param) {
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		HttpPost httpPost = new HttpPost(url + action);
		Lg.d("url:" + url);
		Lg.d("action:" + action);
		List<NameValuePair> pairs = new ArrayList<>();
		if (param != null) {
			Set<Entry<String, String>> entry = param.entrySet();
			Iterator<Entry<String, String>> iterator = entry.iterator();
			Lg.d("===========设置的请求参数开始===========");
			while (iterator.hasNext()) {
				Entry<String, String> item = iterator.next();
				Lg.d(item.getKey() + ":::" + item.getValue());
				pairs.add(
						new BasicNameValuePair(item.getKey(), item.getValue()));
			}
			Lg.d("===========设置的请求参数结束===========");
		}
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(pairs,
				Consts.UTF_8);
		httpPost.setEntity(entity);
		CloseableHttpResponse httpResponse = null;
		InputStream is = null;
		try {
			httpResponse = httpClient.execute(httpPost);

			HttpEntity httpEntity = httpResponse.getEntity();
			is = httpEntity.getContent();
			String content = StringUtils.streamToString(is);
			// LogUtils.d("content：" + content);
			return content;
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 关闭相关流
			if (httpResponse != null) {
				try {
					httpResponse.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return "";
	}

	/**
	 * 基本的get请求
	 * 
	 * @param url
	 *            url
	 * @param action
	 *            action
	 * @param param
	 *            参数
	 * @param listener
	 *            回调监听器
	 */
	public static String baseGet(String url, String action,
			Map<String, String> param) {
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		HttpGet httpGet = new HttpGet(url + action);
		Lg.d("url:" + url);
		Lg.d("action:" + action);
		List<NameValuePair> pairs = new ArrayList<>();
		if (param != null) {
			Set<Entry<String, String>> entry = param.entrySet();
			Iterator<Entry<String, String>> iterator = entry.iterator();
			Lg.d("===========设置的请求参数开始===========");
			while (iterator.hasNext()) {
				Entry<String, String> item = iterator.next();
				Lg.d(item.getKey() + ":::" + item.getValue());
				pairs.add(
						new BasicNameValuePair(item.getKey(), item.getValue()));
			}
			Lg.d("===========设置的请求参数结束===========");
		}
		// UrlEncodedFormEntity entity = new UrlEncodedFormEntity(pairs,
		// Consts.UTF_8);
		// httpGet.setEntity(entity);
		CloseableHttpResponse httpResponse = null;
		InputStream is = null;
		try {
			httpResponse = httpClient.execute(httpGet);

			HttpEntity httpEntity = httpResponse.getEntity();
			is = httpEntity.getContent();
			String content = StringUtils.streamToString(is);
			// LogUtils.d("content：" + content);
			return content;
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 关闭相关流
			if (httpResponse != null) {
				try {
					httpResponse.close();
				} catch (IOException e) {
				}
			}
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
				}
			}
		}
		return "";
	}

}
