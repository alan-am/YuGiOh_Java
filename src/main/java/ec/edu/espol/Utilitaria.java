package ec.edu.espol;

import java.util.Scanner;

public class Utilitaria {
    private static Scanner lector = new Scanner(System.in);
    //clase creada para funciones utilitarias

    public static String inputString(String mensajeAMostrar){
        //Scanner para recibir Strings
        //muestra el mensaje 
        //ppide un dato y luego lo devuelve
        // se utili print envez de println para personalizar mejor
        System.out.print(mensajeAMostrar + "\n" + "> ");
        return lector.nextLine();

    }

    public static int inputInt(String mensajeAMostrar){

        //para obtener un int se debe tener en cuenta limpiar el Scanner
        System.out.print(mensajeAMostrar + "\n" + "> ");
        int dato = lector.nextInt();
        //deja limpio el scanner
        lector.nextLine();
        return dato;
    }

    public static void simularEnter(String mensajeAMostrar){
        //se utiliza un print diferente que no genere un salto 
        //de linea automatico(para q la barra de escritura aparezca alado del mensaje)
        System.out.print(mensajeAMostrar);
        lector.nextLine();
    }

}

//no olvidar cerrar el scanner en el main al finaalll del juego