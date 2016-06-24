package com.hengda.smart.media;

public interface MusicControlInterface {
     void setMusicKinds();
     void setDataRes();
     void stop();
     void pause();
     void resume();
     void destroy();
     void play(String path, int seekTime, int kind);
     void previous();
     void next();
}
