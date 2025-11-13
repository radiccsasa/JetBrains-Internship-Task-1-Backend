package com.example.queryapp.dataloader;

import com.example.queryapp.model.Passenger;
import com.example.queryapp.repository.PassengerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;

@Component
public class DataLoader implements CommandLineRunner {

    private final PassengerRepository passengerRepository;

    public DataLoader(PassengerRepository passengerRepository) {
        this.passengerRepository = passengerRepository;
    }

    @Override
    public void run(String... args) throws Exception{loadPassengersFromCSV();}

    private void loadPassengersFromCSV()
    {
        try(
                BufferedReader reader = new BufferedReader(
                new InputStreamReader(getClass().getResourceAsStream("/titanic.csv"))
        )
        ) {
            reader.readLine();

            String line;
            List<Passenger> passengers = new ArrayList<>();

            while ((line = reader.readLine()) != null)
            {

                String[] parts = line.split(",(?=([^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*$)", -1);

                if (parts.length < 12) continue;

                Passenger p = new Passenger();

                p.setPassengerId(parseInt(parts[0]));
                p.setSurvived(parseInt(parts[1]));
                p.setPclass(parseInt(parts[2]));
                p.setName(parts[3].replace("\"",""));
                p.setSex(parts[4]);
                p.setAge(parseDouble(parts[5]));
                p.setSibSp(parseInt(parts[6]));
                p.setParch(parseInt(parts[7]));
                p.setTicket(parts[8]);
                p.setFare(parseDouble(parts[9]));
                p.setCabin(parts[10]);
                p.setEmbarked(parts[11]);

                passengers.add(p);
            }

            passengerRepository.saveAll(passengers);
            System.out.println(" Loaded " + passengers.size() + " passengers into H2 database. ");
        } catch (Exception e) {
            System.err.println(" Error loading Titanic dataset: " + e.getMessage());
        }


    }
    private Integer parseInt(String s) {
        try {
            return (s == null || s.isEmpty()) ? null : Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Double parseDouble(String s) {
        try {
            return (s == null || s.isEmpty()) ? null : Double.parseDouble(s);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
