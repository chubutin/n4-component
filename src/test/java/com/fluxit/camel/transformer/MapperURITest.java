package com.fluxit.camel.transformer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPathExpressionException;

import org.apache.camel.RuntimeCamelException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.w3c.dom.Document;

public class MapperURITest {

	private String mapPropertiesURI = "src/test/resources/files/mapParameter/URI/mapParameter.properties";
	private String mapPropertiesURIFail = "src/test/resources/files/mapParameter/URI/mapParameterFail.txt";
	private String mapPropertiesURINotFound = "src/test/resources/files/mapParameter/URI/mapParameterX.properties";
	private String mapPropertiesFromDOM = "src/test/resources/files/mapParameter/URI/mapParameterWithName.properties";
	private String mapPropertiesFromDOMFail = "src/test/resources/files/mapParameter/URI/mapParameterXMLFail.properties";

	private String inputXML = "src/test/resources/files/mapParameter/INPUT/request.xml";

	MapperURITransformForFilter transformer = new MapperURITransformForFilter();

	@Before
	public void setUp() throws Exception {
	}

	@Test(expected = FileNotFoundException.class)
	public void testFailCatchFileNotFound() throws Throwable {
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

		List<String> body = new ArrayList<String>();
		try {
			transformer.transformURI(mapPropertiesURIFail, body);
		} catch (RuntimeCamelException e) {
			throw e.getCause();
		}
	}

	@Test
	public void testFailDifferentsSize() throws Throwable {

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
	public void testMapList() throws Throwable {

		List<String> body = new ArrayList<String>();
		body.add("Parametro1");
		body.add("Parametro2");
		body.add("Parametro3");

		try {
			String transformURI = transformer.transformURI(mapPropertiesURI,
					body);
			Assert.assertTrue(
					"El mensaje de respuesta no está correctamente formado",
					transformURI.contains("PARM_PARAMETRO_1=Parametro1"));
			Assert.assertTrue(transformURI
					.contains("PARM_PARAMETRO_2=Parametro2"));
			Assert.assertTrue(transformURI
					.contains("PARM_PARAMETRO_3=Parametro3"));
		} catch (RuntimeCamelException e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void testMapXML() throws Throwable {

		File fXmlFile = new File(inputXML);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(fXmlFile);

		// optional, but recommended
		// read this -
		// http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
		doc.getDocumentElement().normalize();

		try {
			String transformURI = transformer.transformURI(
					mapPropertiesFromDOM, doc);
			Assert.assertTrue(
					"El mensaje de respuesta no está correctamente formado",
					transformURI.contains("PARM_NOMBRE=Ramiro"));
			Assert.assertTrue(transformURI.contains("PARM_APELLIDO=Pugh"));
			Assert.assertTrue(transformURI.contains("PARM_CUIT=201234567890"));
		} catch (RuntimeCamelException e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test(expected = XPathExpressionException.class)
	public void testFailMapXML() throws Throwable {

		File fXmlFile = new File(inputXML);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(fXmlFile);

		// optional, but recommended
		// read this -
		// http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
		doc.getDocumentElement().normalize();

		try {
			transformer.transformURI(mapPropertiesFromDOMFail, doc);
		} catch (RuntimeCamelException e) {
			throw e.getCause();
		}
	}

}
