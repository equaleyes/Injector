![Injector Logo](http://i.imgur.com/QDhSvvm.png)
An Android library for fas binding of views and their OnClick events

### Using Injector:

#### Basic usage:
```java
// At field declaration:
@Inject private TextView mMyTextView;

// When you want to bind them(for example activity onCreate()):
Injector.inject(mActivity); // Can also use instance of View as a parameter if desired

```

This will look for a TextView(or any subclass) with the following IDs: mMyTextView, mmytextview, my_text_view and inject them to the annotated field.

#### Specifying custom id:
```java
// At field declaration
@Inject(id=R.id.custom_id) private TextView mMyTextView;
```

This will look for a TextView(or any subclass) with the id R.id.custom_id.

#### On click events:
If you so desire, you can use Injector to automatically bind the OnClickListener like this:

1. Set the xml property of the view `android:clickable="true"`
2. Define an View.OnClickListener (can also pass any class implementing this instance)

The only difference is the injecting part:

```java
// Inject with mListener, which is an instance of a class implementing View.OnClickListener:
Injector.inject(mActivity, mListener); // Again, View can be used instead of an activity
```

#### Using Injector with ViewHolders:

Injector can also be used with ViewHolders, you can declare fields as usual then use the following code:
```java
// mContainer is the class that declares any views you may want to inject
// mView is the view containing the subviews that you want to bind to the container
Injector.injectToFrom(mContainer, mView);
Injector.injectToFrom(mContainer, mView, mListener); // Can also use View.OnClickListener
```
