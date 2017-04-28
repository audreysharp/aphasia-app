package com.example.audrey.gridview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomGrid extends BaseAdapter {

    String [] result;
    Context context;
    int [] imageId;
    private static LayoutInflater inflater = null;

    public CustomGrid (MainActivity mainActivity, String[] osNameList, int[] osImages) {
        // TODO Auto-generated constructor stub
        result = osNameList;
        context = mainActivity;
        imageId = osImages;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return result.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class Holder {
        TextView os_text;
        ImageView os_img;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        Holder holder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.grid_single, parent, false);
            holder = new Holder();
            holder.os_img = (ImageView) convertView.findViewById(R.id.mThumbIds);
            holder.os_text = (TextView) convertView.findViewById(R.id.captions);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        ImageView imageView = holder.os_img;
        TextView textView = holder.os_text;

        holder.os_img.getLayoutParams().height = 400;
        holder.os_img.getLayoutParams().width = 400;
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setPadding(2, 2, 2, 2);

        imageView.setImageResource(imageId[position]);
        textView.setText(result[position]);

        return convertView;
    }

}