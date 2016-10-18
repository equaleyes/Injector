dsadsatesttrestsa
![Injector Logo](http://i.imgur.com/QDhSvvm.png)
An Android library for fast binding of views and their OnClick events

[![Build Status](https://travis-ci.org/equaleyes/Injector.png?branch=master)](https://travis-ci.org/equaleyes/Injector)[ ![Download](https://api.bintray.com/packages/zskamljic/maven/Injector/images/download.svg) ](https://bintray.com/zskamljic/maven/Injector/_latestVersion)

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
@Inject(R.id.custom_id) private TextView mMyTextView;
```

This will look for a `TextView` (or any subclass) with the id `R.id.custom_id`.

#### Injecting arrays and lists:
```java
// Using an array:
@InjectGroup({R.id.text1, R.id.text2, R.id.text3})
private View[] mViewArray;

// Or using a list:
@InjectGroup({R.id.text1, R.id.text2, R.id.text3})
private List<View> mTextViewsList;

// Using an array of specific type:
@InjectGroup({R.id.text1, R.id.text2, R.id.text3})
private TextView[] mTypedArray;

// Using a list of specific type:
@InjectGroup({R.id.text1, R.id.text2, R.id.text3})
private List<TextView> mTextViewList;

// Proceed to inject the usual way. Any click listeners will be added as usual.
```

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

#### If any of the views are not found they are *NOT* injected. There Injector.inject call will report a warning with a tag "INJECTOR". This allows you to use multiple xml layouts that don't necessarily have the same views.

### To use Injector simply add the following to build.gradle dependencies:
```groovy
compile 'com.equaleyes.injector:injector:1.2.0'
```

Or if you use Maven:

```
<dependency>
  <groupId>com.equaleyes.injector</groupId>
  <artifactId>injector</artifactId>
  <version>1.2.0</version>
  <type>pom</type>
</dependency>
```

#### Changelog
```
Version
1.2.0:  - Added @InjectRes annotation to inject Drawables, Strings or colors

1.1.0:  - Added @InjectGroup annotation to inject arrays and lists of views
        - Added searching for android.R.id.* if the id is not found in usual R.id.* class
        - The views that were not injected are now reported using a warning
        
1.0.0:  - Initial release
```

### License

```
Copyright 2016 Equaleyes Solutions Ltd.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

Author: [Žan Skamljič](https://github.com/zskamljic)
