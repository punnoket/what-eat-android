package pannawat.com.whateat;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.ArrayList;
import java.util.Random;

import pannawat.com.whateat.data.Food;
import pannawat.com.whateat.model.FoodModel;


public class RandomFragment extends Fragment implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    /*view variable*/
    private View view;
    private Toolbar toolbar;
    private Spinner spinner;
    private Button randomButton;
    private ImageView foodImageView;
    private ImageView completeImageView;
    private TextView foodTextView;
    private LinearLayout linearLayout;

    /*data variable*/
    private FoodModel foodModel;
    private ArrayList<Food> foodArrayList = new ArrayList<>();
    private Food food;

    private FragmentActivity context;
    private static final String[] types = {"All", "Dish", "Dessert"};
    private String type;

    public RandomFragment() {
        // Required empty public constructor
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
        randomButton = view.findViewById(R.id.random);
        foodImageView = view.findViewById(R.id.food_image);
        completeImageView = view.findViewById(R.id.complete);
        foodTextView = view.findViewById(R.id.food_name);
        linearLayout = view.findViewById(R.id.food_random);
        spinner = view.findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_item, types);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        setListener();
    }

    public void setListener() {
        randomButton.setOnClickListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_random, container, false);
        setUI();
        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    private void startCompleteAnimation() {
        completeImageView.setVisibility(View.VISIBLE);
        YoYo.with(Techniques.SlideInUp)
                .duration(1000)
                .playOn(completeImageView);
        int splashInterval = 2000;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                YoYo.with(Techniques.SlideOutDown)
                        .duration(1000)
                        .playOn(completeImageView);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        completeImageView.setVisibility(View.GONE);
                    }
                }, 2000);
            }
        }, splashInterval);
    }

    private void startAnimation() {
        linearLayout.setVisibility(View.VISIBLE);
        foodImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.splash));
        foodTextView.setText("");
        YoYo.with(Techniques.FadeIn)
                .duration(700)
                .repeat(randomNum(3)+1)
                .playOn(foodImageView);
        int splashInterval = 3000;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                randomFood();
            }
        }, splashInterval);
    }

    public void randomFood() {
        startCompleteAnimation();
        if (type.equalsIgnoreCase("ALL")) {
            foodModel.getFoodArrayList(new GetAllFoodListener() {
                @Override
                public void getAllFood(ArrayList<Food> foods) {
                    foodArrayList = foods;
                    food = foodArrayList.get(randomNum(foodArrayList.size()));
                    foodImageView.setImageBitmap(convertImage(food.getImage()));
                    foodTextView.setText(food.getName());
                    linearLayout.setVisibility(View.VISIBLE);
                }
            });
        } else {
            foodModel.randomFoodByType(type, new GetAllFoodListener() {
                @Override
                public void getAllFood(ArrayList<Food> foods) {
                    int i = 0;
                    while (i<50) {
                        foodArrayList = foods;
                        food = foodArrayList.get(randomNum(foodArrayList.size()));
                        foodImageView.setImageBitmap(convertImage(food.getImage()));
                        foodTextView.setText(food.getName());
                        linearLayout.setVisibility(View.VISIBLE);
                        i++;
                    }
                }
            });
        }
    }

    public Bitmap convertImage(String strBase64) {
        byte[] decodedString = Base64.decode(strBase64, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }

    public int randomNum(int bound) {
        Random random = new Random();
        int ran = random.nextInt(bound);
        return ran;
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
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        type = types[i];
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.random:
                startAnimation();
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
