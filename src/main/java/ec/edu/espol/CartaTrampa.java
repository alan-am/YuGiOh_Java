package ec.edu.espol;

public class CartaTrampa extends Carta {
    private TipoAtributo tipoAtributo;
    private boolean isBocaArriba;

    public CartaTrampa(String nombre, String descripcion, TipoAtributo tipoAtributo){
        super(nombre, descripcion);
        this.tipoAtributo = tipoAtributo;
        this.isBocaArriba = false; //por defecto
    }

    //toString





    
    //getters and setters
    public TipoAtributo getTipoAtributo() {
        return tipoAtributo;
    }

    public void setTipoAtributo(TipoAtributo tipoAtributo) {
        this.tipoAtributo = tipoAtributo;
    }

    public boolean isBocaArriba() {
        return isBocaArriba;
    }

    public void setBocaArriba(boolean isBocaArriba) {
        this.isBocaArriba = isBocaArriba;
    }

    
}
