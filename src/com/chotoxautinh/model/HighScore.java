package com.chotoxautinh.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

@XmlRootElement(name = "highscore")
public class HighScore {
    private final StringProperty name;
	private final LongProperty point;

	public HighScore() {
		this(null, 0);
	}

	public HighScore(String name, long point) {
		this.name = new SimpleStringProperty(name);
		this.point = new SimpleLongProperty(point);
	}
	
	@XmlElement(name = "name", required = true)
    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public StringProperty nameProperty() {
        return name;
    }

	@XmlElement(name = "point", required = true)
	public long getPoint() {
		return point.get();
	}

	public void setPoint(long point) {
		this.point.set(point);
	}

	public LongProperty pointProperty() {
		return point;
	}
}
