package com.imooc._22_Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.imooc.toast.R;

public class MyAdapter extends BaseAdapter{ 
    private static final String TAG="MyAdapter";  //用于Log.d(TAG, ……); 
    private Context mContext = null; 
    private LayoutInflater infalter = null; 
    
    /* 【数据源（1）】： icons[]是图片资源的ID，是原始数据，通过资源ID生产Bitmap图片 myImages[]，这是原始尺寸的图片，实际上，我们对图片做了进行伸缩处理，放置在myThumbs[]中，而myThumbs是需要呈现在UI的图片。 */
    private int[] icons = {R.drawable.tbs1,R.drawable.tbs2,R.drawable.tbs3
    		,R.drawable.tbs1,R.drawable.tbs2,R.drawable.tbs3
    		,R.drawable.tbs1,R.drawable.tbs2,R.drawable.tbs3};
    private Bitmap[] myImages = new Bitmap[icons.length];  //原图 
    private Bitmap[] myThumbs = new Bitmap[icons.length]; //按尺寸伸缩图 
    
    private int convertViewCount = 0;  //用于Log.d中的跟踪信息。

    /* 用于存储子view中的控件，本例只有一个，比较简单。 */ 
    private class ViewHolder{ 
        ImageView image; 
    } 
    
    public MyAdapter(Context context){ 
        /*【初始化（1）】：保存context，创建infalter，并于context关联。infalter可 从xml中创建view对象，非常方便。*/
        mContext = context; 
        infalter = LayoutInflater.from(mContext); 
        /*【初始化（2）】【数据源（2）】：创建数据源，将资源ID转换为最终呈现的Bitmap信息*/
        for(int i = 0 ; i <icons.length; i ++){ 
            myImages[i] = BitmapFactory.decodeResource(context.getResources(), icons[i]);
            //还可以使用ThumbnailUtils来处理相关的图片和视频 
            myThumbs[i] = Bitmap.createScaledBitmap(myImages[i], 100, 100, false);
        } 
    }

	@Override
	public int getCount() {
		Log.d(TAG,"getCount() return " + icons.length);
        return icons.length; 
	}

	@Override
	public Object getItem(int position) {
		return icons[position]; 
	}

	@Override
	public long getItemId(int position) {
		Log.d(TAG,"getItemId(position) is " + position);
        return position; 
	}

	// Get a View that displays the data at the specified position in the data set.
    // 这是子View布局显示的实现，是最关键的代码部分。Android只会询问当前显示的子View的呈现， 也就是不显示的子view，系统不会调用getView()，这就很好地提高了性能。系统要显示某个子view的使用，就会根据position来调用该方法。
    //参数2：convertView就是子View，如果第一次要求给出position的子view的呈现，convertView为null，我们上下滚动屏幕，系统会要求获得显示部分的各个子view的具体呈现，如果该子view之前已经创建，则convertView就是之前子view对象，我们可以利用系统保存的之前已经实现的子view对象，简化代码，提升效率。参数3 parent，从本例的层次上看就是GridView 
    public View getView(int position, View convertView, ViewGroup parent) { 
        ViewHolder holder;  //存放该子view的控件，这样我们不用每次都通过Id来查找，直接引用对象。
        Log.d(TAG,"getView() : position = " +  position 
                + "  convertView = " + (convertView == null ? "null" : convertView.toString())
                /*+ "  parent : " + (parent == null ? "null" : parent.toString())*/);
        
        //实现布局 
        if(convertView == null){  //子view第一次出现，需要构造，将重要内容（本例为控件对象）放置在viewHolder，并通过setTag()存放。 
            convertView = infalter.inflate(R.layout.ui_gridimage, null ); //通过xml来创建view 
            convertViewCount ++; 
            Log.d(TAG,"convertViewCount is " + convertViewCount); 
            holder = new ViewHolder(); 
            holder.image = (ImageView) convertView.findViewById(R.id.ui_myimage);
            // holder.image.setImageBitmap(myThumbs[position]);
            convertView.setTag(holder); 
        }else{   //子view已经出现过，利用原来已经创建的对象，获得控件信息 
            holder = (ViewHolder)convertView.getTag(); 
            Log.d(TAG,"image is " + holder.image.toString());
        } 
        
        //将图片在view中呈现，这里有个地方不是很明白，我们如果放在convertView == null中，即只在第一次出现的时候对设置控件的图片，如果我们上下滚动屏幕，发现图片显示出现混乱现象，为何？
        holder.image.setImageBitmap(myThumbs[position]); 
       return convertView;  //返回子view的对象
    } 
        
}