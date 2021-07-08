package clasesBase;

public class TablaSimbolos {


	private String nombre;
	private String tipoDato;
	private int posicion;
	private String valor;
	private int fila;
	public TablaSimbolos() {
		
	}
	
	public TablaSimbolos(String nom, String tipo, int pos, String val, int fila) {
		nombre = nom;
		tipoDato = tipo;
		posicion = pos;
		valor = val;
		this.fila = fila;
	}



	public String getNombre() {
		return nombre;
	}

	public int getFila(){return fila; }
	public void setNombre(String nom) {
		nombre = nom;
	}
	
	public String getTipoDato() {
		return tipoDato;
	}
	
	public void setTipoDato(String tipo) {
		tipoDato = tipo;
	} 
	
	public int getPosicion() {
		return posicion;
	}
	
	public void setPosicion(int pos) {
		posicion = pos;
	} 
	
	public String getValor() {
		return valor;
	}
	
	public void setValor(String val) {
		valor = val;
	} 
	
}
