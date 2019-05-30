package com.example.cipherslab.myapplication.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.cipherslab.myapplication.Activity_Classes.Item_descrp;
import com.example.cipherslab.myapplication.ItemList.Deals_Model_List;
import com.example.cipherslab.myapplication.R;

import java.util.ArrayList;


public class sub_deals_Adapter extends RecyclerView.Adapter<sub_deals_Adapter.MainViewHolder>
{
    private Context mContext;
    private ArrayList<Deals_Model_List> arrayList;
    private ItemClickListener clickListener;

    public sub_deals_Adapter(Context context, ArrayList<Deals_Model_List> Arraylist)
    {
        mContext = context;
        arrayList =Arraylist;
    }

    @Override
    public sub_deals_Adapter.MainViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.sub_deals_itemlist, viewGroup,false);
        return new MainViewHolder (v);
    }

    @Override
    public void onBindViewHolder(sub_deals_Adapter.MainViewHolder holder, final int position)
    {
        final Deals_Model_List currentItem = arrayList.get(position);

        String itemName = currentItem.getDeal_Name ();
        holder.ProductName.setText(itemName);

        String itemPrice = currentItem.getDeal_Price ();
        holder.Product_Price.setText (itemPrice);

        String itemTotalPrice = currentItem.getProduct_total_price ();
        holder.Product_TotalPrice.setText (itemTotalPrice);

        String itemQuantity = currentItem.getProduct_quantitiy();
        holder.Product_Quantity.setText ("Quantitiy: "+itemQuantity);

       holder.RL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext , Item_descrp.class);
                intent.putExtra("productname",currentItem.getDeal_Name ());
                intent.putExtra("productprice",currentItem.getProduct_total_price ());
                intent.putExtra("product_Descrip","Price of 1: "+currentItem.getDeal_Price ()+" Rs");
                intent.putExtra ("Product_Quantity",currentItem.getProduct_quantitiy ());
                intent.putExtra ("Product_ActualPrice",currentItem.getProduct_total_price ());
                intent.putExtra ("ProductImage",currentItem.getDeal_Image ());
                mContext.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MainViewHolder extends  RecyclerView.ViewHolder {
        public TextView ProductName,Product_Quantity,Product_Price,Product_TotalPrice;
        public ImageView ProductImage;
        public RelativeLayout RL;
        public MainViewHolder(View itemView) {
            super (itemView);
            ProductName = itemView.findViewById (R.id.sub_deals_itemname);
            Product_Quantity = itemView.findViewById (R.id.sub_deals_quantity);
            Product_Price = itemView.findViewById (R.id.sub_deals_itemprice);
          //  ProductImage = itemView.findViewById (R.id.deals_item_image);
            Product_TotalPrice = itemView.findViewById (R.id.sub_deals_totalprice);

            RL = itemView.findViewById(R.id.sub_deals_parent_layout);


        }
    }
    public interface ItemClickListener {

        void itemClick(View view, int position);
    }
}
