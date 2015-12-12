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

import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.place.shared.PlaceHistoryHandler.Historian;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.InlineHyperlink;

/**
 * Widget that is an internal hyperlink and supports HTML5 pushState.
 *
 * <p>
 * It extends GWT's original {@link InlineHyperlink} and moves the history token from the hash to
 * the path.
 * </p>
 *
 * <p>
 * This has the advantage that no-pushState browsers see the correct nice link while still using a
 * {@link com.google.gwt.user.client.ui.Hyperlink} implementation which does not cause reloads.
 * </p>
 *
 * @author <a href="mailto:jb@barop.de">Johannes Barop</a>
 *
 */
public class HyperlinkPushState extends Hyperlink {

  private String targetHistoryToken;

  private static final Historian HISTORIAN = GWT.create(Historian.class);

  /**
   * Calls {@link InlineHyperlink#InlineHyperlink(String, String)}.
   */
  public HyperlinkPushState(final String ptext, final String ptargetHistoryToken) {
    super(ptext, ptargetHistoryToken);
  }

  /**
   * No arg constructor, calls {@link InlineHyperlink#InlineHyperlink()}.
   */
  public HyperlinkPushState() {
    super();
  }

  @Override
  public void setTargetHistoryToken(final String ptargetHistoryToken) {
    assert ptargetHistoryToken != null : "New history item cannot be null!";

    this.targetHistoryToken = ptargetHistoryToken;

    final String href = (ptargetHistoryToken.length() > 0 && ptargetHistoryToken.charAt(0) == '/')
        ? ptargetHistoryToken : "/" + ptargetHistoryToken;
    ((Element) this.getElement().getChild(0)).setPropertyString("href", href);
  }

  @Override
  public String getTargetHistoryToken() {
    return this.targetHistoryToken;
  }

  @Override
  public void onBrowserEvent(final Event pevent) {
    switch (DOM.eventGetType(pevent)) {
      case Event.ONMOUSEOVER:
      case Event.ONMOUSEOUT:
        // Only fire the mouse over event if it's coming from outside this widget.
        // Only fire the mouse out event if it's leaving this widget.
        final Element related = pevent.getRelatedEventTarget().cast();
        if (related != null && this.getElement().isOrHasChild(related)) {
          return;
        }
        break;
      default:
        break;
    }
    DomEvent.fireNativeEvent(pevent, this, this.getElement());
    if (DOM.eventGetType(pevent) == Event.ONCLICK) {
      HyperlinkPushState.HISTORIAN.newItem(this.getTargetHistoryToken(), true);
      pevent.preventDefault();
    }
  }

}
