package com.lag.connectfour.data;

import com.lag.connectfour.game.IGameListener;

import java.util.ArrayList;
import java.util.List;

public class Game {

    public Disc[][] board;
    private Player roundPlayer;
    private Player winnerPlayer;
    private List<Disc> winnerDiscs;
    private IGameListener listener;

    private Boolean isFinished;

    public Game(IGameListener listener) {
        this.listener = listener;
        newGame();
    }

    private void newGame() {
        board = new Disc[7][8];
        roundPlayer = Player.PLAYER_ONE;
        winnerPlayer = null;
        winnerDiscs = new ArrayList<>();
        isFinished = false;
    }

    public void playerMove(int col) {
        if(!isFinished) {
            if (col >= 1 && col <= 7) {
                for (int row = 1; row <= 6; row++) {
                    if (board[row][col] == null) {
                        board[row][col] = new Disc(row, col, roundPlayer);
                        notifyPlayerMove(board[row][col]);

                        if(hasWinner() || isBoardFull()) {
                            isFinished = true;
                            notifyGameFinished();
                        } else {
                            switchPlayer();
                        }
                        break;
                    }
                }
            }
        }
    }

    private void switchPlayer() {
        if(roundPlayer == Player.PLAYER_ONE) {
            setRoundPlayer(Player.PLAYER_TWO);
        } else {
            setRoundPlayer(Player.PLAYER_ONE);
        }
        notifyRoundPlayer();
    }

    private boolean isBoardFull() {
        for(int row = 1; row <= 6; row++) {
            for(int col = 1; col <= 7; col++) {
                if(board[row][col] == null) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean hasWinner() {

        for(int row = 1; row <= 6; row++) {
            for(int col = 1; col <=7; col++) {
                if(board[row][col] != null) {
                    if(col + 3 <= 7) {
                        if(board[row][col + 1] != null && board[row][col + 2] != null &&  board[row][col + 3] != null) {
                            if(board[row][col].getPlayer() == board[row][col + 1].getPlayer() &&
                                    board[row][col].getPlayer() == board[row][col + 2].getPlayer() &&
                                    board[row][col].getPlayer() == board[row][col + 3].getPlayer()){
                                setWinnerPlayer(board[row][col].getPlayer());
                                setWinnerDiscs(board[row][col], board[row][col + 1], board[row][col + 2], board[row][col + 3]);
                                return true;
                            }
                        }
                    }
                    if(row + 3 <= 6) {
                        if(board[row + 1][col] != null && board[row + 2][col] != null &&  board[row + 3][col] != null) {
                            if (board[row][col].getPlayer() == board[row + 1][col].getPlayer() &&
                                    board[row][col].getPlayer() == board[row + 2][col].getPlayer() &&
                                    board[row][col].getPlayer() == board[row + 3][col].getPlayer()) {
                                setWinnerPlayer(board[row][col].getPlayer());
                                setWinnerDiscs(board[row][col], board[row + 1][col], board[row + 2][col], board[row + 3][col]);
                                return true;
                            }
                        }
                    }
                    if(col + 3 <= 7 && row + 3 <= 6) {
                        if(board[row + 1][col + 1] != null && board[row + 2][col + 2] != null &&  board[row + 3][col + 3] != null) {
                            if (board[row][col].getPlayer() == board[row + 1][col + 1].getPlayer() &&
                                    board[row][col].getPlayer() == board[row + 2][col + 2].getPlayer() &&
                                    board[row][col].getPlayer() == board[row + 3][col + 3].getPlayer()) {
                                setWinnerPlayer(board[row][col].getPlayer());
                                setWinnerDiscs(board[row][col], board[row + 1][col + 1], board[row + 2][col + 2], board[row + 3][col + 3]);
                                return true;
                            }
                        }
                    }
                    if(col + 3 <= 7 && row - 3 >= 1) {
                        if(board[row - 1][col + 1] != null && board[row - 2][col + 2] != null &&  board[row - 3][col + 3] != null) {
                            if (board[row][col].getPlayer() == board[row - 1][col + 1].getPlayer() &&
                                    board[row][col].getPlayer() == board[row - 2][col + 2].getPlayer() &&
                                    board[row][col].getPlayer() == board[row - 3][col + 3].getPlayer()) {
                                setWinnerPlayer(board[row][col].getPlayer());
                                setWinnerDiscs(board[row][col], board[row - 1][col + 1], board[row - 2][col + 2], board[row - 3][col + 3]);
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public Player getRoundPlayer() {
        return roundPlayer;
    }

    public void setRoundPlayer(Player roundPlayer) {
        this.roundPlayer = roundPlayer;
    }

    private Player getWinnerPlayer() {
        return winnerPlayer;
    }

    private void setWinnerPlayer(Player winnerPlayer) {
        this.winnerPlayer = winnerPlayer;
    }

    private List<Disc> getWinnerDiscs() {
        return winnerDiscs;
    }

    private void setWinnerDiscs(Disc one, Disc two, Disc three, Disc four) {
        winnerDiscs.add(one);
        winnerDiscs.add(two);
        winnerDiscs.add(three);
        winnerDiscs.add(four);
    }

    private void notifyPlayerMove(Disc disc) {
        listener.playerMove(disc);
    }

    private void notifyRoundPlayer() {
        listener.setRoundPlayer(roundPlayer);
    }

    private void notifyGameFinished() {
        listener.gameFinished(winnerPlayer, winnerDiscs);
    }



}
