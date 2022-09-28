package com.example.sampleappalivecordevice.utils

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import androidx.annotation.StringRes
import com.example.sampleappalivecordevice.utils.omron_2.AppPreffs
import java.util.*

class LocaleHelper {

    enum class SupportedLocale(
        val locale: Locale,
        val customDisplayName: String,
        val displayNameEnglish: String,
        val serverName: String
    ) {
        ENGLISH(Locale("en"), "English", " ", "en"),
        THAI(Locale("th", "TH"), "ภาษาไทย", "Thai", "th-TH"),
        SIMPLIFIED_CHINESE(Locale("zh", "CN"), "简体中文(中国)", "Chinese (Simplified)", "zh-cn"),
        TRADITIONAL_CHINESE(Locale("zh", "TW"), "繁体中文(中国)", "Chinese (Traditional)", "zh-TW"),
        MALAY(Locale("ms"), "Bahasa Melayu", "Malay", "ms"),
        SPANISH_US(Locale("es", "US"), "Español", "Spanish", "es-US"),
        KOREAN(Locale("ko"), "한국인", "Korean", "ko"),
        HAITIAN_CREOLE(Locale("ht"), "Kreyòl ayisyen", "Haitian Creole", "ht"),
        VIETNAMESE(Locale("vi"), "Tiếng Việt", "Vietnamese", "vi")
    }

    companion object {

        /**
         * Returns the string value of [stringId] based on the Locale.
         */

        @JvmStatic
        fun onAttach(context: Context): Context {
            val lang: String = AppPreffs.locale
            val cont: String = AppPreffs.country
            return setLocale(context, lang, cont)
        }

        fun setLocale(
            context: Context,
            localeString: String, localeCountry: String
        ): Context {
            AppPreffs.locale = localeString
            AppPreffs.country = localeCountry
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                updateResources(context, localeString, localeCountry)
            } else updateResourcesLegacy(context, localeString, localeCountry)
        }

        @TargetApi(Build.VERSION_CODES.N)
        private fun updateResources(
            context: Context,
            localeString: String,
            localeCountry: String
        ): Context {
            val locale = Locale(localeString, localeCountry)
            Locale.setDefault(locale)
            val configuration =
                context.resources.configuration
            configuration.setLocale(locale)
            configuration.setLayoutDirection(locale)
            return context.createConfigurationContext(configuration)
        }

        private fun updateResourcesLegacy(
            context: Context,
            localeString: String,
            localeCountry: String
        ): Context {
            val locale = Locale(localeString, localeCountry)
            Locale.setDefault(locale)
            val resources = context.resources
            val configuration = resources.configuration
            configuration.setLocale(locale)
            configuration.setLayoutDirection(locale)
            context.createConfigurationContext(configuration);
            return context
        }
    }
}