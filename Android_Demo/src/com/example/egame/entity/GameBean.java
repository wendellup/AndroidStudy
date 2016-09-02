package com.example.egame.entity;

import java.io.Serializable;

public class GameBean implements Serializable{

	private static final long serialVersionUID = -16461674045804013L;
	
	public Integer gameId;
	public String gameName;
	public String gameRecommendWord;
	public String gameIcon;
	
	public String getGameName() {
		return gameName;
	}
	public void setGameName(String gameName) {
		this.gameName = gameName;
	}
	public String getGameRecommendWord() {
		return gameRecommendWord;
	}
	public void setGameRecommendWord(String gameRecommendWord) {
		this.gameRecommendWord = gameRecommendWord;
	}
	public String getGameIcon() {
		return gameIcon;
	}
	public void setGameIcon(String gameIcon) {
		this.gameIcon = gameIcon;
	}
	public Integer getGameId() {
		return gameId;
	}
	public void setGameId(Integer gameId) {
		this.gameId = gameId;
	}
	
}
