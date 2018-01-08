/*
 * Copyright 2018 Alfresco Software, Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.alfresco.event.gateway;

import static org.junit.Assert.assertEquals;

import java.util.UUID;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.spi.DataFormat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GatewayRouteKafkaTest
{
    @Autowired
    private CamelContext camelContext;
    
    @Autowired
    private ProducerTemplate producerTemplate;

    @Autowired
    private DataFormat dataFormat;
    
    @Autowired
    private TestEventConsumer testEventConsumer;
    
    public static final String ENDPOINT_TEST_EVENT_FROM = "direct:alfresco.event.test";
    private static final String EXPECTED_VALUE_PREFIX = "Test Value";

    @Test
    public void testRoute() throws Exception
    {
        // Setup our routes
        camelContext.addRoutes(new RouteBuilder() {
            public void configure() {
                from(ENDPOINT_TEST_EVENT_FROM)          // in-memory
                .marshal(dataFormat)                    // marshal into JSON
                .to("amqp:topic:{{route.from.topic}}"); // route to the configured AMQP topic

                from("kafka:{{kafka.host}}:{{kafka.port}}"
                        + "?brokers={{kafka.host}}:{{kafka.port}}"
                        + "&topic={{route.to.topic}}")
                .unmarshal(dataFormat)
                .to("bean:testEventConsumer");
            }
        });

        // Generate a test event
        String expectedValue = EXPECTED_VALUE_PREFIX + " " + UUID.randomUUID();
        TestEvent event = new TestEvent(expectedValue);

        // Publish our event to in-memory endpoint which gets routed to AMQP
        producerTemplate.sendBody(ENDPOINT_TEST_EVENT_FROM, event);

        // Hack to just wait a bit for routing
        Thread.sleep(2000);

        // Check that our consumer received the expected value
        assertEquals(expectedValue, testEventConsumer.getLastValue());
    }
}
