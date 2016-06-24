package com.hengda.smart.media;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.hengda.smart.wt.R;
import com.orhanobut.logger.Logger;

import org.kymjs.kjframe.utils.KJLoger;

/**
 *
 */
public class MusicService extends Service implements MusicControlInterface ,OnCompletionListener{


	//mediaPlayer相关类
	private MediaPlayer  mediaPlayer;
	private boolean isPause; //暂停状态
	private String mediaPath;//音频路劲
	private int duration; //播放总时长
	private int currentTime;//设置播放时间。
	private int msg;//接受服务消息
	//播放命令常量

	//人工讲解所用到空音频
	private MediaPlayer micPlayer;
	//配置文件工具

	private Handler handler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 1) {
				if(mediaPlayer != null) {
					currentTime = mediaPlayer.getCurrentPosition(); // 获取当前音乐播放的位置
					Intent intent = new Intent();
					intent.setAction(MediaConstant.MUSIC_CURRENT);
					intent.putExtra(MediaConstant.FILELD_CURRENT, currentTime);
					sendBroadcast(intent); // 给PlayerActivity发送广播
					handler.sendEmptyMessageDelayed(1, 1000);
				}

			}
		}
	};
	@Override
	public void onCreate() {
		super.onCreate();
		mediaPlayer = new MediaPlayer();
//		micPlayer= new MediaPlayer();
    	micPlayer=MediaPlayer.create(getApplicationContext(), R.raw.mute);
		try {
//    		micPlayer.reset();// 把各项参数恢复到初始状态
//			micPlayer.setDataSource(DbConfig.Music.MUTE_PATH);
			micPlayer.setLooping(true);
//			micPlayer.prepare();
			micPlayer.start();
			KJLoger.debug("kaiqidsf sdf ");
		} catch (IllegalArgumentException e1) {
			e1.printStackTrace();
		} catch (SecurityException e1) {
			e1.printStackTrace();
		} catch (IllegalStateException e1) {
			e1.printStackTrace();
		}

	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}


	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		try {
			mediaPath = intent.getStringExtra("url");        //路径
			msg = intent.getIntExtra("MSG", 0);         //播放信息
			currentTime=intent.getIntExtra("current", 0);
			if (msg == MediaConstant.MSG.PLAY_MSG) {    //直接播放音乐
				play(mediaPath,0,0);
			} else if (msg == MediaConstant.MSG.PAUSE_MSG) {    //暂停
				pause();
			} else if (msg == MediaConstant.MSG.STOP_MSG) {     //停止
				stop();
			} else if (msg == MediaConstant.MSG.CONTINUE_MSG) { //继续播放
				resume();
			} else if (msg == MediaConstant.MSG.PROGRESS_CHANGE) {  //进度更新
				currentTime = intent.getIntExtra("progress", -1);
				play(mediaPath,currentTime,0);
			} else if (msg == MediaConstant.MSG.MIC_PLAY) {
				micPlayer.start();
			}  else if(msg ==MediaConstant.MSG.MIC_PAUSE){
				micPlayer.pause();
			}

		} catch (Exception e) {
			// TODO: handle exception
		}

		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	@Deprecated
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
	}

	@Override
	public void setMusicKinds() {

	}

	@Override
	public void setDataRes() {

	}

	@Override
	public void stop() {
		if (mediaPlayer != null) {
			mediaPlayer.stop();
			try {
				mediaPlayer.prepare(); // 在调用stop后如果需要再次通过start进行播放,需要之前调用prepare函数
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void pause() {

		if (mediaPlayer != null && mediaPlayer.isPlaying()) {
			mediaPlayer.pause();
			isPause = true;
		}
	}

	@Override
	public void destroy() {
		if (mediaPlayer != null) {
			mediaPlayer.stop();
			mediaPlayer.release();
			mediaPlayer = null;
		}
	}

	@Override
	public void play(String path, int currentTime, int kind) {
		Logger.v(""+path);
		try {
			mediaPlayer.reset();// 把各项参数恢复到初始状态
			mediaPlayer.setDataSource(path);
			mediaPlayer.prepare(); // 进行缓冲
			mediaPlayer.setOnPreparedListener(new PreparedListener(currentTime));// 注册一个监听器
			mediaPlayer.setOnCompletionListener(this);
			handler.sendEmptyMessage(1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void resume() {
		if (isPause) {
			mediaPlayer.start();
			isPause = false;
		}
	}
	@Override
	public void previous() {

	}
	@Override
	public void next() {

	}

	/**
	 * 实现一个OnPrepareLister接口,当音乐准备好的时候开始播放
	 * @Description TODO
	 * @author wzq
	 * @date 2015-7-6 上午11:18:12
	 * @update (date)
	 * @version V1.0
	 */
	private final class PreparedListener implements OnPreparedListener {
		private int currentTime;

		public PreparedListener(int currentTime) {
			this.currentTime = currentTime;
		}

		@Override
		public void onPrepared(MediaPlayer mp) {
			mediaPlayer.start(); // 开始播放
			if (currentTime > 0) { // 如果音频不是从头播放
				mediaPlayer.seekTo(currentTime);
			}
			Intent intent = new Intent();
			intent.setAction(MediaConstant.MUSIC_DURATION);
			duration = mediaPlayer.getDuration();
			intent.putExtra(MediaConstant.FILELD_DURATION, duration);  //通过Intent来传递歌曲的总长度
			sendBroadcast(intent);
		}
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		sendBroadcast(new Intent(MediaConstant.MUSIC_PLAY_COM));
		handler.removeMessages(1);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if(micPlayer!=null){
			micPlayer.stop(); //停止播放
			micPlayer.reset(); //重置状态到uninitialized态
		}
		if(mediaPlayer!=null){
			mediaPlayer.stop(); //停止播放
			mediaPlayer.reset(); //重置状态到uninitialized态
		}
	}


}
     
