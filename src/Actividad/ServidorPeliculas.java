package Actividad;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ServidorPeliculas {

    int id;
    String titulo, nombreDirector;
    double precio;

    private static List<ServidorPeliculas> listaPeliculas = new ArrayList<>();

    public ServidorPeliculas() {
    }

    public ServidorPeliculas(int id, String titulo, String nombreDirector, double precio ) {
        this.id = id;
        this.titulo = titulo;
        this.nombreDirector = nombreDirector;
        this.precio = precio;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServidorPeliculas that = (ServidorPeliculas) o;
        return id == that.id && Double.compare(precio, that.precio) == 0 && Objects.equals(titulo, that.titulo) && Objects.equals(nombreDirector, that.nombreDirector);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, titulo, nombreDirector, precio);
    }

    @Override
    public String toString() {
        return "ServidorPeliculas{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", nombreDirector='" + nombreDirector + '\'' +
                ", precio=" + precio +
                '}';
    }

    public static void main(String[] args) {

        listaPeliculas.add(new ServidorPeliculas(1, "Pelicula 1", "Director 1", 12.0));
        listaPeliculas.add(new ServidorPeliculas(2, "Pelicula 2", "Director 2", 14.0));
        listaPeliculas.add(new ServidorPeliculas(3, "Pelicula 3", "Director 3", 16.0));
        listaPeliculas.add(new ServidorPeliculas(4, "Pelicula 4", "Director 4", 18.0));
        listaPeliculas.add(new ServidorPeliculas(5, "Pelicula 5", "Director 5", 20.0));

        System.out.println(listaPeliculas);
    }
}
