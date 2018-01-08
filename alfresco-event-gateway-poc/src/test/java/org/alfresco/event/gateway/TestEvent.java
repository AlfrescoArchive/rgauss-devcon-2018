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
