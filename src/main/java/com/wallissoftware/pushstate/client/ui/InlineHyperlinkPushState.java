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

import com.google.gwt.place.shared.PlaceHistoryHandler.Historian;
import com.google.gwt.user.client.Event;
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
 * {@link Historian} implementation which does not cause reloads.
 * </p>
 *
 * @author <a href="mailto:jb@barop.de">Johannes Barop</a>
 *
 */
public class InlineHyperlinkPushState extends InlineHyperlink {

  private HyperlinkPushStateDelegate delegate;

  /**
   * Calls {@link InlineHyperlink#InlineHyperlink(String, String)}.
   */
  public InlineHyperlinkPushState(final String ptext, final String ptargetHistoryToken) {
    super(ptext, ptargetHistoryToken);
    ensureDelegate();
  }

  /**
   * No arg constructor, calls {@link InlineHyperlink#InlineHyperlink()}.
   */
  public InlineHyperlinkPushState() {
    super();
    ensureDelegate();
  }

  private void ensureDelegate() {
    if(delegate == null)
      delegate = new HyperlinkPushStateDelegate(this);
  }
  
  @Override
  public void setTargetHistoryToken(String ptargetHistoryToken) {
    ensureDelegate();
    delegate.setTargetHistoryToken(ptargetHistoryToken);
  }

  
  @Override
  public String getTargetHistoryToken() {
    return delegate.getTargetHistoryToken();
  }

  @Override
  public void onBrowserEvent(final Event pevent) {
    delegate.onBrowserEvent(pevent);
  }

}
