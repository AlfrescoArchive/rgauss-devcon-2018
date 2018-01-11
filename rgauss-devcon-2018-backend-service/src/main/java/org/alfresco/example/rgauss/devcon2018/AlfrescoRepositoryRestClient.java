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

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * REST client for the Alfresco repository
 */
public class AlfrescoRepositoryRestClient
{
    private String baseUrl;
    private RestTemplate restTemplate;
    
    public AlfrescoRepositoryRestClient(RestTemplate restTemplate, String baseUrl)
    {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    public InputStream getInputStream(String nodeId)
    {
        URI targetUrl = UriComponentsBuilder.fromUriString(baseUrl).path("nodes")
            .pathSegment(nodeId).pathSegment("content").build().toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        ResponseEntity<Resource> responseEntity = 
                restTemplate.exchange(targetUrl, HttpMethod.GET, null, Resource.class);

        try
        {
            return responseEntity.getBody().getInputStream();
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}
