package com.skcc.cloudz.zcp.portal.iam.user.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skcc.cloudz.zcp.api.iam.domain.vo.ApiResponseVo;
import com.skcc.cloudz.zcp.api.iam.domain.vo.ZcpUserListVo;
import com.skcc.cloudz.zcp.api.iam.domain.vo.ZcpUserResVo;
import com.skcc.cloudz.zcp.api.iam.domain.vo.ZcpUserVo;
import com.skcc.cloudz.zcp.api.iam.service.IamApiService;
import com.skcc.cloudz.zcp.common.constants.ApiResult;
import com.skcc.cloudz.zcp.portal.iam.user.domain.dto.UserDto;

@Service
public class UserService {
    
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    
    @Autowired
    private IamApiService iamApiService;

    public List<ZcpUserVo> getUsers() throws Exception {
        ZcpUserListVo zcpUserListVo = iamApiService.users();
        if (!zcpUserListVo.getCode().equals(ApiResult.SUCCESS.getCode())) {
            throw new Exception(zcpUserListVo.getMsg());
        }
        
        return zcpUserListVo.getData().getItems();
    }
    
    public ZcpUserVo getUser(String id) throws Exception {
        ZcpUserResVo zcpUserResVo = iamApiService.getUser(id);
        if (!zcpUserResVo.getCode().equals(ApiResult.SUCCESS.getCode())) {
            throw new Exception(zcpUserResVo.getMsg());
        }
        
        return zcpUserResVo.getData();
    }
    
    public void setUser(UserDto userDto) throws Exception {
        HashMap<String, Object> reqMap = new HashMap<String, Object>();
        reqMap.put("email", userDto.getEmail());
        reqMap.put("firstName", userDto.getFirstName());
        reqMap.put("username", userDto.getUsername());
        reqMap.put("clusterRole", userDto.getClusterRole());
        
        ApiResponseVo apiResponseVo = iamApiService.setUser(reqMap);
        if (!apiResponseVo.getCode().equals(ApiResult.SUCCESS.getCode())) {
            throw new Exception(apiResponseVo.getMsg());
        }
    }
    
    public void updateUser(UserDto userDto) throws Exception {
        HashMap<String, Object> reqMap = new HashMap<String, Object>();
        reqMap.put("email", userDto.getEmail());
        reqMap.put("firstName", userDto.getFirstName());
        reqMap.put("enabled", userDto.getEnabled());
        reqMap.put("username", userDto.getUsername());
        
        ApiResponseVo apiResponseVo = iamApiService.updateUser(userDto.getId(), reqMap);
        if (!apiResponseVo.getCode().equals(ApiResult.SUCCESS.getCode())) {
            throw new Exception(apiResponseVo.getMsg());
        }
    }
    
    public void deleteUser(String id) throws Exception {
        ApiResponseVo apiResponseVo = iamApiService.deleteUser(id);
        if (!apiResponseVo.getCode().equals(ApiResult.SUCCESS.getCode())) {
            throw new Exception(apiResponseVo.getMsg());
        }
    }
    
    public void resetPassword(UserDto userDto) throws Exception {
        HashMap<String, Object> reqMap = new HashMap<String, Object>();
        reqMap.put("password", userDto.getPassword());
        reqMap.put("temporary", userDto.getTemporary());
        
        ApiResponseVo apiResponseVo = iamApiService.resetPassword(userDto.getId(), reqMap);
        if (!apiResponseVo.getCode().equals(ApiResult.SUCCESS.getCode())) {
            throw new Exception(apiResponseVo.getMsg());
        }
    }
    
    public void resetCredentials(UserDto userDto) throws Exception  {
        HashMap<String, Object> reqMap = new HashMap<String, Object>();
        reqMap.put("period", userDto.getPeriod());
        reqMap.put("type", userDto.getType());
        reqMap.put("actions", userDto.getActions());
        
        ApiResponseVo apiResponseVo = iamApiService.resetCredentials(userDto.getId(), reqMap);
        if (!apiResponseVo.getCode().equals(ApiResult.SUCCESS.getCode())) {
            throw new Exception(apiResponseVo.getMsg());
        }
    }
    
    @SuppressWarnings("unchecked")
    public List<String> getClusterRoles() throws Exception {
        List<String> clusterRoles = new ArrayList<String>();
        
        ApiResponseVo apiResponseVo = iamApiService.getClusterRoles();
        if (!apiResponseVo.getCode().equals(ApiResult.SUCCESS.getCode())) {
            throw new Exception(apiResponseVo.getMsg());
        }
        
        Map<String, Object> data = apiResponseVo.getData();
        List<HashMap<String, Object>> items = (List<HashMap<String, Object>>) data.get("items");
        
        for (HashMap<String, Object> item : items) {
            clusterRoles.add(((HashMap<String, Object>) item.get("metadata")).get("name").toString());
        }
        
        return clusterRoles;
    }

}
