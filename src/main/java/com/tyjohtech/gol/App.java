package com.tyjohtech.gol;

import com.tyjohtech.gol.logic.*;
import com.tyjohtech.gol.model.Board;
import com.tyjohtech.gol.model.BoundedBoard;
import com.tyjohtech.gol.state.EditorState;
import com.tyjohtech.gol.util.event.EventBus;
import com.tyjohtech.gol.view.InfoBar;
import com.tyjohtech.gol.view.MainView;
import com.tyjohtech.gol.view.SimulationCanvas;
import com.tyjohtech.gol.view.Toolbar;
import com.tyjohtech.gol.viewmodel.BoardViewModel;
import com.tyjohtech.gol.viewmodel.InfoBarViewModel;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {

    @Override
    public void start(Stage stage) {
        EventBus eventBus = new EventBus();

        ApplicationStateManager appViewModel = new ApplicationStateManager();
        BoardViewModel boardViewModel = new BoardViewModel();
        Board board = new BoundedBoard(20, 12);

        EditorState editorState = new EditorState(board);
        Editor editor = new Editor(editorState);
        eventBus.listenFor(DrawModeEvent.class, editor::handle);
        eventBus.listenFor(BoardEvent.class, editor::handle);
        editorState.getCursorPosition().listen(cursorPosition -> {
            boardViewModel.getCursorPosition().set(cursorPosition);
        });

        Simulator simulator = new Simulator(appViewModel);
        eventBus.listenFor(SimulatorEvent.class, simulator::handle);
        editorState.getEditorBoard().listen(editorBoard -> {
            simulator.getInitialBoard().set(editorBoard);
            boardViewModel.getBoard().set(editorBoard);
        });
        simulator.getCurrentBoard().listen(simulationBoard -> {
            boardViewModel.getBoard().set(simulationBoard);
        });

        appViewModel.getApplicationState().listen(editor::onAppStateChanged);
        appViewModel.getApplicationState().listen(newState -> {
            if (newState == ApplicationState.EDITING) {
                boardViewModel.getBoard().set(editorState.getEditorBoard().get());
            }
        });

        boardViewModel.getBoard().set(board);


        SimulationCanvas simulationCanvas = new SimulationCanvas(boardViewModel, eventBus);
        Toolbar toolbar = new Toolbar(eventBus);

        InfoBarViewModel infoBarViewModel = new InfoBarViewModel();
        editorState.getCursorPosition().listen(cursorPosition -> {
            infoBarViewModel.getCursorPosition().set(cursorPosition);
        });
        editorState.getDrawMode().listen(drawMode -> {
            infoBarViewModel.getCurrentDrawMode().set(drawMode);
        });
        InfoBar infoBar = new InfoBar(infoBarViewModel);

        MainView mainView = new MainView(eventBus);
        mainView.setTop(toolbar);
        mainView.setCenter(simulationCanvas);
        mainView.setBottom(infoBar);

        Scene scene = new Scene(mainView, 1200, 800);
        stage.setScene(scene);
        stage.show();


    }


    public static void main(String[] args) {
        launch();
    }

}