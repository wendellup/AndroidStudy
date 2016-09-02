package com.example.egame.entity;

import java.io.Serializable;

public class GameDetailBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6546057395205637671L;

	private Integer gameId;
	private String gameName;
	private String gameIntroduction;
	private String gameIcon;
	private String gameDownloadUrl;
	
	public Integer getGameId() {
		return gameId;
	}
	public void setGameId(Integer gameId) {
		this.gameId = gameId;
	}
	public String getGameName() {
		return gameName;
	}
	public void setGameName(String gameName) {
		this.gameName = gameName;
	}
	public String getGameIntroduction() {
		return gameIntroduction;
	}
	public void setGameIntroduction(String gameIntroduction) {
		this.gameIntroduction = gameIntroduction;
	}
	public String getGameIcon() {
		return gameIcon;
	}
	public void setGameIcon(String gameIcon) {
		this.gameIcon = gameIcon;
	}
	public String getGameDownloadUrl() {
		return gameDownloadUrl;
	}
	public void setGameDownloadUrl(String gameDownloadUrl) {
		this.gameDownloadUrl = gameDownloadUrl;
	}
	
}
