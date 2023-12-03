/*
 * Copyright (c) 2022
 */

package mo.specdoc.util;

public class Case {
    private String family, name, lastname;
    private boolean gender;

    public Case(String family, String name, String lastname, boolean gender) {
        this.family = family;
        this.name = name;
        this.lastname = lastname;
        this.gender = gender;
    }

    public Case() {
    }

    public String[] getFamInRP() {
        String endingFam, endingName, endingLastname;
        String endingFamRP = "";
        String endingNameRP = "";
        String endingLastnameRP = "";
        String baseFamily = "";
        String baseName = "";
        String baseLastname = "";

        if (family.length() == 2) {
            endingFam  = family;
        } else if (family.length() > 2) {
            endingFam = family.substring(family.length() - 2);
            baseFamily = family.substring(0, family.length() - 2);
        } else {
            // whatever is appropriate in this case
            throw new IllegalArgumentException("word has less than 2 characters!");
        }
        endingName = name.substring(name.length() - 2);
        baseName = name.substring(0, name.length() - 2);
        endingLastname = lastname.substring(lastname.length() - 2);
        baseLastname = lastname.substring(0, lastname.length() - 2);
        if (gender) { //мужчины
            switch (endingFam) {
                case ("ий") : endingFamRP = "ого"; break;
                case ("ов") : endingFamRP = "ова"; break;
                case ("ев") : endingFamRP = "ева"; break;
                case ("ин") : endingFamRP = "ина"; break;
                default: endingFamRP = endingFam; // не склоняем
            }
            switch (endingName) {
                case ("ел") : endingNameRP = "ла"; break;
                case ("др") : endingNameRP = "дра"; break;
                case ("ей") : endingNameRP = "ея"; break;
                case ("тр") : endingNameRP = "тра"; break;
                case ("ег") : endingNameRP = "ега"; break;
                case ("ан") : endingNameRP = "ана"; break;
                case ("ль") : endingNameRP = "ля"; break;
                case ("ир") : endingNameRP = "ира"; break;
                case ("ий") : endingNameRP = "ия"; break;
                case ("ор") : endingNameRP = "ора"; break;
                case ("рь") : endingNameRP = "ря"; break;
                default: endingNameRP = endingName; // не склоняем
            }
            switch (endingLastname) {
                case ("ич") : endingLastnameRP = "ича"; break;
                default: endingLastnameRP = endingLastname; // не склоняем
            }
        } else { //женщины
            switch (endingFam) {
                case ("ая") : endingFamRP = "ую"; break;
                case ("ва") : endingFamRP = "ву"; break;
                case ("на") : endingFamRP = "ну"; break;
                default: endingFamRP = endingFam; // не склоняем
            }
            switch (endingName) {
                case ("га") : endingNameRP = "гу"; break;
                case ("ия") : endingNameRP = "ию"; break;
                case ("на") : endingNameRP = "ну"; break;
                case ("та") : endingNameRP = "ту"; break;
                case ("да") : endingNameRP = "ду"; break;
                case ("ея") : endingNameRP = "ею"; break;
                case ("ль") : endingNameRP = "лю"; break;
                case ("ья") : endingNameRP = "ью"; break;
                default: endingNameRP = endingName; // не склоняем
            }
            switch (endingLastname) {
                case ("на") : endingLastnameRP = "ину"; break;
                default: endingLastnameRP = endingLastname; // не склоняем
            }
        }
        String[] result = {
                baseFamily + endingFamRP,
                baseName + endingNameRP,
                baseLastname + endingLastnameRP
        };
        return result;
    }
    public String[] getFamInDP() {
        String endingFam, endingName, endingLastname;
        String endingFamDP = "";
        String endingNameDP = "";
        String endingLastnameDP = "";
        String baseFamily = "";
        String baseName = "";
        String baseLastname = "";

        if (family.length() == 2) {
            endingFam  = family;
        } else if (family.length() > 2) {
            endingFam = family.substring(family.length() - 2);
            baseFamily = family.substring(0, family.length() - 2);
        } else {
            // whatever is appropriate in this case
            throw new IllegalArgumentException("word has less than 2 characters!");
        }
        endingName = name.substring(name.length() - 2);
        baseName = name.substring(0, name.length() - 2);
        endingLastname = lastname.substring(lastname.length() - 2);
        baseLastname = lastname.substring(0, lastname.length() - 2);
        if (gender) { //мужчины
            switch (endingFam) {
                case ("ий") : endingFamDP = "ому"; break;
                case ("ов") : endingFamDP = "ову"; break;
                case ("ев") : endingFamDP = "еву"; break;
                case ("ин") : endingFamDP = "ину"; break;
                default: endingFamDP = endingFam; // не склоняем
            }
            switch (endingName) {
                case ("ел") : endingNameDP = "лу"; break;
                case ("др") : endingNameDP = "дру"; break;
                case ("ей") : endingNameDP = "ею"; break;
                case ("тр") : endingNameDP = "тру"; break;
                case ("ег") : endingNameDP = "егу"; break;
                case ("ан") : endingNameDP = "ану"; break;
                case ("ль") : endingNameDP = "лю"; break;
                case ("ир") : endingNameDP = "иру"; break;
                case ("ий") : endingNameDP = "ию"; break;
                case ("ор") : endingNameDP = "ору"; break;
                case ("рь") : endingNameDP = "рю"; break;
                default: endingNameDP = endingName; // не склоняем
            }
            switch (endingLastname) {
                case ("ич") : endingLastnameDP = "ичу"; break;
                default: endingLastnameDP = endingLastname; // не склоняем
            }
        } else { //женщины
            switch (endingFam) {
                case ("ая") : endingFamDP = "ай"; break;
                case ("ва") : endingFamDP = "вой"; break;
                case ("на") : endingFamDP = "ной"; break;
                default: endingFamDP = endingFam; // не склоняем
            }
            switch (endingName) {
                case ("га") : endingNameDP = "ге"; break;
                case ("ия") : endingNameDP = "ии"; break;
                case ("на") : endingNameDP = "не"; break;
                case ("та") : endingNameDP = "ту"; break;
                case ("да") : endingNameDP = "де"; break;
                case ("ея") : endingNameDP = "ее"; break;
                case ("ль") : endingNameDP = "ле"; break;
                case ("ья") : endingNameDP = "ье"; break;
                default: endingNameDP = endingName; // не склоняем
            }
            switch (endingLastname) {
                case ("на") : endingLastnameDP = "ине"; break;
                default: endingLastnameDP = endingLastname; // не склоняем
            }
        }
        String[] result = {
                baseFamily + endingFamDP,
                baseName + endingNameDP,
                baseLastname + endingLastnameDP
        };
        return result;
    }
}
