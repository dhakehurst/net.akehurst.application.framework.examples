package temperatureSensor.computational.interfaceSampler;

import net.akehurst.application.framework.common.AbstractDataType;

//DataType
public class TimeStampMilliseconds extends AbstractDataType {

	public TimeStampMilliseconds(long value) {
		super(value);
		this.value = value;
	}

	private long value;
	public long asPrimitive() {
		return this.value;
	}
	
}