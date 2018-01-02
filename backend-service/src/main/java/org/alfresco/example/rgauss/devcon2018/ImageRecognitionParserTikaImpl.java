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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tika.Tika;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.recognition.ObjectRecognitionParser;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Calls Tika to parse a given content input stream and return the recognition tag results
 */
public class ImageRecognitionParserTikaImpl implements ImageRecognitionParser
{
    @Autowired
    private Tika tika;

    public List<RecognitionTagResult> parse(String contentId, InputStream inputStream) throws Exception
    {
        try
        {
            List<RecognitionTagResult> recognitionTagResults = new ArrayList<RecognitionTagResult>();
            Metadata metadata = new Metadata();
            
            // Call Tika to parse (which passes to TensorFlow)
            tika.parse(inputStream, metadata);
            
            // Convert the Tika results into our POJO 
            String[] results = metadata.getValues(ObjectRecognitionParser.MD_KEY_OBJ_REC);
            for (int i = 0; i < results.length; i++)
            {
                String[] resultParts = results[i].split("\\(");
                String[] tags = resultParts[0].split(", ");
                Float confidence = Float.valueOf(resultParts[1].substring(0, resultParts[1].length() - 1));
                for (int j = 0; j < tags.length; j++)
                {
                    recognitionTagResults.add(
                            new RecognitionTagResult(contentId, tags[j].trim(), confidence, new Date()));
                }
            }
            return recognitionTagResults;
        }
        finally
        {
            if (inputStream != null)
            {
                inputStream.close();
            }
        }
    }
}
