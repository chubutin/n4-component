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

import java.text.MessageFormat;

import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author chubutin
 *
 */
public class N4Producer extends DefaultProducer {
	
    private static final transient Logger LOG = LoggerFactory.getLogger(N4Producer.class);
    
    private N4Endpoint endpoint;

    public N4Producer(N4Endpoint endpoint) {
        super(endpoint);
        this.endpoint = endpoint;
        LOG.debug(MessageFormat.format("Productor N4 creado con el endpoint {0}", this.endpoint));
        LOG.debug(MessageFormat.format("El tipo de invocacion a n4 será {0}", this.endpoint.getProviderType()));
    }

    public void process(Exchange exchange) throws Exception {
        LOG.debug(MessageFormat.format("Exchange en N4 Producer: {0}", exchange.getIn().getBody()));
        
        	
    }

}