package com.kevinhomorales.botcakotlin.utils

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

class CreditCardTextWatcher(private val editText: EditText) : TextWatcher {
    private val maxLength = 16 // Longitud máxima del número de tarjeta
    private val spaceEvery = 4 // Espacio después de cada 4 caracteres

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    override fun afterTextChanged(editable: Editable?) {
        editText.removeTextChangedListener(this)

        val text = editable.toString()
        if (text.length <= maxLength) {
            val formattedText = formatCreditCardNumber(text)
            editText.setText(formattedText)
            editText.setSelection(formattedText.length) // Mantener el cursor al final
        }

        editText.addTextChangedListener(this)
    }

    private fun formatCreditCardNumber(input: String): String {
        val filteredText = input.replace("\\s".toRegex(), "") // Eliminar espacios existentes
        val formatted = StringBuilder()
        for (i in filteredText.indices) {
            formatted.append(filteredText[i])
            if ((i + 1) % spaceEvery == 0 && i != filteredText.length - 1) {
                formatted.append(" ") // Agregar espacio después de cada 4 caracteres
            }
        }
        return formatted.toString()
    }
}