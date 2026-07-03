package com.example.model

data class Article(
    val id: Int,
    val title: String,
    val author: String,
    val date: String,
    val category: String, // Akidah, Ibadah, Akhlak, Sirah
    val content: String,
    val readTimeMinutes: Int
)

object DummyArticles {
    val list = listOf(
        // Akidah
        Article(
            id = 1,
            title = "Memurnikan Tauhid di Era Modern",
            author = "Ust. Dr. Muhammad Yasir",
            date = "01 Jul 2026",
            category = "Akidah",
            content = "Tauhid adalah fondasi utama dalam agama Islam. Memurnikan ibadah hanya kepada Allah Subhanahu wa Ta'ala membebaskan manusia dari penghambaan kepada sesama makhluk menuju penghambaan kepada Sang Pencipta alam semesta.\n\nDi era digital yang serba cepat ini, menjaga kemurnian akidah menuntut kita untuk senantiasa mawas diri terhadap segala bentuk kemusyrikan modern, baik yang terselubung maupun yang terang-terangan. Bentuk kemusyrikan modern bisa berupa ketergantungan hati yang berlebihan pada teknologi, ramalan-ramalan masa depan digital, atau mengutamakan opini manusia di atas firman Allah.\n\nLangkah praktis untuk menjaga akidah kita:\n1. Memperbanyak menuntut ilmu syar'i tentang tauhid.\n2. Senantiasa mengikhlaskan niat dalam setiap aktivitas harian.\n3. Membiasakan zikir pagi dan petang sebagai pelindung spiritual.\n4. Berteman dengan lingkungan yang saling mengingatkan pada ketakwaan.\n\nSemoga Allah senantiasa menjaga keimanan kita hingga akhir hayat.",
            readTimeMinutes = 5
        ),
        Article(
            id = 2,
            title = "Makna Syahadatain dalam Kehidupan Sehari-hari",
            author = "Ust. Abdul Somad, Lc., M.A.",
            date = "28 Jun 2026",
            category = "Akidah",
            content = "Dua kalimat syahadat bukan sekadar ucapan lisan, melainkan komitmen hidup. Bersaksi bahwa tiada tuhan selain Allah dan Muhammad adalah utusan-Nya menuntut kita untuk menyelaraskan seluruh kehendak kita dengan syariat yang dibawa oleh Rasulullah SAW.\n\nKetika kita mengucapkan 'La ilaha illallah', kita menyatakan bahwa Allah-lah satu-satunya tujuan hidup, tempat bersandar, dan pembuat hukum tertinggi. Sementara 'Muhammad Rasulullah' menggariskan bahwa satu-satunya cara beribadah kepada Allah adalah dengan mengikuti sunah nabi kita tercinta.",
            readTimeMinutes = 4
        ),
        
        // Ibadah
        Article(
            id = 3,
            title = "Keutamaan Salat Tepat Waktu dan Berjamaah",
            author = "Ust. Adi Hidayat, Lc., M.A.",
            date = "30 Jun 2026",
            category = "Ibadah",
            content = "Salat adalah tiang agama dan amalan yang pertama kali dihisab pada hari kiamat. Melaksanakan salat tepat waktu mencerminkan kedisiplinan dan rasa hormat yang mendalam seorang hamba kepada Sang Pencipta.\n\nBagi kaum pria, melaksanakan salat fardu secara berjamaah di masjid memiliki keutamaan 27 derajat dibanding salat sendirian. Selain pahala yang melimpah, salat berjamaah mempererat tali silaturahmi antar-sesama Muslim di lingkungan sekitar.\n\nRasulullah SAW bersabda: 'Salat berjamaah lebih utama daripada salat sendirian dengan dua puluh tujuh derajat.' (HR. Bukhari & Muslim).\n\nMari kita jadikan panggilan azan sebagai prioritas utama di sela-sela kesibukan duniawi kita.",
            readTimeMinutes = 6
        ),
        Article(
            id = 4,
            title = "Rahasia Keberkahan di Balik Salat Tahajud",
            author = "Ust. KH. Bahauddin Nur Salim",
            date = "25 Jun 2026",
            category = "Ibadah",
            content = "Tahajud adalah waktu intim terbaik antara seorang hamba dengan Tuhannya. Di sepertiga malam yang terakhir, pintu langit terbuka lebar dan Allah SWT turun ke langit dunia untuk mengabulkan doa-doa hamba-Nya yang bersujud.\n\nMenghidupkan malam dengan ruku dan sujud memberikan ketenangan jiwa, melapangkan rezeki yang berkah, serta mengangkat derajat seseorang ke tempat yang terpuji, sebagaimana janji Allah dalam Surah Al-Isra ayat 79.",
            readTimeMinutes = 5
        ),

        // Akhlak
        Article(
            id = 5,
            title = "Menjaga Lisan dan Jari di Media Sosial",
            author = "Prof. Dr. KH. Quraish Shihab",
            date = "02 Jul 2026",
            category = "Akhlak",
            content = "Rasulullah SAW bersabda: 'Barangsiapa yang beriman kepada Allah dan hari akhir, hendaklah ia berkata baik atau diam.' (HR. Bukhari).\n\nDi zaman modern ini, berkata baik tidak lagi hanya melalui lisan, tetapi juga melalui tulisan, komentar, dan unggahan di media sosial. Setiap ketikan jari kita memiliki konsekuensi hukum syariat yang setara dengan ucapan lisan.\n\nSebelum membagikan informasi atau memberikan komentar, tanyakan tiga hal pada diri kita:\n1. Apakah ini benar? (Validitas)\n2. Apakah ini bermanfaat? (Manfaat)\n3. Apakah cara menyampaikannya santun? (Etika)\n\nMari kita hiasi ruang digital kita dengan dakwah yang santun, damai, dan penuh keteladanan akhlak mulia.",
            readTimeMinutes = 5
        ),
        Article(
            id = 6,
            title = "Pentingnya Birrul Walidain (Berbakti pada Orang Tua)",
            author = "Ust. Hanan Attaki, Lc.",
            date = "24 Jun 2026",
            category = "Akhlak",
            content = "Keridaan Allah terletak pada keridaan orang tua, dan kemurkaan Allah terletak pada kemurkaan orang tua. Berbakti kepada orang tua (birrul walidain) adalah amal paling utama setelah salat pada waktunya.\n\nMenghormati mereka, mendengarkan nasihat mereka dengan tulus, serta mendoakan kebaikan bagi mereka di setiap sujud kita adalah kunci keberhasilan hidup di dunia dan jalan pintas menuju surga Allah.",
            readTimeMinutes = 4
        ),

        // Sirah
        Article(
            id = 7,
            title = "Keteladanan Rasulullah SAW dalam Berniaga",
            author = "Dr. Erwandi Tarmizi, M.A.",
            date = "29 Jun 2026",
            category = "Sirah",
            content = "Sebelum diutus menjadi Rasul, Nabi Muhammad SAW dikenal sebagai seorang saudagar yang sangat sukses, jujur, dan terpercaya (Al-Amin). Beliau memberikan standar etika bisnis terbaik sepanjang sejarah kemanusiaan.\n\nBeberapa prinsip perdagangan Rasulullah SAW:\n1. Kejujuran Mutlak: Menjelaskan cacat barang apa adanya tanpa menutup-nutupi.\n2. Keadilan Timbangan: Tidak pernah mengurangi takaran atau timbangan.\n3. Ramah dan Memudahkan: Memudahkan dalam menjual, membeli, dan menuntut hak.\n4. Tidak Melakukan Monopoli (Ihtikar).\n\nDengan mempraktikkan sunah beliau dalam bekerja, profesi kita akan bernilai ibadah dan mendatangkan keberkahan yang berlimpah.",
            readTimeMinutes = 6
        ),
        Article(
            id = 8,
            title = "Kisah Hijrah: Momentum Kebangkitan Peradaban",
            author = "Ust. Salim A. Fillah",
            date = "20 Jun 2026",
            category = "Sirah",
            content = "Hijrah bukan sekadar perpindahan geografis dari Makkah ke Madinah, melainkan transformasi total umat Islam. Perjalanan penuh rintangan ini mengajarkan kita tentang pentingnya perencanaan matang, tawakal yang kokoh, dan persaudaraan yang tulus (Ukhuwah Islamiyah) antara kaum Muhajirin dan Ansar.\n\nDari peristiwa hijrah, lahir tatanan masyarakat baru yang berlandaskan keadilan, toleransi, dan ketakwaan.",
            readTimeMinutes = 7
        )
    )
}
