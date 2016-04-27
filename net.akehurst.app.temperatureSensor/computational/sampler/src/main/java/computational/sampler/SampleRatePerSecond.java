package computational.sampler;

import net.akehurst.application.framework.common.AbstractDataType;

public class SampleRatePerSecond extends AbstractDataType {
	
	public SampleRatePerSecond(String value) {
		this(Integer.parseInt(value));
	}
	
	public SampleRatePerSecond(int value) {
		super(value);
		this.value = value;
	}

	private int value;
	public int asPrimitive() {
		return this.value;
	}
}
