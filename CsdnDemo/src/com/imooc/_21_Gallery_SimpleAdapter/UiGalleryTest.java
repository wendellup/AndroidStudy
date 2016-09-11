package com.imooc._21_Gallery_SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Gallery;
import android.widget.SimpleAdapter;

import com.imooc.toast.R;

public class UiGalleryTest extends Activity{ 
    /* int[] pics对应我们在res/drawable/下的图片资源，picsName对应他们的key，我们在此特地将第二个资源的key设为不一样。在最后的结果重，我们可以看到第二章图片没有在gallery中显示 */
    private int[] pics = {R.drawable.tbs1, R.drawable.tbs2,R.drawable.tbs3};
    private String[] picsName = {"image","image1","image"}; 
    @Override 
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState); 
        setContentView(R.layout.ui_gallery);          
        @SuppressWarnings("deprecation") 
        Gallery gallery = (Gallery)findViewById(R.id.ui_gallery);
        //SimpleAdapter的数据源的格式是List<?extends Map<String ?>>，这个格式写法比较特别，它是list of Map，每个entry就是一个数据单位。 
        ArrayList<HashMap<String,Object>> images = new ArrayList<HashMap<String,Object>>();
        for(int i = 0 ; i < pics.length; i ++){ 
            HashMap<String,Object> one = new HashMap<String, Object>();  
            one.put(picsName[i], Integer.valueOf(pics[i])); 
            images.add(one);  
        }  
        //SimpleAdapter和其他的adapter差不多，参数很容易理解                 
        SimpleAdapter adapter = new SimpleAdapter(this /*context*/, images /*数据源*/,  
               R.layout.ui_imageitem,  /* 子View的layout，有预置android.R.layout.simple_gallery_item，但那是TextView，不符合我们的要求 */ 
               new String[]{"image"},  // 从Map中选取数据，本例即哪个key，我们选取“image”，故不显示第二个图
               new int[]{R.id.ui_imageItem}); //子view中控件ID
        gallery.setAdapter(adapter); 
    } 
}