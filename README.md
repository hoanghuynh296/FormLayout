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
  1. ```isRequired="boolean"```: this field can't be empty 
  2. ```minLength="int"```: input's length must be greater X value
  3. ```minLengthErrorMessage="string|reference"``` error message for minimum length error 
  4. ```minLengthErrorMessageForAll="string|reference"``` error message for minimum length error for all input
  5. ```maxLength="int"```: input's length must be smaller X value
  6. ```maxLengthErrorMessage="string|reference"``` error message for maxium length error 
  7. ```maxLengthErrorMessageForAll="string|reference"``` error message for max length error for all input
  8. ```minValue="int"```: input's value must be greater X value
  9. ```minValueErrorMessage="string|reference"``` error message for minimum value error
  10. ```minValueErrorMessageForAll="string|reference"``` error message for minimum value error for all input
  11. ```maxValue="int"```: input's value must be smaller X value
  12. ```maxValueErrorMessage="string|reference"``` error message for maxium value error 
  13. ```maxValueErrorMessageForAll="string|reference"``` error message for maxium value error for all input
  #### [UPDATE 27/10/2018]
  14. ```validateRegex="string|enum|reference"```: regex pattern to check (as String, Enum(email) or reference to String in strings.xml). 
  >Ex: ```validateRegex="email"``` to validate format input should be email format or </br>
  >Ex: ```validateRegex="^([_a-zA-Z0-9-]+(\\.[_a-zA-Z0-9-]+)*@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*(\\.[a-zA-Z]{1,6}))?$"``` 
  15. ```validateRegexErrorMessage="string|reference"``` error message for regex error
  16. ```compareWith="reference"``` to compare value between views. Ex: compare password and confirm password
  17. ```compareErrorMessage="string|reference"``` error message for comparation error
  18. ```showToastForErrorMessage="boolean"``` set it to true if you want show error message as toast instead of display in text view
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
 ![Each edit text has one error view](http://i1214.photobucket.com/albums/cc484/giuse96suoire/Screenshot_2018-07-10-14-18-33-086_vn.semicolon.semicolonlib_zpspeyhgg7y.png)

 #### Each edit text has one error view
 ![Each edit text has one error view](http://i1214.photobucket.com/albums/cc484/giuse96suoire/Screenshot_2018-07-10-14-27-47-337_vn.semicolon.semicolonlib_zpsmrvvqndv.png)
 # SOMETHING IMPORTANCE
1. This layout extends Relative layout, thus you will layout as Relative layout's rule </br>
~~2. Use ```errorViewTag``` instead of ```errorViewId```, ```errorViewId``` will exist in next version~~ 
2. [New update] can use both ```errorViewTag="string"``` or ```errorViewId="reference"``` to specify error view 
3. When you use ```errorViewTagForAll``` in root view and not declare ```errorViewTag``` for any child view, all error will be showed in the TextView that has the tag as you declare in root
4. Use ```errorViewTag="string"``` or ```errorViewId="reference"``` in every view when you want each input view has it's error view
## Pros & Cons
Everything has its advantages and disadvantages, please consider carefully before use
### Pros: Easy to use, easy to validate 
### Cons: can't include neasted layout, it's mean all EditText in neasted layout of FormLayout will not be validated, thus if your layout is not simple, skip it! See example below.
```xml
  <vn.semicolon.form.FormLayout
        android:id="@+id/registerForm"
        app:errorViewTagForAll="CommonError"
        android:layout_width="match_parent"
        android:layout_height="match_parent">
        <EditText // this input is ok
            app:requiredErrorMessage="You have to input this field"
            android:hint="Your name"
            android:id="@+id/etYourName"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            app:isRequired="true" />
	<FrameLayout
	    android:layout_width="match_parent"
            android:layout_height="match_parent">
		<EditText  // this input is not ok, can't validate this field
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
	</FrameLayout>
    </vn.semicolon.form.FormLayout>
```
## What's next?
In version 1.0, there're many inconveniences: [Fixed in version 1.0.1] [27/10/2018]
~~1. Can't use String resource id in ```app:requiredErrorMessage```, ```app:maxLengthErrorMessage```,...
2. Only 3 cases to validate
3. Use tag to determine error view (maybe you like using @id instead)~~  
In version 1.1, there're many inconveniences: 
1. Can't validate input in nestest view group in FormLayout
Don't worry, I'll update it to version ~~1.0.1~~ 1.0.2, and everything will be ok
# RATE ME IF YOU FEEL IT IS USEFUL, THANK YOU!



