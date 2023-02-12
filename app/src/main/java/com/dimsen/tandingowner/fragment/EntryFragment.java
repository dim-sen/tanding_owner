package com.dimsen.tandingowner.fragment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

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
 * Use the {@link EntryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EntryFragment extends Fragment {

    Toolbar entryToolbar;
    EditText editTextTime;
    EditText editTextTimeClosed;
    EditText editTextDate;
    EditText editTextImg;
    EditText editTextNama;
    EditText editTextAlamat;
    EditText editTextTelepon;
    EditText editTextHarga;
    private final Integer GALLERY_CODE = 100;

    Button submitButton;
    Uri imgUri;
    DatabaseReference databaseReference;
    ProgressDialog progressDialog;
    StorageReference storageReference;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EntryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EntryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EntryFragment newInstance(String param1, String param2) {
        EntryFragment fragment = new EntryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_entry, container, false);

        databaseReference = FirebaseDatabase.getInstance().getReference("Gor");

        storageReference = FirebaseStorage.getInstance().getReference("Gor");

        entryToolbar = view.findViewById(R.id.toolbar_entry);
        ((AppCompatActivity)getActivity()).setSupportActionBar(entryToolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Tambah Gor");

        setOnBackPressed(view);

        editTextTime = view.findViewById(R.id.input_time);
        editTextTimeClosed = view.findViewById(R.id.input_time_closed);
        editTextDate = view.findViewById(R.id.input_date);
        editTextImg = view.findViewById(R.id.input_img);

        editTextNama = view.findViewById(R.id.txt_input_nama_gor);
        editTextAlamat = view.findViewById(R.id.txt_input_alamat);
        editTextTelepon = view.findViewById(R.id.txt_input_kontak);
        editTextHarga = view.findViewById(R.id.txt_input_harga);

        setTimePicker();
        setDatePicker();
        setImgPicker();

        submitButton = view.findViewById(R.id.btn_submit);

        setBtnClick();

        return view;
    }

    private void setBtnClick() {
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String namaGor = editTextNama.getText().toString();
                String alamat = editTextAlamat.getText().toString();
                String jamBuka = editTextTime.getText().toString();
                String jamTutup = editTextTimeClosed.getText().toString();
                String tanggal = editTextDate.getText().toString();
                String kontak = editTextTelepon.getText().toString();
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
                            String imgUploadId = databaseReference.push().getKey();
                            if (imgUploadId != null) {
                                databaseReference.child(imgUploadId).setValue(gor);
                                Toast.makeText(getActivity().getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                            }
                            editTextNama.getText().clear();
                            editTextAlamat.getText().clear();
                            editTextTime.getText().clear();
                            editTextTimeClosed.getText().clear();
                            editTextDate.getText().clear();
                            editTextImg.getText().clear();
                            editTextTelepon.getText().clear();
                            editTextHarga.getText().clear();
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

    private void setImgPicker() {
        editTextImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextImg.setText("");
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
            editTextImg.setText(cursor.getString(name));
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

    private void setDatePicker() {
        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                Integer year = calendar.get(Calendar.YEAR);
                Integer month = calendar.get(Calendar.MONTH);
                Integer day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), android.R.style.Theme_DeviceDefault_Dialog, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        editTextDate.setText(day + " / " + (month + 1) + " / " + year);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });
    }

    private void setTimePicker() {
        editTextTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                Integer hour = calendar.get(Calendar.HOUR_OF_DAY);
                Integer minute = calendar.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), android.R.style.Theme_DeviceDefault_Dialog, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        editTextTime.setText(String.format(Locale.getDefault(), "%d:%d", hour, minute));
                    }
                }, hour, minute, true);
                timePickerDialog.show();
            }
        });

        editTextTimeClosed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                Integer hour = calendar.get(Calendar.HOUR_OF_DAY);
                Integer minute = calendar.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), android.R.style.Theme_DeviceDefault_Dialog, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        editTextTimeClosed.setText(String.format(Locale.getDefault(), "%d:%d", hour, minute));
                    }
                }, hour, minute, true);
                timePickerDialog.show();
            }
        });
    }

    private void setOnBackPressed(View view) {
        entryToolbar.setNavigationIcon(R.drawable.outline_chevron_left_24);
        entryToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
    }
}