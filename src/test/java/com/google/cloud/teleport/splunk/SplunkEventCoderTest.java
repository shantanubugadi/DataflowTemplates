/*
 * Copyright (C) 2019 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.google.cloud.teleport.splunk;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import com.google.gson.JsonObject;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.junit.Test;

/** Unit tests for {@link com.google.cloud.teleport.splunk.SplunkEventCoder} class. */
public class SplunkEventCoderTest {

  /**
   * Test whether {@link SplunkEventCoder} is able to encode/decode a {@link SplunkEvent}
   * correctly.
   * @throws IOException
   */
  @Test
  public void testEncodeDecode() throws IOException {

    String event = "test-event";
    String host = "test-host";
    String index = "test-index";
    String source = "test-source";
    String sourceType = "test-source-type";
    Long time = 123456789L;

    SplunkEvent actualEvent =
        SplunkEvent.newBuilder()
            .withEvent(event)
            .withHost(host)
            .withIndex(index)
            .withSource(source)
            .withSourceType(sourceType)
            .withTime(time)
            .build();

    SplunkEventCoder coder = SplunkEventCoder.of();
    try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
      coder.encode(actualEvent, bos);
      try (ByteArrayInputStream bin = new ByteArrayInputStream(bos.toByteArray())) {
        SplunkEvent decodedEvent = coder.decode(bin);
        assertThat(decodedEvent, is(equalTo(actualEvent)));
      }
    }
  }
  
  /**
   * Test whether {@link SplunkEventCoder} is able to encode/decode a {@link SplunkEvent}
   * with metadata 'fields'.
   * @throws IOException
   */
  @Test
  public void testEncodeDecodeFields() throws IOException {
    
    String event = "test-event";
    JsonObject fields = new JsonObject();
    fields.addProperty("test-key", "test-value");
    
    SplunkEvent actualEvent =
        SplunkEvent.newBuilder()
            .withEvent(event)
            .withFields(fields)
            .build();
    
    SplunkEventCoder coder = SplunkEventCoder.of();
    try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
      coder.encode(actualEvent, bos);
      try (ByteArrayInputStream bin = new ByteArrayInputStream(bos.toByteArray())) {
        SplunkEvent decodedEvent = coder.decode(bin);
        assertThat(decodedEvent, is(equalTo(actualEvent)));
      }
    }
  }
}
