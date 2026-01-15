package io.github.solid_kiss.peppyrus_api.model;


public class OrganizationInfo {

	private String zipCode;

	private String country;

	private String city;

	private String street;

 	private String name;

	private String houseNumber;

	private String VAT;

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHouseNumber() {
		return houseNumber;
	}

	public void setHouseNumber(String houseNumber) {
		this.houseNumber = houseNumber;
	}

	public String getVAT() {
		return VAT;
	}

	public void setVAT(String VAT) {
		this.VAT = VAT;
	}
}
