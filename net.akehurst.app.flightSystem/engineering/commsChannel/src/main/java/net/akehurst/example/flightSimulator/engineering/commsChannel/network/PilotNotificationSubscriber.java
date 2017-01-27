/*
 * Copyright (c) 2015 D. David H. Akehurst
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.akehurst.example.flightSimulator.engineering.commsChannel.network;

import net.akehurst.example.flightSimulator.computational.pilotInterface.AirSpeed;
import net.akehurst.example.flightSimulator.computational.pilotInterface.Elevation;
import net.akehurst.example.flightSimulator.computational.pilotInterface.Heading;
import net.akehurst.example.flightSimulator.computational.pilotInterface.HeightAboveSeaLevel;
import net.akehurst.example.flightSimulator.computational.pilotInterface.IPilotNotification;
import net.akehurst.example.flightSimulator.computational.pilotInterface.Roll;
import net.akehurst.example.flightSimulator.computational.pilotInterface.Yaw;
import net.akehurst.example.flightSimulator.engineering.commsChannel.Serialiser;
import net.akehurst.example.flightSimulator.technology.network.IPublishSubscribe;
import net.akehurst.example.flightSimulator.technology.network.ISubscriber;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PilotNotificationSubscriber implements ISubscriber, Runnable {

	Logger log;
	
	public PilotNotificationSubscriber() {
		this.log = LoggerFactory.getLogger(this.getClass());
		this.serialiser = new Serialiser();
	}
	
	Serialiser serialiser; 
	
	IPilotNotification pilotNotification;
	public IPilotNotification getPilotNotification() {
		return pilotNotification;
	}
	public void setPilotNotification(IPilotNotification value) {
		this.pilotNotification = value;
	}

	IPublishSubscribe network;
	public IPublishSubscribe getNetwork() {
		return this.network;
	}
	public void setNetwork(IPublishSubscribe value) {
		this.network = value;
	}
	

	//--------- ISubscriber ---------
	@Override
	public void update(byte[] bytes) {
		Object data = this.serialiser.unserialise(bytes);
		
		if (data instanceof AirSpeed) {
			AirSpeed value = (AirSpeed)data;
			this.getPilotNotification().notifyCurrentAirSpeed(value);
		} else if (data instanceof Elevation) {
			Elevation value = (Elevation)data;
			this.getPilotNotification().notifyCurrentElevation(value);
		} else if (data instanceof Roll) {
			Roll value = (Roll)data;
			this.getPilotNotification().notifyCurrentRoll(value);
		} else if (data instanceof Yaw) {
			Yaw value = (Yaw)data;
			this.getPilotNotification().notifyCurrentYaw(value);
		} else if (data instanceof Heading) {
			Heading value = (Heading)data;
			this.getPilotNotification().notifyCurrentHeading(value);
		} else if (data instanceof HeightAboveSeaLevel) {
			HeightAboveSeaLevel value = (HeightAboveSeaLevel)data;
			this.getPilotNotification().notifyCurrentHeightAboveSeaLevel(value);
		} else {
			// data not relevant to this subscriber
			log.trace("Do not know what to do with data: "+data);
		}
		
	}

	// ---------------- Runnable --------------------
	public void run() {
		this.getNetwork().subscribe(this);
	}

	Thread thread;

	public void start() {
		this.thread = new Thread(this, this.getClass().getSimpleName());
		this.thread.start();
	}
	
	public void join() throws InterruptedException {
		this.thread.join();
	}

}
