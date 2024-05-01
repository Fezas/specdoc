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
            endingFamRP = switch (endingFam) {
                case ("ий") -> "ого";
                case ("ов") -> "ова";
                case ("ев") -> "ева";
                case ("ин") -> "ина";
                default -> endingFam;
            }; // не склоняем
            endingNameRP = switch (endingName) {
                case ("ел") -> "ла";
                case ("др") -> "дра";
                case ("ей") -> "ея";
                case ("тр") -> "тра";
                case ("ег") -> "ега";
                case ("ан") -> "ана";
                case ("ль") -> "ля";
                case ("ир") -> "ира";
                case ("ий") -> "ия";
                case ("ор") -> "ора";
                case ("рь") -> "ря";
                default -> endingName;
            }; // не склоняем
            endingLastnameRP = switch (endingLastname) {
                case ("ич") -> "ича";
                default -> endingLastname;
            }; // не склоняем
        } else { //женщины
            endingFamRP = switch (endingFam) {
                case ("ая") -> "ую";
                case ("ва") -> "ву";
                case ("на") -> "ну";
                default -> endingFam;
            }; // не склоняем
            endingNameRP = switch (endingName) {
                case ("га") -> "гу";
                case ("ия") -> "ию";
                case ("на") -> "ну";
                case ("та") -> "ту";
                case ("да") -> "ду";
                case ("ея") -> "ею";
                case ("ль") -> "лю";
                case ("ья") -> "ью";
                default -> endingName;
            }; // не склоняем
            endingLastnameRP = switch (endingLastname) {
                case ("на") -> "ину";
                default -> endingLastname;
            }; // не склоняем
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
            endingFamDP = switch (endingFam) {
                case ("ий") -> "ому";
                case ("ов") -> "ову";
                case ("ев") -> "еву";
                case ("ин") -> "ину";
                default -> endingFam;
            }; // не склоняем
            endingNameDP = switch (endingName) {
                case ("ел") -> "лу";
                case ("др") -> "дру";
                case ("ей") -> "ею";
                case ("тр") -> "тру";
                case ("ег") -> "егу";
                case ("ан") -> "ану";
                case ("ль") -> "лю";
                case ("ир") -> "иру";
                case ("ий") -> "ию";
                case ("ор") -> "ору";
                case ("рь") -> "рю";
                default -> endingName;
            }; // не склоняем
            endingLastnameDP = switch (endingLastname) {
                case ("ич") -> "ичу";
                default -> endingLastname;
            }; // не склоняем
        } else { //женщины
            endingFamDP = switch (endingFam) {
                case ("ая") -> "ай";
                case ("ва") -> "вой";
                case ("на") -> "ной";
                default -> endingFam;
            }; // не склоняем
            endingNameDP = switch (endingName) {
                case ("га") -> "ге";
                case ("ия") -> "ии";
                case ("на") -> "не";
                case ("та") -> "ту";
                case ("да") -> "де";
                case ("ея") -> "ее";
                case ("ль") -> "ле";
                case ("ья") -> "ье";
                default -> endingName;
            }; // не склоняем
            endingLastnameDP = switch (endingLastname) {
                case ("на") -> "ине";
                default -> endingLastname;
            }; // не склоняем
        }
        String[] result = {
                baseFamily + endingFamDP,
                baseName + endingNameDP,
                baseLastname + endingLastnameDP
        };
        return result;
    }
}
