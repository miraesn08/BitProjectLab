package kr.co.bit.osf.projectlab;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

// http://berabue.blogspot.kr/2014/05/android-listview.html
// http://ankyu.entersoft.kr/Lecture/android/listview_02.asp
// http://itmir.tistory.com/477
public class AlbumItemAdapter extends BaseAdapter  implements View.OnClickListener {
    private static final String TAG = "AlbumItemAdapterLog";

    private Activity activity = null;
    private Context context = null;
    private List<AlbumItemDTO> list = null;
    private AlbumItemDTO dto;

    // ListView 내부 View들을 가르킬 변수들
    private ImageView itemImageView;
    private TextView itemTextView;
    private Button itemButton;

    public AlbumItemAdapter(Context context, Activity activity) {
        super();
        this.activity = activity;
        this.context = context;
        this.list = new ArrayList<>();
    }

    // 현재 아이템의 수를 리턴
    @Override
    public int getCount() {
        return this.list.size();
    }

    // 현재 아이템의 오브젝트를 리턴, Object를 상황에 맞게 변경하거나 리턴받은 오브젝트를 캐스팅해서 사용
    @Override
    public Object getItem(int position) {
        if (position < list.size() ) {
            return this.list.get(position);
        } else {
            return null;
        }
    }

    // 아이템 position의 ID 값 리턴
    @Override
    public long getItemId(int position) {
        return this.list.get(position).getId();
    }

    // 출력 될 아이템 관리
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Log.i(TAG, "convertView == null");

        // http://www.vogella.com/tutorials/AndroidListView/article.html
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView;
        ImageView itemImageView;
        TextView itemTextView;
        Button previewButton;
        Button deleteButton;

        if (position % 2 == 0) {
            rowView = inflater.inflate(R.layout.activity_image_util_album_item_even, parent, false);
            itemImageView = (ImageView) rowView.findViewById(R.id.AlbumEvenItemImageView);
            itemTextView = (TextView) rowView.findViewById(R.id.AlbumEvenItemText);
            previewButton = (Button) rowView.findViewById(R.id.AlbumEvenItemPreviewButton);
            deleteButton = (Button) rowView.findViewById(R.id.AlbumEvenItemDeleteButton);
        } else {
            rowView = inflater.inflate(R.layout.activity_image_util_album_item, parent, false);
            itemImageView = (ImageView) rowView.findViewById(R.id.AlbumItemImageView);
            itemTextView = (TextView) rowView.findViewById(R.id.AlbumItemText);
            previewButton = (Button) rowView.findViewById(R.id.AlbumItemPreviewButton);
            deleteButton = (Button) rowView.findViewById(R.id.AlbumItemDeleteButton);
        }

        // 받아온 position 값을 이용하여 배열에서 아이템을 가져온다.
        dto = (AlbumItemDTO)getItem(position);
        dto.setPosition(position);

        // Tag를 이용하여 데이터와 뷰를 묶습니다.
        rowView.setTag(dto);
        //Log.i(TAG, "setTag : " + dto.getId() + " : " + position);

        // 데이터의 실존 여부를 판별합니다.
        if (dto != null) {
            // 데이터가 있다면 갖고 있는 정보를 뷰에 알맞게 배치시킵니다.
            itemTextView.setText(dto.getText());
            previewButton.setOnClickListener(this);
            deleteButton.setOnClickListener(this);

            // thumbnail
            String tagLog = "thumbnail : ";
            String photoPath = dto.getImageUrl();
            // http://developer.android.com/intl/ko/training/camera/photobasics.html
		    /* Get the size of the ImageView */
            int targetW = itemImageView.getWidth();
            int targetH = itemImageView.getHeight();
            Log.i(TAG, tagLog + "Get the size of the ImageView");
            Log.i(TAG, tagLog + "targetWidth : " + targetW);
            Log.i(TAG, tagLog + "targetHeight : " + targetH);

		    /* Get the size of the image */
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(photoPath, bmOptions);
            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;
            Log.i(TAG, tagLog + "Get the size of the image");
            Log.i(TAG, tagLog + photoW);
            Log.i(TAG, tagLog + photoH);

		    /* Figure out which way needs to be reduced less */
            int scaleFactor = 1;
            if ((targetW > 0) || (targetH > 0)) {
                scaleFactor = Math.min(photoW/targetW, photoH/targetH);
            }
            Log.i(TAG, tagLog + "scaleFactor : " + scaleFactor);

		    /* Set bitmap options to scale the image decode target */
            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaleFactor;
            bmOptions.inPurgeable = true;
            Log.i(TAG, tagLog + "Set bitmap options to scale the image decode target");

		    /* Decode the JPEG file into a Bitmap */
            Bitmap bitmap = BitmapFactory.decodeFile(photoPath, bmOptions);
            Log.i(TAG, tagLog + "Decode the JPEG file into a Bitmap");

		    /* Associate the Bitmap to the ImageView */
            itemImageView.setImageBitmap(bitmap);
            Log.i(TAG, tagLog + "Associate the Bitmap to the ImageView");
        }

        // 완성된 아이템 뷰를 반환합니다.
        return rowView;
    }

    // 외부에서 아이템 추가 요청 시 사용
    public void add(AlbumItemDTO dto) {
        this.list.add(dto);
        //Log.i(TAG, dto.toString());
        dataChange();
    }

    // 외부에서 아이템 삭제 요청 시 사용
    public void remove(int position) {
        this.list.remove(position);
        dataChange();
    }

    public void clear() {
        this.list.clear();
        //Log.i(TAG, "clear : " + list.size());
        dataChange();
    }

    public void dataChange(){
        this.notifyDataSetChanged();
    }
    @Override
    public void onClick(View v) {
        // Tag를 이용하여 Data를 가져옵니다.
        final View view = (View)(v.getParent());
        final AlbumItemDTO clickedItem = (AlbumItemDTO) view.getTag();

        switch (v.getId()) {
            case R.id.AlbumItemPreviewButton:
            case R.id.AlbumEvenItemPreviewButton:
                //Toast.makeText(context, clickedItem.getImageUrl(), Toast.LENGTH_SHORT).show();
                //
                Intent intent = new Intent();
                intent.setAction(android.content.Intent.ACTION_VIEW);
                File file = new File(clickedItem.getImageUrl());
                intent.setDataAndType(Uri.fromFile(file), "image/*");
                this.activity.startActivity(intent);
                //
                break;
            case R.id.AlbumItemDeleteButton:
            case R.id.AlbumEvenItemDeleteButton:
                //Log.i(TAG, "delete : " + clickedItem.getPosition());
                final int itemPosition = clickedItem.getPosition();
                // http://www.vogella.com/tutorials/AndroidListView/article.html
                view.animate().setDuration(2000).alpha(0)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                list.remove(itemPosition);
                                notifyDataSetChanged();
                                view.setAlpha(1);
                            }
                        });
                break;
        }
    }
}
