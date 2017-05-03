package net.akehurst.app.umlEditor.computational.interfaceUser.data;

import java.util.regex.Pattern;

import net.akehurst.app.umlEditor.computational.interfaceUser.UserException;
import net.akehurst.application.framework.common.AbstractDataType;
import net.akehurst.application.framework.common.annotations.declaration.DataType;

@DataType
public class TransitionIdentity extends AbstractDataType {
	private static Pattern valid = Pattern.compile("[a-zA-Z_][a-zA-Z_0-9]*");

	public TransitionIdentity(final String value) throws UserException {
		super(value);
		if (TransitionIdentity.valid.matcher(value).matches()) {
			// ok
		} else {
			throw new UserException("Invalid State Identity value - " + value, null);
		}
	}

	public String getValue() {
		return (String) super.getIdentityValues().get(0);
	}

}