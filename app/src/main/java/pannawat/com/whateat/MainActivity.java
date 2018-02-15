package pannawat.com.whateat;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    /*view variable*/
    private BottomNavigationView bottomNavigationView;

    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;

    private RandomFragment randomFragment;
    private AddFoodFragment addFoodFragment;
    private MenuFragment menuFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        randomFragment = new RandomFragment();
        addFoodFragment = new AddFoodFragment();
        menuFragment = new MenuFragment();
        openFragment(randomFragment);
        setTitleName("RANDOM");
        setUI();
        setListener();
    }

    public void setUI() {
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_nav_view);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);

    }

    private void setTitleName(String name) {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.bringToFront();
    }

    public void setListener() {
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_food:
                setTitleName("RANDOM");
                openFragment(randomFragment);
                break;
            case R.id.item_add:
                setTitleName("ADD FOOD");
                openFragment(addFoodFragment);
                break;
            case R.id.item_menu:
                setTitleName("MENU FOOD");
                openFragment(menuFragment);
                break;
        }
        return false;
    }

    public void openFragment(final Fragment fragment) {
        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
