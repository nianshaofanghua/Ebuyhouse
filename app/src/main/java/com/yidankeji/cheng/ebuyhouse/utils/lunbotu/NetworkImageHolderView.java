package com.yidankeji.cheng.ebuyhouse.utils.lunbotu;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.yidankeji.cheng.ebuyhouse.R;


/**
 * Created by Sai on 15/8/4. 网络图片加载例子
 */
public class NetworkImageHolderView implements CBPageAdapter.Holder<String> {
	private ImageView imageView;

	@Override
	public View createView(Context context) {
		imageView = new ImageView(context);
		imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
		return imageView;
	}

	@Override
	public void UpdateUI(Context context, int position, String data) {

		RequestOptions options = new RequestOptions();
		options.placeholder(R.mipmap.erro_image_chang);
		options.error(R.mipmap.erro_image_chang);
        Glide.with(context).load(data).apply(options).into(imageView);

	}
}
