/*
 * Copyright 2012 Johannes Barop
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package de.barop.gwt.client;


import com.google.gwt.user.client.History;

import static com.google.gwt.user.client.Window.Location.getQueryString;

/**
 * 
 * @author <a href="mailto:jb@barop.de">Johannes Barop</a>
 * 
 */
public class HistoryImplPushStateGwtTest extends AbstractPushStateTest {


  public void testInit() {
    assertEquals(statesOnTestStart, states.size());
   
  }

  public void testOnPopState() {
    History.newItem("historyToken_1");
    assertEquals(statesOnTestStart + 1, states.size());
    assertEquals("/historyToken_1" + getQueryString(), states.peek().url);
    assertEquals("historyToken_1", History.getToken());

    History.newItem("historyToken_2");
    assertEquals(statesOnTestStart + 2, states.size());
    assertEquals("/historyToken_2" + getQueryString(), states.peek().url);
    assertEquals("historyToken_2", History.getToken());

    popState();

    History.newItem("historyToken_1");
    assertEquals(statesOnTestStart + 1, states.size());
    assertEquals("/historyToken_1" + getQueryString(), states.peek().url);
    assertEquals("historyToken_1", History.getToken());
  }

}
