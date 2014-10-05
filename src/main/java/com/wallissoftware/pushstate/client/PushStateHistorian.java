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
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.place.shared.PlaceHistoryHandler.Historian;

public class PushStateHistorian implements Historian, HasValueChangeHandlers<String> {
    
    private static String relativePath = "";

    /**
     * Call this method in your entry point or bootstrapper to set the relative path of your application.
     * @param relativePath - the relative path of your app.
     */
    public static void setRelativePath(String relativePath) {
        assert(IMPL != null) : "You must set relative path before using any history method";
        PushStateHistorian.relativePath = relativePath;
    };
    
    private static PushStateHistorianImpl IMPL; 

    @Override
    public void fireEvent(GwtEvent<?> event) {
        getImpl().fireEvent(event);
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> valueChangeHandler) {
        return getImpl().addValueChangeHandler(valueChangeHandler);
    }

    @Override
    public String getToken() {
        return getImpl().getToken();
    }

    @Override
    public void newItem(String token, boolean issueEvent) {
        getImpl().newItem(token, issueEvent);        
    }
    
    private static PushStateHistorianImpl getImpl() {
        if (IMPL == null) {
            IMPL = new PushStateHistorianImpl(relativePath);
        }
        return IMPL;
    }

}
