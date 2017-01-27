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

import net.akehurst.example.maths.AngleRadians;
import net.akehurst.example.maths.IAngle;



public class Vector3D {

	public Vector3D(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	double x;
	double y;
	double z;
	
	public Velocity asVelocity() {
		double mag = Math.sqrt( this.x*this.x + this.y*this.y + this.z*this.z );
		double xy = Math.sqrt( this.x*this.x + this.y*this.y );
		double az = Math.asin(this.x / xy);
		double el = Math.asin(this.z / mag);
		double tr = 0;
		IAngle azimuth = new AngleRadians(az);
		IAngle elevation =new AngleRadians(el);
		IAngle tortion =new AngleRadians(tr);
		Direction direction = new Direction(azimuth, elevation, tortion);
		Speed magnitude = new Speed(mag);
		Velocity vel = new Velocity(magnitude, direction);
		return vel;
	}
	
	public Vector3D plus(Vector3D other) {
		return new Vector3D(this.x+other.x, this.y+other.y, this.z+other.z);
	}
	
	public Vector3D minus(Vector3D other) {
		return new Vector3D(this.x-other.x, this.y-other.y, this.z-other.z);
	}
	
	// ---------- Object ----------
	@Override
	public boolean equals(Object arg) {
		if (arg instanceof Vector3D) {
			Vector3D other = (Vector3D)arg;
			return this.x == other.x && this.y==other.y && this.z==other.z;
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		return this.toString().hashCode();
	}
	
	@Override
	public String toString() {
		return "Vector3D{"+this.x+", "+this.y+", "+this.z+"}";
	}
}
