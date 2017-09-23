package com.dw.num.to.word;

public class EnglishNumberToWord {
  
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

  public static final String TEXT_RUPEES = "Rupees";
  public static final String TEXT_PAISE = "Paise";
  public static final String TEXT_ONLY = "Only";
  public static final String SPACE = " ";

  /**
   * Give word representation of given friction number in given language.
   * 
   * @param number need to represent in word
   */
  public static String getNumberToWord(String number) {
    String[] parts = number.split("\\.");
    long primitive = Long.parseLong(parts[0]);
    StringBuilder sb = new StringBuilder();
    sb.append(getWordRepresentationForPrimitive(primitive));
    sb.append(" ");
    sb.append(TEXT_RUPEES);
    if (parts.length == 2) {
      long fraction = Long.parseLong(parts[1]);
      if (fraction > 0) {
        if (sb.length() > 0) {
          sb.append(SPACE);
        }
        sb.append(getWordRepresentationForPrimitive(fraction));
        sb.append(SPACE);
        sb.append(TEXT_PAISE);
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
