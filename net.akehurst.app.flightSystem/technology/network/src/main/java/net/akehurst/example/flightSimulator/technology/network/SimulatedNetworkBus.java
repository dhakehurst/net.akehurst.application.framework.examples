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
package net.akehurst.example.flightSimulator.technology.network;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class SimulatedNetworkBus implements IPublishSubscribe, Runnable {

	public SimulatedNetworkBus() {
		this.subscribers = new ArrayList<>();
		this.queue = new LinkedBlockingQueue<>();
	}

	List<ISubscriber> subscribers;
	BlockingQueue<byte[]> queue;

	@Override
	public void publish(byte[] bytes) {
		this.queue.add(bytes);
	}

	@Override
	public void subscribe(ISubscriber subscriber) {
		this.subscribers.add(subscriber);
	}

	// ---------------- Runnable --------------------
	@Override
	public void run() {
		try {
			while (true) {
				byte[] packet = this.queue.take();
				for (ISubscriber sub : this.subscribers) {
					sub.update(packet);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	Thread thread;

	public void start() {
		this.thread = new Thread(this, "SimulatedNetworkBus");
		this.thread.setPriority(Thread.MAX_PRIORITY);
		this.thread.start();
	}

	public void join() throws InterruptedException {
		this.thread.join();
	}

}
