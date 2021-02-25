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

import org.apache.pulsar.client.api.Message;
import org.apache.pulsar.client.api.MessageId;
import org.apache.pulsar.common.api.EncryptionContext;
import org.apache.pulsar.common.classification.InterfaceAudience;
import org.apache.pulsar.common.classification.InterfaceStability;

import java.util.Map;
import java.util.Optional;

/**
 * The chunk of messages abstraction used in Pulsar batch reader.
 */
@InterfaceAudience.Public
@InterfaceStability.Unstable
public interface MessageChunk<T> extends Cloneable {
    /**
     * Return number of messages within the chunk
     *
     * @return size of message chunk
     */
    int size();

    /**
     * Return the message at index
     *
     * @param index
     *            index of message within the chunk
     * @return an unmodifiable view of the message
     */
    Message<T> get(int index);

    /**
     * Return the properties attached to the message at index.
     *
     * <p>Properties are application defined key/value pairs that will be attached to the message.
     *
     * @param index
     *            index of message within the chunk
     * @return an unmodifiable view of the properties map
     */
    default Map<String, String> getProperties(int index) {
        return get(index).getProperties();
    }

    /**
     * Check whether the message has a specific property attached at index.
     *
     * @param index
     *            index of message within the chunk
     * @param name the name of the property to check
     * @return true if the message has the specified property and false if the properties is not defined
     */
    default boolean hasProperty(int index, String name) {
        return get(index).hasProperty(name);
    }

    /**
     * Get the value of a specific property at index.
     *
     * @param index
     *            index of message within the chunk
     * @param name the name of the property
     * @return the value of the property or null if the property was not defined
     */
    default String getProperty(int index, String name) {
        return get(index).getProperty(name);
    }

    /**
     * Get the raw payload of the message at index.
     *
     * <p>Even when using the Schema and type-safe API, an application
     * has access to the underlying raw message payload.
     *
     * @param index
     *            index of message within the chunk
     * @return the byte array with the message payload
     */
    default byte[] getData(int index) {
        return get(index).getData();
    }

    /**
     * Get the de-serialized value of the message, according the configured {@link Schema} at index.
     *
     * @param index
     *            index of message within the chunk
     * @return the deserialized value of the message
     */
    default T getValue(int index) {
        return get(index).getValue();
    }

    /**
     * Get the unique message ID associated with the message at index.
     *
     * <p>The message id can be used to univocally refer to a message without having the keep
     * the entire payload in memory.
     *
     * <p>Only messages received from the consumer will have a message id assigned.
     *
     * @param index
     *            index of message within the chunk
     * @return the message id null if the message was not received by this client instance
     */
    default MessageId getMessageId(int index) {
        return get(index).getMessageId();
    }

    /**
     * Get the publish time of the message. The publish time is the timestamp that a client publish the message at index.
     *
     * @param index
     *            index of message within the chunk
     * @return publish time of the message.
     * @see #getEventTime()
     */
    default long getPublishTime(int index) {
        return get(index).getPublishTime();
    }

    /**
     * Get the event time associated with the message at index. It is typically set by the applications via
     * {@link MessageBuilder#setEventTime(long)}.
     *
     * <p>If there isn't any event time associated with this event, it will return 0.
     *
     * @param index
     *            index of message within the chunk
     * @see MessageBuilder#setEventTime(long)
     * @since 1.20.0
     * @return the message event time or 0 if event time wasn't set
     */
    default long getEventTime(int index) {
        return get(index).getEventTime();
    }

    /**
     * Get the sequence id associated with the message at index. It is typically set by the applications via
     * {@link MessageBuilder#setSequenceId(long)}.
     *
     * @param index
     *            index of message within the chunk
     * @return sequence id associated with the message.
     * @see MessageBuilder#setEventTime(long)
     * @since 1.22.0
     */
    default long getSequenceId(int index) {
        return get(index).getSequenceId();
    }

    /**
     * Get the producer name who produced the message at index.
     *
     * @param index
     *            index of message within the chunk
     * @return producer name who produced the message, null if producer name is not set.
     * @since 1.22.0
     */
    default String getProducerName(int index) {
        return get(index).getProducerName();
    }

    /**
     * Check whether the message at index has a key.
     *
     * @param index
     *            index of message within the chunk
     * @return true if the key was set while creating the message and false if the key was not set
     * while creating the message
     */
    default boolean hasKey(int index) {
        return get(index).hasKey();
    }

    /**
     * Get the key of the message at index.
     *
     * @param index
     *            index of message within the chunk
     * @return the key of the message
     */
    default String getKey(int index) {
        return get(index).getKey();
    }

    /**
     * Check whether the key of the message at index has been base64 encoded.
     *
     * @param index
     *            index of message within the chunk
     * @return true if the key is base64 encoded, false otherwise
     */
    default boolean hasBase64EncodedKey(int index) {
        return get(index).hasBase64EncodedKey();
    }

    /**
     * Get bytes in key of message at index. If the key has been base64 encoded, it is decoded before being returned.
     * Otherwise, if the key is a plain string, this method returns the UTF_8 encoded bytes of the string.
     *
     * @param index
     *            index of message within the chunk
     * @return the key in byte[] form
     */
    default byte[] getKeyBytes(int index) {
        return get(index).getKeyBytes();
    }

    /**
     * Check whether the message at index has a ordering key.
     *
     * @param index
     *            index of message within the chunk
     * @return true if the ordering key was set while creating the message
     *         false if the ordering key was not set while creating the message
     */
    default boolean hasOrderingKey(int index) {
        return get(index).hasOrderingKey();
    }

    /**
     * Get the ordering key of the message at index.
     *
     * @param index
     *            index of message within the chunk
     * @return the ordering key of the message
     */
    default byte[] getOrderingKey(int index) {
        return get(index).getOrderingKey();
    }

    /**
     * Get the topic the message at index was published to.
     *
     * @param index
     *            index of message within the chunk
     * @return the topic the message was published to
     */
    default String getTopicName(int index) {
        return get(index).getTopicName();
    }

    /**
     * {@link EncryptionContext} contains encryption and compression information in it using which application can
     * decrypt consumed message at index with encrypted-payload.
     *
     * @param index
     *            index of message within the chunk
     * @return the optiona encryption context
     */
    default Optional<EncryptionContext> getEncryptionCtx(int index) {
        return get(index).getEncryptionCtx();
    }

    /**
     * Get message at index redelivery count, redelivery count maintain in pulsar broker. When client acknowledge message
     * timeout, broker will dispatch message again with message redelivery count in CommandMessage defined.
     *
     * <p>Message redelivery increases monotonically in a broker, when topic switch ownership to a another broker
     * redelivery count will be recalculated.
     *
     * @param index
     *            index of message within the chunk
     * @since 2.3.0
     * @return message redelivery count
     */
    default int getRedeliveryCount(int index) {
        return get(index).getRedeliveryCount();
    }

    /**
     * Get schema version of the message at index.
     * @param index
     *            index of message within the chunk
     * @since 2.4.0
     * @return Schema version of the message if the message is produced with schema otherwise null.
     */
    default byte[] getSchemaVersion(int index) {
        return get(index).getSchemaVersion();
    }

    /**
     * Check whether the message at index is replicated from other cluster.
     *
     * @param index
     *            index of message within the chunk
     * @since 2.4.0
     * @return true if the message is replicated from other cluster.
     *         false otherwise.
     */
    default boolean isReplicated(int index) {
        return get(index).isReplicated();
    }

    /**
     * Get name of cluster, from which the message at index is replicated.
     *
     * @param index
     *            index of message within the chunk
     * @since 2.4.0
     * @return the name of cluster, from which the message is replicated.
     */
    default String getReplicatedFrom(int index) {
        return get(index).getReplicatedFrom();
    }
}
