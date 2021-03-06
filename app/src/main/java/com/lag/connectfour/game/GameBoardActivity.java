package com.lag.connectfour.game;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.lag.connectfour.R;
import com.lag.connectfour.util.Constants;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;

public class GameBoardActivity extends AppCompatActivity implements IGameBoardView, LoaderManager.LoaderCallbacks<IGameBoardPresenter> {

    @BindView(R.id.fullscreen_content)
    View contentView;
    @BindView(R.id.disc_holder_layout)
    FrameLayout discHolderLayout;
    @BindView(R.id.board_image_view)
    ImageView boardImageView;
    @BindView(R.id.player_text_view)
    TextView playerTextView;
    @BindView(R.id.pause_fab)
    FloatingActionButton pauseFab;
    @BindView(R.id.close_fab)
    FloatingActionButton closeFab;
    @BindView(R.id.fullscreen_menu)
    View menuView;
    @BindView(R.id.during_game_controls)
    View duringGameControlsView;
    @BindView(R.id.after_game_controls)
    View afterGameControlsView;

    @BindString(R.string.player_one)
    String playerOne;
    @BindString(R.string.player_two)
    String playerTwo;
    @BindString(R.string.player_one_won)
    String playerOneWon;
    @BindString(R.string.player_two_won)
    String playerTwoWon;
    @BindString(R.string.draw)
    String draw;

    private static final int LOADER_ID = 101;
    private static final String MENU_VIEW_VISIBILITY = "menuViewVisibility";
    private static final String DURING_GAME_CONTROLS_VIEW_VISIBILITY = "duringGameControlsViewVisibility";
    private static final String AFTER_GAME_CONTROLS_VIEW_VISIBILITY = "afterGameControlsViewVisibility";
    private static final String PLAYER_TEXT_VIEW = "playerTextView";

    private IGameBoardPresenter presenter;
    private AnimatorSet animSetXY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_board);
        ButterKnife.bind(this);
        getSupportLoaderManager().initLoader(LOADER_ID, null, this).forceLoad();
    }

    @OnTouch(R.id.board_image_view)
    public boolean onPlayerMove(View view, MotionEvent motionEvent) {
        int width = view.getWidth();
        float touchX = motionEvent.getX();

        for (int col = 1; col <= Constants.COLUMNS; col++) {

            float wStart = ((width / Constants.BOARD_WIDTH) * (Constants.BOARD_PADDING + Constants.SLOT_MARGIN))
                    + ((width / (Constants.BOARD_WIDTH / Constants.SLOT_SIZE)) * Constants.ROWS * (col - 1));
            float wEnd = ((width / Constants.BOARD_WIDTH) * (Constants.BOARD_PADDING + Constants.SLOT_MARGIN))
                    + ((width / (Constants.BOARD_WIDTH / Constants.SLOT_SIZE)) * Constants.ROWS * (col));

            if (touchX >= wStart && touchX <= wEnd) {
                presenter.onPlayerMove(col);
            }
        }
        return false;
    }

    public void drawDisc(int column, int row, int drawable) {

        int height = boardImageView.getHeight();
        int width = boardImageView.getWidth();
        int top = boardImageView.getTop();

        row = Constants.ROWS - row + 1;

        float wStart = ((width / Constants.BOARD_WIDTH) * (Constants.BOARD_PADDING + Constants.SLOT_MARGIN))
                + ((width / (Constants.BOARD_WIDTH / Constants.SLOT_SIZE)) * Constants.ROWS * (column - 1));
        float wEnd = ((width / Constants.BOARD_WIDTH) * (Constants.BOARD_PADDING + Constants.SLOT_MARGIN))
                + ((width / (Constants.BOARD_WIDTH / Constants.SLOT_SIZE)) * Constants.ROWS * (column));
        float hStart = ((height / Constants.BOARD_HEIGHT) * Constants.SLOT_MARGIN)
                + ((height / (Constants.BOARD_HEIGHT / Constants.SLOT_SIZE)) * Constants.ROWS * (row - 1));
        float hEnd = ((height / Constants.BOARD_HEIGHT) * Constants.SLOT_MARGIN)
                + ((height / (Constants.BOARD_HEIGHT / Constants.SLOT_SIZE)) * Constants.ROWS * (row));

        int discSize = Math.round((width / (Constants.BOARD_WIDTH / Constants.SLOT_SIZE)) * 5);
        float discX = (wStart + ((wEnd - wStart) / 2) - (discSize / 2));
        float discY = (hStart + ((hEnd - hStart) / 2) - (discSize / 2));

        ImageView disc = new ImageView(this);
        disc.setId(View.generateViewId());
        disc.setImageResource(drawable);
        disc.setX(discX);
        disc.setY(top + discY);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(discSize, discSize);
        discHolderLayout.addView(disc, params);
    }

    public void drawAndAnimateDisc(int column, int row, final int drawable) {

        int height = boardImageView.getHeight();
        int width = boardImageView.getWidth();
        int top = boardImageView.getTop();

        row = Constants.ROWS - row + 1;

        float wStart = ((width / Constants.BOARD_WIDTH) * (Constants.BOARD_PADDING + Constants.SLOT_MARGIN))
                + ((width / (Constants.BOARD_WIDTH / Constants.SLOT_SIZE)) * Constants.ROWS * (column - 1));
        float wEnd = ((width / Constants.BOARD_WIDTH) * (Constants.BOARD_PADDING + Constants.SLOT_MARGIN))
                + ((width / (Constants.BOARD_WIDTH / Constants.SLOT_SIZE)) * Constants.ROWS * (column));
        float hStart = ((height / Constants.BOARD_HEIGHT) * Constants.SLOT_MARGIN)
                + ((height / (Constants.BOARD_HEIGHT / Constants.SLOT_SIZE)) * Constants.ROWS * (row - 1));
        float hEnd = ((height / Constants.BOARD_HEIGHT) * Constants.SLOT_MARGIN)
                + ((height / (Constants.BOARD_HEIGHT / Constants.SLOT_SIZE)) * Constants.ROWS * (row));

        int discSize = Math.round((width / (Constants.BOARD_WIDTH / Constants.SLOT_SIZE)) * 5);
        float discX = (wStart + ((wEnd - wStart) / 2) - (discSize / 2));
        float discY = (hStart + ((hEnd - hStart) / 2) - (discSize / 2));

        ImageView disc = new ImageView(this);
        disc.setId(View.generateViewId());
        disc.setImageResource(drawable);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(discSize, discSize);
        discHolderLayout.addView(disc, params);

        ObjectAnimator animY = ObjectAnimator.ofFloat(disc, "y", top - discSize, top + discY);
        ObjectAnimator animX = ObjectAnimator.ofFloat(disc, "x", discX, discX);
        animY.setInterpolator(new AccelerateInterpolator(1.0f));
        animY.setDuration(500);
        animSetXY = new AnimatorSet();
        animSetXY.playTogether(animY, animX);
        animSetXY.start();
        animSetXY.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if(drawable == R.drawable.player_one_disc_drawable) {
                    final Handler handler = new Handler();
                    handler.postDelayed(() -> setRequestedOrientation(ActivityInfo
                            .SCREEN_ORIENTATION_REVERSE_PORTRAIT), 500);
                } else {
                    final Handler handler = new Handler();
                    handler.postDelayed(() -> setRequestedOrientation(ActivityInfo
                            .SCREEN_ORIENTATION_PORTRAIT), 500);
                }
            }
        });
    }

    public void drawHighlightDisc(int column, int row, int drawable) {
        int height = boardImageView.getHeight();
        int width = boardImageView.getWidth();
        int top = boardImageView.getTop();

        row = Constants.ROWS - row + 1;

        float wStart = ((width / Constants.BOARD_WIDTH) * (Constants.BOARD_PADDING + Constants.SLOT_MARGIN))
                + ((width / (Constants.BOARD_WIDTH / Constants.SLOT_SIZE)) * Constants.ROWS * (column - 1));
        float wEnd = ((width / Constants.BOARD_WIDTH) * (Constants.BOARD_PADDING + Constants.SLOT_MARGIN))
                + ((width / (Constants.BOARD_WIDTH / Constants.SLOT_SIZE)) * Constants.ROWS * (column));
        float hStart = ((height / Constants.BOARD_HEIGHT) * Constants.SLOT_MARGIN)
                + ((height / (Constants.BOARD_HEIGHT / Constants.SLOT_SIZE)) * Constants.ROWS * (row - 1));
        float hEnd = ((height / Constants.BOARD_HEIGHT) * Constants.SLOT_MARGIN)
                + ((height / (Constants.BOARD_HEIGHT / Constants.SLOT_SIZE)) * Constants.ROWS * (row));

        int discSize = Math.round((width / (Constants.BOARD_WIDTH / Constants.SLOT_SIZE)) * 5);
        float discX = (wStart + ((wEnd - wStart) / 2) - (discSize / 2));
        float discY = (hStart + ((hEnd - hStart) / 2) - (discSize / 2));

        ImageView disc = new ImageView(this);
        disc.setId(View.generateViewId());
        disc.setImageResource(drawable);
        disc.setX(discX);
        disc.setY(top + discY);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(discSize, discSize);
        discHolderLayout.addView(disc, params);
    }

    @Override
    public void drawDiscPlayerOne(int column, int row) {
        drawAndAnimateDisc(column, row, R.drawable.player_one_disc_drawable);
    }

    @Override
    public void drawDiscPlayerTwo(int column, int row) {
        drawAndAnimateDisc(column, row, R.drawable.player_two_disc_drawable);
    }

    @Override
    public void redrawDiscPlayerOne(int column, int row) {
        drawDisc(column, row, R.drawable.player_one_disc_drawable);
    }

    @Override
    public void redrawDiscPlayerTwo(int column, int row) {
        drawDisc(column, row, R.drawable.player_two_disc_drawable);
    }

    @Override
    public void highlightDisc(final int column, final int row) {
        animSetXY.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                drawHighlightDisc(column, row, R.drawable.highlight_disc_drawable);
            }
        });
    }

    @Override
    public void setRoundPlayerOne() {
        setRoudPlayer(playerOne);
    }

    @Override
    public void setRoundPlayerTwo() {
        setRoudPlayer(playerTwo);
    }

    private void setRoudPlayer(final String player) {
        if(animSetXY != null) {
            if (animSetXY.isRunning()) {
                animSetXY.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        final Handler handler = new Handler();
                        handler.postDelayed(() -> playerTextView.setText(player), 500);
                    }
                });
            } else{
                playerTextView.setText(player);
            }
        } else {
            playerTextView.setText(player);
        }
    }

    @Override
    public void setWinnerPlayerOne() {
        animSetXY.removeAllListeners();
        animSetXY.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                playerTextView.setText(playerOneWon);
                switchGameControlsView();
            }
        });
    }

    @Override
    public void setWinnerPlayerTwo() {
        animSetXY.removeAllListeners();
        animSetXY.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
                playerTextView.setText(playerTwoWon);
                switchGameControlsView();
            }
        });
    }

    @Override
    public void setWinnerDraw() {
        animSetXY.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                playerTextView.setText(draw);
                switchGameControlsView();
            }
        });
    }

    @OnClick(R.id.pause_fab)
    public void showMenu() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {

            int cx = menuView.getWidth() - (closeFab.getHeight() / 2) - (int) getResources().getDimension(R.dimen.fab_margin);
            int cy = menuView.getHeight() - (closeFab.getHeight() / 2) - (int) getResources().getDimension(R.dimen.fab_margin);
            float finalRadius = (float) Math.hypot(cx, cy);

            Animator anim = ViewAnimationUtils.createCircularReveal(menuView, cx, cy, 0, finalRadius);
            menuView.setVisibility(View.VISIBLE);
            anim.start();

        } else {
            Animation slideIn = AnimationUtils.loadAnimation(this, R.anim.slide_in_animation);
            menuView.startAnimation(slideIn);
            menuView.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.close_fab)
    public void hideMenu() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {

            int cx = menuView.getWidth() / 2;
            int cy = menuView.getHeight() - (closeFab.getHeight() / 2) - (int) getResources().getDimension(R.dimen.fab_margin);
            float initialRadius = (float) Math.hypot(cx, cy);

            Animator anim = ViewAnimationUtils.createCircularReveal(menuView, cx, cy, initialRadius, 0);
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    menuView.setVisibility(View.INVISIBLE);
                }
            });
            anim.start();

        } else {
            Animation slideOut = AnimationUtils.loadAnimation(this, R.anim.slide_out_animation);
            menuView.startAnimation(slideOut);
            menuView.setVisibility(View.INVISIBLE);
        }
    }

    @OnClick({R.id.new_game_text_view, R.id.menu_new_game_text_view})
    public void newGame() {
        finish();
        startActivity(getIntent());
    }

    public void switchGameControlsView() {
        if (duringGameControlsView.getVisibility() == View.VISIBLE) {
            duringGameControlsView.setVisibility(View.INVISIBLE);
            afterGameControlsView.setVisibility(View.VISIBLE);
        } else {
            duringGameControlsView.setVisibility(View.VISIBLE);
            afterGameControlsView.setVisibility(View.INVISIBLE);
        }
    }

    @OnClick(R.id.menu_exit_game_text_view)
    public void exitGame() {
        this.finish();
    }

    @Override
    public void onBackPressed() {
        if (menuView.getVisibility() == View.VISIBLE) {
            hideMenu();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onViewAttached(this);
        onWindowFocusChanged(true);
    }

    @Override
    protected void onStop() {
        presenter.onViewDetached();
        super.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (menuView.getVisibility() == View.VISIBLE) {
            outState.putBoolean(MENU_VIEW_VISIBILITY, true);
        } else {
            outState.putBoolean(MENU_VIEW_VISIBILITY, false);
        }

        if (duringGameControlsView.getVisibility() == View.VISIBLE) {
            outState.putBoolean(DURING_GAME_CONTROLS_VIEW_VISIBILITY, true);
        } else {
            outState.putBoolean(DURING_GAME_CONTROLS_VIEW_VISIBILITY, false);
        }

        if (afterGameControlsView.getVisibility() == View.VISIBLE) {
            outState.putBoolean(AFTER_GAME_CONTROLS_VIEW_VISIBILITY, true);
        } else {
            outState.putBoolean(AFTER_GAME_CONTROLS_VIEW_VISIBILITY, false);
        }
        outState.putCharSequence(PLAYER_TEXT_VIEW, playerTextView.getText());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState.getBoolean(MENU_VIEW_VISIBILITY)) {
            menuView.setVisibility(View.VISIBLE);
        } else {
            menuView.setVisibility(View.INVISIBLE);
        }
        if (savedInstanceState.getBoolean(DURING_GAME_CONTROLS_VIEW_VISIBILITY)) {
            duringGameControlsView.setVisibility(View.VISIBLE);
        } else {
            duringGameControlsView.setVisibility(View.INVISIBLE);
        }
        if (savedInstanceState.getBoolean(AFTER_GAME_CONTROLS_VIEW_VISIBILITY)) {
            afterGameControlsView.setVisibility(View.VISIBLE);
        } else {
            afterGameControlsView.setVisibility(View.INVISIBLE);
        }
        playerTextView.setText(savedInstanceState.getCharSequence(PLAYER_TEXT_VIEW));
    }

    @NonNull
    @Override
    public Loader<IGameBoardPresenter> onCreateLoader(int id, Bundle args) {
        return new PresenterLoader(getApplicationContext());
    }

    @Override
    public void onLoadFinished(@NonNull Loader<IGameBoardPresenter> loader, IGameBoardPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onLoaderReset(@NonNull Loader<IGameBoardPresenter> loader) {
        this.presenter = null;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            contentView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

}
