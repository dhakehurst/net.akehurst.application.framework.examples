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
package net.akehurst.example.flightSimulator.gui.controls;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.akehurst.example.flightSimulator.computational.pilotInterface.ElevationRate;
import net.akehurst.example.flightSimulator.computational.pilotInterface.EngineThrust;
import net.akehurst.example.flightSimulator.computational.pilotInterface.IPilotRequest;
import net.akehurst.example.flightSimulator.computational.pilotInterface.RollRate;
import net.akehurst.example.flightSimulator.computational.pilotInterface.YawRate;

public class PilotControls implements Runnable {

	public PilotControls() {
	}

	IPilotRequest request;

	public IPilotRequest getRequest() {
		return request;
	}

	public void setRequest(IPilotRequest request) {
		this.request = request;
	}

	GuiControlsPanel gui;
	Stage primary;

	void createWindow() {
		new JFXPanel();
		Platform.runLater(new Runnable() {
			public void run() {
				try {
					primary = new Stage();

					PilotControls.this.gui = new GuiControlsPanel();
					Scene scene = new Scene(PilotControls.this.gui);

					primary.setTitle("Controls");
					primary.setScene(scene);
					primary.sizeToScene();

					PilotControls.this.registerListeners();

					primary.show();

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	void registerListeners() {
		this.gui.thrust.knobYPercentProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				PilotControls.this.requestEngineThrust(100 - newValue.doubleValue());
			}
		});
		this.gui.joystick.knobXPercentProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				PilotControls.this.requestRollRate(newValue.doubleValue());
			}
		});
		this.gui.joystick.knobYPercentProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				PilotControls.this.requestElevationRate(newValue.doubleValue());
			}
		});
	}

	// ---------------- Runnable --------------------
	public void run() {
		this.createWindow();
	}

	Thread thread;

	public void start() {
		this.thread = new Thread(this, this.getClass().getSimpleName());
		this.thread.start();
	}

	public void join() throws InterruptedException {
		this.thread.join();
	}

	// ---------------- IPilotRequest -----------
	public void requestElevationRate(double value) {
		if (null != this.getRequest()) {
			ElevationRate rate = new ElevationRate(value);
			this.getRequest().requestElevationRate(rate);
		}
	}

	public void requestRollRate(double value) {
		if (null != this.getRequest()) {
			RollRate rate = new RollRate(value);
			this.getRequest().requestRollRate(rate);
		}
	}

	public void requestYawRate(double value) {
		if (null != this.getRequest()) {
			YawRate rate = new YawRate(value);
			this.getRequest().requestYawRate(rate);
		}
	}

	public void requestEngineThrust(double value) {
		if (null != this.getRequest()) {
			EngineThrust thrust = new EngineThrust(value);
			this.getRequest().requestEngineThrust(thrust);
		}
	}
}
