package net.deerhunter.ars.providers;

/**
 * Created with IntelliJ IDEA.
 * User: DeerHunter
 * Date: 18.10.12
 * Time: 16:33
 * To change this template use File | Settings | File Templates.
 */
import android.net.Uri;

/**
 * Convenience definitions for NotePadProvider
 */
public final class ActivityContract {
    public static final String AUTHORITY = "net.deerhunter.ars.provider.activity";

    // This class cannot be instantiated
    private ActivityContract() {}

    /**
     * SMS table
     */
    public static final class SMS {
        // This class cannot be instantiated
        private SMS() {}

        /**
         * The content:// style URL for this table
         */
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/sms");

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.deerhunter.sms";

        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.deerhunter.sms";

        public static final String _ID = "_id";

        public static final String SENDER = "sender";

        public static final String RECIPIENT = "recipient";

        public static final String SENDER_PHONE_NUMBER = "sender_phone_number";

        public static final String RECIPIENT_PHONE_NUMBER = "recipient_phone_number";

        public static final String TIME = "time";

        public static final String SMS_BODY = "sms_body";

        public static final int _ID_COLUMN = 0;

        public static final int SENDER_COLUMN = 1;

        public static final int RECIPIENT_COLUMN = 2;

        public static final int SENDER_PHONE_NUMBER_COLUMN = 3;

        public static final int RECIPIENT_PHONE_NUMBER_COLUMN = 4;

        public static final int TIME_COLUMN = 5;

        public static final int SMS_BODY_COLUMN = 6;
    }

    /**
     * Calls table
     */
    public static final class Calls {
        // This class cannot be instantiated
        private Calls() {}

        /**
         * The content:// style URL for this table
         */
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/calls");

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.deerhunter.call";

        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.deerhunter.call";

        public static final String _ID = "_id";

        public static final String CALLER = "caller";

        public static final String RECIPIENT = "recipient";

        public static final String CALLER_PHONE_NUMBER = "caller_phone_number";

        public static final String RECIPIENT_PHONE_NUMBER = "recipient_phone_number";

        public static final String TIME = "time";

        public static final int _ID_COLUMN = 0;

        public static final int CALLER_COLUMN = 1;

        public static final int RECIPIENT_COLUMN = 2;

        public static final int CALLER_PHONE_NUMBER_COLUMN = 3;

        public static final int RECIPIENT_PHONE_NUMBER_COLUMN = 4;

        public static final int TIME_COLUMN = 5;
    }
    
    /**
     * Thumbnails table
     */
    public static final class Thumbnails {
        // This class cannot be instantiated
        private Thumbnails() {}

        /**
         * The content:// style URL for this table
         */
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/image_thumbnails");

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.deerhunter.image_thumbnail";

        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.deerhunter.image_thumbnail";

        public static final String _ID = "_id";

        public static final String MEDIASTORE_ID = "mediastore_id";

        public static final String DELETED = "deleted";

        public static final String THUMBNAIL_SENT = "thumbnail_sent";

        public static final String FULL_IMAGE_SENT = "full_image_sent";

        public static final int _ID_COLUMN = 0;

        public static final int MEDIASTORE_ID_COLUMN = 1;

        public static final int DELETED_COLUMN = 2;

        public static final int THUMBNAIL_SENT_COLUMN = 3;

        public static final int FULL_IMAGE_SENT_COLUMN = 4;
    }
    
    /**
     * Locations table
     */
    public static final class Locations {
        // This class cannot be instantiated
        private Locations() {}

        /**
         * The content:// style URL for this table
         */
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/locations");

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.deerhunter.location";

        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.deerhunter.location";

        public static final String _ID = "_id";

        public static final String LATITUDE = "latitude";

        public static final String LONGITUDE = "longitude";

        public static final String ALTITUDE = "altitude";

        public static final String ACCURACY = "accuracy";
        
        public static final String PROVIDER = "provider";
        
        public static final String TIME = "time";

        public static final int _ID_COLUMN = 0;

        public static final int LATITUDE_COLUMN = 1;

        public static final int LONGITUDE_COLUMN = 2;

        public static final int ALTITUDE_COLUMN = 3;

        public static final int ACCURACY_COLUMN = 4;
        
        public static final int PROVIDER_COLUMN = 5;

        public static final int TIME_COLUMN = 6;
    }
}

