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
package org.alfresco.example.rgauss.devcon2018;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.http.HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN;
import static org.springframework.http.HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD;
import static org.springframework.http.HttpHeaders.ORIGIN;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.spi.DataFormat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class RecognitionTagResultControllerTest
{
    @Autowired
    private MockMvc mvc;

    @Autowired
    private CamelContext camelContext;

    @Autowired
    private ProducerTemplate producerTemplate;

    @Autowired
    private DataFormat dataFormat;

    private static final String EXPECTED_TAG = "horizontal bar";
    private static final String ORIGIN_URL = "http://localhost:4200";
    
    private static final String ENDPOINT_TEST_EVENT_FROM = "direct:alfresco.event.test";
    private static final String EVENT_CONTENTPUT = "{\"@class\":\"org.alfresco.events.types.NodeContentPutEvent\""
            + ",\"id\":\"3ea23b92-b1a1-4eed-b9fa-a85faa63822d\""
            + ",\"type\":\"CONTENTPUT\""
            + ",\"username\":\"admin\""
            + ",\"timestamp\":1515203787325"
            + ",\"seqNumber\":1"
            + ",\"txnId\":\"e1459b0c-a9ca-430b-ae55-b5d4f7abd2d0\""
            + ",\"networkId\":\"\""
            + ",\"client\":null"
            + ",\"nodeId\":\"classpath:testJPEG.jpg\""
            + ",\"siteId\":\"swsdp\""
            + ",\"nodeType\":\"cm:content\""
            + ",\"name\":\"testJPEG.jpg\""
            + ",\"nodeModificationTime\":1515203787238"
            + ",\"paths\":[\"java.util.ArrayList\",[\"/Company Home/Sites/swsdp/documentLibrary/testJPEG.jpg\"]]"
            + ",\"parentNodeIds\":[\"java.util.ArrayList\",[[\"java.util.ArrayList\",[\"8f2105b4-daaf-4874-9e8a-2152569d109b\",\"b4cff62a-664d-4d45-9302-98723eac1319\",\"28dbb8d8-9f3d-4b23-91f5-2ad3bab89a1e\",\"0bb7e4e8-5978-4ec4-bfa7-f63b41a8c263\",\"5df7c3f5-2256-4b8e-ab28-c46839390cce\"]]]]"
            + ",\"aspects\":[\"java.util.HashSet\",[\"sys:localized\",\"sys:referenceable\",\"cm:auditable\"]]"
            + ",\"nodeProperties\":{\"@class\":\"java.util.HashMap\",\"NODE_IS_CLASSIFIED\":false}"
            + ",\"size\":10289"
            + ",\"mimeType\":\"image/jpeg\""
            + ",\"encoding\":\"UTF-8\"}\n";
    

    @Test
    public void testHelloAPI() throws Exception
    {
        // Setup our routes
        camelContext.addRoutes(new RouteBuilder() {
            public void configure() {
                from(ENDPOINT_TEST_EVENT_FROM)          // in-memory
                .unmarshal(dataFormat)                    // unmarshal into POJO
                .to("bean:alfrescoNodeEventConsumer"); // route to the configured AMQP topic
            }
        });
        
        // Inject an event
        producerTemplate.sendBody(ENDPOINT_TEST_EVENT_FROM, EVENT_CONTENTPUT);

        // Retrieve the results
        mvc.perform(MockMvcRequestBuilders.get("/recognition-results").accept(MediaType.APPLICATION_JSON)
                .header(ACCESS_CONTROL_REQUEST_METHOD, GET).header(ORIGIN, ORIGIN_URL)).andExpect(status().isOk())
                .andExpect(content().string(containsString(EXPECTED_TAG)))
                .andExpect(header().string(ACCESS_CONTROL_ALLOW_ORIGIN, ORIGIN_URL));
    }
}
