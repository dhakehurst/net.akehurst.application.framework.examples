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

import net.akehurst.application.framework.realisation.AbstractIdentifiableObject;
import net.akehurst.example.flightSimulator.computational.pilotInterface.AirSpeed;
import net.akehurst.example.flightSimulator.computational.pilotInterface.Elevation;
import net.akehurst.example.flightSimulator.computational.pilotInterface.GroundSpeed;
import net.akehurst.example.flightSimulator.computational.pilotInterface.Heading;
import net.akehurst.example.flightSimulator.computational.pilotInterface.HeightAboveSeaLevel;
import net.akehurst.example.flightSimulator.computational.pilotInterface.IPilotNotification;
import net.akehurst.example.flightSimulator.computational.pilotInterface.Roll;
import net.akehurst.example.flightSimulator.computational.pilotInterface.Yaw;
import net.akehurst.example.flightSimulator.engineering.commsChannel.Serialiser;

public class PilotNotificationPublisher extends AbstractIdentifiableObject implements IPilotNotification {

	public PilotNotificationPublisher(final String afId) {
		super(afId);
		this.serialiser = new Serialiser();
	}

	IPublishSubscribe network;

	public IPublishSubscribe getNetwork() {
		return this.network;
	}

	public void setNetwork(final IPublishSubscribe value) {
		this.network = value;
	}

	Serialiser serialiser;

	// --------- IPilotNotification ----------
	@Override
	public void notifyCurrentHeightAboveSeaLevel(final HeightAboveSeaLevel value) {
		final byte[] bytes = this.serialiser.serialiseDouble(value.getClass(), value.asPrimitive());
		this.getNetwork().publish(bytes);
	}

	@Override
	public void notifyCurrentElevation(final Elevation value) {
		final byte[] bytes = this.serialiser.serialiseDouble(value.getClass(), value.asPrimitive());
		this.getNetwork().publish(bytes);
	}

	@Override
	public void notifyCurrentRoll(final Roll value) {
		final byte[] bytes = this.serialiser.serialiseDouble(value.getClass(), value.asPrimitive());
		this.getNetwork().publish(bytes);
	}

	@Override
	public void notifyCurrentYaw(final Yaw value) {
		final byte[] bytes = this.serialiser.serialiseDouble(value.getClass(), value.asPrimitive());
		this.getNetwork().publish(bytes);
	}

	@Override
	public void notifyCurrentAirSpeed(final AirSpeed value) {
		final byte[] bytes = this.serialiser.serialiseDouble(value.getClass(), value.asPrimitive());
		this.getNetwork().publish(bytes);
	}

	@Override
	public void notifyCurrentGroundSpeed(final GroundSpeed value) {
		final byte[] bytes = this.serialiser.serialiseDouble(value.getClass(), value.asPrimitive());
		this.getNetwork().publish(bytes);
	}

	@Override
	public void notifyCurrentHeading(final Heading value) {
		final byte[] bytes = this.serialiser.serialiseDouble(value.getClass(), value.asPrimitive());
		this.getNetwork().publish(bytes);
	}

}
