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

    public CartaMonstruo(String nombre, String descripcion,int ataque, int defensa, TipoAtributo tipoAtributo, TipoMonstruo tipoMonstruo){
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
    @Override
    public String toString() {
        return "------------------- CARTA MONSTRUO -------------------\n" +
               super.getNombre() + "\n" +
               "    Tipo: " + this.tipoMonstruo + "   Atributo: " + this.tipoAtributo + "\n" +
               "    ATK: " + this.ataque + "               DEF: " + this.defensa + "\n" +
               (this.isInAtaque ? "    MODO DE ATAQUE: Sí\n" : "    MODO DE ATAQUE: No\n") +
               "------------------- Descripción -------------------\n" +
               super.getDescripcion() + "\n" +
               "---------------------------------------------------";
    }
    

    public String toString2() {
        String modo = this.isInAtaque ? "Ataque" : "Defensa";
        return "CARTA MONSTRUO || " + this.getNombre() +
               " [MODO " + modo + "]" +
               " [ATK: " + this.ataque + ", DEF: " + this.defensa + "]" +
               "   Tipo: " + this.tipoMonstruo + ", ATR: " + this.tipoAtributo;
    }

    public String toString3() {
        String modo = this.isInAtaque ? "Ataque" : "Defensa";
        Integer incAtaque = 0;
        Integer incDefensa = 0;

        if(this.cartaMagica != null){
            incAtaque = (Integer)(this.obtenerIncrementosATKyDEF().obj1);
            incDefensa = (Integer)(this.obtenerIncrementosATKyDEF().obj2);
        }
        return "CARTA MONSTRUO || " + this.getNombre() +
               " [MODO " + modo + "]" +
               " [ATK: " + this.ataque + " + " + incAtaque +
               ", DEF: " + this.defensa + " + " + incDefensa + "]" +
               "   Tipo: " + this.tipoMonstruo;
    }
    
    public Tupla obtenerIncrementosATKyDEF(){
        if(this.getCartaMagica() != null){
            //si tiene asociada -> se guarda sus incrementos de ATK y DEF y se devuelven
            Integer incAtk = this.getCartaMagica().getIncrementoAtaque();
            Integer incDef = this.getCartaMagica().getIncrementoDefensa();
            return new Tupla(incAtk, incDef);
        }else{
            return new Tupla(0,0);
        }
    }
    

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

    public boolean getisInAtaque() {
        return isInAtaque;
    }

    public void setisInAtaque(boolean isInAtaque) {
        this.isInAtaque = isInAtaque;
    }

    public boolean getisBocaArriba() {
        return isBocaArriba;
    }

    public void setBocaArriba(boolean isBocaArriba) {
        this.isBocaArriba = isBocaArriba;
    }

    public boolean getPuedeAtacar() {
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



	public boolean getIsBocaArriba() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'getIsBocaArriba'");
	}



    public boolean getIsInAtaque() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getIsInAtaque'");
    }

    

}
