package ec.edu.espol;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import ec.edu.espol.CartaMonstruo;
import ec.edu.espol.Carta;
import ec.edu.espol.Tablero;
import ec.edu.espol.Partida;
import ec.edu.espol.Deck;
import ec.edu.espol.CartaMagica;

public class Jugador {
    public static int jugadores; //contador de instancias

    //atributos
    private int id;
    private int puntosVida;
    private String nombre;
    private Deck deck;
    private ArrayList<Carta> cartasEnMano;
    private boolean noAgregoMonstruo;  //indicador si en el turno agrego monstruo


    //constructor
    public Jugador(String nombre, Deck deck) 
    {
        jugadores += 1;
        this.id = jugadores;
        this.puntosVida = 4000;
        this.nombre = nombre;
        this.deck = deck;
        this.cartasEnMano = new ArrayList<>();
        this.noAgregoMonstruo = true;
    }
    //METODOS

    public void tomar5Cartas() {
        for (int i = 0; i < 5; i++) {
            tomarCartaEnTurno();
        }
    }

    // Método para tomar una carta en turno
    public void tomarCartaEnTurno() {
        Carta cartaTomada = this.deck.robarCarta();
        this.cartasEnMano.add(cartaTomada);
    }

    // Método para cambiar la posición de ataque de una carta
    public void cambiarPosicionAtaque(Partida partida) {
        Tablero tablero = partida.getTablero();
        List<Carta> espacioCartasMonstruo = tablero.getTableroCompartido().get(this.id).getCartasMonstruo();

        System.out.println("¿Deseas cambiar la posición de una carta?");
        System.out.println("1. Sí");
        System.out.println("2. No");
        Scanner sc = new Scanner(System.in);
        String eleccion = sc.nextLine();

        while (!eleccion.equals("1") && !eleccion.equals("2")) {
            System.out.println("Elige un número entre 1 y 2");
            eleccion = sc.nextLine();
        }

        if (eleccion.equals("1") && tablero.hayCartasMonstruoBocaArriba(this.id)) {
            System.out.println("Selecciona la carta monstruo a cambiar:");
            int i = 0;
            for (Carta carta : espacioCartasMonstruo) {
                System.out.println((i + 1) + ". " + carta.getNombre());
                i++;
            }

            String seleccion = sc.nextLine();
            while (!seleccion.matches("\\d+") || Integer.parseInt(seleccion) > i || Integer.parseInt(seleccion) <= 0) {
                System.out.println("Por favor, ingresa un número válido.");
                seleccion = sc.nextLine();
            }

            Carta cartaSeleccionada = espacioCartasMonstruo.get(Integer.parseInt(seleccion) - 1);
            cartaSeleccionada.setIsInAtaque(!cartaSeleccionada.getIsInAtaque());
        } else {
            System.out.println("WARNING| No existen cartas Monstruo boca Arriba para cambiar su posición");
        }
    }


   public void declararBatalla(Jugador enemigo, Partida partida) {
        Tablero tablero = partida.getTablero();
        List<Carta> espacioCartasMonstruoJ = tablero.getTableroCompartido().get(this.id).getCartasMonstruo();

        // Validación si existen cartas monstruo
        if (espacioCartasMonstruoJ.isEmpty()) {
            System.out.println("---> No puede atacar porque no tiene cartas monstruo equipadas");
            return;
        }

        System.out.println("|    Ejecutar ataque?");
        Scanner sc = new Scanner(System.in);
        String eleccion = sc.nextLine();

        // Validamos la elección del jugador
        while (!eleccion.equals("1") && !eleccion.equals("2")) {
            System.out.println("Elige un número entre 1 y 2");
            eleccion = sc.nextLine();
        }

        // Validación si existen cartas monstruo en ataque
        while (tablero.hayCartasMonstruoEnAtaque(this) && eleccion.equals("1")) {
            System.out.println("Elige tu carta de ataque: ");
            int i = 0;
            for (Carta cartaMonstruo : espacioCartasMonstruoJ) {
                System.out.println((i + 1) + ". " + cartaMonstruo.toString3());
                i++;
            }

            String seleccion = sc.nextLine();

            // Validación de entrada
            while (!seleccion.matches("\\d+") || Integer.parseInt(seleccion) > i || Integer.parseInt(seleccion) <= 0) {
                System.out.println("Por favor, ingresa un número válido.");
                seleccion = sc.nextLine();
            }

            Carta cartaSeleccionada = espacioCartasMonstruoJ.get(Integer.parseInt(seleccion) - 1);

            // Validamos si la carta está en modo ataque y puede atacar
            if (cartaSeleccionada.getIsInAtaque() && cartaSeleccionada.getPuedeAtacar()) {
                List<Carta> espacioEnemigo = tablero.getTableroCompartido().get(enemigo.getId()).getCartasMonstruo();

                if (espacioEnemigo.isEmpty()) {
                    // Ataque directo
                    if (tablero.verificarCartaTrampa(enemigo, cartaSeleccionada) != null) {
                        System.out.println("| Se ha atacado directamente! pero una carta Trampa se interpuso");
                        Carta cartaTrampa = tablero.verificarCartaTrampa(enemigo, cartaSeleccionada);
                        System.out.println(cartaTrampa.getNombre() + " detiene el ataque de un monstruo con tipo de atributo " + cartaTrampa.getTipoAtributo());
                        System.out.println("| Carta Trampa eliminada del tablero");
                        tablero.quitarCartaTablero(cartaTrampa, enemigo.getId());
                        cartaSeleccionada.setPuedeAtacar(false);
                    } else {
                        // Ataque sin trampa
                        int incAtkJugador = cartaSeleccionada.getCartaMagica().getIncrementoAtaque();
                        int danio = cartaSeleccionada.getAtaque() + incAtkJugador;
                        System.out.println("| Se ha atacado directamente con " + cartaSeleccionada.getNombre());
                        System.out.println(" \t " + cartaSeleccionada.getAtaque() + "  +  " + incAtkJugador + " -->  " + enemigo.getPuntosVida() + " Puntos Vida " + enemigo.getNombre());
                        enemigo.setPuntosVida(enemigo.getPuntosVida() - danio);
                        cartaSeleccionada.setPuedeAtacar(false);
                    }
                } else {
                    // Elegir carta enemiga
                    System.out.println("Elige la carta Enemiga a atacar: ");
                    i = 0;
                    for (Carta cartaMonstruo : espacioEnemigo) {
                        if (cartaMonstruo.getIsBocaArriba()) {
                            System.out.println((i + 1) + ". " + cartaMonstruo.toString2());
                        } else {
                            System.out.println((i + 1) + ". CARTA MONSTRUO|| *** Carta boca abajo ***");
                        }
                        i++;
                    }

                    seleccion = sc.nextLine();

                    // Validación de entrada
                    while (!seleccion.matches("\\d+") || Integer.parseInt(seleccion) > i || Integer.parseInt(seleccion) <= 0) {
                        System.out.println("Por favor, ingresa un número válido.");
                        seleccion = sc.nextLine();
                    }

                    Carta cartaEnemigaSeleccionada = espacioEnemigo.get(Integer.parseInt(seleccion) - 1);

                    // Ejecutar el ataque
                    int danioAEnemigo = 0, danioAJugador = 0;
                    danioAEnemigo = tablero.ataqueEntreCartas(cartaSeleccionada, cartaEnemigaSeleccionada, this, enemigo);

                    // Actualizar vida de los jugadores
                    this.puntosVida -= danioAJugador;
                    enemigo.setPuntosVida(enemigo.getPuntosVida() - danioAEnemigo);
                    cartaSeleccionada.setPuedeAtacar(false);
                }
            } else {
                if (cartaSeleccionada.getIsInAtaque()) {
                    System.out.println("WARNING| La carta seleccionada ya ha atacado en este Turno.");
                } else if (cartaSeleccionada.getPuedeAtacar()) {
                    System.out.println("WARNING| La carta seleccionada no está en modo de ataque");
                }
            }

            // Pregunta si quiere ejecutar el ataque nuevamente
            System.out.println("|    Ejecutar ataque?");
            eleccion = sc.nextLine();
            while (!eleccion.equals("1") && !eleccion.equals("2")) {
                System.out.println("Elige un número entre 1 y 2");
                eleccion = sc.nextLine();
            }
        }
    }


    public void jugarCarta(Partida partida) {
        Tablero tablero = partida.getTablero();

        Scanner scanner = new Scanner(System.in);

        // Mostrar las cartas en mano
        System.out.println("Cartas en mano".center(40, "-"));
        for (int i = 0; i < cartasEnMano.size(); i++) {
            System.out.println((i + 1) + ". " + cartasEnMano.get(i).toString2());
        }

        System.out.println("Selecciona la carta a añadir: ");
        String seleccion = scanner.nextLine();

        // Validación de la entrada
        while (!seleccion.matches("\\d+") || Integer.parseInt(seleccion) > cartasEnMano.size() || Integer.parseInt(seleccion) <= 0) {
            System.out.println("Por favor, ingresa un número válido.");
            seleccion = scanner.nextLine();
        }

        Carta cartaSeleccionada = cartasEnMano.get(Integer.parseInt(seleccion) - 1);

        // Si la carta es un monstruo
        if (cartaSeleccionada instanceof CartaMonstruo) {
            if (tablero.getTableroCompartido().get(this.id).getCartasMonstruo().size() < 3 && noAgregoMonstruo) {
                System.out.println("Elige el modo de la carta: ");
                System.out.println("1. Modo Ataque");
                System.out.println("2. Modo Defensa");

                String eleccion = scanner.nextLine();
                while (!eleccion.equals("1") && !eleccion.equals("2")) {
                    System.out.println("Elige un número entre 1 y 2.");
                    eleccion = scanner.nextLine();
                }

                if (eleccion.equals("1")) {
                    ((CartaMonstruo) cartaSeleccionada).setIsInAtaque(true);
                    ((CartaMonstruo) cartaSeleccionada).setIsBocaArriba(true);
                } else {
                    ((CartaMonstruo) cartaSeleccionada).setIsInAtaque(false);
                    ((CartaMonstruo) cartaSeleccionada).setIsBocaArriba(false);
                }

                tablero.aniadirCartaTablero(cartaSeleccionada, this.id);
                setNoAgregoMonstruo(false);
            } else {
                if (tablero.getTableroCompartido().get(this.id).getCartasMonstruo().size() < 3) {
                    System.out.println("WARNING| Ya no puedes colocar más cartas monstruos en este turno.");
                } else {
                    System.out.println("WARNING| Has alcanzado el límite de cartas monstruos en el tablero.");
                }
            }
        }

        // Si la carta es trampa
        else if (cartaSeleccionada instanceof CartaTrampa) {
            if (tablero.getTableroCompartido().get(this.id).getCartasEspeciales().size() < 3) {
                tablero.aniadirCartaTablero(cartaSeleccionada, this.id);
            } else {
                System.out.println("WARNING| No se puede agregar más cartas mágicas o trampas.");
            }
        }

        // Si la carta es mágica
        else if (cartaSeleccionada instanceof CartaMagica) {
            if (tablero.validarAgregacionCartaMagica((CartaMagica) cartaSeleccionada, this)) {
                List<CartaMonstruo> espacioCartasMonstruo = tablero.getTableroCompartido().get(this.id).getCartasMonstruo();
                System.out.println("| Selecciona el monstruo al cual asociar la carta mágica");

                for (int i = 0; i < espacioCartasMonstruo.size(); i++) {
                    System.out.println((i + 1) + ". " + espacioCartasMonstruo.get(i).toString3());
                }

                String seleccionMonstruo = scanner.nextLine();
                while (!seleccionMonstruo.matches("\\d+") || Integer.parseInt(seleccionMonstruo) > espacioCartasMonstruo.size() || Integer.parseInt(seleccionMonstruo) <= 0) {
                    System.out.println("Por favor, ingresa un número válido.");
                    seleccionMonstruo = scanner.nextLine();
                }

                CartaMonstruo cartaAAsociar = espacioCartasMonstruo.get(Integer.parseInt(seleccionMonstruo) - 1);

                if (cartaAAsociar.getTipoMonstruo().equals(((CartaMagica) cartaSeleccionada).getTipoMonstruo())) {
                    cartaAAsociar.setCartaMagica((CartaMagica) cartaSeleccionada);
                    tablero.aniadirCartaTablero(cartaSeleccionada, this.id);
                } else {
                    System.out.println("WARNING| La carta mágica elegida no se puede asociar a la carta monstruo \"" + cartaAAsociar.getNombre() + "\".");
                }
            } else {
                System.out.println("WARNING| No existe carta monstruo en el tablero con la cual asociarla.");
            }
        }
    }

    public void imprimirMano() {
        // Imprime por consola las cartas en la mano del jugador
        for (Carta carta : this.cartasEnMano) {
            System.out.println(carta.toString2());
        }
    }
    
    public List<String> listarCartasEnMano() {
        // Retorna una lista solo con los nombres de las cartas en la mano
        List<String> nombresCartas = new ArrayList<>();
        for (Carta carta : this.cartasEnMano) {
            nombresCartas.add(carta.getNombre());
        }
        return nombresCartas;
    }
        
    public void declararBatallaComoMaquina(Tablero tablero, Jugador oponente) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("La " + this.getNombre() + " te va a declarar batalla!! " + oponente.getNombre() + " estás preparadx?: ");
        scanner.nextLine();
    
        // Obtener monstruos en ataque que pueden atacar
        List<CartaMonstruo> monstruosAtacantes = new ArrayList<>();
        List<CartaMagica> cartasMagicas = new ArrayList<>();
        List<CartaTrampa> cartasTrampa = new ArrayList<>();
    
        for (CartaMonstruo carta : tablero.getTableroCompartido().get(this.getId()).getCartasMonstruo()) {
            if (carta.isInAtaque() && carta.puedeAtacar()) {
                monstruosAtacantes.add(carta);
            }
        }
    
        // Si no hay monstruos en ataque, cambiar a modo defensa
        if (monstruosAtacantes.isEmpty()) {
            System.out.println("La " + this.getNombre() + " no tiene monstruos disponibles para atacar. Se coloca a la defensiva.");
            for (CartaMonstruo carta : tablero.getTableroCompartido().get(this.getId()).getCartasMonstruo()) {
                if (carta.isInAtaque()) {
                    carta.setInAtaque(false);
                    carta.setBocaArriba(false);
                    System.out.println("---> " + this.getNombre() + " pone al monstruo " + carta.getNombre() + " en defensa.");
                }
            }
            return;
        }
    
        // Obtener monstruos del oponente
        List<CartaMonstruo> monstruosOponente = tablero.getTableroCompartido().get(oponente.getId()).getCartasMonstruo();
    
        // Obtener cartas mágicas y trampa
        for (Carta carta : tablero.getTableroCompartido().get(this.getId()).getCartasEspeciales()) {
            if (carta instanceof CartaTrampa) {
                cartasTrampa.add((CartaTrampa) carta);
            } else if (carta instanceof CartaMagica) {
                cartasMagicas.add((CartaMagica) carta);
            }
        }
    
        // Función de carta mágica
        for (CartaMagica cartaMagica : cartasMagicas) {
            if (tablero.validarAgregacionCartaMagica(cartaMagica, this)) {
                for (CartaMonstruo monstruo : tablero.getTableroCompartido().get(this.getId()).getCartasMonstruo()) {
                    if (monstruo.getTipoMonstruo().equals(cartaMagica.getTipoMonstruo())) {
                        if (cartaMagica.getIncrementoAtaque() > 0) {
                            monstruo.setAtaque(monstruo.getAtaque() + cartaMagica.getIncrementoAtaque());
                            System.out.println("---> " + this.getNombre() + " equipa '" + cartaMagica.getNombre() + "' a '" + monstruo.getNombre() + "', aumentando su ataque en " + cartaMagica.getIncrementoAtaque() + ".");
                        } else if (cartaMagica.getIncrementoDefensa() > 0) {
                            monstruo.setDefensa(monstruo.getDefensa() + cartaMagica.getIncrementoDefensa());
                            System.out.println("---> " + this.getNombre() + " equipa '" + cartaMagica.getNombre() + "' a '" + monstruo.getNombre() + "', aumentando su defensa en " + cartaMagica.getIncrementoDefensa() + ".");
                        }
                        tablero.quitarCartaTablero(cartaMagica, this.getId());
                        break;
                    }
                }
            }
        }
    
        // Función de carta monstruo y carta trampa
        for (CartaMonstruo cartaAtacante : monstruosAtacantes) {
            CartaTrampa cartaTrampa = tablero.verificarCartaTrampa(oponente, cartaAtacante);
            if (cartaTrampa != null) {
                System.out.println("---> " + oponente.getNombre() + " activa la carta trampa '" + cartaTrampa.getNombre() + "' y detiene el ataque de '" + cartaAtacante.getNombre() + "'.");
                tablero.quitarCartaTablero(cartaTrampa, oponente.getId());
                continue;
            }
    
            if (monstruosOponente.isEmpty()) {
                System.out.println(cartaAtacante.getNombre() + " realiza un ataque directo");
                oponente.setPuntosVida(oponente.getPuntosVida() - cartaAtacante.getAtaque());
                cartaAtacante.setPuedeAtacar(false);
    
                System.out.println(this.getNombre() + " ha terminado su fase de batalla.");
                System.out.println("=========================================================");
            } else {
                for (CartaMonstruo cartaMounstroRival : monstruosOponente) {
                    if ((cartaMounstroRival.getAtaque() + cartaMounstroRival.getCartaMagica().getIncrementoAtaque()) < 
                        (cartaAtacante.getAtaque() + cartaAtacante.getCartaMagica().getIncrementoAtaque())) {
    
                        System.out.println("---+ " + cartaAtacante.getNombre() + " ataca a " + cartaMounstroRival.getNombre() + "!");
                        int danioAEnemigo = tablero.ataqueEntreCartas(cartaAtacante, cartaMounstroRival, this, oponente).getDanioAEnemigo();
                        int danioAJugador = tablero.ataqueEntreCartas(cartaAtacante, cartaMounstroRival, this, oponente).getDanioAJugador();
                        this.setPuntosVida(this.getPuntosVida() - danioAJugador);
                        oponente.setPuntosVida(oponente.getPuntosVida() - danioAEnemigo);
                        cartaAtacante.setPuedeAtacar(false);
    
                    } else {
                        System.out.println("La Maquina ha decidido no usar a " + cartaAtacante.getNombre() + " para atacar a tu carta " + cartaMounstroRival.getNombre() + " porque no le conviene.");
                    }
                }
            }
        }
    }
    
    public void llenarTableroMaquina(Tablero tablero) {
        System.out.println("La " + getNombre() + " está organizando su tablero.");
        Scanner scanner = new Scanner(System.in);
        System.out.println("Loading....");
        // mano_maquina sería la lista de cartas en mano del jugador
        List<Carta> manoMaquina = getCartasEnMano();
        System.out.println("");

        // Colocar cartas de monstruo
        for (Carta carta : manoMaquina) {
            if (carta instanceof CartaMonstruo) {
                if (this.noAgregoMonstruo) { // si no se ha agregado monstruo en ese turno
                    if (tablero.getTableroCompartido().get(2).get("CartasMonstruo").size() < 3) {
                        tablero.aniadirCartaTablero(carta, 2); // agrega la carta al tablero
                        System.out.println(getNombre() + " coloca al monstruo " + carta.getNombre() + " en el tablero.");
                        setNoAgregoMonstruo(false); // ya tiene un monstruo agregado
                    }
                }
            } else { // si no es monstruo, será cualquier otra carta
                if (tablero.getTableroCompartido().get(2).get("CartasEspeciales").size() < 3) {
                    tablero.aniadirCartaTablero(carta, 2);
                    System.out.println(getNombre() + " coloca una carta especial: " + carta.getNombre() + ".");
                }
            }
        }

        System.out.println(getNombre() + " ha terminado de organizar su tablero.");
        System.out.println("Enter para seguir");
        scanner.nextLine(); // pausa para seguir
    }

    public boolean esDerrotado() {
        boolean sinCartasEnMano = getCartasEnMano().isEmpty();
        boolean sinCartasEnDeck = getDeck().getBaraja().isEmpty();
        boolean sinVida = getPuntosVida() <= 0;

        return sinVida || (sinCartasEnMano && sinCartasEnDeck);
    }

    // Método toString
    @Override
    public String toString() {
        return String.format("Datos Jugador - %s -\nId: %d\nPuntos de vida: %d\n%s\n%s\n-----------------------------------",
                getNombre(), getId(), getPuntosVida(), " Baraja ".center(25, "-"), getDeck().toString());
    }

    
    //getters and setters
    public int getId() {
        return id;
    }


    public void setId(int id) {
        this.id = id;
    }


    public int getPuntosVida() {
        return puntosVida;
    }


    public void setPuntosVida(int puntosVida) {
        this.puntosVida = puntosVida;
    }


    public String getNombre() {
        return nombre;
    }


    public void setNombre(String nombre) {
        this.nombre = nombre;
    }


    public Deck getDeck() {
        return deck;
    }


    public void setDeck(Deck deck) {
        this.deck = deck;
    }


    public ArrayList<Carta> getCartasEnMano() {
        return cartasEnMano;
    }


    public void setCartasEnMano(ArrayList<Carta> cartasEnMano) {
        this.cartasEnMano = cartasEnMano;
    }


    public boolean isNoAgregoMonstruo() {
        return noAgregoMonstruo;
    }


    public void setNoAgregoMonstruo(boolean noAgregoMonstruo) {
        this.noAgregoMonstruo = noAgregoMonstruo;
    }

    

}
