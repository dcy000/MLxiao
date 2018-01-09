package com.witspring.model.entity;

import org.json.JSONObject;

/**
 * 封装数据的实体类
 * @author Goven 2014年9月29日 上午11:40:13
 * @email gxl3999@gmail.com
 */
public class Result<T> {

	private String code;// 请求编码
	private int status;// 结果状态码
	private String msg;// 结果消息
	private T content;// 数据内容
	private int flag;// 请求的标识

    public final static int STATUS_SUCCESS = 200;//请求数据成功
	public final static int STATUS_LACK_PARAM = -13;//缺少参数
    public final static int STATUS_NOT_LOGIN = -16;//未登录
    public final static int STATUS_NOT_UPDATE = -99;// 无需更新数据

	public final static int STATUS_NETWORK_ERROR = -1000;// 无网络
	public final static int STATUS_FAIL = -10001;// 请求失败
	public final static int STATUS_DATA_ERROR = -10002;// 数据异常

	public Result() {}

	public Result(JSONObject jObj) {
		this.status = jObj.optInt("status", STATUS_FAIL);
		this.msg = jObj.optString("msg");
		this.code = jObj.optString("code");
	}

	public Result(int status) {
		this.status = status;
        if (status == STATUS_NETWORK_ERROR) {
            msg = "网络不给力！";
        } else if (status == STATUS_FAIL) {
            msg = "服务器繁忙...";
        }
	}

	public boolean successed() {
		return status == STATUS_SUCCESS;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public T getContent() {
		return content;
	}
	public void setContent(T content) {
		this.content = content;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}

}
