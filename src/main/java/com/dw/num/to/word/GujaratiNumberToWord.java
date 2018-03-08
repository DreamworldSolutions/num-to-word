package com.dw.num.to.word;

import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.lang3.StringUtils;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class GujaratiNumberToWord {

  private static final String[] TEXT_ONE_TO_NINETYNINE =
      {"શૂન્ય", "એક", "બે", "ત્રણ", "ચાર", "પાંચ", "છ", "સાત", "આઠ", "નવ", "દસ", "અગિયાર", "બાર",
          "તેર", "ચૌદ", "પંદર", "સોળ", "સત્તર", "અઢાર", "ઓગણીસ", "વીસ", "એકવીસ", "બાવીસ", "ત્રેવીસ",
          "ચોવીસ", "પચ્ચિસ", "છવ્વીસ", "સત્તાવીસ", "અઠયાવીસ", "ઓગણત્રીસ", "ત્રીસ", "એકત્રીસ",
          "બત્રીસ", "તેત્રીસ", "ચોત્રીસ", "પાત્રીસ", "છત્રીસ", "સાડત્રીસ", "આડત્રીસ", "ઓગણ ચાલીસ",
          "ચાલીસ", "એકતાળીસ", "બેતાલીસ", "તેતાલીસ", "ચુમ્માલીસ", "પિસ્તાલીસ", "છેતાલીસ", "સુડતાલીસ",
          "અડતાલીસ", "ઓગણપચાસ", "પચાસ", "એકાવન", "બાવન", "ત્રેપન", "ચોપન", "પંચાવન", "છપ્પન",
          "સત્તાવન", "અઠાવન", "ઓગણસાઠ", "સાઇઠ", "એકસઠ", "બાસઠ", "ત્રેસઠ", "ચોસઠ", "પાસંઠ", "છાસઠ",
          "સડસઠ", "અડસઠ", "ઓગણ સિતેર", "સિત્તેર", "એકોતેર", "બોતેર", "તોતેર", "ચુમોતેર", "પંચોતેર",
          "છોતેર", "સિત્યોતેર", "ઇઠ્યોતેર", "ઓગણાએંસી", "એંસી", "એક્યાસી", "બ્યાશી", "ત્રયાંસી",
          "ચોર્યાસી", "પંચાસી", "છ્યાસી", "સિત્યાસી", "ઈઠ્યાસી", "નેવ્યાસી", "નેવું", "એકાણું",
          "બાણું", "ત્રાણુ", "ચોરાણું", "પંચાણું", "છન્નું", "સત્તાણું", "અઠ્ઠાણું", "નવ્વાણું"};
  private static final String[] TEXT_UNITS_IN_WORDS = {"કરોડ઼", "લાખ", "હજાર", "સો"};
  private static final long[] UNITS_IN_NUMBER = {10000000, 100000, 1000, 100};

  public static final String TEXT_RUPEES = "રૂપિયા";
  public static final String TEXT_PAISE = "પૈસા";
  public static final String TEXT_ONLY = "કેવળ";
  public static final String SPACE = " ";
  public static final String TEXT_TWO_HUNDRED = "બસ્સો";
  private static final String DECIMAL_VAL_TEXT = "decimalValText";
  private static final String WHOLE_VAL_TEXT = "wholeValText";

  private static final String FILE_PATH = "currency-text-gu.json";
  private static Map<String, Map<String, String>> currencyText = new HashMap<>();

  static {
    InputStream in = GujaratiNumberToWord.class.getClassLoader().getResourceAsStream(FILE_PATH);

    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.configure(Feature.AUTO_CLOSE_SOURCE, true);

    try {
      currencyText =
          objectMapper.readValue(in, new TypeReference<HashMap<String, Map<String, String>>>() {});
      System.out.println("GujaratiNumberToWord :: Currency text loaded.");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Give word representation of given friction number in given language.
   * 
   * @param number need to represent in word
   */
  public static String getNumberToWord(String number) {
    return getNumberToWord(number, TEXT_RUPEES, TEXT_PAISE);
  }

  /**
   * Give word representation of given friction number in given currencyCode.
   * 
   * @param number need to represent in word
   */
  public static String getNumberToWord(String number, String currencyCode) {
    if (StringUtils.isBlank(currencyCode)) {
      return getNumberToWord(number);
    }
    Map<String, String> texts = currencyText.get(currencyCode);
    if (texts == null || texts.size() == 0) {
      return getNumberToWord(number);
    }

    return getNumberToWord(number, texts.get(WHOLE_VAL_TEXT), texts.get(DECIMAL_VAL_TEXT));
  }

  private static String getNumberToWord(String number, String rupeesText, String paiseText) {
    String[] parts = number.split("\\.");
    long primitive = Long.parseLong(parts[0]);
    StringBuilder sb = new StringBuilder();
    sb.append(getWordRepresentationForPrimitive(primitive));
    sb.append(" ");
    sb.append(rupeesText);
    if (parts.length == 2) {
      long fraction = Long.parseLong(parts[1]);
      if (fraction > 0) {
        if (sb.length() > 0) {
          sb.append(SPACE);
        }

        // Write fraction part
        sb.append(getWordRepresentationForPrimitive(fraction));
        if (StringUtils.isNotBlank(paiseText)) {
          sb.append(SPACE);
          sb.append(paiseText);
        }
      }
    }
    sb.append(SPACE);
    sb.append(TEXT_ONLY);
    return sb.toString();
  }

  /**
   * Give word representation of given nonfriction number in given language.
   * 
   * @param number need to represent in word
   */
  public static String getWordRepresentationForPrimitive(long number) {
    StringBuilder sb = new StringBuilder();
    if (number < 0) {
      return "";
    }
    if (number < 100) {
      return getWordRepresentation((int) number);
    }
    for (int i = 0; i < TEXT_UNITS_IN_WORDS.length; i++) {
      long unit = UNITS_IN_NUMBER[i];
      long numb = (number / unit);

      if (numb > 0) {
        if (numb > 99) {
          sb.append(getWordRepresentationForPrimitive(numb));
        }
        if (i == 3 && numb == 2) {
          sb.append(TEXT_TWO_HUNDRED);
          sb.append(SPACE);
        } else {
          sb.append(getWordRepresentation((int) (numb)));
          sb.append(SPACE);
          sb.append(TEXT_UNITS_IN_WORDS[i]);
          sb.append(SPACE);
        }
      }
      number = number % unit;
    }
    if (number > 0) {
      sb.append(getWordRepresentation((int) number));
    }
    return sb.toString().trim();
  }

  /**
   * returns word representation of the number < 100 (0-99).
   * 
   * @param number An integer between 0 and 99
   */
  public static String getWordRepresentation(int number) {
    if (number < 0 || number > 99) {
      return "";
    }
    return TEXT_ONE_TO_NINETYNINE[number];
  }
}
