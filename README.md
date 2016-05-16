# gwt-pushstate

gwt-pushstate implements easy to use HTML5 pushState support for GWT projects.

## Install

Add the gwt-pushstate dependency to your project:

    <dependency>
      <groupId>de.knightsoft-net</groupId>
      <artifactId>gwt-pushstate</artifactId>
      <version>2.0.0.6</version>
    </dependency>

And inherit the PushState module in your GWT module:

    <inherits name="com.wallissoftware.pushstate.PushState" />
    
If you're using GWTP then you must put the inherits PushState tag after the inherits Mvp tag.


## Use *Historian* not *History*!

gwt-pushstate replaces the `Historian` interface with a PushState Implementation. Because of this you must use the `Historian` interface.

You can create an `Historian` using `GWT.create`:

`Historian historian = GWT.create(Historian.class)`

or by injecting with gin:

```
@Inject
ctor(Historian historian) {
```

then use `historian` where you used `History` before:

`historian.newItem(token, true)`

The PlaceManagers for GWT and GWTP (1.4 or above) will work with gwt-pushstate without any modification.

### Setting relative path

If you're serving your app from a relative path then you need to call `PushStateHistorian.setRelativePath('/path')` in your BootStrapper or EntryPoint before making any calls to `Historian`.

### GWTP 1.3.1 and below

The placemanager for GWTP 1.3.1 and below does not use the `Historian` interface and will not work with gwt-pushstate 2.0.0

Instead use gwt-pushstate 1.1.4:

```
<dependency>
  <groupId>com.wallissoftware</groupId>
  <artifactId>gwt-pushstate</artifactId>
  <version>1.1.4</version>
</dependency>
```

Due to changes to GWT's History class 1.1.4 is incompatible with GWT 2.7 and higher.
