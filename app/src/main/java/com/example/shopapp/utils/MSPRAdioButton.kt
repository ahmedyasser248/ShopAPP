package com.example.shopapp.utils

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatRadioButton

class MSPRAdioButton (context : Context, attributeSet : AttributeSet) :AppCompatRadioButton(context ,attributeSet) {
    init{
        applyFont()
    }
    private fun applyFont(){
        val typeFace : Typeface = Typeface.createFromAsset(context.assets
        ,"Montserrat-Bold.ttf")
        setTypeface(typeFace)
    }
}