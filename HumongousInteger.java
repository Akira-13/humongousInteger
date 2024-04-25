package uni.aed.humoungousinteger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author akira
 */
class HumongousInteger {
    private static final char MINUS = '-';
    private Node head;
    private byte sign;
    
    /**************************
    *Constructores
    ***************************/
    //Formato válido [<signo negativo>]<digit>+
    
    public HumongousInteger(HumongousInteger num){
        this.sign = num.sign;
        this.head = new Node();
        Node p = head;
        Node q = num.head;
        while(q != null){
            p.next = new Node(q.value);
            p = p.next;
            q = q.next;
        }
        this.head = this.head.next;
    }
    
    private HumongousInteger(Node head){
        this.head = head;
        this.sign = +1;
    }
    
    public HumongousInteger() {
        this("0");
    }
    public HumongousInteger(long value) {
        this("" + value);
    }
    public HumongousInteger(String value) {
        //Pregunta 13
        //Que la clase solo acepte el formato [<signo negativo>]<digit>+
        //Se usan expresiones regulares
        //--------------------------
        String pattern = "^[-]?\\d+$";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(value);
        if(!m.find()) throw new IllegalArgumentException("Formato inadecuado "
                + "para HumongousInteger");
        //--------------------------
        value = value.trim();
        sign = +1;
        if(value.charAt(0) == MINUS){
            sign = -1;
            value = value.substring(1);
        }
        value = trimLeadingZero(value);
        if(value.equals("0")){
            sign = +1;
        }
        head = new Node();  //Usa un nodo dummy
        Node tail = head;
        String digits;
        while(!value.equals("")){
            int loc = Math.max(value.length() - Node.MAX_DIGITS, 0);
            digits = value.substring(loc); //Quitar los ultimos 3 digitos
                                           //Si < 3 quitar todo
            value = value.substring (0, loc); //Si loc == 0, number es ""
            Node block = new Node(digits);
            tail.next = block;
            tail = block;
        }
        head = head.next; //Remover el nodo dummy
    }

    /************************************************************
    *Metodos
    ************************************************************/
    
    /**************************
    *Adicion
    ***************************/
    public HumongousInteger add(HumongousInteger num){
        /*
        * Se consideran cuatro casos. Para cada uno, alteramos
        * La operacion para que sea addPos y subPos
        * Los cuatro casos son:
        * A + B -> A + B
        * A + -B -> A - B
        * -A + B -> B - A
        * -A + -B -> -(A + B)
        */
        HumongousInteger L = new HumongousInteger(this);
        HumongousInteger R = new HumongousInteger(num);
        if(L.isPositive() && R.isPositive()){
            return L.addPos(R);
        }
        if(L.isPositive() && R.isNegative()){
            return L.subPos(R.negate());
        }
        if(L.isNegative() && R.isPositive()){
            return R.subPos(L.negate());
        }
        //ambos negativos
        return L.negate().addPos(R.negate()).negate();
    }
    
    private HumongousInteger addPos(HumongousInteger num){
        Node p, q, r, t;
        p = this.head;
        q = num.head;
        t = new Node(); // dummy head node
        r = t;
        short carry = 0;
        while(p != null && q != null){
            short sum = (short) (carry + p.value + q.value);
            r.next = new Node();
            r = r.next;
            r.value = (short) (sum % Node.MAX_VALUE);
            carry = (short) (sum / Node.MAX_VALUE);
            p = p.next;
            q = q.next;
        }
        p = (p == null) ? q : p;
        while(p != null){
            r.next = new Node();
            r = r.next;
            r.value = (short) ((p.value+carry) % Node.MAX_VALUE);
            carry = (short) ((p.value + carry) / Node.MAX_VALUE);
            p = p.next;
        }
        if(carry > 0){ //overflow, final carry
            r.next = new Node ((short) carry);
        }
        return new HumongousInteger(t.next);
    }
    
    /**************************
    *Sustraccion
    ***************************/
    
    public HumongousInteger sub(HumongousInteger num){
        /*
        Se consideran cuatro casos nuevamente
        A - B -> A - B
        A - -B -> A + B
        -A - B -> -(A + B)
        -A - -B -> B - A
        */
        HumongousInteger L = new HumongousInteger(this);
        HumongousInteger R = new HumongousInteger(num);
        if(L.isPositive() && R.isPositive()){
            return L.subPos(R);
        }
        if(L.isPositive() && R.isNegative()){
            return L.addPos(R.negate());
        }
        if(L.isNegative() && R.isPositive()){
            return L.negate().addPos(R).negate();
        }
        return R.negate().subPos(L.negate());
    }
    
    private HumongousInteger subPos(HumongousInteger num){
        Node p, q, r, t;
        boolean isNegative = false;
        //Siempre sustraer pequeño del mayor
        //Si el número es mayor, el resultado es negativo
        if(this.compareTo(num) >= 0){ //this - num
            p = this.head;
            q = num.head;
        } else{                     //-(num - this)
            p = num.head;
            q = this.head; isNegative = true;
        }
        t = new Node(); //Nodo dummy
        r = t;
        short borrow = 0, minuend;
        while(p != null && q != null){
            r.next = new Node();
            r = r.next;
            minuend = (short) (p.value - borrow);
            if(minuend < q.value){ //borrow
                r.value = (short) (Node.MAX_VALUE + minuend - q.value);
                borrow = 1;
            } else{ //no borrow
                r.value = (short) (minuend - q.value);
                borrow = 0;
            }
            p = p.next;
            q = q.next;
        }
        p = (p == null) ? q : p; //reset p to point to remaining blocks
        while( p != null){
            r.next = new Node();
            r = r.next;
            r.value = (short) (p.value - borrow);
            if(r.value < 0){
                r.value += Node.MAX_VALUE;
                borrow = 1;
            } else{
                borrow = 0;
            }
            p = p.next;
        }
        HumongousInteger result = new HumongousInteger(t.next);
        result = result.trimLeadingZero();
        if(isNegative) result.negate();
        return result;
    }
    
    /**************************
    *Multiplicacion
    ***************************/
    
    //Pregunta 17
    //Multiplicacion simple
    public HumongousInteger simpleMul(HumongousInteger num){
        HumongousInteger counter = new HumongousInteger();
        HumongousInteger limit, term;
        HumongousInteger product = new HumongousInteger();
        limit = new HumongousInteger(num.abs());
        term = new HumongousInteger(this.abs());
        if(this.compareTo(num) < 0){
            limit = new HumongousInteger(this.abs());
            term = new HumongousInteger(num.abs());
        }
        while(counter.compareTo(limit) < 0){
            product = product.add(term);
            counter.incr();
        }
        if(product.compareTo(new HumongousInteger()) == 0 || 
                this.sign == num.sign){
            product.sign = +1;
        }else product.sign = -1;
        return product;
    }
    
    //Pregunta 18
    //Multiplicacion eficiente
    public HumongousInteger mul(HumongousInteger num){
        /*
        Se consideran cuatro casos
        A * B -> A * B
        A * -B -> -(A * B)
        -A * B -> -(A * B)
        -A * -B -> A * B
        */
        HumongousInteger L = new HumongousInteger(this);
        HumongousInteger R = new HumongousInteger(num);
        if(L.isPositive() && R.isPositive()){
            return L.mulPos(R);
        }
        if(L.isPositive() && R.isNegative()){
            return (L.mulPos(R.negate())).negate();
        }
        if(L.isNegative() && R.isPositive()){
            return ((L.negate()).mulPos(R)).negate();
        }
        return (L.negate()).mulPos(R.negate());
    }
    
    private HumongousInteger mulPos(HumongousInteger num){
        HumongousInteger pResult;
        HumongousInteger result = new HumongousInteger();
        Node p = num.head;
        String numberAsString;
        int zeros = 0, j;
        byte multiplicator;
        while(p != null){
            numberAsString = Short.toString(p.value);
            for(j = numberAsString.length()-1; j >= 0; j--){
                multiplicator = (byte) (numberAsString.charAt(j) - '0');
                pResult = this.singleMulPos(multiplicator);
                pResult = pResult.fillZeros(zeros);
                result = result.addPos(pResult);
                zeros++;
            }
            zeros += Node.MAX_DIGITS - numberAsString.length();
            p = p.next;
        }
        return result;
    }
    
    private HumongousInteger singleMulPos(byte num){
        if (num == 0){
            return new HumongousInteger();
        }
        HumongousInteger result = new HumongousInteger();
        for(short i = 0; i < num; i++){
            result = result.addPos(this);
        }
        return result;
    }

    /**************************
    *Division
    ***************************/
    
    //Pregunta 19
    //Division simple
    public HumongousInteger simpleDiv(HumongousInteger num){
        if(num.compareTo(new HumongousInteger()) == 0) 
            throw new ArithmeticException("Division entre 0");
        HumongousInteger quotient = new HumongousInteger();
        HumongousInteger remainder = new HumongousInteger(this.abs());
        HumongousInteger divisor = new HumongousInteger(num.abs());
        while(!(remainder.compareTo(divisor)<0)){
            remainder = remainder.sub(divisor);
            quotient.incr();
        }
        if(quotient.compareTo(new HumongousInteger()) == 0 ||
                this.sign == num.sign)
            quotient.sign = +1;
        else
            quotient.sign = -1;
        return quotient;
    }
    
    //Pregunta 20
    //Division eficiente
    public HumongousInteger div(HumongousInteger num){
        /*
        Se consideran cuatro casos
        A / B -> A / B
        A / -B -> -(A / B)
        -A / B -> -(A / B)
        -A / -B -> A / B
        */
        if(num.compareTo(new HumongousInteger()) == 0){
            throw new ArithmeticException("Division entre 0");
        }
        if(this.compareTo(new HumongousInteger()) == 0){
            return new HumongousInteger();
        }
        HumongousInteger L = new HumongousInteger(this);
        HumongousInteger R = new HumongousInteger(num);
        HumongousInteger quotient;
        if(L.isPositive() && R.isPositive()){
            quotient = L.divide(R);
        }
        else if(L.isPositive() && R.isNegative()){
            quotient = L.divide(R.negate());
        }
        else if(L.isNegative() && R.isPositive()){
            quotient = (L.negate()).divide(R);
        }
        else quotient = (L.negate()).divide(R.negate());
        if((quotient.head.next == null && quotient.head.value == 0) || 
                this.sign == num.sign)
            quotient.sign = +1;
        else quotient.sign = -1;
        return quotient;
    }
    
    private HumongousInteger divide(HumongousInteger divisor){
        if(this.compareTo(divisor) < 0)
            return new HumongousInteger();
        HumongousInteger dividend = this.reverseLinks(), pDividend;
        Node dividendNode = dividend.head;
        String quotient = "", pDividendMax, pDividendString = "";
        byte q = 0, zeros;
        while(dividendNode != null){
            pDividendMax = Short.toString(dividendNode.value);
            zeros = (byte) (Node.MAX_DIGITS -  pDividendMax.length());
            for(int i = 0; i < pDividendMax.length(); i++){
                if(zeros-->0){ 
                    pDividendString += "0";
                    i--;
                }
                else pDividendString += pDividendMax.charAt(i);
                if(divisor.compareTo(new HumongousInteger(pDividendString)) > 0){
                    quotient += "0";
                    continue;
                }
                pDividend = new HumongousInteger(pDividendString);
                for(; pDividend.sign == 1; q++){
                    pDividend = pDividend.sub(divisor);
                }
                quotient += Byte.toString(--q);
                pDividendString = (pDividend.add(divisor)).toString();
                q = 0;
            }
            dividendNode = dividendNode.next;
        }
        return new HumongousInteger(quotient);
    }
    
    /**************************
    *Metodos auxiliares
    ***************************/    
    
    private boolean isPositive(){
        return sign > 0;
    }
    
    private boolean isNegative(){
        return sign < 0;
    }
    
    private HumongousInteger trimLeadingZero(){
        String numStr = this.toString();
        String result = trimLeadingZero(numStr);
        if(result.equals("0")){
            return new HumongousInteger(0);
        } else if (result.length() < numStr.length()){
            return new HumongousInteger(result);
        } else return this;
    }
    
    private static String trimLeadingZero(String str){
        StringBuffer strBuf = new StringBuffer(str);
        int length = strBuf.length();
        for(int i = 0; i < length; i++){
            if(strBuf.charAt(0) == '0'){
                strBuf.deleteCharAt(0);
            }
        }
        if(strBuf.length() == 0){
            strBuf.append('0');
        }
        return strBuf.toString();
    }
    
    //Llena de ceros a la derecha del numero
    //Segun un parametro z
    //Util para la multiplicacion
    private HumongousInteger fillZeros(int z){
        String convertedHI = this.toString();
        for(int i = 0; i < z; i++){
            convertedHI += "0";
        }
        return new HumongousInteger(convertedHI);
    }
    
    private HumongousInteger negate(){
        sign = (byte) -sign;
        return this;
    }
    
    //Invertir orden de los enlaces
    //head apunta a la cifra mas significativa
    //apunta a las menos significativas
    //util para division larga
    private HumongousInteger reverseLinks(){
        if(head.next == null){
            return this;
        }
        Node startR = null, p = head;
        while(p != null){
            startR = new Node(p.value, startR);
            p = p.next;
        }
        return new HumongousInteger(startR);
    }
    
    /*Pregunta 15
    * Funcion para incrementar un HI una unidad
    */
    
    public void incr(){
        if(this.isNegative()){
            this.negate().decr();
            this.negate();
            return;
        }
        byte carry = 1;
        Node p = this.head, r, t;
        r = t = new Node();
        Short sum;
        while(p != null && carry != 0){
            sum = (short) (p.value + carry);
            r.next = new Node();
            r = r.next;
            r.value = (short) (sum % Node.MAX_VALUE);
            carry = (byte) (sum / Node.MAX_VALUE);
            p = p.next;
        }
        while(p != null){
            r.next = new Node();
            r = r.next;
            r.value = p.value;
            p = p.next;
        }
        if(carry != 0){
            r.next = new Node((short) carry);
        }
        this.head = t.next;
    }
    
    /*Pregunta 16
    *Funcion para decrementar un HI una unidad
    */
    
    public void decr(){
        if(this.isNegative()){
            this.negate().incr();
            this.negate();
            return;
        }
        if(this.head.next == null && this.head.value == 0){
            this.incr();
            this.negate();
            return;
        }
        Node p = this.head, r, t;
        r = t = new Node();
        short borrow = 0, subtrahend = 1, minuend;
        r.next = new Node();
        r = r.next;
        minuend = p.value;
        if(minuend < subtrahend){
            r.value = (short) (Node.MAX_VALUE + minuend - subtrahend);
            borrow = 1;
        }
        else{
            r.value = (short) (minuend - subtrahend);
        }
        p = p.next;
        while(p != null){
            r.next = new Node();
            r = r.next;
            r.value = (short) (p.value - borrow);
            if(r.value < 0){
                r.value += Node.MAX_VALUE;
                borrow = 1;
            } else borrow = 0;
            p = p.next;
        }
        HumongousInteger result = new HumongousInteger(t.next);
        result = result.trimLeadingZero();
        this.head = result.head;
    }
        
    public int compareTo(HumongousInteger num){
        HumongousInteger L = this;
        HumongousInteger R = num;
        if(L.isPositive() && R.isNegative()){
            return +1;
        }
        if(L.isNegative() && R.isPositive()){
            return -1;
        }
        //Ambos tienen el mismo signo
        //Se comparan pasándolos a String
        String Lstr = L.toString();
        String Rstr = R.toString();
        int result;
        int lengthL = Lstr.length();
        int lengthR = Rstr.length();
        if(lengthL == lengthR){
            result = Lstr.compareTo(Rstr);
        } else{
            result = (lengthL < lengthR) ? -1 : +1;
        }
        //Verifica signo de los dos HI
        return L.sign * result;
    }
    
    public HumongousInteger abs(){
        HumongousInteger absoluteValue = new HumongousInteger(this);
        absoluteValue.sign = +1;
        return absoluteValue;
        
    }
    
    /**************************
    *Clase Interna
    ***************************/
    class Node{
        private static final short MAX_DIGITS = 3;
        private static final short MAX_VALUE = 1000;
        private short value;
        private Node next;
        private Node(){
            this("0");
        }
        private Node(String str){
            this(Short.parseShort(str));
        }
        private Node(short val){
            value = val;
            next = null;
        }
        private Node(short val, Node node){
            value = val;
            next = node;
        }
    }
    
    /**************************
    *toString
    ***************************/
    
    /*14. Implementar el metodo toString que acepte
    *un separador cada tres caracteres
    */
    
    @Override
    public String toString() {
        return this.toString("");
    }
    
    public String toString(String separator){
        StringBuffer strBuf = new StringBuffer("");
        String format = "%0" + Node.MAX_DIGITS + "d" + separator;
        Node p = head;
        while(p.next != null){
            strBuf.insert(0, String.format(format, p.value));
                //pad leading 0s if the digits are
                //in the middle of the number
            p = p.next;
        }
        strBuf.insert(0, p.value + separator);
        if(sign < 0){
            strBuf.insert(0, "-");
        }
        if("".equals(separator)) return strBuf.toString();
        return (strBuf.deleteCharAt(strBuf.length()-1)).toString();
    }
}