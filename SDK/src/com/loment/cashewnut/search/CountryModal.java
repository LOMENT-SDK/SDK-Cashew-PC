package com.loment.cashewnut.search;

public class CountryModal {
	private String country_code;
	private String country_Name;
	private String country_abbr;

	public CountryModal(String country_code, String country_Name, String country_abbr) {
		this.country_code = country_code;
		this.country_Name = country_Name;
		this.country_abbr = country_abbr;
	}

	public String getCountryCode() {
		return this.country_code;
	}

	public String getCountryName() {
		return this.country_Name;
	}

	public String getCountryAbbr() {
		return this.country_abbr;
	}
}
