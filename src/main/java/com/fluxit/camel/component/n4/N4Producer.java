/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fluxit.camel.component.n4;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.camel.Exchange;
import org.apache.camel.builder.xml.XsltUriResolver;
import org.apache.camel.component.http.HttpOperationFailedException;
import org.apache.camel.impl.DefaultProducer;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthPolicy;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fluxit.camel.transformer.MapperURITransformForFilter;

/**
 * 
 * @author chubutin
 * 
 */
public class N4Producer extends DefaultProducer {

	private static final transient Logger LOG = LoggerFactory
			.getLogger(N4Producer.class);

	private N4Endpoint endpoint;

	public N4Producer(N4Endpoint endpoint) {
		super(endpoint);
		this.endpoint = endpoint;

		if (LOG.isDebugEnabled()) {
			LOG.debug("Productor N4 creado con el endpoint {0}", this.endpoint);
			LOG.debug("El tipo de invocacion a n4 será {0}",
					this.endpoint.getProviderType());
			if (endpoint.getClassSerialization() != null) {
				LOG.debug("Se serializará a la clase {0}",
						this.endpoint.getClassSerialization());
			} else {
				LOG.debug("No se serializará a ninguna clase luego de la transformación");
			}
		}
	}

	/**
	 * TODO JAVADOC!!!!
	 */
	public void process(Exchange exchange) throws Exception {

		Object resultBody = null;

		LOG.debug("Exchange en N4 Producer: {0}", exchange.getIn().getBody());

		XsltUriResolver uriResolver = new XsltUriResolver(exchange.getContext()
				.getClassResolver(), null);

		if (endpoint.getUriXSLTInput() != null
				&& endpoint.getTransformerForInput() != null) {
			LOG.trace("Creando transformer para input de XSLT");
			Transformer transformer = TransformerFactory.newInstance()
					.newTransformer(
							uriResolver.resolve(endpoint.getUriXSLTInput(),
									null));
			// omito la declaracion de XML en la salida
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION,
					"yes");
			endpoint.setTransformerForInput(transformer);
		}

		if (endpoint.getProviderType().equals(N4Endpoint.FILTER_PROVIDER)) {

			String httpQuery = "";

			if (endpoint.getUriMapInput() != null) {

				Object body = exchange.getIn().getBody();
				MapperURITransformForFilter inputTransformer = new MapperURITransformForFilter();
				httpQuery = inputTransformer.transformURI(
						endpoint.getUriMapInput(), body);

			} else if (endpoint.getUriXSLTInput() != null) {

				if ((exchange.getIn().getBody() instanceof List)) {
					throw new IllegalArgumentException(
							"Como cuerpo del mensaje existe una lista pero no existe mapa de conversion en la property uriMapInput ");
				}

				// TODO chequear que si viene en modo POJO no se puede hacer
				StringWriter writer = new StringWriter();
				Source body = exchange.getIn().getBody(Source.class);
				endpoint.getTransformerForInput().transform(body,
						new StreamResult(writer));
				httpQuery = writer.toString();
				LOG.debug("La query luego de la transformacion XSLT es {0}",
						httpQuery);
			}

			// TODO VER QUÉ USAR PARA CREAR LA URL CON UN BUILDER
			// FIXME CAMBIAR LOS VALORES DE SCOPE POR PROPERTIES DEL SISTEMA
			String url = new StringBuffer(endpoint.getN4EndpointURI())
					.append("?")
					.append("filtername=")
					.append(endpoint.getFilterName())
					.append("&")
					.append(httpQuery)
					.append("&operatorId=ICT&complexId=TPL&facilityId=PLP&yardId=ZPB")
					.toString();

			LOG.debug(
					"Se invocará a la URL {0} con el usuario {1} y password {2}",
					url, endpoint.getN4User(), endpoint.getN4Pass());

			HttpClient client = createHttpClient();

			// Create a method instance.
			GetMethod method = new GetMethod(url);

			resultBody = invokeHttp(url, client, method);

			// TODO hacer la transformación
			if (endpoint.getUriXSLTOutput() != null) {

				if (endpoint.getTransformerForOutput() == null) {

					LOG.trace("Creando transformer para output de XSLT");
					Transformer transformer = TransformerFactory.newInstance()
							.newTransformer(
									uriResolver.resolve(
											endpoint.getUriXSLTOutput(), null));
					transformer.setOutputProperty(
							OutputKeys.OMIT_XML_DECLARATION, "yes");
					endpoint.setTransformerForOutput(transformer);
				}

				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				endpoint.getTransformerForOutput().transform(
						new StreamSource((InputStream) resultBody),
						new StreamResult(outputStream));

				resultBody = new ByteArrayInputStream(
						outputStream.toByteArray());

				// TODO chequear que el defaultCharset este bien
				LOG.debug(
						"Resultado de la transformacion XSLT de salida: {0}",
						new String(outputStream.toByteArray(), Charset
								.defaultCharset()));

			}// end if de transformacion XSLT output

			if (endpoint.getClassSerialization() != null) {
				JAXBElement<?> element = endpoint
						.getJaxbContext()
						.createUnmarshaller()
						.unmarshal(new StreamSource((InputStream) resultBody),
								Class.forName(endpoint.getClassSerialization()));

				Object value = element.getValue();

				resultBody = value;

			}// end if de serializacion

		} else if (endpoint.getProviderType() == N4Endpoint.GENERIC_WS_PROVIDER) {
			// TODO hacer todo el procesamiento para el ws generico
		}

		exchange.getOut().setBody(resultBody);

	}

	private InputStream invokeHttp(String url, HttpClient client,
			GetMethod method) throws HttpOperationFailedException, IOException {
		try {

			// Execute the method.
			int statusCode = client.executeMethod(method);

			// Read the response body.
			byte[] responseBody = method.getResponseBody();

			// IDK que hacer con esto
			Header[] responseHeaders = method.getResponseHeaders();

			if (statusCode != HttpStatus.SC_OK) {
				LOG.error("Method failed: {0}", method.getStatusLine());
				throw new HttpOperationFailedException(url, statusCode, null,
						null, null, new String(responseBody));
			}

			// Deal with the response.
			// Use caution: ensure correct character encoding and is not
			// binary data
			LOG.debug("Respuesta con codigo {0} de la invocacion HTTP {1}",
					statusCode, new String(responseBody));

			method.releaseConnection();

			return new ByteArrayInputStream(responseBody);

		} catch (HttpException e) {
			LOG.error("Fatal protocol violation: " + e.getMessage(), e);
			throw e;
		} catch (IOException e) {
			LOG.error("Fatal transport error: " + e.getMessage(), e);
			throw e;
		}

	}

	private HttpClient createHttpClient() {

		UsernamePasswordCredentials creds = new UsernamePasswordCredentials(
				endpoint.getN4User(), endpoint.getN4Pass());

		HttpClient client = new HttpClient();
		List<String> authPrefs = new ArrayList<String>(2);
		authPrefs.add(AuthPolicy.BASIC);
		// This will exclude the NTLM authentication scheme
		client.getParams().setParameter(AuthPolicy.AUTH_SCHEME_PRIORITY,
				authPrefs);
		client.getParams().setParameter("http.connection.timeout",
				new Integer(2000));
		client.getParams().setParameter("http.socket.timeout",
				new Integer(2000));
		client.getParams().setAuthenticationPreemptive(true);
		client.getState().setCredentials(AuthScope.ANY, creds);

		return client;
	}

}
