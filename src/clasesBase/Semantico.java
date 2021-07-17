package clasesBase;

import javax.swing.*;
import java.lang.invoke.VarHandle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Semantico implements Tipo {
    private HashMap<String, TablaSimbolos> tablaSimbolos;
    public String[] columnas;
    public String[][] filas;
    private ArrayList<ArrayList<Token>> tokens;
    private String errorSemanticos = "";
    String errorcin = "";
    int fila = 0, column = 0;
    int esperado = 0;
    int noLlave = Integer.MAX_VALUE;

    public Semantico(ArrayList<ArrayList<Token>> tokens) {
        this.tokens = tokens;

        tablaSimbolos = new HashMap<String, TablaSimbolos>();
    }
    public HashMap<String, TablaSimbolos> getTablaSimbolos(){
        return  tablaSimbolos;
    }
    // Generar la tabla de simbolos
    public String generarTablaSimbolos(ArrayList<ArrayList<Token>> tokens) {


        columnas = new String[5];
        filas = new String[tokens.size()][5];

        String columnas[] = {"Nombre", "Tipo de Dato", "Posicion", "Valor"};
        this.columnas = columnas;


        for (int i = 0; i < tokens.size(); i++) {
            try {
                switch (tokens.get(i).get(0).getTipo()) {
                    case PUBLIC:
                        metodoDeclarado(tokens.get(i));
                        break;
                    case WHILE:
                        while_if(tokens.get(i));

                        break;
                    case IF:
                        while_if(tokens.get(i));


                        break;
                    case CLASS:

                        break;

                    case RETURN:
                        break;

                    default:
                        comprobarVariables(tokens.get(i));
                        if (i == (noLlave + 1)) {
                            noLlave = Integer.MAX_VALUE;

                        }
                        break;
                }
            } catch (IndexOutOfBoundsException e) {
                // Esto se ignora
            }
        }
        if (!errorcin.isEmpty()) errorSemanticos += "ERRORES SEMANTICOS ENCONTRADOS: \n" + errorcin;

        return errorSemanticos;


    }

    private void while_if(ArrayList<Token> while_if) {
        for (int i = 0; i < while_if.size(); i++) {
            if (while_if.get(i).getTipo() == PARENTESIS_A) {
                sonExpresiones("", while_if, i, false);
                break;
            }
        }
    }

    private void metodoDeclarado(ArrayList<Token> metodoDeclarado) {
        for (int i = 4; i < metodoDeclarado.size(); i++) {
            if (metodoDeclarado.get(i).getTipo() == LLAVE_A) ++esperado;


            if (metodoDeclarado.get(i).getTipo() != IDENT && metodoDeclarado.get(i).getTipo() != COMA && metodoDeclarado.get(i).getTipo() != LLAVE_A && metodoDeclarado.get(i).getTipo() != PARENTESIS_C) {
                if (!tablaSimbolos.containsKey(metodoDeclarado.get(i + 1).getValor()))
                    agregarValor(metodoDeclarado.get(i + 1).getValor(), metodoDeclarado.get(i).getValor(), metodoDeclarado.get(i).getRenglon(), "");
                else
                    errorcin += "La variable " + metodoDeclarado.get(i + 1).getValor() + " en la linea " + metodoDeclarado.get(i).getRenglon() + " ya ha sido declarada en la linea  " + tablaSimbolos.get(metodoDeclarado.get(i + 1).getValor()).getPosicion() + "\n";


            } else if (metodoDeclarado.get(i).getTipo() == PARENTESIS_C)
                break;


        }
    }

    //validadando que sean numeros enteros y float
    private void sonNumeros(String variable, ArrayList<Token> numeros, int i, int tipoVariable, String tipo) {
        boolean pasoIgual = false, error = false;
        String valor = "";
        int renglon = numeros.get(i).getRenglon();
        for (; i < numeros.size(); i++) {

            if (!pasoIgual) {
                pasoIgual = numeros.get(i).getTipo() == IGUAL;
            }
            if (numeros.get(i).getTipo() != PUNTO_COMA && numeros.get(i).getTipo() != IGUAL) {
                valor += " " + numeros.get(i).getValor();
                if (numeros.get(i).getTipo() != SUMA && numeros.get(i).getTipo() != RESTA && numeros.get(i).getTipo() != POR && numeros.get(i).getTipo() != DIV && numeros.get(i).getTipo() != PARENTESIS_A && numeros.get(i).getTipo() != PARENTESIS_C) {
                    if (numeros.get(i).getTipo() == IDENT) {
                        if (tablaSimbolos.containsKey(numeros.get(i).getValor())) {

                            String tipoVariableStr = "";
                            switch (tipoVariable) {
                                case INT:
                                    tipoVariableStr = "int";
                                    break;
                                case FLOAT:
                                    tipoVariableStr = "float";
                                    break;
                                default:
                                    tipoVariableStr = "boolean";
                                    break;
                            }

                            //System.out.println("-" + tablaSimbolos.get(numeros.get(i).getValor()).getTipoDato() + " " + tipoVariableStr);
                            if (!tablaSimbolos.get(numeros.get(i).getValor()).getTipoDato().equals(tipoVariableStr)) {
                                errorcin += "¡Error semantico!... en la linea " + numeros.get(i).getRenglon() + " no corresponde el tipo de la variable " + numeros.get(i).getValor() + " con el de la variable " + variable + ". \n";
                                error = true;
                            }
                        } else {
                            errorcin += "¡Error semantico!...la variable " + numeros.get(i).getValor() + " en la linea " + numeros.get(i).getRenglon() + " no ha sido declarada\n";
                            error = true;
                        }
                    } else {

                        if (!numeros.get(i).getValor().matches("[0-9]+([0-9])*") && tipoVariable == INT) {
                            errorcin += "¡Error semantico!...en la linea " + numeros.get(i).getRenglon() + " ya que " + numeros.get(i).getValor() + " no es un entero\n";
                            error = true;
                        } else {
                            if (!numeros.get(i).getValor().matches("(([0-9]+([0-9])*)*[.])?([0-9]+([0-9])*)+") && (tipoVariable == FLOAT)) {
                                errorcin += "¡Error semantico!... en la linea " + numeros.get(i).getRenglon() + " ya que " + numeros.get(i).getValor() + " no es un valor flotante\n";
                                error = true;
                            } else {
                                if(numeros.get(i).getTipo() == MENOR || numeros.get(i).getTipo() == AND ){
                                    errorcin += "¡Error semantico!...en la linea " + numeros.get(i).getRenglon() + " no se pueden usar operadores lógicos en tipos de variables númerica\n";
                                    error = true;

                                }
                            }
                        }
                    }
                }
            }

        }
        if (!error) agregarValor(variable, tipo, renglon, valor);
    }

    //validando que la variable corresponda a un entero, flotante o booleano
    private void comprobarVariables(ArrayList<Token> variables) {
        String variable = "";
        if (variables.get(0).getTipo() == INT || variables.get(0).getTipo() == FLOAT || variables.get(0).getTipo() == BOOLEAN) {
            variable = variables.get(1).getValor();
            if (tablaSimbolos.containsKey(variables.get(1).getValor())) {
                errorcin += "La variable " + variables.get(1).getValor() + " en la linea " + variables.get(1).getRenglon() + " ya ha sido declarada en la linea  " + tablaSimbolos.get(variables.get(1).getValor()).getPosicion() + "\n";
            } else {
                switch (variables.get(0).getTipo()) {
                    case INT:
                        sonNumeros(variable, variables, 3, variables.get(0).getTipo(), "int");
                        break;
                    case FLOAT:
                        sonNumeros(variable, variables, 3, variables.get(0).getTipo(), "float");
                        break;
                    default:
                        sonExpresiones(variable, variables, 3, true);
                        break;
                }

            }
        } else {
            variable = variables.get(0).getValor();
            if (!tablaSimbolos.containsKey(variables.get(0).getValor())) {
                errorcin += "La variable " + variables.get(0).getValor() + " en la linea " + variables.get(1).getRenglon() + " no ha sido declarada\n";

            } else {
                System.out.println(" AIUDA");
                if (tablaSimbolos.get(variables.get(0).getValor()).getTipoDato().equals("int") || tablaSimbolos.get(variables.get(0).getValor()).getTipoDato().equals("float")) {
                    if (tablaSimbolos.get(variables.get(0).getValor()).getTipoDato().equals("int")) {
                        sonNumeros(variable, variables, 2, INT, tablaSimbolos.get(variables.get(0).getValor()).getTipoDato());
                    } else {
                        System.out.println(" AIUDA");
                        sonNumeros(variable, variables, 2, FLOAT, tablaSimbolos.get(variables.get(0).getValor()).getTipoDato());
                    }
                } else {
                    sonExpresiones(variable, variables, 2, true);
                }
            }
        }
    }


    private void sonExpresiones(String variable, ArrayList<Token> expresiones, int i, boolean asign) {
        boolean error = false, operandoNum = true, operandoLog = true, varNumerica = true, varLogica = true, primeraVuelta = true;
        String tipoDato = "boolean", valor = "";
        String tipo = "", exp = "";
        int numeros = 0;
        int renglon = expresiones.get(i).getRenglon();

        for (; i < expresiones.size(); i++) {
            if (expresiones.get(i).getTipo() != PUNTO_COMA) {
                valor += " " + expresiones.get(i).getValor();

                if (expresiones.get(i).getTipo() != PARENTESIS_A && expresiones.get(i).getTipo() != PARENTESIS_C) {
                    if (expresiones.get(i).getTipo() == IDENT) {
                        if (!tablaSimbolos.containsKey(expresiones.get(i).getValor())) {
                            // ERROR
                            errorcin += "Error Semantico! Se intento utilizar la variable " + expresiones.get(i).getValor() + " en la linea " + expresiones.get(i).getRenglon() + " y no se encuentra declada.\n";
                        } else {
                            //ANALISIS
                            tipo = tablaSimbolos.get(expresiones.get(i).getValor()).getTipoDato();

                            if (tipo.equals("boolean")) {
                                if (!varLogica && !primeraVuelta) {
                                    errorcin += "Error Semantico! Se intento utilizar la variable booleana " + expresiones.get(i).getValor() + " en la comparación numerica de la linea " + expresiones.get(i).getRenglon() + "\n.";
                                    varNumerica = !varNumerica;
                                    error = true;
                                } else {
                                    operandoLog = true;
                                    ++numeros;
                                }
                            } else {
                                if (!varNumerica && !primeraVuelta) {
                                    errorcin += "Error Semantico! Se intento utilizar la variable numerica " + expresiones.get(i).getValor() + " en la comparación booleana de la linea " + expresiones.get(i).getRenglon() + "\n.";
                                    varLogica = !varLogica;
                                    error = true;
                                } else operandoNum = true;
                            }
                        }
                    } else {
                        int analizarTipo = expresiones.get(i).getTipo();
                        if (analizarTipo == NUM || analizarTipo == DECIMAL) {
                            if (!varNumerica) {
                                errorcin += "Error Semantico! Se intento utilizar el valor numerico " + expresiones.get(i).getValor() + " en la comparación booleana de la linea " + expresiones.get(i).getRenglon() + "\n.";
                                varLogica = false;
                                error = true;
                            } else {
                                varNumerica = false;
                                if (numeros == -55) {
                                    operandoLog = true;
                                } else {
                                    operandoNum = true;
                                    ++numeros;
                                }

                            }
                        } else {
                            // operandos
                            if (analizarTipo == SUMA || analizarTipo == RESTA || analizarTipo == POR || analizarTipo == DIV || analizarTipo == MENOR) {
                                if (operandoNum) {
                                    varNumerica = true;
                                    numeros = -55;
                                } else {
                                    errorcin += "Error Semantico! Se intento utilizar el operando numerico " + expresiones.get(i).getValor() + " en la comparación booleana de la linea " + expresiones.get(i).getRenglon() + "\n.";
                                    operandoLog = false;
                                    error = true;
                                }
                            } else {
                                if (analizarTipo == TRUE || analizarTipo == FALSE) {
                                    if (varLogica) {
                                        varLogica = !varLogica;
                                        operandoLog = true;
                                    } else {
                                        errorcin += "Error Semantico! Se intento utilizar el valor booleano " + expresiones.get(i).getValor() + " en la comparación numerica de la linea " + expresiones.get(i).getRenglon() + "\n.";
                                        error = true;
                                    }
                                } else {
                                    if (operandoLog) {
                                        operandoLog = !operandoLog;
                                        varLogica = true;
                                        varNumerica = true;
                                    } else {
                                        errorcin += "Error Semantico! Se intento utilizar el operador logico " + expresiones.get(i).getValor() + " en la comparación numerica de la linea " + expresiones.get(i).getRenglon() + "\n.";
                                        operandoNum = false;
                                        varNumerica = true;
                                        error = true;
                                    }
                                }
                            }

                        }
                    }
                }

            }
            primeraVuelta = false;
        }
        if (!error && asign) agregarValor(variable, tipoDato, renglon, valor);
    }


    // En éste método se agrega el valor a las filas de el código analizado
    private void agregarValor(String variable, String tipo, int renglon, String valor) {
        String[] array = {"1", "2", "3"};


        if (!tablaSimbolos.containsKey(variable)) {
            tablaSimbolos.put(variable, new TablaSimbolos(variable, tipo, renglon, valor, fila));
            filas[fila][0] = variable;
            filas[fila][1] = tipo;
            filas[fila][2] = renglon + "";
            filas[fila][3] = valor;

            fila++;
        } else {
            tablaSimbolos.get(variable).setValor(valor);
            filas[tablaSimbolos.get(variable).getFila()][3] = valor;
        }


    }
}