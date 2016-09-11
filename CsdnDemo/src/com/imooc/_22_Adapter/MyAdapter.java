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
    private static final String TAG="MyAdapter";  //����Log.d(TAG, ����); 
    private Context mContext = null; 
    private LayoutInflater infalter = null; 
    
    /* ������Դ��1������ icons[]��ͼƬ��Դ��ID����ԭʼ���ݣ�ͨ����ԴID����BitmapͼƬ myImages[]������ԭʼ�ߴ��ͼƬ��ʵ���ϣ����Ƕ�ͼƬ���˽�����������������myThumbs[]�У���myThumbs����Ҫ������UI��ͼƬ�� */
    private int[] icons = {R.drawable.tbs1,R.drawable.tbs2,R.drawable.tbs3
    		,R.drawable.tbs1,R.drawable.tbs2,R.drawable.tbs3
    		,R.drawable.tbs1,R.drawable.tbs2,R.drawable.tbs3};
    private Bitmap[] myImages = new Bitmap[icons.length];  //ԭͼ 
    private Bitmap[] myThumbs = new Bitmap[icons.length]; //���ߴ�����ͼ 
    
    private int convertViewCount = 0;  //����Log.d�еĸ�����Ϣ��

    /* ���ڴ洢��view�еĿؼ�������ֻ��һ�����Ƚϼ򵥡� */ 
    private class ViewHolder{ 
        ImageView image; 
    } 
    
    public MyAdapter(Context context){ 
        /*����ʼ����1����������context������infalter������context������infalter�� ��xml�д���view���󣬷ǳ����㡣*/
        mContext = context; 
        infalter = LayoutInflater.from(mContext); 
        /*����ʼ����2����������Դ��2��������������Դ������ԴIDת��Ϊ���ճ��ֵ�Bitmap��Ϣ*/
        for(int i = 0 ; i <icons.length; i ++){ 
            myImages[i] = BitmapFactory.decodeResource(context.getResources(), icons[i]);
            //������ʹ��ThumbnailUtils��������ص�ͼƬ����Ƶ 
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
    // ������View������ʾ��ʵ�֣�����ؼ��Ĵ��벿�֡�Androidֻ��ѯ�ʵ�ǰ��ʾ����View�ĳ��֣� Ҳ���ǲ���ʾ����view��ϵͳ�������getView()����ͺܺõ���������ܡ�ϵͳҪ��ʾĳ����view��ʹ�ã��ͻ����position�����ø÷�����
    //����2��convertView������View�������һ��Ҫ�����position����view�ĳ��֣�convertViewΪnull���������¹�����Ļ��ϵͳ��Ҫ������ʾ���ֵĸ�����view�ľ�����֣��������view֮ǰ�Ѿ���������convertView����֮ǰ��view�������ǿ�������ϵͳ�����֮ǰ�Ѿ�ʵ�ֵ���view���󣬼򻯴��룬����Ч�ʡ�����3 parent���ӱ����Ĳ���Ͽ�����GridView 
    public View getView(int position, View convertView, ViewGroup parent) { 
        ViewHolder holder;  //��Ÿ���view�Ŀؼ����������ǲ���ÿ�ζ�ͨ��Id�����ң�ֱ�����ö���
        Log.d(TAG,"getView() : position = " +  position 
                + "  convertView = " + (convertView == null ? "null" : convertView.toString())
                /*+ "  parent : " + (parent == null ? "null" : parent.toString())*/);
        
        //ʵ�ֲ��� 
        if(convertView == null){  //��view��һ�γ��֣���Ҫ���죬����Ҫ���ݣ�����Ϊ�ؼ����󣩷�����viewHolder����ͨ��setTag()��š� 
            convertView = infalter.inflate(R.layout.ui_gridimage, null ); //ͨ��xml������view 
            convertViewCount ++; 
            Log.d(TAG,"convertViewCount is " + convertViewCount); 
            holder = new ViewHolder(); 
            holder.image = (ImageView) convertView.findViewById(R.id.ui_myimage);
            // holder.image.setImageBitmap(myThumbs[position]);
            convertView.setTag(holder); 
        }else{   //��view�Ѿ����ֹ�������ԭ���Ѿ������Ķ��󣬻�ÿؼ���Ϣ 
            holder = (ViewHolder)convertView.getTag(); 
            Log.d(TAG,"image is " + holder.image.toString());
        } 
        
        //��ͼƬ��view�г��֣������и��ط����Ǻ����ף������������convertView == null�У���ֻ�ڵ�һ�γ��ֵ�ʱ������ÿؼ���ͼƬ������������¹�����Ļ������ͼƬ��ʾ���ֻ�������Ϊ�Σ�
        holder.image.setImageBitmap(myThumbs[position]); 
       return convertView;  //������view�Ķ���
    } 
        
}