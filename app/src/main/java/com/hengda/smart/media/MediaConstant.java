package com.hengda.smart.media;

public class MediaConstant {
	//消息命令嘛
	public class  MSG{
		public static final int PLAY_MSG = 0x01;
		public static final int PAUSE_MSG = 0x02;
		public static final int STOP_MSG = 0x03;
		public static final int CONTINUE_MSG = 0x04;
		public static final int PRIVIOUS_MSG = 0x05;
		public static final int NEXT_MSG = 0x06;
		public static final int PROGRESS_CHANGE = 0x07;
		public static final int PLAYING_MSG = 0x08;
		public static final int MIC_PLAY = 0x09;//mic打开
		public static final int MIC_PAUSE=0x10;//mic关闭
	}

	//服务中消息发送字段
	public static final String FILELD_DURATION ="MUSIC_DURATION"; //总长度
	public static final String FILELD_CURRENT  ="MUSIC_CURRENT"; //总长度


	//播放相关广播
	public static final String MUSIC_DURATION="hengda.media.duration"; //总长度
	public static final String MUSIC_CURRENT ="hengda.media.currentposition";//当前位置
	public static final String MUSIC_PLAY_ACTION ="hengda.media.play";//播放广播
	public static final String MUSIC_PLAY_COM ="hengda.media.cpmletion";//播放广播


}

