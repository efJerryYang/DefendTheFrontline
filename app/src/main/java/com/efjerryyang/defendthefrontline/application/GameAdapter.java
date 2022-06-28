package com.efjerryyang.defendthefrontline.application;

import com.efjerryyang.defendthefrontline.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class GameAdapter extends BaseAdapter {

    private List<String> data;//创建私有的Bean类的data
    private Context context;

    public GameAdapter(List<String> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @Override
    public int getCount() {
        return data.size();//获取data的长度
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {//获取id
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null){//防止view不停的新建，
            view = LayoutInflater.from(context).inflate(R.layout.list_item,viewGroup,false);
        }
        TextView textView = view.findViewById(R.id.tv);
        textView.setText(data.get(i));//系统会去R文件里面找type类型的值匹配String值
        return view;
    }
}


