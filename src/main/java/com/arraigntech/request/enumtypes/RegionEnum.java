package com.arraigntech.request.enumtypes;

import java.util.Arrays;
import java.util.List;

/**
 * 
 * @author tulabandula.kumar
 *
 */
public enum RegionEnum {

    INDIA("+91"), US("+1");

    private final String code;

    RegionEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
    
    public static String[] getRegionCodes(){
    	List<RegionEnum> regions = Arrays.asList(values());
    	return regions.stream().map(region -> region.getCode()).toArray(String[] :: new);
    }
    
    public static RegionEnum getEnum(String code){
    	List<RegionEnum> regions = Arrays.asList(values());
    	return regions.stream().filter(region -> region.getCode().equalsIgnoreCase(code)).findFirst().get();
    }
    


}
