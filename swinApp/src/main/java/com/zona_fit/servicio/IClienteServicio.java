package com.zona_fit.servicio;

import com.zona_fit.modelo.Cliente;
import java.util.List;

public interface IClienteServicio {
    public List<Cliente> listClient();

    public Cliente buscarClientePorId(Integer idCliente);

    public void savedClient(Cliente cliente);

    public void deletedClient(Cliente cliente);

}
