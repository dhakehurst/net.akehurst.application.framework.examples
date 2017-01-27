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
import net.akehurst.example.flightSimulator.simulation.Velocity;


public class AirSpeedSensor implements Runnable, IHardwareRegister {

	public AirSpeedSensor(double sensingRatePerSecond) {
		this.sensingRatePerSecond = sensingRatePerSecond;
		this.environment = new EnvironmentReference();
		this.sensorReading = new byte[8];
	}
	
	double sensingRatePerSecond;
	byte[] sensorReading;
	
	EnvironmentReference environment;
	
	/**
	 * returns an array of 8 bytes that encode a double value
	 * the value is in the range 1.0 to 5.0 volts.
	 * this represents an air speed 0-300 knots.
	 */
	@Override
	public byte[] readValue() {
		return this.sensorReading;
	}

	@Override
	public void writeValue(byte[] bytes) {
	}
	
	static double MAX_SPEED = 500;
	static double VOLTS_MIN = 1.0;
	static double VOLTS_MAX = 5.0;
	static double VOLTS_RANGE = VOLTS_MAX - VOLTS_MIN;
	
	void updateSimulatedAirSpeed() {
		Velocity airSpeed = this.environment.getAirVelocity();
		Velocity speedOverGround = this.environment.getVelocityOverGround();
		double speed = airSpeed.plus(speedOverGround).getMagnitude().asPrimitive();
		double volts = (speed / MAX_SPEED) * VOLTS_RANGE + VOLTS_MIN;
		ByteBuffer bb =  ByteBuffer.allocate(Double.BYTES);
		bb.putDouble(volts);
		this.sensorReading = bb.array();	
	}
	
	// ---------------- Runnable --------------------
	public void run() {
		try {
			while(true) {
				this.updateSimulatedAirSpeed();
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
		this.thread = new Thread(this, this.getClass().getSimpleName());
		this.thread.start();
	}
	
	public void join() throws InterruptedException {
		this.thread.join();
	}

	
}
