/*
 * Copyright 2014 Richard Wallis
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

package com.wallissoftware.pushstate.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.place.shared.PlaceHistoryHandler.Historian;

import org.apache.commons.lang3.StringUtils;

public class PushStateHistorian implements Historian, HasValueChangeHandlers<String> {

  private static String relativePath;

  static {
    String prefix = StringUtils.removeEnd(StringUtils
        .removeEnd(StringUtils.removeEnd(GWT.getModuleBaseURL(), "/"), GWT.getModuleName()), "/");
    prefix = StringUtils.substring(prefix, StringUtils.indexOf(prefix, '/', 8));
    if (StringUtils.length(prefix) > 1) {
      PushStateHistorian.relativePath = prefix;
    } else {
      PushStateHistorian.relativePath = StringUtils.EMPTY;
    }
  }

  private static PushStateHistorianImpl impl;


  /**
   * Call this method in your entry point or bootstrapper to set the relative path of your
   * application.
   *
   * @param prelativePath - the relative path of your app.
   */
  public static void setRelativePath(final String prelativePath) {
    assert (PushStateHistorian.impl == null) : //
    "You must set relative path before calling any history method";
    PushStateHistorian.relativePath = prelativePath;
  }

  @Override
  public void fireEvent(final GwtEvent<?> pevent) {
    PushStateHistorian.getImpl().fireEvent(pevent);
  }

  @Override
  public HandlerRegistration addValueChangeHandler(
      final ValueChangeHandler<String> pvalueChangeHandler) {
    return PushStateHistorian.getImpl().addValueChangeHandler(pvalueChangeHandler);
  }

  @Override
  public String getToken() {
    return PushStateHistorian.getImpl().getToken();
  }

  @Override
  public void newItem(final String ptoken, final boolean pissueEvent) {
    PushStateHistorian.getImpl().newItem(ptoken, pissueEvent);
  }

  private static PushStateHistorianImpl getImpl() {
    if (PushStateHistorian.impl == null) {
      PushStateHistorian.impl = new PushStateHistorianImpl(PushStateHistorian.relativePath);
    }
    return PushStateHistorian.impl;
  }

  public static void replaceItem(final String pnewToken, final boolean pfireEvent) {
    PushStateHistorian.getImpl().newItem(pnewToken, pfireEvent, true);
  }

}
