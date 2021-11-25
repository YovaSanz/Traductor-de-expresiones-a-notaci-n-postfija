package newpackage;

import java.util.ArrayList;
import java.util.Stack;
import javax.swing.JOptionPane;


public class Logica {

    public String[] conversor(String token){
    
    //Depurar la expresion algebraica
    String expr = depurar(token);
    String[] arrayInfix = expr.split(" ");

    //Declaración de las pilas
    Stack <String> E = new Stack();//Pila entrada
    Stack <String> P = new Stack();//Pila temporal para operadores
    Stack <String> S = new Stack();//Pila salida

    //Añadir la array a la Pila de entrada (E)
    for (int i = arrayInfix.length - 1; i >= 0; i--) {
      E.push(arrayInfix[i]);
    }

    try {
      //Algoritmo Infijo a Postfijo
      while (!E.isEmpty()) {
        switch (pref(E.peek())){
          case 1:
            P.push(E.pop());
            break;
          case 3:
          case 4:
            while(pref(P.peek()) >= pref(E.peek())) {
              S.push(P.pop());
            }
            P.push(E.pop());
            break; 
          case 2:
            while(!P.peek().equals("(")) {
              S.push(P.pop());
            }
            P.pop();
            E.pop();
            break; 
          default:
            S.push(E.pop()); 
        } 
      }

      //Eliminacion de `impurezas´ en la expresiones algebraicas
      String postfix = S.toString().replaceAll("[\\]\\[,]", "");

      //Retorna los resultados:
      
      return new String[]{postfix,calcular(new ArrayList(S))};

    }catch(Exception ex){ 
      JOptionPane.showMessageDialog(null, "Error en la expresión algebraica \n" + ex, "Error", JOptionPane.ERROR_MESSAGE);
      return new String[]{"",""};
    }
}
    
    //Depurar expresión algebraica
    private static String depurar(String s) {
        s = s.replaceAll("\\s+", ""); //Elimina espacios en blanco
        s = "(" + s + ")";
        String simbols = "+-*/^()";
        String str = "";

        //Deja espacios entre operadores
        for (int i = 0; i < s.length(); i++) {
            if (simbols.contains("" + s.charAt(i))) {
                str += " " + s.charAt(i) + " ";
            } else {
                str += s.charAt(i);
            }
        }
        return str.replaceAll("\\s+", " ").trim();
    }

    //Jerarquia de los operadores
    private static int pref(String op) {
        int prf = 99;
        if (op.equals("^")) prf = 4;
        if (op.equals("*") || op.equals("/")) prf = 4;
        if (op.equals("+") || op.equals("-")) prf = 3;
        if (op.equals(")")) prf = 2;
        if (op.equals("(")) prf = 1;
        return prf;
    }
    
    //Allgoridmo para Resolver la expresion
    private static String calcular(ArrayList<String> posfija){
        Stack <String> pila = new Stack();
        
        String eleDer, eleIzq;
        
        for(String c: posfija){
            if (esOperador(c)) { //Compara si es un operador
                eleDer = pila.pop();
                eleIzq = pila.pop();
                double resultado = operar(eleIzq, c, eleDer);
                pila.push("" + resultado);
            } else {
                pila.push(c);
            }
        }
        
        return pila.pop();
    }
    
    //Evaulua el Operador
    private static boolean esOperador(String c) {
        return c.equals("+") || c.equals("-") || c.equals("*")|| c.equals("/")|| c.equals("%") || c.equals("^");
    }
    
    //Resuelve La operacion
    private static double operar(String eleIzq,String operador, String eleDer) {
        double a = Double.parseDouble(eleIzq);
        double b = Double.parseDouble(eleDer);
        
        switch(operador){
            case "+": return a+b;
            case "-": return a-b;
            case "*": return a*b;
            case "/": return a/b;
            case "%": return a%b;
            case "^": return Math.pow(a,b);
            default: return 0;
        }
    }
    
}
