package com.example.cipherslab.myapplication.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.cipherslab.myapplication.Activity_Classes.SubMenus_Activity;
import com.example.cipherslab.myapplication.ItemList.Main_Model_List;
import com.example.cipherslab.myapplication.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class Main_Adapater extends RecyclerView.Adapter<Main_Adapater.MainViewHolder>
{
    private Context mContext;
    private ArrayList<Main_Model_List> arrayList;
    private ItemClickListener clickListener;

    public Main_Adapater(Context context,ArrayList<Main_Model_List> Arraylist)
    {
        mContext = context;
        arrayList =Arraylist;
    }

    @Override
    public Main_Adapater.MainViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.main_itemlist, viewGroup,false);
        return new MainViewHolder (v);
    }

    @Override
    public void onBindViewHolder(Main_Adapater.MainViewHolder holder, final int position)
    {
        final Main_Model_List currentItem = arrayList.get(position);

        String itemName = currentItem.getCategory_Name ();
        holder.ProductName.setText(itemName);


        String imageUrl = (currentItem.getImage ());
        Picasso.with (mContext).load (imageUrl).fit ().centerInside ().into (holder.ProductImage);

        holder.ProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (mContext , SubMenus_Activity.class);
                intent.putExtra("position",position);
                mContext.startActivity(intent);

            }
        });
        holder.RL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (mContext , SubMenus_Activity.class);
             intent.putExtra("position",position);
                mContext.startActivity(intent);
            }
        });
        holder.ProductImage.setTag(holder);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MainViewHolder extends  RecyclerView.ViewHolder {
        public TextView ProductName;
        public ImageView ProductImage;
        public LinearLayout RL;
        public MainViewHolder(View itemView) {
            super (itemView);
            ProductName = itemView.findViewById (R.id.main_list_text);
            ProductImage = itemView.findViewById (R.id.main_list_image);
            RL = itemView.findViewById(R.id.main_list_btn);


        }
    }
    public interface ItemClickListener {

        void itemClick(View view, int position);
    }
}