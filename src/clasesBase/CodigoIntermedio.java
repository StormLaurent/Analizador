package clasesBase;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

public class CodigoIntermedio {
    private HashMap<String, TablaSimbolos> tablaSimbolos;
    private ArrayList<String>posfija;
    private ArrayList<String>operador;

    public CodigoIntermedio( HashMap<String, TablaSimbolos> tbSimbolos){
        this.tablaSimbolos = tbSimbolos;
        posfija = new ArrayList<>();
        operador = new ArrayList<>();
    }
    public void validarOperacion(){
        for(String clave: tablaSimbolos.keySet()){
            if(!tablaSimbolos.get(clave).getTipoDato().equals("boolean")){
                String expresion = tablaSimbolos.get(clave).getValor();
                System.out.println(expresion);
                posfijo(tablaSimbolos.get(clave).getNombre(),expresion);
                System.out.println(posfija);

            }
        }
    }
    public void posfijo(String var, String expresion) {
        posfija = new ArrayList<>();
        operador.add("");
        StringTokenizer exp = new StringTokenizer(expresion, " ");
        posfija.add("");
        boolean op = false;
        int countTokens = 0;

        while(exp.hasMoreTokens()) {
            ++countTokens;
            String token = exp.nextToken();
            System.out.println(token);
            if(token.matches("[0-9]+([0-9])*") || token.matches("[a-zA-Z]+([a-zA-Z0-9])*")|| token.matches("(([0-9]+([0-9])*)*[.])?([0-9]+([0-9])*)+")) {
                posfija.set(posfija.size()-1, posfija.get(posfija.size()-1) + " " + token);
                op = false;
            }else {
                if(countTokens == 1 && token.equals("-")) {
                    posfija.set(posfija.size()-1, posfija.get(posfija.size()-1) + " " + "0");
                }
                if(token.equals("(")) {
                    operador.add(token);
                    op = false;
                }else {
                    if(token.equals(")")){
                        int i = operador.size()-1;
                        while(!operador.get(i).equals("(")) {
                            posfija.set(posfija.size()-1, posfija.get(posfija.size()-1) + " " + operador.remove(i));
                            i--;
                        }
                        operador.remove(i);
                    }else {
                        if(token.equals("*"))
                            op = true;
                        if(token.equals("-")&&op) {
                            posfija.set(posfija.size()-1, posfija.get(posfija.size()-1) + " " + "0");
                            operador.add(token);
                        }else {
                            if(importanciaOperador(elemento()) == importanciaOperador(token)) {
                                posfija.set(posfija.size()-1, posfija.get(posfija.size()-1) + " " + operador.remove(operador.size()-1));
                                operador.add(token);
                            }else {
                                if(importanciaOperador(token) > importanciaOperador(elemento())) {
                                    operador.add(token);

                                }else {
                                    int i = operador.size()-1;
                                    while(!operador.isEmpty()) {
                                        posfija.set(posfija.size()-1, posfija.get(posfija.size()-1) + " " + operador.remove(i));
                                        i--;
                                    }

                                    operador.add(token);
                                }
                            }
                        }

                    }
                }
            }
        }
        int i = operador.size()-1;
        while(!operador.isEmpty()) {
            posfija.set(posfija.size()-1, posfija.get(posfija.size()-1) + " " + operador.remove(i));
            i--;
        }

    }
    public int importanciaOperador(String token){
        switch (token){
            case "*":
            case "/":
                return 1;
            case "+":
            case "-":
                return 0;
        }
        return 0;
    }
    public String elemento(){
        if(!operador.isEmpty())
            return operador.get(operador.size()-1);
        else
        return "";
    }
    public void evaluarExprecion(){
        String posfijo = posfija.get(posfija.size()-1);
        StringTokenizer evaluar = new StringTokenizer(posfijo," ");
        String tokens;
        ArrayList<String> numeros = new ArrayList<>();
        ArrayList<ArrayList<String>> tabla = new ArrayList<>();

        while(evaluar.hasMoreTokens()) {
            tokens = evaluar.nextToken();
            if (tokens.matches("[0-9]+([0-9])*") || tokens.matches("[a-zA-Z]+([a-zA-Z0-9])*") || tokens.matches("(([0-9]+([0-9])*)*[.])?([0-9]+([0-9])*)+"))
                numeros.add(tokens);

            else {

            }




        }
    }

}
