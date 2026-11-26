public class Produk{
    private String id;
    private String nama;
    private long hargaAwal;
    private boolean sudahTerjual;

    public Produk(String id, String nama, long hargaAwal){
        this.id=id;
        this.nama=nama;
        this.hargaAwal=hargaAwal;
        this.sudahTerjual=false;
    }

    public String getId(){
        return id;
    }
    
    public String getNama() {
        return nama;
    }

    public long getHargaAwal() {
        return hargaAwal;
    }

    public boolean isSudahTerjual() {
        return sudahTerjual;
    }

    public void tandaiTerjual() {
        this.sudahTerjual=true;
    }

    public void tampilkanInfoDasar(){
        String status = sudahTerjual ? "TERJUAL" : "TERSEDIA";
        System.out.printf("[%s] %s | Harga Awal: Rp%,d | Status: %s%n", id, nama, hargaAwal, status);
    }
}