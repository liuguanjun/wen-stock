package com.okcoin.websocket.test;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.okcoin.websocket.WebSocketBase;
import com.okcoin.websocket.WebSocketService;
/**
 * 订阅信息处理类需要实现WebSocketService接口
 * @author okcoin
 *
 */
public class BuissnesWebSocketServiceImpl implements WebSocketService{
	private Logger log = Logger.getLogger(WebSocketBase.class);
	
	private long previousReceiveTime = 0;
	private long previousServerTime = 0;
	
	private int receivedMsgCount = 0;
	private int receivedSlowlyMsgCount = 0;
	private int receivedNotQuicklyMsgCount = 0;
	
	@Override
	public void onReceive(String msg){
	    // 过滤心跳
	    if (msg != null && msg.contains("pong")) {
	        //log.info("WebSocket Client received heart beat: " + msg);
	        return;
	    }
	    JSONArray jsonArray = JSON.parseArray(msg);
	    JSONObject jsonObject = (JSONObject) jsonArray.get(0);
	    String channel = jsonObject.getString("channel");
	    if (!channel.equals("ok_sub_spotcny_btc_ticker")) {
	        log.error("received illegal msg:" + msg);
	        return;
	    }
	    if (!jsonObject.containsKey("data")) {
	        log.warn("received ok_sub_spotcny_btc_ticker empty msg:" + msg);
	        return;
	    }
	    JSONObject tickerDataJsonObject = jsonObject.getJSONObject("data");
	    long currentServerTime = tickerDataJsonObject.getLongValue("timestamp");
	    if (previousReceiveTime > 0) {
	        receivedMsgCount++;
	        long intervalTime = System.currentTimeMillis() - previousReceiveTime;
	        long intervalServerTime = currentServerTime - previousServerTime;
	        if (intervalTime >= 1000) {
	            receivedSlowlyMsgCount++;
	            log.warn("received too slowly,  intervalTime:" + intervalTime + "ms,intervalServerTime:" + intervalServerTime + "ms,message: " + msg);
	        } else if (intervalTime >= 500) {
	            receivedNotQuicklyMsgCount++;
	            log.warn("received not quickly, intervalTime:" + intervalTime + "ms,intervalServerTime:" + intervalServerTime + "ms,message: " + msg);
	        } else {
	            log.info("received,             intervalTime:" + intervalTime + "ms,intervalServerTime:" + intervalServerTime + "ms,message: " + msg);
	        }
	        if (intervalServerTime > intervalTime) {
	            log.error("intervalServerTime > intervalTime :" + (intervalServerTime - intervalTime) + "ms");
	        }
	    }
	    
	    if (receivedMsgCount > 0 && receivedMsgCount % 100 == 0) {
	        log.info("received msg count:" + receivedMsgCount + 
	                ",receivedSlowlyMsgCount(>=1s):" + receivedSlowlyMsgCount + "," + (receivedSlowlyMsgCount * 1d / receivedMsgCount) * 100 + "%" + 
	                ",receivedNotQuicklyMsgCount(>=500ms):" + receivedNotQuicklyMsgCount + "," + (receivedNotQuicklyMsgCount * 1d / receivedMsgCount) * 100 + "%");
	    }
		
		previousReceiveTime = System.currentTimeMillis();
		previousServerTime = currentServerTime;
	
	}
}
