package com.hengda.smart.wt;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hengda.smart.blelib.Ble10GScanService;
import com.hengda.smart.blelib.HD10GBeacon;

import org.kymjs.kjframe.KJActivity;
import org.kymjs.kjframe.ui.BindView;

public class Ble10GTestActivtity extends KJActivity {
	@BindView(id=R.id.tv_ble_top_no)
	private TextView  bleNoTv;
	@BindView(id=R.id.tv_ble_top_no_ac)
	private TextView  bleNoAC;
	@BindView(id=R.id.tv_ble_top_detail)
	private TextView  bleDetailTv;
	@BindView(id=R.id.btn_open,click=true)
	private Button    openBleBtn;
	@BindView(id=R.id.btn_close,click=true)
	private Button    closeBleBtn;
	
	//�㲥
	private IntentFilter  intentFilter;
	private HD10GBeacon  beacon=new HD10GBeacon();
   	@Override
	public void setRootView() {
		setContentView(R.layout.activity_test);
	}
   	@Override
   	public void initWidget() {
   		super.initWidget();
   		registBeaconReceiver();
   	}
   	
   	@Override
   	public void widgetClick(View v) {
   		super.widgetClick(v);
   		switch (v.getId()) {
		case R.id.btn_open:
			startService(new Intent(Ble10GTestActivtity.this,Ble10GScanService.class));
			break;
		case R.id.btn_close:
			stopService(new Intent(Ble10GTestActivtity.this,Ble10GScanService.class));
			bleNoTv.setText("");
			break;
		default:
			break;
		}
   	}

   	public void registBeaconReceiver(){
   		intentFilter=new IntentFilter();
   		intentFilter.addAction(Ble10GScanService.ACTION_BLE_BEST);
   		intentFilter.addAction(Ble10GScanService.ACTION_BLE_CHULI);
   		registerReceiver(receiver, intentFilter);
   	}

   	private BroadcastReceiver receiver =new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if(Ble10GScanService.ACTION_BLE_BEST.equals(intent.getAction())){
				bleNoTv.setText(""+intent.getIntExtra(Ble10GScanService.TAG_BLE_NO, 0));
				beacon=(HD10GBeacon)intent.getSerializableExtra(Ble10GScanService.TAG_BEACON);
				bleDetailTv.setText(beacon.toString());
			}
			if(Ble10GScanService.ACTION_BLE_CHULI.equals(intent.getAction())){
				bleNoAC.setText(""+intent.getIntExtra(Ble10GScanService.TAG_BLE_NO, 0));
				bleDetailTv.setText(beacon.toString());
			}
		}
	};
}
