# Analisis Penerapan 7 Konsep Java Collections

## Aplikasi Terminal: Toko Alat Tulis Sejahtera (Modular OOP)

---

## Struktur Program

| File                      | Peran               | Konsep                                                                         |
| ------------------------- | ------------------- | ------------------------------------------------------------------------------ |
| `Barang.java`             | Data class (Record) | **Record**                                                                     |
| `DataToko.java`           | Mengelola data toko | **List, Set, Map, Optional, Immutable Collections, Vector, ConcurrentHashMap** |
| `KelolaPesanan.java`      | Mengelola pesanan   | **Queue, Deque, Concurrent Collections**                                       |
| `TokoAlatTulisHafiz.java` | Main class + menu   | Integrasi semua konsep (OOP)                                                   |

---

## Pemetaan Konsep ke Menu

| Menu                    | Konsep                      |
| ----------------------- | --------------------------- |
| 1. Lihat Barang         | **List**, Set               |
| 2. Cari Barang          | **Map**, **Optional**       |
| 3. Buat Pesanan         | **Queue**, Optional, Record |
| 4. Proses Pesanan       | **Queue**, **Deque**        |
| 5. Lihat Riwayat        | **Deque**                   |
| 6. Lihat Log            | **Vector**                  |
| 7. Info Toko            | **Immutable Collections**   |
| 8. Simulasi Multi-Kasir | **Concurrent Collections**  |

---

## 1. List, Set, Map (`DataToko.java`)

```java
private final List<Barang> daftarBarang = new ArrayList<>();     // berurutan
private final Set<String> kategori = new HashSet<>();             // unik
private final Map<String, Barang> mapBarang = new HashMap<>();   // lookup O(1)
```

- **List**: Menyimpan daftar barang berurutan
- **Set**: Menyimpan kategori unik tanpa duplikat
- **Map**: Pencarian cepat berdasarkan kode barang

---

## 2. Record (`Barang.java`)

```java
public record Barang(String kode, String nama, double harga) {}
```

Otomatis membuat constructor, getter, toString, equals, hashCode. Data bersifat
immutable.

---

## 3. Optional (`DataToko.java`)

```java
public Optional<Barang> cariByKode(String kode) {
    return Optional.ofNullable(mapBarang.get(kode));
}
```

Digunakan di menu Cari Barang dan Buat Pesanan. Menghindari
NullPointerException.

---

## 4. Concurrent Collections (`DataToko.java` + `KelolaPesanan.java`)

```java
// DataToko: stok barang thread-safe
private final ConcurrentHashMap<String, Integer> stok = new ConcurrentHashMap<>();

// KelolaPesanan: simulasi 3 kasir bersamaan
CopyOnWriteArrayList<String> logKasir = new CopyOnWriteArrayList<>();
ExecutorService executor = Executors.newFixedThreadPool(3);
```

3 thread kasir mengubah stok bersamaan tanpa race condition.

---

## 5. Queue dan Deque (`KelolaPesanan.java`)

```java
private final Queue<String> antrian = new LinkedList<>();   // FIFO
private final Deque<String> riwayat = new ArrayDeque<>();   // dua arah

antrian.offer(pesanan);        // masuk belakang
antrian.poll();                // keluar depan (FIFO)

riwayat.offerLast(diproses);   // tambah di belakang
riwayat.pollFirst();           // hapus terlama dari depan
riwayat.peekFirst();           // lihat terlama
riwayat.peekLast();            // lihat terbaru
```

- **Queue**: Antrian pesanan FIFO
- **Deque**: Riwayat transaksi, akses dari kedua ujung

---

## 6. Immutable Collections (`DataToko.java`)

```java
private static final List<String> HARI_BUKA = List.of("Senin", "Selasa", "Rabu", "Kamis", "Jumat");
private static final Set<String> METODE_BAYAR = Set.of("Cash", "QRIS", "Transfer");
private static final Map<String, Integer> DISKON = Map.of("Member", 10, "Promo", 20, "Normal", 0);
```

Tidak bisa diubah. Jika dicoba akan melempar UnsupportedOperationException.

---

## 7. Vector (`DataToko.java`)

```java
private final Vector<String> log = new Vector<>();
log.add("Sistem dimulai");
```

Semua method synchronized, aman untuk multi-thread. Digunakan sebagai log
aktivitas.

---

## Cara Menjalankan

```bash
cd Challenge
javac *.java
java TokoAlatTulisHafiz
```

## Contoh Input

```
1              -> lihat daftar barang
[Enter]

2              -> cari barang
A1             -> kode barang
[Enter]

3              -> buat pesanan
Budi           -> nama pembeli
A1             -> kode barang
3              -> jumlah
[Enter]

3              -> buat pesanan lagi
Siti
A2
5
[Enter]

4              -> proses pesanan
y              -> konfirmasi
[Enter]

5              -> lihat riwayat
[Enter]

6              -> lihat log
[Enter]

7              -> info toko
[Enter]

8              -> simulasi multi-kasir
[Enter]

0              -> keluar
```
