package org.altbeacon.beacon;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.util.Log;

import org.altbeacon.beacon.Beacon;

/**
 * Created by Matt Tyler on 4/18/14.
 */
public class TimedBeaconSimulator implements org.altbeacon.beacon.simulator.BeaconSimulator {
	protected static final String TAG = "TimedBeaconSimulator";
	private List<Beacon> beacons;

	/*
	 * You may simulate detection of iBeacons by creating a class like this in your project.
	 * This is especially useful for when you are testing in an Emulator or on a device without BluetoothLE capability.
	 * 
	 * Uncomment lines 36, 37, and 139 - 142 of MonitoringActivity.java and 
	 * set USE_SIMULATED_IBEACONS = true to initialize the sample code in this class.
	 * If using a bluetooth incapable test device (i.e. Emulator), you will want to comment
	 * out the verifyBluetooth() call on line 32 of MonitoringActivity.java as well.
	 * 
	 * Any simulated iBeacons will automatically be ignored when building for production.
	 */
	public boolean USE_SIMULATED_IBEACONS = false;

	/**
	 *  Creates empty iBeacons ArrayList.
	 */
	public TimedBeaconSimulator(){
		beacons = new ArrayList<Beacon>();
	}
	
	/**
	 * Required getter method that is called regularly by the Android iBeacon Library. 
	 * Any iBeacons returned by this method will appear within your test environment immediately. 
	 */
	public List<Beacon> getBeacons(){
		return beacons;
	}
	
	/**
	 * Creates simulated iBeacons all at once.
	 */
	public void createBasicSimulatedBeacons(){
		if (USE_SIMULATED_IBEACONS) {
			Beacon beacon1 = new Beacon("DF7E1C79-43E9-44FF-886F-1D1F7DA6997A".toLowerCase(),
					1, 1);
			Beacon beacon2 = new Beacon("DF7E1C79-43E9-44FF-886F-1D1F7DA6997B".toLowerCase(),
			         1, 2);
			Beacon beacon3 = new Beacon("DF7E1C79-43E9-44FF-886F-1D1F7DA6997C".toLowerCase(),
					1, 3);
			Beacon beacon4 = new Beacon("DF7E1C79-43E9-44FF-886F-1D1F7DA6997D".toLowerCase(),
					1, 4);
			beacons.add(beacon1);
			beacons.add(beacon2);
			beacons.add(beacon3);
			beacons.add(beacon4);


		}
	}
	
	
	private ScheduledExecutorService scheduleTaskExecutor;


	/**
	 * Simulates a new iBeacon every 10 seconds until it runs out of new ones to add.
	 */
	public void createTimedSimulatedBeacons(){
		if (USE_SIMULATED_IBEACONS){
			beacons = new ArrayList<Beacon>();
			Beacon iBeacon1 = new Beacon("DF7E1C79-43E9-44FF-886F-1D1F7DA6997A".toLowerCase(), 1, 1);
			Beacon iBeacon2 = new Beacon("DF7E1C79-43E9-44FF-886F-1D1F7DA6997B".toLowerCase(), 1, 2);
			Beacon iBeacon3 = new Beacon("DF7E1C79-43E9-44FF-886F-1D1F7DA6997C".toLowerCase(), 1, 3);
			Beacon iBeacon4 = new Beacon("DF7E1C79-43E9-44FF-886F-1D1F7DA6997D".toLowerCase(), 1, 4);
			beacons.add(iBeacon1);
			beacons.add(iBeacon2);
			beacons.add(iBeacon3);
			beacons.add(iBeacon4);
			
			final List<Beacon> finalIBeacons = new ArrayList<Beacon>(beacons);

			//Clearing iBeacons list to prevent all iBeacons from appearing immediately.
			//These will be added back into the iBeacons list from finalIBeacons later.
			beacons.clear();

			scheduleTaskExecutor= Executors.newScheduledThreadPool(5);

			// This schedules an iBeacon to appear every 10 seconds:
			scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {
				public void run() {
					try{
						//putting a single iBeacon back into the iBeacons list.
						if (finalIBeacons.size() > beacons.size())
							beacons.add(finalIBeacons.get(beacons.size()));
						else 
							scheduleTaskExecutor.shutdown();
						
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}, 0, 10, TimeUnit.SECONDS);
		} 
	}

}