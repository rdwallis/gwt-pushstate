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

    PushStateHistorianImpl() {
        initToken();
        registerPopstateHandler();
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
        setToken(Window.Location.getPath() + Window.Location.getQueryString());
        replaceState(getToken());
    }

    private static native void replaceState(final String token) /*-{
        $wnd.history.replaceState({'token':token}, $doc.title, token);
    }-*/;

    private static native void pushState(final String token) /*-{
        $wnd.history.pushState({'token':token}, $doc.title, token);
    }-*/;

    private boolean setToken(String newToken) {
        newToken = newToken.startsWith("/") ? newToken.substring(1): newToken;
        if (!newToken.equals(token)) {
            token = newToken;
            return true;
        }
        return false;
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> valueChangeHandler) {
        return this.handlers.addHandler(ValueChangeEvent.getType(), valueChangeHandler);
    }

    @Override
    public String getToken() {
        return token;
    }

    @Override
    public void newItem(String token, boolean issueEvent) {
        if (setToken(token)) {
            pushState(token);
    
            if (issueEvent) {
                ValueChangeEvent.fire(this, getToken());
            }
        }
    }

    @Override
    public void fireEvent(GwtEvent<?> event) {
        this.handlers.fireEvent(event);
    }
}
