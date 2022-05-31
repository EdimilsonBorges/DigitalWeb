package br.radio.DigitalWeb.Programation;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Edimilson Borges em 01/05/2022.
 *
 * Esta classe serve para puxar as programações ao vivo do dia de domingo das nuvens.
 */
public class Domingo implements IDiasDaSemanaStrategy {

    @Override
    public String actualize(JSONObject JO3, String hora, String minute, int horaMinute) throws JSONException {

        if(horaMinute >= 500 && horaMinute <= 2200){

            if(horaMinute <= 530){

                return ""+JO3.get("dom0500-0530");
            }
            if(horaMinute <= 600){
                return  ""+JO3.get("dom0530-0600");
            }
            if(horaMinute <= 630){
                return ""+JO3.get("dom0600-0630");
            }
            if(horaMinute <= 700){
                return ""+JO3.get("dom0630-0700");
            }
            if(horaMinute <= 730){
                return ""+JO3.get("dom0700-0730");
            }
            if(horaMinute <= 800){
                return ""+JO3.get("dom0730-0800");
            }
            if(horaMinute <= 830){
                return ""+JO3.get("dom0800-0830");
            }
            if(horaMinute <= 900){
                return ""+JO3.get("dom0830-0900");
            }
            if(horaMinute <= 930){
                return ""+JO3.get("dom0900-0930");
            }
            if(horaMinute <= 1000){
                return ""+JO3.get("dom0930-1000");
            }
            if(horaMinute <= 1030){
                return ""+JO3.get("dom1000-1030");
            }
            if(horaMinute <= 1100){
                return ""+JO3.get("dom1030-1100");
            }
            if(horaMinute <= 1130){
                return ""+JO3.get("dom1100-1130");
            }
            if(horaMinute <= 1200){
                return ""+JO3.get("dom1130-1200");
            }
            if(horaMinute <= 1230){
                return ""+JO3.get("dom1200-1230");
            }
            if(horaMinute <= 1300){
                return ""+JO3.get("dom1230-1300");
            }
            if(horaMinute <= 1330){
                return ""+JO3.get("dom1300-1330");
            }
            if(horaMinute <= 1400){
                return ""+JO3.get("dom1330-1400");
            }
            if(horaMinute <= 1430){
                return ""+JO3.get("dom1400-1430");
            }
            if(horaMinute <= 1500){
                return ""+JO3.get("dom1430-1500");
            }
            if(horaMinute <= 1530){
                return ""+JO3.get("dom1500-1530");
            }
            if(horaMinute <= 1600){
                return ""+JO3.get("dom1530-1600");
            }
            if(horaMinute <= 1630){
                return ""+JO3.get("dom1600-1630");
            }
            if(horaMinute <= 1700){
                return ""+JO3.get("dom1630-1700");
            }
            if(horaMinute <= 1730){
                return ""+JO3.get("dom1700-1730");
            }
            if(horaMinute <= 1800){
                return ""+JO3.get("dom1730-1800");
            }
            if(horaMinute <= 1830){
                return ""+JO3.get("dom1800-1830");
            }
            if(horaMinute <= 1900){
                return ""+JO3.get("dom1830-1900");
            }
            if(horaMinute <= 1930){
                return ""+JO3.get("dom1900-1930");
            }
            if(horaMinute <= 2000){
                return ""+JO3.get("dom1930-2000");
            }
            if(horaMinute <= 2030){
                return ""+JO3.get("dom2000-2030");
            }
            if(horaMinute <= 2100){
                return ""+JO3.get("dom2030-2100");
            }
            if(horaMinute <= 2130){
                return ""+JO3.get("dom2100-2130");
            }
                return ""+JO3.get("dom2130-2200");
        }
        return "";
    }
}
