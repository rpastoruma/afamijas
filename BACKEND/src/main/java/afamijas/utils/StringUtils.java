package afamijas.utils;

import java.util.ArrayList;

public class StringUtils
{
    static public boolean isBlank(String cad)
    {
        return org.apache.commons.lang3.StringUtils.isBlank(cad);
    }

    static public String removeAccents(String cad)
    {
        return org.apache.commons.lang3.StringUtils.stripAccents(cad);
    }



    /* Texto a url wordpress:
     *
     *  "Esto es un tÃ­tulo con & y con Ã¡ Ã© Ã­ Ã– Ã±Ã±Ã±" --> "esto-es-un-titulo-con-y-con-a-e-i-o-nnn"
     *
     */
    static public String sanitize(String title)
    {
        title = title.trim();
        title = StringUtils.replaceString(title, "'", "");
        title = java.text.Normalizer.normalize(title, java.text.Normalizer.Form.NFD);
        title = title.replaceAll("\\p{M}", "");
        title = title.replaceAll("[^a-zA-Z0-9_]", "-");

        while(title.indexOf("--")!=-1)
            title = StringUtils.replaceString(title, "--", "-");

        if(title.endsWith("-"))
            title = title.substring(0, title.length()-1);

        if(title.startsWith("-"))
            title = title.substring(1, title.length());

        return title.toLowerCase();
    }



    /**
     * Devuelve lo que hay en text entre tstart y tend y tambiÃ©n el resto de text
     * para poder seguir iterando sobre dicho texto.
     *
     * String text = "ksfsdINICIOaaaaFINxxxRESTO";
     *
     * getSubText(text, "INICIO", "FIN", true)  --> { "aaaa", "xxxRESTO" }
     * getSubText(text, "INICIO", "FIN", false)  --> { "aaaa", "xxxRESTO" }
     * getSubText(text, "INICIO", "fin", false)  --> { "", "ksfsdINICIOaaaaFINxxxRESTO" }
     *
     * Ejemplo de iteraciÃ³n:
     *
     String text = "ksfjskdfKKK1OOOfksfsdjfsKKK2OOOdjflksdjfslkfKKK3OOO";
     while(true)
     {
     String res[] = getSubText(text, "KKK", "OOO", false);
     if(res[0]==null) break;
     System.out.println("res="+res[0]);
     System.out.println("text="+res[1]);
     System.out.println();
     text = res[1];
     }
     *
     *  Salida: 123
     */
    public static String[] getSubText(String text, String tstart, String tend, boolean ignorecase)
    {
        if(text==null) return null;
        String ttext = text;

        if(ignorecase)
        {
            tstart = tstart.toLowerCase();
            tend = tend.toLowerCase();
            ttext = text.toLowerCase();
        }

        int i1 = ttext.indexOf(tstart);
        if(i1==-1) return new String[]{null, text};

        ttext = ttext.substring(i1 + tstart.length());
        text = text.substring(i1 + tstart.length());

        int i2 = ttext.indexOf(tend);
        if(i2==-1) return new String[]{null, text};

        String result = text.substring(0, i2);

        return new String[]{result, text.substring(i2+tend.length())};
    }





    public static String removeSubstringBetween(String text, String tstart, String tend, boolean ignorecase, boolean inclusive)
    {
        String ttext = text;

        if(ignorecase)
        {
            tstart = tstart.toLowerCase();
            tend = tend.toLowerCase();
            ttext = text.toLowerCase();
        }

        int i1 = ttext.indexOf(tstart);
        if(i1==-1) return text;


        int i2 = ttext.indexOf(tend);
        while(i2+tend.length()<i1 && i2!=-1)
        {
            String aux = ttext.substring(i2 + tend.length());
            i2 = aux.indexOf(tend) + (ttext.length()-aux.length());
        }
        if(i2==-1) return text;

        if(i2<i1) return text;

        if(inclusive)
            return  text.substring(0, i1) + text.substring(i2 + tend.length());
        else
            return  text.substring(0, i1 + tend.length()) + text.substring(i2);

    }




    public static String removeConsecutiveChars(String text, String caracter)
    {
        while(text.indexOf(caracter+caracter)!=-1)
            text = replaceString(text, caracter+caracter, caracter);
        return text;
    }



    /**
     * Devuelve una cadena con el reemplazo de TODAS las ocurrencias
     * en str de pattern por replace sin modificar str
     * @param str
     * @param pattern
     * @param replace
     * @return
     */
    static public String replaceString(String str, String pattern, String replace)
    {
        if(str==null) return null;
        if(pattern==null || pattern.equals("")) return str;
        if(pattern.equals(replace)) return str;

        int s = 0;
        int e = 0;
        StringBuffer result = new StringBuffer();

        while ((e = str.indexOf(pattern, s)) >= 0)
        {
            result.append(str.substring(s, e));
            result.append(replace);
            s = e+pattern.length();
        }
        result.append(str.substring(s));
        return result.toString();
    }



    /**
     * Elimina todos los caracteres excepto los dÃ­gitos
     * @param str -- String a tratar
     * @return -- String formado Ãºnicamente por dÃ­gitos
     */
    public static String removeNonNumericCharacters(String str)
    {
        StringBuffer temp = new StringBuffer(str);

        for(int i=0; i<temp.length(); i++)
            if(!Character.isDigit(temp.charAt(i)))
                temp.deleteCharAt(i);

        return temp.toString();
    }


    // Returns hex String representation of byte b
    // byte b[] = "a".getBytes("UTF-8");
    // for(int i=0; i<b.length; i++)
    //   System.out.println(byteToHex(b[i]));
    // --> 61
    static public String byteToHex(byte b)
    {
        char hexDigit[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
        char[] array = { hexDigit[(b >> 4) & 0x0f], hexDigit[b & 0x0f] };
        return new String(array);
    }


    // Returns hex String representation of char c
    // 'a' --> "0061"
    static public String charToHex(char c)
    {
        byte hi = (byte) (c >>> 8);
        byte lo = (byte) (c & 0xff);
        return byteToHex(hi) + byteToHex(lo);
    }



    public static String unicodeEscaped(String text)
    {
        if(text==null) return null;
        String res = "";
        for(int i=0; i<text.length(); i++)
            if(text.charAt(i)==' ')
                res += ' ';
            else
                res += unicodeEscaped(text.charAt(i));

        return res;
    }



    private static String unicodeEscaped(char ch) {
        if (ch < 0x10) {
            return "\\u000" + Integer.toHexString(ch);
        } else if (ch < 0x100) {
            return "\\u00" + Integer.toHexString(ch);
        } else if (ch < 0x1000) {
            return "\\u0" + Integer.toHexString(ch);
        }
        return "\\u" + Integer.toHexString(ch);
    }




    /*
     * Divide una cadena en trozos de maxsize como máximo.
     *
     * Siempre intentará dividir por el separador y a falta de este el espacio en blanco.
     *
     * splitBySizeAndSeparator("12345678901.2345567890.123456789012 345678901234567890", 10, ".") -->
     *
     * ["1234567890", "1.", "2345567890", ".", "1234567890", "12 ", "3456789012", "34567890"]
     */
    public static ArrayList<String> splitBySizeAndSeparator(String cad, int maxsize, String separator)
    {
        if(cad==null) return null;
        ArrayList<String> res = new ArrayList<String>();

        while(!cad.equals(""))
        {
            if(cad.length()<=maxsize) {  res.add(cad); return res; }

            String aux = cad.substring(0, maxsize);
            int idx = aux.lastIndexOf(separator);
            if(idx==-1) idx = aux.lastIndexOf(" ");

            if(idx!=-1)
                aux = cad.substring(0, idx+1);
            else
                idx = maxsize-1;

            cad = cad.substring(idx+1);
            res.add(aux);

            //  System.out.println(aux + " - " + cad);


        }

        return res;

    }

    public static String numberFoString(String input) {
        if (input == null || input.isEmpty()) {
            return input; // Retorna la entrada si es nula o vacía
        }

        // Verifica si la cadena contiene números utilizando una expresión regular
        if (input.matches(".*\\d.*")) {
            return input; // Si tiene números, devuelve la cadena tal cual
        } else {
            return input + "1"; // Si no tiene números, concatena "1"
        }
    }

    public static String incrementLastNumber(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        // Utilizamos una expresión regular para encontrar el último tramo de números
        String regex = "(\\d+)(?!.*\\d)";
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(regex);
        java.util.regex.Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            // Obtenemos el último tramo de números
            String lastNumber = matcher.group(1);
            // Incrementamos el número
            int incrementedNumber = Integer.parseInt(lastNumber) + 1;
            // Construimos la cadena con el tramo incrementado
            //return input.substring(0, matcher.start()) + incrementedNumber + input.substring(matcher.end());
            return incrementedNumber + "";
        } else {
            // Si no hay números en la cadena, devolvemos la cadena original
            return input;
        }
    }


}
