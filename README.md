# Sistem Perpustakaan Digital

## 👥 Tim Pengembang

**Kelompok 11 - PBO E**

| Nama | NIM |
|------|-----|
| Umar Faqih | 24060123120003 |
| Shakila Tungga Dewi | 24060123120025 |
| Vern Dharmawan | 24060123130057 |
| Sion Yehezkiel Pardomuan | 24060123130103 |
| Zoe Mohamed | 24060123140182 |

---

## 📖 Deskripsi Proyek

Proyek "Sistem Perpustakaan Digital" ini adalah sebuah aplikasi desktop modern yang dikembangkan menggunakan JavaFX, dirancang untuk mengelola operasional perpustakaan secara efisien. Aplikasi ini memungkinkan pengguna untuk meminjam dan mengembalikan buku, serta menyediakan fitur lengkap bagi administrator untuk mengelola koleksi buku, data pengguna, dan melacak semua transaksi peminjaman. Fokus utama proyek ini adalah pada kemudahan penggunaan, kejelasan alur kerja, dan keandalan data dengan integrasi database MySQL.

## ✨ Fitur Utama

### 1. 👤 Manajemen Pengguna yang Dinamis

**Peran Pengguna Berbeda:**
- **Member:** Pengguna standar yang dapat menjelajahi katalog buku, meminjam buku, melihat riwayat peminjaman mereka, dan mengajukan permintaan pengembalian.
- **Guest:** Pengguna yang hanya dapat melihat buku yang tersedia.
- **Admin:** Pengguna dengan hak istimewa yang memiliki kontrol penuh atas sistem, termasuk manajemen buku, persetujuan/penolakan transaksi, dan modifikasi status anggota.

**Sistem Status Anggota Lanjutan (`Member.MemberStatus` Enum):**
- `ACTIVE`: Status default yang memungkinkan anggota untuk login dan menggunakan semua fitur yang relevan.
- `FREEZE`: Status pembatasan. Anggota dengan status ini tidak dapat login ke sistem. Ini adalah respons otomatis ketika anggota memiliki buku yang `OVERDUE` atau dapat diatur secara manual oleh admin.

**Pencarian dan Filter Pengguna di Dashboard Admin:**
- **Pencarian Teks:** Mencari berdasarkan nama pengguna, email, atau detail lainnya.
- **Filter Status:** Memfilter anggota berdasarkan status `ACTIVE` atau `FREEZE` untuk manajemen yang lebih terfokus.

### 2. 📚 Pengelolaan Koleksi Buku yang Komprehensif

**Operasi CRUD (Create, Read, Update, Delete) Buku (Admin Dashboard):**
- **Create (Tambah Buku):** Formulir intuitif untuk menambahkan buku baru dengan detail lengkap seperti judul, ISBN, penulis, genre, kategori, jumlah stok, penerbit, dan deskripsi.
- **Read (Lihat Detail Buku):** Menampilkan daftar buku dengan opsi untuk melihat detail lengkap setiap buku.
- **Update (Perbarui Buku):** Dialog khusus untuk memodifikasi informasi buku yang sudah ada. Dilengkapi dengan label bidang yang jelas dan opsi penutupan via tombol `Escape` untuk pengalaman pengguna yang lebih baik.
- **Delete (Hapus Buku):** Menghapus entri buku secara permanen dari database. Penting untuk dicatat bahwa ini adalah penghapusan langsung (*hard delete*), bukan perubahan status menjadi "dihapus".

**Tampilan Daftar Buku yang Ditingkatkan (Member Dashboard):**
Desain tampilan buku untuk anggota telah diperbaharui dari `GridPane` tradisional menjadi `FlowPane`. Ini memungkinkan setiap buku ditampilkan sebagai "kartu vertikal panjang" yang lebih besar dan informatif, menampilkan:
- Judul Buku
- Penulis
- Penerbit
- Tahun Terbit
- Genre & Kategori
- Deskripsi Singkat
- Ketersediaan Stok

### 3. 🔄 Otomatisasi dan Pelacakan Transaksi

**Alur Peminjaman dan Pengembalian yang Terstruktur:** Sistem mengelola seluruh siklus hidup transaksi, dari permintaan peminjaman hingga pengembalian akhir.

**Status Transaksi yang Detail (`Transaksi.Status` Enum):**
- `PENDING_PEMINJAMAN`: Anggota telah meminta buku, menunggu persetujuan admin.
- `ACCEPTED`: Permintaan peminjaman disetujui; buku ada pada anggota.
- `REJECTED`: Permintaan peminjaman atau pengembalian ditolak oleh admin.
- `PENDING_PENGEMBALIAN`: Anggota telah memulai proses pengembalian, menunggu konfirmasi admin.
- `RETURNED`: Buku berhasil dikembalikan dan dicatat dalam sistem.
- `OVERDUE`: Buku belum dikembalikan setelah tanggal jatuh tempo. Status ini penting karena memicu pembekuan anggota.

**Fitur Tambahan:**
- **Tombol "Kembalikan Buku" di Riwayat Anggota:** Anggota dapat dengan mudah mengajukan permintaan pengembalian untuk buku yang berstatus `ACCEPTED` langsung dari riwayat peminjaman mereka.
- **Kontrol Admin atas Pengembalian:** Admin dapat menerima atau menolak permintaan pengembalian.
- **Pembekuan Anggota Otomatis:** Sistem secara otomatis mengubah status anggota menjadi `FREEZE` jika ada transaksi peminjaman mereka yang berstatus `OVERDUE`.

### 4. 🎨 Peningkatan Antarmuka Pengguna (UI/UX)

- **Dialog "Update Buku" yang Disempurnakan:** Dengan label bidang yang jelas dan penutupan via tombol `Escape`.
- **Penyesuaian Lebar Kolom "Status" yang Responsif:** Kolom status diperlebar dan diatur untuk `wrapText` agar teks status panjang dapat ditampilkan sepenuhnya.
- **Penyederhanaan Teks Tombol Aksi Admin:** Tombol aksi disederhanakan menjadi "Setuju" dan "Tolak" untuk kejelasan maksimal.

## 🛠️ Teknologi yang Digunakan

| Teknologi | Deskripsi |
|-----------|-----------|
| **JavaFX** | Framework untuk membangun aplikasi desktop dengan antarmuka pengguna grafis yang kaya |
| **MySQL** | Sistem manajemen basis data relasional (RDBMS) untuk backend database |
| **Maven** | Alat manajemen proyek dan build untuk mengelola dependensi dan siklus hidup proyek |

## 📁 Struktur Folder Proyek
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       ├── controller/          # Kelas-kelas kontroler JavaFX
│   │   │       ├── DAO/                 # Data Access Objects
│   │   │       ├── model/               # Model data entities
│   │   │       ├── Service/             # Kelas layanan logika bisnis
│   │   │       ├── util/                # Kelas utilitas dan helper
│   │   │       └── App.java             # Main application class
│   │   └── resources/
│   │       └── com/
│   │           └── view/                # File FXML untuk UI
│   └── test/                            # Test source code
├── .vscode/                             # VS Code configuration
├── README.md                            # Project documentation
├── pom.xml                              # Maven POM file
└── target/                              # Maven build output

## 🚀 Cara Menjalankan Aplikasi

### 1. 📋 Prasyarat Sistem

Pastikan komponen berikut telah terinstal:

- **Java Development Kit (JDK) 11+** - [Download JDK](https://adoptium.net/temurin/releases/)
- **Apache Maven 3.6.0+** - [Download Maven](https://maven.apache.org/download.cgi)
- **MySQL Server 8.0+** - [Download MySQL](https://dev.mysql.com/downloads/mysql/)
- **IDE (Opsional):**
  - [IntelliJ IDEA Community](https://www.jetbrains.com/idea/download/) (Direkomendasikan)
  - [Eclipse IDE](https://www.eclipse.org/downloads/packages/)
