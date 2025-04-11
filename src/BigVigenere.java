import java.util.Scanner;
import java.math.BigInteger;

public class BigVigenere {
    private final int[] key;

    public BigVigenere(int[] key) {
        this.key = key;
        char[][] alphabet = matriz();
    }

    public static int[] pedirLlave() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Ingrese la clave numerica: ");
        BigInteger num = sc.nextBigInteger();
        String numString = String.valueOf(num);     //Se pide un numero int y se pasa a String

        int[] llave = new int[numString.length()];      //Arreglo de int que guarda la llave

        for (int i = 0; i < numString.length(); i++) {
            llave[i] = Character.getNumericValue(numString.charAt(i));
        }
        return llave;       //Devuelve la llave como un arreglo de int
    }

    public char[][] matriz() {
        String caracteres = "abcdefghijklmnñopqrstuvwxyzABCDEFGHIJKLMNÑOPQRSTUVWXYZ0123456789";
        int n = caracteres.length() + 1;        //Se añade 1 para incluir un espacio en blanco
        char[][] alphabet = new char[n][n];     //Crear una matriz cuadrada de nxn
        alphabet[0][0] = ' ';

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i != 0 || j != 0) {     //Asignar valores a las celdas que no son la posición (0,0)
                    alphabet[i][j] = caracteres.charAt(((i + j - 1) % caracteres.length()));
                }
            }
        }
    /*
    for (int i = 0; i < n; i++) {       //Ciclo para mostrar la matriz
        for (int j = 0; j < n; j++) {
            System.out.print(alphabet[i][j] + " ");
        }
        System.out.println();       //Nueva línea para la siguiente fila
    }

     */
        return alphabet;        //alphabet es la matriz cuadrada utilizada para cifrar y descifrar
    }

    public int[] igualarLargo(char[] mensaje, int[] llave) {
        String mensajeS = new String(mensaje);
        mensajeS = mensajeS.replaceAll("\\s+", "");     //Elimina los espacios del mensaje
        char[] mensajeSin = mensajeS.toCharArray();
        int[] llaveCiclo = new int[mensajeSin.length];      //Crea un arreglo del largo del mensaje sin espacios
        int cont = 0;       //cont = iterador para recorrer la llave
        for (int i = 0; i < mensajeSin.length; i++) {
            llaveCiclo[i] = llave[cont];
            cont++;
            if (cont == llave.length) {
                cont = 0;
            }
        }
        return llaveCiclo;      //Queda la llave repetida el mismo numero de caracteres que el mensaje
    }

    public String encrypt(String message) {
        char[] mensajeA = message.toCharArray();
        int[] llaveCiclo = igualarLargo(mensajeA, pedirLlave()); // Igualar la longitud de la clave al mensaje (pide la llave)

        char[] encriptado = new char[mensajeA.length];
        String caracteres = "abcdefghijklmnñopqrstuvwxyzABCDEFGHIJKLMNÑOPQRSTUVWXYZ0123456789";
        int indiceLlave = 0;

        for (int i = 0; i < mensajeA.length; i++) {
            if (mensajeA[i] == ' ') {
                encriptado[i] = ' ';        // Si es un espacio, no hacemos nada, lo dejamos igual
                continue;
            }

            int mensajeIndice = -1;     // Buscar el índice del carácter del mensaje en el alfabeto
            for (int j = 0; j < caracteres.length(); j++) {
                if (mensajeA[i] == caracteres.charAt(j)) {
                    mensajeIndice = j;
                    break;
                }
            }

            if (mensajeIndice != -1) {
                // Tomamos el valor de la clave para el desplazamiento
                int desplazamiento = llaveCiclo[indiceLlave]+55;
                // Calculamos el nuevo índice, asegurándonos de que esté dentro del rango
                int encriptadoIndice = (mensajeIndice + desplazamiento) % caracteres.length();
                encriptado[i] = caracteres.charAt(encriptadoIndice);
            }

            // Incrementamos el índice de la clave
            indiceLlave = (indiceLlave + 1) % llaveCiclo.length;
        }

        return new String(encriptado);
    }

    public String decrypt(String message) {
        char[] mensajeA = message.toCharArray();        // Convertir el mensaje a un arreglo de caracteres
        int[] llaveCiclo = igualarLargo(mensajeA, key);     // Igualar la longitud de la clave al mensaje

        char[] desencriptado = new char[mensajeA.length];       // Array para guardar el mensaje desencriptado
        String caracteres = "abcdefghijklmnñopqrstuvwxyzABCDEFGHIJKLMNÑOPQRSTUVWXYZ0123456789";
        int indiceLlave = 0;        // Índice para recorrer la clave

        for (int i = 0; i < mensajeA.length; i++) {
            if (mensajeA[i] == ' ') {
                desencriptado[i] = ' ';     // Si el carácter es un espacio, lo dejamos igual
                continue;
            }

            int mensajeIndice = -1;     // Buscar el índice del carácter cifrado en el alfabeto
            for (int j = 0; j < caracteres.length(); j++) {
                if (mensajeA[i] == caracteres.charAt(j)) {
                    mensajeIndice = j;
                    break;
                }
            }

            if (mensajeIndice != -1) {      // Tomamos el valor de la clave para el desplazamiento
                int desplazamiento = llaveCiclo[indiceLlave] + 55;      // Desplazamiento de la clave
                int desencriptadoIndice = (mensajeIndice - desplazamiento + caracteres.length()) % caracteres.length();     // Calculamos el nuevo índice, asegurándonos de que esté dentro del rango
                desencriptado[i] = caracteres.charAt(desencriptadoIndice);      // Asignamos el carácter desencriptado
            }
            indiceLlave = (indiceLlave + 1) % llaveCiclo.length;        // Incrementamos el índice de la clave
        }

        return new String(desencriptado);       //Convertir el arreglo de caracteres desencriptado en un String
    }

    public void reEncrypt(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Ingrese el mensaje encriptado para reencriptar: ");
        String encriptado = sc.nextLine();

        System.out.println("El mensaje desencriptado es: "+decrypt(encriptado));        //Se desencripta el mensaje encriptado

        String mensaje=decrypt(encriptado);     //mensaje=mensaje original
        System.out.println("El mensaje reencriptado es: "+encrypt(mensaje));        //En el metodo encrypt se pide la nueva clave

    }


    public char search(int position) {
        char[][] alphabet = matriz();       // Genera la matriz
        int contador = 0;       // Contador de posición recorrida

        for (int i = 0; i < alphabet.length; i++) {
            for (int j = 0; j < alphabet[i].length; j++) {
                if (contador == position) {
                    return alphabet[i][j];      //Cuando llegamos a la posición, devolvemos el carácter
                }
                contador++;     //Avanzamos una posición
            }
        }
        return '\0';        //Si el número está fuera del rango, devolvemos carácter nulo
    }


    //Teniendo la posicion, no es necesario recorrer la matriz completa, se puede calcular directamente la posicion, ya
    //que la fila es la (posicion/64) y la columna es el modulo posicion%64.

    //El tiempo de ejecucion es O(1), es decir, un tiempo constante sin importar la posicion ni el tamaño de la matriz.
    public char optimalSearch(int position) {
        char[][] alphabet = matriz();       //Se genera la matriz
        int filas = alphabet.length;        //Largo de las filas
        int columnas = alphabet[0].length;      //Largo de las columnas
        int total = filas * columnas;

        int fila = position / columnas;
        int columna = position % columnas;

        if (position >= total) {
            return '\0';        //Si position está fuera del rango, retorna carácter nulo
        }
        return alphabet[fila][columna];
    }



    public static void main(String[] args) {
        BigVigenere bv = new BigVigenere(pedirLlave());     // Crear objeto BigVigenere

        Scanner sc = new Scanner(System.in);        // Pedir el mensaje a encriptar

        System.out.println("Ingrese el mensaje a encriptar: ");
        String mensaje = sc.nextLine();

        // Encriptar
        System.out.println("El mensaje encriptado es: " + bv.encrypt(mensaje));     //(El codigo pide la clave numerica 2 veces cuando quiere encriptar)

        //Desencriptar
        System.out.println("Ingrese el mensaje a desencriptar: ");
        String mensaje2 = sc.nextLine();
        System.out.println("El mensaje desencriptado es: " + bv.decrypt(mensaje2));

        //Reencriptar
        bv.reEncrypt();

        //Metodo search y optimalSearch
        System.out.println("Ingresa una posicion: ");
        int posicion= sc.nextInt();
        System.out.println(bv.search(posicion));

        System.out.println(bv.optimalSearch(posicion));

    }
}