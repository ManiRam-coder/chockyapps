package com.nidea.app.selfiethief.support;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.nidea.app.selfiethief.R;

public class CustomAdapter extends BaseAdapter {

	private LayoutInflater layoutinflater;
	private List<ItemObject> listStorage;
	private Context context;
	public ArrayList timings;
	public CustomAdapter(Context context, List<ItemObject> customizedListView,ArrayList timings) {
		this.context = context;
		layoutinflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		listStorage = customizedListView;
		this.timings = timings;
	}

	@Override
	public int getCount() {
		if(listStorage != null){
			return listStorage.size();	
		}
		else{
			return 0;
		}
	}

	@Override
	public Object getItem(int position) {
		return listStorage.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder listViewHolder;
		if(convertView == null){
			listViewHolder = new ViewHolder();
			convertView = layoutinflater.inflate(R.layout.listview_with_text_image, parent, false);
			listViewHolder.textDate = (TextView)convertView.findViewById(R.id.textView);
			listViewHolder.textTime = (TextView)convertView.findViewById(R.id.textView1);
			listViewHolder.imageInListView = (ImageView)convertView.findViewById(R.id.imageView);
			convertView.setTag(listViewHolder);
		}else{
			listViewHolder = (ViewHolder)convertView.getTag();
		}

		listViewHolder.textDate.setText(listStorage.get(position).getDate());
		listViewHolder.textTime.setText(listStorage.get(position).getTime());
		// int imageResourceId = this.context.getResources().getIdentifier(listStorage.get(position).getImageResource(), "drawable", this.context.getPackageName());
		//listViewHolder.imageInListView.setBackground(decodeSampledBitmapFromUri(listStorage.get(position).getImagePath(), 220, 220));
		listViewHolder.imageInListView.setImageBitmap(decodeSampledBitmapFromUri(listStorage.get(position).getImagePath(), 220, 220));

		return convertView;
	}

	static class ViewHolder{
		TextView textTime;
		TextView textDate;
		ImageView imageInListView;
	}

	public Bitmap decodeSampledBitmapFromUri(String path, int reqWidth, int reqHeight) {

		Bitmap bm = null;
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);

		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

		options.inJustDecodeBounds = false;
		bm = BitmapFactory.decodeFile(path, options); 

		return bm;   
	}

	public int calculateInSampleSize(

			BitmapFactory.Options options, int reqWidth, int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			if (width > height) {
				inSampleSize = Math.round((float)height / (float)reqHeight);    
			} else {
				inSampleSize = Math.round((float)width / (float)reqWidth);    
			}   
		}

		return inSampleSize;    
	}
}