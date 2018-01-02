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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ImageRecognitionParserTest
{
    private static final Log logger = LogFactory.getLog(ImageRecognitionParserTest.class);
    
    private static final ClassLoader loader = Application.class.getClassLoader();

    @Autowired
    private ImageRecognitionParser imageParser;
    
    @Test
    public void testImageParse() throws Exception
    {
        String resourcePath = "testJPEG.jpg";
        String expectedTags[] = new String[] { "horizontal bar", "high bar" };
        try (InputStream inputStream = loader.getResourceAsStream(resourcePath)) {
            List<RecognitionTagResult> results = imageParser.parse(resourcePath, inputStream);
            logger.info("results=" + results);
            for (int i = 0; i < expectedTags.length; i++)
            {
                Assert.assertTrue(isValueInResults(expectedTags[i], results));
            }
        }
    }
    
    protected boolean isValueInResults(String value, List<RecognitionTagResult> results)
    {
        if (value == null || results == null)
        {
            return false;
        }
        for (RecognitionTagResult recognitionTagResult : results)
        {
            if (value.equals(recognitionTagResult.getValue()))
            {
                return true;
            }
        }
        return false;
    }
}
