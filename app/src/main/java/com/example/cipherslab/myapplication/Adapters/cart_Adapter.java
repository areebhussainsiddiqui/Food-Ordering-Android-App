package com.example.cipherslab.myapplication.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cipherslab.myapplication.Activity_Classes.Cart_Activity;
import com.example.cipherslab.myapplication.ItemList.SubItem_GS;
import com.example.cipherslab.myapplication.R;
import com.example.cipherslab.myapplication.Sqlite.DB;

import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

public class cart_Adapter extends RecyclerView.Adapter<cart_Adapter.Myholder> {
    Context context;
    private OnTotalPriceChanger mListener;
    List<SubItem_GS> dataModelArrayList;
    DB db;
        private String Url = "https://alshaikh-restaurant-server.herokuapp.com/";
        private String Path = "api/order/create";
    public cart_Adapter(List<SubItem_GS> dataModelArrayList, OnTotalPriceChanger listener) {
        mListener = listener;
        this.dataModelArrayList = dataModelArrayList;

    }

     public cart_Adapter(Context context, List<SubItem_GS> dataModelArrayList, OnTotalPriceChanger listener) {
         this.context = context;
         mListener = listener;
         this.dataModelArrayList = dataModelArrayList;
         db = new DB (context);

     }

class Myholder extends RecyclerView.ViewHolder{
        TextView name,price,quantity,actualprice;
        ImageView deletebtn;

    public Myholder(View itemView) {
        super(itemView);
            name            = itemView.findViewById(R.id.receipt_name);
            price           =itemView.findViewById(R.id.receipt_price);
            quantity        =itemView.findViewById(R.id.receipt_Quantity);
            actualprice     =itemView.findViewById(R.id.receipt_actualprice);
            deletebtn       = itemView.findViewById (R.id.deletebtn);
    }
}


    @Override
    public cart_Adapter.Myholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.receipt_itemlayout,null,false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        return new cart_Adapter.Myholder(view);

    }

    int id;
    @Override
    public void onBindViewHolder(cart_Adapter.Myholder holder, final int position) {
        final SubItem_GS dataModel = dataModelArrayList.get(position);

        holder.name.setText(dataModel.getItemName());
        holder.price.setText(String.valueOf(dataModel.getItemPrice()));
        holder.quantity.setText(String.valueOf(dataModel.getItemQuantity()));
        holder.actualprice.setText(String.valueOf(dataModel.getTotalAmount ()));

        final Cart_Activity cart_activity = new Cart_Activity ();
        holder.deletebtn.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {

                Log.d ("TAG", "onClick: "+dataModel.getId ());
                removeItem(position);
                db_delete (dataModel.getId ());

 /*    if( dataModel.getItemQuantity () == 0 )// Can't remove an item if it's already at 0
                    return;
                dataModel.setItemQuantity ( dataModel.getItemQuantity () - 1);
                int a= Integer.parseInt (db.DisplayInfo ());
               int difference =  (a- (dataModel.getItemPrice ()));
*/
                mListener.onPriceChange ( dataModel.getTotalAmount () );

                notifyItemChanged( position );


//refresh the activity page.

            }
        });
    }





    public void removeItem(int position) {
        dataModelArrayList.remove (position);
        notifyItemRemoved (position);
        notifyItemRangeChanged (position, dataModelArrayList.size ( ));
    }
    private void db_delete(int id){
      Cart_Activity cart_activity = new Cart_Activity ();
        db = new DB (getApplicationContext ());
        db.getWritableDatabase ( );
        db.delete_(id);
        notifyDataSetChanged ( );

    }
    @Override
    public int getItemCount() {

        return dataModelArrayList.size();
    }
    public interface OnTotalPriceChanger {
        void onPriceChange(int change );
    }

  }



