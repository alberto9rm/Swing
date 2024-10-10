package com.zona_fit;

import gm.zona_fit.modelo.Cliente;
import gm.zona_fit.servicio.IClienteServicio;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
import java.util.Scanner;

//@SpringBootApplication
public class ZonaFitApplication implements CommandLineRunner {

	@Autowired
	private IClienteServicio clienteServicio;

	private static final Logger logger =
			LoggerFactory.getLogger(ZonaFitApplication.class);

	String nl = System.lineSeparator();

	public static void main(String[] args) {
		logger.info("Starting the App");
		// Levantar la fabrica de spring
		SpringApplication.run(ZonaFitApplication.class, args);
		logger.info("Finished App!!!");
	}

	@Override
	public void run(String... args) throws Exception {
		zonaFitApp();
	}

	private void zonaFitApp(){
		var Exit = false;
		var console = new Scanner(System.in);
		while(!Exit){
			var opcion = mostraMenu(console);
			Exit = ejecutarOpciones(console, opcion);
			logger.info(nl);
		}
	}

	private int mostraMenu(Scanner console){
		logger.info("""
        \n*** App Zona Fit (GYM) ***
		1. List Client
		2. Search Client
		3. Create Client
		4. Update Client
		5. Deleted Client
		6. Exit
     	Select an option:\s""");
		return Integer.parseInt(console.nextLine());
	}

	private boolean ejecutarOpciones(Scanner console, int opcion){
		var Exit = false;
		switch (opcion){
			case 1 -> {
				logger.info(nl + "--- List of Client ---" + nl);
				List<Cliente> clientes = clienteServicio.listarClientes();
				clientes.forEach(cliente -> logger.info(cliente.toString() + nl));
			}
			case 2 -> {
				logger.info(nl + "--- Search Client for Id ---" + nl);
				logger.info("Id Cliente search: ");
				var idCliente = Integer.parseInt(console.nextLine());
				Cliente cliente = clienteServicio.buscarClientePorId(idCliente);
				if(cliente != null)
					logger.info("Client found: " + cliente + nl);
				else
					logger.info("Client NO found: " + cliente + nl);
			}
			case 3 -> {
				logger.info("--- Create Client ---" + nl);
				logger.info("Name: ");
				var name = console.nextLine();
				logger.info("Surname: ");
				var surname = console.nextLine();
				logger.info("Priveleges: ");
				var priveleges = Integer.parseInt(console.nextLine());
				var cliente = new Cliente();
				cliente.setname(name);
				cliente.setsurname(surname);
				cliente.setpriveleges(priveleges);
				clienteServicio.guardarCliente(cliente);
				logger.info("Client created: " + cliente + nl);
			}

			case 4 -> {
				logger.info("--- Update Client ---" + nl);
				logger.info("Id Cliente: ");
				var idCliente = Integer.parseInt(console.nextLine());
				Cliente cliente = clienteServicio.buscarClientePorId(idCliente);
				if(cliente != null){
					logger.info("Name: " );
					var name = console.nextLine();
					logger.info("Surname: ");
					var surname = console.nextLine();
					logger.info("Priveleges: ");
					var priveleges = Integer.parseInt(console.nextLine());
					cliente.setName(name);
					cliente.setSurname(surname);
					cliente.setPriveleges(priveleges);
					clienteServicio.savedClient(cliente);
					logger.info("Modified client: " + cliente + nl);
				}
				else
					logger.info("Client NOT found: " + cliente + nl);
			}
			case 5 -> {
				logger.info("--- Deleted Client ---" + nl);
				logger.info("Id Cliente: ");
				var idCliente = Integer.parseInt(console.nextLine());
				var cliente = clienteServicio.buscarClientePorId(idCliente);
				if(cliente != null){
					clienteServicio.eliminarCliente(cliente);
					logger.info("Deleted client: " + cliente + nl);
				}
				else
					logger.info("Client NOT found: " + cliente + nl);
			}
			case 6 -> {
				logger.info("See you!!!" + nl + nl);
				Exit = true;
			}
			default -> logger.info("Option NO recognized: " + opcion + nl);
		}
		return Exit;
	}
}
