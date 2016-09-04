package com.example.egame.constvar;

public class ConstVar {
	public static final String URL_RECOMMEND = "http://open.play.cn/api/v2/mobile/channel/content.json?channel_id=701&terminal_id=100&rows_of_page=20&current_page=";

	public static final String URL_NEW = "http://open.play.cn/api/v2/mobile/channel/content.json?channel_id=702&terminal_id=100&rows_of_page=20&current_page=";

	public static final String URL_NETWORK_GAME = "http://open.play.cn/api/v2/mobile/channel/content.json?channel_id=705&terminal_id=100&rows_of_page=20&current_page=";

	public static final String URL_RANK_DOWNLOAD = "http://open.play.cn/api/v2/mobile/channel/content.json?channel_id=911&terminal_id=100&rows_of_page=20&current_page=";

	public static final String URL_RANK_SOAR = "http://open.play.cn/api/v2/mobile/channel/content.json?channel_id=916&terminal_id=100&rows_of_page=20&current_page=";

	public static final String URL_GAME_DETAIL = "http://open.play.cn/api/v2/mobile/game_detail.json?terminal_id=100&game_id=";

	public static String[] TITLES = new String[] { "推荐", "新品", "网游", "下载榜",
			"飙升榜" };

	public static String[] TITLE_URLS = new String[] { URL_RECOMMEND, URL_NEW,
			URL_NETWORK_GAME, URL_RANK_DOWNLOAD, URL_RANK_SOAR };
}
