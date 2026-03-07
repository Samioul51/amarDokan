package com.amarDokan.amarDokan.service.implementations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.amarDokan.amarDokan.service.CommonService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Service
public class CommonServiceImpl implements CommonService {

    @Value("${taka.sign:৳}")
    public String takaSign;

    @Override
    public void removeSessionMessage() {
        HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.getRequestAttributes()))
                .getRequest();
        HttpSession session = request.getSession();
        session.removeAttribute("succMsg");
        session.removeAttribute("errorMsg");
    }

    @Override
    public String takaSign() {
        return takaSign;
    }

}
