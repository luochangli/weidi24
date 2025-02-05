package com.weidi.fragment;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.weidi.R;
import com.weidi.activity.CropImageActivity;
import com.weidi.activity.PicSrcPickerActivity;
import com.weidi.bean.User;
import com.weidi.common.base.BaseFragment;
import com.weidi.util.Const;
import com.weidi.util.ImageCache;
import com.weidi.util.ToastUtil;
import com.weidi.util.Util;
import com.weidi.util.XmppLoadThread;
import com.weidi.util.XmppUtil;
import com.weidi.view.CircleImageView;
import com.weidi.view.wheel.OnWheelChangedListener;
import com.weidi.view.wheel.WheelView;
import com.weidi.view.wheel.adapters.ArrayWheelAdapter;
import com.weidi.view.wheel.model.CityModel;
import com.weidi.view.wheel.model.DistrictModel;
import com.weidi.view.wheel.model.ProvinceModel;
import com.weidi.view.wheel.service.XmlParserHandler;

public class SettingFragment extends BaseFragment implements OnClickListener,
		OnWheelChangedListener {
	private ImageView ivEditSex, ivEditArea, ivEditSign;
	private ImageView ivSex;
	private CircleImageView civHeadImg;
	private TextView tvNickName, tvAdr, tvSign, tvWeidi, tvSex;
	private WheelView mViewProvince, mViewCity, mViewDistrict;// 设置地址的
	private LinearLayout changeAdrLayout, changeLayout;
	// 信息修改
	private TextView changeView, changeNameView;
	private String field;
	private EditText changeText;
	private RadioGroup sexGroup;
	private RadioButton manRadio, womanRadio;
	private Button subBtn, sureBtn, cancelBtn, btnCancle;
	private ScrollView scrollView;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.sureBtn:
			showSelectedResult();
			break;

		case R.id.subBtn:
			// 提交修改
			String cText = changeText.getEditableText().toString();
			if (field.equals("mobile")
					&& !Util.getInstance().isMobileNumber(cText)) {
				ToastUtil.showShortToast(mApp, "不是手机号码");
			} else if (field.equals("email")
					&& !Util.getInstance().isEmail(cText)) {

				ToastUtil.showShortToast(mApp, "不是邮箱");
			} else {

				if (changeView.getId() == R.id.tvSex) {
					if (changeText.getEditableText().toString().equals("男")) {
						ivSex.setImageResource(R.drawable.male);
					} else if(changeText.getEditableText().toString().equals("女")){
						ivSex.setImageResource(R.drawable.female);
					}
				} else {
					changeView.setText(changeText.getEditableText().toString());
				}
				if (changeView.getId() == R.id.tvNickName) {
					tvNickName.setText(changeText.getEditableText().toString());
				}
				Const.loginUser.getvCard().setField(field,
						changeText.getEditableText().toString());
				XmppUtil.getInstance().changeVcard(Const.loginUser.getvCard());
				changeLayout.setVisibility(View.GONE);
				changeText.setText("");

			}
			break;

		case R.id.cancelBtn:
			changeLayout.setVisibility(View.GONE);
			changeText.setText("");
			break;

		case R.id.civHeadImg:
			Intent intent = new Intent();
			CropImageActivity.isAutoSend = false;
			intent.setClass(getActivity(), PicSrcPickerActivity.class);
			getActivity().startActivityForResult(intent,
					PicSrcPickerActivity.CROP);
			break;

		case R.id.tvNickName:
			showChangelayout("修改昵称", "nickName", tvNickName);
			break;

		case R.id.ivSex:
			showChangelayout("修改性别", "sex", tvSex);
			break;
		case R.id.ivEditArea:
			// showChangelayout("修改地区", "adr", adrView);
			changeAdrLayout.setVisibility(View.VISIBLE);

			break;
		case R.id.ivEditSign:
			showChangelayout("修改签名", "intro", tvSign);
			break;

		default:
			break;
		}
	}

	@Override
	protected void handleMsg(Message msg) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initView(Bundle savedInstanceState) {
		setRootView(R.layout.frag_mine);
		initView();
		Const.loginUser = new User(XmppUtil.getUserInfo(null));// 加载用户数据
		setUpData();
		initData();
	}

	private void initData() {
	
			

		if (Const.loginUser != null) {
			tvWeidi.setText(Const.loginUser.getUsername());
			if (Const.loginUser.getNickname() != null) {
				tvNickName.setText(Const.loginUser.getNickname());
				tvNickName.setText(Const.loginUser.getNickname());
			}

			if (Const.loginUser.getSex() != null) {
				if (Const.loginUser.getSex().equals("男")) {
					ivSex.setImageResource(R.drawable.male);
				} else {
					ivSex.setImageResource(R.drawable.female);
				}

			}
			if (Const.loginUser.getIntro() != null) {
				tvSign.setText(Const.loginUser.getIntro());
			}
			if (Const.loginUser.getBitmap() != null) {
				civHeadImg.setImageBitmap(Const.loginUser.getBitmap());
			}
			if (Const.loginUser.getAdr() != null) {
				tvAdr.setText(Const.loginUser.getAdr());
			}
		}

	}

	private void initView() {
		ivEditArea = (ImageView) mRootView.findViewById(R.id.ivEditArea);
		
		ivEditSex = (ImageView) mRootView.findViewById(R.id.ivSex);
		ivEditSign = (ImageView) mRootView.findViewById(R.id.ivEditSign);

		tvAdr = (TextView) mRootView.findViewById(R.id.tvAddress);
		tvNickName = (TextView) mRootView.findViewById(R.id.tvNickName);
		tvSign = (TextView) mRootView.findViewById(R.id.tvSign);
		tvWeidi = (TextView) mRootView.findViewById(R.id.tvWeidi);
		tvSex = (TextView) mRootView.findViewById(R.id.tvSex);

		mViewProvince = (WheelView) mRootView.findViewById(R.id.id_province);
		mViewCity = (WheelView) mRootView.findViewById(R.id.id_city);
		mViewDistrict = (WheelView) mRootView.findViewById(R.id.id_district);
		changeAdrLayout = (LinearLayout) mRootView
				.findViewById(R.id.changeAdrLayout);

		ivSex = (ImageView) mRootView.findViewById(R.id.ivSex);
		civHeadImg = (CircleImageView) mRootView.findViewById(R.id.civHeadImg);
		// 信息修改
		changeText = (EditText) mRootView.findViewById(R.id.changeText);
		sexGroup = (RadioGroup) mRootView.findViewById(R.id.sexGroup);
		manRadio = (RadioButton) mRootView.findViewById(R.id.manRadio);
		womanRadio = (RadioButton) mRootView.findViewById(R.id.womanRadio);
		changeLayout = (LinearLayout) mRootView.findViewById(R.id.changeLayout);
		changeNameView = (TextView) mRootView.findViewById(R.id.changeNameView);
		// 信息修改的提交按钮
		subBtn = (Button) mRootView.findViewById(R.id.subBtn);
		sureBtn = (Button) mRootView.findViewById(R.id.sureBtn);
		cancelBtn = (Button) mRootView.findViewById(R.id.cancelBtn);
		btnCancle = (Button) mRootView.findViewById(R.id.btnCancle);

		scrollView = (ScrollView) mRootView.findViewById(R.id.scrollView);
	}

	@Override
	protected void setListener() {
		mViewProvince.addChangingListener(this);
		mViewCity.addChangingListener(this);
		mViewDistrict.addChangingListener(this);

		ivEditArea.setOnClickListener(this);
		tvNickName.setOnClickListener(this);
		ivEditSex.setOnClickListener(this);
		ivEditSign.setOnClickListener(this);

		subBtn.setOnClickListener(this);
		sureBtn.setOnClickListener(this);
		cancelBtn.setOnClickListener(this);
		btnCancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				changeAdrLayout.setVisibility(View.GONE);
			}
		});

		civHeadImg.setOnClickListener(this);
		sexGroup.setOnCheckedChangeListener(new android.widget.RadioGroup.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == manRadio.getId()) {
					changeText.setText("男");
				} else {
					changeText.setText("女");
				}
			}
		});

	}

	@Override
	protected void afterViews(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue) {
		if (wheel == mViewProvince) {
			updateCities();
		} else if (wheel == mViewCity) {
			updateAreas();
		} else if (wheel == mViewDistrict) {
			updateDistrict(newValue);
		}
	}

	private void updateDistrict(int newValue) {
		mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[newValue];
		mCurrentZipCode = mZipcodeDatasMap.get(mCurrentDistrictName);
	}

	/**
	 * 根据当前的市，更新区WheelView的信息
	 */
	private void updateAreas() {
		int pCurrent = mViewCity.getCurrentItem();
		mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];
		String[] areas = mDistrictDatasMap.get(mCurrentCityName);

		if (areas == null) {
			areas = new String[] { "" };
		}
		mViewDistrict.setViewAdapter(new ArrayWheelAdapter<String>(
				getActivity(), areas));
		mViewDistrict.setCurrentItem(0);
		updateDistrict(0);
	}

	/**
	 * 根据当前的省，更新市WheelView的信息
	 */
	private void updateCities() {
		int pCurrent = mViewProvince.getCurrentItem();
		mCurrentProviceName = mProvinceDatas[pCurrent];
		String[] cities = mCitisDatasMap.get(mCurrentProviceName);
		if (cities == null) {
			cities = new String[] { "" };
		}
		mViewCity.setViewAdapter(new ArrayWheelAdapter<String>(getActivity(),
				cities));
		mViewCity.setCurrentItem(0);
		updateAreas();
	}

	// 以下是选地区用的。

	/**
	 * 所有省
	 */
	protected String[] mProvinceDatas;
	/**
	 * key - 省 value - 市
	 */
	protected Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();
	/**
	 * key - 市 values - 区
	 */
	protected Map<String, String[]> mDistrictDatasMap = new HashMap<String, String[]>();

	/**
	 * key - 区 values - 邮编
	 */
	protected Map<String, String> mZipcodeDatasMap = new HashMap<String, String>();

	/**
	 * 当前省的名称
	 */
	protected String mCurrentProviceName;
	/**
	 * 当前市的名称
	 */
	protected String mCurrentCityName;
	/**
	 * 当前区的名称
	 */
	protected String mCurrentDistrictName = "";

	/**
	 * 当前区的邮政编码
	 */
	protected String mCurrentZipCode = "";

	/**
	 * 解析省市区的XML数据
	 */

	private void setUpData() {
		initProvinceDatas();
		mViewProvince.setViewAdapter(new ArrayWheelAdapter<String>(
				getActivity(), mProvinceDatas));
		// 设置可见条目数量
		mViewProvince.setVisibleItems(7);
		mViewCity.setVisibleItems(7);
		mViewDistrict.setVisibleItems(7);
		updateCities();
		updateAreas();
	}

	protected void initProvinceDatas() {
		List<ProvinceModel> provinceList = null;
		AssetManager asset = getActivity().getAssets();
		try {
			InputStream input = asset.open("province_data.xml");
			// 创建一个解析xml的工厂对象
			SAXParserFactory spf = SAXParserFactory.newInstance();
			// 解析xml
			SAXParser parser = spf.newSAXParser();
			XmlParserHandler handler = new XmlParserHandler();
			parser.parse(input, handler);
			input.close();
			// 获取解析出来的数据
			provinceList = handler.getDataList();
			// */ 初始化默认选中的省、市、区
			if (provinceList != null && !provinceList.isEmpty()) {
				mCurrentProviceName = provinceList.get(0).getName();
				List<CityModel> cityList = provinceList.get(0).getCityList();
				if (cityList != null && !cityList.isEmpty()) {
					mCurrentCityName = cityList.get(0).getName();
					List<DistrictModel> districtList = cityList.get(0)
							.getDistrictList();
					mCurrentDistrictName = districtList.get(0).getName();
					mCurrentZipCode = districtList.get(0).getZipcode();
				}
			}
			// */
			mProvinceDatas = new String[provinceList.size()];
			for (int i = 0; i < provinceList.size(); i++) {
				// 遍历所有省的数据
				mProvinceDatas[i] = provinceList.get(i).getName();
				List<CityModel> cityList = provinceList.get(i).getCityList();
				String[] cityNames = new String[cityList.size()];
				for (int j = 0; j < cityList.size(); j++) {
					// 遍历省下面的所有市的数据
					cityNames[j] = cityList.get(j).getName();
					List<DistrictModel> districtList = cityList.get(j)
							.getDistrictList();
					String[] distrinctNameArray = new String[districtList
							.size()];
					DistrictModel[] distrinctArray = new DistrictModel[districtList
							.size()];
					for (int k = 0; k < districtList.size(); k++) {
						// 遍历市下面所有区/县的数据
						DistrictModel districtModel = new DistrictModel(
								districtList.get(k).getName(), districtList
										.get(k).getZipcode());
						// 区/县对于的邮编，保存到mZipcodeDatasMap
						mZipcodeDatasMap.put(districtList.get(k).getName(),
								districtList.get(k).getZipcode());
						distrinctArray[k] = districtModel;
						distrinctNameArray[k] = districtModel.getName();
					}
					// 市-区/县的数据，保存到mDistrictDatasMap
					mDistrictDatasMap.put(cityNames[j], distrinctNameArray);
				}
				// 省-市的数据，保存到mCitisDatasMap
				mCitisDatasMap.put(provinceList.get(i).getName(), cityNames);
			}
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {

		}
	}

	private void showSelectedResult() {
		String adr = mCurrentCityName + " " + mCurrentDistrictName;
		Toast.makeText(
				getActivity(),
				"当前选中:" + mCurrentProviceName + "," + mCurrentCityName + ","
						+ mCurrentDistrictName + "," + mCurrentZipCode,
				Toast.LENGTH_SHORT).show();
		tvAdr.setText(adr);
		Const.loginUser.getvCard().setField("adr", adr);
		XmppUtil.getInstance().changeVcard(Const.loginUser.getvCard());
		changeAdrLayout.setVisibility(View.GONE);
		Const.loginUser = new User(XmppUtil.getUserInfo(null));// 加载用户数据
	}

	/**
	 * @param name
	 *            //显示修改XXX
	 * @param field
	 *            //修改的字段名
	 * @param fieldView
	 *            //修改的view
	 */
	public void showChangelayout(String name, String field, TextView fieldView) {
		changeLayout.setVisibility(View.VISIBLE);
		if (field.equals("sex")) {
			sexGroup.setVisibility(View.VISIBLE);
			changeText.setVisibility(View.GONE);
		} else {
			sexGroup.setVisibility(View.GONE);
			changeText.setVisibility(View.VISIBLE);
		}
		changeNameView.setText(name);
		this.field = field;
		this.changeView = fieldView;
		scrollView.fullScroll(ScrollView.FOCUS_UP);
	}

	public void changeHead(final String imgPath) {
		new XmppLoadThread(getActivity()) {

			@Override
			protected void result(Object object) {
				if (object != null) {
					Bitmap bitmap = (Bitmap) object;
					civHeadImg.setImageBitmap((Bitmap) object);
					ImageCache.getInstance().put(Const.USER_ACCOUNT, bitmap);
				}
			}

			@Override
			protected Object load() {
				return XmppUtil.getInstance().changeImage(new File(imgPath));
			}
		};
	}
}
