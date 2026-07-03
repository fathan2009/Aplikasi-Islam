package com.example.util

import java.util.Calendar
import kotlin.math.floor

object HijriDateConverter {
    fun getHijriDate(calendar: Calendar): String {
        // Tabular Hijri Calendar approximation
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        var m = month
        var y = year
        if (m < 3) {
            y -= 1
            m += 12
        }

        var jd = floor(365.25 * y) + floor(30.6001 * (m + 1)) + day - 735643.0
        if (y > 1582 || (y == 1582 && m > 10) || (y == 1582 && m == 10 && day >= 15)) {
            val a = floor(y / 100.0)
            jd += 1 + a - floor(a / 4.0)
        }

        val epoch = 1948439.5
        val julianDate = jd + 1721424.5

        val l = julianDate - epoch + 10632.0
        val n = floor((l - 1) / 10631.0).toInt()
        val hYear = floor((l - 30.0) / 354.36667).toInt() + 1
        val hYearDays = floor((hYear - 1) * 354.36667 + 0.5).toInt()
        var hDayOfYear = (l - hYearDays).toInt()

        if (hDayOfYear <= 0) {
            val prevYearDays = floor((hYear - 2) * 354.36667 + 0.5).toInt()
            hDayOfYear = (l - prevYearDays).toInt()
        }

        // Determine Hijri Month and Day
        var hMonth = 1
        var hDay = hDayOfYear
        val monthDays = intArrayOf(0, 30, 29, 30, 29, 30, 29, 30, 29, 30, 29, 30, 30)

        for (i in 1..12) {
            val length = if (i == 12 && isHijriLeapYear(hYear)) 30 else monthDays[i]
            if (hDay <= length) {
                hMonth = i
                break
            }
            hDay -= length
        }

        val monthNames = arrayOf(
            "Muharram", "Safar", "Rabi'ul Awwal", "Rabi'ul Akhir",
            "Jumadil Awwal", "Jumadil Akhir", "Rajab", "Sya'ban",
            "Ramadhan", "Syawal", "Dzulqa'dah", "Dzulhijjah"
        )

        return "$hDay ${monthNames[hMonth - 1]} $hYear H"
    }

    private fun isHijriLeapYear(hYear: Int): Boolean {
        val leapYears = intArrayOf(2, 5, 7, 10, 13, 16, 18, 21, 24, 26, 29)
        return (hYear % 30) in leapYears
    }
}
