package com.mcardoso.doutorrj.model;

import android.location.Location;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mcardoso on 12/7/15.
 */
public class Establishment {

    @SerializedName("nome")
    String name;

    @SerializedName("razao_social")
    String legalName;

    String cnes;

    String cnpj;

    @SerializedName("endereco")
    String address;

    @SerializedName("numero")
    String addressNumber;

    @SerializedName("complemento")
    String addressAppend;

    @SerializedName("bairro")
    String neighborhood;

    @SerializedName("cep")
    String postalCode;

    @SerializedName("telefone")
    String phone;

    String email;

    @SerializedName("esfera_administrativa")
    String kind;

    String latitude;

    String longitude;

    @SerializedName("tipo_da_unidade")
    String establishmentType;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLegalName() {
        return legalName;
    }

    public void setLegalName(String legalName) {
        this.legalName = legalName;
    }

    public String getCnes() {
        return cnes;
    }

    public void setCnes(String cnes) {
        this.cnes = cnes;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddressNumber() {
        return addressNumber;
    }

    public void setAddressNumber(String addressNumber) {
        this.addressNumber = addressNumber;
    }

    public String getAddressAppend() {
        return addressAppend;
    }

    public void setAddressAppend(String addressAppend) {
        this.addressAppend = addressAppend;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getEstablishmentType() {
        return establishmentType;
    }

    public void setEstablishmentType(String establishmentType) {
        this.establishmentType = establishmentType;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public Location getLocation() {
        Location location = new Location("");
        location.setLatitude(Double.parseDouble(getLatitude()));
        location.setLongitude(Double.parseDouble(getLongitude()));
        return location;
    }
}
