package cn.itcast.lockservice;
interface IService{
    void callStartProtectApp(String packname);
    void callStopProtectApp(String packname);
	void callstopprotect();
}