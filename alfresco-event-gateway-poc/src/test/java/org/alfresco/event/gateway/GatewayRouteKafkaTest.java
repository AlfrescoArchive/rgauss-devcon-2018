/*
 * Copyright 2018 Alfresco Software, Ltd.
 *
 * This example is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This example is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this example. If not, see <http://www.gnu.org/licenses/>.
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
