package ec.edu.espol;

import java.util.HashMap;
import java.util.ArrayList;

public class Tablero {
    public int id;
    public Jugador jugador1;
    public Jugador jugador2;
    public HashMap< Integer, HashMap<String, ArrayList<Carta>> > tableroCompartido; //diccionario en java xD


    public Tablero(){
        this.id = 1;
        this.jugador1 = aniadirJugador();
        this.jugador2 = new Jugador("Maquina", new Deck());
        //inicializando el hasmap principal
        tableroCompartido = new HashMap<>();

        //creando espacio de cartas del jugador 1(diccionario q sera anidado)
        HashMap<String, ArrayList<Carta>> espacioJugador1 = new HashMap<>();
        espacioJugador1.put("CartasMonstruo", new ArrayList<>());
        espacioJugador1.put("CartasEspeciales", new ArrayList<>());
        //creando espacio de cartas del jugador 2(diccionario q sera anidado)
        HashMap<String, ArrayList<Carta>> espacioJugador2 = new HashMap<>();
        espacioJugador2.put("CartasMonstruo", new ArrayList<>());
        espacioJugador2.put("CartasEspeciales", new ArrayList<>());

        //anidando los diccionarios al principal
        tableroCompartido.put(jugador1.getId(), espacioJugador1);
        tableroCompartido.put(jugador2.getId(), espacioJugador2);

    }

    //METODOS
    public Jugador aniadirJugador(){
        return new Jugador(null, null);
    }




    //getters and setters
    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Jugador getJugador1() {
        return jugador1;
    }

    public void setJugador1(Jugador jugador1) {
        this.jugador1 = jugador1;
    }

    public Jugador getJugador2() {
        return jugador2;
    }

    public void setJugador2(Jugador jugador2) {
        this.jugador2 = jugador2;
    }

    public HashMap<Integer, HashMap<String, ArrayList<Carta>>> getTableroCompartido() {
        return tableroCompartido;
    }

    public void setTableroCompartido(HashMap<Integer, HashMap<String, ArrayList<Carta>>> tableroCompartido) {
        this.tableroCompartido = tableroCompartido;
    }


    
}
