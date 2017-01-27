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
package net.akehurst.example.maths;

public class AngleRadians implements IAngle {

	public static double TWO_PI = Math.PI*2;
	
	/**
	 * range -PI <= value < PI
	 * 
	 * @param value
	 */
	public AngleRadians(Double value) {
		value = value % TWO_PI;
		if (value > Math.PI) {
			this.value = value - TWO_PI;
		} else if (value <= -Math.PI) {
			this.value = value + TWO_PI;
		} else {
			this.value = value;
		}
	}

	double value;
	public double asPrimitive() {
		return this.value;
	}
	
	@Override
	public AngleRadians plus(IAngle other) {
		double value = this.asPrimitive() + other.asDegrees().asPrimitive();
		return new AngleRadians(value);
	}

	@Override
	public AngleDegrees asDegrees() {
		double d = this.value * 360 / TWO_PI;
		return new AngleDegrees(d);
	}
	
	@Override
	public AngleRadians asRadians() {
		return new AngleRadians(this.value);
	}
	
	public double sine() {
		return Math.sin(this.value);
	}
	
	public double cosine() {
		return Math.cos(this.value);
	}
	
	// ---------- Object ----------
	@Override
	public boolean equals(Object arg) {
		if (arg instanceof AngleRadians) {
			AngleRadians other = (AngleRadians)arg;
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
		return "AngleRadians{"+this.asPrimitive()+"}";
	}
}
