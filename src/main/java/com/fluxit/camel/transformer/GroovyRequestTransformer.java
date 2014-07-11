package com.fluxit.camel.transformer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.camel.RuntimeCamelException;
import org.apache.commons.validator.routines.UrlValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Transformer que genera un XML del tipo <br/>
 * <p>
 * 
 * <pre>
 * {@code
 * <groovy class-location="database" class-name="RecordServiceEventForUnit">
 * 	<parameters>
 * 		<parameter id="unitNbr" value="FMPU1231231"/>
 * 		<parameter id="eventType" value="RCTEST"/>
 * 		<parameter id="quantity" value="30"/>
 * 		<parameter id="quantityUnit" value="METERS"/>
 * 	</parameters>
 * </groovy>}
 * 
 * @author chubutin
 * 
 */
public class GroovyRequestTransformer {

	private static final Logger LOG = LoggerFactory
			.getLogger(GroovyRequestTransformer.class);

	private final String rootElementName = "groovy";
	private String classLocation;
	private String className;

	// mapa de parámetros en donde el key será el parámetro y el valor el nombre
	// del parámetro en el request
	private Map<String, String> mapValues;

	public GroovyRequestTransformer() {
	}

	public GroovyRequestTransformer(String mapPropertiesURI,
			String classLocation, String rootElementName, String className) {
		super();
		LOG.debug(
				"Creando el transformer con rootElement {0}, classLocation {1}, className {2} y las properties en la URI {3} ",
				rootElementName, classLocation, className, mapPropertiesURI);

		loadMapFromProperties(mapPropertiesURI);

	}

	private void loadMapFromProperties(String mapPropertiesURI) {
		Properties prop = new Properties();
		InputStream input = null;
		try {
			input = new FileInputStream(mapPropertiesURI);
			prop.load(input);
		} catch (FileNotFoundException e) {
			LOG.error(MessageFormat.format(
					"No se encontró el archivo de propiedades en la URI: {0}",
					mapPropertiesURI), e);
			throw new RuntimeCamelException(e);
		} catch (IOException e) {
			LOG.error(
					MessageFormat
							.format("I/O exception en el archivo de propiedades en la URI: {0}",
									mapPropertiesURI), e);
			throw new RuntimeCamelException(e);
		}

		this.mapValues = new HashMap<String, String>((Map) prop);
		LOG.debug("Las propiedades del transformer son {0}", mapValues);
	}

	/**
	 * Retorna el XML a enviar a N4 a través de su Interface Groovy
	 * 
	 * @return
	 */
	public String transform() {

		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder docBuilder;

			docBuilder = docFactory.newDocumentBuilder();

			// root elements
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement(rootElementName);
			rootElement.setAttribute("class-location", classLocation);
			rootElement.setAttribute("class-name", className);

			doc.appendChild(rootElement);

			Element parameters = doc.createElement("parameters");

			for (String parameter : mapValues.keySet()) {
				Element parElement = doc.createElement("parameter");
				parElement.setAttribute("id", parameter);
				parElement.setAttribute("value", mapValues.get(parameter));
				parameters.appendChild(parElement);
			}

			rootElement.appendChild(parameters);

			return convertDocumentToString(doc);

		} catch (ParserConfigurationException e) {
			LOG.error("Error de parseo", e);
			throw new RuntimeCamelException("Error de parseo", e);
		}

	}

	private static String convertDocumentToString(Document doc) {
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer;
		try {
			transformer = tf.newTransformer();
			// below code to remove XML declaration
			// transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION,
			// "yes");
			StringWriter writer = new StringWriter();
			transformer.transform(new DOMSource(doc), new StreamResult(writer));
			String output = writer.getBuffer().toString();
			return output;
		} catch (TransformerException e) {
			LOG.error(
					"Error en la transformacion del documento de parametros a String",
					e);
			throw new RuntimeCamelException(
					"Error en la transformacion del documento de parametros a String",
					e);
		}
	}

	public String getClassLocation() {
		return classLocation;
	}

	public void setClassLocation(String classLocation) {
		this.classLocation = classLocation;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public Map<String, String> getMapValues() {
		return mapValues;
	}

	public void setMapValues(Map<String, String> mapValues) {
		this.mapValues = mapValues;
	}

	public String getRootElementName() {
		return rootElementName;
	}

}
