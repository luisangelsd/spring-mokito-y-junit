package com.mokitoyjunit.springmokitoyjunit.repositories;

import com.mokitoyjunit.springmokitoyjunit.models.DtoCuenta;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.PathVariable;


import java.util.List;
import java.util.Optional;

public interface IRepositoryCuenta extends JpaRepository<DtoCuenta, Long> {


    @Query(name = "select * from cuentas where nombre= :persona", nativeQuery = true)
    Optional<DtoCuenta> findByPersona(@PathVariable(name = "persona", required = true) String persona);


}
