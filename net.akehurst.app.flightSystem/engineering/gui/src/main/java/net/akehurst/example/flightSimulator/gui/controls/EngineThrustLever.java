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
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.shape.Ellipse;

public class EngineThrustLever extends Region {
	private static final double PREFERRED_WIDTH = 50;
	private static final double PREFERRED_HEIGHT = 100;
	private static final double MINIMUM_WIDTH = 5;
	private static final double MINIMUM_HEIGHT = 5;
	private static final double MAXIMUM_WIDTH = 1024;
	private static final double MAXIMUM_HEIGHT = 1024;

	public EngineThrustLever() {
		getStylesheets().add(getClass().getResource("EngineThrustLever.css").toExternalForm());
		getStyleClass().add("EngineThrustLever");

		this.init();
		this.initG();

		widthProperty().addListener(observable -> resize());
		heightProperty().addListener(observable -> resize());

		this.knobRadiusX.addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				knob.setRadiusX(newValue.doubleValue());
			}
		});
		this.knobRadiusY.addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				knob.setRadiusY(newValue.doubleValue());
			}
		});
		this.knobYPercent.addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				
				knob.setLayoutY(getKnobYValue());
			}
		});
		this.prefHeightProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				knob.setLayoutY(getKnobYValue());
			}
		});
		this.knob.onMousePressedProperty().set(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				mouseX = event.getSceneX();
				mouseY = event.getSceneY();
				kX = knob.getLayoutX();
				kY = knob.getLayoutY();
			}
		});

		this.knob.onMouseDraggedProperty().set(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				 double offsetX = event.getSceneX() - mouseX;
				 double offsetY = event.getSceneY() - mouseY;
				 kX += offsetX;
				 kY += offsetY;
				 moveTo(kX,kY);

				 mouseX = event.getSceneX();
				 mouseY = event.getSceneY();
				  
				 event.consume(); 
			}
		});
	}

	double mouseX;
	double mouseY;
	double kX;
	double kY;
	
	Pane pane;
	// Canvas canvas;
	Region background;
	Ellipse knob;
	GraphicsContext gc;

	double getRange() {
		return this.getHeight() - this.getKnobRadiusY()*2;
	}

	DoubleProperty knobX = new SimpleDoubleProperty();

	public final double getKnobX() {
		return knobX.get();
	}

	public final void setKnobX(final double value) {
		knobX.set(value);
	}

	public final DoubleProperty knobXProperty() {
		return knobX;
	}

	double getKnobYValue() {
		return (this.getKnobYPercent() / 100 * this.getRange())+this.getKnobRadiusY();
	}

	void setKnobYValue(double value) {
		this.setKnobYPercent(((value-this.getKnobRadiusY()) / this.getRange()) * 100);
	}

	DoubleProperty knobYPercent = new SimpleDoubleProperty();

	public final double getKnobYPercent() {
		return knobYPercent.get();
	}

	public final void setKnobYPercent(final double value) {
		knobYPercent.set(value);
	}

	public final DoubleProperty knobYPercentProperty() {
		return knobYPercent;
	}

	DoubleProperty knobRadiusX = new SimpleDoubleProperty();

	public final double getKnobRadiusX() {
		return knobRadiusX.get();
	}

	public final void setKnobRadiusX(final double value) {
		knobRadiusX.set(value);
	}

	public final DoubleProperty knobRadiusXProperty() {
		return knobRadiusX;
	}

	DoubleProperty knobRadiusY = new SimpleDoubleProperty();

	public final double getKnobRadiusY() {
		return knobRadiusY.get();
	}

	public final void setKnobRadiusY(final double value) {
		knobRadiusY.set(value);
	}

	public final DoubleProperty knobRadiusYProperty() {
		return knobRadiusY;
	}

	void resize() {
		double width = getWidth();
		double height = getHeight();

		if (width > 0 && height > 0) {
			this.setKnobX(width / 2);
			this.background.setPrefWidth(width);
			this.background.setPrefHeight(height);
			this.moveTo(0, getKnobYValue());
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
		this.background = new Region();
		this.background.getStyleClass().add("background");
		this.background.setPrefSize(PREFERRED_WIDTH, PREFERRED_HEIGHT);

		this.knob = new Ellipse();
		this.knob.getStyleClass().add("knob");

		this.pane = new Pane(this.background, this.knob);
		this.getChildren().setAll(this.pane);
		this.resize();
	}

	void moveTo(double x, double y) {
		double maxX = this.getWidth() - this.getKnobRadiusX();
		double minX = this.getKnobRadiusX();
		double maxY = this.getHeight() - this.getKnobRadiusY();
		double minY = this.getKnobRadiusY();

		this.setKnobX(this.getWidth() / 2);
		double ky = Math.min(maxY, Math.max(y, minY));
		this.setKnobYValue(ky);
		
		this.knob.setLayoutX(this.getKnobX());
		this.knob.setLayoutY(this.getKnobYValue());
	}
}
