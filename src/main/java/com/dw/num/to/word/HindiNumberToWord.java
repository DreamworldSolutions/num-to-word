package com.dw.num.to.word;

import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.lang3.StringUtils;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class HindiNumberToWord {

  private static final String[] TEXT_ONE_TO_NINETYNINE = {"शून्य", "एक", "दो", "तीन", "चार", "पांच",
      "छह", "सात", "आठ", "नौ", "दस", "ग्यारह", "बारह", "तेरह", "चौदह", "पंद्रह", "सोलह", "सत्रह",
      "अठारह", "उन्नीस", "बीस", "इक्कीस", "बाईस", "तेईस", "चौबीस", "पच्चीस", "छब्बीस", "सत्ताईस",
      "अट्ठाइस", "उन्तीस", "तीस", "इकत्तीस", "बत्तीस", "तेंतीस", "चौंतीस", "पैंतीस", "छत्तीस",
      "सैंतीस", "अड़तीस", "उनतालीस", "चालीस", "इकतालीस", "बयालीस", "तैंतालीस", "चवालीस", "पैंतालीस",
      "छियालीस", "सैंतालीस", "अड़तालीस", "उनचास", "पचास", "इक्यावन", "बावन", "तिरपन", "चौवन",
      "पचपन", "छप्पन", "सत्तावन", "अट्ठावन", "उनसठ", "साठ", "इकसठ", "बासठ", "तिरसठ", "चौंसठ",
      "पैसठ", "छियासठ", "सड़सठ", "अड़सठ", "उनहत्तर", "सत्तर", "इकहत्तर", "बहत्तर", "तिहत्तर",
      "चौहत्तर", "पचहत्तर", "छिहत्तर", "सतहत्तर", "अठहत्तर", "उन्यासी", "अस्सी", "इक्यासी", "बयासी",
      "तिरासी", "चौरासी", "पचासी", "छियासी", "सत्तासी", "अठासी", "नवासी", "नब्बे", "इक्यानबे",
      "बयान्वे", "तिरानवे", "चौरानवे", "पंचानवे", "छियानबे", "सत्तानवे", "अठानवे", "निन्यानवे"};
  private static final String[] TEXT_UNITS_IN_WORDS = {"करोड़", "लाख", "हज़ार", "सौ"};
  private static final long[] UNITS_IN_NUMBER = {10000000, 100000, 1000, 100};

  public static final String TEXT_RUPEES = "रुपये";
  public static final String TEXT_PAISE = "पैसे";
  public static final String TEXT_ONLY = "केवल";
  public static final String SPACE = " ";
  private static final String DECIMAL_VAL_TEXT = "decimalValText";
  private static final String WHOLE_VAL_TEXT = "wholeValText";

  private static final String FILE_PATH = "currency-text-hi.json";
  private static Map<String, Map<String, String>> currencyText = new HashMap<>();

  static {
    InputStream in = HindiNumberToWord.class.getClassLoader().getResourceAsStream(FILE_PATH);

    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.configure(Feature.AUTO_CLOSE_SOURCE, true);

    try {
      currencyText =
          objectMapper.readValue(in, new TypeReference<HashMap<String, Map<String, String>>>() {});
      System.out.println("HindiNumberToWord :: Currency text loaded.");
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
    sb.append(SPACE);
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
        sb.append(getWordRepresentation((int) (numb)));
        sb.append(SPACE);
        sb.append(TEXT_UNITS_IN_WORDS[i]);
        sb.append(SPACE);
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
