package com.websoftquality.agaphey.adapters.payment;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.websoftquality.agaphey.R;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

public class PaymentPlanAdapter extends RecyclerView.Adapter {


    JSONObject jsonObject;
    Context context;
    int count;
    int selectedPosition=-1;
    public OnShareClickedListener mCallback;


    public void setOnShareClickedListener(OnShareClickedListener mCallback) {
        this.mCallback = mCallback;
    }

    public interface OnShareClickedListener {
        public void ShareClicked(String item,String amount);
    }


    public PaymentPlanAdapter(Context context, JSONObject jsonObject) {
        this.context=context;
        this.jsonObject=jsonObject;
        try {

            count=jsonObject.getJSONArray("data").length();
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("TAG", "PaymentPlanAdapterexc: "+e.getMessage());
        }
    }


    @NotNull
    @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.payment_plan_adapter, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        try {
            if(selectedPosition==position)
                holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.star_color));
            else
                holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.white));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedPosition=position;
                    notifyDataSetChanged();

                    try {
                        mCallback.ShareClicked(jsonObject.getJSONArray("data").getJSONObject(position).getString("id"),
                                jsonObject.getJSONArray("data").getJSONObject(position).getString("price"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("TAG", "onClick: "+e.getMessage());
                    }

                }
            });



            ((ViewHolder)holder).tv_payment_title.setText(jsonObject.getJSONArray("data").getJSONObject(position).getString("title"));
            ((ViewHolder)holder).tv_payment_description.setText(jsonObject.getJSONArray("data").getJSONObject(position).getString("description"));
            ((ViewHolder)holder).tv_payment_price.setText("$ "
            .concat(jsonObject.getJSONArray("data").getJSONObject(position).getString("price")));
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("TAG", "onBindViewHolderexc: "+e.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return count;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
//        public ImageView image;
        TextView tv_payment_title,tv_payment_description,tv_payment_price;

        public ViewHolder(View itemView) {
            super(itemView);

            tv_payment_title =itemView.findViewById(R.id.tv_payment_title);
            tv_payment_description =itemView.findViewById(R.id.tv_payment_description);
            tv_payment_price =itemView.findViewById(R.id.tv_payment_price);

        }
    }
}