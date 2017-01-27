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
package net.akehurst.example.flightSimulator.computational.engineInterface;

import net.akehurst.application.framework.common.AbstractDataType;
import net.akehurst.application.framework.common.annotations.declaration.DataType;

@DataType
public class EngineSpeed extends AbstractDataType {

	public static final double MIN = 0;
	public static final double MAX = 10000;
	
	/**
	 * Engine Speed int RPM
	 * will clip value to the range 0 <= value <= 10000
	 * 
	 * @param value
	 */
	public EngineSpeed(double value) {
		super(Math.max(Math.min(MAX, value),  MIN));
		this.value = Math.max(Math.min(MAX, value),  MIN);
	}
	double value;
	
	public double asPrimitive() {return this.value;}
	
	// ---------- Object ----------
	@Override
	public String toString() {
		return "EngineSpeed{"+this.asPrimitive()+" rpm}";
	}
}
