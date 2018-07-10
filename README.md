# FormLayout
Use this lib when you want validate your input as fast as possible (the same as Form in HTML, just use form.validate() for validation). See example below. 
# Installation
Add it in your root build.gradle at the end of repositories:

 ```
allprojects { 
		repositories { 
			... 
			maven { url 'https://jitpack.io' } 
		} 
}
```
Add the following dependency to your `build.gradle` file: 
```
dependencies {
    implementation 'com.github.hoanghuynh296:FormLayout:1.0'
}
```
# Usage
  In version 1.0, we can validate 3 cases for input:
  1. Required: this field can't be empty 
  2. Max/min length: input's length must be greater X or smaller Y value
  3. Max/min value: input value must be greater X or smaller Y value
## Layout your view
### Show all error in one text view
  >This layout extend Relative layout, so you will layout your view as Relative layout rule.
```xml
  <vn.semicolon.form.FormLayout
        android:id="@+id/registerForm"
        app:errorViewTagForAll="CommonError"
        android:layout_width="match_parent"
        android:layout_height="match_parent">
        <TextView
            android:tag="CommonError"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content" />
        <EditText
            app:requiredErrorMessage="You have to input this field"
            android:hint="Your name"
            android:id="@+id/etYourName"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            app:isRequired="true" />
        <EditText
            app:requiredErrorMessage="You have to input this field"
            android:hint="Your phone number"
            android:inputType="phone"
            android:maxLength="11"
            app:maxLengthErrorMessage="Phone number must be less than 12 characters"
            android:layout_below="@+id/etYourName"
            android:id="@+id/etYourPhoneNumber"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            app:isRequired="true" />
        <EditText
            app:minValueErrorMessage="Make sure that you're not under 18 years old"
            app:requiredErrorMessage="You have to input this field"
            android:hint="Your age"
            app:minValue="18"
            android:layout_width="match_parent"
            android:id="@+id/etYourAge"
            android:layout_below="@+id/etYourPhoneNumber"
            android:layout_height="wrap_content" />
        <Button
            android:id="@+id/btnRegister"
            android:layout_below="@+id/etYourAge"
            android:layout_width="match_parent"
            android:text="Register"
            android:layout_height="wrap_content" />
    </vn.semicolon.form.FormLayout>
```
### Each edit text has one error view 
```xml
 <vn.semicolon.form.FormLayout
        android:gravity="center"
        android:id="@+id/registerForm"
        android:layout_width="match_parent"
        android:layout_height="match_parent">
        <TextView
            android:visibility="gone"
            android:textSize="16sp"
            android:textColor="@android:color/holo_red_light"
            android:id="@+id/nameError"
            android:tag="NameError"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content" />
        <EditText
            app:errorViewTag="NameError"
            android:layout_below="@+id/error"
            app:requiredErrorMessage="You have to input this field"
            android:hint="Your name"
            android:id="@+id/etYourName"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            app:isRequired="true" />
        <TextView
            android:layout_below="@+id/etYourName"
            android:visibility="gone"
            android:textSize="16sp"
            android:textColor="@android:color/holo_red_light"
            android:id="@+id/phoneError"
            android:tag="phoneError"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content" />
        <EditText
            app:errorViewTag="phoneError"
            app:requiredErrorMessage="You have to input this field"
            android:hint="Your phone number"
            android:inputType="phone"
            app:maxLength="12"
            app:maxLengthErrorMessage="Phone number must be less than 12 characters"
            android:layout_below="@+id/phoneError"
            android:id="@+id/etYourPhoneNumber"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            app:isRequired="true" />
        <TextView
            android:layout_below="@+id/etYourPhoneNumber"
            android:visibility="gone"
            android:textSize="16sp"
            android:textColor="@android:color/holo_red_light"
            android:id="@+id/ageError"
            android:tag="AgeError"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content" />
        <EditText
            app:errorViewTag="AgeError"
            app:minValueErrorMessage="Make sure that you're not under 18 years old"
            android:hint="Your age"
            app:minValue="18"
            android:layout_width="match_parent"
            android:id="@+id/etYourAge"
            android:layout_below="@+id/ageError"
            android:layout_height="wrap_content" />
        <Button
            android:id="@+id/btnRegister"
            android:layout_below="@+id/etYourAge"
            android:layout_width="match_parent"
            android:text="Register"
            android:layout_height="wrap_content" />
    </vn.semicolon.form.FormLayout>
```
## Kotlin code
```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnRegister.setOnClickListener {
            if (registerForm.validate()) {
                // input data is ok, do something
            } else {
                // by default, FormLayout will show error message that you declare in xml
                // if you want do something else when input incorrect, do it here
            }
        }
}
 ```
 ### Result
 #### Show all error in one text view
 ![Each edit text has one error view](https://lh3.googleusercontent.com/1qpPGytsRNlW3GC4CQS1Z9qCoUPKoA3mN85AprUtXXs0je1pqqfUaM9mOIbXpxXptny0ONsPqYhHnDtkRD8u=w1920-h948-rw)
 
 #### Each edit text has one error view
 ![Each edit text has one error view](https://lh4.googleusercontent.com/iIIQtmmzqGC2eq0aQoGeUqAVqcdYNDZtVklbvW8_Pt-Ogin0i2TXbkdrkYmNzdEJQXwTk46AItX25RaZUTfU=w1920-h948-rw)
 # SOMETHING IMPORTANCE
1. This layout extends Relative layout, thus you will layout as Relative layout's rule
2. Use ```errorViewTag``` instead of ```errorViewId```, ```errorViewId``` will exist in next version
3. When you use ```errorViewTagForAll``` in root view and not declare ```errorViewTag``` for any child view, all error will be showed in the TextView that has the tag as you declare in root
4. Use ```app:errorViewTag="someTag"``` in every will when you want each input has it's error view
## Pros & Cons
Everything has its advantages and disadvantages, please consider carefully before use
### Pros: Easy to use, easy to validate 
### Cons: can't include neasted layout, I mean all EditText in neasted layout of FormLayout will not be validated, thus if your layout is not simple, skip it!
## What's next?
In version 1.0, there're many inconveniences:
1. Can't use String resource id in ```app:requiredErrorMessage```, ```app:maxLengthErrorMessage```,...
2. Only 3 cases to validate
3. Use tag to determine error view (maybe you like using @id instead)  
Don't worry, I'll update it to version 1.0.1, and everything will be ok
# RATE ME IF YOU FEEL IT IS USEFUL, THANK YOU!



