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

import org.apache.pulsar.client.api.ConsumerCryptoFailureAction;
import org.apache.pulsar.client.api.CryptoKeyReader;
import org.apache.pulsar.client.api.PulsarClientException;
import org.apache.pulsar.common.classification.InterfaceAudience;
import org.apache.pulsar.common.classification.InterfaceStability;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * {@link ChunkSplit} is used to configure and create instances of {@link ChunkReader} for a split.
 */
@InterfaceAudience.Public
@InterfaceStability.Unstable
public interface ChunkSplit<T> extends Cloneable {
    /**
     * Finalize the creation of the {@link ChunkReader} instance.
     *
     * <p>This method will block until the reader is created successfully or an exception is thrown.
     *
     * @return the reader instance
     * @throws PulsarClientException
     *             if the reader creation fails
     */
    ChunkReader<T> createReader() throws PulsarClientException;

    /**
     * Finalize the creation of the {@link ChunkReader} instance in asynchronous mode.
     *
     * <p>This method will return a {@link CompletableFuture} that can be used to access the instance when it's ready.
     *
     * @return the reader instance
     * @throws PulsarClientException
     *             if the reader creation fails
     */
    CompletableFuture<ChunkReader<T>> createReaderAsync() throws PulsarClientException;

    /**
     * Load the configuration from provided <tt>config</tt> map.
     *
     * <p>Example:
     *
     * <pre>{@code
     * Map<String, Serializable> config = new HashMap<>();
     * config.put("defaultCryptoKeyReader", "key.pem");
     *
     * ChunkSplit<byte[]> split = ...;
     * split = split.loadConf(config);
     *
     * ChunkReader<byte[]> reader = split.create();
     * }</pre>
     *
     * @param config
     *            configuration to load
     * @return the chunk split
     */
    ChunkSplit<T> loadConf(Map<String, Serializable> config);

    /**
     * Create a copy of the current {@link ChunkSplit}.
     *
     * <p>Cloning the split can be used to share an incomplete configuration and specialize it multiple times. For
     *
     * @return a clone of the chunk split
     */
    ChunkSplit<T> clone();

    /**
     * Create a query filter builder to build message filtering expression
     *
     * @return expression builder
     */
    FilterRefBuilder<T> filter();

    /**
     * Set message filtering expression used for reader
     *
     * @param expression
     *            message filter expression
     *
     * @return the chunk split
     */
    ChunkSplit<T> filter(MessageExpression expression);

    /**
     * Sets a {@link ChunkReaderListener} for the reader.
     *
     * <p>When a {@link ChunkReaderListener} is set, application will receive messages through it. Calls to
     * {@link ChunkReader#readNext()} will not be allowed.
     *
     * @param readerListener
     *            the listener object
     * @return the chunk split
     */
    ChunkSplit<T> readerListener(ChunkReaderListener<T> readerListener);

    /**
     * Sets a {@link CryptoKeyReader} to decrypt the message payloads.
     *
     * @param cryptoKeyReader
     *            CryptoKeyReader object
     * @return the chunk split instance
     */
    ChunkSplit<T> cryptoKeyReader(CryptoKeyReader cryptoKeyReader);

    /**
     * Sets the default implementation of {@link CryptoKeyReader}.
     *
     * <p>Configure the key reader to be used to decrypt the message payloads.
     *
     * @param privateKey
     *            the private key that is always used to decrypt message payloads.
     * @return the chunk split instance
     * @since 2.8.0
     */
    ChunkSplit<T> defaultCryptoKeyReader(String privateKey);

    /**
     * Sets the default implementation of {@link CryptoKeyReader}.
     *
     * <p>Configure the key reader to be used to decrypt the message payloads.
     *
     * @param privateKeys
     *            the map of private key names and their URIs used to decrypt message payloads.
     * @return the chunk split instance
     * @since 2.8.0
     */
    ChunkSplit<T> defaultCryptoKeyReader(Map<String, String> privateKeys);

    /**
     * Sets the {@link ConsumerCryptoFailureAction} to specify.
     *
     * @param action
     *            The action to take when the decoding fails
     * @return the chunk split instance
     */
    ChunkSplit<T> cryptoFailureAction(ConsumerCryptoFailureAction action);

    /**
     * Message expression builder to specify reference to message
     */
    interface FilterRefBuilder<T> extends Cloneable {
        /**
         * Create builder referring to message property
         *
         * @param name
         *            property name
         * @return operator builder
         */
        FilterOperatorBuilder<T> property(String name);

        /**
         * Create builder referring to message publish time
         *
         * @return operator builder
         */
        FilterOperatorBuilder<T> publishTime();

        /**
         * Create builder referring to message event time
         *
         * @return operator builder
         */
        FilterOperatorBuilder<T> eventTime();

        /**
         * Create builder referring to message key
         *
         * @return operator builder
         */
        FilterOperatorBuilder<T> key();

        /**
         * Clone this message expression builder
         *
         * @return clone of this message expression builder
         */
        FilterRefBuilder<T> clone();
    }

    /**
     * Message expression builder to specify operator and it's operands
     */
    interface FilterOperatorBuilder<T> extends Cloneable {
        /**
         * Create equals expression builder
         *
         * @param rhs
         *           operand
         *
         * @return expression builder
         */
        FilterExpressionBuilder<T> equals(String rhs);

        /**
         * Create not equals expression builder
         *
         * @param rhs
         *           operand
         *
         * @return expression builder
         */
        FilterExpressionBuilder<T> notEquals(String rhs);

        /**
         * Create in expression builder
         *
         * @param arg1
         *           operand 1
         * @param arg2
         *           operand 2
         * @param others
         *           other operands
         *
         * @return expression builder
         */
        FilterExpressionBuilder<T> in(String arg1, String arg2, String... others);

        /**
         * Create not in expression builder
         *
         * @param arg1
         *           operand 1
         * @param arg2
         *           operand 2
         * @param others
         *           other operands
         *
         * @return expression builder
         */
        FilterExpressionBuilder<T> notIn(String arg1, String arg2, String... others);

        /**
         * Create greater than expression builder
         *
         * @param rhs
         *           operand
         *
         * @return expression builder
         */
        FilterExpressionBuilder<T> greaterThan(String rhs);

        /**
         * Create greater than equals expression builder
         *
         * @param rhs
         *           operand
         *
         * @return expression builder
         */
        FilterExpressionBuilder<T> greaterThanEquals(String rhs);

        /**
         * Create less than expression builder
         *
         * @param rhs
         *           operand
         *
         * @return expression builder
         */
        FilterExpressionBuilder<T> lessThan(String rhs);

        /**
         * Create less than equals expression builder
         *
         * @param rhs
         *           operand
         *
         * @return expression builder
         */
        FilterExpressionBuilder<T> lessThanEquals(String rhs);

        /**
         * Create like expression builder
         *
         * @param rhs
         *           operand
         *
         * @return expression builder
         */
        FilterExpressionBuilder<T> like(String rhs);

        /**
         * Create not like expression builder
         *
         * @param rhs
         *           operand
         *
         * @return expression builder
         */
        FilterExpressionBuilder<T> notLike(String rhs);

        /**
         * Create contains all expression builder
         *
         * @param arg1
         *           operand 1
         * @param arg2
         *           operand 2
         * @param others
         *           other operands
         *
         * @return expression builder
         */
        FilterExpressionBuilder<T> containsAll(String arg1, String arg2, String... others);

        /**
         * Create contains any expression builder
         *
         * @param arg1
         *           operand 1
         * @param arg2
         *           operand 2
         * @param others
         *           other operands
         *
         * @return expression builder
         */
        FilterExpressionBuilder<T> containsAny(String arg1, String arg2, String... others);

        /**
         * Create contains none expression builder
         *
         * @param arg1
         *           operand 1
         * @param arg2
         *           operand 2
         * @param others
         *           other operands
         *
         * @return expression builder
         */
        FilterExpressionBuilder<T> containsNone(String arg1, String arg2, String... others);

        /**
         * Create between none expression builder
         *
         * @param arg1
         *           operand 1
         * @param arg2
         *           operand 2
         *
         * @return expression builder
         */
        FilterExpressionBuilder<T> between(String arg1, String arg2);

        /**
         * Create equals expression builder
         *
         * @param rhs
         *           operand
         *
         * @return expression builder
         */
        FilterExpressionBuilder<T> equals(Long rhs);

        /**
         * Create not equals expression builder
         *
         * @param rhs
         *           operand
         *
         * @return expression builder
         */
        FilterExpressionBuilder<T> notEquals(Long rhs);

        /**
         * Create in expression builder
         *
         * @param arg1
         *           operand 1
         * @param arg2
         *           operand 2
         * @param others
         *           other operands
         *
         * @return expression builder
         */
        FilterExpressionBuilder<T> in(Long arg1, Long arg2, Long... others);

        /**
         * Create not in expression builder
         *
         * @param arg1
         *           operand 1
         * @param arg2
         *           operand 2
         * @param others
         *           other operands
         *
         * @return expression builder
         */
        FilterExpressionBuilder<T> notIn(Long arg1, Long arg2, Long... others);

        /**
         * Create greater than expression builder
         *
         * @param rhs
         *           operand
         *
         * @return expression builder
         */
        FilterExpressionBuilder<T> greaterThan(Long rhs);

        /**
         * Create greater than equals expression builder
         *
         * @param rhs
         *           operand
         *
         * @return expression builder
         */
        FilterExpressionBuilder<T> greaterThanEquals(Long rhs);

        /**
         * Create less than expression builder
         *
         * @param rhs
         *           operand
         *
         * @return expression builder
         */
        FilterExpressionBuilder<T> lessThan(Long rhs);

        /**
         * Create less than equals expression builder
         *
         * @param rhs
         *           operand
         *
         * @return expression builder
         */
        FilterExpressionBuilder<T> lessThanEquals(Long rhs);

        /**
         * Create between none expression builder
         *
         * @param arg1
         *           operand 1
         * @param arg2
         *           operand 2
         *
         * @return expression builder
         */
        FilterExpressionBuilder<T> between(Long arg1, Long arg2);

        /**
         * Create equals expression builder
         *
         * @param rhs
         *           operand
         *
         * @return expression builder
         */
        FilterExpressionBuilder<T> equals(Double rhs);

        /**
         * Create not equals expression builder
         *
         * @param rhs
         *           operand
         *
         * @return expression builder
         */
        FilterExpressionBuilder<T> notEquals(Double rhs);

        /**
         * Create in expression builder
         *
         * @param arg1
         *           operand 1
         * @param arg2
         *           operand 2
         * @param others
         *           other operands
         *
         * @return expression builder
         */
        FilterExpressionBuilder<T> in(Double arg1, Double arg2, Double... others);

        /**
         * Create not in expression builder
         *
         * @param arg1
         *           operand 1
         * @param arg2
         *           operand 2
         * @param others
         *           other operands
         *
         * @return expression builder
         */
        FilterExpressionBuilder<T> notIn(Double arg1, Double arg2, Double... others);

        /**
         * Create greater than expression builder
         *
         * @param rhs
         *           operand
         *
         * @return expression builder
         */
        FilterExpressionBuilder<T> greaterThan(Double rhs);

        /**
         * Create greater than equals expression builder
         *
         * @param rhs
         *           operand
         *
         * @return expression builder
         */
        FilterExpressionBuilder<T> greaterThanEquals(Double rhs);

        /**
         * Create less than expression builder
         *
         * @param rhs
         *           operand
         *
         * @return expression builder
         */
        FilterExpressionBuilder<T> lessThan(Double rhs);

        /**
         * Create less than equals expression builder
         *
         * @param rhs
         *           operand
         *
         * @return expression builder
         */
        FilterExpressionBuilder<T> lessThanEquals(Double rhs);

        /**
         * Create between none expression builder
         *
         * @param arg1
         *           operand 1
         * @param arg2
         *           operand 2
         *
         * @return expression builder
         */
        FilterExpressionBuilder<T> between(Double arg1, Double arg2);

        /**
         * Clone this message expression builder
         *
         * @return clone of this message expression builder
         */
        FilterExpressionBuilder<T> clone();
    }

    interface FilterExpressionBuilder<T> extends Cloneable {
        /**
         * Finalize expression building and return the chunk split created the builder
         *
         * @return chunk split
         */
        ChunkSplit<T> build();

        /**
         * Start creating a new expression builder with and logic
         *
         * @return new expression builder
         */
        FilterRefBuilder<T> and();

        /**
         * Start creating a new expression builder with or logic
         *
         * @return new expression builder
         */
        FilterRefBuilder<T> or();

        /**
         * Apply not logic to current expression
         *
         * @return new expression builder
         */
        FilterExpressionBuilder<T> not();

        /**
         * Clone this message expression builder
         *
         * @return clone of this message expression builder
         */
        FilterExpressionBuilder<T> clone();
    }
}
