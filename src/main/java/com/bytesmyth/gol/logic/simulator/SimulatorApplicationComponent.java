package com.bytesmyth.gol.logic.simulator;

import com.bytesmyth.gol.ApplicationComponent;
import com.bytesmyth.gol.ApplicationContext;
import com.bytesmyth.gol.model.Board;
import com.bytesmyth.gol.model.BoundedBoard;
import com.bytesmyth.gol.state.SimulatorState;

public class SimulatorApplicationComponent implements ApplicationComponent {

    @Override
    public void initComponent(ApplicationContext context) {
        SimulatorState simulatorState = context.getStateRegistry().getState(SimulatorState.class);

        Simulator simulator = new Simulator(appViewModel, simulatorState, commandExecutor);
        context.getEventBus().listenFor(SimulatorEvent.class, simulator::handle);
        editorState.getEditorBoard().listen(editorBoard -> {
            simulatorState.getBoard().set(editorBoard);
            boardViewModel.getBoard().set(editorBoard);
        });
        simulatorState.getBoard().listen(simulationBoard -> {
            boardViewModel.getBoard().set(simulationBoard);
        });
    }

    @Override
    public void initState(ApplicationContext context) {
        Board board = new BoundedBoard(context.getBoardWidth(), context.getBoardHeight());
        SimulatorState simulatorState = new SimulatorState(board);
        context.getStateRegistry().registerState(SimulatorState.class, simulatorState);
    }
}
