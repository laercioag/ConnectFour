package com.lag.connectfour.game;

public interface IGameBoardPresenter{

    void onViewAttached(IGameBoardView view);

    void onViewDetached();

    void onDestroyed();

    void onPlayerMove(int col);

    void redraw();
}