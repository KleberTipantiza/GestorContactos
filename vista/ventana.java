package vista;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import controlador.logica_ventana;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JList;

public class ventana extends JFrame {

	public JPanel contentPane; // Panel principal que contendrá todos los componentes de la interfaz.
	public JTextField txt_nombres; // Campo de texto para ingresar nombres.
	public JTextField txt_telefono; // Campo de texto para ingresar números de teléfono.
	public JTextField txt_email; // Campo de texto para ingresar direcciones de correo electrónico.
	public JTextField txt_buscar; // Campo de texto adicional.
	public JCheckBox chb_favorito; // Casilla de verificación para marcar un contacto como favorito.
	public JComboBox cmb_categoria; // Menú desplegable para seleccionar la categoría de contacto.
	public JButton btn_add; // Botón para agregar un nuevo contacto.
	public JButton btn_modificar; // Botón para modificar un contacto existente.
	public JButton btn_eliminar; // Botón para eliminar un contacto.
	public JList lst_contactos; // Lista para mostrar los contactos.
	public JScrollPane scrLista; // Panel de desplazamiento para la lista de contactos.
	//Etiquetas para el panel de contactos
	private JTabbedPane tabbedPane;
	private JPanel panelContactos;  
    private JPanel panelEstadisticas; 
	//Etiquetas para mostrar las estadisticas
    private JLabel lbl_totalContactos;
    private JLabel lbl_favoritos;
    private JLabel lbl_porCategoria;
	//Etiquetas para los elementos de la tabla
    private JTable tablaContactos;
    public DefaultTableModel modeloTabla;
    //Creacion de la variable para exportar
    public JButton btn_exportar;
    //Creacion de la variable para el JProgressBar
    public JProgressBar barraProgreso;
    
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		 // Invoca el método invokeLater de la clase EventQueue para ejecutar la creación de la interfaz de usuario en un hilo de despacho de eventos (Event Dispatch Thread).
	    EventQueue.invokeLater(new Runnable() {
	        public void run() {
	            try {
	                // Dentro de este método, se crea una instancia de la clase ventana, que es la ventana principal de la aplicación.
	                ventana frame = new ventana();
	                // Establece la visibilidad de la ventana como verdadera, lo que hace que la ventana sea visible para el usuario.
	                frame.setVisible(true);
	            } catch (Exception e) {
	                // En caso de que ocurra una excepción durante la creación o visualización de la ventana, se imprime la traza de la pila de la excepción.
	                e.printStackTrace();
	            }
	        }
	    });
	}

	/**
	 * Create the frame.
	 */
	public ventana() {
		setTitle("GESTION DE CONTACTOS"); // Establece el título de la ventana.
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Define el comportamiento al cerrar la ventana.
		setResizable(false); // Evita que la ventana sea redimensionable.
		setBounds(100, 100, 1026, 748); // Establece el tamaño y la posición inicial de la ventana.
		contentPane = new JPanel(); // Crea un nuevo panel de contenido.
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5)); // Establece un borde vacío alrededor del panel.
		
		setContentPane(contentPane); // Establece el panel de contenido como el panel principal de la ventana.
		contentPane.setLayout(null); // Configura el diseño del panel como nulo para posicionar manualmente los componentes.
		
		//Crear el JTabbedPane
		tabbedPane = new JTabbedPane();
		tabbedPane.setBounds(20, 20, 970, 700);
		
		//Crear los paneles para cada pestaña
		panelContactos = new JPanel();
		panelEstadisticas = new JPanel();
		
		//Asegurar que se pueda modificar la posicion manualmente
		panelContactos.setLayout(null);
		panelEstadisticas.setLayout(null);
		
		//Agregar pestañas al JTabbedPane
		tabbedPane.addTab("Contactos", panelContactos);
		tabbedPane.addTab("Estadisticas", panelEstadisticas);
		
		//JTabbed agregado a contentPane
		contentPane.add(tabbedPane);
		
		//Crear el modelo de la tabla con las columnas
		modeloTabla = new DefaultTableModel();
		modeloTabla.addColumn("Nombre");
		modeloTabla.addColumn("Telefono");
		modeloTabla.addColumn("Email");
		modeloTabla.addColumn("Categoria");
		modeloTabla.addColumn("Favorito");
		
		//Instanciar la tabla con el modelo
		tablaContactos = new JTable(modeloTabla);
		
		//Agregar la tabla dentro de un JScrollPane para desplazar
		JScrollPane scrollTabla = new JScrollPane(tablaContactos);
		scrollTabla.setBounds(50, 230, 800, 300);
		panelContactos.add(scrollTabla);
		
		//1. Creación y configuración de etiquetas para los campos de entrada.
		//2. Mover los componentes al "panel de contactos"
		JLabel lbl_etiqueta1 = new JLabel("NOMBRES:"); // Etiqueta para nombres.
		lbl_etiqueta1.setBounds(25, 41, 89, 13); // Define la posición y tamaño de la etiqueta.
		lbl_etiqueta1.setFont(new Font("Tahoma", Font.BOLD, 15)); // Configura la fuente de la etiqueta.
		panelContactos.add(lbl_etiqueta1); // Agrega la etiqueta al panel de contenido.
		
		JLabel lbl_etiqueta2 = new JLabel("TELEFONO:");
		lbl_etiqueta2.setBounds(25, 80, 89, 13);
		lbl_etiqueta2.setFont(new Font("Tahoma", Font.BOLD, 15));
		panelContactos.add(lbl_etiqueta2);
		
		JLabel lbl_etiqueta3 = new JLabel("EMAIL:");
		lbl_etiqueta3.setBounds(25, 122, 89, 13);
		lbl_etiqueta3.setFont(new Font("Tahoma", Font.BOLD, 15));
		panelContactos.add(lbl_etiqueta3);
		
		JLabel lbl_etiqueta4 = new JLabel("BUSCAR POR NOMBRE:");
		lbl_etiqueta4.setFont(new Font("Tahoma", Font.BOLD, 15));
		lbl_etiqueta4.setBounds(80, 628, 192, 13);
		panelContactos.add(lbl_etiqueta4);
		
		// Creación y configuración de campos de texto para ingresar nombres, teléfonos y correos electrónicos.
		txt_nombres = new JTextField(); // Campo de texto para nombres.
		txt_nombres.setBounds(150, 40, 300, 30); // Define la posición y tamaño del campo de texto.
		txt_nombres.setFont(new Font("Tahoma", Font.PLAIN, 15)); // Configura la fuente del campo de texto.
		panelContactos.add(txt_nombres); // Agrega el campo de texto al panel de contenido.
		txt_nombres.setColumns(10); // Establece el número de columnas para el campo de texto.
		
		txt_telefono = new JTextField();
		txt_telefono.setBounds(150, 80, 300, 30);
		txt_telefono.setFont(new Font("Tahoma", Font.PLAIN, 15));
		txt_telefono.setColumns(10);
		panelContactos.add(txt_telefono);
		
		txt_email = new JTextField();
		txt_email.setBounds(150, 120, 300, 30);
		txt_email.setFont(new Font("Tahoma", Font.PLAIN, 15));
		txt_email.setColumns(10);
		panelContactos.add(txt_email);
		
		txt_buscar = new JTextField();
		txt_buscar.setFont(new Font("Tahoma", Font.PLAIN, 15));
		txt_buscar.setColumns(10);
		txt_buscar.setBounds(270, 620, 500, 30);
		panelContactos.add(txt_buscar);
		
		// Creación y configuración de una casilla de verificación para indicar si un contacto es favorito.
		chb_favorito = new JCheckBox("CONTACTO FAVORITO"); // Casilla de verificación.
		chb_favorito.setBounds(500, 40, 180, 30); // Define la posición y tamaño de la casilla de verificación.
		chb_favorito.setFont(new Font("Tahoma", Font.PLAIN, 15)); // Configura la fuente de la casilla de verificación.
		panelContactos.add(chb_favorito); // Agrega la casilla de verificación al panel de contenido.

		
		cmb_categoria = new JComboBox(); // Crea un nuevo JComboBox para permitir la selección de categorías.
		cmb_categoria.setBounds(500, 80, 180, 30); // Establece la posición y el tamaño del JComboBox en el panel.
		panelContactos.add(cmb_categoria); // Agrega el JComboBox al panel de contenido.

		// Arreglo que contiene las categorías disponibles.
		String[] categorias = {"Elija una Categoria", "Familia", "Amigos", "Trabajo"};
		for (String categoria : categorias) {
		    // Agrega cada categoría al JComboBox.
		    cmb_categoria.addItem(categoria);
		}

		btn_add = new JButton("AGREGAR"); // Crea un nuevo botón con el texto "AGREGAR".
		btn_add.setFont(new Font("Tahoma", Font.PLAIN, 15)); // Configura la fuente del botón.
		btn_add.setBounds(150, 170, 140, 40); // Establece la posición y el tamaño del botón en el panel.
		panelContactos.add(btn_add); // Agrega el botón al panel de contenido.
		
		btn_modificar = new JButton("MODIFICAR");
		btn_modificar.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btn_modificar.setBounds(310, 170, 140, 40);
		panelContactos.add(btn_modificar);
		
		btn_eliminar = new JButton("ELIMINAR");
		btn_eliminar.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btn_eliminar.setBounds(470, 170, 140, 40);
		panelContactos.add(btn_eliminar);
		
		lst_contactos = new JList(); // Crea una nueva JList para mostrar la lista de contactos.
		lst_contactos.setFont(new Font("Tahoma", Font.PLAIN, 15)); // Configura la fuente de la JList.
		lst_contactos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Establece el modo de selección a un solo elemento.
		lst_contactos.setBounds(50, 230, 800, 380); // Establece la posición y el tamaño de la JList en el panel.

		scrLista = new JScrollPane(lst_contactos); // Crea un JScrollPane para permitir el desplazamiento de la JList.
		scrLista.setBounds(50, 230, 800, 380); // Establece la posición y el tamaño del JScrollPane en el panel.
		panelContactos.add(scrLista); // Agrega el JScrollPane al panel de contenido.
		panelContactos.remove(scrLista); // Remover JList si no la necesitas

		
		//Agregar etiquetas a la pestaña de estadisticas
		lbl_totalContactos = new JLabel("Total de Contactos: ");
		lbl_totalContactos.setBounds(50, 50, 300, 25);
		lbl_totalContactos.setFont(new Font("Tahoma", Font.BOLD, 15));
		panelEstadisticas.add(lbl_totalContactos);
		
		lbl_favoritos = new JLabel("Contactos favoritos: ");
		lbl_favoritos.setBounds(50, 90, 300, 25);
		lbl_favoritos.setFont(new Font("Tahoma", Font.BOLD, 15));
		panelEstadisticas.add(lbl_favoritos);
		
		lbl_porCategoria = new JLabel("Contactos por categoria: ");
		lbl_porCategoria.setBounds(50, 130, 500, 25);
		lbl_porCategoria.setFont(new Font("Tahoma", Font.BOLD, 15));
		panelEstadisticas.add(lbl_porCategoria);
		
		//Agregar un boton para exportar los contactos
		btn_exportar = new JButton("EXPORTAR CSV");
		btn_exportar.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btn_exportar.setBounds(630, 170, 140, 40); // Ajuste de la ubicación
		panelContactos.add(btn_exportar);

		//Agregar barra de progreso
		barraProgreso = new JProgressBar(0, 100); // Rango de progreso de 0 a 100%
		barraProgreso.setBounds(50, 550, 800, 30);
		barraProgreso.setStringPainted(true); // Muestra el porcentaje dentro de la barra
		panelContactos.add(barraProgreso);
		barraProgreso.setValue(0); // Reiniciar al empezar
		barraProgreso.setVisible(true);
		

		
		//Instanciar el controlador para usar el delegado
		logica_ventana lv=new logica_ventana(this);
	}
	
	public void actualizarEstadisticas(int total, int favoritos, String categorias) {
		lbl_totalContactos.setText("Total de Contactos: " + total);
		lbl_favoritos.setText("Contactos favoritos: " + favoritos);
		lbl_porCategoria.setText("Contactos por categoria: " + categorias);
	}
	
}
