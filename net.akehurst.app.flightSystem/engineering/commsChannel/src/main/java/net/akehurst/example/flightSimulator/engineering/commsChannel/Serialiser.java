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
package net.akehurst.example.flightSimulator.engineering.commsChannel;

import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonStructure;
import javax.json.JsonWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Serialiser {

	Logger log;

	public Serialiser() {
		this.log = LoggerFactory.getLogger(this.getClass());
	}
	
	public byte[] serialiseDouble(Class<?> type, double value) {
		JsonObject jsonModel = Json.createObjectBuilder()
				.add("type", type.getName())
				.add("value", value).build();

		StringWriter strWriter = new StringWriter();
		try (JsonWriter jsonWriter = Json.createWriter(strWriter)) {
			jsonWriter.writeObject(jsonModel);
		}

		String jsonData = strWriter.toString();

		log.trace("marshalled: " + jsonData);
		return jsonData.getBytes();
	}
	
	public <T> T unserialseDouble(Class<T> type, JsonObject jobj) {
		try {
			double value = jobj.getJsonNumber("value").doubleValue();
			T v = type.getConstructor(double.class).newInstance(value);
			return v;
		} catch (InstantiationException e) {
		} catch (IllegalAccessException e) {
		} catch (IllegalArgumentException e) {
		} catch (InvocationTargetException e) {
		} catch (NoSuchMethodException e) {
		} catch (SecurityException e) {
		}
		throw new RuntimeException("Should not occur");
	}
	
	public Object unserialise(byte[] bytes) {
		try {
			String jsonData = new String(bytes);
			JsonReader reader = Json.createReader(new StringReader(jsonData));
			JsonStructure json = reader.read();

			JsonObject jobj = (JsonObject) json;
			String typeName = jobj.getString("type");
			Class<?> type = Class.forName(typeName);

			return this.unserialseDouble(type, jobj);

		} catch (Exception e) {
			log.error("in unserialise", e);
		}
		return null;
	}

}
