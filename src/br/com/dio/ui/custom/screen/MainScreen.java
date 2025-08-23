package br.com.dio.ui.custom.screen;

import br.com.dio.model.Space;
import br.com.dio.service.BoardService;
import br.com.dio.ui.custom.button.FinishGameButton;
import br.com.dio.ui.custom.button.ResetButton;
import br.com.dio.ui.custom.frame.MainFrame;
import br.com.dio.ui.custom.input.NumberText;
import br.com.dio.ui.custom.panel.MainPanel;
import br.com.dio.ui.custom.panel.SudokuSector;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Map;
import static javax.swing.JOptionPane.QUESTION_MESSAGE;
import static javax.swing.JOptionPane.YES_NO_OPTION;

public class MainScreen {

    private final static Dimension dimension = new Dimension(600,600);

    private final BoardService boardService;

    private JButton checkStatusGameButton;
    private JButton finishGameButton;
    private JButton resetButton;

    public MainScreen(final Map<String,String> gameConfig, JButton checkStatusGameButton) {
        this.boardService = new BoardService(gameConfig);
        this.checkStatusGameButton = checkStatusGameButton;
    }
    public void buildMainScreeen(){
        JPanel mainPanel = new MainPanel(dimension);
        JFrame mainFrame = new MainFrame(dimension, mainPanel);
        for (int r = 0; r < 9; r+=3) {
            var endRow = r + 2;
            for (int c = 0; c < 9; c+=3) {
                var endCol = c + 2;
                var spaces = getSpacesFromSector(boardService.getSpace(),c, endCol, r, endRoW);
                JPanel sector = generateSection(spaces);
                mainPanel.add(sector);
            }
        }
        addResetButton(mainPanel);
        addCheckGameStatusButton(mainPanel);
        addFinishGameButton(mainPanel);
        mainFrame.revalidate();
        mainFrame.repaint();
    }
    private List<Space> getSpacesFromSector(final List<<List<Space>> spaces,
                                            final int initCol, final int endCol,
                                            final int initRow, final int endRow){
        List<Space> spaceSector = new ArrayList<>();
        for (int r = initRow; r <= endRow; r++) {
            for (int c = initCol; c <= endCol; c++) {
                spaceSector.add(spaces.get(c).get(r));
            }
            return spaceSector;
        }
    }
    private JPanel generateSection(final List<Space> spaces){
        List<NumberText> fields = new ArrayList<>(spaces.stream().map(NumberText :: new).toList());
        return new SudokuSector(fields);
    }

    private void addFinishGameButton(JPanel mainPanel) {
        finishGameButton = new FinishGameButton(e->{
            if(boardService.gameIsFinished()){
                JOptionPane.showMessageDialog(null,"Parabens você concluiu o jogo");
            }

        });
    }

    private void addCheckGameStatusButton(JPanel mainPanel) {
        JButton checkGameStatusButton = new checkGameStatusButton(e->{
            boolean hasErros = boardService.hasErrors();
            var gameStatus= boardService.getStatus();
            var message= switch (gameStatus){

                case NON_STARTED -> " O jogo não foi iniciado";
                case INCOMPLETE -> " O jogo está incompleto";
                case COMPLETE -> "O jogo está completo";
            };
            message += hasErros? " e contém erros" : " não contém erros";
            JOptionPane.showMessageDialog(null, message);
        });
        mainPanel.add(checkGameStatusButton);
        mainPanel.add(checkStatusGameButton);
    }

    private void addResetButton(JPanel mainPanel) {
        resetButton = new ResetButton(e ->{
            var dialogResult = JOptionPane.showConfirmDialog(
                    null,
                    "Deseja realmete reiniciar o jogo ?",
                    "Limpar o Jogo",
                    YES_NO_OPTION,
                    QUESTION_MESSAGE
            );
            if(dialogResult == 0){
                boardService.reset();
            }
        });
        mainPanel.add(resetButton);
    }
}
