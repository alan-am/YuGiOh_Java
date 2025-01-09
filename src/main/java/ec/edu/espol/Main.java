package ec.edu.espol;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Cuerpo de la partida
        Partida partida = new Partida();

        System.out.println("---+ Jugador 2 corresponde a la máquina (Billy)");
        Jugador usuario = partida.getTablero().getJugador1();
        Jugador maquina = partida.getTablero().getJugador2(); // J2 es la máquina

        // INICIO DEL JUEGO
        System.out.println("\n \n \nEmpieza el juego!");
        System.out.println("Presiona enter para continuar");
        try (Scanner scanner = new Scanner(System.in)) {
            scanner.nextLine(); // Espera la entrada del usuario
        }
        System.out.println("");

        // Inicio con sorteo de quien empieza
        partida.sorteoInicios(usuario, maquina);

        // Ciclo de turnos
        while (!usuario.esDerrotado() && !maquina.esDerrotado()) {
            partida.ronda();
        }

        // Final del juego, luego de que uno es derrotado
        partida.finalizarPartida(usuario, maquina);
    }
}
