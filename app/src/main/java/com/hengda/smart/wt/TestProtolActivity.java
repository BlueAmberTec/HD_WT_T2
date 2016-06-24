package com.hengda.smart.wt;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.hengda.smart.application.Application;
import com.hengda.smart.media.MediaConstant;
import com.hengda.smart.media.MusicService;
import com.hengda.smart.stc.ControlService;
import com.hengda.smart.stc.SharePreStcUtil;
import com.hengda.smart.stc.StcConfig;
import com.hengda.smart.stc.StcLauncher;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author WZQ
 * @version V1.0
 * @Email :wzq_steady@126.com
 * @date 2016/6/17 14:23
 * @update (date)
 */
public class TestProtolActivity extends Activity {

    @BindView(R.id.btn_open_rengong)
    Button btnOpenRengong;
    @BindView(R.id.btn_close_tongxin)
    Button btnCloseTongxin;
    @BindView(R.id.btn_open_id_pipei)
    Button btnOpenIdPipei;
    @BindView(R.id.btn_close_id_pipei)
    Button btnCloseIdPipei;
    @BindView(R.id.btn_channel_set)
    Button btnChannelSet;
    @BindView(R.id.btn_modle_gaoyz)
    Button btnModleGaoyz;
    @BindView(R.id.btn_modle_kanggr)
    Button btnModleKanggr;
    @BindView(R.id.btn_id_set)
    Button btnIdSet;
    @BindView(R.id.btn_volume_set)
    Button btnVolumeSet;
    @BindView(R.id.btn_tixing_open)
    Button btnTixingOpen;
    @BindView(R.id.btn_tixing_close)
    Button btnTixingClose;
    @BindView(R.id.container)
    LinearLayout container;
    @BindView(R.id.etv_channel)
    EditText etvChannel;
    @BindView(R.id.etv_id)
    EditText etvId;
    @BindView(R.id.etv_volume)
    EditText etvVolume;
    @BindView(R.id.btn_openmic)
    Button btnOpenmic;
    @BindView(R.id.btn_closemic)
    Button btnClosemic;

    //==工具
    private StcLauncher stcLauncher;
    private SharePreStcUtil stcSaveUtil;
    //mic音频播放
    private Intent  playIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_protol);
        ButterKnife.bind(this);
        startService(new Intent(this, ControlService.class));
        stcLauncher = new StcLauncher((Application) this.getApplication(), this);
        stcSaveUtil = new SharePreStcUtil(this, StcConfig.SHARENAME);
        playIntent =new Intent(this, MusicService.class);
        playIntent.setAction("hengda.media.play.action");
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
    }

    @OnClick({R.id.btn_open_rengong,
            R.id.btn_close_tongxin, R.id.btn_open_id_pipei, R.id.btn_close_id_pipei,
            R.id.btn_channel_set, R.id.btn_modle_gaoyz, R.id.btn_modle_kanggr,
            R.id.btn_id_set, R.id.btn_volume_set, R.id.btn_tixing_open,
            R.id.btn_tixing_close, R.id.container,
            R.id.btn_openmic,R.id.btn_closemic})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_open_rengong:
                stcLauncher.openRenGongAudio();
                break;
            case R.id.btn_close_tongxin:
                stcLauncher.closeTongXin();
                break;
            case R.id.btn_open_id_pipei:
                stcLauncher.openIDPiPei();
                break;
            case R.id.btn_close_id_pipei:
                stcLauncher.exitIDPiPei();
                break;
            case R.id.btn_channel_set:
                if (etvChannel.getText().toString().length() > 0) {
                    stcLauncher.setChannel(Integer.parseInt(etvChannel.getText().toString()));
                    stcSaveUtil.setChannel(Integer.parseInt(etvChannel.getText().toString()));
                }
                break;
            case R.id.btn_modle_gaoyz:
                stcLauncher.setModleGaoYinZ();
                break;
            case R.id.btn_modle_kanggr:
                stcLauncher.setModleKangGanR();
                break;
            case R.id.btn_id_set:
                if (etvId.getText().toString().length() > 0) {
                    stcLauncher.setID(Integer.parseInt(etvId.getText().toString()));
                    stcSaveUtil.setID(Integer.parseInt(etvId.getText().toString()));
                }
                break;
            case R.id.btn_volume_set:
                if (etvVolume.getText().toString().length() > 0) {
                    stcLauncher.setVolume(Integer.parseInt(etvVolume.getText().toString()));
                    stcSaveUtil.setVolume(Integer.parseInt(etvVolume.getText().toString()));
                }
                break;
            case R.id.btn_tixing_open:
                stcLauncher.setTiXing((byte) 0x80);
                break;
            case R.id.btn_tixing_close:
                stcLauncher.setTiXing((byte) 0x00);
                break;
            case R.id.container:
                break;
            case R.id.btn_openmic:
                playIntent.putExtra("MSG", MediaConstant.MSG.MIC_PLAY);
                this.startService(playIntent);
                break;
            case R.id.btn_closemic:
                playIntent.putExtra("MSG", MediaConstant.MSG.MIC_PAUSE);
                this.startService(playIntent);

                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(this, ControlService.class));
        stopService(new Intent(this,MusicService.class));
        System.exit(0);
    }


}
