package com.evans.swinspector;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.room.Room;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class InspectorService extends Service  implements View.OnClickListener{

    private final int CLICK_ACTION_THRESHOLD = 10;
    private final int SUGGEST_THRESHOLD = 60;
    private WindowManager mWindowManager;
    private View mFloatingView;
    private View collapsedView;
    private View expandedView;

    private RadioButton rb_sixStar;
    private RadioButton rb_fiveStar;
    private RadioButton rb_regular;
    private RadioButton rb_ancient;

    private RadioGroup rg_star;
    private RadioGroup rg_type;

    private Spinner spinner;

    private TextView tv_runeEfficiency;
    private TextView tv_runeDetails;
    private TextView tv_suggestMonster;

    AppDatabase db;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onCreate() {
        super.onCreate();
        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "inspector").allowMainThreadQueries().build();
        mFloatingView = LayoutInflater.from(this).inflate(R.layout.inspector_view, null);

        int LAYOUT_FLAG;

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
        }

        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 0;
        params.y = 100;

        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mWindowManager.addView(mFloatingView, params);

        collapsedView = mFloatingView.findViewById(R.id.layoutCollapsed);
        expandedView = mFloatingView.findViewById(R.id.layoutExpanded);

        ImageView placeHolder = expandedView.findViewById(R.id.placeHolder);

        rb_sixStar = (RadioButton) expandedView.findViewById(R.id.rb_sixStar);
        rb_fiveStar = (RadioButton) expandedView.findViewById(R.id.rb_fiveStar);

        rg_star = (RadioGroup) expandedView.findViewById(R.id.starRadioGroup);

        rg_star.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                readImage(getLatestScreenshot(), placeHolder);
            }
        });

        tv_runeEfficiency = (TextView) expandedView.findViewById(R.id.runeEfficiency);
        tv_runeDetails = (TextView) expandedView.findViewById(R.id.runeDetails);
        tv_suggestMonster = (TextView) expandedView.findViewById(R.id.suggestMonster);
        mFloatingView.findViewById(R.id.buttonClose).setOnClickListener(this);
        expandedView.setOnClickListener(this);

        mFloatingView.setOnTouchListener(new View.OnTouchListener(){
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = params.x;
                        initialY = params.y;
                        initialTouchX = motionEvent.getRawX();
                        initialTouchY = motionEvent.getRawY();
                        return true;
                    case MotionEvent.ACTION_UP:
                        float endX = motionEvent.getRawX();
                        float endY = motionEvent.getRawY();

                        if(isClick(initialTouchX, endX, initialTouchY, endY)) {
                            if(expandedView.getVisibility() == View.VISIBLE) {
                                expandedView.setVisibility(View.GONE);
                            } else {
                                StringBuilder stats = null;
                                if(getLatestScreenshot() != null){
                                    readImage(getLatestScreenshot(), placeHolder);
                                    expandedView.setVisibility(View.VISIBLE);
                                } else {
                                    Toast.makeText(getApplicationContext(), "No images were found.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        params.x = initialX + (int) (motionEvent.getRawX() - initialTouchX);
                        params.y = initialY + (int) (motionEvent.getRawY() - initialTouchY);
                        mWindowManager.updateViewLayout(mFloatingView, params);
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.buttonClose) {
            stopSelf();
            mWindowManager.removeView(view.getRootView());
        }
    }

    /**
     * Checks if the movement is within the threshold to be considered a click.
     * @param startX Start position of X
     * @param endX End position of X
     * @param startY Start position of Y
     * @param endY End position of Y
     * @return boolean
     */
    private boolean isClick(float startX, float endX, float startY, float endY) {
        float differenceX = Math.abs(startX - endX);
        float differenceY = Math.abs(startY - endY);

        return !(differenceX > CLICK_ACTION_THRESHOLD/* =5 */ || differenceY > CLICK_ACTION_THRESHOLD);
    }

    /**
     * Gets the latest screenshot from screenshot folder.
     * @return Bitmap
     */
    public Bitmap getLatestScreenshot() {
        Bitmap bitmap = null;
        ArrayList<File> images = new ArrayList<>();

        String[] projection = new String[]{
                MediaStore.Images.Media.DATA
        };

        Uri imageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        Cursor cur = getContentResolver().query(imageUri,
                projection, // Which columns to return
                MediaStore.Images.Media.DATA + " like ? ",       // Which rows to return (all rows)
                new String[] {"%Screenshots%"},       // Selection arguments (none)
                "datetaken DESC"        // Ordering
        );

        if(cur.getCount() > 0) {
            cur.moveToFirst();
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                File file = new File(cur.getString(0));
                bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            } else {
                File file = new File(cur.getString(0));
                bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            }
        }else {
            cur = getContentResolver().query(imageUri,
                    projection, // Which columns to return
                    MediaStore.Images.Media.DATA + " like ? ",       // Which rows to return (all rows)
                    new String[] {"%SharedFolder%"},       // Selection arguments (none)
                    "datetaken DESC"        // Ordering
            );
            if(cur.getCount() > 0) {
                cur.moveToFirst();
                File file = new File(cur.getString(0));
                bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            }
        }

        return bitmap;
    }

    /**
     * Crops an image.
     * @param bitmap Image to be cropped.
     * @return Bitmap
     */
    public Bitmap cropImage(Bitmap bitmap, int amount) {
        if(bitmap != null) {

            if(amount == 0) {
                //Shrunk
                int cropX = (int) (bitmap.getWidth() * 0.4);
                int startX = (int) (bitmap.getWidth() * 0.30);
                int cropY = (int) (bitmap.getHeight() * 0.65);
                int startY = (int) (bitmap.getHeight() * 0.18);

                return Bitmap.createBitmap(bitmap, startX, startY, cropX, cropY);
            }else if(amount == 1) {
                //Bottom left
                int cropX = (int) (bitmap.getWidth() * 0.5);
                int startX = (int) 0;
                int cropY = (int) (bitmap.getHeight() * 0.6);
                int startY = (int) (bitmap.getHeight() * 0.4);

                return Bitmap.createBitmap(bitmap, startX, startY, cropX, cropY);
            }else if(amount == 2) {
                //Top right
                int cropX = (int) (bitmap.getWidth() * 0.5);
                int startX = (int) (bitmap.getWidth() * 0.5);
                int cropY = (int) (bitmap.getHeight() * 0.6);
                int startY = (int) 0;

                return Bitmap.createBitmap(bitmap, startX, startY, cropX, cropY);
            }else {
                return bitmap;
            }

        }

        return null;
    }

    /**
     * Retrieve the values of passed image.
     * @param bitmap Image to be read.
     * @return StringBuilder
     */
    private void readImage(Bitmap bitmap, ImageView imageView) {
        if(bitmap != null) {
            TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
            ReadSuccess readSuccessListener;
            InputImage image = InputImage.fromBitmap(bitmap, 0);
            recognizer.process(image).addOnSuccessListener(readSuccessListener = new ReadSuccess());
            imageView.setImageBitmap(bitmap);
            if(readSuccessListener.rune == null) {
                image = InputImage.fromBitmap(cropImage(bitmap, 0), 0);
                recognizer.process(image).addOnSuccessListener(readSuccessListener = new ReadSuccess());
                imageView.setImageBitmap(cropImage(bitmap, 0));
                if(readSuccessListener.rune == null){
                    image = InputImage.fromBitmap(cropImage(bitmap, 1), 0);
                    recognizer.process(image).addOnSuccessListener(readSuccessListener = new ReadSuccess());
                    imageView.setImageBitmap(cropImage(bitmap, 1));
                    if(readSuccessListener.rune == null){
                        image = InputImage.fromBitmap(cropImage(bitmap, 2), 0);
                        recognizer.process(image).addOnSuccessListener(readSuccessListener = new ReadSuccess());
                        imageView.setImageBitmap(cropImage(bitmap, 2));
                    }
                }
            }
        }
    }

    public Rune createRune(Text text) {
        SettingsHandler settingsHandler = new SettingsHandler(getApplicationContext().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE));
        List<String> grades = Arrays.asList(
                "common",
                "magic",
                "rare",
                "hero",
                "legend");

        List<String> stats = Arrays.asList(
                "accuracy",
                "atk",
                "cri dmg",
                "cri rate",
                "def",
                "hp",
                "resistance",
                "spd");

        List<String> sets = Arrays.asList(
                "energy",
                "fatal",
                "blade",
                "swift",
                "focus",
                "guard",
                "endure",
                "shield",
                "revenge",
                "will",
                "nemesis",
                "vampire",
                "destroy",
                "despair",
                "violent",
                "rage",
                "fight",
                "determination",
                "enhance",
                "accuracy",
                "tolerance"
        );
        boolean failed = false;
        List<Substat> subStats = new ArrayList<>();
        Rune.Grade grade = null;
        Rune.Set set = null;
        Rune.Type type = null;
        int star = 0;
        int currentUpgrade = 0;
        int slot = 0;

        //Get the grade.
        List<String> words = Arrays.asList(text.getText().split("\n"));
        for(String word : words){
            if(grades.contains(word.toLowerCase())){
                grade = Rune.Grade.valueOf(word.toUpperCase());
            }
        }

        //Loop over all words found in screenshot.
        for(int i = 0; i < text.getTextBlocks().size(); i++) {
            //Check if the current row has the word rune in it but not power-up.
            if(text.getTextBlocks().get(i).getText().toLowerCase().contains("rune") && !text.getTextBlocks().get(i).getText().toLowerCase().contains("power-up")) {
                //Check if line has a set in the words.
                for(String check : text.getTextBlocks().get(i).getText().toLowerCase().split(" ")) {
                    if(sets.contains(check)) {
                        set = Rune.Set.valueOf(check.toUpperCase());
                    }
                }

                try {
                    //Checks if rune has been powered up.
                    if(text.getTextBlocks().get(i).getText().contains("+")){
                        //Assign the upgrade if it has and get the slot.
                        currentUpgrade = Integer.parseInt(text.getTextBlocks().get(i).getText().split(" ")[0].replace("+", ""));
                        if(text.getTextBlocks().get(i).getText().toLowerCase().split(" ").length == 5) {
                            slot = Integer.parseInt(text.getTextBlocks().get(i).getText().toLowerCase().split(" ")[4].replace("(", "").replace(")", ""));
                        } else {
                            slot = Integer.parseInt(text.getTextBlocks().get(i).getText().toLowerCase().split(" ")[3].replace("(", "").replace(")", ""));
                        }
                    } else {
                        //Get the slot of the rune.
                        if(text.getTextBlocks().get(i).getText().toLowerCase().split(" ").length == 4) {
                            slot = Integer.parseInt(text.getTextBlocks().get(i).getText().toLowerCase().split(" ")[3].replace("(", "").replace(")", ""));
                        } else {
                            slot = Integer.parseInt(text.getTextBlocks().get(i).getText().toLowerCase().split(" ")[2].replace("(", "").replace(")", ""));
                        }
                    }
                }catch (Exception ex) {
                    failed = true;
                }
            }

            //Get sub stats from the image.
            for(int k = 0; k < stats.size(); k++) {
                if(text.getTextBlocks().get(i).getText().toLowerCase().contains(stats.get(k).toLowerCase()) && !text.getTextBlocks().get(i).getText().toLowerCase().contains("set") && !text.getTextBlocks().get(i).getText().toLowerCase().contains("increase") && !text.getTextBlocks().get(i).getText().toLowerCase().contains("bar")) {
                    //Checks if the line has any anomaly line breaks.
                    if(text.getTextBlocks().get(i).getText().contains("\n")) {
                        String[] removeBreaks = text.getTextBlocks().get(i).getText().split("\n");
                        for(String stat : removeBreaks) {
                            if(stat.contains(" ")) {
                                try{
                                    Substat substat = generateSubStat(stat.split(" "));
                                    subStats.add(substat);
                                }catch(Exception ex) {
                                    failed = true;
                                }
                            } else {
                                try {
                                    Substat substat = generateSubStat(stat.split("\\+"));
                                    subStats.add(substat);
                                }catch(Exception ex) {
                                    failed = true;
                                }
                            }
                        }
                        break;
                    } else {

                        //If it doesn't contain line breaks, generate the sub stats.
                        if(text.getTextBlocks().get(i).getText().contains(" ")) {
                            try{
                                Substat substat = generateSubStat(text.getTextBlocks().get(i).getText().split(" "));
                                subStats.add(substat);
                            }catch(Exception ex) {
                                failed = true;
                            }
                        } else {
                            try {
                                Substat substat = generateSubStat(text.getTextBlocks().get(i).getText().split("\\+"));
                                subStats.add(substat);
                            }catch(Exception ex) {
                                failed = true;
                            }
                        }
                    }
                }else if(text.getTextBlocks().get(i).getText().toLowerCase().contains("set")) {
                    i = text.getTextBlocks().size() - 1;
                }
            }
        }

        if(rb_sixStar.isChecked()) {
            star = 6;
        } else {
            star = 5;
        }

        //Check if the grade is null, it is crucial to the rune.
        if(grade == null || failed) {
            return null;
        } else {
            if(subStats.size() > 0) {
                Rune rune = null;
                rune = new Rune(grade, set, slot, currentUpgrade, Rune.Type.REGULAR, star, subStats, false);
                if(db.runeDao().getAllScanned().size() > 9) {
                    db.runeDao().delete(db.runeDao().getAllScanned().get(0));
                }
                db.runeDao().insertAll(rune);
                tv_runeEfficiency.setText(String.format(Locale.ENGLISH, "Efficiency: %.2f%%/%.2f%%", rune.getEfficiency(settingsHandler.reduceFlats(), settingsHandler.getFlatWeight()).get(0), rune.getEfficiency(settingsHandler.reduceFlats(), settingsHandler.getFlatWeight()).get(1)));

                tv_runeDetails.setText(rune.toString());
                List<SuggestMonster> suggest = rune.suggestMonster(db.monsterDao().getAll(), settingsHandler.reduceFlats(), settingsHandler.getFlatWeight(), settingsHandler.includeSet(), settingsHandler.getSetWeight());
                int counter = 0;
                String suggestString = "";
                do {
                    suggestString += suggest.get(counter).toString() + "\n";
                    counter++;
                }while(suggest.get(counter).score >= SUGGEST_THRESHOLD && counter < suggest.size() - 1);

                tv_suggestMonster.setText(suggestString);
                return rune;
            } else {
                return null;
            }
        }
    }

    public Substat generateSubStat(String[] values) {
        if(values[1].contains("%")) {
            if(values[0].toLowerCase().contains("hp")) {
                return new Substat(Substat.StatType.HP_PERC, Integer.parseInt(values[1].replace("+", "").replace("%", "")));
            }else if(values[0].toLowerCase().contains("def")) {
                return new Substat(Substat.StatType.DEF_PERC, Integer.parseInt(values[1].replace("+", "").replace("%", "")));
            }else if(values[0].toLowerCase().contains("atk")) {
                return new Substat(Substat.StatType.ATK_PERC, Integer.parseInt(values[1].replace("+", "").replace("%", "")));
            } else {
                return new Substat(Substat.StatType.valueOf(values[0].toUpperCase()), Integer.parseInt(values[1].replace("+", "").replace("%", "")));
            }
        } else {
            if(values[0].toLowerCase().contains("cri")) {
                return new Substat(Substat.StatType.valueOf(values[0].toUpperCase() + "_" + values[1].toUpperCase()), Integer.parseInt(values[2].replace("+", "").replace("%", "")));
            } else {
                return new Substat(Substat.StatType.valueOf(values[0].toUpperCase()), Integer.parseInt(values[1].replace("+", "")));
            }

        }
    }

    public class ReadSuccess implements OnSuccessListener<Text> {
        public Rune rune;

        public ReadSuccess() {
            this.rune = null;
        }

        @Override
        public void onSuccess(@NonNull Text text) {
            rune = createRune(text);
        }
    }
}
