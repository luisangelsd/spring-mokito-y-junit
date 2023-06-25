package com.mokitoyjunit.springmokitoyjunit.dtos;

import java.time.LocalDate;

public class DtoResponse {

    private String mensaje;
    private Boolean status;


    public DtoResponse(String mensaje, Boolean status) {
        this.mensaje = mensaje;
        this.status = status;
    }

    public DtoResponse(){}

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }


}
