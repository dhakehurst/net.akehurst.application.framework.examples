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

import net.akehurst.example.flightSimulator.computational.pilotInterface.ElevationRate;
import net.akehurst.example.flightSimulator.computational.pilotInterface.EngineThrust;
import net.akehurst.example.flightSimulator.computational.pilotInterface.IPilotRequest;
import net.akehurst.example.flightSimulator.computational.pilotInterface.RollRate;
import net.akehurst.example.flightSimulator.computational.pilotInterface.YawRate;
import net.akehurst.example.flightSimulator.engineering.commsChannel.Serialiser;
import net.akehurst.example.flightSimulator.technology.network.IPublishSubscribe;
import net.akehurst.example.flightSimulator.technology.network.ISubscriber;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PilotRequestSubscriber implements ISubscriber, Runnable {

	Logger log;
	
	public PilotRequestSubscriber() {
		this.log = LoggerFactory.getLogger(this.getClass());
		this.serialiser = new Serialiser();
	}
	
	Serialiser serialiser; 
	
	IPilotRequest pilotRequest;
	public IPilotRequest getPilotRequest() {
		return pilotRequest;
	}
	public void setPilotRequest(IPilotRequest pilotRequest) {
		this.pilotRequest = pilotRequest;
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
		
		if (data instanceof EngineThrust) {
			EngineThrust thrust = (EngineThrust)data;
			this.getPilotRequest().requestEngineThrust(thrust);
		} else if (data instanceof ElevationRate) {
			ElevationRate value = (ElevationRate)data;
			this.getPilotRequest().requestElevationRate(value);
		} else if (data instanceof RollRate) {
			RollRate value = (RollRate)data;
			this.getPilotRequest().requestRollRate(value);
		} else if (data instanceof YawRate) {
			YawRate value = (YawRate)data;
			this.getPilotRequest().requestYawRate(value);
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
		this.thread = new Thread(this, "PilotRequestSubscriber");
		this.thread.start();
	}
	
	public void join() throws InterruptedException {
		this.thread.join();
	}

}
