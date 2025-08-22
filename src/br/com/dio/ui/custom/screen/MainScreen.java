package br.com.dio.ui.custom.screen;

import br.com.dio.service.BoardService;
import br.com.dio.ui.custom.button.FinishGameButton;
import br.com.dio.ui.custom.button.ResetButton;
import br.com.dio.ui.custom.frame.MainFrame;
import br.com.dio.ui.custom.panel.MainPanel;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

import static br.com.dio.model.GameStatusEnum.*;
import static javax.swing.JOptionPane.QUESTION_MESSAGE;
import static javax.swing.JOptionPane.YES_NO_OPTION;

public class MainScreen {

    private final static Dimension dimension = new Dimension(600,600);

    private final BoardService boardService;

    private JButton checkStatusGameButton;
    private JButton finishGameButton;
    private JButton resetButton;

    public MainScreen(final Map<String,String> gameConfig) {
        this.boardService = new BoardService(gameConfig);
    }
    public void buildMainScreeen(){
        JPanel mainPanel = new MainPanel(dimension);
        JFrame mainFrame = new MainFrame(dimension, mainPanel);
        addResetButton(mainPanel)
        addCheckGameStatusButton(mainPanel)
        addFinishGameButton(mainPanel)
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    private void addFinishGameButton(JPanel mainPanel) {
        finishGameButton = new FinishGameButton(e->{
            if(boardService.gameIsFinished()){
                JOptionPane.showMessageDialog(null,"Parabens você concluiu o jogo");
            }

        });


    }

    private void addCheckGameStatusButton(JPanel mainPanel) {
        JButton checkGameStatusButton = new FinishGameButton(e ->{
            boolean hasErros:boolean = boardService.hasErrors();
            var gameStatus:gameStatusEnum = boardService.getStatus();
            var message:String = switch (gameStatus){

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
