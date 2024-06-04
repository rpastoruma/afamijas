package afamijas.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/*
Solo vale para precargar los códigos postales descargados de https://github.com/inigoflores/ds-codigos-postales-ine-es/blob/master/data/codigos_postales_municipios.csv
y luego importarlos con importPostalCodes() que quedarían guardados en la colección Cities

 */

@Document(collection = "postalcodes")
public class PostalCode
{
	@Id
	private String _id;

	private String codigo_postal;

	private String municipio_id;
	private String municipio_nombre;

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getCodigo_postal() {
		return codigo_postal;
	}

	public void setCodigo_postal(String codigo_postal) {
		this.codigo_postal = codigo_postal;
	}

	public String getMunicipio_id() {
		return municipio_id;
	}

	public void setMunicipio_id(String municipio_id) {
		this.municipio_id = municipio_id;
	}

	public String getMunicipio_nombre() {
		return municipio_nombre;
	}

	public void setMunicipio_nombre(String municipio_nombre) {
		this.municipio_nombre = municipio_nombre;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		PostalCode log = (PostalCode) o;

		return _id.equals(log._id);
	}

	@Override
	public int hashCode() {
		return _id.hashCode();
	}
}
