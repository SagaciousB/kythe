/*
 * Copyright 2017 Google Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.devtools.kythe.analyzers.base;

import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import junit.framework.TestCase;
import com.google.devtools.kythe.proto.Storage.Entry;
import com.google.devtools.kythe.proto.Storage.VName;

/** Tests {@link StreamFactEmitter} */
public class StreamFactEmitterTest extends TestCase {
  private StreamFactEmitter emitter;
  private ByteArrayOutputStream outputStream;

  public void setUp() {
    this.outputStream = new ByteArrayOutputStream();
    this.emitter = new StreamFactEmitter(this.outputStream);
  }

  public void testEmitEntry() throws Exception {
    VName testVName = VName.newBuilder().setSignature("testSignature").build();
    String testFactName = "testFactName";
    String testFactValue = "testFactValue";
    this.emitter.emit(testVName, null, null, testFactName, testFactValue.getBytes());
    this.outputStream.flush();

    ByteArrayInputStream inputStream = new ByteArrayInputStream(this.outputStream.toByteArray());
    Entry.Builder builder = Entry.newBuilder();
    builder.mergeDelimitedFrom(inputStream);
    Entry entry = builder.build();

    assertNotNull(entry);
    assertEquals(entry.getSource(), testVName);
    assertEquals(entry.getFactName(), testFactName);
    assertEquals(entry.getFactValue().toString(Charset.defaultCharset()), testFactValue);
  }
}
