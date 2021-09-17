package com.alex.study.services;

import com.alex.study.beans.Company;
import com.alex.study.beans.Root;
import com.alex.study.beans.Security;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class JsonJackson {

    public static Root parseJsonJackson() {
        ObjectMapper mapper = new ObjectMapper();
        Root root = null;

        try (FileReader fileReader = new FileReader("src/companies.json")){
            root = mapper.readValue(fileReader, Root.class);

        } catch (Exception e) {
            System.out.println("Jackson pars error " + e);;
        }

        return root;
    }


    public static void printCompanies() {
        Root root = parseJsonJackson();
        root.getCompanies().stream().forEach(c -> System.out.println(c.getName() + " - "
                + LocalDate.ofInstant(c.getFounded().toInstant(), ZoneId.systemDefault())));
        System.out.println();
    }


    public static void printSecurities() {
        Root root = parseJsonJackson();
        Date input = new Date();
        List<Company> companies = root.getCompanies();
        LocalDate dateToday = LocalDate.ofInstant(input.toInstant(), ZoneId.systemDefault());
        List<Security> securitiesList = root.getCompanies().listIterator().next().getSecurities();
        List<Security> securities = new ArrayList<>();
        int count = 0;

        for (int i = 0; i < companies.size(); i++) {
            for (int j = 0; j < companies.get(i).getSecurities().size(); j++) {
                if (LocalDate.ofInstant(companies.get(i).getSecurities().get(j).getDate().toInstant(), ZoneId.systemDefault()).isAfter(dateToday)) {
                    System.out.println(companies.get(i).getSecurities().get(j).getCode() + " - "
                            + LocalDate.ofInstant(companies.get(i).getSecurities().get(j).getDate().toInstant(), ZoneId.systemDefault()) + " - "
                            + companies.get(i).getName());
                    count++;
                }
            }
        }

        System.out.println("Total = " + count);
        System.out.println();
    }


    public static void printCompaniesFoundedAfter() {
        Root root = parseJsonJackson();
        List<Company> companies = root.getCompanies();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String str;
        LocalDate localDate;
        System.out.println("Enter date");

        try {
            str = bufferedReader.readLine();
            String str1 = checkAndChangeString(str);
            localDate = LocalDate.parse(str1, dateTimeFormatter);

            companies.stream()
                    .filter(c -> LocalDate.ofInstant(c.getFounded().toInstant(), ZoneId.systemDefault()).isAfter(localDate))
                    .forEach(c -> System.out.println(c.getName() + " - " + LocalDate.ofInstant(c.getFounded().toInstant(), ZoneId.systemDefault())));

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println();
    }


    public static void printCurrencyCodeInformation() {
        Root root = parseJsonJackson();
        List<Company> companies = root.getCompanies();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String str;
        System.out.println("Enter the currency code");

        try {
            str = bufferedReader.readLine();

            for (int i = 0; i < companies.size(); i++) {
                for (int j = 0; j < companies.get(i).getSecurities().size(); j++) {
                    for (int z = 0; z < companies.get(i).getSecurities().get(j).getCurrency().size(); z++) {
                        if (companies.get(i).getSecurities().get(j).getCurrency().get(z).equals(str)) {
                            System.out.println(companies.get(i).getSecurities().get(j).getCode() + " - " + companies.get(i).getId());
                        }
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println();
    }


    public static String checkAndChangeString(String str) {
        String cutDateAndMount = str.substring(0, 6);
        String c = "";
        if (str.length() <= 8) {
            String strCut = str.substring(6);
            int a = Integer.parseInt(strCut);
            LocalDate todayDate = LocalDate.now();
            String todayDateString = todayDate.toString();
            String todayDateStringCut = todayDateString.substring(2, 4);
            int b = Integer.parseInt(todayDateStringCut);

            if (a > b) {
                c = "19" + a;
            } else {
                if (a < 10) {
                    c = "200" + a;
                } else {
                    c = "20" + a;
                }
            }
            str = cutDateAndMount + c;
        }

        String res = str.replaceAll(",|/", ".");
        return res;
    }

}