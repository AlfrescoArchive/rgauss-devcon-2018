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
    private AlfrescoEventListener eventListener;
    
    @Autowired
    private MockMvc mvc;

    private String EXPECTED_TAG = "horizontal bar";
    private String ORIGIN_URL = "http://localhost:4200";

    @Test
    public void testHelloAPI() throws Exception
    {
        // Inject an event
        ContentEvent event = new ContentEvent();
        event.setContentId("testJPEG.jpg");
        event.setContentUri("classpath:testJPEG.jpg");
        eventListener.onReceive(event);

        // Retrieve the results
        mvc.perform(MockMvcRequestBuilders.get("/recognition-results").accept(MediaType.APPLICATION_JSON)
                .header(ACCESS_CONTROL_REQUEST_METHOD, GET).header(ORIGIN, ORIGIN_URL)).andExpect(status().isOk())
                .andExpect(content().string(containsString(EXPECTED_TAG)))
                .andExpect(header().string(ACCESS_CONTROL_ALLOW_ORIGIN, ORIGIN_URL));
    }
}
