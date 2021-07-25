package clasesBase;


import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

public class CodigoIntermedio {
    private HashMap<String, TablaSimbolos> tablaSimbolos;
    private ArrayList<String>posfija;
    private ArrayList<String>operador;
    private String cuadruplo = "<html> <body>";
    private ArrayList<String> temporales;
    private ArrayList<String> variables;
    private ArrayList<ArrayList<String>> cuadruplos;
    private ArrayList<String[]> declar_Var;

    int count = 0;
    private String[][] filas;
    public CodigoIntermedio( HashMap<String, TablaSimbolos> tbSimbolos, String[][] filas){
        this.tablaSimbolos = tbSimbolos;
        posfija = new ArrayList<>();
        operador = new ArrayList<>();
        temporales = new ArrayList<>();
        variables = new ArrayList<>();
        cuadruplos = new ArrayList<>();
        declar_Var = new ArrayList<>();
        this.filas = filas;
    }
    public void validarOperacion(){
        for(String clave: tablaSimbolos.keySet()){
            if(!tablaSimbolos.get(clave).getTipoDato().equals("boolean")){
                String expresion = tablaSimbolos.get(clave).getValor();
                posfijo(tablaSimbolos.get(clave).getNombre(),expresion);
                declar_Var.add(new String[3]);
                declar_Var.get(declar_Var.size()-1)[0] =clave;
                declar_Var.get(declar_Var.size()-1)[2] =tablaSimbolos.get(clave).getTipoDato();
                evaluarExpresion(clave+" = "+expresion,clave, tablaSimbolos.get(clave).getTipoDato().equals("int"));
                posfija.clear();
            }
        }
    }
    // a = 10+2*3/4;
    public ArrayList<String[]> getDeclaraciones(){
        return declar_Var;
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
    int i = 1;
    public void evaluarExpresion(String expresion,String variable, boolean entero){
        cuadruplo += "<font color='#7a3858'>=====================================================</font><br>";
        cuadruplo += " &nbsp; &nbsp; &nbsp;  &nbsp; &nbsp; &nbsp; <font color='#622a85'> Cuadruplo #"+(++count)+"</font><br>";
        cuadruplo += expresion+"<br>";
        cuadruplo += "<font color ='blue'><u><b>RESULTADO&nbsp; &nbsp; &nbsp; OPERADOR&nbsp; &nbsp; &nbsp;OPERANDO1&nbsp; &nbsp; &nbsp; OPERANDO2</b></u></font><br>";

        cuadruplos.add(new ArrayList<>());
        String posfijo = posfija.get(posfija.size()-1);
        StringTokenizer evaluar = new StringTokenizer(posfijo," ");
        String tokens;
        ArrayList<String> numeros = new ArrayList<>();
        ArrayList<ArrayList<String>> tabla = new ArrayList<>();

        int op1 = Integer.MIN_VALUE;
        int op2 = Integer.MAX_VALUE;
        double dop1 = Double.MIN_VALUE;
        double dop2 = Double.MAX_VALUE;
        ArrayList<String> temporales = new ArrayList<>();

        while(evaluar.hasMoreTokens()) {
            op1 = Integer.MIN_VALUE;
            op2 = Integer.MAX_VALUE;
            dop1 = Double.MIN_VALUE;
            dop2 = Double.MAX_VALUE;
            tokens = evaluar.nextToken();
            if (tokens.matches("[0-9]+([0-9])*") || tokens.matches("[a-zA-Z]+([a-zA-Z0-9])*") || tokens.matches("(([0-9]+([0-9])*)*[.])?([0-9]+([0-9])*)+")) {
                if(tokens.matches("[a-zA-Z]+([a-zA-Z0-9])*")){
                    numeros.add(tokens);
                }
                else{
                    numeros.add(tokens);
                }
            }
            else {
                String operando2 = "" , operando1 = "";
                String tVariable2 = numeros.remove(numeros.size()-1), tVariable = "";
                if(Pattern.compile("Salcido").matcher(tVariable2).find()) {
                    operando2 = temporales.remove(temporales.size()-1);
                }
                else {
                    if(tablaSimbolos.containsKey(tVariable2)){
                        operando2 = tablaSimbolos.get(tVariable2).getValor();
                    }else {
                        operando2 = tVariable2;
                    }
                }
                tVariable = numeros.remove(numeros.size()-1);
                if(Pattern.compile("Salcido").matcher(tVariable).find()){
                    operando1 = temporales.remove(temporales.size()-1);
                }
                else {
                    if(tablaSimbolos.containsKey(tVariable)){
                        operando1 = tablaSimbolos.get(tVariable).getValor();
                    }else {
                        operando1 = tVariable;
                    }
                }
                cuadruplo += "&nbsp; &nbsp;"+"Salcido"+i+"&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;  &nbsp;  &nbsp;  &nbsp; <font color = 'teal'>"+tokens+" </font> &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;  &nbsp;  &nbsp;  &nbsp;"+tVariable+"&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;  &nbsp;  &nbsp;  &nbsp;"+tVariable2+"&nbsp; &nbsp; &nbsp; &nbsp;"+"<br>";
                numeros.add("Salcido"+i);

                // rssultado operador   operando 2 operando1
                 getCuadruplos().add(numeros.get(numeros.size()-1));
                 getCuadruplos().add(tokens);
                 getCuadruplos().add(tVariable);
                 getCuadruplos().add(tVariable2);

                // size -1 ... 15, 14 0-14
                cuadruplos.get(cuadruplos.size()-1);
                declar_Var.add(new String[3]);
                declar_Var.get(declar_Var.size()-1)[0] = numeros.get(numeros.size()-1);
                declar_Var.get(declar_Var.size()-1)[2] = "0";
                declar_Var.get(declar_Var.size()-1)[3] = (entero)  ? "int" : "float" ;


                i++;

                if(entero){
                    try {
                         op1 = Integer.parseInt(operando1.strip());
                         op2 = Integer.parseInt(operando2.strip());
                         entero = true;
                    } catch(Exception e){

                    }
                } else {
                    try {
                         dop1 = Double.parseDouble(operando1);
                         dop2 = Double.parseDouble(operando2);
                    } catch(Exception e){

                    }
                }
                switch (tokens){

                    case "*":
                        if(entero){
                            temporales.add((int)(op1*op2)+"");
                        }
                        else
                            temporales.add((dop1*dop2)+"");
                        break;
                    case "/":
                        if(entero){
                            temporales.add((int)(op1/op2)+"");
                        }
                        else
                            temporales.add((dop1/dop2)+"");
                        break;
                    case "+":
                        if(entero){
                            temporales.add((int)(op1+op2)+"");
                        }
                        else
                            temporales.add((dop1+dop2)+"");
                        break;
                    case "-":
                        if(entero){
                            temporales.add((int)(op1-op2)+"");
                        }
                        else
                            temporales.add((dop1-dop2)+"");
                        break;
                }
            }
        }
        if(temporales.size()!= 0) {
            String valor = temporales.remove(temporales.size() - 1);
            if (entero) {
                cuadruplo +=  "<font color='purple' >&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;"  +variable+  "&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;  &nbsp;  &nbsp;  &nbsp;" + "=" + "&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;  &nbsp;  &nbsp;  &nbsp;" + Integer.parseInt(valor) + "&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;  &nbsp;  &nbsp;  &nbsp;" + "</font><br>";
                tablaSimbolos.get(variable).setValor(Integer.parseInt(valor)+"");
                filas[tablaSimbolos.get(variable).getFila()][3] = Integer.parseInt(valor)+"";

              valor = Integer.parseInt(valor)+ "";
            } else {
                cuadruplo += "<font color='purple' >&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;" +variable+ "&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;  &nbsp;  &nbsp;  &nbsp;" + "=" + "&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;  &nbsp;  &nbsp;  &nbsp;" + valor + "&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;  &nbsp;  &nbsp;  &nbsp;" + "</font><br>";
                tablaSimbolos.get(variable).setValor(valor);
                filas[tablaSimbolos.get(variable).getFila()][3] = valor;

            }

            getCuadruplos().add(variable);
            getCuadruplos().add("=");
            getCuadruplos().add(valor);
            getCuadruplos().add("");

            declar_Var.get(declar_Var.size()-1)[1] ="0";
        }else {
            if (!numeros.isEmpty()) {
                if (entero) { // a = 2:
                    cuadruplo += "<i><p bgColor='white'><font color='purple' >&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;" + variable + "&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;  &nbsp;  &nbsp;  &nbsp;" + "=" + "&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;  &nbsp;  &nbsp;  &nbsp;" + (numeros.get(numeros.size() - 1)) + "&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;  &nbsp;  &nbsp;  &nbsp;" + "</i></font></p><br>";
                } else {
                    cuadruplo += "<font color='purple' >&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;" + variable + "&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;  &nbsp;  &nbsp;  &nbsp;" + "=" + "&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;  &nbsp;  &nbsp;  &nbsp;" + numeros.get(numeros.size() - 1) + "&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;  &nbsp;  &nbsp;  &nbsp;" + "</font><br>";

                }

                declar_Var.get(declar_Var.size()-1)[1] =numeros.remove(numeros.size() - 1);
                // variable contenido a = 2
                // int a = 3*2;
                // int b = 0;
                // int x = 5+2-3;
                // int c = 3;


                //   A   ---    0
                //  B           0



            }
        }
        cuadruplo += "<font color='#7a3858'>=====================================================</font><br>";
        temporales.clear();
        numeros.clear();
    }
    public String getCuadruplo(){
        return cuadruplo;
    }

    // [ [1], [2]. [3] ]

    private ArrayList<String> getCuadruplos(){
        if(!cuadruplos.isEmpty())
           return cuadruplos.get(cuadruplos.size()-1);
        else
            return null;
    }

    public ArrayList<ArrayList<String>> getCuadruplosArray() {
        return cuadruplos;
    }
}
