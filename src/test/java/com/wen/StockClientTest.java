package com.wen;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.okcoin.rest.stock.IStockRestApi;
import com.okcoin.rest.stock.impl.StockRestApi;

/**
 * 现货 REST API 客户端请求
 * 
 * @author zhangchi
 *
 */
public class StockClientTest {
	
	private static final Log logger = LogFactory.getLog(StockClientTest.class.getName());

	public static void main(String[] args) throws Exception {
		
	    String api_key = "";  //OKCoin申请的apiKey
       	String secret_key = "";  //OKCoin 申请的secret_key
 	    String url_prex = "https://www.okcoin.cn";  //注意：请求URL 国际站https://www.okcoin.com ; 国内站https://www.okcoin.cn
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
		//IStockRestApi stockPost = new StockRestApi(url_prex, api_key, secret_key);
		long tryCount = 0;
		long successCount = 0;
		// 现货行情
		while (true) {
            try {
                long beginTime = System.currentTimeMillis();
                tryCount++;
                String tickerString = stockGet.ticker("btc_cny");
                long endTime = System.currentTimeMillis();
                logger.info(endTime - beginTime + "ms, " + tickerString);
                
                JSONObject jsonObject = JSON.parseObject(tickerString);
                Long date = jsonObject.getLong("date");
                if (date == null || date <= 0) {
                    logger.error("date error:" + date);
                    continue;
                }
                JSONObject tickerObject = jsonObject.getJSONObject("ticker");
                if (tickerObject == null) {
                    logger.error("ticker error:" + tickerObject);
                    continue;
                }
                Double buy = tickerObject.getDouble("buy");
                if (buy == null || buy <= 0) {
                    logger.error("ticker buy error:" + buy);
                    continue;
                }
                Double high = tickerObject.getDouble("high");
                if (high == null || high <= 0) {
                    logger.error("ticker high error:" + high);
                    continue;
                }
                Double last = tickerObject.getDouble("last");
                if (last == null || last <= 0) {
                    logger.error("ticker last error:" + last);
                    continue;
                }
                Double low = tickerObject.getDouble("low");
                if (low == null || low <= 0) {
                    logger.error("ticker low error:" + low);
                    continue;
                }
                Double sell = tickerObject.getDouble("sell");
                if (sell == null || sell <= 0) {
                    logger.error("ticker sell error:" + sell);
                    continue;
                }
                Double vol = tickerObject.getDouble("vol");
                if (vol == null || vol <= 0) {
                    logger.error("ticker vol error:" + vol);
                    continue;
                }
                
                successCount++;
            } catch (Exception e) {
                logger.error("rest ticker error.", e);
            }
            if (tryCount > 0 && tryCount % 100 == 0) {
                logger.info("try count:" + tryCount + 
                        ",success count:" + successCount + "," + (successCount * 1d / tryCount) * 100 + "%");
            }
		    Thread.sleep(200);
		}

//		// 现货市场深度
//		System.out.println(stockGet.depth("btc_cny"));
//
//		// 现货OKCoin历史交易信息
//		System.out.println(stockGet.trades("btc_cny", "20"));
//
//		// 现货用户信息
//		System.out.println(stockPost.userinfo());
//		
//		IStockRestExtApi stockExtGet = new StockRestExtApi(url_prex);
//		
//		long oneHourBefore = System.currentTimeMillis() - 1000 * 60 * 60;
//		
//		System.out.println(stockExtGet.kline("btc_cny", "15min", null, oneHourBefore));

	}
}
