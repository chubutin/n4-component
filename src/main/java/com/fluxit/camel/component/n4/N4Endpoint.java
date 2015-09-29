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

import javax.xml.bind.JAXBContext;
import javax.xml.transform.Transformer;

import org.apache.camel.Consumer;
import org.apache.camel.FailedToCreateConsumerException;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.api.management.ManagedResource;
import org.apache.camel.impl.DefaultEndpoint;
import org.apache.camel.spi.UriEndpoint;
import org.apache.camel.spi.UriParam;

import com.fluxit.camel.component.n4.exception.InvalidParameterException;
import com.fluxit.camel.transformer.GroovyRequestTransformer;

/**
 * Representa un endpoint para la utilizacion de N4 dentro de Camel
 * 
 * @author chubutin
 */
@ManagedResource(description = "Managed N4Endpoint")
@UriEndpoint(scheme = "n4")
public class N4Endpoint extends DefaultEndpoint {

	public static final String FILTER_PROVIDER = "FILTER_PROVIDER";
	public static final String GENERIC_WS_PROVIDER = "WS_PROVIDER";

	private Transformer transformerForInput;
	private Transformer transformerForOutput;
	private JAXBContext jaxbContext;

	@UriParam
	protected String n4EndpointURI;

	@UriParam
	protected String classSerialization;

	@UriParam
	protected GroovyRequestTransformer groovyRequestBean;

	@UriParam
	protected String uriXSLTInput;

	@UriParam
	protected String uriMapInput;

	@UriParam
	protected String uriXSLTOutput;

	@UriParam
	private String n4User = "n4api";

	@UriParam
	private String n4Pass = "lookitup";
	
	@UriParam
	private String filterName;

	@UriParam
	// tipo de invocacion a n4. Por defecto se invocara a un filtro
	protected String providerType = FILTER_PROVIDER;

	public N4Endpoint() {
		super();
	}

	public N4Endpoint(String uri, N4Component component) {
		super(uri, component);
	}

	public Producer createProducer() throws Exception {
		return new N4Producer(this);
	}

	public Consumer createConsumer(Processor processor) throws Exception {
		throw new FailedToCreateConsumerException(
				"No se pueden crear endpoints consumidores de N4", null);
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
			this.providerType = providerType.toUpperCase();
		} else {
			throw new InvalidParameterException("providerType", providerType);
		}
	}

	public GroovyRequestTransformer getGroovyRequestBean() {
		return groovyRequestBean;
	}

	public void setGroovyRequestBean(GroovyRequestTransformer groovyRequestBean) {
		this.groovyRequestBean = groovyRequestBean;
	}

	public String getUriXSLTInput() {
		return uriXSLTInput;
	}

	public void setUriXSLTInput(String uriXSLTInput) {
		this.uriXSLTInput = uriXSLTInput;
	}

	public String getUriXSLTOutput() {
		return uriXSLTOutput;
	}

	public void setUriXSLTOutput(String uriXSLTOutput) {
		this.uriXSLTOutput = uriXSLTOutput;
	}

	public String getUriMapInput() {
		return uriMapInput;
	}

	public void setUriMapInput(String uriMapInput) {
		this.uriMapInput = uriMapInput;
	}

	public Transformer getTransformerForInput() {
		return transformerForInput;
	}

	public void setTransformerForInput(Transformer transformerForInput) {
		this.transformerForInput = transformerForInput;
	}

	public Transformer getTransformerForOutput() {
		return transformerForOutput;
	}

	public void setTransformerForOutput(Transformer transformerForOutput) {
		this.transformerForOutput = transformerForOutput;
	}

	public String getN4User() {
		return n4User;
	}

	public void setN4User(String n4User) {
		this.n4User = n4User;
	}

	public String getN4Pass() {
		return n4Pass;
	}

	public void setN4Pass(String n4Pass) {
		this.n4Pass = n4Pass;
	}

	public JAXBContext getJaxbContext() {
		return jaxbContext;
	}

	public void setJaxbContext(JAXBContext jaxbContext) {
		this.jaxbContext = jaxbContext;
	}

	public String getFilterName() {
		return filterName;
	}

	public void setFilterName(String filterName) {
		this.filterName = filterName;
	}

}
