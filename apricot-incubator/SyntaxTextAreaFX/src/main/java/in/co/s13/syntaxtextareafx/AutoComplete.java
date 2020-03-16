package in.co.s13.syntaxtextareafx;

///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package in.co.s13;
//
//import static in.co.s13.AutocompleteState.State.HIDE;
//import static in.co.s13.AutocompleteState.State.INSERTION;
//import static in.co.s13.AutocompleteState.State.REFILTER;
//import static in.co.s13.AutocompleteState.State.SHOW;
//import org.fxmisc.richtext.model.PlainTextChange;
//
///**
// *
// * @author nika
// */
//class AutocompleteState {
//
//    private static final String EMPTY_STRING = "";
//
//    private String filterText;
//    private State state;
//
//    public static enum State { SHOW, HIDE, REFILTER, INSERTION }
//
//    private AutocompleteState(String filterText, State state) {
//        this.filterText = filterText;
//        this.state = state;
//    }
//
//    public static AutocompleteState initial() {
//        return new AutocompleteState(EMPTY_STRING, State.HIDE);
//    }
//
//    public void showBox() {
//        state = SHOW;
//    }
//
//    public void hideBox() {
//        state = HIDE;
//        filterText = EMPTY_STRING;
//    }
//
//    // Note: this method's implementation is probably wrong because a PlainTextChange
//    //  can be merged with a previous one. Although I'm assuming a textChange in this case
//    //  only inserts or deletes one letter at a time, this may not be true in its actual usage.
//    public void updateFilter(PlainTextChange textChange) {
//        // Note: textChange.isInsertion() is pseudo code to demonstrate idea
//        if (textChange.isInsertion()) {
//            filterText += textChange.getInserted();
//        } else if (textChange.isDeletion()) {
//            filterText -= textChange.getRemoved();
//        }
//        state = !filterText.isEmpty() ? REFILTER : HIDE;
//    }
//    
//    public void insertSelected() {
//        state = INSERTION;
//    }
//
//}
//
//EventStream<?> appearanceTriggers = ...;
//EventStream<?> disappearanceTriggers = ...;
//EventStream<PlainTextChange> textModifications = area.plainTextChanges().conditionOn(popup.showingProperty());
//EventStream<?> insertionTriggers = ...;
//EventStream<AutocompleteState> boxEvents = StateMachine
//    .init(AutocompleteState.initial())
//    .on(appearanceTriggers).transition((appearance, state) -> state.showBox())
//    .on(disappearanceTriggers).transition((ignore, state) -> state.hideBox())
//    .on(textModifications).transition((textChange, state) -> state.updateFilter(textChange))
//    .on(insertionTriggers).transition((ignore, state) -> state.insertSelected())
//    .toEventStream();
//
//Subscription sub = boxEvents.subscribe(state -> {
//    switch (state.getType()) {
//        case: show
//            // show popup at caret bounds
//            Optional<Bounds> caretBounds = area.getCaretBounds();
//            if (caretBounds.isPresent()) { popup.show( /* code */); }
//            break;
//        case: hide
//            popup.hide()
//            break;
//        case: refilter
//            // assumes popup is already displayed
//            autocompleteBox.refilter(state.getFilterText());
//            // move autocompleteBox to new caret location
//            popup.show( /* new location */ );
//        default: case: insertion
//            area.insertText(state.getStartPosition(), autocompleteBox.getSelectedItem());
//            popu.hide();
//    }
//})