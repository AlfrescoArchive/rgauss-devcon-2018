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

import org.springframework.web.bind.annotation.RestController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * REST API controller for recognition results
 */
@RestController
@RequestMapping("/recognition-results")
public class RecognitionTagResultController
{
    @Autowired
    private RecognitionTagResultRepository recognitionTagResultRepository;

    @RequestMapping(path = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Iterable<RecognitionTagResult>> getRecognitionTagResults()
    {
        return new ResponseEntity<Iterable<RecognitionTagResult>>(
                recognitionTagResultRepository.findAll(), HttpStatus.OK);
    }
    
    @RequestMapping(path = "/{key}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RecognitionTagResult> getRecognitionTagResult(@PathVariable String key)
    {
        RecognitionTagResult recognitionTagResult = recognitionTagResultRepository.findOne(key);
        if (recognitionTagResult == null)
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<RecognitionTagResult>(recognitionTagResult, HttpStatus.OK);
    }

}
