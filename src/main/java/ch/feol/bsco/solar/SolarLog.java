package ch.feol.bsco.solar;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.Builder;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import ch.feol.bsco.quantity.Power;

public class SolarLog {

   // One instance, reuse
   private final HttpClient httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).build();

   private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm:ss");

   private final String address;
   
   public SolarLog(String address) {
      this.address = address;
   }

   public Data read() throws SolarLogException {

      Builder builder = HttpRequest.newBuilder();
      builder.POST(HttpRequest.BodyPublishers.ofString("{\"801\":{\"170s\":null}}"));
      builder.uri(URI.create("http://" + address + "/getjp"));
      builder.header("Content-Type", "application/json");
      HttpRequest request = builder.build();

      HttpResponse<String> response;
      try {
         response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
      } catch (IOException e) {
         throw new SolarLogException("Sending the request failed", e);
      } catch (InterruptedException e) {
         Thread.currentThread().interrupt();
         throw new SolarLogException("The request was interrupted", e);
      }

      if (response.statusCode() != 200) {
         throw new SolarLogException("Post request status code is " + response.statusCode());
      }
      return parse(response.body());
   }

   static Data parse(String data) throws SolarLogException {

      LocalDateTime lastUpdate = null;
      long productionPowerAc = 0;
      long productionPowerDc = 0;
      long consumptionPowerAc = 0;

      JsonElement element = JsonParser.parseString(data);
      if (element.isJsonObject()) {
         JsonObject logData = element.getAsJsonObject();
         if (logData.has("801")) {
            JsonObject root = logData.getAsJsonObject("801");
            if (root.has("170")) {
               JsonObject properties = root.getAsJsonObject("170");
               lastUpdate = getAsTimestamp(properties, "100", FORMATTER);
               productionPowerAc = getAsLong(properties, "101");
               productionPowerDc = getAsLong(properties, "102");
               consumptionPowerAc = getAsLong(properties, "110");
            } else {
               throw new SolarLogException("SolarLog data has no properties (170)");
            }
         } else {
            throw new SolarLogException("SolarLog data has no root (801)");
         }
      } else {
         throw new SolarLogException("Input data is no JsonObject");
      }
      return new Data(lastUpdate, Power.watt(productionPowerAc), Power.watt(productionPowerDc), Power.watt(consumptionPowerAc));
   }

   private static LocalDateTime getAsTimestamp(JsonObject properties, String name, DateTimeFormatter formatter) throws SolarLogException {
      if (properties.has(name)) {
         return LocalDateTime.parse(properties.getAsJsonPrimitive(name).getAsString(), formatter);
      } else {
         throw new SolarLogException("Timestamp with name " + name + " does not exist");
      }
   }

   private static long getAsLong(JsonObject properties, String name) throws SolarLogException {
      if (properties.has(name)) {
         return properties.getAsJsonPrimitive(name).getAsLong();
      } else {
         throw new SolarLogException("Long with name " + name + " does not exist");
      }
   }
}
