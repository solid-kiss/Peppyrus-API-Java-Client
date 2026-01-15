package be.solid_kiss.peppyrus_api.model;

import java.util.List;

public class BusinessCardEntity{

  /**
   * @param name
   * @param language
   */
	public static record BusinessCardEntityName (String name, String language) {}

  /**
   * @param phone
   * @param name
   * @param type
   * @param email
   */
	public static record BusinessCardEntityContact (String phone, String name, String type, String email) {}

  /**
   * @param scheme
   * @param value
   */
	public static record BusinessCardEntityIdentifier(String scheme, String value){}


	private List<String> website;
	private String geoInfo;
	private String countryCode;
	private List<BusinessCardEntityIdentifier> identifiers;
	private List<BusinessCardEntityName> name;
	private String additionalInfo;
	private String regDate;
	private List<BusinessCardEntityContact> contacts;

	public void setWebsite(List<String> website){
		this.website = website;
	}

	public List<String> getWebsite(){
		return website;
	}

	public void setGeoInfo(String geoInfo){
		this.geoInfo = geoInfo;
	}

	public String getGeoInfo(){
		return geoInfo;
	}

	public void setCountryCode(String countryCode){
		this.countryCode = countryCode;
	}

	public String getCountryCode(){
		return countryCode;
	}

	public void setIdentifiers(List<BusinessCardEntityIdentifier> identifiers){
		this.identifiers = identifiers;
	}

	public List<BusinessCardEntityIdentifier> getIdentifiers(){
		return identifiers;
	}

	public void setName(List<BusinessCardEntityName> name){
		this.name = name;
	}

	public List<BusinessCardEntityName> getName(){
		return name;
	}

	public void setAdditionalInfo(String additionalInfo){
		this.additionalInfo = additionalInfo;
	}

	public String getAdditionalInfo(){
		return additionalInfo;
	}

	public void setRegDate(String regDate){
		this.regDate = regDate;
	}

	public String getRegDate(){
		return regDate;
	}

	public void setContacts(List<BusinessCardEntityContact> contacts){
		this.contacts = contacts;
	}

	public List<BusinessCardEntityContact> getContacts(){
		return contacts;
	}


}
