package ec.edu.espol;

public class Carta {

    //contador del numero de instancias
    public static int cartas = 0;

    private int id;
    private String nombre;
    private String descripcion;

    //Constructor
    public Carta(String nombre, String descripcion) {
        cartas += 1;
        this.id = cartas;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }
    
    //getters and setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String toString(){
        return "Este es el toString1 de la Carta con ID: " + id;
    }

    public String toString2(){
        return "Este es el toString2 de la Carta con ID: " + id;
    }

    public String toString3(){
        return "Este es el toString3 de la Carta con ID: " + id;
    }
    

}
