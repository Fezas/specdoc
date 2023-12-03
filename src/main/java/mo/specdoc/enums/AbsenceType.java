/*
 * Copyright (c) 2022
 */

package mo.specdoc.enums;

public enum AbsenceType {
    OTPUSK1("основной отпуск"),
    OTPUSK2("учебный отпуск"),
    OTPUSK3("отпуск по болезни"),
    OTPUSK4("отпуск по семейным обстоятельствам"),
    OTPUSK5("ветеранский отпуск"),
    OTPUSK6("отпуск по увольнению"),
    KOMANDIROVKA("командировка"),
    GOSPITAL("госпить"),
    BOLEN("болен"),
    VVK("ВВК");

    private final String type;

    AbsenceType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
