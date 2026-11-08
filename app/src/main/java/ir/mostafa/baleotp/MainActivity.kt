package ir.mostafa.baleotp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefs = getSharedPreferences("cfg", MODE_PRIVATE)
        val d = resources.displayMetrics.density
        val pad = (16 * d).toInt()

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(pad, pad, pad, pad)
        }

        fun label(t: String) = TextView(this).apply { text = t }

        val ip = EditText(this).apply {
            setText(prefs.getString("ip", "192.168.70.177"))
            hint = "IP کامپیوتر"
        }
        val port = EditText(this).apply {
            setText(prefs.getString("port", "8720"))
            inputType = InputType.TYPE_CLASS_NUMBER
            hint = "پورت"
        }
        val token = EditText(this).apply {
            setText(prefs.getString("token", ""))
            hint = "توکن مخفی (مثل relay)"
        }
        val bot = EditText(this).apply {
            setText(prefs.getString("bot", ""))
            hint = "نام چت بات (خالی = همه‌ی بله)"
        }
        val pkg = EditText(this).apply {
            setText(prefs.getString("pkg", "ir.nasim"))
            hint = "پکیج بله (معمولاً ir.nasim)"
        }

        val save = Button(this).apply {
            text = "ذخیره"
            setOnClickListener {
                prefs.edit()
                    .putString("ip", ip.text.toString().trim())
                    .putString("port", port.text.toString().trim())
                    .putString("token", token.text.toString().trim())
                    .putString("bot", bot.text.toString().trim())
                    .putString("pkg", pkg.text.toString().trim())
                    .apply()
                Toast.makeText(this@MainActivity, "ذخیره شد", Toast.LENGTH_SHORT).show()
            }
        }

        val access = Button(this).apply {
            text = "فعال‌سازی دسترسی به نوتیفیکیشن"
            setOnClickListener {
                startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
            }
        }

        root.addView(label("IP کامپیوتر")); root.addView(ip)
        root.addView(label("پورت")); root.addView(port)
        root.addView(label("توکن مخفی")); root.addView(token)
        root.addView(label("نام چت بات")); root.addView(bot)
        root.addView(label("پکیج بله")); root.addView(pkg)
        root.addView(save)
        root.addView(access)

        setContentView(root)
    }
}
