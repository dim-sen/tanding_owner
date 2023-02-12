package com.dimsen.tandingowner.adapter;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dimsen.tandingowner.R;
import com.dimsen.tandingowner.model.Gor;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

public class GorAdapter extends RecyclerView.Adapter<GorAdapter.GorViewHolder> {
    List<Gor> gorList;
    Context context;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Gor");

    private GorItemCLickListener gorItemCLickListener;

    public GorAdapter(List<Gor> gorList, Context context, GorItemCLickListener gorItemCLickListener) {
        this.gorList = gorList;
        this.context = context;
        this.gorItemCLickListener = gorItemCLickListener;
    }

    @NonNull
    @Override
    public GorAdapter.GorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gor_items, parent, false);
        return new GorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GorAdapter.GorViewHolder holder, int position) {
        Gor gor = gorList.get(position);
        Glide.with(holder.itemView.getContext())
                .load(gor.getGor_image())
                        .into(holder.imageViewGor);
        holder.textViewNama.setText(gor.getNama_gor());
        holder.textViewTanggal.setText(gor.getTanggal_buka());
        holder.textViewJam.setText(gor.getJam_buka());
        holder.textViewHarga.setText(gor.getHarga());
        holder.imageButtonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        databaseReference.child(gor.getKey()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).setMessage("Apakah yakin?" + gor.getNama_gor());
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gorItemCLickListener.gorOnItemClick(gor);
            }
        });
    }

    @Override
    public int getItemCount() {
        return gorList.size();
    }

    public static class GorViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewGor;
        TextView textViewNama, textViewTanggal, textViewJam, textViewHarga;
        ImageButton imageButtonDelete;
        public GorViewHolder(@NonNull View itemView) {
            super(itemView);

            imageViewGor = itemView.findViewById(R.id.img_gor);
            textViewNama = itemView.findViewById(R.id.txt_nama_gor);
            textViewTanggal = itemView.findViewById(R.id.txt_tanggal);
            textViewJam = itemView.findViewById(R.id.txt_jam);
            textViewHarga = itemView.findViewById(R.id.txt_harga);
            imageButtonDelete = itemView.findViewById(R.id.btn_delete);
        }
    }

    public interface GorItemCLickListener {
        public void gorOnItemClick(Gor gor);
    }
}
