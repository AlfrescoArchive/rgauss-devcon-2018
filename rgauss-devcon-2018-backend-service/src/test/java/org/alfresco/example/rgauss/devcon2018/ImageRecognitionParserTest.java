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
