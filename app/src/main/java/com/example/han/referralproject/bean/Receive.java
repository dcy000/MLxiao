package com.example.han.referralproject.bean;

public class Receive {
	private int id;
	private String app_id;
	private String group_id;
	private String input;
	private String output;
	private String proactive;
	private String topicid;
	private String topic;
	private String emotion;
	private String replay;
	private String output_type;
	private String output_resource;
	private String client_user_id;
	private String tts_url;

	public Receive() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Receive(int id, String app_id, String group_id, String input, String output, String proactive,
			String topicid, String topic, String emotion, String replay, String output_type, String output_resource,
			String client_user_id, String tts_url) {
		super();
		this.id = id;
		this.app_id = app_id;
		this.group_id = group_id;
		this.input = input;
		this.output = output;
		this.proactive = proactive;
		this.topicid = topicid;
		this.topic = topic;
		this.emotion = emotion;
		this.replay = replay;
		this.output_type = output_type;
		this.output_resource = output_resource;
		this.client_user_id = client_user_id;
		this.tts_url = tts_url;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getApp_id() {
		return app_id;
	}

	public void setApp_id(String app_id) {
		this.app_id = app_id;
	}

	public String getGroup_id() {
		return group_id;
	}

	public void setGroup_id(String group_id) {
		this.group_id = group_id;
	}

	public String getInput() {
		return input;
	}

	public void setInput(String input) {
		this.input = input;
	}

	public String getOutput() {
		return output;
	}

	public void setOutput(String output) {
		this.output = output;
	}

	public String getProactive() {
		return proactive;
	}

	public void setProactive(String proactive) {
		this.proactive = proactive;
	}

	public String getTopicid() {
		return topicid;
	}

	public void setTopicid(String topicid) {
		this.topicid = topicid;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getEmotion() {
		return emotion;
	}

	public void setEmotion(String emotion) {
		this.emotion = emotion;
	}

	public String getReplay() {
		return replay;
	}

	public void setReplay(String replay) {
		this.replay = replay;
	}

	public String getOutput_type() {
		return output_type;
	}

	public void setOutput_type(String output_type) {
		this.output_type = output_type;
	}

	public String getOutput_resource() {
		return output_resource;
	}

	public void setOutput_resource(String output_resource) {
		this.output_resource = output_resource;
	}

	public String getClient_user_id() {
		return client_user_id;
	}

	public void setClient_user_id(String client_user_id) {
		this.client_user_id = client_user_id;
	}

	public String getTts_url() {
		return tts_url;
	}

	public void setTts_url(String tts_url) {
		this.tts_url = tts_url;
	}

	@Override
	public String toString() {
		return "Receive [id=" + id + ", app_id=" + app_id + ", group_id=" + group_id + ", input=" + input + ", output="
				+ output + ", proactive=" + proactive + ", topicid=" + topicid + ", topic=" + topic + ", emotion="
				+ emotion + ", replay=" + replay + ", output_type=" + output_type + ", output_resource="
				+ output_resource + ", client_user_id=" + client_user_id + ", tts_url=" + tts_url + "]";
	}

}
