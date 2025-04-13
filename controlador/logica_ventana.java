package controlador;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingWorker;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import vista.ventana;
import modelo.*;

//Definición de la clase logica_ventana que implementa tres interfaces para manejar eventos.
public class logica_ventana implements ActionListener, ListSelectionListener, ItemListener {
	private ventana delegado; // Referencia a la ventana principal que contiene la GUI.
	private String nombres, email, telefono, categoria=""; // Variables para almacenar datos del contacto.
	private persona persona; // Objeto de tipo persona, que representa un contacto.
	private List<persona> contactos; // Lista de objetos persona que representa todos los contactos.
	private boolean favorito = false; // Booleano que indica si un contacto es favorito.
	private DefaultTableModel modeloTabla;
	
	// Constructor que inicializa la clase y configura los escuchadores de eventos para los componentes de la GUI.
	public logica_ventana(ventana delegado) {
		  // Asigna la ventana recibida como parámetro a la variable de instancia delegado.
	    this.delegado = delegado;
	    this.modeloTabla = delegado.modeloTabla;
	    // Carga los contactos almacenados al inicializar.
	    cargarContactosRegistrados();
	    cargarContactosEnTabla(); //Cargar los contactos en la tabla
	    
	    //Vincular el boton con la accion 
	    this.delegado.btn_exportar.addActionListener(e -> exportarContactosCSV());

	    
	    // Registra los ActionListener para los botones de la GUI.
	    this.delegado.btn_add.addActionListener(this);
	    this.delegado.btn_eliminar.addActionListener(this);
	    this.delegado.btn_modificar.addActionListener(this);
	    // Registra los ListSelectionListener para la lista de contactos.
	    this.delegado.lst_contactos.addListSelectionListener(this);
	    // Registra los ItemListener para el JComboBox de categoría y el JCheckBox de favoritos.
	    this.delegado.cmb_categoria.addItemListener(this);
	    this.delegado.chb_favorito.addItemListener(this);
	    
	    // Agregar evento de teclado en los campos de entrada
	    this.delegado.txt_nombres.addKeyListener(new KeyAdapter() {
	    	@Override
	    	public void keyPressed(KeyEvent e) {
	    		if(e.getKeyCode() == KeyEvent.VK_ENTER) {
	    			agregarContacto();
	    		}
	    	}
	   
	    });
	    
	    this.delegado.txt_telefono.addKeyListener(new KeyAdapter() {
	    	@Override
	    	public void keyPressed(KeyEvent e) {
	    		if(e.getKeyCode() == KeyEvent.VK_ENTER) {
	    			agregarContacto();
	    		}
	    	}
	    });
	    
	    this.delegado.txt_email.addKeyListener(new KeyAdapter() {
	    	 @Override
	     	public void keyPressed(KeyEvent e) {
	     		if(e.getKeyCode() == KeyEvent.VK_ENTER) {
	     			agregarContacto();
	     		}
	     	}
	    });
	}

	
	// Método privado para inicializar las variables con los valores ingresados en la GUI.
	private void incializacionCampos() {
		// Obtiene el texto ingresado en los campos de nombres, email y teléfono de la GUI.
		nombres = delegado.txt_nombres.getText();
		email = delegado.txt_email.getText();
		telefono = delegado.txt_telefono.getText();
	}

	// Método privado para cargar los contactos almacenados desde un archivo.
	private void cargarContactosRegistrados() {
		 try {
		        // Lee los contactos almacenados utilizando una instancia de personaDAO.
		        contactos = new personaDAO(new persona()).leerArchivo();
		        DefaultListModel modelo = new DefaultListModel();
		        // Agrega cada contacto al modelo de la lista de contactos de la GUI.
		        for (persona contacto : contactos) {
		            modelo.addElement(contacto.formatoLista());
		        }
		        // Establece el modelo actualizado en la lista de contactos de la GUI.
		        delegado.lst_contactos.setModel(modelo);
		    } catch (IOException e) {
		        // Muestra un mensaje de error si ocurre una excepción al cargar los contactos.
		        JOptionPane.showMessageDialog(delegado, "Existen problemas al cargar todos los contactos");
		    }
	}
	
	//Nuevo método para cargar contactos en la JTable
	private void cargarContactosEnTabla() {
	    modeloTabla.setRowCount(0); //Limpiar la tabla antes de agregar nuevos datos.

	    for (int i = 0; i < contactos.size(); i++) {
	        if (i == 0 && contactos.get(i).getNombre().equalsIgnoreCase("Nombre")) {
	            continue; //Saltar la primera fila si es un encabezado
	        }

	        Object[] fila = { 
	            contactos.get(i).getNombre(),
	            contactos.get(i).getTelefono(),
	            contactos.get(i).getEmail(),
	            contactos.get(i).getCategoria(),
	            contactos.get(i).isFavorito() ? "Sí" : "No"
	        };
	        modeloTabla.addRow(fila);
	    }
	}


	// Método privado para limpiar los campos de entrada en la GUI y reiniciar variables.
	private void limpiarCampos() {
		// Limpia los campos de nombres, email y teléfono en la GUI.
	    delegado.txt_nombres.setText("");
	    delegado.txt_telefono.setText("");
	    delegado.txt_email.setText("");
	    // Reinicia las variables de categoría y favorito.
	    categoria = "";
	    favorito = false;
	    // Desmarca la casilla de favorito y establece la categoría por defecto.
	    delegado.chb_favorito.setSelected(favorito);
	    delegado.cmb_categoria.setSelectedIndex(0);
	    // Reinicia las variables con los valores actuales de la GUI.
	    incializacionCampos();
	    // Recarga los contactos en la lista de contactos de la GUI.
	    cargarContactosRegistrados();
	}

	// Método que maneja los eventos de acción (clic) en los botones.
	@Override
	public void actionPerformed(ActionEvent e) {
		incializacionCampos(); // Inicializa las variables con los valores actuales de la GUI.

	    // Verifica si el evento proviene del botón "Agregar".
	    if (e.getSource() == delegado.btn_add) {
	        // Verifica si los campos de nombres, teléfono y email no están vacíos.
	        if ((!nombres.equals("")) && (!telefono.equals("")) && (!email.equals(""))) {
	            // Verifica si se ha seleccionado una categoría válida.
	            if ((!categoria.equals("Elija una Categoria")) && (!categoria.equals(""))) {
	                // Crea un nuevo objeto persona con los datos ingresados y lo guarda.
	                persona = new persona(nombres, telefono, email, categoria, favorito);
	                new personaDAO(persona).escribirArchivo();
	                // Limpia los campos después de agregar el contacto.
	                limpiarCampos();
	                // Muestra un mensaje de éxito.
	                JOptionPane.showMessageDialog(delegado, "Contacto Registrado!!!");
	            } else {
	                // Muestra un mensaje de advertencia si no se ha seleccionado una categoría válida.
	                JOptionPane.showMessageDialog(delegado, "Elija una Categoria!!!");
	            }
	        } else {
	            // Muestra un mensaje de advertencia si algún campo está vacío.
	            JOptionPane.showMessageDialog(delegado, "Todos los campos deben ser llenados!!!");
	        }
	        actualizarEstadisticas();
	    } else if (e.getSource() == delegado.btn_eliminar) {
	        actualizarEstadisticas();
	    	// Lugar para implementar la funcionalidad de eliminar un contacto.
	    } else if (e.getSource() == delegado.btn_modificar) {
	        actualizarEstadisticas();
	    	// Lugar para implementar la funcionalidad de modificar un contacto.
	    }
	}

	//Metodo para agregar desde el teclado 
	private void agregarContacto() {
	    incializacionCampos(); // Obtener los datos ingresados

	    if (!nombres.equals("") && !telefono.equals("") && !email.equals("")) {
	        if (!categoria.equals("Elija una Categoria") && !categoria.equals("")) {
	            persona = new persona(nombres, telefono, email, categoria, favorito);
	            new personaDAO(persona).escribirArchivo();
	            limpiarCampos();
	            JOptionPane.showMessageDialog(delegado, "Contacto Registrado!!!");
	            actualizarEstadisticas();
	        } else {
	            JOptionPane.showMessageDialog(delegado, "Elija una categoría!!!");
	        }
	    } else {
	        JOptionPane.showMessageDialog(delegado, "Todos los campos deben estar llenos!!!");
	    }
	}

	
	// Método que maneja los eventos de selección en la lista de contactos.
	@Override
	public void valueChanged(ListSelectionEvent e) {
		// Obtiene el índice del elemento seleccionado en la lista de contactos.
	    int index = delegado.lst_contactos.getSelectedIndex();
	    // Verifica si se ha seleccionado un índice válido en la lista.
	    if (index != -1) {
	        // Si el índice es mayor que cero (no se seleccionó la primera fila),
	        // carga los detalles del contacto seleccionado.
	        if (index > 0) {
	            cargarContacto(index);
	        }
	    } 
	}

	// Método privado para cargar los datos del contacto seleccionado en los campos de la GUI.
	private void cargarContacto(int index) {
		// Establece el nombre del contacto en el campo de texto de nombres.
	    delegado.txt_nombres.setText(contactos.get(index).getNombre());
	    // Establece el teléfono del contacto en el campo de texto de teléfono.
	    delegado.txt_telefono.setText(contactos.get(index).getTelefono());
	    // Establece el correo electrónico del contacto en el campo de texto de correo electrónico.
	    delegado.txt_email.setText(contactos.get(index).getEmail());
	    // Establece el estado de favorito del contacto en el JCheckBox de favorito.
	    delegado.chb_favorito.setSelected(contactos.get(index).isFavorito());
	    // Establece la categoría del contacto en el JComboBox de categoría.
	    delegado.cmb_categoria.setSelectedItem(contactos.get(index).getCategoria());
	}

	
	// Método que maneja los eventos de cambio de estado en los componentes cmb_categoria y chb_favorito.
	@Override
	public void itemStateChanged(ItemEvent e) {
		// Verifica si el evento proviene del JComboBox de categoría.
	    if (e.getSource() == delegado.cmb_categoria) {
	        // Obtiene el elemento seleccionado en el JComboBox y lo convierte en una cadena.
	        categoria = delegado.cmb_categoria.getSelectedItem().toString();
	        // Actualiza la categoría seleccionada en la variable "categoria".
	    } else if (e.getSource() == delegado.chb_favorito) {
	        // Verifica si el evento proviene del JCheckBox de favorito.
	        favorito = delegado.chb_favorito.isSelected();
	        // Obtiene el estado seleccionado del JCheckBox y actualiza el estado de favorito en la variable "favorito".
	    }
	}
	
	//Metodo para actualizar las estadisticas
	private void actualizarEstadisticas() {
		int total = contactos.size();
		int favoritos = 0;
		int familia = 0, amigos = 0, trabajo = 0;
		
		for (modelo.persona contacto : contactos) {
			if (contacto.isFavorito()) {
				favoritos++;
			}
			switch (contacto.getCategoria()) {
				case "Familia" : familia++;
				break;
				case "Amigos" : amigos++;
				break;
				case "Trabajo" : trabajo++;
				break;
			}
		}
		//Formatear la distribucion de categorias
		String categorias = String.format("Familia: %d | Amigos: %d | Trabajo: %d", familia, amigos, trabajo);
		
		//Actualizar la interfaz de la Ventana 
		delegado.actualizarEstadisticas(total, favoritos, categorias);
	}
	
	//Metodos para exportar los contactos
	private void exportarContactosCSV() {
	    String archivo = "contactos_exportados.csv"; // Nombre del archivo

	    SwingWorker<Void, Integer> worker = new SwingWorker<>() {
	        @Override
	        protected Void doInBackground() {
	            try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivo))) {
	                writer.write("Nombre,Teléfono,Email,Categoría,Favorito");
	                writer.newLine();

	                int totalContactos = contactos.size();
	                for (int i = 0; i < totalContactos; i++) {
	                    String favoritoStr = contactos.get(i).isFavorito() ? "Sí" : "No";
	                    writer.write(String.format("%s,%s,%s,%s,%s",
	                        contactos.get(i).getNombre(),
	                        contactos.get(i).getTelefono(),
	                        contactos.get(i).getEmail(),
	                        contactos.get(i).getCategoria(),
	                        favoritoStr));
	                    writer.newLine();

	                    // 🔹 Actualizar barra de progreso
	                    publish((i + 1) * 100 / totalContactos);
	                    Thread.sleep(50); // Simulación de retraso para visualizar el avance
	                }
	            } catch (IOException | InterruptedException e) {
	                JOptionPane.showMessageDialog(delegado, "Error al exportar contactos.");
	            }
	            return null;
	        }

	        @Override
	        protected void process(List<Integer> chunks) {
	            int progreso = chunks.get(chunks.size() - 1);
	            delegado.barraProgreso.setValue(progreso);
	        }

	        @Override
	        protected void done() {
	            JOptionPane.showMessageDialog(delegado, "Exportación completada. Archivo: " + archivo);
	        }
	    };
	    worker.execute();
	}


	
}