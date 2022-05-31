package br.radio.DigitalWeb.Programation;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Edimilson Borges em 01/05/2022.
 */

  public interface IDiasDaSemanaStrategy {

  /**
   * Este método serve para atualizar o nome do programa que está ao vivo.
   * @param JO3 {@link JSONObject} para puxar das nuvens o nome do programa.
   * @param hora {@link String} numeração em texto da hora atual do dispositivo.
   * @param minuto {@link String} numeração em texto dos minutos atual do dispositivo.
   * @param horaMinuto INT Junção da hora e do minuto em numero.
   * @return Retorna uma String com o nome do programa que está ao vivo.
   * @throws JSONException força o compilamento sem tratamento try catch no J03 JSONException.
   */
    String actualize(JSONObject JO3, String hora, String minuto, int horaMinuto) throws JSONException;

}
