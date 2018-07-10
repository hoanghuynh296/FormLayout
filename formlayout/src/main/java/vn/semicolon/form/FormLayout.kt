package vn.semicolon.form

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView


const val NOT_SET = -6969

class FormLayout : RelativeLayout, FormLayoutBehavior {
    private var mErrorViewTagForAll: String? = null
    private var mRequiredErrorMessageForAll: String? = null
    private var mMaxValueErrorMessageForAll: String? = null
    private var mMinValueErrorMessageForAll: String? = null
    private var mMaxLengthErrorMessageForAll: String? = null
    private var mMinLengthErrorMessageForAll: String? = null
    private var mIsHideErrorMessageWhenOk = false
    private var mHideErrorMessageMode = View.GONE

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        val ta = context?.obtainStyledAttributes(attrs, R.styleable.FormLayout, 0, 0)
        try {
            mErrorViewTagForAll = ta?.getString(R.styleable.FormLayout_errorViewTagForAll)
            mRequiredErrorMessageForAll = ta?.getString(R.styleable.FormLayout_requiredErrorMessageForAll)
            mMaxValueErrorMessageForAll = ta?.getString(R.styleable.FormLayout_maxValueErrorMessageForAll)
            mMinValueErrorMessageForAll = ta?.getString(R.styleable.FormLayout_minValueErrorMessageForAll)
            mMaxLengthErrorMessageForAll = ta?.getString(R.styleable.FormLayout_maxLengthErrorMessageForAll)
            mMinLengthErrorMessageForAll = ta?.getString(R.styleable.FormLayout_minLengthErrorMessageForAll)
        } finally {
            ta?.recycle()
        }
    }

    override fun validate(): Boolean {
        var isOk = true
        for (i in 0 until childCount) {
            val view = getChildAt(i) ?: continue
            if (view is TextView) {
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
            }
        }
        return isOk
    }

    private fun clearError(view: View) {
        val params = view.layoutParams as LayoutParams
        val errorViewTag = params.errorViewTag
        var errorView: View? = null
        if (!TextUtils.isEmpty(errorViewTag)) {
            errorView = findViewWithTag<View>(errorViewTag)
        } else if (!TextUtils.isEmpty(mErrorViewTagForAll)) {
            errorView = findViewWithTag<View>(mErrorViewTagForAll)
        }
        if (errorView != null && errorView is TextView) {
            errorView.text = ""
            if (mIsHideErrorMessageWhenOk) errorView.visibility = mHideErrorMessageMode
        }
    }

    private fun showError(view: View, message: String) {
        view.requestFocus()
        val params = view.layoutParams as LayoutParams
        val errorViewTag = params.errorViewTag

        if (!TextUtils.isEmpty(errorViewTag)) {
            bindErrorText(errorViewTag!!, message)
        } else if (!TextUtils.isEmpty(mErrorViewTagForAll)) {
            bindErrorText(mErrorViewTagForAll!!, message)
        }
    }


    private fun bindErrorText(errorViewTag: String, message: String) {
        val errorView = findViewWithTag<View>(errorViewTag)
        if (errorView.visibility == View.GONE || errorView.visibility == View.INVISIBLE) {
            mHideErrorMessageMode = errorView.visibility
            mIsHideErrorMessageWhenOk = true
            errorView.visibility = View.VISIBLE
        }
        if (errorView is TextView) {
            errorView.text = message
        } else {
            throw Exception("Error view should be a TextView")
        }
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
        var errorViewTag: String? = null
        var requiredErrorMessage: String? = null
        var maxValueErrorMessage: String? = null
        var minValueErrorMessage: String? = null
        var maxLengthErrorMessage: String? = null
        var minLengthErrorMessage: String? = null

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
                errorViewId = ta.getInt(R.styleable.FormLayout_Layout_errorViewId, NOT_SET)
                errorViewTag = ta.getString(R.styleable.FormLayout_Layout_errorViewTag)
                requiredErrorMessage = ta.getString(R.styleable.FormLayout_Layout_requiredErrorMessage)
                maxValueErrorMessage = ta.getString(R.styleable.FormLayout_Layout_maxValueErrorMessage)
                minValueErrorMessage = ta.getString(R.styleable.FormLayout_Layout_minValueErrorMessage)
                maxLengthErrorMessage = ta.getString(R.styleable.FormLayout_Layout_maxLengthErrorMessage)
                minLengthErrorMessage = ta.getString(R.styleable.FormLayout_Layout_minLengthErrorMessage)
            } finally {
                ta.recycle()
            }
        }

    }

    fun isNumeric(str: String): Boolean {
        return str.matches("-?\\d+(\\.\\d+)?".toRegex())  //match a number with optional '-' and decimal.
    }
}

interface FormLayoutBehavior {
    fun validate(): Boolean
}