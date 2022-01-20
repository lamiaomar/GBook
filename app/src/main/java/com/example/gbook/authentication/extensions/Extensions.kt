package com.example.gbook.authentication.extensions

import android.text.TextUtils
import com.example.gbook.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout


enum class TextTypes {
    EMAIL, PASSWORD, RE_PASSWORD,
    FIRSTNAME, LASTNAME, DAY, YEAR
}

/*
*To avoid repetition use Extension function
 */
object Extensions {

    fun TextInputLayout.isValid(textInput: TextInputEditText, validationType: TextTypes): Boolean {

        val text = textInput.text.toString()
        val textNum = textInput.text
        when (validationType) {
            TextTypes.EMAIL -> {
                if (TextUtils.isEmpty(text) && !(textInput.text!!.contains("@" + "."))) {
                    this.error = this.context.getString(R.string.not_Valid_email_msg)
                    return false
                }
            }
            TextTypes.PASSWORD -> {
                if (TextUtils.isEmpty(text)) {
                    this.error = this.context.getString(R.string.not_Valid_pass_msg)
                    return false
                }
            }
            TextTypes.RE_PASSWORD -> {
                if (TextUtils.isEmpty(text)) {
                    this.error = this.context.getString(R.string.not_Valid_pass_msg)
                    return false
                }
            }
            TextTypes.FIRSTNAME -> {
                if (TextUtils.isEmpty(text) || textInput.text!!.length < 3) {
                    this.error = this.context.getString(R.string.not_Valid_name_msg)
                    return false
                }
            }
            TextTypes.LASTNAME -> {
                if (TextUtils.isEmpty(text) || textInput.text!!.length < 3) {
                    this.error = this.context.getString(R.string.not_Valid_name_msg)
                    return false
                }
            }

            TextTypes.DAY -> {
                if (TextUtils.isEmpty(textNum) || textInput.text.toString().toInt() > 31) {
                    this.error = this.context.getString(R.string.not_Valid_day_msg)
                    return false
                }
            }

            TextTypes.YEAR -> {
                if (TextUtils.isEmpty(textNum) || textInput.text.toString().toInt() > 2022) {
                    this.error = this.context.getString(R.string.not_Valid_name_msg)
                    return false
                }
            }

        }
        this.error = null
        return true
    }

}