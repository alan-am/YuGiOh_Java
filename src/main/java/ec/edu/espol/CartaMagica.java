package ec.edu.espol;

public class CartaMagica extends Carta {

    private int incrementoAtaque;
    private int incrementoDefensa;
    private TipoMonstruo tipoMonstruo;
    private boolean isBocaArriba;

    public CartaMagica(String nombre, String descripcion, int incATK, int incDEF, TipoMonstruo tipoMonstruo){

        super(nombre, descripcion);
        this.incrementoAtaque = incATK;
        this.incrementoDefensa = incDEF;
        this.tipoMonstruo = tipoMonstruo;
        this.isBocaArriba = true; //una carta magica siempre esta boca arriba

    }

    //toString






    
    //getter and setters
    public int getIncrementoAtaque() {
        return incrementoAtaque;
    }

    public void setIncrementoAtaque(int incrementoAtaque) {
        this.incrementoAtaque = incrementoAtaque;
    }

    public int getIncrementoDefensa() {
        return incrementoDefensa;
    }

    public void setIncrementoDefensa(int incrementoDefensa) {
        this.incrementoDefensa = incrementoDefensa;
    }

    public TipoMonstruo getTipoMonstruo() {
        return tipoMonstruo;
    }

    public void setTipoMonstruo(TipoMonstruo tipoMonstruo) {
        this.tipoMonstruo = tipoMonstruo;
    }

    public boolean isBocaArriba() {
        return isBocaArriba;
    }

    public void setBocaArriba(boolean isBocaArriba) {
        this.isBocaArriba = isBocaArriba;
    }

    
}
