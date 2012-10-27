/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: D:\\My Documents\\GitHub\\EclipseGit\\EclipseGitRepo\\mobilesafe\\src\\cn\\itcast\\lockservice\\IService.aidl
 */
package cn.itcast.lockservice;
public interface IService extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements cn.itcast.lockservice.IService
{
private static final java.lang.String DESCRIPTOR = "cn.itcast.lockservice.IService";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an cn.itcast.lockservice.IService interface,
 * generating a proxy if needed.
 */
public static cn.itcast.lockservice.IService asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = (android.os.IInterface)obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof cn.itcast.lockservice.IService))) {
return ((cn.itcast.lockservice.IService)iin);
}
return new cn.itcast.lockservice.IService.Stub.Proxy(obj);
}
public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_callStartProtectApp:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.callStartProtectApp(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_callStopProtectApp:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.callStopProtectApp(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_callstopprotect:
{
data.enforceInterface(DESCRIPTOR);
this.callstopprotect();
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements cn.itcast.lockservice.IService
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
public void callStartProtectApp(java.lang.String packname) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(packname);
mRemote.transact(Stub.TRANSACTION_callStartProtectApp, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
public void callStopProtectApp(java.lang.String packname) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(packname);
mRemote.transact(Stub.TRANSACTION_callStopProtectApp, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
public void callstopprotect() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_callstopprotect, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_callStartProtectApp = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_callStopProtectApp = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_callstopprotect = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
}
public void callStartProtectApp(java.lang.String packname) throws android.os.RemoteException;
public void callStopProtectApp(java.lang.String packname) throws android.os.RemoteException;
public void callstopprotect() throws android.os.RemoteException;
}
