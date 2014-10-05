package com.wallissoftware.pushstate.client;

import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.place.shared.PlaceHistoryHandler.Historian;

public class PushStateHistorian implements Historian, HasValueChangeHandlers<String> {
    
    private final static PushStateHistorianImpl IMPL = new PushStateHistorianImpl(); 

    @Override
    public void fireEvent(GwtEvent<?> event) {
        IMPL.fireEvent(event);
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> valueChangeHandler) {
        return IMPL.addValueChangeHandler(valueChangeHandler);
    }

    @Override
    public String getToken() {
        return IMPL.getToken();
    }

    @Override
    public void newItem(String token, boolean issueEvent) {
        IMPL.newItem(token, issueEvent);        
    }

}
