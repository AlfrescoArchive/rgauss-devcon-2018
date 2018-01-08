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

import java.io.InputStream;
import java.util.List;

import org.alfresco.events.types.BasicNodeEvent;
import org.alfresco.events.types.NodeContentPutEvent;
import org.apache.camel.Handler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Listens for Alfresco events from the Event Gateway and when a relevant event is encountered
 * sends the content for image tag recognition and persists the results.
 */
public class AlfrescoNodeEventConsumerImpl implements AlfrescoNodeEventConsumer
{
    private static final Log logger = LogFactory.getLog(AlfrescoNodeEventConsumerImpl.class);

    private static final ClassLoader loader = Application.class.getClassLoader();

    @Autowired
    private ImageRecognitionParser parser;

    @Autowired
    private RecognitionTagResultRepository repository;

    /**
     * Processes an event
     * 
     * @param event
     * @throws Exception
     */
    @Handler
    public void onReceive(BasicNodeEvent event) throws Exception
    {
        if (event == null)
        {
            return;
        }

        logger.info("Received event: " + event.toString());

        if (NodeContentPutEvent.EVENT_TYPE.equals(event.getType()))
        {
            String nodeId = event.getNodeId();
            InputStream inputStream = getInputStream(event.getNodeId());

            if (inputStream == null)
            {
                logger.error("Could not obtain input stream for node " + nodeId);
                return;
            }

            List<RecognitionTagResult> results = parser.parse(event.getNodeId(), inputStream);
            repository.save(results);
        }
    }

    /**
     * Gets an input stream based on the given content URI
     * 
     * @param contentUri
     * @return the input stream
     */
    protected InputStream getInputStream(String nodeId)
    {
        if (nodeId == null)
        {
            return null;
        }
        if (nodeId.startsWith("classpath:"))
        {
            String path = nodeId.substring(10, nodeId.length());
            return loader.getResourceAsStream(path);
        }
        // TODO get the content from the Alfresco REST API
        
        return null;
    }
}
