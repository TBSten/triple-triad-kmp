package me.tbsten.tripletriad.ui

import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import me.tbsten.tripletriad.R

private val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs,
)

private val fontName = GoogleFont("Kaisei Opti")

val cardFontFamily = FontFamily(
    Font(googleFont = fontName, fontProvider = provider)
)

