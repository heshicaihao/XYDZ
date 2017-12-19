package com.ddiiyy.xydz.widget.wheelview.utils;

import java.util.ArrayList;
import java.util.List;


import org.json.JSONArray;
import org.json.JSONObject;

import com.ddiiyy.xydz.widget.wheelview.bean.CityModel;
import com.ddiiyy.xydz.widget.wheelview.bean.DistrictModel;
import com.ddiiyy.xydz.widget.wheelview.bean.ProvinceModel;

public class JSONUtil {

	/**
	 * ����ȫ��ʡ����
	 * 
	 * @param provinceList
	 * @return
	 * @throws Exception
	 */
	public static  List<ProvinceModel> getJson2Data(JSONArray provinceList)
			throws Exception {
		List<ProvinceModel> mProvinceList = new ArrayList<ProvinceModel>();

		for (int i = 0; i < provinceList.length(); i++) {
			JSONObject province = provinceList.getJSONObject(i);
			String provinceName = province.optString("name");
			JSONArray cityList = province.optJSONArray("children");

			ProvinceModel provinceModel = new ProvinceModel();
			provinceModel.setName(provinceName);

			List<CityModel> mCityList = new ArrayList<CityModel>();
			for (int j = 0; j < cityList.length(); j++) {
				JSONObject city = cityList.getJSONObject(j);
				String cityName = city.optString("name");
				JSONArray districtList = city.optJSONArray("children");

				CityModel cityModel = new CityModel();
				cityModel.setName(cityName);

				List<DistrictModel> mDistrictList = new ArrayList<DistrictModel>();
				for (int k = 0; k < districtList.length(); k++) {
					JSONObject district = districtList.getJSONObject(k);
					String districtName = district.optString("name");
					String districtCode = district.optString("code");

					DistrictModel districtModel = new DistrictModel();

					districtModel.setName(districtName);
					districtModel.setZipcode(districtCode);

					mDistrictList.add(districtModel);
				}
				cityModel.setDistrictList(mDistrictList);
				mCityList.add(cityModel);
			}
			provinceModel.setCityList(mCityList);
			mProvinceList.add(provinceModel);
		}

		return mProvinceList;

	}
}
