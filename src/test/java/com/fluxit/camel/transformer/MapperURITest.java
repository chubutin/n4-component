package com.fluxit.camel.transformer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.camel.RuntimeCamelException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MapperURITest {

	private static final transient Logger log = LoggerFactory
			.getLogger(MapperURITest.class);

	private String mapPropertiesURI = "src/test/resources/files/mapParameter/URI/mapParameter.properties";
	private String mapPropertiesURIFail = "src/test/resources/files/mapParameter/URI/mapParameterFail.txt";
	private String mapPropertiesURINotFound = "src/test/resources/files/mapParameter/URI/mapParameterX.properties";

	@Before
	public void setUp() throws Exception {
	}

	@Test(expected = FileNotFoundException.class)
	public void testFailCatchFileNotFound() throws Throwable {

		MapperURITransform transformer = new MapperURITransform();
		List<String> body = new ArrayList<String>();
		try {
			transformer.transformURI(mapPropertiesURINotFound, body);
		} catch (RuntimeCamelException e) {
			throw e.getCause();
		}
	}

	// TODO ver como tirar el IO exception
	@Test(expected = IOException.class)
	@Ignore(value = "Se ignora porque no se puede capturar una IO exception")
	public void testFailCatchIOException() throws Throwable {

		writeInFile();
		MapperURITransform transformer = new MapperURITransform();
		List<String> body = new ArrayList<String>();
		try {
			transformer.transformURI(mapPropertiesURIFail, body);
		} catch (RuntimeCamelException e) {
			throw e.getCause();
		}
	}

	@Test
	public void testFailDifferentsSize() throws Throwable {

		MapperURITransform transformer = new MapperURITransform();
		List<String> body = new ArrayList<String>();
		body.add("Parametro1");
		body.add("Parametro2");

		try {
			transformer.transformURI(mapPropertiesURI, body);
		} catch (RuntimeCamelException e) {
			Assert.assertTrue(
					"El mensaje de excepcion no es el esperado",
					e.getMessage()
							.contains(
									"El tamaño de las propiedades del mapa de URI es diferente al tamaño de la lista "));
			return;
		}
		Assert.fail("Se debería haber capturado una excepcion");
	}

	@Test
	public void testMap() throws Throwable {

		MapperURITransform transformer = new MapperURITransform();
		List<String> body = new ArrayList<String>();
		body.add("Parametro1");
		body.add("Parametro2");
		body.add("Parametro3");

		try {
			String transformURI = transformer.transformURI(mapPropertiesURI,
					body);
			Assert.assertEquals(
					"El mensaje de respuesta es nulo",
					transformURI,
					"PARM_PARAMETRO_1=Parametro1&PARM_PARAMETRO_2=Parametro2&PARM_PARAMETRO_3=Parametro3");
		} catch (RuntimeCamelException e) {
			Assert.fail(e.getMessage());
		}

	}

	private void writeInFile() throws FileNotFoundException,
			UnsupportedEncodingException {
		PrintWriter out1 = new PrintWriter(new File(mapPropertiesURIFail),
				"ISO8859_1");
		char c = 0x000A;
		out1.append(c);
		out1.close();
	}

	public void test() {
		MapperURITransform transformer = new MapperURITransform();
		List<String> body = new ArrayList<String>();
		transformer.transformURI(mapPropertiesURI, body);

	}

}
