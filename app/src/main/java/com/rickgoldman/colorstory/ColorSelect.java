package com.rickgoldman.colorstory;

import android.app.ListActivity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jason Shepherd on 3/31/2015.
 */
public class ColorSelect extends ListActivity {

    public static String RESULT_COLORCODE = "colorcode";
    public String[] colornames, colorcodes;
    private TypedArray imgs;
    private List<ColorAdapter> colorList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        populateColorList();
        ArrayAdapter<ColorAdapter> adapter = new ColorListArrayAdapter(this, colorList);
        setListAdapter(adapter);
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ColorAdapter c = colorList.get(position);
                Intent returnIntent = new Intent();
                returnIntent.putExtra(RESULT_COLORCODE, c.getCrayonCode());
                setResult(RESULT_OK, returnIntent);
                imgs.recycle(); //recycle images
                finish();
            }
        });
    }

    private void populateColorList() {
        colorList = new ArrayList<ColorAdapter>();
        imgs = getResources().obtainTypedArray(R.array.crayonarray);
        for(int i = 0; i < 7; i++){
            colorList.add(new ColorAdapter(imgs.getDrawable(i)));
        }
    }

    public class ColorAdapter {

        private Drawable crayon;

        public ColorAdapter(Drawable crayon){

            this.crayon = crayon;
        }
        public String getCrayonCode() {
            String crayonCode = "#FFFF0000";
            return crayonCode;
        }

        public Drawable getCrayon() {
            return crayon;
        }
    }
}
