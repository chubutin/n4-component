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
import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.impl.DefaultComponent;
import org.apache.camel.impl.DefaultExchange;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

import com.fluxit.camel.component.n4.N4Endpoint;
import com.tecplata.esb.entities.Container;

public class N4RouteTest extends CamelTestSupport {

	private String FILTER_PROVIDER = N4Endpoint.FILTER_PROVIDER;
//	private String HTTP_URI = "http://192.168.16.201:10080/apex/api/query";
	private String HTTP_URI = "http://localhost:80/container.xml";
	private String CLASS_SERIALIZATION = Container.class.getName();
	private String INPUT_MAP = "files/mapParameter/URI/mapParameter.properties";
	private String INPUT_TRANSFORMATION = "files/xslt/INPUT/inputTransformation.xsl";
	private String OUTPUT_TRANSFORMATION = "files/xslt/INPUT/outputTransformation.xsl";
	private String INPUT_TRANSFORMATION_FAIL = "files/xslt/INPUT/inputTransformationFAIL.xsl";
	private String FILTER_NAME = "PARM_UNIT";

	@Produce(uri = "direct:startProcess")
	ProducerTemplate template;

	@Test
	public void testSearchContainer() throws Exception {
		MockEndpoint mock = getMockEndpoint("mock:result");
		mock.expectedMinimumMessageCount(1);

		List<String> body = new ArrayList<>();
		body.add("PROB1111111");
		
		DefaultExchange exchange = new DefaultExchange(context);
		exchange.getIn().setBody(body);
		
		Exchange exchangeOut = template.send(exchange);
		
		Container container = exchange.getIn().getBody(Container.class);
		
		assertMockEndpointsSatisfied();
		
		
	}


	@Override
	protected RouteBuilder createRouteBuilder() throws Exception {
		return new RouteBuilder() {

			public void configure() {
				from("direct:startProcess")
						.to(MessageFormat
								.format("n4:holaMundo?n4EndpointURI={0}&classSerialization={1}&providerType={2}&uriMapInput={3}&filterName={5}&uriXSLTOutput={6}",
										HTTP_URI, CLASS_SERIALIZATION,
										FILTER_PROVIDER, INPUT_MAP,
										INPUT_TRANSFORMATION, FILTER_NAME, OUTPUT_TRANSFORMATION))
						.to("mock:result");
			}
		};
	}
}
