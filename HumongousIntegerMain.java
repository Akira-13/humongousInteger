/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uni.aed.humoungousinteger;

import java.math.BigInteger;

/**
 *
 * @author akira
 */
public class HumongousIntegerMain {
    public static void main (String[] args){
        HumongousIntegerMain him = new HumongousIntegerMain();
        him.testInput();
        //him.testOutput();
        //him.testSum();
        //him.testSub();
        //him.testAddSub();
        //him.testMul();
        //him.testDiv();
        //him.testMulDiv();
        //him.testIncr();
        //him.testDecr();
        //him.testSimpleMulDiv();
    }
    
    private void testInput(){
        String strArray[] ={
            "+100005000",
            "-8000",
            "10",
            "--3283748300000",
            "7",
            "10000e5000",
            "-4000e",
            "2147483646",
            "-4500",
            "2147480000",
            "-2147483646",
            "1000000000000000000000000000000000000"
        };
        HumongousInteger hi;
            for(int i = 0; i < strArray.length; i++){        
                try{
                    System.out.print(i + ": " + strArray[i] + "->");
                    hi = new HumongousInteger(strArray[i]);
                    System.out.println(hi.toString());
                }catch(IllegalArgumentException e){
                    System.out.println("Excepcion: " + e.getMessage());
            }
        }

    }
    
    private void testOutput(){
        HumongousInteger[] hi = new HumongousInteger[7];
        hi[0] = new HumongousInteger(123456789);
        hi[1] = new HumongousInteger(-45);
        hi[2] = new HumongousInteger("123456789012344");
        hi[3] = new HumongousInteger("-0004000000");
        hi[4] = new HumongousInteger(-3458);
        hi[5] = new HumongousInteger(-0000);
        hi[6] = new HumongousInteger();
        System.out.println("Sin formato");
        for(int i = 0; i < hi.length; i++){
            System.out.println(i + ": " + hi[i].toString());
        }
        System.out.println("Formato con \",\"");
        for(int i = 0; i < hi.length; i++){
            System.out.println(i + ":" + hi[i].toString(","));
        }
        System.out.println("Formato con \".\"");
        for(int i = 0; i < hi.length; i++){
            System.out.println(i + ":" + hi[i].toString("."));
        }
    }
    
    private void testSum(){
        HumongousInteger h1, h2, hsum;
        BigInteger b1, b2, bsum;
        h1 = new HumongousInteger("12340006789");
        h2 = new HumongousInteger("987654321");
        hsum = h1.add(h2);
        b1 = new BigInteger("12340006789");
        b2 = new BigInteger("987654321");
        bsum = b1.add(b2);
        if(bsum.compareTo(new BigInteger(hsum.toString())) == 0){
            System.out.println("Todo bien");
        }
        else System.out.println("Algo salio mal");
    }
    
    private void testSub(){
        HumongousInteger h1, h2, hsub;
        BigInteger b1, b2, bsub;
        h1 = new HumongousInteger("12340006789");
        h2 = new HumongousInteger("987654321");
        hsub = h1.sub(h2);
        b1 = new BigInteger("12340006789");
        b2 = new BigInteger("987654321");
        bsub = b1.subtract(b2);
        if(bsub.compareTo(new BigInteger(hsub.toString())) == 0){
            System.out.println("Todo bien");
        }
        else System.out.println("Algo salio mal");
    }
    
    private void testMul(){
        HumongousInteger h1, h2, hmul;
        BigInteger b1, b2, bmul;
        h1 = new HumongousInteger("-12340006789");
        h2 = new HumongousInteger("987654321");
        hmul = h1.mul(h2);
        b1 = new BigInteger("-12340006789");
        b2 = new BigInteger("987654321");
        bmul = b1.multiply(b2);
        System.out.print(hmul.toString());
        if(bmul.compareTo(new BigInteger(hmul.toString())) == 0){
            System.out.println("Todo bien");
        }
        else System.out.println("Algo salio mal");
    }
    
    private void testDiv(){
        HumongousInteger h1, h2, hdiv;
        BigInteger b1, b2, bdiv;
        h1 = new HumongousInteger("-12340006789");
        h2 = new HumongousInteger("987654321");
        hdiv = h1.div(h2);
        b1 = new BigInteger("-12340006789");
        b2 = new BigInteger("987654321");
        bdiv = b1.divide(b2);
        System.out.println(hdiv.toString());
        if(bdiv.compareTo(new BigInteger(hdiv.toString())) == 0){
            System.out.println("Todo bien");
        }
        else System.out.println("Algo salio mal");
    }
    
    private void testAddSub(){
        String strArray[] ={
            "100005000",
            "9182734738218170000000072817",
            "8000",
            "10",
            "3283748300000",
            "7",
            "100005000",
            "-4000",
            "2147483646",
            "-4500",
            "2147480000",
            "-2147483646",
            "1000000000000000000000000000000000000"
        };
        HumongousInteger hi1;
        HumongousInteger hi2;
        HumongousInteger hi3;
        
        BigInteger bi1;
        BigInteger bi2;
        BigInteger bi3;
        int addErrorCnt = 0, subErrorCnt = 0;
        for(int i = 0; i < strArray.length; i++){
            for(int j = 0; j < strArray.length; j++){
                hi1 = new HumongousInteger(strArray[i]);
                hi2 = new HumongousInteger(strArray[j]);
                
                bi1 = new BigInteger(strArray[i]);
                bi2 = new BigInteger(strArray[j]);
                
                System.out.println("\nPara los pares i=" + i + " j= " + j);
                System.out.print("hi1: " + hi1.toString() + " ");
                System.out.println("hi2: " + hi2.toString() + " ");
                //---------------Adicion----------------//
                hi3 = hi1.add(hi2);
                bi3 = bi1.add(bi2);
                System.out.print("Resultado: " + hi3.toString() + "       ");
                if(bi3.compareTo(new BigInteger (hi3.toString())) != 0 ||
                        !bi3.toString().equals(hi3.toString())){
                    addErrorCnt++; System.out.println("Adicion fallida");
                }
                //---------------Sustraccion----------------//
                hi3 = hi1.sub(hi2);
                bi3 = bi1.subtract(bi2);
                System.out.print("Resultado: " + hi3.toString() + "       ");
                if(bi3.compareTo(new BigInteger (hi3.toString())) != 0 ||
                        !bi3.toString().equals(hi3.toString())){
                    addErrorCnt++; System.out.println("Sustraccion fallida");
                }
            }
        }
        System.out.println("\n\nResultado de tests: " +
                (addErrorCnt == 0 && subErrorCnt == 0 ? 
                        "Todo bien" : "Error"));
    }
    
    private void testMulDiv(){
        String strArray[] ={
            "100005000",
            "9182734738218170000000072817",
            "8000",
            "10",
            "3283748300000",
            "7",
            "100005000",
            "-4000",
            "2147483646",
            "-4500",
            "2147480000",
            "-2147483646",
            "1000000000000000000000000000000000000"
        };
        HumongousInteger hi1;
        HumongousInteger hi2;
        HumongousInteger hi3;
        
        BigInteger bi1;
        BigInteger bi2;
        BigInteger bi3;
        int mulErrorCnt = 0, divErrorCnt = 0;
        for(int i = 0; i < strArray.length; i++){
            for(int j = 0; j < strArray.length; j++){
                hi1 = new HumongousInteger(strArray[i]);
                hi2 = new HumongousInteger(strArray[j]);
                
                bi1 = new BigInteger(strArray[i]);
                bi2 = new BigInteger(strArray[j]);
                
                System.out.println("\nPara los pares i=" + i + " j= " + j);
                System.out.print("hi1: " + hi1.toString() + " ");
                System.out.println("hi2: " + hi2.toString() + " ");
                //---------------Multiplicacion----------------//
                hi3 = hi1.mul(hi2);
                bi3 = bi1.multiply(bi2);
                System.out.print("Resultado: " + hi3.toString() + "       ");
                if(bi3.compareTo(new BigInteger (hi3.toString())) != 0 ||
                        !bi3.toString().equals(hi3.toString())){
                    mulErrorCnt++; System.out.println("Multiplicacion fallida");
                }
                //---------------Division----------------//
                hi3 = hi1.div(hi2);
                bi3 = bi1.divide(bi2);
                System.out.print("Resultado: " + hi3.toString() + "       ");
                if(bi3.compareTo(new BigInteger (hi3.toString())) != 0 ||
                        !bi3.toString().equals(hi3.toString())){
                    divErrorCnt++; System.out.println("Division fallida");
                }
            }
        }
        System.out.println("\n\nResultado de tests: " +
                (mulErrorCnt == 0 && divErrorCnt == 0 ? 
                        "Todo bien" : "Error"));
    }
    
    private void testSimpleMulDiv(){
        String strArray[] ={
            "1000005",
            "8000",
            "10",
            "7",
            "0",
            "-4000",
            "2147496",
            "-4500",
            "-000",
        };
        HumongousInteger hi1;
        HumongousInteger hi2;
        HumongousInteger hi3;
        
        BigInteger bi1;
        BigInteger bi2;
        BigInteger bi3;
        int mulErrorCnt = 0, divErrorCnt = 0;
        for(int i = 0; i < strArray.length; i++){
            for(int j = 0; j < strArray.length; j++){
                hi1 = new HumongousInteger(strArray[i]);
                hi2 = new HumongousInteger(strArray[j]);
                
                bi1 = new BigInteger(strArray[i]);
                bi2 = new BigInteger(strArray[j]);
                
                System.out.println("\nPara los pares i=" + i + " j= " + j);
                System.out.print("hi1: " + hi1.toString() + " ");
                System.out.println("hi2: " + hi2.toString() + " ");
                //---------------Multiplicacion----------------//
                hi3 = hi1.simpleMul(hi2);
                bi3 = bi1.multiply(bi2);
                System.out.print("Resultado: " + hi3.toString() + "       ");
                if(bi3.compareTo(new BigInteger (hi3.toString())) != 0 ||
                        !bi3.toString().equals(hi3.toString())){
                    mulErrorCnt++; System.out.println("Multiplicacion fallida");
                }
                //---------------Division----------------//
                try{
                    hi3 = hi1.simpleDiv(hi2);
                    bi3 = bi1.divide(bi2);
                    System.out.print("Resultado: " + hi3.toString() + "       ");
                    if(bi3.compareTo(new BigInteger (hi3.toString())) != 0 ||
                            !bi3.toString().equals(hi3.toString())){
                        divErrorCnt++; System.out.println("Division fallida");
                    }
                }catch(ArithmeticException e){
                    System.out.println("Excepcion: " + e.getMessage());
                }
            }
        }
        System.out.println("\n\nResultado de tests: " +
                (mulErrorCnt == 0 && divErrorCnt == 0 ? 
                        "Todo bien" : "Error"));
    }
    
    private void testIncr(){
        HumongousInteger[] hi = new HumongousInteger[10];
        hi[0] = new HumongousInteger(123456789);
        hi[1] = new HumongousInteger(0);
        hi[2] = new HumongousInteger("123456789012344");
        hi[3] = new HumongousInteger("0004000000");
        hi[4] = new HumongousInteger(3458);
        hi[5] = new HumongousInteger(-9999);
        hi[6] = new HumongousInteger(999);
        hi[7] = new HumongousInteger(99999); 
        hi[8] = new HumongousInteger(999999);
        hi[9] = new HumongousInteger(9999999);
        for(int i = 0; i < 10; i++){
            hi[i].incr();
            System.out.println(hi[i].toString());
        }
    }
    
    private void testDecr(){
        HumongousInteger hi = new HumongousInteger(1000);
        hi.decr();
        System.out.println(hi.toString());
    }
}
