import java.util.*;
import java.util.concurrent.*;

public class KelolaPesanan {

    private final Queue<String> antrian = new LinkedList<>();

    private final Deque<String> riwayat = new ArrayDeque<>();

    private static final int MAX_RIWAYAT = 10;

    public void tambahPesanan(String pesanan) {
        antrian.offer(pesanan);
    }

    public Optional<String> prosesPesanan() {
        String diproses = antrian.poll();
        if (diproses != null) {
            riwayat.offerLast(diproses);
            while (riwayat.size() > MAX_RIWAYAT) {
                riwayat.pollFirst();
            }
        }
        return Optional.ofNullable(diproses);
    }

    public CopyOnWriteArrayList<String> simulasiMultiKasir(DataToko data) {
        CopyOnWriteArrayList<String> logKasir = new CopyOnWriteArrayList<>();
        ExecutorService executor = Executors.newFixedThreadPool(3);
        CountDownLatch selesai = new CountDownLatch(3);

        executor.submit(() -> {
            data.kurangiStok("Buku Tulis", 2);
            logKasir.add("Kasir-1: Jual Buku Tulis x2");
            selesai.countDown();
        });
        executor.submit(() -> {
            data.kurangiStok("Pensil 2B", 3);
            logKasir.add("Kasir-2: Jual Pensil 2B x3");
            selesai.countDown();
        });
        executor.submit(() -> {
            data.kurangiStok("Pulpen", 1);
            logKasir.add("Kasir-3: Jual Pulpen x1");
            selesai.countDown();
        });

        try {
            selesai.await(3, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
        }
        executor.shutdown();
        return logKasir;
    }

    public Queue<String> getAntrian() {
        return antrian;
    }

    public Deque<String> getRiwayat() {
        return riwayat;
    }
}
