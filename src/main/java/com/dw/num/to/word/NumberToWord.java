package com.dw.num.to.word;

public class NumberToWord {

  /**
   * Provide Word representation of given number in given language.
   * 
   * @param number need to represent in to word
   * @param language code of language in which number need to represent
   * @return word representation of given number in given language
   */
  public static String numToWord(String number, String language) {
    return numToWord(number, language, null);
  }

  /**
   * Provide Word representation of given number in given language.
   * 
   * @param number need to represent in to word
   * @param language code of language in which number need to represent
   * @param currencyCode of currency in which word are use in convert amount in word.
   * @return word representation of given number in given language
   */
  public static String numToWord(String number, String language, String currencyCode) {
    switch (language) {
      case "en":
        return EnglishNumberToWord.getNumberToWord(number, currencyCode);
      case "hi":
        return HindiNumberToWord.getNumberToWord(number, currencyCode);
      case "gu":
        return GujaratiNumberToWord.getNumberToWord(number, currencyCode);
      default:
        return EnglishNumberToWord.getNumberToWord(number, currencyCode);
    }
  }
}
