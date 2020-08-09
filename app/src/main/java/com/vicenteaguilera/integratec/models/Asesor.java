package com.vicenteaguilera.integratec.models;

public class Asesor extends User
{
    String carrera;
    String uRI_image;
    boolean activo;

    public Asesor(String uid,String nombre,String apellidos,String email,String password,String carrera, String URI_image,boolean activo) {
        super(uid, nombre, apellidos, email, password);
        this.carrera = carrera;
        this.uRI_image = URI_image;
        this.activo=false;
    }

    public String getCarrera() {
        return carrera;
    }

    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }

    public String getuRI_image() {
        return uRI_image;
    }

    public void setuRI_image(String uRI_image) {
        this.uRI_image = uRI_image;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
