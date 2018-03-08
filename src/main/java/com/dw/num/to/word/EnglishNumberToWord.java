package com.dw.num.to.word;

import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class EnglishNumberToWord {

  // @formatter:off
  private static final String[] TEXT_ONE_TO_NINE = {
      "Zero",
      "One",
      "Two",
      "Three",
      "Four",
      "Five",
      "Six",
      "Seven",
      "Eight",
      "Nine"
  };
  private static final String[] TEXT_UNITS_IN_WORDS = {
      "Crore",
      "Lacs",
      "Thousand",
      "Hundred"
  };
  private static final long[] UNITS_IN_NUMBER = {
      10000000,
      100000,
      1000,
      100
  };
  private static final String[] TEXT_ELEVEN_TO_NINTEEN = {
      "Eleven",
      "Twelve",
      "Thirteen",
      "Fourteen",
      "Fifteen",
      "Sixteen",
      "Seventeen",
      "Eighteen",
      "Nineteen"
  };
  private static final String[] TEXT_MULT_OF_TEN = {
      "Ten",
      "Twenty",
      "Thirty",
      "Forty",
      "Fifty",
      "Sixty",
      "Seventy",
      "Eighty",
      "Ninety"
  };
  // @formatter:on

  public static final String TEXT_RUPEES = "Rupees";
  public static final String TEXT_PAISE = "Paise";
  public static final String TEXT_ONLY = "Only";
  public static final String SPACE = " ";
  private static final String DECIMAL_VAL_TEXT = "decimalValText";
  private static final String WHOLE_VAL_TEXT = "wholeValText";

  private static final String FILE_PATH = "currency-text-en.json";
  private static Map<String, Map<String, String>> currencyText = new HashMap<>();

  static {
    InputStream in = EnglishNumberToWord.class.getClassLoader().getResourceAsStream(FILE_PATH);

    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.configure(Feature.AUTO_CLOSE_SOURCE, true);

    try {
      currencyText =
          objectMapper.readValue(in, new TypeReference<HashMap<String, Map<String, String>>>() {});
      System.out.println("EnglishNumberToWord :: Currency text loaded.");
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

    return getNumberToWord(number, WordUtils.capitalize(texts.get(WHOLE_VAL_TEXT)),
        WordUtils.capitalize(texts.get(DECIMAL_VAL_TEXT)));
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
    if (number < 10) {
      return TEXT_ONE_TO_NINE[number];
    }
    if (number % 10 == 0) {
      return TEXT_MULT_OF_TEN[(number / 10) - 1];
    }
    if (number > 10 && number < 20) {
      return TEXT_ELEVEN_TO_NINTEEN[number - 10 - 1];
    }
    StringBuilder sb = new StringBuilder();
    sb.append(TEXT_MULT_OF_TEN[(number / 10) - 1]);
    sb.append(" ");
    sb.append(TEXT_ONE_TO_NINE[number % 10]);
    return sb.toString();
  }
}
