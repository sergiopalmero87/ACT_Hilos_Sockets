package Actividad;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class Cliente {

    public static final int PUERTO = 12346;
    public static final String IP_SERVER = "LocalHost";

    public static void main(String[] args) throws IOException {

        InetSocketAddress direccion = new InetSocketAddress(IP_SERVER, PUERTO);
        System.out.println("-----CLIENTE-----");
        // Try-catch with resources para que el scanner se cierre solo.
        try(Scanner sc = new Scanner(System.in)){

            System.out.println("CLIENTE: Esperando a que el servidor acepte la conexión");
            Socket socketAlServidor = new Socket();
            socketAlServidor.connect(direccion);
            System.out.println("CLIENTE: Conexion establecida... a " + IP_SERVER + " por el puerto " + PUERTO);

            //Le mandamos los datos al servidor
            PrintStream salida = new PrintStream(socketAlServidor.getOutputStream());

            //Recibimos los datos del servidor
            InputStreamReader entrada = new InputStreamReader(socketAlServidor.getInputStream());

            //Procesamos los datos que nos manda el servidor
            BufferedReader entradaBuffer = new BufferedReader(entrada);

            boolean continuar = true;
            do {
                String opcion;
                //Mostramos el menu al cliente
                System.out.println("-----------------------------");
                System.out.println("Elige una opción: ");
                System.out.println("1. Pelicula por ID: ");
                System.out.println("2. Pelicula por Titulo: ");
                System.out.println("3. Pelicula por Nombre del director: ");
                System.out.println("4. Añadir pelicula: ");
                System.out.println("5. Salir: ");
                System.out.println("-----------------------------");

                opcion = sc.nextLine();
                String texto;
                switch (opcion){

                    case "1":
                        System.out.println("Escribe el id: ");
                        texto = sc.nextLine();
                        salida.println(texto);
                        break;
                    case "2":
                        System.out.println("Escribe el titulo: ");
                        texto = sc.nextLine();
                        salida.println(texto);
                        break;
                    case "3":
                        System.out.println("Escribe el Nombre del director: ");
                        texto = sc.nextLine();
                        salida.println(texto);
                        break;
                    case "4":
                        System.out.println("Escribe los datos para añadir una nueva pelicula");
                        System.out.println("ID: ");
                        int id = sc.nextInt();
                        System.out.println("Titulo: ");
                        String titulo = sc.nextLine();
                        sc.nextLine();
                        System.out.println("Nombre del director: ");
                        String nombreDirector = sc.nextLine();
                        System.out.println("Precio: ");
                        double precio = sc.nextDouble();
                        texto = sc.nextLine();
                        salida.println(texto);
                    case "5":
                        opcion = sc.nextLine();
                        salida.println(opcion);
                        continuar = false;
                        break;
                }

                //Le mandamos el texto al servidor para que lo procese con salida.println(texto)

                System.out.println("Consulta enviada al servidor");
                System.out.println("Cliente esperando respuesta del servidor...");

                //Procesamos los datos del servidor
                String respuestaDelServidor = entradaBuffer.readLine();

                System.out.println("CLIENTE: Servidor responde: " + respuestaDelServidor);


            } while (continuar);
            //Cerramos conexion con el servidor
            socketAlServidor.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            System.err.println("CLIENTE: Error -> " + e);
            e.printStackTrace();
        }

        System.out.println("Cliente: Fin del prorgama");

    }

}
