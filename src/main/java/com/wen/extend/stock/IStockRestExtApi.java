package com.wen.extend.stock;

import java.io.IOException;

import org.apache.http.HttpException;

public interface IStockRestExtApi {

    /**
     * 获取比特币或莱特币的K线数据
     * 
     * @param symbol btc_cny：比特币， ltc_cny：莱特币
     * @param type
     *            1min : 1分钟 
     *            3min : 3分钟 
     *            5min : 5分钟 
     *            15min : 15分钟 
     *            30min : 30分钟
     *            1day : 1日 
     *            3day : 3日 
     *            1week : 1周 
     *            1hour : 1小时 
     *            2hour : 2小时 
     *            4hour :4小时 
     *            6hour : 6小时 
     *            12hour : 12小时
     * 
     * @param size 指定获取数据的条数, 如果为空或者-1，返回所有
     * @param since 时间戳（eg：1417536000000）。 返回该时间戳以后的数据, 如果为空或者-1，返回所有
     * @return
     * @throws HttpException
     * @throws IOException
     */
    String kline(String symbol, String type, Integer size, Long since) throws HttpException, IOException;

}
