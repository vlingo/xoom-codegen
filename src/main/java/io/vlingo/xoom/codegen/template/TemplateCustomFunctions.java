package io.vlingo.xoom.codegen.template;

import freemarker.template.utility.StringUtil;

import java.beans.Introspector;

public class TemplateCustomFunctions {

  private static final char[] VOWEL_CHARS = {'a', 'e', 'i', 'o', 'u'};

  private static TemplateCustomFunctions instance;

  public static TemplateCustomFunctions instance() {
    if(instance == null) {
      instance = new TemplateCustomFunctions();
    }
    return instance;
  }

  private TemplateCustomFunctions() {
  }

  private boolean isVowel(char ch) {
    for (char vch : VOWEL_CHARS) {
      if (ch == vch){
        return true;
      }
    }
    return false;
  }

  /**
   * TODO: improve
   */
  public String makePlural(final String word) {
    if (word.endsWith("s") || word.endsWith("sh") || word.endsWith("ch") || word.endsWith("z")) {
      return word + "es";
    }else
    if (word.endsWith("y") && !isVowel(word.charAt(word.length()-2))) {
      return word.substring(0, word.length()-1) + "ies";
    }else{
      return word + "s";
    }
  }

  public String capitalize(String word) {
    char ch = word.charAt(0);
    if (Character.isUpperCase(ch)) {
      return word;
    }else{
      return Character.toUpperCase(ch) + word.substring(1);
    }
  }

  public String capitalizeMultiWord(final String word) {
    StringBuilder result = new StringBuilder();
    int i = 0;
    int j;
    while (true) {
      j = word.indexOf(' ', i);
      if (j==-1){
        result.append(capitalize(word.substring(i)));
        break;
      }else{
        result.append(capitalize(word.substring(i, ++j)));
      }
      i = j;
    }
    return result.toString();
  }

  public String decapitalize(final String word) {
    return Introspector.decapitalize(word);
  }
}
