package com.dnatividad.cutapp.Utilitarios.Entidades;

public class Usuarios {
    public int getIdUser() {
        return idUser;
    }

    public String getNomUser() {
        return nomUser;
    }

    public String getApePatUser() {
        return apePatUser;
    }

    public String getApeMatUser() {
        return apeMatUser;
    }

    public String getTelfUser() {
        return telfUser;
    }

    public int getRol() {
        return rol;
    }

    public String getCorreoUser() {
        return correoUser;
    }

    public String getContrasenaUser() {
        return contrasenaUser;
    }

    private int idUser;
    private String nomUser;
    private String apePatUser;
    private String apeMatUser;
    private String telfUser;
    private int rol;
    private String correoUser;
    private String contrasenaUser;


    public Usuarios(int idUser, String nomUser, String apePatUser, String apeMatUser, String telfUser, int rol, String correoUser, String contrasenaUser) {
        this.idUser = idUser;
        this.nomUser = nomUser;
        this.apePatUser = apePatUser;
        this.apeMatUser = apeMatUser;
        this.telfUser = telfUser;
        this.rol = rol;
        this.correoUser = correoUser;
        this.contrasenaUser = contrasenaUser;
    }
    public Usuarios(String nomUser, String apePatUser, String apeMatUser, String telfUser, int rol, String correoUser, String contrasenaUser) {
        this.nomUser = nomUser;
        this.apePatUser = apePatUser;
        this.apeMatUser = apeMatUser;
        this.telfUser = telfUser;
        this.rol = rol;
        this.correoUser = correoUser;
        this.contrasenaUser = contrasenaUser;
    }


    }
