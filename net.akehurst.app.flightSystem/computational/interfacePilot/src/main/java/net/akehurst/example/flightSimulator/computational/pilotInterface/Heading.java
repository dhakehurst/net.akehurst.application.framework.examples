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
package net.akehurst.example.flightSimulator.computational.pilotInterface;

import net.akehurst.example.maths.AngleRadians;

public class Heading extends AngleRadians {

	/**
	 * Heading (angle in Radians)
	 * will clip value to the range -180 <= value <= +180
	 * 
	 * @param value
	 */
	public Heading(double value) {
		super(value);
	}

	
	// ---------- Object ----------
	@Override
	public boolean equals(Object arg) {
		if (arg instanceof Heading) {
			Heading other = (Heading)arg;
			return this.asPrimitive() == other.asPrimitive();
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		return Double.valueOf(this.asPrimitive()).hashCode();
	}
	
	@Override
	public String toString() {
		return "Heading{"+this.asPrimitive()+" radians}";
	}
}
