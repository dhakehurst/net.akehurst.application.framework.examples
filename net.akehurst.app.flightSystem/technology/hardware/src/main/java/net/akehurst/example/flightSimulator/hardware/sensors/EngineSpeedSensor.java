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
package net.akehurst.example.flightSimulator.hardware.sensors;

import java.nio.ByteBuffer;

import net.akehurst.example.flightSimulator.hardware.IHardwareRegister;
import net.akehurst.example.flightSimulator.simulation.EnvironmentReference;


public class EngineSpeedSensor implements Runnable, IHardwareRegister {

	public EngineSpeedSensor(double sensingRatePerSecond) {
		this.sensingRatePerSecond = sensingRatePerSecond;
		this.environment = new EnvironmentReference();
		this.sensorReading = new byte[8];
	}
	
	double sensingRatePerSecond;
	byte[] sensorReading;
	
	EnvironmentReference environment;
	
	@Override
	public byte[] readValue() {
		return this.sensorReading;
	}
	
	@Override
	public void writeValue(byte[] bytes) {
	}
	
	void simulateSampleEngineSpeed() {
		double d = this.environment.getEngineSpeed();
		ByteBuffer bb = ByteBuffer.allocate(Double.BYTES);
		bb.putDouble(d);
		this.sensorReading = bb.array();	
	}
	
	// ---------------- Runnable --------------------
	public void run() {
		try {
			while(true) {
				this.simulateSampleEngineSpeed();
				Thread.sleep((long)(1000.0 / this.sensingRatePerSecond));
			}
		} catch (InterruptedException e) {
			//ok thread interrupted
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	Thread thread;

	public void start() {
		this.thread = new Thread(this, "AirPressureSensor");
		this.thread.start();
	}
	
	public void join() throws InterruptedException {
		this.thread.join();
	}


	
}
