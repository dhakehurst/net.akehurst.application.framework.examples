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

import net.akehurst.application.framework.common.IPort;
import net.akehurst.application.framework.common.annotations.instance.ConfiguredValue;
import net.akehurst.application.framework.common.annotations.instance.PortContract;
import net.akehurst.application.framework.common.annotations.instance.PortInstance;
import net.akehurst.application.framework.technology.interfaceComms.ChannelIdentity;
import net.akehurst.application.framework.technology.interfaceComms.IPublishSubscribeNotification;
import net.akehurst.application.framework.technology.interfaceComms.IPublishSubscribeRequest;
import net.akehurst.application.framework.technology.interfaceComms.PublishSubscribeException;
import net.akehurst.example.flightSimulator.computational.pilotInterface.ElevationRate;
import net.akehurst.example.flightSimulator.computational.pilotInterface.EngineThrust;
import net.akehurst.example.flightSimulator.computational.pilotInterface.IPilotRequest;
import net.akehurst.example.flightSimulator.computational.pilotInterface.RollRate;
import net.akehurst.example.flightSimulator.computational.pilotInterface.YawRate;
import net.akehurst.example.flightSimulator.engineering.commsChannel.Serialiser;

public class PilotRequestPublisher implements IPilotRequest {

	public PilotRequestPublisher() {
		this.serialiser = new Serialiser();
	}

	@ConfiguredValue(defaultValue = "channelPilotRequest")
	ChannelIdentity channelPilotRequest;

	Serialiser serialiser;

	// ---------- IPilotRequest ----------
	@Override
	public void requestElevationRate(final ElevationRate value) {
		final byte[] data = this.serialiser.serialiseDouble(value.getClass(), value.asPrimitive());
		try {
			this.portComms().out(IPublishSubscribeRequest.class).requestPublish(this.channelPilotRequest, data);
		} catch (final PublishSubscribeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void requestRollRate(final RollRate value) {
		final byte[] bytes = this.serialiser.serialiseDouble(value.getClass(), value.asPrimitive());
		this.getNetwork().publish(bytes);
	}

	@Override
	public void requestYawRate(final YawRate value) {
		final byte[] bytes = this.serialiser.serialiseDouble(value.getClass(), value.asPrimitive());
		this.getNetwork().publish(bytes);
	}

	@Override
	public void requestEngineThrust(final EngineThrust value) {
		final byte[] bytes = this.serialiser.serialiseDouble(value.getClass(), value.asPrimitive());
		this.getNetwork().publish(bytes);
	}

	// --- Ports ---
	@PortInstance
	@PortContract(provides = IPublishSubscribeNotification.class, requires = IPublishSubscribeRequest.class)
	private IPort portComms;

	public IPort portComms() {
		return this.portComms;
	}
}
