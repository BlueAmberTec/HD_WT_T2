package com.hengda.smart.stc;

import android.content.Context;
import android.content.SharedPreferences;

/**
 *   STC本地存储工具
 */
public class SharePreStcUtil {
	private SharedPreferences sp;
	private SharedPreferences.Editor editor;
	
	public SharePreStcUtil(Context context, String file) {
		sp = context.getSharedPreferences(file, Context.MODE_PRIVATE);
		editor = sp.edit();
	}

	//==频道
	public void setChannel(int channel){
		editor.putInt("Channel", channel);
		editor.commit();
	}
	public int getChannel(){
		return sp.getInt("Channel",10);
	}

	//==音量
	public void setVolume(int volume){
		editor.putInt("Volume", volume);
		editor.commit();
	}
	public int getVolume(){
		return sp.getInt("Volume",10);
	}

	//==ID
	public void setID(int id){
		editor.putInt("id", id);
		editor.commit();
	}
	public int getID(){
		return sp.getInt("id",10);
	}

	//==提醒
	public void setTiXing(int id){
		editor.putInt("TiXing", id);
		editor.commit();
	}
	public int getTiXing(){
		return sp.getInt("TiXing",0);
	}
	//==模式
	public void setModle(int id){
		editor.putInt("Modle", id);
		editor.commit();
	}
	public int getModle(){
		return sp.getInt("Modle",1);
	}


	//==ID匹配
	public void setIdPiPei(int state){
		editor.putInt("IDPP", state);
		editor.commit();
	}

	public int getIdPiPei(){
		return sp.getInt("IDPP",0);
	}

	//==ID匹配
	public void setRengongJJ(int state){
		editor.putInt("RGJJ", state);
		editor.commit();
	}

	public int getRengongJJ(){
		return sp.getInt("RGJJ",0);
	}


}




