package za.co.apricotdb.syntaxtext;

import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;

import javafx.concurrent.Task;
import za.co.apricotdb.syntaxtext.meta.Language;

/**
 * This is adaptation of the original library from github. It only supports SQL.
 * 
 * @author Anton Nazarov
 * @since 17/03/2020
 */
public class SyntaxTextAreaFX extends CodeArea {

    private Pattern PATTERN;
    private ExecutorService executor;
    private Language lang;

    public SyntaxTextAreaFX() {
        getStylesheets().add(SyntaxTextAreaFX.class.getResource("sql.css").toExternalForm());
        lang = new Sql();
        PATTERN = lang.generatePattern();

        executor = Executors.newSingleThreadExecutor(runnable -> {
            Thread thread = Executors.defaultThreadFactory().newThread(runnable);
            thread.setDaemon(true);
            return thread;
        });

        setParagraphGraphicFactory(LineNumberFactory.get(this));

        this.richChanges().filter(ch -> !ch.getInserted().equals(ch.getRemoved()))
                .successionEnds(Duration.ofMillis(500)).supplyTask(this::computeHighlightingAsync)
                .awaitLatest(this.richChanges()).filterMap(t -> {
                    if (t.isSuccess()) {
                        return Optional.of(t.get());
                    } else {
                        t.getFailure().printStackTrace();
                        return Optional.empty();
                    }
                }).subscribe(this::applyHighlighting);
    }

    public void setText(String text) {
        replaceText(0, getText().length(), text);
        getUndoManager().forgetHistory();
        getUndoManager().mark();
    }

    public CodeArea getNode() {
        return this;
    }

    private Task<StyleSpans<Collection<String>>> computeHighlightingAsync() {
        String text = getText();
        Task<StyleSpans<Collection<String>>> task = new Task<StyleSpans<Collection<String>>>() {
            @Override
            protected StyleSpans<Collection<String>> call() throws Exception {
                return computeHighlighting(text);
            }
        };
        executor.execute(task);
        return task;
    }

    private void applyHighlighting(StyleSpans<Collection<String>> highlighting) {
        this.setStyleSpans(0, highlighting);
    }

    private StyleSpans<Collection<String>> computeHighlighting(String text) {
        Matcher matcher = PATTERN.matcher(text);
        int lastKwEnd = 0;
        StyleSpansBuilder<Collection<String>> spansBuilder = new StyleSpansBuilder<>();
        while (matcher.find()) {
            String styleClass = lang.getStyleClass(matcher);
            /* never happens */ assert styleClass != null;
            spansBuilder.add(Collections.emptyList(), matcher.start() - lastKwEnd);
            spansBuilder.add(Collections.singleton(styleClass), matcher.end() - matcher.start());
            lastKwEnd = matcher.end();
        }
        spansBuilder.add(Collections.emptyList(), text.length() - lastKwEnd);
        return spansBuilder.create();
    }
}
