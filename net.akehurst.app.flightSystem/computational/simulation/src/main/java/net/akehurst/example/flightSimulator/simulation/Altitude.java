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
package net.akehurst.example.flightSimulator.simulation;

public class Altitude {

	public Altitude(double value) {
		this.value = value;
	}
	
	double value;
	
	public double asPrimitive() {
		return this.value;
	}
	
	// ---------- Object ----------
	@Override
	public boolean equals(Object arg) {
		if (arg instanceof Altitude) {
			Altitude other = (Altitude)arg;
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
		return "Altitude{"+this.asPrimitive()+" m}";
	}
}
