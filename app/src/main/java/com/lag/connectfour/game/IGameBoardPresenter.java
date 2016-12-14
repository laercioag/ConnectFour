package com.lag.connectfour.game;

import java.util.List;

public interface IGameBoardPresenter {

    void onCreate();

    void onPause();

    void onResume();

    void onDestroy();

    void onPlayerMove(int col);
}