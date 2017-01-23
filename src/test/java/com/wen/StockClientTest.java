package com.wen;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpException;

import com.okcoin.rest.stock.IStockRestApi;
import com.okcoin.rest.stock.impl.StockRestApi;
import com.wen.extend.stock.IStockRestExtApi;
import com.wen.extend.stock.impl.StockRestExtApi;

/**
 * 现货 REST API 客户端请求
 * 
 * @author zhangchi
 *
 */
public class StockClientTest {
	
	private static final Log logger = LogFactory.getLog(StockClientTest.class.getName());

	public static void main(String[] args) throws HttpException, IOException {
		
		String api_key = ""; // OKCoin申请的apiKey
		String secret_key = ""; // OKCoin
																// 申请的secret_key
		String url_prex = ""; // 注意：请求URL
													// 国际站https://www.okcoin.com
													// ;
													// 国内站https://www.okcoin.cn

		/**
		 * get请求无需发送身份认证,通常用于获取行情，市场深度等公共信息
		 * 
		 */
		IStockRestApi stockGet = new StockRestApi(url_prex);

		/**
		 * post请求需发送身份认证，获取用户个人相关信息时，需要指定api_key,与secret_key并与参数进行签名，
		 * 此处对构造方法传入api_key与secret_key,在请求用户相关方法时则无需再传入，
		 * 发送post请求之前，程序会做自动加密，生成签名。
		 * 
		 */
		IStockRestApi stockPost = new StockRestApi(url_prex, api_key, secret_key);

		// 现货行情
		System.out.println(stockGet.ticker("btc_cny"));

		// 现货市场深度
		System.out.println(stockGet.depth("btc_cny"));

		// 现货OKCoin历史交易信息
		System.out.println(stockGet.trades("btc_cny", "20"));

		// 现货用户信息
		System.out.println(stockPost.userinfo());
		
		IStockRestExtApi stockExtGet = new StockRestExtApi(url_prex);
		
		long oneHourBefore = System.currentTimeMillis() - 1000 * 60 * 60;
		
		System.out.println(stockExtGet.kline("btc_cny", "15min", null, oneHourBefore));

	}
}
