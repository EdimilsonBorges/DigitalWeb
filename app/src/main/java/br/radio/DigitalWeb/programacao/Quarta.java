package br.radio.DigitalWeb.programacao;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Edimilson Borges em 01/05/2022.
 *
 * Esta classe serve para puxar as programações ao vivo do dia de quarta das nuvens.
 */

public class Quarta implements IDiasDaSemanaStrategy {
    @Override
    public String actualize(JSONObject JO3, String hora, String minuto, int horaMinuto) throws JSONException {

        if(horaMinuto >= 500 && horaMinuto <= 2200){

            if(horaMinuto <= 530){
                return ""+JO3.get("qua0500-0530");
            }
            if(horaMinuto <= 600){
                return ""+JO3.get("qua0530-0600");
            }
            if(horaMinuto <= 630){
                return ""+JO3.get("qua0600-0630");
            }
            if(horaMinuto <= 700){
                return ""+JO3.get("qua0630-0700");
            }
            if(horaMinuto <= 730){
                return ""+JO3.get("qua0700-0730");
            }
            if(horaMinuto <= 800){
                return ""+JO3.get("qua0730-0800");
            }
            if(horaMinuto <= 830){
                return ""+JO3.get("qua0800-0830");
            }
            if(horaMinuto <= 900){
                return ""+JO3.get("qua0830-0900");
            }
            if(horaMinuto <= 930){
                return ""+JO3.get("qua0900-0930");
            }
            if(horaMinuto <= 1000){
                return ""+JO3.get("qua0930-1000");
            }
            if(horaMinuto <= 1030){
                return ""+JO3.get("qua1000-1030");
            }
            if(horaMinuto <= 1100){
                return ""+JO3.get("qua1030-1100");
            }
            if(horaMinuto <= 1130){
                return ""+JO3.get("qua1100-1130");
            }
            if(horaMinuto <= 1200){
                return ""+JO3.get("qua1130-1200");
            }
            if(horaMinuto <= 1230){
                return ""+JO3.get("qua1200-1230");
            }
            if(horaMinuto <= 1300){
                return ""+JO3.get("qua1230-1300");
            }
            if(horaMinuto <= 1330){
                return ""+JO3.get("qua1300-1330");
            }
            if(horaMinuto <= 1400){
                return ""+JO3.get("qua1330-1400");
            }
            if(horaMinuto <= 1430){
                return ""+JO3.get("qua1400-1430");
            }
            if(horaMinuto <= 1500){
                return ""+JO3.get("qua1430-1500");
            }
            if(horaMinuto <= 1530){
                return ""+JO3.get("qua1500-1530");
            }
            if(horaMinuto <= 1600){
                return ""+JO3.get("qua1530-1600");
            }
            if(horaMinuto <= 1630){
                return ""+JO3.get("qua1600-1630");
            }
            if(horaMinuto <= 1700){
                return ""+JO3.get("qua1630-1700");
            }
            if(horaMinuto <= 1730){
                return ""+JO3.get("qua1700-1730");
            }
            if(horaMinuto <= 1800){
                return ""+JO3.get("qua1730-1800");
            }
            if(horaMinuto <= 1830){
                return ""+JO3.get("qua1800-1830");
            }
            if(horaMinuto <= 1900){
                return ""+JO3.get("qua1830-1900");
            }
            if(horaMinuto <= 1930){
                return ""+JO3.get("qua1900-1930");
            }
            if(horaMinuto <= 2000){
                return ""+JO3.get("qua1930-2000");
            }
            if(horaMinuto <= 2030){
                return ""+JO3.get("qua2000-2030");
            }
            if(horaMinuto <= 2100){
                return ""+JO3.get("qua2030-2100");
            }
            if(horaMinuto <= 2130){
                return ""+JO3.get("qua2100-2130");
            }
                return ""+JO3.get("qua2130-2200");
        }
        return "";
    }
}
