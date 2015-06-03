# Android-AnomalyButton
Android不规则按钮

利用判断图片背景像素来判断形状

使用方法如下：
```xml
    <info.smemo.androidanomalybutton.AnomalyButton
        android:id="@+id/button"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:src="@drawable/play"
        app:bgcolor="@android:color/transparent"
        android:layout_alignParentTop="true"
        android:layout_centerHorizontal="true" />

```

设置bgcolor，为其他区域颜色，如果其它区域均为透明色，则设置透明。判断像素是透明的地方均为形状之外

设置src为需要使用的图片
支持使用selector

使用selector的时候，如果有两个状态（press=true和false）判断背景根据当前状态。如果只有一个状态，则取该状态

使用时候直接复制AnomalyButton以及value/attrs.xml


#About Devloper

Neo (http://blog.smemo.info) 
