package ec.edu.espol;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    
    public static int decks = 0; //contador del numero de instancias

    //atributos
    private int id;
    private ArrayList<Carta> baraja;

    //constructor
    public Deck() {
        decks += 1;
        this.id = decks;
        this.baraja = (ArrayList<Carta>) cargarCartas();
    }
    
    //METODOS

    public List<Carta> cargarCartas() {
        List<Carta> baraja = new ArrayList<>();
        
        // Procesamiento de cartas monstruo
        List<Carta> cartasMonstruo = new ArrayList<>();
        try (BufferedReader archivo = new BufferedReader(new FileReader("src/archivoCartasMonstruo.txt"))) {
            archivo.readLine(); // Salta la primera línea
            String linea;
            while ((linea = archivo.readLine()) != null) {
                String[] lstDatos = linea.split(", ");
                String nombre = lstDatos[0];
                String tipoMonstruo = lstDatos[1];
                String tipoAtributo = lstDatos[2];
                int ataque= Integer.parseInt(lstDatos[3]);
                int defensa = Integer.parseInt(lstDatos[4]);
                String descripcion = lstDatos[5];
                cartasMonstruo.add(new CartaMonstruo(nombre, descripcion, ataque, defensa, TipoAtributo.valueOf(tipoAtributo), TipoMonstruo.valueOf(tipoMonstruo)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // Barajear las cartas monstruo
        barajear(cartasMonstruo);

        // Agregar 20 cartas monstruo a la baraja final
        for (int i = 0; i < 20; i++) {
            baraja.add(cartasMonstruo.get(i));
        }

        // Procesamiento de cartas mágicas
        List<Carta> cartasMagicas = new ArrayList<>();
        try (BufferedReader archivo = new BufferedReader(new FileReader("src/archivoCartasMagicas.txt"))) {
            archivo.readLine(); // Salta la primera línea
            String linea;
            while ((linea = archivo.readLine()) != null) {
                String[] lstDatos = linea.split(", ");
                String nombre = lstDatos[0];
                String descripcion = lstDatos[1];
                int incrementoAtaque = Integer.parseInt(lstDatos[2]);
                int incrementoDefensa = Integer.parseInt(lstDatos[3]);
                String tipoMonstruo = lstDatos[4].strip();
                cartasMagicas.add(new CartaMagica(nombre, descripcion, incrementoAtaque, incrementoDefensa, TipoMonstruo.valueOf(tipoMonstruo)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Barajear las cartas mágicas
        barajear(cartasMagicas);

        // Agregar 5 cartas mágicas a la baraja final
        for (int i = 0; i < 5; i++) {
            baraja.add(cartasMagicas.get(i));
        }

        // Procesamiento de cartas trampas
        List<Carta> cartasTrampas = new ArrayList<>();
        try (BufferedReader archivo = new BufferedReader(new FileReader("src/archivoCartasTrampa.txt"))) {
            archivo.readLine(); // Salta la primera línea
            String linea;
            while ((linea = archivo.readLine()) != null) {
                String[] lstDatos = linea.split(", ");
                String nombre = lstDatos[0];
                String descripcion = lstDatos[1];
                String atributo = lstDatos[2].strip();
                cartasTrampas.add(new CartaTrampa(nombre, descripcion, TipoAtributo.valueOf(atributo)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Barajear las cartas trampas
        barajear(cartasTrampas);

        // Agregar 5 cartas trampas a la baraja final
        for (int i = 0; i < 5; i++) {
            baraja.add(cartasTrampas.get(i));
        }

        // Barajear la baraja final y retornar
        barajear(baraja);

        return baraja;
    }

     public void barajear(List<Carta> baraja2) {
                            Collections.shuffle(baraja2);
    }

    // Devuelve una carta para el jugador tomada de la baraja y la elimina de la baraja
    public Carta robarCarta() {
        if (!baraja.isEmpty()) {
            return baraja.remove(0); // Elimina y devuelve la primera carta
        } else {
            return null; // Si la baraja está vacía, retorna null
        }
    }
    
    //ToString
    @Override
    public String toString() {
        StringBuilder lstAMostrar = new StringBuilder();
        for (Carta carta : this.baraja) {
            String nombreCarta = carta.getNombre();
            lstAMostrar.append(nombreCarta).append(", ");
        }
        // Elimina la última coma y espacio, si es necesario
        if (lstAMostrar.length() > 0) {
            lstAMostrar.setLength(lstAMostrar.length() - 2);
        }
        return lstAMostrar.toString();
    }
    
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
