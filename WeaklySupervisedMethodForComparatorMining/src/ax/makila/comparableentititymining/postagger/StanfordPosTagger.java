package ax.makila.comparableentititymining.postagger;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class StanfordPosTagger {
	// Initialize the tagger
	private static MaxentTagger tagger = new MaxentTagger(
			"edu/stanford/nlp/models/pos-tagger/english-left3words/english-left3words-distsim.tagger");

	/**
	 * Splits the document into sentences, then splits each sentence into words
	 * after which POS tags are added.
	 * 
	 * @param documentURL
	 *            The document to be tagged
	 * @return A list containing a list of sentences containing tokenized words
	 *         with associated POS tags
	 */
	public static List<ArrayList<TaggedWord>> tagDocument(String documentURL) {
		List<List<HasWord>> sentences = null;
		try {
			sentences = MaxentTagger.tokenizeText(new FileReader("test.txt"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<ArrayList<TaggedWord>> taggedWords = tagger.process(sentences);
		return taggedWords;
	}

	/**
	 * Splits the document into sentences, then splits each sentence into words
	 * after which POS tags are added.
	 * 
	 * @param question
	 *            The string to be tagged
	 * @return A list containing a list of sentences containing tokenized words
	 *         with associated POS tags
	 */
	public static List<ArrayList<TaggedWord>> tagString(String question) {
		List<List<HasWord>> sentences = null;
		sentences = MaxentTagger.tokenizeText(new StringReader(question));
		List<ArrayList<TaggedWord>> taggedWords = tagger.process(sentences);
		return taggedWords;
	}
	
	/**
	 * Tags a string but sets a special pos tag, "#" to #start and #end and merges any
	 * occurence of a lone $ and a c to $c.
	 * @param question The input string
	 * @return A sentence separated array of tagged words
	 */
	public static List<ArrayList<TaggedWord>> tagStringHandleIdentifier(String question) {
		List<ArrayList<TaggedWord>> taggedWords = tagString(question);
		
		for(ArrayList<TaggedWord> wordList : taggedWords) {
			ListIterator<TaggedWord> it = wordList.listIterator();
			while(it.hasNext()) {
				TaggedWord word = it.next();
				if(word.equals("#start") || word.equals("#end")) {
					word.setTag("#");
				}
				//The tagger splits $c to $ and c so we have to merge them
				else if(word.equals("$") && it.hasNext()) {
					TaggedWord next = it.next();
					if(next.equals("c")) {
						word.setValue("$c");
						word.setTag(next.tag());
						it.remove();
					}
					else {
						it.previous();
					}
				}
			}
		}
		
		return taggedWords;
		
	}
	
	
	

	/**
	 * Tags the words in the document for each sentence and return them as
	 * String representation
	 * 
	 * @param documentURL
	 *            The document to be tagged
	 * @return A string with attached POS tags for each word
	 */
	public static List<String> tagDocumentToString(String documentURL) {
		List<String> taggedStrings = new ArrayList<String>();
		List<ArrayList<TaggedWord>> taggedSentences = tagDocument(documentURL);
		for (ArrayList<TaggedWord> sentence : taggedSentences) {
			StringBuilder out = new StringBuilder();
			for (TaggedWord t : sentence) {
				out.append(t.toString());
				out.append(" ");
			}

			String s = PTBTokenizer.ptb2Text(out.toString());
			taggedStrings.add(s);
		}
		return taggedStrings;
	}

	/**
	 * Gives a list of POS tags from the input string
	 * 
	 * @param question
	 *            The string to be tagged
	 * @return A list containing POS tags generated from each word in the
	 *         question
	 */
	public static List<List<String>> getStringTags(String question) {
		List<List<String>> tags = new ArrayList<List<String>>();
		List<List<HasWord>> sentences = MaxentTagger
				.tokenizeText(new StringReader(question));
		List<ArrayList<TaggedWord>> taggedWords = tagger.process(sentences);
		for (int i = 0; i < taggedWords.size(); i++) {
			ArrayList<TaggedWord> sentence = taggedWords.get(i);
			ArrayList<String> inner = new ArrayList<String>();
			for (TaggedWord tag : sentence) {
				inner.add("_" + tag.tag());
			}
			tags.add(inner);
		}
		return tags;
	}

	/**
	 * Tokenizes the input question into sentences and tokenize each sentence
	 * into words.
	 * 
	 * @param question
	 *            The string containing a question to be tokenized.
	 * @return A double list split into sentences containing words.
	 */
	public static List<List<String>> tokenizeString(String question) {
		List<List<String>> tags = new ArrayList<List<String>>();
		List<List<HasWord>> sentences = MaxentTagger
				.tokenizeText(new StringReader(question));
		List<ArrayList<TaggedWord>> taggedWords = tagger.process(sentences);
		for (int i = 0; i < taggedWords.size(); i++) {
			ArrayList<TaggedWord> sentence = taggedWords.get(i);
			ArrayList<String> inner = new ArrayList<String>();
			for (TaggedWord tag : sentence) {
				inner.add(tag.value());
			}
			tags.add(inner);
		}
		return tags;

	}
	
	/**
	 * Tokenizes the string and merge $ and c to $c 
	 * @param question The question to be tokenized
	 * @return The tokenized question
	 */
	public static List<List<String>> tokenizeStringMergeComp(String question) {
		List<List<String>> tokens = tokenizeString(question);
		for(List<String> token : tokens) {
			ListIterator<String> it = token.listIterator();
			while(it.hasNext()) {
				String t = it.next();
				if(t.equals("$") && it.hasNext()) {
					String n = it.next();
					if(n.equals("c")) {
						t = "$c";
						it.remove();
					}
					else {
						it.previous();
					}
				}
			}
			
		}
		return tokens;
	}
	

	/**
	 * Tags a string with it's POS tags and then returns a string representation
	 * of said string.
	 * 
	 * @param question
	 *            The string to be tagged
	 * @return A string where each word has an associated POS tag
	 */
	public static List<String> tagStringToString(String question) {
		List<String> tags = new ArrayList<String>();
		List<ArrayList<TaggedWord>> taggedWords = tagString(question);
		for (ArrayList<TaggedWord> words : taggedWords) {
			StringBuilder out = new StringBuilder();
			for (TaggedWord tagged : words) {
				out.append(tagged.toString());
				out.append(" ");
			}

			String taggedString = PTBTokenizer.ptb2Text(out.toString());
			tags.add(taggedString);
		}
		return tags;
	}

	/**
	 * Given an input document specified by documentURL. Splits the document
	 * into sentences after which each sentence is split into word tokens. These
	 * words are then tagged with their POS tags after which only the POS tags
	 * are returned as a word list separated into sentences.
	 * 
	 * @param documentURL
	 *            The document URL for the text document to be read
	 * @return A double list split into sentences and words containing POS tags
	 *         generated from the sentences in the document.
	 */
	public static List<ArrayList<String>> getTagsFromDocument(String documentURL) {
		List<ArrayList<String>> tags = new ArrayList<ArrayList<String>>();
		List<ArrayList<TaggedWord>> taggedSentences = tagDocument(documentURL);
		for (int i = 0; i < taggedSentences.size(); i++) {
			ArrayList<TaggedWord> sentence = taggedSentences.get(i);
			ArrayList<String> inner = new ArrayList<String>();
			for (TaggedWord tag : sentence) {
				inner.add("_" + tag.tag());
			}
			tags.add(inner);
		}
		return tags;
	}

	/**
	 * This one is similar to {@link #getTagsFromDocument(String)} but instead
	 * of returning only the POS tags, it only returns the words. It's
	 * effectively a sentence and word tokenizer.
	 * 
	 * @see #getTagsFromDocument(String)
	 * @param documentURL
	 *            The document to be tokenized
	 * @return The document found in documentURL tokenized into sentences and
	 *         words.
	 */
	public static List<ArrayList<String>> getTokenizedString(String documentURL) {
		List<ArrayList<String>> tokens = new ArrayList<ArrayList<String>>();
		List<List<HasWord>> tokenized = null;
		try {
			tokenized = MaxentTagger.tokenizeText(new FileReader("test.txt"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (int i = 0; i < tokenized.size(); i++) {
			ArrayList<String> inner = new ArrayList<String>();
			List<HasWord> words = tokenized.get(i);
			for (HasWord word : words) {
				inner.add(word.toString());
			}
			tokens.add(inner);
		}

		return tokens;
	}

	/**
	 * Returns a string representation of a list containing a string. The list
	 * is assumed to contain one sentence.
	 * 
	 * @param tokenizedString
	 *            The list containing words from one sentence
	 * @return A string representation of the list
	 */
	public static String unTokenizeString(List<String> tokenizedString) {
		StringBuilder sb = new StringBuilder();
		for (String s : tokenizedString) {
			sb.append(s);
			sb.append(" ");
		}
		String untokenized = PTBTokenizer.ptb2Text(sb.toString());
		return untokenized;
	}

	/**
	 * Tokenizes the String but removes tags from #start, #end and merge the occurence of
	 * $ and c to $c
	 * @param string The input string to be tagged
	 * @return A PosTag object containing the tagged string
	 */
	public static PosTag posTagString(String string) {
		List<List<HasWord>> sentences = null;
		sentences = MaxentTagger.tokenizeText(new StringReader(string));
		List<ArrayList<TaggedWord>> taggedWords = tagger.process(sentences);
		StringBuilder sb = new StringBuilder();
		Pattern cPattern = Pattern.compile("^c$");
		// Remove tags from #start and #end and merge $ and c to $c
		for (ArrayList<TaggedWord> list : taggedWords) {
			ListIterator<TaggedWord> it = list.listIterator();
			while (it.hasNext()) {
				TaggedWord tag = it.next();
				if (tag.value().equals("#start") || tag.value().equals("#end")) {
					sb.append(tag.value());
					sb.append(" ");
				} else if (tag.value().equals("$") && tag.tag().equals("$")
						&& it.hasNext()) {
					TaggedWord next = it.next();
					Matcher m = cPattern.matcher(next.value());
					if (m.matches()) {
						it.remove();
						it.previous();
						tag.setValue("$c");
						tag.setTag(next.tag());
						sb.append(tag);
						sb.append(" ");
					} else {
						it.previous();
					}
				} else {
					sb.append(tag);
					sb.append(" ");
				}
			}
		}
		PosTag posTag = new PosTag(taggedWords, PTBTokenizer.ptb2Text(sb
				.toString()));
		return posTag;
	}

	public static class PosTag {
		public String stringRepresentation;
		public List<ArrayList<TaggedWord>> taggedWords;

		public PosTag(List<ArrayList<TaggedWord>> taggedWords, String stringRep) {
			this.taggedWords = taggedWords;
			this.stringRepresentation = stringRep;
		}

		@Override
		public String toString() {
			return stringRepresentation;
		}

		public void updateStringRepresentation() {
			StringBuilder sb = new StringBuilder();
			boolean prevComp = false;
			for (ArrayList<TaggedWord> list : taggedWords) {
				Iterator<TaggedWord> it = list.iterator();
				while (it.hasNext()) {
					TaggedWord tag = it.next();
					if (tag.value().equals("#start")
							|| tag.value().equals("#end")) {
						sb.append(tag.value());
						sb.append(" ");
					} else if (tag.value().equals("$") && tag.tag().equals("$")
							&& it.hasNext()) {
						tag.setValue("$c");
						tag.setTag("NN");
						sb.append(tag);
						prevComp = true;
						sb.append(" ");
					} else if (prevComp && tag.value().equals("c")) {
						// Do nothing! Ignore it
					} else {
						sb.append(tag);
						sb.append(" ");
					}
				}
			}
			String str = PTBTokenizer.ptb2Text(sb.toString());
			stringRepresentation = str;
		}
	}
}
