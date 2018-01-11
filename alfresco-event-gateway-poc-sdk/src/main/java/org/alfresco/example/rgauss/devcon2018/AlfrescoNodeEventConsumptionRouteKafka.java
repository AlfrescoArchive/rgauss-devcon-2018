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

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.spi.DataFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile({"kafka", "default"})
public class AlfrescoNodeEventConsumptionRouteKafka extends RouteBuilder
{
    @Autowired
    private DataFormat dataFormat;
    
    @Override
    public void configure() throws Exception {
        from("kafka:{{kafka.topic.nodeEvents}}"
                + "?brokers={{kafka.host}}:{{kafka.port}}"
                + "&groupId={{kafka.groupId}}"
                + "&consumersCount={{kafka.consumersCount}}")
        .unmarshal(dataFormat)
        .to("bean:alfrescoRepositoryEventConsumer");
    }
}
