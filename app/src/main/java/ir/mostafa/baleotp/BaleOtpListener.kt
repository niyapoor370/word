package ir.mostafa.baleotp

import android.app.Notification
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification

class BaleOtpListener : NotificationListenerService() {

    private val codeRe = Regex("""\b(\d{4,6})\b""")

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        val prefs = getSharedPreferences("cfg", MODE_PRIVATE)
        val pkg = prefs.getString("pkg", "ir.nasim") ?: "ir.nasim"
        if (sbn.packageName != pkg) return

        val ex = sbn.notification.extras
        val title = ex.getString(Notification.EXTRA_TITLE) ?: ""
        val text = ex.getCharSequence(Notification.EXTRA_TEXT)?.toString() ?: ""

        // اگر نام بات تنظیم شده، فقط همان چت را قبول کن
        val botFilter = prefs.getString("bot", "") ?: ""
        if (botFilter.isNotEmpty() && !title.contains(botFilter, ignoreCase = true)) return

        val code = codeRe.find(text)?.groupValues?.get(1) ?: return
        Sender.push(prefs, code)
    }
}
