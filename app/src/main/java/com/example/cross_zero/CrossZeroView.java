package com.example.cross_zero;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_MOVE;
import static android.view.MotionEvent.ACTION_UP;

public class CrossZeroView extends View {

    private static final String NONE = "Ничья";
    private float scale = (float) getWidth() / 1036;
    private static final Paint BACKGROUND_PAINT = new Paint(Paint.ANTI_ALIAS_FLAG);
    private static final Paint LINES_PAINT = new Paint(Paint.ANTI_ALIAS_FLAG);
    private float STROKE_WIDTH = 16 * scale;
    private float LINES_WIDTH = 10 * scale;
    private float TEXT_WIDTH = (float) 4 * scale;
    private static final RectF FIELD_RECT = new RectF();
    private static final Rect CROSS_RECT = new Rect();
    private static final Rect CIRCLE_RECT = new Rect();
    private static final Rect CROSS_TEXT_RECT = new Rect();
    private static final Rect CIRCLE_TEXT_RECT = new Rect();
    private static final Paint TEXT_PAINT = new Paint(Paint.ANTI_ALIAS_FLAG);
    private float TEXT_SIZE = (float) 90 * scale;
    private final float startPoint = 1.5f * STROKE_WIDTH;
    private float LENGTH = getWidth() - 2f * STROKE_WIDTH;
    private float divider = LENGTH / 3;
    private static final String CROSSES = "Крестики";
    private static final String ZEROES = "Нолики";
    private static Path lines = new Path();
    private Rect textBounds = new Rect();
    private static TicTacToeField tictactoefield = new TicTacToeField(3);
    private static int step = 0;
    private static int numberCrossVicts = 0;
    private static int numberCircleVicts = 0;
    private EndGame endGame;
    private ShowDialog dialog;
    private boolean onFirstDrawed = true;
    private static boolean gameOver = false;


    public void setProgress(int progress) {
        this.progress = progress;
        invalidate();
        Log.d("CUSTOM_VIEW", "progress = " + this.progress);
    }

    private int progress = 1;
    DialogFragment.OnDialogClickListener onDialogClickListener = new DialogFragment.OnDialogClickListener() {
        @Override
        public void onExitClick() {
            endGame.endingOfGame();
        }

        @Override
        public void onAgainClick() {
            tictactoefield = new TicTacToeField(3);
            gameOver = false;
            step = 0;
            invalidate();
        }
    };

    public CrossZeroView(Context context, ShowDialog showDialog, EndGame endGame) {
        this(context, null, 0);
        this.dialog = showDialog;
        this.endGame = endGame;
    }

    public CrossZeroView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CrossZeroView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (onFirstDrawed) {
            scaling();
            init();
            onFirstDrawed = false;
        }

        FIELD_RECT.set(STROKE_WIDTH, STROKE_WIDTH, canvas.getWidth() - STROKE_WIDTH, canvas.getWidth() - STROKE_WIDTH);
        canvas.drawRect(FIELD_RECT, BACKGROUND_PAINT);
        drawLines(canvas);
        drawScore(canvas);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (tictactoefield.getFigure(i, j) == TicTacToeField.Figure.CROSS) {
                    Drawable cross = getResources().getDrawable(R.drawable.cross, null);
                    CROSS_RECT.set((int) (1.5f * STROKE_WIDTH + j * divider), (int) (1.5f * STROKE_WIDTH + i * divider), (int) (0.75f * STROKE_WIDTH + (j + 1) * divider), (int) (0.75f * STROKE_WIDTH + (i + 1) * divider));
                    cross.setBounds(CROSS_RECT);
                    cross.draw(canvas);
                } else if (tictactoefield.getFigure(i, j) == TicTacToeField.Figure.CIRCLE) {
                    Drawable circle = getResources().getDrawable(R.drawable.circle, null);
                    CIRCLE_RECT.set((int) (1.5f * STROKE_WIDTH + j * divider), (int) (1.5f * STROKE_WIDTH + i * divider), (int) (0.75f * STROKE_WIDTH + (j + 1) * divider), (int) (0.75f * STROKE_WIDTH + (i + 1) * divider));
                    circle.setBounds(CIRCLE_RECT);
                    circle.draw(canvas);
                }
            }
        }
    }

    private void scaling() {
        scale = (float) getWidth() / 1036;
        STROKE_WIDTH = 16 * scale;
        LENGTH = getWidth() - 2f * STROKE_WIDTH;
        divider = LENGTH / 3;
        TEXT_WIDTH = (float) 4 * scale;
        TEXT_SIZE = (float) 90 * scale;
        LINES_WIDTH = 10 * scale;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        float eventX = event.getX();
        float eventY = event.getY();
        if (gameOver) {
            return false;
        }
        TicTacToeField.Figure figure = step % 2 == 0 ? TicTacToeField.Figure.CROSS : TicTacToeField.Figure.CIRCLE;
        int col = (int) (eventX - startPoint) / (int) divider;
        int row = (int) (eventY - startPoint) / (int) divider;
        if (col > 2 || row > 2)
            return false;
        switch (action) {
            case ACTION_DOWN:
                if (!tictactoefield.isEmptyCell(row, col)) {
                    return false;
                }
                tictactoefield.setFigure(row, col, figure);
                return true;
            case ACTION_MOVE:
                break;
            case ACTION_UP:
                break;
            default:
                return false;
        }
        invalidate();
        if (tictactoefield.getWinner() != TicTacToeField.Figure.NONE) {
            if (tictactoefield.getWinner() == TicTacToeField.Figure.CIRCLE) {
                numberCircleVicts++;
                DialogFragment dialogFragment = new DialogFragment(ZEROES, onDialogClickListener);
                dialog.onShowDialog(dialogFragment);

            } else if (tictactoefield.getWinner() == TicTacToeField.Figure.CROSS) {
                numberCrossVicts++;
                DialogFragment dialogFragment = new DialogFragment(CROSSES, onDialogClickListener);
                dialog.onShowDialog(dialogFragment);
            }
        } else if (tictactoefield.isFull()) {
            DialogFragment dialogFragment = new DialogFragment(NONE, onDialogClickListener);
            dialog.onShowDialog(dialogFragment);
        }
        step++;
        if (tictactoefield.isFull() || tictactoefield.getWinner() != TicTacToeField.Figure.NONE)
            gameOver = true;
        return true;
    }

    private void drawScore(Canvas canvas) {
        Drawable cross = getResources().getDrawable(R.drawable.cross, null);
        CROSS_RECT.set((int) LENGTH / 6, (int) (getHeight() - 2 * LENGTH / 6), (int) (2 * LENGTH / 6), (int) (getHeight() - 2 * LENGTH / 6 + LENGTH / 6));
        cross.setBounds(CROSS_RECT);
        cross.draw(canvas);

        Drawable circle = getResources().getDrawable(R.drawable.circle, null);
        CIRCLE_RECT.set((int) (getWidth() - 2 * LENGTH / 6), (int) (getHeight() - 2 * LENGTH / 6), (int) (getWidth() - LENGTH / 6), (int) (getHeight() - 2 * LENGTH / 6 + LENGTH / 6));
        circle.setBounds(CIRCLE_RECT);
        circle.draw(canvas);

        CROSS_TEXT_RECT.set((int) LENGTH / 6, (int) (getHeight() - LENGTH / 6), (int) (2 * LENGTH / 6), getHeight());
        drawText(canvas, numberCrossVicts, CROSS_TEXT_RECT);
        CIRCLE_TEXT_RECT.set((int) (getWidth() - 2 * LENGTH / 6), (int) (getHeight() - LENGTH / 6), (int) (getWidth() - LENGTH / 6), getHeight());
        drawText(canvas, numberCircleVicts, CIRCLE_TEXT_RECT);
    }


    private void drawLines(Canvas canvas) {
        lines.moveTo(STROKE_WIDTH, STROKE_WIDTH + LENGTH / 3);
        lines.lineTo((STROKE_WIDTH + LENGTH), STROKE_WIDTH + LENGTH / 3);
        lines.moveTo(STROKE_WIDTH, STROKE_WIDTH + 2 * LENGTH / 3);
        lines.lineTo((STROKE_WIDTH + LENGTH), STROKE_WIDTH + 2 * LENGTH / 3);
        lines.moveTo((STROKE_WIDTH + LENGTH / 3), STROKE_WIDTH);
        lines.lineTo(STROKE_WIDTH + LENGTH / 3, (STROKE_WIDTH + LENGTH));
        lines.moveTo(STROKE_WIDTH + 2 * LENGTH / 3, STROKE_WIDTH);
        lines.lineTo(STROKE_WIDTH + 2 * LENGTH / 3, (STROKE_WIDTH + LENGTH));
        canvas.drawPath(lines, LINES_PAINT);
    }

    private void drawText(Canvas canvas, int victories, Rect rect) {
        textBounds = new Rect();
        final String victoryNumber = String.valueOf(victories);
        getTextBounds(victoryNumber);
        float x = rect.width() / 2f - textBounds.width() / 2f - textBounds.left + rect.left;
        float y = rect.height() / 2f + textBounds.height() / 2f - textBounds.bottom + rect.top;
        canvas.drawText(victoryNumber, x, y, TEXT_PAINT);

    }

    private void getTextBounds(@NonNull String progressString) {
        TEXT_PAINT.getTextBounds(progressString, 0, progressString.length(), textBounds);
    }

    private void init() {
        configureField();
        configureText();
        configureLines();
    }

    private void configureField() {
        BACKGROUND_PAINT.setColor(getResources().getColor(R.color.colorPrimaryDark));
        BACKGROUND_PAINT.setStrokeWidth(STROKE_WIDTH);
        BACKGROUND_PAINT.setStyle(Paint.Style.STROKE);
    }

    private void configureLines() {
        LINES_PAINT.setColor(getResources().getColor(R.color.colorPrimaryDark));
        LINES_PAINT.setStrokeWidth(LINES_WIDTH);
        LINES_PAINT.setStyle(Paint.Style.STROKE);
    }

    private void configureText() {
        TEXT_PAINT.setColor(Color.BLACK);
        TEXT_PAINT.setTextSize(TEXT_SIZE);
        Log.i("TEXT_SIZE", String.valueOf(TEXT_SIZE));
        TEXT_PAINT.setStyle(Paint.Style.FILL_AND_STROKE);
        TEXT_PAINT.setStrokeWidth(TEXT_WIDTH);
        Log.i("TEXT_WIDTH", String.valueOf(TEXT_WIDTH));
    }

    interface EndGame {
        void endingOfGame();
    }

    interface ShowDialog {
        void onShowDialog(DialogFragment dialogFragment);
    }
}
