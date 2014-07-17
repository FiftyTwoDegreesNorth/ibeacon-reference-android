package org.altbeacon.beaconreference;

import android.os.Bundle;
import android.os.RemoteException;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.Region;

/**
 * 
 * @author dyoung
 * @author Matt Tyler
 */
public class MonitoringActivity extends Activity implements BeaconConsumer {
	protected static final String TAG = "MonitoringActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_monitoring);
		verifyBluetooth();
	    beaconManager.bind(this);
	    
		//initializing simulated iBeacons
		//BeaconManager.setBeaconSimulator(new TimedBeaconSimulator() );
		//((TimedBeaconSimulator) BeaconManager.getBeaconSimulator()).createTimedSimulatedBeacons();
	}
	
	public void onRangingClicked(View view) {
		Intent myIntent = new Intent(this, RangingActivity.class);
		this.startActivity(myIntent);
	}
	public void onBackgroundClicked(View view) {
		Intent myIntent = new Intent(this, BackgroundActivity.class);
		this.startActivity(myIntent);
	}

	private void verifyBluetooth() {

		try {
			if (!BeaconManager.getInstanceForApplication(this).checkAvailability()) {
				final AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("Bluetooth not enabled");			
				builder.setMessage("Please enable bluetooth in settings and restart this application.");
				builder.setPositiveButton(android.R.string.ok, null);
				builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
					@Override
					public void onDismiss(DialogInterface dialog) {
						finish();
			            System.exit(0);					
					}					
				});
				builder.show();
			}			
		}
		catch (RuntimeException e) {
			final AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Bluetooth LE not available");			
			builder.setMessage("Sorry, this device does not support Bluetooth LE.");
			builder.setPositiveButton(android.R.string.ok, null);
			builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

				@Override
				public void onDismiss(DialogInterface dialog) {
					finish();
		            System.exit(0);					
				}
				
			});
			builder.show();
			
		}
		
	}	

    private BeaconManager beaconManager = org.altbeacon.beacon.BeaconManager.getInstanceForApplication(this);

    @Override 
    protected void onDestroy() {
        super.onDestroy();
        beaconManager.unBind(this);
    }
    @Override 
    protected void onPause() {
    	super.onPause();
    	if (beaconManager.isBound(this)) beaconManager.setBackgroundMode(this, true);
    }
    @Override 
    protected void onResume() {
    	super.onResume();
    	if (beaconManager.isBound(this)) beaconManager.setBackgroundMode(this, false);
    }    
    
    private void logToDisplay(final String line) {
    	runOnUiThread(new Runnable() {
    	    public void run() {
    	    	EditText editText = (EditText)MonitoringActivity.this
    					.findViewById(R.id.monitoringText);
       	    	editText.append(line+"\n");            	    	    		
    	    }
    	});
    }
    @Override
    public void onBeaconServiceConnect() {
        beaconManager.setMonitorNotifier(new MonitorNotifier() {
        @Override
        public void didEnterRegion(Region region) {
          logToDisplay("I just saw a beacon named "+ region.getUniqueId() +" for the first time!" );
        }

        @Override
        public void didExitRegion(Region region) {
        	logToDisplay("I no longer see a beacon named "+ region.getUniqueId());
        }

        @Override
        public void didDetermineStateForRegion(int state, Region region) {
        	logToDisplay("I have just switched from seeing/not seeing beacons: "+state);
        }


        });

        try {
        	beaconManager.startMonitoringBeaconsInRegion(new Region("myMonitoringUniqueId", null, null, null));
        	
        	//Sample Simulated iBeacons
        	//iBeaconManager.startMonitoringBeaconsInRegion(new Region("test1","DF7E1C79-43E9-44FF-886F-1D1F7DA6997A".toLowerCase(), 1, 1));
        	//iBeaconManager.startMonitoringBeaconsInRegion(new Region("test2","DF7E1C79-43E9-44FF-886F-1D1F7DA6997B".toLowerCase(), 1, 2));
        	//iBeaconManager.startMonitoringBeaconsInRegion(new Region("test3","DF7E1C79-43E9-44FF-886F-1D1F7DA6997C".toLowerCase(), 1, 3));
        	//iBeaconManager.startMonitoringBeaconsInRegion(new Region("test4","DF7E1C79-43E9-44FF-886F-1D1F7DA6997D".toLowerCase(), 1, 4));
        } catch (RemoteException e) {   }
    }
	
}
