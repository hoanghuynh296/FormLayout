package vn.semicolon.form

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import java.util.regex.Pattern


const val NOT_SET = -6969

class FormLayout : RelativeLayout, FormLayoutBehavior {
    private var mErrorViewIdForAll: Int? = null
    private var mRequiredErrorMessageForAll: String? = null
    private var mMaxValueErrorMessageForAll: String? = null
    private var mMinValueErrorMessageForAll: String? = null
    private var mMaxLengthErrorMessageForAll: String? = null
    private var mMinLengthErrorMessageForAll: String? = null
    private var mIsHideErrorMessageWhenOk = false
    private var mHideErrorMessageMode = View.GONE
    private var mIsShowToastForErrorMessage: Boolean? = null

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        val ta = context?.obtainStyledAttributes(attrs, R.styleable.FormLayout, 0, 0)
        try {
            mErrorViewIdForAll = ta?.getResourceId(R.styleable.FormLayout_errorViewIdForAll, NOT_SET)
            mRequiredErrorMessageForAll = ta?.getString(R.styleable.FormLayout_requiredErrorMessageForAll)
            mMaxValueErrorMessageForAll = ta?.getString(R.styleable.FormLayout_maxValueErrorMessageForAll)
            mMinValueErrorMessageForAll = ta?.getString(R.styleable.FormLayout_minValueErrorMessageForAll)
            mMaxLengthErrorMessageForAll = ta?.getString(R.styleable.FormLayout_maxLengthErrorMessageForAll)
            mMinLengthErrorMessageForAll = ta?.getString(R.styleable.FormLayout_minLengthErrorMessageForAll)
            mIsShowToastForErrorMessage = ta?.getBoolean(R.styleable.FormLayout_showToastForErrorMessage, false)
        } finally {
            ta?.recycle()
        }
    }


    override fun validate(): Boolean {
        var isOk = true
        for (i in 0 until childCount) {
            val view = getChildAt(i)
            if (view is TextView && view.visibility == View.VISIBLE) {
                val param = view.layoutParams as LayoutParams
                // handle case required
                if (param.isRequired) {
                    if (view.text.isEmpty()) {
                        if (!TextUtils.isEmpty(mRequiredErrorMessageForAll))
                            showError(view, mRequiredErrorMessageForAll!!)
                        else if (!TextUtils.isEmpty(param.requiredErrorMessage)) {
                            showError(view, param.requiredErrorMessage!!)
                        }
                        isOk = false
                        break
                    } else {
                        clearError(view)
                    }
                }
                // handle case max length
                if (param.maxLength != NOT_SET && view.text.isNotEmpty()) {
                    if (view.text.length > param.maxLength) {
                        if (!TextUtils.isEmpty(mMaxLengthErrorMessageForAll))
                            showError(view, mMaxLengthErrorMessageForAll!!)
                        else if (!TextUtils.isEmpty(param.maxLengthErrorMessage)) {
                            showError(view, param.maxLengthErrorMessage!!)
                        }
                        isOk = false
                        break
                    } else {
                        clearError(view)
                    }
                }
                // handle case min length
                if (param.minLength != NOT_SET && view.text.isNotEmpty()) {
                    if (view.text.length < param.minLength) {
                        if (!TextUtils.isEmpty(mMinLengthErrorMessageForAll))
                            showError(view, mMinLengthErrorMessageForAll!!)
                        else if (!TextUtils.isEmpty(param.minLengthErrorMessage)) {
                            showError(view, param.minLengthErrorMessage!!)
                        }
                        isOk = false
                        break
                    } else {
                        clearError(view)
                    }
                }
                // handle case min value
                if (param.minValue != NOT_SET && view.text.isNotEmpty()) {
                    if (isNumeric(view.text.toString())) {
                        if (view.text.toString().toInt() < param.minValue) {
                            if (!TextUtils.isEmpty(mMinValueErrorMessageForAll))
                                showError(view, mMinValueErrorMessageForAll!!)
                            else if (!TextUtils.isEmpty(param.minValueErrorMessage)) {
                                showError(view, param.minValueErrorMessage!!)
                            }
                            isOk = false
                            break
                        } else {
                            clearError(view)
                        }
                    } else {
                        continue
                        // wrong format - not a number
                    }

                }
                // handle case max value
                if (param.maxValue != NOT_SET && view.text.isNotEmpty()) {
                    if (isNumeric(view.text.toString())) {
                        if (view.text.toString().toInt() > param.maxValue) {
                            if (!TextUtils.isEmpty(mMaxValueErrorMessageForAll))
                                showError(view, mMaxValueErrorMessageForAll!!)
                            else if (!TextUtils.isEmpty(param.maxValueErrorMessage)) {
                                showError(view, param.maxValueErrorMessage!!)
                            }
                            isOk = false
                            break
                        } else {
                            clearError(view)
                        }
                    } else {
                        // wrong format - not a number
                    }

                }
                // handle case campare with another view's value
                if (param.compareWith != NOT_SET) {
                    val target = findViewById<TextView>(param.compareWith)
                    if (target != null) {
                        if (view.text.toString() == target.text.toString()) {
                            clearError(view)
                        } else {
                            if (!param.compareErrorMessage.isNullOrEmpty())
                                showError(view, param.compareErrorMessage!!)
                            isOk = false
                        }
                    } else throw Exception("Target to compare is null! Please check!")
                }
                // handle case validate format, ex: email, phone number,..
                if (!param.validateRegex.isNullOrEmpty()) {
                    if (isStringValid(view.text.toString(), FormLayoutRegex.validateRegex(param.validateRegex!!))) {
                        clearError(view)
                    } else {
                        if (!param.validateRegexErrorMessage.isNullOrEmpty())
                            showError(view, param.validateRegexErrorMessage!!)
                        isOk = false
                    }
                }
            }
        }
        return isOk
    }

    private fun clearError(view: View) {
        val params = view.layoutParams as LayoutParams
        val errorViewId = params.errorViewId
        val errorView: View? = findViewById(errorViewId)
        (errorView as? TextView)?.text = ""
        if (mIsHideErrorMessageWhenOk) errorView?.visibility = mHideErrorMessageMode
    }

    private fun showError(view: View, message: String) {
        view.requestFocus()
        val params = view.layoutParams as LayoutParams
        val errorViewId = params.errorViewId
        if (mIsShowToastForErrorMessage != null && mIsShowToastForErrorMessage!!) {
            Toast.makeText(context.applicationContext, message, Toast.LENGTH_SHORT).show()
        } else if (errorViewId != NOT_SET) {
            bindErrorText(errorViewId, message)
        } else if (mErrorViewIdForAll != NOT_SET) {
            bindErrorText(mErrorViewIdForAll!!, message)
        }
    }


    private fun bindErrorText(errorViewId: Int, message: String) {
        val errorView = findViewById<View>(errorViewId)
        if (errorView.visibility == View.GONE || errorView.visibility == View.INVISIBLE) {
            mHideErrorMessageMode = errorView.visibility
            mIsHideErrorMessageWhenOk = true
            errorView.visibility = View.VISIBLE
        }
        (errorView as? TextView)?.text = message
    }

    private fun isStringValid(string: String, regexToValidate: String): Boolean {
        val pattern = Pattern.compile(regexToValidate)
        val matcher = pattern.matcher(string)
        if (!matcher.matches()) {
            return false
        }
        return true
    }

    override fun generateLayoutParams(attrs: AttributeSet): LayoutParams {
        return FormLayout.LayoutParams(context, attrs)
    }

    override fun generateDefaultLayoutParams(): ViewGroup.LayoutParams {
        return FormLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun checkLayoutParams(p: ViewGroup.LayoutParams): Boolean {
        return p is LayoutParams
    }

    override fun generateLayoutParams(lp: ViewGroup.LayoutParams): ViewGroup.LayoutParams {
        return FormLayout.LayoutParams(lp)
    }


    class LayoutParams : RelativeLayout.LayoutParams {
        var isRequired: Boolean = false
        var maxLength: Int = NOT_SET
        var minLength: Int = NOT_SET
        var minValue: Int = NOT_SET
        var maxValue: Int = NOT_SET
        var errorViewId: Int = NOT_SET
        var compareWith: Int = NOT_SET
        var requiredErrorMessage: String? = null
        var maxValueErrorMessage: String? = null
        var minValueErrorMessage: String? = null
        var maxLengthErrorMessage: String? = null
        var minLengthErrorMessage: String? = null
        var compareErrorMessage: String? = null
        var validateRegex: String? = null
        var validateRegexErrorMessage: String? = null

        constructor() : super(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        constructor(width: Int, height: Int) : super(width, height)
        constructor(lp: ViewGroup.LayoutParams) : super(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
            val ta = context.obtainStyledAttributes(attrs, R.styleable.FormLayout_Layout, 0, 0)
            try {
                isRequired = ta.getBoolean(R.styleable.FormLayout_Layout_isRequired, false)
                maxLength = ta.getInt(R.styleable.FormLayout_Layout_maxLength, NOT_SET)
                minLength = ta.getInt(R.styleable.FormLayout_Layout_minLength, NOT_SET)
                minValue = ta.getInt(R.styleable.FormLayout_Layout_minValue, NOT_SET)
                maxValue = ta.getInt(R.styleable.FormLayout_Layout_maxValue, NOT_SET)
                compareWith = ta.getResourceId(R.styleable.FormLayout_Layout_compareWith, NOT_SET)
                errorViewId = ta.getResourceId(R.styleable.FormLayout_Layout_errorViewId, NOT_SET)
                requiredErrorMessage = ta.getString(R.styleable.FormLayout_Layout_requiredErrorMessage)
                maxValueErrorMessage = ta.getString(R.styleable.FormLayout_Layout_maxValueErrorMessage)
                minValueErrorMessage = ta.getString(R.styleable.FormLayout_Layout_minValueErrorMessage)
                maxLengthErrorMessage = ta.getString(R.styleable.FormLayout_Layout_maxLengthErrorMessage)
                minLengthErrorMessage = ta.getString(R.styleable.FormLayout_Layout_minLengthErrorMessage)
                compareErrorMessage = ta.getString(R.styleable.FormLayout_Layout_compareErrorMessage)
                validateRegex = ta.getString(R.styleable.FormLayout_Layout_validateRegex)
                validateRegexErrorMessage = ta.getString(R.styleable.FormLayout_Layout_validateRegexErrorMessage)
            } finally {
                ta.recycle()
            }
        }

    }

    private fun isNumeric(str: String): Boolean {
        return str.matches("-?\\d+(\\.\\d+)?".toRegex())  //match a number with optional '-' and decimal.
    }
}

object FormLayoutRegex {
    const val EMAIL = "^([_a-zA-Z0-9-]+(\\\\.[_a-zA-Z0-9-]+)*@[a-zA-Z0-9-]+(\\\\.[a-zA-Z0-9-]+)*(\\\\.[a-zA-Z]{1,6}))?\$"
    fun validateRegex(regex: String): String {
        when (regex) {
            "0" -> {
                return EMAIL
            }
        }
        return regex
    }
}

interface FormLayoutBehavior {
    fun validate(): Boolean
}