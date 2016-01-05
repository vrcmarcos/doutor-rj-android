package com.mcardoso.doutorrj.model.establishment;

import com.mcardoso.doutorrj.R;

/**
 * Created by mcardoso on 12/23/15.
 */
public enum EstablishmentType {

    UNIDADE_APOIO("UNIDADE DE APOIO DIAGNOSE E TERAPIA (SADT ISOLADO)"),
    CONSULTORIO("CONSULTORIO ISOLADO"),
    CLINICA("CLINICA/CENTRO DE ESPECIALIDADE"),
    HOSPITAL("HOSPITAL GERAL", R.id.menu_hospital, true),
    POLICLINICA("POLICLINICA", R.id.menu_policlina),
    HOSPITAL_ESPECIALIZADO("HOSPITAL ESPECIALIZADO"),
    HOSPITAL_ISOLADO("HOSPITAL/DIA - ISOLADO"),
    CENTRO_DE_SAUDE("CENTRO DE SAUDE/UNIDADE BASICA"),
    HOME_CARE("SERVICO DE ATENCAO DOMICILIAR ISOLADO(HOME CARE)"),
    UNIDADE_MOVEL("UNIDADE MOVEL DE NIVEL PRE-HOSPITALAR NA AREA DE URGENCIA"),
    CENTRAL_ORGAOS("CENTRAL DE NOTIFICACAO,CAPTACAO E DISTRIB DE ORGAOS ESTADUAL"),
    PRONTO_SOCORRO_ESPECIALIZADO("PRONTO SOCORRO ESPECIALIZADO"),
    POSTO_SAUDE("POSTO DE SAUDE"),
    CENTRO_HEMOTERAPIA_HEMATOLOGIA("CENTRO DE ATENCAO HEMOTERAPICA E OU HEMATOLOGICA"),
    CENTRO_PISCOSSOCIAL("CENTRO DE ATENCAO PSICOSSOCIAL"),
    LABORATORIO("LABORATORIO DE SAUDE PUBLICA"),
    PRONTO_ATENDIMENTO("PRONTO ATENDIMENTO"),
    UNIDADE_VIGILANCIA("UNIDADE DE VIGILANCIA EM SAUDE"),
    PARTO("CENTRO DE PARTO NORMAL - ISOLADO"),
    CENTRO_REGULACAO_ACESSO("CENTRAL DE REGULACAO DO ACESSO"),
    UNIDADE_MISTA("UNIDADE MISTA"),
    CENTRAL_REGULACAO_SAUDE("CENTRAL DE REGULACAO DE SERVICOS DE SAUDE"),
    PRONTO_SOCORRO_GERAL("PRONTO SOCORRO GERAL"),
    SECRETARIA_SAUDE("SECRETARIA DE SAUDE", R.id.menu_secretaria_saude),
    TELESAUDE("TELESSAUDE");

    private String name;
    private Integer menuId;
    private Boolean isDefault;

    EstablishmentType(String name) {
        this(name, null);
    }

    EstablishmentType(String name, Integer menuId) {
        this(name, menuId, false);
    }

    EstablishmentType(String name, Integer menuId, Boolean isDefault) {
        this.name = name;
        this.menuId = menuId;
        this.isDefault = isDefault;
    }

    public String getName() {
        return name;
    }

    public static EstablishmentType getTypeById(Integer menuId) {
        EstablishmentType result = null;
        for (EstablishmentType type : EstablishmentType.values()) {
            if ( menuId.equals(type.menuId) ) {
                result = type;
                break;
            }
        }

        return result;
    }

    public static EstablishmentType getDefault() {
        EstablishmentType result = null;
        for (EstablishmentType type : EstablishmentType.values()) {
            if ( type.isDefault ) {
                result = type;
                break;
            }
        }

        return result;
    }
}