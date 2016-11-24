package com.zgmao.listener;

/**
 * 网络请求监听器
 * 
 * @author mzg
 *
 */
public interface RequestListener {
	public void onSuccess(String result);

	public void onError(Exception exception);
}
