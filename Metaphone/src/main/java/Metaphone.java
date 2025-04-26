public class Metaphone {
    public static String metaphone(String word) {
        if (word == null || word.isEmpty()) {
            return "";
        }

        // Convert to uppercase and get characters
        word = word.toUpperCase();
        char[] chars = word.toCharArray();
        
        // Variables for tracking position and building result
        StringBuilder result = new StringBuilder();
        int length = chars.length;
        int current = 0;
        
        // Skip initial characters according to rules
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

        // Process each character according to Metaphone rules
        while (current < length && result.length() < 4) {
            switch (chars[current]) {
                case 'A':
                case 'E':
                case 'I':
                case 'O':
                case 'U':
                    // Rule 1: Vowels are only kept when they are the first letter
                    if (current == 0) {
                        result.append(chars[current]);
                    }
                    break;

                case 'B':
                    // Rule 2: 'B' -> 'B' unless at the end of word after 'M'
                    if (!(current > 0 && chars[current - 1] == 'M' && current == length - 1)) {
                        result.append('B');
                    }
                    break;

                case 'C':
                    // Rule 3: Various 'C' rules
                    if (current > 0 && chars[current - 1] == 'S' && current + 1 < length && 
                        (chars[current + 1] == 'I' || chars[current + 1] == 'E' || chars[current + 1] == 'Y')) {
                        // Skip 'C' in "SCE", "SCI", "SCY"
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
                    // Rule 4: 'D' -> 'J' if followed by "GE", "GY", "GI", else 'T'
                    if (current + 2 < length && chars[current + 1] == 'G' && 
                        (chars[current + 2] == 'E' || chars[current + 2] == 'Y' || chars[current + 2] == 'I')) {
                        result.append('J');
                        current += 2;
                    } else {
                        result.append('T');
                    }
                    break;

                case 'F':
                    // Rule 5: 'F' -> 'F'
                    result.append('F');
                    break;

                case 'G':
                    // Rule 6: Various 'G' rules
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
                                // Skip GN at end or GNE at end
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
                    // Rule 7: 'H' is only kept if after vowel and not after C,G,P,S,T
                    if (current > 0 && isVowel(chars[current - 1]) && 
                        (current + 1 >= length || !isVowel(chars[current + 1]))) {
                        result.append('H');
                    }
                    break;

                case 'J':
                    // Rule 8: 'J' -> 'J'
                    result.append('J');
                    break;

                case 'K':
                    // Rule 9: 'K' is only kept if not after 'C'
                    if (current == 0 || chars[current - 1] != 'C') {
                        result.append('K');
                    }
                    break;

                case 'L':
                    // Rule 10: 'L' -> 'L'
                    result.append('L');
                    break;

                case 'M':
                    // Rule 11: 'M' -> 'M'
                    result.append('M');
                    break;

                case 'N':
                    // Rule 12: 'N' -> 'N'
                    result.append('N');
                    break;

                case 'P':
                    // Rule 13: 'P' -> 'F' if followed by 'H', else 'P'
                    if (current + 1 < length && chars[current + 1] == 'H') {
                        result.append('F');
                        current++;
                    } else {
                        result.append('P');
                    }
                    break;

                case 'Q':
                    // Rule 14: 'Q' -> 'K'
                    result.append('K');
                    break;

                case 'R':
                    // Rule 15: 'R' -> 'R'
                    result.append('R');
                    break;

                case 'S':
                    // Rule 16: Special 'S' rules
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
                    // Rule 17: Special 'T' rules
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
                    // Rule 18: 'V' -> 'F'
                    result.append('F');
                    break;

                case 'W':
                case 'Y':
                    // Rule 19: 'W' and 'Y' are kept if followed by a vowel
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

    private static boolean isVowel(char c) {
        return "AEIOU".indexOf(c) != -1;
    }
}
