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

import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tika.Tika;
import org.apache.tika.config.TikaConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Spring Boot Application class
 */
@SpringBootApplication
public class Application
{
    private static final Log logger = LogFactory.getLog(Application.class);

    @Value("${alfresco.repository.baseUrl}")
    private String alfrescoRepositoryBaseUrl;

    @Value("${alfresco.repository.username}")
    private String alfrescoRepositoryUsername;

    @Value("${alfresco.repository.password}")
    private String alfrescoRepositoryPassword;

    @Value("${tika.config}")
    private String tikaConfig;

    @Value("${cors.allowedOrigin}")
    private String corsAllowedOrigin;

    private static final ClassLoader loader = Application.class.getClassLoader();

    public static void main(String[] args)
    {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public AlfrescoRepositoryEventConsumer alfrescoRepositoryEventConsumer()
    {
        return new AlfrescoRepositoryEventConsumerImpl();
    }

    @Bean
    public AlfrescoRepositoryRestClient alfrescoRepositoryRestClient(RestTemplate restTemplate)
    {
        String alfrescoRepositoryRestUrl = alfrescoRepositoryBaseUrl + 
                "/api/-default-/public/alfresco/versions/1/";
        return new AlfrescoRepositoryRestClient(restTemplate, alfrescoRepositoryRestUrl);
    }

    @Bean
    public RestTemplate restTemplate()
    {
        RestTemplateBuilder builder = new RestTemplateBuilder();
        return builder.basicAuthorization(alfrescoRepositoryUsername, alfrescoRepositoryPassword).build();
    }

    @Bean
    public ImageRecognitionParser imageParser()
    {
        return new ImageRecognitionParserTikaImpl();
    }

    @Bean
    public Tika tika() throws Exception
    {
        if (tikaConfig == null)
        {
            throw new Exception("tika.config property must not be null");
        }
        logger.info("Loading TikaConfig from " + tikaConfig);
        InputStream stream = null;
        if (tikaConfig.startsWith("classpath:"))
        {
            stream = loader.getResourceAsStream(tikaConfig.split("classpath:")[1]);
        }
        else
        {
            stream = new FileInputStream(tikaConfig);
        }
        return new Tika(new TikaConfig(stream));
    }

    @Bean
    public WebMvcConfigurer corsConfigurer()
    {
        return new WebMvcConfigurerAdapter()
        {
            @Override
            public void addCorsMappings(CorsRegistry registry)
            {
                String[] originsArray = new String[] { corsAllowedOrigin };
                registry.addMapping("/recognition-results").allowedOrigins(originsArray);
                registry.addMapping("/recognition-results/*").allowedOrigins(originsArray).allowedMethods("GET");
            }
        };
    }
}