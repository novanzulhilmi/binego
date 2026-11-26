public class BotPenawar {
    private String namaBot;
    private long maxBudget;
    private boolean aktif;

    public BotPenawar(String namaBot, long hargaAwalBarang){
        this.namaBot=namaBot;
        double faktor=1.10+(Math.random()*0.35);
        this.maxBudget=Math.round(hargaAwalBarang*faktor);
        this.aktif=true;
    }

    public String getNamaBot(){
        return namaBot;
    }

    public boolean isAktif() {
        return aktif;
    }

    public long pertimbangkanBid(long bidSekarang, long kelipatanBid){
        if(!aktif){
            return -1;
        }
        long calonBid=bidSekarang+kelipatanBid;
        if (calonBid>maxBudget) {
            aktif=false;
            return -1;
        }
        if (Math.random() < 0.65) {
            return calonBid;
        }
        
        return 0;
    }
}
