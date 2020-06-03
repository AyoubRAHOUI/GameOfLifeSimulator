package com.tyjohtech.gol.logic.editor;

import com.tyjohtech.gol.command.Command;
import com.tyjohtech.gol.state.EditorState;

public interface EditorCommand extends Command<EditorState> {
    @Override
    void execute(EditorState editorState);

    @Override
    default Class<EditorState> getStateClass() {
        return EditorState.class;
    }
}
