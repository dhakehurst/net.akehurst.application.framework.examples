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

import net.akehurst.example.flightSimulator.computational.engineInterface.EngineSpeed;
import net.akehurst.example.flightSimulator.computational.pilotInterface.AirSpeed;
import net.akehurst.example.maths.AngleDegrees;
import net.akehurst.example.maths.AngleRadians;


public class EnvironmentSimulation implements IEnvironment, Runnable {

	public EnvironmentSimulation(double simulationRatePerSecond) {
		this.simulationRatePerSecond = simulationRatePerSecond;
		
		this.airVelocity = new Velocity(new Speed(0), new Direction(new AngleDegrees(0.0), new AngleDegrees(0.0), new AngleDegrees(0.0)));
		this.velocityOverGround = new Velocity(new Speed(0), new Direction(new AngleDegrees(0.0), new AngleDegrees(0.0), new AngleDegrees(0.0)));
		this.attitude = new Direction(new AngleDegrees(0.0), new AngleDegrees(0.0), new AngleDegrees(0.0));
		this.altitude = new Altitude(0.0);
		this.airPressure = new AirPressure(1015.25);
		this.bankRate = new BankRate(0);
		this.pitchRate = new PitchRate(0);
	}
	
	double simulationRatePerSecond;
	
	Velocity airVelocity;
	
	@Override
	public Velocity getAirVelocity() {
		return this.airVelocity;
	}

	public Speed getAirSpeed() {
		return this.airVelocity.getMagnitude();
	}
	@Override
	public void setAirSpeed(Speed value) {
		this.airVelocity =  new Velocity(value, this.airVelocity.getDirection());
	}
	
	Velocity velocityOverGround;
	@Override
	public Velocity getVelocityOverGround() {
		return this.velocityOverGround;
	}

	IDirection attitude;
	@Override
	public IDirection getAttitude() {
		return this.attitude;
	}

	Altitude altitude;
	@Override
	public Altitude getAltitude() {
		return this.altitude;
	}
	
	AirPressure airPressure;
	@Override
	public AirPressure getAirPressure() {
		return this.airPressure;
	}

	BankRate bankRate;
	@Override
	public void setBankRate(BankRate value) {
		this.bankRate = value;
	}
	
	PitchRate pitchRate;
	@Override
	public void setPitchRate(PitchRate value) {
		this.pitchRate = value;
	}
	
	double engineSpeed;
	@Override
	public double getEngineSpeed() {
		return this.engineSpeed;
	}
	
	double engineThrottlePosition;
	@Override
	public void setEngineThrottlePosition(double value) {
		this.engineThrottlePosition = value;
		// TODO: Engine power currently simulated as direct correlation to throttle position
		// a better calculation would be nice
		this.engineSpeed = value * 100;
		//TODO: be nice to have a proper relationship between engine speed and air speed
		// airSpeed = airSpeed.Max / engineSpeed.Max * engineSpeed
		double asp = (AirSpeed.MAX / EngineSpeed.MAX) * this.engineSpeed;
		this.setAirSpeed( new Speed(asp) );
	}
	
	void updateSimulation() {
		double br = bankRate.asPrimitive();
		double newTor = this.attitude.getTortion().asRadians().asPrimitive() + br;
		double azInc = newTor / 100;
		AngleRadians newAz = new AngleRadians(this.attitude.getAzimuth().asRadians().asPrimitive() + azInc );
		AngleRadians newEl = new AngleRadians(this.attitude.getElevation().asRadians().asPrimitive() + pitchRate.asPrimitive());
		AngleRadians newTr = new AngleRadians(newTor);
		IDirection newAttitude = new Direction(newAz, newEl, newTr);
		this.attitude = newAttitude;
		
		//to simulate altitude we make a simple function of airSpeed
		if (this.getAirSpeed().asPrimitive() > 200) {
			double climbRate = this.getAirSpeed().asPrimitive();
			double alt = this.getAltitude().asPrimitive();
			alt += (climbRate/100);
			this.altitude = new Altitude(alt);
		} else if (this.getAirSpeed().asPrimitive() < 100) {
			double asp = this.getAirSpeed().asPrimitive();
			double climbRate = - (10 - asp/10);
			double alt = this.getAltitude().asPrimitive();
			alt = Math.max(0,alt+climbRate);
			this.altitude = new Altitude(alt);
		}
		
	}
	
	// ---------------- Runnable --------------------
	public void run() {
		try {
			while(true) {
				this.updateSimulation();
				Thread.sleep((long)(1000.0 / this.simulationRatePerSecond));
			}
		} catch (InterruptedException e) {
			//ok thread interrupted
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	Thread thread;

	public void start() {
		this.thread = new Thread(this, "EnvironmentSimulation");
		this.thread.start();
	}
	
	public void join() throws InterruptedException {
		this.thread.join();
	}

}
