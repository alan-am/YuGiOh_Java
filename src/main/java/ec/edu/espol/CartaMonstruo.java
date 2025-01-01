package ec.edu.espol;

public class CartaMonstruo extends Carta{

    private TipoAtributo tipoAtributo;
    private TipoMonstruo tipoMonstruo;
    private int ataque;
    private int defensa;
    private boolean isInAtaque; //para saber si esta en posicion de ataque y defensa
    private boolean isBocaArriba;
    private boolean puedeAtacar;   // indica si ya ha atacado en el turno
    private CartaMagica cartaMagica; //un monstruo puede estar asociado a una carta magica que incremente sus atributos

    public CartaMonstruo(String nombre, String descripcion, TipoAtributo tipoAtributo, TipoMonstruo tipoMonstruo, int ataque, int defensa){
        super(nombre, descripcion);
        this.tipoAtributo = tipoAtributo;
        this.tipoMonstruo = tipoMonstruo;
        this.ataque = ataque;
        this.defensa = defensa;
        this.isInAtaque = true; //por defecto
        this.puedeAtacar = true;
        this.isBocaArriba = true;
        this.cartaMagica = null;
    }
    //toString





    

    //getters and setters
    public TipoAtributo getTipoAtributo() {
        return tipoAtributo;
    }

    public void setTipoAtributo(TipoAtributo tipoAtributo) {
        this.tipoAtributo = tipoAtributo;
    }

    public TipoMonstruo getTipoMonstruo() {
        return tipoMonstruo;
    }

    public void setTipoMonstruo(TipoMonstruo tipoMonstruo) {
        this.tipoMonstruo = tipoMonstruo;
    }

    public int getAtaque() {
        return ataque;
    }

    public void setAtaque(int ataque) {
        this.ataque = ataque;
    }

    public int getDefensa() {
        return defensa;
    }

    public void setDefensa(int defensa) {
        this.defensa = defensa;
    }

    public boolean isInAtaque() {
        return isInAtaque;
    }

    public void setInAtaque(boolean isInAtaque) {
        this.isInAtaque = isInAtaque;
    }

    public boolean isBocaArriba() {
        return isBocaArriba;
    }

    public void setBocaArriba(boolean isBocaArriba) {
        this.isBocaArriba = isBocaArriba;
    }

    public boolean isPuedeAtacar() {
        return puedeAtacar;
    }

    public void setPuedeAtacar(boolean puedeAtacar) {
        this.puedeAtacar = puedeAtacar;
    }

    public CartaMagica getCartaMagica() {
        return cartaMagica;
    }

    public void setCartaMagica(CartaMagica cartaMagica) {
        this.cartaMagica = cartaMagica;
    }

    

}
