package com.example.cipherslab.myapplication.Adapters;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cipherslab.myapplication.Activity_Classes.Item_descrp;
import com.example.cipherslab.myapplication.ItemList.SubItem_GS;
import com.example.cipherslab.myapplication.ItemList.SubMenu;
import com.example.cipherslab.myapplication.R;
import com.example.cipherslab.myapplication.Sqlite.DB;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Sub_Menu_Adapter extends RecyclerView.Adapter<Sub_Menu_Adapter.MenuViewHolder>
        {
private Context mContext;
private ArrayList<SubMenu> arrayList;
            private ItemClickListener clickListener;

public Sub_Menu_Adapter(Context context,ArrayList<SubMenu> Arraylist)
        {
        mContext = context;
        arrayList =Arraylist;
        }

@Override
public MenuViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.submenu_itemlayout, viewGroup,false);
        return new MenuViewHolder(v);
        }

@Override
public void onBindViewHolder(MenuViewHolder holder, final int position)
        {
        final SubMenu currentItem = arrayList.get(position);

        String itemName = currentItem.getName ();
        holder.ProductName.setText(itemName);

        final String ItemPrice = String.valueOf (currentItem.getPrice ());
        holder.ProductPrice.setText(ItemPrice);

        String imageUrl = (currentItem.getPicture ());
        Picasso.with (mContext).load (imageUrl).fit ().centerInside ().into (holder.ProductImage);

            holder.ProductImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext , Item_descrp.class);
                    intent.putExtra("image",currentItem.getPicture ());
                    intent.putExtra("productname",currentItem.getName ());
                    intent.putExtra("productprice",ItemPrice);
                    intent.putExtra("product_Descrip",currentItem.getDescrpition_ ());
                    intent.putExtra("ProductImage",currentItem.getPicture ());

                    mContext.startActivity(intent);

                }
            });
            holder.RL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext , Item_descrp.class);
                    intent.putExtra("image",currentItem.getPicture ());
                    intent.putExtra("productname",currentItem.getName ());
                    intent.putExtra("productprice",ItemPrice);
                    intent.putExtra("product_Descrip",currentItem.getDescrpition_ ());
                    intent.putExtra("ProductImage",currentItem.getPicture ());

                    mContext.startActivity(intent);

                }
            });
            holder.ProductImage.setTag(holder);
            holder.btn_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    SubItem_GS subItem_gs =new SubItem_GS (currentItem.getName (),Integer.parseInt (ItemPrice),1,Integer.parseInt (ItemPrice));

                    DB db = new DB (mContext);
                    if(db.hasObject (currentItem.getName (),subItem_gs)== true){
                        Log.d ("TAG","item_Update");
                        //     Toast.makeText (getApplicationContext (),"DATA UPDATE",Toast.LENGTH_LONG).show ();
                    }
                    else {
                    }
                    //(count*product_price_Int)
                    db.add_ (subItem_gs);
                    Log.d ("TAG","Item ADDED"+currentItem.getName ()+" "+Integer.parseInt (ItemPrice)+" "+1+" "+ Integer.parseInt (ItemPrice));
                    //  Toast.makeText (mContext,currentItem.getName ()+" "+Integer.parseInt (ItemPrice)+" "+1+" "+ Integer.parseInt (ItemPrice)+"", Toast.LENGTH_SHORT).show ( );
                    Toast.makeText(mContext,"ITEM ADDED",Toast.LENGTH_SHORT).show();
                }
            });
        }

@Override
public int getItemCount() {
        return arrayList.size();
        }

public class MenuViewHolder extends  RecyclerView.ViewHolder {
    public TextView ProductName, ProductPrice;
    public ImageView ProductImage;
    public ImageView btn_add;
    public RelativeLayout RL;
    public MenuViewHolder(View itemView) {
        super (itemView);
        ProductName = itemView.findViewById (R.id.itemname);
        ProductPrice = itemView.findViewById (R.id.itemprice);
        ProductImage = itemView.findViewById (R.id.submenu_item_image);
        btn_add = itemView.findViewById(R.id.itemadd);
        RL = itemView.findViewById(R.id.parent_layout);


    }
}
            public interface ItemClickListener {

                 void itemClick(View view, int position);
            }

        }
