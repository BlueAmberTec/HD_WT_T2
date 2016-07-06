package com.hengda.smart.wt;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

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
 * @Description ${todo}
 * @Email :wzq_steady@126.com
 * @date 2016/7/5 10:59
 * @update (date)
 */
public class ProtocolActivity extends Activity {

    @BindView(R.id.tv_set_rengong)
    TextView tvSetRengong;
    @BindView(R.id.btn_switch_rengong)
    Button btnSwitchRengong;
    @BindView(R.id.tv_diaodui_state)
    TextView tvDiaoduiState;
    @BindView(R.id.btn_switch_diaodui)
    Button btnSwitchDiaodui;
    @BindView(R.id.tv_set_idpipei)
    TextView tvSetIdpipei;
    @BindView(R.id.btn_switch_idpipei)
    Button btnSwitchIdpipei;
    @BindView(R.id.etv_channel)
    EditText etvChannel;
    @BindView(R.id.btn_channel_set)
    Button btnChannelSet;
    @BindView(R.id.etv_id)
    EditText etvId;
    @BindView(R.id.btn_id_set)
    Button btnIdSet;
    @BindView(R.id.etv_volume)
    EditText etvVolume;
    @BindView(R.id.btn_volume_set)
    Button btnVolumeSet;
    @BindView(R.id.tv_set_modle)
    TextView tvSetModle;
    @BindView(R.id.btn_modle_gaoyz)
    Button btnModleGaoyz;
    @BindView(R.id.btn_modle_kanggr)
    Button btnModleKanggr;
    @BindView(R.id.btn_openmic)
    Button btnOpenmic;
    @BindView(R.id.btn_closemic)
    Button btnClosemic;
    @BindView(R.id.btn_close_tongxin)
    Button btnClosetx;
    @BindView(R.id.container)
    LinearLayout container;


    //==工具
    private StcLauncher stcLauncher;
    private SharePreStcUtil stcSaveUtil;
    //mic音频播放
    private Intent playIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_protol);
        ButterKnife.bind(this);
        startService(new Intent(this, ControlService.class));
        stcLauncher = new StcLauncher((Application) this.getApplication(), this);
        stcSaveUtil = new SharePreStcUtil(this, StcConfig.SHARENAME);
        resetConfig();
        playIntent =new Intent(this, MusicService.class);
        playIntent.setAction("hengda.media.play.action");
        refreshUi();
    }

    private void resetConfig() {
        stcSaveUtil.setRengongJJ(0);
        stcSaveUtil.setIdPiPei(0);
    }

    @OnClick({R.id.btn_switch_rengong, R.id.btn_switch_diaodui, R.id.btn_switch_idpipei, R.id.btn_channel_set, R.id.btn_id_set, R.id.btn_volume_set, R.id.btn_modle_gaoyz, R.id.btn_modle_kanggr, R.id.btn_openmic, R.id.btn_closemic, R.id.btn_close_tongxin})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_switch_rengong:
                if(stcSaveUtil.getRengongJJ()==0){
                    stcSaveUtil.setRengongJJ(1);
                    stcLauncher.openRenGongAudio();
                    playIntent.putExtra("MSG", MediaConstant.MSG.MIC_PLAY);
                    this.startService(playIntent);
                    refreshUi();
                }else {
                    stcSaveUtil.setRengongJJ(0);
                    stcLauncher.closeTongXin();
                    playIntent.putExtra("MSG", MediaConstant.MSG.MIC_PAUSE);
                    this.startService(playIntent);
                    refreshUi();
                }

                break;
            case R.id.btn_switch_diaodui:
                if(stcSaveUtil.getTiXing()==120){
                    stcSaveUtil.setTiXing(0);
                    stcLauncher.setTiXing();
                    refreshUi();
                }else {
                    stcSaveUtil.setTiXing(120);
                    stcLauncher.setTiXing();
                    refreshUi();
                }
                break;
            case R.id.btn_switch_idpipei:
                if(stcSaveUtil.getIdPiPei()==0){
                    stcSaveUtil.setIdPiPei(1);
                    stcLauncher.openIDPiPei();
                    refreshUi();
                }else {
                    stcSaveUtil.setIdPiPei(0);
                    stcLauncher.exitIDPiPei();
                    refreshUi();
                }
                break;
            case R.id.btn_modle_gaoyz:
                stcSaveUtil.setModle(1);
                stcLauncher.setModle();
                refreshUi();
                break;
            case R.id.btn_modle_kanggr:
                stcSaveUtil.setModle(2);
                stcLauncher.setModle();
                refreshUi();
                break;
            case R.id.btn_openmic:
                break;
            case R.id.btn_closemic:
                break;
            case R.id.btn_close_tongxin:
                stcLauncher.closeTongXin();
                break;
            case R.id.btn_channel_set:
                if (etvChannel.getText().toString().length() > 0) {
                    stcLauncher.setChannel();
                    stcSaveUtil.setChannel(Integer.parseInt(etvChannel.getText().toString()));
                }
                break;
            case R.id.btn_id_set:
                if (etvId.getText().toString().length() > 0) {
                    stcLauncher.setID();
                    stcSaveUtil.setID(Integer.parseInt(etvId.getText().toString()));
                }
                break;
            case R.id.btn_volume_set:
                if (etvVolume.getText().toString().length() > 0) {
                    stcLauncher.setVolume();
                    stcSaveUtil.setVolume(Integer.parseInt(etvVolume.getText().toString()));
                }
                break;
        }
    }


    public void refreshUi(){

        etvChannel.setText(stcSaveUtil.getChannel() + "");
        etvId.setText(stcSaveUtil.getID() + "");
        etvVolume.setText(stcSaveUtil.getVolume() + "");
        if (stcSaveUtil.getTiXing() == 120) {
            tvDiaoduiState.setText("开启");
            btnSwitchDiaodui.setBackgroundResource(R.mipmap.setting_bofang_on);
        } else if (stcSaveUtil.getTiXing() == 0) {
            tvDiaoduiState.setText("关闭");
            btnSwitchDiaodui.setBackgroundResource(R.mipmap.setting_bofang_off);
        }

        if(stcSaveUtil.getIdPiPei()==0){
            tvSetIdpipei.setText("关闭");
            btnSwitchIdpipei.setBackgroundResource(R.mipmap.setting_bofang_off);
        }else if(stcSaveUtil.getIdPiPei()==1){
            tvSetIdpipei.setText("开启");
            btnSwitchIdpipei.setBackgroundResource(R.mipmap.setting_bofang_on);
        }

        if(stcSaveUtil.getRengongJJ()==0){
            tvSetRengong.setText("关闭");
            btnSwitchRengong.setBackgroundResource(R.mipmap.setting_bofang_off);
        }else if(stcSaveUtil.getRengongJJ()==1){
            tvSetRengong.setText("开启");
            btnSwitchRengong.setBackgroundResource(R.mipmap.setting_bofang_on);
        }

        if (stcSaveUtil.getModle() == 1) {
            tvSetModle.setText("高音质模式");
        } else if (stcSaveUtil.getModle() == 2) {
            tvSetModle.setText("抗干扰模式");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        stcSaveUtil.setRengongJJ(0);
//        stcSaveUtil.setIdPiPei(0);
    }
}
