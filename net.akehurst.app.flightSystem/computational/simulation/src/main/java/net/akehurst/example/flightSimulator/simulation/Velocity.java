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

public class Velocity {

	public Velocity(Speed magnitude, IDirection direction) {
		this.direction = direction;
		this.magnitude = magnitude;
	}
	
	IDirection direction;
	public IDirection getDirection() {
		return this.direction;
	}
	
	Speed magnitude;
	public Speed getMagnitude() {
		return this.magnitude;
	}
	
	Vector3D asVector() {
		double length = direction.getElevation().cosine()*this.getMagnitude().asPrimitive();
		double z = direction.getElevation().sine()*this.getMagnitude().asPrimitive();
		double x = length * this.getDirection().getAzimuth().sine();
		double y = length * this.getDirection().getAzimuth().cosine();
		return new Vector3D(x, y, z);
	}
	
	public Velocity plus(Velocity other) {
		Vector3D vec = this.asVector().plus(other.asVector());
		Velocity res = vec.asVelocity();
		return res;
	}
	
	public Velocity minus(Velocity other) {
		Vector3D vec = this.asVector().minus(other.asVector());
		Velocity res = vec.asVelocity();
		return res;	
	}
}
