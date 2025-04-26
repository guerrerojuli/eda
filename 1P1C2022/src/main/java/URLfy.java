public class URLfy {
    public static void main(String[] args) {
        URLfy urlfy = new URLfy();
        char [] arreglo = new char[] { 'e', 's', ' ', 'u', 'n', ' ', 'e', 'j', 'e', 'm', 'p', 'l', 'o', '\0', '\0', '\0', '\0'};
        System.out.println(arreglo);
        urlfy.reemplazarEspacios(arreglo);
        System.out.println(arreglo);

        arreglo= new char [] {'a', ' ', 'b', ' ', 'c', ' ', 'd', ' ', 'e', ' ', 'f', ' ', 'g', ' ', 'h', 'o', 'l', 'a', '\0', '\0',
                '\0', '\0', '\0', '\0', '\0', '\0', '\0', '\0', '\0', '\0', '\0', '\0'};
        System.out.println(arreglo);
        urlfy.reemplazarEspacios(arreglo);
        System.out.println(arreglo);

        arreglo= new char [] {' ', ' ', 'e', 's', 'p', 'a', 'c', 'i', 'o', 's', ' ', ' ', '\0', '\0', '\0', '\0', '\0', '\0',
                '\0', '\0'};
        System.out.println(arreglo);
        urlfy.reemplazarEspacios(arreglo);
        System.out.println(arreglo);
    }

    public void reemplazarEspacios(char[] arregloParam) {
        int j = arregloParam.length - 1;
        for (int i = j; i >= 0; i--) {
            if (arregloParam[i] == '\0') {
                // Ignorar el carácter nulo
                continue;
            }
            if (arregloParam[i] == ' ') {
                // Reemplazar el espacio por %20
                arregloParam[j--] = '0';
                arregloParam[j--] = '2';
                arregloParam[j--] = '%';
            } else {
                // Copiar el carácter original
                arregloParam[j--] = arregloParam[i];
            }
        }
    }
}
