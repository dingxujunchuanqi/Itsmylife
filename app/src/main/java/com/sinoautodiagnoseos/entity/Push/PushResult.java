package com.sinoautodiagnoseos.entity.Push;

/**
 * Created by Lanye on 2017/3/6.
 */

public class PushResult {
    public String Content;

    public void setContent(String content) {
        Content = content;
    }

    public String getContent() {

        return Content;
    }

    public Extension Extension;

    public PushResult.Extension getExtension() {
        return Extension;
    }

    public void setExtension(PushResult.Extension extension) {
        Extension = extension;
    }

    public class Extension {
        public String Type;

        public void setType(String type) {
            Type = type;
        }

        public String getType() {

            return Type;
        }

        public Dataa Data;

        public Dataa getData() {
            return Data;
        }

        public void setData(Dataa data) {
            Data = data;
        }

        public class Dataa {
            //发起询问或邀请的账号
            public String SAccount;
            //发起询问或邀请的姓名
            public String SName;
            //会议的房间ID
            public String RoomID;
            //专家的账号
            public String PAccount;
            //专家的姓名
            public String PName;
            //文件名称
            public String FileTitle;
            //文件大小
            public String FileSize;
            //文件类型
            public String FileType;
            //文件下载路径
            public String FileUrl;
            //专家ID
            public String PId;
            //消息内容
            public String Message;
            //图片地址
            public String ImageUrl;
            //更新地址
            public String UpdateUrl;

            public void setSAccount(String SAccount) {
                this.SAccount = SAccount;
            }

            public void setSName(String SName) {
                this.SName = SName;
            }

            public void setRoomID(String roomID) {
                RoomID = roomID;
            }

            public String getSAccount() {

                return SAccount;
            }

            public String getSName() {
                return SName;
            }

            public String getRoomID() {
                return RoomID;
            }

            public void setPAccount(String PAccount) {
                this.PAccount = PAccount;
            }

            public void setPName(String PName) {
                this.PName = PName;
            }

            public void setFileTitle(String fileTitle) {
                FileTitle = fileTitle;
            }

            public void setFileSize(String fileSize) {
                FileSize = fileSize;
            }

            public void setFileType(String fileType) {
                FileType = fileType;
            }

            public void setFileUrl(String fileUrl) {
                FileUrl = fileUrl;
            }

            public void setPId(String PId) {
                this.PId = PId;
            }

            public void setMessage(String message) {
                Message = message;
            }

            public void setImageUrl(String imageUrl) {
                ImageUrl = imageUrl;
            }

            public void setUpdateUrl(String updateUrl) {
                UpdateUrl = updateUrl;
            }

            public String getPAccount() {

                return PAccount;
            }

            public String getPName() {
                return PName;
            }

            public String getFileTitle() {
                return FileTitle;
            }

            public String getFileSize() {
                return FileSize;
            }

            public String getFileType() {
                return FileType;
            }

            public String getFileUrl() {
                return FileUrl;
            }

            public String getPId() {
                return PId;
            }

            public String getMessage() {
                return Message;
            }

            public String getImageUrl() {
                return ImageUrl;
            }

            public String getUpdateUrl() {
                return UpdateUrl;
            }
        }
    }
}
