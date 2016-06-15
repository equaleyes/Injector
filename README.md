![Injector Logo](http://i.imgur.com/QDhSvvm.png)
An Android library for fast binding of views and their OnClick events

[![Build Status](https://travis-ci.org/equaleyes/Injector.png?branch=master)](https://travis-ci.org/equaleyes/Injector)

### Using Injector:

#### Basic usage:
```java
// At field declaration:
@Inject private TextView mMyTextView;

// When you want to bind them(for example activity onCreate()):
Injector.inject(mActivity); // Can also use instance of View as a parameter if desired

```

This will look for a `TextView` (or any subclass) with the following IDs: `mMyTextView`, `mmytextview`, `my_text_view` and inject them to the annotated field.

#### Specifying a custom id:
```java
// At field declaration
@Inject(id=R.id.custom_id) private TextView mMyTextView;
```

This will look for a `TextView` (or any subclass) with the id `R.id.custom_id`.

#### Click events:
If you so desire, you can use Injector to automatically bind the `OnClickListener` like this:

1. Set the XML property of the view `android:clickable="true"`
2. Define a `View.OnClickListener` (You can also pass any class implementing this instance)

The only difference is the injecting part:

```java
// Inject with mListener, which is an instance of a class implementing View.OnClickListener:
Injector.inject(mActivity, mListener); // Again, View can be used instead of an activity
```

#### Using Injector with ViewHolders:

Injector can also be used with `ViewHolders`, you can declare fields as usual then use the following code:
```java
// mContainer is the class that declares any views you may want to inject
// mView is the view containing the subviews that you want to bind to the container
Injector.injectToFrom(mContainer, mView);
Injector.injectToFrom(mContainer, mView, mListener); // Can also use View.OnClickListener
```

### To use Injector simply add the following to build.gradle dependencies:
```groovy
compile 'com.equaleyes.injector:injector:1.0.0'
```

Or if you use Maven:

```
<dependency>
  <groupId>com.equaleyes.injector</groupId>
  <artifactId>injector</artifactId>
  <version>1.0.0</version>
  <type>pom</type>
</dependency>
```
