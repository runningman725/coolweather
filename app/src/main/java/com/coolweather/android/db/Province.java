package com.coolweather.android.db;

import org.litepal.crud.DataSupport;

/**
 * Created by Admin on 2017/8/1.
 */

public class Province extends DataSupport{
    private int id;
    private String provinceName;
    private int provinceCode;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getProvinceName() {
            return provinceName;
        }

        public void setProvinceName(String provinceName) {
            this.provinceName = provinceName;
        }

        public int getProvinceId() {
            return provinceCode;
        }

        public void setProvinceId(int provinceId) {
            this.provinceCode = provinceId;
        }
}
