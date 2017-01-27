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

import net.akehurst.example.flightSimulator.computational.sensorsInterface.AirPressureSensorSample;
import net.akehurst.example.flightSimulator.computational.sensorsInterface.IAirPressureSensor;
import net.akehurst.example.flightSimulator.hardware.IHardwareRegister;

public class AirPressureSensorProxy implements IAirPressureSensor {

	public AirPressureSensorProxy() {
	}
	
	IHardwareRegister hwRegister;
	public IHardwareRegister getHwRegister() {
		return hwRegister;
	}
	public void setHwRegister(IHardwareRegister hwRegister) {
		this.hwRegister = hwRegister;
	}
	
	
	// ---------- IAirPressureSensor ----------
	@Override
	public AirPressureSensorSample readValue() {

		// read the hardware register
		byte[] bytes = this.getHwRegister().readValue();
		
		//convert the read value into a double
		ByteBuffer bb = ByteBuffer.wrap(bytes);
		double hwValue = bb.getDouble();
		
		//convert the value into an air speed sample
		double value = hwValue; //this needs a calculation.
		
		AirPressureSensorSample sample = new AirPressureSensorSample(value);
		return sample;
	}
	
	
}
