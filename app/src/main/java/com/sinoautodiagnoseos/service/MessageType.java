package com.sinoautodiagnoseos.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.LongSerializationPolicy;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 推送数据拆装箱
 * 
 * @author xiams
 */
public class MessageType {
	private static Gson gson = new Gson();

	private Map<String, Object> resultMap = null; // 装箱后的结果数据
	private Map<String, Object> apsMap = null; // 推送后的效果配置 
	private List<Object> messageArr = null; // 消息数据
	private Map<String, Object> message = null; // 推送的消息内容
	private Map<String, Object> Extension = null; // 扩展数据对象
	private Map<String, Object> data = null; // 扩展数据
	private Map<String, Object> iosAlert = null; //ios 专用的提示 
	private Map<String, Object> pushModelMessageMap = null;

	public Map<String, Object> getPushModelMessageMap() {
		return pushModelMessageMap;
	}

	public void setPushModelMessageMap(Map<String, Object> pushModelMessageMap) {
		this.pushModelMessageMap = pushModelMessageMap;
	}

	public Map<String, Object> getIosAlert() {
		return iosAlert;
	}

	public void setIosAlert(Map<String, Object> iosAlert) {
		this.iosAlert = iosAlert;
	}

	public Map<String, Object> getResultMap() {
		return resultMap;
	}

	public void setResultMap(Map<String, Object> resultMap) {
		this.resultMap = resultMap;
	}

	public Map<String, Object> getApsMap() {
		return apsMap;
	}

	public void setApsMap(Map<String, Object> apsMap) {
		this.apsMap = apsMap;
	}

	public List<Object> getMessageArr() {
		return messageArr;
	}

	public void setMessageArr(List<Object> messageArr) {
		this.messageArr = messageArr;
	}

	public Map<String, Object> getMessage() {
		return message;
	}

	public void setMessage(Map<String, Object> message) {
		this.message = message;
	}

	public Map<String, Object> getExtension() {
		return Extension;
	}

	public void setExtension(Map<String, Object> extension) {
		Extension = extension;
	}

	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}
	
	/**
	 * resultMap拆箱
	 * 
	 * @param jsonStr
	 */
	public MessageType(String jsonStr) throws JSONException {
		JSONObject jsonResultMap = null;
		try {
			jsonResultMap = new JSONObject(jsonStr);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		resultMap = mapStrToMap(jsonStr);
		if (resultMap.containsKey("Messages")) {
			JSONArray jsonMessages = null;
			try {
				jsonMessages = jsonResultMap.getJSONArray("Messages");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			try {
				messageArr = toList(jsonMessages);
			} catch (JSONException e) {
				e.printStackTrace();
			}

			if (messageArr.size() > 0) {
				JSONObject jsonMessage = jsonMessages.getJSONObject(0);
				message = toMap(jsonMessage);

				if (message.containsKey("Extension")) {
					JSONObject jsonExtension = jsonMessage.getJSONObject("Extension");
					Extension = toMap(jsonExtension);

					if (Extension.containsKey("data")) {
						JSONObject jsonData = jsonExtension.getJSONObject("data");
						data = toMap(jsonData);
					}
				}
			}
		}

		if (resultMap.containsKey("aps")) {
			JSONObject jsonApsMap = jsonResultMap.getJSONObject("aps");
			apsMap = toMap(jsonApsMap);
		}
		
		if(resultMap.containsKey("iosAlert")){ 
			JSONObject jsonIosAlertMap = jsonResultMap.getJSONObject("iosAlert");
		}
		
		if(resultMap.containsKey("pushModelMessage")){
			JSONObject jsonpushModelMessageMap = jsonResultMap.getJSONObject("pushModelMessage");
		}
		
	}
	
	/**
	 * 转换成MAP
	 * @param object
	 * @return
	 * @throws JSONException
	 */
	private static Map<String, Object> toMap(JSONObject object) throws JSONException {
	    Map<String, Object> map = new HashMap<String, Object>();

	    @SuppressWarnings("unchecked")
		Iterator<String> keysItr = object.keys();
	    while(keysItr.hasNext()) {
	        String key = keysItr.next();
	        Object value = object.get(key);

	        if(value instanceof JSONArray) {
	            value = toList((JSONArray) value);
	        }

	        else if(value instanceof JSONObject) {
	            value = toMap((JSONObject) value);
	        }
	        map.put(key, value);
	    }
	    return map;
	}
	
	/**
	 * 转换成LIST	
	 * @param array
	 * @return
	 * @throws JSONException
	 */
	private static List<Object> toList(JSONArray array) throws JSONException {
	    List<Object> list = new ArrayList<Object>();
	    for(int i = 0; i < array.length(); i++) {
	        Object value = array.get(i);
	        if(value instanceof JSONArray) {
	            value = toList((JSONArray) value);
	        }

	        else if(value instanceof JSONObject) {
	            value = toMap((JSONObject) value);
	        }
	        list.add(value);
	    }
	    return list;
	}
	
	
	// 发送时机
	public static final String permission = "permission"; // 后台发送
	public static final String PERMISSION = "PERMISSION"; // 技师端／专家端发送
	public static final String AGREE = "AGREE"; // 专家点击同意问诊时发送
	public static final String ENTER = "ENTER"; // 技师创建好房间之后推送
	public static final String FILEDOWNLOAD = "FILEDOWNLOAD"; // 专家上传文件完成后
	public static final String REFUSE = "REFUSE"; // 专家收到技师／专家的邀请后点击拒绝或30s没有处理的时候发送
	public static final String DETAIL = "ACTION／MAINTENANCE／UPDATED"; // 有具体需求的时候发送
	public static final String CLOSE = "CLOSE"; // 挂断电话

	/**
	 * 数据装箱
	 * 
	 * @param typeStr
	 * @param map
	 */
	@SuppressWarnings("unused")
	public MessageType(String typeStr, Map<String, Object> map) {
		message = new HashMap<>();
		data = new HashMap<>();
		Extension = new HashMap<>();
		messageArr = new ArrayList<Object>();
		resultMap = new HashMap<>();

		/************************* 必填 start */
		message.put("Account", map.get("Account")); // 根据最新需求， 该字段为填写接受者账户
		Object temData = map.get("Data");
		message.put("Data", new String[] { temData == null ? "占位字符串" : temData.toString() }); // 占位字符串

		// 模板名称（有默认值）
		Object templateIdObj = map.get("TemplateId");
		message.put("TemplateId", (templateIdObj == null || templateIdObj.equals(""))
				? "7b3026db-7f58-4e80-a723-8db4a5134931" : templateIdObj);

		// 客户端id（默认值） 这个id是客户端id，只要是这个项目里用的都传llyj(蓝领驿家的简称)
		Object clientidObj = map.get("clientid");
		message.put("clientid", (clientidObj == null || clientidObj.equals("")) ? "llyj" : clientidObj);

		/************************* 必填 end **/
		
		/************************* 选择性必填 start *****/
		if (!PushType.valueOf(typeStr).getPushType().equals("")) { 
			switch (typeStr) {
			case permission: // 询问专家是否接收本次问诊 - 后台发送
				Extension.put("type", permission); // 推送类型
				data.put("SAccount", map.get("SAccount")); // 发起询问或邀请的账号
				data.put("SName", map.get("SName")); // 发起询问或邀请的姓名

				resultMap.put("aps", buildApsMap(map));
				break;

			case PERMISSION: // 询问专家是否接收本次问诊 - 技师端／专家端发送
				Extension.put("type", PERMISSION); // 推送类型
				data.put("SAccount", map.get("SAccount")); // 发起询问或邀请的账号
				data.put("SName", map.get("SName")); // 发起询问或邀请的姓名
				data.put("RoomID", map.get("RoomID")); // 会议的房间ID

				resultMap.put("aps", buildApsMap(map));
				break;

			case AGREE: // 同意本次问诊通知技术创建房间
				Extension.put("type", AGREE);
				data.put("PAccount", map.get("PAccount")); // 专家的账号
				data.put("PName", map.get("PName")); // 专家姓名
				break;

			case ENTER: // 创建并假如房间后，将房间号推送给专家
				Extension.put("type", ENTER);
				data.put("RoomID", map.get("RoomID")); // 房间编号
				break;

			case FILEDOWNLOAD: // 文件下载地址的推送
				Extension.put("type", FILEDOWNLOAD);
				data.put("FileTitle", map.get("FileTitle")); // 文件名称
				data.put("FileSize", map.get("FileSize")); // 文件大小
				data.put("FileType", map.get("FileType")); // 文件下载路径
				data.put("FileUrl", map.get("FileUrl")); // 文件类型
				break;

			case REFUSE: // 专家拒绝技师／专家的邀请
				Extension.put("type", REFUSE);
				data.put("PId", map.get("PId")); // 专家ID
				data.put("PName", map.get("PName")); // 专家姓名
				break;

			case DETAIL: // 通用推送，例如：活动信息通知，系统维护通知，新版本通知等等。
				Extension.put("type", DETAIL);
				data.put("Message", map.get("Message")); // 消息内容
				data.put("ImageUrl", map.get("ImageUrl")); // 如果有图片，图片加载地址
				data.put("UpdateUrl", map.get("UpdateUrl")); // 应用更新地址
				break;
			
			case CLOSE: //挂断电话
				Extension.put("type", CLOSE);
				break;
				
			default:
				break;
			}

			Extension.put("data", data);
			message.put("Extension", Extension);
			messageArr.add(message);
			resultMap.put("Messages", messageArr);
			
			//FIXME 二期升级推送时根据业务修改规则
			if(1 == 2){
			}
			
		}
		/************************* 选择性必填 end *****/

	}

	public MessageType() {
	}
	
	/**
	 * 拼装APS
	 * @param map
	 * @return
	 */
	private Map<String, Object> buildApsMap(Map<String, Object> map){
		Object alert = map.get("alert"); // 响铃提示消息
		apsMap = new HashMap<String, Object>(); // 响铃
		apsMap.put("badge", "1"); // 固定值 ios
		apsMap.put("sound", "pushring.caf"); // 固定值 ios
		// 提示消息 （SName + 需要您为他远程诊断）
		apsMap.put("alert", (alert == null || alert.equals("")) ? "要您为他远程诊断" : alert); //andriod and ios
		if(map.containsKey("contentAvailable")){
			apsMap.put("contentAvailable", map.get("contentAvailable")); //ios
		}
		if(map.containsKey("builderId")){
			apsMap.put("builderId", map.get("builderId")); //andriod
		}
		if(map.containsKey("title")){
			apsMap.put("title", map.get("title")); //andriod
		}
		
		return apsMap;
	}
	


	/**
	 * 将Map字符串转换成Map
	 * 
	 * @param mapStr
	 * @return
	 */
	private static Map<String, Object> mapStrToMap(String mapStr) {
		GsonBuilder gb = new GsonBuilder();
		gb.setLongSerializationPolicy(LongSerializationPolicy.STRING);
		Gson gson = gb.create();
		
		Map<String, Object> jsonMap = null;
		try {
			jsonMap = gson.fromJson(mapStr, new TypeToken<Map<String, Object>>() {
				private static final long serialVersionUID = -9191093792962144105L;
			}.getType());
		} catch (JsonSyntaxException e) {
			jsonMap = new HashMap<>();
		}
		return jsonMap;
	}

	/**
	 * 将list字符串转换成List
	 * 
	 * @return
	 */
	@SuppressWarnings("unused")
	private static List<Map<String, Object>> listStrToList(String listStr) {
		GsonBuilder gb = new GsonBuilder();
		gb.setLongSerializationPolicy(LongSerializationPolicy.STRING);
		Gson gson = gb.create();
		
		List<Map<String, Object>> messageArr = null;
		try {
			messageArr = gson.fromJson(listStr, new TypeToken<List<Map<String, Object>>>() {
				private static final long serialVersionUID = 1L;
			}.getType());
		} catch (JsonSyntaxException e) {
			messageArr = new ArrayList<>();
		}
		return messageArr;
	}

	/**
	 * 获取webScoket推送的账号
	 * 
	 * @return
	 */
	public String getWebAcount(MessageType mType) {
		String Account = ""; // 最终推送的账号
		data = mType.getData();
		Extension = mType.getExtension();
		message = mType.getMessage();
		String typeStr = Extension.get("type").toString();
		switch (typeStr) {
		case permission: // 询问专家是否接收本次问诊 - 后台发送
			
			break;

		case PERMISSION: // 询问专家是否接收本次问诊 - 技师端／专家端发送
			if(message.containsKey("Account")){
				if(message.get("Account") != null){
					Account = message.get("Account").toString();
				}
			}
			break;

		case AGREE: // 同意本次问诊通知技术创建房间
			
			break;

		case ENTER: // 创建并假如房间后，将房间号推送给专家
			
			break;

		case FILEDOWNLOAD: // 文件下载地址的推送
			
			break;

		case REFUSE: // 专家拒绝技师／专家的邀请
			
			break;

		case DETAIL: // 通用推送，例如：活动信息通知，系统维护通知，新版本通知等等。
			
			break;
		
		case CLOSE: //挂断电话
			if(message.containsKey("Account")){
				if(message.get("Account") != null){
					Account = message.get("Account").toString();
				}
			}
			break;
			
		default:
			break;
		}
		
		return Account;
	}

}
