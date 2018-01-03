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
package org.alfresco.event.gateway;

import java.util.Date;

/**
 * Simple test event POJO
 */
public class TestEvent
{
    private Date datetime;
    private String value;
    
    public TestEvent()
    {
    }
    
    public TestEvent(String value)
    {
        this.value = value;
        this.datetime = new Date();
    }
    
    public Date getDatetime()
    {
        return datetime;
    }
    public void setDatetime(Date datetime)
    {
        this.datetime = datetime;
    }
    public String getValue()
    {
        return value;
    }
    public void setValue(String value)
    {
        this.value = value;
    }
}
