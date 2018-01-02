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

import org.apache.tika.Tika;
import org.apache.tika.config.TikaConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Spring Boot Application class
 */
@SpringBootApplication
public class Application
{
    
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
    public CommandLineRunner init(AlfrescoEventListener eventListener)
    {
        return (args) -> {
            eventListener.start();
        };
    }
    
    @Bean
    public AlfrescoEventListener eventListener()
    {
        return new AlfrescoEventListener();
    }
    
    @Bean
    public ImageRecognitionParser imageParser()
    {
        return new ImageRecognitionParserTikaImpl();
    }
    
    @Bean
    public Tika tika() throws Exception
    {
        InputStream stream = loader.getResourceAsStream(tikaConfig);
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