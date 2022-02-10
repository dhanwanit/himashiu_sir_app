package com.arcticfoxappz.walmartcoupon.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.arcticfoxappz.walmartcoupon.R;
import com.arcticfoxappz.walmartcoupon.activity.ClickListenerItem;
import com.arcticfoxappz.walmartcoupon.bean.Coupons;
import java.util.List;
import java.util.Random;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductViewHolder> {

    private Context mCtx;
    private List<Coupons> couponsList;
    private ClickListenerItem onItemClickListener;
 
    public ProductsAdapter(Context mCtx, List<Coupons> couponsList, ClickListenerItem onItemClickListener) {
        this.mCtx = mCtx;
        this.couponsList = couponsList;
        this.onItemClickListener=onItemClickListener;
    }
 
    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.coupons_list,parent,false);
        return new ProductViewHolder(view);
    }
 
    @Override
    public void onBindViewHolder(ProductViewHolder holder, final int position) {
        final Coupons coupons = couponsList.get(position);
        String peopleused="People Used";
        String likes="Likes";
        Random r = new Random();
        int result = r.nextInt(500 -50 ) + 50 ;
        int result2 = r.nextInt(5000 -2500 ) + 50 ;
        holder.textViewRating.setText(String.valueOf(result2 + " " + peopleused));
        holder.textViewRating21.setText(String.valueOf(result + " " + likes));
        //Log.d("coupons",""+coupons.getDescription());
        holder.textViewTitle.setText(coupons.getDescription());

        holder.textViewRating2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null)
                    onItemClickListener.onItemCLickListener(position,coupons);
            }
        });
      /*   holder.textViewShortDesc.setText(product.getShortdesc());
        holder.textViewRating.setText(String.valueOf(product.getRating()));
        holder.textViewPrice.setText(String.valueOf(product.getPrice()));
    */
    }
 
    @Override
    public int getItemCount() {
        return couponsList.size();
    }
 
    static class ProductViewHolder extends RecyclerView.ViewHolder {
 
        TextView textViewTitle, textViewShortDesc, textViewRating, textViewRating2,textViewRating21;
        ImageView imageView;
 
        ProductViewHolder(View itemView) {
            super(itemView);
 
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewShortDesc = itemView.findViewById(R.id.textViewShortDesc);
            textViewRating = itemView.findViewById(R.id.textViewRating);
//            imageView = itemView.findViewById(R.id.imageView);
            textViewRating2 = itemView.findViewById(R.id.textViewRating2);
            textViewRating21 = itemView.findViewById(R.id.textViewRating21);
        }
    }
}