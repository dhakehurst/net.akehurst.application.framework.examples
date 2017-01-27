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

public class PilotRequestPublisher implements IPilotRequest {

	public PilotRequestPublisher() {
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

	// ---------- IPilotRequest ----------
	@Override
	public void requestElevationRate(ElevationRate value) {
		byte[] bytes = this.serialiser.serialiseDouble(value.getClass(), value.asPrimitive());
		this.getNetwork().publish(bytes);
	}

	@Override
	public void requestRollRate(RollRate value) {
		byte[] bytes = this.serialiser.serialiseDouble(value.getClass(), value.asPrimitive());
		this.getNetwork().publish(bytes);
	}

	@Override
	public void requestYawRate(YawRate value) {
		byte[] bytes = this.serialiser.serialiseDouble(value.getClass(), value.asPrimitive());
		this.getNetwork().publish(bytes);
	}

	@Override
	public void requestEngineThrust(EngineThrust value) {
		byte[] bytes = this.serialiser.serialiseDouble(value.getClass(), value.asPrimitive());
		this.getNetwork().publish(bytes);
	}

}
