package ar.edu.utn.frsfco.garlan.mam.commands;

import org.springframework.shell.core.CommandMarker;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.shell.core.annotation.CliOption;
import org.springframework.stereotype.Component;

/**
 * Command for fetch tweets data from Twitter API.
 *
 * <p><a href="TweetsCrawler.java.html"><i>View Source</i></a></p>
 *
 * @author <a href="mailto:eduardo.scarello@gmail.com">Eduardo Scarello</a>
 */
@Component
public class TweetsCrawler implements CommandMarker {

    @CliCommand(value = "twitter get-tweets-by-list-of-words", help = "Return tweets by list of words")
    public void getTweetsByListOfWords(
        @CliOption(key = "words", mandatory = true, help = "Add words seprated by comma") final String words
    ) {
        String[] listOfWords = words.split(",");

        for (String word: listOfWords) {
            System.out.println(word);
        }
    }
}
