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
package net.akehurst.example.flightSimulator.hardware.actuators;

import java.nio.ByteBuffer;

import net.akehurst.example.flightSimulator.hardware.IHardwareRegister;
import net.akehurst.example.flightSimulator.simulation.BankRate;
import net.akehurst.example.flightSimulator.simulation.EnvironmentReference;
import net.akehurst.example.maths.AngleDegrees;

public class AileronActuator implements IHardwareRegister {

	final double MAX_BANK_RATE = (new AngleDegrees(10.0)).asRadians().asPrimitive();
	
	public AileronActuator() {
		this.currentValue = new byte[Double.BYTES];
		this.environment = new EnvironmentReference();
	}
	
	EnvironmentReference environment;
	
	byte[] currentValue;
	
	@Override
	public byte[] readValue() {
		return this.currentValue;
	}

	@Override
	public void writeValue(byte[] bytes) {
		this.currentValue = bytes;
		this.updateSimulation(bytes);
	}

	void updateSimulation(byte[] bytes) {
		ByteBuffer bb = ByteBuffer.wrap(bytes);
		double v = bb.getDouble();
		double r = MAX_BANK_RATE * (v/100.0);
		this.environment.setBankRate(new BankRate(r));
	}

}
