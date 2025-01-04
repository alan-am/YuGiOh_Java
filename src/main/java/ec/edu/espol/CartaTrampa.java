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

    @Override
    public String toString() {
        return "................. CARTA TRAMPA .................\n" +
               super.getNombre() + "\n" +
               "    Atributo: " + this.tipoAtributo + "\n" +
               "................. Descripción .................\n" +
               super.getDescripcion() + "\n" +
               ".............................................";
    }
       

    public String toString2() {
        return "CARTA TRAMPA || " + this.getNombre() +
               " Atributo: " + this.tipoAtributo;
    }
    



    
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
