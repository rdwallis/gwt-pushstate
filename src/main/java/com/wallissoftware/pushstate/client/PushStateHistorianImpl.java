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
    
    
    PushStateHistorianImpl(String relativePath) {
        relativePath = relativePath.startsWith("/") ? relativePath: "/" + relativePath;
        relativePath = relativePath.endsWith("/") ? relativePath: relativePath + "/";
        this.relativePath = relativePath;
        initToken();
        registerPopstateHandler();
    }

    @Override
    public String getToken() {
        return token;
    }

    @Override
    public void newItem(String token, boolean issueEvent) {
        if (setToken(token)) {
            pushState(relativePath, getToken());
    
            if (issueEvent) {
                ValueChangeEvent.fire(this, getToken());
            }
        }
    }

    @Override
    public void fireEvent(GwtEvent<?> event) {
        this.handlers.fireEvent(event);
    }

    private native void registerPopstateHandler()/*-{
        var that = this;
        var oldHandler = $wnd.onpopstate;
        $wnd.onpopstate = $entry(function(e) {
            that.@com.wallissoftware.pushstate.client.PushStateHistorianImpl::onPopState(Ljava/lang/String;)(e.state.token);
            if (oldHandler) {
                oldHandler(e);
            }
        });
    }-*/;

    private void onPopState(String token) {
        if (setToken(token)) {
            ValueChangeEvent.fire(this, getToken());
        }
    }

    private void initToken() {
        String token = Window.Location.getPath() + Window.Location.getQueryString();
        
        setToken(token);
        replaceState(relativePath, getToken());
    }
    
    private String stripStartSlash(String input) {
        return input.startsWith("/") ? input.substring(1):input;
    }
    
    private String stripRelativePath(String token) {
        String relPath = stripStartSlash(relativePath);
        token = stripStartSlash(token);
        
        if (token.startsWith(relPath)) {
            return stripStartSlash(token.substring(relPath.length()));
        }
        return token;
    }
    
    private static native void replaceState(final String relativePath, final String token) /*-{
        $wnd.history.replaceState({'token':token}, $doc.title, relativePath + token);
    }-*/;

    private static native void pushState(final String relativePath, final String token) /*-{
        $wnd.history.pushState({'token':token}, $doc.title,  relativePath + token);
    }-*/;
    
    private boolean setToken(String newToken) {
        newToken = stripRelativePath(newToken);
        if (!matchesToken(newToken)) {
            token = newToken;
            return true;
        }
        return false;
    }

    private boolean matchesToken(String compare) {
        return token != null && (compare.equals(token) || compare.equals(token + "/") || token.equals(compare + "/"));
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> valueChangeHandler) {
        return this.handlers.addHandler(ValueChangeEvent.getType(), valueChangeHandler);
    }
}
