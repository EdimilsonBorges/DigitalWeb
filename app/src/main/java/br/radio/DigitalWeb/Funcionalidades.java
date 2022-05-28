package br.radio.DigitalWeb;

public  class Funcionalidades {

    public static boolean tocando;
    public static boolean conectando;
    public static boolean erroAoConectar;
    public static boolean erroAoEncontrarServidor;
    public static boolean procurandoServidor;

    public static boolean isProcurandoServidor() {
        return procurandoServidor;
    }

    public static void setProcurandoServidor(boolean procurandoServidor) {
        Funcionalidades.procurandoServidor = procurandoServidor;
    }

    public static boolean isErroAoEncontrarServidor() {
        return erroAoEncontrarServidor;
    }

    public static void setErroAoEncontrarServidor(boolean erroAoEncontrarServidor) {
        Funcionalidades.erroAoEncontrarServidor = erroAoEncontrarServidor;
    }

    public static boolean isConectando() {
        return conectando;
    }

    public static void setConectando(boolean conectando) {
        Funcionalidades.conectando = conectando;
    }

    public static boolean isErroAoConectar() {
        return erroAoConectar;
    }

    public static void setErroAoConectar(boolean erroAoConectar) {
        Funcionalidades.erroAoConectar = erroAoConectar;
    }

    public static boolean isTocando() {
        return tocando;
    }

    public static void setTocando(boolean tocando) {
        Funcionalidades.tocando = tocando;
    }

}