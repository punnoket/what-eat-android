package pannawat.com.whateat;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import pannawat.com.whateat.adapter.FoodAdapter;
import pannawat.com.whateat.data.Food;
import pannawat.com.whateat.model.FoodModel;


public class MenuFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private RandomFragment.OnFragmentInteractionListener mListener;

    /*view variable*/
    private View view;
    private Toolbar toolbar;
    private ListView listView;
    private RelativeLayout relativeLayout;
    private ProgressBar progressBar;

    private FragmentActivity context;
    private FoodModel foodModel;
    private ArrayList<Food> foodArrayList = new ArrayList<>();
    private FoodAdapter foodAdapter;

    public MenuFragment() {
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

    }

    public void setUI() {
        toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("MENU FOOD");
        listView = view.findViewById(R.id.list_menu);
        progressBar = view.findViewById(R.id.progress);
        relativeLayout = view.findViewById(R.id.loding_layout);
    }

    public void getData() {
        progressBar.setVisibility(View.VISIBLE);
        relativeLayout.setVisibility(View.VISIBLE);
        foodModel = new FoodModel();
        foodModel.getFoodArrayList(new GetAllFoodListener() {
            @Override
            public void getAllFood(ArrayList<Food> food) {
                foodArrayList  = food;
                foodAdapter = new FoodAdapter(context, foodArrayList);
                listView.setAdapter(foodAdapter);
                progressBar.setVisibility(View.GONE);
                relativeLayout.setVisibility(View.GONE);
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_menu, container, false);
        setUI();
        getData();
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}