package com.X.tcbj.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.csrx.data.PreferencesUtils;
import com.X.tcbj.utils.CompressPictures;
import com.X.tcbj.utils.Constant;
import com.X.tcbj.utils.GetMyData;
import com.X.tcbj.utils.ImageUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

/**
 * 发表商家评论
 * 
 * @author zmz
 * 
 */
@SuppressLint("SdCardPath")
public class ShopsDiscussActivity extends Activity implements OnClickListener {
	ImageView img_back;

	RatingBar ratingBar;// 商家星级评定
	// EditText et_title;// 评论标题
	EditText et_context;// 评论内容
	ImageView img_camera;// 拍照
	ImageView img_picture;// 手机图片
	Button btn_submit;// 提交评论
	ImageView img_shop;// 店家图片
	TextView txt_name;// 商家名称
	TextView txt_address;// 商圈
	TextView txt_class;// 分类

	// 提交评论参数
	int areaID;// 区域id
	String mUserId;// 用户id
	String storeID;// 商家id
	String startNum;// 商家星级
	String title;// 评论标题
	String content;// 评论内容
	String picIDs;// 图片id
	int logn;// 用户登录状态
	CompressPictures pictures = new CompressPictures();
	File temp;
	String path = "0";// 图片路径
	private String photoName;// 照片名称
	String fileName;
	String res;// 图片上传返回结果
	private String actionUrl = Constant.url+"store/storecomment";// 图片上传地址
	Dialog dialog;
	CompressPictures compressor;
	com.nostra13.universalimageloader.core.ImageLoader ImageLoader;
	DisplayImageOptions options;
	com.X.tcbj.utils.ImageUtils ImageUtils;
	ImageLoadingListener animateFirstListener;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		compressor = new CompressPictures();
		ImageUtils = new ImageUtils();
		ImageLoader = ImageLoader.getInstance();
		ImageLoader.init(ImageLoaderConfiguration.createDefault(this));
		animateFirstListener = new ImageUtils.AnimateFirstDisplayListener();
		options = ImageUtils.setnoOptions();
		areaID = PreferencesUtils.getInt(this, "cityID");
		storeID = PreferencesUtils.getString(ShopsDiscussActivity.this,
				"storeID");
		mUserId = PreferencesUtils.getString(ShopsDiscussActivity.this,
				"userid");
		logn = PreferencesUtils.getInt(ShopsDiscussActivity.this, "logn");
		setContentView(R.layout.activity_shops_discuss);
		init();

	}

	/**
	 * 进行数据刷新
	 */
	protected void onResume() {
		super.onResume();

		areaID = PreferencesUtils.getInt(this, "cityID");
		storeID = PreferencesUtils.getString(ShopsDiscussActivity.this,
				"storeID");
		MobclickAgent.onResume(this);
	}

	/**
	 * 图片上传时开启一个新线程
	 */
	public void myInfo() {
		new Thread() {
			public void run() {
				try {
					res = uploadFile(actionUrl, path);
					Message msg = new Message();
					msg.what = 1;
					handler.sendMessage(msg);
				} catch (Exception e) {
					// TODO: handle exception
					// Message msg = new Message();
					// msg.what = 2;
					// handler.sendEmptyMessage(2);
				}

			}
		}.start();
	}

	/**
	 * 图片上传
	 */
	public String uploadFile(String Url, String filePath) {
		StringBuilder sb2 = new StringBuilder();
		String BOUNDARY = "ARCFormBoundaryp859n1863ri19k9";
		String PREFIX = "--", LINEND = "\r\n";
		// String MULTIPART_FROM_DATA = "multipart/form-data";
		String CHARSET = "UTF-8";
		int areaId = PreferencesUtils.getInt(ShopsDiscussActivity.this,
				"cityID");
		URL httpurl;
		try {
			httpurl = new URL(actionUrl);
			HttpURLConnection conn = (HttpURLConnection) httpurl
					.openConnection();

			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("Charset", "utf-8");
			conn.setRequestProperty("Content-Type",
					"multipart/form-data;boundary=" + BOUNDARY);

			DataOutputStream outStream = new DataOutputStream(
					conn.getOutputStream());

			StringBuilder sb = new StringBuilder();

			sb.append(PREFIX);
			sb.append(BOUNDARY);
			sb.append(LINEND);
			sb.append("Content-Disposition: form-data; name=\"" + "areaId"
					+ "\"" + LINEND);
			sb.append(LINEND);
			sb.append(areaId);
			sb.append(LINEND);
			sb.append(PREFIX);
			sb.append(BOUNDARY);
			sb.append(LINEND);

			sb.append("Content-Disposition: form-data; name=\"" + "userId"
					+ "\"" + LINEND);
			sb.append(LINEND);
			sb.append(mUserId);
			sb.append(LINEND);
			sb.append(PREFIX);
			sb.append(BOUNDARY);
			sb.append(LINEND);

			sb.append("Content-Disposition: form-data; name=\"" + "storeId"
					+ "\"" + LINEND);
			sb.append(LINEND);
			sb.append(storeID);
			sb.append(LINEND);
			sb.append(PREFIX);
			sb.append(BOUNDARY);
			sb.append(LINEND);

			sb.append("Content-Disposition: form-data; name=\"" + "title"
					+ "\"" + LINEND);
			sb.append(LINEND);
			sb.append("88888");
			sb.append(LINEND);
			sb.append(PREFIX);
			sb.append(BOUNDARY);
			sb.append(LINEND);

			sb.append("Content-Disposition: form-data; name=\"" + "content"
					+ "\"" + LINEND);
			sb.append(LINEND);
			sb.append(content);
			sb.append(LINEND);
			sb.append(PREFIX);
			sb.append(BOUNDARY);
			sb.append(LINEND);

			sb.append("Content-Disposition: form-data; name=\"" + "startNum"
					+ "\"" + LINEND);
			sb.append(LINEND);
			sb.append(startNum);
			sb.append(LINEND);

			outStream.write(sb.toString().getBytes());
			File files;
	
			if (!filePath.equals("0")) 
			{
				files = new File(filePath);
				StringBuilder sb1 = new StringBuilder();
				sb1.append(PREFIX);
				sb1.append(BOUNDARY);
				sb1.append(LINEND);
				sb1.append("Content-Disposition: form-data; name=\"files\"; filename=\""
						+ "123.jpg" + "\"" + LINEND);
				sb1.append("Content-Type: image/jpg; charset=" + CHARSET
						+ LINEND);
				sb1.append(LINEND);
				outStream.write(sb1.toString().getBytes());
				InputStream is;
			
				is = new FileInputStream(files);
				byte[] buffer = new byte[1024];
				int len = 0;
				while ((len = is.read(buffer)) != -1) 
				{
					outStream.write(buffer, 0, len);
				}

				is.close();
				outStream.write(LINEND.getBytes());

			}
			else
			{
				StringBuilder sb1 = new StringBuilder();
				sb1.append(PREFIX);
				sb1.append(BOUNDARY);
				sb1.append(LINEND);
				sb1.append("Content-Disposition: form-data; name=\"files\"; filename=\""
						+ "123.jpg" + "\"" + LINEND);
				sb1.append("Content-Type: image/jpg; charset=" + CHARSET
						+ LINEND);
				sb1.append(LINEND);
				sb1.append(LINEND);
				outStream.write(sb1.toString().getBytes());
			}

			
			// }
			byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
			outStream.write(end_data);
			outStream.flush();

			int res = conn.getResponseCode();
			System.out.println(res);
			InputStream in = null;
			// System.out.println("***************res结果：" + res);
			if (res == 200 || res == 200) {
				in = conn.getInputStream();
				int ch;

				while ((ch = in.read()) != -1) {
					sb2.append((char) ch);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(ShopsDiscussActivity.this, "图片上传操作失败",
					Toast.LENGTH_SHORT).show();
		}
		return sb2.toString();
	}

	public void init() {
		img_back = (ImageView) findViewById(R.id.imgview_shops_discuss_back);
		img_shop = (ImageView) findViewById(R.id.img_shops_discuss_picture);
		txt_name = (TextView) findViewById(R.id.txt_shops_discuss_name);
		txt_address = (TextView) findViewById(R.id.txt_shops_discuss_address);
		txt_class = (TextView) findViewById(R.id.txt_shops_discuss_class);
		ImageLoader.displayImage(Constant.SHOP_IMG, img_shop, options,
				animateFirstListener);
		txt_name.setText(Constant.SHOP_NAME);// 店家名称
		ratingBar = (RatingBar) findViewById(R.id.ratingbar);
		// et_title = (EditText) findViewById(R.id.et_shops_discuss_title);
		et_context = (EditText) findViewById(R.id.et_shops_discuss_context);
		img_camera = (ImageView) findViewById(R.id.img_shops_discuss_camera);
		img_picture = (ImageView) findViewById(R.id.img_shops_discuss_photo);
		btn_submit = (Button) findViewById(R.id.btn_shops_discuss_submit);
		ratingBar = (RatingBar) findViewById(R.id.ratingbar);
		img_back.setOnClickListener(this);
		img_camera.setOnClickListener(this);
		img_picture.setOnClickListener(this);
		btn_submit.setOnClickListener(this);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imgview_shops_discuss_back:
			ShopsDiscussActivity.this.finish();
			break;
		case R.id.img_shops_discuss_camera:
			Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			String state = Environment.getExternalStorageState();
			if (state.equals(Environment.MEDIA_MOUNTED)) {// 如果存储卡可用
				startActivityForResult(intentCamera, 4);
			} else {
				long time = Calendar.getInstance().getTimeInMillis();
				photoName = time + ".jpg";
				intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, Uri
						.fromFile(new File(Environment
								.getExternalStorageDirectory()
								.getAbsolutePath()
								+ "/DCIM/" + photoName)));
				startActivityForResult(intentCamera, 4);
			}
			

			break;
		case R.id.img_shops_discuss_photo:
			Intent intent = new Intent(Intent.ACTION_PICK, null);

			/**
			 * 下面这句话，与其它方式写是一样的效果，如果： intent.setData(MediaStore.Images
			 * .Media.EXTERNAL_CONTENT_URI);
			 * intent.setType(""image/*");设置数据类型 如果朋友们要限制上传到服务器的图片类型时可以直接写如
			 * ："image/jpeg 、 image/png等的类型"
			 */
			intent.setDataAndType(
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");

			startActivityForResult(intent, 1);
			break;
		case R.id.btn_shops_discuss_submit:

			int starNumber = ((int) ratingBar.getRating()) * 2;
			// System.out.println("实际评星：" + ratingBar.getRating());
			// 确定商家星级
			startNum = String.valueOf(starNumber);
			content = et_context.getText().toString().trim();
			if (content.equals("")) {
				Toast.makeText(ShopsDiscussActivity.this, "输入信息不能都为空",
						Toast.LENGTH_SHORT).show();
			}else if (content.length()>500){
				Toast.makeText(ShopsDiscussActivity.this, "最多输入500个字符",
						Toast.LENGTH_SHORT).show();
			}else  if(ratingBar.getRating()==0){
				Toast.makeText(ShopsDiscussActivity.this, "请评分，最小为1星",
						Toast.LENGTH_SHORT).show();
			}
			else {
				dialog = GetMyData.createLoadingDialog(
						ShopsDiscussActivity.this, "正在提交评论······");
				dialog.show();
				myInfo();// 图片上传
			}
			break;
		default:
			break;
		}
	}

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			// msg.what=1时，上传图片
			switch (msg.what) {
			case 1:
				try {
					dialog.dismiss();
					JSONObject jo1 = new JSONObject(res);
					picIDs = jo1.getString("status");
					if (picIDs.equals("0")) {
						Toast.makeText(ShopsDiscussActivity.this, "评论成功！",
								Toast.LENGTH_SHORT).show();
						finish();
					} else {
						Toast.makeText(ShopsDiscussActivity.this, "评论失败！",
								Toast.LENGTH_SHORT).show();
					}

				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				break;
			case 2:
				dialog.dismiss();
				Toast.makeText(ShopsDiscussActivity.this, "操作出错！",
						Toast.LENGTH_SHORT).show();
				break;
			}
		};
	};

	Bitmap photo;

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		try {

			switch (requestCode) {
			// 如果是直接从相册获取
			case 1:
				String[] proj = { MediaStore.Images.Media.DATA };
				// 好像是android多媒体数据库的封装接口，具体的看Android文档
				Uri originalUri = data.getData();
				Cursor cursor = managedQuery(originalUri, proj, null, null,
						null);
				// 按我个人理解 这个是获得用户选择的图片的索引值
				int column_index = cursor
						.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
				// 将光标移至开头 ，这个很重要，不小心很容易引起越界
				cursor.moveToFirst();
				// 最后根据索引值获取图片路径
				path = cursor.getString(column_index);
				System.out.println("path:"+path);
				Bitmap bit = BitmapFactory.decodeFile(path);
				float pc = (float) 100 / (float) bit.getWidth();
				Bitmap bitmap = compressor.resize_img(bit, pc);
				BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
//				head.setBackgroundDrawable(bitmapDrawable);
				String filename = path.substring(path.lastIndexOf("/") + 1);
				File file = saveMyBitmap(filename, bitmap);
				path = file.toString();
				setEditImg(bitmapDrawable);// 往editText中添加图片
				
				break;
			// 如果是调用相机拍照时
			case 2:
				break;
			// 取得裁剪后的图片
			case 3:
				/**
				 * 非空判断大家一定要验证，如果不验证的话， 在剪裁之后如果发现不满意，要重新裁剪，丢弃
				 * 当前功能时，会报NullException，只 在这个地方加下，大家可以根据不同情况在合适的 地方做判断处理类似情况
				 * &nbsp;
				 */

				if (data != null) {
					setPicToView(data);
				}
				break;
			case 4:
				Uri uri = data.getData();
				if (uri != null) {
					photo = BitmapFactory.decodeFile(uri.getPath());
				}

				if (photo == null) {
					Bundle bundle = data.getExtras();
					if (bundle != null) {
						photo = (Bitmap) bundle.get("data");
						savePicInLocal(photo);// 保存到本地
						// 图片显示到指定地方
						setPicToView(data);

					} else {
						Toast.makeText(ShopsDiscussActivity.this, "未拍摄照片",
								Toast.LENGTH_LONG).show();
					}
				}
				break;
			default:
				break;

			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			Toast.makeText(ShopsDiscussActivity.this, "放弃选择 ",
					Toast.LENGTH_SHORT).show();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	// 保存拍摄的照片到手机的sd卡
	private void savePicInLocal(Bitmap bitmap) {
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
		ByteArrayOutputStream baos = null; // 字节数组输出流
		try {
			baos = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
			byte[] byteArray = baos.toByteArray();// 字节数组输出流转换成字节数组
			String saveDir = Environment.getExternalStorageDirectory()
					+ "/liangPic";
			File dir = new File(saveDir);
			if (!dir.exists()) {
				dir.mkdir(); // 创建文件夹
			}
			fileName = saveDir + "/" + System.currentTimeMillis() + ".JPEG";
			File file = new File(fileName);
			file.delete();
			if (!file.exists()) {
				file.createNewFile();// 创建文件
				path = fileName;
			}
			// 将字节数组写入到刚创建的图片文件中
			fos = new FileOutputStream(file);
			bos = new BufferedOutputStream(fos);
			bos.write(byteArray);

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (baos != null) {
				try {
					baos.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (bos != null) {
				try {
					bos.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 保存裁剪之后的图片数据 &nbsp;
	 * 
	 * @param picdata
	 */
	private void setPicToView(Intent picdata) {
		Bundle extras = picdata.getExtras();
		if (extras != null) {
			Bitmap photo = extras.getParcelable("data");
			// CompressPictures pictures = new CompressPictures();
			// pictures.resize_img(photo, 4);
			Drawable drawable = new BitmapDrawable(
					pictures.resize_img(photo, 4));
			// Drawable drawable = new BitmapDrawable(photo);// 得到要显示的图片资源
			setEditImg(drawable);
		}
	}

	/**
	 * 往EditText中添加图片
	 * 
	 * @param drawable
	 *            要添加的图片
	 */
	public void setEditImg(Drawable drawable) {
		SpannableString ss = new SpannableString(et_context.getText() + " ");

		// 设置图片宽高
		drawable.setBounds(0, 0, 50, 50);
		// 跨度底部应与周围文本的基线对齐
		ImageSpan span = new ImageSpan(drawable, ImageSpan.ALIGN_BOTTOM);
		// 添加图片
		ss.setSpan(span, ss.length() - 1, ss.length(),
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		et_context.setText(ss);
		Editable editable = et_context.getEditableText();
		Selection.setSelection(editable, editable.length());
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
	@SuppressLint("SdCardPath")
	public File saveMyBitmap(String filename, Bitmap bit) {
		File dir = new File("/data/data/com.hkkj.csrx.activity/cache/head/");
		if (!dir.exists()) {
			dir.mkdir();
		}
		File f = new File("/data/data/com.hkkj.csrx.activity/cache/head/" + filename);
		try {
			f.createNewFile();
			FileOutputStream fOut = null;
			fOut = new FileOutputStream(f);
			bit.compress(Bitmap.CompressFormat.PNG, 100, fOut);
			fOut.flush();
			fOut.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			f = null;
			e1.printStackTrace();
		}

		return f;
	}
}
