package pannawat.com.whateat.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import pannawat.com.whateat.R;
import pannawat.com.whateat.data.Food;

/**
 * Created by Pannawat on 23/07/2017.
 */

public class FoodAdapter extends BaseAdapter {

    Context mContext;
    ArrayList<Food> foodArrayList;

    public FoodAdapter(Context context, ArrayList<Food> foodArrayList) {
        this.mContext= context;
        this.foodArrayList = foodArrayList;
    }

    public int getCount() {
        return foodArrayList.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater mInflater =
                (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(view == null)
            view = mInflater.inflate(R.layout.listview_menu, parent, false);

        TextView name = (TextView)view.findViewById(R.id.name);
        name.setText(foodArrayList.get(position).getName());

        TextView type = (TextView)view.findViewById(R.id.type);
        type.setText(foodArrayList.get(position).getType());

        TextView location = (TextView)view.findViewById(R.id.location);
        location.setText(foodArrayList.get(position).getLocation());

        TextView cal = (TextView)view.findViewById(R.id.cal);
        cal.setText(foodArrayList.get(position).getCal());

        ImageView imageView = (ImageView)view.findViewById(R.id.image_food);
        imageView.setImageBitmap(convertImage(foodArrayList.get(position).getImage()));


        return view;
    }

    public Bitmap convertImage(String strBase64) {
        byte[] decodedString = Base64.decode(strBase64, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }
}