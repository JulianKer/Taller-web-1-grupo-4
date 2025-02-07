package com.tallerwebi.dominio;

import com.tallerwebi.dominio.entidades.Suscripcion;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.repositorio.RepositorioSuscripcion;
import com.tallerwebi.dominio.servicio.ServicioSuscripcion;
import com.tallerwebi.dominio.servicio.ServicioUsuario;
import com.tallerwebi.dominio.servicio.impl.ServicioSuscripcionImpl;
import com.tallerwebi.infraestructura.servicio.impl.ServicioEmail;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ServicioSuscripcionTest {

    private MockHttpServletRequest request = new MockHttpServletRequest();
    private ServicioUsuario servicioUsuario = mock(ServicioUsuario.class);
    private ServicioEmail servicioEmail = mock(ServicioEmail.class);
    private RepositorioSuscripcion repositorioSuscripcion = mock(RepositorioSuscripcion.class);
    private ServicioSuscripcion servicioSuscripcion = new ServicioSuscripcionImpl(servicioUsuario, servicioEmail, repositorioSuscripcion);

    @Test
    public void queSeObtengaElTipoDePagoDesconocido(){
        String paymentTypeEsperado =  "Medio de pago desconocido.";
        String medioDePagoObtenido = servicioSuscripcion.obtenerTipoDePago("bank_transfer");

        assertEquals(paymentTypeEsperado, medioDePagoObtenido);
    }

    @Test
    public void queMeDevuelvaElMedioDePagoCorrespondienteSiPagaConPlataDeSuCuentaDeMP(){
        String paymentTypeEsperado =  "Cuenta de Mercado Pago.";
        String medioDePagoObtenido = servicioSuscripcion.obtenerTipoDePago("account_money");

        assertEquals(paymentTypeEsperado, medioDePagoObtenido);
    }

    @Test
    public void queMeDevuelvaElMedioDePagoCorrespondienteSiPagaConTarjetaDeDebito(){
        String paymentTypeEsperado =  "Tarjeta de Debito.";
        String medioDePagoObtenido = servicioSuscripcion.obtenerTipoDePago("debit_card");

        assertEquals(paymentTypeEsperado, medioDePagoObtenido);
    }

    @Test
    public void queMeDevuelvaElMedioDePagoCorrespondienteSiPagaConTarjetaDeCredito(){
        String paymentTypeEsperado =  "Tarjeta de Credito.";
        String medioDePagoObtenido = servicioSuscripcion.obtenerTipoDePago("credit_card");

        assertEquals(paymentTypeEsperado, medioDePagoObtenido);
    }

    @Test
    public void queSePuedaVerificarEstadoDelPagoAprobado(){
        request.getSession().setAttribute("emailUsuario", "test@user.com");
        String status = "approved";
        String paymentId = "123456";
        String paymentType = "account_money";


        Usuario usuario = new Usuario();
        usuario.setEmail("test@user.com");
        when(servicioUsuario.buscarUsuarioPorEmail("test@user.com")).thenReturn(usuario);

        String parametroEsperado = "?mensaje=Suscripcion exitosa.";
        String parametroObtenido = servicioSuscripcion.verificarEstadoDelPago(request, status, paymentId, paymentType);

        assertEquals(parametroEsperado, parametroObtenido);
    }

    @Test
    public void queSePuedaVerificarEstadoDelPagoPendiente(){
        request.getSession().setAttribute("emailUsuario", "test@user.com");
        String status = "pending";
        String paymentId = "123456";
        String paymentType = "account_money";

        Usuario usuario = new Usuario();
        usuario.setEmail("test@user.com");
        when(servicioUsuario.buscarUsuarioPorEmail("test@user.com")).thenReturn(usuario);

        String parametroEsperado = "?mensaje=Estamos esperando que se realize el pago.";
        String parametroObtenido = servicioSuscripcion.verificarEstadoDelPago(request, status, paymentId, paymentType);

        assertEquals(parametroEsperado, parametroObtenido);
    }

    @Test
    public void queSePuedaVerificarEstadoDelPagoFallido(){
        request.getSession().setAttribute("emailUsuario", "test@user.com");
        String status = "failure";
        String paymentId = "123456";
        String paymentType = "account_money";

        Usuario usuario = new Usuario();
        usuario.setEmail("test@user.com");
        when(servicioUsuario.buscarUsuarioPorEmail("test@user.com")).thenReturn(usuario);

        String parametroEsperado = "?mensaje=Hubo un error al intentar procesar el pago. Vuelava a intentarlo.";
        String parametroObtenido = servicioSuscripcion.verificarEstadoDelPago(request, status, paymentId, paymentType);

        assertEquals(parametroEsperado, parametroObtenido);
    }

    @Test
    public void queMeDevuelvaVacioSiElEstadoDekPagoEsDesconocido(){
        request.getSession().setAttribute("emailUsuario", "test@user.com");
        String status = "rejected";
        String paymentId = "123456";
        String paymentType = "account_money";

        Usuario usuario = new Usuario();
        usuario.setEmail("test@user.com");
        when(servicioUsuario.buscarUsuarioPorEmail("test@user.com")).thenReturn(usuario);

        String parametroEsperado = "";
        String parametroObtenido = servicioSuscripcion.verificarEstadoDelPago(request, status, paymentId, paymentType);

        assertEquals(parametroEsperado, parametroObtenido);
    }

    @Test
    public void queSeObtenganLasSuscripciones(){
        List<Suscripcion> suscripciones = new ArrayList<>();

        Suscripcion suscripcion = new Suscripcion();
        Suscripcion suscripcion2 = new Suscripcion();
        Suscripcion suscripcion3 = new Suscripcion();

        suscripciones.add(suscripcion);
        suscripciones.add(suscripcion2);
        suscripciones.add(suscripcion3);

        when(repositorioSuscripcion.obtenerSuscripciones()).thenReturn(suscripciones);
        assertEquals(suscripciones, servicioSuscripcion.obtenerSuscripciones());
    }
}
