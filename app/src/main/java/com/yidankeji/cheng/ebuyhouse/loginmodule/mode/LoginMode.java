package com.yidankeji.cheng.ebuyhouse.loginmodule.mode;

import java.io.Serializable;

/**
 * Created by Administrator on 2017\12\26 0026.
 */

public class LoginMode implements Serializable{

    private String zhanghao;
    private String yanzhengma;
    private String mima;
    private String accountType;//email 还是 phone
    private boolean isReg;
    private String mode;//短信模块标示，reg 注册 reset 重置密码 edit编辑房屋
    private boolean isForgetPW ;

    public boolean isForgetPW() {
        return isForgetPW;
    }

    public void setForgetPW(boolean forgetPW) {
        isForgetPW = forgetPW;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public boolean isReg() {
        return isReg;
    }

    public void setReg(boolean reg) {
        isReg = reg;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getZhanghao() {
        return zhanghao;
    }

    public void setZhanghao(String zhanghao) {
        this.zhanghao = zhanghao;
    }

    public String getYanzhengma() {
        return yanzhengma;
    }

    public void setYanzhengma(String yanzhengma) {
        this.yanzhengma = yanzhengma;
    }

    public String getMima() {
        return mima;
    }

    public void setMima(String mima) {
        this.mima = mima;
    }
}
