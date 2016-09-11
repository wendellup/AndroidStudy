package com.imooc._21_Gallery_SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Gallery;
import android.widget.SimpleAdapter;

import com.imooc.toast.R;

public class UiGalleryTest extends Activity{ 
    /* int[] pics��Ӧ������res/drawable/�µ�ͼƬ��Դ��picsName��Ӧ���ǵ�key�������ڴ��صؽ��ڶ�����Դ��key��Ϊ��һ���������Ľ���أ����ǿ��Կ����ڶ���ͼƬû����gallery����ʾ */
    private int[] pics = {R.drawable.tbs1, R.drawable.tbs2,R.drawable.tbs3};
    private String[] picsName = {"image","image1","image"}; 
    @Override 
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState); 
        setContentView(R.layout.ui_gallery);          
        @SuppressWarnings("deprecation") 
        Gallery gallery = (Gallery)findViewById(R.id.ui_gallery);
        //SimpleAdapter������Դ�ĸ�ʽ��List<?extends Map<String ?>>�������ʽд���Ƚ��ر�����list of Map��ÿ��entry����һ�����ݵ�λ�� 
        ArrayList<HashMap<String,Object>> images = new ArrayList<HashMap<String,Object>>();
        for(int i = 0 ; i < pics.length; i ++){ 
            HashMap<String,Object> one = new HashMap<String, Object>();  
            one.put(picsName[i], Integer.valueOf(pics[i])); 
            images.add(one);  
        }  
        //SimpleAdapter��������adapter��࣬�������������                 
        SimpleAdapter adapter = new SimpleAdapter(this /*context*/, images /*����Դ*/,  
               R.layout.ui_imageitem,  /* ��View��layout����Ԥ��android.R.layout.simple_gallery_item��������TextView�����������ǵ�Ҫ�� */ 
               new String[]{"image"},  // ��Map��ѡȡ���ݣ��������ĸ�key������ѡȡ��image�����ʲ���ʾ�ڶ���ͼ
               new int[]{R.id.ui_imageItem}); //��view�пؼ�ID
        gallery.setAdapter(adapter); 
    } 
}