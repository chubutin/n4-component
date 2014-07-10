package com.fluxit.camel.transformer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.camel.RuntimeCamelException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Clase encargada de transformar el request de entrada en los parámetros URI
 * para un Filtro
 * 
 * @author Ramiro Pugh
 * @email ramiro.pugh@fluxit.com.ar
 * 
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class MapperURITransformForGroovy {

	private static final transient Logger LOG = LoggerFactory
			.getLogger(MapperURITransformForGroovy.class);

	/**
	 * Orquestador de la generación de parámetros para el filtro de N4
	 * 
	 * @param mapPropertiesURI
	 * @param body
	 * @return
	 */
	public String transformURI(String mapPropertiesURI, Object body) {
		LOG.debug(MessageFormat
				.format("Transformando el mapa de parametros {0} y el body {1} en el listado de argumentos para un filtro",
						mapPropertiesURI, body));
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

		Map<String, String> map = new HashMap<String, String>((Map) prop);

		LOG.debug("Transformando el body al mapa de URIS");

		if (body instanceof List) {
			LOG.debug("El body es una lista");
			return transformBodyAsList(map, (List) body);
		}
		if (body instanceof Document) {
			LOG.debug("El body es un document");
			return transformBodyAsDocument(map, (Document) body);
		}

		throw new RuntimeCamelException(
				MessageFormat
						.format("No se encontraron tipos de datos para mapear las URIS, el body es : {0} y su tipo es {1}",
								body, body.getClass()));

	}

	/**
	 * Transforma el dom del request en los parámetros del filtro a enviar
	 * 
	 * @param mapUri
	 * @param body
	 * @return
	 */
	private String transformBodyAsDocument(Map<String, String> mapUri,
			Document body) {

		XPathFactory xPathfactory = XPathFactory.newInstance();
		XPath xpath = xPathfactory.newXPath();

		HashMap<String,String> map = new HashMap<String, String>();

		for (String key : mapUri.keySet()) {
			try {
				XPathExpression expr = xpath.compile("//" + key);
				NodeList nl = (NodeList) expr.evaluate(body,
						XPathConstants.NODESET);
				if (nl != null && nl.getLength() > 0) {
					if (nl.getLength() > 1) {
						LOG.warn(MessageFormat
								.format("Existen múltiples elementos en el XML de entrada con el tag {0}",
										key));
					} else {
						Node currentItem = nl.item(0);
						String value = currentItem.getTextContent();
						map.put(mapUri.get(key), value);
					}
				} else {
					LOG.warn(MessageFormat.format(
							"No se encontró el parámetro {0} en el XML", key));
				}
			} catch (XPathExpressionException e) {
				LOG.error("Error al compilar la expresion xpath : //" + key, e);
				throw new RuntimeCamelException(MessageFormat.format(
						"Error al compilar la expresion xpath : //{0}", key), e);
			}
		}

		return formatParameters(map);
	}

	/**
	 * Transforma la lista serializada del request en los parámetros del filtro
	 * a enviar
	 * 
	 * @param mapUri
	 * @param body
	 * @return
	 */
	private String transformBodyAsList(Map<String, String> mapUri, List body) {

		HashMap<String, String> map = new HashMap<String, String>();

		if (mapUri.entrySet().size() != body.size()) {
			throw new RuntimeCamelException(
					MessageFormat
							.format("El tamaño de las propiedades del mapa de URI es diferente al tamaño de la lista del request. El tamaño de la lista es: {0} ",
									body.size()));
		}
		for (int i = 0; i < body.size(); i++) {
			if (mapUri.get(String.valueOf(i)) == null) {
				throw new RuntimeCamelException(MessageFormat.format(
						"No se encontró el item de la posicion {0} en el mapa",
						i));
			}
			map.put(mapUri.remove(String.valueOf(i)),String.valueOf(body.get(i)));
		}
		return formatParameters(map);
	}

	/**
	 * Genera la lista de parámetros. El mapa estara compuesto por <br>
	 * <b>key = PARAM_NAME; value = PARAM_VALUE</b>
	 * 
	 * @param mapUri
	 * @param body
	 * @return
	 */
	private String formatParameters(Map<String, String> mapUri) {

		LOG.debug("Formateando los parámetros para el filtro");
		
		return null;
	}

}
