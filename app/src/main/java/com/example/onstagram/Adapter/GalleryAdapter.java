package com.example.onstagram.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onstagram.NewPost.NewPost_image;
import com.example.onstagram.R;

import java.util.ArrayList;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder> {

    Context context;
    Cursor cursor;
    public ArrayList<String> path_list = new ArrayList<>();


    public ArrayList<String> whole_list = new ArrayList<>();

    int lastPosition;
    public GalleryAdapter(Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;
    }

    @NonNull
    @Override
    public GalleryAdapter.GalleryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.gallery_dialog_image, parent, false);

        return new GalleryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryAdapter.GalleryViewHolder holder, int position) {
        System.out.println("dd");

        if(cursor.moveToPosition(position)) {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

            String imagePath = cursor.getString(columnIndex);

            holder.path = imagePath;

            whole_list.add(imagePath);

            System.out.println(String.valueOf(whole_list.size()));

            String temp_image = whole_list.get(0);

            ImageView main_image = ((NewPost_image)context).findViewById(R.id.gallery_main_image);

            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);

            Bitmap temp_bm = BitmapFactory.decodeFile(temp_image);

            main_image.setImageBitmap(temp_bm);

            holder.imageView.setImageBitmap(bitmap);

            holder.imageView.setOnClickListener(v -> {

                if(path_list.size() == 0) {

                    Bitmap bm = BitmapFactory.decodeFile(whole_list.get(0));

                    main_image.setImageBitmap(bm);
                }

                if(!path_list.contains(holder.path)) {
                    if(path_list.size() >= 10) {
                        Toast.makeText(context, "사진은 10장까지 선택할 수 있습니다.", Toast.LENGTH_SHORT).show();
                    } else {

                        System.out.println( "holder.imagePath : " + holder.path);

                        path_list.add(holder.path);

                        for (int i = 0; i < path_list.size(); i++) {
                            String p = path_list.get(i);

                            System.out.println("path_list :   " + p);
                        }

                        Bitmap bm = BitmapFactory.decodeFile(holder.path);

                        main_image.setImageBitmap(bm);
                        main_image.setFocusable(true);
                        holder.check.setVisibility(View.VISIBLE);

                    }
                } else {

                    int indexToRemove = path_list.indexOf(holder.path);

                        path_list.remove(indexToRemove);
                        holder.check.setVisibility(View.GONE);

                        if(path_list.size() == 1) {
                            lastPosition = 0;

                            Bitmap bm = BitmapFactory.decodeFile(temp_image);

                            main_image.setImageBitmap(bm);

                        } else if (path_list.size() > 1) {

                            System.out.println(String.valueOf(indexToRemove));

                            for (int i = indexToRemove; i < path_list.size(); i++) {
                                String currentValue = path_list.get(i);
                                System.out.println("CurrentValue : " + currentValue);

                                path_list.set(i, currentValue);
                            }

                            lastPosition = path_list.size() - 1;
                            String temp_path = path_list.get(lastPosition);

                            Bitmap bm = BitmapFactory.decodeFile(temp_path);

                            main_image.setImageBitmap(bm);
                        }
                }
            });
        }
    }

    @Override
    public int getItemCount() {

        if(cursor == null) {
            System.out.println("GetItemCount : 0");
            return 0;

        }

        return cursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        if(cursor != null) {
            cursor.close();
        }
        cursor = newCursor;

        if(newCursor != null) {
            notifyDataSetChanged();
        }
    }

    public class GalleryViewHolder extends RecyclerView.ViewHolder{

        protected ImageView imageView;
        protected ImageView check;

        protected String path;

        public GalleryViewHolder(@NonNull View itemView) {
            super(itemView);
            this.imageView = itemView.findViewById(R.id.galleryImageView);
            this.check = itemView.findViewById(R.id.galleryItemCount);
            this.path = path;

        }
    }
}
