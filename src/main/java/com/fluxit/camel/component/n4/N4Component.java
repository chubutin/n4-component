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

import java.util.Map;

import org.apache.camel.Endpoint;
import org.apache.camel.impl.DefaultComponent;
import org.apache.camel.util.ObjectHelper;
import org.apache.commons.validator.routines.UrlValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents the component that manages {@link N4Endpoint}.
 * 
 * @author chubutin
 * 
 */
public class N4Component extends DefaultComponent {

	private static final transient Logger LOG = LoggerFactory.getLogger(N4Component.class);

	protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters)
			throws Exception {

		N4Endpoint endpoint = new N4Endpoint(uri, this);
		LOG.debug("Creando el endpoint de la clase {0} con la URI {1} y los parametros {2} ",
				endpoint.getClass(), uri, parameters);
		setProperties(endpoint, parameters);
		
		validateRequiredProperties(endpoint);
		
		return endpoint;
	}

	private void validateRequiredProperties(N4Endpoint endpoint) throws Exception {
		
		if(ObjectHelper.isEmpty(endpoint.getN4EndpointURI())){
			throw new IllegalArgumentException("La propiedad n4EndpointURI no puede ser nula");
		}else{
			UrlValidator urlValidator = new UrlValidator(UrlValidator.ALLOW_LOCAL_URLS);
			if(!urlValidator.isValid(endpoint.getN4EndpointURI())){
				throw new IllegalArgumentException("La URL del provider no es valida, corregir la propiedad n4EndpointURI");
			}
		}
		
	}
}
