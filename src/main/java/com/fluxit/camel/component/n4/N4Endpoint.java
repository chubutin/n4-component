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

import org.apache.camel.Consumer;
import org.apache.camel.FailedToCreateConsumerException;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.api.management.ManagedResource;
import org.apache.camel.impl.DefaultEndpoint;
import org.apache.camel.spi.UriEndpoint;
import org.apache.camel.spi.UriParam;

import com.fluxit.camel.component.n4.exception.InvalidParameterException;

/**
 * Representa un endpoint para la utilizacion de N4 dentro de Camel
 * 
 * @author chubutin
 */
@ManagedResource(description = "Managed N4Endpoint")
@UriEndpoint(scheme = "n4")
public class N4Endpoint extends DefaultEndpoint {

	public final String FILTER_PROVIDER = "FILTER_PROVIDER";
	public final String GENERIC_WS_PROVIDER = "WS_PROVIDER";

	@UriParam
	private String n4EndpointURI;

	@UriParam
	private String classSerialization;

	@UriParam
	// tipo de invocacion a n4. Por defecto se invocara a un filtro
	private String providerType = FILTER_PROVIDER;

	public N4Endpoint() {
	}

	public N4Endpoint(String uri, N4Component component) {
		super(uri, component);
	}

	public Producer createProducer() throws Exception {
		return new N4Producer(this);
	}

	public Consumer createConsumer(Processor processor) throws Exception {
		throw new FailedToCreateConsumerException(
				"No se pueden crear endpoints consumidores de N4 con la URI",
				null);
	}

	public boolean isSingleton() {
		return true;
	}

	public String getN4EndpointURI() {
		return n4EndpointURI;
	}

	public void setN4EndpointURI(String n4EndpointURI) {
		this.n4EndpointURI = n4EndpointURI;
	}

	public String getClassSerialization() {
		return classSerialization;
	}

	public void setClassSerialization(String classSerialization) {
		this.classSerialization = classSerialization;
	}

	public String getProviderType() {
		return providerType;
	}

	public void setProviderType(String providerType) {
		if (providerType.equalsIgnoreCase(FILTER_PROVIDER)
				|| providerType.equalsIgnoreCase(GENERIC_WS_PROVIDER)) {
			this.providerType = providerType;
		} else {
			throw new InvalidParameterException("providerType", providerType);
		}
	}

}
