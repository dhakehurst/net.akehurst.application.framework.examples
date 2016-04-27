package temperatureSensor.computational.interfaceSampler;

import net.akehurst.application.framework.common.AbstractDataType;

//DataType
public class SensorIdentity extends AbstractDataType {

	public SensorIdentity(String value) {
		super(value);
		this.value = value;
	}

	private String value;
	public String asPrimitive() {
		return this.value;
	}
	
}