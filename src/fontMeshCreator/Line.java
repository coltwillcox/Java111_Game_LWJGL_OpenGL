package fontMeshCreator;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a line of text during the loading of a text.
 */

public class Line {

	private double maxLength;
	private double spaceSize;
    private double currentLineLength = 0;
	private List<Word> words = new ArrayList<>();

	/**
	 * Creates an empty line.
	 * @param spaceWidth - The screen-space width of a space character.
	 * @param fontSize - The size of font being used.
	 * @param maxLength - The screen-space maximum length of a line.
	 */
	//Constructor.
    protected Line(double spaceWidth, double fontSize, double maxLength) {
		this.spaceSize = spaceWidth * fontSize;
		this.maxLength = maxLength;
	}

	/**
	 * Attempt to add a word to the line. If the line can fit the word in
	 * without reaching the maximum line length then the word is added and the
	 * line length increased.
	 * @param word - The word to try to add.
	 */
	protected boolean attemptToAddWord(Word word) {
		double additionalLength = word.getWordWidth();
		additionalLength += !words.isEmpty() ? spaceSize : 0;
		if (currentLineLength + additionalLength <= maxLength) {
			words.add(word);
			currentLineLength += additionalLength;
			return true;
		} else
			return false;
	}

	protected double getMaxLength() {
		return maxLength;
	}

	protected double getLineLength() {
		return currentLineLength;
	}

	protected List<Word> getWords() {
		return words;
	}

}