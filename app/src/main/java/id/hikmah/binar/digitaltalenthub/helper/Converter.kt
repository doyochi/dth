package id.hikmah.binar.digitaltalenthub.helper

import java.text.NumberFormat
import java.util.*


class Converter {
    companion object {
        fun rupiah(number: Int): String{
            val localeID =  Locale("in", "ID")
            val numberFormat = NumberFormat.getCurrencyInstance(localeID)
            return numberFormat.format(number).toString()
        }

        fun dollar(number: Int): String{
            val localeID = Locale("eng", "US")
            val numberFormat = NumberFormat.getCurrencyInstance(localeID)
            return numberFormat.format(number).toString()
        }
    }
}
