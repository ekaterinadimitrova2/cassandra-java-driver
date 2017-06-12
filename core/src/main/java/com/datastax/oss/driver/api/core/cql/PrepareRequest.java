/*
 * Copyright (C) 2017-2017 DataStax Inc.
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
package com.datastax.oss.driver.api.core.cql;

import com.datastax.oss.driver.api.core.session.Request;
import java.util.concurrent.CompletionStage;

/** A request to prepare a CQL query. */
public interface PrepareRequest
    extends Request<PreparedStatement, CompletionStage<PreparedStatement>> {

  static PrepareRequest from(String query) {
    throw new UnsupportedOperationException("TODO");
  }

  static PrepareRequest from(Statement statement) {
    throw new UnsupportedOperationException("TODO");
  }
}