package afamijas.model.dto;

import afamijas.model.Doc;
import afamijas.model.Project;
import afamijas.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ProjectDTO
{

    private String id;

    private String idworker;

    private String worker_fullname;

    private String nombre;

    private LocalDate fecha_presentacion;

    private LocalDate fecha_resolucion;

    private String plazo_ejecucion;

    private Boolean subvencion_concedida;

    private Double importe_solicitado;

    private Double importe_concedido;

    List<DocDTO> documentos;


    public ProjectDTO(Project project, User worker)
    {
        this.id = project.get_id();

        this.idworker = project.getIdworker();
        if(worker != null)this.worker_fullname = worker.getFullname();

        this.nombre = project.getNombre();
        this.fecha_presentacion = project.getFecha_presentacion();
        this.fecha_resolucion = project.getFecha_resolucion();
        this.plazo_ejecucion = project.getPlazo_ejecucion();
        this.subvencion_concedida = project.getSubvencion_concedida();
        this.importe_solicitado = project.getImporte_solicitado();
        this.importe_concedido = project.getImporte_concedido();
        this.documentos = new ArrayList<>();
        if(project.getDocumentos() != null)
            for(Doc doc : project.getDocumentos())
                this.documentos.add(new DocDTO(doc, null));
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdworker() {
        return idworker;
    }

    public void setIdworker(String idworker) {
        this.idworker = idworker;
    }

    public String getWorker_fullname() {
        return worker_fullname;
    }

    public void setWorker_fullname(String worker_fullname) {
        this.worker_fullname = worker_fullname;
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

    public List<DocDTO> getDocumentos() {
        return documentos;
    }

    public void setDocumentos(List<DocDTO> documentos) {
        this.documentos = documentos;
    }
}
