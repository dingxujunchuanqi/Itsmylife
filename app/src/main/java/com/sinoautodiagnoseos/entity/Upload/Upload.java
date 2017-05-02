package com.sinoautodiagnoseos.entity.Upload;

/**
 * Created by Lanye on 2017/2/20.
 */

public class Upload {
    public UploadData data;

    public void setData(UploadData data) {
        this.data = data;
    }

    public UploadData getData() {

        return data;
    }

    public class UploadData {
        public String id;
        public String description;
        public String downloadUrl;
        public String extension;
        public String filename;

        public String getId() {
            return id;
        }

        public String getDescription() {
            return description;
        }

        public String getDownloadUrl() {
            return downloadUrl;
        }

        public String getExtension() {
            return extension;
        }

        public String getFilename() {
            return filename;
        }
    }
}
