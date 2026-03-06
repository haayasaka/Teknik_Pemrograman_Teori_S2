import java.util.*;
import java.util.concurrent.*;

public class DataToko {

    private final List<Barang> daftarBarang = new ArrayList<>();

    private final Set<String> kategori = new HashSet<>();

    private final Map<String, Barang> mapBarang = new HashMap<>();

    private final ConcurrentHashMap<String, Integer> stok = new ConcurrentHashMap<>();

    private final Vector<String> log = new Vector<>();

    private static final List<String> HARI_BUKA = List.of("Senin", "Selasa", "Rabu", "Kamis", "Jumat");
    private static final Set<String> METODE_BAYAR = Set.of("Cash", "QRIS", "Transfer");
    private static final Map<String, Integer> DISKON = Map.of("Member", 10, "Promo", 20, "Normal", 0);

    public DataToko() {
        initBarang();
        log.add("Sistem dimulai");
    }

    private void initBarang() {
        tambahBarang(new Barang("A1", "Buku Tulis", 5000), 50);
        tambahBarang(new Barang("A2", "Pensil 2B", 3000), 50);
        tambahBarang(new Barang("A3", "Penghapus", 2000), 50);
        tambahBarang(new Barang("A4", "Penggaris", 4000), 50);
        tambahBarang(new Barang("A5", "Pulpen", 3500), 50);
        tambahBarang(new Barang("A6", "Rautan", 1500), 50);
    }

    private void tambahBarang(Barang b, int jumlah) {
        daftarBarang.add(b);
        mapBarang.put(b.kode(), b);
        kategori.add("Alat Tulis");
        stok.put(b.nama(), jumlah);
    }

    public Optional<Barang> cariByKode(String kode) {
        return Optional.ofNullable(mapBarang.get(kode));
    }

    public int getStok(String nama) {
        return stok.getOrDefault(nama, 0);
    }

    public void kurangiStok(String nama, int jumlah) {
        stok.compute(nama, (k, v) -> v != null ? v - jumlah : 0);
    }

    public void tambahLog(String pesan) {
        log.add(pesan);
    }

    // Getter
    public List<Barang> getDaftarBarang() {
        return daftarBarang;
    }

    public Set<String> getKategori() {
        return kategori;
    }

    public ConcurrentHashMap<String, Integer> getStokMap() {
        return stok;
    }

    public Vector<String> getLog() {
        return log;
    }

    public List<String> getHariBuka() {
        return HARI_BUKA;
    }

    public Set<String> getMetodeBayar() {
        return METODE_BAYAR;
    }

    public Map<String, Integer> getDiskon() {
        return DISKON;
    }
}
