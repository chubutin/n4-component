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

import java.io.File;
import java.io.IOException;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;

import org.apache.camel.Exchange;
import org.apache.camel.RuntimeCamelException;
import org.apache.camel.builder.xml.XsltUriResolver;
import org.apache.camel.impl.DefaultProducer;
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

	public void process(Exchange exchange) throws Exception {
		LOG.debug("Exchange en N4 Producer: {0}", exchange.getIn().getBody());

		XsltUriResolver uriResolver = new XsltUriResolver(exchange.getContext()
				.getClassResolver(), null);

		Object body = exchange.getIn().getBody();

		if (endpoint.getUriXSLTInput() != null) {
			LOG.debug("Creando transformer para input de XSLT");
			endpoint.setTransformerForInput(TransformerFactory.newInstance()
					.newTransformer(
							uriResolver.resolve(endpoint.getUriXSLTInput(),
									null)));
		}
		if (endpoint.getUriXSLTOutput() != null) {
			LOG.debug("Creando transformer para output de XSLT");
			endpoint.setTransformerForOutput(TransformerFactory.newInstance()
					.newTransformer(
							uriResolver.resolve(endpoint.getUriXSLTOutput(),
									null)));
		}

		if (endpoint.getProviderType() == N4Endpoint.FILTER_PROVIDER) {

			if (endpoint.getUriMapInput() != null) {

				MapperURITransformForFilter inputTransformer = new MapperURITransformForFilter();

			} else if (endpoint.getUriXSLTInput() != null) {

				// Transformer transformer =
				// TransformerFactory.newInstance().newTransformer(xsl);

			}
		}

	}

}
