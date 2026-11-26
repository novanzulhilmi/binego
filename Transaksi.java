public class Transaksi {
    private String idTransaksi;
    private String tipe;
    private String namaProduk;
    private long hargaFinal;

    public Transaksi(String idTransaksi, String tipe, String namaProduk, long hargaFinal){
        this.idTransaksi=idTransaksi;
        this.tipe=tipe;
        this.namaProduk=namaProduk;
        this.hargaFinal=hargaFinal;
    }

    public void tampilkanStruk(){
        System.out.printf("[%s] %-6s | %-25s | Total: Rp%,d%n", idTransaksi, tipe, namaProduk, hargaFinal);
    }
}
