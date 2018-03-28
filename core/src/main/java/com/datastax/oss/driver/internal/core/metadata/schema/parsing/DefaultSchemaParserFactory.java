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
package com.datastax.oss.driver.internal.core.metadata.schema.parsing;

import com.datastax.oss.driver.internal.core.context.InternalDriverContext;
import com.datastax.oss.driver.internal.core.metadata.schema.queries.SchemaRows;
import net.jcip.annotations.ThreadSafe;

@ThreadSafe
public class DefaultSchemaParserFactory implements SchemaParserFactory {

  private final InternalDriverContext context;

  public DefaultSchemaParserFactory(InternalDriverContext context) {
    this.context = context;
  }

  @Override
  public SchemaParser newInstance(SchemaRows rows) {
    return new SchemaParser(rows, context);
  }
}
