package com.socialcodia.famblah.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.loader.content.CursorLoader;

import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.socialcodia.famblah.R;
import com.socialcodia.famblah.api.ApiClient;
import com.socialcodia.famblah.model.ModelUser;
import com.socialcodia.famblah.model.ResponseDefault;
import com.socialcodia.famblah.storage.SharedPrefHandler;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class AddFeedFragment extends Fragment {

    private EditText inputContent;
    private ImageView selectFeedImage,ivFeedImage,inputFeedImage,userProfileImage;
    private TextView tvFeedContent,tvUserName,tvFeedTimestamp;
    private Button btnPostFeed;
    private CardView cardView;
    private Bitmap bitmap;
    String token;
    Uri filePath;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_feed, container, false);

        inputContent = view.findViewById(R.id.inputFeedContent);
        selectFeedImage = view.findViewById(R.id.selectFeedImage);
        tvFeedContent = view.findViewById(R.id.tvFeedContent);
        ivFeedImage = view.findViewById(R.id.ivFeedImage);
        inputFeedImage = view.findViewById(R.id.inputFeedImage);
        btnPostFeed = view.findViewById(R.id.btnPostFeed);
        tvUserName = view.findViewById(R.id.tvUserName);
        tvFeedTimestamp = view.findViewById(R.id.tvFeedTimestamp);
        tvFeedContent = view.findViewById(R.id.tvFeedContent);
        userProfileImage = view.findViewById(R.id.userProfileImage);
        cardView = view.findViewById(R.id.cardView);

        ModelUser modelUser = SharedPrefHandler.getInstance(getContext()).getUser();
        token = modelUser.getToken();
        tvUserName.setText(modelUser.getName());
        Picasso.get().load(modelUser.getImage()).into(userProfileImage);



        selectFeedImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        inputContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()!=0)
                {
                    cardView.setVisibility(View.VISIBLE);
                    tvFeedContent.setVisibility(View.VISIBLE);
                    tvFeedContent.setText(s);
                }
                else
                {
                    tvFeedContent.setText(null);
                    if (filePath==null)
                    {
                        cardView.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        cardView.setVisibility(View.GONE);
        tvFeedContent.setVisibility(View.GONE);
        ivFeedImage.setVisibility(View.GONE);
        tvFeedTimestamp.setText(getTime(System.currentTimeMillis()));

        btnPostFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });

        return view;
    }

    private void validateData()
    {
        String content = inputContent.getText().toString().trim();
        if (content.length()<=0 && filePath==null)
        {
            Toast.makeText(getContext(), "Please write something, or select an image", Toast.LENGTH_SHORT).show();
        }
        else
        {
            postFeed(content);
        }
    }

    private void postFeed(String content)
    {
        MultipartBody.Part body = null;
        if (filePath!=null)
        {
            File file = new File(getRealPathFromURI(filePath));
            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), file);

            // MultipartBody.Part is used to send also the actual file name
            body =
                    MultipartBody.Part.createFormData("image", file.getName(), requestFile);

        }
        btnPostFeed.setEnabled(false);
        String image = "";
        Call<ResponseDefault> call = ApiClient.getInstance().getApi().postFeed(token,content,body);
        call.enqueue(new Callback<ResponseDefault>() {
            @Override
            public void onResponse(Call<ResponseDefault> call, Response<ResponseDefault> response) {
                if (response.isSuccessful())
                {
                    Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    inputContent.setText("");
                    inputFeedImage.setImageBitmap(null);
                    btnPostFeed.setEnabled(true);
                }
                else
                {
                    Toast.makeText(getContext(), "Server Not Responding", Toast.LENGTH_SHORT).show();
                    btnPostFeed.setEnabled(true);
                }
            }

            @Override
            public void onFailure(Call<ResponseDefault> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                btnPostFeed.setEnabled(true);
            }
        });
    }

    private void chooseImage()
    {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        intent.setAction(Intent.ACTION_PICK);
//        intent.setType("image/*");
        startActivityForResult(intent,200);
    }

    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(getContext(), contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }


    private String getTime(Long timestamp)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:a");
        String time = sdf.format(new Date(timestamp));
        return String.valueOf(time);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==200 && resultCode== RESULT_OK && data!=null)
        {
            filePath = data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),filePath);
                selectFeedImage.setImageBitmap(bitmap);
                ivFeedImage.setVisibility(View.VISIBLE);
                cardView.setVisibility(View.VISIBLE);
                ivFeedImage.setVisibility(View.VISIBLE);
                ivFeedImage.setImageBitmap(bitmap);
            }
            catch (Exception e)
            {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

}