package com.glassline.flames

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

private data class Profile(
    val name: String,
    val age: Int,
    val city: String,
    val bio: String,
    val emoji: String,
    val accent: Int
)

class MainActivity : AppCompatActivity() {
    private val red = Color.rgb(229, 57, 53)
    private val orange = Color.rgb(255, 109, 0)
    private val yellow = Color.rgb(255, 193, 7)
    private val bg = Color.rgb(25, 9, 7)
    private val surface = Color.rgb(43, 17, 13)
    private val text = Color.rgb(255, 248, 238)
    private val muted = Color.rgb(205, 170, 155)

    private val profiles = listOf(
        Profile("Amara", 24, "Abuja", "Coffee, sunsets and spontaneous road trips. Looking for someone genuine.", "🌅", orange),
        Profile("Zainab", 26, "Lagos", "Designer, foodie and professional playlist curator. Let's find a new spot.", "✨", yellow),
        Profile("Nia", 23, "Ibadan", "Book lover with a soft spot for live music and late-night conversations.", "🎧", red),
        Profile("Maya", 27, "Enugu", "Good energy only. Gym, travel, films and building cool things.", "🌴", orange)
    )

    private lateinit var root: FrameLayout
    private lateinit var content: FrameLayout
    private var profileIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = bg
        window.navigationBarColor = bg
        showApp()
    }

    private fun showApp() {
        root = FrameLayout(this).apply { setBackgroundColor(bg) }
        content = FrameLayout(this)
        root.addView(content, FrameLayout.LayoutParams(-1, -1))
        setContentView(root)
        showDiscover()
    }

    private fun showDiscover() {
        content.removeAllViews()
        val page = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(20), dp(18), dp(20), 0)
        }
        page.addView(header("Flames", "Find your spark 🔥"))
        val scroll = ScrollView(this).apply { isFillViewport = true }
        val body = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; setPadding(0, dp(8), 0, dp(92)) }
        val p = profiles[profileIndex % profiles.size]
        body.addView(profileCard(p))
        body.addView(TextView(this).apply {
            text = "People who match your vibe"
            setTextColor(text); textSize = 18f; typeface = Typeface.DEFAULT_BOLD
            setPadding(4, dp(20), 0, dp(10))
        })
        profiles.filterIndexed { i, _ -> i != profileIndex % profiles.size }.take(3).forEach { body.addView(compactProfile(it)) }
        scroll.addView(body)
        page.addView(scroll, LinearLayout.LayoutParams(-1, 0, 1f))
        content.addView(page)
        addBottomNav(0)
    }

    private fun profileCard(p: Profile): View {
        val card = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            background = rounded(p.accent, 28)
            setPadding(dp(20), dp(18), dp(20), dp(18))
        }
        val avatar = TextView(this).apply {
            text = p.emoji; textSize = 70f; gravity = Gravity.CENTER
            background = rounded(Color.argb(45, 255, 255, 255), 22)
        }
        card.addView(avatar, LinearLayout.LayoutParams(-1, dp(235)))
        card.addView(TextView(this).apply {
            text = "${p.name}, ${p.age}"; textSize = 29f; typeface = Typeface.DEFAULT_BOLD; setTextColor(Color.WHITE)
            setPadding(0, dp(14), 0, 0)
        })
        card.addView(TextView(this).apply { text = "📍 ${p.city}"; textSize = 15f; setTextColor(Color.WHITE) })
        card.addView(TextView(this).apply {
            text = p.bio; textSize = 15f; setTextColor(Color.WHITE); setLineSpacing(2f, 1.05f)
            setPadding(0, dp(12), 0, dp(8))
        })
        val actions = LinearLayout(this).apply { gravity = Gravity.CENTER; setPadding(0, dp(8), 0, 0) }
        actions.addView(actionButton("✕", red) { nextProfile() }, LinearLayout.LayoutParams(0, dp(54), 1f).apply { setMargins(0, 0, dp(6), 0) })
        actions.addView(actionButton("🔥", yellow) { likeProfile(p) }, LinearLayout.LayoutParams(0, dp(54), 1f).apply { setMargins(dp(6), 0, dp(6), 0) })
        actions.addView(actionButton("★", orange) { likeProfile(p) }, LinearLayout.LayoutParams(0, dp(54), 1f).apply { setMargins(dp(6), 0, 0, 0) })
        card.addView(actions)
        return card
    }

    private fun compactProfile(p: Profile): View {
        val row = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL; gravity = Gravity.CENTER_VERTICAL
            background = rounded(surface, 18); setPadding(dp(12), dp(10), dp(12), dp(10))
        }
        val avatar = TextView(this).apply { text = p.emoji; textSize = 30f; gravity = Gravity.CENTER; background = rounded(p.accent, 15) }
        row.addView(avatar, LinearLayout.LayoutParams(dp(58), dp(58)))
        val info = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; setPadding(dp(12), 0, dp(8), 0) }
        info.addView(TextView(this).apply { text = "${p.name}, ${p.age}"; textSize = 16f; typeface = Typeface.DEFAULT_BOLD; setTextColor(text) })
        info.addView(TextView(this).apply { text = "📍 ${p.city}"; textSize = 13f; setTextColor(muted) })
        row.addView(info, LinearLayout.LayoutParams(0, -2, 1f))
        row.addView(TextView(this).apply { text = "🔥"; textSize = 24f })
        (row.layoutParams as? ViewGroup.MarginLayoutParams)?.setMargins(0, 0, 0, dp(10))
        return row
    }

    private fun showMatches() {
        content.removeAllViews()
        val page = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; setPadding(dp(20), dp(18), dp(20), dp(90)) }
        page.addView(header("Your Flames", "It's mutual 🔥"))
        val scroll = ScrollView(this)
        val body = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; setPadding(0, dp(12), 0, dp(30)) }
        profiles.forEach { p -> body.addView(compactProfile(p)) }
        scroll.addView(body); page.addView(scroll, LinearLayout.LayoutParams(-1, 0, 1f))
        content.addView(page); addBottomNav(1)
    }

    private fun showMessages() {
        content.removeAllViews()
        val page = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; setPadding(dp(20), dp(18), dp(20), dp(90)) }
        page.addView(header("Messages", "Keep the spark going"))
        val list = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; setPadding(0, dp(15), 0, 0) }
        profiles.forEachIndexed { i, p ->
            val row = compactProfile(p)
            row.setOnClickListener { showChat(p) }
            list.addView(row)
        }
        val scroll = ScrollView(this).apply { addView(list) }
        page.addView(scroll, LinearLayout.LayoutParams(-1, 0, 1f)); content.addView(page); addBottomNav(2)
    }

    private fun showChat(p: Profile) {
        content.removeAllViews()
        val page = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; setPadding(dp(20), dp(18), dp(20), dp(18)) }
        val top = header("${p.name} ${p.emoji}", "Active now")
        top.setOnClickListener { showMessages() }; page.addView(top)
        val chat = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; setPadding(0, dp(20), 0, dp(12)) }
        chat.addView(messageBubble("Hey! Your profile caught my attention 🔥", false))
        chat.addView(messageBubble("Haha, I was hoping it would 😄", true))
        chat.addView(messageBubble("What's your perfect weekend?", false))
        val scroll = ScrollView(this).apply { addView(chat) }; page.addView(scroll, LinearLayout.LayoutParams(-1, 0, 1f))
        val input = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL; gravity = Gravity.CENTER_VERTICAL }
        val field = TextView(this).apply { text = "  Write a message..."; textSize = 15f; setTextColor(muted); gravity = Gravity.CENTER_VERTICAL; background = rounded(surface, 24) }
        input.addView(field, LinearLayout.LayoutParams(0, dp(52), 1f)); input.addView(actionButton("➤", orange) { showToast("Message sent 🔥") }, LinearLayout.LayoutParams(dp(58), dp(52)).apply { setMargins(dp(8),0,0,0) })
        page.addView(input); content.addView(page)
    }

    private fun messageBubble(value: String, mine: Boolean): View = TextView(this).apply {
        text = value; textSize = 15f; setTextColor(text); setPadding(dp(16), dp(12), dp(16), dp(12)); background = rounded(if (mine) orange else surface, 20)
        layoutParams = LinearLayout.LayoutParams(-2, -2).apply { gravity = if (mine) Gravity.END else Gravity.START; setMargins(0, 0, 0, dp(10)); width = minOf(dp(285), -1) }
    }

    private fun showProfile() {
        content.removeAllViews()
        val scroll = ScrollView(this)
        val page = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; setPadding(dp(20), dp(18), dp(20), dp(90)) }
        page.addView(header("My Profile", "Make your spark count"))
        page.addView(TextView(this).apply { text = "🔥"; textSize = 88f; gravity = Gravity.CENTER; background = rounded(orange, 30); setPadding(0, dp(18), 0, dp(18)) })
        page.addView(TextView(this).apply { text = "Genesis, 24"; textSize = 28f; typeface = Typeface.DEFAULT_BOLD; setTextColor(text); setPadding(0, dp(18), 0, dp(4)) })
        page.addView(TextView(this).apply { text = "📍 Abuja  •  ✨ Looking for something real"; textSize = 14f; setTextColor(muted) })
        page.addView(section("About me", "Building things, chasing sunsets, and always down for a great conversation."))
        page.addView(section("Interests", "💻 Technology    🎵 Music    ✈️ Travel    🎬 Movies"))
        page.addView(section("Safety", "Never share passwords, financial information, or send money to someone you met online."))
        page.addView(Button(this).apply { text = "Edit profile"; setTextColor(Color.WHITE); background = rounded(red, 18); setOnClickListener { showToast("Profile editor coming next 🔥") } })
        scroll.addView(page); content.addView(scroll); addBottomNav(3)
    }

    private fun section(title: String, body: String): View = LinearLayout(this).apply {
        orientation = LinearLayout.VERTICAL; background = rounded(surface, 18); setPadding(dp(16), dp(14), dp(16), dp(14));
        addView(TextView(this@MainActivity).apply { text = title; textSize = 16f; typeface = Typeface.DEFAULT_BOLD; setTextColor(yellow) })
        addView(TextView(this@MainActivity).apply { text = body; textSize = 14f; setTextColor(text); setPadding(0, dp(8), 0, 0) })
        layoutParams = LinearLayout.LayoutParams(-1, -2).apply { setMargins(0, dp(12), 0, 0) }
    }

    private fun addBottomNav(selected: Int) {
        val nav = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL; gravity = Gravity.CENTER; background = rounded(surface, 26); setPadding(dp(6), dp(7), dp(6), dp(7)) }
        val labels = listOf("🔥\nDiscover", "♥\nMatches", "💬\nChats", "☺\nProfile")
        labels.forEachIndexed { i, label ->
            val item = TextView(this).apply { text = label; textSize = 12f; gravity = Gravity.CENTER; setTextColor(if (i == selected) yellow else muted); typeface = if (i == selected) Typeface.DEFAULT_BOLD else Typeface.DEFAULT; setPadding(0, dp(7), 0, dp(7)); setOnClickListener { when(i) { 0 -> showDiscover(); 1 -> showMatches(); 2 -> showMessages(); else -> showProfile() } } }
            nav.addView(item, LinearLayout.LayoutParams(0, dp(60), 1f))
        }
        root.addView(nav, FrameLayout.LayoutParams(-1, dp(74), Gravity.BOTTOM).apply { setMargins(dp(14), 0, dp(14), dp(10)) })
    }

    private fun header(title: String, subtitle: String): LinearLayout = LinearLayout(this).apply {
        orientation = LinearLayout.VERTICAL
        addView(TextView(this@MainActivity).apply { text = title; textSize = 30f; typeface = Typeface.DEFAULT_BOLD; setTextColor(text) })
        addView(TextView(this@MainActivity).apply { text = subtitle; textSize = 14f; setTextColor(muted); setPadding(0, dp(2), 0, dp(8)) })
    }

    private fun actionButton(label: String, color: Int, click: () -> Unit): View = TextView(this).apply {
        text = label; textSize = 24f; gravity = Gravity.CENTER; setTextColor(Color.WHITE); background = rounded(color, 18); setOnClickListener { click() }
    }

    private fun nextProfile() { profileIndex++; showDiscover(); showToast("Passed for now") }

    private fun likeProfile(p: Profile) {
        showToast("It's a Flame with ${p.name}! 🔥")
        profileIndex++
        showDiscover()
    }

    private fun showToast(message: String) = android.widget.Toast.makeText(this, message, android.widget.Toast.LENGTH_SHORT).show()

    private fun rounded(color: Int, radius: Int): GradientDrawable = GradientDrawable().apply { setColor(color); cornerRadius = dp(radius).toFloat() }
    private fun dp(value: Int): Int = (value * resources.displayMetrics.density).toInt()
}
