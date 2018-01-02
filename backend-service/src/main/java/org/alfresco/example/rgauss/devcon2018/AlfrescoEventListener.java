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

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Listens for Alfresco events from the Event Gateway and when a relevant event is encountered
 * sends the content for image tag recognition and persists the results.
 */
public class AlfrescoEventListener
{
    private static final ClassLoader loader = Application.class.getClassLoader();

    @Autowired
    private ImageRecognitionParser parser;

    @Autowired
    private RecognitionTagResultRepository repository;

    public void start()
    {
        // TODO
    }

    /**
     * Processes an event
     * 
     * @param event
     * @throws Exception
     */
    public void onReceive(ContentEvent event) throws Exception
    {
        if (event == null)
        {
            return;
        }
        InputStream inputStream = getInputStream(event.getContentUri());
        
        List<RecognitionTagResult> results = parser.parse(event.getContentId(), inputStream);
        
        repository.save(results);
    }

    /**
     * Gets an input stream based on the given content URI
     * 
     * @param contentUri
     * @return the input stream
     */
    protected InputStream getInputStream(String contentUri)
    {
        if (contentUri == null)
        {
            return null;
        }
        if (contentUri.startsWith("classpath:"))
        {
            String path = contentUri.substring(10, contentUri.length());
            return loader.getResourceAsStream(path);
        }
        return null;
    }
}
