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

import org.apache.pulsar.client.api.PulsarClientException;
import org.apache.pulsar.common.classification.InterfaceAudience;
import org.apache.pulsar.common.classification.InterfaceStability;

import java.io.Closeable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * A batch chunk reader can be used to scan through all the messages currently available in a topic
 * without going through broker.
 */
@InterfaceAudience.Public
@InterfaceStability.Unstable
public interface ChunkReader<T> extends Closeable {
    /**
     * @return the topic from which this reader is reading from
     */
    String getTopic();

    /**
     * Read the next chunk of messages in the topic.
     *
     * <p>This method will never block and returns a empty MessageChunk at EOF
     *
     * @return the next chunk of messages
     * @throws PulsarClientException
     */
    MessageChunk<T> readNext() throws PulsarClientException;

    /**
     * Read the next message in the topic waiting for a maximum time.
     *
     * <p>Returns null if no message is received before the timeout.
     *
     * @return the next message(Could be null if none received in time)
     * @throws PulsarClientException
     */
    MessageChunk<T> readNext(int timeout, TimeUnit unit) throws PulsarClientException;

    /**
     * Read asynchronously the next message in the topic.
     *
     * <p>{@code readNextAsync()} should be called subsequently once returned {@code CompletableFuture} gets complete
     * with received message. Else it creates <i> backlog of receive requests </i> in the application.
     *
     * <p>The returned future can be cancelled before completion by calling {@code .cancel(false)}
     * ({@link CompletableFuture#cancel(boolean)}) to remove it from the the backlog of receive requests. Another
     * choice for ensuring a proper clean up of the returned future is to use the CompletableFuture.orTimeout method
     * which is available on JDK9+. That would remove it from the backlog of receive requests if receiving exceeds
     * the timeout.
     *
     * @return a future that will yield a message (when it's available) or {@link PulsarClientException} if the reader
     *         is already closed.
     */
    CompletableFuture<MessageChunk<T>> readNextAsync();

    /**
     * Asynchronously close the reader and stop the broker to push more messages.
     *
     * @return a future that can be used to track the completion of the operation
     */
    CompletableFuture<Void> closeAsync();

    /**
     * Return true if the topic was terminated and this reader has reached the end of the topic.
     *
     * <p>Note that this only applies to a "terminated" topic (where the topic is "sealed" and no
     * more messages can be published) and not just that the reader is simply caught up with
     * the publishers. Use {@link #hasMessageAvailable()} to check for for that.
     */
    boolean hasReachedEndOfSplit();

    /**
     * Check if there is any message available to read from the current position.
     *
     * <p>This check can be used by an application to scan through a topic and stop
     * when the reader reaches the current last published message. For example:
     *
     * <pre>{@code
     * while (reader.hasMessageAvailable()) {
     *     Message<String> msg = reader.readNext();
     *     // Do something
     * }
     *
     * // Done reading
     * }</pre>
     *
     * <p>Note that this call might be blocking (see {@link #hasMessageAvailableAsync()} for async version) and
     * that even if this call returns true, that will not guarantee that a subsequent call to {@link #readNext()}
     * will not block.
     *
     * @return true if the are messages available to be read, false otherwise
     * @throws PulsarClientException if there was any error in the operation
     */
    boolean hasMessageAvailable() throws PulsarClientException;

    /**
     * Asynchronously check if there is any message available to read from the current position.
     *
     * <p>This check can be used by an application to scan through a topic and stop when the reader reaches the current
     * last published message.
     *
     * @return a future that will yield true if the are messages available to be read, false otherwise, or a
     *         {@link PulsarClientException} if there was any error in the operation
     */
    CompletableFuture<Boolean> hasMessageAvailableAsync();

    /**
     * Returns if the ChunkReader is bounded
     *
     * @return if the ChunkReader is bounded
     *         {@link PulsarClientException} if there was any error in the operation
     */
    boolean isBounded() throws PulsarClientException;
}
