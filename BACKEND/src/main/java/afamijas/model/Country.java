package afamijas.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/*
https://dr5hn.github.io/countries-states-cities-database/
https://github.com/dr5hn/countries-states-cities-database

TODO: En la base de datos el campo native debe llamarse nativename


Otras opciones para códigos postales de todos los paíse en:

https://www.geonames.org/export/
https://github.com/Zeeshanahmad4/Zip-code-of-all-countries-cities-in-the-world-CSV-TXT-SQL-DATABASE



 */

@Document(collection = "countries")
public class Country
{
	@Id
	private String _id;

	private Integer id;

	private String name;

	private String iso3;

	private String iso2;

	private Integer numeric_code;

	private String phone_code;

	private String capital;

	private String currency;

	private String currency_name;

	private String tld;

	private String nativename;

	private String region;

	private String region_id; //no importadas

	private String subregion;

	private String subregion_id; //no importadas

	private String nationality;

	private String timezones;

	private Double latitude;

	private Double longitude;

	private String emoji;

	private String emojiU;

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIso3() {
		return iso3;
	}

	public void setIso3(String iso3) {
		this.iso3 = iso3;
	}

	public String getIso2() {
		return iso2;
	}

	public void setIso2(String iso2) {
		this.iso2 = iso2;
	}

	public Integer getNumeric_code() {
		return numeric_code;
	}

	public void setNumeric_code(Integer numeric_code) {
		this.numeric_code = numeric_code;
	}

	public String getPhone_code() {
		return phone_code;
	}

	public void setPhone_code(String phone_code) {
		this.phone_code = phone_code;
	}

	public String getCapital() {
		return capital;
	}

	public void setCapital(String capital) {
		this.capital = capital;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getCurrency_name() {
		return currency_name;
	}

	public void setCurrency_name(String currency_name) {
		this.currency_name = currency_name;
	}

	public String getTld() {
		return tld;
	}

	public void setTld(String tld) {
		this.tld = tld;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getRegion_id() {
		return region_id;
	}

	public void setRegion_id(String region_id) {
		this.region_id = region_id;
	}

	public String getSubregion() {
		return subregion;
	}

	public void setSubregion(String subregion) {
		this.subregion = subregion;
	}

	public String getSubregion_id() {
		return subregion_id;
	}

	public void setSubregion_id(String subregion_id) {
		this.subregion_id = subregion_id;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public String getTimezones() {
		return timezones;
	}

	public void setTimezones(String timezones) {
		this.timezones = timezones;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public String getEmoji() {
		return emoji;
	}

	public void setEmoji(String emoji) {
		this.emoji = emoji;
	}

	public String getEmojiU() {
		return emojiU;
	}

	public void setEmojiU(String emojiU) {
		this.emojiU = emojiU;
	}

	public String getNativename() {
		return nativename;
	}

	public void setNativename(String nativename) {
		this.nativename = nativename;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Country log = (Country) o;

		return _id.equals(log._id);
	}

	@Override
	public int hashCode() {
		return _id.hashCode();
	}
}
