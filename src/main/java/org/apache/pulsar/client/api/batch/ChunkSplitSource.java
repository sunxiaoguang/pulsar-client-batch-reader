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
 * ChunkSplitSource create {@link ChunkSplit}s that define portions of data of a topic
 *
 * @param <T> The type of the value associated with Message.
 */
@InterfaceAudience.Public
@InterfaceStability.Unstable
public interface ChunkSplitSource<T> {
    /**
     * Computes the chunk splits. The given minimum number of splits is a hint as to how many splits
     * are desired.
     *
     * @param minSplitsHint of minimal input splits, as a hint.
     * @return An array of chunk splits.
     */
    ChunkSplit<T>[] createSplit(int minSplitsHint);
}
