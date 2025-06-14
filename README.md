# Sistem Perpustakaan Digital

## Deskripsi Proyek
Proyek "Sistem Perpustakaan Digital" ini adalah sebuah aplikasi desktop modern yang dikembangkan menggunakan JavaFX, dirancang untuk mengelola operasional perpustakaan secara efisien. Aplikasi ini memungkinkan pengguna untuk meminjam dan mengembalikan buku, serta menyediakan fitur lengkap bagi administrator untuk mengelola koleksi buku, data pengguna, dan melacak semua transaksi peminjaman. Fokus utama proyek ini adalah pada kemudahan penggunaan, kejelasan alur kerja, dan keandalan data dengan integrasi database MySQL.

## Fitur Utama

Aplikasi ini menawarkan berbagai fitur yang terbagi dalam beberapa kategori:

### 1. Manajemen Pengguna yang Dinamis

*   **Peran Pengguna Berbeda:**

    *   **Member:** Pengguna standar yang dapat menjelajahi katalog buku, meminjam buku, melihat riwayat peminjaman mereka, dan mengajukan permintaan pengembalian.
    *   **Guest:** Pengguna yang hanya dapat melihat buku yang tersedia.
    *   **Admin:** Pengguna dengan hak istimewa yang memiliki kontrol penuh atas sistem, termasuk manajemen buku, persetujuan/penolakan transaksi, dan modifikasi status anggota.
*   **Sistem Status Anggota Lanjutan (`Member.MemberStatus` Enum):** Untuk mengelola perilaku dan akses anggota secara efektif:
    *   `ACTIVE`: Status default yang memungkinkan anggota untuk login dan menggunakan semua fitur yang relevan.
    *   `FREEZE`: Status pembatasan. Anggota dengan status ini tidak dapat login ke sistem. Ini adalah respons otomatis ketika anggota memiliki buku yang `OVERDUE` atau dapat diatur secara manual oleh admin.
*   **Pencarian dan Filter Pengguna di Dashboard Admin:** Memungkinkan admin untuk dengan cepat menemukan dan menyaring daftar anggota berdasarkan:
    *   **Pencarian Teks:** Mencari berdasarkan nama pengguna, email, atau detail lainnya.
    *   **Filter Status:** Memfilter anggota berdasarkan status `ACTIVE` atau `FREEZE` untuk manajemen yang lebih terfokus.

### 2. Pengelolaan Koleksi Buku yang Komprehensif

*   **Operasi CRUD (Create, Read, Update, Delete) Buku (Admin Dashboard):** Admin memiliki kemampuan penuh untuk mengelola katalog buku:
    *   **Create (Tambah Buku):** Formulir intuitif untuk menambahkan buku baru dengan detail lengkap seperti judul, ISBN, penulis, genre, kategori, jumlah stok, penerbit, dan deskripsi.
    *   **Read (Lihat Detail Buku):** Menampilkan daftar buku dengan opsi untuk melihat detail lengkap setiap buku.
    *   **Update (Perbarui Buku):** Dialog khusus untuk memodifikasi informasi buku yang sudah ada. Dilengkapi dengan label bidang yang jelas dan opsi penutupan via tombol `Escape` untuk pengalaman pengguna yang lebih baik.
    *   **Delete (Hapus Buku):** Menghapus entri buku secara permanen dari database. Penting untuk dicatat bahwa ini adalah penghapusan langsung (*hard delete*), bukan perubahan status menjadi "dihapus".
*   **Tampilan Daftar Buku yang Ditingkatkan (Member Dashboard):** Desain tampilan buku untuk anggota telah diperbaharui dari `GridPane` tradisional menjadi `FlowPane`. Ini memungkinkan setiap buku ditampilkan sebagai "kartu vertikal panjang" yang lebih besar dan informatif, menampilkan lebih banyak detail seperti:
    *   Judul Buku
    *   Penulis
    *   Penerbit
    *   Tahun Terbit
    *   Genre & Kategori
    *   Deskripsi Singkat
    *   Ketersediaan Stok


### 3. Otomatisasi dan Pelacakan Transaksi

*   **Alur Peminjaman dan Pengembalian yang Terstruktur:** Sistem mengelola seluruh siklus hidup transaksi, dari permintaan peminjaman hingga pengembalian akhir.
*   **Status Transaksi yang Detail (`Transaksi.Status` Enum):** Setiap transaksi melalui serangkaian status yang jelas:
    *   `PENDING_PEMINJAMAN`: Anggota telah meminta buku, menunggu persetujuan admin.
    *   `ACCEPTED`: Permintaan peminjaman disetujui; buku ada pada anggota.
    *   `REJECTED`: Permintaan peminjaman atau pengembalian ditolak oleh admin.
    *   `PENDING_PENGEMBALIAN`: Anggota telah memulai proses pengembalian, menunggu konfirmasi admin.
    *   `RETURNED`: Buku berhasil dikembalikan dan dicatat dalam sistem.
    *   `OVERDUE`: Buku belum dikembalikan setelah tanggal jatuh tempo. Status ini penting karena memicu pembekuan anggota.
*   **Tombol "Kembalikan Buku" di Riwayat Anggota:** Anggota dapat dengan mudah mengajukan permintaan pengembalian untuk buku yang berstatus `ACCEPTED` langsung dari riwayat peminjaman mereka.
*   **Kontrol Admin atas Pengembalian:** Admin memiliki kemampuan untuk:
    *   **Menerima Pengembalian:** Mengubah status transaksi menjadi `RETURNED`.
    *   **Menolak Pengembalian:** Mengubah status transaksi kembali menjadi `ACCEPTED`, mengharuskan anggota untuk mengajukan ulang permintaan jika buku masih ingin dikembalikan.
*   **Pembekuan Anggota Otomatis:** Sistem secara otomatis mengubah status anggota menjadi `FREEZE` jika ada transaksi peminjaman mereka yang berstatus `OVERDUE`. Ini mencegah peminjaman lebih lanjut sampai buku dikembalikan atau situasi diselesaikan.

### 4. Peningkatan Antarmuka Pengguna (UI/UX)

*   **Dialog "Update Buku" yang Disempurnakan:** Desain ulang dialog pembaruan buku untuk admin meliputi:
    *   **Label Bidang yang Jelas:** Setiap bidang input sekarang memiliki label deskriptif yang mendampinginya, seperti "Judul Buku:", "ISBN:", "Penulis:", dll., menghilangkan ambiguitas dan meningkatkan pengalaman pengguna.
    *   **Penutupan dengan Tombol Escape:** Dialog kini dapat ditutup dengan mudah menggunakan tombol `Escape` pada keyboard, menyediakan cara navigasi yang cepat dan umum.
*   **Penyesuaian Lebar Kolom "Status" yang Responsif:** Kolom "Status" di tabel riwayat peminjaman (Dashboard Anggota) dan tabel transaksi (Dashboard Admin) telah diperlebar secara signifikan dan diatur untuk `wrapText`. Hal ini memastikan bahwa teks status yang panjang, seperti `PENDING_PENGEMBALIAN` atau `OVERDUE`, dapat ditampilkan sepenuhnya tanpa terpotong, meningkatkan keterbacaan data.
*   **Penyederhanaan Teks Tombol Aksi Admin:** Tombol-tombol aksi di dashboard manajemen transaksi admin (`AdminTransactionsController`) telah disederhanakan menjadi "Setuju" dan "Tolak" untuk kejelasan maksimal dan mengurangi kebingungan, terutama untuk tindakan kritis persetujuan/penolakan.

## Teknologi yang Digunakan

Proyek ini dibangun menggunakan kombinasi teknologi berikut:

*   **JavaFX:** Framework perangkat lunak sumber terbuka untuk membangun aplikasi desktop dengan antarmuka pengguna grafis yang kaya. Digunakan untuk semua aspek UI dan interaksi pengguna.
*   **MySQL:** Sistem manajemen basis data relasional (RDBMS) yang populer. Digunakan sebagai backend untuk menyimpan dan mengelola semua data aplikasi, termasuk detail buku, informasi anggota, dan catatan transaksi.
*   **Maven:** Alat manajemen proyek dan pemahaman yang kuat. Maven digunakan untuk mengelola siklus hidup proyek, dependensi, dan proses build, memastikan konsistensi dan kemudahan dalam pengembangan dan deployment.

## Struktur Folder Proyek

Proyek ini mengikuti konvensi standar Maven untuk struktur direktori. Berikut adalah gambaran umum folder dan isinya:

```
├── src/
│   ├── main/                                 # Berisi kode sumber utama aplikasi.
│   │   ├── java/                             # Kode sumber Java utama.
│   │   │   └── com/
│   │   │       ├── controller/               # Kelas-kelas kontroler JavaFX. Mereka menangani logika bisnis yang terkait dengan tampilan UI, memproses input pengguna, dan berinteraksi dengan layanan (Service) dan objek akses data (DAO).
│   │   │       ├── DAO/                      # Data Access Objects. Kelas-kelas ini bertanggung jawab untuk semua operasi CRUD (Create, Read, Update, Delete) dengan database MySQL. Setiap kelas DAO berinteraksi dengan satu atau lebih tabel database.
│   │   │       ├── model/                  # Kelas-kelas model data yang merepresentasikan entitas bisnis di aplikasi (e.g., Buku.java, Member.java, Transaksi.java). Mereka berisi atribut dan logika dasar entitas.
│   │   │       ├── Service/                # Kelas-kelas layanan yang mengimplementasikan logika bisnis tingkat tinggi. Mereka mengorkestrasi operasi DAO dan menyediakan antarmuka yang lebih bersih untuk kontroler.
│   │   │       └── util/                   # Kelas utilitas dan pembantu. Contohnya termasuk DatabaseConnection.java untuk mengelola koneksi database, dan DatabaseInitializer.java untuk setup awal database.
│   │   │       └── App.java                # Kelas utama aplikasi JavaFX. Ini adalah titik masuk aplikasi, bertanggung jawab untuk inisialisasi Stage dan Scene awal, serta manajemen navigasi antar tampilan (FXML).
│   │   └── resources/                      # Berisi sumber daya non-Java yang dibutuhkan oleh aplikasi.
│   │       └── com/
│   │           └── view/                   # File FXML (XML-based User Interface Markup Language) yang mendefinisikan struktur dan elemen visual antarmuka pengguna JavaFX. Setiap file FXML biasanya dipasangkan dengan satu kelas kontroler.
│   ├── test/                                 # Berisi kode sumber untuk pengujian unit dan integrasi.
├── .vscode/                                  # Folder konfigurasi untuk editor Visual Studio Code, termasuk pengaturan peluncuran dan debugger.
├── README.md                                 # File dokumentasi utama proyek ini yang sedang Anda baca.
├── pom.xml                                   # Project Object Model (POM) Maven. Ini adalah file konfigurasi inti Maven yang mendefinisikan proyek, dependensinya (library eksternal), plugin build, dan target build.
└── target/                                   # Direktori yang dibuat oleh Maven selama proses build. Berisi file-file yang dikompilasi (.class), JAR yang dapat dieksekusi, dan artefak build lainnya.
```

## Cara Menjalankan Aplikasi

Ikuti langkah-langkah di bawah ini untuk mengunduh, mengkonfigurasi, membangun, dan menjalankan Sistem Perpustakaan Digital JavaFX:

### 1. Prasyarat Sistem

Pastikan lingkungan pengembangan Anda memiliki komponen-komponen berikut terinstal dan dikonfigurasi:

*   **Java Development Kit (JDK) 11 atau Lebih Tinggi:** JavaFX versi yang digunakan dalam proyek ini membutuhkan JDK 11 atau yang lebih baru. Anda dapat mengunduh distribusi OpenJDK dari [Adoptium Temurin](https://adoptium.net/temurin/releases/) atau versi Oracle dari [situs resmi Oracle](https://www.oracle.com/java/technologies/javase-downloads.html).
*   **Apache Maven (Versi 3.6.0 atau Lebih Tinggi):** Maven adalah alat build dan manajemen dependensi utama. Pastikan Maven terinstal dan variabel lingkungan `M2_HOME` serta `PATH` Anda telah dikonfigurasi dengan benar. Unduh dari [situs Maven resmi](https://maven.apache.org/download.cgi).
*   **MySQL Server (Versi 8.0 atau Lebih Tinggi Direkomendasikan):** Aplikasi ini menggunakan MySQL sebagai database backend. Pastikan server MySQL Anda berjalan. Unduh dari [MySQL Community Downloads](https://dev.mysql.com/downloads/mysql/).
*   **MySQL Workbench (Opsional, Direkomendasikan):** Alat GUI untuk mengelola database MySQL Anda, sangat berguna untuk langkah konfigurasi database. Unduh dari [MySQL Workbench](https://www.mysql.com/products/workbench/).
*   **Integrated Development Environment (IDE) - Opsional, Sangat Direkomendasikan:**
    *   **IntelliJ IDEA Community Edition:** Sangat direkomendasikan karena integrasi Maven dan JavaFX yang sangat baik. Tersedia [di sini](https://www.jetbrains.com/idea/download/).
    *   **Eclipse IDE for Java Developers:** Pilihan populer lainnya dengan dukungan Maven. Tersedia [di sini](https://www.eclipse.org/downloads/packages/).

### 2. Konfigurasi Basis Data MySQL

Langkah-langkah ini penting untuk memastikan aplikasi dapat terhubung dan berinteraksi dengan database Anda:

1.  **Buat Database Baru:**
    Buka MySQL Workbench atau klien MySQL pilihan Anda, lalu jalankan perintah SQL berikut untuk membuat database:
    ```sql
    CREATE DATABASE IF NOT EXISTS perpustakaan_digital;
    USE perpustakaan_digital;
    ```
2.  **Impor Skema Tabel:**
    Anda harus membuat tabel-tabel yang diperlukan (misalnya, `buku`, `transaksi`, `members`, `admin`, `user`) di dalam database `perpustakaan_digital`. Skema tabel (`.sql` file) yang berisi definisi `CREATE TABLE` biasanya disediakan bersama kode sumber proyek. Jika tidak, Anda mungkin perlu membuatnya secara manual berdasarkan struktur model Java (`com.model`).

    **Peringatan Penting: Penghapusan Kolom 'status' dari Tabel 'buku'**
    Selama pengembangan, kolom `status` telah dihapus dari tabel `buku` dalam kode Java. Oleh karena itu, **sangat krusial** untuk memastikan kolom ini juga tidak ada di tabel `buku` database MySQL Anda. Jika kolom `status` masih ada, Anda akan mengalami `java.sql.SQLSyntaxErrorException`. Jalankan perintah SQL berikut jika perlu:
    ```sql
    -- Pastikan Anda memilih database 'perpustakaan_digital' terlebih dahulu (USE perpustakaan_digital;)
    ALTER TABLE buku DROP COLUMN IF EXISTS status;
    ```
3.  **Perbarui Kredensial Koneksi Database:**
    Buka file `tubes/src/main/java/com/util/DatabaseConnection.java` dalam proyek Anda dan perbarui nilai `DB_URL`, `DB_USER`, dan `DB_PASSWORD` agar sesuai dengan konfigurasi server MySQL Anda.
    ```java
    // Dalam tubes/src/main/java/com/util/DatabaseConnection.java
    private static final String DB_URL = "jdbc:mysql://localhost:3306/perpustakaan_digital?useSSL=false&serverTimezone=UTC";
    private static final String DB_USER = "your_mysql_username"; // <-- Ganti dengan username MySQL Anda
    private static final String DB_PASSWORD = "your_mysql_password"; // <-- Ganti dengan password MySQL Anda
    ```
    *Catatan:* `?useSSL=false&serverTimezone=UTC` adalah parameter koneksi JDBC umum yang disarankan untuk MySQL.

### 3. Membangun Proyek dengan Apache Maven

Setelah database dikonfigurasi, Anda dapat membangun aplikasi:

1.  **Buka Terminal/Command Prompt:** Navigasi ke direktori root proyek Anda (`/d%3A/pbo-fix`) tempat file `pom.xml` berada.
2.  **Bersihkan dan Bangun Proyek:** Jalankan perintah Maven berikut:
    ```bash
    mvn clean install
    ```
    *   `clean`: Menghapus semua file yang dihasilkan dari build sebelumnya.
    *   `install`: Mengkompilasi kode sumber, menjalankan tes (jika ada), mengemas kode yang dikompilasi ke dalam file JAR, dan menginstal JAR ini ke repositori Maven lokal Anda.
    *   Maven akan secara otomatis mengunduh semua dependensi yang didefinisikan dalam `pom.xml` (seperti JavaFX SDK dan MySQL Connector/J).

    Jika build berhasil, Anda akan melihat pesan `BUILD SUCCESS` di akhir output konsol.

### 4. Menjalankan Aplikasi JavaFX

Anda memiliki beberapa opsi untuk menjalankan aplikasi:

#### Opsi A: Melalui Integrated Development Environment (IDE) - Direkomendasikan

1.  **Impor Proyek:**
    *   **IntelliJ IDEA:** Pilih `File` -> `Open` atau `Import Project`, lalu navigasi ke direktori `pbo-fix` dan pilih `pom.xml`.
    *   **Eclipse:** Pilih `File` -> `Import` -> `Maven` -> `Existing Maven Projects`, lalu navigasi ke direktori `pbo-fix`.
2.  **Konfigurasi JDK:** Pastikan IDE Anda dikonfigurasi untuk menggunakan JDK 11 atau yang lebih tinggi untuk proyek ini.
3.  **Jalankan Aplikasi:**
    *   Navigasi ke kelas `App.java` yang terletak di `tubes/src/main/java/com/tubes/App.java`.
    *   Klik kanan pada file `App.java` dan pilih `Run 'App.main()'` atau opsi serupa. IDE akan secara otomatis menangani argumen modul JavaFX yang diperlukan.

#### Opsi B: Melalui Command Line / Terminal

Ini adalah cara cepat untuk menjalankan aplikasi setelah proyek berhasil dibangun dengan Maven:

1.  **Dari Direktori Root Proyek:** Pastikan Anda berada di direktori `pbo-fix` (tempat `pom.xml` berada).
2.  **Jalankan dengan Plugin JavaFX Maven:**
    ```bash
    mvn javafx:run
    ```
    Perintah ini menggunakan plugin JavaFX Maven untuk menjalankan aplikasi secara langsung, menangani semua konfigurasi classpath dan modul JavaFX secara otomatis.

## Catatan Penting dan Pemecahan Masalah

*   **`SQLSyntaxErrorException`:** Kesalahan ini hampir selalu menunjukkan ketidaksesuaian antara skema database Anda dan kode Java yang mencoba mengakses kolom yang tidak ada (misalnya, kolom `status` di tabel `buku`). **Pastikan Anda telah menjalankan `ALTER TABLE buku DROP COLUMN IF EXISTS status;`** di database MySQL Anda.
*   **Koneksi Database:** Jika Anda mengalami masalah koneksi, periksa kembali:
    *   Apakah MySQL Server Anda berjalan?
    *   Apakah kredensial (`DB_USER`, `DB_PASSWORD`) dan `DB_URL` di `DatabaseConnection.java` sudah benar?
    *   Apakah ada firewall yang memblokir koneksi ke port MySQL (default: 3306)?
*   **Fungsionalitas Gambar Dihapus:** Harap dicatat bahwa aplikasi ini tidak lagi memiliki fungsionalitas untuk mengunggah, menampilkan, atau menyimpan gambar buku apa pun. Semua kode dan elemen UI yang terkait dengan fitur ini telah dihapus.
*   **Log Konsol:** Selalu periksa output konsol di IDE atau terminal Anda untuk pesan kesalahan, jejak tumpukan (`stack trace`), atau log debug yang dapat memberikan petunjuk tentang akar masalah apa pun.
*   **Masalah dengan JDK / JavaFX SDK:** Pastikan Anda menggunakan JDK yang kompatibel (JDK 11 atau lebih tinggi) dan konfigurasi Maven Anda dengan benar untuk JavaFX.

---
