package ec.edu.espol;

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
