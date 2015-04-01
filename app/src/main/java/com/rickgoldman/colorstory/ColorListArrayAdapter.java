package com.rickgoldman.colorstory;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by Jason Shepherd on 3/31/2015.
 */

public class ColorListArrayAdapter extends ArrayAdapter<ColorSelect.ColorAdapter> {

    private final List<ColorSelect.ColorAdapter> list;
    private final Activity context;

    static class ViewHolder {
        protected ImageView crayon;
    }
    ColorListArrayAdapter(Activity context, List<ColorSelect.ColorAdapter> list) {
        super(context, R.layout.activity_colorcode_row, list);
        this.context = context;
        this.list = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;

        if (convertView == null) {
            LayoutInflater inflator = context.getLayoutInflater();
            view = inflator.inflate(R.layout.activity_colorcode_row, null);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.crayon = (ImageView) view.findViewById(R.id.crayon);
            view.setTag(viewHolder);
        } else {
            view = convertView;
        }

        ViewHolder holder = (ViewHolder) view.getTag();
        holder.crayon.setImageDrawable(list.get(position).getFlag());
        return view;
    }
}