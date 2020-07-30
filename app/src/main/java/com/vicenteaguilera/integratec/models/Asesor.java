package com.vicenteaguilera.integratec.models;

public class Asesor extends User
{
    String carrera;
    String uRI_image;

    public Asesor(String uid,String nombre,String apellidos,String email,String password,String carrera, String URI_image) {
        super(uid, nombre, apellidos, email, password);
        carrera = carrera;
        this.uRI_image = URI_image;
    }

}
