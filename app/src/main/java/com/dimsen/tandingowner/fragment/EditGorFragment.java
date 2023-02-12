package com.dimsen.tandingowner.fragment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.dimsen.tandingowner.R;
import com.dimsen.tandingowner.model.Gor;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditGorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditGorFragment extends Fragment {

    Toolbar entryEditToolbar;
    EditText editTextNama, editTextAlamat, editTextjamBuka, editTextJamTutup, editTextTanggal, editTextGorImg, editTextKontak, editTextHarga;
    Button buttonSubmit;
    private final Integer GALLERY_CODE = 100;
    Uri imgUri;

    DatabaseReference databaseReference;
    ProgressDialog progressDialog;
    StorageReference storageReference;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "nama_gor";
    private static final String ARG_PARAM2 = "alamat";
    private static final String ARG_PARAM3 = "jam_buka";
    private static final String ARG_PARAM4 = "jam_tutup";
    private static final String ARG_PARAM5 = "tanggal_buka";
    private static final String ARG_PARAM6 = "gor_image";
    private static final String ARG_PARAM7 = "kontak";
    private static final String ARG_PARAM8 = "harga";
    private static final String ARG_PARAM9 = "key";

    // TODO: Rename and change types of parameters
    private String nama_gor;
    private String alamat;
    private String jam_buka;
    private String jam_tutup;
    private String tanggal_buka;
    private String gor_image;
    private String kontak;
    private String harga;
    private String key;

    public EditGorFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param nama_gor Parameter 1.
     * @param alamat Parameter 2.
     * @return A new instance of fragment EditGorFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditGorFragment newInstance(String nama_gor, String alamat, String jam_buka,
                                              String jam_tutup, String tanggal_buka, String gor_image,
                                              String kontak, String harga, String key) {
        EditGorFragment fragment = new EditGorFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, nama_gor);
        args.putString(ARG_PARAM2, alamat);
        args.putString(ARG_PARAM3, jam_buka);
        args.putString(ARG_PARAM4, jam_tutup);
        args.putString(ARG_PARAM5, tanggal_buka);
        args.putString(ARG_PARAM6, gor_image);
        args.putString(ARG_PARAM7, kontak);
        args.putString(ARG_PARAM8, harga);
        args.putString(ARG_PARAM9, key);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            nama_gor = getArguments().getString(ARG_PARAM1);
            alamat = getArguments().getString(ARG_PARAM2);
            jam_buka = getArguments().getString(ARG_PARAM3);
            jam_tutup = getArguments().getString(ARG_PARAM4);
            tanggal_buka = getArguments().getString(ARG_PARAM5);
            gor_image = getArguments().getString(ARG_PARAM6);
            kontak = getArguments().getString(ARG_PARAM7);
            harga = getArguments().getString(ARG_PARAM8);
            key = getArguments().getString(ARG_PARAM9);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_gor, container, false);

        databaseReference = FirebaseDatabase.getInstance().getReference("Gor");

        storageReference = FirebaseStorage.getInstance().getReference("Gor");

        entryEditToolbar = view.findViewById(R.id.toolbar_entry_edit);
        ((AppCompatActivity)getActivity()).setSupportActionBar(entryEditToolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Edit Gor");

        setOnBackPressed(view);

        editTextNama = view.findViewById(R.id.txt_input_nama_gor_edit);
        editTextNama.setText(nama_gor);

        editTextAlamat = view.findViewById(R.id.txt_input_alamat_edit);
        editTextAlamat.setText(alamat);

        editTextjamBuka = view.findViewById(R.id.input_time_edit);
        editTextjamBuka.setText(jam_buka);

        editTextJamTutup = view.findViewById(R.id.input_time_closed_edit);
        editTextJamTutup.setText(jam_tutup);

        editTextTanggal = view.findViewById(R.id.input_date_edit);
        editTextTanggal.setText(tanggal_buka);

        editTextGorImg = view.findViewById(R.id.input_img_edit);
        editTextGorImg.setText(gor_image);

        editTextKontak = view.findViewById(R.id.txt_input_kontak_edit);
        editTextKontak.setText(kontak);

        editTextHarga = view.findViewById(R.id.txt_input_harga_edit);
        editTextHarga.setText(harga);

        buttonSubmit = view.findViewById(R.id.btn_submit_edit);

        setTimePicker();
        setDatePicker();
        setImgPicker();

        setBtnClick();

        return view;
    }

    private void setBtnClick() {
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String namaGor = editTextNama.getText().toString();
                String alamat = editTextAlamat.getText().toString();
                String jamBuka = editTextjamBuka.getText().toString();
                String jamTutup = editTextJamTutup.getText().toString();
                String tanggal = editTextTanggal.getText().toString();
                String kontak = editTextKontak.getText().toString();
                String harga = editTextHarga.getText().toString();

                if (namaGor.isEmpty() || alamat.isEmpty() || jamBuka.isEmpty() ||
                        jamTutup.isEmpty() || tanggal.isEmpty() || kontak.isEmpty() || harga.isEmpty()) {
                    Toast.makeText(getActivity().getApplicationContext(), "Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();

                } else {
                    progressDialog = new ProgressDialog(getContext());
                    progressDialog.setTitle("Uploading..");
                    progressDialog.show();

                    StorageReference storageReference1 = storageReference.child(System.currentTimeMillis() + "." + GetFileExtension(imgUri));
                    storageReference1.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Gor gor = new Gor(namaGor, alamat, jamBuka, jamTutup, tanggal, taskSnapshot.getUploadSessionUri().toString(), kontak, harga);
//                            String imgUploadId = databaseReference.push().getKey();
                            databaseReference.child(key).setValue(gor);
                            Toast.makeText(getActivity().getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
//                            if (imgUploadId != null) {
//                                databaseReference.child("Gor").child(gor.getKey()).setValue(gor);
//                                Toast.makeText(getActivity().getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
//                            }
                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

                            fragmentManager.popBackStack();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity().getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private void setTimePicker() {
        editTextjamBuka.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                Integer hour = calendar.get(Calendar.HOUR_OF_DAY);
                Integer minute = calendar.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), android.R.style.Theme_DeviceDefault_Dialog, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        editTextjamBuka.setText(String.format(Locale.getDefault(), "%d:%d", hour, minute));
                    }
                }, hour, minute, true);
                timePickerDialog.show();
            }
        });

        editTextJamTutup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                Integer hour = calendar.get(Calendar.HOUR_OF_DAY);
                Integer minute = calendar.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), android.R.style.Theme_DeviceDefault_Dialog, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        editTextJamTutup.setText(String.format(Locale.getDefault(), "%d:%d", hour, minute));
                    }
                }, hour, minute, true);
                timePickerDialog.show();
            }
        });
    }

    private void setDatePicker() {
        editTextTanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                Integer year = calendar.get(Calendar.YEAR);
                Integer month = calendar.get(Calendar.MONTH);
                Integer day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), android.R.style.Theme_DeviceDefault_Dialog, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        editTextTanggal.setText(day + " / " + (month + 1) + " / " + year);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });
    }

    private void setImgPicker() {
        editTextGorImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextGorImg.setText("");
                imagePicker();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_CODE && data != null && data.getData() != null) {
            imgUri = data.getData();
            Cursor cursor = getActivity().getApplicationContext().getContentResolver().query(imgUri, null, null, null, null);
            int name = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            cursor.moveToFirst();
            editTextGorImg.setText(cursor.getString(name));
        }
    }

    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getActivity().getApplicationContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)) ;

    }

    private void imagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, GALLERY_CODE);
    }

    private void setOnBackPressed(View view) {
        entryEditToolbar.setNavigationIcon(R.drawable.outline_chevron_left_24);
        entryEditToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
    }
}