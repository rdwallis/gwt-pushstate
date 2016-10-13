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

package com.wallissoftware.pushstate.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;

/**
 * Utility class with methods to extract and add the <code>gwt.codesvr</code> to the URL.
 * 
 * <p>
 * This is needed so that we not leave the development mode after pushing a history state.
 * </p>
 * 
 * @author <a href="mailto:jb@barop.de">Johannes Barop</a>
 * 
 */
final class CodeServerParameterHelper {

  /**
   * Hidden constructor as this class is not meant to be instantiated.
   */
  private CodeServerParameterHelper() {
  }

  /**
   * Append the <code>gwt.codesvr</code> parameter to the token when needed.
   */
  public static String append(final String token) {
    String result = token;

    /*
     * This gets compiled out in production mode!
     */
    if (!GWT.isProdMode() && GWT.isClient()) {
      String gwtCodesvr = Window.Location.getParameter("gwt.codesvr");
      if (gwtCodesvr != null) {
        if (token.contains("?")) {
          result += "&";
        } else {
          result += "?";
        }
        result += "gwt.codesvr=" + gwtCodesvr;
      }
    }

    return result;
  }

}
