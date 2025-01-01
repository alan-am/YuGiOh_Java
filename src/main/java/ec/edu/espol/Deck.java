package ec.edu.espol;

import java.util.ArrayList;

public class Deck {
    
    public static int decks = 0; //contador del numero de instancias

    //atributos
    private int id;
    private ArrayList<Carta> baraja;

    //constructor
    public Deck() {
        decks += 1;
        this.id = decks;
        this.baraja = cargarCartas();
    }
    
    //METODOS
    public ArrayList<Carta> cargarCartas(){
        ArrayList<Carta> baraja = new ArrayList<>();
        return baraja;
    }
    
    //ToString




    //getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<Carta> getBaraja() {
        return baraja;
    }

    public void setBaraja(ArrayList<Carta> baraja) {
        this.baraja = baraja;
    }


}
