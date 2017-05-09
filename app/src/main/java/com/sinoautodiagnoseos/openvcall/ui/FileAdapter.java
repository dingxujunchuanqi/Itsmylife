package com.sinoautodiagnoseos.openvcall.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sinoautodiagnoseos.R;
import com.sinoautodiagnoseos.entity.Download.FileDownload;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by HQ_Demos on 2017/4/12.
 */

public class FileAdapter extends BaseAdapter {
    private List<FileDownload> filelists;
    private Context context;
    private LayoutInflater inflater = null;

    public FileAdapter(Context context, List<FileDownload> filelists) {
        System.out.println("adapter="+filelists.size());
        this.filelists=filelists;
        this.context=context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return filelists.size();
    }

    @Override
    public Object getItem(int position) {
        return filelists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if (convertView==null){
            holder= new ViewHolder();
            convertView=inflater.inflate(R.layout.grid_item,null);
            holder.pic= (CircleImageView) convertView.findViewById(R.id.downloads_pic);
            holder.file_name= (TextView) convertView.findViewById(R.id.downloads_filename);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }
        System.out.println("图片名字-----"+filelists.get(position).getFileName());
        holder.pic.setImageResource(R.drawable.none);
        holder.file_name.setText(filelists.get(position).getFileName().toString());
        return convertView;
    }

    static class ViewHolder {
        CircleImageView pic;
        TextView file_name;
    }
}
