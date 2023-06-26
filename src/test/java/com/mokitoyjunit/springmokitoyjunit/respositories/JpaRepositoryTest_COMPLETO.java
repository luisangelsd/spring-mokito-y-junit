package com.mokitoyjunit.springmokitoyjunit.respositories;

import com.mokitoyjunit.springmokitoyjunit.models.DtoCuenta;
import com.mokitoyjunit.springmokitoyjunit.repositories.IRepositoryCuenta;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DataJpaTest
public class JpaRepositoryTest_COMPLETO {

    //-- Test: Repositorios
    //-- Aquí estamos probando directamente la Interfaz con JPA
    //-- Probamos los métodos directamente sin pasar por el Services

    @Autowired
    IRepositoryCuenta repositoryCuenta;


//------------------------------------------------------------------------------------------------

    @Test
    @DisplayName("findByID() - Encuentra la persona")
    @Order(1)
    void findByID2(){
        Optional<DtoCuenta> cuenta= repositoryCuenta.findById(1L);
        Assertions.assertTrue(cuenta.isPresent());
        Assertions.assertEquals(1,cuenta.get().getId());
        Assertions.assertEquals("persona 1",cuenta.get().getPersona());
        Assertions.assertEquals("1000.00",cuenta.get().getSaldo().toPlainString());
    }


    @Test
    @DisplayName("findByID() - No encuentra la persona")
    @Order(2)
    void findByID1(){
        Optional<DtoCuenta> cuenta= repositoryCuenta.findByPersona("persona inexistente");
        Assertions.assertEquals(false, cuenta.isPresent());
    }

//------------------------------------------------------------------------------------------------


    @Test
    @DisplayName("findAll() - Lista todas las cuentas con Valores")
    @Order(3)
    void findAll(){
        List<DtoCuenta> cuenta= repositoryCuenta.findAll();
        Assertions.assertTrue(!cuenta.isEmpty());
    }

    /* Nota: Solo puedes testear cuando no existan registros
    @Test
    @DisplayName("findAll() - Lista todas las cuentas con Valores")
    void findAllSinValores(){
        List<DtoCuenta> cuenta= repositoryCuenta.findAll();
        Assertions.assertTrue(cuenta.isEmpty());
    }*/

    //------------------------------------------------------------------------------------------------

    @Test
    @DisplayName("save() - Guardar Correctamente")
    @Order(4)
    void saveCorrectamente(){

        //-- Datos + Servicio
        DtoCuenta NewUser =new DtoCuenta("persona3",new BigDecimal("3000"));
        DtoCuenta user= this.repositoryCuenta.save(NewUser);

        //-- Test
        Assertions.assertNotNull(user);
        Assertions.assertEquals(3, user.getId());
        Assertions.assertEquals("persona3", user.getPersona());
        Assertions.assertEquals("3000", user.getSaldo().toPlainString());
    }


    @Test
    @DisplayName("save() - No Guardar Campos Incompletos")
    @Order(5)
    void saveIncorrectamente(){

        //-- Datos
        DtoCuenta newUser =new DtoCuenta();

        //-- Datos + Test
        Assertions.assertThrows(ConstraintViolationException.class,()->{
            DtoCuenta user= this.repositoryCuenta.save(newUser);
        });
    }


    //------------------------------------------------------------------------------------------------

    @Test
    @DisplayName("saveUpdate() - Actualizar Correctamente")
    @Order(6)
    void saveUpdate(){

        //-- Datos
        DtoCuenta user=new DtoCuenta(1L, "persona1 - editada", new BigDecimal("1000"));

        //-- Servicio
        DtoCuenta userActualizado=this.repositoryCuenta.save(user);

        //-- Test
        Assertions.assertNotNull(userActualizado);
        Assertions.assertEquals(1, userActualizado.getId());
        Assertions.assertEquals("persona1 - editada", userActualizado.getPersona());
        Assertions.assertEquals("1000", userActualizado.getSaldo().toPlainString());

    }

    @Test
    @DisplayName("saveUpdate() - No Actualiza: Compos Incompletos")
    @Order(7)
    void saveUpdateBadRequest(){

        //-- Datos
        DtoCuenta user=new DtoCuenta();

        //-- Servicio + Test
        Assertions.assertThrows(ConstraintViolationException.class,()->{
           DtoCuenta userEditado=this.repositoryCuenta.save(user);
        });



    }

    //------------------------------------------------------------------------------------------------

    @Test
    @DisplayName("deleteById() - Delete Correctamente")
    @Order(7)
    void deleteByIdOk(){
        this.repositoryCuenta.deleteById(1L);
    }

/* Nota: No pude testear ya que regresa und void
    @Test
    @DisplayName("deleteById() - Delete Incorrctamente")
    @Order(7)
    void deleteByIdError(){
        this.repositoryCuenta.deleteById(1L);
    }*/




}
