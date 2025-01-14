package ec.edu.espol;
import java.util.ArrayList;
import java.util.Random;
import ec.edu.espol.Partida;

public class Partida {

    private int turno;
    private Tablero tablero;
    private int jugadorActual;
    private int ronda;

    public Partida(){
        this.turno = 1;
        this.ronda = 1; // 1 ronda es 2 turnos
        this.jugadorActual = 1; //por defecto
        this.tablero = new Tablero();
    }

    //metodos
    public void ronda()
    {
       Jugador j1 = this.getTablero().getJugador1();
       Jugador maquina = this.getTablero().getJugador2();
       //int jugadorActual = this.getJugadorActual(); no es necesario

       if(this.getRonda()==1)
       {
        for(int i=0;i<2;i++)
        {
            faseTomarCarta(j1,maquina);
            fasePrincipal(j1,maquina);
            System.out.println(this.getTablero().toString());
            cambiarTurno();
        System.out.println("--- Es su primer turno, no puede declarar batalla");
        }
       } else {
            for(int i=0;i<2;i++)
            {
                if(!j1.esDerrotado() && !maquina.esDerrotado())
                {
                    faseTomarCarta(j1,maquina);
                    System.out.println("-".repeat(30));
                    fasePrincipal(j1,maquina);
                    System.out.println("-".repeat(30));
                    faseBatalla(j1,maquina);
                    Utilitaria.simularEnter("> Da Enter para mostrar el tablero actualizado");
                    System.out.println(this.getTablero().toString()); 
                    resetearEstadoCartasMonstruo();
                    cambiarTurno();
                }
            }
       }
       if(!j1.esDerrotado() && !maquina.esDerrotado()) 
       {
        this.ronda++;
        Utilitaria.simularEnter("Presione Enter para comenzar una nueva ronda");
        System.out.println("Loading...");
        System.out.println("------> Ronda " + this.ronda + " comienza:");
        System.out.println("==".repeat(40));
        }
    }
    
    public void faseTomarCarta(Jugador j1, Jugador j2) {
        if (this.turno <= 3 && (j1.getCartasEnMano().isEmpty() || j2.getCartasEnMano().isEmpty())) {
            if (j1.getId() == this.jugadorActual) {
                j1.tomar5Cartas();
                System.out.println(j1.getNombre() + " ha tomado 5 cartas del deck");
            } else {
                tablero.getJugador2().tomar5Cartas();
                System.out.println(j2.getNombre() + " ha tomado 5 cartas del deck");
            }
        } else {
            if (jugadorActual == 1) {
                System.out.println("-Es tu turno de robar una carta-");
                Utilitaria.simularEnter("Presiona enter para tomar la carta del Deck...");
                j1.tomarCartaEnTurno();
            } else {
                System.out.println("-".repeat(30) + " Turno de la máquina " + "-".repeat(30));
                System.out.println("La máquina ya ha robado su carta");
                j2.tomarCartaEnTurno();
            }
        }
    }


      //@SuppressWarnings("resource")
    public void fasePrincipal(Jugador j1, Jugador maquina) {
        String eleccion;
        if (jugadorActual == 1) {
            j1.setNoAgregoMonstruo(true);
            Utilitaria.simularEnter("|--> Presiona enter para visualizar tus cartas en mano ");
            System.out.println("enter quitado para evitar problemas--------------------------------------");
            j1.imprimirMano();
            System.out.println("Desea añadir una carta a su tablero?");
            eleccion = Utilitaria.inputString("1.Si\n2.No");
            //validando la entrada
            while (!eleccion.equals("1") && !eleccion.equals("2")) {
                eleccion = Utilitaria.inputString("Elige un número entre 1 y 2");
            }
            System.out.println(eleccion);
            while (eleccion.equals("1")){
                j1.jugarCarta(this);
                System.out.println("Desea añadir una carta en su tablero?");
                eleccion = Utilitaria.inputString("1.Si\n2.No");
            }
        } else {
            maquina.setNoAgregoMonstruo(true);
            maquina.llenarTableroMaquina(tablero);
        }
    }


    public void faseBatalla(Jugador j1, Jugador maquina) {
        if (jugadorActual == 1) {
            j1.declararBatalla(maquina, this);
        } else {
            maquina.declararBatallaComoMaquina(tablero, j1);
        }
    }


    public void sorteoInicios(Jugador j1, Jugador j2) {
        System.out.println("¡Bienvenido al juego de YuGiOH!");
        Jugador[] jugadores = {j1, j2};
        Jugador jugadorEmpieza = jugadores[new Random().nextInt(jugadores.length)];
        this.turno = jugadorEmpieza.getId();
        this.jugadorActual = turno;
        System.out.println("El jugador que empieza es: " + jugadorEmpieza.getNombre() + " -Id: " + jugadorEmpieza.getId());
    }



    public void cambiarTurno() {
        this.turno++;
        if (turno % 2 == 0) {
            this.jugadorActual = 2;
        } else {
            this.jugadorActual = 1;
        }
    }

    public void resetearEstadoCartasMonstruo() {
        int idJ1 = tablero.getJugador1().getId();
        int idJ2 = tablero.getJugador2().getId();
        ArrayList<Carta> cartasMonstruoJ1 = tablero.getTableroCompartido().get(idJ1).get("CartasMonstruo");
        ArrayList<Carta> cartasMonstruoJ2 = tablero.getTableroCompartido().get(idJ2).get("CartasMonstruo");
        for (Carta carta : cartasMonstruoJ1) {
            CartaMonstruo carta_c = (CartaMonstruo)carta;
            carta_c.setPuedeAtacar(true);
        }
        for (Carta carta : cartasMonstruoJ2) {
            CartaMonstruo carta_c = (CartaMonstruo)carta;
            carta_c.setPuedeAtacar(true);
        }
    }


    public void finalizarPartida(Jugador j1, Jugador j2) {
        if (j1.esDerrotado()) {
            System.out.println("El jugador " + j1.getNombre() + " ha sido derrotado.");
            System.out.println("La ganadora es la  " + j2.getNombre());
        } else if (j2.esDerrotado()) {
            System.out.println("La "+ j2.getNombre() + " ha sido derrotada");
            System.out.println("El ganador es " + j1.getNombre());
        }
    }


    //getter and setters
    public int getTurno() {
        return turno;
    }

    public void setTurno(int turno) {
        this.turno = turno;
    }

    public Tablero getTablero() {
        return tablero;
    }

    public void setTablero(Tablero tablero) {
        this.tablero = tablero;
    }

    public int getJugadorActual() {
        return jugadorActual;
    }

    public void setJugadorActual(int jugadorActual) {
        this.jugadorActual = jugadorActual;
    }

    public int getRonda() {
        return ronda;
    }

    public void setRonda(int ronda) {
        this.ronda = ronda;
    }

    
}

