package afamijas.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

/*
https://dr5hn.github.io/countries-states-cities-database/
https://github.com/dr5hn/countries-states-cities-database

Otras opciones para códigos postales de todos los países en:

https://www.geonames.org/export/
https://github.com/Zeeshanahmad4/Zip-code-of-all-countries-cities-in-the-world-CSV-TXT-SQL-DATABASE

 */

@Document(collection = "cities")
public class City
{
	@Id
	private String _id;

	private Integer id;

	private String name;

	private Integer state_id;

	private String state_code;

	private String state_name;

	private Integer country_id;

	private String country_code;

	private String  country_name;

	private Double latitude;

	private Double longitude;

	private String wikiDataId;

	private List<String> postalcodes;


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

	public Integer getState_id() {
		return state_id;
	}

	public void setState_id(Integer state_id) {
		this.state_id = state_id;
	}

	public String getState_code() {
		return state_code;
	}

	public void setState_code(String state_code) {
		this.state_code = state_code;
	}

	public String getState_name() {
		return state_name;
	}

	public void setState_name(String state_name) {
		this.state_name = state_name;
	}

	public Integer getCountry_id() {
		return country_id;
	}

	public void setCountry_id(Integer country_id) {
		this.country_id = country_id;
	}

	public String getCountry_code() {
		return country_code;
	}

	public void setCountry_code(String country_code) {
		this.country_code = country_code;
	}

	public String getCountry_name() {
		return country_name;
	}

	public void setCountry_name(String country_name) {
		this.country_name = country_name;
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

	public String getWikiDataId() {
		return wikiDataId;
	}

	public void setWikiDataId(String wikiDataId) {
		this.wikiDataId = wikiDataId;
	}

	public List<String> getPostalcodes() {
		return postalcodes;
	}

	public void setPostalcodes(List<String> postalcodes) {
		this.postalcodes = postalcodes;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		City log = (City) o;

		return _id.equals(log._id);
	}

	@Override
	public int hashCode() {
		return _id.hashCode();
	}
}
