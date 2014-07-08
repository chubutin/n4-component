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

import org.apache.camel.RuntimeCamelException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class MapperURITransform {

	private static final transient Logger LOG = LoggerFactory
			.getLogger(MapperURITransform.class);

	public String transformURI(String mapPropertiesURI, Object body) {

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

	private String transformBodyAsDocument(Map<String, String> mapUri,
			Document body) {
		// TODO Auto-generated method stub
		return null;
	}

	private String transformBodyAsList(Map<String, String> mapUri, List body) {
		if (mapUri.entrySet().size() != body.size()) {
			throw new RuntimeCamelException(
					MessageFormat
							.format("El tamaño de las propiedades del mapa de URI es diferente al tamaño de la lista del request. El tamaño de la lista es: {0} ",
									body.size()));
		}
		for (int i = 0; i < body.size(); i++) {
			if (mapUri.get(i) == null) {
				throw new RuntimeCamelException(MessageFormat.format(
						"No se encontró el item de la posicion {0} en el mapa",
						i));
			}
		}
		return formatParameters(mapUri, body);
	}

	private String formatParameters(Map<String, String> mapUri,
			List<String> body) {
		StringBuffer returnString = new StringBuffer();
		for (int i = 0; i < body.size(); i++) {
			returnString.append(mapUri.get(i)).append("=").append(body.get(i));
		}
		return returnString.toString();
	}

}
