package br.radio.DigitalWeb.Status;

public class SetDados {

    private final String nomeDoPrograma;
    private final String nomeDoApresentador;
    private final String horario;
    private final String urlImagemItem;

    public SetDados(String nomeDoPrograma, String nomeDoApresentador, String horario, String urlImagemItem) {
        this.nomeDoPrograma = nomeDoPrograma;
        this.nomeDoApresentador = nomeDoApresentador;
        this.horario = horario;
        this.urlImagemItem = urlImagemItem;
    }

    public String getNomeDoPrograma() {
        return nomeDoPrograma;
    }

    public String getNomeDoApresentador() {
        return nomeDoApresentador;
    }

    public String getHorario() {
        return horario;
    }
    public String getUrlImagemItem() {
        return urlImagemItem;
    }

}

