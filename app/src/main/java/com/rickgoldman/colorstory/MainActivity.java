package com.rickgoldman.colorstory;

import android.app.Activity;
import java.util.UUID;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.provider.MediaStore;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;


public class MainActivity extends Activity implements OnClickListener {

    private DrawingView drawView;
    private Bitmap firstPageBitmap, secondPageBitmap, thirdPageBitmap, fourthPageBitmap,
                    fifthPageBitmap;
    private float smallBrush, mediumBrush, largeBrush;
//    private ImageButton currPaint; // drawBtn, eraseBtn, newBtn, saveBtn, nextBtn, prevBtn;
    private int pageNumber = 1;
    private int drawingUpdated = 0;
    private int firststep = 0;

    private String colorName;
    public String[] crayonNames;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton drawBtn, eraseBtn, newBtn, saveBtn, nextBtn, prevBtn;

        drawView = (DrawingView)findViewById(R.id.drawing);

        smallBrush = getResources().getInteger(R.integer.small_size);
        mediumBrush = getResources().getInteger(R.integer.medium_size);
        largeBrush = getResources().getInteger(R.integer.large_size);
        drawBtn = (ImageButton)findViewById(R.id.draw_btn);
        drawBtn.setOnClickListener(this);
        drawView.setBrushSize(mediumBrush);
        eraseBtn = (ImageButton)findViewById(R.id.erase_btn);
        eraseBtn.setOnClickListener(this);
        newBtn = (ImageButton)findViewById(R.id.color_btn);
        newBtn.setOnClickListener(this);
        saveBtn = (ImageButton)findViewById(R.id.save_btn);
        saveBtn.setOnClickListener(this);
        nextBtn = (ImageButton)findViewById(R.id.next_btn);
        nextBtn.setOnClickListener(this);
        prevBtn = (ImageButton)findViewById(R.id.prev_btn);
        prevBtn.setOnClickListener(this);

        drawView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                drawingUpdated = 1;
                Log.d("test", "ontouch");
                return false;
            }
        });

    }

    @Override
    public void onClick(View view){

        //draw button properties
        if(view.getId()==R.id.draw_btn){
            //draw button clicked
            final Dialog brushDialog = new Dialog(this);
            brushDialog.setTitle("Brush size:");
            brushDialog.setContentView(R.layout.brush_chooser);
            ImageButton smallBtn = (ImageButton)brushDialog.findViewById(R.id.small_brush);
            smallBtn.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v) {
                    drawView.setBrushSize(smallBrush);
                    drawView.setLastBrushSize(smallBrush);
                    drawView.setErase(false);
                    brushDialog.dismiss();
                }
            });
            ImageButton mediumBtn = (ImageButton)brushDialog.findViewById(R.id.medium_brush);
            mediumBtn.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v) {
                    drawView.setBrushSize(mediumBrush);
                    drawView.setLastBrushSize(mediumBrush);
                    drawView.setErase(false);
                    brushDialog.dismiss();
                }
            });

            ImageButton largeBtn = (ImageButton)brushDialog.findViewById(R.id.large_brush);
            largeBtn.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v) {
                    drawView.setBrushSize(largeBrush);
                    drawView.setLastBrushSize(largeBrush);
                    drawView.setErase(false);
                    brushDialog.dismiss();
                }
            });
            brushDialog.show();

        }

        else if(view.getId()==R.id.erase_btn){
            //switch to erase - choose size
            final Dialog brushDialog = new Dialog(this);
            brushDialog.setTitle("Eraser size:");
            brushDialog.setContentView(R.layout.brush_chooser);
            ImageButton smallBtn = (ImageButton)brushDialog.findViewById(R.id.small_brush);
            smallBtn.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v) {
                    drawView.setErase(true);
                    drawView.setBrushSize(smallBrush);
                    brushDialog.dismiss();
                }
            });
            ImageButton mediumBtn = (ImageButton)brushDialog.findViewById(R.id.medium_brush);
            mediumBtn.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v) {
                    drawView.setErase(true);
                    drawView.setBrushSize(mediumBrush);
                    brushDialog.dismiss();
                }
            });
            ImageButton largeBtn = (ImageButton)brushDialog.findViewById(R.id.large_brush);
            largeBtn.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v) {
                    drawView.setErase(true);
                    drawView.setBrushSize(largeBrush);
                    brushDialog.dismiss();
                }
            });
            brushDialog.show();

        }

        else if(view.getId()==R.id.color_btn){

            final Intent intent = new Intent(this, ColorSelect.class);
            startActivityForResult(intent, 1);

            /*
            //new button
            AlertDialog.Builder newDialog = new AlertDialog.Builder(this);
            newDialog.setTitle("New drawing");
            newDialog.setMessage("Start new drawing (you will lose the current drawing)?");
            newDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    drawView.startNew();
                    dialog.dismiss();
                }
            });
            newDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            newDialog.show();
            */
        }

        else if(view.getId()==R.id.save_btn){
            AlertDialog.Builder saveDialog = new AlertDialog.Builder(this);
            saveDialog.setTitle("Save drawing");
            saveDialog.setMessage("Save drawing to device Gallery?");
            saveDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    //save drawing
                    drawView.setDrawingCacheEnabled(true);
                    String imgSaved = MediaStore.Images.Media.insertImage(
                            getContentResolver(), drawView.getDrawingCache(),
                            UUID.randomUUID().toString() + ".png", "drawing");
                    if (imgSaved != null) {
                        Toast savedToast = Toast.makeText(getApplicationContext(),
                                "Drawing saved to Gallery!", Toast.LENGTH_SHORT);
                        savedToast.show();
                    } else {
                        Toast unsavedToast = Toast.makeText(getApplicationContext(),
                                "Oops! Image could not be saved.", Toast.LENGTH_SHORT);
                        unsavedToast.show();
                    }
                    drawView.destroyDrawingCache();
                }
            });
            saveDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            saveDialog.show();
        }

        else if(view.getId()==R.id.next_btn && pageNumber != 5) {

            //go to next image in list
            //set page number

            if (pageNumber < 6) {

                pageNumber++;
                if (pageNumber >= 5) pageNumber = 5;

                Drawable d;
                drawView.setDrawingCacheEnabled(true);

                if (drawingUpdated == 1) {

                    switch (pageNumber-1) {

                        //Assign images based on page number.
                        case 1: {
                            firstPageBitmap = Bitmap.createBitmap(drawView.getDrawingCache());

                            //Check whether bitmaps are initialized. If not then initialize.
                            if (firststep == 0) {
                                drawView.startNew();
                                secondPageBitmap = Bitmap.createBitmap(drawView.getDrawingCache());
                                thirdPageBitmap = Bitmap.createBitmap(drawView.getDrawingCache());
                                fourthPageBitmap = Bitmap.createBitmap(drawView.getDrawingCache());
                                fifthPageBitmap = Bitmap.createBitmap(drawView.getDrawingCache());
                                firststep = 1;
                            }

                            break;
                        }
                        case 2: {
                            secondPageBitmap = Bitmap.createBitmap(drawView.getDrawingCache());
                            //canvasPage2 = new Canvas(secondPageBitmap);
                            break;
                        }
                        case 3: {
                            thirdPageBitmap = Bitmap.createBitmap(drawView.getDrawingCache());
                            //canvasPage3 = new Canvas(thirdPageBitmap);
                            break;
                        }
                        case 4: {
                            fourthPageBitmap = Bitmap.createBitmap(drawView.getDrawingCache());
                            //canvasPage4 = new Canvas(fourthPageBitmap);
                            break;
                        }
                        case 5: {
                            fifthPageBitmap = Bitmap.createBitmap(drawView.getDrawingCache());
                            //canvasPage5 = new Canvas(fifthPageBitmap);
                            break;
                        }
                        default: {
                            break;
                        }

                    }
                    drawingUpdated = 0;
                }

                // Get the current page number and redraw the images based on page number.
                switch (pageNumber) {

                    //Assign images based on page number.
                    case 1: {
                        drawView.startNew();
                        d = new BitmapDrawable(getResources(), firstPageBitmap);
                        drawView.setBackground(d);
                        break;
                    }
                    case 2: {
                        drawView.startNew();
                        d = new BitmapDrawable(getResources(), secondPageBitmap);
                        drawView.setBackground(d);
                        break;
                    }
                    case 3: {
                        drawView.startNew();
                        d = new BitmapDrawable(getResources(), thirdPageBitmap);
                        drawView.setBackground(d);
                        break;
                    }
                    case 4: {
                        drawView.startNew();
                        d = new BitmapDrawable(getResources(), fourthPageBitmap);
                        drawView.setBackground(d);
                        break;
                    }
                    case 5: {
                        drawView.startNew();
                        d = new BitmapDrawable(getResources(), fifthPageBitmap);
                        drawView.setBackground(d);
                        break;
                    }
                    default: {
                        break;
                    }

                }

            }

            //Clear drawViewCache and clear drawView for next page
            drawView.setDrawingCacheEnabled(false);

        }

        else if(view.getId()==R.id.prev_btn && pageNumber != 1 ) {

            Drawable d;
            drawView.setDrawingCacheEnabled(true);

                //go to next image in list
                pageNumber--;
                if (pageNumber <= 1) pageNumber = 1;

                if (drawingUpdated == 1) {

                    switch (pageNumber+1) {

                        //Assign images based on page number.
                        case 1: {
                            firstPageBitmap = Bitmap.createBitmap(drawView.getDrawingCache());
                            break;
                        }
                        case 2: {
                            secondPageBitmap = Bitmap.createBitmap(drawView.getDrawingCache());
                            break;
                        }
                        case 3: {
                            thirdPageBitmap = Bitmap.createBitmap(drawView.getDrawingCache());
                            break;
                        }
                        case 4: {
                            fourthPageBitmap = Bitmap.createBitmap(drawView.getDrawingCache());
                            break;
                        }
                        case 5: {
                            fifthPageBitmap = Bitmap.createBitmap(drawView.getDrawingCache());
                            break;
                        }
                        default: {
                            break;
                        }
                    }
                    drawingUpdated = 0;
                }


                // Get the current page number and redraw the images based on page number.
                switch (pageNumber) {

                    //Assign images based on page number.
                    case 1: {
                        drawView.startNew();
                        d = new BitmapDrawable(getResources(), firstPageBitmap);
                        drawView.setBackground(d);
                        break;
                    }
                    case 2: {
                        drawView.startNew();
                        d = new BitmapDrawable(getResources(), secondPageBitmap);
                        drawView.setBackground(d);
                        break;
                    }
                    case 3: {
                        drawView.startNew();
                        d = new BitmapDrawable(getResources(), thirdPageBitmap);
                        drawView.setBackground(d);
                        break;
                    }
                    case 4: {
                        drawView.startNew();
                        d = new BitmapDrawable(getResources(), fourthPageBitmap);
                        drawView.setBackground(d);
                        break;
                    }
                    case 5: {
                        drawView.startNew();
                        d = new BitmapDrawable(getResources(), fifthPageBitmap);
                        drawView.setBackground(d);
                        break;
                    }
                    default: {
                        break;
                    }

                }

            }
            //Clear drawViewCache and clear drawView for next page
            drawView.setDrawingCacheEnabled(false);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        //assign crayon names from string array
        crayonNames = getResources().getStringArray(R.array.crayon_names);

        //assign color code based on result from color select activity
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == Activity.RESULT_OK){
            String colorCode = data.getStringExtra(ColorSelect.RESULT_COLORCODE);

            //set color to color code
            drawView.setColor(colorCode);

            //Check color code and assign color name
            if(colorCode.equals("#ff0000")) colorName = crayonNames[0];
            if(colorCode.equals("#ff3300")) colorName = crayonNames[1];
            if(colorCode.equals("#ff6600")) colorName = crayonNames[2];
            if(colorCode.equals("#ffcc00")) colorName = crayonNames[3];
            if(colorCode.equals("#ffff00")) colorName = crayonNames[4];
            if(colorCode.equals("#ccff00")) colorName = crayonNames[5];
            if(colorCode.equals("#66ff00")) colorName = crayonNames[6];
            if(colorCode.equals("#33ff00")) colorName = crayonNames[7];
            if(colorCode.equals("#00ff00")) colorName = crayonNames[8];
            if(colorCode.equals("#00ff33")) colorName = crayonNames[9];
            if(colorCode.equals("#00ff66")) colorName = crayonNames[10];
            if(colorCode.equals("#00ffcc")) colorName = crayonNames[11];
            if(colorCode.equals("#00ffff")) colorName = crayonNames[12];
            if(colorCode.equals("#00ccff")) colorName = crayonNames[13];
            if(colorCode.equals("#0066ff")) colorName = crayonNames[14];
            if(colorCode.equals("#0033ff")) colorName = crayonNames[15];
            if(colorCode.equals("#0000ff")) colorName = crayonNames[16];
            if(colorCode.equals("#6600ff")) colorName = crayonNames[17];
            if(colorCode.equals("#9900ff")) colorName = crayonNames[18];
            if(colorCode.equals("#cc00ff")) colorName = crayonNames[19];
            if(colorCode.equals("#ff00ff")) colorName = crayonNames[20];
            if(colorCode.equals("#ff00cc")) colorName = crayonNames[21];
            if(colorCode.equals("#ff0066")) colorName = crayonNames[22];
            if(colorCode.equals("#ff0033")) colorName = crayonNames[23];
            if(colorCode.equals("#000000")) colorName = crayonNames[24];
            if(colorCode.equals("#999999")) colorName = crayonNames[25];
            if(colorCode.equals("#ffffff")) colorName = crayonNames[26];

            //Display color selection as toast.
            Toast.makeText(this, colorName, Toast.LENGTH_LONG).show();

        }
    }

}
