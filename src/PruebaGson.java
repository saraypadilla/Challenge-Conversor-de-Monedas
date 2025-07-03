package src;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class PruebaGson {
    public static void main(String[] args) {
        String json = "{\"base_code\":\"USD\", \"conversion_rates\":{\"COP\":4000.25}}";

        Gson gson = new Gson();
        JsonObject objetoJson = gson.fromJson(json, JsonObject.class);

        String baseCode = objetoJson.get("base_code").getAsString();
        double tasaCOP = objetoJson.getAsJsonObject("conversion_rates").get("COP").getAsDouble();

        System.out.println("Moneda base: " + baseCode);
        System.out.println("Tasa de COP: " + tasaCOP);
    }
}
