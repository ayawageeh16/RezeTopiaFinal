package io.krito.com.rezetopia.models.pojo.Group;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("status")
    @Expose
    private String status;
//    @SerializedName("data")
//    @Expose
//    //private List<List<Datum>> data = null;

    public String getStatus() {
        return status;
    }

//    public void setStatus(String status) {
//        this.status = status;
//    }
//
//    //public List<List<Datum>> getData() {
//        return data;
//    }
//
//   // public void setData(List<List<Datum>> data) {
//        this.data = data;
//    }

}
