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

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;
import eu.hansolo.airseries.AirCompass;
import eu.hansolo.airseries.Altimeter;
import eu.hansolo.airseries.Horizon;
import eu.hansolo.enzo.gauge.Gauge;

public class GuiInstrumentPanel extends GridPane {

	@FXML Gauge airSpeed;
	@FXML AirCompass airCompass;
	@FXML Altimeter altimeter;
	@FXML Horizon horizon;
	@FXML Gauge engineSpeed;
	
	public GuiInstrumentPanel() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("GuiInstrumentPanel.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);

		try {
			fxmlLoader.load();
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
	}

}
