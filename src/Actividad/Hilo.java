package Actividad;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


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

            String textoDelCliente = "";
            String respuesta = "";
            boolean continuar = true;

            //Procesamos entradas hasta que el texto sea 5.
            while (continuar){
                textoDelCliente = bufferCliente.readLine();

                if (textoDelCliente.trim().equals("5")){
                    salidaAlCliente.println("Fin del programa");
                    System.out.println(hilo.getName() + " ha cerrado la comunicacion");
                    continuar = false;
                } else {
                    switch (textoDelCliente){
                        case "1":
                            respuesta = getPeliculaByTitle(textoDelCliente).toString();
                            salidaAlCliente.println(respuesta);
                        case "2":
                            getPeliculaByTitle(textoDelCliente);
                        case "3":
                            getPeliculaByDirectorName(textoDelCliente);
                        case "4":
                            addPelicula();
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
    public String getPeliculaById(String textoCliente){
        for (ServidorPeliculas p : listaPeliculas){
            if (String.valueOf(p.id).equals(textoCliente)){
                System.out.println(p);
            }
        }
        return "No encontrado";
    }

    public String getPeliculaByTitle(String textoCliente) {
        List<ServidorPeliculas> peliculas = new ArrayList<>();
        for (ServidorPeliculas p : listaPeliculas) {
            if (textoCliente.equalsIgnoreCase(p.titulo)) {
                peliculas.add(p);
                System.out.println(p);
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

    private String addPelicula() {
        ServidorPeliculas nuevaPelicula = new ServidorPeliculas();
        listaPeliculas.add(nuevaPelicula);
        System.out.println("La pelicula " + nuevaPelicula + " se ha añadido");
        return listaPeliculas.toString();

    }

}
