/*
 * Copyright 2017 University of Michigan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
// Licensed to Julian Hyde under one or more contributor license
// agreements. See the NOTICE file distributed with this work for
// additional information regarding copyright ownership.
//
// Julian Hyde licenses this file to you under the Modified BSD License
// (the "License"); you may not use this file except in compliance with
// the License. You may obtain a copy of the License at:
//
// http://opensource.org/licenses/BSD-3-Clause
*/
package sqlline;

import java.util.Collections;
import java.util.List;

import jline.console.completer.Completer;

/**
 * A {@link CommandHandler} implementation that
 * uses reflection to determine the method to dispatch the command.
 */
public class ReflectiveCommandHandler extends AbstractCommandHandler {
  public ReflectiveCommandHandler(SqlLine sqlLine, List<Completer> completers,
      String... cmds) {
    super(sqlLine, cmds, sqlLine.loc("help-" + cmds[0]), completers);
  }

  public ReflectiveCommandHandler(SqlLine sqlLine, Completer completer,
      String... cmds) {
    this(sqlLine, Collections.<Completer>singletonList(completer), cmds);
  }

  public void execute(String line, DispatchCallback callback) {
    try {
      sqlLine.getCommands().getClass()
          .getMethod(getName(), String.class, DispatchCallback.class)
          .invoke(sqlLine.getCommands(), line, callback);
    } catch (Throwable e) {
      callback.setToFailure();
      sqlLine.error(e);
    }
  }
}

// End ReflectiveCommandHandler.java
