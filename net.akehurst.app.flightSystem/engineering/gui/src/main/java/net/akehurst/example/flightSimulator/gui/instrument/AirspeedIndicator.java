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

import java.text.DecimalFormat;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.transform.Rotate;



public class AirspeedIndicator extends StackPane {


	
	DoubleProperty radius = new SimpleDoubleProperty(50);
	public DoubleProperty radiusProperty() { return this.radius; }
	public Double getRadius() { return this.radius.get(); }
	public void setRadius(Double value) { this.radius.set(value); }
	
	DoubleProperty rangeLabelMax = new SimpleDoubleProperty(100);
	public DoubleProperty rangeLabelMaxProperty() { return this.rangeLabelMax; }
	public Double getRangeLableMax() { return this.rangeLabelMax.get(); }
	public void setRangeLableMax(Double value) { this.rangeLabelMax.set(value); }
	
	DoubleProperty rangeLabelMin = new SimpleDoubleProperty(0);
	public DoubleProperty rangeLabelMinProperty() { return this.rangeLabelMin; }
	public Double getRangeLableMin() { return this.rangeLabelMin.get(); }
	public void setRangeLableMin(Double value) { this.rangeLabelMin.set(value); }
	
	IntegerProperty numberOfLongTicks = new SimpleIntegerProperty(10);
	public IntegerProperty numberOfLongTicksProperty() { return this.numberOfLongTicks; }
	public Integer getNumberOfLongTicks() { return this.numberOfLongTicks.get(); }
	public void setNumberOfLongTicks(Integer value) { this.numberOfLongTicks.set(value); }
	
	DoubleProperty longTickLengthPercent = new SimpleDoubleProperty(15);
	public DoubleProperty longTickLengthPercentProperty() { return this.longTickLengthPercent; }
	public Double getLongTickLengthPercent() { return this.longTickLengthPercent.get(); }
	public void setLongTickLengthPercent(Double value) { this.longTickLengthPercent.set(value); }

	
	Circle face;
	Circle spindle;
	StackPane ticks;
	
	public AirspeedIndicator() {
		
		this.createFace();
		this.createSpindle();
		this.createTicks();

		this.spindle = new Circle(this.getRadius(), this.getRadius(), 5);
		this.spindle.setStroke(Color.WHITE);
		this.spindle.setFill(Color.BLACK);
		

		this.radiusProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				AirspeedIndicator.this.face.radiusProperty().set(newValue.doubleValue());
			}
		});
		
		this.getChildren().addAll(
			this.face,
			spindle,
			ticks
		);
		
	}

	void createFace() {
		this.face = new Circle(this.getRadius(), this.getRadius(), this.getRadius());
		this.face.setStroke(Color.WHITE);
		this.face.setFill(Color.BLACK);
	}
	
	void createSpindle() {
		
	}

	void createTicks() {
		this.ticks = new StackPane();
		Group longTicks = new Group();
		Group labels = new Group();
		this.ticks.getChildren().addAll(longTicks, labels);
		double r = this.getRadius();
		double tickStart = -1 * r * 95 / 100;
		double tickEnd = -1 * r * (95 - this.getLongTickLengthPercent()) / 100;
		double tickLength = tickEnd - tickStart;
		double labelIncrement = (this.getRangeLableMax() - this.getRangeLableMin()) / this.getNumberOfLongTicks();
		int n = this.getNumberOfLongTicks();
		double labelValue = this.getRangeLableMin();
		DecimalFormat df = new DecimalFormat("#"); 
		for(int i=0; i< n; ++i) {
			double angle = (360/n)*i;
			Line tick = new Line(0, tickStart, 0, tickEnd);
			tick.setStroke(Color.WHITE);
			tick.setLayoutX(this.getRadius());
			tick.setLayoutY(this.getRadius());
			tick.getTransforms().add( new Rotate(angle) );
			longTicks.getChildren().add(tick);
			
			Label label = new Label(df.format(labelValue));
			label.setFont(new Font(4));
			label.setTextFill(Color.WHITE);
			label.setLayoutX( Math.cos(angle) * (r-tickLength) );
			label.setLayoutY( Math.sin(angle) * (r-tickLength) );
			//label.getTransforms().add( new Rotate((360/n)*i) );
			labels.getChildren().add(label);
			labelValue +=labelIncrement;
		}
	}
}

