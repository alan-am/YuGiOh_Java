package ec.edu.espol;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class Tablero {
    public int id;
    public Jugador jugador1;
    public Jugador jugador2;
    public HashMap<Integer, HashMap<String, ArrayList<Carta>>> tableroCompartido;

    public Tablero() {
        this.id = 1;
        this.jugador1 = aniadirJugador();
        this.jugador2 = new Jugador("Maquina", new Deck());
        tableroCompartido = new HashMap<>();

        HashMap<String, ArrayList<Carta>> espacioJugador1 = new HashMap<>();
        espacioJugador1.put("CartasMonstruo", new ArrayList<>());
        espacioJugador1.put("CartasEspeciales", new ArrayList<>());

        HashMap<String, ArrayList<Carta>> espacioJugador2 = new HashMap<>();
        espacioJugador2.put("CartasMonstruo", new ArrayList<>());
        espacioJugador2.put("CartasEspeciales", new ArrayList<>());

        tableroCompartido.put(jugador1.getId(), espacioJugador1);
        tableroCompartido.put(jugador2.getId(), espacioJugador2);
    }

    public Jugador aniadirJugador() {
        return new Jugador(null, null);
    }

    public void aniadirCartaTablero(Carta carta, int idJugador) {
        if (carta instanceof CartaMonstruo) {
            if (tableroCompartido.get(idJugador).get("CartasMonstruo").size() < 3) {
                tableroCompartido.get(idJugador).get("CartasMonstruo").add(carta);
                System.out.println("+----Carta " + carta.getNombre() + " añadida");
                quitarCartaMano(idJugador, carta);
            } else {
                System.out.println("No se pudo incluir esa carta, espacio lleno");
            }
        } else if (carta instanceof CartaMagica || carta instanceof CartaTrampa) {
            if (tableroCompartido.get(idJugador).get("CartasEspeciales").size() < 3) {
                tableroCompartido.get(idJugador).get("CartasEspeciales").add(carta);
                System.out.println("+----Carta " + carta.getNombre() + " añadida");
                quitarCartaMano(idJugador, carta);
            } else {
                System.out.println("No se pudo incluir esa carta, espacio lleno");
            }
        }
    }

    public void quitarCartaTablero(Carta carta, int idJugador) {
        if (carta instanceof CartaMonstruo) {
            tableroCompartido.get(idJugador).get("CartasMonstruo").remove(carta);
        } else {
            tableroCompartido.get(idJugador).get("CartasEspeciales").remove(carta);
        }
    }

    private void quitarCartaMano(int idJugador, Carta carta) {
        if (idJugador == 1) {
            jugador1.getCartasEnMano().remove(carta);
        } else if (idJugador == 2) {
            jugador2.getCartasEnMano().remove(carta);
        }
    }

    public boolean hayCartasMonstruoBocaArriba(Jugador jugador) {
        List<CartaMonstruo> espacioMonstruosJ = tableroCompartido.get(jugador.getId()).get("CartasMonstruo");
        int i = 0;
        for (CartaMonstruo cartaMonstruo : espacioMonstruosJ) {
            if (cartaMonstruo.getIsBocaArriba()) {
                i++;
            }
        }
        return i > 0;
    }

    public boolean hayCartasMonstruoEnAtaque(Jugador jugador) {
        List<CartaMonstruo> espacioMonstruosJ = tableroCompartido.get(jugador.getId()).get("CartasMonstruo");
        int i = 0;
        for (CartaMonstruo cartaMonstruo : espacioMonstruosJ) {
            if (cartaMonstruo.getIsInAtaque()) {
                i++;
            }
        }
        return i > 0;
    }

    public CartaTrampa verificarCartaTrampa(Jugador enemigo, Carta cartaAtacante) {
        List<CartaEspecial> espacioEspecialesJ = tableroCompartido.get(enemigo.getId()).get("CartasEspeciales");
        CartaTrampa cartaEncontrada = null;
        for (CartaEspecial cartaEspecial : espacioEspecialesJ) {
            if (cartaEspecial instanceof CartaTrampa) {
                String atributoCarta = ((CartaTrampa) cartaEspecial).getTipoAtributo();
                if (atributoCarta.equals(cartaAtacante.getTipoAtributo())) {
                    cartaEncontrada = (CartaTrampa) cartaEspecial;
                }
            }
        }
        return cartaEncontrada;
    }

    public void destruirCartaMagica(int idJugador) {
        List<CartaEspecial> espacioEspecialesJ = tableroCompartido.get(idJugador).get("CartasEspeciales");
        List<CartaMonstruo> espacioMonstruosJ = tableroCompartido.get(idJugador).get("CartasMonstruo");
        List<CartaMagica> cartasMagicas = new ArrayList<>();
        List<String> tiposMonstruo = new ArrayList<>();

        for (CartaEspecial cartaEspecial : espacioEspecialesJ) {
            if (cartaEspecial instanceof CartaMagica) {
                cartasMagicas.add((CartaMagica) cartaEspecial);
            }
        }

        for (CartaMonstruo cartaMonstruo : espacioMonstruosJ) {
            tiposMonstruo.add(cartaMonstruo.getTipoMonstruo());
        }

        for (CartaMagica cartaMagica : cartasMagicas) {
            if (!tiposMonstruo.contains(cartaMagica.getTipoMonstruo())) {
                espacioEspecialesJ.remove(cartaMagica);
            }
        }
    }

    public Tuple<Integer, Integer> ataqueEntreCartas(CartaMonstruo cartaJugador, CartaMonstruo cartaEnemigo, Jugador jugador, Jugador enemigo) {
        int jugadorID = jugador.getId();
        int enemigoID = enemigo.getId();

        if (verificarCartaTrampa(enemigo, cartaJugador) == null) {
            int incAtkJugador = cartaJugador.getCartaMagica().getIncrementoAtaque();
            int incDefJugador = cartaJugador.getCartaMagica().getIncrementoDefensa();
            int incAtkEnemigo = cartaEnemigo.getCartaMagica().getIncrementoAtaque();
            int incDefEnemigo = cartaEnemigo.getCartaMagica().getIncrementoDefensa();

            if (cartaEnemigo.getIsInAtaque()) {
                if ((cartaJugador.getAtaque() + incAtkJugador) > (cartaEnemigo.getAtaque() + incAtkEnemigo)) {
                    int danioRealAEnemigo = (cartaJugador.getAtaque() + incAtkJugador) - (cartaEnemigo.getAtaque() + incAtkEnemigo);
                    int danioRealAJugador = 0;
                    System.out.println("|| Choque de ataques ||     ||" + cartaJugador.getNombre() + " vs " + cartaEnemigo.getNombre() + "||");
                    System.out.println("\t " + cartaJugador.getAtaque() + " + " + incAtkJugador + "  -->  <--  " + cartaEnemigo.getAtaque() + " + " + incAtkEnemigo);
                    System.out.println("| " + cartaJugador.getNombre() + " destruyó a " + cartaEnemigo.getNombre() + " en batalla!");
                    quitarCartaTablero(cartaEnemigo, enemigoID);
                    eliminarCartaMagicaAsociada(enemigo, cartaEnemigo);
                    return new Tuple<>(danioRealAEnemigo, danioRealAJugador);
                }
            }
        }
        return null;
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
