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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.cipherslab.myapplication.Activity_Classes.Item_descrp;
import com.example.cipherslab.myapplication.ItemList.Deals_Model_List;
import com.example.cipherslab.myapplication.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;


public class Deals_Adapter extends RecyclerView.Adapter<Deals_Adapter.MainViewHolder>
{
    private Context mContext;
    private ArrayList<Deals_Model_List> arrayList;
    private ItemClickListener clickListener;
  private   RequestQueue requestQueue;

    public Deals_Adapter(Context context, ArrayList<Deals_Model_List> Arraylist)
    {
        mContext = context;
        arrayList =Arraylist;
    }

    @Override
    public Deals_Adapter.MainViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.deals_itemlist, viewGroup,false);
        return new MainViewHolder (v);
    }

    @Override
    public void onBindViewHolder(Deals_Adapter.MainViewHolder holder, final int position)
    {
        final Deals_Model_List currentItem = arrayList.get(position);

        final String itemName = currentItem.getDeal_Name ();
        holder.ProductName.setText(itemName);

        final String itemPrice = currentItem.getDeal_Price ();
        holder.Product_Price.setText (itemPrice);


        final String itemExpiryDate = currentItem.getExpiryDate ();
        SimpleDateFormat format = new SimpleDateFormat ("EEE, MMM d, ''yy");
        String date = format.format(Date.from (Instant.parse (itemExpiryDate)));

        holder.Deal_Expiry.setText (date);

        final String item_descrip = currentItem.getDeal_Descrip ();
        holder.Product_Descript.setText (item_descrip);

        final String imageUrl = (currentItem.getDeal_Image ());
        Picasso.with (mContext).load (imageUrl).fit ().centerInside ().into (holder.ProductImage);

        holder.ProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                requestQueue = Volley.newRequestQueue (mContext);
                DealsDetails (position, itemName, itemPrice, itemExpiryDate, imageUrl, item_descrip);


//     Log.d ("BLACK", "onClick: "+  ITEMID(position));

            }
        });
        holder.RL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestQueue = Volley.newRequestQueue (mContext);
                DealsDetails (position, itemName, itemPrice, itemExpiryDate, imageUrl, item_descrip);
           //     Log.d ("BLACK", "onClick: "+  ITEMID(position));
                            }
        });
        holder.ProductImage.setTag(holder);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MainViewHolder extends  RecyclerView.ViewHolder {
        public TextView ProductName,Product_Descript,Product_Price,Deal_Expiry;
        public ImageView ProductImage;
        public RelativeLayout RL;
        public MainViewHolder(View itemView) {
            super (itemView);
            ProductName = itemView.findViewById (R.id.deals_itemname);
            Product_Descript = itemView.findViewById (R.id.deals_Descript);
            Product_Price = itemView.findViewById (R.id.deals_itemprice);
            ProductImage = itemView.findViewById (R.id.deals_item_image);
            Deal_Expiry = itemView.findViewById (R.id.deals_expiry_date);
            RL = itemView.findViewById(R.id.deals_parent_layout);


        }
    }
    public interface ItemClickListener {

        void itemClick(View view, int position);
    }
    Object Main,MainName;

    String Descript;
    private void DealsDetails(final int position, final String itemName, final String itemPrice, final String itemExpiryDate, final String imageUrl,final String ProductMainDetail){

    String url = "https://alshaikh-restaurant-server.herokuapp.com/api/deal/get";
    JsonObjectRequest request = new JsonObjectRequest (
            Request.Method.GET, url,
            null,
            new Response.Listener <JSONObject> ( ) {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONArray array = response.getJSONArray ("data").getJSONObject (position).getJSONArray ("items");
                         Main = response.getJSONArray ("data").getJSONObject (position).get ("_id");
                        MainName = response.getJSONArray ("data").getJSONObject (position).get ("name");
                        for(int i =0;i<array.length ();i++)
                        {
                            JSONObject hits = array.getJSONObject (i);
                            String Product_Name = hits.getString ("subMenuName");
                            String SubMenu_ID = hits.getString ("subMenuId");

                            String MenuID = hits.getString ("menuId");
                            int Product_Price = hits.getInt ("price");
                            int Product_Total_Price = hits.getInt ("totalPrice");
                            String Product_Quantitiy = hits.getString ("quantity");

                            Descript ="Item Name :"+Product_Name +"\n"+"Quantity: "+ Product_Quantitiy+"\n"+"Price of 1: "+Product_Price;
                            Log.d ("BLACK", "OCCC: "+Main.toString ()+"\n"+MainName.toString ()+"\n"+MenuID+"\n"+SubMenu_ID);

                            Intent intent = new Intent(mContext , Item_descrp.class);
                            intent.putExtra ("DealMenuID",Main.toString ());
                            intent.putExtra ("DealMenuName",MainName.toString ());
                            //SubMenu Items
                            intent.putExtra ("menuId",MenuID);
                            intent.putExtra ("subMenuID",SubMenu_ID);

                            String price = String.valueOf (Product_Price);

                            intent.putExtra ("SubMenuItemName",Product_Name);
                            intent.putExtra ("SubMenuItemQty",Product_Quantitiy);
                            intent.putExtra ("SubMenuItemPrice",price);





                            intent.putExtra("productname",itemName);
                            intent.putExtra("productprice",itemPrice);
                              intent.putExtra("product_Descrip","\nDescription\n"+ProductMainDetail+"\n\n"+Descript);
                            intent.putExtra ("Product_Quantity","Exprie :"+itemExpiryDate);
                            // intent.putExtra ("Product_ActualPrice",currentItem.getProduct_total_price ());
                            intent.putExtra ("ProductImage",imageUrl);

                            mContext.startActivity(intent);


                            //ArrayList.add (new Deals_Model_List (Product_Quantitiy,Product_Name,Product_Price,Product_Total_Price));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace ( );
                    }
                }
            }, new Response.ErrorListener ( ) {
        @Override
        public void onErrorResponse(VolleyError error) {
            error.printStackTrace ();
        }
    });
    requestQueue.add (request);
}




 }