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
package net.akehurst.example.flightSimulator.simulation;



public class EnvironmentReference implements IEnvironment {

	public static IEnvironment actual;

	@Override
	public Velocity getAirVelocity() {
		return actual.getAirVelocity();
	}

	@Override
	public Velocity getVelocityOverGround() {
		return actual.getVelocityOverGround();
	}

	public IDirection getAttitude() {
		return actual.getAttitude();
	}

	public Altitude getAltitude() {
		return actual.getAltitude();
	}
	
	public AirPressure getAirPressure() {
		return actual.getAirPressure();
	}

	@Override
	public void setBankRate(BankRate value) {
		actual.setBankRate(value);
	}

	@Override
	public void setPitchRate(PitchRate value) {
		actual.setPitchRate(value);
	}

	public void setAirSpeed(Speed value) {
		actual.setAirSpeed(value);
	}

	public double getEngineSpeed() {
		return actual.getEngineSpeed();
	}
	
	public void setEngineThrottlePosition(double value) {
		actual.setEngineThrottlePosition(value);
	}
	
}
