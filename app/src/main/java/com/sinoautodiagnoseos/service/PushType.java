package com.sinoautodiagnoseos.service;

/**
 * 推送类型列表
 * @author xiams
 */
public enum PushType {
	permission("permission"), //后台发送
	PERMISSION("PERMISSION"), //技师端／专家端发送
	AGREE("AGREE"), //专家点击同意问诊时发送
	ENTER("ENTER"), //技师创建好房间之后推送
	FILEDOWNLOAD("FILEDOWNLOAD"), //专家上传文件完成后
	REFUSE("REFUSE"), //专家收到技师／专家的邀请后点击拒绝或30s没有处理的时候发送
	DETAIL("ACTION／MAINTENANCE／UPDATED"); //有具体需求的时候发送
	
	public String PushType;
	PushType(String pStr) {
		PushType = pStr;
	}
	
	public String getPushType(){
		return PushType;
	}
}
