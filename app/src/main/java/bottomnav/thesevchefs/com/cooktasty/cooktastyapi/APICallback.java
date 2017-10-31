package bottomnav.thesevchefs.com.cooktasty.cooktastyapi;

/**
 * Created by Jun Jie on 31/10/2017.
 */

public interface APICallback {
    void onSuccess(Object result);

    void onError(Object result);
}