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

import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.impl.DefaultComponent;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

import com.fluxit.camel.component.n4.N4Endpoint;

public class N4ComponentTest extends CamelTestSupport {

	private String FILTER_PROVIDER = N4Endpoint.FILTER_PROVIDER;
	private String HTTP_URI = "http://localhost:8080/rest";
	private String CLASS_SERIALIZATION = DefaultComponent.class.getName();

	@Produce(uri = "direct:startProcess")
	ProducerTemplate template;

	@Test
	public void testHelloWorld() throws Exception {
		MockEndpoint mock = getMockEndpoint("mock:result");
		mock.expectedMinimumMessageCount(1);

		template.sendBody("Hola mundo!");

		assertMockEndpointsSatisfied();
	}

	@Override
	protected RouteBuilder createRouteBuilder() throws Exception {
		return new RouteBuilder() {

			public void configure() {
				from("direct:startProcess")
				.to(MessageFormat
					.format("n4:holaMundo?n4EndpointURI={0}&classSerialization={1}&providerType={2}",
							HTTP_URI, CLASS_SERIALIZATION, FILTER_PROVIDER))
				.to("mock:result");
			}
		};
	}
}
