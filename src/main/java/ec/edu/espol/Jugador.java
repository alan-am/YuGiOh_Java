package ec.edu.espol;

import java.util.ArrayList;

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

    //Revisada
    public void tomar5Cartas() {
        for (int i = 0; i < 5; i++) {
            tomarCartaEnTurno();
        }
    }

    //Revisada
    // Método para tomar una carta en turno
    public void tomarCartaEnTurno() {
        Carta cartaTomada = this.deck.robarCarta();
        this.cartasEnMano.add(cartaTomada);
    }


    //revisada
    // Método para cambiar la posición de ataque de una carta
    public void cambiarPosicionAtaque(Partida partida) {
        Tablero tablero = partida.getTablero();
        ArrayList<Carta> espacioCartasMonstruo = tablero.getTableroCompartido().get(this.id).get("CartasMonstruo");
        System.out.println("¿Deseas cambiar la posición de una carta?");

        // "1.Si\n2.No"
        //lectura de input
        String eleccion = Utilitaria.inputString("Si\n2.No");

    
        while (!eleccion.equals("1") && !eleccion.equals("2")) {
            eleccion = Utilitaria.inputString("Elige un número entre 1 y 2");
        }

        if (eleccion.equals("1") && tablero.hayCartasMonstruoBocaArriba(this)) {
            System.out.println("Selecciona la carta monstruo a cambiar:");
            int i = 0;
            for (Carta carta : espacioCartasMonstruo) {
                System.out.println((i + 1) + ". " + carta.getNombre());
                i++;
            }

            String seleccion = Utilitaria.inputString("");
            while (!seleccion.matches("\\d+") || Integer.parseInt(seleccion) > i || Integer.parseInt(seleccion) <= 0) {
                seleccion = Utilitaria.inputString("Por favor, ingresa un número válido.");
            }

            Carta cartaSeleccionada = espacioCartasMonstruo.get(Integer.parseInt(seleccion) - 1);
            CartaMonstruo cartaSeleccion = (CartaMonstruo)cartaSeleccionada; //evita errores de polimorfimo
            cartaSeleccion.setisInAtaque(!cartaSeleccion.getisInAtaque()); 
        } else {
            System.out.println("WARNING| No existen cartas Monstruo boca Arriba para cambiar su posición");
        }
    }


   public void declararBatalla(Jugador enemigo, Partida partida) {
        Tablero tablero = partida.getTablero();
        ArrayList<Carta> espacioCartasMonstruoJ = tablero.getTableroCompartido().get(this.id).get("CartasMonstruo");

        // Validación si existen cartas monstruo
        if (espacioCartasMonstruoJ.isEmpty()) {
            System.out.println("---> No puede atacar porque no tiene cartas monstruo equipadas");
            return;
        }

        System.out.println("|    Ejecutar ataque?");
        String eleccion = Utilitaria.inputString("Si\n2.No");
        // Validamos la elección del jugador
        while (!eleccion.equals("1") && !eleccion.equals("2")) {
            eleccion = Utilitaria.inputString("Elige un número entre 1 y 2");
        }

        // Validacion existen cartas monstruo en ataque
        while (tablero.hayCartasMonstruoEnAtaque(this) && eleccion.equals("1")) {
            System.out.println("Elige tu carta de ataque: ");
            int i = 0;
            for (Carta cartaMonstruo : espacioCartasMonstruoJ) {
                
                System.out.println((i + 1) + ". " + ((CartaMonstruo)cartaMonstruo).toString3());
                i++;
            }

            String seleccion = Utilitaria.inputString("");

            // Validación de entrada
            while (!seleccion.matches("\\d+") || Integer.parseInt(seleccion) > i || Integer.parseInt(seleccion) <= 0) {
                seleccion = Utilitaria.inputString("Por favor, ingresa un número válido.");
            }

            Carta cartaSeleccionada = espacioCartasMonstruoJ.get(Integer.parseInt(seleccion) - 1);
            CartaMonstruo cartaMonstruoSelec = (CartaMonstruo)cartaSeleccionada; //casting

            // Validamos si la carta está en modo ataque y puede atacar
            if (cartaMonstruoSelec.getisInAtaque() && cartaMonstruoSelec.getPuedeAtacar()) {
                ArrayList<Carta> espacioEnemigo = tablero.getTableroCompartido().get(enemigo.getId()).get("CartasMonstruo");

                if (espacioEnemigo.isEmpty()) {
                    // Ataque directo
                    if (tablero.verificarCartaTrampa(enemigo, cartaMonstruoSelec) != null) {
                        System.out.println("| Se ha atacado directamente! pero una carta Trampa se interpuso");
                        CartaTrampa cartaTrampa = tablero.verificarCartaTrampa(enemigo, cartaMonstruoSelec);
                        
                        System.out.println(cartaTrampa.getNombre() + " detiene el ataque de un monstruo con tipo de atributo " + cartaTrampa.getTipoAtributo());
                        System.out.println("| Carta Trampa eliminada del tablero");
                        tablero.quitarCartaTablero(cartaTrampa, enemigo.getId());
                        cartaMonstruoSelec.setPuedeAtacar(false);
                    } else {
                        // Ataque sin trampa
    
                        //obtenemos los INC de ataque de la carta monstruo
                        Integer incAtkJugador;
                        Tupla tIncrementos =  cartaMonstruoSelec.obtenerIncrementosATKyDEF();
                        incAtkJugador = (Integer)tIncrementos.obj1;

                        int danio = cartaMonstruoSelec.getAtaque() + incAtkJugador;
                        System.out.println("| Se ha atacado directamente con " + cartaSeleccionada.getNombre());
                        System.out.println(" \t " + cartaMonstruoSelec.getAtaque() + "  +  " + incAtkJugador + " -->  " + enemigo.getPuntosVida() + " Puntos Vida " + enemigo.getNombre());
                        enemigo.setPuntosVida(enemigo.getPuntosVida() - danio);
                        cartaMonstruoSelec.setPuedeAtacar(false);
                    }
                } else {
                    
                    // Elegir carta enemiga
                    System.out.println("Elige la carta Enemiga a atacar: ");
                    i = 0;
                    for (Carta cartaMonstruo : espacioEnemigo) {
                        CartaMonstruo cartaMonstruo_c = (CartaMonstruo)cartaMonstruo;
                        if (cartaMonstruo_c.getIsBocaArriba()) {
                            System.out.println((i + 1) + ". " + cartaMonstruo_c.toString2());
                        } else {
                            System.out.println((i + 1) + ". CARTA MONSTRUO|| *** Carta boca abajo ***");
                        }
                        i++;
                    }

                    seleccion = Utilitaria.inputString("");

                    // Validación de entrada
                    while (!seleccion.matches("\\d+") || Integer.parseInt(seleccion) > i || Integer.parseInt(seleccion) <= 0) {
                        seleccion = Utilitaria.inputString("Por favor, ingresa un número válido.");
                    }

                    Carta cartaEnemigaSeleccionada = espacioEnemigo.get(Integer.parseInt(seleccion) - 1);
                    CartaMonstruo cartaEnemigaSeleccion = (CartaMonstruo)cartaEnemigaSeleccionada;

                    // Ejecutar el ataque
                    Integer danioAEnemigo = 0;
                    Integer danioAJugador = 0;
                    //se devuelve una tupla
                    Tupla tupla = tablero.ataqueEntreCartas(cartaMonstruoSelec, cartaEnemigaSeleccion, this, enemigo);
                    //descomprimimos la tupla
                    danioAEnemigo =(Integer)tupla.getObj1();
                    danioAJugador = (Integer)tupla.getObj1();

                    // Actualizar vida de los jugadores
                    this.puntosVida -= danioAJugador;
                    enemigo.puntosVida -= danioAEnemigo;
                    cartaMonstruoSelec.setPuedeAtacar(false);

                }
            } else {
                if (cartaMonstruoSelec.getIsInAtaque()) {
                    System.out.println("WARNING| La carta seleccionada ya ha atacado en este Turno.");
                } else if (cartaMonstruoSelec.getPuedeAtacar()) {
                    System.out.println("WARNING| La carta seleccionada no está en modo de ataque");
                }
            }

            // Pregunta si quiere ejecutar el ataque nuevamente
            System.out.println("|    Ejecutar otro ataque?");
            eleccion = Utilitaria.inputString("Si\n2.No");
            while (!eleccion.equals("1") && !eleccion.equals("2")) {
                eleccion = Utilitaria.inputString("Elige un número entre 1 y 2");
            }
        }
    }


    public void jugarCarta(Partida partida) {
        Tablero tablero = partida.getTablero();

        // Mostrar las cartas en mano
        System.out.println("Cartas en mano");
        for (int i = 0; i < cartasEnMano.size(); i++) {
            
            System.out.println((i + 1) + ". " + cartasEnMano.get(i).toString2());
        }

        String seleccion = Utilitaria.inputString("Selecciona la carta a añadir: ");

        // Validación de la entrada
        while (!seleccion.matches("\\d+") || Integer.parseInt(seleccion) > cartasEnMano.size() || Integer.parseInt(seleccion) <= 0) {
            seleccion = Utilitaria.inputString("Por favor, ingresa un número válido.");
        }

        Carta cartaSeleccionada = cartasEnMano.get(Integer.parseInt(seleccion) - 1);
        //CartaMonstruo cartaSeleccion = (CartaMonstruo)cartaSeleccionada; no siempre el usuario eligira una carta Monstruo

        // Si la carta es un monstruo
        if (cartaSeleccionada instanceof CartaMonstruo) {
            if (tablero.getTableroCompartido().get(this.id).get("CartasMonstruo").size() < 3 && noAgregoMonstruo) {
                //convertimos la seleccion en una carta monstruo
                CartaMonstruo cartaMonstruoSelec = (CartaMonstruo)cartaSeleccionada;
                System.out.println("Elige el modo de la carta: ");
                String eleccion = Utilitaria.inputString("1. Modo Ataque \n 2. Modo Defensa");
                while (!eleccion.equals("1") && !eleccion.equals("2")) {
                    eleccion = Utilitaria.inputString("Elige un número entre 1 y 2.");
                }

                if (eleccion.equals("1")) {
                    cartaMonstruoSelec.setisInAtaque(true);
                    cartaMonstruoSelec.setBocaArriba(true);
                } else {
                    cartaMonstruoSelec.setisInAtaque(false);
                    cartaMonstruoSelec.setBocaArriba(false);
                }

                tablero.aniadirCartaTablero(cartaSeleccionada, this.id);
                setNoAgregoMonstruo(false);
            } else {
                if (tablero.getTableroCompartido().get(this.id).get("CartasMonstruo").size() < 3) {
                    System.out.println("WARNING| Ya no puedes colocar más cartas monstruos en este turno.");
                } else {
                    System.out.println("WARNING| Has alcanzado el límite de cartas monstruos en el tablero.");
                }
            }
        }

        // Si la carta es trampa
        else if (cartaSeleccionada instanceof CartaTrampa) {
            if (tablero.getTableroCompartido().get(this.id).get("CartasEspeciales").size() < 3) {
                tablero.aniadirCartaTablero(cartaSeleccionada, this.id);
            } else {
                System.out.println("WARNING| No se puede agregar más cartas mágicas o trampas.");
            }
        }
        
        // Si la carta es mágica
        else if (cartaSeleccionada instanceof CartaMagica) {
            if (tablero.validarAgregacionCartaMagica((CartaMagica)cartaSeleccionada, this)) {
                ArrayList<Carta> espacioCartasMonstruo = tablero.getTableroCompartido().get(this.id).get("CartasMonstruo");
                System.out.println("| Selecciona el monstruo al cual asociar la carta mágica");

                for (int i = 0; i < espacioCartasMonstruo.size(); i++) {
                    System.out.println((i + 1) + ". " + espacioCartasMonstruo.get(i).toString3());
                }

                String seleccionMonstruo = Utilitaria.inputString("");
                while (!seleccionMonstruo.matches("\\d+") || Integer.parseInt(seleccionMonstruo) > espacioCartasMonstruo.size() || Integer.parseInt(seleccionMonstruo) <= 0) {
                    seleccionMonstruo = Utilitaria.inputString("Por favor, ingresa un número válido.");
                }

                CartaMonstruo cartaAAsociar = (CartaMonstruo)(espacioCartasMonstruo.get(Integer.parseInt(seleccionMonstruo) - 1));

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
    
    public ArrayList<String> listarCartasEnMano() {
        // Retorna una lista solo con los nombres de las cartas en la mano
        ArrayList<String> nombresCartas = new ArrayList<>();
        for (Carta carta : this.cartasEnMano) {
            nombresCartas.add(carta.getNombre());
        }
        return nombresCartas;
    }
        
    public void declararBatallaComoMaquina(Tablero tablero, Jugador oponente) {
        System.out.println("La " + this.getNombre() + " te va a declarar batalla!! " + oponente.getNombre() + " estás preparadx? ");
        Utilitaria.simularEnter("> presione enter: ");
    
        // Obtener monstruos en ataque que pueden atacar
        ArrayList<CartaMonstruo> monstruosAtacantes = new ArrayList<>();
        ArrayList<CartaMagica> cartasMagicas = new ArrayList<>();
        ArrayList<CartaTrampa> cartasTrampa = new ArrayList<>();
    
        for (Carta carta : tablero.getTableroCompartido().get(this.getId()).get("CartasMonstruo")) {
            CartaMonstruo carta_c = (CartaMonstruo)carta;
            if (carta_c.getisInAtaque() && carta_c.getPuedeAtacar()) {
                monstruosAtacantes.add(carta_c);
            }
        }
    
        // Si no hay monstruos en ataque, cambiar a modo defensa
        if (monstruosAtacantes.isEmpty()) {
            System.out.println("La " + this.getNombre() + " no tiene monstruos disponibles para atacar. Se coloca a la defensiva.");
            for (Carta carta : tablero.getTableroCompartido().get(this.getId()).get("CartasMonstruo")) {
                CartaMonstruo carta_c = (CartaMonstruo)carta;
                if (carta_c.getisInAtaque()) {
                    carta_c.setisInAtaque(noAgregoMonstruo);
                    carta_c.setBocaArriba(false);
                    System.out.println("---> " + this.getNombre() + " pone al monstruo " + carta.getNombre() + " en defensa.");
                }
            }
            return;
        }
    
        // Obtener monstruos del oponente
        ArrayList<Carta> monstruosOponente = tablero.getTableroCompartido().get(oponente.getId()).get("CartasMonstruo");
    
        // Obtener cartas mágicas y trampa
        for (Carta carta : tablero.getTableroCompartido().get(this.getId()).get("CartasEspeciales")) {
            if (carta instanceof CartaTrampa) {
                cartasTrampa.add((CartaTrampa) carta);
            } else if (carta instanceof CartaMagica) {
                cartasMagicas.add((CartaMagica) carta);
            }
        }
    
        // Función de carta mágica
        for (CartaMagica cartaMagica : cartasMagicas) {
            if (tablero.validarAgregacionCartaMagica(cartaMagica, this)) {
                for (Carta monstruo : tablero.getTableroCompartido().get(this.getId()).get("CartasMonstruo")) {
                    CartaMonstruo monstruo_c = (CartaMonstruo)monstruo; //casting
                    if (monstruo_c.getTipoMonstruo().equals(cartaMagica.getTipoMonstruo())) {
                        if (cartaMagica.getIncrementoAtaque() > 0) {
                            monstruo_c.setAtaque(monstruo_c.getAtaque() + cartaMagica.getIncrementoAtaque());
                            System.out.println("---> " + this.getNombre() + " equipa '" + cartaMagica.getNombre() + "' a '" + monstruo_c.getNombre() + "', aumentando su ataque en " + cartaMagica.getIncrementoAtaque() + ".");
                        } else if (cartaMagica.getIncrementoDefensa() > 0) {
                            monstruo_c.setDefensa(monstruo_c.getDefensa() + cartaMagica.getIncrementoDefensa());
                            System.out.println("---> " + this.getNombre() + " equipa '" + cartaMagica.getNombre() + "' a '" + monstruo_c.getNombre() + "', aumentando su defensa en " + cartaMagica.getIncrementoDefensa() + ".");
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
                for (Carta cartaMounstroRival : monstruosOponente) {
                    CartaMonstruo cartaMonstruoRival = (CartaMonstruo)cartaMounstroRival;
                    //if
                    //obtenemos el incremento de ataque de la carta rival y de la propia
        
                    Integer incAtaqueCartaEleg =  (Integer)(cartaAtacante.obtenerIncrementosATKyDEF().obj1);
                    Integer incAtaqueCartaRival = (Integer)(cartaMonstruoRival.obtenerIncrementosATKyDEF().obj1);
                    if ((cartaMonstruoRival.getAtaque() + incAtaqueCartaRival) < (cartaAtacante.getAtaque() + incAtaqueCartaEleg)) {
    
                        System.out.println("---+ " + cartaAtacante.getNombre() + " ataca a " + cartaMounstroRival.getNombre() + "!");
                        Tupla tupla = tablero.ataqueEntreCartas(cartaAtacante, cartaMonstruoRival, this, oponente);
                        Integer danioAEnemigo = (Integer)(tupla.obj1);
                        Integer danioAJugador = (Integer)(tupla.obj2);
                        this.puntosVida -= danioAJugador;
                        oponente.puntosVida -= danioAEnemigo;
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
        Utilitaria.simularEnter("Loading....");
        // mano_maquina sería la lista de cartas en mano del jugador
        ArrayList<Carta> manoMaquina = new ArrayList<>(getCartasEnMano()); //debe ser una copiar para que no se modifique
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
        System.out.println("Le quite el enter porque da muchos problemas");
        System.out.println("");
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
                getNombre(), getId(), getPuntosVida(), " Baraja ", getDeck().toString());
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

