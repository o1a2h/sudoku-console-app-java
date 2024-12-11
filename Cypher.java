

public class Cypher {
  
  public static String SHA256(String input) {
    
    String buffer = "";
    int n = 1;

    // charcters to ASCII bits
    for (int i = 0; i < input.length(); i++) {
      int decimal = charToDecimal(input.charAt(i));
      String binary_string = decimalToBinaryString(decimal, 8);
      buffer += binary_string;
    }

    // add 1
    buffer += '1';

    // get N
    while (buffer.length() + 64 > 512 * n) {
      n += 1;
    }

    // add zeros
    while (buffer.length() < (512 * n) - 64) {
      buffer += '0';
    }

    // add length
    buffer += decimalToBinaryString(input.length() * 8, 64);

    // get first 8 primes
    int[] primes8 = getFirstPrimes(8);
    double[] primes8_sqrt = sqrtOfArray(primes8);
    String[] primes8_sqrt_binary = fractionalDecimalToBinaryStringArray(primes8_sqrt, 8, 32);
    String[] hash_h = splitFractionalPartArray(primes8_sqrt_binary);

    int[] primes64 = getFirstPrimes(64);
    double[] primes64_cbrt = cbrtOfArray(primes64);
    String[] primes64_cbrt_binary = fractionalDecimalToBinaryStringArray(primes64_cbrt, 8, 32);
    String[] hash_k = splitFractionalPartArray(primes64_cbrt_binary);

    

    String[] buffer512 = splitToArrayByBits(buffer, 512);

    for (String each_buffer512: buffer512) {
      each_buffer512 += decimalToBinaryString(0, 48 * 32);
      String[] w = splitToArrayByBits(each_buffer512, 32);

      for (int i = 16; i < 64; i++) {
        String rr7 = binaryStringRightRotate(w[i-15], 7);
        String rr18 = binaryStringRightRotate(w[i-15], 18);
        String rs3 = binaryStringRightShift(w[i-15], 3);

        String s0 = binaryStringXOR(binaryStringXOR(rr7, rr18), rs3);

        String rr17 = binaryStringRightRotate(w[i-2], 17);
        String rr19 = binaryStringRightRotate(w[i-2], 19);
        String rs10 = binaryStringRightShift(w[i-2], 10);

        String s1 = binaryStringXOR(binaryStringXOR(rr17, rr19), rs10);

        w[i] = binaryStringAddition(w[i-16], s0, w[i-7], s1, 32);
      }      

      String a = hash_h[0];
      String b = hash_h[1];
      String c = hash_h[2];
      String d = hash_h[3];
      String e = hash_h[4];
      String f = hash_h[5];
      String g = hash_h[6];
      String h = hash_h[7];

      for (int i = 0; i < 64; i++) {
        String err6 = binaryStringRightRotate(e, 6);
        String err11 = binaryStringRightRotate(e, 11);
        String err25 = binaryStringRightRotate(e, 25);

        String s1 = binaryStringXOR(binaryStringXOR(err6, err11), err25);
        
        String ch = binaryStringXOR(binaryStringAND(e, f), binaryStringAND(binaryStringNOT(e), g));
        String temp_1 = binaryStringAddition(h, s1, ch, hash_k[i], w[i], 32);

        String arr2 = binaryStringRightRotate(a, 2);
        String arr13 = binaryStringRightRotate(a, 13);
        String arr22 = binaryStringRightRotate(a, 22);
        String s0 = binaryStringXOR(binaryStringXOR(arr2, arr13), arr22);

        String anb = binaryStringAND(a, b);
        String anc = binaryStringAND(a, c);
        String bnc = binaryStringAND(b, c);
        String maj = binaryStringXOR(binaryStringXOR(anb, anc), bnc);
        String temp_2 = binaryStringAddition(s0, maj, 32);

        h = g;
        g = f;
        f = e;
        e = binaryStringAddition(d, temp_1, 32);
        d = c;
        c = b;
        b = a;
        a = binaryStringAddition(temp_1, temp_2, 32);
      }

      hash_h[0] = binaryStringAddition(hash_h[0], a, 32);
      hash_h[1] = binaryStringAddition(hash_h[1], b, 32);
      hash_h[2] = binaryStringAddition(hash_h[2], c, 32);
      hash_h[3] = binaryStringAddition(hash_h[3], d, 32);
      hash_h[4] = binaryStringAddition(hash_h[4], e, 32);
      hash_h[5] = binaryStringAddition(hash_h[5], f, 32);
      hash_h[6] = binaryStringAddition(hash_h[6], g, 32);
      hash_h[7] = binaryStringAddition(hash_h[7], h, 32);
    }

    String digest = "";
    for (int i = 0; i < 8; i++) {
      digest += hash_h[i];
    }

    return binaryStringToHexadecimalString(digest);
  }



  public static String AESEncrypt(String plain_text, String secret_key) {
    
    String buffer_plain = "";
    String buffer_secret = "";

    for (int i = 0; i < plain_text.length(); i++) {
      int decimal = charToDecimal(plain_text.charAt(i));
      String binary_string = decimalToBinaryString(decimal, 8);
      buffer_plain += binary_string;
    }

    while (buffer_plain.length() < 256) {
      buffer_plain = '0' + buffer_plain;
    }

    System.out.println(buffer_plain);

    for (int i = 0; i < secret_key.length(); i++) {
      int decimal = charToDecimal(secret_key.charAt(i));
      String binary_string = decimalToBinaryString(decimal, 8);
      buffer_secret += binary_string;
    }

    while (buffer_secret.length() < 256) {
      buffer_secret = '0' + buffer_secret;
    }

    String[] round_buffer_secrets = getAESSecrets(buffer_secret, 16);

    for (int i = 0; i < round_buffer_secrets.length; i++) {
      buffer_plain = binaryStringXOR(buffer_plain, round_buffer_secrets[i]);
    }

    String digest = binaryStringToHexadecimalString(buffer_plain);

    return digest;
  }

  public static String AESDecrypt(String cypher, String secret_key) {
    
    String buffer_cypher;
    String buffer_secret = "";

    buffer_cypher = hexdecimalToDecimal(cypher);

    for (int i = 0; i < secret_key.length(); i++) {
      int decimal = charToDecimal(secret_key.charAt(i));
      String binary_string = decimalToBinaryString(decimal, 8);
      buffer_secret += binary_string;
    }

    while (buffer_secret.length() < 256) {
      buffer_secret = '0' + buffer_secret;
    }

    
    String[] round_buffer_secrets = getAESSecrets(buffer_secret, 16);

    for (int i = round_buffer_secrets.length - 1; i >= 0; i--) {
      buffer_cypher = binaryStringXOR(buffer_cypher, round_buffer_secrets[i]);
    }

    
    
    String plain_text = binaryStringToCharString(buffer_cypher, 8).trim();
    
    return plain_text;
  }


  private static String[] getAESSecrets(String buffer_secret, int number_of_secrets) {
    String[] round_buffer_secrets = new String[number_of_secrets];
    round_buffer_secrets[0] = buffer_secret;
    for (int i = 1; i < number_of_secrets; i++) {
      round_buffer_secrets[i] = decimalToBinaryString(0, 256);
      round_buffer_secrets[i] = binaryStringXOR(binaryStringRightRotate(round_buffer_secrets[i-1], i+17), round_buffer_secrets[i]);
    }
    return round_buffer_secrets;
  }




  private static int[] getFirstPrimes(int number_of_primes) {
    int[] primes = new int[number_of_primes];
    int index = 0;
    int number = 2;

    while (index < number_of_primes) {
      int deviders_count = 0;
      for (int devider = 1; devider <= number; devider++) {
        if (number % devider == 0) {
          deviders_count += 1;
        }
      }
      if (deviders_count == 2) {
        primes[index] = number;
        index += 1;
      } 
      number += 1;
    }

    return primes;
  }
  
  private static int charToDecimal(char character) { // ASCII
    return (int)character;
  }

  

  private static String decimalToBinaryString(int decimal, int bits) {
    String binary_string = "";
    while (decimal > 0){
      binary_string = decimal % 2 + binary_string;
      decimal /= 2;
    }
    
    while (binary_string.length() < bits) {
      binary_string = '0' + binary_string;
    }
    return binary_string;
  }

  private static String hexdecimalToDecimal(String hexdecimal) {
    String hexadecimal_digits = "0123456789abcdef";
    if (hexdecimal.length() == 1) {
      return decimalToBinaryString(hexadecimal_digits.indexOf(hexdecimal), 4); 
    }
    String binary_string = "";
    for (int i = 0; i < hexdecimal.length(); i++) {
      binary_string += hexdecimalToDecimal("" + hexdecimal.charAt(i));
    }
    return binary_string;
  }

  private static String fractionalDecimalToBinaryString(double fractional_decimal, int bits, int fraction_bits) {
    String binary_string = "";
    binary_string += (int)fractional_decimal;
    while (binary_string.length() < bits) {
      binary_string = '0' + binary_string;
    }
    binary_string += '.';
    for (int i = 0; i < fraction_bits; i++) {
      fractional_decimal = fractional_decimal - (int)fractional_decimal;
      fractional_decimal *= 2;
      binary_string += (int)fractional_decimal;
      
    }

    return binary_string;
  }

  private static String binaryStringToHexadecimalString(String binary_string) {
    String hexadecimal_digits = "0123456789abcdef";
    if (binary_string.length() <= 4) {
      int hexadecimal_value = 0;
      for (int i = 0; i < binary_string.length(); i++) {
        hexadecimal_value += Integer.parseInt("" + binary_string.charAt(binary_string.length() - 1 - i)) * Math.pow(2, i);
      }
      return "" + hexadecimal_digits.charAt(hexadecimal_value);
    }
    
    String hexadecimal_string = "";
    for (int i = binary_string.length(); i > 0; i-=4) {
      int start_index = (i - 4 >= 0) ? i - 4 : 0;
      hexadecimal_string = binaryStringToHexadecimalString(binary_string.substring(start_index, i)) + hexadecimal_string;
    }
    return hexadecimal_string;
  }

  private static String binaryStringToCharString(String binary_string, int bits) {
    if (binary_string.length() <= bits) {
      int decimal = 0;
      for (int i = 0; i < binary_string.length(); i++) {
        decimal += Integer.parseInt("" + binary_string.charAt(binary_string.length() - 1 - i)) * Math.pow(2, i);
      }
      return "" + (char)decimal;
    }

    String char_string = "";
    for (int i = binary_string.length(); i > 0; i-=bits) {
      int start_index = (i - bits >= 0) ? i - bits : 0;
      char_string += binaryStringToCharString(binary_string.substring(start_index, i), bits);
    }
    return char_string;
  }



  private static double[] sqrtOfArray(int[] array) {
    double[] new_array = new double[array.length];
    for (int i = 0; i < array.length; i++) {
      new_array[i] = Math.sqrt(array[i]);
    }
    return new_array;
  }

  private static double[] cbrtOfArray(int[] array) {
    double[] new_array = new double[array.length];
    for (int i = 0; i < array.length; i++) {
      new_array[i] = Math.cbrt(array[i]);
    }
    return new_array;
  }

  private static String[] fractionalDecimalToBinaryStringArray(double[] array, int bits, int fraction_bits) {
    String[] new_array = new String[array.length];
    for (int i = 0; i < array.length; i++) {
      new_array[i] = fractionalDecimalToBinaryString(array[i], bits, fraction_bits);
    }
    return new_array;
  }

  private static String splitFractionalPart(String fractional_string) {
    return fractional_string.split("\\.")[1];
  }

  private static String[] splitFractionalPartArray(String[] array) {
    String[] new_array = new String[array.length];
    for (int i = 0; i < array.length; i++) {
      new_array[i] = splitFractionalPart(array[i]);
    }
    return new_array;
  }

  private static String[] splitToArrayByBits(String buffer, int bits) {
    String[] new_array = new String[buffer.length()/bits];
    for (int i = 0; i < new_array.length; i++) {
      new_array[i] = buffer.substring(bits*i, bits*(i+1));
    }
    return new_array;
  }



  private static String binaryStringRightRotate(String binary_string, int value) {
    return binary_string.substring(binary_string.length()-value) + binary_string.substring(0, binary_string.length()-value);
  }

  private static String binaryStringRightShift(String binary_string, int value) {
    return decimalToBinaryString(0, value) + binary_string.substring(0, binary_string.length()-value);
  }



  private static String binaryStringAND(String binary_string_1, String binary_string_2) {
    String result = "";
    for (int i = 0; i < Math.min(binary_string_1.length(), binary_string_2.length()); i++) {
      result += (
        binary_string_1.charAt(i) == '1' && binary_string_2.charAt(i) == '1'
      ) ? '1' : '0';
    }
    return result;
  }

  private static String binaryStringOR(String binary_string_1, String binary_string_2) {
    String result = "";
    for (int i = 0; i < Math.min(binary_string_1.length(), binary_string_2.length()); i++) {
      result += (
        binary_string_1.charAt(i) == '1' || binary_string_2.charAt(i) == '1'
      ) ? '1' : '0';
    }
    return result;
  }

  private static String binaryStringNOT(String binary_string) {
    String result = "";
    for (int i = 0; i < binary_string.length(); i++) {
      result += (
        binary_string.charAt(i) == '0'
      ) ? '1' : '0';
    }
    return result;
  }

  private static String binaryStringXOR(String binary_string_1, String binary_string_2) {
    String result = "";
    for (int i = 0; i < Math.min(binary_string_1.length(), binary_string_2.length()); i++) {
      result += (
        binary_string_1.charAt(i) == '1' && binary_string_2.charAt(i) == '0' ||
        binary_string_1.charAt(i) == '0' && binary_string_2.charAt(i) == '1'
      ) ? '1' : '0';
    }
    return result;
  }



  private static String binaryStringAddition(String binary_string_1, String binary_string_2, String binary_string_3, String binary_string_4, String binary_string_5, int bits) {
    String result = binaryStringAddition(binaryStringAddition(binary_string_1, binary_string_2, binary_string_3, binary_string_4, bits), binary_string_5, bits);
    return result;
  }

  private static String binaryStringAddition(String binary_string_1, String binary_string_2, String binary_string_3, String binary_string_4, int bits) {
    String result = binaryStringAddition(binaryStringAddition(binary_string_1, binary_string_2, binary_string_3, bits), binary_string_4, bits);
    return result;
  }

  private static String binaryStringAddition(String binary_string_1, String binary_string_2, String binary_string_3, int bits) {
    String result = binaryStringAddition(binaryStringAddition(binary_string_1, binary_string_2, bits), binary_string_3, bits);
    return result;
  }

  private static String binaryStringAddition(String binary_string_1, String binary_string_2, int bits) {
    String result = "";
    int reminder = 0;
    for (int i = bits - 1; i >= 0; i--) {
      
      int local_result = Integer.parseInt("" + binary_string_1.charAt(i)) + Integer.parseInt("" + binary_string_2.charAt(i)) + reminder;
      reminder = (local_result > 1) ? 1 : 0;
      local_result %= 2;
      result = local_result + result;
    }
    return result;
  }



  private static void debug(String text, int bits, int bytes) {
    for (int i = 0; i < text.length(); i++) {
      System.out.print(text.charAt(i));
      if ((i+1) % bits == 0) {
        System.out.print(' ');
        if (((i+1) / bits) % bytes == 0 && i + 1 != text.length()) {
          System.out.println();
        }
      }
    }
    System.out.println();
  }

  private static void debug(String[] array) {
    System.out.print("{\n\t");
    for (int i = 0; i < array.length; i++) {
      System.out.print(array[i]);
      if (i != array.length - 1) {
        System.out.print(",\n\t");
      } else {
        System.out.print("\n}\n");
      }
    }
  }

  private static void debug(int[] array) {
    System.out.print("{\n\t");
    for (int i = 0; i < array.length; i++) {
      System.out.print(array[i]);
      if (i != array.length - 1) {
        System.out.print(",\n\t");
      } else {
        System.out.print("\n}\n");
      }
    }
  }

  private static void debug(double[] array) {
    System.out.print("{\n\t");
    for (int i = 0; i < array.length; i++) {
      System.out.print(array[i]);
      if (i != array.length - 1) {
        System.out.print(",\n\t");
      } else {
        System.out.print("\n}\n");
      }
    }
  }

  private static void debug(String text) {
    System.out.println(text);
  }

  private static void debug(int number) {
    System.out.println(number);
  }

  private static void debug(float number) {
    System.out.println(number);
  }

  private static void debug(double number) {
    System.out.println(number);
  }

  private static void debug(boolean bool) {
    System.out.println(bool);
  }
  
}