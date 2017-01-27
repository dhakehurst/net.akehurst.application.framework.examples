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
package net.akehurst.example.flightSimulator.gui.controls;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;

public class VirtualJoystick extends Region {
	private static final double PREFERRED_WIDTH = 320;
	private static final double PREFERRED_HEIGHT = 320;
	private static final double MINIMUM_WIDTH = 5;
	private static final double MINIMUM_HEIGHT = 5;
	private static final double MAXIMUM_WIDTH = 1024;
	private static final double MAXIMUM_HEIGHT = 1024;

	public VirtualJoystick() {

		this.init();
		this.initG();

		widthProperty().addListener(observable -> resize());
		heightProperty().addListener(observable -> resize());

		this.canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				double deadX = VirtualJoystick.this.getDeadZoneX();
				double deadY = VirtualJoystick.this.getDeadZoneY();
				double cX = VirtualJoystick.this.getWidth() / 2;
				double cY = VirtualJoystick.this.getHeight() / 2;
				double x = (e.getX() < (cX-deadX) || e.getX() > (cX+deadX)) ? (e.getX()) : cX;
				double y = (e.getY() < (cY-deadY) || e.getY() > (cY+deadY)) ? (e.getY()) : cY;
				
				moveTo(x, y);
				draw();
			}
		});
		this.canvas.addEventHandler(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				center();
				draw();
			}
		});
	}

	Pane pane;
	Canvas canvas;
	GraphicsContext gc;

    double getKnobXValue() {
    	double w = this.canvas.getWidth();
    	double v = this.getKnobXPercent()/100 * (w/2);
    	double vd = v + (w/2);
    	double x = vd > (w/2) ? vd + this.getDeadZoneX() : (vd < (w/2) ? vd - this.getDeadZoneX() : w/2);
    	return x;
    }
    void setKnobXValue(double value) {
    	double w = this.canvas.getWidth();
    	double max = (w/2) - this.getDeadZoneX();
    	double v = value - (w/2);
    	double vd = (v > 0) ? v - this.getDeadZoneX() : (v < 0 ? v + this.getDeadZoneX() : 0); 
    	double p = (vd/(max)) *100;
    	this.setKnobXPercent( p );
    }
	DoubleProperty knobXPercent = new SimpleDoubleProperty();
    public final double getKnobXPercent() { return knobXPercent.get(); }
    public final void setKnobXPercent(final double value) { knobXPercent.set(value); }
    public final DoubleProperty knobXPercentProperty() { return knobXPercent; }
    
    
    double getKnobYValue() {
    	double h = this.canvas.getHeight();
    	double v = this.getKnobYPercent()/100 * (h/2);
    	double vd = v + (h/2);
    	double y = vd > (h/2) ? vd + this.getDeadZoneY() : (vd < (h/2) ? vd - this.getDeadZoneY() : h/2);
    	return y;
    }
    void setKnobYValue(double value) {
    	double h = this.canvas.getHeight();
    	double max = (h/2) - this.getDeadZoneY();
    	double v = value - (h/2);
    	double vd = (v > 0) ? v - this.getDeadZoneY() : (v < 0 ? v + this.getDeadZoneY() : 0); 
    	double p = (vd/(max)) *100;
    	this.setKnobYPercent( p );
    }
	DoubleProperty knobYPercent = new SimpleDoubleProperty();
    public final double getKnobYPercent() { return knobYPercent.get(); }
    public final void setKnobYPercent(final double value) { knobYPercent.set(value); }
    public final DoubleProperty knobYPercentProperty() { return knobYPercent; }
    
	DoubleProperty deadZoneX = new SimpleDoubleProperty();
    public final double getDeadZoneX() { return deadZoneX.get(); }
    public final void setDeadZoneX(final double value) { deadZoneX.set(value); }
    public final DoubleProperty knobDeadZoneX() { return deadZoneX; }
    
	DoubleProperty deadZoneY = new SimpleDoubleProperty();
    public final double getDeadZoneY() { return deadZoneY.get(); }
    public final void setDeadZoneY(final double value) { deadZoneY.set(value); }
    public final DoubleProperty knobDeadZoneY() { return deadZoneY; }

    
	double knobRadius = 10;

	void resize() {
		double width = getWidth();
		double height = getHeight();

		if (width > 0 && height > 0) {
			this.canvas.setWidth(width);
			this.canvas.setHeight(height);
			this.draw();
		}
	}

	private void init() {
		if (Double.compare(getPrefWidth(), 0.0) <= 0 || Double.compare(getPrefHeight(), 0.0) <= 0
				|| Double.compare(getWidth(), 0.0) <= 0 || Double.compare(getHeight(), 0.0) <= 0) {
			if (getPrefWidth() > 0 && getPrefHeight() > 0) {
				setPrefSize(getPrefWidth(), getPrefHeight());
			} else {
				setPrefSize(PREFERRED_WIDTH, PREFERRED_HEIGHT);
			}
		}

		if (Double.compare(getMinWidth(), 0.0) <= 0 || Double.compare(getMinHeight(), 0.0) <= 0) {
			setMinSize(MINIMUM_WIDTH, MINIMUM_HEIGHT);
		}

		if (Double.compare(getMaxWidth(), 0.0) <= 0 || Double.compare(getMaxHeight(), 0.0) <= 0) {
			setMaxSize(MAXIMUM_WIDTH, MAXIMUM_HEIGHT);
		}
	}

	void initG() {
		this.canvas = new Canvas(PREFERRED_WIDTH, PREFERRED_HEIGHT);
		this.gc = this.canvas.getGraphicsContext2D();
		this.pane = new Pane(this.canvas);
		this.getChildren().setAll(this.pane);
		this.resize();
	}

	void draw() {
		this.drawBackgound();
		this.drawKnob();
	}

	void drawBackgound() {
		double w = this.canvas.getWidth();
		double h = this.canvas.getHeight();

		gc.clearRect(0, 0, w, h);

		gc.setFill(Color.BLACK);
		gc.fillRect(0, 0, w, h);

		gc.setStroke(Color.BLUE);
		gc.setLineWidth(3);
		gc.strokeRect(0, 0, w - 3, h - 3);
	}

	void drawKnob() {
		double width = this.canvas.getWidth();
		double height = this.canvas.getHeight();

		gc.setFill(Color.GREEN);
		gc.fillOval(this.getKnobXValue() - knobRadius, this.getKnobYValue() - knobRadius, knobRadius * 2, knobRadius * 2);

	}

	void center() {
		this.moveTo(this.canvas.getWidth() / 2, this.canvas.getHeight() / 2);
	}
	
	void moveTo(double x, double y) {
		double maxX = this.canvas.getWidth() - this.knobRadius;
		double minX = this.knobRadius;
		double maxY = this.canvas.getHeight() - this.knobRadius;
		double minY = this.knobRadius;

		this.setKnobXValue( Math.min(maxX, Math.max(x, minX)) );
		this.setKnobYValue( Math.min(maxY, Math.max(y, minY)) );
	}

}
