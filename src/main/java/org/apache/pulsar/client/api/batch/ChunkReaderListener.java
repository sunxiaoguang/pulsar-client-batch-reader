/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.pulsar.client.api.batch;

import org.apache.pulsar.common.classification.InterfaceAudience;
import org.apache.pulsar.common.classification.InterfaceStability;

import java.io.Serializable;

/**
 * A listener that will be called in order for chunk of message received.
 */
@InterfaceAudience.Public
@InterfaceStability.Unstable
public interface ChunkReaderListener<T> extends Serializable {
    /**
     * This method is called whenever a new chunk of messages are received.
     *
     * <p>Application is responsible of handling any exception that could be thrown while processing the message.
     *
     * @param reader
     *            the ChunkReader object from where the messages was received
     * @param chunk
     *            the message chunk
     */
    void received(ChunkReader<T> reader, MessageChunk<T> chunk);

    /**
     * Get the notification when a topic is terminated.
     *
     * @param reader
     *            the ChunkReader object associated with the terminated topic
     */
    default void reachedEndOfTopic(ChunkReader<T> reader) {
        // By default ignore the notification
    }
}
