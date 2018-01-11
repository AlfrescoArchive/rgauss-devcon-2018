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
package org.alfresco.example.rgauss.devcon2018;

import java.io.InputStream;
import java.util.List;

import org.alfresco.events.types.NodeContentPutEvent;
import org.alfresco.events.types.RepositoryEvent;
import org.apache.camel.Handler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Listens for Alfresco events from the Event Gateway and when a relevant event is encountered
 * sends the content for image tag recognition and persists the results.
 */
public class AlfrescoRepositoryEventConsumerImpl implements AlfrescoRepositoryEventConsumer
{
    private static final Log logger = LogFactory.getLog(AlfrescoRepositoryEventConsumerImpl.class);

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
    public void onReceive(RepositoryEvent event) throws Exception
    {
        if (event == null)
        {
            return;
        }

        logger.info("Received event: " + event.toString());

        if (NodeContentPutEvent.EVENT_TYPE.equals(event.getType()))
        {
            NodeContentPutEvent nodeEvent = (NodeContentPutEvent) event;
            String nodeId = nodeEvent.getNodeId();
            InputStream inputStream = getInputStream(nodeEvent.getNodeId());

            if (inputStream == null)
            {
                logger.error("Could not obtain input stream for node " + nodeId);
                return;
            }

            List<RecognitionTagResult> results = parser.parse(nodeEvent.getNodeId(), inputStream);
            repository.save(results);
            
            // TODO start process instance with tags
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
