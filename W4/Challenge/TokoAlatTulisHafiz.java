import java.util.*;
import java.util.concurrent.*;

public class TokoAlatTulisHafiz {

    static DataToko data = new DataToko();
    static KelolaPesanan pesanan = new KelolaPesanan();
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        boolean running = true;
        while (running) {
            clearScreen();
            System.out.println("========================================");
            System.out.println("          TOKO ALAT TULIS HAFIZ         ");
            System.out.println("========================================");
            System.out.println("  1. Lihat Barang");
            System.out.println("  2. Cari Barang");
            System.out.println("  3. Buat Pesanan");
            System.out.println("  4. Proses Pesanan");
            System.out.println("  5. Lihat Riwayat");
            System.out.println("  6. Lihat Log Aktivitas");
            System.out.println("  7. Info Toko");
            System.out.println("  8. Simulasi Multi-Kasir");
            System.out.println("  0. Keluar");
            System.out.println("----------------------------------------");
            System.out.print("  Pilihan: ");

            String pilihan = scanner.nextLine().trim();
            switch (pilihan) {
                case "1" -> lihatBarang();
                case "2" -> cariBarang();
                case "3" -> buatPesanan();
                case "4" -> prosesPesanan();
                case "5" -> lihatRiwayat();
                case "6" -> lihatLog();
                case "7" -> infoToko();
                case "8" -> simulasiMultiKasir();
                case "0" -> {
                    running = false;
                    clearScreen();
                    System.out.println("Terima kasih! Sampai jumpa.");
                }
                default -> {
                    System.out.println("  Pilihan tidak valid.");
                    tunggu();
                }
            }
        }
        scanner.close();
    }

    static void lihatBarang() {
        clearScreen();
        System.out.println("========================================");
        System.out.println("          DAFTAR BARANG (List)          ");
        System.out.println("========================================");
        System.out.printf("  %-6s %-15s %8s %6s%n", "Kode", "Nama", "Harga", "Stok");
        System.out.println("  " + "-".repeat(37));
        for (Barang b : data.getDaftarBarang()) {
            System.out.printf("  %-6s %-15s Rp%,5.0f   %3d%n",
                    b.kode(), b.nama(), b.harga(), data.getStok(b.nama()));
        }
        System.out.println("  " + "-".repeat(37));
        System.out.println("  Kategori (Set): " + data.getKategori());
        data.tambahLog("Melihat daftar barang");
        tunggu();
    }

    static void cariBarang() {
        clearScreen();
        System.out.println("========================================");
        System.out.println("       CARI BARANG (Map + Optional)     ");
        System.out.println("========================================");
        System.out.print("  Masukkan kode (A1-A6): ");
        String kode = scanner.nextLine().trim().toUpperCase();

        data.cariByKode(kode).ifPresentOrElse(
                b -> {
                    System.out.println("\n  Ditemukan!");
                    System.out.println("  Kode  : " + b.kode());
                    System.out.println("  Nama  : " + b.nama());
                    System.out.printf("  Harga : Rp %,.0f%n", b.harga());
                    System.out.println("  Stok  : " + data.getStok(b.nama()));
                },
                () -> System.out.println("\n  Barang '" + kode + "' tidak ditemukan."));
        data.tambahLog("Mencari barang: " + kode);
        tunggu();
    }

    static void buatPesanan() {
        clearScreen();
        System.out.println("========================================");
        System.out.println("          BUAT PESANAN (Queue)          ");
        System.out.println("========================================");
        System.out.print("  Nama pembeli: ");
        String nama = scanner.nextLine().trim();
        if (nama.isEmpty()) {
            System.out.println("  Nama tidak boleh kosong.");
            tunggu();
            return;
        }

        System.out.print("  Kode barang : ");
        String kode = scanner.nextLine().trim().toUpperCase();

        Optional<Barang> opt = data.cariByKode(kode);
        if (opt.isEmpty()) {
            System.out.println("  Barang tidak ditemukan.");
            tunggu();
            return;
        }

        Barang b = opt.get();
        System.out.print("  Jumlah      : ");
        int jml;
        try {
            jml = Integer.parseInt(scanner.nextLine().trim());
            if (jml <= 0) {
                System.out.println("  Jumlah harus > 0.");
                tunggu();
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("  Input tidak valid.");
            tunggu();
            return;
        }

        int stok = data.getStok(b.nama());
        if (jml > stok) {
            System.out.println("  Stok tidak cukup! (sisa: " + stok + ")");
            tunggu();
            return;
        }

        String info = nama + " - " + b.nama() + " x" + jml +
                " (Rp " + String.format("%,.0f", b.harga() * jml) + ")";
        pesanan.tambahPesanan(info);
        data.kurangiStok(b.nama(), jml);
        data.tambahLog("Pesanan dibuat: " + info);

        System.out.println("\n  Pesanan masuk antrian!");
        System.out.println("  " + info);
        System.out.println("  Antrian saat ini: " + pesanan.getAntrian().size() + " pesanan");
        tunggu();
    }

    static void prosesPesanan() {
        clearScreen();
        System.out.println("========================================");
        System.out.println("      PROSES PESANAN (Queue + Deque)    ");
        System.out.println("========================================");

        if (pesanan.getAntrian().isEmpty()) {
            System.out.println("  Tidak ada pesanan dalam antrian.");
            tunggu();
            return;
        }

        System.out.println("  Antrian saat ini (" + pesanan.getAntrian().size() + "):");
        int i = 1;
        for (String p : pesanan.getAntrian()) {
            System.out.println("    " + i++ + ". " + p);
        }

        System.out.print("\n  Proses pesanan terdepan? (y/n): ");
        if (scanner.nextLine().trim().equalsIgnoreCase("y")) {
            pesanan.prosesPesanan().ifPresent(diproses -> {
                data.tambahLog("Pesanan diproses: " + diproses);
                System.out.println("\n  Diproses: " + diproses);
                System.out.println("  Sisa antrian: " + pesanan.getAntrian().size());
            });
        } else {
            System.out.println("  Dibatalkan.");
        }
        tunggu();
    }

    static void lihatRiwayat() {
        clearScreen();
        System.out.println("========================================");
        System.out.println("       RIWAYAT TRANSAKSI (Deque)        ");
        System.out.println("========================================");

        if (pesanan.getRiwayat().isEmpty()) {
            System.out.println("  Belum ada transaksi.");
        } else {
            int i = 1;
            for (String r : pesanan.getRiwayat()) {
                System.out.println("    " + i++ + ". " + r);
            }
            System.out.println("  " + "-".repeat(37));
            System.out.println("  Terlama : " + pesanan.getRiwayat().peekFirst());
            System.out.println("  Terbaru : " + pesanan.getRiwayat().peekLast());
        }
        data.tambahLog("Melihat riwayat transaksi");
        tunggu();
    }

    static void lihatLog() {
        clearScreen();
        System.out.println("========================================");
        System.out.println("       LOG AKTIVITAS (Vector)           ");
        System.out.println("========================================");
        for (int i = 0; i < data.getLog().size(); i++) {
            System.out.printf("  %2d. %s%n", i + 1, data.getLog().get(i));
        }
        System.out.println("  " + "-".repeat(37));
        System.out.println("  Total: " + data.getLog().size() + " log");
        tunggu();
    }

    static void infoToko() {
        clearScreen();
        System.out.println("========================================");
        System.out.println("    INFO TOKO (Immutable Collections)   ");
        System.out.println("========================================");
        System.out.println("  Hari buka (List.of)   : " + data.getHariBuka());
        System.out.println("  Pembayaran (Set.of)   : " + data.getMetodeBayar());
        System.out.println("  Diskon (Map.of)       :");
        data.getDiskon().forEach((k, v) -> System.out.println("    - " + k + ": " + v + "%"));

        System.out.println("\n  Immutable = tidak bisa diubah:");
        try {
            data.getHariBuka().add("Sabtu");
        } catch (UnsupportedOperationException e) {
            System.out.println("    List.of -> gagal ditambah!");
        }
        try {
            data.getMetodeBayar().add("Bitcoin");
        } catch (UnsupportedOperationException e) {
            System.out.println("    Set.of  -> gagal ditambah!");
        }
        try {
            data.getDiskon().put("VIP", 50);
        } catch (UnsupportedOperationException e) {
            System.out.println("    Map.of  -> gagal ditambah!");
        }

        data.tambahLog("Melihat info toko");
        tunggu();
    }

    static void simulasiMultiKasir() {
        clearScreen();
        System.out.println("========================================");
        System.out.println("  SIMULASI MULTI-KASIR (Concurrent)     ");
        System.out.println("========================================");
        System.out.println("  Stok sebelum:");
        data.getStokMap().forEach((n, s) -> System.out.println("    " + n + ": " + s));

        CopyOnWriteArrayList<String> logKasir = pesanan.simulasiMultiKasir(data);

        System.out.println("\n  3 kasir melayani bersamaan...");
        for (String l : logKasir)
            System.out.println("    " + l);

        System.out.println("\n  Stok sesudah:");
        data.getStokMap().forEach((n, s) -> System.out.println("    " + n + ": " + s));
        System.out.println("\n  Data aman! Tidak ada race condition.");
        data.tambahLog("Simulasi multi-kasir dijalankan");
        tunggu();
    }

    static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    static void tunggu() {
        System.out.print("\n  Tekan Enter untuk kembali...");
        scanner.nextLine();
    }
}
