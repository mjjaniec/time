package com.github.plushaze.traynotification.notification;

public enum Notifications implements Notification {

	INFORMATION("images/info.png", "#32bef0"),
	NOTICE("images/notice.png", "#8D9695"),
	SUCCESS("images/success.png", "#009961"),
	WARNING("images/warning.png", "#ff9920"),
	ERROR("images/error.png", "#CC0033"),
	QUESTION("images/arrow.png", "#606060");

	private final String urlResource;
	private final String paintHex;

	Notifications(String urlResource, String paintHex) {
		this.urlResource = urlResource;
		this.paintHex = paintHex;
	}

	@Override
	public String getURLResource() {
		return urlResource;
	}

	@Override
	public String getPaintHex() {
		return paintHex;
	}

}
