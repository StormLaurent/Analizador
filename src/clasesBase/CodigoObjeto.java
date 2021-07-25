package clasesBase;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class CodigoObjeto {
    private String ensamblador =  "<html> <body> .MODEL SMALL <br> .STACK 100H <br> .DATA<br>";
    private ArrayList<ArrayList<String>> cuadruplos;
    private ArrayList<String[]> declaraciones;
    public CodigoObjeto(ArrayList<ArrayList<String>> cuadruplos, ArrayList<String[]> declaraciones){
        this.cuadruplos = cuadruplos;
        this.declaraciones = declaraciones;
    }
    private String traductor(String operadores){

        switch (operadores){
            case "+":
               return "ADD";
            case "-":
                return "SUB";
            case "*":
                return "IMUL";
            case "/":
                return "IDIV";

        }

        return null;
    }
    private void ensamblador(){
        int count = 1;
        for(int i = 0; i<cuadruplos.size(); i++){
            for(int c = 0; c < cuadruplos.get(i).size(); c+=4){

            }
        }


    }
}
