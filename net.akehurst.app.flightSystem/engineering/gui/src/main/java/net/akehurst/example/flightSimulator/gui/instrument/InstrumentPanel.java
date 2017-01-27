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
package net.akehurst.example.flightSimulator.gui.instrument;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.akehurst.example.flightSimulator.computational.engineInterface.EngineSpeed;
import net.akehurst.example.flightSimulator.computational.engineInterface.IEngineNotification;
import net.akehurst.example.flightSimulator.computational.pilotInterface.AirSpeed;
import net.akehurst.example.flightSimulator.computational.pilotInterface.Elevation;
import net.akehurst.example.flightSimulator.computational.pilotInterface.GroundSpeed;
import net.akehurst.example.flightSimulator.computational.pilotInterface.Heading;
import net.akehurst.example.flightSimulator.computational.pilotInterface.HeightAboveSeaLevel;
import net.akehurst.example.flightSimulator.computational.pilotInterface.IPilotNotification;
import net.akehurst.example.flightSimulator.computational.pilotInterface.Roll;
import net.akehurst.example.flightSimulator.computational.pilotInterface.Yaw;
import net.akehurst.example.maths.AngleDegrees;

public class InstrumentPanel implements IPilotNotification, IEngineNotification, Runnable {

	public InstrumentPanel() {
	}

	GuiInstrumentPanel gui;
	Stage primary;

	void createWindow() {
		new JFXPanel();
		Platform.runLater(new Runnable() {
			public void run() {
				try {
					primary = new Stage();

					InstrumentPanel.this.gui = new GuiInstrumentPanel();
					Scene scene = new Scene(InstrumentPanel.this.gui);

					primary.setTitle("Controls");
					primary.setScene(scene);
					primary.sizeToScene();

					primary.show();

				} catch (Exception e) {
					e.printStackTrace();
				}
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

	// ---------------- IPilotNotification -----------
	public void notifyCurrentHeightAboveSeaLevel(HeightAboveSeaLevel value) {
		try {
			Platform.runLater(new Runnable() {
				public void run() {
					try {
						InstrumentPanel.this.gui.altimeter.setValue(value.asPrimitive());
					} catch (NullPointerException e) {
						//may be thrown if gui not started yet
					}
				}
			});
		} catch (IllegalStateException e) {
			// gui not started yet
		}
	}

	public void notifyCurrentElevation(Elevation value) {
		try {
			Platform.runLater(new Runnable() {
				public void run() {
					try {
						AngleDegrees deg = value.asDegrees();
						double d = deg.asPrimitive();
						InstrumentPanel.this.gui.horizon.setPitch(d);
						boolean upsidedown = (d > 90 && d < 270);
					} catch (NullPointerException e) {
						//may be thrown if gui not started yet
					}
				}
			});
		} catch (IllegalStateException e) {
			// gui not started yet
		}
	}

	public void notifyCurrentRoll(Roll value) {
		try {
			Platform.runLater(new Runnable() {
				public void run() {
					try {
						AngleDegrees deg = value.asDegrees();
						double d = deg.asPrimitive();
						InstrumentPanel.this.gui.horizon.setRoll(360 - d);
					} catch (NullPointerException e) {
						//may be thrown if gui not started yet
					}
				}
			});
		} catch (IllegalStateException e) {
			// gui not started yet
		}
	}

	public void notifyCurrentYaw(Yaw value) {

	}

	public void notifyCurrentAirSpeed(AirSpeed value) {
		try {
			Platform.runLater(new Runnable() {
				public void run() {
					try {
						InstrumentPanel.this.gui.airSpeed.setValue(value.asPrimitive());
					} catch (NullPointerException e) {
						//may be thrown if gui not started yet
					}
				}
			});
		} catch (IllegalStateException e) {
			// gui not started yet
		}
	}

	@Override
	public void notifyCurrentHeading(Heading value) {
		try {
			Platform.runLater(new Runnable() {
				public void run() {
					try {
						InstrumentPanel.this.gui.airCompass.setBearing(value.asDegrees().asPrimitive());
					} catch (NullPointerException e) {
						//may be thrown if gui not started yet
					}
				}
			});
		} catch (IllegalStateException e) {
			// gui not started yet
		}
	}

	public void notifyCurrentGroundSpeed(GroundSpeed value) {
	}

	// --------- IEngineNotification ---------
	@Override
	public void notifyEnginePower(EngineSpeed value) {
		try {
			Platform.runLater(new Runnable() {
				public void run() {
					try {
						InstrumentPanel.this.gui.engineSpeed.setValue(value.asPrimitive());
					} catch (NullPointerException e) {
						//may be thrown if gui not started yet
					}
				}
			});
		} catch (IllegalStateException e) {
			// gui not started yet
		}
	}

}
