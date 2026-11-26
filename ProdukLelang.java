public class ProdukLelang extends Produk {
    private long kelipatanBid;
    private long bidTertinggi;
    private String pemenangSementara;

    public ProdukLelang(String id, String nama, long hargaAwal, long kelipatanBid){
        super(id, nama, hargaAwal);
        this.kelipatanBid=kelipatanBid;
        this.bidTertinggi=hargaAwal;
        this.pemenangSementara="Belum Ada";
    }

    public long getKelipatanBid(){
        return kelipatanBid;
    }

    public long getBidTertinggi(){
        return bidTertinggi;
    }

    public String getPemenangSementara(){
        return pemenangSementara;
    }

    public long getMinimalBidBerikutnya(){
        return bidTertinggi+kelipatanBid;
    }
    public boolean ajukanBid(String namaPenawar, long nominalBid){
        if (nominalBid<getMinimalBidBerikutnya()) {
            return false;
        }
        this.bidTertinggi=nominalBid;
        this.pemenangSementara=namaPenawar;
        return true;
    }
}
