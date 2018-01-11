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

import java.util.Date;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Represents a single tag recognition result
 */
@Entity
public class RecognitionTagResult
{
    @Id
    private String id;
    private String contentId;
    private String value;
    private Float confidence;
    private Date datetime;
    
    public RecognitionTagResult()
    {
        this.id = UUID.randomUUID().toString();
    }
    
    public RecognitionTagResult(String contentId, String value, Float confidence, Date datetime)
    {
        super();
        this.id = UUID.randomUUID().toString();
        this.contentId = contentId;
        this.value = value;
        this.confidence = confidence;
        this.datetime = datetime;
    }

    public String getId()
    {
        return id;
    }
    public void setId(String id)
    {
        this.id = id;
    }
    public String getContentId()
    {
        return contentId;
    }
    public void setContentId(String contentId)
    {
        this.contentId = contentId;
    }
    public String getValue()
    {
        return value;
    }
    public void setValue(String value)
    {
        this.value = value;
    }
    public Float getConfidence()
    {
        return confidence;
    }
    public void setConfidence(Float confidence)
    {
        this.confidence = confidence;
    }
    public Date getDatetime()
    {
        return datetime;
    }
    public void setDatetime(Date datetime)
    {
        this.datetime = datetime;
    }

    @Override
    public String toString()
    {
        return "RecognitionTagResult [id=" + id + ", contentId=" + contentId + ", value=" + value + ", confidence="
                + confidence + ", datetime=" + datetime + "]";
    }
}
