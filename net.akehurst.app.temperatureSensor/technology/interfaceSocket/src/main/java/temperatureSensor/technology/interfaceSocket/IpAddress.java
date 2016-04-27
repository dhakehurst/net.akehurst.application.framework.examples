package temperatureSensor.technology.interfaceSocket;

import net.akehurst.application.framework.common.AbstractDataType;

public class IpAddress extends AbstractDataType {

	public IpAddress(String value) {
		super(value);
		this.value = value;
	}

	String value;
	public String asPrimitive() {
		return this.value;
	}
}
