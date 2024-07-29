package com.chrissy.core.region;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * @author chrissy
 * @description ip所属信息
 * @date 2024/7/29 14:54
 */
@Data
public class IpRegionInfo {
    private String country;
    private String region;
    private String province;
    private String city;
    private String isp;

    /**
     * 根据ip信息字符串解析ip所属信息<p>
     * @param info 国家，区域，行省，城市，服务提供商isp。五种信息用{@code |}隔开
     */
    public IpRegionInfo(String info) {
        String[] cells = StringUtils.split(info, "|");
        if (cells.length < 5) {
            country = "";
            region = "";
            province = "";
            city = "";
            isp = "";
            return;
        }
        country = "0".equals(cells[0]) ? "" : cells[0];
        region = "0".equals(cells[1]) ? "" : cells[1];
        province = "0".equals(cells[2]) ? "" : cells[2];
        city = "0".equals(cells[3]) ? "" : cells[3];
        isp = "0".equals(cells[4]) ? "" : cells[4];
    }

    /**
     * 将IpRegionInfo转化为区域信息 <p>
     * 若在国内，大陆，则返回 省-城市 <p>
     * 若在国内，非大陆，则返回 国家-行政区 <p>
     * 若在国外，      则返回 国家-省份 <p>
     * @return 区域信息字符串
     */
    public String toRegionStr() {
        if (Objects.equals(country, "中国")) {
            // 大陆，返回省 + 城市
            if (StringUtils.isNotBlank(province) && StringUtils.isNotBlank(city)) {
                return province + "·" + city;
            } else if (StringUtils.isNotBlank(province)) {
                return province;
            } else {
                return country;
            }
        } else {
            if (StringUtils.isNotBlank(province)) {
                // 非大陆，返回国家+省份
                return country + "·" + province;
            }
            return country;
        }
    }
}