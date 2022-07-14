package com.kgandroid.roomwithcoroutine.utilities

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.databinding.BindingAdapter




class EditTextValidations {

    interface StringRule {
        fun validate(s: Editable?): Boolean
    }

    @BindingAdapter("android:watcher")
    fun bindTextWatcher(pEditText: EditText, pTextWatcher: TextWatcher?) {
        pEditText.addTextChangedListener(pTextWatcher)
    }

    @BindingAdapter(value = ["email:rule", "email:errorMsg"], requireAll = true)
    fun bindTextChange(pEditText: EditText, pStringRule: StringRule, msg: String?) {
        pEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
            }

            override fun afterTextChanged(s: Editable) {
                if (!pStringRule.validate(s)) {
                    pEditText.error = msg
                }
            }
        })
    }
    /*
    Your other custom binding method
     */
}