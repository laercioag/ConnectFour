package com.lag.connectfour.game;

import com.lag.connectfour.data.Disc;
import com.lag.connectfour.data.Player;

import java.util.List;

public interface IGameListener {

    void newGame();

    void playerMove(Disc disc);

    void switchPlayer(Player player);

    void gameFinished(Player player, List<Disc> winnerDiscs);

}
