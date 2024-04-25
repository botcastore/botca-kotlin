package com.kevinhomorales.botcakotlin.utils

import android.app.AlertDialog
import android.content.Context
import android.view.ContextThemeWrapper
import android.widget.EditText
import com.kevinhomorales.botcakotlin.R

class Alerts {
    companion object {
        fun warning(title: String, message: String, context: Context, completion: (() -> Unit)? = null) {
            val builder = AlertDialog.Builder(ContextThemeWrapper(context, R.style.AlertDialogTheme))
            builder.setTitle(title)
            builder.setMessage(message)
            builder.setCancelable(false)
            builder.setPositiveButton(Constants.ok) { dialog, which ->
                if (completion == null) {
                    return@setPositiveButton
                }
                completion!!()
            }
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }

        fun twoOptions(title: String, message: String, positiveTitle: String, negativeTitle: String, context: Context, completion: ((Boolean) -> Unit)? = null) {
            val builder = AlertDialog.Builder(ContextThemeWrapper(context, R.style.AlertDialogTheme))
            builder.setTitle(title)
            builder.setMessage(message)
            builder.setPositiveButton(positiveTitle) { dialog, which ->
                completion!!(true)
            }
            builder.setNegativeButton(negativeTitle) { dialog, which ->
                completion!!(false)
            }
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }

        fun showAlertWithEditText(context: Context, completion: ((String) -> Unit)? = null) {
            val editText = EditText(context)
            val builder = AlertDialog.Builder(ContextThemeWrapper(context, R.style.AlertDialogTheme))
            builder.setTitle("Add Coupon")
            builder.setMessage("Please enter the reference code")
            builder.setView(editText)
            builder.setPositiveButton("Add") { dialog, _ ->
                    val message = editText.text.toString()
                    if (completion != null) {
                        completion(message)
                    }
                    dialog.dismiss()
                }
            builder.setNegativeButton(context.getString(R.string.cancel)) { dialog, _ ->
                    dialog.dismiss()
                }
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }

//        fun toNotifications(title: String, message: String, context: Context) {
//            val alert = AlertDialog.Builder(context)
//            alert.setTitle(title)
//            alert.setMessage(message)
//
//            val layout = LinearLayout(context)
//            layout.orientation = LinearLayout.VERTICAL
//
//            val notificationTitle = EditText(context)
//            notificationTitle.setSingleLine()
//            notificationTitle.hint = "Título"
//            layout.addView(notificationTitle)
//
//            val notificationMessage = EditText(context)
//            notificationMessage.setSingleLine()
//            notificationMessage.hint = "Mensaje"
//            layout.addView(notificationMessage)
//
//            layout.setPadding(50, 40, 50, 10)
//
//            alert.setView(layout)
//
//            alert.setPositiveButton(context.getString(R.string.notification_send_button)) { _, _ ->
//                val notificationTitleText = notificationTitle.text.toString()
//                val notificationMessageText = notificationMessage.text.toString()
//                if (notificationTitleText.isEmpty() || notificationMessageText.isEmpty()) {
//                    warning(context.getString(R.string.error_title), context.getString(R.string.complete_fields), context)
//                    return@setPositiveButton
//                }
//                FirebaseNotificationsManager.sendMessage(notificationTitleText,  notificationMessageText, "admin")
//                FirebaseNotificationsManager.sendMessage(notificationTitleText,  notificationMessageText, "employee")
//                FirebaseNotificationsManager.sendMessage(notificationTitleText,  notificationMessageText, "user")
//            }
//
//            alert.setNegativeButton(Constants.cancel) { dialog, _ ->
//                dialog.dismiss()
//            }
//
//            alert.setCancelable(false)
//            alert.show()
//        }


    }
}