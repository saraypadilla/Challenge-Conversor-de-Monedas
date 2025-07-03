package src;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class ConversorApp {
    private static final String API_KEY = "08d0d00251cf828a2df58683";
    private static final String[] MONEDAS = {"ARS", "BOB", "BRL", "CLP", "COP", "USD"};

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int opcion;

        do {
            mostrarMenu();
            System.out.print("Ingrese una opción (0 para salir): ");
            opcion = scanner.nextInt();
            scanner.nextLine(); // Limpiar buffer

            if (opcion >= 1 && opcion <= MONEDAS.length) {
                String monedaDestino = MONEDAS[opcion - 1];
                System.out.print("Ingrese la cantidad en USD: ");
                double cantidadUSD = scanner.nextDouble();

                double tasa = obtenerTasa(monedaDestino);

                if (tasa > 0) {
                    double convertido = convertir(cantidadUSD, tasa);
                    System.out.printf("%.2f USD equivalen a %.2f %s\n\n", cantidadUSD, convertido, monedaDestino);
                } else {
                    System.out.println("No se pudo obtener la tasa de conversión.\n");
                }
            } else if (opcion != 0) {
                System.out.println("⚠️ Opción inválida. Intente nuevamente.\n");
            }

        } while (opcion != 0);

        System.out.println("Gracias por usar el Conversor de Monedas. ¡Hasta pronto!");
    }

    // Método para mostrar menú
    private static void mostrarMenu() {
        System.out.println("\n🌍 Usted a ingresado al Conversor de Moneda bienvenido/a  🌍");
        for (int i = 0; i < MONEDAS.length; i++) {
            System.out.println((i + 1) + " - Convertir a " + MONEDAS[i]);
        }
        System.out.println("0 - Salir");
    }

    // Método para obtener la tasa de la API
    private static double obtenerTasa(String monedaDestino) {
        String url = "https://v6.exchangerate-api.com/v6/" + API_KEY + "/latest/USD";

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Accept", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                Gson gson = new Gson();
                JsonObject json = gson.fromJson(response.body(), JsonObject.class);
                JsonObject tasas = json.getAsJsonObject("conversion_rates");

                if (tasas.has(monedaDestino)) {
                    return tasas.get(monedaDestino).getAsDouble();
                }
            } else {
                System.out.println("Error al consultar la API. Código: " + response.statusCode());
            }
        } catch (Exception e) {
            System.out.println("Error al conectar con la API: " + e.getMessage());
        }

        return -1; // Valor de error
    }

    // Método de conversión
    private static double convertir(double cantidadUSD, double tasa) {
        return cantidadUSD * tasa;
    }
}
