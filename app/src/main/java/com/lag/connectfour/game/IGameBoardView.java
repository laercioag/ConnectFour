package com.lag.connectfour.game;

public interface IGameBoardView {

    void drawDiscPlayerOne(int column, int row);

    void drawDiscPlayerTwo(int column, int row);

    void redrawDiscPlayerOne(int column, int row);

    void redrawDiscPlayerTwo(int column, int row);

    void highlightDisc(int column, int row);

    void setRoundPlayerOne();

    void setRoundPlayerTwo();

    void setWinnerPlayerOne();

    void setWinnerPlayerTwo();

    void setWinnerDraw();

}
