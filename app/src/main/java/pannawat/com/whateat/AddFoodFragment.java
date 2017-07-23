package pannawat.com.whateat;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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

import java.io.ByteArrayOutputStream;

import pannawat.com.whateat.data.Food;
import pannawat.com.whateat.model.FoodModel;


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

    private final int RESULT_LOAD_IMAGE = 1;
    private FragmentActivity context;

    /*data variable*/
    private static final String[] types = {"Dish", "Dessert"};
    private FoodModel foodModel;
    private Food food;
    private String type;
    private String picturePath;
    private Intent intent;

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
        toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("ADD FOOD");
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
                try {
                    if(nameEditText.getText().toString().length()==0||picturePath.length()==0) {
                        throw new Exception();
                    }
                    addFood();
                } catch (Exception e) {
                    toast = Toast.makeText(context, "Please Fill Information", Toast.LENGTH_LONG);
                    toast.show();
                }
                break;
        }

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
                    toast = Toast.makeText(context, "Add Success", Toast.LENGTH_LONG);
                    toast.show();
                    intent = new Intent(context, MainActivity.class);
                    startActivity(intent);
                } else {
                    toast = Toast.makeText(context, "Add Fail Please Check Internet", Toast.LENGTH_LONG);
                    toast.show();
                }

                return success;
            }
        });
    }

    private String storeImageToFirebase() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8; // shrink it down otherwise we will use stupid amounts of memory
        Bitmap bitmap = BitmapFactory.decodeFile(picturePath, options);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] bytes = baos.toByteArray();
        String imageBase64 = Base64.encodeToString(bytes, Base64.DEFAULT);
        return imageBase64;
    }

    public void browseImage() {
        intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, RESULT_LOAD_IMAGE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            Uri selectedImage = data.getData();

            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = context.getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            cursor.close();
            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        }

    }

}
