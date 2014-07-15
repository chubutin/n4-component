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
package com.fluxit.camel;

import java.text.MessageFormat;

import javax.xml.transform.TransformerException;

import org.apache.camel.FailedToCreateRouteException;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultProducerTemplate;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

import com.fluxit.camel.component.n4.N4Endpoint;

public class N4ComponentTest extends CamelTestSupport {

	private String FILTER_PROVIDER = N4Endpoint.FILTER_PROVIDER;
	private String HTTP_URI = "http://192.168.16.201:10080/apex/api/query";
	private String CLASS_SERIALIZATION = Integer.class.getName();
	private String INPUT_MAP = "files/mapParameter/URI/mapParameter.properties";
	private String INPUT_TRANSFORMATION = "files/xslt/INPUT/inputTransformation.xsl";
	private String OUTPUT_TRANSFORMATION = "files/xslt/INPUT/inputTransformation.xsl";
	private String INPUT_TRANSFORMATION_FAIL = "files/xslt/INPUT/inputTransformationFAIL.xsl";

	@Produce(uri = "direct:startProcess")
	ProducerTemplate template;

	@Test(expected = FailedToCreateRouteException.class)
	public void testFailMultipleInputs() throws Exception {

		context.addRoutes(new RouteBuilder(context) {

			@Override
			public void configure() throws Exception {
				from("direct:testFailMultipleInputs")
						.to(MessageFormat
								.format("n4:holaMundo?n4EndpointURI={0}&classSerialization={1}&providerType={2}&uriMapInput={3}&uriXSLTInput={4}",
										HTTP_URI, CLASS_SERIALIZATION,
										FILTER_PROVIDER, INPUT_MAP,
										INPUT_TRANSFORMATION));
			}
		});
	}

	@Test(expected = TransformerException.class)
	public void testFailXsltNotFound() throws Throwable {
		
		try {

		final String uri = "direct:testFailXsltNotFound";
		context.addRoutes(new RouteBuilder(context) {
			@Override
			public void configure() throws Exception {
				from(uri)
						.routeId("FailXsltNotFound")
						.to(MessageFormat
								.format("n4:holaMundo?n4EndpointURI={0}&classSerialization={1}&providerType={2}&uriXSLTInput={5}",
										HTTP_URI, CLASS_SERIALIZATION,
										FILTER_PROVIDER, INPUT_MAP,
										INPUT_TRANSFORMATION_FAIL))
						.to("mock:result");
			}
		});

		
			DefaultProducerTemplate template = DefaultProducerTemplate
					.newInstance(context, uri);
			template.start();
			template.sendBody("Hola mundo!");

		} catch (Exception e) {
			throw e.getCause().getCause();
		}

	}

}
