package Actividad;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


import static Actividad.ServidorPeliculas.listaPeliculas;

public class Hilo implements Runnable {

    private final Thread hilo;
    private static int numCliente = 0;
    private final Socket socketAlCliente;


    public Hilo(Socket socketAlCliente) {
        numCliente++;
        hilo = new Thread(this, "Cliente_"+numCliente);
        this.socketAlCliente = socketAlCliente;
        hilo.start();
    }

    @Override
    public void run() {

        System.out.println("Conexion establecida con el cliente " + hilo.getName());

        InputStreamReader entradaDelCliente;
        BufferedReader bufferCliente;
        PrintStream salidaAlCliente;

        try{
            //Datos que nos llegan del cliente
            entradaDelCliente = new InputStreamReader(socketAlCliente.getInputStream());

            //Procesamos los datos del cliente
            bufferCliente = new BufferedReader(entradaDelCliente);

            //Le enviamos los datos al cliente
            salidaAlCliente = new PrintStream(socketAlCliente.getOutputStream());

            String opcionDelCliente = "";
            boolean continuar = true;

            //Procesamos entradas hasta que el texto sea 5.
            while (continuar){
                opcionDelCliente = bufferCliente.readLine();
                //Si la opcion del cliente es 5 entonces se cierra la comunicacion.
                if (opcionDelCliente.trim().equals("5")){
                    salidaAlCliente.println("Fin del programa");
                    System.out.println(hilo.getName() + " ha cerrado la comunicacion");
                    continuar = false;
                } else {
                    //Si no, el cliente manda la opcion y entra en el switch.
                    //Entonces el cliente manda la consulta que se procesa con el bufferedReader.
                    String consultaCliente = "";
                    consultaCliente = bufferCliente.readLine();
                    switch (opcionDelCliente){
                        case "1":
                            System.out.println("Consulta por id aceptada. Respuesta enviada"); //Este mensaje se muestra en el servidor
                            getPeliculaById(consultaCliente, salidaAlCliente);
                            break;
                        case "2":
                            System.out.println("Consulta por titulo aceptada. Respuesta enviada");
                            getPeliculaByTitle(consultaCliente, salidaAlCliente);
                            break;
                        case "3":
                            System.out.println("Consulta por director aceptada. Respuesta enviada");
                            buscarPorDirector(consultaCliente, salidaAlCliente);
                            break;
                        case "4":
                            System.out.println("Consulta para añadir nueva película aceptada. Respuesta enviada");
                            int id = Integer.parseInt(bufferCliente.readLine());
                            String titulo = bufferCliente.readLine();
                            String nombreDirector = bufferCliente.readLine();
                            double precio = Double.parseDouble(bufferCliente.readLine());
                            addPelicula(id, titulo, nombreDirector, precio, salidaAlCliente);
                            break;
                        default:
                            salidaAlCliente.println("Error. Numero de consulta incorrecto.");
                    }
                }
            }
            //Cerramos conexion con el cliente
            socketAlCliente.close();

        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //Logica para responder al cliente

    //Funciones para buscar peliculas dentro de la lista.
    //Reciben dos parametros: El dato a consultar y la salida hacia el cliente.
    private synchronized void getPeliculaById(String idConsulta, PrintStream salidaAlCliente) {
        for (ServidorPeliculas p : listaPeliculas) {
            if (String.valueOf(p.id).equals(idConsulta)) {
                salidaAlCliente.println(p);
                return; // Salir del bucle una vez que se encuentra una coincidencia.
            }
        }
        salidaAlCliente.println("No se encontró ninguna película con el ID especificado");
    }

    private synchronized void getPeliculaByTitle(String tituloConsulta, PrintStream salidaAlCliente) {
        List<ServidorPeliculas> peliculas = new ArrayList<>();
        for (ServidorPeliculas p : listaPeliculas) {
            if (p.titulo.equalsIgnoreCase(tituloConsulta)) {
                peliculas.add(p);
            }
        }
        if (peliculas.isEmpty()) {
            salidaAlCliente.println("No se encontró ninguna película con el título especificado");
        } else {
            salidaAlCliente.println(peliculas);
        }
    }


    private synchronized void buscarPorDirector(String directorConsulta, PrintStream salidaAlCliente) {
        List<ServidorPeliculas> peliculasDirector = new ArrayList<>();
        for (ServidorPeliculas p : listaPeliculas) {
            if (directorConsulta.equalsIgnoreCase(p.nombreDirector)) {
                peliculasDirector.add(p);
            }
        }
        if (peliculasDirector.isEmpty()) {
            salidaAlCliente.println("No se encontró ninguna película con el nombre del director especificado");
        } else {
            salidaAlCliente.println(peliculasDirector);
        }
    }

    private synchronized void addPelicula(int id, String titulo, String nombreDirector, double precio, PrintStream salidaAlCliente) {
        ServidorPeliculas nuevaPelicula = new ServidorPeliculas(id, titulo, nombreDirector, precio);
        listaPeliculas.add(nuevaPelicula);
        salidaAlCliente.println("La película se ha añadido con éxito:");
        salidaAlCliente.println(listaPeliculas.toString());
    }



}
