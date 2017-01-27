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


public class AngleDegrees implements IAngle {

	/**
	 * range 0 <= value < 360
	 * 
	 * @param value
	 */
	public AngleDegrees(Double value) {
		if (value >= 360) {
			this.value = value % 360;
		} else if (value < 0) {
			this.value = 360 + (value % 360);
		} else {
			this.value = value;
		}
	}

	Double value;
	public Double asPrimitive() {
		return this.value;
	}
	
	@Override
	public AngleDegrees plus(IAngle other) {
		double value = this.asPrimitive() + other.asDegrees().asPrimitive();
		return new AngleDegrees(value);
	}

	@Override
	public AngleDegrees asDegrees() {
		return new AngleDegrees(this.value);
	}
	
	@Override
	public AngleRadians asRadians() {
		double v = this.value * AngleRadians.TWO_PI / 360;
		return new AngleRadians(v);
	}
	
	public double sine() {
		return this.asRadians().sine();
	}
	
	public double cosine() {
		return this.asRadians().cosine();
	}
	
	// ---------- Object ----------
	@Override
	public boolean equals(Object arg) {
		if (arg instanceof AngleDegrees) {
			AngleDegrees other = (AngleDegrees)arg;
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
		return "AngleDegrees{"+this.asPrimitive()+"}";
	}
}
