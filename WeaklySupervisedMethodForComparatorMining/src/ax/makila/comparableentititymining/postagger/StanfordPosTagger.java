package ax.makila.comparableentititymining.postagger;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class StanfordPosTagger {
	// Initialize the tagger
	private static MaxentTagger tagger = new MaxentTagger(
			"edu/stanford/nlp/models/pos-tagger/english-left3words/english-left3words-distsim.tagger");


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
	 * occurence of a lone $ and a c to $c. Adds #start and #end to each sentence if missing.
	 * @param question The input string
	 * @return A sentence separated array of tagged words
	 */
	public static List<List<CompTaggedWord>> tagStringHandleIdentifier(String question) {
		List<List<CompTaggedWord>> compTaggedWords = new ArrayList<List<CompTaggedWord>>();
		List<ArrayList<TaggedWord>> taggedWords = tagString(question);
		ListIterator<ArrayList<TaggedWord>> iterator = taggedWords.listIterator();

		while(iterator.hasNext()) {
			ArrayList<TaggedWord> word = iterator.next();
			//If the first token in each sentence doesn't match "#start", add "#start"
			if(!word.get(0).value().equals("#start")) {
				TaggedWord tagged = new TaggedWord("#start");
				tagged.setTag("#");
				tagged.setValue("#start");
				word.add(0, tagged);
			}
			//If the last token in each sentence doesn't match "#end", add "#end"
			if(!word.get(word.size() - 1).value().equals("#end")) {
				TaggedWord tagged = new TaggedWord("#end");
				tagged.setTag("#");
				tagged.setValue("#end");
				word.add(word.size(), tagged);
			}
		}
		
		//Transform all taggedwords to my own tagged version which is better suited for this task
		for(int i = 0; i < taggedWords.size(); i++) {
			ArrayList<TaggedWord> innerList = taggedWords.get(i);
			List<CompTaggedWord> compInnerList = new ArrayList<CompTaggedWord>(); 
			for(TaggedWord word : innerList) {
				CompTaggedWord comp = new CompTaggedWord(word);
				compInnerList.add(comp);
			}
			compTaggedWords.add(compInnerList);
		}
		
		for(List<CompTaggedWord> wordList : compTaggedWords) {
			ListIterator<CompTaggedWord> it = wordList.listIterator();
			while(it.hasNext()) {
				CompTaggedWord word = it.next();
				if(word.value().equals("#start") || word.value().equals("#end")) {
					word.setTag("#");
				}
				//The tagger splits $c to $ and c so we have to merge them
				else if(word.value().equals("$") && it.hasNext()) {
					CompTaggedWord next = it.next();
					if(next.value().equals("c")) {
						it.remove();
						it.previous();
						word.setValue("$c");
						word.setTag(next.tag());
						word.setCompTag(CompTaggedWord.COMP_TAG);
					}
					else {
						it.previous();
					}
				}
			}
		}
		
		return compTaggedWords;
		
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
						it.remove();
						int prevIndex = it.previousIndex();
						it.previous();
						token.set(prevIndex, "$c");
					}
					else {
						it.previous();
					}
				}
			}
			
			
		}
		return tokens;
	}

	
	
}
