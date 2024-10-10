package com.zona_fit.gui;

import com.zona_fit.modelo.Cliente;
import com.zona_fit.servicio.ClienteServicio;
import com.zona_fit.servicio.IClienteServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

@Component
public class ZonaFitForma extends JFrame{
    private JPanel panelPrincipal;
    private JTable clientesTabla;
    private JTextField nameText;
    private JTextField surnameText;
    private JTextField privelegesText;
    private JButton savedButton;
    private JButton deletedButton;
    private JButton cleanButton;
    IClienteServicio clienteServicio;
    private DefaultTableModel tablaModeloClientes;
    private Integer idCliente;

    @Autowired
    public ZonaFitForma(ClienteServicio clienteServicio){
        this.clienteServicio = clienteServicio;
        iniForm();
        savedButton.addActionListener(e -> savedClient());
        clientesTabla.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                cargarClienteSeleccionado();
            }
        });
        deletedButton.addActionListener(e -> deletedClient());
        cleanButton.addActionListener(e -> cleanForm());
    }

    private void iniForm(){
        setContentPane(panelPrincipal);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900,700);
        setLocationRelativeTo(null);//centra ventana
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        //this.tablaModeloClientes = new DefaultTableModel(0, 4);
        // Evitamos la edicion de los valores de las celdas de la tabla
        this.tablaModeloClientes = new DefaultTableModel(0,4){
            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };
        String[] cabeza = {"Id", "Name", "Surname", "Priveleges"};
        this.tablaModeloClientes.setColumnIdentifiers(cabeza);
        this.clientesTabla = new JTable(tablaModeloClientes);
        // Restringimos la seleccion de la tabla a un solo registro
        this.clientesTabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        // Cargar listado de clientes
        listClient();
    }

    private void listClient(){
        this.tablaModeloClientes.setRowCount(0);
        var clientes = this.clienteServicio.listClient();
        clientes.forEach(cliente -> {
            Object[] renglonCliente = {
                    cliente.getId(),
                    cliente.getName(),
                    cliente.getSurname(),
                    cliente.getPriveleges()
            };
            this.tablaModeloClientes.addRow(renglonCliente);
        });
    }

    private void savedClient(){
        if(nameText.getText().equals("")){
            showMessage("Type a name ");
            nameText.requestFocusInWindow();
            return;
        }
        if(privelegesText.getText().equals("")){
            showMessage("Write a privelegy ");
            privelegesText.requestFocusInWindow();
            return;
        }
        // Recuperamos los valores del formulario
        var name = nameText.getText();
        var surname = surnameText.getText();
        var priveleges = Integer.parseInt(privelegesText.getText());
        var cliente = new Cliente(this.idCliente, name, surname, priveleges);
        this.clienteServicio.savedClient(cliente);// insertar / modificar
        if(this.idCliente == null)
            showMessage("New Client added ");
        else
            showMessage("Updated Client ");
        cleanForm();
        listClient();
    }

    private void cargarClienteSeleccionado(){
        var renglon = clientesTabla.getSelectedRow();
        if(renglon != -1){ // -1 significa que no selecciono ningun registro
            var id = clientesTabla.getModel().getValueAt(renglon, 0).toString();
            this.idCliente = Integer.parseInt(id);
            var name = clientesTabla.getModel().getValueAt(renglon, 1).toString();
            this.nameText.setText(name);
            var surname = clientesTabla.getModel().getValueAt(renglon, 2).toString();
            this.surnameText.setText(surname);
            var priveleges = clientesTabla.getModel().getValueAt(renglon, 3).toString();
            this.privelegesText.setText(priveleges);
        }
    }

    private void deletedClient(){
        var renglon = clientesTabla.getSelectedRow();
        if(renglon != -1){
            var idClienteStr = clientesTabla.getModel().getValueAt(renglon,0).toString();
            this.idCliente = Integer.parseInt(idClienteStr);
            var cliente = new Cliente();
            cliente.setId(this.idCliente);
            clienteServicio.deletedClient(cliente);
            showMessage("Cliente con id " + this.idCliente + " eliminado");
            cleanForm();
            listClient();
        }
        else
            showMessage("You must select a Client to delete ");

    }

    private void cleanForm(){
        nameText.setText("");
        surnameText.setText("");
        privelegesText.setText("");
        // Limpiamos el id cliente seleccionado
        this.idCliente = null;
        // Deseleccionamos el registro seleccionado de la tabla
        this.clientesTabla.getSelectionModel().clearSelection();
    }

    private void showMessage(String mensaje){
        JOptionPane.showMessageDialog(this, mensaje);
    }
}
