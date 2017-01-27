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
package net.akehurst.example.flightSimulator.engineering.commsChannel.hardware;

import java.nio.ByteBuffer;

import net.akehurst.example.flightSimulator.computational.flightSurfaceInterface.AileronPosition;
import net.akehurst.example.flightSimulator.computational.flightSurfaceInterface.ElevatorPosition;
import net.akehurst.example.flightSimulator.computational.flightSurfaceInterface.RudderPosition;
import net.akehurst.example.flightSimulator.engineering.commsChannel.Serialiser;
import net.akehurst.example.flightSimulator.hardware.IHardwareRegister;
import net.akehurst.example.flightSimulator.technology.network.IPublishSubscribe;
import net.akehurst.example.flightSimulator.technology.network.ISubscriber;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FlightSurfaceProxy implements ISubscriber, Runnable {

	Logger log;
	
	public FlightSurfaceProxy() {
		this.log = LoggerFactory.getLogger(this.getClass());
		this.serialiser = new Serialiser();
	}
	Serialiser serialiser; 
	
	IPublishSubscribe network;
	public IPublishSubscribe getNetwork() {
		return this.network;
	}
	public void setNetwork(IPublishSubscribe value) {
		this.network = value;
	}
	
	IHardwareRegister aileronActuator;
	public IHardwareRegister getAileronActuator() {
		return aileronActuator;
	}
	public void setAileronActuator(IHardwareRegister value) {
		this.aileronActuator = value;
	}
	
	IHardwareRegister elevatorActuator;
	public IHardwareRegister getElevatorActuator() {
		return elevatorActuator;
	}
	public void setElevatorActuator(IHardwareRegister value) {
		this.elevatorActuator = value;
	}
	
	IHardwareRegister rudderActuator;
	public IHardwareRegister getRudderActuator() {
		return rudderActuator;
	}
	public void setRudderActuator(IHardwareRegister value) {
		this.rudderActuator = value;
	}
	
	// --------- ISubscriber ---------
	@Override
	public void update(byte[] bytes) {
		try {
		Object data = this.serialiser.unserialise(bytes);
		
		if (data instanceof AileronPosition) {
			AileronPosition value = (AileronPosition)data;
			ByteBuffer bb = ByteBuffer.allocate(Double.BYTES);
			bb.putDouble(value.asPrimitive());
			this.getAileronActuator().writeValue(bb.array());
		} else if (data instanceof ElevatorPosition) {
			ElevatorPosition value = (ElevatorPosition)data;
			ByteBuffer bb = ByteBuffer.allocate(Double.BYTES);
			bb.putDouble(value.asPrimitive());
			this.getElevatorActuator().writeValue(bb.array());
		} else if (data instanceof RudderPosition) {
			RudderPosition value = (RudderPosition)data;
			ByteBuffer bb = ByteBuffer.allocate(Double.BYTES);
			bb.putDouble(value.asPrimitive());
			this.getRudderActuator().writeValue(bb.array());
		} else {
			log.trace("Do not know what to do with data: "+data);
		}
		} catch (Exception e) {
			log.error("",e);
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
