package com.example.model

data class Hadith(
    val id: Int,
    val source: String, // e.g. HR. Bukhari, HR. Muslim
    val number: String,
    val arabicText: String,
    val indonesianTranslation: String,
    val explanation: String
)

object DummyHadiths {
    val list = listOf(
        Hadith(
            id = 1,
            source = "HR. Bukhari",
            number = "No. 1",
            arabicText = "إِنَّمَا الأَعْمَالُ بِالنِّيَّاتِ، وَإِنَّمَا لِكُلِّ امْرِئٍ مَا نَوَى",
            indonesianTranslation = "Sesungguhnya setiap amalan itu bergantung pada niatnya, dan setiap orang akan mendapatkan apa yang ia niatkan.",
            explanation = "Hadits utama ini menjelaskan bahwa keikhlasan niat semata-mata karena Allah SWT adalah penentu utama diterima atau ditolaknya seluruh amal ibadah kita."
        ),
        Hadith(
            id = 2,
            source = "HR. Bukhari & Muslim",
            number = "No. 6011",
            arabicText = "الْمُسْلِمُ مَنْ سَلِمَ الْمُسْلِمُونَ مِنْ لِسَانِهِ وَيَدِهِ",
            indonesianTranslation = "Seorang Muslim yang sejati adalah dia yang membuat Muslim lainnya selamat dari gangguan lisan dan tangannya.",
            explanation = "Hadits ini menekankan pentingnya menjaga akhlak sosial, menyebarkan kedamaian, dan menahan diri dari menyakiti orang lain baik secara lisan maupun tindakan fisik."
        ),
        Hadith(
            id = 3,
            source = "HR. Tirmidzi",
            number = "No. 2002",
            arabicText = "أَكْمَلُ الْمُؤْمِنِينَ إِيمَانًا أَحْسَنُهُمْ خُلُقًا",
            indonesianTranslation = "Orang mukmin yang paling sempurna imannya adalah yang paling baik akhlaknya.",
            explanation = "Menunjukkan hubungan erat antara tingkat keimanan seseorang dengan keluhuran perangai dan budi pekerti dalam berinteraksi sehari-hari."
        ),
        Hadith(
            id = 4,
            source = "HR. Muslim",
            number = "No. 2699",
            arabicText = "مَنْ سَلَكَ طَرِيقًا يَلْتَمِسُ فِيهِ عِلْمًا سَهَّلَ اللَّهُ لَهُ بِهِ طَرِيقًا إِلَى الْجَنَّةِ",
            indonesianTranslation = "Barangsiapa menempuh suatu jalan untuk menuntut ilmu, maka Allah akan memudahkan baginya jalan menuju surga.",
            explanation = "Dorongan luar biasa bagi setiap Muslim untuk tiada henti belajar dan menuntut ilmu, baik ilmu agama maupun ilmu umum yang bermanfaat."
        ),
        Hadith(
            id = 5,
            source = "HR. Bukhari",
            number = "No. 6475",
            arabicText = "نِعْمَتَانِ مَغْبُونٌ فِيهِمَا كَثِيرٌ مِنَ النَّاسِ الصِّحَّةُ وَالْفَرَاغُ",
            indonesianTranslation = "Dua nikmat yang sering dilalaikan oleh sebagian besar manusia adalah nikmat kesehatan dan waktu luang.",
            explanation = "Mengingatkan kita agar senantiasa produktif dan memanfaatkan nikmat sehat serta waktu luang untuk beribadah dan berbuat kebaikan sebelum datangnya sakit atau kesibukan."
        ),
        Hadith(
            id = 6,
            source = "HR. Bukhari & Muslim",
            number = "No. 13",
            arabicText = "لاَ يُؤْمِنُ أَحَدُكُمْ حَتَّى يُحِبَّ لأَخِيهِ مَا يُحِبُّ لِنَفْسِهِ",
            indonesianTranslation = "Tidak sempurna iman salah seorang di antara kalian sampai ia mencintai saudaranya sebagaimana ia mencintai dirinya sendiri.",
            explanation = "Membangun rasa empati yang tinggi dan ukhuwah (persaudaraan) yang kokoh di antara sesama umat Islam."
        )
    )
}
