package kr.co.bit.osf.projectlab;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.File;
import java.util.List;

public class ImageUtilActivity extends AppCompatActivity {
    private static final String TAG = "ImageUtilActivityLog";

    private ListView imageListView;
    private AlbumItemAdapter imageListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_util);

        // http://berabue.blogspot.kr/2014/05/android-listview.html
        // Android에서 제공하는 string 문자열 하나를 출력 가능한 layout으로 어댑터 생성
        //imageListAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1);
        imageListAdapter = new AlbumItemAdapter(getApplicationContext(), this);

        // Xml에서 추가한 ListView 연결
        imageListView = (ListView) findViewById(R.id.imageListView);

        // ListView에 어댑터 연결
        imageListView.setAdapter(imageListAdapter);

        // ListView 아이템 터치 시 이벤트 추가
        //imageListView.setOnItemClickListener(onClickImageListItem);

        //
        imageListView.setVisibility(View.INVISIBLE);

        (findViewById(R.id.btnGetOutputMediaFileUri)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // sd card check
                Log.i(TAG,"sd card is ? " + ImageUtil.isSDPresent());
                //
                Log.i(TAG, "btnGetOutputMediaFileUri clicked");
                File outputName = ImageUtil.getOutputMediaFile(ImageUtil.MEDIA_TYPE_IMAGE);
                if (outputName != null) {
                    Log.i(TAG, outputName.getName());
                    Log.i(TAG, outputName.getAbsolutePath());
                } else {
                    Log.i(TAG, "no name!");
                }
            }
        });

        (findViewById(R.id.btnGetImageList)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageListAdapter.clear();

                // http://stackoverflow.com/questions/4195660/get-list-of-photo-galleries-on-android
                // which image properties are we querying
                String[] projection = new String[]{
                        MediaStore.Images.Media._ID,
                        MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                        MediaStore.Images.Media.DATE_TAKEN,
                        MediaStore.Images.Media.DATA
                };

                // content:// style URI for the "primary" external storage volume
                Uri images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                Log.i(TAG, "images:" + images.getEncodedPath() );

                // Make the query.
                Cursor cur = getContentResolver().query(images,
                        projection, // Which columns to return
                        null,       // Which rows to return (all rows)
                        null,       // Selection arguments (none)
                        null        // Ordering
                );
                if (cur == null) {
                    Log.i(TAG, "cursor is null");
                    return;
                }

                Log.i(TAG, "query count=" + cur.getCount());

                if (cur.moveToFirst()) {
                    String bucket;
                    String date;
                    String data;

                    int bucketColumn = cur.getColumnIndex(
                            MediaStore.Images.Media.BUCKET_DISPLAY_NAME);

                    int dateColumn = cur.getColumnIndex(
                            MediaStore.Images.Media.DATE_TAKEN);

                    int dataColumn = cur.getColumnIndex(
                            MediaStore.Images.Media.DATA);

                    do {
                        // Get the field values
                        bucket = cur.getString(bucketColumn);
                        date = cur.getString(dateColumn);
                        data = cur.getString(dataColumn);

                        // Do something with the values.
                        Log.i(TAG, " bucket=" + bucket
                                + "  date_taken=" + date
                                + "  data=" + data);

                        // ListView에 아이템 추가
                        File imageFile = new File(data);
                        AlbumItemDTO dto = new AlbumItemDTO(0,0,imageFile.getName(),imageFile.getAbsolutePath(),0);
                        imageListAdapter.add(dto);
                    } while (cur.moveToNext());
                }
                imageListView.setVisibility(View.VISIBLE);
            }
        });

        (findViewById(R.id.btnGetAlbumList)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageListAdapter.clear();
                List<File> albumList = ImageUtil.getImageListFromDefaultAlbum(getApplicationContext());
                if (albumList.size() > 0) {
                    for(File item : albumList) {
                        Log.i(TAG, "album image : " + item.getName());
                    }
                    //
                    for(File item : albumList) {
                        AlbumItemDTO dto = new AlbumItemDTO(0,0,item.getName(),item.getAbsolutePath(),0);
                        imageListAdapter.add(dto);
                    }
                } else {
                    Log.i(TAG, "there is no image in album.");
                }
                imageListView.setVisibility(View.VISIBLE);
            }
        });

    }

    // 아이템 터치 이벤트
    private AdapterView.OnItemClickListener onClickImageListItem = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            // 이벤트 발생 시 해당 아이템 위치의 텍스트를 출력
            //Toast.makeText(getApplicationContext(), imageListAdapter.getItem(arg2), Toast.LENGTH_SHORT).show();

            // http://stackoverflow.com/questions/1740654/view-image-in-action-view-intent
            Intent intent = new Intent();
            intent.setAction(android.content.Intent.ACTION_VIEW);
            AlbumItemDTO dto = (AlbumItemDTO)imageListAdapter.getItem(arg2);
            File file = new File(dto.getImageUrl());
            intent.setDataAndType(Uri.fromFile(file), "image/*");
            startActivity(intent);
        }
    };
}
