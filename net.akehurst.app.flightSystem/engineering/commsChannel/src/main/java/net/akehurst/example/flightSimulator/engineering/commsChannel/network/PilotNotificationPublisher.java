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
import net.akehurst.example.flightSimulator.computational.pilotInterface.GroundSpeed;
import net.akehurst.example.flightSimulator.computational.pilotInterface.Heading;
import net.akehurst.example.flightSimulator.computational.pilotInterface.HeightAboveSeaLevel;
import net.akehurst.example.flightSimulator.computational.pilotInterface.IPilotNotification;
import net.akehurst.example.flightSimulator.computational.pilotInterface.Roll;
import net.akehurst.example.flightSimulator.computational.pilotInterface.Yaw;
import net.akehurst.example.flightSimulator.engineering.commsChannel.Serialiser;
import net.akehurst.example.flightSimulator.technology.network.IPublishSubscribe;

public class PilotNotificationPublisher implements IPilotNotification {

	public PilotNotificationPublisher() {
		this.serialiser = new Serialiser();
	}
	
	IPublishSubscribe network;
	public IPublishSubscribe getNetwork() {
		return this.network;
	}
	public void setNetwork(IPublishSubscribe value) {
		this.network = value;
	}
	
	Serialiser serialiser;
	
	//--------- IPilotNotification ----------
	@Override
	public void notifyCurrentHeightAboveSeaLevel(HeightAboveSeaLevel value) {
		byte[] bytes = this.serialiser.serialiseDouble(value.getClass(), value.asPrimitive());
		this.getNetwork().publish(bytes);
	}
	@Override
	public void notifyCurrentElevation(Elevation value) {
		byte[] bytes = this.serialiser.serialiseDouble(value.getClass(), value.asPrimitive());
		this.getNetwork().publish(bytes);
	}
	@Override
	public void notifyCurrentRoll(Roll value) {
		byte[] bytes = this.serialiser.serialiseDouble(value.getClass(), value.asPrimitive());
		this.getNetwork().publish(bytes);
	}
	@Override
	public void notifyCurrentYaw(Yaw value) {
		byte[] bytes = this.serialiser.serialiseDouble(value.getClass(), value.asPrimitive());
		this.getNetwork().publish(bytes);
	}
	@Override
	public void notifyCurrentAirSpeed(AirSpeed value) {
		byte[] bytes = this.serialiser.serialiseDouble(value.getClass(), value.asPrimitive());
		this.getNetwork().publish(bytes);
	}
	@Override
	public void notifyCurrentGroundSpeed(GroundSpeed value) {
		byte[] bytes = this.serialiser.serialiseDouble(value.getClass(), value.asPrimitive());
		this.getNetwork().publish(bytes);
	}
	@Override
	public void notifyCurrentHeading(Heading value) {
		byte[] bytes = this.serialiser.serialiseDouble(value.getClass(), value.asPrimitive());
		this.getNetwork().publish(bytes);
	}
		
}
