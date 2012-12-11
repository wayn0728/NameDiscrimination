package parser;

import java.util.HashSet;
import java.util.Set;

import org.tartarus.snowball.ext.PorterStemmer;

public class Stemmer {
	public static final String[] ENGLISH_STOP_WORDS = {
		"a", "an", "and", "are", "as", "at", "be", "but", "by",
		"for", "if", "in", "into", "is", "it",
		"no", "not", "of", "on", "or", "such",
		"that", "the", "their", "then", "there", "these",
		"they", "this", "to", "was", "will", "with", " "
		};
	static Set stopList = new HashSet<String>();
	Stemmer() {
		for (String s: ENGLISH_STOP_WORDS) {
			stopList.add(s);
		}
	}
	
	public static boolean isStop(String word) {
		if (stopList.contains(word))
			return true;
		else return false;
	}
	public static String stem(String word) {
		PorterStemmer stem = new PorterStemmer();
		stem.setCurrent(word);
		stem.stem();
		String result = stem.getCurrent();
		return result;
	}

	public static void main(String[] args) {
//		System.out.print(Stemmer.Stem("hope."));
	}
}
