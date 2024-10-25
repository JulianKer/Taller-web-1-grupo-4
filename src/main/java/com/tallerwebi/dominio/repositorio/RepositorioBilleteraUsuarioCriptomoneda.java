package com.tallerwebi.dominio.repositorio;

import com.tallerwebi.dominio.entidades.BilleteraUsuarioCriptomoneda;
import com.tallerwebi.dominio.entidades.Criptomoneda;
import com.tallerwebi.dominio.entidades.Usuario;

public interface RepositorioBilleteraUsuarioCriptomoneda {
    BilleteraUsuarioCriptomoneda buscarBilleteraCriptoUsuario(Criptomoneda criptomoneda, Usuario usuario);

    void guardarBilletera(BilleteraUsuarioCriptomoneda billeteraNueva);

    void actualizarBilletera(BilleteraUsuarioCriptomoneda billetera);
}