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

import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.place.shared.PlaceHistoryHandler.Historian;
import com.google.gwt.user.client.Window;

public class PushStateHistorianImpl implements Historian, HasValueChangeHandlers<String> {

  private final EventBus handlers = new SimpleEventBus();
  private String token;
  private final String relativePath;


  /**
   * constructor.
   *
   * @param prelativePath relative path to use
   */
  PushStateHistorianImpl(final String prelativePath) {
    final StringBuffer tmpRelativePath = new StringBuffer(64);
    if (prelativePath != null && prelativePath.length() > 0) {
      if (prelativePath.charAt(0) != '/') {
        tmpRelativePath.append('/');
      }
      tmpRelativePath.append(prelativePath);
      if (prelativePath.charAt(prelativePath.length() - 1) != '/') {
        tmpRelativePath.append('/');
      }
    }
    this.relativePath = tmpRelativePath.toString();
    this.initToken();
    this.registerPopstateHandler();
  }

  @Override
  public final String getToken() {
    return this.token;
  }

  @Override
  public void newItem(final String ptoken, final boolean pissueEvent) {
    this.newItem(ptoken, pissueEvent, false);
  }

  /**
   * set new item.
   *
   * @param ptoken token to set
   * @param pissueEvent true if event should be fired
   * @param preplaceState true if state should be replaced
   */
  public void newItem(final String ptoken, final boolean pissueEvent, final boolean preplaceState) {
    if (this.setToken(ptoken)) {
      if (preplaceState) {
        PushStateHistorianImpl.replaceState(this.relativePath, this.getToken());
      } else {
        PushStateHistorianImpl.pushState(this.relativePath, this.getToken());
      }

      if (pissueEvent) {
        ValueChangeEvent.fire(this, this.getToken());
      }
    }
  }

  @Override
  public void fireEvent(final GwtEvent<?> pevent) {
    this.handlers.fireEvent(pevent);
  }

  private native void registerPopstateHandler() /*-{
    var that = this;
    var oldHandler = $wnd.onpopstate;
    $wnd.onpopstate = $entry(function(e) {
      if (e) {
        if (e.state) {
          that.@com.wallissoftware.pushstate.client.PushStateHistorianImpl::onPopState(Ljava/lang/String;)(e.state.token);
        }
        if (oldHandler) {
          oldHandler(e);
        }
      }
    });
  }-*/;

  private void onPopState(final String ptoken) {
    if (this.setToken(ptoken)) {
      ValueChangeEvent.fire(this, this.getToken());
    }
  }

  private final void initToken() {
    final String token = Window.Location.getPath() + Window.Location.getQueryString();

    this.setToken(token);
    PushStateHistorianImpl.replaceState(this.relativePath, this.getToken());
  }

  private String stripStartSlash(final String pinput) {
    return (pinput != null && pinput.length() > 1 && pinput.charAt(0) == '/') ? pinput.substring(1)
        : pinput;
  }

  private String stripRelativePath(final String ptoken) {
    final String relPath = this.stripStartSlash(this.relativePath);
    final String token = this.stripStartSlash(ptoken);

    if (token != null && relPath != null && token.startsWith(relPath)) {
      return this.stripStartSlash(token.substring(relPath.length()));
    }
    return token;
  }

  private static native void replaceState(final String prelativePath, final String ptoken) /*-{
    $wnd.history.replaceState({'token':ptoken}, $doc.title, prelativePath + ptoken);
  }-*/;

  private static native void pushState(final String prelativePath, final String ptoken) /*-{
    $wnd.history.pushState({'token':ptoken}, $doc.title, prelativePath + ptoken);
  }-*/;

  private final boolean setToken(final String pnewToken) {
    final String newToken = this.stripRelativePath(pnewToken);
    if (!this.matchesToken(newToken)) {
      this.token = newToken;
      return true;
    }
    return false;
  }

  private boolean matchesToken(final String pcompare) {
    return this.token != null && pcompare != null && (pcompare.equals(this.token)
        || pcompare.equals(this.token + "/") || this.token.equals(pcompare + "/"));
  }

  @Override
  public HandlerRegistration addValueChangeHandler(
      final ValueChangeHandler<String> valueChangeHandler) {
    return this.handlers.addHandler(ValueChangeEvent.getType(), valueChangeHandler);
  }
}
