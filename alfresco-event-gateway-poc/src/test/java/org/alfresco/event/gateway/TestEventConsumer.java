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

import org.apache.camel.Handler;

/**
 * Simple test event consumer that keeps track of the last event
 */
public class TestEventConsumer
{
    private String lastValue;

    public String getLastValue()
    {
        return lastValue;
    }

    @Handler
    public void onReceive(TestEvent event)
    {
        if (event == null)
        {
            return;
        }
        this.lastValue = event.getValue();
    }
}
