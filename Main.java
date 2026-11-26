import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final ArrayList<Produk> daftarProduk = new ArrayList<>();
    private static final ArrayList<Transaksi> riwayatTransaksi = new ArrayList<>();

    private static long saldoPembeli = 1_500_000L;
    private static long pemasukanPenjual = 0L;
    private static int counterTransaksi = 1;
    private static int counterProduk = 1;

    public static void main(String[] args) {
        seedDataAwal();
        tampilkanMenuUtama();
    }

    private static void seedDataAwal() {
        daftarProduk.add(new ProdukNego("P01", "Mechanical Keyboard 65%", 450_000L, 360_000L, 3));
        daftarProduk.add(new ProdukNego("P02", "Headset Wireless Bekas", 300_000L, 240_000L, 3));
        daftarProduk.add(new ProdukLelang("P03", "Kamera Action Cam", 600_000L, 25_000L));
        daftarProduk.add(new ProdukLelang("P04", "Monitor Gaming 24 Inch", 900_000L, 50_000L));
        counterProduk = 5;
    }

    private static void tampilkanMenuUtama() {
        boolean jalan = true;
        while (jalan) {
            System.out.println("\n==========================================");
            System.out.println("             BINEGO CONSOLE             ");
            System.out.println("==========================================");
            System.out.printf("Saldo Dompet Pembeli : Rp%,d%n", saldoPembeli);
            System.out.printf("Total Pemasukan Penjual: Rp%,d%n", pemasukanPenjual);
            System.out.println("------------------------------------------");
            System.out.println("1. Mode Pembeli (Lihat Katalog, Nego, atau Ikut Lelang)");
            System.out.println("2. Mode Penjual (Tambah Barang Nego/Lelang & Cek Stok)");
            System.out.println("3. Riwayat Transaksi");
            System.out.println("0. Keluar");
            System.out.println("------------------------------------------");

            int pilihan = bacaIntAman("Pilih menu: ");
            if (pilihan == 1) {
                modePembeli();
            } else if (pilihan == 2) {
                modePenjual();
            } else if (pilihan == 3) {
                tampilkanRiwayatTransaksi();
            } else if (pilihan == 0) {
                jalan = false;
                System.out.println("Terima kasih telah menggunakan BINEGO.");
            } else {
                System.out.println("Pilihan tidak valid.");
            }
        }
    }

    private static void modePembeli() {
        ArrayList<Produk> tersedia = new ArrayList<>();
        for (Produk p : daftarProduk) {
            if (!p.isSudahTerjual()) {
                tersedia.add(p);
            }
        }

        System.out.println("\n--- KATALOG BARANG TERSEDIA ---");
        if (tersedia.isEmpty()) {
            System.out.println("Belum ada barang yang tersedia saat ini.");
            return;
        }

        for (int i = 0; i < tersedia.size(); i++) {
            Produk p = tersedia.get(i);
            String tipe = (p instanceof ProdukLelang) ? "LELANG" : "NEGO";
            System.out.printf("%d. [%s] %s (%s) - Harga Awal: Rp%,d%n",
                    (i + 1), p.getId(), p.getNama(), tipe, p.getHargaAwal());
        }
        System.out.println("0. Kembali ke Menu Utama");

        int pilih = bacaIntAman("Pilih nomor barang: ");
        if (pilih == 0) {
            return;
        }
        if (pilih < 1 || pilih > tersedia.size()) {
            System.out.println("Nomor barang tidak valid.");
            return;
        }

        Produk terpilih = tersedia.get(pilih - 1);
        if (terpilih instanceof ProdukNego) {
            prosesNego((ProdukNego) terpilih);
        } else if (terpilih instanceof ProdukLelang) {
            prosesLelang((ProdukLelang) terpilih);
        }
    }

    private static void prosesNego(ProdukNego produk) {
        System.out.println("\n--- SESI NEGO: " + produk.getNama() + " ---");
        System.out.printf("Harga Awal: Rp%,d | Sisa Kesabaran: %d | Saldo Anda: Rp%,d%n",
                produk.getHargaAwal(), produk.getSisaKesabaran(), saldoPembeli);

        while (true) {
            System.out.println("\n1. Ajukan Tawaran Harga");
            System.out.println("2. Beli Langsung (Harga Awal: Rp" + String.format("%,d", produk.getHargaAwal()) + ")");
            System.out.println("0. Batal / Kembali");

            int aksi = bacaIntAman("Pilih aksi: ");
            if (aksi == 0) {
                System.out.println("Sesi nego dibatalkan.");
                return;
            } else if (aksi == 2) {
                selesaikanPembelian(produk, "NEGO", produk.getHargaAwal());
                return;
            } else if (aksi == 1) {
                if (produk.getSisaKesabaran() <= 0) {
                    System.out.println("Kesabaran penjual sudah habis! Anda hanya bisa membeli di Harga Awal.");
                    continue;
                }

                long tawaran = bacaLongAman("Masukkan nominal tawaran (Rp): ");
                if (tawaran <= 0) {
                    System.out.println("Nominal tawaran harus lebih dari 0.");
                    continue;
                }

                String hasil = produk.evaluasiTawaran(tawaran);
                if ("DEAL".equals(hasil)) {
                    System.out.println("Tawaran DI-ACC otomatis! Harga sepakat: Rp" + String.format("%,d", tawaran));
                    selesaikanPembelian(produk, "NEGO", tawaran);
                    return;
                } else if ("DITOLAK_LANJUT".equals(hasil)) {
                    System.out.printf("Tawaran ditolak! Sisa kesabaran penjual sekarang: %d%n",
                            produk.getSisaKesabaran());
                } else {
                    System.out.println("Tawaran ditolak dan kesabaran penjual sudah habis!");
                    System.out.printf("Anda masih bisa membeli di Harga Awal (Rp%,d) atau membatalkan.%n",
                            produk.getHargaAwal());
                    int lanjut = bacaIntAman("Beli di Harga Awal sekarang? (1 = Ya, 0 = Tidak): ");
                    if (lanjut == 1) {
                        selesaikanPembelian(produk, "NEGO", produk.getHargaAwal());
                    } else {
                        System.out.println("Negosiasi berakhir. Kembali ke katalog pembeli.");
                    }
                    return;
                }
            } else {
                System.out.println("Pilihan tidak valid.");
            }
        }
    }

    private static void prosesLelang(ProdukLelang produk) {
        System.out.println("\n--- ARENA LELANG: " + produk.getNama() + " ---");
        System.out.printf("Harga Awal: Rp%,d | Kelipatan Bid: Rp%,d | Saldo Anda: Rp%,d%n",
                produk.getHargaAwal(), produk.getKelipatanBid(), saldoPembeli);

        BotPenawar[] bots = {
                new BotPenawar("Bot_Rama", produk.getHargaAwal()),
                new BotPenawar("Bot_Sinta", produk.getHargaAwal()),
                new BotPenawar("Bot_Budi", produk.getHargaAwal())
        };

        int maxRonde = 4;
        boolean pembeliMundur = false;

        for (int ronde = 1; ronde <= maxRonde; ronde++) {
            System.out.printf("%n>>> RONDE %d / %d <<<%n", ronde, maxRonde);
            System.out.printf("Bid Tertinggi Saat Ini: Rp%,d (Penawar: %s)%n",
                    produk.getBidTertinggi(), produk.getPemenangSementara());
            System.out.printf("Minimal Bid Berikutnya: Rp%,d | Saldo Anda: Rp%,d%n",
                    produk.getMinimalBidBerikutnya(), saldoPembeli);

            System.out.println("1. Naikkan Tawaran (Bid)");
            System.out.println("2. Mundur dari Lelang");
            int pilih = bacaIntAman("Aksi Anda: ");

            if (pilih == 2) {
                pembeliMundur = true;
                System.out.println("Anda memilih mundur dari lelang.");
                break;
            } else if (pilih == 1) {
                long bidBaru = bacaLongAman("Masukkan nominal bid Anda: ");
                if (bidBaru > saldoPembeli) {
                    System.out.println("Bid melebihi saldo dompet Anda! Giliran ini dilewati.");
                } else if (!produk.ajukanBid("Pembeli", bidBaru)) {
                    System.out.printf("Bid terlalu rendah! Minimal Rp%,d. Giliran ini dilewati.%n",
                            produk.getMinimalBidBerikutnya());
                } else {
                    System.out.printf("Bid Anda diterima! Tertinggi sekarang Rp%,d oleh Pembeli.%n",
                            produk.getBidTertinggi());
                }
            } else {
                System.out.println("Pilihan tidak valid, giliran dilewati.");
            }

            int botAktifCount = 0;
            for (BotPenawar bot : bots) {
                long respons = bot.pertimbangkanBid(produk.getBidTertinggi(), produk.getKelipatanBid());
                if (respons > 0) {
                    produk.ajukanBid(bot.getNamaBot(), respons);
                    System.out.printf("  [%s] menaikkan bid menjadi Rp%,d%n", bot.getNamaBot(), respons);
                    botAktifCount++;
                } else if (respons == 0) {
                    System.out.printf("  [%s] menahan tawaran di ronde ini.%n", bot.getNamaBot());
                    botAktifCount++;
                } else {
                    if (bot.isAktif()) {
                        botAktifCount++;
                    } else {
                        System.out.printf("  [%s] mundur (batas budget tercapai).%n", bot.getNamaBot());
                    }
                }
            }

            if (botAktifCount == 0 && "Pembeli".equals(produk.getPemenangSementara())) {
                System.out.println("Semua bot sudah mundur! Anda unggul sementara.");
                break;
            }
        }

        System.out.println("\n--- HASIL AKHIR LELANG ---");
        System.out.printf("Bid Tertinggi: Rp%,d | Pemenang: %s%n",
                produk.getBidTertinggi(), produk.getPemenangSementara());

        if (!pembeliMundur && "Pembeli".equals(produk.getPemenangSementara())) {
            selesaikanPembelian(produk, "LELANG", produk.getBidTertinggi());
        } else {
            System.out.println("Anda tidak memenangkan lelang ini. Barang tetap tersedia.");
        }
    }

    private static void selesaikanPembelian(Produk produk, String tipe, long hargaFinal) {
        if (produk.isSudahTerjual()) {
            System.out.println("Barang ini sudah terjual.");
            return;
        }
        if (hargaFinal > saldoPembeli) {
            System.out.printf("Saldo tidak cukup! Dibutuhkan Rp%,d, saldo Anda Rp%,d.%n",
                    hargaFinal, saldoPembeli);
            return;
        }

        saldoPembeli = saldoPembeli - hargaFinal;
        pemasukanPenjual = pemasukanPenjual + hargaFinal;
        produk.tandaiTerjual();

        String idTrx = String.format("TRX-%03d", counterTransaksi++);
        Transaksi trxBaru = new Transaksi(idTrx, tipe, produk.getNama(), hargaFinal);
        riwayatTransaksi.add(trxBaru);

        System.out.println("\nTransaksi BERHASIL! Struk:");
        trxBaru.tampilkanStruk();
    }

    private static void modePenjual() {
        while (true) {
            System.out.println("\n--- MODE PENJUAL ---");
            System.out.printf("Total Pemasukan Penjual: Rp%,d%n", pemasukanPenjual);
            System.out.println("1. Tambah Barang Nego");
            System.out.println("2. Tambah Barang Lelang");
            System.out.println("3. Lihat Semua Barang & Status");
            System.out.println("0. Kembali ke Menu Utama");

            int pilih = bacaIntAman("Pilih menu penjual: ");
            if (pilih == 0) {
                return;
            } else if (pilih == 1) {
                tambahBarangNego();
            } else if (pilih == 2) {
                tambahBarangLelang();
            } else if (pilih == 3) {
                tampilkanSemuaBarang();
            } else {
                System.out.println("Pilihan tidak valid.");
            }
        }
    }

    private static void tambahBarangNego() {
        System.out.print("Nama barang nego: ");
        String nama = scanner.nextLine().trim();
        if (nama.isEmpty()) {
            System.out.println("Nama barang tidak boleh kosong.");
            return;
        }

        long hargaAwal = bacaLongAman("Harga awal (Rp): ");
        long hargaMinimal = bacaLongAman("Harga minimal rahasia (Rp): ");
        int kesabaran = bacaIntAman("Jumlah kesabaran (misal 3): ");

        if (hargaAwal <= 0 || hargaMinimal <= 0 || kesabaran <= 0) {
            System.out.println("Input harga dan kesabaran harus lebih dari 0.");
            return;
        }
        if (hargaMinimal > hargaAwal) {
            System.out.println("Harga minimal tidak boleh melebihi harga awal.");
            return;
        }

        String id = String.format("P%02d", counterProduk++);
        daftarProduk.add(new ProdukNego(id, nama, hargaAwal, hargaMinimal, kesabaran));
        System.out.println("Barang nego berhasil ditambahkan dengan ID: " + id);
    }

    private static void tambahBarangLelang() {
        System.out.print("Nama barang lelang: ");
        String nama = scanner.nextLine().trim();
        if (nama.isEmpty()) {
            System.out.println("Nama barang tidak boleh kosong.");
            return;
        }

        long hargaAwal = bacaLongAman("Harga buka lelang (Rp): ");
        long kelipatan = bacaLongAman("Kelipatan kenaikan bid (Rp): ");

        if (hargaAwal <= 0 || kelipatan <= 0) {
            System.out.println("Harga buka dan kelipatan bid harus lebih dari 0.");
            return;
        }

        String id = String.format("P%02d", counterProduk++);
        daftarProduk.add(new ProdukLelang(id, nama, hargaAwal, kelipatan));
        System.out.println("Barang lelang berhasil ditambahkan dengan ID: " + id);
    }

    private static void tampilkanSemuaBarang() {
        System.out.println("\n--- DAFTAR SEMUA BARANG ---");
        if (daftarProduk.isEmpty()) {
            System.out.println("Belum ada barang.");
            return;
        }
        for (Produk p : daftarProduk) {
            p.tampilkanInfoDasar();
        }
    }

    private static void tampilkanRiwayatTransaksi() {
        System.out.println("\n--- RIWAYAT TRANSAKSI ---");
        if (riwayatTransaksi.isEmpty()) {
            System.out.println("Belum ada transaksi yang selesai.");
            return;
        }
        for (Transaksi t : riwayatTransaksi) {
            t.tampilkanStruk();
        }
    }

    private static int bacaIntAman(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Masukkan angka bulat yang valid.");
            }
        }
    }

    private static long bacaLongAman(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return Long.parseLong(input);
            } catch (NumberFormatException e) {
                System.out.println("Masukkan nominal angka yang valid.");
            }
        }
    }
}