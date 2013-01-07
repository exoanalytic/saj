package it.dangelo.saj.impl.parser;

import it.dangelo.saj.SAJException;

import java.math.BigDecimal;
import java.util.ArrayList;

public class EventPipeline {

	private ArrayList<Component> components = new ArrayList<Component>();
	private Handlers handlers;
	
	public void setHandlers(Handlers handlers) {
		this.handlers = handlers;
	}
	
	public Handlers getHandlers() {
		return handlers;
	}
	
	public void addComponent(Component component) {
		this.components.add(component);
	}
	
	public void _boolean(boolean value) throws SAJException {
		for (Component component : this.components) {
			component._boolean(value);
		}
	}

	public void _null() throws SAJException {
		for (Component component : this.components) {
			component._null();
		}
	}

	public void _number(BigDecimal value) throws SAJException {
		for (Component component : this.components) {
			component._number(value);
		}
	}

	public void _string(String value) throws SAJException {
		for (Component component : this.components) {
			component._string(value);
		}
	}

	public void attribute(String name) throws SAJException {
		for (Component component : this.components) {
			component.attribute(name);
		}
	}

	public void endArray() throws SAJException {
		for (Component component : this.components) {
			component.endArray();
		}
	}

	public void endJSON() throws SAJException {
		for (Component component : this.components) {
			component.endJSON();
		}
	}

	public void endObject() throws SAJException {
		for (Component component : this.components) {
			component.endObject();
		}
	}

	public void startArray() throws SAJException {
		for (Component component : this.components) {
			component.startArray();
		}
	}

	public void startJSON() throws SAJException {
		for (Component component : this.components) {
			component.startJSON();
		}
	}

	public void startObject() throws SAJException {
		for (Component component : this.components) {
			component.startObject();
		}
	}

}
