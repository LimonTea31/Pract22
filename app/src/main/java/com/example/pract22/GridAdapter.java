package com.example.pract22;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.net.http.UrlRequest;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Collections;
public class GridAdapter extends BaseAdapter {

        private final Context mContext;
        private final Integer mCols;
    private final Integer mRows;
    private final ArrayList<String> arrPict;
    private final ArrayList<String> arrFoundPairs;// массив картинок
    private final String PictureCollection; // Префикс набора картинок
    private final Resources mRes; // Ресурсы приложени
    private static final int CELL_CLOSE = 0;
    private static final int CELL_OPEN = 1;
    private static final int CELL_DELETE = -1;
    private static enum Status {CELL_OPEN, CELL_CLOSE, CELL_DELETE};
    private final ArrayList<Status> arrStatus; // состояние ячеек

        public GridAdapter(Context context, int cols, int rows) {
            mContext = context;
            mCols = cols;
            mRows = rows;
            arrPict = new ArrayList<String>();
            arrStatus = new ArrayList<Status>();
            arrFoundPairs = new ArrayList<String>();
            // Пока определяем префикс так, позже он будет браться из настроек
            PictureCollection = "animal";
            // Получаем все ресурсы приложения
            mRes = mContext.getResources();

            // Метод заполняющий массив vecPict
            makePictArray();
            closeAllCells();
        }
    private void closeAllCells() {
        arrStatus.clear();
        for (int i = 0; i < mCols * mRows; i++)
            arrStatus.add(Status.CELL_CLOSE);
    }

    private void makePictArray () {

        // очищаем вектор
        arrPict.clear();

        // добавляем
        for (int i = 0; i < ((mCols * mRows) / 2); i++)
        {
            arrPict.add (PictureCollection + Integer.toString (i));
            arrPict.add (PictureCollection + Integer.toString (i));
        }
        // перемешиваем
        Collections.shuffle(arrPict);
    }

    @Override
    public int getCount() {
        return mCols * mRows;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ImageView view; // для вывода картинки

        if (convertView == null)
            view = new ImageView(mContext);
        else
            view = (ImageView) convertView;

        // Получаем идентификатор ресурса для картинки,
        // которая находится в векторе vecPict на позиции position
        switch (arrStatus.get(position)) {
            case CELL_OPEN:
                // Получаем идентификатор ресурса для картинки,
                // которая находится в векторе vecPict на позиции position
                @SuppressLint("DiscouragedApi") int drawableId = mRes.getIdentifier(arrPict.get(position),
                        "drawable", mContext.getPackageName());
                view.setImageResource(drawableId);
                break;
            case CELL_CLOSE:
                view.setImageResource(R.drawable.close);
                break;
            default:
                view.setImageResource(R.drawable.none);
        }
        return view;
    }
    public void checkOpenCells() {
        int first = arrStatus.indexOf(Status.CELL_OPEN);
        int second = arrStatus.lastIndexOf(Status.CELL_OPEN);
        if (first == second)
            return;
        if (arrPict.get(first).equals (arrPict.get(second)))
        {
            arrStatus.set(first, Status.CELL_DELETE);
            arrStatus.set(second, Status.CELL_DELETE);
        }
        else
        {
            arrStatus.set(first, Status.CELL_CLOSE);
            arrStatus.set(second, Status.CELL_CLOSE);
        }
        return;
    }

    public boolean openCell(int position) {
        if (arrStatus.get(position) == Status.CELL_DELETE || arrStatus.get(position) == Status.CELL_OPEN) {
            return false;
        }

        // Добавляем проверку наличия выбранной ячейки в списке уже найденных пар
        if (arrFoundPairs.contains(arrPict.get(position))) {
            return false; // Игнорируем нажатие на уже найденную пару
        }

        arrStatus.set(position, Status.CELL_OPEN);
        notifyDataSetChanged();
        return true;
    }

    public boolean checkGameOver() {
        if (arrStatus.indexOf(Status.CELL_CLOSE) < 0)
            return true;
        return false;
    }

}