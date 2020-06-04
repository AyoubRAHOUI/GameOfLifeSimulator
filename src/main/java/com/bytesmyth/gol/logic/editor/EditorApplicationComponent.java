package com.bytesmyth.gol.logic.editor;

import com.bytesmyth.gol.ApplicationComponent;
import com.bytesmyth.gol.ApplicationContext;
import com.bytesmyth.gol.logic.ApplicationState;
import com.bytesmyth.gol.model.Board;
import com.bytesmyth.gol.model.BoundedBoard;
import com.bytesmyth.gol.state.EditorState;

public class EditorApplicationComponent implements ApplicationComponent {

    @Override
    public void initComponent(ApplicationContext context) {

        EditorState editorState = context.getStateRegistry().getState(EditorState.class);

        Editor editor = new Editor(editorState, context.getCommandExecutor());
        context.getEventBus().listenFor(DrawModeEvent.class, editor::handle);
        context.getEventBus().listenFor(BoardEvent.class, editor::handle);

        editorState.getCursorPosition().listen(cursorPosition -> {
            boardViewModel.getCursorPosition().set(cursorPosition);
        });

        appViewModel.getApplicationState().listen(editor::onAppStateChanged);
        appViewModel.getApplicationState().listen(newState -> {
            if (newState == ApplicationState.EDITING) {
                boardViewModel.getBoard().set(editorState.getEditorBoard().get());
                simulatorState.getBoard().set(editorState.getEditorBoard().get());
            }
        });
    }

    @Override
    public void initState(ApplicationContext context) {
        Board board = new BoundedBoard(context.getBoardWidth(), context.getBoardHeight());
        EditorState editorState = new EditorState(board);
        context.getStateRegistry().registerState(EditorState.class, editorState);
    }
}
