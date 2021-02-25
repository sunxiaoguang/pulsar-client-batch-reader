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

/**
 * Message expression
 */
@InterfaceAudience.Public
@InterfaceStability.Unstable
public interface MessageExpression<T> {
    static <T> RefBuilder<T> builder() {
        return null;
    }

    /**
     * Message expression builder to specify reference to message
     */
    interface RefBuilder<T> extends Cloneable {
        /**
         * Create builder referring to message property
         *
         * @param name property name
         * @return operator builder
         */
        OperatorBuilder<T> property(String name);

        /**
         * Create builder referring to message publish time
         *
         * @return operator builder
         */
        OperatorBuilder<T> publishTime();

        /**
         * Create builder referring to message event time
         *
         * @return operator builder
         */
        OperatorBuilder<T> eventTime();

        /**
         * Create builder referring to message key
         *
         * @return operator builder
         */
        OperatorBuilder<T> key();

        /**
         * Clone this message expression builder
         *
         * @return clone of this message expression builder
         */
        RefBuilder<T> clone();
    }

    /**
     * Message expression builder to specify operator and it's operands
     */
    interface OperatorBuilder<T> extends Cloneable {
        /**
         * Create equals expression builder
         *
         * @param rhs operand
         * @return expression builder
         */
        ExprBuilder<T> equals(String rhs);

        /**
         * Create not equals expression builder
         *
         * @param rhs operand
         * @return expression builder
         */
        ExprBuilder<T> notEquals(String rhs);

        /**
         * Create in expression builder
         *
         * @param arg1   operand 1
         * @param arg2   operand 2
         * @param others other operands
         * @return expression builder
         */
        ExprBuilder<T> in(String arg1, String arg2, String... others);

        /**
         * Create not in expression builder
         *
         * @param arg1   operand 1
         * @param arg2   operand 2
         * @param others other operands
         * @return expression builder
         */
        ExprBuilder<T> notIn(String arg1, String arg2, String... others);

        /**
         * Create greater than expression builder
         *
         * @param rhs operand
         * @return expression builder
         */
        ExprBuilder<T> greaterThan(String rhs);

        /**
         * Create greater than equals expression builder
         *
         * @param rhs operand
         * @return expression builder
         */
        ExprBuilder<T> greaterThanEquals(String rhs);

        /**
         * Create less than expression builder
         *
         * @param rhs operand
         * @return expression builder
         */
        ExprBuilder<T> lessThan(String rhs);

        /**
         * Create less than equals expression builder
         *
         * @param rhs operand
         * @return expression builder
         */
        ExprBuilder<T> lessThanEquals(String rhs);

        /**
         * Create like expression builder
         *
         * @param rhs operand
         * @return expression builder
         */
        ExprBuilder<T> like(String rhs);

        /**
         * Create not like expression builder
         *
         * @param rhs operand
         * @return expression builder
         */
        ExprBuilder<T> notLike(String rhs);

        /**
         * Create contains all expression builder
         *
         * @param arg1   operand 1
         * @param arg2   operand 2
         * @param others other operands
         * @return expression builder
         */
        ExprBuilder<T> containsAll(String arg1, String arg2, String... others);

        /**
         * Create contains any expression builder
         *
         * @param arg1   operand 1
         * @param arg2   operand 2
         * @param others other operands
         * @return expression builder
         */
        ExprBuilder<T> containsAny(String arg1, String arg2, String... others);

        /**
         * Create contains none expression builder
         *
         * @param arg1   operand 1
         * @param arg2   operand 2
         * @param others other operands
         * @return expression builder
         */
        ExprBuilder<T> containsNone(String arg1, String arg2, String... others);

        /**
         * Create between none expression builder
         *
         * @param arg1 operand 1
         * @param arg2 operand 2
         * @return expression builder
         */
        ExprBuilder<T> between(String arg1, String arg2);

        /**
         * Create equals expression builder
         *
         * @param rhs operand
         * @return expression builder
         */
        ExprBuilder<T> equals(Long rhs);

        /**
         * Create not equals expression builder
         *
         * @param rhs operand
         * @return expression builder
         */
        ExprBuilder<T> notEquals(Long rhs);

        /**
         * Create in expression builder
         *
         * @param arg1   operand 1
         * @param arg2   operand 2
         * @param others other operands
         * @return expression builder
         */
        ExprBuilder<T> in(Long arg1, Long arg2, Long... others);

        /**
         * Create not in expression builder
         *
         * @param arg1   operand 1
         * @param arg2   operand 2
         * @param others other operands
         * @return expression builder
         */
        ExprBuilder<T> notIn(Long arg1, Long arg2, Long... others);

        /**
         * Create greater than expression builder
         *
         * @param rhs operand
         * @return expression builder
         */
        ExprBuilder<T> greaterThan(Long rhs);

        /**
         * Create greater than equals expression builder
         *
         * @param rhs operand
         * @return expression builder
         */
        ExprBuilder<T> greaterThanEquals(Long rhs);

        /**
         * Create less than expression builder
         *
         * @param rhs operand
         * @return expression builder
         */
        ExprBuilder<T> lessThan(Long rhs);

        /**
         * Create less than equals expression builder
         *
         * @param rhs operand
         * @return expression builder
         */
        ExprBuilder<T> lessThanEquals(Long rhs);

        /**
         * Create between none expression builder
         *
         * @param arg1 operand 1
         * @param arg2 operand 2
         * @return expression builder
         */
        ExprBuilder<T> between(Long arg1, Long arg2);

        /**
         * Create equals expression builder
         *
         * @param rhs operand
         * @return expression builder
         */
        ExprBuilder<T> equals(Double rhs);

        /**
         * Create not equals expression builder
         *
         * @param rhs operand
         * @return expression builder
         */
        ExprBuilder<T> notEquals(Double rhs);

        /**
         * Create in expression builder
         *
         * @param arg1   operand 1
         * @param arg2   operand 2
         * @param others other operands
         * @return expression builder
         */
        ExprBuilder<T> in(Double arg1, Double arg2, Double... others);

        /**
         * Create not in expression builder
         *
         * @param arg1   operand 1
         * @param arg2   operand 2
         * @param others other operands
         * @return expression builder
         */
        ExprBuilder<T> notIn(Double arg1, Double arg2, Double... others);

        /**
         * Create greater than expression builder
         *
         * @param rhs operand
         * @return expression builder
         */
        ExprBuilder<T> greaterThan(Double rhs);

        /**
         * Create greater than equals expression builder
         *
         * @param rhs operand
         * @return expression builder
         */
        ExprBuilder<T> greaterThanEquals(Double rhs);

        /**
         * Create less than expression builder
         *
         * @param rhs operand
         * @return expression builder
         */
        ExprBuilder<T> lessThan(Double rhs);

        /**
         * Create less than equals expression builder
         *
         * @param rhs operand
         * @return expression builder
         */
        ExprBuilder<T> lessThanEquals(Double rhs);

        /**
         * Create between none expression builder
         *
         * @param arg1 operand 1
         * @param arg2 operand 2
         * @return expression builder
         */
        ExprBuilder<T> between(Double arg1, Double arg2);

        /**
         * Clone this message expression builder
         *
         * @return clone of this message expression builder
         */
        ExprBuilder<T> clone();
    }

    interface ExprBuilder<T> extends Cloneable {
        /**
         * Finalize expression building and return the chunk split created the builder
         *
         * @return chunk split
         */
        MessageExpression<T> build();

        /**
         * Start creating a new expression builder with and logic
         *
         * @return new expression builder
         */
        RefBuilder<T> and();

        /**
         * Start creating a new expression builder with or logic
         *
         * @return new expression builder
         */
        RefBuilder<T> or();

        /**
         * Apply not logic to current expression
         *
         * @return new expression builder
         */
        ExprBuilder<T> not();

        /**
         * Clone this message expression builder
         *
         * @return clone of this message expression builder
         */
        ExprBuilder<T> clone();
    }
}
