package com.fluxit.camel.transformer;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

public class GroovyRequestTest {

	@Test
	public void testGenerateRequest() {

		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("unitNbr", "FMPU1231231");
		parameters.put("eventType", "RCTEST");
		parameters.put("quantity", "30");
		parameters.put("quantityUnit", "METERS");

		GroovyRequestTransformer transformer = new GroovyRequestTransformer();
		transformer.setClassLocation("database");
		transformer.setClassName("RecordServiceEventForUnit");
		transformer.setMapValues(parameters);

		String transform = transformer.transform();

		Assert.assertFalse("El string es vacío", transform.isEmpty());
		Assert.assertTrue("El string no está bien formado", transform.contains("<groovy class-location"));

	}

}
