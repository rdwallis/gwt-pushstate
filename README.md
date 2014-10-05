# gwt-pushstate

gwt-pushstate implements easy to use HTML5 pushState support for GWT projects.


## License

    Copyright 2014 Richard Wallis
    
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    
          http://www.apache.org/licenses/LICENSE-2.0
    
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.


## Install

Checkout this project and run `mvn install`

Add the gwt-pushstate dependency to your project:

    <dependency>
      <groupId>com.wallissoftware</groupId>
      <artifactId>gwt-pushstate</artifactId>
      <version>2.0.0-SNAPSHOT</version>
    </dependency>

And inherit the PushState module in your GWT module:

    <inherits name="com.wallissoftware.pushstate.PushState" />


## Don't call History directly

gwt-pushstate replaces the Historian interface with a PushState Implementation if you make a direct call to History gwt-pushstate will not work.

Instead create an Historian:

`Historian historian = GWT.create(Historian.class)`

then use historian where you used History before:

`historian.newItem(token, true)`

The place managers for GWT and GWTP (1.4 or above) will work with gwt-pushstate without any modification.
