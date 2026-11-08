package ir.mostafa.baleotp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony

class SmsReceiver : BroadcastReceiver() {

    private val codeRe = Regex("""\b(\d{4,6})\b""")

    override fun onReceive(ctx: Context, intent: Intent) {
        if (intent.action != Telephony.Sms.Intents.SMS_RECEIVED_ACTION) return
        val prefs = ctx.getSharedPreferences("cfg", Context.MODE_PRIVATE)

        for (sms in Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
            val body = sms.messageBody ?: continue
            val code = codeRe.find(body)?.groupValues?.get(1) ?: continue
            Sender.push(prefs, code)
            break
        }
    }
}
