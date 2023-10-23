package Actividad;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
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

        System.out.println("Estableciendo conexion con cliente " + hilo.getName());

        InputStreamReader entradaCliente;
        BufferedReader leerCliente;
        PrintStream salidaAlCliente;


        try{
            //Datos que nos llegan del cliente
            entradaCliente = new InputStreamReader(socketAlCliente.getInputStream());

            //Procesamos los datos del cliente
            leerCliente = new BufferedReader(entradaCliente);

            //Le enviamos los datos al cliente
            salidaAlCliente = new PrintStream(socketAlCliente.getOutputStream());

            String texto = "";
            boolean continuar = true;

            //Procesamos entradas hasta que el texto sea 5.
            while (continuar){
                texto = leerCliente.readLine();
                String respuesta;


                if (texto.equalsIgnoreCase("5")){
                    salidaAlCliente.println("Fin del programa");
                    System.out.println(hilo.getName() + " ha cerrado la comunicacion");
                    continuar = false;
                } else {

                    switch (texto) {

                        case "1":
                            System.out.println("Recibida consulta para buscar por ID.");
                            respuesta = getPeliculaById(texto);
                            salidaAlCliente.println(respuesta);
                            break;
                        case "2":
                            System.out.println("Recibida consulta para buscar por Título.");
                            respuesta = getPeliculaByTitle(texto);
                            salidaAlCliente.println(respuesta);
                            break;
                        case "3":
                            System.out.println("Recibida consulta para buscar por Nombre del Director.");
                            respuesta = getPeliculaByDirectorName(texto);
                            salidaAlCliente.println(respuesta);
                            break;
                        case "4":
                            System.out.println("Recibida consulta para agregar película.");
                            respuesta = addPelicula(texto);
                            salidaAlCliente.println(respuesta);
                            break;
                        default:
                            System.out.println("Consulta no reconocida: " + texto);
                            salidaAlCliente.println("Error: Consulta no reconocida.");
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
    public String getPeliculaById(String textoCliente) {
        for (ServidorPeliculas p : listaPeliculas){
            if (textoCliente.equalsIgnoreCase(String.valueOf(p.id))){
                return p.toString();
            } else {
                System.out.println("No encontrada.");
            }
        }
        return null;
    }

    public String getPeliculaByTitle(String textoCliente) {
        List<ServidorPeliculas> peliculas = new ArrayList<>();
        for (ServidorPeliculas p : listaPeliculas) {
            if (textoCliente.equalsIgnoreCase(p.titulo)) {
                peliculas.add(p);
            }
        }

        // Si no se encontró ninguna película, envía un mensaje al cliente.
        if (peliculas.isEmpty()) {
            System.out.println("No se encontró ninguna película con el título especificado");
            return null;
        }

        // Devuelve la lista de películas encontradas.
        return peliculas.toString();
    }

    public String getPeliculaByDirectorName(String textoCliente) {
        List<ServidorPeliculas> peliculasDirector = new ArrayList<>();
        for (ServidorPeliculas p : listaPeliculas) {
            if (textoCliente.equalsIgnoreCase(p.nombreDirector)) {
                peliculasDirector.add(p);
            }
        }

        // Si no se encontró ninguna película, envía un mensaje al cliente.
        if (peliculasDirector.isEmpty()) {
            System.out.println("No se encontró ninguna película con el nombre de director especificado");
            return null;
        }
        return peliculasDirector.toString();
    }

    private String addPelicula(String textoCliente) {
        ServidorPeliculas nuevaPelicula = new ServidorPeliculas();
        listaPeliculas.add(nuevaPelicula);
        System.out.println("La pelicula " + nuevaPelicula + " se ha añadido");
        return listaPeliculas.toString();

    }

}
