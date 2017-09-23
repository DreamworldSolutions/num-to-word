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
    switch (language) {
      case "en":
        return EnglishNumberToWord.getNumberToWord(number);
      case "hi":
        return HindiNumberToWord.getNumberToWord(number);
      case "guj":
        return GujaratiNumberToWord.getNumberToWord(number);
      default:
        return EnglishNumberToWord.getNumberToWord(number);
    }
  }
}
