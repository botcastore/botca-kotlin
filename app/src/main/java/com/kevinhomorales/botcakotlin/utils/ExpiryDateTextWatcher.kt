package com.kevinhomorales.botcakotlin.utils

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

class ExpiryDateTextWatcher(private val editText: EditText) : TextWatcher {
    private val maxLength = 5 // Longitud máxima del campo de fecha de vencimiento

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    override fun afterTextChanged(editable: Editable?) {
        editText.removeTextChangedListener(this)

        val text = editable.toString()
        if (text.length <= maxLength) {
            val formattedText = formatExpiryDate(text)
            editText.setText(formattedText)
            editText.setSelection(formattedText.length) // Mantener el cursor al final
        }

        editText.addTextChangedListener(this)
    }

    private fun formatExpiryDate(input: String): String {
        val filteredText = input.replace("/", "") // Eliminar barras existentes
        val formatted = StringBuilder()
        for (i in filteredText.indices) {
            formatted.append(filteredText[i])
            if (i == 1 && i != filteredText.length - 1) {
                formatted.append("/") // Agregar "/" después de los primeros dos caracteres
            }
        }
        return formatted.toString()
    }
}