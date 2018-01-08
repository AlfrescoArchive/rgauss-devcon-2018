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

import org.alfresco.events.types.BasicNodeEvent;

/**
 * Defines a listener for Alfresco node events from the Event Gateway
 */
public interface AlfrescoNodeEventConsumer
{
    /**
     * Processes a node event
     * 
     * @param event
     * @throws Exception
     */
    public void onReceive(BasicNodeEvent event) throws Exception;
}
