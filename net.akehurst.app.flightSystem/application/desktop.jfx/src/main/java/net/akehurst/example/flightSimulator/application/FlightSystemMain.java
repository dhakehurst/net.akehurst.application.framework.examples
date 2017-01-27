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
package net.akehurst.example.flightSimulator.application;

import java.net.URL;

import net.akehurst.application.framework.realisation.ApplicationFramework;
import net.akehurst.example.flightSimulator.gui.controls.GuiControlsPanel;


public class FlightSystemMain {

	public static void main(String[] args) {
		
		//URL url = GuiControlsPanel.class.getResource("GuiControlsPanel.fxml");
		
		
		ApplicationFramework.start(FlightSystemApplication.class, args);
		
		
//		try {
//			EnvironmentSimulation envSim = new EnvironmentSimulation(5);
//			EnvironmentReference.actual = envSim;
//			
//			FlightSystemApplication main = new FlightSystemApplication(args);
//			envSim.start();
//			main.start();
//			main.join();
//		} catch (Throwable t) {
//			t.printStackTrace();
//		}
	}

}
