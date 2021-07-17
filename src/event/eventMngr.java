
package event;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.*;
import java.io.*;
import java.util.StringTokenizer;

import javax.swing.*;

import Aplicacion.Panel;
import clasesBase.*;

public class eventMngr implements ActionListener{
	private Panel panel;
	private JFileChooser escoger;
	
	public eventMngr(Panel pan) {
		panel = pan;
		escoger = new JFileChooser();
	}
	
	public void actionPerformed(ActionEvent e) {
		
		//evento para boton de guardar archivo
		if(e.getSource() == panel.getBtnGuardarArchivo()) {
			
			escoger.setDialogTitle("");
			int seleccion = escoger.showSaveDialog(panel.getTxtEscribir());
			if(seleccion == escoger.APPROVE_OPTION) {
				
				String guardado = escoger.getSelectedFile().getName();
				String direccion = escoger.getCurrentDirectory().toString();
				
				try {
					FileWriter archivoEs = new FileWriter(direccion + "/" + guardado + ".txt");
					PrintWriter escribir = new PrintWriter(archivoEs);
					String contenido = panel.getTxtEscribir().getText();
					StringTokenizer tokenizar = new StringTokenizer(contenido,"\n");
					while(tokenizar.hasMoreTokens()) {
						
						String linea = tokenizar.nextToken();
						escribir.println(linea);
						
					}
					
					escribir.close();
					
				}catch(IOException z) {
					z.printStackTrace();
				}
			}
		}
		
		//evento para boton analizar
		if(e.getSource() == panel.getBtnAnalizar()) {

			Palabritas palabritas = new Palabritas(panel.getTxtEscribir().getText());

			palabritas.analizador();
			panel.getTxtResultado().setText(palabritas.getErrorL());
			
			if(panel.getTpnConsolaTabla().getTabCount()> 1) {
				panel.getTpnConsolaTabla().removeTabAt(1);
					if(panel.getTpnConsolaTabla().getTabCount()>1)
						panel.getTpnConsolaTabla().removeTabAt(1);
			}
			//JTable tablaSimbolos = new JTable(palabritas.semantico.filas,palabritas.semantico.columnas);
			JScrollPane contiene1;

			contiene1 = new JScrollPane(new JTable(palabritas.semantico.filas,palabritas.semantico.columnas));


			panel.getTpnConsolaTabla().add("Tabla Simbolos",contiene1);
			
			

		
			
		}
		
		//evento para boton salir
		if(e.getSource() == panel.getBtnSalir()) {
			System.exit(0);
		}
		//Evento boton para abrir archivo
		if(e.getSource() == panel.getBtnAbrirArchivo()) {
			
			int lineas = 1;
			
			if(panel.getTpnConsolaTabla().getTabCount()> 1)
				panel.getTpnConsolaTabla().removeTabAt(1);
			
			escoger.setDialogTitle("");
			int seleccion = escoger.showOpenDialog(panel.getTxtEscribir());
			
			if(seleccion == escoger.APPROVE_OPTION) {
				String guardado = escoger.getSelectedFile().getName();
				String direccion = escoger.getCurrentDirectory().toString();
				try {
					FileReader lector = new FileReader(direccion+"/"+guardado);
					BufferedReader archivin = new BufferedReader(lector);
					String linea = archivin.readLine();
					StringTokenizer tokenizar;
					panel.getTxtEscribir().setText("");
					lineas = 0;
					panel.getTxtRenglones().setText("");
					panel.getTxtResultado().setText("Building in process...");
					
					while(linea != null) {
						
						tokenizar = new StringTokenizer(linea,"\n");
						String linita = "";
						panel.getTxtRenglones().append((++lineas) +"\n");
						
						try {
							linita = tokenizar.nextToken();
						}catch(Exception z) {
							
						}
						
						panel.setLineas(lineas);
						panel.getTxtEscribir().append(linita+"\n");
						linea = archivin.readLine();
						
					}
					archivin.close();
				}catch(IOException z) {
					
				}
			}
		}
		
		if(e.getSource() == panel.getBtnModoObscuro()){
			ImageIcon icon = new ImageIcon(this.getClass().getResource("/Imagenes/obs.png"));
			if(!panel.isObs()) {
			panel.getBtnModoObscuro().setText("Modo Claro");
			icon = new ImageIcon(this.getClass().getResource("/Imagenes/claro.png"));
			
			panel.getTxtEscribir().setForeground(Color.white);
			panel.getTxtEscribir().setBackground(new Color(3,30,39));
			panel.setBackground(new Color(47,47,47));
			
			//botones
			panel.getBtnAbrirArchivo().setBackground(new Color(9,76,96));
			panel.getBtnAbrirArchivo().setForeground(Color.yellow);
			panel.getBtnAnalizar().setBackground(new Color(9,76,96));
			panel.getBtnAnalizar().setForeground(Color.yellow);
			panel.getBtnGuardarArchivo().setBackground(new Color(9,76,96));
			panel.getBtnGuardarArchivo().setForeground(Color.yellow);
			panel.getBtnSalir().setBackground(new Color(9,76,96));
			panel.getBtnSalir().setForeground(Color.yellow);
			panel.getBtnModoObscuro().setBackground(new Color(9,76,96));
			panel.getBtnModoObscuro().setForeground(Color.yellow);
			
			panel.getTxtRenglones().setForeground(Color.white);
			panel.getTxtRenglones().setBackground(new Color(3,30,39));
			
			panel.getTxtResultado().setForeground(Color.white);
			panel.getTxtResultado().setBackground(new Color(3,30,39));
			
			panel.getScbRenglones().setBackground(new Color(3,30,39));
			panel.getScbContiene().setBackground(new Color(3,30,39));
			panel.getScbContiene2().setBackground(new Color(3,30,39));
			
			panel.getScbContiene().getVerticalScrollBar().setBackground(new Color(3,30,39));
			panel.getScbContiene().getHorizontalScrollBar().setBackground(new Color(3,30,39));
			
			panel.getScbContiene2().getVerticalScrollBar().setBackground(new Color(3,30,39));
			panel.getScbContiene2().getHorizontalScrollBar().setBackground(new Color(3,30,39));
			
			panel.setObs(true);
		}else {
			panel.getTxtEscribir().setForeground(Color.black);
			panel.getTxtEscribir().setBackground(Color.white);
			panel.setBackground(Color.white);
			panel.getBtnAbrirArchivo().setBackground(Color.white);
			panel.getBtnAbrirArchivo().setForeground(Color.black);
			
			panel.getBtnAnalizar().setBackground(Color.white);
			panel.getBtnAnalizar().setForeground(Color.black);
			panel.getBtnGuardarArchivo().setBackground(Color.white);
			panel.getBtnGuardarArchivo().setForeground(Color.black);
			panel.getBtnSalir().setBackground(Color.white);
			panel.getBtnSalir().setForeground(Color.black);
			panel.getBtnModoObscuro().setBackground(Color.white);
			panel.getBtnModoObscuro().setForeground(Color.black);
			
			
			panel.getTxtRenglones().setForeground(Color.black);
			panel.getTxtRenglones().setBackground(Color.white);
			
			panel.getTxtResultado().setForeground(Color.black);
			panel.getTxtResultado().setBackground(Color.white);
			
			panel.getScbRenglones().setBackground(Color.white);
			panel.getScbContiene().setBackground(Color.white);
			panel.getScbContiene2().setBackground(Color.white);
			
			panel.getScbContiene().getVerticalScrollBar().setBackground(Color.white);
			panel.getScbContiene().getHorizontalScrollBar().setBackground(Color.white);
			
			panel.getScbContiene2().getVerticalScrollBar().setBackground(Color.white);
			panel.getScbContiene2().getHorizontalScrollBar().setBackground(Color.white);
			
			
			panel.getBtnModoObscuro().setText("Modo Oscuro");
			panel.setObs(false);
		}
			panel.getBtnModoObscuro().setIcon(icon);

		}
	}
	

}
