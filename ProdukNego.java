public class ProdukNego extends Produk {
    private long hargaMinimal;
    private int sisaKesabaran;

    public ProdukNego(String id, String nama, long hargaAwal, long hargaMinimal, int sisaKesabaran) {
        super(id, nama, hargaAwal);
        this.hargaMinimal = hargaMinimal;
        this.sisaKesabaran = sisaKesabaran;
    }

    public long getHargaMinimal() {
        return hargaMinimal;
    }

    public int getSisaKesabaran() {
        return sisaKesabaran;
    }

    public String evaluasiTawaran(long nominalTawaran) {
        if (sisaKesabaran <= 0) {
            return "HABIS";
        }

        if (nominalTawaran >= hargaMinimal) {
            return "DEAL";
        }

        sisaKesabaran = sisaKesabaran - 1;
        if (sisaKesabaran > 0) {
            return "DITOLAK_LANJUT";
        } else {
            return "HABIS";
        }
    }
}