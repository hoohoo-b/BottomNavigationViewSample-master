package bottomnav.thesevchefs.com.cooktasty.utilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.SyncStateContract;
import android.util.Base64;

import org.jcodec.api.SequenceEncoder;
import org.jcodec.api.android.AndroidSequenceEncoder;
import org.jcodec.common.io.NIOUtils;
import org.jcodec.common.io.SeekableByteChannel;
import org.jcodec.common.model.Rational;
import org.jcodec.containers.mp4.MP4Packet;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by user on 4/11/2017.
 */

public class ImageToVideoConverter {

    public static File setvideo(Context context, String imageUrl) throws IOException {
        SeekableByteChannel out = null;
        File file = null;

        try {
            Bitmap bitmap = getBitmapFromURL(imageUrl);
            file = new File(context.getFilesDir(), "video.mp4");
            out = NIOUtils.writableFileChannel(String.valueOf(file));

            // for Android use: AndroidSequenceEncoder
            AndroidSequenceEncoder encoder = new AndroidSequenceEncoder(out, Rational.R(30000, 1001));
            // Encode the image
            encoder.encodeImage(bitmap);
            // Finalize the encoding, i.e. clear the buffers, write the header, etc.
            encoder.finish();
        } finally {
            NIOUtils.closeQuietly(out);
            return file;
        }
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }



}