package buildnlive.com.buildlive;

public class Interfaces {

    public interface  NetworkInterfaceListener{
       void onNetworkRequestStart();
       void onNetworkRequestError(String error);
       void onNetworkRequestComplete(String response);
    }

    public interface SyncListener{
        void onSyncStart();
        void onSyncError(String error);
        void onSync(Object object);
        void onSyncFinish();
    }
}
