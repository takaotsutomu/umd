package student_classes;
import java.util.ArrayList;
import java.util.List;

/**
 * When you do a web search, the results page shows you a 
 * <a href="http://searchengineland.com/anatomy-of-a-google-snippet-38357">snippet</a> 
 * for each result, showing you search terms in context. For purposes of this project, a
 * snippet is a subsequence of a document that contains all the search terms.
 * 
 * For this project, you will write code that, given a document (a sequence of
 * words) and set of search terms, find the minimal length subsequence in the
 * document that contains all of the search terms.
 * 
 * If there are multiple subsequences that have the same minimal length, you may
 * return any one of them.
 * 
 */
public class MinimumSnippet {
	private Iterable<String> document;
	private List<String> terms;
	private boolean hasAllTerms;
	private int length;
	private int startingPos;
	private int endingPos;
	/**
	 * Compute minimum snippet.
	 * 
	 * Given a document (represented as a <code>List<String></code>), and a set of terms
	 * (also represented as a <code>List<String></code>), find the shortest subsequence of the
	 * document that contains all of the terms.
	 * 
	 * This constructor should find the minimum snippet, and store information
	 * about the snippet in fields so that the methods can be called to query
	 * information about the snippet. All significant computation should be done
	 * during construction.
	 * 
	 * @param document
	 *            The Document is an <code>Iterable<String></code>. Do not change the
	 *            document. It is an <code>Iterable</code>, rather than a <code>List</code>, to allow for
	 *            implementations where we scan very large documents that are
	 *            not read entirely into memory. If you have problems figuring
	 *            out how to solve it with the document represented as an
	 *            <code>Iterable</code>, you may cast it to a <code>List<String></code>; in all but a very
	 *            small number of test cases, in will in fact be a <code>List<String></code>.
	 * 
	 * @param terms
	 *            The terms you need to look for. The terms will be unique
	 *            (e.g., no term will be repeated), although you do not need to
	 *            check for that. There should always be at least one term and 
	 *            your code should
	 *            <code>throw</code> an <code>IllegalArgumentException</code> if "terms" is
	 *            empty.
	 * 
	 * 
	 */
	public MinimumSnippet(Iterable<String> document, List<String> terms) {
		if (terms.isEmpty()) { 
			throw new IllegalArgumentException();
		}
		//store the document and term list for future use
		this.document = document;
		this.terms = terms;
		//create data structures to store possible snippet
		ArrayList<String> snippet = new ArrayList<String>(); 
		ArrayList<Integer> indice = new ArrayList<Integer>();
		//calculate the length of the document
		int lengthOfDocument = 0;
		for (String word : document) {
			lengthOfDocument++;
		}
		//set the initial value of minimum snippet property
		length = lengthOfDocument;
		startingPos = 0;
		endingPos = 0;
		int index = 0;
		//check every word in document and see if one match one of the term in term list
		for (String word : document) {
			for (int i = 0; i < terms.size(); i++) {
				//if the word match one of the term in term list, put the word and its index
				//into the data structures
				if (word.equals(terms.get(i))) {
					snippet.add(word);
					indice.add(index);
					//check if the first term in the data structure appear more than once
					//if so, remove it and its index from the data structures
					for (int k = 1; k < snippet.size();) {
						if (snippet.get(0).equals(snippet.get(k))) {
							snippet.remove(0);
							indice.remove(0);
							k = 1;
						} else {
							k++;
						}
					}
					//count the number of terms in the data structure
					int numOfTermFound = 0;
					for (int j = 0; j < terms.size(); j++) {
						for (int k = 0; k < snippet.size(); k++) {
							if (terms.get(j).equals(snippet.get(k))) {
								if (numOfTermFound < terms.size()) {
									numOfTermFound++;
								}
							}
						}
					}
					//if the number of terms found match the size of the term list
					//the program found all terms in the document
					if (numOfTermFound == terms.size()) {
						hasAllTerms = true;
						//calculate the length of the snippet 
						int lengthOfSnippet = Math.abs(indice.get(0) - indice.get(indice.size() - 1)) + 1;
						//if the shortest snippet so far is found update the minimum snippet information
						if (lengthOfSnippet <= length) {
							length = lengthOfSnippet;
							startingPos = indice.get(0);
							endingPos = indice.get(indice.size() - 1);
						}
					}
				}
			}
			//count the index
			index++;
		}
		//if not all the terms were found, the length of the minimum snippet should be 0
		if (!hasAllTerms) {
			length = 0;
		}
	}
	/**
	 * Returns whether or not all terms were found in the document. If all terms
	 * were not found, then none of the other methods should be called.
	 * 
	 * @return whether all terms were found in the document.
	 */
	public boolean foundAllTerms() {
		return hasAllTerms;
	}

	/**
	 * Return the starting position of the snippet
	 * 
	 * @return the index in the document of the first element of the snippet
	 */
	public int getStartingPos() {
		if (!hasAllTerms) {
			throw new UnsupportedOperationException("No snippet is found");
		}
		return startingPos;
	}

	/**
	 * Return the ending position of the snippet
	 * 
	 * @return the index in the document of the last element of the snippet
	 */
	public int getEndingPos() {
		if (!hasAllTerms) {
			throw new UnsupportedOperationException("No snippet is found");
		}
		return endingPos;
	}

	/**
	 * Return total number of elements contained in the snippet.
	 * 
	 * @return an integer greater than or equal to 1
	 */
	public int getLength() {
		if (!hasAllTerms) {
			throw new UnsupportedOperationException("No snippet is found");
		}
		return length;
	}

	/**
	 * Returns the position of one of the search terms as it appears in the original document
	 * 
	 * @param index index of the term in the original list of terms.  For example, if index
	 * is 0 then the method will return the position (in the document) of the first search term.
	 * If the index is 1, then the method will return the position (in the document) of the
	 * second search term.  Etc.
	 *  
	 * @return position of the term in the document
	 */
	public int getPos(int index) {
		if (!hasAllTerms) {
			throw new UnsupportedOperationException("No snippet is found");
		}
		boolean hasFoundFirstPos = false;
		int pos = 0;
		int indexInDoc = 0;
		for (String word : document) {
			if (word.equals(terms.get(index)) && indexInDoc >= startingPos && indexInDoc <= endingPos) {
				if (!hasFoundFirstPos) {
					pos = indexInDoc;
					hasFoundFirstPos = true;
				}
			}
			indexInDoc++;
		}
		return pos;
	}

}
