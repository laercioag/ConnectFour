package com.lag.connectfour.game;

import android.content.Context;
import android.support.v4.content.Loader;

public class PresenterLoader extends Loader<IGameBoardPresenter> {

    private GameBoardPresenter presenter;

    PresenterLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        if (presenter != null) {
            deliverResult(presenter);
            return;
        }

        forceLoad();
    }

    @Override
    protected void onForceLoad() {
        presenter = new GameBoardPresenter();
        deliverResult(presenter);
    }

    @Override
    protected void onReset() {
        presenter.onDestroyed();
        presenter = null;
    }
}
