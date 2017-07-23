package pannawat.com.whateat.model;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

import pannawat.com.whateat.GetAllFoodListener;
import pannawat.com.whateat.OnsuccessListener;
import pannawat.com.whateat.data.Food;

/**
 * Created by Pannawat on 23/07/2017.
 */

public class FoodModel {
    private ArrayList<Food> foodArrayList = new ArrayList<>();
    private DatabaseReference reference;
    private FirebaseDatabase database;
    private DatabaseReference foodReference;


    public FoodModel() {
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        foodReference = reference.child("food");
    }

    public void getFoodArrayList(final GetAllFoodListener listener) {
        foodArrayList = new ArrayList<>();
        foodReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Map map = (Map) postSnapshot.getValue();
                    Food food = new Food(
                            map.get("name").toString(),
                            map.get("image").toString(),
                            map.get("location").toString(),
                            map.get("cal").toString(),
                            map.get("type").toString()
                    );
                    foodArrayList.add(food);
                }
                listener.getAllFood(foodArrayList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void addFood(Food food, final OnsuccessListener listener) {
        foodReference.push().setValue(food).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                listener.isSuccess(task.isComplete());
            }
        });
    }

    public void randomFoodByType(final String type, final GetAllFoodListener listener) {
        foodArrayList = new ArrayList<>();
        foodReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Map map = (Map) postSnapshot.getValue();
                    if (type.equalsIgnoreCase(map.get("type").toString())) {
                        Food food = new Food(
                                map.get("name").toString(),
                                map.get("image").toString(),
                                map.get("location").toString(),
                                map.get("cal").toString(),
                                map.get("type").toString()
                        );
                        foodArrayList.add(food);
                    }
                }
                listener.getAllFood(foodArrayList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
