package com.wen.extend.stock.impl;

import java.io.IOException;

import org.apache.http.HttpException;

import com.okcoin.rest.HttpUtilManager;
import com.okcoin.rest.StringUtil;
import com.okcoin.rest.stock.impl.StockRestApi;
import com.wen.extend.stock.IStockRestExtApi;

public class StockRestExtApi extends StockRestApi implements IStockRestExtApi {
	
	public StockRestExtApi(String url_prex) {
        super(url_prex);
    }

    /**
	 * K线数据URL
	 */
	private final String KLINE_URL = "/api/v1/kline.do?";

	@Override
	public String kline(String symbol, String type, Integer size, Long since) throws HttpException, IOException {
		HttpUtilManager httpUtil = HttpUtilManager.getInstance();
		StringBuilder param = new StringBuilder();
		if(StringUtil.isNotEmpty(symbol)) {
		    param.append("symbol=").append(symbol).append("&");
		} else {
		    param.append("symbol=btc_cny&");
		}
		param.append("type=").append(type).append("&");
		if (size != null && size != -1) {
		    param.append("size=").append(type).append("&");
		}
		if (since != null && since != -1) {
		    param.append("since=").append(since);
		}
		String result = httpUtil.requestHttpGet(url_prex, KLINE_URL, param.toString());
	    return result;
	}

}
