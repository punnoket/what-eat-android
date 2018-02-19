package pannawat.com.whateat;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import pannawat.com.whateat.data.Food;
import pannawat.com.whateat.model.FoodModel;

import static android.app.Activity.RESULT_OK;


public class AddFoodFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private RandomFragment.OnFragmentInteractionListener mListener;

    /*view variable*/
    private View view;
    private Toolbar toolbar;
    private ImageView imageView;
    private Spinner spinner;
    private EditText nameEditText;
    private Button addbutton;
    private RelativeLayout relativeLayout;
    private ProgressBar progressBar;
    private Toast toast;
    private Bitmap bitmap;

    private final int RESULT_LOAD_IMAGE = 1;
    private static int IMG_RESULT = 1;
    private FragmentActivity context;

    /*data variable*/
    private static final String[] types = {"Dish", "Dessert"};
    private FoodModel foodModel;
    private Food food;
    private String type;
    private String picturePath;
    private Intent intent;
    private String string;

    public AddFoodFragment() {
    }

    public static RandomFragment newInstance(String param1, String param2) {
        RandomFragment fragment = new RandomFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        foodModel = new FoodModel();

    }

    public void setUI() {
        imageView = view.findViewById(R.id.food_image);
        spinner = view.findViewById(R.id.spiner);
        nameEditText = view.findViewById(R.id.input_name);
        addbutton = view.findViewById(R.id.add_food);
        progressBar = view.findViewById(R.id.progress);
        relativeLayout = view.findViewById(R.id.loding_layout);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_item, types);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    public void setListener() {
        imageView.setOnClickListener(this);
        addbutton.setOnClickListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_add_food, container, false);
        setUI();
        setListener();
        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = (FragmentActivity) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.food_image:
                browseImage();
                break;
            case R.id.add_food:
                    if (validate()) {
                        addFood();
                    } else {
                        Snackbar.make(addbutton, "Please Fill Information", Snackbar.LENGTH_LONG);
                    }
                    break;
                }
        }

    private boolean validate() {
        return nameEditText.getText().toString().length() != 0 && picturePath.length() != 0;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        type = types[i];
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void addFood() {
        progressBar.setVisibility(View.VISIBLE);
        relativeLayout.setVisibility(View.VISIBLE);
        String imageFood = storeImageToFirebase();
        food = new Food(nameEditText.getText().toString(),
                imageFood,
                "",
                "",
                type);
        foodModel.addFood(food, new OnsuccessListener() {
            @Override
            public boolean isSuccess(boolean success) {
                progressBar.setVisibility(View.GONE);
                relativeLayout.setVisibility(View.GONE);
                if (success) {
                    Snackbar.make(imageView, "Add Success", Snackbar.LENGTH_SHORT).show();
                    intent = new Intent(context, MainActivity.class);
                    startActivity(intent);
                } else {
                    Snackbar.make(imageView, "Add Fail Please Check Internet", Snackbar.LENGTH_SHORT).show();
                }

                return success;
            }
        });
    }

    private String storeImageToFirebase() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    public void browseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Video"),RESULT_LOAD_IMAGE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == RESULT_LOAD_IMAGE) {
                Uri selectedImageUri = data.getData();

                picturePath = selectedImageUri.getPath();
                bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), selectedImageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                imageView.setImageBitmap(bitmap);//trying bitmap
            }
        }
    }
}
