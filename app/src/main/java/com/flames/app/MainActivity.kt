package com.flames.app

import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView

class MainActivity : AppCompatActivity() {
    private lateinit var content: LinearLayout
    private lateinit var nav: LinearLayout
    private var currentIndex = 0

    private val profiles by lazy {
        listOf(
            Profile("Amara", 24, "2 km away", "Coffee, sunsets and good conversations. Looking for something genuine.", listOf("Coffee", "Travel", "Music"), "A", R.color.flames_red),
            Profile("Zainab", 26, "4 km away", "Creative soul who loves food adventures and spontaneous road trips.", listOf("Food", "Art", "Travel"), "Z", R.color.flames_orange),
            Profile("Chidi", 25, "6 km away", "Tech by day, football by weekend. Always down for a new experience.", listOf("Football", "Tech", "Movies"), "C", R.color.flames_yellow),
            Profile("Maya", 23, "8 km away", "Book lover, gym regular and amateur photographer. Let's make memories.", listOf("Books", "Fitness", "Photos"), "M", R.color.flames_red)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = color(R.color.flames_dark)
        window.navigationBarColor = color(R.color.flames_dark)
        buildShell()
        showDiscover()
    }

    private fun buildShell() {
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(color(R.color.flames_dark))
        }
        content = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(18), dp(12), dp(18), dp(8))
        }
        root.addView(content, LinearLayout.LayoutParams(-1, 0, 1f))
        nav = LinearLayout(this).apply {
            gravity = Gravity.CENTER
            setPadding(dp(8), dp(8), dp(8), dp(10))
            setBackgroundColor(color(R.color.flames_surface))
        }
        addNavItem("🔥\nDiscover") { showDiscover() }
        addNavItem("💛\nMatches") { showMatches() }
        addNavItem("💬\nChats") { showChats() }
        addNavItem("👤\nProfile") { showProfile() }
        root.addView(nav, LinearLayout.LayoutParams(-1, dp(72)))
        setContentView(root)
    }

    private fun addNavItem(text: String, click: () -> Unit) {
        nav.addView(navButton(text, click), LinearLayout.LayoutParams(0, -1, 1f))
    }

    private fun showDiscover() {
        content.removeAllViews()
        header("Discover", "Find someone who matches your energy.")
        val card = MaterialCardView(this).apply {
            radius = dp(28).toFloat(); cardElevation = dp(6).toFloat()
            setCardBackgroundColor(color(profiles[currentIndex].accent))
        }
        val cardBox = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL; setPadding(dp(24), dp(24), dp(24), dp(22))
            gravity = Gravity.BOTTOM; minimumHeight = dp(430)
        }
        val p = profiles[currentIndex]
        val avatar = TextView(this).apply {
            text = p.initials; textSize = 62f; gravity = Gravity.CENTER; setTextColor(Color.WHITE)
            typeface = Typeface.DEFAULT_BOLD; background = circleDrawable(Color.argb(55, 0, 0, 0))
        }
        cardBox.addView(avatar, LinearLayout.LayoutParams(dp(112), dp(112)).apply { gravity = Gravity.CENTER_HORIZONTAL })
        cardBox.addView(space(24)); cardBox.addView(label("${p.name}, ${p.age}", 32f, Color.WHITE, true))
        cardBox.addView(label("${p.distance}", 14f, Color.WHITE, false)); cardBox.addView(space(10))
        cardBox.addView(label(p.bio, 16f, Color.WHITE, false)); cardBox.addView(space(16))
        val chips = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL }
        p.interests.forEach { interest ->
            val chip = label("  $interest  ", 12f, Color.WHITE, true).apply {
                background = roundedDrawable(Color.argb(50, 0, 0, 0), 50); setPadding(dp(8), dp(6), dp(8), dp(6))
            }
            chips.addView(chip, LinearLayout.LayoutParams(-2, -2).apply { marginEnd = dp(7) })
        }
        cardBox.addView(chips); card.addView(cardBox)
        content.addView(card, LinearLayout.LayoutParams(-1, 0, 1f))
        val actions = LinearLayout(this).apply { gravity = Gravity.CENTER; setPadding(0, dp(14), 0, 0) }
        actions.addView(actionButton("✕", R.color.flames_red) { nextProfile() })
        actions.addView(actionButton("🔥", R.color.flames_orange) { match(p.name) })
        actions.addView(actionButton("★", R.color.flames_yellow) { showProfileDetails(p) })
        content.addView(actions)
    }

    private fun showMatches() {
        content.removeAllViews(); header("Matches", "People who sparked a connection.")
        val scroll = ScrollView(this); val list = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }
        list.addView(matchCard("Amara", "You both liked each other 🔥", R.color.flames_red))
        list.addView(matchCard("Zainab", "You matched 2 hours ago", R.color.flames_orange))
        list.addView(matchCard("Maya", "Say hello and start the conversation", R.color.flames_yellow))
        scroll.addView(list); content.addView(scroll, LinearLayout.LayoutParams(-1, 0, 1f))
    }

    private fun showChats() {
        content.removeAllViews(); header("Messages", "Keep the spark going.")
        val scroll = ScrollView(this); val list = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }
        list.addView(chatRow("Amara", "Hey! I loved your travel story 😊", "2m", R.color.flames_red))
        list.addView(chatRow("Zainab", "That food spot sounds amazing!", "1h", R.color.flames_orange))
        list.addView(chatRow("Maya", "Want to grab coffee this weekend?", "3h", R.color.flames_yellow))
        scroll.addView(list); content.addView(scroll, LinearLayout.LayoutParams(-1, 0, 1f))
    }

    private fun showProfile() {
        content.removeAllViews(); header("My Profile", "Make your profile feel like you.")
        val avatar = TextView(this).apply {
            text = "G"; textSize = 54f; gravity = Gravity.CENTER; setTextColor(Color.WHITE)
            typeface = Typeface.DEFAULT_BOLD; background = circleDrawable(color(R.color.flames_orange))
        }
        content.addView(avatar, LinearLayout.LayoutParams(dp(120), dp(120)).apply { gravity = Gravity.CENTER_HORIZONTAL; bottomMargin = dp(16) })
        content.addView(label("Genesis, 24", 27f, Color.WHITE, true).apply { gravity = Gravity.CENTER })
        content.addView(label("Lagos • Open to meeting someone special", 14f, color(R.color.flames_muted), false).apply { gravity = Gravity.CENTER })
        content.addView(space(22))
        listOption("✎  Edit profile") { showMessage("Profile editing is ready for the next step.") }
        listOption("⚙  Preferences") { showMessage("Preferences screen coming next.") }
        listOption("🔔  Notifications") { showMessage("Notifications are enabled.") }
        listOption("🛡  Safety & privacy") { showMessage("Your safety comes first on Flames.") }
    }

    private fun header(title: String, subtitle: String) {
        content.addView(label("🔥  $title", 28f, Color.WHITE, true))
        content.addView(label(subtitle, 14f, color(R.color.flames_muted), false)); content.addView(space(14))
    }

    private fun navButton(text: String, click: () -> Unit): TextView = label(text, 12f, Color.WHITE, true).apply {
        gravity = Gravity.CENTER; setOnClickListener { click() }
    }

    private fun actionButton(text: String, bg: Int, click: () -> Unit): MaterialButton = MaterialButton(this).apply {
        this.text = text; textSize = 20f; setTextColor(Color.WHITE)
        backgroundTintList = android.content.res.ColorStateList.valueOf(color(bg)); cornerRadius = dp(32)
        minWidth = dp(64); minimumHeight = dp(56); setOnClickListener { click() }
        layoutParams = LinearLayout.LayoutParams(dp(72), dp(56)).apply { marginStart = dp(5); marginEnd = dp(5) }
    }

    private fun matchCard(name: String, subtitle: String, accent: Int): View = MaterialCardView(this).apply {
        radius = dp(20).toFloat(); setCardBackgroundColor(color(R.color.flames_surface)); setContentPadding(dp(14), dp(12), dp(14), dp(12))
        val row = LinearLayout(context).apply { gravity = Gravity.CENTER_VERTICAL }
        val a = TextView(context).apply { text = name.take(1); textSize = 24f; gravity = Gravity.CENTER; setTextColor(Color.WHITE); background = circleDrawable(color(accent)) }
        row.addView(a, LinearLayout.LayoutParams(dp(58), dp(58)))
        val info = LinearLayout(context).apply { orientation = LinearLayout.VERTICAL; setPadding(dp(14), 0, 0, 0) }
        info.addView(label(name, 18f, Color.WHITE, true)); info.addView(label(subtitle, 13f, color(R.color.flames_muted), false))
        row.addView(info, LinearLayout.LayoutParams(0, -2, 1f))
        val button = MaterialButton(context).apply { text = "Chat"; cornerRadius = dp(22); setOnClickListener { showMessage("Opening your chat with $name…") } }
        row.addView(button, LinearLayout.LayoutParams(dp(88), dp(48))); addView(row)
        layoutParams = LinearLayout.LayoutParams(-1, dp(88)).apply { bottomMargin = dp(12) }
    }

    private fun chatRow(name: String, message: String, time: String, accent: Int): View = matchCard(name, "$message   •   $time", accent)

    private fun listOption(text: String, click: () -> Unit) {
        val item = TextView(this).apply {
            this.text = text; textSize = 16f; setTextColor(Color.WHITE); gravity = Gravity.CENTER_VERTICAL
            setPadding(dp(18), 0, dp(18), 0); background = roundedDrawable(color(R.color.flames_surface), 18)
            setOnClickListener { click() }
        }
        content.addView(item, LinearLayout.LayoutParams(-1, dp(58)).apply { bottomMargin = dp(10) })
    }

    private fun nextProfile() { currentIndex = (currentIndex + 1) % profiles.size; showDiscover() }
    private fun match(name: String) { showMessage("🔥 It's a match with $name!"); currentIndex = (currentIndex + 1) % profiles.size }
    private fun showProfileDetails(p: Profile) = showMessage("${p.name}: ${p.bio}")
    private fun showMessage(message: String) = android.widget.Toast.makeText(this, message, android.widget.Toast.LENGTH_SHORT).show()
    private fun label(text: String, size: Float, color: Int, bold: Boolean) = TextView(this).apply { this.text = text; textSize = size; setTextColor(color); if (bold) typeface = Typeface.DEFAULT_BOLD }
    private fun space(height: Int) = View(this).apply { layoutParams = LinearLayout.LayoutParams(1, dp(height)) }
    private fun color(id: Int) = ContextCompat.getColor(this, id)
    private fun dp(value: Int) = (value * resources.displayMetrics.density).toInt()
    private fun roundedDrawable(fill: Int, radius: Int) = GradientDrawable().apply { setColor(fill); cornerRadius = dp(radius).toFloat() }
    private fun circleDrawable(fill: Int) = GradientDrawable().apply { shape = GradientDrawable.OVAL; setColor(fill) }
}
