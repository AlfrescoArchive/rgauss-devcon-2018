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

import java.net.URI;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * REST client for Alfresco Process Services
 */
public class AlfrescoProcessServicesRestClient
{
    private String baseUrl;
    private RestTemplate restTemplate;
    
    public AlfrescoProcessServicesRestClient(RestTemplate restTemplate, String baseUrl)
    {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    public void startProcessInstance(String processDefinitionId, String nodeId, 
            List<RecognitionTagResult> results) throws Exception
    {
        if (StringUtils.isEmpty(processDefinitionId) || StringUtils.isEmpty(nodeId))
        {
            throw new Exception("processDefinitionId and nodeId must not be null");
        }
        URI targetUrl = UriComponentsBuilder.fromUriString(baseUrl).path("process-instances")
            .build().toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        JSONObject json = new JSONObject();
        json.put("processDefinitionId", processDefinitionId);
        JSONObject jsonValues = new JSONObject();
        
        jsonValues.put("alfrescocontentnodeid", nodeId);
        // Process definition currently only takes 3 tags, obviously this could be optimized
        int maxTags = 3;
        int i = 1;
        for (RecognitionTagResult result : results)
        {
            if (i <= maxTags);
            jsonValues.put("tag" + i, result.getValue());
            i++;
        }
        json.put("values", jsonValues);
        HttpEntity<String> entity = new HttpEntity<String>(json.toString(), headers);
        
        ResponseEntity<String> response = restTemplate.exchange(
                targetUrl, HttpMethod.POST, entity, String.class);
        if (response.getStatusCode() != HttpStatus.OK)
        {
            throw new Exception("Could not start process instance: " + response.getBody());
        }
    }
}
