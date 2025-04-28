public class Metaphone {
    public static String metaphone(String word) {
        // Si la palabra es nula o vacía, retornar cadena vacía
        if (word == null || word.isEmpty()) {
            return "";
        }

        // Convertir a mayúsculas y obtener caracteres
        word = word.toUpperCase();
        char[] chars = word.toCharArray();
        
        // Variables para seguimiento de posición y construcción del resultado
        StringBuilder result = new StringBuilder();
        int length = chars.length;
        int current = 0;
        
        // Saltar caracteres iniciales según reglas específicas
        // KN, GN, PN, AE, WR, WH al inicio se ignoran
        if (length > 1 && chars[0] == 'K' && chars[1] == 'N') {
            current = 1;
        } else if (length > 1 && chars[0] == 'G' && chars[1] == 'N') {
            current = 1;
        } else if (length > 1 && chars[0] == 'P' && chars[1] == 'N') {
            current = 1;
        } else if (length > 1 && chars[0] == 'A' && chars[1] == 'E') {
            current = 1;
        } else if (length > 1 && chars[0] == 'W' && chars[1] == 'R') {
            current = 1;
        } else if (length > 1 && chars[0] == 'W' && chars[1] == 'H') {
            current = 1;
        }

        // Procesar cada carácter según las reglas de Metaphone
        while (current < length && result.length() < 4) {
            switch (chars[current]) {
                case 'A':
                case 'E':
                case 'I':
                case 'O':
                case 'U':
                    // Regla 1: Las vocales solo se mantienen si son la primera letra
                    if (current == 0) {
                        result.append(chars[current]);
                    }
                    break;

                case 'B':
                    // Regla 2: 'B' -> 'B' a menos que esté al final después de 'M'
                    if (!(current > 0 && chars[current - 1] == 'M' && current == length - 1)) {
                        result.append('B');
                    }
                    break;

                case 'C':
                    // Regla 3: Varias reglas para 'C'
                    if (current > 0 && chars[current - 1] == 'S' && current + 1 < length && 
                        (chars[current + 1] == 'I' || chars[current + 1] == 'E' || chars[current + 1] == 'Y')) {
                        // Ignorar 'C' en "SCE", "SCI", "SCY"
                    } else if (current + 1 < length && 
                             (chars[current + 1] == 'I' || chars[current + 1] == 'E' || chars[current + 1] == 'Y')) {
                        result.append('S');
                    } else if (current + 1 < length && chars[current + 1] == 'H') {
                        if (current > 0 && chars[current - 1] == 'S') {
                            result.append('K');
                        } else {
                            result.append('X');
                        }
                        current++;
                    } else {
                        result.append('K');
                    }
                    break;

                case 'D':
                    // Regla 4: 'D' -> 'J' si va seguido de "GE", "GY", "GI", sino 'T'
                    if (current + 2 < length && chars[current + 1] == 'G' && 
                        (chars[current + 2] == 'E' || chars[current + 2] == 'Y' || chars[current + 2] == 'I')) {
                        result.append('J');
                        current += 2;
                    } else {
                        result.append('T');
                    }
                    break;

                case 'F':
                    // Regla 5: 'F' -> 'F'
                    result.append('F');
                    break;

                case 'G':
                    // Regla 6: Varias reglas para 'G'
                    if (current + 1 < length) {
                        if (chars[current + 1] == 'H') {
                            if (current > 0 && !isVowel(chars[current - 1])) {
                                result.append('K');
                            } else if (current == 0) {
                                if (chars[current + 2] == 'I') {
                                    result.append('J');
                                } else {
                                    result.append('K');
                                }
                            }
                            current++;
                        } else if (chars[current + 1] == 'N') {
                            if (current == length - 2 || 
                                (current + 2 < length && chars[current + 2] == 'E' && current == length - 3)) {
                                // Ignorar GN al final o GNE al final
                            } else {
                                result.append('K');
                            }
                        } else if (chars[current + 1] == 'E' || chars[current + 1] == 'I' || chars[current + 1] == 'Y') {
                            result.append('J');
                        } else {
                            result.append('K');
                        }
                    } else {
                        result.append('K');
                    }
                    break;

                case 'H':
                    // Regla 7: 'H' solo se mantiene si está después de vocal y no después de C,G,P,S,T
                    if (current > 0 && isVowel(chars[current - 1]) && 
                        (current + 1 >= length || !isVowel(chars[current + 1]))) {
                        result.append('H');
                    }
                    break;

                case 'J':
                    // Regla 8: 'J' -> 'J'
                    result.append('J');
                    break;

                case 'K':
                    // Regla 9: 'K' solo se mantiene si no está después de 'C'
                    if (current == 0 || chars[current - 1] != 'C') {
                        result.append('K');
                    }
                    break;

                case 'L':
                    // Regla 10: 'L' -> 'L'
                    result.append('L');
                    break;

                case 'M':
                    // Regla 11: 'M' -> 'M'
                    result.append('M');
                    break;

                case 'N':
                    // Regla 12: 'N' -> 'N'
                    result.append('N');
                    break;

                case 'P':
                    // Regla 13: 'P' -> 'F' si va seguido de 'H', sino 'P'
                    if (current + 1 < length && chars[current + 1] == 'H') {
                        result.append('F');
                        current++;
                    } else {
                        result.append('P');
                    }
                    break;

                case 'Q':
                    // Regla 14: 'Q' -> 'K'
                    result.append('K');
                    break;

                case 'R':
                    // Regla 15: 'R' -> 'R'
                    result.append('R');
                    break;

                case 'S':
                    // Regla 16: Reglas especiales para 'S'
                    if (current + 1 < length && chars[current + 1] == 'H') {
                        result.append('X');
                        current++;
                    } else if (current + 2 < length && chars[current + 1] == 'I' && 
                             (chars[current + 2] == 'O' || chars[current + 2] == 'A')) {
                        result.append('X');
                    } else {
                        result.append('S');
                    }
                    break;

                case 'T':
                    // Regla 17: Reglas especiales para 'T'
                    if (current + 1 < length && chars[current + 1] == 'H') {
                        result.append('0');
                        current++;
                    } else if (current + 2 < length && chars[current + 1] == 'I' && 
                             (chars[current + 2] == 'O' || chars[current + 2] == 'A')) {
                        result.append('X');
                    } else {
                        result.append('T');
                    }
                    break;

                case 'V':
                    // Regla 18: 'V' -> 'F'
                    result.append('F');
                    break;

                case 'W':
                case 'Y':
                    // Regla 19: 'W' y 'Y' se mantienen si van seguidas de vocal
                    if (current + 1 < length && isVowel(chars[current + 1])) {
                        result.append(chars[current]);
                    }
                    break;

                case 'X':
                    // 'X' -> 'KS'
                    result.append("KS");
                    break;

                case 'Z':
                    // 'Z' -> 'S'
                    result.append('S');
                    break;
            }
            current++;
        }

        return result.toString();
    }

    // Método auxiliar para verificar si un carácter es vocal
    private static boolean isVowel(char c) {
        return "AEIOU".indexOf(c) != -1;
    }
}
