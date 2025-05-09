package afamijas.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(collection = "projects")
public class Project
{
    @Id
    private String _id;

    private String idworker;

    private String nombre;

    private LocalDate fecha_presentacion;

    private LocalDate fecha_resolucion;

    private String plazo_ejecucion;

    private Boolean subvencion_concedida;

    private Double importe_solicitado;

    private Double importe_concedido;

    List<Doc> documentos;

    private String status;

    private LocalDateTime created;

    public Project() { this.created = LocalDateTime.now(); }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public LocalDate getFecha_presentacion() {
        return fecha_presentacion;
    }

    public void setFecha_presentacion(LocalDate fecha_presentacion) {
        this.fecha_presentacion = fecha_presentacion;
    }

    public LocalDate getFecha_resolucion() {
        return fecha_resolucion;
    }

    public void setFecha_resolucion(LocalDate fecha_resolucion) {
        this.fecha_resolucion = fecha_resolucion;
    }

    public String getPlazo_ejecucion() {
        return plazo_ejecucion;
    }

    public void setPlazo_ejecucion(String plazo_ejecucion) {
        this.plazo_ejecucion = plazo_ejecucion;
    }

    public Boolean getSubvencion_concedida() {
        return subvencion_concedida;
    }

    public void setSubvencion_concedida(Boolean subvencion_concedida) {
        this.subvencion_concedida = subvencion_concedida;
    }

    public Double getImporte_solicitado() {
        return importe_solicitado;
    }

    public void setImporte_solicitado(Double importe_solicitado) {
        this.importe_solicitado = importe_solicitado;
    }

    public Double getImporte_concedido() {
        return importe_concedido;
    }

    public void setImporte_concedido(Double importe_concedido) {
        this.importe_concedido = importe_concedido;
    }

    public List<Doc> getDocumentos() {
        return documentos;
    }

    public void setDocumentos(List<Doc> documentos) {
        this.documentos = documentos;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public String getIdworker() {
        return idworker;
    }

    public void setIdworker(String idworker) {
        this.idworker = idworker;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Project log = (Project) o;

        return _id.equals(log._id);
    }

    @Override
    public int hashCode() {
        return _id.hashCode();
    }
}
