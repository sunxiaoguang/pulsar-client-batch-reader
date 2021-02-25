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

import java.io.Serializable;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * {@link ChunkSplitSourceBuilder} is used to configure and create instances of {@link ChunkSplitSource} for a topic
 */
@InterfaceAudience.Public
@InterfaceStability.Unstable
public interface ChunkSplitSourceBuilder<T> extends Cloneable {
    /**
     * Finalize the creation of the {@link ChunkSplitSource} instance.
     *
     * <p>This method will block until the source is created successfully or an exception is thrown.
     *
     * @return the reader instance
     * @throws PulsarClientException
     *             if the source creation fails
     */
    ChunkSplitSource<T> create() throws PulsarClientException;

    /**
     * Finalize the creation of the {@link ChunkSplitSource} instance in asynchronous mode.
     *
     * <p>This method will return a {@link CompletableFuture} that can be used to access the instance when it's ready.
     *
     * @return the reader instance
     * @throws PulsarClientException
     *             if the source creation fails
     */
    CompletableFuture<ChunkSplitSource<T>> createAsync() throws PulsarClientException;

    /**
     * Load the configuration from provided <tt>config</tt> map.
     *
     * <p>Example:
     *
     * <pre>{@code
     * Map<String, Serializable> config = new HashMap<>();
     * config.put("topic", "test-topic");
     * config.put("bounded", true);
     *
     * ChunkSplitSourceBuilder<byte[]> builder = ...;
     * builder = builder.loadConf(config);
     *
     * ChunkReader<byte[]> reader = split.create();
     * }</pre>
     *
     * @param config
     *            configuration to load
     * @return the chunk split source builder
     */
    ChunkSplitSourceBuilder<T> loadConf(Map<String, Serializable> config);

    /**
     * Create a copy of the current {@link ChunkSplitSourceBuilder}.
     *
     * <p>Cloning the builder can be used to share an incomplete configuration and specialize it multiple times. For
     * example:
     *
     * <pre>{@code
     * ChunkSplitSourceBuilder<String> builder = ...;
     * builder.topic("test-topic");
     *
     * ChunkSplitSourceBuilder<String> builder1 = builder.clone().bounded(true);
     * ChunkSplitSourceBuilder<String> builder2 = builder.clone().bounded(false);
     * }</pre>
     *
     * @return a clone of the chunk split source builder instance
     */
    ChunkSplitSourceBuilder<T> clone();

    /**
     * Specify the topic this split source will read from.
     *
     * <p>This argument is required when constructing the reader.
     *
     * @param topicName
     *            the name of the topic
     * @return the chunk split source builder instance
     */
    ChunkSplitSourceBuilder<T> topic(String topicName);

    /**
     * Specify this split source will be bounded or not.
     *
     * @param bounded
     *            if the split source will be bounded
     * @return the chunk split source builder instance
     */
    ChunkSplitSourceBuilder<T> bounded(boolean bounded);

    /**
     * Change range of messages for this split source, by default split source scans all data from earliest to latest.
     *
     * @param begin
     *            expression to specify the start message inclusively
     * @param end
     *            expression to specify the end message exclusively
     * @return the chunk split source builder instance
     */
    ChunkSplitSourceBuilder<T> range(Optional<MessageExpression<T>> begin,
                                     Optional<MessageExpression<T>> end);
}
