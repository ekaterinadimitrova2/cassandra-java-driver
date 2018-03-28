/*
 * Copyright DataStax, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.datastax.oss.driver.internal.core.util;

import com.datastax.oss.driver.shaded.guava.common.annotations.VisibleForTesting;
import com.datastax.oss.driver.shaded.guava.common.base.Preconditions;
import com.datastax.oss.driver.shaded.guava.common.collect.LinkedHashMultimap;
import com.datastax.oss.driver.shaded.guava.common.collect.Lists;
import com.datastax.oss.driver.shaded.guava.common.collect.Maps;
import com.datastax.oss.driver.shaded.guava.common.collect.Multimap;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import net.jcip.annotations.NotThreadSafe;

/** A basic directed graph implementation to perform topological sorts. */
@NotThreadSafe
public class DirectedGraph<V> {

  // We need to keep track of the predecessor count. For simplicity, use a map to store it
  // alongside the vertices.
  private final Map<V, Integer> vertices;
  private final Multimap<V, V> adjacencyList;
  private boolean wasSorted;

  public DirectedGraph(Collection<V> vertices) {
    this.vertices = Maps.newLinkedHashMapWithExpectedSize(vertices.size());
    this.adjacencyList = LinkedHashMultimap.create();

    for (V vertex : vertices) {
      this.vertices.put(vertex, 0);
    }
  }

  @VisibleForTesting
  @SafeVarargs
  DirectedGraph(V... vertices) {
    this(Arrays.asList(vertices));
  }

  /**
   * this assumes that {@code from} and {@code to} were part of the vertices passed to the
   * constructor
   */
  public void addEdge(V from, V to) {
    Preconditions.checkArgument(vertices.containsKey(from) && vertices.containsKey(to));
    adjacencyList.put(from, to);
    vertices.put(to, vertices.get(to) + 1);
  }

  /** one-time use only, calling this multiple times on the same graph won't work */
  public List<V> topologicalSort() {
    Preconditions.checkState(!wasSorted);
    wasSorted = true;

    Queue<V> queue = new ArrayDeque<>();

    for (Map.Entry<V, Integer> entry : vertices.entrySet()) {
      if (entry.getValue() == 0) {
        queue.add(entry.getKey());
      }
    }

    List<V> result = Lists.newArrayList();
    while (!queue.isEmpty()) {
      V vertex = queue.remove();
      result.add(vertex);
      for (V successor : adjacencyList.get(vertex)) {
        if (decrementAndGetCount(successor) == 0) {
          queue.add(successor);
        }
      }
    }

    if (result.size() != vertices.size()) {
      throw new IllegalArgumentException("failed to perform topological sort, graph has a cycle");
    }

    return result;
  }

  private int decrementAndGetCount(V vertex) {
    Integer count = vertices.get(vertex);
    count = count - 1;
    vertices.put(vertex, count);
    return count;
  }
}
