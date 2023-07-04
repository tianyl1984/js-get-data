package com.tianyl.demo.service;

import com.tianyl.demo.vo.UserVo;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class UserService {

    public List<UserVo> getUsers() {
        return Arrays.asList(new UserVo("张三", 20), new UserVo("lisi", 35));
    }

}
