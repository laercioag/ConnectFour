package com.lag.connectfour.game;

import com.lag.connectfour.data.Disc;
import com.lag.connectfour.data.Game;
import com.lag.connectfour.data.Player;

import java.util.List;

public class GameBoardPresenter implements IGameBoardPresenter, IGameListener {

    private IGameBoardView view;
    private Game game;

    GameBoardPresenter() {
        newGame();
    }

    @Override
    public void onViewAttached(IGameBoardView view) {
        this.view = view;
        setRoundPlayer(game.getRoundPlayer());
        redraw();
    }

    @Override
    public void onViewDetached() {
        //Not sure
        this.view = null;
    }

    @Override
    public void onDestroyed() {
        this.view = null;
        this.game = null;
    }

    @Override
    public void newGame() {
        this.game = new Game(this);
    }

    @Override
    public void onPlayerMove(int col) {
        game.playerMove(col);
    }

    @Override
    public void playerMove(Disc disc) {
        if (view != null) {
            if (disc.getPlayer() == Player.PLAYER_ONE) {
                view.drawDiscPlayerOne(disc.getColumn(), disc.getRow());
            } else {
                view.drawDiscPlayerTwo(disc.getColumn(), disc.getRow());
            }
        }
    }

    @Override
    public void setRoundPlayer(Player player) {
        if (view != null) {
            if (player == Player.PLAYER_ONE) {
                view.setRoundPlayerOne();
            } else {
                view.setRoundPlayerTwo();
            }
        }
    }

    @Override
    public void gameFinished(Player player, List<Disc> winnerDiscs) {
        if (view != null) {
            if (player == null) {
                view.setWinnerDraw();
                return;
            }
            for (Disc disc : winnerDiscs) {
                view.highlightDisc(disc.getColumn(), disc.getRow());
            }
            if (player == Player.PLAYER_ONE) {
                view.setWinnerPlayerOne();
            } else {
                view.setWinnerPlayerTwo();
            }
        }
    }

    // This method probably brakes the concept of MVP
    @Override
    public void redraw() {
        for (int row = 1; row <= 6; row++) {
            for (int col = 1; col <= 7; col++) {
                if (game.board[row][col] != null) {
                    if (game.board[row][col].getPlayer() == Player.PLAYER_ONE) {
                        view.redrawDiscPlayerOne(col, row);
                    } else if (game.board[row][col].getPlayer() == Player.PLAYER_TWO) {
                        view.redrawDiscPlayerTwo(col, row);
                    }
                }
            }
        }
    }

}
